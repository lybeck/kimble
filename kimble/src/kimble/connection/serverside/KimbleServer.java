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
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.ServerGame;
import kimble.connection.messages.DisconnectMessage;
import kimble.connection.messages.GameInitMessage;
import kimble.connection.messages.PingMessage;
import kimble.connection.messages.YourTeamIdMessage;
import kimble.logic.IPlayer;

/**
 *
 * @author Christoffer
 */
public class KimbleServer implements Runnable {

    public static final int TIME_OUT_MS = 500;

    private final int maxPlayers;

    private final ServerSocket serverSocket;
    private final List<IPlayer> clients;
    private final boolean noGui;

    KimbleServer(int port, int maxPlayers, boolean noGui) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clients = new ArrayList<>();

        this.maxPlayers = maxPlayers;

        this.noGui = noGui;
    }

    public KimbleServer(int port, boolean noGui) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clients = new ArrayList<>();
        this.maxPlayers = 4;
        this.noGui = noGui;
    }

    @Override
    public void run() {

        try {
            ServerGame serverGame = new ServerGame(noGui, clients);
            for (IPlayer iPlayer : clients) {
                KimbleClientAI client = (KimbleClientAI) iPlayer;
                client.send(new GameInitMessage(serverGame.getLogic().getGame().getBoard(), maxPlayers));
            }
            serverGame.start();
        } catch (Exception ex) {
            Logger.getLogger(KimbleServer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            for (IPlayer iPlayer : clients) {
                KimbleClientAI client = (KimbleClientAI) iPlayer;
                client.send(new DisconnectMessage());
            }
        }

        disconnectClients();
    }

    void disconnectClients() {
        for (IPlayer iPlayer : clients) {
            KimbleClientAI client = (KimbleClientAI) iPlayer;
            client.send(new DisconnectMessage());
        }

        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(KimbleServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void addPlayer(KimbleClientAI client) throws IOException {
        if (clients.size() == maxPlayers) {
            throw new UnsupportedOperationException("You can't load more players than: " + maxPlayers);
        }
        Socket socket = serverSocket.accept();
        socket.setSoTimeout(TIME_OUT_MS);
        client.setSocket(socket);
        clients.add(client);
        client.send(new YourTeamIdMessage(client.getID()));
    }
}
