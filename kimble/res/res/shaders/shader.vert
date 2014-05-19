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
        vec4 normal = normalize(normalMatrix * vec4(in_Normal, 0.0));
        
        float intensity = max(dot(normal, viewMatrix * material.lightPosition), 0.0);

        if(intensity > 0.0){
            vec4 pos = viewMatrix * modelMatrix * vec4(in_Position, 1);
            vec4 eye = normalize(-pos);
            vec4 h = normalize(viewMatrix * material.lightPosition + eye);

            float intSpec = max(dot(h, normal), 0.0);
            spec = material.specular * pow(intSpec, material.shininess);
        }

	pass_TextureCoord = in_TextureCoord;
        pass_Color = in_Color * max(intensity * material.diffuse + spec, material.ambient);
       
        gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(in_Position, 1);
}