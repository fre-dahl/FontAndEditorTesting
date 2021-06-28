package graphics;

public class SpriteSheet {

    Sprite[] sprites;
    String name;
    int rows;
    int cols;
    int spriteW;
    int spriteH;

    private SpriteSheet(String name, Sprite[] sprites, int rows, int cols, int spriteW, int spriteH) {
        this.name = name;
        this.sprites = sprites;
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.spriteW = spriteW;
        this.spriteH = spriteH;
    }

    public Sprite get(int index) {
        return sprites[index];
    }

    public Sprite get(int row, int col) {
        return sprites[row * cols + col];
    }

    public static SpriteSheet generate(String name, Texture texture, TextureRegion region) {

        final int x0 = region.originX;
        final int y0 = region.originY;
        final int rows = region.rows;
        final int cols = region.cols;
        final int count = region.count;
        final int offset = region.offset;
        final int spriteW = region.spriteW;
        final int spriteH = region.spriteH;

        Sprite[] array = new Sprite[count];

        SpriteSheet result = new SpriteSheet(name,array,rows,cols,spriteW,spriteH);

        final float invTxWidth = 1f / texture.width();
        final float invTxHeight = 1f / texture.height();
        final float fix = 0.01f;

        int n = 0;
        int o = offset;
        int index = 0;

        out:

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                if (o > 0) o--;

                else { if (n++ == count) break out;

                    float u  = ((x0 + spriteW * col)           + fix) * invTxWidth;
                    float v  = ((y0 - spriteH - spriteH * row) + fix) * invTxHeight;
                    float u2 = ((x0 + spriteW + spriteW * col) - fix) * invTxWidth;
                    float v2 = ((y0 - spriteH * row)           - fix) * invTxHeight;

                    Sprite sprite = new Sprite(spriteW,spriteH,u,v,u2,v2);
                    array[index] = sprite;
                    index++;
                }
            }
        }
        return result;
    }
}
