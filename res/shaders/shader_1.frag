#version 150 core

uniform sampler2D texture_diffuse;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

in vec4 eyePosition;
in vec4 eyeNormal;

out vec4 out_Color;


void main(void) {

        vec4 s = normalize(lightPos - eyePosition);
        vec4 r = reflect(-s, eyeNormal);
        vec4 v = normalize(-eyePosition);

        float spec = max(dot(v, r), 0.0);
        float diff = max(dot(eyeNormal, s), 0.0);

        vec4 specColor = diff * vec4(1, 0, 0, 1);
        vec4 diffColor = pow(spec, 3) * vec4(1, 1, 1, 1);
        vec4 ambientColor = vec4(0.1, 0.1, 0.1, 1);

        out_Color = diffColor + 0.5 * specColor + ambientColor;
  
        // out_Color = pass_Color;
	// out_Color = texture2D(texture_diffuse, pass_TextureCoord);
}