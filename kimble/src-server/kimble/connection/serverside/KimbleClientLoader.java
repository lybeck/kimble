package kimble.connection.serverside;

import java.io.IOException;
import java.util.List;
import kimble.connection.clientside.KimbleClient;

/**
 *
 * @author Christoffer
 */
public class KimbleClientLoader {

    /**
     * Method for the server to start the clients on it's specific host and port. Not accessible from the client.
     *
     * @param server
     * @param clientInfo
     * @param hostAddress
     * @param port
     * @throws IOException
     */
    static void load(KimbleServer server, List<KimbleClientInfo> clientInfo, String hostAddress, int port) throws IOException {
        for (int i = 0; i < clientInfo.size(); i++) {
            int id = server.getClients().size();
            KimbleClientAI client = new KimbleClientAI(id, clientInfo.get(i).getClientName());
            KimbleClientAIProcessStarter.startAI(client, clientInfo.get(i).getDirectory(), clientInfo.get(i).getJarName(), hostAddress, port);
            server.addPlayer(client);
        }
    }

    /**
     * Starts a new process with the specified jar information.
     *
     * @param server
     * @param clientInfo
     * @throws IOException
     */
    public static void load(KimbleServer server, List<KimbleClientInfo> clientInfo) throws IOException {
        for (int i = 0; i < clientInfo.size(); i++) {
            int id = server.getClients().size();
            KimbleClientAI client = new KimbleClientAI(id, clientInfo.get(i).getClientName());
            KimbleClientAIProcessStarter.startAI(client, clientInfo.get(i).getDirectory(), clientInfo.get(i).getJarName());
            server.addPlayer(client);
        }
    }

    /**
     * Starts a new thread with the specified AI. For starting with the play button in NetBeans.
     *
     * @param server
     * @param clients
     * @throws IOException
     */
    public static void load(KimbleServer server, KimbleClient... clients) throws IOException {
        for (int i = 0; i < clients.length; i++) {
            int id = server.getClients().size();
            KimbleClientAI client = new KimbleClientAI(id, clients[i].getName());
            new Thread(clients[i]).start();
            server.addPlayer(client);
        }
    }

}
