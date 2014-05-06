package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import logic.exception.IllegalMoveException;

/**
 *
 * @author Lasse Lybeck
 */
public class Game {

    private final Set<Integer> startValues;
    private final List<Team> teams;
    private final Board board;
    private final Die die;

    public Game() {
        this(Constants.DEFAULT_START_VALUES, Constants.DEFAULT_NUMBER_OF_TEAMS, Constants.DEFAULT_NUMBER_OF_PIECES, Constants.DEFAULT_SIDE_LENGTH);
    }

    public Game(Set<Integer> startValues, int numberOfTeams, int numberOfPieces, int sideLength) {
        teams = new ArrayList<>(numberOfTeams);
        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(new Team(i, numberOfPieces));
        }
        this.startValues = startValues;
        this.board = new Board(numberOfTeams, numberOfPieces, sideLength);
        this.die = new Die();
    }

    public Board getBoard() {
        return board;
    }

    public Set<Integer> getStartValues() {
        return startValues;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Team getTeam(int id) {
        return teams.get(id);
    }

    public int getNumberOfTeams() {
        return teams.size();
    }

    @Override
    public String toString() {
        // <editor-fold defaultstate="collapsed" desc="Complicated code to get the current game status as a string.">
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < getNumberOfTeams(); i++) {
            int pieces = getTeam(i).getPieces().size();
            for (int j = 0; j < pieces; j++) {
                if (getTeam(i).getPiece(j).isHome()) {
                    s.append(getTeam(i).getId());
                } else {
                    s.append(".");
                }
            }
            s.append(" ");
        }

        s.append("\n");

        for (int i = 0; i < board.getSquares().size(); ++i) {
            Square square = board.getSquare(i);
            if (square.isPiecePresent()) {
                s.append(square.getPiece().getTeamId());
            } else {
                if (board.isStartSquare(i)) {
                    s.append("o");
                } else {
                    s.append(".");
                }
            }
        }

        return s.toString();
        // </editor-fold>
    }

    public static void main(String[] args) throws IllegalMoveException {
        Game game = new Game();
        System.out.println(game);
        System.out.println("-----------------------");
        Piece p0 = game.getTeam(0).getPiece(1);
        Piece p1 = game.getTeam(1).getPiece(1);
        for (int i = 0; i < 100; i++) {
            int roll = new Random().nextInt(6) + 1;
            System.out.println("Player 0 rolled " + roll + ".");
            Move move = new Move(game, p0, roll);
            if (move.isValidMove()) {
                move.execute();
            }
            System.out.println(game);
            System.out.println("-----------------------");

            roll = new Random().nextInt(6) + 1;
            System.out.println("Player 1 rolled " + roll + ".");
            move = new Move(game, p1, roll);
            if (move.isValidMove()) {
                move.execute();
            }
            System.out.println(game);
            System.out.println("-----------------------");
        }
    }
}
