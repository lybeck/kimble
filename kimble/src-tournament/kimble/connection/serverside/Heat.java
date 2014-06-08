package kimble.connection.serverside;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Lasse Lybeck
 */
class Heat {

    private final List<KimbleClientInfo> teams;

    Heat() {
        this.teams = new ArrayList<>();
    }

    Heat(List<KimbleClientInfo> teams) {
        this.teams = teams;
    }

    Heat(Heat o) {
        this.teams = o.getTeams();
    }

    List<KimbleClientInfo> getTeams() {
        return teams;
    }

    List<KimbleClientInfo> getShuffledTeams() {
        ArrayList<KimbleClientInfo> list = new ArrayList<>(teams);
        Collections.shuffle(list);
        return list;
    }

    void addTeam(KimbleClientInfo kimbleClientInfo) {
        teams.add(kimbleClientInfo);
    }

    int size() {
        return teams.size();
    }
}
