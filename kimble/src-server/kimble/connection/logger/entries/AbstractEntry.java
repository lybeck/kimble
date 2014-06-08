package kimble.connection.logger.entries;

/**
 *
 * @author Christoffer
 */
public abstract class AbstractEntry {

    public final int turnCount;
    public final int teamID;
    public final int dieRoll;

    public AbstractEntry(int turnCount, int teamID, int dieRoll) {
        this.turnCount = turnCount;
        this.teamID = teamID;
        this.dieRoll = dieRoll;
    }
}
