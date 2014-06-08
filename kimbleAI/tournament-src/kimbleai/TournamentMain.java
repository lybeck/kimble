package kimbleai;

import starters.Main;
import java.io.IOException;

/**
 *
 * @author Christoffer
 */
public class TournamentMain {

    public static void main(String[] args) throws IOException {
        if (args.length >= 2) {
            Main.getAI(args[0], Integer.parseInt(args[1])).run();
        } else {
            Main.getAI(Main.HOST_ADDRESS, Main.PORT).run();
        }
    }
}
