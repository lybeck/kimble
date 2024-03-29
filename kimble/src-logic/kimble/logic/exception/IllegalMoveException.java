package kimble.logic.exception;

/**
 *
 * @author Lasse Lybeck
 */
public class IllegalMoveException extends Exception {

    /**
     * Creates a new instance of <code>IllegalMoveException</code> without detail message.
     */
    public IllegalMoveException() {
    }

    /**
     * Constructs an instance of <code>IllegalMoveException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public IllegalMoveException(String msg) {
        super(msg);
    }
}
