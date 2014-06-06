package kimble.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Christoffer
 */
public class FileUtil {

    public static File findDirectory(String directoryName, File root) {
        if (!root.isDirectory()) {
            throw new UnsupportedOperationException("You can't use this method on a file!");
        }

        if (root.getName().equals(directoryName)) {
            return root;
        }

        File[] folders = root.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        });

        File foundIt = null;
        for (int i = 0; i < folders.length; i++) {
            if (foundIt != null) {
                break;
            }
            foundIt = findDirectory(directoryName, folders[i]);
        }

        return foundIt;
    }

    public static File findFile(String fileName, File root) {

        if (root.getName().equals(fileName) && root.isFile()) {
            return root;
        }

        File[] files = root.listFiles();
        File foundIt = null;

        if (files == null) {
            return null;
        }

        for (int i = 0; i < files.length; i++) {
            if (foundIt != null) {
                break;
            }
            foundIt = findFile(fileName, files[i]);
        }

        return foundIt;
    }
}
