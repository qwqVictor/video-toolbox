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

    // ���캯��
    // ���𴴽� FFBridge ʵ��������ʼ�� ffmpeg �����·��
    public FFBridge() {
        this.runtime = Runtime.getRuntime();
        /* ������ϵͳΪ Windows�����ð���Ԥװ�� ffmpeg �� */
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
            /* ������ϵͳ��Ϊ Windows������ϵͳ�����е� ffmpeg */
            this.FFmpegPath = "ffmpeg";
            this.FFprobePath = "ffprobe";
        }
    }

    /**
     * ̽���ļ�������Ϣ��������롢��ߡ�ʱ�������ʵ�
     * 
     * @param inputFile �����ļ�
     * @return ̽����ļ�����
     * @throws FFRuntimeException
     */
    private Map<String, Object> probe(String inputFile) throws FFRuntimeException {
        Map<String, Object> ret = new HashMap<String, Object>();
        // ������ͨ�� Java ���õĽ��̹�������� ffprobe ����̽�顣
        ProcessBuilder pBuilder = new ProcessBuilder();
        List<String> cmdArray = new ArrayList<String>();
        cmdArray.add(FFprobePath);
        /* ffprobe ̽��������в�����������չ����������б� */
        for (String arg : "-v error -show_entries stream=codec_type,codec_name,width,height,duration,bit_rate -of \"csv=s=,:p=0\""
                .split(" ")) {
            cmdArray.add(arg);
        }
        cmdArray.add(inputFile);
        pBuilder.command(cmdArray); // ���ý�����������
        try {
            Process p = pBuilder.start(); // ���� ffmpeg ����
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            do {
                /* ��ȡ��� */
                line = reader.readLine();
            } while (line == null && (p.isAlive() || p.exitValue() == 0));
            if (!p.isAlive() && p.exitValue() != 0) { // ������ֵ��Ϊ 0����˵���������쳣�������׳�
                throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
            }
            /* ����Ϊ�����Ϣ���� */
            String[] tokens = line.split(",");
            ret.put("codec_type", tokens[0]);
            ret.put("codec_name", tokens[1]);
            if (tokens.length == 6) {
                /* ���� 6 ����Ŀ����Ϊ��Ƶ����Ϊ��Ƶ�����ڿ������û�� width �� height ��Ŀ */
                ret.put("width", Integer.parseInt(tokens[2]));
                ret.put("height", Integer.parseInt(tokens[3]));
                ret.put("duration", Float.parseFloat(tokens[4]));
                ret.put("bit_rate", Integer.parseInt(tokens[5]) / 1024); // ����Ĭ��Ϊ bps������ 1024 ��Ϊ kbps
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
     * ת�빦��ģ��ĺ��ʵ�֡�ͨ���Խ� ffmpeg ���и�ʽת���Ȳ�����
     * 
     * @param inputFiles    ��������ת�����ļ��б�
     * @param outputPath    ���������Ŀ¼·��
     * @param outputFormat  �����ʽ
     * @param outputCodec   ������룬��Ϊ null ��ά��Ĭ��
     * @param outputWidth   �����Ƶ��ȣ����� outputHeight ͬʱ��Ϊ null ����������Ƶ
     * @param outputHeight  �����Ƶ�߶ȣ����� outputWidth ͬʱ��Ϊ null ����������Ƶ
     * @param outputBitRate �����Ƶ���ʣ���Ϊ null �����ر��ѹ�ƴ���
     * @throws FFRuntimeException
     */
    public void transform(List<String> inputFiles, String outputPath, String outputFormat, String outputCodec,
            Integer outputWidth,
            Integer outputHeight, Integer outputBitRate) throws FFRuntimeException {
        /* ���������ļ��б� */
        for (String file : inputFiles) {
            ProcessBuilder pBuilder = new ProcessBuilder();
            List<String> cmdArray = new ArrayList<String>();
            /* ���ȶ��ļ�����̽�飬��ȡ�ļ�������Ϣ */
            Map<String, Object> probeResult = probe(file);
            cmdArray.add(FFmpegPath);
            cmdArray.add("-y"); // �����ļ�
            cmdArray.add("-nostdin"); // Ҫ��ǽ���ʽ���飬���� stdin ����
            cmdArray.add("-i");
            cmdArray.add(file);
            cmdArray.add("-vcodec");
            if ((outputCodec == null || probeResult.get("codec_type").equals(outputCodec)) && outputWidth == null
                    && outputHeight == null && outputBitRate == null) // ��������б�������ֱ�ӿ�����Ƶ������Ҫȷ��û�����ź�����Ҫ������������ѹ��
                cmdArray.add("copy");
            else
                cmdArray.add(outputCodec == null ? (String) probeResult.get("codec_type") : outputCodec); // ������ѹ�������Ҫָ����ȷ�ı���
            if (outputBitRate != null && outputBitRate < (Integer) probeResult.get("bit_rate")) {
                cmdArray.add("-b:v");
                cmdArray.add(String.format("%dk", outputBitRate));
            }
            if (outputWidth != null && outputHeight != null) { // ������������������ָ���������
                cmdArray.add("-s");
                cmdArray.add(String.format("%dx%d", outputWidth, outputHeight));
            }
            cmdArray.add(getOutputFilePath(outputPath, file, outputFormat)); // ͨ�� getOutputFilePath
                                                                             // �������÷���ʵ�ּ�Ϊ������׸������������·��
            System.out.println(String.join(" ", cmdArray));
            pBuilder.redirectErrorStream(true); // ���д������ض�����⻺��������
            pBuilder.command(cmdArray); // ���ý�����������
            try {
                Process p = pBuilder.start(); // ���� ffmpeg ����
                BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = stdoutReader.readLine()) != null) {
                    System.out.println("Stdout: " + line);
                }
                p.waitFor(); // �ȴ��������
                if (!p.isAlive() && p.exitValue() != 0) { // ������ֵ��Ϊ 0����˵���������쳣�������׳�
                    throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
                }
            } catch (IOException | InterruptedException e) {
                throw new FFRuntimeException(e.getMessage());
            }
        }
    }

    /**
     * ��Ƶ�ϲ�����ģ��ĺ��ʵ�֡�ͨ�� FFmpeg �����˾��� concat �๤�����ʵ�ֶ��ֳ�������Ƶ�ϲ���
     * 
     * @param inputFiles �����ļ��б�
     * @param outputFile ����ļ�
     * @param transition ת��Ч������Ϊ null ����ת��
     * @throws FFRuntimeException
     */
    public void cat(List<String> inputFiles, String outputFile, String transition) throws FFRuntimeException {
        if (inputFiles.size() < 2) { // �������ļ���������2��������ϲ����׳��쳣
            throw new FFRuntimeException("No need to concentrate one video.");
        }
        ProcessBuilder pBuilder = new ProcessBuilder();
        List<String> cmdArray = new ArrayList<String>();
        cmdArray.add(FFmpegPath);
        cmdArray.add("-y"); // �����ļ�
        cmdArray.add("-nostdin"); // Ҫ��ǽ���ʽ���飬���� stdin ����
        Integer lastWidth = null, lastHeight = null;
        List<Map<String, Object>> probeResults = new ArrayList<Map<String, Object>>();
        String[] videoFilter = new String[inputFiles.size() - 1], audioFilter = new String[inputFiles.size() - 1];
        /* ���������ļ��б� */
        for (String file : inputFiles) {
            /* ���ȶ��ļ�����̽�飬��ȡ�ļ�������Ϣ */
            Map<String, Object> probeResult = probe(file);
            probeResults.add(probeResult);
            if (lastWidth == null)
                lastWidth = (Integer) probeResult.get("width");
            if (lastHeight == null)
                lastHeight = (Integer) probeResult.get("height");
            if (!(probeResult.get("width").equals(lastWidth))
                    || !(probeResult.get("height").equals(lastHeight))) { // ����Ƶ��߲��ȣ����׳��쳣�޷��ϲ�
                throw new FFRuntimeException("Width and height must be same");
            }
            if (transition != null) {
                cmdArray.add("-i");
                cmdArray.add(file);
            }
        }
        if (transition != null) { /* ��������ת��Ҫ���в�ͬ�Ĵ���ʽ */
            /* ��ת��������£�Ҫʹ�� FFmpeg �����˾����д��� */
            final int transitionDuration = 1; // �̶�ת��ʱ��Ϊ 1 ��
            float videoLastOffset = 0; // ��¼ת��ƫ����
            /* ����ѭ��������Ƶת��Ч�������˾�ָ�� */
            for (int i = 1; i < inputFiles.size(); i++) { // i=1 �𲽣���ÿ�κ���һ����Ƶ����ת��
                videoLastOffset += ((Float) probeResults.get(i - 1).get("duration"))
                        .floatValue() - transitionDuration; // ת��ƫ�����Ӻͣ����㹫ʽΪ����ǰת��ƫ����=��һ��ת��ƫ����+��ǰ��Ƶ����-ת��ʱ��
                videoFilter[i - 1] = String.format("[%s][%d]xfade=transition=%s:duration=%d:offset=%f%s", // ��ʽ�������˾�ָ����¸�ʽΪ��[Դ1][Դ2]xfade����[��ǰ�˾�������]
                        i == 1 ? "0" : String.format("vfade%d", i - 1), i, // ���ڵ�һ��ת����Դ1Ϊ0������Ϊ��һ���˾���������Դ2Ϊ��ǰ��Ƶ���i
                        transition, transitionDuration, videoLastOffset, // ����ת��Ч��������ʱ���ƫ����
                        i == inputFiles.size() - 1 ? ",format=yuv420p[video]" : String.format("[vfade%d]", i)); // ��Ϊ���һ���˾����ߣ���ָ��������������Ų���ʽ������Ե�ǰ�˾���������
            }
            /* ����ѭ��������Ƶ���뵭�������˾�ָ�� */
            for (int i = 1; i < inputFiles.size(); i++) {
                audioFilter[i - 1] = String.format("[%s][%d:a]acrossfade=d=%d%s", // ͬ�ϣ���ʽ������
                        i == 1 ? "0:a" : String.format("afade%d", i - 1), i, transitionDuration, // ͬ�ϣ����ڵ�һ����Դ1Ϊ0������Ϊ��һ���˾���������
                        i == inputFiles.size() - 1 ? "[audio]" : String.format("[afade%d]", i)); // ͬ�ϣ���Ϊ���һ���˾����ߣ���ָ�������ʽ������������
            }
            cmdArray.add("-filter_complex"); // ����ʹ�ø����˾�
            cmdArray.add(String.join(";", videoFilter) + ";" + String.join(";", audioFilter)); // �����ϲ������˾�ָ��ƴ�ӳɲ�����ʽ
            cmdArray.add("-map");
            cmdArray.add("[video]");
            cmdArray.add("-map");
            cmdArray.add("[audio]"); // ���ϲ��ֽ��й��߽����ӳ��
            cmdArray.add("-movflags");
            cmdArray.add("+faststart"); // ��Ƶ���ɵĿ��ٿ�ʼ����
        } else {
            /* ��ת��������������ļ��б��� concat �๤������������ϲ� */
            File filelist = null;
            try {
                filelist = File.createTempFile("cqupt3g-", ".txt"); // �����б���ʱ�ļ�
                filelist.deleteOnExit(); // �����˳�ʱɾ��
                FileWriter writer = new FileWriter(filelist);
                for (String file : inputFiles) {
                    writer.write(String.format("file '%s'\n", file.replaceAll("'", "\\'"))); // д���ļ��б���ת��
                }
                writer.close();
            } catch (IOException e) {
                throw new FFRuntimeException("Unable to create temp file: " + e.getMessage());
            }
            cmdArray.add("-f");
            cmdArray.add("concat"); // ����ʹ�� concat �๤�����
            cmdArray.add("-safe");
            cmdArray.add("0"); // ȡ������Ҫ�İ�ȫ��飬��Ϊ������Ƶ�������ε���Ӱ��ϲ�
            cmdArray.add("-i");
            cmdArray.add(filelist.getAbsolutePath());
            cmdArray.add("-codec");
            cmdArray.add("copy");
        }

        cmdArray.add(outputFile);
        System.out.println(String.join(" ", cmdArray));
        pBuilder.redirectErrorStream(true); // ���д������ض�����⻺��������
        pBuilder.command(cmdArray); // ���ý�����������
        try {
            Process p = pBuilder.start(); // ���� ffmpeg ����
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println("Stdout: " + line);
            }
            p.waitFor(); // �ȴ��������
            if (!p.isAlive() && p.exitValue() != 0) { // ������ֵ��Ϊ 0����˵���������쳣�������׳�
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
        cmdArray.add("-y"); // �����ļ�
        cmdArray.add("-nostdin"); // Ҫ��ǽ���ʽ���飬���� stdin ����
        cmdArray.add("-i");
        cmdArray.add(file);
        cmdArray.add("-vcodec");
        cmdArray.add("copy");
        cmdArray.add("-an");
        cmdArray.add(output);
        System.out.println(String.join(" ", cmdArray));
        pBuilder.redirectErrorStream(true); // ���д������ض�����⻺��������
        pBuilder.command(cmdArray); // ���ý�����������
        try {
            Process p = pBuilder.start(); // ���� ffmpeg ����
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println("Stdout: " + line);
            }
            p.waitFor(); // �ȴ��������
            if (!p.isAlive() && p.exitValue() != 0) { // ������ֵ��Ϊ 0����˵���������쳣�������׳�
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
        cmdArray.add("-y"); // �����ļ�
        cmdArray.add("-nostdin"); // Ҫ��ǽ���ʽ���飬���� stdin ����
        cmdArray.add("-i");
        cmdArray.add(file);
        cmdArray.add("-vn");
        if (copy) {
            cmdArray.add("-acodec");
            cmdArray.add("copy");
        }
        cmdArray.add(output);
        System.out.println(String.join(" ", cmdArray));
        pBuilder.redirectErrorStream(true); // ���д������ض�����⻺��������
        pBuilder.command(cmdArray); // ���ý�����������
        try {
            Process p = pBuilder.start(); // ���� ffmpeg ����
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println("Stdout: " + line);
            }
            p.waitFor(); // �ȴ��������
            if (!p.isAlive() && p.exitValue() != 0) { // ������ֵ��Ϊ 0����˵���������쳣�������׳�
                throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
            }
        } catch (IOException | InterruptedException e) {
            throw new FFRuntimeException(e.getMessage());
        }
    }

    /**
     * ������Ƶ���������
     * 
     * @param inputFiles ����������ļ��б�
     * @param outputPath �������·��
     * @throws FFRuntimeException
     */
    public void splitAudio(List<String> inputFiles, String outputPath) throws FFRuntimeException {
        for (String file : inputFiles) {
            String ext = getFileExtension(file); // ͨ�� getFileExtension ��ȡ��Ƶ��չ����ʵ�ּ�಻��׸��
            getVideoOnly(file, getOutputFilePath(outputPath, getFileNameWithoutExtension(file) + "_videoOnly", ext)); // ������Ƶ
            String audioExt = "m4a";
            boolean audioCopy = true;
            if (ext.equals("wmv") || ext.equals("ogv")) { // ����ȷ���� aac ����ʹ�� mp3 ��������������
                audioExt = "mp3";
                audioCopy = false;
            }
            getAudioOnly(file, getOutputFilePath(outputPath, getFileNameWithoutExtension(file), audioExt), audioCopy); // ������Ƶ
        }
    }

    /**
     * ������Ƶ���������
     * 
     * @param videoInputFiles ���������ϵ���Ƶ�ļ��б�
     * @param audioInputFiles ���������ϵ���Ƶ�ļ��б�
     * @param outputPath      �������·��
     * @throws FFRuntimeException
     */
    public void joinVideoAudio(List<String> videoInputFiles, List<String> audioInputFiles, String outputPath)
            throws FFRuntimeException {
        if (videoInputFiles.size() != audioInputFiles.size()) { // �����ϲ�����Ƶ����Ƶ�����޷���Ӧ���׳��쳣
            throw new FFRuntimeException("Video files and audio files mismatch!");
        }
        int length = videoInputFiles.size();
        for (int i = 0; i < length; i++) {
            ProcessBuilder pBuilder = new ProcessBuilder();
            List<String> cmdArray = new ArrayList<String>();
            String videoFile = videoInputFiles.get(i), audioFile = audioInputFiles.get(i); // ���
            Map<String, Object> videoProbeResult = probe(videoFile), audioProbeResult = probe(audioFile); // ����̽���ļ���Ϣ
            cmdArray.add(FFmpegPath);
            cmdArray.add("-y"); // �����ļ�
            cmdArray.add("-nostdin"); // Ҫ��ǽ���ʽ���飬���� stdin ����
            cmdArray.add("-i");
            cmdArray.add(videoFile);
            cmdArray.add("-i");
            cmdArray.add(audioFile);
            cmdArray.add("-vcodec");
            cmdArray.add("copy");
            if (((String) audioProbeResult.get("codec_type")).equals("aac")) { // ����ƵΪ AAC �����ֱ�ӿ�����
                cmdArray.add("-acodec");
                cmdArray.add("copy");
            }

            cmdArray.add(getOutputFilePath(outputPath, getFileNameWithoutExtension(videoFile) + "_joined",
                    getFileExtension(videoFile))); // ����ԭ��Ƶ�ļ����Ӻ�׺���
            System.out.println(String.join(" ", cmdArray));
            pBuilder.redirectErrorStream(true); // ���д������ض�����⻺��������
            pBuilder.command(cmdArray); // ���ý�����������
            try {
                Process p = pBuilder.start(); // ���� ffmpeg ����
                BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = stdoutReader.readLine()) != null) {
                    System.out.println("Stdout: " + line);
                }
                p.waitFor(); // �ȴ��������
                if (!p.isAlive() && p.exitValue() != 0) { // ������ֵ��Ϊ 0����˵���������쳣�������׳�
                    throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
                }
            } catch (IOException | InterruptedException e) {
                throw new FFRuntimeException(e.getMessage());
            }
        }
    }
}
