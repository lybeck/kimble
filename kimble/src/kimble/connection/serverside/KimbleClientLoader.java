/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.serverside;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Christoffer
 */
public class KimbleClientLoader {

    public KimbleClientLoader(KimbleServer server, List<KimbleClientInfo> clientInfo) throws IOException {
        for (int i = 0; i < clientInfo.size(); i++) {
            KimbleClientAI client = new KimbleClientAI(i);
            client.startAI(clientInfo.get(i).getDirectory(), clientInfo.get(i).getJarName());
            server.addPlayer(client);
        }
    }

}
