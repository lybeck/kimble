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
public class LogEntrySkip extends LogEntry {

    private final String reason;

    public LogEntrySkip(Integer teamID, Integer dieRoll, String reason) {
        super(teamID, dieRoll);

        this.reason = reason;
    }

    public final String getReason() {
        return reason;
    }

}
