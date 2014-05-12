/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class OBJLoader {

    private static List<Vector3f> vertices;
    private static List<Vector3f> normals;
    private static List<Vector2f> texCoords;
    private static List<Face> faces;

    private static void createLists() {
        vertices = new ArrayList<>();
        normals = new ArrayList<>();
        texCoords = new ArrayList<>();
        faces = new ArrayList<>();
    }

    public static void load(InputStream inputStream) {
        createLists();

        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] args = line.split(" ");
            if (line.startsWith("v ")) {
                float x = Float.parseFloat(args[1]);
                float y = Float.parseFloat(args[2]);
                float z = Float.parseFloat(args[3]);
                vertices.add(new Vector3f(x, y, z));

            } else if (line.startsWith("vt ")) {
                float s = Float.parseFloat(args[1]);
                float t = Float.parseFloat(args[2]);
                texCoords.add(new Vector2f(s, t));

            } else if (line.startsWith("vn ")) {
                float x = Float.parseFloat(args[1]);
                float y = Float.parseFloat(args[2]);
                float z = Float.parseFloat(args[3]);
                normals.add(new Vector3f(x, y, z));

            } else if (line.startsWith("f ")) {
                faces.add(extractIndices(args[1], args[2], args[3]));
            } else if (line.startsWith("#")) {

            }
        }
    }

    private static Face extractIndices(String v0, String v1, String v2) {
        String[] args0 = v0.split("/");
        String[] args1 = v1.split("/");
        String[] args2 = v2.split("/");

        Face face = new Face();

        int i0 = Integer.parseInt(args0[0]) - 1;
        int i1 = Integer.parseInt(args1[0]) - 1;
        int i2 = Integer.parseInt(args2[0]) - 1;

        face.setVertexIndices(i0, i1, i2);

        if (!(args0[1].length() == 0 || args1[1].length() == 0 || args2[1].length() == 0)) {
            int t0 = Integer.parseInt(args0[1]) - 1;
            int t1 = Integer.parseInt(args1[1]) - 1;
            int t2 = Integer.parseInt(args2[1]) - 1;

            face.setTexCoordIndices(t0, t1, t2);
        }

        if (!(args0[2].length() == 0 || args1[2].length() == 0 || args2[2].length() == 0)) {
            int n0 = Integer.parseInt(args0[2]) - 1;
            int n1 = Integer.parseInt(args1[2]) - 1;
            int n2 = Integer.parseInt(args2[2]) - 1;

            face.setNormalIndices(n0, n1, n2);
        }

        return face;
    }

    public static List<Vector3f> getVertices() {
        return vertices;
    }

    public static List<Vector2f> getTexCoords() {
        return texCoords;
    }

    public static List<Vector3f> getNormals() {
        return normals;
    }

    public static List<Face> getFaces() {
        return faces;
    }

    public static class Face {

        private boolean normals;
        private boolean texCoords;

        private final FaceIndex[] indices;

        public Face() {
            indices = new FaceIndex[3];
            for (int i = 0; i < indices.length; i++) {
                indices[i] = new FaceIndex();
            }
        }

        public void setVertexIndices(int i0, int i1, int i2) {
            indices[0].vertexIndex = i0;
            indices[1].vertexIndex = i1;
            indices[2].vertexIndex = i2;
        }

        public void setTexCoordIndices(int t0, int t1, int t2) {
            indices[0].texCoordIndex = t0;
            indices[1].texCoordIndex = t1;
            indices[2].texCoordIndex = t2;
            texCoords = true;
        }

        public void setNormalIndices(int n0, int n1, int n2) {
            indices[0].normalIndex = n0;
            indices[1].normalIndex = n1;
            indices[2].normalIndex = n2;
            normals = true;
        }

        public FaceIndex[] getIndices() {
            return indices;
        }

        public FaceIndex getIndex(int i) {
            return indices[i];
        }

        public boolean hasTexCoords() {
            return texCoords;
        }

        public boolean hasNormals() {
            return normals;
        }
    }

    public static class FaceIndex {

        public int vertexIndex;
        public int texCoordIndex;
        public int normalIndex;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof FaceIndex) {
                FaceIndex index = (FaceIndex) obj;
                return vertexIndex == index.vertexIndex
                        && texCoordIndex == index.texCoordIndex
                        && normalIndex == index.normalIndex;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 71 * hash + this.vertexIndex;
            hash = 71 * hash + this.texCoordIndex;
            hash = 71 * hash + this.normalIndex;
            return hash;
        }
    }

}
