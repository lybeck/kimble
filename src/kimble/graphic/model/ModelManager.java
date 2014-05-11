/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.model;

import java.util.HashMap;
import java.util.Map;
import kimble.graphic.board.BoardGraphic;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class ModelManager {

    private static String[] modelNames = new String[]{"game_piece", "game_board_position", "game_die"};

    private static final Map<String, OBJModel> models = new HashMap<>();

    public static void loadModels() {
        String dir = "/res/models/";
        for (int i = 0; i < modelNames.length; i++) {
            load(modelNames[i], ModelManager.class.getResource(dir + modelNames[i] + ".obj").getFile());
        }
    }

    private static void load(String key, String filename) {
        OBJLoader.load(filename);
        OBJModel model = new OBJModel(OBJLoader.getFaces(), OBJLoader.getVertices(), OBJLoader.getNormals());
        models.put(key, model);
    }

    public static OBJModel getModel(String name) {
        return models.get(name);
    }

}
