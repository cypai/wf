package com.pipai.wf.battle.map;

import java.util.ArrayList;
import java.util.List;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.util.UtilFunctions;

public class BattleMapGenerator {
	
	public static BattleMap generateRandomTestMap(List<AgentState> party) {
		int width = UtilFunctions.randInt(30, 40);
		int height = UtilFunctions.randInt(30, 40);
        BattleMap map = new BattleMap(width, height);
        generateRandomEnvironment(map);
        generatePartyPod(map, party);
        for (int i = 0; i < 2; i++) {
        	generateEnemyPod(map);
        }
        return map;
	}
	
	/**
	 * Generates a random GridPosition within the box specified by bl and ur
	 * @param bl Bottom left corner
	 * @param ur Upper right corner
	 */
	private static GridPosition randPos(GridPosition bl, GridPosition ur) {
		int x = UtilFunctions.randInt(bl.x, ur.x);
		int y = UtilFunctions.randInt(bl.y, ur.y);
		return new GridPosition(x, y);
	}
	
	private static void generateRandomEnvironment(BattleMap map) {
		for (int i = 0; i < 30; i++) {
			GridPosition pos = randPos(new GridPosition(5, 5), new GridPosition(map.getCols() - 5, map.getRows() - 5));
	        map.getCell(pos).setTileEnvironmentObject(new FullCoverIndestructibleObject());
		}
	}
	
	private static void generatePartyPod(BattleMap map, List<AgentState> party) {
		GridPosition center = randPos(new GridPosition(1, 1), new GridPosition(map.getCols() - 1, 4));
        map.addAgent(AgentState.battleAgentFromStats(Team.PLAYER, new GridPosition(center.x - 1, center.y), party.get(0)));
        map.addAgent(AgentState.battleAgentFromStats(Team.PLAYER, new GridPosition(center.x + 1, center.y), party.get(1)));
	}
	
	private static void generateEnemyIfEmpty(BattleMap map, GridPosition pos) {
		if (map.getCell(pos).isEmpty()) {
			map.addAgent(AgentState.newBattleAgentState(Team.ENEMY, pos, 3, 5, 2, 5, 65, 0));
		}
	}
	
	private static void generateEnemyPod(BattleMap map) {
		int amt = UtilFunctions.randInt(2, 3);
		GridPosition center = randPos(new GridPosition(8, 8), new GridPosition(map.getCols() - 8, map.getRows() - 8));
		List<GridPosition> enemyPos = new ArrayList<GridPosition>();
		for (int i = 0; i < amt; i++) {
			enemyPos.add(randPos(new GridPosition(center.x - 2, center.y - 2), new GridPosition(center.x + 2, center.y + 2)));
		}
		for (GridPosition pos : enemyPos) {
			generateEnemyIfEmpty(map, pos);
		}
	}
	
}
