/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.model;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.glGenTextures;

/**
 *
 * @author Christoffer
 */
public class TextureManager {

    private static String[] textureNames = new String[]{"Die_tex"};

    private static final Map<String, Texture> textures = new HashMap<>();

    public static void loadTextures() {
        String dir = "/res/textures/";
        for (int i = 0; i < textureNames.length; i++) {
            load(textureNames[i], TextureManager.class.getResource(dir + textureNames[i] + ".png").getFile());
        }
    }

    private static void load(String key, String filename) {

        ByteBuffer buffer = null;
        int textureWidth = 0;
        int textureHeight = 0;
        InputStream in = null;
        try {
            in = new FileInputStream(filename);
            PNGDecoder decoder = new PNGDecoder(in);

            textureWidth = decoder.getWidth();
            textureHeight = decoder.getHeight();

            buffer = ByteBuffer.allocateDirect(4 * textureWidth * textureHeight);
            decoder.decode(buffer, textureWidth * 4, Format.RGBA);
            buffer.flip();

            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextureManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TextureManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        int textureID = glGenTextures();
        Texture texture = new Texture(textureID, textureWidth, textureHeight);
        texture.setBuffer(buffer);

        textures.put(key, texture);
    }

    public static Texture getTexture(String key) {
        return textures.get(key);
    }

    public static void cleanUp() {
        for (String key : textures.keySet()) {
            textures.get(key).cleanUp();
        }
    }
}
