package com.cssrc.ibms.index.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.index.model.AgendaMsg;


/**
 * 日程交流信息DAO层
 * @author YangBo
 *
 */
@Repository
public class AgendaMsgDao extends BaseDao<AgendaMsg>{

	public Class<AgendaMsg> getEntityClass() {
		return AgendaMsg.class;
	}
	
	/**
	 * 删除日程下交流信息
	 * @param agendaId
	 */
	public void delByAgendaId(Long agendaId) {
		delBySqlKey("delByAgendaId", agendaId);
	}

}
