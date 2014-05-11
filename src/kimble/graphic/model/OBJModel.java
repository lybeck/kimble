/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.model;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class OBJModel extends Mesh {

    private final List<OBJLoader.Face> faceList;
    private final List<Vector3f> vertexList;
    private final List<Vector2f> texCoordList;
    private final List<Vector3f> normalList;

    private VertexData[] vertices;
    private int[] indices;

    public OBJModel(List<OBJLoader.Face> faces, List<Vector3f> vertices, List<Vector2f> texCoords, List<Vector3f> normals) {
        this.faceList = faces;
        this.vertexList = vertices;
        this.texCoordList = texCoords;
        this.normalList = normals;

        setup();
    }

    @Override
    public VertexData[] setupVertexData() {

        List<VertexData> vertexData = new ArrayList<>();
        for (int i = 0; i < vertexList.size(); i++) {
            vertexData.add(new VertexData());
            vertexData.get(i).setPosition(vertexList.get(i));
        }

        indices = new int[faceList.size() * 3];
        int cur = 0;
        for (int i = 0; i < faceList.size(); i++) {
            OBJLoader.Face face = faceList.get(i);
            for (int j = 0; j < face.getIndices().length; j++) {
                OBJLoader.FaceIndex index = face.getIndex(j);

                VertexData v = vertexData.get(index.vertexIndex);
                Vector2f texCoord = null;
                Vector3f normal = null;
                if (face.hasNormals()) {
                    normal = normalList.get(index.normalIndex);
                }
                if (face.hasTexCoords()) {
                    texCoord = texCoordList.get(index.texCoordIndex);
                }

                if (v.getTexCoords() == null) {
                    v.setTexCoords(texCoord);
                    indices[cur++] = index.vertexIndex;
                } else {
                    VertexData v2 = new VertexData();
                    v2.setPosition(new Vector3f(v.getPosition().x, v.getPosition().y, v.getPosition().z));
                    v2.setTexCoords(texCoord);
                    vertexData.add(v2);
                    indices[cur++] = vertexData.size();
                }

                v.setNormal(normal);
            }
        }
        System.out.println("Vertices = " + vertexData.size() + ", indices = " + indices.length);

        vertices = new VertexData[vertexData.size()];
        int index = 0;
        for (VertexData vertex : vertexData) {
            vertices[index++] = vertex;
            System.out.println(vertex.getPosition() + ": " + vertex.getTexCoords());
        }
        for (int i = 0; i < indices.length; i++) {
            System.out.print(indices[i] + ", ");
        }
        System.out.println("");

        return vertices;
    }

    @Override
    public int[] setupIndexData() {
        return indices;
    }

}
