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
