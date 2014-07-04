package kimble.graphic.shader;

import java.nio.FloatBuffer;
import java.util.Scanner;
import kimble.graphic.camera.Camera;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

/**
 *
 * @author Christoffer
 */
public class Shader {

    private final static String SHADER_DIR = "/res/shaders/";
    private final int vertexShaderID;
    private final int fragmentShaderID;
    private final int programID;

    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int modelMatrixLocation;

    public Shader(String vertFile, String fragFile) {
        vertexShaderID = load(SHADER_DIR + vertFile, GL_VERTEX_SHADER);
        fragmentShaderID = load(SHADER_DIR + fragFile, GL_FRAGMENT_SHADER);

        programID = glCreateProgram();

        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);

        glBindAttribLocation(programID, 0, "in_Position");
        glBindAttribLocation(programID, 1, "in_Normal");
        glBindAttribLocation(programID, 2, "in_Color");
        glBindAttribLocation(programID, 3, "in_TextureCoord");

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

//        DefaultMaterial.fetchUniformLocations(programID);
    }

    private int load(String filename, int type) {
        StringBuilder shaderSource = new StringBuilder();
        int shaderID;

        Scanner scanner = new Scanner(Shader.class.getResourceAsStream(filename));
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            shaderSource.append(line).append("\n");
        }
        scanner.close();

        shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        //if the compiling was unsuccessful, throw an exception
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Couldn't compile shader: " + filename);
            String infoLog = glGetShaderInfoLog(shaderID, 1024);
            System.err.println(infoLog);
            System.exit(1);
        }

        return shaderID;
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void render(Camera camera, FloatBuffer modelMatrixBuffer, Material material) {
        material.fetchUniformLocations(programID);

        glUniformMatrix4(getProjectionMatrixLocation(), false, camera.getProjectionMatrixBuffer());
        glUniformMatrix4(getViewMatrixLocation(), false, camera.getViewMatrixBuffer());
        glUniformMatrix4(getModelMatrixLocation(), false, modelMatrixBuffer);

        material.uploadUniforms();
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void dispose() {
        glUseProgram(0);

        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);

        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
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
