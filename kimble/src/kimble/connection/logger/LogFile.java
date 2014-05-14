/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Christoffer
 */
public class LogFile {

    // - Date
    // - Teams
    // - Moves (entries)
    // - Result
    private final Date date;
    private final List<TeamInfo> teams;
    private Set<Integer> startValues;
    private Set<Integer> continueTurnValues;
    private List<Map<Integer, Integer>> startRolls;
    private Integer startingTeam;
    private final List<LogEntry> moves;
    private final List<Integer> teamFinnishOrder;
    private Integer winner;

    public LogFile() {
        this.date = Calendar.getInstance().getTime();
        this.teams = new ArrayList<>();
        this.moves = new ArrayList<>();
        this.teamFinnishOrder = new ArrayList<>();
    }

    public void addTeam(Integer teamID, String teamName) {
        teams.add(new TeamInfo(teamID, teamName));
    }

    public void setStartValues(Set<Integer> startValues) {
        this.startValues = startValues;
    }

    public void setContinueTurnValues(Set<Integer> continueTurnValues) {
        this.continueTurnValues = continueTurnValues;
    }

    public void setStartRolls(List<Map<Integer, Integer>> startRolls) {
        this.startRolls = startRolls;
    }

    public void setStartingTeam(Integer teamID) {
        this.startingTeam = teamID;
    }

    public void addEntry(LogEntry entry) {
        moves.add(entry);
    }

    public List<LogEntry> getMoves() {
        return moves;
    }

    public List<Integer> getTeamFinnishOrder() {
        return teamFinnishOrder;
    }

    public void addTeamFinnish(Integer teamID) {
        teamFinnishOrder.add(teamID);
    }

    public void setWinner(Integer teamID) {
        this.winner = teamID;
    }

    private class TeamInfo {

        final Integer teamID;
        final String teamName;

        public TeamInfo(Integer teamID, String teamName) {
            this.teamID = teamID;
            this.teamName = teamName;
        }
    }
}
