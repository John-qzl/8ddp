package com.cssrc.ibms.core.user.service;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserParamService;
import com.cssrc.ibms.api.sysuser.model.ISysUserParam;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.SysUserParamDao;
import com.cssrc.ibms.core.user.model.SysUserParam;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 用户参数Service层
 * <p>Title:SysUserParamService</p>
 * @author Yangbo 
 * @date 2016-8-19上午11:33:12
 */
@Service
public class SysUserParamService extends BaseService<SysUserParam> implements ISysUserParamService{

	@Resource
	private SysUserParamDao sysUserParamDao;

	protected IEntityDao<SysUserParam, Long> getEntityDao() {
		return this.sysUserParamDao;
	}
	
	public void add(long userId, List<SysUserParam> valueList) {
		this.sysUserParamDao.delByUserId(userId);
		if ((valueList == null) || (valueList.size() == 0))
			return;
		for (SysUserParam p : valueList){
			p.setUserParam_creatorId(UserContextUtil.getCurrentUserId());
			p.setUserParam_createTime(new Date());
			p.setUserParam_updateId(UserContextUtil.getCurrentUserId());
			p.setUserParam_updateTime(new Date());
			this.sysUserParamDao.add(p);
		}
	}
	/**
	 * 指定userid下的参数
	 * @param userId
	 * @return
	 */
	public List<SysUserParam> getByUserId(long userId) {
		return this.sysUserParamDao.getBySqlKey("getByUserId", Long
				.valueOf(userId));
	}
	/**
	 * paramKey作为指定参数值
	 * @param paramKey
	 * @param userId
	 * @return
	 */
	public SysUserParam getByParamKeyAndUserId(String paramKey, Long userId) {
		return this.sysUserParamDao.getByParaUserId(userId.longValue(),
				paramKey);
	}
	/**
	 * paramKey, paramValue作为参数获取参数列表可求出用户类型和参数类型
	 * @param paramKey
	 * @param paramValue
	 * @return
	 */
	public List<SysUserParam> getByParamKeyValue(String paramKey,
			Object paramValue) {
		if ((StringUtil.isEmpty(paramKey)) || (paramValue == null)) {
			return null;
		}
		return this.sysUserParamDao.getByParamKeyValue(paramKey, paramValue);
	}
	/**
	 * 删除该用户所有属性字段
	 * @param userId
	 */
	public void delByUserId(Long userId) {
		this.sysUserParamDao.delByUserId(userId);
	}

}
