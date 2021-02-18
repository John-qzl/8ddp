package com.cssrc.ibms.core.user.model.custom;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cssrc.ibms.api.custom.model.IListConfs;

public class ListConfs implements IListConfs{	
	public static final String KEY = "portalListing";
	public List<ListConf> confs;	
	
	public static ListConfs toBean(JSONObject obj){
		try{			
	    	JSONArray confs = JSONArray.fromObject(obj.getString("confs"));
	    	List<ListConf> list = JSONArray.toList(confs, ListConf.class);
	    	ListConfs listConfs = (ListConfs)JSONObject.toBean(obj, ListConfs.class);
	    	listConfs.setConfs(list);
	    	return listConfs;
		}catch(Exception e){
			return new ListConfs();
		}
	}
	public static ListConfs toBean(String str){
		return toBean(JSONObject.fromObject(str));
	}
	public static ListConfs getDefault(){
		return null;
	}
	public List<ListConf> getConfs() {
		return confs;
	}
	public void setConfs(List<ListConf> confs) {
		this.confs = confs;
	}
}
