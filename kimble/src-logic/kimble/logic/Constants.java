package kimble.logic;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Lasse Lybeck
 */
public class Constants {

    public static final int DEFAULT_NUMBER_OF_TEAMS = 4;
    public static final int DEFAULT_NUMBER_OF_PIECES = 4;
    public static final int DEFAULT_SIDE_LENGTH = 8;
    public static final int DEFAULT_DIE_SIDES = 6;
    public static final int DEFAULT_FINISHING_TEAMS = 1;
    public static final Set<Integer> DEFAULT_START_VALUES;
    public static final Set<Integer> DEFAULT_CONTINUE_TURN_VALUES;
    
    public static final float DEFAULT_MOUSE_SPEED = 0.5f;
    public static final float DEFAULT_MOVE_SPEED = 5f;

    static {
        DEFAULT_START_VALUES = new HashSet<>(2);
        DEFAULT_START_VALUES.add(1);
        DEFAULT_START_VALUES.add(6);

        DEFAULT_CONTINUE_TURN_VALUES = new HashSet<>(1);
        DEFAULT_CONTINUE_TURN_VALUES.add(6);
    }

}
