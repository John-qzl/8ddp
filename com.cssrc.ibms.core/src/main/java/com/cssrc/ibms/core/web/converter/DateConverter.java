package com.cssrc.ibms.core.web.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.cssrc.ibms.core.util.date.DateUtil;

public class DateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String date) {
		try {
			if (date == null || "".equals(date))
				return null;
			if (date.length() <= 10) {
				return DateUtil.getDate(date, "yyyy-MM-dd");
			} else if (date.length() <= 14) {
				return DateUtil.getDate(date, "yyyy-MM-dd HH");
			} else if (date.length() <= 16) {
				return DateUtil.getDate(date, "yyyy-MM-dd HH:mm");
			} else {
				return DateUtil.getDate(date, "yyyy-MM-dd HH:mm:ss");
			}
		} catch (Exception e) {
			return null;
		}

	}

}
