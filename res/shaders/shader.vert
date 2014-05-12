#version 150 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform vec4 team_Color;

in vec3 in_Position;
in vec3 in_Normal;
in vec4 in_Color;
in vec2 in_TextureCoord;

out vec4 pass_Color;
out vec2 pass_TextureCoord;

vec4 lightPosition = vec4(0, 20, 0, 1);

void main(void) {
	

        //Calculate the normal matrix
        mat4 normalMatrix = transpose(inverse(viewMatrix * modelMatrix));
        vec4 normal = normalize(normalMatrix * vec4(in_Normal, 0.0));
        
        float intensity = max(dot(normal, lightPosition), 0.0);


	pass_TextureCoord = in_TextureCoord;
        pass_Color = in_Color * team_Color * intensity;
        // pass_Color = in_Color * team_Color;
       
        gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(in_Position, 1);
}