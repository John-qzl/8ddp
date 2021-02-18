package com.cssrc.ibms.core.flow.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IProCopytoService;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.ISysTemplateService;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.dao.DefinitionDao;
import com.cssrc.ibms.core.flow.dao.ProCopytoDao;
import com.cssrc.ibms.core.flow.model.CcMessageType;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.ProCopyto;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 *<pre>
 * 对象功能:流程抄送转发 Service类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Service
public class ProCopytoService extends BaseService<ProCopyto> implements IProCopytoService
{
	protected Logger logger = LoggerFactory.getLogger(ProCopytoService.class);
	@Resource
	private ProCopytoDao dao;
	@Resource
	private DefinitionDao definitionDao;
	@Resource
	private NodeUserService nodeUserService;
	@Resource
	private TaskMessageService taskMessageService;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private ISysTemplateService sysTemplateService;
	@Resource
	private ProcessRunService processRunService;
	
	public ProCopytoService()
	{
	}
	
	@Override
	protected IEntityDao<ProCopyto, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 标记为已读
	 * @param ids
	 */
	public void updateReadStatus(String ids){
		if(StringUtil.isEmpty(ids))return;
		String[] idArr = ids.split(",");
		for(String id : idArr){
			if(StringUtil.isEmpty(id))continue;
			Long copyId = Long.parseLong(id);
			if(copyId>0){
				ProCopyto bpmProCopyto = dao.getById(copyId);
				bpmProCopyto.setIsReaded(1L);
				bpmProCopyto.setReadTime(new Date());
				dao.update(bpmProCopyto);
			}
		}
	}
	
	/**
	 * 获取我的抄送列表
	 * @param queryFilter
	 * @return
	 */
	public List<ProCopyto> getMyList(QueryFilter queryFilter){
		return dao.getMyList(queryFilter);
	}
	
	/**
	 * 标记为已读
	 * @param ids
	 */
	public void markCopyStatus(String id){
		if(StringUtil.isEmpty(id))return;
		ProCopyto bpmProCopyto = dao.getById(Long.parseLong(id));
		bpmProCopyto.setIsReaded(1L);
		bpmProCopyto.setReadTime(new Date());
		dao.update(bpmProCopyto);
	}
	
	/**
	 * 处理抄送任务
	 * @param executionEntity
	 * @param vars
	 */
	public void handlerCopyTask(ProcessRun processRun,Map<String,Object> vars,String preTaskUserId,Definition bpmDefinition) throws Exception{
		if(BeanUtils.isEmpty(bpmDefinition))return;
		
		String actDefId = processRun.getActDefId();
		Long defTypeId = bpmDefinition.getTypeId();
		
		Integer allowFinishedCc = bpmDefinition.getAllowFinishedCc();
		//流程未设置允许办结抄送时  不处理抄送
		if(allowFinishedCc.intValue()!=1)return;
		String subject = processRun.getSubject();
		if(BeanUtils.isEmpty(processRun))return;
		Long startUserId = processRun.getCreatorId();
		String instanceId = processRun.getActInstId();
		List<ISysUser> copyConditionUsers =nodeUserService.getCopyUserByActDefId(actDefId, instanceId, startUserId, vars, preTaskUserId);
		
		
		if(BeanUtils.isEmpty(copyConditionUsers))return;
		//记录抄送日志。
		logger.info("handlerCopyTask:" +actDefId +"," + instanceId + "," + processRun.getRunId() +"," + subject );
		
		
		addCopyTo(copyConditionUsers,processRun,defTypeId);
		handlerCopyMessage(actDefId, copyConditionUsers,vars,subject,processRun.getRunId(), bpmDefinition.getCcMessageType());
	}
	
	private void addCopyTo(List<ISysUser> set,ProcessRun processRun,Long defTypeId){
		String instanceId=processRun.getActInstId();
		Long startUserId=processRun.getCreatorId();
		String creator=processRun.getCreator();
		
		for(Iterator<ISysUser> it=set.iterator();it.hasNext();){
			ISysUser  executor=it.next();
			
			String copyUserId = executor.getUserId().toString();
			String copyUserName = executor.getFullname();
			ProCopyto bpmProCopyto = new ProCopyto();
			bpmProCopyto.setActInstId(Long.parseLong(instanceId));
			bpmProCopyto.setCcTime(new Date());
			bpmProCopyto.setCcUid(Long.parseLong(copyUserId));
			bpmProCopyto.setCcUname(copyUserName);
			bpmProCopyto.setCopyId(UniqueIdUtil.genId());
			bpmProCopyto.setCpType(ProCopyto.CPTYPE_COPY);
			bpmProCopyto.setIsReaded(0L);
			bpmProCopyto.setRunId(processRun.getRunId());
			bpmProCopyto.setSubject(processRun.getSubject());
			bpmProCopyto.setDefTypeId(defTypeId);
			bpmProCopyto.setCreateId(startUserId);
			bpmProCopyto.setCreator(creator);
			dao.add(bpmProCopyto);
		}
	}
	
	/**
	 * 处理抄送提醒消息
	 * @param conditionId
	 * @param copyUsers
	 * @throws Exception 
	 */
	private void handlerCopyMessage(String actDefId,List<ISysUser> receiverUserList,Map<String,Object> vars,String subject,Long runId, String ccMessageType) throws Exception{
		if(BeanUtils.isEmpty(receiverUserList))return;
		ISysUser curUser=(ISysUser)UserContextUtil.getCurrentUser();
		String informTypes = "";
		if(StringUtil.isEmpty(ccMessageType)){
			//若抄送类型未设置，则默认为站内消息和邮件
			ccMessageType = "{\"inner\":1,\"mail\":1,\"sms\":0}";
		}
		JSONObject jsonObj=JSONObject.fromObject(ccMessageType);
		CcMessageType cc =(CcMessageType)JSONObject.toBean(jsonObj, CcMessageType.class);
		
		Map<String,String> msgTempMap = sysTemplateService.getTempByFun(ISysTemplate.USE_TYPE_COPYTO);
		
		if(cc.getMail()==1){//邮件
			if(!informTypes.contains(BpmConst.MESSAGE_TYPE_MAIL))
				informTypes = informTypes+","+BpmConst.MESSAGE_TYPE_MAIL;
		}
		
		if(cc.getInner()==1){//站内消息
			if(!informTypes.contains(BpmConst.MESSAGE_TYPE_INNER))
				informTypes = informTypes+","+BpmConst.MESSAGE_TYPE_INNER;
		}
		
		if(cc.getSms()==1){//短息
			if(!informTypes.contains(BpmConst.MESSAGE_TYPE_SMS))
				informTypes = informTypes+","+BpmConst.MESSAGE_TYPE_SMS;
		}
		
		//记录抄送信息日志
		logger.info("[发送抄送提醒信息]subject:" +subject +",informTypes:" + informTypes + ",runId:" + runId );
		
		taskMessageService.sendMessage(curUser, receiverUserList, informTypes, msgTempMap, subject, "", null, runId);
	}
	

	
	/**
	 * 根据运行Id获取用户数据。
	 * @param queryFilter
	 * @return
	 */
	public List<ProCopyto> getUserInfoByRunId(QueryFilter queryFilter){
		return dao.getUserInfoByRunId(queryFilter);
	}
	
	/**
	 * 根据流程runId删除抄送转发事宜
	 * @param runId
	 */
	public void delByRunId(Long runId) {
		dao.delByRunId(runId);
	}
	
	/**
	 * 显示抄送总数目
	 */
	@Override
	public Integer getCountByUser(Long userId) {
		return this.dao.getCountByUser(userId);
	}
	
	/**
	 * 返回未读抄送数量
	 */
	@Override
	public Integer getCountNotReadByUser(Long userId) {
		return this.dao.getCountNotReadByUser(userId);
	}
	
	/**
	 *获取当前用户的抄送数据 
	 * @param curUserId
	 * @param pb
	 * @return
	 */
	public List<ProCopyto> getMyProCopytoList(Long curUserId, PagingBean pb) {
		return this.dao.getMyProCopytoList(curUserId,pb);
	}
	
}
