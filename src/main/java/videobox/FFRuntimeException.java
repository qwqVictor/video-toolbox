package videobox;

import java.io.BufferedReader;
import java.io.IOException;

public class FFRuntimeException extends RuntimeException {

    /**
     * ���ڴ� FFmpeg �ı�׼��������ȡ������Ϣ��FFmpeg ����ʱ��������ʽ����
     * ������ȡ�������쳣�׳���Ϣ��
     * 
     * @param reader Ҫ��ȡ������Ϣ�Ļ����ȡ��
     * @return ��ȡ��ϵĴ�����Ϣ
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

    // һ��������̬�Ĺ��캯���������쳣������ȡΪ�ַ���
    public FFRuntimeException(BufferedReader reader) {
        this(readError(reader));
    }

    // һ��������̬�Ĺ��캯���������쳣��Ϣ�ַ���
    public FFRuntimeException(String e) {
        super("FFRuntimeException: " + e);
    }
}
