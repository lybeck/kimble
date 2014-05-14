/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.serverside;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.connection.clientside.StreamRedirecter;
import kimble.connection.messages.MoveMessage;
import kimble.connection.messages.ReceiveMessage;
import kimble.connection.messages.SendMessage;
import kimble.logic.Game;
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

    public KimbleClientAI(int id) throws IOException {
        this.id = id;
    }

    /**
     * Starts a new process at in the desired directory withe the name specified
     * with jarName. It also sends a "dummy" argument, to fool the client not to
     * start a new server.
     *
     * @param dir
     * @param jarName
     * @throws IOException
     */
    public void startAI(String dir, String jarName) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", dir + jarName, "serverstart");
        pb.directory(new File(dir));
        Process p = pb.start();
        new Thread(new StreamRedirecter(p.getInputStream(), System.out)).start();
        new Thread(new StreamRedirecter(p.getErrorStream(), System.err)).start();
    }

    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void closeSocket() throws IOException {
        this.socket.close();
    }

    public ReceiveMessage receiveMessage() throws IOException {
        JsonObject jsonObject = new JsonParser().parse(reader.readLine()).getAsJsonObject();
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

    public int getID() {
        return id;
    }

    @Override
    public boolean isAIPlayer() {
        return true;
    }

    @Override
    public int selectMove(Turn turn, Game game) {

        send(new MoveMessage(turn, game));

        try {
            ReceiveMessage receiveMessage = receiveMessage();
            String type = receiveMessage.getType();
            if (type.equals("selectedMove")) {
                return receiveMessage.getData().getAsJsonObject().get("selectedMove").getAsInt();
            } else if (type.equals("ping")) {
                return -1;
            }
        } catch (IOException ex) {
            Logger.getLogger(KimbleClientAI.class.getName()).log(Level.SEVERE, null, ex);
        }

        throw new RuntimeException("Did not receive proper message..");
    }
}
