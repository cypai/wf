#ifdef GL_ES 
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform sampler2D u_fogOfWarTexture;
uniform vec4 u_lightColor;

varying vec2 v_texCoord;
varying vec2 v_gridPos;

void main() {
	vec4 diffuseColor = texture2D(u_texture, v_texCoord);
	vec4 fogOfWarColor = texture2D(u_fogOfWarTexture, v_gridPos);
	gl_FragColor = diffuseColor;
}