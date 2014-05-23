/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.playback;

import java.util.List;
import kimble.logic.Team;
import kimble.logic.Turn;
import kimble.logic.player.KimbleAI;

/**
 *
 * @author Christoffer
 */
public class PlaybackPlayer extends KimbleAI {

    private final int id;

    public PlaybackPlayer(int id, String teamName) {
        super(teamName);

        this.id = id;
    }

    @Override
    public int selectMove(Turn turn, List<Team> teams) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
