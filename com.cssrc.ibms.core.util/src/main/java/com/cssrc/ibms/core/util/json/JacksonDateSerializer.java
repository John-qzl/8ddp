package com.cssrc.ibms.core.util.json;

import java.io.IOException;
import java.util.Date;

import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JacksonDateSerializer extends JsonSerializer<Date> {
	public void serialize(Date value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeString(DateFormatUtil.format(value, "yyyy-MM-dd HH:mm:ss"));
	}
}