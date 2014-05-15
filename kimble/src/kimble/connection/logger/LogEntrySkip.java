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
    private final Boolean optional;

    public LogEntrySkip(Integer teamID, Integer dieRoll, Boolean optional, String reason) {
        super(teamID, dieRoll);

        this.optional = optional;
        this.reason = reason;
    }

    public final Boolean isOptional() {
        return optional;
    }

    public final String getReason() {
        return reason;
    }

}
