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
     * One log file is approximately 100 kB.
     */
    public static final boolean USE_LOGGER = false;

    /**
     * USE_GUI = true, runs the project with the OpenGL GUI at ~60 FPS.
     * <p>
     * USE_GUI = false, runs the project as fast as possible without GUI.
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Start server method - You don't have to change anything here.">
    /**
     * Private method starting a local server on the specified 'port' with the
     * clients loaded in the 'StartJarInterface' implementation.
     *
     * in the template package there's a template on how to implement it. The
     * template is starting the same instance four (4) times.
     *
     * @param port
     * @param loadClientsInterface
     * @throws IOException
     */
    private static void startServer(int port, LoadClientsInterface loadClientsInterface) throws IOException {
        KimbleServer server = new KimbleServer(port, USE_LOGGER, USE_GUI, USE_HUD);
        KimbleClientLoader.load(server, loadClientsInterface.clientStartInfo());
        KimbleClientLoader.load(server, loadClientsInterface.jarStartInfo());
        server.run();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Create client method - You must change the 'new RandomAI(hostAddress, port)' to point to your AI">
    /**
     * =====================================<br>
     * Dont remove this method!<br>
     * =====================================<br>
     *
     * You can and should change the 'new KimbleClient(hostAddress, port)'
     * implementation in this method.
     *
     * This method is run from the 'TournamentMain' to start your AI. When you
     * run this project the main method in this class will start the local
     * server. Then the server starts the clients based on the client
     * information in the 'StartJarInterface' implementation.
     *
     * @param hostAddress
     * @param port
     * @return
     * @throws IOException
     */
    // </editor-fold>
    public static KimbleClient createAI(String hostAddress, int port) throws IOException {
        // TODO: Change this to point to your own AI.
        return new KimbleBot3(hostAddress, port);
    }

    // <editor-fold defaultstate="collapsed" desc="Define which AI:s runs each run.">
    /**
     * Generates the LoadClientsInterface implementation by creating a new
     * LoadClients instance.
     *
     * It is recommended to use the "createAI(hostAddress, port)" method to
     * start the instance of your own AI.
     *
     * ========================================================================
     * Example usage:
     * ------------------------------------------------------------------------
     * Creates a new LoadClients instance with only "yourAI". If you don't
     * create four (4) clients in this constructor, the LoadClients
     * implementation will try to start some bots randomly from the bots folder.
     *
     * <code>
     * LoadClients loadClients = new LoadClients(
     *      createAI(hostAddress, port)
     * );
     * </code>
     * ------------------------------------------------------------------------
     * You could start your AI implementation this way also, but remember to
     * change the return argument of the "createAI()" method to point to your
     * AI, otherwise the TournamentMain will not be able to find it!
     *
     * <code>
     * LoadClients loadClients = new LoadClients(
     *      new RandomAI(hostAddress, port)
     * );
     * </code>
     *
     * ------------------------------------------------------------------------
     *
     * We have provided some simple Bots for you to compete against.
     *
     * kimble.bot.KimbleBot1 <br>
     * kimble.bot.KimbleBot2 <br>
     * kimble.bot.KimbleBot3 <br>
     *
     * You can load these from code with the example below, or run the pre-built
     * jars in the jar folder (see LoadClients implementation for how this is
     * done).
     *
     *
     * Creates a new LoadClients list with "yourAI" and all of the three other
     * bots. Don't exceed the maximum number of four (4) AI:s running on the
     * server. It won't allow you!
     *
     * <code>
     * LoadClients loadClients = new LoadClients(
     *      createAI(hostAddress, port),
     *      new KimbleBot1(hostAddress, port),
     *      new KimbleBot2(hostAddress, port),
     *      new KimbleBot3(hostAddress, port)
     * );
     * </code>
     *
     * @param hostAddress
     * @param port
     * @return
     * @throws IOException
     */
    // </editor-fold>
    private static LoadClientsInterface generateLoadClients(String hostAddress, int port) throws IOException {

        LoadClients loadClients = new LoadClients(
                createAI(hostAddress, port)
        );

        return loadClients;
    }

    // <editor-fold defaultstate="collapsed" desc="Main method - run's one instance of your AI and randomly chosen AI:s from the bots directory">
    /**
     *
     * By surrounding the "startServer()" call with a loop you can make it run
     * over and over again. Remember to disable the GUI with the USE_GUI
     * variable at the top of this class, to make it go fast as well.
     *
     * @param args
     * @throws IOException
     */
    // </editor-fold>
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 1; i++) {
            startServer(PORT, generateLoadClients(HOST_ADDRESS, PORT));
        }
    }

}
