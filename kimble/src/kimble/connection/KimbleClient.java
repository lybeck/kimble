/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection;

import java.net.Socket;

/**
 *
 * @author Christoffer
 */
public class KimbleClient {

    private final int id;

    private Socket socket;

    public KimbleClient(int id) {
        this.id = id;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getID() {
        return id;
    }

}
