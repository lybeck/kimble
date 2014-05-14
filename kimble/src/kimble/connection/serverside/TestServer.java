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

//    private static String dir = "/home/lasse/NetBeansProjects/kimble/kimbleAI/dist/";
    private static String dir = "D:\\Programmering\\Java\\Kimble\\kimble\\kimbleAI\\dist\\";
    private static String jarName = "KimbleAI.jar";

    public static void main(String[] args) throws IOException {
        KimbleServer kimbleServer = new KimbleServer(5391);
        int numberOfPlayers = 4;
        for (int i = 0; i < numberOfPlayers; i++) {
            KimbleClientAI client = new KimbleClientAI(i);
            client.startAI(dir, jarName);
            kimbleServer.addPlayer(new KimbleClientAI(i));
        }
        kimbleServer.run();
    }
}
