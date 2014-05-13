/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.messages;

/**
 *
 * @author Christoffer
 */
public class Message {

    private String type;
    private byte[] data;

    public Message(String type) {
        this(type, null);
    }

    public Message(String type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    public void send() {

    }

    public String getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

}
