package com.cssrc.ibms.index.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.index.dao.InsColNewDao;
import com.cssrc.ibms.index.model.InsColNew;
/**
 * 新闻栏目关联信息Service层
 * @author YangBo
 *
 */
@Service
public class InsColNewService extends BaseService<InsColNew>{
	@Resource
	private InsColNewDao dao;

	protected IEntityDao<InsColNew, Long> getEntityDao() {
		return this.dao;
	}

	/**
	 * 删除栏目colId下有关新闻
	 * @param colId
	 */
	public void delByColId(Long colId)
	{
		this.dao.delByColId(colId);
	}
	
	/**
	 * 删除所有显示newId的栏目新闻
	 * @param newId
	 */
	public void delByNewId(Long newId)
	{
		this.dao.delByNewId(newId);
	}
	
	/**
	 * 获取一条栏目新闻
	 * @param colId
	 * @param newId
	 * @return
	 */
	public InsColNew getByColIdNewId(Long colId, Long newId)
	{
		return this.dao.getByColIdNewId(colId, newId);
	}
	
	/**
	 * 删除一条栏目新闻
	 * 
	 */
	public void delByColIdNewId(Long colId, Long newId)
	{
		this.dao.delByColIdNewId(colId, newId);
	}
}
