/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble;

import java.util.List;
import kimble.logic.Team;
import kimble.logic.board.Board;

/**
 *
 * @author Christoffer
 */
public interface KimbleLogicInterface {

    public Board getBoard();

//    public int getStartingTeamIndex();

    public List<Team> getTeams();

    public Team getTeam(int teamID);

    public void executeMove();

    public boolean isGameOver();

    public int getDieRoll();
}
