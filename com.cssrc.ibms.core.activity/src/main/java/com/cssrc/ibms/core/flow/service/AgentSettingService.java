package com.cssrc.ibms.core.flow.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateTask;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IAgentSettingService;
import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.AgentConditionDao;
import com.cssrc.ibms.core.flow.dao.AgentDefDao;
import com.cssrc.ibms.core.flow.dao.AgentSettingDao;
import com.cssrc.ibms.core.flow.model.AgentCondition;
import com.cssrc.ibms.core.flow.model.AgentDef;
import com.cssrc.ibms.core.flow.model.AgentSetting;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.util.bean.BeanUtils;


/**
 *<pre>
 * 对象功能:代理设定 Service类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Service
public class AgentSettingService extends BaseService<AgentSetting> implements IAgentSettingService
{
	@Resource
	private AgentSettingDao dao;
	@Resource
	private AgentConditionDao agentConditionDao;
	@Resource
	private AgentDefDao agentDefDao;
	@Resource
	private IBpmService bpmService;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private AgentConditionService agentConditionService;
	@Resource
	private DefinitionService definitionService;
	@Resource
	private IFormDefService formDefService;
	
	
	
	public AgentSettingService()
	{
	}
	
	@Override
	protected IEntityDao<AgentSetting, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 根据代理设置，删除代理的条件和代理的流程设置信息
	 * @param id
	 */
	private void delByPk(Long id){
		agentConditionDao.delByMainId(id);
		agentDefDao.delByMainId(id);
	}
	
	/**
	 * 删除代理设置
	 * @param lAryId
	 */
	public void delAll(Long[] lAryId) {
		for(Long id:lAryId){	
			delByPk(id);
			dao.delById(id);	
		}	
	}
	
	/**
	 * 添加代理设置
	 * @param agentSetting
	 * @throws Exception
	 */
	public void addAll(AgentSetting agentSetting) throws Exception{
		add(agentSetting);
		addSubList(agentSetting);
	}
	
	/**
	 * 更新代理设置
	 * @param agentSetting
	 * @throws Exception
	 */
	public void updateAll(AgentSetting agentSetting) throws Exception{
		update(agentSetting);
		delByPk(agentSetting.getId());
		addSubList(agentSetting);
	}
	
	/**
	 * 添加获取条件代理的条件或部分代理的代理的流程
	 * @param agentSetting
	 * @throws Exception
	 */
	public void addSubList(AgentSetting agentSetting) throws Exception{
		List<AgentCondition> agentConditionList=agentSetting.getAgentConditionList();
		if(BeanUtils.isNotEmpty(agentConditionList)){
			for(AgentCondition agentCondition:agentConditionList){
				agentCondition.setSettingid(agentSetting.getId());
				agentCondition.setId(UniqueIdUtil.genId());
				agentConditionDao.add(agentCondition);
			}
		}
		List<AgentDef> agentDefList=agentSetting.getAgentDefList();
		if(BeanUtils.isNotEmpty(agentDefList)){
			for(AgentDef agentDef:agentDefList){
				agentDef.setSettingid(agentSetting.getId());
				agentDef.setId(UniqueIdUtil.genId());
				agentDefDao.add(agentDef);
			}
		}
	}
	
	/**
	 * 获取条件代理的条件
	 * @param id
	 * @return
	 */
	public List<AgentCondition> getAgentConditionList(Long id) {
		return agentConditionDao.getByMainId(id);
	}
	
	/**
	 * 获取部分代理的代理的流程
	 * @param id
	 * @return
	 */
	public List<AgentDef> getAgentDefList(Long id) {
		return agentDefDao.getByMainId(id);
	}

	
	/**
	 * 判断流程定义是否已经设置了有效部分代理设置
	 * @param flowkey 流程定义Key
	 * @return 
	 * 	true 已经设置了有效的部分代理设置
	 *  false 未设置有效的部分代理设置
	 */
	public boolean isComplictAgainstPartialByFlow(String flowkey,Date startDate,Date endDate,Long authid) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("flowkey", flowkey);
		params.put("authid", authid);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("authtype", AgentSetting.AUTHTYPE_PARTIAL);
		params.put("enabled", AgentSetting.ENABLED_YES);
		List<AgentSetting> settings = dao.getByFlowKey(flowkey,params);
		boolean flag=false;
		if(BeanUtils.isNotEmpty(settings)){
			flag=true;
		}
		return flag;
	}

	/**
	 * 判断流程定义是否已经设置了有效条件代理设置
	 * @param flowkey 流程定义Key
	 * @return 
	 * 	true 已经设置了有效的条件代理设置
	 *  false 未设置有效的条件代理设置
	 */
	public boolean isComplictAgainstConditionByFlow(String flowkey,Date startDate,Date endDate,Long authid) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("flowkey", flowkey);
		params.put("authid", authid);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("authtype", AgentSetting.AUTHTYPE_CONDITION);
		params.put("enabled", AgentSetting.ENABLED_YES);
		List<AgentSetting> settings = dao.getByFlowKey(flowkey,params);
		boolean flag=false;
		if(BeanUtils.isNotEmpty(settings)){
			flag=true;
		}
		return flag;
	}
	
	/**
	 * 判断流程定义是否已经设置了有效部分代理设置
	 * @param flowkey 流程定义Key
	 * @param settingId 排除的代理设置
	 * @return 
	 * 	true 已经设置了有效的部分代理设置
	 *  false 未设置有效的部分代理设置
	 */
	public boolean isComplictAgainstPartialByFlow(String flowkey,Date startDate,Date endDate,Long authid,Long settingId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("flowkey", flowkey);
		params.put("authid", authid);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("authtype",  AgentSetting.AUTHTYPE_PARTIAL);
		params.put("enabled", AgentSetting.ENABLED_YES);
		params.put("id", settingId);
		List<AgentSetting> settings = dao.getByFlowKey(flowkey,params);
		boolean flag=false;
		if(BeanUtils.isNotEmpty(settings)){
			flag=true;
		}
		return flag;
	}
	
	/**
	 * 判断流程定义是否已经设置了有效部分或条件代理设置
	 * @param flowkey 流程定义Key
	 * @param settingId 排除的代理设置
	 * @return 
	 * 	true 已经设置了有效的部分代理设置
	 *  false 未设置有效的部分代理设置
	 */
	public boolean isComplictAgainstPartialOrConditionByFlow(String flowkey,Date startDate,Date endDate,Long authid,Long settingId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("flowkey", flowkey);
		params.put("authid", authid);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
//		params.put("authtype",  AgentSetting.AUTHTYPE_PARTIAL);
		params.put("enabled", AgentSetting.ENABLED_YES);
		params.put("id", settingId);
		List<AgentSetting> settings = dao.getByFlowKey(flowkey,params);
		boolean flag=false;
		if(BeanUtils.isNotEmpty(settings)){
			flag=true;
		}
		return flag;
	}
	
	
	/**
	 * 判断流程定义是否已经设置了有效部分或条件代理设置
	 * @param flowkey 流程定义Key
	 * @param settingId 排除的代理设置
	 * @return 
	 * 	true 已经设置了有效的部分代理设置
	 *  false 未设置有效的部分代理设置
	 */
	public boolean isComplictAgainstPartialOrConditionByFlow(String flowkey,Date startDate,Date endDate,Long authid) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("flowkey", flowkey);
		params.put("authid", authid);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
//		params.put("authtype",  AgentSetting.AUTHTYPE_PARTIAL);
		params.put("enabled", AgentSetting.ENABLED_YES);
		List<AgentSetting> settings = dao.getByFlowKey(flowkey,params);
		boolean flag=false;
		if(BeanUtils.isNotEmpty(settings)){
			flag=true;
		}
		return flag;
	}

	/**
	 * 判断流程定义是否已经设置了有效条件代理设置
	 * @param flowkey 流程定义Key
	 * @param settingId 排除的代理设置
	 * @return 
	 * 	true 已经设置了有效的条件代理设置
	 *  false 未设置有效的条件代理设置
	 */
	public boolean isComplictAgainstConditionByFlow(String flowkey,Date startDate,Date endDate,Long authid,Long settingId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("flowkey", flowkey);
		params.put("authid", authid);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("authtype", AgentSetting.AUTHTYPE_CONDITION);
		params.put("enabled", AgentSetting.ENABLED_YES);
		params.put("id", settingId);
		List<AgentSetting> settings = dao.getByFlowKey(flowkey,params);
		boolean flag=false;
		if(BeanUtils.isNotEmpty(settings)){
			flag=true;
		}
		return flag;
	}
	
	
	/**
	 * 判断是否已经设置了有效全权代理设置
	 * @param flowkey 流程定义Key
	 * @return 
	 * 	true 已经设置了有效的条件代理设置
	 *  false 未设置有效的条件代理设置
	 */
	public boolean isComplictAgainstGeneral(Date startDate,Date endDate,Long authid) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("authid", authid);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("enabled", AgentSetting.ENABLED_YES);
		params.put("authtype", AgentSetting.AUTHTYPE_GENERAL);
		List<AgentSetting> settings = dao.getGeneralAgentByFilter(params);
		boolean flag=false;
		if(BeanUtils.isNotEmpty(settings)){
			flag=true;
		}
		return flag;
	}
	
	/**
	 * 判断是否已经设置了有效全权代理设置
	 * @param settingId 排除的代理设置
	 * @return 
	 * 	true 已经设置了有效的条件代理设置
	 *  false 未设置有效的条件代理设置
	 */
	public boolean isComplictAgainstGeneral(Date startDate,Date endDate,Long authid,Long settingId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("authid", authid);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("enabled", AgentSetting.ENABLED_YES);
		params.put("authtype", AgentSetting.AUTHTYPE_GENERAL);
		params.put("id", settingId);
		List<AgentSetting> settings = dao.getGeneralAgentByFilter(params);
		boolean flag=false;
		if(BeanUtils.isNotEmpty(settings)){
			flag=true;
		}
		return flag;
	}
	
	/**
	 * 判断是否已经设置了有效全权代理设置
	 * @param settingId 排除的代理设置
	 * @return 
	 * 	true 已经设置了有效的条件代理设置
	 *  false 未设置有效的条件代理设置
	 */
	public boolean validateSettingComplictAgainstAll(Date startDate,Date endDate,Long authid) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("authid", authid);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("enabled", AgentSetting.ENABLED_YES);
//		params.put("authtype", AgentSetting.AUTHTYPE_GENERAL);
		List<AgentSetting> settings = dao.getGeneralAgentByFilter(params);
		boolean flag=false;
		if(BeanUtils.isNotEmpty(settings)){
			flag=true;
		}
		return flag;
	}
	
	/**
	 * 判断是否已经设置了有效全权代理设置
	 * @param settingId 排除的代理设置
	 * @return 
	 * 	true 已经设置了有效的条件代理设置
	 *  false 未设置有效的条件代理设置
	 */
	public boolean validateSettingComplictAgainstAll(Date startDate,Date endDate,Long authid,Long settingId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("authid", authid);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("enabled", AgentSetting.ENABLED_YES);
//		params.put("authtype", AgentSetting.AUTHTYPE_GENERAL);
		params.put("id", settingId);
		List<AgentSetting> settings = dao.getGeneralAgentByFilter(params);
		boolean flag=false;
		if(BeanUtils.isNotEmpty(settings)){
			flag=true;
		}
		return flag;
	}
	
	/**
	 * 根据流程key、授权人和日期，取得代理设置
	 * @param flowKey 流程key
	 * @param authid 授权人
	 * @param date 日期
	 * @param cascade 级联查询标志。如果为true，将级联查询代理的流程和条件
	 * @return
	 */
	public AgentSetting getValidAgentSetting(String flowKey,Long authid,Date date,boolean cascade){
		AgentSetting setting = dao.getValidByFlowAndAuthidAndDate(flowKey,authid,date);
		if(setting==null){
			return null;
		}else{
			if(cascade){
				if(AgentSetting.AUTHTYPE_PARTIAL == setting.getAuthtype().intValue()){
					List<AgentDef> agentDefList = getAgentDefList(setting.getId());
					setting.setAgentDefList(agentDefList);
				}else if(AgentSetting.AUTHTYPE_CONDITION== setting.getAuthtype().intValue()) {
					List<AgentCondition> agentConditionList = getAgentConditionList(setting.getId());
					setting.setAgentConditionList(agentConditionList);
				}
			}
			return setting;
		}
	}
	
	

	/**
	 * 获取 代理人
	 * <pre>
	 * 	1.根据任务获取流程定义ID.
	 * 	2.根据流程定义ID，当前用户id，当前日期获取流程代理定义。
	 * 	3.根据流程代理定义获取任务的执行人。
	 * 		1.全局
	 * 		2.部分
	 * 		3.条件代理
	 * </pre>
	 * @param delegateTask
	 * @param userId
	 * @return
	 */
	public ISysUser getAgent(DelegateTask delegateTask ,Long userId){
		String actDefId = delegateTask.getProcessDefinitionId();
		Definition bpmDefinition= definitionService.getByActDefId(actDefId);
		
		String flowKey = bpmDefinition.getDefKey();
		Map<String,Object> formVars =delegateTask.getVariables();//; tuntimeService.getVariables(delegateTask.getExecutionId());
		//2016-7-1  yangbo  任务实例的流程定义id的判断
		if (formVars.containsKey("parentActDefId")) {
			String mainActDefId = (String)formVars.get("mainActDefId");
			bpmDefinition = this.definitionService.getByActDefId(mainActDefId);
		}
		ISysUser sysUser = null;
		Date currentDate = new Date();
		//获取代理配置。
		AgentSetting agentSetting = getValidAgentSetting(flowKey, userId, currentDate,false);
		if(agentSetting!=null){
			//全局
			if(AgentSetting.AUTHTYPE_GENERAL==agentSetting.getAuthtype().intValue()){
				sysUser = sysUserService.getById(agentSetting.getAgentid());
			}
			//部分代理
			else if(AgentSetting.AUTHTYPE_PARTIAL==agentSetting.getAuthtype().intValue()){
				sysUser = sysUserService.getById(agentSetting.getAgentid());
			}
			//条件代理
			else if(AgentSetting.AUTHTYPE_CONDITION ==agentSetting.getAuthtype().intValue()){
				Long tableId = formDefService.getTableIdByDefId(bpmDefinition.getDefId());
				if(tableId!=null){
				List<AgentCondition> conditions = getAgentConditionList(agentSetting.getId());
					for(AgentCondition condition:conditions){
						if(agentConditionService.conditionCheck(condition,tableId, formVars)){
							sysUser = sysUserService.getById(condition.getAgentid());
							break;
						}
					}
				}
			}
		}
		return sysUser;
	}

}

	
