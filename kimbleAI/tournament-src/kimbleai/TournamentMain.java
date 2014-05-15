/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimbleai;

import yourpackage.Main;
import java.io.IOException;

/**
 *
 * @author Christoffer
 */
public class TournamentMain {

    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            Main.startClient(args[0], Integer.parseInt(args[1]));
        } else {
            Main.startClient(Main.HOST_ADDRESS, Main.PORT);
        }
    }
}
