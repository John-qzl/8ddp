package com.cssrc.ibms.index.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.index.dao.InsColTypeDao;
import com.cssrc.ibms.index.model.InsColType;

/**
 * 首页栏目类型模板Service层
 * @author YangBo
 *
 */
@Service
public class InsColTypeService extends BaseService<InsColType>{
	@Resource
	private InsColTypeDao dao;
	
	protected IEntityDao<InsColType, Long> getEntityDao() {
		return this.dao;
	}
	
	/**
	 * 根据key获取唯一模板
	 * @param key
	 * @param orgId
	 * @return
	 */
	public  InsColType getByKey(String key, String orgId)
	{
		return this.dao.getByKey(key, orgId);
	}
	
}
