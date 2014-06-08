package starters;

import java.util.Arrays;
import java.util.List;
import kimble.connection.clientside.KimbleClient;
import kimble.connection.serverside.clientloading.StartClientInterface;

/**
 * This is a template for how to setup multiple AIs when you run your project.
 */
public class ClientLoader implements StartClientInterface {

    // The server doesn't allow more or less than four players! It's up to you to choose if you use the already built jar or start them from code.
    private static final int NUMBER_OF_PLAYERS = 4;
    private final KimbleClient[] clients;

    /**
     * Accepts all from zero to 4 clients. If the amount is less then 4 the jarStartInfo method will choose randomly
     * bots from the bot directory.
     *
     * @param clients
     */
    public ClientLoader(KimbleClient... clients) {
        this(Arrays.asList(clients));
    }

    public ClientLoader(List<KimbleClient> clientList) {
        if (clientList.size() != NUMBER_OF_PLAYERS) {
            throw new IllegalArgumentException("*** Number of players is: " + NUMBER_OF_PLAYERS + ", you tried to add: " + clientList.size() + ".");
        }
        KimbleClient[] clientArray = new KimbleClient[clientList.size()];
        clientList.toArray(clientArray);
        this.clients = clientArray;
    }

    /**
     * This method is used to run the bots/AI:s from the project.
     *
     * @return
     */
    @Override
    public KimbleClient[] clientStartInfo() {
        return clients;
    }
}
