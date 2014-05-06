package logic;

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
    public static final Set<Integer> DEFAULT_START_VALUES;

    static {
        DEFAULT_START_VALUES = new HashSet<>(2);
        DEFAULT_START_VALUES.add(1);
        DEFAULT_START_VALUES.add(6);
    }

}
