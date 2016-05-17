package com.pipai.wf.save;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipai.wf.unit.schema.UnitSchema;

public class UnitSchemaSaveConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnitSchemaSaveConverter.class);

	public String transformUnitSchemaToString(UnitSchema as) {
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

	public UnitSchema parseStringRepresentation(String rawSaveData) throws CorruptedSaveException {
		ObjectMapper mapper = new ObjectMapper();
		UnitSchema as;
		try {
			as = mapper.readValue(rawSaveData, UnitSchema.class);
		} catch (IOException e) {
			throw new CorruptedSaveException("Could not parse save data: " + e.getMessage(), e);
		}
		return as;
	}

}
