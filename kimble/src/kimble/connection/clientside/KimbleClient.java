/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.clientside;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import kimble.connection.messages.MoveSelectedMessage;
import kimble.connection.messages.PingMessage;
import kimble.connection.messages.ReceiveMessage;
import kimble.connection.messages.SendMessage;

/**
 *
 * @author Christoffer
 */
public abstract class KimbleClient {

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private boolean running;

    private String receiveMessageType;
    private int dieRoll;
    private List<MoveInfo> availableMoves;
    private List<PieceInfo> pieceInfoList;

    public KimbleClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);

        this.running = true;

        loop();
    }

    private void loop() throws IOException {
        preLoop();
        while (running) {
            ReceiveMessage receiveMessage = receiveMessage();
            if (getReceiveMessageType().equals("disconnect")) {
                System.out.println("Got a disconnect message!");
                sendMessage(new PingMessage());
                running = false;
            }
            parseMessageData(receiveMessage);
            duringLoop();
        }
        postLoop();
    }

    /**
     * This method doesn't parse the message data. It only receives the message
     * and reads the message type.
     *
     * @return
     * @throws IOException
     */
    private ReceiveMessage receiveMessage() throws IOException {
        JsonObject jsonObject = new JsonParser().parse(reader.readLine()).getAsJsonObject();
        ReceiveMessage receiveMessage = new ReceiveMessage();
        receiveMessage.setType(jsonObject.get("type").getAsString());
        receiveMessage.setData(jsonObject.get("data"));
        receiveMessageType = receiveMessage.getType();
        return receiveMessage;
    }

    private void parseMessageData(ReceiveMessage message) {
        JsonElement dataElement = message.getData();
        if (message.getType().equals("moves")) {
            parseDieRoll(dataElement);
            parseAvailableMoves(dataElement);
            parsePieceInfo(dataElement);
        }
    }

    private void parseDieRoll(JsonElement dataElement) {
        dieRoll = dataElement.getAsJsonObject().get("dieRoll").getAsInt();
    }

    private void parseAvailableMoves(JsonElement dataElement) throws UnsupportedOperationException {
        JsonArray availableMoveElement = dataElement.getAsJsonObject().get("availableMoves").getAsJsonArray();
        availableMoves = new ArrayList<>();
        for (int i = 0; i < availableMoveElement.size(); i++) {
            JsonElement moveIdElement = availableMoveElement.get(i).getAsJsonObject().get("moveId");
            JsonElement pieceIdElement = availableMoveElement.get(i).getAsJsonObject().get("pieceId");
            JsonElement isHomeElement = availableMoveElement.get(i).getAsJsonObject().get("isHome");
            JsonElement startSquareIdElement = availableMoveElement.get(i).getAsJsonObject().get("startSquareId");
            JsonElement destSqureIdElement = availableMoveElement.get(i).getAsJsonObject().get("destSquareId");

            Integer moveId;
            Integer pieceId;
            Boolean isHome;
            Integer startSquareId;
            Integer destSquareId;
            if (moveIdElement != null) {
                moveId = moveIdElement.getAsInt();
            } else {
                throw new UnsupportedOperationException("Cant't send a MoveInfo without 'moveId'");
            }
            if (pieceIdElement != null) {
                pieceId = pieceIdElement.getAsInt();
            } else {
                throw new UnsupportedOperationException("Cant't send a MoveInfo without 'pieceId'");
            }
            if (isHomeElement != null) {
                isHome = isHomeElement.getAsBoolean();
            } else {
                isHome = false;
            }
            if (startSquareIdElement != null) {
                startSquareId = startSquareIdElement.getAsInt();
            } else {
                startSquareId = null;
            }
            if (destSqureIdElement != null) {
                destSquareId = destSqureIdElement.getAsInt();
            } else {
                throw new UnsupportedOperationException("Can't send a MoveInfo without 'destSquareId'");
            }
            MoveInfo moveInfo = new MoveInfo(moveId, pieceId, isHome, startSquareId, destSquareId);
            availableMoves.add(moveInfo);
        }
    }

    private void parsePieceInfo(JsonElement dataElement) {
        JsonArray pieceInfoArray = dataElement.getAsJsonObject().get("pieces").getAsJsonArray();
        pieceInfoList = new ArrayList<>();
        for (int i = 0; i < pieceInfoArray.size(); i++) {
            JsonElement teamIdElement = pieceInfoArray.get(i).getAsJsonObject().get("teamId");
            JsonElement pieceIdElement = pieceInfoArray.get(i).getAsJsonObject().get("pieceId");
            JsonElement isHomeElement = pieceInfoArray.get(i).getAsJsonObject().get("isHome");
            JsonElement squareIdElement = pieceInfoArray.get(i).getAsJsonObject().get("squareId");

            Integer teamId;
            Integer pieceId;
            Boolean isHome;
            Integer squareId;
            if (teamIdElement != null) {
                teamId = teamIdElement.getAsInt();
            } else {
                throw new UnsupportedOperationException("Cant't send a PieceInfo without 'teamId'");
            }
            if (pieceIdElement != null) {
                pieceId = pieceIdElement.getAsInt();
            } else {
                throw new UnsupportedOperationException("Cant't send a PieceInfo without 'pieceId'");
            }
            if (isHomeElement != null) {
                isHome = isHomeElement.getAsBoolean();
            } else {
                isHome = false;
            }
            if (squareIdElement != null) {
                squareId = squareIdElement.getAsInt();
            } else {
                squareId = null;
            }
            PieceInfo pieceInfo = new PieceInfo(teamId, pieceId, isHome, squareId);
            pieceInfoList.add(pieceInfo);
        }
    }

    /**
     * The message type of the received message.
     *
     * @return
     */
    public String getReceiveMessageType() {
        return receiveMessageType;
    }

    /**
     * Gives the die roll for this turn.
     *
     * @return
     */
    public int getDieRoll() {
        return dieRoll;
    }

    /**
     * Gives this teams available moves after a new message has been received.
     *
     * @return
     */
    public List<MoveInfo> getAvailableMoves() {
        return availableMoves;
    }

    /**
     * Gives all the pieces on the board.
     *
     * @return
     */
    public List<PieceInfo> getPieceInfo() {
        return pieceInfoList;
    }

    private void sendMessage(String message) {
        writer.println(message);
    }

    public void sendMessage(SendMessage message) {
        sendMessage(message.toJson());
    }

    public void sendMove(MoveInfo move) {
        sendMessage(new MoveSelectedMessage(move));
    }

    public abstract void preLoop();

    public abstract void duringLoop();

    public abstract void postLoop();

}
