package com.cssrc.ibms.core.mail.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.mail.model.OutMailLinkman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
/**
 * 
 * <p>Title:OutMailLinkmanDao</p>
 * @author Yangbo 
 * @date 2016年9月29日下午3:51:38
 */
@Repository
public class OutMailLinkmanDao extends BaseDao<OutMailLinkman> {
	public Class getEntityClass() {
		return OutMailLinkman.class;
	}
	
	/**
	 * 最近联系人
	 *@author YangBo @date 2016年10月10日上午10:59:19
	 *@param address
	 *@param userId
	 *@return
	 */
	public OutMailLinkman findLinkMan(String address, long userId) {
		Map params = new HashMap();
		params.put("userId", Long.valueOf(userId));
		params.put("linkAddress", address);
		return (OutMailLinkman) getUnique("findLinkMan", params);
	}

	public List<OutMailLinkman> getAllByUserId(Map params) {
		String statement = "getAllByUserId_" + getDbType();
		return getBySqlKey(statement, params);
	}
}
