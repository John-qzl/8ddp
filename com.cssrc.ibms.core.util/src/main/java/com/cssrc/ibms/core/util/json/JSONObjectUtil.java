package com.cssrc.ibms.core.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.json.support.BooleanSerializer;
import com.cssrc.ibms.core.util.json.support.DateSerializer;
import com.cssrc.ibms.core.util.json.support.JsonObjectSerializer;
import com.cssrc.ibms.core.util.json.support.SuperclassExclusionStrategy;
import com.cssrc.ibms.core.util.http.RequestUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

public class JSONObjectUtil {
	public static <C> C toBean(JSONObject jsonObject, Class<C> cls) {
		return toBean(jsonObject.toString(), cls);
	}

	public static <C> C toBean(String jsonStr, Class<C> cls) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(String.class,new JsonObjectSerializer())
		.registerTypeAdapter(Date.class,new DateSerializer())
		.registerTypeAdapter(Boolean.class,new BooleanSerializer())
		.addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
		.addSerializationExclusionStrategy(new SuperclassExclusionStrategy());
		Gson gson = gsonBuilder.create();
		Object o = gson.fromJson(jsonStr, cls);
		return (C) o;
	}

	public static <C> List<C> toBeanList(String arrayStr, Class<C> cls) {
		return toBeanList(JSONArray.fromObject(arrayStr), cls);
	}

	public static <C> List<C> toBeanList(JSONArray jsonArray, Class<C> cls) {
		List list = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Object o = toBean(jsonObject, cls);
			list.add(o);
		}
		return list;
	}

	public static Map<String, Object> getQueryMap(JSONObject jsonObject) {
		Map map = new HashMap();

		for (Iterator i$ = jsonObject.keySet().iterator(); i$.hasNext();) {
			Object obj = i$.next();
			String key = obj.toString();
			String[] values = new String[1];
			values[0] = jsonObject.getString(key);

			if ((key.equals("sortField")) || (key.equals("orderSeq"))
					|| (key.equals("sortColumns"))) {
				String val = values[0].trim();
				if (StringUtil.isNotEmpty(val)) {
					map.put(key, values[0].trim());
				}
			}
			if ((values.length > 0) && (StringUtils.isNotEmpty(values[0]))) {
				if (key.startsWith("Q_")) {
					RequestUtil.addParameter(key, values, map);
				} else if (values.length == 1) {
					String val = values[0].trim();
					if (StringUtil.isNotEmpty(val))
						map.put(key, values[0].trim());
				} else {
					map.put(key, values);
				}
			}

		}

		return map;
	}

	public static void main(String[] args) {
	}
}