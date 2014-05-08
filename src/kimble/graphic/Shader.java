/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class Shader {

    private final int programID;

    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int modelMatrixLocation;

    public Shader(String vertFile, String fragFile) {
        int vertexShaderID = load(vertFile, GL_VERTEX_SHADER);
        int fragmentShaderID = load(fragFile, GL_FRAGMENT_SHADER);

        programID = glCreateProgram();

        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);

        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);

        glBindAttribLocation(programID, 0, "in_Position");
        glBindAttribLocation(programID, 1, "in_Color");
        glBindAttribLocation(programID, 2, "in_TextureCoord");

        glLinkProgram(programID);
        if (glGetShaderi(programID, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Couldn't link shader!");
            System.exit(1);
        }

        glValidateProgram(programID);
        if (glGetShaderi(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println("Couldn't validate shader!");
            System.exit(1);
        }

        projectionMatrixLocation = glGetUniformLocation(programID, "projectionMatrix");
        viewMatrixLocation = glGetUniformLocation(programID, "viewMatrix");
        modelMatrixLocation = glGetUniformLocation(programID, "modelMatrix");
    }

    private int load(String filename, int type) {
        StringBuilder shaderSource = new StringBuilder();
        int shaderID = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException ex) {
            System.err.println("Couldn't read shader file: " + filename);
            System.exit(1);
        }

        shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);
        //if the compiling was unsuccessful, throw an exception

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Couldn't compile shader: " + filename);
            System.exit(1);
        }

        return shaderID;
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void render(FloatBuffer modelMatrixBuffer) {
        glUniformMatrix4(getProjectionMatrixLocation(), false, Camera.getProjectionMatrixBuffer());
        glUniformMatrix4(getViewMatrixLocation(), false, Camera.getViewMatrixBuffer());
        glUniformMatrix4(getModelMatrixLocation(), false, modelMatrixBuffer);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanUp() {
        glUseProgram(0);
        glDeleteProgram(programID);
    }

    public int getProgramID() {
        return programID;
    }

    public int getProjectionMatrixLocation() {
        return projectionMatrixLocation;
    }

    public int getViewMatrixLocation() {
        return viewMatrixLocation;
    }

    public int getModelMatrixLocation() {
        return modelMatrixLocation;
    }

}
