/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.clientside;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christoffer
 */
public class StreamRedirecter implements Runnable {

    private final BufferedReader bufferedReader;
    private final PrintStream destination;

    public StreamRedirecter(InputStream inputStream, PrintStream destination) throws UnsupportedEncodingException {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        this.destination = destination;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                destination.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(StreamRedirecter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
