package mypackage;

import java.util.List;
import java.util.Random;
import kimble.connection.clientside.KimbleClient;
import kimble.connection.clientside.MoveInfo;

/**
 *
 * @author Lasse Lybeck
 */
public class MyKimbleAI extends KimbleClient {

    /**
     * This is the name that is shown in the game <b>during the training</b>. The name of your AI in the actual
     * tournament won't be affected by this.
     */
    private static final String NAME_OF_MY_AI = "<myname>-AI";

    private final Random random;

    public MyKimbleAI(String host, int port) {

        // compulsory call to super class constructor
        super(NAME_OF_MY_AI, host, port);

        // initialize the fields
        this.random = new Random();
    }

    @Override
    protected void handleTurn() {

        // TODO: Check game finished message.

        // Check if the received message was to select moves. If not we don't have to do anything.
        if (!getReceiveMessageType().equals("moves")) {
            return;
        }

        // retrieve the list of available moves
        List<MoveInfo> availableMoves = getAvailableMoves();

        // select the best move
        MoveInfo move = selectMove(availableMoves);

        if (move == null) {
            // send ping if no move was selected (pass turn)
            sendPing();
        } else {
            // send the selected move to the server
            sendMove(move);
        }
    }

    /**
     * Selects the wanted move which is sent to the server. Return value {@code null} means that no move should be made
     * (pass turn).
     *
     * @param availableMoves List of available moves.
     * @return The selected move, or {@code null} if selected to pass turn.
     */
    private MoveInfo selectMove(List<MoveInfo> availableMoves) {

        // return null (pass turn) if no moves are available
        if (availableMoves.isEmpty()) {
            return null;
        }

        // select an index by random
        int index = random.nextInt(availableMoves.size());

        // return the move move
        return availableMoves.get(index);
    }
}
