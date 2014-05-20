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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kimble.connection.messages.MoveSelectedMessage;
import kimble.connection.messages.PingMessage;
import kimble.connection.messages.ReceiveMessage;
import kimble.connection.messages.SendMessage;

/**
 * An abstract class for implementing a client-side AI. This is the class you want if you are going to run the server
 * with your own AI.
 *
 * @author Christoffer
 */
public abstract class KimbleClient {

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private boolean running;

    private int myTeamId;
    private String receiveMessageType;
    private int dieRoll;
    private List<MoveInfo> availableMoves;
    private List<PieceInfo> pieceInfoList;
    private List<SquareInfo> squares;
    private Map<Integer, SquareInfo> startSquares;
    private Map<Integer, List<SquareInfo>> goalSquares;

    /**
     * Creates a new Client to communicate with the server.
     *
     * @param host - The url to the server.
     * @param port - The port the server is running on.
     * @throws IOException
     */
    public KimbleClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
        this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

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
     * This method doesn't parse the message data. It only receives the message and reads the message type.
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
        } else if (message.getType().equals("gameInit")) {
            parseSquares(dataElement);
        } else if (message.getType().equals("yourTeamId")) {
            parseTeamId(dataElement);
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
            JsonElement isOptionalElement = availableMoveElement.get(i).getAsJsonObject().get("isOptional");
            JsonElement startSquareIdElement = availableMoveElement.get(i).getAsJsonObject().get("startSquareId");
            JsonElement destSqureIdElement = availableMoveElement.get(i).getAsJsonObject().get("destSquareId");

            Integer moveId;
            Integer pieceId;
            Boolean isHome;
            Boolean isOptional;
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
            if (isOptionalElement != null) {
                isOptional = isOptionalElement.getAsBoolean();
            } else {
                isOptional = false;
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
            MoveInfo moveInfo = new MoveInfo(moveId, pieceId, isHome, isOptional, startSquareId, destSquareId);
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

    private void parseSquares(JsonElement dataElement) {
        JsonArray squareElements = dataElement.getAsJsonObject().get("squares").getAsJsonArray();
        squares = new ArrayList<>();
        startSquares = new HashMap<>();
        goalSquares = new HashMap<>();
        for (JsonElement jsonElement : squareElements) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            int squareId = jsonObject.get("squareId").getAsInt();
            JsonElement isStartSquareElement = jsonObject.get("isStartSquare");
            JsonElement isGoalSquareElement = jsonObject.get("isGoalSquare");
            boolean isStartSquare;
            boolean isGoalSquare;
            if (isStartSquareElement != null) {
                isStartSquare = isStartSquareElement.getAsBoolean();
            } else {
                isStartSquare = false;
            }
            if (isGoalSquareElement != null) {
                isGoalSquare = isGoalSquareElement.getAsBoolean();
            } else {
                isGoalSquare = false;
            }
            if (!isStartSquare && !isGoalSquare) {
                squares.add(new SquareInfo(squareId));
            } else {
                int teamId = jsonObject.get("teamId").getAsInt();
                SquareInfo squareInfo = new SquareInfo(squareId, isStartSquare, isGoalSquare, teamId);
                if (!isGoalSquare) {
                    squares.add(squareInfo);
                } else {
                    if (!goalSquares.containsKey(teamId)) {
                        goalSquares.put(teamId, new ArrayList<>());
                    }
                    goalSquares.get(teamId).add(squareInfo);
                }
                if (isStartSquare) {
                    startSquares.put(teamId, squareInfo);
                }
            }
        }
    }

    private void parseTeamId(JsonElement dataElement) {
        myTeamId = dataElement.getAsJsonObject().get("teamId").getAsInt();
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

    /**
     * Gives the square info of all the squares on the board, excluding the goal squares.
     *
     * @return A list of squares.
     */
    public List<SquareInfo> getSquareInfo() {
        return squares;
    }

    /**
     * Gives the start square of the specific team.
     *
     * @param teamId Team id.
     * @return The team's start square.
     */
    public SquareInfo getStartSquare(int teamId) {
        return startSquares.get(teamId);
    }

    /**
     * Gives the goal squares of the specific team.
     *
     * @param teamId Team id.
     * @return A list of goal squares.
     */
    public List<SquareInfo> getGoalSquares(int teamId) {
        return goalSquares.get(teamId);
    }

    /**
     * Gives the id of the team you are playing with.
     *
     * @return Your team id.
     */
    public int getMyTeamId() {
        return myTeamId;
    }

    private void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * This method is used to send answers to the server.
     *
     * @param message
     */
    public void sendMessage(SendMessage message) {
        sendMessage(message.toJson());
    }

    /**
     * Sends a 'new MoveSelectedMessage(move)' to the server with 'sendMessage(SendMessage message)' method.
     *
     * @param move
     */
    public void sendMove(MoveInfo move) {
        sendMessage(new MoveSelectedMessage(move));
    }

    /**
     * Sends a 'new PingMessage()' to the server through the 'sendMessage(SendMessage message)' method.
     */
    public void sendPing() {
        sendMessage(new PingMessage());
    }

    /**
     * This method is run before the loop. Recommended for initialization stuff. Because the constructor starts the loop
     * your own constructor (extending this class' constructor) will be run at the end of the loop.
     */
    public abstract void preLoop();

    /**
     * This method is run once every loop after the client has received the message from the server.
     */
    public abstract void duringLoop();

    /**
     * This method is run when the loop has ended. Suitable for clean up code (.close() on streams etc).
     */
    public abstract void postLoop();
}