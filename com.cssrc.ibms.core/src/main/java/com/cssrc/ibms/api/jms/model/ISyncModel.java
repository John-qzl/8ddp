package com.cssrc.ibms.api.jms.model;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.cssrc.ibms.api.system.model.ISysFile;


public interface ISyncModel {

	String getMainTableName();

	List<ISysFile> getSysFiles();

	Map<String, Object> getMain();

}