package com.cssrc.ibms.system.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.IDemensionService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.system.dao.DemensionDao;
import com.cssrc.ibms.system.model.Demension;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
/**
 * 维度新加了查询方法
 * <p>Title:DemensionService</p>
 * @author Yangbo 
 * @date 2016-8-1下午04:28:13
 */
@Service
public class DemensionService extends BaseService<Demension> implements IDemensionService{

	@Resource
	private DemensionDao dao;

	protected IEntityDao<Demension, Long> getEntityDao() {
		return this.dao;
	}

	public boolean getNotExists(Map params) {
		return this.dao.getNotExists(params);
	}

	public List<Demension> getDemenByQuery(QueryFilter queryFilter) {
		return this.dao.getDemenByQuery(queryFilter);
	}
	
	public Demension getByName(String name) {
		return this.dao.getByName(name);
	}
}
