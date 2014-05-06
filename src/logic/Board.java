package logic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lasse Lybeck
 */
public class Board {

    private final int sideLength;
    private final int numberOfTeams;
    private final int numberOfPieces;

    private final List<Square> squares;
    private final List<Square> goalSquares;

    public Board(int numberOfTeams, int numberOfPieces, int sideLength) {
        this.numberOfTeams = numberOfTeams;
        this.numberOfPieces = numberOfPieces;
        this.squares = new ArrayList<>();
        this.goalSquares = new ArrayList<>();
        this.sideLength = sideLength;
        initBoard();
    }

    private void initBoard() {
        // <editor-fold defaultstate="collapsed" desc="Initializing the board.">
        Square square;
        Square prev = null;
        for (int i = 0; i < numberOfTeams; i++) {
            for (int j = 0; j < sideLength - 1; j++) {
                square = new Square();
                squares.add(square);
                if (prev != null) {
                    square.setPrev(prev);
                    prev.setNext(square);
                }
                prev = square;
            }
        }
        Square firstSquare = squares.get(0);
        firstSquare.setPrev(prev);
        prev.setNext(firstSquare);

        for (int i = 0; i < numberOfTeams; i++) {
            square = new Square();
            goalSquares.add(square);
            square.setPrev(getStartSquare(i).getPrev());
            prev = square;
            for (int j = 0; j < numberOfPieces - 1; j++) {
                square = new Square();
                goalSquares.add(square);
                square.setPrev(prev);
                prev.setNext(square);
                prev = square;
            }
        }
        // </editor-fold>
    }

    public List<Square> getSquares() {
        return squares;
    }

    public Square getSquare(int i) {
        return squares.get(i);
    }

    boolean isStartSquare(int squareIndex) {
        for (int i = 0; i < numberOfTeams; i++) {
            if (getStartSquareIndex(i) == squareIndex) {
                return true;
            }
        }
        return false;
    }

    public Square getStartSquare(int teamId) {
        return squares.get(getStartSquareIndex(teamId));
    }

    private int getStartSquareIndex(int teamId) {
        return teamId * (sideLength - 1);
    }

    public Square getGoalSquare(int teamId) {
        return goalSquares.get(getGoalSquareIndex(teamId));
    }

    private int getGoalSquareIndex(int teamId) {
        return teamId * numberOfPieces;
    }

    public int getSideLength() {
        return sideLength;
    }
}
