package com.cssrc.ibms.core.util.json;


import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * JSON帮助类
 * 
 * @author zhulongchao
 * 
 */
@Service
public class JSONUtil {
	
	private Log logger = LogFactory.getLog(JSONUtil.class);

	/**
	 * 判断JSON是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null)
			return true;
		if (obj instanceof JSONObject)
			return ((JSONObject) obj).isNullObject();
		if (obj instanceof JSONArray) {
			return ((JSONArray) obj).isEmpty();
		}
		return JSONNull.getInstance().equals(obj);
	}

	/**
	 * 判断JSON 是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(JSONObject obj) {
		return !isEmpty(obj);
	}

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(JSONUtil.class);
		JSONArray jsonAry = new JSONArray();
		JSONObject json = new JSONObject();
		jsonAry.add(json);
		logger.info(String.valueOf(JSONUtil.isEmpty(jsonAry)));
	}
	
	/**
	 *  根据json获取对应long值，为空则返回缺省值
	 *@author YangBo @date 2016年12月5日下午4:58:33
	 *@param obj
	 *@param key
	 *@param defaultValue
	 *@return
	 */
	public static Long getLong(JSONObject obj, String key,
			Long defaultValue) {
		Object o = obj.get(key);
		if(o instanceof JSONNull||o==null){
			return defaultValue;
		}
		if(o instanceof Integer){
			return Long.valueOf(o+"");
		}
		return Long.valueOf(o.toString());
	}
	
	/**
	 * 根据json获取对应key值，为空则返回缺省值
	 *@author YangBo @date 2016年12月5日下午8:18:52
	 *@param obj
	 *@param key
	 *@param defaultValue
	 *@return
	 */
	public static String getString(JSONObject obj, String key) {
		Object o = obj.get(key);
		if(o instanceof JSONNull||o==null){
			return "";
		}
		return String.valueOf(o);
	}
	public static boolean getBoolean(JSONObject obj, String key){
		String str = getString(obj,key);
		if (StringUtils.isNumeric(str))
			return (Integer.parseInt(str) == 1 ? true : false);
		return Boolean.parseBoolean(str);
	}
	
}
