package templates;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import kimble.connection.clientside.KimbleClient;
import kimble.connection.clientside.MoveInfo;

/**
 * This file is a template for an AI class.
 */
public class RandomAI extends KimbleClient {

    private Random random;

    public RandomAI(String hostAddress, int port) throws IOException {
        super(hostAddress, port);
        this.random = new Random();
    }

    @Override
    public void handleTurn() {
        // =======================================
        // This method is run after server message
        // is received.
        // =======================================
        String messageType = getReceiveMessageType();
        System.out.println("Team " + getMyTeamId() + " Recieved: " + messageType);

        if (messageType.equals("moves")) {
            List<MoveInfo> moves = getAvailableMoves();
            MoveInfo move = moves.get(random.nextInt(moves.size()));
            sendMove(move);
        }
    }
}
