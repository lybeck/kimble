package kimble.graphic.shader;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class DefaultMaterial implements Material {

    private static int textureModulatorLocation;

    private Vector4f lightPosition;
    private Vector4f diffuse;
    private Vector4f ambient;
    private Vector4f specular;
    private float shininess;

    private float textureModulator;

    private int lightPositionLocation;
    private int diffuseLocation;
    private int ambientLocation;
    private int specularLocation;
    private int shininessLocation;

    public DefaultMaterial() {
        lightPosition = new Vector4f(10, 20, 10, 1);
        diffuse = new Vector4f(1, 1, 1, 1);
        ambient = new Vector4f(0.15f, 0.15f, 0.15f, 1f);
        specular = new Vector4f(0.75f, 0.75f, 0.75f, 1);
        shininess = 100;
        textureModulator = 0.0f;
    }

    @Override
    public void fetchUniformLocations(int programID) {
        lightPositionLocation = glGetUniformLocation(programID, "material.lightPosition");
        diffuseLocation = glGetUniformLocation(programID, "material.diffuse");
        ambientLocation = glGetUniformLocation(programID, "material.ambient");
        specularLocation = glGetUniformLocation(programID, "material.specular");
        shininessLocation = glGetUniformLocation(programID, "material.shininess");

        textureModulatorLocation = glGetUniformLocation(programID, "textureModulator");
    }

    @Override
    public void uploadUniforms() {
        glUniform4f(lightPositionLocation, lightPosition.x, lightPosition.y, lightPosition.z, lightPosition.w);
        glUniform4f(diffuseLocation, diffuse.x, diffuse.y, diffuse.z, diffuse.w);
        glUniform4f(ambientLocation, ambient.x, ambient.y, ambient.z, ambient.w);
        glUniform4f(specularLocation, specular.x, specular.y, specular.z, specular.w);
        glUniform1f(shininessLocation, shininess);

        glUniform1f(textureModulatorLocation, textureModulator);
    }

    public void setLightPosition(Vector4f lightPosition) {
        this.lightPosition = lightPosition;
    }

    public Vector4f getLightPosition() {
        return lightPosition;
    }

    public void setDiffuse(Vector3f diffuse) {
        this.diffuse = new Vector4f(diffuse.x, diffuse.y, diffuse.z, 1.0f);
    }

    public void setDiffuse(Vector4f diffuse) {
        this.diffuse = diffuse;
    }

    public Vector4f getDiffuse() {
        return diffuse;
    }

    public void setAmbient(Vector4f ambient) {
        this.ambient = ambient;
    }

    public Vector4f getAmbient() {
        return ambient;
    }

    public Vector4f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector4f specular) {
        this.specular = specular;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public float getTextureModulator() {
        return textureModulator;
    }

    /**
     * Used to mix the color and the texture inside the shader.
     * <p>
     * Defaults to zero
     *
     * @param textureModulator
     */
    public void setTextureModulator(float textureModulator) {
        this.textureModulator = textureModulator;
    }

}
