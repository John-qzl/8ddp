package com.cssrc.ibms.api.core.intf;

import java.util.List;

import com.cssrc.ibms.core.db.mybatis.query.BusQueryRule;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IBaseService<T> extends IGenericService<T, Long>{

	/**
	 * 高级查询的方法
	 * 
	 * @param queryFilter 查询
	 * @param busQueryRule 查询规则
	 * @return
	 */
	public abstract List<T> getAll(QueryFilter queryFilter,
			BusQueryRule busQueryRule);

}