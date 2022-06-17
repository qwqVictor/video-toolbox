package videobox;

import java.io.BufferedReader;
import java.io.IOException;

public class FFRuntimeException extends RuntimeException {

    /**
     * 用于从 FFmpeg 的标准错误流读取错误信息。FFmpeg 运行时产生的流式错误
     * 被它读取并产生异常抛出信息。
     * 
     * @param reader 要读取错误信息的缓冲读取器
     * @return 读取完毕的错误信息
     */
    private static String readError(BufferedReader reader) {
        StringBuilder builder = new StringBuilder();
        try {
            do {
                builder.append(reader.readLine());
                builder.append("\n");
            } while (!reader.ready());
        } catch (IOException e) {
            return "Unknown error";
        }
        return builder.toString();
    }

    // 一种重载形态的构造函数，接受异常流并读取为字符串
    public FFRuntimeException(BufferedReader reader) {
        this(readError(reader));
    }

    // 一种重载形态的构造函数，接受异常信息字符串
    public FFRuntimeException(String e) {
        super("FFRuntimeException: " + e);
    }
}
