package com.cssrc.ibms.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.system.model.SysWs;
 
@Repository
public class SysWsDao extends BaseDao<SysWs>
{
	@Override
	public Class<?> getEntityClass()
	{
		return SysWs.class;
	}
	
	public SysWs getByAlias(String alias){
		List<SysWs> list = this.getBySqlKey("getByAlias",alias);
		if(BeanUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

}