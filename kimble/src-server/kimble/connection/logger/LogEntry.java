package kimble.connection.logger;

import kimble.connection.logger.entries.AbstractEntry;
import kimble.connection.logger.entries.SkipEntry;
import kimble.connection.logger.entries.MoveEntry;

/**
 *
 * @author Christoffer
 */
public class LogEntry {

    public enum EntryType {

        MOVE, SKIP
    }

    public EntryType type;
    public Integer turnCount;
    public Integer teamID;
    public Integer dieRoll;
    public Integer pieceID;
    public Integer startSquareID;
    public Integer destSquareID;
    public Boolean home;
    public Boolean optional;

    public LogEntry() {
    }

    public LogEntry(EntryType type, Integer turnCount, Integer teamID, Integer dieRoll, Integer pieceID, Integer startSquareID, Integer destSquareID, Boolean home, Boolean optional) {
        this.type = type;
        this.turnCount = turnCount;
        this.teamID = teamID;
        this.dieRoll = dieRoll;
        this.pieceID = pieceID;
        this.startSquareID = startSquareID;
        this.destSquareID = destSquareID;
        this.home = home;
        this.optional = optional;
    }

    public AbstractEntry getEntry() {
        if (type == EntryType.MOVE) {
            return new MoveEntry(turnCount, teamID, dieRoll, pieceID, startSquareID, destSquareID, home, optional);
        } else if (type == EntryType.SKIP) {
            return new SkipEntry(turnCount, teamID, dieRoll, optional);
        }

        throw new UnsupportedOperationException("No return Entry yet created for this type...");
    }

}
