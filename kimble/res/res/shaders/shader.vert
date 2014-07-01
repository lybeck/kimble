#version 150

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

struct Material {
    vec4 lightPosition;
    vec4 diffuse;
    vec4 ambient;
    vec4 specular;
    float shininess;
};

uniform Material material;

in vec3 in_Position;
in vec3 in_Normal;
in vec4 in_Color;
in vec2 in_TextureCoord;

out vec4 pass_Color;
out vec2 pass_TextureCoord;
out vec4 pos;
out vec3 normal;
out vec4 lightDir;

void main(void) {

        //Calculate the normal matrix
        mat4 normalMatrix = transpose(inverse(viewMatrix * modelMatrix));
        normal = normalize(mat3(normalMatrix) * in_Normal);  //view space normal
		pos = viewMatrix * modelMatrix * vec4(in_Position, 1); //view space position
		lightDir = normalize(viewMatrix * material.lightPosition - pos); //view space light vector
        
        float intensity = max(dot(normal, lightDir.xyz), 0.0);

        pass_TextureCoord = vec2(in_TextureCoord.x, 1 - in_TextureCoord.y); //flip y due to problems with how LWJGL maps textures
        pass_Color = in_Color * (intensity * material.diffuse + material.ambient);
       
        gl_Position = projectionMatrix * pos; 
}