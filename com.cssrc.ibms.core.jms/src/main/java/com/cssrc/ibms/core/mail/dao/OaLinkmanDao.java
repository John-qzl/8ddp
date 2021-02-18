package com.cssrc.ibms.core.mail.dao;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.mail.model.OaLinkman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class OaLinkmanDao extends BaseDao<OaLinkman> {
	public Class<?> getEntityClass() {
		return OaLinkman.class;
	}
	/**
	 * 根据userId和email获取联系人信息
	 *@author Yangbo @date 2016年10月10日上午9:30:52
	 */
	public List<OaLinkman> getByUserEmail(Long userId, String email) {
		Map params = new HashMap();
		params.put("userId", userId);
		params.put("email", email);
		return getBySqlKey("getByUserEmail", params);
	}
}
