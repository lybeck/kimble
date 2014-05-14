package templates;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import kimble.connection.serverside.KimbleClientInfo;
import kimble.connection.serverside.clientloading.LoadClientsInterface;

/**
 * This file is a template for how to setup multiple AIs when you run your
 * project.
 */
public class LoadClients implements LoadClientsInterface {

    @Override
    public List<KimbleClientInfo> loadInfoList() {

        String dir = new File("dist").getAbsolutePath();
        File[] files = new File(dir).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });

        if (files == null) {
            System.out.println("You must clean build your project before running it!");
            System.exit(1);
        }
        String jarName = files[0].getName();

        List<KimbleClientInfo> clientInfo = new ArrayList<>();
        if (files.length > 0) {
            for (int i = 0; i < 4; i++) {
                System.out.println("Loading AI: " + jarName);
                System.out.println("          : " + dir);
                clientInfo.add(new KimbleClientInfo("Client_" + i, dir, jarName));
            }
        }

        return clientInfo;
    }

}
