package kimble.graphic.hud;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.KimbleGraphic;
import kimble.graphic.AbstractKimbleGraphic;
import kimble.graphic.Screen;
import kimble.graphic.camera.Camera;
import kimble.graphic.hud.TextElement.Word;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.shader.Shader;
import kimble.logic.Team;
import kimble.playback.PlaybackProfile;
import org.lwjgl.util.vector.Vector4f;

public class KimbleHud extends AbstractHud {

    private BitmapFont font2;

    private float widestTeamName;

    private TextElement turnCountTextElement;
    private List<AbstractHudElement> teamOrderTextElements;
    private List<AbstractHudElement> teamInfoTextElements;
    private List<AbstractHudElement> playbackSpeedTextElements;

    private Button showTagsButton;
    private Button moveAutoButton;
    private Button passTurnButton;

    private Rectangle teamInfoBackgroundRectangle;
    private Rectangle speedBackgroundRectangle;

    public KimbleHud(AbstractKimbleGraphic graphic, List<Team> teams) {
        super(graphic, teams);
    }

    @Override
    public void updateViewport() {
        super.updateViewport();
        positionTurnCountTextElement();
        positionTeamOrderTextElements(font2);
        positionTeamInfoTextElements(font2);
        positionPlaybackSpeedTextElements(font2);
        positionToggleButtons();
        positionButtons();

        createRectangles();
    }

    @Override
    public final void setup() {
        try {
            font2 = FontGenerator.create("font2", new Font("Monospaced", Font.BOLD, 20), new Vector4f(1, 1, 1, 1));
        } catch (IOException ex) {
            Logger.getLogger(BitmapFont.class.getName()).log(Level.SEVERE, null, ex);
        }
        createTurnCountTextElement(font2);
        createTeamOrderTextElements(font2);
        createTeamInfoTextElements(font2);
        createPlaybackSpeedTextElements(font2);
        createToggleButtons(font2);
        createButtons(font2);

        updateViewport();
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        teamInfoBackgroundRectangle.update(dt);
        speedBackgroundRectangle.update(dt);
    }

    @Override
    public void preRender(Shader shader, Camera camera) {
        teamInfoBackgroundRectangle.render(shader, camera);
        speedBackgroundRectangle.render(shader, camera);
    }

    private void createTurnCountTextElement(BitmapFont font) {
        turnCountTextElement = new TextElement(font);
        turnCountTextElement.addWord("Turn Count: 0", BitmapFont.WHITE);
        addElement(turnCountTextElement);
    }

    private void createTeamOrderTextElements(BitmapFont font) {
        teamOrderTextElements = new ArrayList<>();

        widestTeamName = 0;

        for (int i = 0; i < getTeams().size(); i++) {
            final Team team = getTeams().get(i);
            TextElement te = new TextElement(font);
            te.addWord("[" + team.getId() + "] " + team.getName(), BitmapFont.TEXT_MATERIALS.get(team.getId()));
            if (font.calculateWidth(te.getWords()) > widestTeamName) {
                widestTeamName = font.calculateWidth(te.getWords());
            }
            te.setPosition(15, 10 + i * font.getVerticalSpacing());
            te.addCallback(new Callback() {

                @Override
                public void execute() {
                    getGraphic().rotateCameraToTeam(team.getId());
                }
            });
            teamOrderTextElements.add(te);
        }

        addElements(teamOrderTextElements);
    }

    private void createTeamInfoTextElements(BitmapFont font) {
        teamInfoTextElements = new ArrayList<>();

        for (int i = 0; i < getTeams().size(); i++) {
            TextElement te = new TextElement(font);
            te.addWord("", BitmapFont.WHITE);
            te.setPosition(15 + 15 + widestTeamName, 10 + i * font.getVerticalSpacing());
            teamInfoTextElements.add(te);
        }

        addElements(teamInfoTextElements);
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
            final PlaybackProfile profile = PlaybackProfile.values()[PlaybackProfile.values()[i].ordinal()];
            te.addCallback(new Callback() {

                @Override
                public void execute() {
                    PlaybackProfile.setCurrentProfile(profile);
                }
            });
            playbackSpeedTextElements.add(te);
        }

        positionPlaybackSpeedTextElements(font);

        addElements(playbackSpeedTextElements);
    }

    private void createToggleButtons(BitmapFont font) {
        showTagsButton = new Button("Toggle Tags OFF", font);
        updateTextShowTagsButton();
        showTagsButton.addCallback(new Callback() {

            @Override
            public void execute() {
                getGraphic().toggleTags();
                updateTextShowTagsButton();
            }
        });
        addElement(showTagsButton);

        moveAutoButton = new Button("Auto OFF", font);
        updateTextMoveAutoButton();
        moveAutoButton.addCallback(new Callback() {

            @Override
            public void execute() {
                getGraphic().toggleMoveAuto();
                updateTextMoveAutoButton();
            }
        });
        addElement(moveAutoButton);
    }

    private void createButtons(BitmapFont font) {
        passTurnButton = new Button("Pass Turn", font);
        passTurnButton.addCallback(new Callback() {

            @Override
            public void execute() {
                getGraphic().passTurnIfOnlyOptionalMoves();
            }
        });
        addElement(passTurnButton);
    }

    private void createRectangles() {

        if (teamInfoBackgroundRectangle != null) {
            teamInfoBackgroundRectangle.dispose();
        }
        if (speedBackgroundRectangle != null) {
            speedBackgroundRectangle.dispose();
        }

        float x = turnCountTextElement.getX() - 5;
        float y = turnCountTextElement.getY();
        float width = widestTeamName + 200;
        float height = teamOrderTextElements.size() * teamOrderTextElements.get(0).getHeight()
                + turnCountTextElement.getHeight() + 5;
        Vector4f color = new Vector4f(0.4f, 0.4f, 0.4f, 0.4f);

        teamInfoBackgroundRectangle = new Rectangle(x, y, width, height, color);

        x = playbackSpeedTextElements.get(playbackSpeedTextElements.size() - 1).getX() - 5;
        y = playbackSpeedTextElements.get(playbackSpeedTextElements.size() - 1).getY();
        height = playbackSpeedTextElements.size() * playbackSpeedTextElements.get(0).getHeight();

        speedBackgroundRectangle = new Rectangle(x, y, width, height, color);
    }

    private void updateTextShowTagsButton() {
        if (getGraphic().isShowTags()) {
            showTagsButton.setTextKeepWidth("Toggle Tags ON");
        } else {
            showTagsButton.setTextKeepWidth("Toggle Tags OFF");
        }
    }

    private void updateTextMoveAutoButton() {
        if (getGraphic().isMoveAuto()) {
            moveAutoButton.setTextKeepWidth("Auto ON");
        } else {
            moveAutoButton.setTextKeepWidth("Auto OFF");
        }
    }

    private void positionTurnCountTextElement() {
        turnCountTextElement.setPosition(15, 10);
    }

    private void positionTeamOrderTextElements(BitmapFont font) {
        for (int i = 0; i < teamOrderTextElements.size(); i++) {
            AbstractHudElement te = teamOrderTextElements.get(i);
            te.setPosition(15, 15 + (i + 1) * font.getVerticalSpacing());
        }
    }

    private void positionTeamInfoTextElements(BitmapFont font) {
        for (int i = 0; i < teamInfoTextElements.size(); i++) {
            AbstractHudElement te = teamInfoTextElements.get(i);
            te.setPosition(30 + widestTeamName, 15 + (i + 1) * font.getVerticalSpacing());
        }
    }

    private void positionPlaybackSpeedTextElements(BitmapFont font) {
        for (int i = 0; i < playbackSpeedTextElements.size(); i++) {
            AbstractHudElement te = playbackSpeedTextElements.get(i);
            te.setPosition(15, Screen.getHeight() - (i + 1) * font.getVerticalSpacing() - 15);
        }
    }

    private void positionToggleButtons() {
        showTagsButton.setPosition(Screen.getWidth() - showTagsButton.getWidth() - 15, 15);
        moveAutoButton.setPosition(Screen.getWidth() - moveAutoButton.getWidth() - 15, 15 + moveAutoButton.getHeight()
                + 5);
    }

    private void positionButtons() {
        passTurnButton.setPosition(Screen.getWidth() - passTurnButton.getWidth() - 15, Screen.getHeight()
                - passTurnButton.getHeight() - 15);
    }

    // =======================================================
    /*
     * Setters
     */
    // =======================================================
    public void setTeamInfo(int teamID, String... info) {
        ((TextElement) teamInfoTextElements.get(teamID)).clear();
        for (String text : info) {
            ((TextElement) teamInfoTextElements.get(teamID)).addWord(text, BitmapFont.WHITE);
        }
    }

    public void appendTeamInfo(int teamID, String info) {
        ((TextElement) teamInfoTextElements.get(teamID)).addWord(info, BitmapFont.WHITE);
    }

    public void removeLastAppendTeamInfo(int teamID) {
        List<Word> words = ((TextElement) teamInfoTextElements.get(teamID)).getWords();
        if (words.size() > 0) {
            words.remove(words.size() - 1);
        }
    }

    public String getTeamInfo(int teamID) {
        List<Word> words = ((TextElement) teamInfoTextElements.get(teamID)).getWords();
        StringBuilder sb = new StringBuilder();
        for (Word word : words) {
            sb.append(word.getText());
        }
        return sb.toString();
    }

    public void setPlaybackSpeed(PlaybackProfile currentProfile) {
        for (int i = 0; i < playbackSpeedTextElements.size(); i++) {
            if (i == currentProfile.ordinal()) {
                ((TextElement) playbackSpeedTextElements.get(i)).getWords().get(0).setColor(BitmapFont.WHITE);
            } else {
                ((TextElement) playbackSpeedTextElements.get(i)).getWords().get(0).setColor(BitmapFont.GREY);
            }
        }
    }

    public void setTurnCount(int turnCount) {
        turnCountTextElement.clear();
        turnCountTextElement.addWord("Turn Count: " + turnCount, BitmapFont.WHITE);
    }

    public Button getPassTurnButton() {
        return passTurnButton;
    }

    @Override
    public KimbleGraphic getGraphic() {
        return (KimbleGraphic) super.getGraphic();
    }

    @Override
    public void dispose() {
        super.dispose();

        teamInfoBackgroundRectangle.dispose();
        speedBackgroundRectangle.dispose();
    }
}
