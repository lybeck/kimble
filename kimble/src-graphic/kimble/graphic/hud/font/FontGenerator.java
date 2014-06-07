package kimble.graphic.hud.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

    private static final boolean DEBUG = false;

    public static BitmapFont create(String key, Font font, Vector4f color) throws IOException {
        return FontGenerator.create(key, font, color, 1.0f);
    }

    public static BitmapFont create(String key, Font font, Vector4f color, float scale) throws IOException {
        Map<Character, Glyph> glyphs = new HashMap<>();

        int imageSize = 512;
        BufferedImage outputImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D) outputImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setBackground(new Color(0, 0, 0, 0));
        g.setColor(new Color(color.x, color.y, color.z, color.w));

        double height = font.getStringBounds("A", g.getFontRenderContext()).getHeight();

        double x = 0;
        double y = height;

        for (int i = 0; i < 256; i++) {
            TextLayout layout = new TextLayout("" + (char) i, font, g.getFontRenderContext());

            Rectangle2D rect = font.getStringBounds("" + (char) i, g.getFontRenderContext());
            Rectangle2D bounds = layout.getBounds();

            // Move drawing cursor to next line (y += height) with a small offset.
            // Force move drawing cursor forwarad to prevent the tex coords to fall outside the image (negative bounds).
            if (i != 0 && i % 16 == 0) {
                y += height + 2;
                x = Math.abs(bounds.getX());
            }

            Rectangle2D pixelBounds = layout.getPixelBounds(null, (int) x, (int) y);
            rect.setRect(pixelBounds.getX(), (int) y + rect.getY(), pixelBounds.getWidth(), rect.getHeight());
            layout.draw(g, (int) x, (int) y);

            if (DEBUG) {
                renderBoundingBoxes(g, rect, bounds, pixelBounds);
            }

            // Force the space character to have 1/3 of the font size as width.
            if (i == ' ') {
                rect.setRect(rect.getX(), rect.getY(), rect.getWidth() + font.getSize() / 3, rect.getHeight());
            }

            // Create the glyph data for this character ((char) i). Texture coordinates and character height and width.
            glyphs.put((char) i, new Glyph(
                    (float) (rect.getX() / imageSize),
                    (float) (rect.getY() / imageSize),
                    (float) (rect.getWidth() / imageSize),
                    (float) (rect.getHeight() / imageSize),
                    (float) rect.getWidth() * scale,
                    (float) rect.getHeight() * scale,
                    color));

            x = (float) (rect.getX() + rect.getWidth()) + 5;
        }

        createAndLoadTexture(outputImage, key);

        BitmapFont bitmapFont = new BitmapFont(glyphs, (int) (height * scale), key);
        return bitmapFont;
    }

    private static void createAndLoadTexture(BufferedImage outputImage, String key) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "png", outputStream);
        byte[] byteArray = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);

        TextureManager.load(key, inputStream);

        if (DEBUG) {
            ImageIO.write(outputImage, "png", new File("test_font.png"));
        }
    }

    /**
     * Method used when debugging. Draws boxes around the glyphs.
     *
     * @param g
     * @param stringBounds
     * @param layoutBounds
     * @param layoutPixelBounds
     */
    private static void renderBoundingBoxes(Graphics2D g, Rectangle2D stringBounds, Rectangle2D layoutBounds, Rectangle2D layoutPixelBounds) {
        g.setColor(Color.RED);
        g.draw(stringBounds);
        g.setColor(Color.GREEN);
        g.draw(layoutBounds);
        g.setColor(Color.YELLOW);
        g.draw(layoutPixelBounds);
        g.setColor(Color.BLACK);
    }

}
