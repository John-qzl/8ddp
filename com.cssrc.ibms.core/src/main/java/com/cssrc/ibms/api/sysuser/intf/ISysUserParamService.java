package com.cssrc.ibms.api.sysuser.intf;

import java.util.List;

import com.cssrc.ibms.api.sysuser.model.ISysUserParam;

public interface ISysUserParamService {

	/**
	 * 指定userid下的参数
	 * @param userId
	 * @return
	 */
	public abstract List<?extends ISysUserParam> getByUserId(long userId);

	/**
	 * paramKey作为指定参数值
	 * @param paramKey
	 * @param userId
	 * @return
	 */
	public abstract ISysUserParam getByParamKeyAndUserId(String paramKey,
			Long userId);

	/**
	 * paramKey, paramValue作为参数获取参数列表可求出用户类型和参数类型
	 * @param paramKey
	 * @param paramValue
	 * @return
	 */
	public abstract List<?extends ISysUserParam> getByParamKeyValue(String paramKey,
			Object paramValue);

	/**
	 * 删除该用户所有属性字段
	 * @param userId
	 */
	public abstract void delByUserId(Long userId);

}