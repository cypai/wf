package com.pipai.wf.save;

import java.lang.reflect.Type;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.unit.ability.Ability;

public class AgentStateSaveConverter {

	public String transformAgentStateToString(AgentState as) {
		Gson gson = new GsonBuilder()
				.addSerializationExclusionStrategy(new ConfigExclusionStrategy())
				.registerTypeHierarchyAdapter(Ability.class, new AbilityAdapter())
				.create();
		String json = gson.toJson(as);
		return json;
	}

	public AgentState parseStringRepresentation(String rawSaveData) {
		Gson gson = new GsonBuilder()
				.addDeserializationExclusionStrategy(new ConfigExclusionStrategy())
				.create();
		return gson.fromJson(rawSaveData, AgentState.class);
	}

	private static class ConfigExclusionStrategy implements ExclusionStrategy {
		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return clazz.equals(BattleConfiguration.class);
		}

		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			return false;
		}
	}

	public class AbilityAdapter implements JsonSerializer<Ability>, JsonDeserializer<Ability> {

		private static final String CLASSNAME = "CLASSNAME";
		private static final String INSTANCE = "INSTANCE";

		@Override
		public JsonElement serialize(Ability src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject retValue = new JsonObject();
			String className = src.getClass().getName();
			retValue.addProperty(CLASSNAME, className);
			Gson gson = new Gson();
			JsonElement elem = gson.toJsonTree(src);
			retValue.add(INSTANCE, elem);
			return retValue;
		}

		@Override
		public Ability deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();
			JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
			String className = prim.getAsString();

			Class<?> klass = null;
			try {
				klass = Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new JsonParseException(e.getMessage());
			}
			return context.deserialize(jsonObject.get(INSTANCE), klass);
		}
	}

}
