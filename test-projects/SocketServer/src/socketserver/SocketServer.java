package socketserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lasse Lybeck
 */
public class SocketServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

//        String dir = "/home/lasse/NetBeansProjects/SocketClient/dist/";
        String dir = "D:\\Programmering\\Java\\Kimble\\kimble\\test-projects\\SocketClient\\dist\\";
        String filename = "SocketClient.jar";
        int players = 2;

        ServerSocket server = new ServerSocket(5391);
        List<Client> clients = new ArrayList<>();

        for (int i = 0; i < players; i++) {
            Client client = new Client(dir, filename);
            Socket socket = server.accept();
            socket.setSoTimeout(500);
            client.setSocket(socket);
            clients.add(client);
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < clients.size(); j++) {
                Client client = clients.get(j);
                System.out.println("Client " + j);
                String sendmsg = "Test message, " + i + ", " + j + ".";
                System.out.println("Sending message:");
                System.out.println(sendmsg);
                client.sendMessage(sendmsg);
                String receiveMessage = client.receiveMessage();
                System.out.println("Client returned:");
                System.out.println(receiveMessage);
                System.out.println("-----------------------------------------");
            }
        }
        
        for (Client client : clients) {
            client.sendMessage("end");
        }
    }

    static class Client {

        PrintWriter writer;
        BufferedReader reader;
        Socket socket;

        public Client(String dir, String filename) throws IOException {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", dir + filename);
            pb.directory(new File(dir));
            Process p = pb.start();
            Thread thread = new Thread(new StreamRedirect(p.getInputStream()));
            thread.start();
        }

        String receiveMessage() throws IOException {
            return reader.readLine();
        }

        void sendMessage(String msg) {
            writer.println(msg);
        }

        public void setSocket(Socket socket) throws IOException {
            this.socket = socket;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        }
    }

    private static class StreamRedirect implements Runnable {

        private final BufferedReader reader;

        public StreamRedirect(InputStream inputStream) {
            reader = new BufferedReader(new InputStreamReader(inputStream));
        }

        @Override
        public void run() {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    System.out.println("[LOG] " + line);
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
