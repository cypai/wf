package com.pipai.wf.save;

import java.util.ArrayList;

import com.pipai.wf.battle.agent.AgentState;

public class Save {

	private ArrayList<AgentState> partyInfo;

	public Save() {
		partyInfo = new ArrayList<>();
	}

	public ArrayList<AgentState> getParty() {
		return partyInfo;
	}

	public void setParty(ArrayList<AgentState> party) {
		partyInfo = party;
	}

}
