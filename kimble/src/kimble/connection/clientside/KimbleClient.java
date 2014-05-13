/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.clientside;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Christoffer
 */
public abstract class KimbleClient {

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private boolean running;
    private String message;

    public KimbleClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);

        this.running = true;

        loop();
    }

    public final void loop() throws IOException {
        preLoop();
        while (running) {
            message = reader.readLine();
            if (message.equals("end")) {
                System.out.println("Got end message!");
                sendMessage("Client disconnected.");
                break;
            }
            duringLoop();
        }
        postLoop();
    }

    public String readMessage() {
        return message;
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public abstract void preLoop();

    public abstract void duringLoop();

    public abstract void postLoop();

}
