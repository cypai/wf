package com.pipai.wf.artemis.system;

import java.util.PriorityQueue;

import com.artemis.Aspect;
import com.artemis.Aspect.Builder;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.artemis.components.AgentComponent;
import com.pipai.wf.artemis.components.EndpointsComponent;
import com.pipai.wf.artemis.components.InterpolationComponent;
import com.pipai.wf.artemis.components.PlayerUnitComponent;
import com.pipai.wf.artemis.components.SelectedUnitComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.event.LeftClickEvent;
import com.pipai.wf.artemis.event.MovementTileUpdateEvent;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.MapGraph;

import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.event.common.Subscribe;

public class SelectedUnitSystem extends IteratingSystem implements InputProcessor {

	// private static final Logger LOGGER = LoggerFactory.getLogger(SelectedUnitSystem.class);

	private static final Builder ASPECT_MATCHER = Aspect.all(PlayerUnitComponent.class, SelectedUnitComponent.class,
			XYZPositionComponent.class);

	private ComponentMapper<PlayerUnitComponent> mPlayerUnit;
	private ComponentMapper<SelectedUnitComponent> mSelectedUnit;
	private ComponentMapper<InterpolationComponent> mInterpolation;
	private ComponentMapper<XYZPositionComponent> mXyzPosition;
	private ComponentMapper<EndpointsComponent> mEndpoints;
	private ComponentMapper<AgentComponent> mAgentInventory;

	private TagManager tagManager;
	private GroupManager groupManager;
	private EventSystem eventSystem;

	private UiSystem uiSystem;
	private BattleSystem battleSystem;

	private MapGraph selectedMapGraph;

	private boolean processing;

	public SelectedUnitSystem() {
		super(ASPECT_MATCHER);
		processing = true;
	}

	@Override
	protected boolean checkProcessing() {
		return processing;
	}

	@Override
	protected void inserted(int e) {
		// LOGGER.debug("Unit was selected: " + e);
		Entity previous = tagManager.getEntity(Tag.SELECTED_UNIT.toString());
		if (previous != null) {
			mSelectedUnit.remove(previous);
		}
		tagManager.register(Tag.SELECTED_UNIT.toString(), world.getEntity(e));
		Agent cAgent = mAgentInventory.get(e).agent;
		selectedMapGraph = new MapGraph(battleSystem.getBattleMap(),
				cAgent.getPosition(), cAgent.getEffectiveMobility(), cAgent.getAP(), cAgent.getMaxAP());
		eventSystem.dispatch(new MovementTileUpdateEvent(selectedMapGraph));
		uiSystem.updateSelectedAgentUi(getSelectedAgent());
		processing = true;
	}

	@Subscribe
	public void clickListener(LeftClickEvent event) {
		PlayerUnitComponent player = mPlayerUnit.get(event.clickedEntityId);
		if (player != null) {
			mSelectedUnit.create(event.clickedEntityId);
		}
	}

	@Override
	protected void process(int entityId) {
		if (processing) {
			beginMovingCamera(mXyzPosition.get(entityId).position);
			processing = false;
		}
	}

	private void beginMovingCamera(Vector3 destination) {
		Entity camera = tagManager.getEntity(Tag.CAMERA.toString());
		XYZPositionComponent cXyz = mXyzPosition.get(camera);
		InterpolationComponent cInterpolation = mInterpolation.create(camera);
		EndpointsComponent cEndpoints = mEndpoints.create(camera);
		cEndpoints.start = cXyz.position.cpy();
		cEndpoints.end = destination.cpy();
		cEndpoints.end.z = cXyz.position.z;
		cInterpolation.interpolation = Interpolation.sineOut;
		// Reset t if interpolation pre-exists
		cInterpolation.t = 0;
		cInterpolation.maxT = 20;
		// LOGGER.debug("Camera is moving to " + cEndpoints.end);
	}

	private void selectNext() {
		PriorityQueue<Entity> partyQueue = getSortedPlayerParty();
		boolean found = false;
		for (Entity e : partyQueue) {
			if (found) {
				// Previous entity was selected, add selected component to next one, let inserted() handle it
				mSelectedUnit.create(e);
				return;
			}
			// Look for the currently selected entity
			if (mSelectedUnit.has(e)) {
				found = true;
			}
		}
		// Select the first one - selected unit was the last one
		mSelectedUnit.create(partyQueue.peek());
	}

	private PriorityQueue<Entity> getSortedPlayerParty() {
		ImmutableBag<Entity> party = groupManager.getEntities(Group.PLAYER_PARTY.toString());
		PriorityQueue<Entity> queue = new PriorityQueue<>(party.size(),
				(e1, e2) -> {
					int i1 = mPlayerUnit.get(e1).index;
					int i2 = mPlayerUnit.get(e2).index;
					return i1 > i2 ? 1 : (i1 < i2 ? -1 : 0);
				});
		for (Entity e : party) {
			queue.add(e);
		}
		return queue;
	}

	public Agent getSelectedAgent() {
		Entity selected = tagManager.getEntity(Tag.SELECTED_UNIT.toString());
		return mAgentInventory.get(selected).agent;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.SHIFT_LEFT) {
			selectNext();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
