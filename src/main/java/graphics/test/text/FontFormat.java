package graphics.test.text;

import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author Frederik Dahl
 * 30/06/2021
 */


public class FontFormat {

    private final GlyphOld[] glyphs = new GlyphOld[Byte.MAX_VALUE]; // only use 32 -> 126
    private Texture texture;

    private int fontSize;
    private int fontHeight;
    private int maxDecent;
    private int maxAscent;

    private boolean monoSpaced;
    private boolean monoSized;



    public GlyphOld getGlyph(byte charCode) {
        return glyphs[charCode];
    }


    /*
    public static ImageContext createContext(Font font, boolean packed) {

        BufferedImage image = new BufferedImage(1,1, TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setFont(font);
        FontMetrics metrics = graphics2D.getFontMetrics();

        final boolean monospaced = isMonospaced(font);
        final int lineHeight = metrics.getHeight();

        int numDisplayable = 0;
        for (byte c = 32; c < Byte.MAX_VALUE; c++) {
            if (font.canDisplay(c)) numDisplayable++;
        }

        ImageContext context = new ImageContext(numDisplayable);

        if (packed) {

            final int estimatedWidth = (int)(Math.ceil(Math.sqrt(numDisplayable))) * lineHeight;
            int imageWidth = 0;
            int imageHeight = lineHeight;
            int originX = 0;
            int originY = lineHeight;

            if (monospaced) {

            }
            else {

            }
        }

    }

     */

    public static Texture createTexture(BufferedImage image, boolean blur) {

        final int width = image.getWidth();
        final int height = image.getHeight();
        final int[] pixels = new int[width * height];

        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = MemoryUtil.memAlloc(pixels.length * 4); // 4 bytes for int

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // r
                buffer.put((byte) ((pixel >> 8 ) & 0xFF)); // g
                buffer.put((byte) ((pixel      ) & 0xFF)); // b
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // a
            }
        }
        buffer.flip();

        int blurParam = blur ? GL_LINEAR : GL_NEAREST;

        Texture texture = Texture.create(width,height,buffer,GL_REPEAT,blurParam);

        MemoryUtil.memFree(buffer);

        return texture;
    }

    public static void createPNG(BufferedImage image, String folder, String name) {

        StringBuilder path = new StringBuilder(folder);

        String optional = "/";
        if (!folder.endsWith(optional)) {
            path.append(optional);
        }
        path.append(name);

        optional = ".png";
        if (!name.endsWith(optional)) {
            path.append(optional);
        }

        createPNG(image,path.toString());
    }

    public static void createPNG(BufferedImage image, String path) {

        File png = new File(String.valueOf(path));

        try {
            ImageIO.write(image,"png",png);
        } catch (IOException e) {
            System.out.println(png.getAbsolutePath());
            e.printStackTrace();
        }

    }

    // todo: assumes that the char 'W' is wider than 'i'. Replace with loop
    public static boolean isMonospaced(Font font) {

        if (font == null) throw new IllegalArgumentException("Argument: font cannot be null");

        final char W = 'W';
        final char i = 'i';

        if (!font.canDisplay(W) || !font.canDisplay(i)) {
            System.out.println("Monospace check for " + font.getFontName() + ", might not be valid");
            System.out.println("Font does not contain basic characters");
            return false;
        }

        FontRenderContext frc = new FontRenderContext(
                null,
                RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
                RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT
        );

        Rectangle2D bounds_i = font.getStringBounds(String.valueOf(i),frc);
        Rectangle2D bounds_W = font.getStringBounds(String.valueOf(W),frc);

        int width_i = bounds_i.getBounds().width;
        int width_W = bounds_W.getBounds().width;

        return !(width_i == width_W);
    }

    static class ImageContext {

        private final short[] glyphImageCoordinates;

        private int imageWidth;
        private int imageHeight;


        private ImageContext(int glyphCount) {
            glyphImageCoordinates = new short[glyphCount * 4];
        }

        public short[] getGlyphImageCoordinates() {
            return glyphImageCoordinates;
        }

        public int imageWidth() {
            return imageWidth;
        }

        public void setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
        }

        public int imageHeight() {
            return imageHeight;
        }

        public void setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
        }


    }
}
