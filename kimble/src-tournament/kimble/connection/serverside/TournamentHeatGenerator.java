package kimble.connection.serverside;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kimble.util.FileUtil;

/**
 *
 * @author Christoffer, Lasse
 */
class TournamentHeatGenerator {

    private final File aiDirectory;
    private final List<Heat> heats;
    private final int playersPerHeat;

    TournamentHeatGenerator(String jarDirectory, int playersPerHeat) {
        this.aiDirectory = FileUtil.findDirectory(jarDirectory, "");
        if (this.aiDirectory == null) {
            throw new UnsupportedOperationException("directory: " + jarDirectory + " not found!");
        }
        this.heats = generateHeats();
        this.playersPerHeat = playersPerHeat;
    }

    private List<Heat> generateHeats() {
        List<KimbleClientInfo> jars = getJars();
        System.out.println("Amount of AI directories found: " + jars.size());
        List<Heat> allHeats = new ArrayList<>();
        generateHeats(jars, new Heat(), allHeats, 0);
        return allHeats;
    }

    private List<KimbleClientInfo> getJars() throws UnsupportedOperationException {
        List<File> dirs = Arrays.asList(aiDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        }));
        if (dirs == null || dirs.size() < playersPerHeat) {
            throw new UnsupportedOperationException("Couldn't find any or enough AI directories in "
                    + aiDirectory.getAbsolutePath());
        }
        List<KimbleClientInfo> jars = new ArrayList<>(dirs.size());
        for (File dir : dirs) {
            String[] jarFiles = dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            });
            if (jarFiles == null || jarFiles.length != 1) {
                throw new RuntimeException("AI directory does not have exactly one jar-file! Dir: "
                        + dir.getAbsolutePath());
            }
            jars.add(new KimbleClientInfo(dir.getName(), dir.getAbsolutePath(), jarFiles[0]));
        }
        return jars;
    }

    private void generateHeats(List<KimbleClientInfo> jars, Heat heat, List<Heat> allHeats, int index) {
        if (heat.size() == playersPerHeat) {
            allHeats.add(heat);
            return;
        }
        for (int i = index; i < jars.size(); i++) {
            if (jars.size() - i < playersPerHeat - heat.size()) {
                break;
            }
            Heat newHeat = new Heat(heat);
            newHeat.addTeam(jars.get(i));
            generateHeats(jars, newHeat, allHeats, i + 1);
        }
    }

    List<Heat> getHeats() {
        return new ArrayList<>(heats);
    }
}
