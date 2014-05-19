/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.model;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import static kimble.ServerGame.DEBUG;

/**
 *
 * @author Christoffer
 */
public class ModelManager {

    private static String[] modelNames = new String[]{"game_piece",
        "game_board_position",
        "game_die",
        "game_board_die_holder",
        "game_board_die_holder_dome",
        "cube"};

    private static final Map<String, OBJModel> models = new HashMap<>();

    public static void loadModels() {
        String dir = "/res/models/";
        for (int i = 0; i < modelNames.length; i++) {
            load(modelNames[i], ModelManager.class.getResourceAsStream(dir + modelNames[i] + ".obj"));
        }
        
        if(DEBUG){
            System.out.println("Done loading models.");
        }
    }

    public static void load(String key, InputStream inputStream) {
        OBJLoader.load(inputStream);
        OBJModel model = new OBJModel(OBJLoader.getFaces(), OBJLoader.getVertices(), OBJLoader.getTexCoords(), OBJLoader.getNormals());
        models.put(key, model);
    }

    public static OBJModel getModel(String name) {
        return models.get(name);
    }

    public static void dispose() {
        for (String key : models.keySet()) {
            models.get(key).dispose();
        }
    }

}
