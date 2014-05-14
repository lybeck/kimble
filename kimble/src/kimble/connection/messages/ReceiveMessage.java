package kimble.connection.messages;

import com.google.gson.JsonElement;

/**
 *
 * @author Lasse Lybeck
 */
public class ReceiveMessage {

    private String type;
    private JsonElement data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

}
