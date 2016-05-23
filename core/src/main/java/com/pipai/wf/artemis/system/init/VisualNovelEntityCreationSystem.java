package com.pipai.wf.artemis.system.init;

import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pipai.wf.artemis.components.OrthographicCameraComponent;
import com.pipai.wf.artemis.components.PartialTextComponent;
import com.pipai.wf.artemis.components.TextBoxComponent;
import com.pipai.wf.artemis.components.TextListComponent;
import com.pipai.wf.artemis.components.TextListAdvancementStrategyComponent;
import com.pipai.wf.artemis.components.TextListAdvancementStrategyComponent.TextListAdvancementStrategy;
import com.pipai.wf.artemis.components.XYPositionComponent;
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

	private TagManager tagManager;

	private final BatchHelper batch;

	private final VisualNovelScene vnScene;

	public VisualNovelEntityCreationSystem(BatchHelper batch, VisualNovelScene vnScene) {
		this.batch = batch;
		this.vnScene = vnScene;
	}

	@Override
	protected void initialize() {
		OrthographicCamera camera = generateCameras();
		generateEntities(camera);
	}

	private OrthographicCamera generateCameras() {
		int orthoCameraId = world.create();
		OrthographicCameraComponent cCamera = mOrthoCamera.create(orthoCameraId);
		tagManager.register(Tag.ORTHO_CAMERA.toString(), orthoCameraId);
		batch.getShapeRenderer().setProjectionMatrix(cCamera.camera.combined);
		batch.getSpriteBatch().setProjectionMatrix(cCamera.camera.combined);
		return cCamera.camera;
	}

	private void generateEntities(OrthographicCamera camera) {
		int textBoxId = world.create();
		tagManager.register(Tag.VN_SCENE_TEXTBOX.toString(), textBoxId);
		PartialTextComponent cPartialText = mPartialText.create(textBoxId);
		cPartialText.currentText = "";
		cPartialText.fullText = vnScene.getSceneLines().isEmpty() ? "" : vnScene.getSceneLines().get(0);
		cPartialText.textUpdateRate = 1;
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
	}

}
