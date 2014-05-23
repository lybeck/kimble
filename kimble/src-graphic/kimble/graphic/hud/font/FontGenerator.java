package kimble.graphic.hud.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import kimble.graphic.model.TextureManager;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class FontGenerator {

    public static BitmapFont create(String key, Font font, Vector4f color) throws IOException {
        Map<Character, Glyph> glyphs = new HashMap<>();

        int imageSize = 512;
        BufferedImage outputImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) outputImage.getGraphics();

        g.setBackground(new Color(0, 0, 0, 0));
        g.setColor(new Color(color.x, color.y, color.z, color.w));

        double height = font.getStringBounds("", g.getFontRenderContext()).getHeight();

        double x = 0;
        double y = height;
        for (int i = 0; i < 256; i++) {
            TextLayout layout = new TextLayout("" + (char) i, font, g.getFontRenderContext());
            Rectangle2D bounds = layout.getBounds();

            if (i != 0 && i % 16 == 0) {
                y += height;
                x = 0;
            }
            double offsetY = bounds.getY();

            bounds.setRect(x + bounds.getX() - 1, y + bounds.getY() - 1, bounds.getWidth() + 2, bounds.getHeight() + 2);
            layout.draw(g, (float) x, (float) y);

//            g.setColor(Color.GREEN);
//            g.draw(bounds);
//            g.setColor(Color.BLACK);
            if (i == ' ') {
                bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth() + font.getSize() / 3, bounds.getHeight());
            }

            glyphs.put((char) i, new Glyph(
                    (float) (bounds.getX() / imageSize),
                    (float) (bounds.getY() / imageSize),
                    (float) (bounds.getWidth() / imageSize),
                    (float) (bounds.getHeight() / imageSize),
                    (float) bounds.getWidth(),
                    (float) bounds.getHeight(),
                    (float) offsetY,
                    color));

            x = (float) (bounds.getX() + bounds.getWidth()) + 2;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "png", outputStream);
        byte[] byteArray = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);

        TextureManager.load(key, inputStream);

//        ImageIO.write(outputImage, "png", new File("test_font.png"));
        BitmapFont bitmapFont = new BitmapFont(glyphs, (float) height, key);
        return bitmapFont;
    }

}
