package com.pipai.wf.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class FogOfWarShader implements Shader {

	private ShaderProgram program;
	private int u_projViewTrans;
	private int u_worldTrans;
	private int u_texture, u_fogOfWarTexture;
	private int v_lightColor;
	private int v_gridPos;

	@Override
	public void dispose() {
		program.dispose();
	}

	@Override
	public void init() {
		String vert = Gdx.files.internal("graphics/shaders/fogofwar.vertex.glsl").readString();
		String frag = Gdx.files.internal("graphics/shaders/fogofwar.fragment.glsl").readString();
		program = new ShaderProgram(vert, frag);
		if (!program.isCompiled()) {
			throw new GdxRuntimeException(program.getLog());
		}
		u_projViewTrans = program.getUniformLocation("u_projViewTrans");
		u_worldTrans = program.getUniformLocation("u_worldTrans");
		v_lightColor = program.getUniformLocation("v_lightColor");
		u_texture = program.getUniformLocation("u_texture");
		u_fogOfWarTexture = program.getUniformLocation("u_fogOfWarTexture");
		v_gridPos = program.getUniformLocation("v_gridPos");
	}

	@Override
	public int compareTo(Shader other) {
		return 0;
	}

	@Override
	public boolean canRender(Renderable instance) {
		return true;
	}

	@Override
	public void begin(Camera camera, RenderContext context) {
		program.begin();
		program.setUniformMatrix(u_projViewTrans, camera.combined);
		context.setDepthTest(GL20.GL_LEQUAL);
		context.setCullFace(GL20.GL_BACK);
	}

	@Override
	public void render(Renderable renderable) {
		DirectionalLightsAttribute light = (DirectionalLightsAttribute)renderable.environment.get(DirectionalLightsAttribute.Type);
		GridPositionAttribute gpos = (GridPositionAttribute)renderable.material.get(GridPositionAttribute.Type);
		program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
		program.setUniformi(u_texture, 0);
		program.setUniformi(u_fogOfWarTexture, 1);
		program.setUniformf(v_lightColor, light.lights.first().color);
		program.setUniformf(v_gridPos, gpos.value.x, gpos.value.y);
		renderable.mesh.render(program,
				renderable.primitiveType,
				renderable.meshPartOffset,
				renderable.meshPartSize);
	}

	@Override
	public void end() {
		program.end();
	}

}
