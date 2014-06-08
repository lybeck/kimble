package starters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kimble.bot.KimbleBot1;
import kimble.bot.KimbleBot2;
import kimble.bot.KimbleBot3;
import kimble.connection.clientside.KimbleClient;
import kimble.connection.serverside.KimbleClientLoader;
import kimble.connection.serverside.KimbleServer;
import kimble.connection.serverside.clientloading.StartClientInterface;
import mypackage.MyKimbleAI;

/**
 *
 * @author Lasse Lybeck
 */
public class Main {

    public static final boolean USE_LOGGER = true;
    public static final boolean USE_GUI = true;
    public static final String HOST_ADDRESS = "localhost";
    public static final int PORT = 5391;

    // <editor-fold defaultstate="collapsed" desc="Start server method. You don't have to change anything here.">
    /**
     * Private method starting a local server on the specified 'port' with the clients loaded in the 'StartJarInterface'
     * implementation.
     *
     * in the template package there's a template on how to implement it. The template is starting the same instance
     * four (4) times.
     *
     * @param port
     * @param loadClientsInterface
     * @throws IOException
     */
    private static void startServer(int port, StartClientInterface loadClientsInterface) throws IOException {
        KimbleServer server = new KimbleServer(port, USE_LOGGER, USE_GUI);
        KimbleClientLoader.load(server, loadClientsInterface.clientStartInfo());
        server.run();
    }

    private static StartClientInterface generateLoadClients(String hostAddress, int port) {
        List<KimbleClient> ais = new ArrayList<>(4);
        ais.add(getAI(hostAddress, port));
        ais.addAll(getTrainingOpponents(hostAddress, port));
        ClientLoader loadClients = new ClientLoader(ais);
        return loadClients;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Create your training opponents. This method must return a list of 3 KimbleClients!">
    /**
     * This method returns a list of KimbleClients that are started as your training opponents. The list must contain
     * exactly 3 instances, or an exception will be thrown later on at server start.
     *
     * @param hostAddress
     * @param port
     * @return
     */
    // </editor-fold>
    private static List<KimbleClient> getTrainingOpponents(String hostAddress, int port) {
        List<KimbleClient> opponents = new ArrayList<>(3);
        opponents.add(new KimbleBot1(hostAddress, port));
        opponents.add(new KimbleBot2(hostAddress, port));
        opponents.add(new KimbleBot3(hostAddress, port));
        return opponents;
    }

    // <editor-fold defaultstate="collapsed" desc="Create client method. This method must return an instance of your AI.">
    /**
     * =====================================<br>
     * Dont remove this method!<br>
     * =====================================<br>
     *
     * You can change the return value of this method.
     *
     * This method is run from the 'TournamentMain' to start your AI. When you run this project the main method in this
     * class will start the local server. Then the server starts the clients based on the client information in the
     * 'StartJarInterface' implementation.
     *
     * @param hostAddress
     * @param port
     * @return
     * @throws IOException
     */
    // </editor-fold>
    public static KimbleClient getAI(String hostAddress, int port) {
        /*
         * This is the AI that will be run. If you create a new AI class you must change this to return an instance of
         * your new class.
         */
        return new MyKimbleAI(hostAddress, port);
    }

    public static void main(String[] args) throws IOException {
        int numberOfGames = 1;
        for (int i = 0; i < numberOfGames; i++) {
            startServer(PORT, generateLoadClients(HOST_ADDRESS, PORT));
        }
    }
}
