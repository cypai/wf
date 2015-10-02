package com.pipai.wf.battle.agent;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent.State;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.unit.schema.UnitSchema;

public class AgentStateFactory {

	private final BattleConfiguration config;

	public AgentStateFactory(BattleConfiguration config) {
		this.config = config;
	}

	public AgentState statsOnlyState(int hp, int mp, int ap, int mobility, int aim, int defense) {
		AgentState a = new AgentState(config);
		a.maxHP = hp;
		a.hp = hp;
		a.maxMP = mp;
		a.mp = mp;
		a.maxAP = ap;
		a.ap = ap;
		a.mobility = mobility;
		a.aim = aim;
		a.defense = defense;
		a.name = "No name";
		return a;
	}

	public AgentState createFromSchema(UnitSchema schema) {
		AgentState as = statsOnlyState(schema.hp(), schema.mp(), schema.ap(), schema.mobility(), schema.aim(), schema.defense());
		as.abilities = schema.abilities();
		as.armor = schema.armor();
		as.weapons = schema.weapons(config);
		as.name = schema.name();
		return as;
	}

	public AgentState battleAgentFromSchema(Team team, GridPosition position, UnitSchema schema) {
		AgentState a = createFromSchema(schema);
		a.team = team;
		a.state = State.NEUTRAL;
		a.position = position;
		return a;
	}

	public AgentState newBattleAgentState(Team team, GridPosition position, int hp, int mp, int ap, int mobility, int aim, int defense) {
		AgentState a = statsOnlyState(hp, mp, ap, mobility, aim, defense);
		a.team = team;
		a.state = State.NEUTRAL;
		a.position = position;
		return a;
	}

	public AgentState battleAgentFromStats(Team team, GridPosition position, AgentState stats) {
		AgentState a = statsOnlyState(stats.maxHP, stats.maxMP, stats.maxAP, stats.mobility, stats.aim, stats.defense);
		a.team = team;
		a.state = State.NEUTRAL;
		a.position = position;
		a.weapons.add(new Pistol(config));
		return a;
	}
}
