package graphics;

public class TextureRegion {

    public final int originX;
    public final int originY;
    public final int rows;
    public final int cols;
    public final int count;
    public final int offset;
    public final int spriteW;
    public final int spriteH;

    private TextureRegion(int originX, int originY, int rows, int cols, int count, int offset, int spriteW, int spriteH) {
        this.originX = originX;
        this.originY = originY;
        this.rows = rows;
        this.cols = cols;
        this.count = count;
        this.offset = offset;
        this.spriteW = spriteW;
        this.spriteH = spriteH;
    }

    public static TextureRegion spriteSheet(int originX, int originY, int rows, int cols, int count, int offset, int spriteW, int spriteH) {
        return new TextureRegion(originX, originY, rows, cols, count, offset, spriteW, spriteH);
    }

    public static TextureRegion singleRegion(int originX, int originY, int regionW, int regionH) {
        return new TextureRegion(originX,originY,0,0,1,0,regionW,regionH);
    }
}
