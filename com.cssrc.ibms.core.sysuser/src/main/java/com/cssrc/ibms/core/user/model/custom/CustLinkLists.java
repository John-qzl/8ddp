package com.cssrc.ibms.core.user.model.custom;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cssrc.ibms.api.custom.model.ICustLinkLists;

public class CustLinkLists implements ICustLinkLists{
	
	public static final String KEY = "linkList";
	public List<CustLinkList> confs;
	
	public static CustLinkLists toBean(JSONObject obj){
		try{
			JSONArray confs = JSONArray.fromObject(obj.getString("confs"));
			List<CustLinkList> list = JSONArray.toList(confs,CustLinkList.class);
			CustLinkLists custLinkLists = (CustLinkLists) JSONObject.toBean(obj,CustLinkLists.class);
			custLinkLists.setConfs(list);
			return custLinkLists;
			
		}catch (Exception e){
			//e.printStackTrace();
			return new CustLinkLists();
		}
	}

	public static CustLinkLists toBean(String str){
		return toBean(JSONObject.fromObject(str));
	}
	
	public static CustLinkLists getDefault(){
		return null;
	}
	
	public List<CustLinkList> getConfs(){
		return confs;
	}
	
	public void setConfs(List<CustLinkList> confs){
		this.confs = confs;
	}
}
