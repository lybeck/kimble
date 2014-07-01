#version 150

uniform sampler2D texture_diffuse;
uniform float textureModulator;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
		//textureModulator determines whether the color will be taken from vertex or texture, lighting info will always be taken from vertex
        vec4 textureColor = mix(texture(texture_diffuse, pass_TextureCoord), vec4(1,1,1,1), 1 - textureModulator);
        vec4 vertexColor = mix(pass_Color, vec4(length(pass_Color.rgb), length(pass_Color.rgb), length(pass_Color.rgb), pass_Color.a), textureModulator); //if texture modulator is 1, use only the brightness value of the vertex color
        out_Color = textureColor * vertexColor;
}