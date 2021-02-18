package com.cssrc.ibms.dp.form.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.form.model.TbInstant;

@Repository
public class TbInstantDao  extends BaseDao<TbInstant>{

	@Override
	public Class getEntityClass() {
		return TbInstant.class;
	}

	/**
	 * 表单实例是否存在
	 * @param id
	 * @return
	 */
	public Integer isInstantExists(String id) {
		Map params = new HashMap();
		params.put("moduleid", id);
		return (Integer) getOne("isIdExists", params);
	}
}
