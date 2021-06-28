package graphics.test;

public class Texture {

    private int width;
    private int height;
    private int textureID;

    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int textureID() {
        return textureID;
    }
}
