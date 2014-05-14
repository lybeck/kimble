package kimbleai;

import java.io.IOException;
import kimble.connection.serverside.KimbleClientLoader;
import kimble.connection.serverside.KimbleServer;
import kimble.connection.serverside.clientloading.LoadClientsInterface;
import kimbleai.clientloading.LoadClients;

/**
 *
 * @author Lasse Lybeck
 */
public class Main {

    public static final boolean NO_GUI = true;
    public static final String HOST_ADDRESS = "localhost";
    public static final int PORT = 5391;

    private static void startServer(int port, LoadClientsInterface loadClientsInterface) throws IOException {
        KimbleServer kimbleServer = new KimbleServer(port, NO_GUI);
        new KimbleClientLoader(kimbleServer, loadClientsInterface.loadInfoList());
        kimbleServer.run();
    }

    private static void startClient(String hostAddress, int port) throws IOException {
        //=======================================================
        // Change this to point to your own AI
        //=======================================================
        new RandomAI(hostAddress, port);
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            //=======================================================
            // Change the implementation of 'loadClients()' in 
            // 'LoadClients' to load different kinds of AIs.
            //=======================================================
            startServer(PORT, new LoadClients());
        } else {
            if (args.length > 2) {
                startClient(args[0], Integer.parseInt(args[1]));
            } else {
                startClient(HOST_ADDRESS, PORT);
            }
        }
    }

}
