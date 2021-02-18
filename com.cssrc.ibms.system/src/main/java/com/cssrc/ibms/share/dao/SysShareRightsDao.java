package com.cssrc.ibms.share.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.share.model.SysShareRights;

import org.springframework.stereotype.Repository;
/**
 * 
 * <p>Title:SysShareRightsDao</p>
 * @author Yangbo 
 * @date 2016-8-9下午03:17:26
 */
@Repository
public class SysShareRightsDao extends BaseDao<SysShareRights> {
	public Class<?> getEntityClass() {
		return SysShareRights.class;
	}
}
