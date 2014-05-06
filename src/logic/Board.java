package logic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lasse Lybeck
 */
public class Board {

    public static final int NUMBER_OF_TEAMS = 4;
    public static final int NUMBER_OF_PIECES = 4;
    public static final int SIDE_LENGTH = 8;

    private final Square[] spawnSquares;
    private final Square[] goalSquares;
    private final Piece[][] pieces;

    public Board() {
        this.spawnSquares = new Square[NUMBER_OF_TEAMS];
        this.goalSquares = new Square[NUMBER_OF_TEAMS];
        this.pieces = new Piece[NUMBER_OF_TEAMS][NUMBER_OF_PIECES];
        initBoard();
        initPieces();
    }

    private void initBoard() {
        // <editor-fold defaultstate="collapsed" desc="Initializing the board.">
        Square prev = null;
        for (int i = 0; i < NUMBER_OF_TEAMS; i++) {
            spawnSquares[i] = new Square();
            spawnSquares[i].setTeamSpawn(i);
            if (prev != null) {
                spawnSquares[i].setPrev(prev);
                prev.setNext(spawnSquares[i]);
            }
            prev = spawnSquares[i];
            for (int j = 0; j < SIDE_LENGTH - 2; j++) {
                Square newSquare = new Square();
                newSquare.setPrev(prev);
                prev.setNext(newSquare);
                prev = newSquare;
            }
        }
        if (prev != null) {
            spawnSquares[0].setPrev(prev);
            prev.setNext(spawnSquares[0]);
        }

        for (int i = 0; i < NUMBER_OF_TEAMS; i++) {
            goalSquares[i] = new Square();
            goalSquares[i].setPrev(getSpawnSquare(i).getPrev());
            prev = goalSquares[i];
            for (int j = 0; j < NUMBER_OF_PIECES - 1; j++) {
                Square newSquare = new Square();
                newSquare.setPrev(prev);
                prev.setNext(newSquare);
                prev = newSquare;
            }
        }
        // </editor-fold>
    }

    private void initPieces() {
        // <editor-fold defaultstate="collapsed" desc="Initializing the pieces.">
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                pieces[i][j] = new Piece(i);
            }
        }
        // </editor-fold>
    }

    public Square getSquares() {
        return spawnSquares[0];
    }

    public Square getSpawnSquare(int teamNo) {
        return spawnSquares[teamNo];
    }

    public Square getGoalSquare(int teamNo) {
        return goalSquares[teamNo];
    }

    public Piece[] getPieces(int teamNo) {
        return pieces[teamNo];
    }

    private int getNumberOfPiecesAtHome(int teamNo) {
        int count = 0;
        for (Piece piece : getPieces(teamNo)) {
            if (piece.getPosition() == null) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        // <editor-fold defaultstate="collapsed" desc="Complicated code to get the current game status as a string.">
        if (NUMBER_OF_TEAMS != 4) {
            return super.toString();
        }
        StringBuilder s = new StringBuilder();
        s.append(" ");
        for (int i = 0; i < SIDE_LENGTH - NUMBER_OF_PIECES; i++) {
            s.append(" ");
        }
        int atHome = getNumberOfPiecesAtHome(1);
        for (int i = 0; i < NUMBER_OF_PIECES; i++) {
            if (i < atHome) {
                s.append("1");
            } else {
                s.append(".");
            }
        }
        s.append(" \n");

        for (int i = 0; i < SIDE_LENGTH; i++) {
            if (i < NUMBER_OF_PIECES) {
                if (i < NUMBER_OF_PIECES - getNumberOfPiecesAtHome(0)) {
                    s.append(".");
                } else {
                    s.append("0");
                }
            } else {
                s.append(" ");
            }

            if (i == 0 || i == SIDE_LENGTH - 1) {
                s.append("o");
            } else {
                s.append(".");
            }
            for (int j = 0; j < SIDE_LENGTH - 2; j++) {
                if (i == 0 || i == SIDE_LENGTH - 1) {
                    s.append(".");
                } else {
                    s.append(" ");
                }
            }
            if (i == 0 || i == SIDE_LENGTH - 1) {
                s.append("o");
            } else {
                s.append(".");
            }
            if (i >= SIDE_LENGTH - NUMBER_OF_PIECES) {
                if (i < SIDE_LENGTH - NUMBER_OF_PIECES + getNumberOfPiecesAtHome(2)) {
                    s.append("2");
                } else {
                    s.append(".");
                }
            } else {
                s.append(" ");
            }

            s.append("\n");
        }

        s.append(" ");
        for (int i = 0; i < SIDE_LENGTH; i++) {
            if (i < NUMBER_OF_PIECES) {
                if (i < NUMBER_OF_PIECES - getNumberOfPiecesAtHome(3)) {
                    s.append(".");
                } else {
                    s.append("3");
                }
            } else {
                s.append(" ");
            }
        }
        s.append(" ");

        return s.toString();
        // </editor-fold>
    }

    public static void main(String[] args) {
        Board board = new Board();
        for (int i = 0; i < 4; i++) {
            board.getPieces(i)[0].setPosition(new Square());
//            board.getPieces(i)[1].setPosition(new Square());
//            board.getPieces(i)[2].setPosition(new Square());
//            board.getPieces(i)[3].setPosition(new Square());
        }
        System.out.println(board);
    }
}
