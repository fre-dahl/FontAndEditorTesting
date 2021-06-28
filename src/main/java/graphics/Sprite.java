package graphics;

public class Sprite {

    private final int w;
    private final int h;

    private final float u;
    private final float v;
    private final float u2;
    private final float v2;

    public Sprite(int w, int h, float u, float v, float u2, float v2) {
        this.w = w;
        this.h = h;
        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;
    }

    public int w() { return w; }

    public int h() { return h; }

    public float u() { return u; }

    public float v() { return v; }

    public float u2() { return u2; }

    public float v2() { return v2; }

}
