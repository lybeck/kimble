package kimble.connection.serverside;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kimble.connection.serverside.clientloading.StartJarInterface;
import kimble.util.FileUtil;

/**
 *
 * @author Christoffer
 */
class LoadTournamentClients implements StartJarInterface {

    private final String directoryName;
    private final Set<String> jarNames;

    public LoadTournamentClients(String directoryName, Set<String> jarNames) {
        this.directoryName = directoryName;
        this.jarNames = jarNames;
    }

    @Override
    public List<KimbleClientInfo> jarStartInfo() {
        File aiDir = FileUtil.findDirectory(directoryName, "");
        if (aiDir == null) {
            throw new IllegalArgumentException("Directory: " + directoryName + " couldn't be found!");
        }

        File[] files = aiDir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                for (String jarName : jarNames) {
                    if (name.equals(jarName)) {
                        return true;
                    }
                }
                return false;
            }
        });

        if (files == null) {
            throw new UnsupportedOperationException("Nothing inside competitors folder or competitors folder doesn't exist!");
        }

        if (files.length != jarNames.size()) {
            throw new UnsupportedOperationException("The system couldn't find the jars specified!");
        }

        List<KimbleClientInfo> clientInfo = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String jarName = files[i].getName();
            String botName = jarName.substring(0, jarName.length() - 4);

            System.out.println("Loading AI: " + jarName);
            System.out.println("      From: " + aiDir.getAbsolutePath());
            System.out.println("        As: " + botName);
            clientInfo.add(new KimbleClientInfo(botName, aiDir.getAbsolutePath(), jarName));
        }

        return clientInfo;
    }

}
