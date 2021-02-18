package com.cssrc.ibms.report.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.report.model.ISignModel;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.report.model.SignModel;

@Repository
public class SignModelDao extends BaseDao<SignModel>{

	@Override
	public Class getEntityClass() {
		return SignModel.class;
	}

	/**
	 * 获取用户所有签章模板
	 * @param userId
	 * @return
	 */
	public List<? extends ISignModel> getByUserId(Long userId) {
		return this.getListBySqlKey("getByUserId", userId);
	}
	
	/**
	 * 重置用户所有默认的印章
	 * @param id
	 */
	public int updateDefaultNot(Long userid) {
		return this.update("updateDefaultNot",userid);
	}
	/**
	 * 更新印章模板为默认印章
	 * @param id
	 */
	public int updateDefault(Long id) {
		return this.update("updateDefault", id);
	}

}