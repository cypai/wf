package com.pipai.wf.battle;

import java.util.ArrayList;
import java.util.List;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.unit.schema.ImmutableUnitSchema;

public final class BattleResult {

	private Result result;
	private ArrayList<ImmutableUnitSchema> partyState;

	public BattleResult(Result result, List<Agent> partyAgents) {
		this.result = result;
		partyState = new ArrayList<>();
		for (Agent a : partyAgents) {
			partyState.add(new ImmutableUnitSchema(a));
		}
	}

	public Result getResult() {
		return result;
	}

	public List<ImmutableUnitSchema> getPartyState() {
		return partyState;
	}

	public enum Result {
		NONE, VICTORY, DEFEAT;
	}

}
