package com.cssrc.ibms.index.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.index.dao.AgendaMsgDao;
import com.cssrc.ibms.index.model.AgendaMsg;

/**
 * 日程交流信息Service层
 * @author YangBo
 *
 */
@Service
public class AgendaMsgService extends BaseService<AgendaMsg>{
	@Resource
	private AgendaMsgDao dao;
	
	protected IEntityDao<AgendaMsg, Long> getEntityDao() {
		return this.dao;
	}
	
	/**
	 * 删除日程相关交流信息
	 * @param agendaId
	 */
	public void delByAgendaId(Long agendaId) {
		this.dao.delByAgendaId(agendaId);
		
	}
	
}
