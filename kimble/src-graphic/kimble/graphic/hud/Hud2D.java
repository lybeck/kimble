package kimble.graphic.hud;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.graphic.Screen;
import kimble.graphic.camera.Camera;
import kimble.graphic.camera.Camera2D;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.shader.Shader;
import kimble.graphic.shader.TextMaterial;
import kimble.logic.Move;
import kimble.logic.Team;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Hud2D {

    private static final int STARTING_TEAM_KEY = 0;
    private static final int CURRENT_PLAYER_KEY = 1;
    private static final int NEXT_PLAYER_KEY = 2;
    private static final int CURRENT_PLAYER_MOVE_KEY = 3;

    private final Camera camera;

    private BitmapFont font1;
    private BitmapFont font2;

    // Mapping element id to the element
    private Map<Integer, TextElement> textElements;

    private List<Team> teams;
    private List<TextElement> teamOrderTextElements;

    public Hud2D(List<Team> teams) {
        camera = new Camera2D();
        camera.setupProjectionMatrix();

        this.teams = teams;

        setup();
    }

    public void updateViewport() {
        camera.setupProjectionMatrix();

        teamOrderTextElements = new ArrayList<>();
        int scale = 1;
        for (int i = teams.size() - 1; i >= 0; i--) {
            Team team = teams.get(i);
            TextElement t = new TextElement(font2);
            t.addWord("[" + team.getId() + "] " + team.getName(), BitmapFont.TEXT_MATERIALS.get(team.getId()));
            t.setPosition(15, Screen.getHeight() - scale * font2.getVerticalSpacing() - 15);
            teamOrderTextElements.add(t);
            scale++;
        }
    }

    public final void setup() {
        try {
            font1 = FontGenerator.create("font1", new Font("Monospaced", Font.BOLD, 24), new Vector4f(1, 1, 1, 1));
            font2 = FontGenerator.create("font2", new Font("Monospaced", Font.BOLD, 20), new Vector4f(1, 1, 1, 1));
        } catch (IOException ex) {
            Logger.getLogger(BitmapFont.class.getName()).log(Level.SEVERE, null, ex);
        }
        textElements = new HashMap<>();

        createTextElement(STARTING_TEAM_KEY, "Starting Player: ", font1, BitmapFont.WHITE, 15, 10);
        createTextElement(CURRENT_PLAYER_KEY, "Current Player: ", font1, BitmapFont.WHITE, 15, 10
                + font1.getVerticalSpacing());
        createTextElement(CURRENT_PLAYER_MOVE_KEY, "Move: ", font2, BitmapFont.WHITE, 30, 10
                + font1.getVerticalSpacing() + font2.getVerticalSpacing());
        createTextElement(NEXT_PLAYER_KEY, "Next Player: ", font1, BitmapFont.WHITE, 15, 10 + 3
                * font1.getVerticalSpacing());
    }

    private void createTextElement(int key, String text, BitmapFont font, TextMaterial color, int x, int y) {
        TextElement t = new TextElement(font);
        t.setPosition(x, y);
        t.addWord(text, color);
        textElements.put(key, t);

    }

    public void setStartingPlayer(Team team) {
        textElements.get(STARTING_TEAM_KEY).addWord("[" + team.getId() + "] " + team.getName(), BitmapFont.TEXT_MATERIALS.get(team.getId()));
    }

    public void setCurrentPlayer(Team team) {
        if (team != null) {
            textElements.get(CURRENT_PLAYER_KEY).clear();
            textElements.get(CURRENT_PLAYER_KEY).addWord("Current Player: ", BitmapFont.WHITE);
            textElements.get(CURRENT_PLAYER_KEY).addWord("[" + team.getId() + "] " + team.getName(), BitmapFont.TEXT_MATERIALS.get(team.getId()));
        }
    }

    public void setNextPlayer(Team team) {
        textElements.get(NEXT_PLAYER_KEY).clear();
        textElements.get(NEXT_PLAYER_KEY).addWord("Next Player: ", BitmapFont.WHITE);
        textElements.get(NEXT_PLAYER_KEY).addWord("[" + team.getId() + "] " + team.getName(), BitmapFont.TEXT_MATERIALS.get(team.getId()));
    }

    public void setMoveInfo(Move selectedMove, String moveMessage) {

        textElements.get(CURRENT_PLAYER_MOVE_KEY).clear();
        textElements.get(CURRENT_PLAYER_MOVE_KEY).addWord("Move: ", BitmapFont.WHITE);

        StringBuilder sb = new StringBuilder();
        if (selectedMove == null) {
            sb.append(moveMessage);
        } else {
            sb.append("Piece [")
                    .append(selectedMove.getPiece().getId())
                    .append("] from [")
                    .append(selectedMove.getOldPositionID())
                    .append("] to [")
                    .append(selectedMove.getDestination().getID())
                    .append("]");
        }

        textElements.get(CURRENT_PLAYER_MOVE_KEY).addWord(sb.toString(), BitmapFont.WHITE);
    }

    // =======================================================
    /*
     * Update and render
     */
    // =======================================================
    public void update(float dt) {
        camera.update(dt);
        for (int key : textElements.keySet()) {
            textElements.get(key).update(dt);
        }
    }

    public void render(Shader shader) {
        for (int key : textElements.keySet()) {
            textElements.get(key).render(shader, camera);
        }
        for (TextElement te : teamOrderTextElements) {
            te.render(shader, camera);
        }
    }

}
