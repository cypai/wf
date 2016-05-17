package com.pipai.wf.battle.map;

import java.util.ArrayList;
import java.util.List;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.util.Direction;
import com.pipai.wf.util.GridPosition;

public class PeekingSquaresCalculator {

	private BattleMap map;

	public PeekingSquaresCalculator(BattleMap map) {
		this.map = map;
	}

	public List<GridPosition> getPeekingSquares(Agent a) {
		GridPosition actualPosition = a.getPosition();
		ArrayList<GridPosition> l = new ArrayList<>();
		l.add(actualPosition);
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		ArrayList<Direction> coverDirs = coverSystem.getCoverDirections(actualPosition);
		for (Direction coverDir : coverDirs) {
			ArrayList<Direction> perpendicularDirs = Direction.getPerpendicular(coverDir);
			for (Direction perpendicular : perpendicularDirs) {
				BattleMapCell peekSquare = map.getCellInDirection(actualPosition, perpendicular);
				if (peekSquare != null) {
					GridPosition pos = peekSquare.getPosition();
					BattleMapCell peekCoverSquare = map.getCellInDirection(pos, coverDir);

					if (peekSquare.isEmpty() && !peekCoverSquare.hasTileSightBlocker() && !l.contains(pos)) {
						l.add(peekSquare.getPosition());
					}
				}
			}
		}
		return l;
	}

}
