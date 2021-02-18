package com.cssrc.ibms.index.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.index.dao.SysIndexLayoutDao;
import com.cssrc.ibms.index.model.SysIndexLayout;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class SysIndexLayoutService extends BaseService<SysIndexLayout> {

	@Resource
	private SysIndexLayoutDao dao;

	protected IEntityDao<SysIndexLayout, Long> getEntityDao() {
		return this.dao;
	}
}
