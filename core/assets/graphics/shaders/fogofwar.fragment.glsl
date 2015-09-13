#ifdef GL_ES 
precision mediump float;
#endif

uniform sampler2D u_texture;

varying vec2 v_texCoord;

uniform vec4 v_lightColor;

void main() {
	vec4 diffuseColor = texture2D(u_texture, v_texCoord);
	gl_FragColor = diffuseColor * v_lightColor;
}