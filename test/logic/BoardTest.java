package logic;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lasse Lybeck
 */
public class BoardTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testNothing() {
        fail("This is a placeholder test.");
    }

//    @Test
//    public void testRightNumberOfSquares() {
//        Square square1 = new Board().getSquares();
//        Square s = square1;
//        int count = 1;
//        int expected = Board.DEFAULT_NUMBER_OF_TEAMS * (Board.DEFAULT_SIDE_LENGTH - 1);
//        while ((s = s.getNext()) != square1 && count++ < expected);
//        assertEquals(expected, count);
//    }
//
//    @Test
//    public void testRightNumberOfGoalSquares() {
//        int expected = Board.DEFAULT_NUMBER_OF_PIECES;
//        Board board = new Board();
//        for (int i = 0; i < Board.DEFAULT_NUMBER_OF_TEAMS; i++) {
//            Square s = board.getGoalSquare(i);
//            int count = 1;
//            while ((s = s.getNext()) != null && count++ < expected);
//            assertEquals(expected, count);
//        }
//    }
}
