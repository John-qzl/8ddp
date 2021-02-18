package com.cssrc.ibms.index.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.index.dao.InsColumnDao;
import com.cssrc.ibms.index.model.InsColumn;

/**
 * 布局栏目Service层
 * @author YangBo
 *
 */
@Service
public class InsColumnService extends BaseService<InsColumn> {

	@Resource
	private InsColumnDao dao;

	protected IEntityDao<InsColumn, Long> getEntityDao() {
		return this.dao;
	}
	
	/**
	 * 根据栏目名获取
	 * @param colName
	 * @param page
	 * @return
	 */
	public List<InsColumn> getByName(String colName, PagingBean page) {
		return this.dao.getByName(colName, page);
	}
	
	/**
	 * 获取可操作的栏目
	 * @return
	 */
	public List<InsColumn> getByEnable() {
		return this.dao.getByEnable();
	}
	
	/**
	 * 更加模板类型获取栏目数组
	 * @param typeId
	 * @param orgId
	 * @return
	 */
	public List<InsColumn> getByColType(Long typeId, String orgId){
		return this.dao.getByColType(typeId, orgId);
	}
	
	/**
	 * 组织下的栏目
	 * @param typeId
	 * @param orgId
	 * @return
	 */
	public List<InsColumn> getAllByOrgId(String orgId){
		return this.dao.getByColType(null, orgId);
	}
}
