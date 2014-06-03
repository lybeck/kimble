#version 150

uniform sampler2D texture_diffuse;
uniform float textureModulator;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
        vec4 textureColor = texture(texture_diffuse, pass_TextureCoord);
        out_Color = pass_Color * textureColor;
        // out_Color.a = pass_Color.a * 1; //textureColor.a; // * pass_Color;
        // out_Color = pass_Color;
}