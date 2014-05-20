package kimble.graphic.hud.font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kimble.graphic.Model;
import kimble.graphic.camera.Camera;
import kimble.graphic.hud.RectangleMesh;
import kimble.graphic.model.Mesh;
import kimble.graphic.model.TextureManager;
import kimble.graphic.model.VertexData;
import kimble.graphic.shader.Shader;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class BitmapFont extends Model {

    private final int gridSize;
    private final float fontSize;
    private float cellSize;

    private final BitmapFontMesh mesh;

    public BitmapFont(int gridSize, float fontSize) {
        this.gridSize = gridSize;
        this.fontSize = fontSize;
        this.setScale(1, 1, 0);

        this.mesh = new BitmapFontMesh();
        this.getMaterial().setDiffuse(new Vector4f(0, 0, 0, 0));
        this.getMaterial().setTextureModulator(1.0f);

//        this.getMaterial().setDiffuse(new Vector4f(1, 1, 1, 1));
        this.setMesh(new BitmapFontMesh());
//        this.setMesh(new RectangleMesh(10, 10, 20, 20, new Vector4f(1, 1, 0, 1)));

        setup();
    }

    private void setup() {
        TextureManager.load("font", BitmapFont.class.getResourceAsStream("/res/fonts/font.png"));
        cellSize = 1.0f / gridSize;
    }

    public void renderString(Shader shader, String line, float x, float y) {
        TextureManager.getTexture("font").bind();
        mesh.renderCharacter('H');
//        for (int i = 0; i < line.length(); i++) {
//            this.setPosition(new Vector3f(x, y, 0));
//            mesh.renderCharacter(line.charAt(i));
//        }
        TextureManager.getTexture("font").unbind();
    }

    @Override
    public void render(Shader shader, Camera camera) {
        TextureManager.getTexture("font").bind();

        setPosition(new Vector3f(10, 30, 0));
        super.render(shader, camera);
        update(0);
        setPosition(new Vector3f(10 + fontSize * 0.75f, 30, 0));
        super.render(shader, camera);
        update(0);
        setPosition(new Vector3f(10 + fontSize * 0.75f + fontSize * 0.75f, 30, 0));
        super.render(shader, camera);
        update(0);
        setPosition(new Vector3f(70, 30, 0));
        super.render(shader, camera);

        TextureManager.getTexture("font").unbind();
    }

    private class BitmapFontMesh extends Mesh {

        private Map<Character, Integer> characterIndexOffset;
        private int[] indexData;

        public BitmapFontMesh() {
            setup();
        }

        @Override
        public VertexData[] setupVertexData() {
            List<VertexData> vertices = new ArrayList<>();
            List<Integer> indices = new ArrayList<>();

            characterIndexOffset = new HashMap<>();

            int character = 0;
            int indexOffset = 0;

            int index = 0;
            for (int i = 0; i < gridSize - 1; i++) {
                for (int j = 0; j < gridSize - 1; j++) {

                    float tx = i / (float) gridSize;
                    float ty = j / (float) gridSize;

                    VertexData v0 = new VertexData();
                    v0.setPosition(new Vector3f(0, 0, 0));
                    v0.setTexCoords(new Vector2f(tx, ty));

                    VertexData v1 = new VertexData();
                    v1.setPosition(new Vector3f(0, fontSize, 0));
                    v1.setTexCoords(new Vector2f(tx, ty + cellSize));

                    VertexData v2 = new VertexData();
                    v2.setPosition(new Vector3f(fontSize, fontSize, 0));
                    v2.setTexCoords(new Vector2f(tx + cellSize, ty + cellSize));

                    VertexData v3 = new VertexData();
                    v3.setPosition(new Vector3f(fontSize, 0, 0));
                    v3.setTexCoords(new Vector2f(tx + cellSize, ty));

                    vertices.add(v0);
                    vertices.add(v1);
                    vertices.add(v2);
                    vertices.add(v3);

                    indices.add(index + 0);
                    indices.add(index + 1);
                    indices.add(index + 2);
                    indices.add(index + 2);
                    indices.add(index + 3);
                    indices.add(index + 0);
                    index += 4;

                    characterIndexOffset.put((char) character, indexOffset);
                    character++;
                }
            }

            indexData = new int[indices.size()];
            for (int i = 0; i < indices.size(); i++) {
                indexData[i] = indices.get(i);
            }

            VertexData[] vertexData = new VertexData[vertices.size()];
            vertices.toArray(vertexData);
            return vertexData;
        }

        @Override
        public int[] setupIndexData() {
            return indexData;
        }

        public void renderCharacter(Character c) {
            glBindVertexArray(getVertexArrayObjectID());

            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);
            glEnableVertexAttribArray(3);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, getIndexBufferID());
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, characterIndexOffset.get(c));

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glDisableVertexAttribArray(3);

            glBindVertexArray(0);
        }

    }
}
