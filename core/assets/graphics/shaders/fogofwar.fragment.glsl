#ifdef GL_ES 
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform sampler2D u_fogOfWarTexture;

varying vec2 v_texCoord;

uniform vec4 v_lightColor;
uniform vec2 v_gridPos;

void main() {
	vec4 diffuseColor = texture2D(u_texture, v_texCoord);
	vec4 fogOfWar = texture2D(u_fogOfWarTexture, v_gridPos);
	gl_FragColor = diffuseColor * v_lightColor * fogOfWar;
}