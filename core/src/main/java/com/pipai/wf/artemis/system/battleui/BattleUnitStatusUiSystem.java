package com.pipai.wf.artemis.system.battleui;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.pipai.wf.artemis.system.rendering.BatchRenderingSystem;
import com.pipai.wf.battle.agent.Agent;

public class BattleUnitStatusUiSystem extends BaseSystem {

	private BatchRenderingSystem batchRenderingSystem;

	private Skin skin;
	private LabelStyle labelStyle;

	private Stage stage;
	private Label agentName;
	private ProgressBar agentHp;
	private ProgressBar agentMp;
	private InventoryButton[] inventoryButtons;

	public BattleUnitStatusUiSystem() {
		stage = new Stage(new ScreenViewport());
		inventoryButtons = new InventoryButton[3];
		labelStyle = new LabelStyle();
		skin = new Skin();
		skin.add("default", new BitmapFont());
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		ProgressBarStyle progressBarStyleGreen = new ProgressBarStyle();
		progressBarStyleGreen.background = skin.newDrawable("white", new Color(0.5f, 0.5f, 0.5f, 0.7f));
		progressBarStyleGreen.background.setMinHeight(4);
		progressBarStyleGreen.knobBefore = skin.newDrawable("white", new Color(0, 1, 0, 0.66f));
		progressBarStyleGreen.knobBefore.setMinHeight(4);
		progressBarStyleGreen.knobAfter = skin.newDrawable("white", Color.CLEAR);
		progressBarStyleGreen.knobAfter.setMinHeight(4);
		skin.add("progress-bar-green", progressBarStyleGreen);
		ProgressBarStyle progressBarStyleBlue = new ProgressBarStyle();
		progressBarStyleBlue.background = skin.newDrawable("white", new Color(0.5f, 0.5f, 0.5f, 0.7f));
		progressBarStyleBlue.background.setMinHeight(4);
		progressBarStyleBlue.knobBefore = skin.newDrawable("white", new Color(0, 0, 1, 0.66f));
		progressBarStyleBlue.knobBefore.setMinHeight(4);
		progressBarStyleBlue.knobAfter = skin.newDrawable("white", Color.CLEAR);
		progressBarStyleBlue.knobAfter.setMinHeight(4);
		skin.add("progress-bar-blue", progressBarStyleBlue);
	}

	@Override
	protected void initialize() {
		labelStyle.font = batchRenderingSystem.getBatch().getFont();
		generateTopUi();
		generateBottomUi();
	}

	private void generateTopUi() {
		Table topTable = new Table();
		topTable.defaults().width(80);
		topTable.top();
		topTable.left();
		topTable.setFillParent(true);
		Image faceImage = new Image(new TextureRegion(new Texture(Gdx.files.internal("graphics/face/face.bmp"))));
		topTable.add(faceImage).top();
		Table topInfoTable = new Table();
		topInfoTable.top();
		topInfoTable.left();
		topTable.add(topInfoTable).top();
		agentName = new Label("", labelStyle);
		topInfoTable.add(agentName).left().padLeft(4).padTop(4).padBottom(4).width(80).height(16);
		topInfoTable.row();
		agentHp = new ProgressBar(0, 1, 0.01f, false, skin.get("progress-bar-green", ProgressBarStyle.class));
		topInfoTable.add(agentHp).padLeft(4).padTop(4).padBottom(4);
		topInfoTable.row();
		agentMp = new ProgressBar(0, 1, 0.01f, false, skin.get("progress-bar-blue", ProgressBarStyle.class));
		topInfoTable.add(agentMp).padLeft(4).padTop(4).padBottom(4);
		topInfoTable.row();
		stage.addActor(topTable);
	}

	private void generateBottomUi() {
		Table bottomTable = new Table();
		bottomTable.defaults().width(120);
		bottomTable.left();
		bottomTable.bottom();
		ButtonStyle buttonStyle = new ButtonStyle();
		buttonStyle.up = new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("graphics/ui/button_up.png"))));
		buttonStyle.down = new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("graphics/ui/button_down.png"))));
		for (int i = 0; i < 3; i++) {
			InventoryButton button = new InventoryButton(buttonStyle, labelStyle);
			bottomTable.add(button).bottom();
			bottomTable.row();
			inventoryButtons[i] = button;
		}
		stage.addActor(bottomTable);
	}

	public void updateSelectedAgentUi(Agent agent) {
		agentName.setText(agent.getName());
		agentHp.setValue((float) agent.getHP() / (float) agent.getMaxHP());
		agentMp.setValue((float) agent.getHP() / (float) agent.getMaxHP());
		for (int i = 0; i < inventoryButtons.length; i++) {
			InventoryButton itemButton = inventoryButtons[i];
			itemButton.label.setText(agent.getInventory().getItemName(i + 1));
		}
	}

	@Override
	protected void processSystem() {
		stage.act();
		stage.draw();
	}

	public Stage getStage() {
		return stage;
	}

	private class InventoryButton extends Button {
		private Label label;

		InventoryButton(ButtonStyle buttonStyle, LabelStyle labelStyle) {
			super(buttonStyle);
			super.left();
			super.padTop(4);
			super.padBottom(4);
			super.padLeft(4);
			super.padRight(4);
			label = new Label("", labelStyle);
			super.add(label);
		}
	}

}
