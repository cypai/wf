package com.pipai.wf.battle.vision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.config.WFConfig;
import com.pipai.wf.guiobject.battle.AgentGuiObject;

public class FogOfWar {

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
		this.agents = agents;
	}

	public Set<GridPosition> visibleTiles() {
		return visibleTiles;
	}

	public void fullScan() {
		visibleTiles.clear();
		for (AgentGuiObject a : agents) {
			spiralPathScan(a);
		}
	}

	public void update(AgentGuiObject changedAgent) {
		fullScan();
	}

	private void spiralPathScan(AgentGuiObject a) {
		agentVisibleTiles.get(a).clear();
		GridPosition center = a.getDisplayPosition();
		LinkedList<GridPosition> queue = new LinkedList<GridPosition>();
		initializeSpiralPathQueue(queue, center);
		while (queue.size() > 0) {
			GridPosition tile = queue.poll();
			visibleTiles.add(tile);
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
