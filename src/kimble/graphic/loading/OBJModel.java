/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.loading;

import java.util.List;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class OBJModel extends Mesh {

//    private Vector3f color;
    private List<OBJLoader.Face> faceList;
    private List<Vector3f> vertexList;
    private List<Vector3f> normalList;

    private VertexData[] vertices;

    public OBJModel(Vector3f color, List<OBJLoader.Face> faces, List<Vector3f> vertices, List<Vector3f> normals) {
//        this.color = color;
        this.faceList = faces;
        this.vertexList = vertices;
        this.normalList = normals;

        setup();
    }

    @Override
    public VertexData[] setupVertexData() {
        vertices = new VertexData[vertexList.size()];
        for (int i = 0; i < vertexList.size(); i++) {
            vertices[i] = new VertexData();
            vertices[i].setPosition(vertexList.get(i));
//            vertices[i].setColor(color);
        }
        return vertices;
    }

    @Override
    public int[] setupIndexData() {
        int[] indices = new int[faceList.size() * 3];

        int index = 0;
        for (int i = 0; i < faceList.size(); i++) {
            OBJLoader.Face face = faceList.get(i);

            int i0 = face.getVertexIndices()[0];
            int i1 = face.getVertexIndices()[1];
            int i2 = face.getVertexIndices()[2];

            int n0 = face.getNormalIndices()[0];
            int n1 = face.getNormalIndices()[1];
            int n2 = face.getNormalIndices()[2];

            indices[index++] = i0;
            indices[index++] = i1;
            indices[index++] = i2;

            vertices[i0].setNormal(normalList.get(n0));
            vertices[i1].setNormal(normalList.get(n1));
            vertices[i2].setNormal(normalList.get(n2));
        }

        return indices;
    }

}
