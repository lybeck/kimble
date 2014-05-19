/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.serverside;

import java.io.File;

/**
 *
 * @author Christoffer
 */
public class KimbleClientInfo {

    private final String clientName;
    private final String directory;
    private final String jarName;

    public KimbleClientInfo(String clientName, String directory, String jarName) {
        this.clientName = clientName;
        if (!directory.endsWith(File.separator)) {
            directory += File.separator;
        }
        this.directory = directory;
        this.jarName = jarName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getDirectory() {
        return directory;
    }

    public String getJarName() {
        return jarName;
    }

}
