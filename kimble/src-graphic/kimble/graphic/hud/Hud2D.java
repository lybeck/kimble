package kimble.graphic.hud;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.KimbleGraphic;
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

    private final KimbleGraphic mainWindow;
    private final Camera camera;

    private BitmapFont font1;
    private BitmapFont font2;

    private final List<Team> teams;
    private float widestTeamName;

    private List<TextElement> teamOrderTextElements;
    private List<TextElement> teamInfoTextElements;
    private List<TextElement> playbackSpeedTextElements;
    private Button showTagsButton;

    private List<AbstractHudItem> items;

    public Hud2D(KimbleGraphic mainWindow, List<Team> teams) {
        this.mainWindow = mainWindow;
        this.teams = teams;

        this.camera = new Camera2D();
        this.camera.setupProjectionMatrix();

        this.items = new ArrayList<>();
        setup();
    }

    public void updateViewport() {
        camera.setupProjectionMatrix();
        positionTeamOrderTextElements(font2);
        positionTeamInfoTextElements(font2);
        positionPlaybackSpeedTextElements(font2);
        positionToggleButton();
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
        createToggleButton(font2);
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
            te.addCallback(new Callback() {

                @Override
                public void execute() {
                    mainWindow.rotateCameraToTeam(team.getId());
                }
            });
            teamOrderTextElements.add(te);
        }

        items.addAll(teamOrderTextElements);
    }

    private void createTeamInfoTextElements(BitmapFont font) {
        teamInfoTextElements = new ArrayList<>();

        for (int i = 0; i < teams.size(); i++) {
            TextElement te = new TextElement(font);
            te.addWord("", BitmapFont.WHITE);
            te.setPosition(15 + 15 + widestTeamName, 10 + i * font.getVerticalSpacing());
            teamInfoTextElements.add(te);
        }

        items.addAll(teamInfoTextElements);
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
            PlaybackProfile profile = PlaybackProfile.values()[PlaybackProfile.values()[i].ordinal()];
            te.addCallback(new Callback() {

                @Override
                public void execute() {
                    PlaybackProfile.setCurrentProfile(profile);
                }
            });
            playbackSpeedTextElements.add(te);
        }

        positionPlaybackSpeedTextElements(font);

        items.addAll(playbackSpeedTextElements);
    }

    private void createToggleButton(BitmapFont font) {
        showTagsButton = new Button("Toggle Tags", font);
        showTagsButton.setPosition(Screen.getWidth() - showTagsButton.getWidth() - 15, 15);
        showTagsButton.addCallback(new Callback() {

            @Override
            public void execute() {
                mainWindow.toggleTags();
            }
        });

        items.add(showTagsButton);
    }

    private void positionTeamOrderTextElements(BitmapFont font) {
        for (int i = 0; i < teamOrderTextElements.size(); i++) {
            TextElement te = teamOrderTextElements.get(i);
            te.setPosition(15, 10 + i * font.getVerticalSpacing());
        }
    }

    private void positionTeamInfoTextElements(BitmapFont font) {
        for (int i = 0; i < teamInfoTextElements.size(); i++) {
            teamInfoTextElements.get(i).setPosition(30 + widestTeamName, 10 + i * font.getVerticalSpacing());
        }
    }

    private void positionPlaybackSpeedTextElements(BitmapFont font) {
        for (int i = 0; i < playbackSpeedTextElements.size(); i++) {
            TextElement te = playbackSpeedTextElements.get(i);
            te.setPosition(15, Screen.getHeight() - (i + 1) * font.getVerticalSpacing() - 15);
        }
    }

    private void positionToggleButton() {
        showTagsButton.setPosition(Screen.getWidth() - showTagsButton.getWidth() - 15, 15);
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

    // =======================================================
    // Update and render
    // =======================================================
    public void update(float dt) {
        camera.update(dt);

        for (AbstractHudItem item : items) {
            item.update(dt);
        }
//        for (TextElement te : teamOrderTextElements) {
//            te.update(dt);
//        }
//        for (TextElement te : playbackSpeedTextElements) {
//            te.update(dt);
//        }
//        showTagsButton.update(dt);
    }

    /**
     * The shader needs to be bound before calling this method
     *
     * @param shader
     */
    public void render(Shader shader) {
        glDisable(GL_DEPTH_TEST);

        for (AbstractHudItem item : items) {
            item.render(shader, camera);
        }

        glEnable(GL_DEPTH_TEST);
    }
}
