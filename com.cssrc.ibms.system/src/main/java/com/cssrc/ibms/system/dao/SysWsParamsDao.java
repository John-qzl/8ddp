package com.cssrc.ibms.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysWsParams;
 
@Repository
public class SysWsParamsDao extends BaseDao<SysWsParams>
{
	@Override
	public Class<?> getEntityClass()
	{
		return SysWsParams.class;
	}

	public List<SysWsParams> getByMainId(Long setid) {
		return this.getBySqlKey("getBpmCommonWsParamsList", setid);
	}
	
	public void delByMainId(Long setid) {
		this.delBySqlKey("delByMainId", setid);
	}
}