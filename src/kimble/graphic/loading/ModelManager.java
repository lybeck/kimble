/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.loading;

import java.util.HashMap;
import java.util.Map;
import kimble.graphic.board.BoardGraphic;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class ModelManager {

    private static String[] modelNames = new String[]{"game_piece"};

    private static final Map<String, OBJModel> models = new HashMap<>();

    public static void loadModels() {
        String dir = "/res/models/";
        for (int j = 0; j < BoardGraphic.teamColors.size(); j++) {
            for (int i = 0; i < modelNames.length; i++) {
                load(modelNames[i] + "_" + j, ModelManager.class.getResource(dir + modelNames[i] + ".obj").getFile(), BoardGraphic.teamColors.get(j));
            }
        }
    }

    private static void load(String key, String filename, Vector3f color) {
        OBJLoader.load(filename);
        OBJModel model = new OBJModel(color, OBJLoader.getFaces(), OBJLoader.getVertices(), OBJLoader.getNormals());
        models.put(key, model);
    }

    public static OBJModel getModel(String name) {
        return models.get(name);
    }

}
