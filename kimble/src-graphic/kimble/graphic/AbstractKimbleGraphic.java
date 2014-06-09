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

    private boolean running;

    private final KimbleLogicInterface logic;
    private AbstractHud hud;

    private Camera3D camera;

    protected BoardGraphic board;
    protected List<PieceGraphic> pieces;
    private DieHolderGraphic dieHolder;
    protected DieGraphic die;
    protected DieHolderDomeGraphic dieHolderDome;

    protected List<Map<Integer, Integer>> startingDieRolls;
    protected Iterator<Map<Integer, Integer>> startingRollsIterator;
    protected Map<Integer, Integer> startingRollMap;
    protected Iterator<Integer> startingRollMapKeyIterator;

    private boolean rotateCamera;
    private boolean updateCameraPosition;
    private float cameraPositionAngle;
    private float goalAngle;

    public AbstractKimbleGraphic(KimbleLogicInterface logic) {
        this.logic = logic;
        this.rotateCamera = false;
        this.updateCameraPosition = true;
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

        camera = new Camera3D(70f, 0.1f, 1000f);
        rotateCameraToTeam(0);

        camera.setupProjectionMatrix();

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
        setGoalAngle(-board.getGoalSquares().get(logic.getBoard().getGoalSquare(teamID, 0).getID()).getRotation().y);
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

        if (isUpdateCameraPosition()) {
            if (isRotateCamera()) {
                setGoalAngle(goalAngle + dt * 0.1f);
            }
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

    /**
     * make a call to the renderComponents
     */
    public abstract void render();

    public void dispose() {
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

    /*
     * GETTERS and SETTERS
     */
    public void setHud(AbstractHud hud) {
        this.hud = hud;
    }

    public Camera3D getCamera() {
        return camera;
    }

    public final void setGoalAngle(float angle) {

        float goalAngle = angle;
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

        this.goalAngle = goalAngle;
    }

    public KimbleLogicInterface getLogic() {
        return logic;
    }

    public AbstractHud getHud() {
        return hud;
    }

    public void setRotateCamera(boolean rotateCamera) {
        this.rotateCamera = rotateCamera;
    }

    public boolean isRotateCamera() {
        return rotateCamera;
    }

    public void setUpdateCameraPosition(boolean updateCameraPosition) {
        this.updateCameraPosition = updateCameraPosition;
    }

    public boolean isUpdateCameraPosition() {
        return updateCameraPosition;
    }

}
