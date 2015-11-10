package com.pipai.wf.save;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.agent.AgentState;

public class AgentSaveRepresentation {

	private BattleConfiguration config;
	private String rawSaveData;
	private AgentState representedAgentState;

	private static final String DELIMITER = "|";

	public AgentSaveRepresentation(BattleConfiguration config, AgentState as) {
		this.config = config;
		representedAgentState = as;
		transformAgentStateToString(as);
	}

	public AgentSaveRepresentation(BattleConfiguration config, String raw) {
		this.config = config;
		rawSaveData = raw;
		parseStringRepresentation(raw);
	}

	private void transformAgentStateToString(AgentState as) {
		StringBuilder builder = new StringBuilder();
		builder.append(as.name);
		builder.append(DELIMITER);
		builder.append(as.hp);
		builder.append(DELIMITER);
		builder.append(as.maxHP);
		builder.append(DELIMITER);
		builder.append(as.mp);
		builder.append(DELIMITER);
		builder.append(as.maxMP);
		builder.append(DELIMITER);
		builder.append(as.ap);
		builder.append(DELIMITER);
		builder.append(as.maxAP);
		builder.append(DELIMITER);
		builder.append(as.mobility);
		builder.append(DELIMITER);
		builder.append(as.aim);
		builder.append(DELIMITER);
		builder.append(as.defense);
		builder.append(DELIMITER);
	}

	private void parseStringRepresentation(String rawSaveData) {
		AgentState as = new AgentState(config);
		String[] tokens = rawSaveData.split(DELIMITER);
		as.name = tokens[0];
		as.hp = Integer.parseInt(tokens[1]);
		as.maxHP = Integer.parseInt(tokens[2]);
		as.mp = Integer.parseInt(tokens[3]);
		as.maxMP = Integer.parseInt(tokens[4]);
		as.ap = Integer.parseInt(tokens[5]);
		as.maxAP = Integer.parseInt(tokens[6]);
		as.mobility = Integer.parseInt(tokens[7]);
		as.aim = Integer.parseInt(tokens[8]);
		as.defense = Integer.parseInt(tokens[9]);
		representedAgentState = as;
	}

	public AgentState getAgentState() {
		return representedAgentState;
	}

	public String getStringRepresentation() {
		return rawSaveData;
	}

}
