/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.serverside;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.connection.messages.MoveMessage;

/**
 *
 * @author Christoffer
 */
public class KimbleGameStateLogger {

    private static FileWriter writer;
    private static File logFile;

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

        logFile = new File(logRoot, "log_" + fileIndex + ".txt");
        writer = new FileWriter(logFile);

        writer.append("TAG" + "\t" + "TeamID" + "\t" + "DieRoll" + "\t" + "PieceID" + "\t" + "StartID" + "\t" + "DestID\n");

        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void logMove(int teamID, int dieRoll) {
        String s = "[move]" + "\t" + teamID + "\t" + dieRoll + "\t";
        try {
            writer.append(s);
        } catch (IOException ex) {
            Logger.getLogger(KimbleGameStateLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void logMoveMessage(MoveMessage message, int selectedMove) {
        Integer pieceId = message.getPieceID(selectedMove);
        Integer startSquare = message.getStartSquareID(selectedMove);
        Integer destSquare = message.getDestSquareID(selectedMove);

        String s = pieceId + "\t" + startSquare + "\t" + destSquare + "\n";
        try {
            writer.append(s);
        } catch (IOException ex) {
            Logger.getLogger(KimbleGameStateLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void logSkip(int teamID, int dieRoll, String reason) {
        String s = "[skip]" + "\t" + teamID + "\t" + dieRoll + "\t\t\t\t" + reason + "\n";
        try {
            writer.append(s);
        } catch (IOException ex) {
            Logger.getLogger(KimbleGameStateLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void close() throws IOException {
        writer.close();
    }

}
