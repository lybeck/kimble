package kimble.connection.messages;

/**
 *
 * @author Lasse Lybeck
 */
public class YourTeamIdMessage extends SendMessage {

    private final int teamId;

    public YourTeamIdMessage(int teamId) {
        this.teamId = teamId;
    }
    
    @Override
    protected String getType() {
        return "yourTeamId";
    }
}
