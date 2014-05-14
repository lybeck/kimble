package templates;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import kimble.connection.clientside.KimbleClient;
import kimble.connection.clientside.MoveInfo;

/**
 *
 * @author Lasse Lybeck
 */
public class RandomAI extends KimbleClient {

    private Random random;

    public RandomAI(String hostAddress, int port) throws IOException {
        super(hostAddress, port);
        // =======================================
        // DON'T PUT ANYTHING IN THE CONSTRUCTOR
        // THE MAIN LOOP WILL START BEFORE THIS
        // CONSTRUCTOR IS RUN.
        //
        // INSTEAD USE THE PRE-LOOP METHOD
        // =======================================
    }

    @Override
    public void preLoop() {
        // =======================================
        // All setup code will go here
        // =======================================
        this.random = new Random();
    }

    @Override
    public void duringLoop() {
        // =======================================
        // This method is run after server message
        // is received.
        // =======================================
        String messageType = getReceiveMessageType();
        System.out.println("Recieved: " + messageType);

        if (messageType.equals("moves")) {
            List<MoveInfo> moves = getAvailableMoves();
            MoveInfo move = moves.get(random.nextInt(moves.size()));
            sendMove(move);
        }
    }

    @Override
    public void postLoop() {
        // =======================================
        // All clean up code will go here
        // =======================================
    }
}
