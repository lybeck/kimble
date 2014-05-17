/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.playback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.KimbleLogicInterface;
import kimble.connection.logger.LogEntry;
import kimble.connection.logger.LogEntry.EntryType;
import kimble.connection.logger.LogFile;
import kimble.connection.logger.entries.MoveEntry;
import kimble.connection.logger.entries.SkipEntry;
import kimble.logic.Move;
import kimble.logic.Piece;
import kimble.logic.Team;
import kimble.logic.board.Board;
import kimble.logic.exception.IllegalMoveException;

/**
 *
 * @author Christoffer
 */
public class PlaybackLogic implements KimbleLogicInterface {

    private List<Team> teams;
    private Board board;

    private final Iterator<LogEntry> logIterator;

    private int dieRoll;

    private String moveMessage;
    private Move move;

    public PlaybackLogic(LogFile log) {
        initTeams(log);
        initBoard(log);

        logIterator = log.getEntries().iterator();
        getNextMove();
    }

    private void initTeams(LogFile log) {
        teams = new ArrayList<>();
        for (int i = 0; i < log.getTeams().size(); i++) {
            int teamID = log.getTeams().get(i).teamID;
            Team team = new Team(teamID, log.getTeams().get(i).numberOfPieces);
            teams.add(team);
        }
    }

    private void initBoard(LogFile log) {
        int numberOfTeams = teams.size();
        int numberOfPieces = log.getTeams().get(0).numberOfPieces;
        int sideLength = log.getBoard().sideLength;
        board = new Board(numberOfTeams, numberOfPieces, sideLength);
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public List<Map<Integer, Integer>> getStartingDieRolls() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Team getStartingTeam() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public Team getTeam(int teamID) {
        return teams.get(teamID);
    }

    @Override
    public void executeMove() {
        try {
            if (move != null) {
                move.execute();
            }
        } catch (IllegalMoveException ex) {
            Logger.getLogger(PlaybackLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        getNextMove();
    }

    private Team nextTeam;

    private void getNextMove() {
        if (logIterator.hasNext()) {
            LogEntry entry = logIterator.next();

            dieRoll = entry.dieRoll;
            nextTeam = teams.get(entry.teamID);

            if (entry.type == EntryType.MOVE) {
                System.out.println("Team " + entry.getEntry().teamID + " rolled " + entry.getEntry().dieRoll);
                MoveEntry me = (MoveEntry) entry.getEntry();

                Piece piece = teams.get(me.teamID).getPiece(me.pieceID);

                if (me.destSquareID >= board.getSquares().size()) {
                    int res = me.destSquareID % board.getGoalSquares(me.teamID).size();
                    move = new Move(piece, board.getGoalSquare(me.teamID, res), me.optional);
                } else {
                    move = new Move(piece, board.getSquare(me.destSquareID), me.optional);
                }

            } else if (entry.type == EntryType.SKIP) {
                System.out.println("Team " + entry.getEntry().teamID + " rolled " + entry.getEntry().dieRoll
                        + " [Can't move]");
                if (((SkipEntry) entry.getEntry()).optional) {
                    moveMessage = "pass";
                } else {
                    moveMessage = "can't move";
                }
                move = null;
            }
        } else {
            dieRoll = 0;
            move = null;
        }
    }

    private boolean gameOver = false;

    @Override
    public boolean isGameOver() {
        if (logIterator.hasNext() == false && gameOver == false) {
            gameOver = true;
            return false;
        }
        return gameOver;
    }

    @Override
    public int getDieRoll() {
        return dieRoll;
    }

    @Override
    public Team getNextTeamInTurn() {
        return nextTeam;
    }

    @Override
    public String getMoveMessage() {
        return moveMessage;
    }

    @Override
    public Move getSelectedMove() {
        return move;
    }

}
