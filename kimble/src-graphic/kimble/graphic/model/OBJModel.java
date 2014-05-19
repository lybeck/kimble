/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<Integer> indexData = new ArrayList<>();
//        for (int i = 0; i < vertexList.size(); i++) {
//            vertexData.add(new VertexData());
//            vertexData.get(i).setPosition(vertexList.get(i));
//        }

        Map<OBJLoader.FaceIndex, Integer> indexMap = new HashMap<>();
        int currentVertexIndex = 0;
        for (int i = 0; i < faceList.size(); i++) {
            OBJLoader.Face face = faceList.get(i);
            for (int j = 0; j < face.getIndices().length; j++) {
                OBJLoader.FaceIndex currentIndex = face.getIndex(j);

                Vector3f currentPosition = vertexList.get(currentIndex.vertexIndex);
                Vector2f currentTexCoord = null;
                Vector3f currentNormal = null;

                if (face.hasTexCoords()) {
                    currentTexCoord = texCoordList.get(currentIndex.texCoordIndex);
                }
                if (face.hasNormals()) {
                    currentNormal = normalList.get(currentIndex.normalIndex);
                }

                Integer previousVertexIndex = indexMap.get(currentIndex);

                if (previousVertexIndex == null) {

                    indexMap.put(currentIndex, currentVertexIndex);

                    VertexData v = new VertexData();
                    v.setPosition(currentPosition);
                    v.setTexCoords(currentTexCoord);
                    v.setNormal(currentNormal);
                    vertexData.add(v);

                    indexData.add(currentVertexIndex);
                    currentVertexIndex++;

                } else {
                    indexData.add(previousVertexIndex);
                }
            }
        }

        indices = new int[indexData.size()];
        for (int i = 0; i < indexData.size(); i++) {
            indices[i] = indexData.get(i);
        }
        vertices = new VertexData[vertexData.size()];
        for (int i = 0; i < vertexData.size(); i++) {
            vertices[i] = vertexData.get(i);
        }
        return vertices;
    }

    @Override
    public int[] setupIndexData() {
        return indices;
    }

}
