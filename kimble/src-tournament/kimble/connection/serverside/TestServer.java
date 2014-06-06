package kimble.connection.serverside;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.connection.serverside.clientloading.StartJarInterface;

/**
 *
 * @author Christoffer
 */
public class TestServer {

    private static final boolean USE_LOGGER = true;
    private static final boolean USE_GUI = false;

    private static final String HOST_ADDRESS = "localhost";
    private static final int PORT = 5391;

    private static void startServer(String hostAddress, int port, StartJarInterface loadClientsInterface) {
        KimbleServer kimbleServer = null;
        try {
            List<KimbleClientInfo> clientInfo = loadClientsInterface.jarStartInfo();
            kimbleServer = new KimbleServer(port, clientInfo.size(), USE_LOGGER, USE_GUI);
            KimbleClientLoader.load(kimbleServer, clientInfo, hostAddress, port);
            kimbleServer.run();
        } catch (Exception ex) {
            Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
            if (kimbleServer != null) {
                kimbleServer.disconnectClients();
            }
        }
    }

    public static void main(String[] args) {
        // TODO: add a loop that loops over all sets of games. This is just a test set - the real sets needs to be generated from the real bots! Combinatorics: sets of four out of all teams!

//        String directoryName = "competitors";
//        
//        Set<String> jarNames = new HashSet<>();
//        jarNames.add("KimbleAI_1.jar");
//        jarNames.add("KimbleAI_2.jar");
//        jarNames.add("KimbleAI_3.jar");
//        jarNames.add("KimbleAI_4.jar");
        
        String directoryName = "kimble-tournament";
        
        Set<String> jarNames = new HashSet<>();
        jarNames.add("KimbleBot1.jar");
        jarNames.add("KimbleBot2.jar");
        jarNames.add("KimbleBot3.jar");
        jarNames.add("KimbleAI.jar");

        startServer(HOST_ADDRESS, PORT, new LoadTournamentClients(directoryName, jarNames));
    }
}
