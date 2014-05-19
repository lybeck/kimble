/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.serverside;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import kimble.connection.serverside.KimbleClientInfo;
import kimble.connection.serverside.clientloading.LoadClientsInterface;

/**
 *
 * @author Christoffer
 */
class LoadTournamentClients implements LoadClientsInterface {

    private final int numberOfPlayers;

    public LoadTournamentClients(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public List<KimbleClientInfo> loadInfoList() {
        File f = new File("");

        File absFilePath = f.getAbsoluteFile();
        File root = absFilePath.getParentFile();
        File aiDir = new File(root, "kimbleAI/dist");

        File[] files = aiDir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });

        if (files == null) {
            throw new UnsupportedOperationException("Clean Build the kimbleAI project");
        }

        String dir = aiDir.getAbsolutePath();
        String name = files[0].getName();

        List<KimbleClientInfo> clientInfo = new ArrayList<>();
        if (files.length > 0) {
            for (int i = 0; i < numberOfPlayers; i++) {
                System.out.println("Loading AI: " + name);
                System.out.println("          : " + dir);
                clientInfo.add(new KimbleClientInfo("Client_" + i, dir, name));
            }
        }
        return clientInfo;
    }

}
