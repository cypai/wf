package com.pipai.wf.battle.ai;

import java.util.ArrayList;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;

public class TopModularAI extends AI {
	
	protected ArrayList<Agent> enemyAgents, playerAgents;

	public TopModularAI(BattleController battleController) {
		super(battleController);
		this.enemyAgents = new ArrayList<Agent>();
		this.playerAgents = new ArrayList<Agent>();
		for (Agent a : this.map.getAgents()) {
			if (a.getTeam() == Team.ENEMY) {
				this.enemyAgents.add(a);
			} else if (a.getTeam() == Team.PLAYER) {
				this.playerAgents.add(a);
			}
		}
	}

	@Override
	public void performMove() {
		
	}
	
}
