package kimble.connection.serverside;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.connection.serverside.clientloading.StartJarInterface;
import kimble.logic.Constants;

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
        } catch (IOException ex) {
            Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
            if (kimbleServer != null) {
                kimbleServer.disconnectClients();
            }
        }
    }

    public static void main(String[] args) {
        
        String directoryName = "kimble-tournament";

        List<Heat> heats = new TournamentHeatGenerator(directoryName, Constants.DEFAULT_NUMBER_OF_TEAMS).getHeats();

        // TODO: Define a way to select number of rounds
        int rounds = 5;

        for (int i = 0; i < rounds; i++) {
            Collections.shuffle(heats);
            for (Heat heat : heats) {
                startServer(HOST_ADDRESS, PORT, new LoadTournamentClients(heat.getShuffledTeams()));
            }
        }

    }
}
