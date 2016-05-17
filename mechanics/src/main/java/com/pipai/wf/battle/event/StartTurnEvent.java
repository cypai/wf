package com.pipai.wf.battle.event;

import com.pipai.wf.battle.Team;

public class StartTurnEvent extends BattleEvent {

	public final Team team;

	public StartTurnEvent(Team team) {
		this.team = team;
	}

	@Override
	public String toString() {
		return "Beginning turn for " + team.toString();
	}

}
