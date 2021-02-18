package com.cssrc.ibms.index.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.index.model.InsPortCol;


/**
 * 布局显示管理DAO层
 * @author YangBo
 *
 */
@Repository
public class InsPortColDao extends BaseDao<InsPortCol> {

	public Class<InsPortCol> getEntityClass() {
		return InsPortCol.class;
	}

	public InsPortCol getByPortCol(Long portId, Long colId) {
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("portId", portId);
		params.put("colId", colId);
		params.put("orgId", orgId);
		return (InsPortCol) getUnique("getByPortCol", params);
	}

	public void delByPortCol(Long portId, Long colId) {
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("portId", portId);
		params.put("colId", colId);
		params.put("orgId", orgId);
		delBySqlKey("delByPortCol", params);
	}

	public void delByPortal(Long portId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("portId", portId);
		delBySqlKey("delByPortal", params);
	}
	
	
	/**
	 * 获取个人布局设置
	 * @param portId
	 * @param userId
	 * @return
	 */
	public List<InsPortCol> getPersonalPort(Long portId, String userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("portId", portId);
		params.put("userId", userId);
		List<InsPortCol> insPortCols = getBySqlKey("getPortCols", params);
		return insPortCols;
	}
	 
	
	/**
	 * 全局个人布局(初始布局)
	 * @param portId
	 * @return
	 */
	public List<InsPortCol> getGlobalPortal(Long portId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("portId", portId);
		List<InsPortCol> insPortCols = getBySqlKey("getPortCols", params);
		return insPortCols;
	}
	
	
	
	/**
	 * 按组织获取布局
	 * @param portId
	 * @param orgId
	 * @return
	 */
	public List<InsPortCol> getByOrgId(Long portId, String orgId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("portId", portId);
		params.put("orgId", orgId);
		List<InsPortCol> insPortCols = getBySqlKey("getPortCols", params);
		return insPortCols;
	}
	
	/**
	 * 更新栏目的高度
	 * @param height
	 * @param colId
	 */
	public void updateHeight(Integer height, Long colId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("height", height);
		params.put("colId", colId);
		update("updateHeight", params);
	}
	
}
