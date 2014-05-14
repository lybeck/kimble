/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.logger;

/**
 *
 * @author Christoffer
 */
public abstract class LogEntry {

    private final Integer teamID;
    private final Integer dieRoll;

    public LogEntry(Integer teamID, Integer dieRoll) {
        this.teamID = teamID;
        this.dieRoll = dieRoll;
    }

    public final Integer getTeamID() {
        return teamID;
    }

    public final Integer getDieRoll() {
        return dieRoll;
    }

}
