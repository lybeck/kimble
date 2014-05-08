/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.loading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class OBJLoader {

    private static List<Vector3f> vertices;
    private static List<Vector3f> normals;
    private static List<Face> faces;

    private static void createLists() {
        vertices = new ArrayList<>();
        normals = new ArrayList<>();
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

        return face;
    }

    public static class Face {

        private int[] vertexIndices;
        private int[] normalIndices;

        public Face() {
            vertexIndices = new int[3];
            normalIndices = new int[3];
        }

        public void setVertexIndices(int i0, int i1, int i2) {
            vertexIndices[0] = i0;
            vertexIndices[1] = i1;
            vertexIndices[2] = i2;
        }

        public int[] getVertexIndices() {
            return vertexIndices;
        }
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
}
