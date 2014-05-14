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

/**
 *
 * @author Christoffer
 */
public class KimbleGameStateLogger {

    private static FileWriter writer;
    private static File logFile;

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

        writer.append("TeamID" + "\t" + "DieRoll" + "\t" + "PieceID" + "\t" + "StartSquareID" + "\t" + "DestSquareID\n");
    }

    public static void logSkip(Integer teamId, Integer dieRoll, String tag) {
        try {
            String s = "SKIP" + "\t" + teamId + "\t" + dieRoll + "\t" + tag;
            writer.append(s + "\n");
        } catch (IOException ex) {
            Logger.getLogger(KimbleGameStateLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void logMove(Integer teamId, Integer dieRoll, Integer pieceId, Integer startSquareId, Integer destSquareId) {
        try {
            String s = "MOVE" + "\t" + teamId + "\t" + dieRoll + "\t" + pieceId + "\t" + startSquareId + "\t" + destSquareId;
            writer.append(s + "\n");
        } catch (IOException ex) {
            Logger.getLogger(KimbleGameStateLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void close() throws IOException {
        writer.close();
    }

}
