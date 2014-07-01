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
        this(logic, position, new Vector4f(color.x, color.y, color.z, 1));
    }
    
    public SquareGraphic(Square logic, Vector3f position, Vector4f color) {
        super(position, new Vector3f());
        this.logic = logic;
        
//        this.getMaterial().setAmbient(new Vector4f(0.15f, 0.15f, 0.15f, color.w));
        this.getMaterial().setDiffuse(color);
        this.setMesh(ModelManager.getModel("game_board_position"));
    }
    
    @Override
    public void update(float dt) {
        super.update(dt);
        
        Vector3f position = getPosition();
        setPosition(new Vector3f(position.x, 0, position.z));
    }
    
    public Square getLogic() {
        return logic;
    }
    
}
