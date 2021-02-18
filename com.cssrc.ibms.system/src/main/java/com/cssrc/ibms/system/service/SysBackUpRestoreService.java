/**
 * 备份还原
 * @yangbo
 */
package com.cssrc.ibms.system.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.system.dao.SysBackUpRestoreDao;
import com.cssrc.ibms.system.model.SysBackUpRestore;

/**
 * 备份service
 * @see
 */
@Service
public class SysBackUpRestoreService extends BaseService<SysBackUpRestore> {
	@Resource
	private SysBackUpRestoreDao dao;
	@Override
	protected IEntityDao<SysBackUpRestore, Long> getEntityDao() {
		return dao;
	}
}
