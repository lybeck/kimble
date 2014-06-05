package kimble.connection.serverside;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.connection.serverside.clientloading.StartJarInterface;

/**
 *
 * @author Christoffer
 */
public class TestServer {

    private static final boolean USE_LOGGER = true;
    private static final boolean USE_GUI = true;
    private static final boolean USE_HUD = false;

    private static final int NUMBER_OF_PLAYERS = 4;

    private static final String HOST_ADDRESS = "localhost";
    private static final int PORT = 5391;

    private static void startServer(String hostAddress, int port, StartJarInterface loadClientsInterface) {
        KimbleServer kimbleServer = null;
        try {
            List<KimbleClientInfo> clientInfo = loadClientsInterface.jarStartInfo();
            kimbleServer = new KimbleServer(port, clientInfo.size(), USE_LOGGER, USE_GUI, USE_HUD);
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
        startServer(HOST_ADDRESS, PORT, new LoadTournamentClients(NUMBER_OF_PLAYERS));
    }
}
