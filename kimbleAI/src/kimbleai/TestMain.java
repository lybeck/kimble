package kimbleai;

import java.io.IOException;
import kimble.connection.serverside.KimbleServer;

/**
 *
 * @author Lasse Lybeck
 */
public class TestMain {

    public static void main(String[] args) throws IOException {
        KimbleServer server = new KimbleServer(5391);
        
        new RandomAI();
    }
}
