package templates;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import kimble.connection.serverside.KimbleClientInfo;
import kimble.connection.serverside.clientloading.LoadClientsInterface;

/**
 * This is a template for how to setup multiple AIs when you run your project.
 */
public class LoadClients implements LoadClientsInterface {

    /**
     *
     * @return
     */
    @Override
    public List<KimbleClientInfo> loadInfoList() {

        // Gets the path to the relative directory "dist" containing the built jar.
        // And gets the whole system path to it.
        String dir = new File("bots").getAbsolutePath();

        // Looks for all files in the directory.
        File[] files = new File(dir).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                // filters the files in the directory and selects only 
                // files with extension .jar
                return name.endsWith(".jar");
            }
        });

        // if no files where found abort.
        if (files == null || files.length == 0) {
            System.out.println("You must clean build your project before running it!");
            System.exit(1);
        }

        // Coldly select the first jar and gets it's name without the system path.
        // (this could fail if you have a jar that is not a build from this project in the dist directory).
        String jarName = files[0].getName();

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
        int playerAmount = 4;
        List<KimbleClientInfo> clientInfo = new ArrayList<>();
        if (files.length > 0) {
            for (int i = 0; i < playerAmount; i++) {
                System.out.println("Loading AI: " + jarName);
                System.out.println("          : " + dir);
                clientInfo.add(new KimbleClientInfo("Client_" + i, dir, jarName));
            }
        }

        // return the list of Client information. The server will then start
        // your clients based on the information in the list.
        return clientInfo;
    }

}
