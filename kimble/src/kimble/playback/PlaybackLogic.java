/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.playback;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import kimble.connection.logger.LogEntry;
import kimble.connection.logger.LogEntry.EntryType;
import kimble.connection.logger.LogFile;
import kimble.logic.Team;
import kimble.logic.board.Board;

/**
 *
 * @author Christoffer
 */
public class PlaybackLogic {

    public PlaybackLogic(LogFile log) {

    }

    public static void main(String[] args) throws FileNotFoundException {

        LogFile log = new Gson().fromJson(new BufferedReader(new FileReader(new File("logs/log_7.txt"))), LogFile.class);

        System.out.println(log);

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < log.getTeams().size(); i++) {
            int teamID = log.getTeams().get(i).teamID;
            Team team = new Team(teamID, log.getBoard().teamToStartSquares.get(teamID));
            teams.add(team);
        }
        
        Board board = new Board(teams.size(), teams.get(0).getPieces().size(), 8);

        for (LogEntry entry : log.getEntries()) {
            if (entry.type == EntryType.MOVE) {
                System.out.println("Team " + entry.getEntry().teamID + " rolled " + entry.getEntry().dieRoll);
            }
        }

        System.out.println(log.getEntries().get(0).type);

        new PlaybackLogic(null);
    }

}
