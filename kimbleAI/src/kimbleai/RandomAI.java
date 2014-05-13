package kimbleai;

import java.io.IOException;
import java.util.Random;
import kimble.connection.clientside.KimbleClient;

/**
 *
 * @author Lasse Lybeck
 */
public class RandomAI extends KimbleClient {

    public RandomAI() throws IOException {
        super("localhost", 5391);
    }

    @Override
    public void preLoop() {
    }

    @Override
    public void duringLoop() {
        System.out.println("test log");
        sendMessage("Recieved: " + readMessage());
    }

    @Override
    public void postLoop() {
    }
}
