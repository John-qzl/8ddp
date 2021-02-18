package com.cssrc.ibms.api.core.intf;


import java.util.List;

import com.cssrc.ibms.core.db.mybatis.query.BusQueryRule;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.db.mybatis.query.QueryUtil;


/**
 * 实现业务的基本操作类，实体主键为Long类型
 * 
 * @author zhulongchao
 * 
 * @param <E> 实体类型，如Role
 */
public abstract class BaseService<E> extends GenericService<E, Long> implements IBaseService<E> {

	/* (non-Javadoc)
	 * @see com.cssrc.ibms.core.base.service.mybatis.IBaseService#getAll(com.cssrc.ibms.core.base.dao.mybatis.query.QueryFilter, com.cssrc.ibms.core.base.dao.mybatis.query.BusQueryRule)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<E> getAll(QueryFilter queryFilter, BusQueryRule busQueryRule) {
		if (QueryUtil.isSuperAdmin())
			return getEntityDao().getAll(queryFilter);
		if (busQueryRule != null) // 从busQueryRule中获得规则进行查询
			return (List<E>) QueryUtil.getPageList(busQueryRule, queryFilter);
		else// 默认方式
			return getEntityDao().getAll(queryFilter);
	}

}

