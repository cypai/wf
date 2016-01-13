package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.collision.Ray;
import com.pipai.wf.artemis.components.MouseHoverTileComponent;
import com.pipai.wf.artemis.event.MouseHoverRayEvent;
import com.pipai.wf.battle.map.GridPosition;

import net.mostlyoriginal.api.event.common.Subscribe;

public class MouseHoverTileSystem extends BaseSystem {

	// private static final Logger LOGGER = LoggerFactory.getLogger(MouseHoverTileSystem.class);

	private ComponentMapper<MouseHoverTileComponent> mMouseTile;

	private GridPosition hoverTile;

	public MouseHoverTileSystem() {
		hoverTile = new GridPosition(0, 0);
	}

	@Subscribe
	public void processMouseHoverRayEvent(MouseHoverRayEvent event) {
		Ray ray = event.pickRay;
		GridPosition newHoverTile = TileRayIntersector.getIntersectedTile(ray);
		if (!newHoverTile.equals(hoverTile)) {
			hoverTile = newHoverTile;
			IntBag entities = world.getAspectSubscriptionManager()
					.get(Aspect.all(MouseHoverTileComponent.class))
					.getEntities();
			int[] ids = entities.getData();
			for (int id : ids) {
				mMouseTile.remove(id);
			}
			int id = world.create();
			mMouseTile.create(id).tile = hoverTile;
		}
	}

	@Override
	protected boolean checkProcessing() {
		return false;
	}

	@Override
	protected void processSystem() {
		// Do nothing
	}

}
