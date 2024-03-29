package kimble.connection.logger;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import kimble.connection.logger.LogEntry.EntryType;
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

        logFile = new LogFile(Calendar.getInstance().getTime());
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void logTeam(Integer teamID, String teamName, Integer numberOfPieces) {
        logFile.addTeam(teamID, teamName, numberOfPieces);
    }

    public static void logBoard(Board board, List<Team> teams) {
        for (int i = 0; i < teams.size(); i++) {
            for (int j = 0; j < board.getGoalSquares(i).size(); j++) {
                logFile.getBoard().addGoalSquare(i, board.getGoalSquare(i, j).getID());
            }
            logFile.getBoard().addStartSquare(i, board.getStartSquare(i).getID());
        }
        for (int i = 0; i < board.getSquares().size(); i++) {
            logFile.getBoard().addSquare(board.getSquare(i).getID());
        }
        logFile.getBoard().setSideLength(board.getSideLength());
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

    public static void logMove(int turnCount, int teamID, int dieRoll, Move move) {
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

        logFile.addEntry(new LogEntry(EntryType.MOVE, turnCount, teamID, dieRoll, pieceId, startSquare, destSquare, home, optional));
    }

    public static void logSkip(int turnCount, int teamID, int dieRoll, boolean optional, String reason) {
        logFile.addEntry(new LogEntry(EntryType.SKIP, turnCount, teamID, dieRoll, null, null, null, null, optional));
    }

    public static void logTeamFinished(int teamID, int turnCount) {
        logFile.addTeamFinished(teamID, turnCount);
    }

    public static void logTeamDisqualified(int teamID, int turnCount) {
        logFile.addTeamDisqualified(teamID, turnCount);
    }

    public static void logWinner(int teamID) {
        logFile.setWinner(teamID);
    }

    public static void close() throws IOException {
        writer.append(new Gson().toJson(logFile));
        writer.close();
    }

}
