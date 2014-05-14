package kimbleai;

import java.io.IOException;
import kimble.bot.KimbleBot1;
import kimble.connection.serverside.KimbleClientLoader;
import kimble.connection.serverside.KimbleServer;
import kimble.connection.serverside.clientloading.LoadClientsInterface;
import kimbleai.clientloading.LoadClients;

/**
 *
 * @author Lasse Lybeck
 */
public class Main {

    private static void startServer(LoadClientsInterface loadClientsInterface) throws IOException {
        KimbleServer kimbleServer = new KimbleServer(5391, false);
        new KimbleClientLoader(kimbleServer, loadClientsInterface.loadInfoList());
        kimbleServer.run();
    }

    private static void startClient() throws IOException {
        //=======================================================
        // Change this to point to your own AI
        //=======================================================
        new KimbleBot1();
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            //=======================================================
            // Change the implementation of 'loadClients()' in 
            // 'LoadClients' to load different kinds of AIs.
            //=======================================================
            startServer(new LoadClients());
        } else {
            startClient();
        }
    }

}
