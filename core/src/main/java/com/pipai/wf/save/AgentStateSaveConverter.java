package com.pipai.wf.save;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipai.wf.battle.agent.AgentState;

public class AgentStateSaveConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AgentStateSaveConverter.class);

	public String transformAgentStateToString(AgentState as) {
		ObjectMapper mapper = new ObjectMapper();
		String saveString;
		try {
			saveString = mapper.writeValueAsString(as);
		} catch (JsonProcessingException e) {
			LOGGER.error("Could not serialize to save data: " + e.getMessage(), e);
			saveString = "";
		}
		return saveString;
	}

	public AgentState parseStringRepresentation(String rawSaveData) {
		ObjectMapper mapper = new ObjectMapper();
		AgentState as;
		try {
			as = mapper.readValue(rawSaveData, AgentState.class);
		} catch (IOException e) {
			LOGGER.error("Could not parse save data: " + e.getMessage(), e);
			as = new AgentState();
		}
		return as;
	}

}
