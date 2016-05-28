package com.pipai.wf.artemis.system.init;

import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pipai.wf.artemis.components.OrthographicCameraComponent;
import com.pipai.wf.artemis.components.XYPositionComponent;
import com.pipai.wf.artemis.components.textbox.PartialTextComponent;
import com.pipai.wf.artemis.components.textbox.SwitchScreenOnTextListCompletionComponent;
import com.pipai.wf.artemis.components.textbox.TextBoxComponent;
import com.pipai.wf.artemis.components.textbox.TextListAdvancementStrategyComponent;
import com.pipai.wf.artemis.components.textbox.TextListAdvancementStrategyComponent.TextListAdvancementStrategy;
import com.pipai.wf.artemis.components.textbox.TextListComponent;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.Tag;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.scenario.VisualNovelScene;

/**
 * This system creates all initial entities for the Battle Screen
 */
public class VisualNovelEntityCreationSystem extends NoProcessingSystem {

	private ComponentMapper<OrthographicCameraComponent> mOrthoCamera;
	private ComponentMapper<PartialTextComponent> mPartialText;
	private ComponentMapper<TextBoxComponent> mTextBox;
	private ComponentMapper<XYPositionComponent> mXy;

	private ComponentMapper<TextListAdvancementStrategyComponent> mIterationStrategy;
	private ComponentMapper<TextListComponent> mTextList;

	private ComponentMapper<SwitchScreenOnTextListCompletionComponent> mNextScreen;

	private TagManager tagManager;

	private final BatchHelper batch;

	private final VisualNovelScene vnScene;
	private final Screen nextScreen;

	public VisualNovelEntityCreationSystem(BatchHelper batch, VisualNovelScene vnScene, Screen nextScreen) {
		this.batch = batch;
		this.vnScene = vnScene;
		this.nextScreen = nextScreen;
	}

	@Override
	protected void initialize() {
		OrthographicCamera camera = generateCameras();
		generateTextEntities(camera);
	}

	private OrthographicCamera generateCameras() {
		int orthoCameraId = world.create();
		OrthographicCameraComponent cCamera = mOrthoCamera.create(orthoCameraId);
		tagManager.register(Tag.ORTHO_CAMERA.toString(), orthoCameraId);
		batch.getShapeRenderer().setProjectionMatrix(cCamera.camera.combined);
		batch.getSpriteBatch().setProjectionMatrix(cCamera.camera.combined);
		return cCamera.camera;
	}

	private void generateTextEntities(OrthographicCamera camera) {
		int textBoxId = world.create();
		tagManager.register(Tag.VN_SCENE_TEXTBOX.toString(), textBoxId);
		PartialTextComponent cPartialText = mPartialText.create(textBoxId);
		cPartialText.currentText = "";
		cPartialText.fullText = vnScene.getSceneLines().isEmpty() ? "" : vnScene.getSceneLines().get(0);
		cPartialText.textUpdateRate = 3;
		cPartialText.timerSlowness = 1;

		TextBoxComponent cTextBox = mTextBox.create(textBoxId);
		cTextBox.width = Gdx.graphics.getWidth();
		cTextBox.height = Gdx.graphics.getHeight() / 3;

		XYPositionComponent cXy = mXy.create(textBoxId);
		cXy.position.set(cTextBox.width / 2, cTextBox.height / 2);

		TextListComponent cTextList = mTextList.create(textBoxId);
		cTextList.textQueue = vnScene.getSceneLines();

		TextListAdvancementStrategyComponent cIterationStrategy = mIterationStrategy.create(textBoxId);
		cIterationStrategy.advancementStrategy = TextListAdvancementStrategy.USER_INPUT;

		SwitchScreenOnTextListCompletionComponent cNextScreen = mNextScreen.create(textBoxId);
		cNextScreen.nextScreen = nextScreen;
	}

}
