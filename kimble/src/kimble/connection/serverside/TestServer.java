/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.serverside;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.connection.serverside.clientloading.LoadClientsInterface;

/**
 *
 * @author Christoffer
 */
public class TestServer {

    private static void startServer(int port, LoadClientsInterface loadClientsInterface) {
        KimbleServer kimbleServer = null;
        try {
            List<KimbleClientInfo> clientInfo = loadClientsInterface.loadInfoList();
            kimbleServer = new KimbleServer(port, clientInfo.size(), false);
            new KimbleClientLoader(kimbleServer, clientInfo);
            kimbleServer.run();
        } catch (Exception ex) {
            Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
            if (kimbleServer != null) {
                kimbleServer.disconnectClients();
            }
        }
    }

    public static void main(String[] args) {
        startServer(5391, new LoadTournamentClients(4));
    }
}
