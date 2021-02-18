package com.cssrc.ibms.index.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.index.dao.InsPortColDao;
import com.cssrc.ibms.index.model.InsPortCol;

/**
 * 布局显示管理Service层
 * @author YangBo
 *
 */
@Service
public class InsPortColService extends BaseService<InsPortCol>{
	@Resource
	private InsPortColDao dao;

	protected IEntityDao<InsPortCol, Long> getEntityDao() {
		
		return this.dao;
	}
	
	public InsPortCol getByPortCol(Long portId, Long colId)
	{
		return this.dao.getByPortCol(portId, colId);
	}

	public void delByPortCol(Long portId, Long colId)
	{
		this.dao.delByPortCol(portId, colId);
	}

	public void delByPortal(Long portId)
	{
		this.dao.delByPortal(portId);
	}
	
	/**
	 * 个人布局获取
	 * @param portId
	 * @param userId
	 * @return
	 */
	public List<InsPortCol> getPersonalPort(Long portId,String userId)
	{
		return this.dao.getPersonalPort(portId, userId);
	}
	
	/**
	 * 获取全局布局
	 * @param portId
	 * @return
	 */
	public List<InsPortCol> getGlobalPortal(Long portId)
	{
		return this.dao.getGlobalPortal(portId);
	}
	
	/**
	 * 获取同组织下的布局
	 * @param portId
	 * @param orgId
	 * @return
	 */
	public List<InsPortCol> getByOrgId(Long portId,String orgId)
	{
		return this.dao.getByOrgId(portId, orgId);
	}
	
	/**
	 * 更新所有布局相关栏目的高
	 * @param height
	 * @param colId
	 */
	public void updateHeight(Integer height,Long colId)
	{
		this.dao.updateHeight(height,colId);
	}
}
