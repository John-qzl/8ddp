package com.cssrc.ibms.api.sysuser.model;

import net.sf.json.JSONArray;

public interface IUserCustom {
	/**-----------------------------列表查询------------------------------------*/
	/**列表查询-key*/
	public static final String QUERY_FIELD =  "query_field";	
	/**列表查询-get*/
	public  String getQueryFieldInfo(Long displayId,Long userId);
	/**列表查询-set*/
	public  void setQueryFieldInfo(Long displayId,Long userId,JSONArray jsonArr);
	
	/**-----------------------------列表显示列------------------------------------*/
	/**列表显示列-key*/
	public static final String DISPLAY_FIELD =  "display_field";	
	/**列表显示列-get*/
	public  String getDisplayFieldInfo(Long displayId,Long userId);
	/**列表显示列-set*/
	public  void setDisplayFieldInfo(Long displayId,Long userId,JSONArray jsonArr);
}
