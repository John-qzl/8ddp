package com.cssrc.ibms.api.sync.intf;


public interface ISyncFormWebService {

	public abstract String saveBatch(String jsonArr) throws Exception;

	public abstract String saveOne(String jsonData);
	
	public abstract void updateMultiIDTO(String jsonArr);
	
	public abstract void updateIDTO(String jsonData);
	
	public abstract String refuseMethod(String jsonData);
}