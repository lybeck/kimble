/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.serverside;

import java.io.IOException;

/**
 *
 * @author Christoffer
 */
public class TestServer {

    public static void main(String[] args) throws IOException {
        KimbleServer kimbleServer = new KimbleServer(5391);
        int numberOfPlayers = 4;
        for (int i = 0; i < numberOfPlayers; i++) {
            kimbleServer.addPlayer(new KimbleClientAI(i));
        }
        kimbleServer.run();
    }
}
