package kimble.logic;

import kimble.logic.board.Board;
import kimble.logic.board.Square;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kimble.logic.exception.IllegalMoveException;

/**
 *
 * @author Lasse Lybeck
 */
public class Game {

    private final Set<Integer> startValues;
    private final Set<Integer> continueTurnValues;
    private final List<Team> teams;
    private final List<Team> finishedTeams;
    private final Board board;
    private final Die die;
    private int turnIndex;
    private Turn currentTurn;

    public Game() {
        this(Constants.DEFAULT_START_VALUES, Constants.DEFAULT_CONTINUE_TURN_VALUES, Constants.DEFAULT_NUMBER_OF_TEAMS,
                Constants.DEFAULT_NUMBER_OF_PIECES, Constants.DEFAULT_SIDE_LENGTH);
    }

    public Game(Set<Integer> startValues, Set<Integer> continueTurnValues, int numberOfTeams, int numberOfPieces, int sideLength) {
        teams = new ArrayList<>(numberOfTeams);
        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(new Team(i, numberOfPieces));
        }
        finishedTeams = new ArrayList<>(numberOfTeams);
        this.startValues = startValues;
        this.continueTurnValues = continueTurnValues;
        this.board = new Board(numberOfTeams, numberOfPieces, sideLength);
        this.die = new Die();
        this.turnIndex = -1;
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

    public GameStart startGame() {
        if (this.turnIndex != -1) {
            throw new RuntimeException("Game::startGame can be called only once!");
        }
        GameStart gameStart = new GameStart(teams, die);
        this.turnIndex = gameStart.getStartingTeamIndex();
        return gameStart;
    }

    public Turn getNextTurn() {
        if (turnIndex == -1) {
            throw new RuntimeException("Game::startGame must be called before the first call to Game::getNextTurn!");
        }
        if (currentTurn != null) {
            throw new RuntimeException("No move selected from last turn!");
        }
        if (isGameOver()) {
            throw new RuntimeException("Game is already over!");
        }
        Team team = getTeamInTurn();
        int roll = die.roll();
        List<Move> moves = new ArrayList<>();
        for (Piece piece : team.getPieces()) {
            Move move = new Move(getStartValues(), getBoard(), piece, roll);
            if (move.isValidMove()) {
                moves.add(move);
            }
        }
        this.currentTurn = new Turn(roll, moves);
        return currentTurn;
    }

    public void executeMove(int i) {
        if (i < 0 || i >= currentTurn.size()) {
            throw new RuntimeException("Move index out of bounds!");
        }
        if (currentTurn == null) {
            throw new RuntimeException("No moves to select from! Game::getNextTurn must be called before calling Game::executeMove!");
        }
        try {
            currentTurn.getMove(i).execute();
        } catch (IllegalMoveException e) {
            throw new RuntimeException("Could not execute move!", e);
        }
        if (isFinished(teams.get(turnIndex))) {
            finishedTeams.add(teams.get(turnIndex));
            teams.get(turnIndex).setFinished(true);
//            System.out.println("Team " + teams.get(turnIndex).getId() + " finished!");
        }
        // check if game just finished
        if (isGameOver()) {
            addLastTeamToFinishedTeams();
        } else {
            nextTurn();
        }
    }

    public void executeNoMove() {
        for (Move move : currentTurn.getMoves()) {
            if (!move.isOptional()) {
                throw new RuntimeException("Must select a move if possible!");
            }
        }
        nextTurn();
    }

    private void nextTurn() {
        if (!continueTurnValues.contains(currentTurn.getDieRoll())) {
            do {
                if (++turnIndex >= teams.size()) {
                    turnIndex = 0;
                }
            } while (isFinished(teams.get(turnIndex)));
        }
        currentTurn = null;
    }

    public Team getTeamInTurn() {
        return teams.get(turnIndex);
    }

    public boolean isGameOver() {
        // all but one team is finishedTeams
        return finishedTeams.size() >= teams.size() - 1;
    }

    private boolean isFinished(Team team) {
        for (Piece piece : team.getPieces()) {
            if (piece.getPosition() == null || !piece.getPosition().isGoalSquare()) {
                return false;
            }
        }
        return true;
    }

    public List<Team> getFinishedTeams() {
        return finishedTeams;
    }

    private void addLastTeamToFinishedTeams() {
        Set<Team> teamSet = new HashSet<>(teams);
        for (Team team : finishedTeams) {
            teamSet.remove(team);
        }
        for (Team team : teamSet) {
            finishedTeams.add(team);
        }
    }

    @Override
    public String toString() {
        // <editor-fold defaultstate="collapsed" desc="Complicated code to get the current game status as a string.">
        StringBuilder s = new StringBuilder();

        // home squares
        for (int i = 0; i < getNumberOfTeams(); i++) {
            int pieces = getTeam(i).getPieces().size();
            for (int j = 0; j < pieces; j++) {
                if (getTeam(i).getPiece(j).isHome()) {
                    s.append(getTeam(i).getId());
                } else {
                    s.append(".");
                }
            }
            for (int j = 0; j < board.getSideLength() - pieces - 1; j++) {
                s.append(" ");
            }
        }
        s.append("\n");

        // main board
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
        s.append("\n");

        // goal squares
        for (int i = 0; i < getNumberOfTeams(); i++) {
            int pieces = getTeam(i).getPieces().size();
            Square square = board.getGoalSquare(i, 0);
            for (int j = 0; j < pieces; j++) {
                if (square.isPiecePresent()) {
                    s.append(square.getPiece().getTeamId());
                } else {
                    s.append(".");
                }
                square = square.getNext();
            }
            for (int j = 0; j < board.getSideLength() - pieces - 1; j++) {
                s.append(" ");
            }
        }

        return s.toString();
        // </editor-fold>
    }

    public Die getDie() {
        return die;
    }

}
