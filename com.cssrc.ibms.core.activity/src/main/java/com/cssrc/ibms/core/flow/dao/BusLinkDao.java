package com.cssrc.ibms.core.flow.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.BusLink;
/**
 *<pre>
 * 对象功能:业务数据关联表 Dao类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Repository
public class BusLinkDao extends BaseDao<BusLink>
{
	@Override
	public Class<?> getEntityClass()
	{
		return BusLink.class;
	}
	
	public BusLink getByPk(Long pk){
		return this.getUnique("getByPk", pk);
	}

	public BusLink getByPkStr(String pk){
		return this.getUnique("getByPkStr", pk);
	}
	
	public void delByPk(Long pk){
		this.delBySqlKey("delByPk", pk);
	}
	
	public void delByPkStr(String pk){
		this.delBySqlKey("delByPkStr", pk);
	}
}