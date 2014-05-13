package kimble.connection.messages;

/**
 *
 * @author Lasse Lybeck
 */
class MessageWrapper {

    public final String type;
    public final Object data;

    MessageWrapper(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public MessageWrapper(SendMessage sendMessage) {
        this(sendMessage.getType(), sendMessage.getData());
    }
}
