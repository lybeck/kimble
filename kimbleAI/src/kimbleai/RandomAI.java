package kimbleai;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.Random;
import kimble.connection.clientside.KimbleClient;

/**
 *
 * @author Lasse Lybeck
 */
public class RandomAI extends KimbleClient {
    
    private final Random random;

    public RandomAI() throws IOException {
        super("localhost", 5391);
        this.random = new Random();
    }

    @Override
    public void preLoop() {
    }

    @Override
    public void duringLoop() {
        String message = readMessage();
        System.out.println("Recieved: " + message);
        new JsonParser().parse(message).getAsJsonObject();
        
        
        // TODO: parse available moves, select index by random
        
        
    }

    @Override
    public void postLoop() {
    }
}
