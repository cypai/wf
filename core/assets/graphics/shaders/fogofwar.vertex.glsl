attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

varying vec2 v_texCoord;

void main() {
	gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
	v_texCoord = a_texCoord0;
}