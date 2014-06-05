package kimble.connection.serverside;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import kimble.connection.clientside.StreamRedirecter;

/**
 *
 * @author Christoffer
 */
public class KimbleClientAIProcessStarter {

    /**
     * Starts a new process at in the desired directory withe the name specified with jarName. It also sends a "dummy"
     * argument, to fool the client not to start a new server.
     *
     * @param client
     * @param dir
     * @param jarName
     * @throws java.io.UnsupportedEncodingException
     * @throws IOException
     */
    public static void startAI(KimbleClientAI client, String dir, String jarName) throws UnsupportedEncodingException, IOException {
//        ProcessBuilder pb = new ProcessBuilder("java", "-jar", dir + jarName, "serverstart");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", dir + jarName, "kimbleai.TournamentMain", "serverstart");
        pb.directory(new File(dir));
        startProcess(pb);
    }

    /**
     * Start the new processes to access this host and port.
     *
     * @param client
     * @param dir
     * @param jarName
     * @param hostAddress
     * @param port
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void startAI(KimbleClientAI client, String dir, String jarName, String hostAddress, int port) throws UnsupportedEncodingException, IOException {
//        ProcessBuilder pb = new ProcessBuilder("java", "-jar", dir + jarName, hostAddress, port + "");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", dir + jarName, "kimbleai.TournamentMain", hostAddress, port
                + "");
        pb.directory(new File(dir));
        startProcess(pb);
    }

    private static void startProcess(ProcessBuilder pb) throws UnsupportedEncodingException, IOException {
        Process p = pb.start();
        new Thread(new StreamRedirecter(p.getInputStream(), System.out)).start();
        new Thread(new StreamRedirecter(p.getErrorStream(), System.err)).start();
    }
}
