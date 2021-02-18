package com.cssrc.ibms.core.user.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.ISysParamService;
import com.cssrc.ibms.api.system.model.ISysParam;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgParamService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.SysOrgParamDao;
import com.cssrc.ibms.core.user.model.SysOrgParam;
import com.cssrc.ibms.core.util.bean.BeanUtils;
/**
 * 
 * <p>Title:SysOrgParamService</p>
 * @author Yangbo 
 * @date 2016-8-3上午09:56:04
 */
@Service
public class SysOrgParamService extends BaseService<SysOrgParam> implements ISysOrgParamService{
	@Resource
	private SysOrgParamDao sysOrgParamDao;
	@Resource
	private ISysParamService sysParamService;

	protected IEntityDao<SysOrgParam, Long> getEntityDao() {
		return this.sysOrgParamDao;
	}

	public void add(long orgId, List<SysOrgParam> valueList) {
		this.sysOrgParamDao.delByOrgId(orgId);
		if ((valueList == null) || (valueList.size() == 0))
			return;
		for (SysOrgParam p : valueList){
			p.setOrgParam_creatorId(UserContextUtil.getCurrentUserId());
			p.setOrgParam_createTime(new Date());
			p.setOrgParam_updateId(UserContextUtil.getCurrentUserId());
			p.setOrgParam_updateTime(new Date());
			this.sysOrgParamDao.add(p);
		}
	}

	public List<SysOrgParam> getByOrgId(Long orgId) {
		List list = this.sysOrgParamDao.getByOrgId(orgId);
		return list;
	}

	public List<SysOrgParam> getListByOrgId(Long orgId) {
		List<SysOrgParam> list = this.sysOrgParamDao.getByOrgId(orgId);
		if (list.size() > 0) {
			for (SysOrgParam param : list) {
				long paramId = param.getParamId().longValue();
				ISysParam sysParam = this.sysParamService.getById(Long
						.valueOf(paramId));
				if (BeanUtils.isNotEmpty(sysParam)) {
					param.setParamName(sysParam.getParamName());
				}
			}
		}
		return list;
	}

	public SysOrgParam getByParamKeyAndOrgId(String paramKey, Long orgId) {
		return this.sysOrgParamDao.getByParamKeyAndOrgId(paramKey, orgId);
	}
}
