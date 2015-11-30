package com.pipai.wf.battle;

import java.util.ArrayList;
import java.util.List;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.unit.schema.UnitSchema;

public final class BattleResult {

	private Result result;
	private ArrayList<UnitSchema> party;

	public BattleResult(Result result, List<Agent> partyAgents) {
		this.result = result;
		party = new ArrayList<>();
		for (Agent a : partyAgents) {
			party.add(new UnitSchema(new AgentState(a)));
		}
	}

	public Result getResult() {
		return result;
	}

	public List<UnitSchema> getPartyState() {
		return party;
	}

	public static enum Result {
		NONE, VICTORY, DEFEAT;
	}

}
