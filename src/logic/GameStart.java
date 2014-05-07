package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Lasse Lybeck
 */
public class GameStart {

    private final int startingTeamIndex;
    private final List<Map<Integer, Integer>> rolls;

    GameStart(List<Team> teams, Die die) {
        rolls = new ArrayList<>();
        List<Integer> teamsIn = new ArrayList<>(teams.size());
        for (int i = 0; i < teams.size(); i++) {
            teamsIn.add(teams.get(i).getId());
        }
        while (teamsIn.size() > 1) {
            Map<Integer, Integer> map = new TreeMap<>();
            rolls.add(map);
            for (int i = 0; i < teamsIn.size(); i++) {
                map.put(teamsIn.get(i), die.roll());
            }
            teamsIn.clear();
            int max = Collections.max(map.values());
            for (Integer teamId : map.keySet()) {
                if (map.get(teamId) == max) {
                    teamsIn.add(teamId);
                }
            }
        }
        int startingId = teamsIn.get(0);
        int startTeam = -1;
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getId() == startingId) {
                startTeam = i;
            }
        }
        this.startingTeamIndex = startTeam;
    }

    public int getStartingTeamIndex() {
        return startingTeamIndex;
    }

    public List<Map<Integer, Integer>> getRolls() {
        return rolls;
    }
}
