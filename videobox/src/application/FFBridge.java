package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
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

    public static String getFileNameWithExtension(String filePath) {
        String[] tokens = Paths.get(filePath).getFileName().toString().split("\\.");
        return String.join(".", tokens);
    }

    public static String getFileNameWithoutExtension(String filePath) {
        String[] tokens = Paths.get(filePath).getFileName().toString().split("\\.");
        tokens = Arrays.copyOfRange(tokens, 0, tokens.length > 1 ? tokens.length - 1 : tokens.length);
        return String.join(".", tokens);
    }

    public static String getOutputFilePath(String outputPath, String fileName, String fileExtension) {
        if (fileExtension != null) {
            fileName = getFileNameWithoutExtension(fileName) + "." + fileExtension;
        }
        return String.join(File.separator, outputPath, fileName);
    }

    public FFBridge() {
        this.runtime = Runtime.getRuntime();
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
            this.FFmpegPath = "ffmpeg";
            this.FFprobePath = "ffprobe";
        }

    }

    public Map<String, Object> probe(String inputFile) throws FFRuntimeException {
        Map<String, Object> ret = new HashMap<String, Object>();
        ProcessBuilder pBuilder = new ProcessBuilder();
        List<String> cmdArray = new ArrayList<String>();
        cmdArray.add(FFprobePath);
        for (String arg : "-v error -show_entries stream=codec_type,codec_name,width,height,bit_rate -of \"csv=s=,:p=0\""
                .split(" ")) {
            cmdArray.add(arg);
        }
        cmdArray.add(inputFile);
        pBuilder.command(cmdArray);
        try {
            Process p = pBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            do {
                line = reader.readLine();
            } while (line == null && (p.isAlive() || p.exitValue() == 0));
            if (!p.isAlive() && p.exitValue() != 0) {
                throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
            }
            String[] tokens = line.split(",");
            ret.put("codec_type", tokens[0]);
            ret.put("codec_name", tokens[1]);
            ret.put("width", Integer.parseInt(tokens[2]));
            ret.put("height", Integer.parseInt(tokens[3]));
            ret.put("bit_rate", Integer.parseInt(tokens[4]) / 1000);
            return ret;
        } catch (IOException e) {
            throw new FFRuntimeException(e.getMessage());
        }

    }

    public void transform(List<String> inputFiles, String outputPath, String outputFormat, String outputCodec,
            Integer outputWidth,
            Integer outputHeight, Integer outputBitRate) throws FFRuntimeException {
        for (String file : inputFiles) {
            ProcessBuilder pBuilder = new ProcessBuilder();
            List<String> cmdArray = new ArrayList<String>();
            Map<String, Object> probeResult = probe(file);
            cmdArray.add(FFmpegPath);
            cmdArray.add("-y");
            cmdArray.add("-nostdin");
            cmdArray.add("-i");
            cmdArray.add(file);
            cmdArray.add("-vcodec");
            if ((outputCodec == null || probeResult.get("codec_type").equals(outputCodec)) && outputWidth == null
                    && outputHeight == null && outputBitRate == null)
                cmdArray.add("copy");
            else
                cmdArray.add(outputCodec);
            if (outputBitRate != null && outputBitRate < (Integer) probeResult.get("bit_rate")) {
                cmdArray.add("-b:v");
                cmdArray.add(String.format("%dk", outputBitRate));
            }
            if (outputWidth != null && outputHeight != null) {
                cmdArray.add("-s");
                cmdArray.add(String.format("%dx%d", outputWidth, outputHeight));
            }
            cmdArray.add(getOutputFilePath(outputPath, file, outputFormat));
            System.out.println(String.join(" ", cmdArray));
            pBuilder.redirectErrorStream(true);
            pBuilder.command(cmdArray);
            try {
                Process p = pBuilder.start();
                BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = stdoutReader.readLine()) != null) {
                    System.out.println("Stdout: " + line);
                }
                p.waitFor();
                if (!p.isAlive() && p.exitValue() != 0) {
                    throw new FFRuntimeException(new BufferedReader(new InputStreamReader(p.getErrorStream())));
                }
            } catch (IOException | InterruptedException e) {
                throw new FFRuntimeException(e.getMessage());
            }
        }
    }

    public static void main(String args[]) {
        try {
            FFBridge bridge = new FFBridge();
            List<String> inputFiles = new ArrayList<String>();
            inputFiles.add("C:\\Users\\Victor\\Downloads\\v.mov");
            bridge.transform(inputFiles, "C:\\Users\\Victor\\Downloads", "flv", "h264", 1920, 1080, 5000);
        } catch (FFRuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}
