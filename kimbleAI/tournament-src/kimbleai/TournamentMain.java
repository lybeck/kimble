package kimbleai;

import yourpackage.Main;
import java.io.IOException;

/**
 *
 * @author Christoffer
 */
public class TournamentMain {

    public static void main(String[] args) throws IOException {
        if (args.length >= 2) {
            Main.createAI(args[0], Integer.parseInt(args[1])).run();
        } else {
            Main.createAI(Main.HOST_ADDRESS, Main.PORT).run();
        }
    }
}
