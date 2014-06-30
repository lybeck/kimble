package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.model.ModelManager;
import kimble.logic.board.Square;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class SquareGraphic extends Model {

    private final Square logic;

    public SquareGraphic(Square logic, Vector3f position, Vector3f color) {
        super(position, new Vector3f(0, 0, 0));
        this.logic = logic;

        this.getMaterial().setDiffuse(new Vector4f(color.x, color.y, color.z, 1));
        this.setMesh(ModelManager.getModel("game_board_position"));
    }

    public Square getLogic() {
        return logic;
    }

}
