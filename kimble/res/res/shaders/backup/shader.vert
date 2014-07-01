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

void main(void) {
	
        vec4 spec = vec4(0.0);

        //Calculate the normal matrix
        mat4 normalMatrix = transpose(inverse(viewMatrix * modelMatrix));
        vec3 normal = normalize(mat3(normalMatrix) * in_Normal);  //view space normal
		vec4 pos = viewMatrix * modelMatrix * vec4(in_Position, 1); //view space position
		vec4 lightDir = normalize(viewMatrix * material.lightPosition - pos); //view space light vector
        
        float intensity = max(dot(normal, lightDir.xyz), 0.0);

		
		//for more accurate reflections, move specularity calculations to fragment shader
        if(intensity > 0.0){
            vec4 eye = normalize(pos);

			vec3 R = reflect(lightDir.xyz, normal); //mirror ray from the light source (view space)
			float intSpec = max(dot(R, eye.xyz), 0.0); //alignment between eye and mirror ray
            spec = material.specular * pow(intSpec, material.shininess);
        }

        pass_TextureCoord = vec2(in_TextureCoord.x, 1 - in_TextureCoord.y); //flip y due to problems with how LWJGL maps textures
        pass_Color = in_Color * (intensity * material.diffuse + spec + material.ambient);
       
        gl_Position = projectionMatrix * pos; 
}