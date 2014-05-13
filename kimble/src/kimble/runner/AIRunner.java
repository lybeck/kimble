package kimble.runner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Lasse Lybeck
 */
public class AIRunner {

    private final Process process;
    private final InputStream inputStream;
    private final InputStream errorStream;
    private final OutputStream outputStream;

    public AIRunner(String dir, String filename) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", dir + filename);
        pb.directory(new File(dir));
        this.process = pb.start();
        this.inputStream = process.getInputStream();
        this.errorStream = process.getErrorStream();
        this.outputStream = process.getOutputStream();
    }

    public void sendMessage(String message) throws IOException {
        outputStream.write(message.getBytes());
    }

    public String receiveMessage() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        char c;
        while ((c = (char) inputStream.read()) != '\n') {
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}
