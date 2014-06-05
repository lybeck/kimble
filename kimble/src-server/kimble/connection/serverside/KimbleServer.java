package kimble.connection.serverside;

import kimble.connection.logger.KimbleGameStateLogger;
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
import kimble.connection.messages.YourTeamIdMessage;
import kimble.logic.IPlayer;

/**
 *
 * @author Christoffer
 */
public class KimbleServer implements Runnable {

    public static final int TIME_OUT_MS = 1000;

    private final int maxPlayers;

    private final ServerSocket serverSocket;
    private final List<IPlayer> clients;
    private final boolean useGui;

    public KimbleServer(int port, boolean useGui) throws IOException {
        this(port, 4, false, useGui);
    }

    public KimbleServer(int port, boolean useLogger, boolean useGui) throws IOException {
        this(port, 4, useLogger, useGui);
    }

    KimbleServer(int port, int maxPlayers, boolean useLogger, boolean useGui) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clients = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.useGui = useGui;

        if (useLogger) {
            KimbleGameStateLogger.init();
        }
    }

    @Override
    public void run() {

        if (clients.size() != maxPlayers) {
            disconnectClientsError("Wrong number of players when started!");
            throw new UnsupportedOperationException("You'll have to have " + maxPlayers + " players!");
        }

        try {
            ServerGame serverGame = new ServerGame(useGui, clients);
            for (IPlayer iPlayer : clients) {
                KimbleClientAI client = (KimbleClientAI) iPlayer;
                client.send(new GameInitMessage(serverGame.getLogic().getGame().getBoard(), maxPlayers));
            }
            serverGame.start();

        } catch (Exception ex) {
            disconnectClientsError("Exception in server.");
            Logger.getLogger(KimbleServer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        disconnectClients();
    }

    void disconnectClientsError(String message) {
        System.err.println(" *** Disconnecting all connected players! " + message);
        disconnectClients();
    }

    void disconnectClients() {
        for (IPlayer iPlayer : clients) {
            KimbleClientAI client = (KimbleClientAI) iPlayer;
            client.send(new DisconnectMessage());
        }

        try {
            serverSocket.close();
            if (KimbleGameStateLogger.isInitialized()) {
                KimbleGameStateLogger.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(KimbleServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void addPlayer(KimbleClientAI client) throws IOException {
        if (clients.size() >= maxPlayers) {
            disconnectClientsError("Adding too many players to the clients.");
            throw new UnsupportedOperationException("You can't load more players than: " + maxPlayers);
        }
        Socket socket = serverSocket.accept();
        socket.setSoTimeout(TIME_OUT_MS);
        client.setSocket(socket);
        clients.add(client);
        client.send(new YourTeamIdMessage(client.getID()));
    }

    List<IPlayer> getClients() {
        return clients;
    }
}
