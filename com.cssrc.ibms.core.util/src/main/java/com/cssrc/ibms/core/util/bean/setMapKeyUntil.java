package com.cssrc.ibms.core.util.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class setMapKeyUntil {

	 
	public static Map<String,Object> setMapKey(Map<String,Object> map){
		Map<String, Object> newMap=new HashMap<>();
		  //mapd的遍历
        for(Entry<String, Object> entry:map.entrySet()){
        	String string="";
           if(!entry.getKey().equals("id")) {
        	   string="F_";
           }
           if(entry.getKey().equals("class")) {
        	   continue;
           }
           newMap.put(string+entry.getKey(), entry.getValue());
        }
		return newMap;
		
	}
}
