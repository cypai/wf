package com.pipai.wf.battle.vision;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.guiobject.battle.AgentGuiObject;

public class FogOfWar {

	private BattleMap map;
	private HashSet<GridPosition> visibleTiles;
	private List<AgentGuiObject> agents;

	public FogOfWar(BattleMap map, List<AgentGuiObject> agents) {
		visibleTiles = new HashSet<GridPosition>();
		this.map = map;
		this.agents = agents;
	}

	public Set<GridPosition> visibleTiles() {
		return visibleTiles;
	}

	public void update() {

	}

}
