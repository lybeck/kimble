/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.serverside;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.connection.clientside.StreamRedirecter;
import kimble.logic.Turn;
import kimble.logic.board.Board;
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

        random = new Random();

        startAI();
    }

    private void startAI() throws IOException {
        String dir = "D:\\Programmering\\Java\\Kimble\\kimble\\kimbleAI\\dist\\";
        String name = "KimbleAI.jar";

        ProcessBuilder pb = new ProcessBuilder("java", "-jar", dir + name);
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

    public String receiveMessage() throws IOException {
        return reader.readLine();
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public int getID() {
        return id;
    }

    @Override
    public boolean isAIPlayer() {
        return true;
    }

    private final Random random;

    @Override
    public int selectMove(Turn turn, Board board) {
        System.out.println("KimbleClientAI::selectMove");
        sendMessage("asdasd");
        try {
            System.out.println("receiveMessage() = " + receiveMessage());
        } catch (IOException ex) {
            Logger.getLogger(KimbleClientAI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return random.nextInt(turn.getMoves().size());
    }
}
