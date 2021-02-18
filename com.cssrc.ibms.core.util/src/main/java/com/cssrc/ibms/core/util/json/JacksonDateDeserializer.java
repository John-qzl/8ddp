package com.cssrc.ibms.core.util.json;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JacksonDateDeserializer extends JsonDeserializer<Date> {
	private static final Logger logger = LoggerFactory
			.getLogger(JacksonDateDeserializer.class);

	public Date deserialize(JsonParser parser, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		try {
			return DateFormatUtil.parse(parser.getText(), "yyyy-MM-dd");
		} catch (ParseException e) {
			logger.error("ParseException: ", e);
		}
		return null;
	}
}