package com.cssrc.ibms.bus.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.bus.dao.BusQueryFilterDao;
import com.cssrc.ibms.bus.dao.BusQueryShareDao;
import com.cssrc.ibms.bus.model.BusQueryFilter;
import com.cssrc.ibms.bus.model.BusQueryShare;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
/**
 * 
 * <p>Title:BusQueryShareService</p>
 * @author Yangbo 
 * @date 2016-8-9下午03:14:36
 */
@Service
public class BusQueryShareService extends BaseService<BusQueryShare> {

	@Resource
	private BusQueryShareDao dao;

	@Resource
	private BusQueryFilterDao busQueryFilterDao;

	protected IEntityDao<BusQueryShare, Long> getEntityDao() {
		return this.dao;
	}

	public BusQueryShare getByFilterId(Long filterId) {
		return this.dao.getByFilterId(filterId);
	}

	public void save(BusQueryShare busQueryShare, boolean flag) {
		String shareRight = busQueryShare.getShareRight();
		JSONObject json = JSONObject.fromObject(shareRight);
		String type = json.getString("type");
		BusQueryFilter busQueryFilter = (BusQueryFilter) this.busQueryFilterDao
				.getById(busQueryShare.getFilterId());
		if (flag)
			this.dao.add(busQueryShare);
		else {
			this.dao.update(busQueryShare);
		}
		if ("none".equalsIgnoreCase(type))
			busQueryFilter.setIsShare(Short.valueOf((short) 0));
		else {
			busQueryFilter.setIsShare(Short.valueOf((short) 1));
		}
		this.busQueryFilterDao.update(busQueryFilter);
	}
}
