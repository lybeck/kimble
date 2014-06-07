#version 150

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform vec4 textColor;

in vec3 in_Position;
in vec3 in_Normal;
in vec4 in_Color;
in vec2 in_TextureCoord;

out vec4 pass_Color;
out vec2 pass_TextureCoord;

void main(void) {
	
        pass_TextureCoord = in_TextureCoord;
        pass_Color = in_Color * textColor;
       
        gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(in_Position, 1);
}