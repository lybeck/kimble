package kimble.graphic.hud;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.graphic.camera.Camera;
import kimble.graphic.camera.Camera2D;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.hud.font.FontGenerator;
import kimble.graphic.shader.Shader;
import kimble.logic.Move;
import kimble.logic.Team;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Hud2D {

    private final Camera camera;

    private BitmapFont font1;

    // Mapping element id to the element
    private Map<Integer, TextElement> textElements;

    public Hud2D() {
        camera = new Camera2D();
        camera.setupProjectionMatrix();

        setup();
    }

    public void updateViewport() {
        camera.setupProjectionMatrix();
    }

    public final void setup() {
        try {
            font1 = FontGenerator.create("font1", new Font("Monospaced", Font.BOLD, 24), new Vector4f(1, 1, 1, 1));
        } catch (IOException ex) {
            Logger.getLogger(BitmapFont.class.getName()).log(Level.SEVERE, null, ex);
        }
        textElements = new HashMap<>();

        TextElement t = new TextElement(font1);
        t.setPosition(15, 10);
        t.addWord("Starting Player: ", BitmapFont.WHITE);
        textElements.put(0, t);

        TextElement t1 = new TextElement(font1);
        t1.setPosition(15, 10 + font1.getVerticalSpacing());
        t1.addWord("Current Player: ", BitmapFont.WHITE);
        textElements.put(1, t1);

        TextElement t3 = new TextElement(font1);
        t3.setPosition(30, 10 + 2 * font1.getVerticalSpacing());
        t3.addWord("Move: ", BitmapFont.WHITE);
        textElements.put(3, t3);

        TextElement t2 = new TextElement(font1);
        t2.setPosition(15, 10 + 3 * font1.getVerticalSpacing());
        t2.addWord("Next Player: ", BitmapFont.WHITE);
        textElements.put(2, t2);

    }

    public void update(float dt) {
        camera.update(dt);
        for (int key : textElements.keySet()) {
            textElements.get(key).update(dt);
        }
    }

    public void setStartingPlayer(Team team) {
        textElements.get(0).addWord("[" + team.getId() + "] " + team.getName(), BitmapFont.TEXT_MATERIALS.get(team.getId()));
    }

    public void setCurrentPlayer(Team team) {
        if (team != null) {
            textElements.get(1).clear();
            textElements.get(1).addWord("Current Player: ", BitmapFont.WHITE);
            textElements.get(1).addWord("[" + team.getId() + "] " + team.getName(), BitmapFont.TEXT_MATERIALS.get(team.getId()));
        }
//        setMoveInfo(null, null);
    }

    public void setNextPlayer(Team team) {
        textElements.get(2).clear();
        textElements.get(2).addWord("Next Player: ", BitmapFont.WHITE);
        textElements.get(2).addWord("[" + team.getId() + "] " + team.getName(), BitmapFont.TEXT_MATERIALS.get(team.getId()));
    }

    public void setMoveInfo(Move selectedMove, String moveMessage) {

        textElements.get(3).clear();
        textElements.get(3).addWord("Move: ", BitmapFont.WHITE);

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
        
        textElements.get(3).addWord(sb.toString(), BitmapFont.WHITE);
    }

    public void render(Shader shader) {
        shader.bind();

        for (int key : textElements.keySet()) {
            textElements.get(key).render(shader, camera);
        }

        shader.bind();
    }

}
