package application;

import java.io.BufferedReader;
import java.io.IOException;

public class FFRuntimeException extends RuntimeException {
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

    public FFRuntimeException(BufferedReader reader) {
        this(readError(reader));
    }

    public FFRuntimeException(String e) {
        super("FFRuntimeException: " + e);
    }
}
