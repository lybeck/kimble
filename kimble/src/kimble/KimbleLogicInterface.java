/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble;

import java.util.List;
import java.util.Map;
import kimble.logic.Team;
import kimble.logic.board.Board;

/**
 *
 * @author Christoffer
 */
public interface KimbleLogicInterface {

    public Board getBoard();

    public List<Map<Integer, Integer>> getStartingDieRolls();

    public Team getStartingTeam();

    public List<Team> getTeams();

    public Team getTeam(int teamID);

    public void executeMove();

    public boolean isGameOver();

    public Team getNextTeamInTurn();

    public int getDieRoll();
}
