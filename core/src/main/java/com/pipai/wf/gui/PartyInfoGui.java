package com.pipai.wf.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pipai.wf.WFGame;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapGenerator;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.ui.PartyInfoList;
import com.pipai.wf.unit.race.Race;
import com.pipai.wf.unit.schema.FlameFairySchema;
import com.pipai.wf.unit.schema.RaceTemplateSchema;
import com.pipai.wf.unit.schema.TidusSchema;
import com.pipai.wf.unit.schema.UnitSchema;

public class PartyInfoGui extends Gui {

	private OrthographicCamera camera;
	private ArrayList<GuiRenderable> renderables, renderablesCreateBuffer, renderablesDelBuffer;
	private ArrayList<UnitSchema> partySchema = new ArrayList<>();
	private ArrayList<AgentState> party = new ArrayList<>();

	public PartyInfoGui(WFGame game) {
		super(game);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, this.getScreenWidth(), this.getScreenHeight());
		renderables = new ArrayList<GuiRenderable>();
		renderablesCreateBuffer = new ArrayList<GuiRenderable>();
		renderablesDelBuffer = new ArrayList<GuiRenderable>();
		partySchema = new ArrayList<UnitSchema>();
		partySchema.add(new TidusSchema());	// Tidus
		partySchema.add(new RaceTemplateSchema(Race.HUMAN));	// Sienna
		partySchema.add(new RaceTemplateSchema(Race.FAIRY));	// Sapphire
		partySchema.add(new RaceTemplateSchema(Race.CAT));	// Mira
		partySchema.add(new FlameFairySchema());	// Sunny
		partySchema.add(new RaceTemplateSchema(Race.FOX));	// Nolan
		party = new ArrayList<AgentState>();
		for (UnitSchema us : partySchema) {
			party.add(AgentStateFactory.createFromSchema(us));
		}
		this.createInstance(new PartyInfoList(this, party, 4, this.getScreenHeight() - 4, this.getScreenWidth() / 2, this.getScreenHeight() / 2, Color.CYAN));
	}

	@Override
	public void createInstance(GuiObject o) {
		super.createInstance(o);
		if (o instanceof GuiRenderable) {
			renderablesCreateBuffer.add((GuiRenderable) o);
		}
	}

	@Override
	public void deleteInstance(GuiObject o) {
		super.deleteInstance(o);
		if (o instanceof GuiRenderable) {
			renderablesDelBuffer.add((GuiRenderable) o);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.getSpriteBatch().setProjectionMatrix(camera.combined);
		batch.getShapeRenderer().setProjectionMatrix(camera.combined);
		for (GuiRenderable r : this.renderables) {
			r.render(batch);
		}
		cleanDelBuffers();
		cleanCreateBuffers();
	}

	@Override
	public void onLeftClick(int screenX, int screenY) {
		BattleMap map = BattleMapGenerator.generateRandomTestMap(partySchema);
		this.game.setScreen(new BattleGui(this.game, map));
		this.dispose();
	}

	@Override
	public void onRightClick(int screenX, int screenY) {

	}

	@Override
	public void onKeyDown(int keycode) {
		if (keycode == Keys.ESCAPE) {
			Gdx.app.exit();
		}
	}

	@Override
	public void onKeyUp(int keycode) {

	}

	@Override
	public void mouseScrolled(int amount) {

	}

	private void cleanCreateBuffers() {
		for (GuiRenderable o : renderablesCreateBuffer) {
			renderables.add(o);
		}
		renderablesCreateBuffer.clear();
	}

	private void cleanDelBuffers() {
		for (GuiRenderable o : renderablesDelBuffer) {
			renderables.remove(o);
		}
		renderablesDelBuffer.clear();
	}

}
