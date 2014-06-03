package kimble.graphic.shader;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class TextMaterial implements Material {

    private Vector4f color;
    private int colorLocation;

    public TextMaterial(Vector3f color) {
        this.color = new Vector4f(color.x, color.y, color.z, 1.0f);
    }

    public TextMaterial(Vector4f color) {
        this.color = color;
    }

    @Override
    public void fetchUniformLocations(int shaderProgramID) {
        colorLocation = glGetUniformLocation(shaderProgramID, "textColor");
    }

    @Override
    public void uploadUniforms() {
        glUniform4f(colorLocation, color.x, color.y, color.z, color.w);
    }

}
