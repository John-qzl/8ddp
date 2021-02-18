package com.cssrc.ibms.core.user.model;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.util.SysContextUtil;
import com.cssrc.ibms.api.sysuser.model.IUserCustom;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.redis.RedisClient;
import com.cssrc.ibms.core.util.redis.RedisKey;

public class  UserCustom implements IUserCustom{
	private static final long serialVersionUID = 1L;	
	public static final String ADVANCED_QUERY = "advanced_query";
	protected Long userId;
	protected String customInfo;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getCustomInfo() {
		return customInfo;
	}
	public void setCustomInfo(String customInfo) {
		this.customInfo = customInfo;
	}
	public UserCustom(){
		
	}
	public UserCustom(Long userId){
		JSONObject obj = new  JSONObject();
		this.userId = userId;
		this.customInfo = obj.toString();
	}
	public static String getUserCustomKey(Long userId){
		 String dataSource = SysContextUtil.getTableSpace();
	     String key = RedisKey.CUSTOM_INFO_PREFIX + dataSource + "." + String.valueOf(userId);
	     return key;
	}
	/**
	 * 列表显示列-get
	 * @param displayId
	 * @return
	 */
	public String getDisplayFieldInfo(Long displayId,Long userId){
		String displayId_s = String.valueOf(displayId);
		String display_field_Info = "";
		Object user_custom_info = RedisClient.get(getUserCustomKey(userId));
		if(BeanUtils.isEmpty(user_custom_info)||user_custom_info.equals("false")){
			return "";
		}
		JSONObject custom = JSONObject.fromObject(user_custom_info);
		if(custom.containsKey(DISPLAY_FIELD)){
			JSONObject  displayField = JSONObject.fromObject(
					custom.get(DISPLAY_FIELD).toString());
			if(displayField.containsKey(String.valueOf(displayId_s))){
				display_field_Info = displayField.get(displayId_s).toString();
			}
		}
		return display_field_Info;	
	}
	/**
	 * 列表显示列-set
	 */
	public void setDisplayFieldInfo(Long displayId,Long userId,JSONArray jsonArr){
		if(RedisClient.jedisPool==null){
			return;
		}
		String displayId_s = String.valueOf(displayId);
		Object user_custom_info = RedisClient.get(getUserCustomKey(userId));
		JSONObject user_custom_info_Obj = null;
		JSONObject display_field_Obj = null;
		if(BeanUtils.isEmpty(user_custom_info)){
			user_custom_info_Obj = new JSONObject();
			display_field_Obj = new JSONObject();
			display_field_Obj.put(displayId_s, jsonArr);
			user_custom_info_Obj.put(DISPLAY_FIELD, display_field_Obj);
		}else{
			user_custom_info_Obj = JSONObject.fromObject(user_custom_info);
			if(user_custom_info_Obj.containsKey(DISPLAY_FIELD)){
				display_field_Obj = JSONObject.fromObject(
						user_custom_info_Obj.get(DISPLAY_FIELD).toString());
				display_field_Obj.put(displayId_s, jsonArr);
			}else{
				display_field_Obj = new JSONObject();
				display_field_Obj.put(displayId_s, jsonArr);
			}
			user_custom_info_Obj.put(DISPLAY_FIELD, display_field_Obj);
		}
		RedisClient.set(getUserCustomKey(userId), user_custom_info_Obj);
	}
	/**
	 * 列表查询-get
	 * @param displayId
	 * @return
	 */
	public  String getQueryFieldInfo(Long displayId,Long userId){
		if(RedisClient.jedisPool==null){
			return "";
		}
		String displayId_s = String.valueOf(displayId);
		String query_field_Info = "";
		Object user_custom_info = RedisClient.get(getUserCustomKey(userId));
		if(BeanUtils.isEmpty(user_custom_info)||user_custom_info.equals("false")){
			return "";
		}
		JSONObject custom = JSONObject.fromObject(user_custom_info);
		if(custom.containsKey(QUERY_FIELD)){
			JSONObject  queryField = JSONObject.fromObject(
					custom.get(QUERY_FIELD).toString());
			if(queryField.containsKey(String.valueOf(displayId_s))){
				query_field_Info = queryField.get(displayId_s).toString();
			}
		}
		return query_field_Info;		
	}
	/**
	 * 列表查询-set
	 */
	public  void setQueryFieldInfo(Long displayId,Long userId,JSONArray jsonArr){
		if(RedisClient.jedisPool==null){
			return;
		}
		String displayId_s = String.valueOf(displayId);
		Object user_custom_info = RedisClient.get(getUserCustomKey(userId));
		JSONObject user_custom_info_Obj = null;
		JSONObject query_field_Obj = null;
		if(BeanUtils.isEmpty(user_custom_info)){
			user_custom_info_Obj = new JSONObject();
			query_field_Obj = new JSONObject();
			query_field_Obj.put(displayId_s, jsonArr);
			user_custom_info_Obj.put(QUERY_FIELD, query_field_Obj);
		}else{
			user_custom_info_Obj = JSONObject.fromObject(user_custom_info);
			if(user_custom_info_Obj.containsKey(QUERY_FIELD)){
				query_field_Obj = JSONObject.fromObject(
						user_custom_info_Obj.get(QUERY_FIELD).toString());
				query_field_Obj.put(displayId_s, jsonArr);
			}else{
				query_field_Obj = new JSONObject();
				query_field_Obj.put(displayId_s, jsonArr);
			}
			user_custom_info_Obj.put(QUERY_FIELD, query_field_Obj);
		}
		RedisClient.set(getUserCustomKey(userId), user_custom_info_Obj);
	}
	
	
	public boolean equals(Object rhs) {
		if ((rhs instanceof UserCustom)) {
			return this.userId.equals(((UserCustom)rhs).userId);
		}
		return false;
	}
	public String toString() {
		return new ToStringBuilder(this).append("userId", this.userId)
				.append("customInfo", this.customInfo).toString();
	}
	
}
