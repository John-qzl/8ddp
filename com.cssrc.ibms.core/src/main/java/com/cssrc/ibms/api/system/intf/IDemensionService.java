package com.cssrc.ibms.api.system.intf;

import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.system.model.IDemension;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IDemensionService{

	public abstract boolean getNotExists(Map params);

	public abstract List<?extends IDemension> getDemenByQuery(QueryFilter queryFilter);

	public abstract List getAll();

	public abstract IDemension getById(Long demId);

}