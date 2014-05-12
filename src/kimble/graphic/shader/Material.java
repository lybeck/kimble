/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.shader;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class Material {

    private Vector4f lightPosition;
    private Vector4f diffuse;
    private Vector4f ambient;
    private Vector4f specular;
    private float shininess;

    private static int lightPositionLocation;
    private static int diffuseLocation;
    private static int ambientLocation;
    private static int specularLocation;
    private static int shininessLocation;

    public Material() {
        lightPosition = new Vector4f(0, 20, 0, 1);
        diffuse = new Vector4f(1, 1, 1, 1);
        ambient = new Vector4f(0.1f, 0.1f, 0.1f, 1f);
        specular = new Vector4f(0.5f, 0.5f, 0.5f, 1f);
        shininess = 0.3f;
    }

    public static void fetchUniformLocations(int programID) {
        lightPositionLocation = glGetUniformLocation(programID, "material.lightPosition");
        diffuseLocation = glGetUniformLocation(programID, "material.diffuse");
        ambientLocation = glGetUniformLocation(programID, "material.ambient");
        specularLocation = glGetUniformLocation(programID, "material.specular");
        shininessLocation = glGetUniformLocation(programID, "material.shininess");
    }

    public void uploadUniforms() {
        glUniform4f(lightPositionLocation, lightPosition.x, lightPosition.y, lightPosition.z, lightPosition.w);
        glUniform4f(diffuseLocation, diffuse.x, diffuse.y, diffuse.z, diffuse.w);
        glUniform4f(ambientLocation, ambient.x, ambient.y, ambient.z, ambient.w);
        glUniform4f(specularLocation, specular.x, specular.y, specular.z, specular.w);
        glUniform1f(shininessLocation, shininess);
    }

    public void setLightPosition(Vector4f lightPosition) {
        this.lightPosition = lightPosition;
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

}
