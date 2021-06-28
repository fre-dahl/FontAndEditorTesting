#version 330 core
layout(location=0) in vec2 aPos;
layout(location=1) in vec3 aColor;
layout(location=2) in vec2 aTexCoords;

out vec2 fTexCoords;
out vec3 fColor;

void main()
{
    fTexCoords = aTexCoords;
    fColor = aColor;
    gl_Position = vec4(aPos, 1, 1);
}
