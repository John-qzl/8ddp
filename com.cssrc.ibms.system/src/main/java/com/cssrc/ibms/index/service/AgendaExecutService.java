package com.cssrc.ibms.index.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.index.dao.AgendaExecutDao;
import com.cssrc.ibms.index.model.Agenda;
import com.cssrc.ibms.index.model.AgendaExecut;

/**
 * 日程人员关系Service层
 * @author YangBo
 *
 */
@Service
public class AgendaExecutService extends BaseService<AgendaExecut>{
	@Resource
	private AgendaExecutDao dao;
	
	protected IEntityDao<AgendaExecut, Long> getEntityDao() {
		return this.dao;
	}
	
	/**
	 * 根据日程Id删除日程人员信息
	 * @param agendaId
	 */
	public void delByAgendaId(Long agendaId) {
		dao.delByAgendaId(agendaId);
	}
	
	/**
	 * 获取日程人员信息
	 * @param agendaId
	 * @return
	 */
	public List<AgendaExecut> getByAgendaId(Long agendaId){
		List<AgendaExecut> list = dao.getByAgendaId(agendaId);
		return list;
	}
	
	/**
	 * 将人员信息塞入日程对象中
	 * @param agenda
	 * @param executList
	 * @return
	 */
	public Agenda setAllExecutor(Agenda agenda ,List<AgendaExecut> executList){
		StringBuffer executorIds = new StringBuffer();//执行人Id组合
		StringBuffer executors = new StringBuffer(); //执新人名组合
		
		if(executList.size()>0){
			for(AgendaExecut agendaExecut : executList){
				executorIds.append(agendaExecut.getExecutorId()).append(","); 
				executors.append(agendaExecut.getExecutor()).append(","); 
			}
		}
		
		//如果存在就塞入对象
		if(executorIds.length()>0){
			executorIds.deleteCharAt(executorIds.length()-1);
			executors.deleteCharAt(executors.length()-1);
			agenda.setExecutorIds(executorIds);
			agenda.setExecutors(executors);
		}
		
		return agenda;
	}
	
}
