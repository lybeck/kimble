package kimble.connection.logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Christoffer
 */
public class LogFile {

    // - Date
    // - Teams
    // - Moves (entries)
    // - Result
    private Date date;
    private List<TeamInfo> teams;
    private BoardInfo board;
    private Set<Integer> startValues;
    private Set<Integer> continueTurnValues;
    private List<Map<Integer, Integer>> startRolls;
    private Integer startingTeam;
    private List<LogEntry> moves;
    private Map<Integer, Integer> teamFinishTurns;
    private Map<Integer, Integer> teamDisqualifiedTurns;
    private Integer winner;

    public LogFile() {
    }

    public LogFile(Date date) {
        this.date = date;
        this.teams = new ArrayList<>();
        this.board = new BoardInfo();
        this.moves = new ArrayList<>();
        this.teamFinishTurns = new HashMap<>();
        this.teamDisqualifiedTurns = new HashMap<>();
    }

    // ================================================
    // Add methods
    // ================================================
    public void addTeam(Integer teamID, String teamName, Integer numberOfPieces) {
        teams.add(new TeamInfo(teamID, teamName, numberOfPieces));
    }

    public void addEntry(LogEntry entry) {
        moves.add(entry);
    }

    public void addTeamFinished(Integer teamID, int turnCount) {
        teamFinishTurns.put(teamID, turnCount);
    }

    public void addTeamDisqualified(Integer teamID, int turnCount) {
        teamDisqualifiedTurns.put(teamID, turnCount);
    }

    // ================================================
    // Setters
    // ================================================
    public void setStartValues(Set<Integer> startValues) {
        this.startValues = startValues;
    }

    public void setContinueTurnValues(Set<Integer> continueTurnValues) {
        this.continueTurnValues = continueTurnValues;
    }

    public void setStartRolls(List<Map<Integer, Integer>> startRolls) {
        this.startRolls = startRolls;
    }

    public void setStartingTeam(Integer teamID) {
        this.startingTeam = teamID;
    }

    public void setWinner(Integer teamID) {
        this.winner = teamID;
    }

    // ================================================
    // Getters
    // ================================================
    public Date getDate() {
        return date;
    }

    public Set<Integer> getStartValues() {
        return startValues;
    }

    public Set<Integer> getContinueTurnValues() {
        return continueTurnValues;
    }

    public List<Map<Integer, Integer>> getStartRolls() {
        return startRolls;
    }

    public int getStartingTeam() {
        return startingTeam;
    }

    public List<TeamInfo> getTeams() {
        return teams;
    }

    public BoardInfo getBoard() {
        return board;
    }

    public List<LogEntry> getEntries() {
        return moves;
    }

    public Map<Integer, Integer> getTeamFinnishTurns() {
        return teamFinishTurns;
    }

    public Map<Integer, Integer> getTeamDisqualifiedTurns() {
        return teamDisqualifiedTurns;
    }

    public int getWinner() {
        return winner;
    }

    // ================================================
    // Inner Classes
    // ================================================
    public class TeamInfo {

        public final Integer teamID;
        public final String teamName;
        public final Integer numberOfPieces;

        public TeamInfo(Integer teamID, String teamName, Integer numberOfPieces) {
            this.teamID = teamID;
            this.teamName = teamName;
            this.numberOfPieces = numberOfPieces;
        }
    }

    public class BoardInfo {

        public int sideLength;
        public List<Integer> squares;
        public Map<Integer, Integer> teamToStartSquares;
        public Map<Integer, List<Integer>> teamToGoalSquares;

        public BoardInfo() {
            squares = new ArrayList<>();
            teamToStartSquares = new HashMap<>();
            teamToGoalSquares = new HashMap<>();
        }

        public void setSideLength(int sideLength) {
            this.sideLength = sideLength;
        }

        public void addSquare(Integer squareID) {
            squares.add(squareID);
        }

        public void addStartSquare(Integer teamID, Integer squareID) {
            teamToStartSquares.put(teamID, squareID);
        }

        public void addGoalSquare(Integer teamID, Integer squareID) {
            if (teamToGoalSquares.containsKey(teamID)) {
                teamToGoalSquares.get(teamID).add(squareID);
            } else {
                teamToGoalSquares.put(teamID, new ArrayList<Integer>());
                teamToGoalSquares.get(teamID).add(squareID);
            }
        }
    }
}
