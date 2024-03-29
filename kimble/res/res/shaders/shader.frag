#version 150

uniform sampler2D texture_diffuse;
uniform float textureModulator;

struct Material {
    vec4 lightPosition;
    vec4 diffuse;
    vec4 ambient;
    vec4 specular;
    float shininess;
};

uniform Material material;

in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec4 pos;
in vec3 normal;
in vec4 lightDir;

out vec4 out_Color;

void main(void) {

		
		//specularity calculations
        vec4 eye = normalize(pos);
		vec3 R = normalize(reflect(lightDir.xyz, normal)); //mirror ray from the light source (view space)
		float intSpec = pow(max(dot(R, eye.xyz), 0.0), material.shininess); //alignment between eye and mirror ray
        vec4 spec = material.specular * intSpec;
		spec.a = min(intSpec, 1);
		
		//textureModulator determines whether the color will be taken from vertex or texture, lighting info will always be taken from vertex
        vec4 textureColor = mix(texture(texture_diffuse, pass_TextureCoord), vec4(1,1,1,1), 1 - textureModulator);
        vec4 vertexColor = mix(pass_Color, vec4(length(pass_Color.rgb), length(pass_Color.rgb), length(pass_Color.rgb), pass_Color.a), textureModulator); //if texture modulator is 1, use only the brightness value of the vertex color
        out_Color = textureColor * (vertexColor + spec);
}