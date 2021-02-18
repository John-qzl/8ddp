package com.cssrc.ibms.core.util.string;

import java.util.HashMap;
import java.util.Map;

public class UrlHelper {
	public static Long getLongValue(String path,String key){
		Long value = null;
		try{
			 Map<String,Object> map =  getParams(path);
			if(map.containsKey(key)){
				value = Long.valueOf(map.get(key).toString());
			}
			return value;
		}catch(Exception e){
			return value;
		}
	}
	public static String getStringValue(String path,String key){
		String value = null;
		try{
			 Map<String,Object> map =  getParams(path);
			if(map.containsKey(key)){
				value = (String)map.get(key);
			}
			return value;
		}catch(Exception e){
			return value;
		}
	}
	public static Map<String,Object> getParams(String path){
		Map<String,Object> map = new HashMap();
		path = path.replace("?", "&");
		String[] arr = path.split("&");
		for(int i=1;i<arr.length;i++){
			String str = arr[i];
			if(str.contains("=")){
				map.put(str.split("=")[0],str.split("=")[1]);
			}
		}
		return map;
	}
}
