package com.pipai.wf.battle.agent;

import com.pipai.wf.battle.Team;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.unit.schema.UnitSchema;
import com.pipai.wf.util.GridPosition;

public class AgentFactory {

	public Agent battleAgentFromSchema(Team team, GridPosition position, UnitSchema schema) {
		Agent a = new Agent(schema);
		a.setTeam(team);
		a.setState(State.NEUTRAL);
		a.setPosition(position);
		return a;
	}

	public Agent battleAgentFromStats(Team team, GridPosition position,
			int hp, int mp, int ap, int aim, int mobility, int defense) {
		Agent as = new Agent("No name", new BasicStats(hp, hp, mp, mp, ap, ap, aim, mobility, defense));
		as.setTeam(team);
		as.setState(State.NEUTRAL);
		as.setPosition(position);
		return as;
	}

	public Agent battleAgentFromBasicStats(Team team, GridPosition position, BasicStats stats) {
		Agent as = new Agent("No name", stats);
		as.setTeam(team);
		as.setState(State.NEUTRAL);
		as.setPosition(position);
		return as;
	}
}
