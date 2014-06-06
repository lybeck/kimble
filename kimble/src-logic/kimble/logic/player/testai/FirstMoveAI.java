/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.logic.player.testai;

import java.util.List;
import kimble.logic.Move;
import kimble.logic.Team;
import kimble.logic.Turn;
import kimble.logic.player.KimbleAI;

/**
 *
 * @author Christoffer
 */
public class FirstMoveAI extends KimbleAI {

    public FirstMoveAI(String teamName) {
        super(teamName);
    }

    @Override
    public int selectMove(Turn turn, List<Team> teams) {
        List<Move> moves = turn.getMoves();

        if (moves.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < moves.size(); i++) {
            if (!moves.get(i).isOptional()) {
                return i;
            }
        }
        return -1;
    }
}
