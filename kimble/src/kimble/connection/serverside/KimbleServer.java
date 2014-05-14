/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.serverside;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import kimble.ServerGame;
import kimble.connection.messages.DisconnectMessage;
import kimble.logic.IPlayer;

/**
 *
 * @author Christoffer
 */
public class KimbleServer implements Runnable {
    
    public static final int TIME_OUT_MS = 500;
    
    private final int port;
    private final ServerSocket serverSocket;
    private final List<IPlayer> clients;
    
    public KimbleServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.clients = new ArrayList<>();
    }
    
    @Override
    public void run() {
        new ServerGame(false, clients);
//        for (int i = 0; i < 10; i++) {
//            for (IPlayer ai : clients) {
//                try {
//                    KimbleClientAI kai = (KimbleClientAI) ai;
//                    kai.sendMessage("hello: " + i);
//                    System.out.println("Client_" + kai.getID() + ": " + kai.receiveMessage());
//                } catch (IOException ex) {
//                    Logger.getLogger(KimbleServer.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            System.out.println("");
//        }
        for (IPlayer iPlayer : clients) {
            KimbleClientAI client = (KimbleClientAI) iPlayer;
            client.send(new DisconnectMessage());
        }
    }
    
    void addPlayer(KimbleClientAI client) throws IOException {
        Socket socket = serverSocket.accept();
        socket.setSoTimeout(TIME_OUT_MS);
        client.setSocket(socket);
        clients.add(client);
    }
}
