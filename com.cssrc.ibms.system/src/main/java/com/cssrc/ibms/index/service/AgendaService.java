package com.cssrc.ibms.index.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.index.dao.AgendaDao;
import com.cssrc.ibms.index.model.Agenda;
import com.cssrc.ibms.index.model.AgendaExecut;

/**
 * 日程信息Service层
 * @author YangBo
 *
 */
@Service
public class AgendaService extends BaseService<Agenda>{
	@Resource
	private AgendaDao dao;
	@Resource
	private AgendaExecutService agendaExecutService;
	@Resource
	private AgendaMsgService agendaMsgService;
	
	protected IEntityDao<Agenda, Long> getEntityDao() {
		return this.dao;
	}
	
	/**
	 * 删除日程相关信息包括交流信息
	 * @param uId
	 */
	public void delAgendas(String uId, Long curUserId) {
		String[] ids = uId.split(",");
		for (String id : ids) {
			if(StringUtils.isNotEmpty(id)){
				Long agendaId = Long.valueOf(id);
				Agenda agenda =(Agenda)this.getById(agendaId);
				//判断是否是该日程创建者,只有日程创建者拥有删除的权限
				if(agenda.getCreatorId().equals(curUserId)||curUserId<0L){
					
					this.delById(agendaId);
					agendaExecutService.delByAgendaId(agendaId);
					
					agendaMsgService.delByAgendaId(agendaId);
				}
				
				
			}
			
		}
	}
	
	/**
	 * 获取时间间隙
	 * @param dStartDate
	 * @param dEndDate
	 * @return
	 */
	public List<Date> getDates(Date dStartDate, Date dEndDate)
	{
		List<Date> resultList = new ArrayList<Date>();
		long lStartTime = dStartDate.getTime();
		long lEndTime = dEndDate.getTime();
		for (long lTempTime = lStartTime; lTempTime <= lEndTime; lTempTime = (lTempTime + 3600 * 24 * 1000))
		{
			Date tempDate = new Date(lTempTime);
			resultList.add(tempDate);
		}
		return resultList;
	}
	
	/**
	 * 获取当前我的日程fullCalendar视图数据
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Agenda> getCalendarList(Date startTime, Date endTime, Long userId) {
		List<Agenda> list = this.dao.getCalendarList(startTime,endTime, userId);
		return list;
	}
	
	
	/**
	 * 日程列表插入人员信息
	 * @param list
	 * @return
	 */
	public List<Agenda> setAllExecutors(List<Agenda> list) {
		if(list.size() > 0){
			for(Agenda agenda : list){
				Long key = agenda.getAgendaId();
				if(key !=null){
					List<AgendaExecut> executList =  agendaExecutService.getByAgendaId(key);
					agendaExecutService.setAllExecutor(agenda, executList);
				}
			}
		}
		return list;
	}
	
	/**
	 * 添加日程执行人信息
	 * @param agenda
	 */
	public void addExecutData(Agenda agenda) throws Exception{
		//删除之前的人员关联
		agendaExecutService.delByAgendaId(agenda.getAgendaId());
		
		StringBuffer executorIds = agenda.getExecutorIds();
		StringBuffer executors = agenda.getExecutors();
		try{
			Long agendaId = agenda.getAgendaId();
			if(executorIds.length() >0){
				String[] userIds = executorIds.toString().split(",");
				String[] userNames = executors.toString().split(",");
				for(int i=0;i<userIds.length;i++){
					AgendaExecut agendaExecut = new AgendaExecut();
					agendaExecut.setId(UniqueIdUtil.genId());
					agendaExecut.setAgendaId(agendaId);
					agendaExecut.setExecutorId(Long.valueOf(userIds[i]));
					agendaExecut.setExecutor(userNames[i]);
					agendaExecutService.add(agendaExecut);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	
}
