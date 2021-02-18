package com.cssrc.ibms.api.system.intf;

import com.cssrc.ibms.api.system.model.ISysWs;

public interface ISysWsService {

//	public abstract void delAll(Long[] lAryId) throws Exception;
//
//	public abstract void addAll(ISysWs ws) throws Exception;
//
//	public abstract void updateAll(ISysWs ws) throws Exception;
//
//	public abstract void addSubList(ISysWs ws) throws Exception;

	public abstract ISysWs getByAlias(String alias);


}