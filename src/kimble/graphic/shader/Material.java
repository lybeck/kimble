/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.shader;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Material {

    private Vector4f lightPosition;
    private Vector4f diffuse;
    private Vector4f ambient;

    private static int lightPositionLocation;
    private static int diffuseLocation;
    private static int ambientLocation;

    public Material() {
        lightPosition = new Vector4f(0, 20, 0, 1);
        diffuse = new Vector4f(1, 1, 1, 1);
        ambient = new Vector4f(0.1f, 0.1f, 0.1f, 1f);
    }

    public static void fetchUniformLocations(int programID) {
        lightPositionLocation = glGetUniformLocation(programID, "material.lightPosition");
        diffuseLocation = glGetUniformLocation(programID, "material.diffuse");
        ambientLocation = glGetUniformLocation(programID, "material.ambient");
    }

    public void uploadUniforms() {
        glUniform4f(lightPositionLocation, lightPosition.x, lightPosition.y, lightPosition.z, lightPosition.w);
        glUniform4f(diffuseLocation, diffuse.x, diffuse.y, diffuse.z, diffuse.w);
        glUniform4f(ambientLocation, ambient.x, ambient.y, ambient.z, ambient.w);
    }

    public void setLightPosition(Vector4f lightPosition) {
        this.lightPosition = lightPosition;
        //new Vector4f(lightPosition.x, lightPosition.y, lightPosition.z, 1);
    }

    public Vector4f getLightPosition() {
        return lightPosition;
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
}
