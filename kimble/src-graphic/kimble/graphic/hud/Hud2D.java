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
import kimble.graphic.hud.TextElement.Word;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.shader.Shader;
import kimble.logic.Team;
import kimble.playback.PlaybackProfile;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Hud2D {

    private final Camera camera;

    private BitmapFont font1;
    private BitmapFont font2;

    private final List<Team> teams;
    private List<TextElement> teamOrderTextElements;
    private Map<Integer, TextElement> teamInfoTextElements;
    private float widestTeamName;

    private List<TextElement> playbackSpeedTextElements;

    public Hud2D(List<Team> teams) {
        camera = new Camera2D();
        camera.setupProjectionMatrix();

        this.teams = teams;

        setup();
    }

    public void updateViewport() {
        camera.setupProjectionMatrix();
        createTeamOrderTextElements(font2);
        createTeamInfoTextElements(font2);
        createPlaybackSpeedTextElements(font2);
    }

    public final void setup() {
        try {
            font1 = FontGenerator.create("font1", new Font("Monospaced", Font.BOLD, 24), new Vector4f(1, 1, 1, 1));
            font2 = FontGenerator.create("font2", new Font("Monospaced", Font.BOLD, 20), new Vector4f(1, 1, 1, 1));
        } catch (IOException ex) {
            Logger.getLogger(BitmapFont.class.getName()).log(Level.SEVERE, null, ex);
        }
        createTeamOrderTextElements(font2);
        createTeamInfoTextElements(font2);
        createPlaybackSpeedTextElements(font2);
    }

    private void createTeamOrderTextElements(BitmapFont font) {
        teamOrderTextElements = new ArrayList<>();

        widestTeamName = 0;

        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            TextElement te = new TextElement(font);
            te.addWord("[" + team.getId() + "] " + team.getName(), BitmapFont.TEXT_MATERIALS.get(team.getId()));
            if (font.calculateWidth(te.getWords()) > widestTeamName) {
                widestTeamName = font.calculateWidth(te.getWords());
            }
            te.setPosition(15, 10 + i * font.getVerticalSpacing());
            teamOrderTextElements.add(te);
        }

//        for (int i = 0; i < teamOrderTextElements.size(); i++) {
//        }
    }

    private void createTeamInfoTextElements(BitmapFont font) {
        teamInfoTextElements = new HashMap<>();

        for (int i = 0; i < teams.size(); i++) {
            TextElement te = new TextElement(font);
            te.addWord("", BitmapFont.WHITE);
            te.setPosition(15 + 15 + widestTeamName, 10 + i * font.getVerticalSpacing());
            teamInfoTextElements.put(i, te);
        }
    }

    private void createPlaybackSpeedTextElements(BitmapFont font) {
        playbackSpeedTextElements = new ArrayList<>();

        float width = 0;
        for (int i = 0; i < PlaybackProfile.values().length; i++) {
            TextElement te = new TextElement(font);
            if (width < font.calculateWidth(PlaybackProfile.values()[i].name())) {
                width = font.calculateWidth(PlaybackProfile.values()[i].name());
            }
            te.addWord("Key " + (PlaybackProfile.values()[i].ordinal() + 1) + ": " + PlaybackProfile.values()[i].name(), BitmapFont.GREY);
            playbackSpeedTextElements.add(te);
        }

        for (int i = 0; i < playbackSpeedTextElements.size(); i++) {
            TextElement te = playbackSpeedTextElements.get(i);
            te.setPosition(15, Screen.getHeight() - (i + 1) * font.getVerticalSpacing() - 15);
        }
    }

    // =======================================================
    /*
     * Setters
     */
    // =======================================================
    public void setTeamInfo(int teamID, String... info) {
        teamInfoTextElements.get(teamID).clear();
        for (String text : info) {
            teamInfoTextElements.get(teamID).addWord(text, BitmapFont.WHITE);
        }
    }

    public void appendTeamInfo(int teamID, String info) {
        setTeamInfo(teamID, getTeamInfo(teamID) + info);
    }

    public String getTeamInfo(int teamID) {
        List<Word> words = teamInfoTextElements.get(teamID).getWords();
        StringBuilder sb = new StringBuilder();
        for (Word word : words) {
            sb.append(word.getText());
        }
        return sb.toString();
    }

    public void setPlaybackSpeed(PlaybackProfile currentProfile) {
        for (int i = 0; i < playbackSpeedTextElements.size(); i++) {
            if (i == currentProfile.ordinal()) {
                playbackSpeedTextElements.get(i).getWords().get(0).setColor(BitmapFont.WHITE);
            } else {
                playbackSpeedTextElements.get(i).getWords().get(0).setColor(BitmapFont.GREY);
            }
        }
    }
//    public void setMoveInfo(Move selectedMove, String moveMessage) {
//
//        textElements.get(CURRENT_PLAYER_MOVE_KEY).clear();
//        textElements.get(CURRENT_PLAYER_MOVE_KEY).addWord("Move: ", BitmapFont.WHITE);
//        textElements.get(CURRENT_PLAYER_MOVE_KEY).addWord(moveMessage, BitmapFont.WHITE);
//
//        textElements.get(CURRENT_PLAYER_MOVE_KEY).addWord(message, BitmapFont.WHITE);
//    }
    // =======================================================
    /*
     * Update and render
     */

    // =======================================================
    public void update(float dt) {
        camera.update(dt);
    }

    /**
     * The shader needs to be bound before calling this method
     *
     * @param shader
     */
    public void render(Shader shader) {
        glDisable(GL_DEPTH_TEST);

        for (TextElement te : teamOrderTextElements) {
            te.render(shader, camera);
        }
        for (int key : teamInfoTextElements.keySet()) {
            teamInfoTextElements.get(key).render(shader, camera);
        }
        for (TextElement te : playbackSpeedTextElements) {
            te.render(shader, camera);
        }

        glEnable(GL_DEPTH_TEST);
    }

}
