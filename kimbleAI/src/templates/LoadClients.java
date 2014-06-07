package templates;

import kimble.connection.serverside.clientloading.LoadClientsInterface;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import kimble.connection.clientside.KimbleClient;
import kimble.connection.serverside.KimbleClientInfo;

/**
 * This is a template for how to setup multiple AIs when you run your project.
 */
public class LoadClients implements LoadClientsInterface {

    // The server doesn't allow more or less than four players! It's up to you to choose if you use the already built jar or start them from code.
    private static final int MAX_NUMBER_OF_PLAYERS = 4;
    private final KimbleClient[] clients;

    /**
     * Accepts all from zero to 4 clients. If the amount is less then 4 the
     * jarStartInfo method will choose randomly bots from the bot directory.
     *
     * @param clients
     */
    public LoadClients(KimbleClient... clients) {
        if (clients.length > MAX_NUMBER_OF_PLAYERS) {
            throw new IllegalArgumentException("*** Maximum number of players is: " + MAX_NUMBER_OF_PLAYERS + ", you tried to add: " + clients.length + ".");
        }
        this.clients = clients;
    }

    /**
     * This method is used to run the bots/AI:s from the project.
     *
     * @return
     */
    @Override
    public KimbleClient[] clientStartInfo() {
        return clients;
    }

    /**
     * This method is used if you want to start the bots from their jars.
     *
     * @return
     */
    @Override
    public List<KimbleClientInfo> jarStartInfo() {
        // Gets the path to the relative directory "bots" containing the pre-built bots.
        // And gets the whole system path to it.
        String jarDir = new File("bots").getAbsolutePath();

        // Get the files in the "jarDir" ending with .jar
        File[] files = new File(jarDir).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar") && (name.contains("bot") || name.contains("Bot"));
            }
        });

        if (files == null || files.length == 0) {
            System.err.println("There aren't any bot files in: " + jarDir);
            System.exit(1);
        }

        // Generate the list of client information that the server needs.
        //   * "new KimbleClientInfo(clientName, runDirectory, jarName)"
        //
        //   * "clientName" = Name on your client that will show up in the logs and the gui
        //     (this have to be a unique name for every team participating).
        //     We strongly recommend that you put the same name on the jar,
        //     and if you want multiple AIs just give them a numbered extension.
        //
        //   * "directory" = the directory from where the AI is run. 
        //     When you build your project the dist/ directory will conatin the built jar.
        //
        //   * "jarName" = the name of the jar. If you name your jars with different names, 
        //     you'd probably would want to change this to load all of them.
        //
        //
        // In the game there'll be four teams playing. This means that you'll have to have 
        // four AIs running. Otherwise the server will crash and throw an exception telling you
        // how many players you need!
        //
        Random random = new Random();
        List<KimbleClientInfo> clientInfo = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER_OF_PLAYERS - clients.length; i++) {
            File botFile = files[random.nextInt(files.length)];
            String jarName = botFile.getName();
            String botName = jarName.substring(0, jarName.length() - 4);
            System.out.println("Loading AI: " + jarName);
            System.out.println("      From: " + jarDir);
            System.out.println("        As: " + botName);
            System.out.println("");
            clientInfo.add(new KimbleClientInfo(botName, jarDir, jarName));
        }

        // return the list of Client information. The server will then start
        // your clients based on the information in the list.
        return clientInfo;
    }

}
