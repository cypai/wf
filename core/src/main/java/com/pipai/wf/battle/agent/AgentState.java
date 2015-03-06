package com.pipai.wf.battle.agent;

import com.pipai.wf.battle.agent.Agent.State;
import com.pipai.wf.battle.agent.Agent.Team;
import com.pipai.wf.battle.exception.BadStateStringException;
import com.pipai.wf.battle.map.GridPosition;

public class AgentState {
	
	protected Team team;
	protected int hp, maxHP;
	protected int ap, maxAP;
	protected int mobility;
	protected GridPosition position;
	protected State state;
	
	public AgentState(String agentString) throws BadStateStringException {
		String split[] = agentString.split(" ");
		if (!split[0].equals("a")) {
			throw new BadStateStringException("StateString must start with 'a': ");
		}
		GridPosition pos;
		try {
			pos = new GridPosition(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		} catch (Exception e) { throw new BadStateStringException("Could not parse position: " + e.getMessage()); }
		try {
			if (split.length == 3) {
				initialize(pos, Team.PLAYER, 3, 3, 2, 2, 7, State.NEUTRAL);
			} else if (split.length == 4) {
				initialize(position, Team.PLAYER, 3, 3, 2, 2, Integer.parseInt(split[3]), State.NEUTRAL);
			} else {
				Team team = split[3].equals("0") ? Team.PLAYER : Team.ENEMY;
				if (split.length == 7) {
					int hp = Integer.parseInt(split[4]);
					int ap = Integer.parseInt(split[5]);
					initialize(position, team, hp, hp, ap, ap, Integer.parseInt(split[6]), State.NEUTRAL);
				} else {
					int hpMax = Integer.parseInt(split[4]);
					int hp = Integer.parseInt(split[5]);
					int apMax = Integer.parseInt(split[6]);
					int ap = Integer.parseInt(split[7]);
					initialize(position, team, hpMax, hp, apMax, ap, Integer.parseInt(split[8]), parseState(split[8]));
				}
			}
		} catch (Exception e) { throw new BadStateStringException("Could not parse string: " + e.getMessage()); }
	}
	
	public AgentState(GridPosition position) {
		initialize(position, Team.PLAYER, 3, 3, 2, 2, 7, State.NEUTRAL);
	}

	public AgentState(GridPosition position, Team team, int mobility) {
		initialize(position, team, 3, 3, 2, 2, mobility, State.NEUTRAL);
	}
	
	public AgentState(GridPosition position, Team team, int maxHP, int maxAP, int mobility) {
		this(position, team, maxHP, maxHP, maxAP, maxAP, mobility, State.NEUTRAL);
	}

	public AgentState(GridPosition position, Team team, int maxHP, int hp, int maxAP, int ap, int mobility, State state) {
		initialize(position, team, maxHP, hp, maxAP, ap, mobility, state);
	}
	
	private void initialize(GridPosition position, Team team, int maxHP, int hp, int maxAP, int ap, int mobility, State state) {
		this.team = team;
		this.maxHP = maxHP;
		this.hp = hp;
		this.maxAP = maxAP;
		this.ap = ap;
		this.mobility = mobility;
		this.position = position;
		this.state = state;
	}
	
	private State parseState(String s) {
		return s.equals('0') ? State.NEUTRAL : State.KO;
	}
	
	/*
	 * MapString version
	 */
	public String generateString() {
		String s = "a ";
		s += String.valueOf(position.x) + " " + String.valueOf(position.y) + " ";
		s += team + " ";
		s += String.valueOf(hp) + " " + String.valueOf(maxHP) + " ";
		s += String.valueOf(ap) + " " + String.valueOf(maxAP) + " ";
		s += String.valueOf(mobility) + " ";
		s += state;
		return s;
	}
	
	/*
	 * Readable version
	 */
	public String toString() {
		String s = "";
		s += "Position: " + position + "\n";
		s += "Team: " + team + "\n";
		s += "HP: " + String.valueOf(hp) + "/" + String.valueOf(maxHP) + "\n";
		s += "AP: " + String.valueOf(ap) + "/" + String.valueOf(maxAP) + "\n";
		s += "Mobility: " + String.valueOf(mobility) + "\n";
		s += "State: " + state + "\n";
		return s;
	}
}