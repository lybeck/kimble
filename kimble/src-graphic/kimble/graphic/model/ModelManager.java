package kimble.graphic.model;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
        "table"
    };

    private static final Map<String, OBJModel> models = new HashMap<>();

    public static void loadModels() {
        String dir = "/res/models/";
        for (int i = 0; i < modelNames.length; i++) {
            load(modelNames[i], ModelManager.class.getResourceAsStream(dir + modelNames[i] + ".obj"));
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
