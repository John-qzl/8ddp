package com.cssrc.ibms.core.util.json.support;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 * 
 * @author Yangbo 2016-7-22
 *
 */
public class JsonObjectSerializer implements JsonDeserializer<String>,
		JsonSerializer<String> {
	public String deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		String result = "";
		if (json.isJsonPrimitive()) {
			result = json.getAsString();
		}
		if ((json.isJsonArray()) || (json.isJsonObject())) {
			result = json.toString();
		}
		return result;
	}

	public JsonElement serialize(String arg0, Type arg1,
			JsonSerializationContext arg2) {
		return new JsonPrimitive(arg0);
	}
}
