package kimble;

import java.util.List;
import java.util.Map;
import java.util.Set;
import kimble.connection.logger.KimbleGameStateLogger;
import kimble.logic.Constants;
import kimble.logic.Game;
import kimble.logic.GameStart;
import kimble.logic.IPlayer;
import kimble.logic.KimbleLogicInterface;
import kimble.logic.Move;
import kimble.logic.Team;
import kimble.logic.Turn;
import kimble.logic.board.Board;
import kimble.logic.player.KimbleAI;
import kimble.logic.player.KimblePlayer;

/**
 *
 * @author Christoffer
 */
public class KimbleGameLogic implements KimbleLogicInterface {

    private static boolean DEBUG;
    private final Game game;
    private final List<IPlayer> players;
    private IPlayer currentPlayer;
    private int winner;

    private final Set<Integer> startValues;
    private final Set<Integer> continueTurnValues;
    private final int numberOfPieces;
    private final int sideLength;

    private Turn currentTurn;
    private int startingTeamIndex;
    private List<Map<Integer, Integer>> startingDieRolls;

    private String moveMessage;
    private Move selectedMove;

    public KimbleGameLogic(List<IPlayer> players, boolean debug) {
        this(players,
                Constants.DEFAULT_START_VALUES,
                Constants.DEFAULT_CONTINUE_TURN_VALUES,
                Constants.DEFAULT_NUMBER_OF_PIECES,
                Constants.DEFAULT_SIDE_LENGTH,
                Constants.DEFAULT_FINISHING_TEAMS,
                debug);
    }

    public KimbleGameLogic(List<IPlayer> players,
            Set<Integer> startValues,
            Set<Integer> continueTurnValues,
            int numberOfPieces,
            int sideLength,
            int numberOfFinishingTeams,
            boolean debug) {
        this.players = players;
        this.startValues = startValues;
        this.continueTurnValues = continueTurnValues;
        this.numberOfPieces = numberOfPieces;
        this.sideLength = sideLength;
        this.game = new Game(startValues, continueTurnValues, players.size(), numberOfPieces, sideLength,
                numberOfFinishingTeams);
        DEBUG = debug;

        setup();
    }

    private void setup() {

        for (int i = 0; i < players.size(); i++) {
            IPlayer player = players.get(i);

            player.setMyTeam(game.getTeam(i));
            if (player.isAIPlayer()) {
                ((KimbleAI) player).setBoard(game.getBoard());

                // *********************************************************************
                if (KimbleGameStateLogger.isInitialized()) {
                    KimbleGameStateLogger.logTeam(i, ((KimbleAI) player).getTeamName(), getGame().getTeam(i).getPieces().size());
                }
                // *********************************************************************

            } else {
                System.out.println("Human player in list");
//                throw new UnsupportedOperationException("Human players not yet supported!");
            }
        }

        GameStart gameStart = game.startGame();
        startingTeamIndex = gameStart.getStartingTeamIndex();
        startingDieRolls = gameStart.getRolls();

        if (DEBUG) {
            System.out.println("Game start:");
            System.out.println(gameStart.getRolls());
            System.out.println("Starting team: " + gameStart.getStartingTeamIndex());
            System.out.println("--------------------------------------------------");
            System.out.println("");
            System.out.println(game);
            System.out.println("--------------------------------------------------");
            System.out.println("");
        }

        // *********************************************************************
        if (KimbleGameStateLogger.isInitialized()) {
            KimbleGameStateLogger.logBoard(game.getBoard(), game.getTeams());
            KimbleGameStateLogger.logStartValues(startValues);
            KimbleGameStateLogger.logContinueTurnValues(continueTurnValues);
            KimbleGameStateLogger.logGameStart(gameStart);
        }
        // *********************************************************************

        currentTurn = game.getNextTurn();
        for (IPlayer player : players) {
            if (player.getMyTeam().equals(game.getTeamInTurn())) {
                currentPlayer = player;
                break;
            }
        }
    }

    @Override
    public List<Map<Integer, Integer>> getStartingDieRolls() {
        return startingDieRolls;
    }

    @Override
    public Team getStartingTeam() {
        return getGame().getTeam(startingTeamIndex);
    }

    @Override
    public void executeMove() {

        if (game.isGameOver()) {
            if (DEBUG) {
                System.out.println("Game is over!");
            }
            return;
        }

        if (DEBUG) {
            System.out.println("Team in turn: " + game.getTeamInTurn().getId());
            System.out.println("Rolled: " + currentTurn.getDieRoll());
        }

        if (currentTurn.getMoves().isEmpty()) {
            if (DEBUG) {
                System.out.println("No possible moves...");
            }

            // *********************************************************************
            if (KimbleGameStateLogger.isInitialized()) {
                KimbleGameStateLogger.logSkip(game.getTurnCount(), game.getTeamInTurn().getId(), currentTurn.getDieRoll(), false, "not possible");
            }
            // *********************************************************************
            moveMessage = "not possible";
            selectedMove = null;
            game.executeNoMove();
        } else {
            if (currentPlayer.isAIPlayer()) {

                // selectMove logs this output...
                int selection = ((KimbleAI) currentPlayer).selectMove(currentTurn, game.getTeams());

                if (selection >= 0) {

                    // *********************************************************************
                    if (KimbleGameStateLogger.isInitialized()) {
                        KimbleGameStateLogger.logMove(game.getTurnCount(), game.getTeamInTurn().getId(), currentTurn.getDieRoll(), currentTurn.getMove(selection));
                    }
                    // *********************************************************************
                    moveMessage = "moving";
                    selectedMove = currentTurn.getMove(selection);
                    game.executeMove(selection);
                } else if (selection == -1) {

                    // *********************************************************************
                    if (KimbleGameStateLogger.isInitialized()) {
                        KimbleGameStateLogger.logSkip(game.getTurnCount(), game.getTeamInTurn().getId(), currentTurn.getDieRoll(), true, "pass");
                    }
                    // *********************************************************************
                    moveMessage = "pass";
                    selectedMove = null;
                    game.executeNoMove();
                } else if (selection == -2) {
                    game.disqualifyPlayer();
                } else {
                    throw new RuntimeException("Move selection was < -2. What?");
                }
            } else {
                // TODO: not yet possible to make "pass" move (can't use selection -1)
                game.executeMove(((KimblePlayer) currentPlayer).getSelectedMove());
//                throw new UnsupportedOperationException("Human players not yet supported!");
            }
        }
        if (DEBUG) {
            System.out.println(game);
            System.out.println("--------------------------------------------------");
            System.out.println("");
        }
        // check if game just ended
        if (game.isGameOver()) {
            if (DEBUG) {
                System.out.println("");
                System.out.println("Game finished!");
                System.out.println("Finishing order: ");
                for (Team team : game.getFinishedTeams()) {
                    System.out.println(team.getId());
                }
                System.out.println("");
            }

            winner = game.getFinishedTeams().get(0).getId();

            // *********************************************************************
            if (KimbleGameStateLogger.isInitialized()) {
                for (Team team : game.getFinishedTeams()) {
                    KimbleGameStateLogger.logTeamFinished(team.getId(), getTurnCount());
                }
                KimbleGameStateLogger.logWinner(winner);
            }
            // *********************************************************************
        } else {
            currentTurn = game.getNextTurn();
        }

        currentPlayer = players.get(game.getTeamInTurn().getId());
    }

    @Override
    public int getWinner() {
        return winner;
    }

    public Game getGame() {
        return game;
    }

    public Set<Integer> getContinueTurnValues() {
        return continueTurnValues;
    }

    public int getNumberOfPieces() {
        return numberOfPieces;
    }

    public List<IPlayer> getPlayers() {
        return players;
    }

    public int getSquaresFromStartToStart() {
        return sideLength;
    }

    public Set<Integer> getStartValues() {
        return startValues;
    }

    @Override
    public Turn getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public boolean isGameOver() {
        return getGame().isGameOver();
    }

    @Override
    public Team getNextTeamInTurn() {
        return getGame().getTeamInTurn();
    }

    @Override
    public boolean isAutoPlayer() {
        return currentPlayer.isAIPlayer();
    }

    @Override
    public int getDieRoll() {
        return getCurrentTurn().getDieRoll();
    }

    @Override
    public Board getBoard() {
        return getGame().getBoard();
    }

    @Override
    public List<Team> getTeams() {
        return getGame().getTeams();
    }

    @Override
    public Team getTeam(int teamID) {
        return getGame().getTeam(teamID);
    }

    @Override
    public String getMoveMessage() {
        return moveMessage;
    }

    @Override
    public Move getSelectedMove() {
        return selectedMove;
    }

    @Override
    public List<Team> getFinishedTeams() {
        return getGame().getFinishedTeams();
    }

    @Override
    public int getTurnCount() {
        return getGame().getTurnCount();
    }

    @Override
    public boolean isFinished(int teamID) {
        return getGame().isFinished(teamID);
    }

    @Override
    public boolean isDisqualified(int teamID) {
        return getGame().isDisqualified(teamID);
    }

}
