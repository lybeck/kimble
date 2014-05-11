/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public static void load(String filename) {
        createLists();

        File inputFile = new File(filename);
        if (!inputFile.exists()) {
            System.err.println("Couldn't load: " + filename);
            System.exit(1);
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(" ");
                if (line.startsWith("v ")) {
                    float x = Float.parseFloat(args[1]);
                    float y = Float.parseFloat(args[2]);
                    float z = Float.parseFloat(args[3]);
                    vertices.add(new Vector3f(x, y, z));
                } else if (line.startsWith("vn ")) {
                    float x = Float.parseFloat(args[1]);
                    float y = Float.parseFloat(args[2]);
                    float z = Float.parseFloat(args[3]);
                    normals.add(new Vector3f(x, y, z));
                } else if (line.startsWith("vt ")) {
                    float s = Float.parseFloat(args[1]);
                    float t = Float.parseFloat(args[2]);
                    texCoords.add(new Vector2f(s, t));
                } else if (line.startsWith("f ")) {
                    faces.add(extractIndices(args[1], args[2], args[3]));
                } else if (line.startsWith("#")) {

                }
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OBJLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OBJLoader.class.getName()).log(Level.SEVERE, null, ex);
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

        int n0 = Integer.parseInt(args0[2]) - 1;
        int n1 = Integer.parseInt(args1[2]) - 1;
        int n2 = Integer.parseInt(args2[2]) - 1;

        face.setNormalIndices(n0, n1, n2);

        return face;
    }

    public static List<Vector3f> getVertices() {
        return vertices;
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

        private int[] vertexIndices;
        private int[] texCoordIndices;
        private int[] normalIndices;

        public Face() {
            vertexIndices = new int[3];
            texCoordIndices = new int[3];
            normalIndices = new int[3];
        }

        public void setVertexIndices(int i0, int i1, int i2) {
            vertexIndices[0] = i0;
            vertexIndices[1] = i1;
            vertexIndices[2] = i2;
        }

        public void setTexCoordIndices(int t0, int t1, int t2) {
            texCoordIndices[0] = t0;
            texCoordIndices[1] = t1;
            texCoordIndices[2] = t2;
            texCoords = true;
        }

        public void setNormalIndices(int n0, int n1, int n2) {
            normalIndices[0] = n0;
            normalIndices[1] = n1;
            normalIndices[2] = n2;
            normals = true;
        }

        public int[] getVertexIndices() {
            return vertexIndices;
        }

        public int[] getTexCoordIndices() {
            return texCoordIndices;
        }

        public int[] getNormalIndices() {
            return normalIndices;
        }

        public boolean hasTexCoords() {
            return texCoords;
        }

        public boolean hasNormals() {
            return normals;
        }
    }

}
