package com.pipai.wf.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pipai.wf.WFGame;
import com.pipai.wf.artemis.screen.ArtemisBattleScreen;
import com.pipai.wf.artemis.screen.WorldMapScreen;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleFactory;
import com.pipai.wf.battle.BattleSchema;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.partyinfo.AbilityTreeDisplay;
import com.pipai.wf.guiobject.partyinfo.PartyInfoList;
import com.pipai.wf.save.Save;
import com.pipai.wf.save.SaveManager;
import com.pipai.wf.unit.abilitytree.UnitClassTree;
import com.pipai.wf.unit.schema.UnitSchema;

public final class PartyInfoGui extends Gui {

	private static final Logger LOGGER = LoggerFactory.getLogger(PartyInfoGui.class);

	private OrthographicCamera camera;
	private ArrayList<GuiRenderable> renderables, renderablesCreateBuffer, renderablesDelBuffer;
	private List<? extends UnitSchema> partySchema = new ArrayList<>();

	public PartyInfoGui(WFGame game, List<? extends UnitSchema> party) {
		super(game);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, getScreenWidth(), getScreenHeight());
		renderables = new ArrayList<GuiRenderable>();
		renderables.add(new AbilityTreeDisplay(this, UnitClassTree.RANGER.getAbilityTree(), getScreenWidth() * 3 / 4,
				getScreenHeight() - 64));
		renderablesCreateBuffer = new ArrayList<GuiRenderable>();
		renderablesDelBuffer = new ArrayList<GuiRenderable>();
		partySchema = party;
		createInstance(new PartyInfoList(this, party, 4, getScreenHeight() - 4, getScreenWidth() / 2,
				getScreenHeight() / 2, Color.CYAN));
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
		BatchHelper batch = getBatch();
		batch.getSpriteBatch().setProjectionMatrix(camera.combined);
		batch.getShapeRenderer().setProjectionMatrix(camera.combined);
		for (GuiRenderable r : renderables) {
			r.render(batch);
		}
		cleanDelBuffers();
		cleanCreateBuffers();
	}

	@Override
	public void onLeftClick(int screenX, int screenY) {
		BattleFactory factory = new BattleFactory(new BattleConfiguration());
		switchGui(new ArtemisBattleScreen(getGame(), factory.build(new BattleSchema(partySchema))));
	}

	@Override
	public void onRightClick(int screenX, int screenY) {

	}

	@Override
	public void onKeyDown(int keycode) {
		if (keycode == Keys.ESCAPE) {
			Save save = new Save();
			save.setParty(partySchema);
			SaveManager manager = new SaveManager();
			try {
				LOGGER.debug("Saving game...");
				manager.save(save, 1);
				LOGGER.debug("Done. Exiting...");
			} catch (IOException e) {
				LOGGER.error("Could not save game", e);
			}
			Gdx.app.exit();
		} else if (keycode == Keys.W) {
			switchGui(new WorldMapScreen(getGame()));
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
