/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christoffer
 */
public class KimbleServer implements Runnable {

    public static final int TIME_OUT_MS = 500;

    private final int port;
    private final KimbleClient[] clients;

    public KimbleServer(int port, int numberOfPlayers) {
        this.port = port;
        this.clients = new KimbleClient[numberOfPlayers];
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            for (int i = 0; i < clients.length; i++) {
                clients[i] = new KimbleClient(i);
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(TIME_OUT_MS);
                clients[i].setSocket(socket);
            }

        } catch (IOException ex) {
            Logger.getLogger(KimbleServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
