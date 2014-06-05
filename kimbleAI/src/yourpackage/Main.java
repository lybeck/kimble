package yourpackage;

import java.io.IOException;
import kimble.bot.KimbleBot3;
import kimble.connection.clientside.KimbleClient;
import kimble.connection.serverside.KimbleClientLoader;
import kimble.connection.serverside.KimbleServer;
import kimble.connection.serverside.clientloading.LoadClientsInterface;
import templates.LoadClients;

/**
 *
 * @author Lasse Lybeck
 */
public class Main {

    // <editor-fold defaultstate="collapsed" desc="Parameters used to start the server. - You can alter these to get less or more info from the runs.">
    /**
     * This parameter is for logging the game run by the server. The logged
     * files are found in 'logs/' directory in the root of this project and the
     * log files are named 'log_x.txt', where x always is calculated from the
     * number of files in logs/ plus one.
     *
     * "x = [number of files in logs/] + 1"
     *
     * One log file is approximately 100 kb.
     */
    public static final boolean USE_LOGGER = false;

    /**
     * USE_GUI = true, runs the project with the OpenGL gui at ~60 fps.
     * <p>
     * USE_GUI = false, runs the project as fast as possible without gui.
     */
    public static final boolean USE_GUI = true;

    /**
     * USE_HUD = true, gives a info text area on top of the screen.
     * <p>
     * USE_HUD = false, doesn't initialize the HUD
     */
    public static final boolean USE_HUD = false;

    /**
     * The following parameters defines where the server is running. You are
     * running a local server, hence we recommend "localhost" as host address.
     *
     * The port isn't that critical. If you have problem running on a certain
     * port, it's most likely that it's already taken.
     *
     * Make sure to end this process before restarting it!
     */
    public static final String HOST_ADDRESS = "localhost";
    public static final int PORT = 5391;
//    public static final int PORT = 5291;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Start server method - You don't have to change anything here.">
    /**
     * Private method starting a local server on the specified 'port' with the
     * clients loaded in the 'LoadClientsInterface' implementation.
     *
     * in the template package there's a template on how to implement it. The
     * template is starting the same instance four (4) times.
     *
     * @param port
     * @param loadClientsInterface
     * @throws IOException
     */
    private static void startServer(int port, LoadClientsInterface loadClientsInterface) throws IOException {
        KimbleServer kimbleServer = new KimbleServer(port, USE_LOGGER, USE_GUI, USE_HUD);

        // starts this instance
        KimbleClientLoader.load(kimbleServer, startClient(HOST_ADDRESS, port));

        // start all the clients in the list.
        KimbleClientLoader.load(kimbleServer, loadClientsInterface.loadInfoList());

        kimbleServer.run();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Start client method - You must change the 'new KimbleClient(hostAddress, port)' to point to your AI">
    /**
     * =====================================<br>
     * Dont remove this method!<br>
     * =====================================<br>
     *
     * You can change the 'new KimbleClient(hostAddress, port)' implementation
     * in the method.
     *
     * This method is run from the 'TournamentMain' to start your AI. When you
     * run this project the main method in this class will start the local
     * server. Then the server starts the clients based on the client
     * information in the 'LoadClientsInterface' implementation.
     *
     * @param hostAddress
     * @param port
     * @return
     * @throws IOException
     */
    public static KimbleClient startClient(String hostAddress, int port) throws IOException {
        //=======================================================
        // Change this to point to your own AI.
        //
        // We have provided some simple Bots for you to compete
        // against.
        //
        //  * kimble.bot.KimbleBot1
        //  * kimble.bot.KimbleBot2
        //  * kimble.bot.KimbleBot3
        //=======================================================

//        new KimbleBot3(hostAddress, port);
        return new KimbleBot3(hostAddress, port);
    }

    // </editor-fold>
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 1; i++) {

//            ai = new RandomAI(HOST_ADDRESS, PORT);
            //=======================================================
            // Change the implementation of 'loadClients()' in 
            // 'LoadClients' to load different kinds of AIs.
            //=======================================================
            startServer(PORT, new LoadClients());
//            KimbleServer server = startServer(PORT, new LoadClients());
//            server.run();
//            Thread serverThread = new Thread(server);
//            serverThread.start();

//            Thread aiThread = new Thread(ai);
//            aiThread.start();
        }
    }

}
