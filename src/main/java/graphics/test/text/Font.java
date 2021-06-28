package graphics.test.text;

import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Font.*;
import static java.awt.Font.PLAIN;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static org.lwjgl.opengl.GL11.*;


public class Font {

    private final Map<Character,Glyph> glyphs;

    public int textureID;

    public Font() {
        this(new java.awt.Font(MONOSPACED, PLAIN, 16), true, true);
    }

    public Font(int size) {

        this(new java.awt.Font(MONOSPACED, PLAIN, size), true, true);
    }

    public Font(int size, boolean antiAlias) {

        this(new java.awt.Font(MONOSPACED, PLAIN, size), antiAlias,true);
    }

    public Font(int size, boolean antiAlias, boolean blur) {

        this(new java.awt.Font(MONOSPACED, PLAIN, size), antiAlias,blur);
    }

    public Font(InputStream in, int size) throws FontFormatException, IOException {

        this(in, size, true,true);
    }

    public Font(InputStream in, int size, boolean antiAlias, boolean blur) throws FontFormatException, IOException {

        this(java.awt.Font.createFont(TRUETYPE_FONT, in).deriveFont(PLAIN, size), antiAlias, blur);
    }

    public Font(java.awt.Font font) {

        this(font, true, true);
    }

    public Font(java.awt.Font font, boolean antiAlias, boolean blur) {

        glyphs = new HashMap<>();

        createFont(font, antiAlias, blur);
    }

    public static Font create(String path, int size, boolean antiAlias, boolean blur) {
        try {
            return new Font(new FileInputStream(path), size,antiAlias,blur);

        } catch (FontFormatException | IOException ex) {
            //Logger.getLogger(Renderer.class.getName()).log(Level.CONFIG, null, ex);
            return new Font();
        }
    }

    private void createFont(java.awt.Font font, boolean antiAlias, boolean blur) {

        BufferedImage image = new BufferedImage(1,1, TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setFont(font);
        FontMetrics metrics = graphics2D.getFontMetrics();

        int numDisplayable = 0;

        for (char i = 0; i < font.getNumGlyphs(); i++) {
            if (font.canDisplay(i)) numDisplayable++;
        }

        final int lineHeight = metrics.getHeight();
        // The estimated width implicitly contains the w/h ratio.
        final int estimatedWidth = (int)(Math.ceil(Math.sqrt(numDisplayable))) * lineHeight;
        int imageWidth = 0;
        int imageHeight = lineHeight;
        int originX = 0;
        int originY = lineHeight;

        int[] glyphInfo = new int[numDisplayable * 4];
        int pointer = 0;

        for (char i = 0; i < font.getNumGlyphs(); i++) {

            if (font.canDisplay(i)) {

                int charWidth = metrics.charWidth(i);
                glyphInfo[    pointer] = originX;
                glyphInfo[1 + pointer] = originY;
                glyphInfo[2 + pointer] = charWidth;
                glyphInfo[3 + pointer] = lineHeight;

                pointer += 4;

                imageWidth = Math.max(originX + charWidth, imageWidth);
                originX += charWidth;

                if (originX > estimatedWidth) {
                    originX = 0;
                    originY += lineHeight;
                    imageHeight += lineHeight;
                }
            }
        }
        graphics2D.dispose();

        // Creating the actual image
        image = new BufferedImage(imageWidth,imageHeight,TYPE_INT_ARGB);
        graphics2D = image.createGraphics();

        if (antiAlias) {
            graphics2D.setRenderingHint(KEY_ANTIALIASING,VALUE_ANTIALIAS_ON);
        }
        graphics2D.setFont(font);
        graphics2D.setColor(Color.WHITE);

        pointer = 0;

        for (char i = 0; i < font.getNumGlyphs(); i++) {

            if (font.canDisplay(i)) {

                int x = glyphInfo[    pointer];
                int y = glyphInfo[1 + pointer];
                int w = glyphInfo[2 + pointer];
                int h = glyphInfo[3 + pointer];
                pointer += 4;

                Glyph glyph = new Glyph(x, y, w, h, imageWidth, imageHeight);
                glyphs.put(i,glyph);

                graphics2D.drawString(String.valueOf(i),x,y);
            }
        }
        graphics2D.dispose();

        /*
        File file = new File( "test.png");
        try {
            ImageIO.write(image,"png",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
         */

        int[] pixels = new int[imageWidth * imageHeight];

        image.getRGB(0,0,imageWidth,imageHeight,pixels,0,imageWidth);

        ByteBuffer buffer = MemoryUtil.memAlloc(pixels.length * 4); // 4 byte per. int

        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int pixel = pixels[y * imageWidth + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // r
                buffer.put((byte) ((pixel >> 8 ) & 0xFF)); // g
                buffer.put((byte) ((pixel      ) & 0xFF)); // b
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // a
            }
        }
        buffer.flip();

        textureID = glGenTextures();

        int blurParamValue = blur ? GL_LINEAR : GL_NEAREST;

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, blurParamValue);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, blurParamValue);

        glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA8,imageWidth,imageHeight,0,GL_RGBA, GL_UNSIGNED_BYTE,buffer);

        MemoryUtil.memFree(buffer);

    }

    // todo: getOrDefault() / Exception?
    public Glyph glyph(char c) {
        return glyphs.get(c);
    }

    public void freeMemory() {
        glDeleteTextures(textureID);
    }

}
