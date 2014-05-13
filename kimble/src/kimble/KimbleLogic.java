/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble;

import java.util.List;
import java.util.Set;
import static kimble.ServerGame.DEBUG;
import static kimble.ServerGame.NUMBER_OF_PIECES;
import static kimble.ServerGame.SQUARES_FROM_START_TO_START;
import kimble.logic.Constants;
import kimble.logic.Game;
import kimble.logic.GameStart;
import kimble.logic.IPlayer;
import kimble.logic.Team;
import kimble.logic.Turn;
import kimble.logic.player.KimbleAI;

/**
 *
 * @author Christoffer
 */
public class KimbleLogic {

    private final Game game;
    private List<IPlayer> players;
    private int winner;

    private Set<Integer> startValues;
    private Set<Integer> continueTurnValues;
    private int numberOfPieces;
    private int squaresFromStartToStart;

    private Turn currentTurn;
    private Turn previousTurn;

    public KimbleLogic(List<IPlayer> players) {
        this(players, Constants.DEFAULT_START_VALUES, Constants.DEFAULT_CONTINUE_TURN_VALUES, NUMBER_OF_PIECES, SQUARES_FROM_START_TO_START);
    }

    public KimbleLogic(List<IPlayer> players, Set<Integer> startValues, Set<Integer> continueTurnValues, int numberOfPieces, int squaresFromStartToStart) {
        this.players = players;
        this.startValues = startValues;
        this.continueTurnValues = continueTurnValues;
        this.numberOfPieces = numberOfPieces;
        this.squaresFromStartToStart = squaresFromStartToStart;

        this.game = new Game(startValues, continueTurnValues, players.size(), numberOfPieces, squaresFromStartToStart);

        setup();
    }

    private void setup() {

        for (int i = 0; i < players.size(); i++) {
            IPlayer player = players.get(i);
            if (player.isAIPlayer()) {
                ((KimbleAI) player).setMyTeam(game.getTeam(i));
            } else {
                throw new UnsupportedOperationException("Human players not yet supported!");
            }
        }

        GameStart gameStart = game.startGame();

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

        currentTurn = game.getNextTurn();
    }

    public void executeMove() {

        if (game.isGameOver()) {
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
            game.executeNoMove();
        } else {
            IPlayer player = players.get(game.getTeamInTurn().getId());
            if (player.isAIPlayer()) {
                int selection = ((KimbleAI) player).selectMove(currentTurn, game.getBoard());
                if (selection >= 0) {
                    game.executeMove(selection);
                } else {
                    game.executeNoMove();
                }
            } else {
                throw new UnsupportedOperationException("Human players not yet supported!");
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
        } else {
            previousTurn = currentTurn;
            currentTurn = game.getNextTurn();
        }
    }

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
        return squaresFromStartToStart;
    }

    public Set<Integer> getStartValues() {
        return startValues;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public Turn getPreviousTrun() {
        return previousTurn;
    }

}
