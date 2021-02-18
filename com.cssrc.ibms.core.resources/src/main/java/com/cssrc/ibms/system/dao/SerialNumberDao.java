package com.cssrc.ibms.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SerialNumber;

@Repository
public class SerialNumberDao extends BaseDao<SerialNumber>{
	public Class getEntityClass() {
		return SerialNumber.class;
	}
	
	public SerialNumber getByAlias(String alias)
	{
		SerialNumber obj = (SerialNumber)getUnique("getByAlias", alias);
		return obj;
	}

	public boolean isAliasExisted(String alias)
	{
		return ((Integer)getOne("isAliasExisted", alias)).intValue() > 0;
	}

	public boolean isAliasExistedByUpdate(SerialNumber serialNumber)
	{
		return ((Integer)getOne("isAliasExistedByUpdate", serialNumber)).intValue() > 0;
	}

	public List<SerialNumber> getList()
	{
		return getBySqlKey("getList");
	}

	public int updateVersion(SerialNumber serialNumber)
	{
		return update("updateVersion", serialNumber);
	}
}
