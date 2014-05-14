package kimbleai.message;

import kimble.connection.messages.SendMessage;

/**
 *
 * @author Lasse Lybeck
 */
public class SelectedMoveMessage extends SendMessage {

    @Override
    protected String getType() {
        return "selectedMove";
    }
}
