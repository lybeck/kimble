package socketclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Lasse Lybeck
 */
public class SocketClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 5391);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String msg = reader.readLine();
            if (msg.equals("end")) {
                System.out.println("Got end message!");
                writer.println("Bye server!");
                break;
            }
            System.out.println("sleeping..");
            Thread.sleep(50);
            writer.println("Received message: '" + msg + "'");
        }
    }

}
