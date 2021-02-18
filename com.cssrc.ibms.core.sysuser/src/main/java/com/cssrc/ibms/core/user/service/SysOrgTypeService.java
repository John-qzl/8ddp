package com.cssrc.ibms.core.user.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgTypeService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.SysOrgTypeDao;
import com.cssrc.ibms.core.user.model.SysOrgType;
/**
 * 
 * <p>Title:SysOrgTypeService</p>
 * @author Yangbo 
 * @date 2016-8-2上午09:00:33
 */
@Service
public class SysOrgTypeService extends BaseService<SysOrgType> implements ISysOrgTypeService{

	@Resource
	private SysOrgTypeDao dao;

	protected IEntityDao<SysOrgType, Long> getEntityDao() {
		return this.dao;
	}

	public Integer getMaxLevel(Long demId) {
		return this.dao.getMaxLevel(demId.longValue());
	}

	public List<SysOrgType> getByDemId(long demId) {
		return this.dao.getByDemId(demId);
	}
}
