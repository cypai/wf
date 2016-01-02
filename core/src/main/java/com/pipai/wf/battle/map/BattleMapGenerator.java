package com.pipai.wf.battle.map;

import java.util.ArrayList;
import java.util.List;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleSchema;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.unit.schema.SlimeSchema;
import com.pipai.wf.unit.schema.UnitSchema;
import com.pipai.wf.util.Rng;

public class BattleMapGenerator {

	private static ArrayList<GridPosition> partyRelativeStartingPositions = new ArrayList<>();

	static {
		partyRelativeStartingPositions.add(new GridPosition(-1, 1));
		partyRelativeStartingPositions.add(new GridPosition(1, 1));
		partyRelativeStartingPositions.add(new GridPosition(-1, 0));
		partyRelativeStartingPositions.add(new GridPosition(1, 0));
		partyRelativeStartingPositions.add(new GridPosition(-1, -1));
		partyRelativeStartingPositions.add(new GridPosition(1, -1));
	}

	private Rng rng;

	public BattleMapGenerator(BattleConfiguration config) {
		rng = config.getRng();
	}

	public BattleMap generateMap(BattleSchema schema) {
		int rows = rng.randInt(30, 40);
		int cols = rng.randInt(30, 40);
		BattleMap map = new BattleMap(rows, cols);
		generateRandomEnvironment(map);
		generatePartyPod(map, schema.getPartySchemas());
		for (int i = 0; i < 1; i++) {
			generateEnemyPod(map);
		}
		return map;
	}

	/**
	 * Generates a random GridPosition within the box specified by bl and ur
	 * @param bl Bottom left corner
	 * @param ur Upper right corner
	 */
	private GridPosition randPos(GridPosition bl, GridPosition ur) {
		int x = rng.randInt(bl.x, ur.x);
		int y = rng.randInt(bl.y, ur.y);
		return new GridPosition(x, y);
	}

	private void generateRandomEnvironment(BattleMap map) {
		for (int i = 0; i < 30; i++) {
			GridPosition pos = randPos(new GridPosition(5, 5), new GridPosition(map.getCols() - 5, map.getRows() - 5));
			map.getCell(pos).setTileEnvironmentObject(new FullCoverIndestructibleObject());
		}
	}

	private void generatePartyPod(BattleMap map, List<UnitSchema> party) {
		AgentStateFactory factory = new AgentStateFactory();
		GridPosition center = randPos(new GridPosition(1, 1), new GridPosition(map.getCols() - 1, 4));
		for (int i = 0; i < partyRelativeStartingPositions.size() && i < party.size(); i++) {
			GridPosition relativePos = partyRelativeStartingPositions.get(i);
			AgentState as = factory.battleAgentFromSchema(Team.PLAYER,
					new GridPosition(center.x + relativePos.x, center.y + relativePos.y),
					party.get(i));
			map.addAgent(as);
		}
	}

	private void generateEnemyIfEmpty(BattleMap map, GridPosition pos) {
		AgentStateFactory factory = new AgentStateFactory();
		if (map.getCell(pos).isEmpty()) {
			map.addAgent(factory.battleAgentFromSchema(Team.ENEMY, pos, new SlimeSchema(1)));
		}
	}

	private void generateEnemyPod(BattleMap map) {
		int amt = 1;
		GridPosition center = randPos(new GridPosition(8, 8), new GridPosition(map.getCols() - 8, map.getRows() - 8));
		List<GridPosition> enemyPos = new ArrayList<>();
		for (int i = 0; i < amt; i++) {
			enemyPos.add(randPos(new GridPosition(center.x - 2, center.y - 2), new GridPosition(center.x + 2, center.y + 2)));
		}
		for (GridPosition pos : enemyPos) {
			generateEnemyIfEmpty(map, pos);
		}
	}

}
