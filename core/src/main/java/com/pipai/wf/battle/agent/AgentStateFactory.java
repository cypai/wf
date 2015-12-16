package com.pipai.wf.battle.agent;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent.State;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.unit.schema.UnitSchema;

public class AgentStateFactory {

	public AgentState battleAgentFromSchema(Team team, GridPosition position, UnitSchema schema) {
		AgentState as = new AgentState(schema.getName(), schema.getBasicStats());
		as.setAbilities(schema.getAbilities());
		as.setArmor(schema.getArmor());
		as.setWeapons(schema.getWeapons());
		as.setTeam(team);
		as.setState(State.NEUTRAL);
		as.setPosition(position);
		as.setExp(schema.getExp());
		as.setExpGiven(schema.getExpGiven());
		return as;
	}

	public AgentState battleAgentFromStats(Team team, GridPosition position, int hp, int mp, int ap, int aim, int mobility, int defense) {
		AgentState as = new AgentState("No name", new BasicStats(hp, hp, mp, mp, ap, ap, aim, mobility, defense));
		as.setTeam(team);
		as.setState(State.NEUTRAL);
		as.setPosition(position);
		return as;
	}

	public AgentState battleAgentFromBasicStats(Team team, GridPosition position, BasicStats stats) {
		AgentState as = new AgentState("No name", stats);
		as.setTeam(team);
		as.setState(State.NEUTRAL);
		as.setPosition(position);
		return as;
	}
}
