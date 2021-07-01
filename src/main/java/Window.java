import graphics.test.text.CharRegister;
import fonts3.Font;
import graphics.test.text.GlyphOld;
import graphics.test.Shader;
import graphics.test.ShaderProgram;
import graphics.test.text.Keyboard;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements CharRegister {

    private long window;
    private Font font;
    private int vao;
    private char letter = 'I';
    private char prevLetter = letter;

    public float dt;
    public float fps;
    public int viewportX;
    public int viewportY;
    public int width, height;
    public int viewportWidth;
    public int viewportHeight;
    public float aspectRatio;
    public float viewW_normalized;
    public float viewH_normalized;

    private static Window instance;
    private Camera camera;
    private Renderer renderer = new Renderer();

    private float[] vertices = {
            // x, y,        r, g, b              ux, uy
            0.5f, 1f,     1.0f, 1.0f, 0.11f,   1.0f, 0.0f,
            0.5f, -1f,    1.0f, 0.2f, 0.11f,   1.0f, 1.0f,
            -0.5f, -1f,   1.0f, 0.2f, 0.11f,   0.0f, 1.0f,
            -0.5f, 1f,    1.0f, 0.2f, 0.11f,   0.0f, 0.0f
    };

    private int[] indices = {
            0, 1, 3,
            1, 2, 3
    };



    private Window() {
        //init();

        //font = Font.create("fonts/ttf/Topaz_a500_v1.0.ttf",16,false,false);
        //font = Font.create("fonts/ttf/Zapf Chancery Italic.ttf",64,false,false);


    }

    public static Window get() {
        if (instance == null) {
            instance = new Window();
        }
        return instance;
    }

    public void init() {
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);


        window = glfwCreateWindow(1280,720,"Font-Testing",NULL,NULL);
        if (window == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }


        glfwSetCharCallback(window,Keyboard::charCallback);
        glfwSetKeyCallback(window,Keyboard::keyCallback);
        Keyboard.get().setReader(this);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        int[] width = new int[2];
        int[] height = new int[2];
        glfwGetWindowSize(window, width,height);

        this.width = width[0];
        this.height = height[0];
        this.viewportX = 0;
        this.viewportY = 0;
        this.viewportWidth = this.width;
        this.viewportHeight = this.height;
        this.viewW_normalized = 1f / this.width;
        this.viewH_normalized = 1f / this.height;
        this.aspectRatio = 16f/9f;

        // Initialize gl functions for windows using GLAD
        GL.createCapabilities();

        glfwSetWindowSizeCallback(window,this::resizeCallback);

        glEnable(GL_BLEND); // Enabling alfa-blend
        // What blending function to use (very typical)
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        font = new Font(64);

        camera = new Camera();
    }

    private void uploadSquare() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);

        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        int stride = 7 * Float.BYTES;
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    public void setGlyph() {

        if (letter != prevLetter) {

            GlyphOld glyph = font.glyph(letter);


            glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
        }



    }

    public void run() {
        //Vector2f[] texCoords = font.getCharacter('A').textureCoordinates;

        camera.setPosition(100,100);
        renderer.init();

        //GlyphOld glyph = font.glyph(letter);

        //renderer.uploadGlyph(glyph,100,100);
        //vertices[5] = glyph.u2(); vertices[6] = glyph.v();
        //vertices[12] = glyph.u2(); vertices[13] = glyph.v2();
        //vertices[19] = glyph.u(); vertices[20] = glyph.v2();
        //vertices[26] = glyph.u(); vertices[27] = glyph.v();


        //uploadSquare();

        //Shader fontShader = new Shader("assets/fontShader.glsl");


        /*
        Shader vertexShader = Shader.loadShader(GL_VERTEX_SHADER,"assets/vertex.glsl");
        Shader fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER,"assets/fragment.glsl");

        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.attachShader(vertexShader);
        shaderProgram.attachShader(fragmentShader);
        //shaderProgram.bindFragmentDataLocation(0,);



        shaderProgram.link();

        vertexShader.delete();
        fragmentShader.delete();

        shaderProgram.use();

        shaderProgram.setUniform("uFontTexture",0);

         */


        while (!glfwWindowShouldClose(window)) {

            /*
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(0, 1, 1, 1);

            //fontShader.use();
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_BUFFER, font.textureID);

            //shaderProgram.setUniform(0);
            //fontShader.uploadTexture("uFontTexture", 0);

            glBindVertexArray(vao);
            setGlyph();

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

             */
            renderer.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        renderer.dispose();
        font.freeMemory();
    }

    @Override
    public void registerControl(int glfwKey) {
        if (glfwKey == GLFW_KEY_ESCAPE) {
            glfwSetWindowShouldClose(window,true);
        }
    }

    @Override
    public void registerChar(byte charCode) {
        letter = (char) charCode;
    }

    @Override
    public void signalActivate() {
        System.out.println("Activated");
    }

    @Override
    public void signalDeactivate() {

    }

    @Override
    public boolean isSleeping() {
        return false;
    }



    public static long glfwWindow() { return instance.window; }

    public static void defaultViewport() {
        glViewport(viewportX(),viewportY(),viewportW(),viewportH());
    }

    public static Font getFont() {
        return instance.font;
    }

    public static Camera getCamera() {

        return instance.camera;
    }

    public static int width() {
        return instance.width;
    }

    public static int height() {
        return instance.height;
    }

    public static int viewportW() {
        return instance.viewportWidth;
    }

    public static int viewportH() {
        return instance.viewportHeight;
    }

    public static int viewportX() {
        return instance.viewportX;
    }

    public static int viewportY() {
        return instance.viewportY;
    }

    public static float aspectRatio() { return instance.aspectRatio; }

    public static float viewW_normalized() { return instance.viewW_normalized; }

    public static float viewH_normalized() { return instance.viewH_normalized; }

    private void resizeCallback(long glfwWindow, int screenWidth, int screenHeight) {
        glfwSetWindowSize(glfwWindow, screenWidth, screenHeight);

        // Figure out the largest area that fits this target aspect ratio
        int aspectWidth = screenWidth;
        int aspectHeight = (int)((float)aspectWidth / instance.aspectRatio);

        if (aspectHeight > screenHeight) {
            // it doesn't fit so we mush change to pillarbox
            aspectHeight = screenHeight;
            aspectWidth = (int)((float)aspectHeight * instance.aspectRatio);
        }
        // Center rectangle
        int viewPortX = (int) (((float)screenWidth / 2f) - ((float)aspectWidth / 2f));
        int viewPortY = (int) (((float)screenHeight / 2f) - ((float)aspectHeight / 2f));

        instance.viewportWidth = aspectWidth;
        instance.viewportHeight = aspectHeight;
        instance.viewW_normalized = 1f / aspectWidth;
        instance.viewH_normalized = 1f / aspectHeight;
        instance.viewportX = viewPortX;
        instance.viewportY = viewPortY;
        instance.width = screenWidth;
        instance.height = screenHeight;

        glViewport(viewPortX,viewPortY,aspectWidth,aspectHeight);
    }


}