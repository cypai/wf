package com.pipai.wf.battle.vision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.config.WFConfig;
import com.pipai.wf.guiobject.battle.AgentGuiObject;

public class FogOfWar {

	private Texture visibilityTexture;
	private Pixmap visibilityPixmap;
	private BattleMap map;
	private HashSet<GridPosition> visibleTiles;
	private HashMap<AgentGuiObject, ArrayList<GridPosition>> agentVisibleTiles;
	private List<AgentGuiObject> agents;

	public FogOfWar(BattleMap map, List<AgentGuiObject> agents) {
		visibleTiles = new HashSet<GridPosition>();
		agentVisibleTiles = new HashMap<AgentGuiObject, ArrayList<GridPosition>>();
		for (AgentGuiObject a : agents) {
			agentVisibleTiles.put(a, new ArrayList<GridPosition>());
		}
		this.map = map;
		this.agents = new ArrayList<AgentGuiObject>(agents);
		visibilityPixmap = new Pixmap(map.getCols(), map.getRows(), Format.RGBA8888);
		visibilityPixmap.setColor(0, 0, 0, 1);
		fullScan();
		visibilityTexture = new Texture(visibilityPixmap);
	}

	public Texture getFogOfWarTexture() {
		return visibilityTexture;
	}

	public Set<GridPosition> visibleTiles() {
		return visibleTiles;
	}

	public void setVisible(GridPosition tile) {
		visibleTiles.add(tile);
		visibilityPixmap.setColor(0, 0, 0, 1);
		visibilityPixmap.drawPixel(tile.x, tile.y);
	}

	public void setNotVisible(GridPosition tile) {
		visibleTiles.remove(tile);
		visibilityPixmap.setColor(0, 0, 0, 0.5f);
		visibilityPixmap.drawPixel(tile.x, tile.y);
	}

	public void fullScan() {
		visibleTiles.clear();
		visibilityPixmap.setColor(0, 0, 0, 0);
		visibilityPixmap.fill();
		visibilityPixmap.setColor(0, 0, 0, 1);

		for (AgentGuiObject a : agents) {
			GridPosition pos = a.getDisplayPosition();
			visibilityPixmap.fillCircle(pos.x, pos.y, WFConfig.battleProps().sightRange());
//			spiralPathScan(a);
		}
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
		LinkedList<GridPosition> queue = new LinkedList<GridPosition>();
		initializeSpiralPathQueue(queue, center);
		while (queue.size() > 0) {
			GridPosition tile = queue.poll();
			setVisible(tile);
			agentVisibleTiles.get(a).add(tile);
			BattleMapCell cell = map.getCell(tile);
			if (cell != null && !cell.hasTileSightBlocker() && center.distance(tile) < WFConfig.battleProps().sightRange()) {
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
