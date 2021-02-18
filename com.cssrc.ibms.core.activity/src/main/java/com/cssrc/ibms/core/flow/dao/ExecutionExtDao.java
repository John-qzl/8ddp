package com.cssrc.ibms.core.flow.dao;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.GenericDao;

@Repository
public class ExecutionExtDao extends GenericDao<ExecutionEntity,String>
{
	@Override
	public Class getEntityClass()
	{
		return ExecutionEntity.class;
	}
}
