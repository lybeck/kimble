/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import kimble.logic.IPlayer;

/**
 *
 * @author Christoffer
 */
public class KimbleServer extends JFrame implements Runnable {
    
    private JLabel serverStatus;

    public static final int TIME_OUT_MS = 500;

    private final int port;
    private final int numberOfPlayers;
    private final List<IPlayer> clients;

    public KimbleServer(int port, int numberOfPlayers) {
        this.port = port;
        this.numberOfPlayers = numberOfPlayers;

        this.clients = new ArrayList<>();

        this.setTitle("Kimble Server");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setSize(new java.awt.Dimension(200, 200));

        serverStatus = new JLabel("Press Button to start Server!");
        this.add(serverStatus, BorderLayout.SOUTH);
        
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                run();
            }
        });
        this.add(startButton, BorderLayout.CENTER);
        
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            for (int i = 0; i < numberOfPlayers; i++) {
                KimbleClientAI client = new KimbleClientAI(i);
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(TIME_OUT_MS);
                client.setSocket(socket);
                clients.add(client);
            }

//            ServerGame serverGame = new ServerGame(true, clients);
            for (int i = 0; i < 10; i++) {
                for (IPlayer ai : clients) {
                    KimbleClientAI kai = (KimbleClientAI) ai;
                    kai.sendMessage("hello: " + i);
                    System.out.println("Client_" + kai.getID() + ": " + kai.receiveMessage());
                }
            }
            
            serverStatus.setText("Done!");

        } catch (IOException ex) {
            Logger.getLogger(KimbleServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
