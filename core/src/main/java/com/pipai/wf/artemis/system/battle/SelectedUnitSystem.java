package com.pipai.wf.artemis.system.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.pipai.wf.artemis.components.AgentComponent;
import com.pipai.wf.artemis.components.PlayerUnitComponent;
import com.pipai.wf.artemis.components.SelectedUnitComponent;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.event.LeftClickEvent;
import com.pipai.wf.artemis.event.MovementTileUpdateEvent;
import com.pipai.wf.artemis.system.Group;
import com.pipai.wf.artemis.system.Tag;
import com.pipai.wf.artemis.system.UiSystem;
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
	private ComponentMapper<XYZPositionComponent> mXyzPosition;
	private ComponentMapper<AgentComponent> mAgent;

	private TagManager tagManager;
	private GroupManager groupManager;
	private EventSystem eventSystem;

	private UiSystem uiSystem;
	private BattleSystem battleSystem;
	private CameraInterpolationMovementSystem cameraInterpolationMovementSystem;

	private MapGraph selectedMapGraph;

	private boolean processing;

	public SelectedUnitSystem() {
		super(ASPECT_MATCHER);
		processing = true;
	}

	public void updateForSelectedAgent() {
		Agent cAgent = getSelectedAgent();

		if (cAgent.getAP() > 0) {
			selectedMapGraph = new MapGraph(battleSystem.getBattleMap(),
					cAgent.getPosition(), cAgent.getEffectiveMobility(), cAgent.getAP(), cAgent.getMaxAP());
			eventSystem.dispatch(new MovementTileUpdateEvent(selectedMapGraph));
		} else {
			List<Entity> party = getSortedAvailablePlayerParty();
			if (party.isEmpty()) {
				battleSystem.getBattleController().endTurn();
			} else {
				selectNext(party);
			}
		}
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
		updateForSelectedAgent();
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
			cameraInterpolationMovementSystem.beginMovingCamera(mXyzPosition.get(entityId).position);
			processing = false;
		}
	}

	private void selectNext(List<Entity> party) {
		boolean found = false;
		for (Entity e : party) {
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
		mSelectedUnit.create(party.get(0));
	}

	private List<Entity> getSortedAvailablePlayerParty() {
		ImmutableBag<Entity> partyEntities = groupManager.getEntities(Group.PLAYER_PARTY.toString());
		List<Entity> party = new ArrayList<>();
		for (Entity e : partyEntities) {
			party.add(e);
		}
		party.sort((e1, e2) -> {
			int i1 = mPlayerUnit.get(e1).index;
			int i2 = mPlayerUnit.get(e2).index;
			return i1 > i2 ? 1 : (i1 < i2 ? -1 : 0);
		});
		List<Entity> filteredParty = party.stream()
				.filter((e) -> mAgent.get(e).agent.getAP() > 0)
				.collect(Collectors.toList());
		return filteredParty;
	}

	public Agent getSelectedAgent() {
		Entity selected = tagManager.getEntity(Tag.SELECTED_UNIT.toString());
		return mAgent.get(selected).agent;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.SHIFT_LEFT) {
			selectNext(getSortedAvailablePlayerParty());
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
