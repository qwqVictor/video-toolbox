package videobox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FFBridge {
    static final String RunOS = System.getProperty("os.name").split(" ")[0];
    Runtime runtime;
    String FFmpegPath;
    String FFprobePath;

    private static String getFileExtension(String filePath) {
        String[] tokens = Paths.get(filePath).getFileName().toString().split("\\.");
        return tokens[tokens.length - 1];
    }

    private static String getFileNameWithExtension(String filePath) {
        String[] tokens = Paths.get(filePath).getFileName().toString().split("\\.");
        return String.join(".", tokens);
    }

    private static String getFileNameWithoutExtension(String filePath) {
        String[] tokens = Paths.get(filePath).getFileName().toString().split("\\.");
        tokens = Arrays.copyOfRange(tokens, 0, tokens.length > 1 ? tokens.length - 1 : tokens.length);
        return String.join(".", tokens);
    }

    private static String getOutputFilePath(String outputPath, String fileName, String fileExtension) {
        if (fileExtension != null) {
            fileName = getFileNameWithoutExtension(fileName) + "." + fileExtension;
        }
        return String.join(File.separator, outputPath, fileName);
    }

    // 构造函数
    // 负责创建 FFBridge 实例，并初始化 ffmpeg 库调用路径
    public FFBridge() {
        this.runtime = Runtime.getRuntime();
        /* 若操作系统为 Windows，调用包内预装的 ffmpeg 库 */
        if (RunOS.equals("Windows")) {
            try {
                this.FFmpegPath = Paths.get(getClass().getResource("ffmpeg.exe").toURI()).toString();
            } catch (URISyntaxException e) {
                throw new FFRuntimeException("Unable to find ffmpeg" + e.getMessage());
            }
            try {
                this.FFprobePath = Paths.get(getClass().getResource("ffprobe.exe").toURI()).toString();
            } catch (URISyntaxException e) {
                throw new FFRuntimeException("Unable to find ffprobe" + e.getMessage());
            }
        } else {
            /* 若操作系统不为 Windows，调用系统环境中的 ffmpeg */
            this.FFmpegPath = "ffmpeg";
            this.FFprobePath = "ffprobe";
        }
    }

    /**
     * 探查文件属性信息，例如编码、宽高、时长和码率等
     * 
     * @param inputFile 输入文件
     * @return 探查的文件属性
     * @throws FFRuntimeException
     */
    private Map<String, Object> probe(String inputFile) throws FFRuntimeException {
        Map<String, Object> ret = new HashMap<String, Object>();
        // 这里是通过 Java 内置的进程工具类调用 ffprobe 进行探查。
        ProcessBuilder pBuilder = new ProcessBuilder();
        List<String> cmdArray = new ArrayList<String>();
        cmdArray.add(FFprobePath);
        /* ffprobe 探查的命令行参数，按分离展开加入参数列表 */
        for (String arg : "-v error -show_entries stream=codec_type,codec_name,width,height,duration,bit_rate -of \"csv=s=,:p=0\""
                .split(" ")) {
            cmdArray.add(arg);
        }
        cmdArray.add(inputFile);
        pBuilder.command(cmdArray); // 设置进程启动参数
        try {
            Process p = pBuilder.start(); // 启动 ffmpeg 进程
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            do {
                /* 读取输出 */
                line = reader.readLine();
            } while (line == null && (p.isAlive() || p.exitValue() == 0));
            if (!p.isAlive() && p.exitValue() != 0) { // 若返回值不为 0，则说明出现了异常，进行抛出
                throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
            }
            /* 下面为输出信息处理 */
            String[] tokens = line.split(",");
            ret.put("codec_type", tokens[0]);
            ret.put("codec_name", tokens[1]);
            if (tokens.length == 6) {
                /* 若有 6 个项目，则为视频，因为音频不存在宽高所以没有 width 和 height 项目 */
                ret.put("width", Integer.parseInt(tokens[2]));
                ret.put("height", Integer.parseInt(tokens[3]));
                ret.put("duration", Float.parseFloat(tokens[4]));
                ret.put("bit_rate", Integer.parseInt(tokens[5]) / 1024); // 码率默认为 bps，除以 1024 变为 kbps
            } else {
                ret.put("duration", Float.parseFloat(tokens[2]));
                ret.put("bit_rate", Integer.parseInt(tokens[3]) / 1024);
            }

            return ret;
        } catch (IOException e) {
            throw new FFRuntimeException(e.getMessage());
        }

    }

    /**
     * 转码功能模块的后端实现。通过对接 ffmpeg 进行格式转换等操作。
     * 
     * @param inputFiles    批量进行转换的文件列表
     * @param outputPath    批量输出的目录路径
     * @param outputFormat  输出格式
     * @param outputCodec   输出编码，若为 null 则维持默认
     * @param outputWidth   输出视频宽度，若和 outputHeight 同时不为 null 则按需缩放视频
     * @param outputHeight  输出视频高度，若和 outputWidth 同时不为 null 则按需缩放视频
     * @param outputBitRate 输出视频码率，若为 null 则不做特别的压制处理
     * @throws FFRuntimeException
     */
    public void transform(List<String> inputFiles, String outputPath, String outputFormat, String outputCodec,
            Integer outputWidth,
            Integer outputHeight, Integer outputBitRate) throws FFRuntimeException {
        /* 遍历输入文件列表 */
        for (String file : inputFiles) {
            ProcessBuilder pBuilder = new ProcessBuilder();
            List<String> cmdArray = new ArrayList<String>();
            /* 首先对文件进行探查，获取文件基本信息 */
            Map<String, Object> probeResult = probe(file);
            cmdArray.add(FFmpegPath);
            cmdArray.add("-y"); // 覆盖文件
            cmdArray.add("-nostdin"); // 要求非交互式体验，避免 stdin 阻塞
            cmdArray.add("-i");
            cmdArray.add(file);
            cmdArray.add("-vcodec");
            if ((outputCodec == null || probeResult.get("codec_type").equals(outputCodec)) && outputWidth == null
                    && outputHeight == null && outputBitRate == null) // 若无需进行编码变更则直接拷贝视频流，但要确保没有缩放和码率要求否则必须重新压制
                cmdArray.add("copy");
            else
                cmdArray.add(outputCodec == null ? (String) probeResult.get("codec_type") : outputCodec); // 在重新压制情况下要指定正确的编码
            if (outputBitRate != null && outputBitRate < (Integer) probeResult.get("bit_rate")) {
                cmdArray.add("-b:v");
                cmdArray.add(String.format("%dk", outputBitRate));
            }
            if (outputWidth != null && outputHeight != null) { // 若有缩放需求则将缩放指令传入命令行
                cmdArray.add("-s");
                cmdArray.add(String.format("%dx%d", outputWidth, outputHeight));
            }
            cmdArray.add(getOutputFilePath(outputPath, file, outputFormat)); // 通过 getOutputFilePath
                                                                             // 方法（该方法实现极为简单无需赘述）计算出输出路径
            System.out.println(String.join(" ", cmdArray));
            pBuilder.redirectErrorStream(true); // 进行错误流重定向避免缓冲区阻塞
            pBuilder.command(cmdArray); // 设置进程启动参数
            try {
                Process p = pBuilder.start(); // 启动 ffmpeg 进程
                BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = stdoutReader.readLine()) != null) {
                    System.out.println("Stdout: " + line);
                }
                p.waitFor(); // 等待进程完成
                if (!p.isAlive() && p.exitValue() != 0) { // 若返回值不为 0，则说明出现了异常，进行抛出
                    throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
                }
            } catch (IOException | InterruptedException e) {
                throw new FFRuntimeException(e.getMessage());
            }
        }
    }

    /**
     * 视频合并功能模块的后端实现。通过 FFmpeg 复合滤镜和 concat 多工解调器实现多种场景的视频合并。
     * 
     * @param inputFiles 输入文件列表
     * @param outputFile 输出文件
     * @param transition 转场效果，若为 null 则无转场
     * @throws FFRuntimeException
     */
    public void cat(List<String> inputFiles, String outputFile, String transition) throws FFRuntimeException {
        if (inputFiles.size() < 2) { // 若传入文件数量少于2个则无需合并，抛出异常
            throw new FFRuntimeException("No need to concentrate one video.");
        }
        ProcessBuilder pBuilder = new ProcessBuilder();
        List<String> cmdArray = new ArrayList<String>();
        cmdArray.add(FFmpegPath);
        cmdArray.add("-y"); // 覆盖文件
        cmdArray.add("-nostdin"); // 要求非交互式体验，避免 stdin 阻塞
        Integer lastWidth = null, lastHeight = null;
        List<Map<String, Object>> probeResults = new ArrayList<Map<String, Object>>();
        String[] videoFilter = new String[inputFiles.size() - 1], audioFilter = new String[inputFiles.size() - 1];
        /* 遍历输入文件列表 */
        for (String file : inputFiles) {
            /* 首先对文件进行探查，获取文件基本信息 */
            Map<String, Object> probeResult = probe(file);
            probeResults.add(probeResult);
            if (lastWidth == null)
                lastWidth = (Integer) probeResult.get("width");
            if (lastHeight == null)
                lastHeight = (Integer) probeResult.get("height");
            if (!(probeResult.get("width").equals(lastWidth))
                    || !(probeResult.get("height").equals(lastHeight))) { // 若视频宽高不等，则抛出异常无法合并
                throw new FFRuntimeException("Width and height must be same");
            }
            if (transition != null) {
                cmdArray.add("-i");
                cmdArray.add(file);
            }
        }
        if (transition != null) { /* 对于有无转场要进行不同的处理方式 */
            /* 有转场的情况下，要使用 FFmpeg 复合滤镜进行处理 */
            final int transitionDuration = 1; // 固定转场时间为 1 秒
            float videoLastOffset = 0; // 记录转场偏移量
            /* 以下循环生成视频转场效果复合滤镜指令 */
            for (int i = 1; i < inputFiles.size(); i++) { // i=1 起步，即每次和上一个视频生成转场
                videoLastOffset += ((Float) probeResults.get(i - 1).get("duration"))
                        .floatValue() - transitionDuration; // 转场偏移量加和，计算公式为：当前转场偏移量=上一个转场偏移量+当前视频长度-转场时间
                videoFilter[i - 1] = String.format("[%s][%d]xfade=transition=%s:duration=%d:offset=%f%s", // 格式化生成滤镜指令，大致格式为：[源1][源2]xfade参数[当前滤镜管线名]
                        i == 1 ? "0" : String.format("vfade%d", i - 1), i, // 对于第一个转场，源1为0，否则为上一个滤镜管线名。源2为当前视频编号i
                        transition, transitionDuration, videoLastOffset, // 传入转场效果，持续时间和偏移量
                        i == inputFiles.size() - 1 ? ",format=yuv420p[video]" : String.format("[vfade%d]", i)); // 若为最后一个滤镜管线，则指定最终输出像素排布格式，否则对当前滤镜管线命名
            }
            /* 以下循环生成音频淡入淡出复合滤镜指令 */
            for (int i = 1; i < inputFiles.size(); i++) {
                audioFilter[i - 1] = String.format("[%s][%d:a]acrossfade=d=%d%s", // 同上，格式化生成
                        i == 1 ? "0:a" : String.format("afade%d", i - 1), i, transitionDuration, // 同上，对于第一个，源1为0，否则为上一个滤镜管线名。
                        i == inputFiles.size() - 1 ? "[audio]" : String.format("[afade%d]", i)); // 同上，若为最后一个滤镜管线，则指定输出格式，否则命名。
            }
            cmdArray.add("-filter_complex"); // 声明使用复合滤镜
            cmdArray.add(String.join(";", videoFilter) + ";" + String.join(";", audioFilter)); // 将以上产生的滤镜指令拼接成参数形式
            cmdArray.add("-map");
            cmdArray.add("[video]");
            cmdArray.add("-map");
            cmdArray.add("[audio]"); // 以上部分进行管线结果的映射
            cmdArray.add("-movflags");
            cmdArray.add("+faststart"); // 视频生成的快速开始参数
        } else {
            /* 无转场的情况下生成文件列表交由 concat 多工解调器进行流合并 */
            File filelist = null;
            try {
                filelist = File.createTempFile("cqupt3g-", ".txt"); // 生成列表临时文件
                filelist.deleteOnExit(); // 声明退出时删除
                FileWriter writer = new FileWriter(filelist);
                for (String file : inputFiles) {
                    writer.write(String.format("file '%s'\n", file.replaceAll("'", "\\'"))); // 写入文件列表，并转义
                }
                writer.close();
            } catch (IOException e) {
                throw new FFRuntimeException("Unable to create temp file: " + e.getMessage());
            }
            cmdArray.add("-f");
            cmdArray.add("concat"); // 声明使用 concat 多工解调器
            cmdArray.add("-safe");
            cmdArray.add("0"); // 取消不必要的安全检查，因为部分视频容器畸形但不影响合并
            cmdArray.add("-i");
            cmdArray.add(filelist.getAbsolutePath());
            cmdArray.add("-codec");
            cmdArray.add("copy");
        }

        cmdArray.add(outputFile);
        System.out.println(String.join(" ", cmdArray));
        pBuilder.redirectErrorStream(true); // 进行错误流重定向避免缓冲区阻塞
        pBuilder.command(cmdArray); // 设置进程启动参数
        try {
            Process p = pBuilder.start(); // 启动 ffmpeg 进程
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println("Stdout: " + line);
            }
            p.waitFor(); // 等待进程完成
            if (!p.isAlive() && p.exitValue() != 0) { // 若返回值不为 0，则说明出现了异常，进行抛出
                throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
            }
        } catch (IOException | InterruptedException e) {
            throw new FFRuntimeException(e.getMessage());
        }
    }

    private void getVideoOnly(String file, String output) {
        ProcessBuilder pBuilder = new ProcessBuilder();
        List<String> cmdArray = new ArrayList<String>();
        cmdArray.add(FFmpegPath);
        cmdArray.add("-y"); // 覆盖文件
        cmdArray.add("-nostdin"); // 要求非交互式体验，避免 stdin 阻塞
        cmdArray.add("-i");
        cmdArray.add(file);
        cmdArray.add("-vcodec");
        cmdArray.add("copy");
        cmdArray.add("-an");
        cmdArray.add(output);
        System.out.println(String.join(" ", cmdArray));
        pBuilder.redirectErrorStream(true); // 进行错误流重定向避免缓冲区阻塞
        pBuilder.command(cmdArray); // 设置进程启动参数
        try {
            Process p = pBuilder.start(); // 启动 ffmpeg 进程
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println("Stdout: " + line);
            }
            p.waitFor(); // 等待进程完成
            if (!p.isAlive() && p.exitValue() != 0) { // 若返回值不为 0，则说明出现了异常，进行抛出
                throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
            }
        } catch (IOException | InterruptedException e) {
            throw new FFRuntimeException(e.getMessage());
        }
    }

    private void getAudioOnly(String file, String output, boolean copy) {
        ProcessBuilder pBuilder = new ProcessBuilder();
        List<String> cmdArray = new ArrayList<String>();
        cmdArray.add(FFmpegPath);
        cmdArray.add("-y"); // 覆盖文件
        cmdArray.add("-nostdin"); // 要求非交互式体验，避免 stdin 阻塞
        cmdArray.add("-i");
        cmdArray.add(file);
        cmdArray.add("-vn");
        if (copy) {
            cmdArray.add("-acodec");
            cmdArray.add("copy");
        }
        cmdArray.add(output);
        System.out.println(String.join(" ", cmdArray));
        pBuilder.redirectErrorStream(true); // 进行错误流重定向避免缓冲区阻塞
        pBuilder.command(cmdArray); // 设置进程启动参数
        try {
            Process p = pBuilder.start(); // 启动 ffmpeg 进程
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println("Stdout: " + line);
            }
            p.waitFor(); // 等待进程完成
            if (!p.isAlive() && p.exitValue() != 0) { // 若返回值不为 0，则说明出现了异常，进行抛出
                throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
            }
        } catch (IOException | InterruptedException e) {
            throw new FFRuntimeException(e.getMessage());
        }
    }

    /**
     * 分离视频画面和音轨
     * 
     * @param inputFiles 批量分离的文件列表
     * @param outputPath 批量输出路径
     * @throws FFRuntimeException
     */
    public void splitAudio(List<String> inputFiles, String outputPath) throws FFRuntimeException {
        for (String file : inputFiles) {
            String ext = getFileExtension(file); // 通过 getFileExtension 获取视频扩展名，实现简洁不必赘述
            getVideoOnly(file, getOutputFilePath(outputPath, getFileNameWithoutExtension(file) + "_videoOnly", ext)); // 分离视频
            String audioExt = "m4a";
            boolean audioCopy = true;
            if (ext.equals("wmv") || ext.equals("ogv")) { // 对于确定非 aac 编码使用 mp3 导出，不拷贝流
                audioExt = "mp3";
                audioCopy = false;
            }
            getAudioOnly(file, getOutputFilePath(outputPath, getFileNameWithoutExtension(file), audioExt), audioCopy); // 分离音频
        }
    }

    /**
     * 整合视频画面和音轨
     * 
     * @param videoInputFiles 待批量整合的视频文件列表
     * @param audioInputFiles 待批量整合的音频文件列表
     * @param outputPath      批量输出路径
     * @throws FFRuntimeException
     */
    public void joinVideoAudio(List<String> videoInputFiles, List<String> audioInputFiles, String outputPath)
            throws FFRuntimeException {
        if (videoInputFiles.size() != audioInputFiles.size()) { // 若待合并的视频和音频数量无法对应则抛出异常
            throw new FFRuntimeException("Video files and audio files mismatch!");
        }
        int length = videoInputFiles.size();
        for (int i = 0; i < length; i++) {
            ProcessBuilder pBuilder = new ProcessBuilder();
            List<String> cmdArray = new ArrayList<String>();
            String videoFile = videoInputFiles.get(i), audioFile = audioInputFiles.get(i); // 结对
            Map<String, Object> videoProbeResult = probe(videoFile), audioProbeResult = probe(audioFile); // 首先探查文件信息
            cmdArray.add(FFmpegPath);
            cmdArray.add("-y"); // 覆盖文件
            cmdArray.add("-nostdin"); // 要求非交互式体验，避免 stdin 阻塞
            cmdArray.add("-i");
            cmdArray.add(videoFile);
            cmdArray.add("-i");
            cmdArray.add(audioFile);
            cmdArray.add("-vcodec");
            cmdArray.add("copy");
            if (((String) audioProbeResult.get("codec_type")).equals("aac")) { // 若音频为 AAC 编码可直接拷贝流
                cmdArray.add("-acodec");
                cmdArray.add("copy");
            }

            cmdArray.add(getOutputFilePath(outputPath, getFileNameWithoutExtension(videoFile) + "_joined",
                    getFileExtension(videoFile))); // 根据原视频文件名加后缀输出
            System.out.println(String.join(" ", cmdArray));
            pBuilder.redirectErrorStream(true); // 进行错误流重定向避免缓冲区阻塞
            pBuilder.command(cmdArray); // 设置进程启动参数
            try {
                Process p = pBuilder.start(); // 启动 ffmpeg 进程
                BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = stdoutReader.readLine()) != null) {
                    System.out.println("Stdout: " + line);
                }
                p.waitFor(); // 等待进程完成
                if (!p.isAlive() && p.exitValue() != 0) { // 若返回值不为 0，则说明出现了异常，进行抛出
                    throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
                }
            } catch (IOException | InterruptedException e) {
                throw new FFRuntimeException(e.getMessage());
            }
        }
    }
}
