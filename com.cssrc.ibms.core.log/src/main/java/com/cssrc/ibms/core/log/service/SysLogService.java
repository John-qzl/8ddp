package com.cssrc.ibms.core.log.service;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.system.intf.ISysLogService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.log.dao.SysLogDao;
import com.cssrc.ibms.core.log.model.SysLog;

@Service
public class SysLogService extends BaseService<SysLog> implements ISysLogService{
	@Resource
	private SysLogDao dao;
	@Resource
	private IFormFieldService formFieldService;

	@Override
	protected IEntityDao<SysLog, Long> getEntityDao() { 
		return dao;
	}
	/**
	 * 三元用户分配日志权限
	 *@author YangBo @date 2016年11月18日上午10:23:57
	 *@param filter
	 *@param curUserId
	 *@return
	 */
	public List<SysLog> getLogListByRight(QueryFilter filter,Long curUserId){
		String userIds=ISysUser.RIGHT_USER.toString()+","+ISysUser.SYSTEM_USER.toString();
		String implementId = ISysUser.IMPLEMENT_USER.toString();
		List<SysLog> list=new ArrayList<SysLog>();
		if(curUserId.longValue()!=ISysUser.CHECK_USER&&curUserId.longValue()!=ISysUser.RIGHT_USER&&curUserId.longValue()!=ISysUser.IMPLEMENT_USER){
			filter.addFilterForIB("userIds", "0");
			list=this.dao.getInUserIds(filter);
		}
	
		//check安全审计员 只查看 系统管理员和安全保密管理员
		if(curUserId.longValue()==ISysUser.CHECK_USER){
			filter.addFilterForIB("userIds", userIds);
			list=this.dao.getInUserIds(filter);		
		}else if(curUserId.longValue()==ISysUser.RIGHT_USER){//right 权限管理员查看普通用户和安全审计员,不包含系统实施员
			userIds += "," + implementId;
			filter.addFilterForIB("userIds", userIds);
			list=this.dao.getNotInUserIds(filter);		
		}else if(curUserId.longValue()==ISysUser.IMPLEMENT_USER){//implement 系统实施人员查看所有日志
			list=this.dao.getNotInUserIds(filter);		
		}
		return list;
		
	}
	
	/**
	 * 获取根据beanName获取表数据
	 *@author YangBo @date 2016年11月23日上午11:46:22
	 *@param beanName
	 *@param primaryKey
	 *@return
	 */
	public List getDataListByBean(Class<?> beanName,long primaryKey){
		return dao.getById(beanName.getName(),primaryKey);
	}
	
	/**
	 * 获取根据beanName获取用户相关表数据
	 *@author liubo
	 *@date 2017-8-23
	 *@param beanName
	 *@param primaryKey
	 *@return
	 */
	public List getUserDataListByBean(Class<?> beanName,long primaryKey){
		return dao.getByUserId(beanName.getName(),primaryKey);
	}
	
	/**
	 * 通过tableId和filedName获取字段中文名
	 *@author YangBo @date 2016年11月26日上午11:34:27
	 *@param tableId
	 *@param filedName
	 *@return
	 */
	public String getFieldDesc(Long tableId,String filedName){
		IFormField formField=formFieldService.getFieldByTidFna(tableId,filedName);
		String filedDesc=formField.getFieldDesc();
		return filedDesc;
	}
	 
}
