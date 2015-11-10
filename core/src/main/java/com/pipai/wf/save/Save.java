package com.pipai.wf.save;

import java.util.ArrayList;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.agent.AgentState;

public class Save {

	private BattleConfiguration config;

	private ArrayList<AgentState> partyInfo;

	public Save(BattleConfiguration config) {
		this.config = config;
		partyInfo = new ArrayList<>();
	}

	public BattleConfiguration getConfig() {
		return config;
	}

	public ArrayList<AgentState> getParty() {
		return partyInfo;
	}

	public void setParty(ArrayList<AgentState> party) {
		partyInfo = party;
	}

}
