package graphics.test;

import static org.lwjgl.opengl.GL30.*;

public class VAO {

    private final int id;

    public VAO() { id = glGenVertexArrays(); }

    public void bind() { glBindVertexArray(id); }

    public void unbind() { glBindVertexArray(0); }

    public void delete() { glDeleteVertexArrays(id); }

    public int getId() { return id; }

}
