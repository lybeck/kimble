#version 150 core

uniform sampler2D texture_diffuse;
uniform float textureModulator;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
        vec4 textureColor = texture2D(texture_diffuse, pass_TextureCoord) * textureModulator;
        vec4 vertexColor = pass_Color * (1.0 - textureModulator);
        out_Color = textureColor + vertexColor;
}