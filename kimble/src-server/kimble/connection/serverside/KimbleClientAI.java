/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.serverside;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import kimble.connection.messages.MoveMessage;
import kimble.connection.messages.ReceiveMessage;
import kimble.connection.messages.SendMessage;
import kimble.logic.Team;
import kimble.logic.Turn;
import kimble.logic.player.KimbleAI;

/**
 *
 * @author Christoffer
 */
public class KimbleClientAI extends KimbleAI {

    private final int id;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public KimbleClientAI(int id, String name) throws IOException {
        super(name);
        this.id = id;
    }

    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
        this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
    }

    public void closeSocket() throws IOException {
        this.socket.close();
    }

    public ReceiveMessage receiveMessage() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }
        JsonObject jsonObject = new JsonParser().parse(line).getAsJsonObject();
        ReceiveMessage receiveMessage = new ReceiveMessage();
        receiveMessage.setType(jsonObject.get("type").getAsString());
        receiveMessage.setData(jsonObject.get("data"));
        return receiveMessage;
    }

    private void sendMessage(String message) {
        writer.println(message);
    }

    public void send(SendMessage message) {
        sendMessage(message.toJson());
    }

    public final int getID() {
        return id;
    }

    @Override
    public boolean isAIPlayer() {
        return true;
    }

    @Override
    public int selectMove(Turn turn, List<Team> teams) {

        MoveMessage moveMessage = new MoveMessage(turn, teams);
        send(moveMessage);

        try {
            ReceiveMessage receiveMessage = receiveMessage();
            if (receiveMessage != null) {
                String type = receiveMessage.getType();
                if (type.equals("selectedMove")) {
                    int selectedMove = receiveMessage.getData().getAsJsonObject().get("selectedMove").getAsInt();
                    return selectedMove;
                } else if (type.equals("ping")) {
                    return -1;
                }
            } else {
                System.err.println("!!! Did not receive message from client !!!\n"
                        + "  - client id: " + id + ".");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Exception occurred in the server!! This should not happen!!"
                    + "PLEASE send this stacktrace to the developers!!", ex);
        }

        // tell game an errenous answer or no answer was received
        return -2;
    }
}
