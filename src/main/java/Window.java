import graphics.test.text.CharRegister;
import graphics.test.text.Font;
import graphics.test.text.Glyph;
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
    private char letter = 'G';
    private char prevLetter = letter;

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

    public Window() {
        init();

        font = Font.create("fonts/ttf/Topaz_a500_v1.0.ttf",16,false,false);

    }

    private void init() {
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(496 * 2,512 , "Font Rendering", NULL, NULL);
        if (window == NULL) {
            System.out.println("Could not create window.");
            glfwTerminate();
            return;
        }
        glfwSetCharCallback(window,Keyboard::charCallback);
        glfwSetKeyCallback(window,Keyboard::keyCallback);
        Keyboard.get().setReader(this);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        // Initialize gl functions for windows using GLAD
        GL.createCapabilities();
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

            Glyph glyph = font.glyph(letter);
            vertices[5] = glyph.u2(); vertices[6] = glyph.v();
            vertices[12] = glyph.u2(); vertices[13] = glyph.v2();
            vertices[19] = glyph.u(); vertices[20] = glyph.v2();
            vertices[26] = glyph.u(); vertices[27] = glyph.v();

            glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
        }



    }

    public void run() {
        //Vector2f[] texCoords = font.getCharacter('A').textureCoordinates;


        Glyph glyph = font.glyph(letter);
        vertices[5] = glyph.u2(); vertices[6] = glyph.v();
        vertices[12] = glyph.u2(); vertices[13] = glyph.v2();
        vertices[19] = glyph.u(); vertices[20] = glyph.v2();
        vertices[26] = glyph.u(); vertices[27] = glyph.v();


        uploadSquare();

        //Shader fontShader = new Shader("assets/fontShader.glsl");


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


        while (!glfwWindowShouldClose(window)) {
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

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        font.freeMemory();
    }

    @Override
    public void registerControl(int glfwKey) {

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
}