package kimble.graphic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kimble.graphic.board.BoardGraphic;
import kimble.graphic.board.BoardSpecs;
import kimble.graphic.board.DieGraphic;
import kimble.graphic.board.DieHolderDomeGraphic;
import kimble.graphic.board.DieHolderGraphic;
import kimble.graphic.board.PieceGraphic;
import kimble.graphic.camera.Camera3D;
import kimble.graphic.hud.AbstractHud;
import kimble.graphic.hud.Hud2D;
import kimble.graphic.hud.font.BitmapFont;
import kimble.graphic.model.ModelManager;
import kimble.graphic.model.TextureManager;
import kimble.graphic.shader.Shader;
import kimble.logic.KimbleLogicInterface;
import kimble.logic.Piece;
import kimble.util.MathHelper;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public abstract class AbstractKimbleGraphic {

    protected final KimbleLogicInterface logic;
    private AbstractHud hud;

    private boolean running;

//    protected Hud2D hud2d;
    protected BitmapFont font;

    protected Camera3D camera;
    private Input3D input;
    protected ExtraInput extraInput;

    protected Shader shader;
    protected Shader textShader;

    private BoardGraphic board;
    protected List<PieceGraphic> pieces;
    private DieHolderGraphic dieHolder;
    protected DieGraphic die;
    protected DieHolderDomeGraphic dieHolderDome;

    protected List<Map<Integer, Integer>> startingDieRolls;
    protected Iterator<Map<Integer, Integer>> startingRollsIterator;
    protected Map<Integer, Integer> startingRollMap;
    protected Iterator<Integer> startingRollMapKeyIterator;

    private float cameraPositionAngle;
    private float goalAngle;

    public AbstractKimbleGraphic(KimbleLogicInterface logic) {
        this.logic = logic;
        setupLWJGL();
    }

    private void setupLWJGL() {
        Screen.setupNativesLWJGL();

        if (!LWJGLUtil.isMacOSXEqualsOrBetterThan(3, 2)) {
            System.err.println("You need an OpenGL version of 3.2 or higher to run this application with a GUI. You can still run the logic just specify the USE_GUI = false");
            System.exit(1);
        }

        Screen.setupDisplay("Kimble - alpha 1.0", 800, 600);

        Screen.setupOpenGL();
        Screen.setResizable(true);
    }

    public void setup() {
        ModelManager.loadModels();
        TextureManager.loadTextures();

        board = new BoardGraphic(logic.getBoard(), logic.getTeams(), new BoardSpecs());
        shader = new Shader("shader.vert", "shader.frag");
        textShader = new Shader("text_shader.vert", "text_shader.frag");

        camera = new Camera3D(70f, 0.1f, 1000f);
        rotateCameraToTeam(0);

        camera.setupProjectionMatrix();

        extraInput = new ExtraInput();
        input = new Input3D(camera);

        pieces = new ArrayList<>();
        for (int i = 0; i < logic.getTeams().size(); i++) {
            for (Piece p : logic.getTeam(i).getPieces()) {
                pieces.add(new PieceGraphic(board, p, new Vector3f(0, 0, 0), BoardGraphic.TEAM_COLORS.get(i)));
            }
        }
        dieHolder = new DieHolderGraphic();
        dieHolder.rotate(0, board.getHomeSquares().get(0).getRotation().y, 0);

        die = new DieGraphic();

        dieHolderDome = new DieHolderDomeGraphic();

        startingDieRolls = logic.getStartingDieRolls();
        startingRollsIterator = startingDieRolls.iterator();
        startingRollMap = startingRollsIterator.next();
        startingRollMapKeyIterator = startingRollMap.keySet().iterator();
    }

    public final void start() {
        if (running) {
            return;
        }
        setup();
        running = true;
        loop();
    }

    public final void stop() {
        running = false;
    }

    private void loop() {
        while (running) {
            Screen.clear();

            float dt = 0.016f;

            input(dt);
            update(dt);
            render();

            Screen.update(60);
        }

        dispose();
    }

    public void input(float dt) {
        extraInput.update(dt);
        input.update(dt);
    }

    public void updateComponents(float dt) {
        board.update(dt);
        dieHolder.update(dt);
        die.update(dt);
        dieHolderDome.update(dt);

        for (PieceGraphic p : pieces) {
            p.update(dt);
        }
    }

    public void update(float dt) {
        if (Screen.isCloseRequested()) {
            stop();
        }

        if (Screen.wasResized()) {
            Screen.updateViewport();
            camera.setupProjectionMatrix();
            hud.updateViewport();
        }

        if (extraInput.isRotateCamera()) {
            cameraPositionAngle += dt * 0.1;
            updateCameraPosition();
        }

        if (extraInput.isUpdateCameraPosition()) {
            updateCameraAngle(dt);
            updateCameraPosition();
        }
        camera.update(dt);

        updateComponents(dt);
    }

    public void renderComponents(Shader shader) {
        board.render(shader, camera);

        dieHolder.render(shader, camera);
        die.render(shader, camera);
        dieHolderDome.render(shader, camera);

        for (PieceGraphic p : pieces) {
            p.render(shader, camera);
        }
    }

    public void render() {
        shader.bind();
        renderComponents(shader);
        shader.unbind();
    }

    public void dispose() {

        shader.dispose();
        textShader.dispose();

        board.dispose();

        dieHolder.dispose();
        die.dispose();
        dieHolderDome.dispose();

        for (PieceGraphic p : pieces) {
            p.dispose();
        }

        ModelManager.dispose();
        TextureManager.dispose();

        Screen.dispose();
    }

    private void updateCameraAngle(float dt) {
        cameraPositionAngle = MathHelper.lerp(cameraPositionAngle, goalAngle, dt * 5);
    }

    private void updateCameraPosition() {

        Vector3f cameraPos = new Vector3f(
                board.getRadius() * 1.2f * (float) Math.cos(cameraPositionAngle),
                board.getRadius() * 1.5f,
                board.getRadius() * 1.2f * (float) Math.sin(cameraPositionAngle)
        );
        Vector3f rotation = new Vector3f(
                (float) (Math.PI / 3),
                cameraPositionAngle - (float) (Math.PI / 2),
                0
        );

        camera.setPosition(cameraPos);
        camera.setRotation(rotation);
    }

    public void rotateCameraToTeam(int teamID) {
        goalAngle = -board.getGoalSquares().get(logic.getBoard().getGoalSquare(teamID, 0).getID()).getRotation().y;

        if (goalAngle < 0) {
            goalAngle += 2 * Math.PI;
        } else if (goalAngle > 2 * Math.PI) {
            goalAngle -= 2 * Math.PI;
        }

        if (cameraPositionAngle > 2 * Math.PI) {
            cameraPositionAngle -= 2 * Math.PI;
        } else if (cameraPositionAngle < 0) {
            cameraPositionAngle += 2 * Math.PI;
        }

        float tempGoalAngle = (float) (goalAngle > cameraPositionAngle ? goalAngle : goalAngle + 2 * Math.PI);
        float diff = tempGoalAngle - cameraPositionAngle;
        if (diff > Math.PI) {
            goalAngle = (float) (tempGoalAngle - 2 * Math.PI);
        } else {
            goalAngle = tempGoalAngle;
        }
    }

    public void setHud(AbstractHud hud) {
        this.hud = hud;
    }

    public AbstractHud getHud() {
        return hud;
    }

}
