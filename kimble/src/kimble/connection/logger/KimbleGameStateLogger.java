/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.logger;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import kimble.logic.GameStart;
import kimble.logic.Move;
import kimble.logic.Team;
import kimble.logic.board.Board;

/**
 *
 * @author Christoffer
 */
public class KimbleGameStateLogger {

    private static LogFile logFile;
    private static FileWriter writer;

    private static boolean initialized = false;

    public static void init() throws IOException {
        File logRoot = new File("logs");
        if (!logRoot.exists()) {
            logRoot.mkdir();
        }

        File[] filesInRoot = logRoot.listFiles();

        int fileIndex;
        if (filesInRoot == null) {
            fileIndex = 0;
        } else {
            fileIndex = filesInRoot.length + 1;
        }

        File outputFile = new File(logRoot, "log_" + fileIndex + ".txt");
        writer = new FileWriter(outputFile);

        initialized = true;

        logFile = new LogFile();
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void logBoard(Board board, List<Team> teams) {
        for (int i = 0; i < teams.size(); i++) {
            for (int j = 0; j < board.getGoalSquares(i).size(); j++) {
                logFile.getBoardInfo().addGoalSquare(i, board.getGoalSquare(i, j).getID());
            }
            logFile.getBoardInfo().addStartSquare(i, board.getStartSquare(i).getID());
        }
        for (int i = 0; i < board.getSquares().size(); i++) {
            logFile.getBoardInfo().addSquare(board.getSquare(i).getID());
        }
    }

    public static void logTeam(Integer teamID, String teamName) {
        logFile.addTeam(teamID, teamName);
    }

    public static void logStartValues(Set<Integer> startValues) {
        logFile.setStartValues(startValues);
    }

    public static void logContinueTurnValues(Set<Integer> continueTurnValues) {
        logFile.setContinueTurnValues(continueTurnValues);
    }

    public static void logGameStart(GameStart gameStart) {
        logFile.setStartRolls(gameStart.getRolls());
        logFile.setStartingTeam(gameStart.getStartingTeamIndex());
    }

    public static void logMove(int teamID, int dieRoll, Move move) {
        Integer pieceId = move.getPiece().getId();
        Boolean home = null;
        if (move.getPiece().isHome()) {
            home = move.getPiece().isHome();
        }
        Boolean optional = null;
        if (move.isOptional()) {
            optional = move.isOptional();
        }
        Integer startSquare = null;
        if (move.getPiece().getPosition() != null) {
            startSquare = move.getPiece().getPosition().getID();
        }
        Integer destSquare = move.getDestination().getID();

        logFile.addEntry(new LogEntryMove(teamID, dieRoll, pieceId, home, optional, startSquare, destSquare));
    }

    public static void logSkip(int teamID, int dieRoll, boolean optional, String reason) {
        logFile.addEntry(new LogEntrySkip(teamID, dieRoll, optional, reason));
    }

    public static void logTeamFinnish(int teamID) {
        logFile.addTeamFinnish(teamID);
    }

    public static void logWinner(int teamID) {
        logFile.setWinner(teamID);
    }

    public static void close() throws IOException {
        writer.append(new Gson().toJson(logFile));
        writer.close();
    }

}
