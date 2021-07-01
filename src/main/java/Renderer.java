import fonts3.Font;
import graphics.test.EBO;
import graphics.test.Shader;
import graphics.test.ShaderProgram;
import graphics.test.VAO;
import graphics.test.VBO;
import graphics.test.text.GlyphOld;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;

/**
 * @author Frederik Dahl
 * 01/07/2021
 */


public class Renderer {

    private ShaderProgram shaderProgram;
    private VAO vao;
    private VBO vbo;
    private EBO ebo;

    final int size = 10;
    private int count = 0;

    private float[] vertices;

    public void init() {

        vao = new VAO();
        vao.bind();

        vertices = new float[7*4*size];

        vbo = new VBO();
        vbo.bind(GL_ARRAY_BUFFER);
        vbo.uploadData(GL_ARRAY_BUFFER,(long) vertices.length * Float.BYTES,GL_STATIC_DRAW);

        ebo = new EBO();
        ebo.bind(GL_ELEMENT_ARRAY_BUFFER);
        ebo.uploadData(new int[] {
                0, 1, 3,
                1, 2, 3
        });

        int stride = 7 * Float.BYTES;
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);

        Shader vertexShader = Shader.loadShader(GL_VERTEX_SHADER,"assets/vertex.glsl");
        Shader fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER,"assets/fragment.glsl");

        shaderProgram = new ShaderProgram();

        shaderProgram.attachShader(vertexShader);
        shaderProgram.attachShader(fragmentShader);
        //shaderProgram.bindFragmentDataLocation(0,);

        shaderProgram.link();

        vertexShader.delete();
        fragmentShader.delete();

        shaderProgram.use();

        shaderProgram.setUniform("uFontTexture",0);

        int varLocation = glGetUniformLocation(shaderProgram.getId(),"uCombined");

        Matrix4f mat = Window.getCamera().projectionMatrix();
        shaderProgram.setUniform(varLocation,mat);


    }

    public void render() {

        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(0, 1, 1, 1);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_BUFFER, Window.getFont().textureID);

        vao.bind();

        uploadGlyph(Window.getFont().glyph('D'),100,100 );

        //glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);


    }


    public void uploadGlyph(GlyphOld glyph, float x, float y) {

        vertices[0] = 50f;
        vertices[1] = 100;

        vertices[2] = 1f;
        vertices[3] = 1;
        vertices[4] = 0.11f;

        vertices[5] = glyph.u2(); vertices[6] = glyph.v();

        vertices[7] = 50f;
        vertices[8] = -100;

        vertices[9] = 1f;
        vertices[10] = 1;
        vertices[11] = 0.11f;

        vertices[12] = glyph.u2(); vertices[13] = glyph.v2();

        vertices[14] = -50f;
        vertices[15] = -100;

        vertices[16] = 1;
        vertices[17] = 1;
        vertices[18] = 0.11f;

        vertices[19] = glyph.u(); vertices[20] = glyph.v2();

        vertices[21] = -50f;
        vertices[22] = 100;

        vertices[23] = 1;
        vertices[24] = 1;
        vertices[25] = 0.11f;

        vertices[26] = glyph.u(); vertices[27] = glyph.v();


        vbo.uploadData(GL_ARRAY_BUFFER,vertices,GL_DYNAMIC_DRAW);
        //vertices.put(x).put(y).put(1).put(0).put(0).put(glyph.u2()).put(glyph.v());
        //vertices.put(x).put(y-glyph.h()).put(1).put(0).put(0).put(glyph.u2()).put(glyph.v2());
        //vertices.put(x-glyph.w()).put(y-glyph.w()).put(1).put(0).put(0).put(glyph.u()).put(glyph.v2());
        //vertices.put(x-glyph.w()).put(y).put(1).put(0).put(0).put(glyph.u()).put(glyph.v());
        //vertices.flip();

        count++;


    }


    public void dispose() {
        shaderProgram.delete();
        ebo.delete();
        vbo.delete();
        vao.delete();

    }


}
