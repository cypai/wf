package com.pipai.wf.battle.vision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.guiobject.battle.AgentGuiObject;

public final class FogOfWar {

	public static final Color NEVER_SEEN_COLOR = Color.BLACK;
	public static final Color SEEN_COLOR = Color.GRAY;
	public static final Color VISIBLE_COLOR = Color.WHITE;

	private Texture fogOfWarStateTexture;
	private Pixmap visibilityPixmap;
	private BattleMap map;
	private HashSet<GridPosition> visibleTiles;
	private HashMap<AgentGuiObject, ArrayList<GridPosition>> agentVisibleTiles;
	private List<AgentGuiObject> agents;
	private BattleConfiguration config;

	public FogOfWar(BattleMap map, List<AgentGuiObject> agents) {
		this(map, agents, new BattleConfiguration());
	}

	public FogOfWar(BattleMap map, List<AgentGuiObject> agents, BattleConfiguration config) {
		this.config = config;
		visibleTiles = new HashSet<GridPosition>();
		agentVisibleTiles = new HashMap<AgentGuiObject, ArrayList<GridPosition>>();
		for (AgentGuiObject a : agents) {
			agentVisibleTiles.put(a, new ArrayList<GridPosition>());
		}
		this.map = map;
		this.agents = new ArrayList<AgentGuiObject>(agents);
		visibilityPixmap = new Pixmap(map.getCols(), map.getRows(), Format.RGBA8888);
		fullScan();
	}

	public Texture getFogOfWarStateTexture() {
		return fogOfWarStateTexture;
	}

	public Set<GridPosition> visibleTiles() {
		return visibleTiles;
	}

	public void setVisible(GridPosition tile) {
		visibleTiles.add(tile);
		visibilityPixmap.setColor(VISIBLE_COLOR);
		visibilityPixmap.drawPixel(tile.x, tile.y);
	}

	public void setNotVisible(GridPosition tile) {
		visibleTiles.remove(tile);
		visibilityPixmap.setColor(SEEN_COLOR);
		visibilityPixmap.drawPixel(tile.x, tile.y);
	}

	public void fullScan() {
		visibleTiles.clear();
		if (fogOfWarStateTexture != null) {
			fogOfWarStateTexture.dispose();
		}
		visibilityPixmap.setColor(NEVER_SEEN_COLOR);
		visibilityPixmap.fill();
		visibilityPixmap.setColor(VISIBLE_COLOR);

		for (AgentGuiObject a : agents) {
			GridPosition pos = a.getDisplayPosition();
			visibilityPixmap.fillCircle(pos.x, pos.y, config.sightRange());
			// spiralPathScan(a);
		}
		fogOfWarStateTexture = new Texture(visibilityPixmap);
	}

	public void update(AgentGuiObject changedAgent) {
		fullScan();
	}

	/**
	 * TODO: Will be finished later
	 */
	@SuppressWarnings("unused")
	private void spiralPathScan(AgentGuiObject a) {
		agentVisibleTiles.get(a).clear();
		GridPosition center = a.getDisplayPosition();
		LinkedList<GridPosition> queue = new LinkedList<>();
		initializeSpiralPathQueue(queue, center);
		while (queue.size() > 0) {
			GridPosition tile = queue.poll();
			setVisible(tile);
			agentVisibleTiles.get(a).add(tile);
			BattleMapCell cell = map.getCell(tile);
			if (cell != null && !cell.hasTileSightBlocker() && center.distance(tile) < config.sightRange()) {
				passLight(queue, center, tile);
			}
		}
	}

	private void initializeSpiralPathQueue(LinkedList<GridPosition> queue, GridPosition center) {
		queue.add(new GridPosition(center.x + 1, center.y));
		queue.add(new GridPosition(center.x, center.y + 1));
		queue.add(new GridPosition(center.x - 1, center.y));
		queue.add(new GridPosition(center.x, center.y - 1));
	}

	private void passLight(LinkedList<GridPosition> queue, GridPosition center, GridPosition tile) {
		switch (center.directionTo(tile)) {
		case E:
			queue.add(new GridPosition(tile.x, tile.y - 1));
			queue.add(new GridPosition(tile.x + 1, tile.y));
			queue.add(new GridPosition(tile.x, tile.y + 1));
			break;
		case NE:
			queue.add(new GridPosition(tile.x + 1, tile.y));
			queue.add(new GridPosition(tile.x, tile.y + 1));
			break;
		case N:
			queue.add(new GridPosition(tile.x + 1, tile.y));
			queue.add(new GridPosition(tile.x, tile.y + 1));
			queue.add(new GridPosition(tile.x - 1, tile.y));
			break;
		case NW:
			queue.add(new GridPosition(tile.x, tile.y + 1));
			queue.add(new GridPosition(tile.x - 1, tile.y));
			break;
		case W:
			queue.add(new GridPosition(tile.x, tile.y + 1));
			queue.add(new GridPosition(tile.x - 1, tile.y));
			queue.add(new GridPosition(tile.x, tile.y - 1));
			break;
		case SW:
			queue.add(new GridPosition(tile.x - 1, tile.y));
			queue.add(new GridPosition(tile.x, tile.y - 1));
			break;
		case S:
			queue.add(new GridPosition(tile.x - 1, tile.y));
			queue.add(new GridPosition(tile.x, tile.y - 1));
			queue.add(new GridPosition(tile.x + 1, tile.y));
			break;
		case SE:
			queue.add(new GridPosition(tile.x, tile.y - 1));
			queue.add(new GridPosition(tile.x + 1, tile.y));
			break;
		}
	}

}
