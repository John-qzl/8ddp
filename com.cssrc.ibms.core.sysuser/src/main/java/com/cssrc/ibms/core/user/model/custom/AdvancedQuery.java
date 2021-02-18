package com.cssrc.ibms.core.user.model.custom;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cssrc.ibms.api.custom.model.IAdvancedQuery;


/**
 * Description:高级查询bean
 * <p>AdvancedQuery.java</p>
 * @author dengwenjie 
 * @date 2017年9月16日
 */
public class AdvancedQuery implements IAdvancedQuery{
	
	public static final String KEY = "aquery";
	public static final String KEYINDEX = "allkey";
	
	//高级查询主键: displayId.queryKey
	/**
	 *  高级查询组合主键之一
	 */
	private String displayId;
	
	/**
	 * 高级查询组合主键之一
	 */
	private String queryKey;
	
	/**
	 * 高级查询名称
	 */
	private String queryName;
	
	private List<FilterJsonStruct> condition;

	public static AdvancedQuery toBean(JSONObject obj){
		try{			
	    	JSONArray condition = JSONArray.fromObject(obj.getString("condition"));
	    	List<FilterJsonStruct> list = JSONArray.toList(condition, FilterJsonStruct.class);
	    	AdvancedQuery advancedQuery = (AdvancedQuery)JSONObject.toBean(obj, AdvancedQuery.class);
	    	advancedQuery.setCondition(list);
	    	return advancedQuery;
		}catch(Exception e){
			return new AdvancedQuery();
		}
	}
	public String getDisplayId() {
		return displayId;
	}

	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public List<FilterJsonStruct> getCondition() {
		return condition;
	}
	public JSONArray getConditionJSON(){
		if(getCondition()==null||getCondition().toString().equals("[null]")){
			return new JSONArray();
		}else{
			return JSONArray.fromObject(getCondition());
		}
	}
	public void setCondition(List<FilterJsonStruct> condition) {
		this.condition = condition;
	}
}
