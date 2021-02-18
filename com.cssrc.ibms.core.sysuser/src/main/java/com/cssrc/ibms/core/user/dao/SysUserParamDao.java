package com.cssrc.ibms.core.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.SysUserParam;
/**
 * 用户定义参数DAO层
 * <p>Title:SysUserParamDao</p>
 * @author Yangbo 
 * @date 2016-8-19上午11:23:05
 */
@Repository
public class SysUserParamDao extends BaseDao<SysUserParam> {
	public Class getEntityClass() {
		return SysUserParam.class;
	}
	/**
	 * 删除指定用户的参数
	 * @param userId
	 * @return
	 */
	public int delByUserId(long userId) {
		return delBySqlKey("delByUserId", Long.valueOf(userId));
	}
	/**
	 * 获取用户所有参数
	 * @param userId
	 * @return
	 */
	public List<SysUserParam> getByUserId(long userId) {
		return getBySqlKey("getByUserId", Long.valueOf(userId));
	}
	/**
	 * 某个用户的对应key参数
	 * @param userId
	 * @param paramKey
	 * @return
	 */
	public SysUserParam getByParaUserId(long userId, String paramKey) {
		Map params = new HashMap();
		params.put("userId", Long.valueOf(userId));
		params.put("paramKey", paramKey);
		return (SysUserParam) getUnique("getByParaUserId", params);
	}
	/**
	 * 对应参数的数据列表
	 * @param paramKey
	 * @param paramValue
	 * @return
	 */
	public List<SysUserParam> getByParamKeyValue(String paramKey,
			Object paramValue) {
		Map params = new HashMap();
		params.put("paramKey", paramKey);
		params.put("paramValue", paramValue);
		return getBySqlKey("getByParamKeyValue", params);
	}
}
