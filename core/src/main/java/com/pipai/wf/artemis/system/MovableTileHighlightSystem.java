package com.pipai.wf.artemis.system;

import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Color;
import com.pipai.wf.artemis.components.TileHighlightComponent;
import com.pipai.wf.artemis.event.MovementTileUpdateEvent;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.util.GridPosition;

import net.mostlyoriginal.api.event.common.Subscribe;

public class MovableTileHighlightSystem extends NoProcessingSystem {

	// private static final Logger LOGGER = LoggerFactory.getLogger(MovableTileHighlightSystem.class);

	private static final Color BLUE_MOVE = new Color(0.3f, 0.3f, 0.8f, 0.2f);
	private static final Color GREEN_MOVE = new Color(0.3f, 0.6f, 0, 0.2f);
	private static final Color YELLOW_MOVE = new Color(0.8f, 0.6f, 0, 0.2f);

	private ComponentMapper<TileHighlightComponent> mTileHighlight;

	@Subscribe
	public void handleMovementTileChange(MovementTileUpdateEvent event) {
		removeAllTileHighlights();
		addAllMovementTileHighlights(event.mapgraph);
	}

	private void removeAllTileHighlights() {
		IntBag entities = world.getAspectSubscriptionManager()
				.get(Aspect.all(TileHighlightComponent.class))
				.getEntities();
		int[] ids = entities.getData();
		for (int id : ids) {
			mTileHighlight.remove(id);
		}
	}

	private void addAllMovementTileHighlights(MapGraph mapgraph) {
		int ap = mapgraph.getAP();
		int apmax = mapgraph.getAPMax();
		if (ap == 1) {
			addTileHighlight(mapgraph.getMovableCellPositions(1), YELLOW_MOVE);
			return;
		}
		if (apmax == 2) {
			addTileHighlight(mapgraph.getMovableCellPositions(1), BLUE_MOVE);
			addTileHighlight(mapgraph.getMovableCellPositions(2), YELLOW_MOVE);
		} else if (apmax == 3) {
			if (ap == 2) {
				addTileHighlight(mapgraph.getMovableCellPositions(1), GREEN_MOVE);
				addTileHighlight(mapgraph.getMovableCellPositions(2), YELLOW_MOVE);
			} else {
				addTileHighlight(mapgraph.getMovableCellPositions(1), BLUE_MOVE);
				addTileHighlight(mapgraph.getMovableCellPositions(2), GREEN_MOVE);
				addTileHighlight(mapgraph.getMovableCellPositions(3), YELLOW_MOVE);
			}
		}
	}

	private void addTileHighlight(List<GridPosition> tiles, Color color) {
		for (GridPosition tile : tiles) {
			int id = world.create();
			TileHighlightComponent cTileHighlight = mTileHighlight.create(id);
			cTileHighlight.tile = tile;
			cTileHighlight.color = color;
		}
	}

}
