package kimble.connection.logger.entries;

/**
 *
 * @author Christoffer
 */
public class SkipEntry extends AbstractEntry {

    public final boolean optional;

    public SkipEntry(int turnCount, int teamID, int dieRoll, boolean optional) {
        super(turnCount, teamID, dieRoll);
        this.optional = optional;
    }
}
