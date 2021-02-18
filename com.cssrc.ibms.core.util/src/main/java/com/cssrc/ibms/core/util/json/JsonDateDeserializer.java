package com.cssrc.ibms.core.util.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

public class JsonDateDeserializer implements JsonDeserializer<Date> {
	public Date deserialize(JsonElement jsonEl, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		return new Date(jsonEl.getAsJsonPrimitive().getAsLong());
	}
}