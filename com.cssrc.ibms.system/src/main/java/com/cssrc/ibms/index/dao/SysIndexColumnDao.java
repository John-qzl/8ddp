package com.cssrc.ibms.index.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.index.model.SysIndexColumn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class SysIndexColumnDao extends BaseDao<SysIndexColumn>
{
	public Class<?> getEntityClass()
	{
		return SysIndexColumn.class;
	}

	public SysIndexColumn getByColumnAlias(String alias)
	{
		return (SysIndexColumn)getOne("getByColumnAlias", alias);
	}

	public List<SysIndexColumn> getByUserIdFilter(Map<String, Object> params)
	{
		return getBySqlKey("getByUserIdFilter", params);
	}

	public Integer getCounts() {
		return (Integer)getOne("getCounts", null);
	}

	public Boolean isExistAlias(String alias, Long id) {
		Map map = new HashMap();
		map.put("alias", alias);
		map.put("id", id);
		Integer count = (Integer)getOne("isExistAlias", map);
		if (count.intValue() > 0) return Boolean.valueOf(true); return Boolean.valueOf(false);
	}
}

