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
import kimble.connection.messages.MoveMessage;
import kimble.logic.GameStart;

/**
 *
 * @author Christoffer
 */
public class KimbleGameStateLogger {

    private static LogFile logFile;
    private static FileWriter writer;
    private static File outputFile;

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

        outputFile = new File(logRoot, "log_" + fileIndex + ".txt");
        writer = new FileWriter(outputFile);

        initialized = true;

        logFile = new LogFile();
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void logTeam(Integer teamID) {
        logFile.addTeam(teamID);
    }

    public static void logGameStart(GameStart gameStart) {
        logFile.setStartRolls(gameStart.getRolls());
        logFile.setStartingTeam(gameStart.getStartingTeamIndex());
    }

    public static void logMove(int teamID, int dieRoll, MoveMessage message, int selectedMove) {
        Integer pieceId = message.getPieceID(selectedMove);
        Boolean isHome = message.getIsHome(selectedMove);
        Boolean isOptional = message.getIsOptional(selectedMove);
        Integer startSquare = message.getStartSquareID(selectedMove);
        Integer destSquare = message.getDestSquareID(selectedMove);

        logFile.addEntry(new LogEntryMove(teamID, dieRoll, pieceId, isHome, isOptional, startSquare, destSquare));
    }

    public static void logSkip(int teamID, int dieRoll, String reason) {
        logFile.addEntry(new LogEntrySkip(teamID, dieRoll, reason));
    }

    public static void logTeamFinnish(int teamID) {
        logFile.addTeamFinnish(teamID);
    }
    
    public static void logWinner(int teamID){
        logFile.setWinner(teamID);
    }

    public static void close() throws IOException {
        writer.append(new Gson().toJson(logFile));
        writer.close();
    }

}
