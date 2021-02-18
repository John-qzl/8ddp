package com.cssrc.ibms.index.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.index.model.AgendaExecut;


/**
 * 日程人员关系DAO层
 * @author YangBo
 *
 */
@Repository
public class AgendaExecutDao extends BaseDao<AgendaExecut>{

	public Class<AgendaExecut> getEntityClass() {
		return AgendaExecut.class;
	}
	
	/**
	 * 删除日程相关人员信息
	 * @param agendaId
	 */
	public void delByAgendaId(Long agendaId) {
		
		delBySqlKey("delByAgendaId", agendaId);
	}
	
	/**
	 * 根据日程id获取接收人list
	 * @param agendaId
	 * @return
	 */
	public List<AgendaExecut> getByAgendaId(Long agendaId){
		List<AgendaExecut> list = getBySqlKey("getByAgendaId",agendaId);
		return list;
	}

}
