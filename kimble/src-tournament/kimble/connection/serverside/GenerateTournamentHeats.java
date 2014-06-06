package kimble.connection.serverside;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import kimble.util.FileUtil;

/**
 *
 * @author Christoffer
 */
public class GenerateTournamentHeats {

    private File jarDirectory;

    private Map<Integer, Set> heats;

    public GenerateTournamentHeats(String jarDirectory) {
        this.jarDirectory = FileUtil.findDirectory(jarDirectory, "");
        if (this.jarDirectory == null) {
            throw new UnsupportedOperationException("directory: " + jarDirectory + " not found!");
        }
    }

    public void generateHeats() {
        heats = new HashMap<>();

        // TODO: implement this method properly
        
        File[] jarFiles = jarDirectory.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });

        if (jarFiles == null) {
            throw new UnsupportedOperationException("Couldn't find any jar-files in " + jarDirectory.getAbsolutePath());
        }

        System.out.println("Amount of jarFiles found: " + jarFiles.length);

    }
}
