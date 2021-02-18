package com.cssrc.ibms.core.flow.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.activity.intf.IDefinitionService;
import com.cssrc.ibms.api.activity.intf.ISubtableRightsService;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.ContextUtil;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.intf.IFormRightsService;
import com.cssrc.ibms.api.form.intf.IFormRunService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.BaseFormDefXml;
import com.cssrc.ibms.api.form.model.BaseFormRights;
import com.cssrc.ibms.api.form.model.BaseFormTableXml;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormDefXml;
import com.cssrc.ibms.api.form.model.IFormRights;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.IFormTableXml;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.model.BaseSysFile;
import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserRoleService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.cache.ActivitiDefCache;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.activity.util.BpmUtil;
import com.cssrc.ibms.core.bpm.entity.CallActivity;
import com.cssrc.ibms.core.bpm.entity.FlowElement;
import com.cssrc.ibms.core.bpm.entity.UserTask;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.XmlUtil;
import com.cssrc.ibms.core.util.file.ZipUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.dao.BpmDao;
import com.cssrc.ibms.core.flow.dao.DefRightsDao;
import com.cssrc.ibms.core.flow.dao.DefVarDao;
import com.cssrc.ibms.core.flow.dao.DefinitionDao;
import com.cssrc.ibms.core.flow.dao.ExecutionDao;
import com.cssrc.ibms.core.flow.dao.ExecutionStackDao;
import com.cssrc.ibms.core.flow.dao.GangedSetDao;
import com.cssrc.ibms.core.flow.dao.NodeButtonDao;
import com.cssrc.ibms.core.flow.dao.NodeMessageDao;
import com.cssrc.ibms.core.flow.dao.NodePrivilegeDao;
import com.cssrc.ibms.core.flow.dao.NodeRuleDao;
import com.cssrc.ibms.core.flow.dao.NodeScriptDao;
import com.cssrc.ibms.core.flow.dao.NodeSetDao;
import com.cssrc.ibms.core.flow.dao.NodeSignDao;
import com.cssrc.ibms.core.flow.dao.NodeUserDao;
import com.cssrc.ibms.core.flow.dao.NodeWebServiceDao;
import com.cssrc.ibms.core.flow.dao.ProStatusDao;
import com.cssrc.ibms.core.flow.dao.ReferDefinitionDao;
import com.cssrc.ibms.core.flow.dao.ReminderStateDao;
import com.cssrc.ibms.core.flow.dao.TaskApprovalItemsDao;
import com.cssrc.ibms.core.flow.dao.TaskDao;
import com.cssrc.ibms.core.flow.dao.TaskOpinionDao;
import com.cssrc.ibms.core.flow.dao.TaskReminderDao;
import com.cssrc.ibms.core.flow.dao.TaskSignDataDao;
import com.cssrc.ibms.core.flow.dao.UserConditionDao;
import com.cssrc.ibms.core.flow.model.DefRights;
import com.cssrc.ibms.core.flow.model.DefVar;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.FormResult;
import com.cssrc.ibms.core.flow.model.GangedSet;
import com.cssrc.ibms.core.flow.model.NodeButton;
import com.cssrc.ibms.core.flow.model.NodeMessage;
import com.cssrc.ibms.core.flow.model.NodeRule;
import com.cssrc.ibms.core.flow.model.NodeScript;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.NodeSign;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.model.NodeUserUplow;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.TaskApprovalItems;
import com.cssrc.ibms.core.flow.model.TaskExe;
import com.cssrc.ibms.core.flow.model.TaskReminder;
import com.cssrc.ibms.core.flow.model.UserCondition;
import com.cssrc.ibms.core.flow.util.BPMN20Util;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.core.flow.xml.DefinitionXml;
import com.cssrc.ibms.core.flow.xml.DefinitionXmlList;
import com.cssrc.ibms.core.bpm.entity.Process;
import com.cssrc.ibms.core.bpm.entity.ht.BPMN20HtExtConst;

/**
 * 对象功能:流程定义扩展 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class DefinitionService extends BaseService<Definition> implements IDefinitionService
{
	@Resource
	private DefinitionDao dao;	
	@Resource
	private NodeSetDao nodeSetDao;	
	@Resource
	private DefVarDao defVarDao;		
	@Resource
	private IUserRoleService userRoleService;
	@Resource
	private IBpmService bpmService;
	@Resource
	private NodeSignDao nodeSignDao;
	@Resource
	private NodeRuleDao nodeRuleDao;
	@Resource
	private TaskSignDataDao taskSignDataDao;
	@Resource
	private NodeMessageDao nodeMessageDao; 
	@Resource
	private ExecutionStackDao executionStackDao;
	@Resource
	private NodeUserDao nodeUserDao;
	@Resource
	private NodeScriptDao nodeScriptDao;
	@Resource
	private DefRightsDao defRightDao;
	@Resource
	private NodeButtonDao nodeButtonDao;
	@Resource
	private TaskApprovalItemsDao taskApprovalItemsDao;
	@Resource
	private TaskReminderDao taskReminderDao;
	@Resource
	private DefRightsDao defRightsDao; 
	@Resource
	private UserConditionDao userConditionDao;
	@Resource
	private IGlobalTypeService globalTypeService;
	@Resource
	private BpmDao bpmDao;
	@Resource
	private IFormDefService formDefService;
	@Resource
	private IFormTableService formTableService;
	@Resource
	private IFormRunService formRunService;
	@Resource
	private NodePrivilegeDao nodePrivilegeDao;
	@Resource
	private NodeWebServiceDao nodeWebServiceDao;
	@Resource
	private ProStatusDao proStatusDao;
	@Resource
	private TaskForkService taskForkService;
	@Resource
	private TaskOpinionDao taskOpinionDao;
	@Resource
	private ReminderStateDao reminderStateDao;
	@Resource
	private ProcessRunService processRunService;
	@Resource
	private TaskDao taskDao;
	@Resource
	private ExecutionDao executionDao;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	protected Properties configproperties;
	@Resource
	protected ISysRoleService sysRoleService;
	@Resource
	protected IFormRightsService formRightsService;
	@Resource
	protected ISysOrgService sysOrgService;
	@Resource
	protected IPositionService positionService;
	@Resource
	protected TaskService taskService;
	@Resource
	protected TaskVarsService taskVarsService;
	@Resource
	protected IFormHandlerService formHandlerService;
	@Resource
	protected RunLogService runLogService;
	@Resource
	protected RuntimeService runtimeService; 
	@Resource
	protected ISubtableRightsService subtableRightsService;
	@Resource
	protected ReferDefinitionDao referDefinitionDao;
	@Resource
	protected ProCopytoService proCopytoService;
	@Resource
	protected TaskExeService taskExeService;
	@Resource
	protected CommuReceiverService commuReceiverService;
	@Resource
	protected TaskReadService taskReadService;
	@Resource
	private ISysFileService sysFileService;
	@Resource
	private GangedSetDao gangedSetDao;	 
	
	
	public DefinitionService()
	{
	}
	
	@Override
	protected IEntityDao<Definition, Long> getEntityDao() {
		return dao;
	}
	
	/**
	 * 发布流程数据。
	 * @param bpmDefinition
	 * @param actFlowDefXml
	 * @throws Exception 
	 */
	public void deploy(Definition bpmDefinition,String actFlowDefXml) throws Exception{
		Deployment deployment=bpmService.deploy(bpmDefinition.getSubject(), actFlowDefXml);
		ProcessDefinitionEntity ent= bpmService.getProcessDefinitionByDeployId(deployment.getId());
		bpmDefinition.setActDeployId(new Long(deployment.getId()));
		bpmDefinition.setActDefId(ent.getId());
		bpmDefinition.setActDefKey(ent.getKey());
		bpmDefinition.setStatus(Definition.STATUS_TEST);
		dao.update(bpmDefinition);
		
		saveOrUpdateNodeSet(actFlowDefXml,bpmDefinition,false);
	}
	
	
	/**
	 * 保存及更新流程定义
	 * @param definition 流程定义实体
	 * @param isDeploy 是否发布新流程
	 * @param actFlowDefXml 流程定义bpmn文档
	 * @throws Exception
	 */
	public void saveOrUpdate(Definition definition,boolean isDeploy,String actFlowDefXml) throws Exception
	{
	    
		Long   oldDefId=definition.getDefId();
		Long   newDefId=definition.getDefId();
		boolean isUpdate=false;
		//新增加的流程
		if(definition.getDefId()==null || definition.getDefId()==0)
		{
			if(isDeploy)//发布定义
			{
				Deployment deployment=bpmService.deploy(definition.getSubject(), actFlowDefXml);
				ProcessDefinitionEntity ent= bpmService.getProcessDefinitionByDeployId(deployment.getId());
				definition.setActDeployId(new Long(deployment.getId()));
				definition.setActDefId(ent.getId());
				definition.setActDefKey(ent.getKey());
			}
			definition.setVersionNo(1);
			definition.setDefId(UniqueIdUtil.genId());
			//主版本
			definition.setIsMain(Definition.MAIN);
			definition.setCreatetime(new Date());
			definition.setUpdatetime(new Date());
			definition.setToFirstNode((short)1);
			definition.setInformStart(FlowUtil.getDefaultSelectInfoType());
			Short status=isDeploy?Definition.STATUS_TEST:Definition.STATUS_UNDEPLOYED;
			definition.setStatus(status);
			add(definition);
			
			if(isDeploy){
				//设置流程节点信息
				saveOrUpdateNodeSet(actFlowDefXml,definition,true);
			}
		}
		//更新流程定义
		else{
			//发布了新的版本定义
			if(isDeploy){
				newDefId=UniqueIdUtil.genId();
				dao.updateSubVersions(newDefId,definition.getDefKey());
				
				Deployment deployment=bpmService.deploy(definition.getSubject(), actFlowDefXml);
				ProcessDefinitionEntity ent= bpmService.getProcessDefinitionByDeployId(deployment.getId());
				String actDefId=ent.getId();
				//原bpmFinition
				Definition newDefinition=(Definition)definition.clone();
				//增加版本号
				newDefinition.setVersionNo(ent.getVersion());
				newDefinition.setActDeployId(new Long(deployment.getId()));
				newDefinition.setActDefId(actDefId);
				newDefinition.setActDefKey(ent.getKey());
				//发布新版本后，需要生成新的发布记录
				newDefinition.setDefId(newDefId);
				newDefinition.setParentDefId(newDefId);
				newDefinition.setUpdatetime(new Date());
				//newDefinition.setStatus(Definition.STATUS_ENABLED);
				//设置新的流程序为主版本
				newDefinition.setIsMain(Definition.MAIN);
				//添加新版本的流程定义
				add(newDefinition);
				
				isUpdate=true;
				//设置流程节点信息
				saveOrUpdateNodeSet(actFlowDefXml,newDefinition,true);
				//同步起始节点全局表单的配置情况。
				syncStartGlobal(oldDefId,newDefId,actDefId);
				
				
			}
			else{
				//直接修改定义
				if(definition.getActDeployId()!=null){
					bpmService.wirteDefXml(definition.getActDeployId().toString(), actFlowDefXml);
					//设置流程节点信息
					saveOrUpdateNodeSet(actFlowDefXml,definition,false);
					
					String actDefId=definition.getActDefId();
					//清除节点的缓存
					NodeCache.clear(actDefId);
					//清除流程缓存。
					ActivitiDefCache.clearByDefId(actDefId);
					
				}
				update(definition);
			}
		}
		
		if(isUpdate){//发布了新的版本定义
			saveOrUpdateBpmDefSeting(newDefId,oldDefId,actFlowDefXml, definition.getDefKey());
		}
		
		updateKeyPath(definition, actFlowDefXml);
		//Locale locale = ContextUtil.getLocale();
		//saveLanguageInfo(bpmDefinition.getActDefId(), actFlowDefXml, locale.toString());
	}

    private void updateKeyPath(Definition definition,String actFlowDefXml)
    {
        this.dao.updateKeyPathByPKeyPath(definition.getDefKey());
        Map<String,String> set=NodeCache.getSubFlowKeyPath(definition.getDefKey(),actFlowDefXml);

        for (String flowKey : set.keySet())
        {
            // 修改子流程 key——path
            this.dao.updateKeyPath(flowKey, set.get(flowKey));
            
        }
    }
	
	
	/**
	 * 保存流程定义的节点国际化资源信息
	 * 
	 * @param actDefId
	 * @param actFlowDefXml
	 * @param language
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private void saveLanguageInfo(String actDefId, String actFlowDefXml,
			String language) {
		if (BeanUtils.isEmpty(actDefId))
			return;
		Document doc = Dom4jUtil.loadXml(actFlowDefXml);
		List<Node> list = doc.selectNodes("/definitions//*[@name]");

		Map<String, Map<String, String>> reskeyMap = new HashMap<String, Map<String,String>>();
		List<String> reskeyList=new ArrayList<String>();
		String local = ContextUtil.getLocale().toString();
		for (Node node : list) {
			Element element = (Element) node;
			String id = element.attributeValue("id");
			String name = element.attributeValue("name");
			if (StringUtil.isEmpty(id) || StringUtil.isEmpty(name))
				continue;
			reskeyList.add(id);
			Map<String, String> map = new HashMap<String, String>();
			map.put(local, name);
			reskeyMap.put(id, map);
		}
		/*for (int i = 0; i < reskeyList.size(); i++) {
			bpmFormLanguageService.batchSaveUpdate(actDefId, reskeyList.get(i), reskeyMap.get(reskeyList.get(i)), BpmFormLanguage.BPM_DEFINITION_TYPE);
		}*/
	}
	
	/**
	 * 设置流程节点属性。
	 * @param actFlowDefXml
	 * @param bpmDefinition
	 * @param isAdd
	 * @throws Exception
	 */
	private void saveOrUpdateNodeSet(String actFlowDefXml,Definition bpmDefinition,boolean isAdd) throws Exception{
		Long defId=bpmDefinition.getDefId();
		List<Process> processes = BPMN20Util.getProcess(actFlowDefXml);
		if(processes.size()==0){
			return;
		}
		@SuppressWarnings("unchecked")
		Class<FlowElement>[] classes= new Class[]{
				UserTask.class,
				CallActivity.class
		};
		List<FlowElement> flowElements = BPMN20Util.getFlowElementByType(processes.get(0),true,classes);
		if(isAdd){
			for(FlowElement flowElement:flowElements){
				addNodeSet(bpmDefinition,flowElement);
			}
		}else{
			Map<String,NodeSet> nodeSetMap=nodeSetDao.getMapByDefId(defId);
			//删除无用的节点
			delNodeSet(nodeSetMap,flowElements);
			//添加新增的节点
			updNodeSet(bpmDefinition,nodeSetMap,flowElements);
		}
	}
	
	/**
	 * 删除当前流程定义中没有的节点。
	 * @param oldSetMap
	 * @param curNodeMap
	 * @param bpmDefinition
	 */
	@SuppressWarnings("unused")
	private void delNodeSet(Map<String,NodeSet> oldSetMap,Map<String,String> curNodeMap){
		Iterator<String> keys=oldSetMap.keySet().iterator();
		while(keys.hasNext()){
			String nodeId=keys.next();
			if(curNodeMap.containsKey(nodeId)) continue;
			NodeSet bpmNodeSet=oldSetMap.get(nodeId);
			nodeSetDao.delById(bpmNodeSet.getSetId());
		}
	}
	
	/**
	 * 原来流程定义中没有的节点则添加流程节点定义。
	 * @param oldSetMap
	 * @param curNodeMap
	 * @param bpmDefinition
	 * @throws Exception
	 */
	private void updNodeSet(Definition bpmDefinition,Map<String,NodeSet> oldSetMap,List<FlowElement> flowNodes) throws Exception{
		for(FlowElement flowElement:flowNodes){
			if(oldSetMap.containsKey(flowElement.getId())){
				Integer nodeOrder = 0;
				List<Object> extensions = BPMN20Util.getFlowElementExtension(flowElement, BPMN20HtExtConst._Order_QNAME);
				
				if(BeanUtils.isNotEmpty(extensions)){
					nodeOrder=(Integer) extensions.get(0);
				}
				NodeSet bpmNodeSet=oldSetMap.get(flowElement.getId());
				bpmNodeSet.setNodeName(flowElement.getName());
				bpmNodeSet.setNodeOrder(nodeOrder);
				nodeSetDao.update(bpmNodeSet);
			}else{
				addNodeSet(bpmDefinition, flowElement);
			}
		}
	}
	
	/**
	 * 原来流程定义中没有的节点则添加流程节点定义。
	 * @param oldSetMap
	 * @param curNodeMap
	 * @param bpmDefinition
	 * @throws Exception
	 */
//	private void updNodeSet(Map<String,NodeSet> oldSetMap,Map<String,String> curNodeMap,Definition bpmDefinition) throws Exception{
//		Iterator<String> keys=curNodeMap.keySet().iterator();
//		while(keys.hasNext()){
//			String nodeId=keys.next();
//			String actNodeName=curNodeMap.get(nodeId);
//			if(oldSetMap.containsKey(nodeId)) {
//				NodeSet bpmNodeSet=oldSetMap.get(nodeId);
//				String nodeName=curNodeMap.get(nodeId);
//				bpmNodeSet.setNodeName(nodeName);
//				bpmNodeSetDao.update(bpmNodeSet);
//			}
//			else{
//				addNodeSet(bpmDefinition,nodeId,actNodeName);
//			}
//		}
//	}
	
	/**
	 * 删除当前流程定义中没有的节点。
	 * @param oldSetMap  原有流程节点
	 * @param flowNodes 新流程节点
	 */
	private void delNodeSet(Map<String,NodeSet> oldSetMap,List<FlowElement> flowNodes){
		Iterator<String> keys=oldSetMap.keySet().iterator();
		while(keys.hasNext()){
			String nodeId=keys.next();
			boolean inflag=false;
			for(FlowElement flowNode:flowNodes){
				if(flowNode.getId().equals(nodeId)){
					inflag=true;
					break;
				}
			}
			if(inflag) continue;
			NodeSet bpmNodeSet=oldSetMap.get(nodeId);
			nodeSetDao.delById(bpmNodeSet.getSetId());
		}
	}
	
	/**
	 * 添加流程定义节点设置。
	 * @param bpmDefinition 流程定义
	 * @param flowNode 流程节点
	 * @throws Exception
	 */
	private void addNodeSet(Definition bpmDefinition,FlowElement flowNode) throws Exception{
		Long defId=bpmDefinition.getDefId();
		String actDefId=bpmDefinition.getActDefId();
		Integer nodeOrder = 0;
		List<Object> extensions = BPMN20Util.getFlowElementExtension(flowNode, BPMN20HtExtConst._Order_QNAME);
		
		if(BeanUtils.isNotEmpty(extensions)){
			nodeOrder=(Integer) extensions.get(0);
		}
		
		NodeSet bpmNodeSet=new NodeSet();
		bpmNodeSet.setSetId(UniqueIdUtil.genId());
		bpmNodeSet.setFormType((short)-1);
		bpmNodeSet.setActDefId(actDefId);
		bpmNodeSet.setDefId(defId);
		bpmNodeSet.setNodeId(flowNode.getId());
		bpmNodeSet.setNodeName(flowNode.getName());
		bpmNodeSet.setNodeOrder(nodeOrder);
		nodeSetDao.add(bpmNodeSet);
	}
	
//	private void addNodeSet(Definition bpmDefinition,String actNodeId,String actNodeName) throws Exception{
//		Long defId=bpmDefinition.getDefId();
//		String actDefId=bpmDefinition.getActDefId();
//		NodeSet bpmNodeSet=new NodeSet();
//		bpmNodeSet.setSetId(UniqueIdUtil.genId());
//		bpmNodeSet.setFormType((short)-1);
//		bpmNodeSet.setActDefId(actDefId);
//		bpmNodeSet.setDefId(defId);
//		bpmNodeSet.setNodeId(actNodeId);
//		bpmNodeSet.setNodeName(actNodeName);
//		bpmNodeSetDao.add(bpmNodeSet);
//	}
	
	/**
	 * 保存及更新流程定义的相关配置
	 * @param newDefId
	 * @param oldDefId
	 * @throws Exception 
	 */
	private void saveOrUpdateBpmDefSeting(Long newDefId,Long oldDefId,String actFlowDefXml,String defKey) throws Exception{
		
		if(oldDefId==null || oldDefId<=0) return;
		
		Definition newDef =getById(newDefId);
		Definition oldDef =getById(oldDefId);
		
		String newActDefId=newDef.getActDefId();
		String oldActDefId=oldDef.getActDefId();
		if(oldActDefId==null) return;
		
		
		//BPM_AGENT	流程代理配置
//		List<BpmAgent> agentList=bpmAgentDao.getByDefKey(defKey);
//		if(BeanUtils.isNotEmpty(agentList)){
//			for(BpmAgent o:agentList){
//				BpmAgent n=(BpmAgent)o.clone();
//				n.setId(UniqueIdUtil.genId());
//				n.setDefKey(defKey);
//				bpmAgentDao.add(n);
//			}
//		}
		
		//BPM_DEF_RIGHT	流程定义权限
//		List<BpmDefRights> defRight=bpmDefRightDao.getByDefKey(defKey);
//		if(BeanUtils.isNotEmpty(defRight)){
//			for(BpmDefRights o:defRight){
//				BpmDefRights n=(BpmDefRights)o.clone();
//				n.setId(UniqueIdUtil.genId());
//				n.setDefKey(defKey);
//				bpmDefRightDao.add(n);
//			}
//		}
		
		//BPM_DEF_VARS	流程变量定义 OK
		List<DefVar> defVarList=defVarDao.getByDefId(oldDefId);
		if(BeanUtils.isNotEmpty(defVarList)){
			for(DefVar o:defVarList){
				DefVar n=(DefVar)o.clone();
				n.setVarId(UniqueIdUtil.genId());
				n.setDefId(newDefId);
				defVarDao.add(n);
			}
		}

		//BPM_NODE_SCRIPT	流程开始结束节点事件脚本 OK
		List<NodeScript> nodeScripts= nodeScriptDao.getByActDefId(oldActDefId);
		Map<String, Map<String, String>> taskActivitysMap = BpmUtil.getTaskActivitys(actFlowDefXml);
		Map<String,String> startActivitysMap = taskActivitysMap.get("开始节点");
		Map<String,String> endActivitysMap = taskActivitysMap.get("结束节点");
		Map<String,String> seActivitysMap=new HashMap<String, String>();
		if(!BeanUtils.isEmpty(startActivitysMap)){
			seActivitysMap.putAll(startActivitysMap);
		}
		if(!BeanUtils.isEmpty(endActivitysMap)){
			seActivitysMap.putAll(endActivitysMap);
		}
		Iterator<String> seNodeIds=seActivitysMap.keySet().iterator();
		while(seNodeIds.hasNext()){
			String nodeId=seNodeIds.next();
			for(NodeScript script:nodeScripts){
				if(script.getNodeId().equals(nodeId)){
					NodeScript newScript=(NodeScript)script.clone();
					newScript.setId(UniqueIdUtil.genId());
					newScript.setActDefId(newActDefId);
					nodeScriptDao.add(newScript);
				}
			}
		}
		
		
		/*//BPM_APPROVAL_ITEM 全局常用语
		TaskApprovalItems globalTApproval=taskApprovalItemsDao.getFlowApproval(oldActDefId, TaskApprovalItems.global);
		if(BeanUtils.isNotEmpty(globalTApproval)){
			globalTApproval.setActDefId(newActDefId);
			globalTApproval.setItemId(UniqueIdUtil.genId());
			taskApprovalItemsDao.add(globalTApproval);
		}*/
		
		//BPM_NODE_SET	流程节点配置 OK
		List<NodeSet> newNodeSetList= nodeSetDao.getByDefId(newDefId);
		Map<String, NodeSet> oldNodeSetMap= nodeSetDao.getMapByDefId(oldDefId);
		if(BeanUtils.isEmpty(newNodeSetList) || BeanUtils.isEmpty(oldNodeSetMap)) return ;
		for(NodeSet bpmNodeSet:newNodeSetList){
			String nodeId=bpmNodeSet.getNodeId();
			if(!oldNodeSetMap.containsKey(nodeId)) continue;
			
			NodeSet oldNodeSet=oldNodeSetMap.get(nodeId);
			Long oldSetId=oldNodeSet.getSetId();
			
			//更新当前的节点配置信息
			bpmNodeSet.setAfterHandler(oldNodeSet.getAfterHandler());
			bpmNodeSet.setBeforeHandler(oldNodeSet.getBeforeHandler());
			bpmNodeSet.setFormDefId(oldNodeSet.getFormDefId());
			bpmNodeSet.setFormDefName(oldNodeSet.getFormDefName());
			bpmNodeSet.setFormKey(oldNodeSet.getFormKey());
			bpmNodeSet.setFormType(oldNodeSet.getFormType());
			bpmNodeSet.setFormUrl(oldNodeSet.getFormUrl());
			bpmNodeSet.setIsHideOption(oldNodeSet.getIsHideOption());
			bpmNodeSet.setIsHidePath(oldNodeSet.getIsHidePath());
			bpmNodeSet.setIsJumpForDef(oldNodeSet.getIsJumpForDef());
			bpmNodeSet.setJoinTaskKey(oldNodeSet.getJoinTaskKey());
			bpmNodeSet.setJoinTaskName(oldNodeSet.getJoinTaskName());
			bpmNodeSet.setJumpType(oldNodeSet.getJumpType());
			bpmNodeSet.setOldFormKey(oldNodeSet.getOldFormKey());
			nodeSetDao.update(bpmNodeSet);
	
			
			//BPM_NODE_RULE	流程节点规则 OK
			List<NodeRule> nodeRuleList=nodeRuleDao.getByDefIdNodeId(oldActDefId, nodeId);
			if(BeanUtils.isNotEmpty(nodeRuleList)){
				for(NodeRule oR:nodeRuleList){
					NodeRule nR=(NodeRule)oR.clone();
					nR.setRuleId(UniqueIdUtil.genId());
					nR.setActDefId(newActDefId);
					nodeRuleDao.add(nR);
				}
			}
			
			//BPM_NODE_SCRIPT	流程节点事件脚本 OK
			List<NodeScript> nodeScriptList= nodeScriptDao.getByNodeScriptId(nodeId,oldActDefId);
			if(BeanUtils.isNotEmpty(nodeScriptList)){
				for(NodeScript oS:nodeScriptList){
					NodeScript nS=(NodeScript)oS.clone();
					nS.setId(UniqueIdUtil.genId());
					nS.setActDefId(newActDefId);
					nodeScriptDao.add(nS);
				}
			}
			
			//BPM_USER_CONDITION 流程节点人员规则 OK
			List<UserCondition> userConditionList = userConditionDao.getBySetId(oldSetId);
			if(BeanUtils.isNotEmpty(userConditionList)){
				for(UserCondition oC:userConditionList){
					UserCondition nC=(UserCondition)oC.clone();
					nC.setId(UniqueIdUtil.genId());
					nC.setActdefid(newActDefId);
					nC.setSetId(bpmNodeSet.getSetId());
					userConditionDao.add(nC);
					//BPM_NODE_USER 	流程节点人员 OK
					List<NodeUser> nodeUserList = nodeUserDao.getByConditionId( oC.getId());
					if(BeanUtils.isNotEmpty(nodeUserList)){
						for(NodeUser oU:nodeUserList){
							NodeUser nU=(NodeUser)oU.clone();
							
							nU.setNodeUserId(UniqueIdUtil.genId());
							
							nU.setConditionId(nC.getId());
							nodeUserDao.add(nU);
						}
					}
				}
			}
			
			//BPM_NODE_MESSAGE	流程消息节点 OK
			List<NodeMessage> nodeMessageList=nodeMessageDao.getByActDefId(oldActDefId);
			if(BeanUtils.isNotEmpty(nodeMessageList)){
				for(NodeMessage oM:nodeMessageList){
					NodeMessage nM=(NodeMessage)oM.clone();
					nM.setId(UniqueIdUtil.genId());
					nM.setActDefId(newActDefId);
					nodeMessageDao.add(nM);
				}
			}
			
			//BPM_NODE_SIGN	任务会签设置 OK
			NodeSign nodeSign= nodeSignDao.getByDefIdAndNodeId(oldActDefId,nodeId);
			if(BeanUtils.isNotEmpty(nodeSign)){
				NodeSign newSign=(NodeSign)nodeSign.clone();
				newSign.setSignId(UniqueIdUtil.genId());
				newSign.setActDefId(newActDefId);
				nodeSignDao.add(newSign);
			}
			
			//BPM_NODE_BTN 操作按钮节点设置
			List<NodeButton> nodeButtonList = nodeButtonDao.getByDefNodeId(oldDefId, nodeId);
			if(BeanUtils.isNotEmpty(nodeButtonList)){
				for(NodeButton oB:nodeButtonList){
					NodeButton nB=oB;
					nB.setId(UniqueIdUtil.genId());
					nB.setActdefid(newActDefId);
					nB.setDefId(newDefId);
					nodeButtonDao.add(nB);
				}
			}
			/*//BPM_APPROVAL_ITEM 非全局常用语
			TaskApprovalItems approvalItems=taskApprovalItemsDao.getTaskApproval(oldActDefId, nodeId, TaskApprovalItems.notGlobal);
			if(BeanUtils.isNotEmpty(approvalItems)){
				approvalItems.setActDefId(newActDefId);
				approvalItems.setSetId(bpmNodeSet.getSetId());
				approvalItems.setItemId(UniqueIdUtil.genId());	
				taskApprovalItemsDao.add(approvalItems);
			}*/
			//BPM_TASK_DUE 催办
			
		//	TaskReminder taskReminder = taskReminderDao.getByActDefAndNodeId(oldActDefId, nodeId);
//			TaskReminder taskReminder = null;
			List<TaskReminder> taskReminders = taskReminderDao.getByActDefAndNodeId(oldActDefId, nodeId);
			for(TaskReminder taskReminder:taskReminders){
				taskReminder.setActDefId(newActDefId);
				taskReminder.setTaskDueId(UniqueIdUtil.genId());
				taskReminderDao.add(taskReminder);
			}
		}
		
	}
	
	/**
	 * 同步起始节点表单的配置情况。
	 * @param oldDefId
	 * @param newDefId
	 * @param newActDefId
	 * @throws Exception
	 */
	private void  syncStartGlobal(Long oldDefId,Long newDefId,String newActDefId) throws Exception{
		//同步流程起始表单和默认表单配置。
		List<NodeSet> list=nodeSetDao.getByOther(oldDefId);
		for(NodeSet nodeSet:list){
			nodeSet.setSetId(UniqueIdUtil.genId());
			nodeSet.setDefId(newDefId);
			nodeSet.setActDefId(newActDefId);
			nodeSetDao.add(nodeSet);
		}
	}
	
	

	
	/**
	 * 取得某个流程下的所有历史版本的定义
	 * @param defId
	 * @return
	 */
	public List<Definition> getAllHistoryVersions(Long defId)
	{
		return dao.getByParentDefIdIsMain(defId,Definition.NOT_MAIN);
	}
	
	/**
	 * 根据ACT流程定义id获取流程定义。
	 * @param actDefId
	 * @return
	 */
	public Definition getByActDefId(String actDefId)
	{
		return dao.getByActDefId(actDefId);
	}
	
	/**
	 * 根据分类Id得到流程定义
	 * @param typeId
	 * @return
	 */
	public List<Definition> getByTypeId(Long typeId)
	{
		return dao.getByTypeId(typeId);
	}
	
	/**
	 * 用于查询管理员下的所有流程
	 * @param queryFilter
	 * @return
	 */
	public List<Definition> getAllForAdmin(QueryFilter queryFilter)
	{
		return dao.getAllForAdmin(queryFilter);
	}
	
	/**
	 * 设置标题规则。
	 * @param defId
	 * @param taskNameRule
	 * @return
	 */
	public int saveParam(Definition bpmDefinition){
		return dao.saveParam(bpmDefinition);
	}
	/**
	 * 删除流程定义
	 * @param flowDefId
	 * @param isOnlyVersion 是否仅删除本版本，不包括其他历史版本
	 */
	public void delDefbyDeployId(Long flowDefId,boolean isOnlyVersion){
		
		 if(BeanUtils.isEmpty(flowDefId))return;
	        
        Definition definition=dao.getById(flowDefId);
        //若该版本尚没有发布
        if(definition.getActDeployId()==null){
        	delById(definition.getDefId());
        	return;
        }
        
        if(isOnlyVersion){//仅删除该版本
        	delDefinition(definition);
        	return;
        }
        
        String actFlowKey=definition.getActDefKey();
        
        List<Definition> list=dao.getByActDefKey(actFlowKey);
        
       
		//删除流程系统表
		for(Definition bpmDefinition:list){
		    
			delDefinition(bpmDefinition);
		}
        
		
	}
	
	private void delDefinition(Definition bpmDefinition){
		Long actDeployId=bpmDefinition.getActDeployId();	
		Long defId=bpmDefinition.getDefId();
		String actDefId=bpmDefinition.getActDefId();
		if(StringUtil.isNotEmpty(actDefId)){
			
			//删除流程运行实例表单数据BPM_FORM_RUN 
			formRunService.delByActDefId(actDefId);
			//删除节点操作的按钮BPM_NODE_BTN
			nodeButtonDao.delByActDefId(actDefId);
			//删除节点消息BPM_NODE_MESSAGE
    		nodeMessageDao.delByActDefId(actDefId);
			//删除节点特权BPM_NODE_PRIVILEGE 
			nodePrivilegeDao.delByActDefId(actDefId);
			//删除节点规则BPM_NODE_RULE
    		nodeRuleDao.delByActDefId(actDefId);
    		//删除节点运行脚本BPM_NODE_SCRIPT
    		nodeScriptDao.delByActDefId(actDefId);
    		//关联BPM_NODE_WEBSERVICE删除BPM_NODE_WS_PARAMS
    		/*List<NodeWebService> bpmNodeWebServiceList =  nodeWebServiceDao.getByActDefId(actDefId);
    		for(NodeWebService bpmNodeWebService:bpmNodeWebServiceList){
    			bpmNodeWsParamsDao.delByWebserviceId(bpmNodeWebService.getId());
    		}*/
    		//删除webservice节点设置BPM_NODE_WEBSERVICE
    		nodeWebServiceDao.delByActDefId(actDefId);
    		//删除流程节点状态BPM_PRO_STATUS
    		proStatusDao.delByActDefId(actDefId);
    		//删除定义催办信息BPM_TASK_DUE
    		taskReminderDao.delByActDefId(actDefId);
    		//删除分发实体BPM_TASK_FORK
    		taskForkService.delByActDefId(actDefId);
    		//删除流程运行实例BPM_PRO_RUN同时删除交办任务BPM_TASK_ASSIGNEE,BPM_PRO_RUN_HIS,ACT_RU_IDENTITYLINK
    		//ACT_RU_TASK,ACT_RU_VARIABLE,ACT_RU_EXECUTION,BPM_PRO_CPTO,BPM_TASK_EXE,BPM_COMMU_RECEIVER,BPM_TASK_READ
			processRunService.delByActDefId(actDefId);
			//删除流程意见信息BPM_TASK_OPINION 
			taskOpinionDao.delByActDefId(actDefId);
			//删除任务提醒状态数据BPM_TASK_REMINDERSTATE 
			reminderStateDao.delByActDefId(actDefId);
			//关联BPM_USER_CONDITION删除BPM_NODE_USER
			List<UserCondition> bpmUserConditionList = userConditionDao.getByActDefId(actDefId);
			for(UserCondition bpmUserCondition:bpmUserConditionList){
				nodeUserDao.delByConditionId(bpmUserCondition.getId());
			}
			//删除节点人员规则BPM_USER_CONDITION 
			userConditionDao.delByActDefId(actDefId);
    		//删除BPM_EXE_STACK
    		executionStackDao.delByActDefId(actDefId);
    		//删除BPM_TKSIGN_DATA
    		taskSignDataDao.delByIdActDefId(actDefId);
    		//删除BPM_NODE_SIGN会签规则
    		nodeSignDao.delActDefId(actDefId);
    		//删除BPM_REFER_DEFINITION流程引用
    		referDefinitionDao.delByDefId(defId);
    		//删除BPM_SUBTABLE_RIGHTS子表数据权限
    		subtableRightsService.delByActDefId(actDefId);
    		//删除BMP_FORM_RIGHTS表单数据权限
    		formRightsService.delByActDefId(actDefId, true);
		}
		if(actDeployId!=null && actDeployId>0){
			//删除删除流程定义数据表ACT_GE_BYTEARRAY，ACT_RE_DEPLOYMENT，ACT_RE_PROCDEF
		    dao.delProcDefByActDeployId(actDeployId);
		}
		//删除bpmDefRight
		defRightDao.delByDefKey(bpmDefinition.getDefKey());
		
		//删除bpm_def_vars
		defVarDao.delByDefId(defId);
		//删除节点数据BPM_NODE_SET 
		nodeSetDao.delByDefId(defId);
		
		dao.delById(defId);
	}
	

	
	// =========== TODO 导入数据库====
	/**
	 * 导入压缩文件
	 * @param fileLoad
	 * @throws Exception
	 */
	public void importZip(MultipartFile fileLoad) throws Exception {

		String realFilePath = StringUtil.trimSufffix(
				AppUtil.getAttachPath().toString(), File.separator)
				//+ File.separator
				//+ "attachFiles"
				+ File.separator
				+ "tempUnZip"
				+ File.separator;
		// 解压文件
		String fileDir = ZipUtil.unZipFile(fileLoad, realFilePath);
		MsgUtil.addFilePath(realFilePath);
		realFilePath = realFilePath+fileDir+File.separator ;
		String xmlStr = FileOperator.readFile(realFilePath+fileDir+ ".flow.xml");
		if (StringUtils.isEmpty(xmlStr))
			throw new Exception(getText("controller.importXml.fail"));

		Document doc = Dom4jUtil.loadXml(xmlStr);
		Element root = doc.getRootElement();
		// 验证格式是否正确
		XmlUtil.checkXmlFormat(root, "flow", "definitions");
		this.importXml(xmlStr, realFilePath);
		// 删除临时解压文件目录
		FileOperator.deleteDir(new File(realFilePath));
		
	}
    
    /**
     * 导入数据库
     * 
     * @param importFilePath
     * 
     * @param inputStream
     * @param bpmDefinition
     * @throws Exception
     */
    public void importXml(String xmlStr, String unzipFilePath)
        throws Exception
    {
        
        ArrayList<Class<?>> class_ = formDefService.getAllClass();
        class_.add(DefinitionXmlList.class);
        Class<?>[] _class = new Class[class_.size()];
        class_.toArray(_class);
        JAXBContext context = JAXBContext.newInstance(_class);
        Unmarshaller u = context.createUnmarshaller();
        
        DefinitionXmlList bpmDefinitionXmlList = (DefinitionXmlList)u.unmarshal(new StringReader(xmlStr));
        
        List<DefinitionXml> list = bpmDefinitionXmlList.getDefinitionXmlList();
        // 保存相关信息
        for (DefinitionXml definitionXml : list)
        {
            this.importDefinitionXml(definitionXml, unzipFilePath);
            MsgUtil.addSplit();
        }
        
    }

	/**
	 * 解析相关信息，导入相关list并保持数据库
	 * 
	 * @param definitionXml
	 * @param unzipFilePath
	 * @param flag
	 * @throws Exception
	 */
	private void importDefinitionXml(DefinitionXml definitionXml,
			String unzipFilePath) throws Exception {

		// 导入自定义表
		List<BaseFormTableXml> bpmFormTableXmlList = definitionXml
				.getFormTableXmlList();
		formTableService.importFormTableXml(bpmFormTableXmlList);
	
		List<Map<Long, Long>> mapFormKeyList = new ArrayList<Map<Long, Long>>();
		//
		List<Map<Long, Long>> mapFormIdList = new ArrayList<Map<Long, Long>>();
		// 导入自定义表单
		List<? extends IFormDefXml> bpmFormDefXmlList = definitionXml.getFormDefXmlList();
		formDefService.importFormDef(bpmFormDefXmlList,mapFormKeyList,mapFormIdList);
		// 导入流程定义
		this.importDefinition(definitionXml, mapFormKeyList,
				mapFormIdList, unzipFilePath);
	}

	/**
	 * 解析相关信息，导入相关list并保持数据库
	 * 
	 * <pre>
	 * 导入以下信息:
	 * ■ 流程定义 bpmDefinition
	 * ■ 历史版本 bpmDefinitionHistory
	 * 	
	 * ■ 流程节点设置 bpmNodeSet
	 * ■ 节点下的人员设置  bpmNodeUser
	 * ■ 节点下的人员的配置规则 bpmUserCondition
	 * ■ 节点下的人员上下级设置 bpmNodeUserUplow
	 * 	
	 * ■ 流程定义权限 bpmDefRights
	 * ■ 常用语设置 taskApprovalItems
	 * 	
	 * ■ 流程跳转规则  bpmNodeRule
	 * ■ 流程事件脚本  bpmNodeScript
	 * 	
	 * ■ 流程操作按钮设置 bpmNodeButton
	 * ■ 流程变量  bpmDefVar
	 * 	 
	 * ■ 流程消息  bpmNodeMessage
	 * ■ 流程会签规则  bpmNodeSign
	 * 
	 * ■ 任务节点催办时间设置 taskReminder
	 * ■ 内（外）部子流程 subDefinition
	 * </pre>
	 * 
	 * @param definitionXml
	 *            导入的流程定义的xml
	 * @param mapFormKeyList
	 * @param mapFormIdList
	 * @param unzipFilePath
	 * @param parentDefId
	 *            主流程定义的id
	 * @param flag
	 * @throws Exception
	 */
	private void importDefinition(DefinitionXml definitionXml,
			List<Map<Long, Long>> mapFormKeyList,
			List<Map<Long, Long>> mapFormIdList, String unzipFilePath)
			throws Exception {
		// 附件
		List<BaseSysFile> sysFileList = definitionXml.getSysFileList();
		if (BeanUtils.isNotEmpty(sysFileList)){
			String realPath = StringUtil.trimSufffix(AppUtil.getAttachPath().toString(), File.separator);
			sysFileService.importSysFile(sysFileList, unzipFilePath, realPath);
		}
		Definition bpmDefinition = definitionXml.getDefinition();
		// 流程定义
		bpmDefinition = this.importDefinition(bpmDefinition);
		String actDefId = bpmDefinition.getActDefId();
		Long defId = bpmDefinition.getDefId();
		String defKey = bpmDefinition.getDefKey();

		// Long actDeployId = bpmDefinition.getActDeployId();
		// 流程定义历史版本
//		List<DefinitionXml> bpmDefinitionXmlList = bpmDefinitionXml.getDefinitionXmlList();
//		if (BeanUtils.isNotEmpty(bpmDefinitionXmlList)) {
//			// 历史版本版本导入
//			for (DefinitionXml definitionXml : bpmDefinitionXmlList) {
//				this.importDefinition(definitionXml, null,null,null);
//			}
//		}

		// 含有子流程
		// List<DefinitionXml> subDefinitionXmlList = bpmDefinitionXml
		// .getSubDefinitionXmlList();
		//
		// if (BeanUtils.isNotEmpty(subDefinitionXmlList)) {
		// // 子流程导入
		// for (DefinitionXml definitionXml : subDefinitionXmlList) {
		// this.importDefinition(definitionXml, null);
		// }
		// }

		// 流程定义权限
		List<DefRights> defRightsList = definitionXml
				.getDefRightsList();
		if (BeanUtils.isNotEmpty(defRightsList))
			this.importDefRights(defRightsList, defKey);

		// 表单权限
		List<? extends IFormRights> bpmFormRightsList = definitionXml.getFormRightsList();
		if (BeanUtils.isNotEmpty(bpmFormRightsList))
			this.importFormRights(bpmFormRightsList, mapFormKeyList,actDefId);
		
		// 流程跳转规则
		List<NodeRule> bpmNodeRuleList = definitionXml
				.getNodeRuleList();
		if (BeanUtils.isNotEmpty(bpmNodeRuleList))
			this.importNodeRule(bpmNodeRuleList, actDefId);

		// 流程事件脚本
		List<NodeScript> bpmNodeScriptList = definitionXml
				.getNodeScriptList();
		if (BeanUtils.isNotEmpty(bpmNodeScriptList))
			this.importNodeScript(bpmNodeScriptList, actDefId);

		// 流程变量
		List<DefVar> bpmDefVarList = definitionXml.getDefVarList();
		if (BeanUtils.isNotEmpty(bpmDefVarList))
			this.importDefVar(bpmDefVarList, defId);

		// 流程会签规则
		List<NodeSign> bpmNodeSignList = definitionXml
				.getNodeSignList();
		if (BeanUtils.isNotEmpty(bpmNodeSignList))
			this.importNodeSign(bpmNodeSignList, actDefId);

		// 流程消息
		List<NodeMessage> bpmNodeMessageList = definitionXml
				.getNodeMessageList();
		if (BeanUtils.isNotEmpty(bpmNodeMessageList))
			this.importNodeMessage(bpmNodeMessageList, actDefId);
//		 List<Message> messageList =
//		 messageDao.getByActDefId(actDefId);

		// 导入节点的相关信息（包含：节点，节点人员条件，节点人员，节点人员的上下级，常用语）
		this.importNodeSet(definitionXml, defId, actDefId, mapFormIdList);

		// 流程操作按钮设置
		List<NodeButton> bpmNodeButtonList = definitionXml
				.getNodeButtonList();
		if (BeanUtils.isNotEmpty(bpmNodeButtonList))
			this.importNodeButton(bpmNodeButtonList, defId, actDefId);

		// 任务节点催办时间设置
		List<TaskReminder> taskReminderList = definitionXml
				.getTaskReminderList();
		if (BeanUtils.isNotEmpty(taskReminderList))
			this.importTaskReminder(taskReminderList, actDefId);

		// 流程联动设置
		List<GangedSet> bpmGangedSetList = definitionXml
				.getGangedSetList();
		if (BeanUtils.isNotEmpty(bpmGangedSetList))
			this.importGangedSet(bpmGangedSetList, defId);
//		//导入流程引用
//		List<BpmReferDefinition> bpmReferDefinitionList=bpmDefinitionXml.
//				getBpmReferDefinitionList();
//		if (BeanUtils.isNotEmpty(bpmReferDefinitionList)) {
//			this.importBpmReferDefinition(bpmReferDefinitionList);
//		}
	}

	/**
	 * 流程联动设置
	 * 
	 * @param bpmGangedSetList
	 * @param defId
	 */
	private void importGangedSet(List<GangedSet> bpmGangedSetList,
			Long defId) {
		for (GangedSet bpmGangedSet : bpmGangedSetList) {
			Long id = bpmGangedSet.getId();
			Object [] args= {id};
			GangedSet gangedSet = gangedSetDao.getById(id);
			if (BeanUtils.isEmpty(gangedSet)) {
				bpmGangedSet.setDefid(defId);
				gangedSetDao.add(bpmGangedSet);
				MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmGangedSet.isNew",args));
			} else {
				bpmGangedSet.setId(UniqueIdUtil.genId());
				bpmGangedSet.setDefid(defId);
				gangedSetDao.add(bpmGangedSet);
				MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmGangedSet.isExist",args));
			}
		}
	}
	
/*/**
	 * 流程引用的导入
	 * @param bpmReferDefinitionList
	 * @param actDefId
	 *//*
	private void importBpmReferDefinition(List<BpmReferDefinition> bpmReferDefinitionList) throws Exception {
		for (BpmReferDefinition bpmReferDefinition : bpmReferDefinitionList) {
			Long id=bpmReferDefinition.getDefId();
			BpmReferDefinition referDefinition=bpmReferDefinitionService.getById(id);
			if (BeanUtils.isEmpty(referDefinition)) {
				bpmReferDefinitionDao.add(bpmReferDefinition);
				MsgUtil.addMsg(MsgUtil.SUCCESS, "流程引用设置,ID:" + id + ",该记录成功导入！");
			}else {
				MsgUtil.addMsg(MsgUtil.WARN, "流程引用设置,ID:" + id
						+ "已经存在,该记录终止导入！");
			}
			
		}
	}
*/
	/**
	 * 表单权限
	 * 
	 * @param bpmFormRightsList
	 * @param mapFormKeyList
	 * @param actDefId
	 * @throws Exception
	 */
	private void importFormRights(List<? extends IFormRights> bpmFormRightsList,
			List<Map<Long, Long>> mapFormKeyList, String actDefId)
			throws Exception {
		formRightsService.delByActDefId(actDefId, true);
		for (IFormRights bpmFormRights : bpmFormRightsList) {
			Long formKey = getFormKey(mapFormKeyList,
					bpmFormRights.getFormDefId());
			if (BeanUtils.isEmpty(formKey))
				continue;
			this.importFormRights(bpmFormRights, formKey, actDefId);
		}

	}
	private Long getFormKey(List<Map<Long, Long>> mapFormKeyList, Long formKey) {
		Long deseFormKey = null;
		for (Map<Long, Long> map : mapFormKeyList) {
			deseFormKey = map.get(formKey);
			if (BeanUtils.isNotEmpty(deseFormKey))
				break;
		}
		return formKey;
	}
	
	/**
	 * 保存 表单权限
	 * 
	 * @param bpmFormRights
	 * @param formKey
	 * @param formDefId
	 * @param msg
	 * @return
	 */
	private void importFormRights(IFormRights bpmFormRights, Long formKey,
			String actDefId) throws Exception {
		IFormRights formRights = formRightsService.getById(bpmFormRights
				.getId());
		bpmFormRights.setFormDefId(formKey);
		bpmFormRights.setActDefId(actDefId);
		if(bpmFormRights.getType() != IFormRights.TableShowRights)
			bpmFormRights.setPermission(formDefService.parsePermission(
				bpmFormRights.getPermission(), false));
		Object [] args = {bpmFormRights.getId()};
		if (BeanUtils.isNotEmpty(formRights)) {
			formRightsService.update(bpmFormRights);
			MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmFormRights.isExist",args));
		} else {
			formRightsService.add(bpmFormRights);
			MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmFormRights.isNew",args));
		}
	}
	
	
	/**
	 * 导入流程定义
	 * 
	 * @param bpmDefinition
	 * 
	 * @return
	 */
	private Definition importDefinition(Definition bpmDefinition)
			throws Exception {
		// 处理xml
		bpmDefinition = parseDefinition(bpmDefinition, false);
		String actFlowDefXml = BpmUtil.transform(bpmDefinition.getDefKey(),
				bpmDefinition.getSubject(), bpmDefinition.getDefXml());
		this.saveDefinition(bpmDefinition, actFlowDefXml);
		return bpmDefinition;
	}
	
	/**
	 * 导入 节点，节点人员条件，节点人员，节点人员的上下级，常用语
	 * 
	 * @param bpmDefinitionXml
	 * @param defId
	 * @param actDefId
	 * @param mapFormKeyList
	 * @throws Exception
	 */
	private void importNodeSet(DefinitionXml bpmDefinitionXml,
			Long defId, String actDefId, List<Map<Long, Long>> mapFormIdList)
			throws Exception {
		// 流程节点设置
		List<NodeSet> bpmNodeSetList = bpmDefinitionXml.getNodeSetList();
		// 常用语设置
		/*List<TaskApprovalItems> taskApprovalItemsList = bpmDefinitionXml
				.getTaskApprovalItemsList();*/
		// 节点规则
		List<UserCondition> bpmUserConditionList = bpmDefinitionXml
				.getUserConditionList();

		this.parseBpmUserCondition(bpmUserConditionList, mapFormIdList);

		// 节点下的人员设置
		List<NodeUser> bpmNodeUserList = bpmDefinitionXml
				.getNodeUserList();

		// 用户节点的上下级设置
		List<NodeUserUplow> bpmNodeUserUplowList = bpmDefinitionXml
				.getNodeUserUplowList();

		/*// 常用语
		List<TaskApprovalItems> approvalItemsList = new ArrayList<TaskApprovalItems>();
		// 不在常用语
		List<TaskApprovalItems> notNodeApprovalItemsList = new ArrayList<TaskApprovalItems>();
		// 所有的常用语
		List<TaskApprovalItems> allApprovalItemsList = new ArrayList<TaskApprovalItems>();

		if (BeanUtils.isNotEmpty(taskApprovalItemsList)) {
			for (TaskApprovalItems taskApprovalItems : taskApprovalItemsList) {
				// 存在节点
				if (BeanUtils.isNotEmpty(taskApprovalItems.getSetId()))
					approvalItemsList.add(taskApprovalItems);
				// 不存在节点
				else
					notNodeApprovalItemsList.add(taskApprovalItems);
			}
		}*/

		// 流程人员条件设置
		List<UserCondition> userConditionList = new ArrayList<UserCondition>();
		// 抄送的人员条件
		List<UserCondition> copyToUserConditionList = new ArrayList<UserCondition>();

		// 其他类型人员条件
		List<UserCondition> otherUserConditionList = new ArrayList<UserCondition>();
		if (BeanUtils.isNotEmpty(bpmUserConditionList)) {
			for (UserCondition bpmUserCondition : bpmUserConditionList) {
				
				if (bpmUserCondition.getConditionType().shortValue() == UserCondition.CONDITION_TYPE_EXEUSER){
					userConditionList.add(bpmUserCondition);
				}else if (bpmUserCondition.getConditionType().shortValue() == UserCondition.CONDITION_TYPE_COPYUSER){
					copyToUserConditionList.add(bpmUserCondition);
				}else{ 
					otherUserConditionList.add(bpmUserCondition);
				}
					
			}
		}

		//TODO 这里导入人员有些问题，需要修改。
		// 节点人员
		List<NodeUser> nodeUserList = new ArrayList<NodeUser>();
		// 抄送的人员
		List<NodeUser> otherBpmNodeUserList = new ArrayList<NodeUser>();
		if (BeanUtils.isNotEmpty(bpmNodeUserList)) {
			for (NodeUser bpmNodeUser : bpmNodeUserList) {
				long  ConditionId=bpmNodeUser.getConditionId();
				for (UserCondition bpmUserCondition : bpmUserConditionList) {
					if (ConditionId==bpmUserCondition.getId()) {
						if (BeanUtils.isNotEmpty(bpmUserCondition.getSetId())
								&& bpmUserCondition.getSetId() > 0)
							nodeUserList.add(bpmNodeUser);
						else
							otherBpmNodeUserList.add(bpmNodeUser);
					}
				}
				

				
			}
		}

		// 导入节点、规则条件、人员
		for (NodeSet bpmNodeSet : bpmNodeSetList) {
			Long origSetId = bpmNodeSet.getSetId();
			// 流程节点设置
			Long destSetId = this.importNodeSet(bpmNodeSet, defId, actDefId,
					origSetId);
			// 规则条件
			this.importUserConditionInfo(userConditionList, nodeUserList,
					bpmNodeUserUplowList, actDefId,
					origSetId, destSetId);
			
			

			// 更新 常用语设置
			/*for (TaskApprovalItems taskApprovalItems : approvalItemsList) {
				if (origSetId.longValue() == taskApprovalItems.getSetId()
						.longValue()) {
					taskApprovalItems.setSetId(destSetId);
					allApprovalItemsList.add(taskApprovalItems);
				}
			}*/
		}
		// 导入常用语设置
		/*if (BeanUtils.isNotEmpty(notNodeApprovalItemsList))
			allApprovalItemsList.addAll(notNodeApprovalItemsList);
		for (TaskApprovalItems taskApprovalItems : allApprovalItemsList) {
			this.importTaskApprovalItems(taskApprovalItems, actDefId);
		}*/
		
		// 抄送
		if (BeanUtils.isNotEmpty(copyToUserConditionList)) {
			this.importUserConditionBpmNodeUser(copyToUserConditionList, actDefId, otherBpmNodeUserList, bpmNodeUserUplowList, 0L);
		}
		
		// 其他类型人员设置导入
		if (BeanUtils.isNotEmpty(otherUserConditionList)) {
			this.importUserConditionBpmNodeUser(otherUserConditionList, actDefId, otherBpmNodeUserList, bpmNodeUserUplowList, 0L);
		}

	}
	
	/**
	 * 人员设置导入
	 * @param bpmUserConditionList
	 * @param actDefId
	 * @param bpmNodeUserList
	 * @param bpmNodeUserUplowList
	 * @param bpmComsiteUserList
	 * @param destSetId
	 * @return
	 * @throws Exception
	 */
	private Map<Long, Long> importUserConditionBpmNodeUser(
			List<UserCondition> bpmUserConditionList, String actDefId,
			List<NodeUser> bpmNodeUserList,
			List<NodeUserUplow> bpmNodeUserUplowList,Long destSetId) throws Exception {
		Map<Long, Long> conditionMap = new HashMap<Long, Long>();
		if(BeanUtils.isEmpty(bpmUserConditionList))
			return conditionMap;
			
		for (UserCondition bpmUserCondition : bpmUserConditionList) {
			Long origConditionId = bpmUserCondition.getId();
			Long conditionId = this.importUserCondition(bpmUserCondition, actDefId, destSetId);
			this.importNodeUserInfo(bpmNodeUserList,
					bpmNodeUserUplowList,
					origConditionId, conditionId, actDefId, destSetId);
			conditionMap.put(origConditionId, conditionId);
		}
		return conditionMap;
	}
	
	
	/**
	 * 处理bpmUserCondition
	 * 
	 * @param bpmUserConditionList
	 * @param mapFormKeyList
	 */
	private void parseBpmUserCondition(
			List<UserCondition> bpmUserConditionList,
			List<Map<Long, Long>> mapFormIdList) {
		if(BeanUtils.isEmpty(bpmUserConditionList))
			return;
		for (UserCondition bpmUserCondition : bpmUserConditionList) {
			if (StringUtil.isEmpty(bpmUserCondition.getFormIdentity()))
				continue;
//			String formIdentity = getFormKey(mapFormIdList,
//					bpmUserCondition.getFormIdentity());
//			if (BeanUtils.isNotEmpty(formId))
//				bpmUserCondition.setTableId(formId);

		}

	}
	
	/**
	 * 导入人员规则条件设置
	 * 
	 * @param userConditionList
	 * @param nodeUserList
	 * @param bpmNodeUserUplowList
	 * @param bpmComsiteUserList
	 * @param actDefId
	 * @param origSetId
	 * @param destSetId
	 * @throws Exception
	 */
	private void importUserConditionInfo(
			List<UserCondition> userConditionList,
			List<NodeUser> nodeUserList,
			List<NodeUserUplow> bpmNodeUserUplowList,String actDefId,
			Long origSetId, Long destSetId) throws Exception {
		// 人员规则条件
		for (UserCondition bpmUserCondition : userConditionList) {
			if (BeanUtils.isEmpty(bpmUserCondition.getSetId()))
				continue;
			if (origSetId.longValue() == bpmUserCondition.getSetId()
					.longValue()) {

				Long origConditionId = bpmUserCondition.getId();
				Long conditionId = this.importUserCondition(
						bpmUserCondition, actDefId, destSetId);

				this.importNodeUserInfo(nodeUserList, bpmNodeUserUplowList, origConditionId, conditionId,
						actDefId, destSetId);
			}
		}
	}
	
	/**
	 * 导入人员设置
	 * 
	 * @param nodeUserList
	 * @param bpmNodeUserUplowList
	 * @param bpmComsiteUserList
	 * @param origConditionId
	 * @param userConditionId
	 * @param actDefId
	 * @param destSetId
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void importNodeUserInfo(List<NodeUser> nodeUserList,
			List<NodeUserUplow> bpmNodeUserUplowList, Long origConditionId,
			Long conditionId, String actDefId, Long destSetId) throws Exception {
		for (NodeUser bpmNodeUser : nodeUserList) {
			if (BeanUtils.isEmpty(bpmNodeUser.getConditionId()))
				continue;
			if (origConditionId.longValue() == bpmNodeUser.getConditionId()
					.longValue()) {
				Long origNodeUserId = bpmNodeUser.getNodeUserId();
				// 节点下的人员设置
				Long nodeUserId = this.importNodeUser(bpmNodeUser, actDefId,
						destSetId, conditionId);
				// if (BeanUtils.isEmpty(bpmNodeUserUplowList))
				// 用户节点的上下级设置
				// for (BpmNodeUserUplow bpmNodeUserUplow :
				// bpmNodeUserUplowList) {
				// if (origNodeUserId.longValue() == bpmNodeUserUplow
				// .getNodeUserId().longValue())
				// // 用户节点的上下级设置
				// this.importBpmNodeUserUplow(bpmNodeUserUplow,
				// nodeUserId);
				//
				// }
//				if (BeanUtils.isNotEmpty(bpmComsiteUserList)) {
//					for (BpmComsiteUser bpmComsiteUser : bpmComsiteUserList) {
//						if (origNodeUserId.longValue() == bpmComsiteUser
//								.getNodeuserid().longValue())
//							// 组合条件
//							this.importBpmComsiteUser(bpmComsiteUser,
//									nodeUserId);
//
//					}
//				}
			}
		}
	}

	
	/**
	 * 流程规则
	 * 
	 * @param bpmUserCondition
	 * @param actDefId
	 * @param setId
	 */
	private Long importUserCondition(UserCondition bpmUserCondition,
			String actDefId, Long setId) {
		bpmUserCondition.setActdefid(actDefId);
		bpmUserCondition.setSetId(setId);

		Long id = bpmUserCondition.getId();
		UserCondition userCondition = userConditionDao.getById(id);
		Object[] args={id};
		if (BeanUtils.isEmpty(userCondition)) {
			userConditionDao.add(bpmUserCondition);
			MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmUserCondition.isNew",args));
		} else {
			id = UniqueIdUtil.genId();
			bpmUserCondition.setId(id);
			userConditionDao.add(bpmUserCondition);
			MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmUserCondition.isExist",args));
		}
		return id;

	}
	
	/**
	 * 常用语设置
	 * 
	 * @param taskApprovalItems
	 * @param actDefId
	 */
	@SuppressWarnings("unused")
	private void importTaskApprovalItems(TaskApprovalItems taskApprovalItems,
			String actDefId) throws Exception {
		/*Long id = taskApprovalItems.getItemId();
		TaskApprovalItems approvalItems = taskApprovalItemsDao.getById(id);
		Object[] args={id};
		if (BeanUtils.isEmpty(approvalItems)) {
			taskApprovalItems.setActDefId(actDefId);
			taskApprovalItemsDao.add(taskApprovalItems);
			MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.tskApprovalItems.isNew",args));
		} else {
			taskApprovalItems.setItemId(UniqueIdUtil.genId());
			taskApprovalItems.setActDefId(actDefId);
			taskApprovalItemsDao.add(taskApprovalItems);
			MsgUtil.addMsg(MsgUtil.WARN,  getText("service.bpmDefinition.import.tskApprovalItems.isExist",args));
		}*/
	}

	/**
	 * 保存及更新流程定义
	 * 
	 * @param bpmDefinition
	 *            流程定义实体
	 * @param actFlowDefXml
	 *            流程定义bpmn文档
	 * @throws Exception
	 */
	private void saveDefinition(Definition bpmDefinition,
			String actFlowDefXml) throws Exception {
		Long id = bpmDefinition.getDefId();
		Definition definition = getById(id);
		long newDefId=UniqueIdUtil.genId();
		// 新增加的流程
		Deployment deployment = bpmService.deploy(
				bpmDefinition.getSubject(), actFlowDefXml);
		ProcessDefinitionEntity ent = bpmService
				.getProcessDefinitionByDeployId(deployment.getId());
		// 设置分类
		bpmDefinition = this.setTypeId(bpmDefinition);
		if (BeanUtils.isEmpty(definition)) {
			bpmDefinition.setDefId(newDefId);
			bpmDefinition.setActDeployId(new Long(deployment.getId()));
			bpmDefinition.setActDefId(ent.getId());
			bpmDefinition.setActDefKey(ent.getKey());
			//bpmDefinition.setStatus(Definition.STATUS_ENABLED);
			bpmDefinition.setUpdatetime(new Date());
			bpmDefinition.setIsMain(Definition.MAIN);
			bpmDefinition.setVersionNo(1);
			add(bpmDefinition);
			MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.saveDefinition.isNew",new Object[]{bpmDefinition.getSubject()}));
		} else {
			dao.updateSubVersions(newDefId,bpmDefinition.getDefKey());
			bpmDefinition.setDefId(newDefId);
			bpmDefinition.setActDeployId(new Long(deployment.getId()));
			bpmDefinition.setActDefId(ent.getId());
			bpmDefinition.setActDefKey(ent.getKey());
			//bpmDefinition.setStatus(Definition.STATUS_ENABLED);
			bpmDefinition.setUpdatetime(new Date());
			bpmDefinition.setIsMain(Definition.MAIN);
			bpmDefinition.setVersionNo(ent.getVersion());
			add(bpmDefinition);
			MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.saveDefinition.isExist",new Object[]{bpmDefinition.getSubject()}));

		}

	}


	/**
	 * 设置分类
	 * 
	 * @param bpmFormDef
	 * @return
	 */
	private Definition setTypeId(Definition bpmDefinition)
			throws Exception {
		if (BeanUtils.isEmpty(bpmDefinition.getTypeId()))
			return bpmDefinition;
		IGlobalType globalType = globalTypeService
				.getById(bpmDefinition.getTypeId());
		if (BeanUtils.isEmpty(globalType))
			bpmDefinition.setTypeId(null);
		return bpmDefinition;
	}

	/**
	 * 任务节点催办时间设置
	 * 
	 * @param taskReminderList
	 * @param actDefId
	 */
	private void importTaskReminder(List<TaskReminder> taskReminderList,
			String actDefId) throws Exception {
		for (TaskReminder taskReminder : taskReminderList) {
			Long id = taskReminder.getTaskDueId();
			TaskReminder reminder = taskReminderDao.getById(id);
			Object[] args={id};
			if (BeanUtils.isEmpty(reminder)) {
				taskReminder.setActDefId(actDefId);
				taskReminderDao.add(taskReminder);
				MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.taskReminder.isNew",args));
			} else {
				taskReminder.setTaskDueId(UniqueIdUtil.genId());
				taskReminder.setActDefId(actDefId);
				taskReminderDao.add(taskReminder);
				MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.taskReminder.isExist",args));
			}
		}
	}

	/**
	 * 流程操作按钮设置
	 * 
	 * @param bpmNodeButtonList
	 * @param defId
	 * @param actDefId
	 */
	private void importNodeButton(List<NodeButton> bpmNodeButtonList,
			Long defId, String actDefId) throws Exception {
		for (NodeButton bpmNodeButton : bpmNodeButtonList) {
			Long id = bpmNodeButton.getId();
			NodeButton nodeButton = nodeButtonDao.getById(id);
			Object[] args={id};
			if (BeanUtils.isEmpty(nodeButton)) {
				bpmNodeButton.setDefId(defId);
				bpmNodeButton.setActdefid(actDefId);
				nodeButtonDao.add(bpmNodeButton);
				MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmNodeButton.isNew",args));
			} else {
				bpmNodeButton.setId(UniqueIdUtil.genId());
				bpmNodeButton.setDefId(defId);
				bpmNodeButton.setActdefid(actDefId);
				nodeButtonDao.add(bpmNodeButton);
				MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmNodeButton.isExist",args));
			}
		}
	}

	/**
	 * 流程节点设置
	 * 
	 * @param bpmNodeSetList
	 * @param bpmNodeUserList
	 * @param bpmNodeUserUplowList
	 * @param taskApprovalItemsList
	 * @param defId
	 * @param actDefId
	 * @param formKeyMap
	 */
	@SuppressWarnings("unused")
	private void importNodeSet(List<NodeSet> bpmNodeSetList,
			List<NodeUser> bpmNodeUserList,
			List<TaskApprovalItems> taskApprovalItemsList, Long defId,
			String actDefId) throws Exception {

		/*List<TaskApprovalItems> approvalItemsList = new ArrayList<TaskApprovalItems>();
		if (BeanUtils.isNotEmpty(taskApprovalItemsList)) {
			for (TaskApprovalItems taskApprovalItems : taskApprovalItemsList) {
				if (BeanUtils.isNotEmpty(taskApprovalItems.getSetId()))
					approvalItemsList.add(taskApprovalItems);
				else
					this.importTaskApprovalItems(taskApprovalItems, actDefId,
							null);
			}
		}*/
		//TODO 这里导入人员有些问题。
		for (NodeSet bpmNodeSet : bpmNodeSetList) {
			Long setId = bpmNodeSet.getSetId();
			// 流程节点设置
			Long nodeSetId = this.importNodeSet(bpmNodeSet, defId, actDefId,
					setId);

			for (NodeUser bpmNodeUser : bpmNodeUserList) {
				
					Long userId = bpmNodeUser.getNodeUserId();
					// 节点下的人员设置
					Long nodeUserId = this.importNodeUser(bpmNodeUser);
//					if(bpmNodeUserUplowList!=null){
//						for (BpmNodeUserUplow bpmNodeUserUplow : bpmNodeUserUplowList) {
//							if (userId.longValue() == bpmNodeUserUplow
//									.getNodeUserId().longValue()) {
//								// 用户节点的上下级设置
//								this.importBpmNodeUserUplow(bpmNodeUserUplow,
//										nodeUserId);
//							}
//						}
//					}
				
			}
			/*// 常用语设置
			for (TaskApprovalItems taskApprovalItems : approvalItemsList) {
				if (setId.longValue() == taskApprovalItems.getSetId()
						.longValue()) {
					this.importTaskApprovalItems(taskApprovalItems, actDefId,
							nodeSetId);
				}
			}*/
		}
	}

	/**
	 * 常用语设置
	 * 
	 * @param taskApprovalItems
	 * @param actDefId
	 * @param setId
	 */
	@SuppressWarnings("unused")
	private void importTaskApprovalItems(TaskApprovalItems taskApprovalItems,
			String actDefId, Long setId) throws Exception {
		/*Long id = taskApprovalItems.getItemId();
		TaskApprovalItems approvalItems = taskApprovalItemsDao.getById(id);
		Object[] args={id};
		if (BeanUtils.isEmpty(approvalItems)) {
			taskApprovalItems.setSetId(setId);
			taskApprovalItems.setActDefId(actDefId);
			taskApprovalItemsDao.add(taskApprovalItems);
			MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.tskApprovalItems.isNew",args));
		} else {
			taskApprovalItems.setItemId(UniqueIdUtil.genId());
			taskApprovalItems.setActDefId(actDefId);
			taskApprovalItemsDao.add(taskApprovalItems);
			MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.tskApprovalItems.isExist",args));
		}*/
	}

	/**
	 * 流程节点设置
	 * 
	 * @param bpmNodeSet
	 * @param defId
	 * @param actDefId
	 * @param setId
	 * @return
	 */
	private Long importNodeSet(NodeSet bpmNodeSet, Long defId,
			String actDefId, Long setId) throws Exception {

		Long id = bpmNodeSet.getSetId();
		NodeSet nodeSet = nodeSetDao.getById(id);
		Object[] args={id};
		if (BeanUtils.isEmpty(nodeSet)) {
			bpmNodeSet.setDefId(defId);
			bpmNodeSet.setActDefId(actDefId);
			nodeSetDao.add(bpmNodeSet);
			MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmNodeSet.isNew",args));
		} else {
			id = UniqueIdUtil.genId();
			bpmNodeSet.setSetId(id);
			bpmNodeSet.setActDefId(actDefId);
			bpmNodeSet.setDefId(defId);
			nodeSetDao.add(bpmNodeSet);
			MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmNodeSet.isExist",args));
		}
		return id;
	}

	/**
	 * 流程节点设置
	 * 
	 * @param bpmUserConditionList
	 * @param defId
	 * @param actDefId
	 * @return
	 */
	@SuppressWarnings("unused")
	private void importUserCondition(
			List<UserCondition> bpmUserConditionList, Long defId,
			String actDefId) throws Exception {
		for (UserCondition bpmUserCondition : bpmUserConditionList) {
			Long id = bpmUserCondition.getId();
			UserCondition userCondition = userConditionDao.getById(id);
			Object[] args={id};
			if (BeanUtils.isEmpty(userCondition)) {
				bpmUserCondition.setActdefid(actDefId);
				userConditionDao.add(bpmUserCondition);
				MsgUtil.addMsg(MsgUtil.SUCCESS,  getText("service.bpmDefinition.import.bpmNodeSet.isNew",args));
			} else {
				id = UniqueIdUtil.genId();
				bpmUserCondition.setId(id);
				userConditionDao.add(bpmUserCondition);
				MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmNodeSet.isExist",args));
			}
		}

	}

	/**
	 * 节点下的人员设置
	 * 
	 * @param bpmNodeUser
	 * @param actDefId
	 * @param setId
	 * @return
	 */
	private Long importNodeUser(NodeUser bpmNodeUser) throws Exception {
		Long id = bpmNodeUser.getNodeUserId();
		NodeUser nodeUser = nodeUserDao.getById(id);
		Object[] args={id};
		if (BeanUtils.isEmpty(nodeUser)) {
			
			nodeUserDao.add(bpmNodeUser);
			MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmNodeUser.isNew",args));
		} else {
			id = UniqueIdUtil.genId();
			bpmNodeUser.setNodeUserId(id);
			nodeUserDao.add(bpmNodeUser);
			MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmNodeUser.isExist",args));
		}
		return id;
	}
	
	/**
	 * 人员设置
	 * 
	 * @param bpmNodeUser
	 * @param actDefId
	 * @param setId
	 * @param conditionId
	 * @return
	 */
	private Long importNodeUser(NodeUser bpmNodeUser, String actDefId,
			Long setId, Long conditionId) {
		bpmNodeUser = this.parseBpmNodeUser(bpmNodeUser);
		bpmNodeUser.setConditionId(conditionId);
		Long id = bpmNodeUser.getNodeUserId();
		Object[] args={id};
		NodeUser nodeUser = nodeUserDao.getById(id);
		if (BeanUtils.isEmpty(nodeUser)) {
			nodeUserDao.add(bpmNodeUser);
			MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmNodeUser1.isNew",args));
		} else {
			id = UniqueIdUtil.genId();
			bpmNodeUser.setNodeUserId(id);
			nodeUserDao.add(bpmNodeUser);
			MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmNodeUser1.isExist",args));
		}
		return id;
	}
	
	/**
	 * 
	 * @param bpmNodeUser
	 * @return
	 */
	private NodeUser parseBpmNodeUser(NodeUser bpmNodeUser) {
		String cmpName = bpmNodeUser.getCmpNames();
		if (StringUtils.isEmpty(cmpName))
			return bpmNodeUser;
		String[] cmpNamesAry = cmpName.split(",");
		String msg = getText("service.bpmDefinition.parseBpmNodeUser.message");
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(0, bpmNodeUser.getCmpIds());
		map.put(1, bpmNodeUser.getCmpNames());
		// 0,9,10,11,13不需要检查设置
		String assignType=bpmNodeUser.getAssignType();
		if ( NodeUser.ASSIGN_TYPE_USER.equals(assignType)) {// 用户1
			map = this.parseSysUser(cmpNamesAry, msg);
		} else if ( NodeUser.ASSIGN_TYPE_ROLE.equals(assignType)) {// 角色2
			map = this.parseSysRole(cmpNamesAry, msg);
		} else if (NodeUser.ASSIGN_TYPE_ORG.equals(assignType)  ) {// 组织3
			map = this.parseSysOrg(cmpNamesAry, msg);
		} else if ( NodeUser.ASSIGN_TYPE_ORG_CHARGE.equals(assignType) ) {// 组织负责人4
			map = this.parseSysOrg(cmpNamesAry, msg);
		} else if (NodeUser.ASSIGN_TYPE_POS.equals(assignType) ) {// 岗位5
			map = this.parsePosition(cmpNamesAry, msg);
		} 
		bpmNodeUser.setCmpIds(map.get(0));
		bpmNodeUser.setCmpNames(map.get(1));
		return bpmNodeUser;
	}

//	/**
//	 * 用户节点的上下级设置
//	 * 
//	 * @param bpmNodeUserUplow
//	 * @param nodeUserId
//	 */
//	private void importBpmNodeUserUplow(BpmNodeUserUplow bpmNodeUserUplow,
//			Long nodeUserId) throws Exception {
//		Long id = bpmNodeUserUplow.getID();
//		BpmNodeUserUplow nodeUserUplow = bpmNodeUserUplowDao.getById(id);
//		if (BeanUtils.isEmpty(nodeUserUplow)) {
//			bpmNodeUserUplow.setNodeUserId(nodeUserId);
//			bpmNodeUserUplowDao.add(bpmNodeUserUplow);
//			MsgUtil.addMsg(MsgUtil.SUCCESS, "用户节点的上下级设置,ID:" + id + ",该记录成功导入！");
//		} else {
//			MsgUtil.addMsg(MsgUtil.WARN, "用户节点的上下级设置,ID:" + id
//					+ "已经存在,该记录终止导入！");
//		}
//	}

	/**
	 * 流程消息设置
	 * 
	 * @param bpmNodeMessageList
	 * @param actDefId
	 */
	private void importNodeMessage(List<NodeMessage> bpmNodeMessageList,
			String actDefId) throws Exception {
		for (NodeMessage bpmNodeMessage : bpmNodeMessageList) {
			Long id = bpmNodeMessage.getId();
			NodeMessage nodeMessage = nodeMessageDao.getById(id);
			Object[] args={id};
			if (BeanUtils.isEmpty(nodeMessage)) {
				bpmNodeMessage.setActDefId(actDefId);
				nodeMessageDao.add(bpmNodeMessage);
				MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmNodeMessage.isNew",args));
			} else {
				bpmNodeMessage.setId(UniqueIdUtil.genId());
				bpmNodeMessage.setActDefId(actDefId);
				nodeMessageDao.add(bpmNodeMessage);
				MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmNodeMessage.isExist",args));
			}
		}

	}

	/**
	 * 流程会签规则
	 * 
	 * @param bpmNodeSignList
	 * @param actDefId
	 */
	private void importNodeSign(List<NodeSign> bpmNodeSignList,
			String actDefId) throws Exception {
		for (NodeSign bpmNodeSign : bpmNodeSignList) {
			Long id = bpmNodeSign.getSignId();
			NodeSign nodeSign = nodeSignDao.getById(id);
			Object[]args={id};
			if (BeanUtils.isEmpty(nodeSign)) {
				bpmNodeSign.setActDefId(actDefId);
				nodeSignDao.add(bpmNodeSign);
				MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmNodeSign.isNew",args));
			} else {
				bpmNodeSign.setSignId(UniqueIdUtil.genId());
				bpmNodeSign.setActDefId(actDefId);
				nodeSignDao.add(bpmNodeSign);
				MsgUtil.addMsg(MsgUtil.WARN,getText("service.bpmDefinition.import.bpmNodeSign.isExist",args));
			}
		}

	}

	/**
	 * 流程变量
	 * 
	 * @param bpmDefVarList
	 * @param defId
	 */
	private void importDefVar(List<DefVar> bpmDefVarList, Long defId)
			throws Exception {
		for (DefVar bpmDefVar : bpmDefVarList) {
			Long id = bpmDefVar.getVarId();
			DefVar defVar = defVarDao.getById(id);
			Object[] args={0};
			if (BeanUtils.isEmpty(defVar)) {
				bpmDefVar.setDefId(defId);
				defVarDao.add(bpmDefVar);
				MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmDefVar.isNew",args));
			} else {
				bpmDefVar.setVarId(UniqueIdUtil.genId());
				bpmDefVar.setDefId(defId);
				defVarDao.add(bpmDefVar);
				MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmDefVar.isExist",args));
			}
		}

	}

	/**
	 * 流程事件脚本
	 * 
	 * @param bpmNodeScriptList
	 * @param actDefId
	 */
	private void importNodeScript(List<NodeScript> bpmNodeScriptList,
			String actDefId) throws Exception {
		for (NodeScript bpmNodeScript : bpmNodeScriptList) {
			Long id = bpmNodeScript.getId();
			NodeScript nodeScript = nodeScriptDao.getById(id);
			Object[] args={id};
			if (BeanUtils.isEmpty(nodeScript)) {
				bpmNodeScript.setActDefId(actDefId);
				nodeScriptDao.add(bpmNodeScript);
				MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmNodeScript.isNew",args));
			} else {
				bpmNodeScript.setId(UniqueIdUtil.genId());
				bpmNodeScript.setActDefId(actDefId);
				nodeScriptDao.add(bpmNodeScript);
				MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmNodeScript.isExist",args));
			}
		}

	}

	/**
	 * 流程跳转规则
	 * 
	 * @param bpmNodeRuleList
	 * @param actDefId
	 */
	private void importNodeRule(List<NodeRule> bpmNodeRuleList,
			String actDefId) throws Exception {
		for (NodeRule bpmNodeRule : bpmNodeRuleList) {
			Long id = bpmNodeRule.getRuleId();
			NodeRule nodeRule = nodeRuleDao.getById(id);
			Object[] args={id};
			if (BeanUtils.isEmpty(nodeRule)) {
				bpmNodeRule.setActDefId(actDefId);
				nodeRuleDao.add(bpmNodeRule);
				MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmNodeRule.isNew",args));
			} else {
				bpmNodeRule.setRuleId(UniqueIdUtil.genId());
				bpmNodeRule.setActDefId(actDefId);
				nodeRuleDao.add(bpmNodeRule);
				MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmNodeRule.isExist",args));
			}
		}
	}

	/**
	 * 流程定义权限
	 * 
	 * @param bpmDefRightsList
	 * @param defId
	 */
	private void importDefRights(List<DefRights> bpmDefRightsList,
			String defKey) throws Exception {
		for (DefRights bpmDefRights : bpmDefRightsList) {
			bpmDefRights = this.parseDefRights(bpmDefRights);
			if (BeanUtils.isEmpty(bpmDefRights))
				continue;

			Long id = bpmDefRights.getId();
			DefRights defRights = defRightsDao.getById(id);
			Object[] args={id};
			if (BeanUtils.isEmpty(defRights)) {
				defRightsDao.add(bpmDefRights);
				MsgUtil.addMsg(MsgUtil.SUCCESS, getText("service.bpmDefinition.import.bpmDefRights.isNew",args));
			} else {
				BeanUtils.copyNotNullProperties(defRights, bpmDefRights);
				defRightsDao.update(defRights);
				MsgUtil.addMsg(MsgUtil.WARN, getText("service.bpmDefinition.import.bpmDefRights.isExist",args));
			}
		}
	}
	
	/**
	 * 处理权限
	 * 
	 * @param defRights
	 * @return
	 */
	private DefRights parseDefRights(DefRights defRights) {
		if (StringUtil.isEmpty(defRights.getOwnerName()))
			return null;
		String ownerName = defRights.getOwnerName();
		Object[] args={ownerName};
		if (defRights.getRightType().shortValue() == DefRights.RIGHT_TYPE_USER) {// 用户
			ISysUser sysUser = (ISysUser)sysUserService.getByUsername(ownerName);
			if (BeanUtils.isEmpty(sysUser)) {
				MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmDefinition.parseDefRights.account.notExist",args));
			} else {
				defRights.setOwnerId(sysUser.getUserId());
				defRights.setOwnerName(sysUser.getFullname());
			}
		} else if (defRights.getRightType().shortValue() == DefRights.RIGHT_TYPE_ROLE) {// 角色
			ISysRole sysRole = sysRoleService.getByRoleName(ownerName).get(0);//争议
			if (BeanUtils.isEmpty(sysRole)) {
				MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmDefinition.parseDefRights.roleName.notExist",args));
			} else { 
				defRights.setOwnerId(sysRole.getRoleId());
				defRights.setOwnerName(sysRole.getRoleName());
			}
		} else if (defRights.getRightType().shortValue() == DefRights.RIGHT_TYPE_ORG) {// 组织
			List<?extends ISysOrg> sysOrgList = sysOrgService.getByOrgName(ownerName);
			if (BeanUtils.isEmpty(sysOrgList)) {
				MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmDefinition.parseDefRights.orgName.notExist",args));
			} else if (sysOrgList.size() > 1) {
				MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmDefinition.parseDefRights.orgName.moreOne",args));
			} else {
				ISysOrg sysOrg = (ISysOrg)sysOrgList.get(0);
				defRights.setOwnerId(sysOrg.getOrgId());
				defRights.setOwnerName(sysOrg.getOrgName());
			}
		} 
		return defRights;
	}
	
	/**
	 * 解析岗位
	 * 
	 * @param cmpNamesAry
	 *            名称数组
	 * @param msg
	 *            提示消息
	 * @return
	 */
	private Map<Integer, String> parsePosition(String[] cmpNamesAry, String msg) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		String cmpIds = "";
		String cmpNames = "";
		for (String name : cmpNamesAry) {
			List<?extends IPosition> positionList = positionService.getByPosName(name);
			Object[] args={msg,name};
			if (BeanUtils.isEmpty(positionList)) {
				MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmDefinition.parsePosition.posName.notExist",args));

			} else if (positionList.size() > 1) {
				MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmDefinition.parsePosition.posName.moreOne",args));
			} else {
				IPosition position = positionList.get(0);
				cmpIds += position.getPosId() + ",";
				cmpNames += position.getPosName() + ",";
			}
		}
		return this.trimInfo(map, cmpIds, cmpNames);
	}

	/**
	 * 解析组织
	 * 
	 * @param cmpNamesAry
	 *            名称数组
	 * @param msg
	 *            提示消息
	 * @return
	 */
	private Map<Integer, String> parseSysOrg(String[] cmpNamesAry, String msg) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		String cmpIds = "";
		String cmpNames = "";
		for (String name : cmpNamesAry) {
			List<?extends ISysOrg> sysOrgList = sysOrgService.getByOrgName(name);
			Object[] args={msg,name};
			if (BeanUtils.isEmpty(sysOrgList)) {
				MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmDefinition.parseSysOrg.orgName.notExist",args));
			} else if (sysOrgList.size() > 1) {
				MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmDefinition.parseSysOrg.orgName.moreOne",args));
			} else {
				ISysOrg sysOrg = (ISysOrg)sysOrgList.get(0);
				cmpIds += sysOrg.getOrgId() + ",";
				cmpNames += sysOrg.getOrgName() + ",";
			}
		}
		return this.trimInfo(map, cmpIds, cmpNames);
	}

	
	/**
	 * 解析角色
	 * 
	 * @param cmpNamesAry
	 *            名称数组
	 * @param msg
	 *            提示消息
	 * @return
	 */
	private Map<Integer, String> parseSysRole(String[] cmpNamesAry, String msg) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		String cmpIds = "";
		String cmpNames = "";
		for (String name : cmpNamesAry) {
			ISysRole sysRole =sysRoleService.getByRoleName(name).get(0);
			Object[] args={msg,name};
			if (BeanUtils.isEmpty(sysRole)) {
				MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmDefinition.parseSysRole.roleName.notExist",args));
			} else { 
				cmpIds += sysRole.getRoleId() + ",";
				cmpNames += sysRole.getRoleName() + ",";
			}
		}

		return this.trimInfo(map, cmpIds, cmpNames);
	}

	/**
	 * 解析用户
	 * 
	 * @param cmpNamesAry
	 *            名称数组
	 * @param msg
	 *            提示消息
	 * @return
	 */
	private Map<Integer, String> parseSysUser(String[] cmpNamesAry, String msg) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		String cmpIds = "";
		String cmpNames = "";
		for (String name : cmpNamesAry) {
			ISysUser sysUser = (ISysUser)sysUserService.getByUsername(name);
			Object [] args={msg,name};
			if (BeanUtils.isEmpty(sysUser)) {
				MsgUtil.addMsg(MsgUtil.ERROR, getText("service.bpmDefinition.parseSysUser.account.notExist",args));
				continue;
			} else {
				cmpIds += sysUser.getUserId() + ",";
				cmpNames += sysUser.getFullname() + ",";
			}
		}
		return this.trimInfo(map, cmpIds, cmpNames);
	}
	/**
	 * 
	 * @param map
	 * @param cmpIds
	 * @param cmpNames
	 * @return
	 */
	private Map<Integer, String> trimInfo(Map<Integer, String> map,
			String cmpIds, String cmpNames) {
		if (cmpIds.length() > 0)
			cmpIds = cmpIds.substring(0, cmpIds.length() - 1);
		if (cmpNames.length() > 0)
			cmpNames = cmpNames.substring(0, cmpNames.length() - 1);

		map.put(0, cmpIds);
		map.put(1, cmpNames);
		return map;
	}

	//TODO 导出exportXML
	/**
	 * 导出XML。
	 * 
	 * <pre>
	 * 导出以下 信息:
	 * 	
	 * ■ 流程定义 bpmDefinition
	 * ■ 历史版本 bpmDefinitionHistory
	 * 	
	 * ■ 流程节点设置 bpmNodeSet
	 * ■ 节点下的人员的配置规则 bpmUserCondition
	 * ■ 节点下的人员设置  bpmNodeUser
	 * ■ 节点下的人员上下级设置 bpmNodeUserUplow
	 * 	
	 * ■ 流程定义权限 bpmDefRights
	 * ■ 常用语设置 taskApprovalItems
	 * 	
	 * ■ 流程跳转规则  bpmNodeRule
	 * ■ 流程事件脚本  bpmNodeScript
	 * 	
	 * ■ 流程操作按钮设置 bpmNodeButton
	 * ■ 流程变量  bpmDefVar
	 * 	 
	 * ■ 流程消息  bpmNodeMessage
	 * ■ 流程会签规则  bpmNodeSign
	 * 
	 * ■ 任务节点催办时间设置 taskReminder
	 * ■ 内（外）部子流程 subDefinition
	 * </pre>
	 * 
	 * @param Long
	 *            [] bpmDefIds
	 * @param map
	 * @param filePath
	 * @return
	 */
	public String exportXml(Long[] bpmDefIds, Map<String, Boolean> map,
			String filePath) throws Exception {
		DefinitionXmlList bpmDefinitionXmlList = new DefinitionXmlList();
		List<DefinitionXml> list = new ArrayList<DefinitionXml>();
		for (int i = 0; i < bpmDefIds.length; i++) {
			// 流程定义
			Definition definition = dao.getById(bpmDefIds[i]);
			DefinitionXml bpmDefinitionXml = this.exportDefinition(
					definition, Definition.MAIN, map, filePath);

			list.add(bpmDefinitionXml);
		}
		bpmDefinitionXmlList.setDefinitionXmlList(list);
		ArrayList<Class<?>> class_ =formDefService.getAllClass();
		class_.add(DefinitionXmlList.class);
        Class<?>[] _class=new Class[class_.size()];
        class_.toArray(_class);
        JAXBContext context = JAXBContext.newInstance(_class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, System.getProperty("file.encoding"));
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        m.marshal(bpmDefinitionXmlList, outputStream);
        return outputStream.toString();
        
        
		//return XmlBeanUtil.marshall(bpmDefinitionXmlList, DefinitionXmlList.class);
	}
	
	

	/**
	 * 导出流程定义
	 * 
	 * @param definition
	 * @param isMain
	 * @param filePath
	 * @return
	 */
	private DefinitionXml exportDefinition(Definition definition,
			Short isMain, Map<String, Boolean> map, String filePath)
			throws Exception {
		DefinitionXml definitionXml = new DefinitionXml();
		if (BeanUtils.isEmpty(definition))
			return definitionXml;
		if (BeanUtils.isEmpty(map))
			return definitionXml;
		definition = parseDefinition(definition, true);
		Long defId = definition.getDefId();
		String actDefId = definition.getActDefId();
		Long actDeployId = definition.getActDeployId();
		String defKey = definition.getDefKey();
		
		//把发布状态改成测试状态输出
		if(Definition.STATUS_ENABLED.shortValue()==definition.getStatus()){
			definition.setStatus(Definition.STATUS_TEST);
		}
		// 流程定义
		definitionXml.setDefinition(definition);

		// 设置流程定义 历史版本
		if (map.get("bpmDefinitionHistory")
				&& isMain.shortValue() == Definition.MAIN.shortValue())
			this.exportDefinitionHistory(defId, definitionXml, map,
					filePath);

		// 内（外）部子流程
		if (map.get("subDefinition") & BeanUtils.isNotEmpty(actDeployId))
			this.exportSubDefinition(actDeployId, definitionXml, map,
					filePath);

		// 流程定义权限
		if (map.get("bpmDefRights") && BeanUtils.isNotEmpty(defKey)) {
			List<DefRights> bpmDefRightsList = defRightsDao
					.getByDefKey(defKey);
			bpmDefRightsList = checkDefRights(bpmDefRightsList);
			definitionXml.setDefRightsList(bpmDefRightsList);
		}

		// 流程跳转规则
		if (map.get("bpmNodeRule") && BeanUtils.isNotEmpty(actDefId)) {
			List<NodeRule> list = nodeRuleDao
					.getByActDefId(actDefId);
			 List<NodeRule> bpmNodeRuleList = new ArrayList<NodeRule>();
		      for (NodeRule bpmNodeRule : list) {
		        bpmNodeRule.setConditionCode(parseScript(bpmNodeRule.getConditionCode(), true));
		        bpmNodeRuleList.add(bpmNodeRule);
		      }
			definitionXml.setNodeRuleList(bpmNodeRuleList);
		}

		// 流程事件脚本
		if (map.get("bpmNodeScript") && BeanUtils.isNotEmpty(actDefId)) {
			List<NodeScript> list = nodeScriptDao
					.getByNodeScriptId("", actDefId);
			 List<NodeScript> bpmNodeScriptList = new ArrayList<NodeScript>();
			for (NodeScript nodeScript : list) {
				nodeScript.setScript(parseScript(nodeScript.getScript(), true));;
				bpmNodeScriptList.add(nodeScript);
		      }
			definitionXml.setNodeScriptList(bpmNodeScriptList);
		}

		// 流程会签规则
		if (map.get("bpmNodeSign") && BeanUtils.isNotEmpty(actDefId)) {
			List<NodeSign> bpmNodeSignList = nodeSignDao
					.getByActDefId(actDefId);
			definitionXml.setNodeSignList(bpmNodeSignList);
		}

		// 流程消息
		if (map.get("bpmNodeMessage") && BeanUtils.isNotEmpty(actDefId)) {
			List<NodeMessage> bpmNodeMessageList = nodeMessageDao
					.getByActDefId(actDefId);
			definitionXml.setNodeMessageList(bpmNodeMessageList);
		}

//		 List<Message> messageList =
//		 messageDao.getByActDefId(actDefId);
//		bpmDefinitionXml.setMessageList(messageList);

		// 流程变量
		if (map.get("bpmDefVar") && BeanUtils.isNotEmpty(defId)) {
			List<DefVar> bpmDefVarList = defVarDao.getByDefId(defId);
			definitionXml.setDefVarList(bpmDefVarList);
		}

		// 流程节点设置
		if (map.get("bpmNodeSet") && BeanUtils.isNotEmpty(defId)) {
			// 流程节点设置
			List<NodeSet> bpmNodeSetList = nodeSetDao
					.getAllByDefId(defId);
			if (!map.get("bpmFormDef")) {
				for (NodeSet bpmNodeSet : bpmNodeSetList) {
					bpmNodeSet.setFormKey(0l);
					bpmNodeSet.setFormDefName("");
				}
			}
			definitionXml.setNodeSetList(bpmNodeSetList);

			if (map.get("bpmNodeUser") && BeanUtils.isNotEmpty(actDefId)) {

				// 节点下的人员的配置规则(包含的抄送的设置也导出)
				List<UserCondition> bpmUserConditionList = userConditionDao
						.getByActDefIdExport(actDefId);
				if (BeanUtils.isNotEmpty(bpmUserConditionList))
					definitionXml.setUserConditionList(bpmUserConditionList);

				// 节点下的人员设置
				List<NodeUser> bpmNodeUserList = nodeUserDao
						.getByActDefId(actDefId);
				if (BeanUtils.isNotEmpty(bpmNodeUserList)) {
					this.checkBpmNodeUserList(bpmNodeUserList);
					definitionXml.setNodeUserList(bpmNodeUserList);
				}
			}

			// 自定义表单
			if (map.get("bpmFormDef") && BeanUtils.isNotEmpty(bpmNodeSetList)) {
				Set<Long> tableIdSet = this.exportBpmFormDef(bpmNodeSetList, definitionXml);
				// 自定义表
				if (map.get("bpmFormTable") && BeanUtils.isNotEmpty(tableIdSet))
					this.exportFormTable(tableIdSet, definitionXml);
			}
		}

		// 表单权限
		if(BeanUtils.isNotEmpty(actDefId)){
			/*List<? extends IFormRights> bpmFormRightsList = formRightsService
					.getFormRightsByActDefId(actDefId);
			if (BeanUtils.isNotEmpty(bpmFormRightsList)){
				bpmFormRightsList = formDefService
						.exportFormRightsUser(bpmFormRightsList);
				definitionXml.setFormRightsList(bpmFormRightsList);
			}*/
			List<? extends BaseFormRights> bpmFormRightsList=formDefService
					.exportFormRightsUser(actDefId);
			definitionXml.setFormRightsList(bpmFormRightsList);
		}
		// 流程操作按钮设置
		if (map.get("bpmNodeButton") && BeanUtils.isNotEmpty(defId)) {
			List<NodeButton> bpmNodeButtonList = nodeButtonDao
					.getByDefId(defId);
			definitionXml.setNodeButtonList(bpmNodeButtonList);
		}

		 
		// 任务节点催办时间设置
		if (map.get("taskReminder") && BeanUtils.isNotEmpty(actDefId)) {
			List<TaskReminder> list = taskReminderDao
					.getByActDefId(actDefId);
			List<TaskReminder> taskReminderList = new ArrayList<TaskReminder>();
		      for (TaskReminder taskReminder : list) {
		        taskReminder.setMailContent(parseScript(taskReminder.getMailContent(), true));
		        taskReminder.setMsgContent(parseScript(taskReminder.getMsgContent(), true));
		        taskReminder.setSmsContent(parseScript(taskReminder.getSmsContent(), true));
		        taskReminder.setCondExp(parseScript(taskReminder.getCondExp(), true));
		        taskReminder.setScript(parseScript(taskReminder.getScript(), true));
		        taskReminderList.add(taskReminder);
		      } 
			definitionXml.setTaskReminderList(taskReminderList);

		
		}
		
		// 流程联动设置
		if (map.get("bpmGangedSet") && BeanUtils.isNotEmpty(defId)) {
			
			List<GangedSet> bpmGangedSetList = gangedSetDao
					.getByDefId(defId);
			definitionXml.setGangedSetList(bpmGangedSetList);

		}
		
//		// 流程引用设置
//		if (map.get("bpmReferDefinition") && BeanUtils.isNotEmpty(defId)) {
//			
//			List<BpmReferDefinition> bpmReferDefinitionList= bpmReferDefinitionDao.getByDefId(defId);
//			bpmDefinitionXml.setBpmReferDefinitionList(bpmReferDefinitionList);
//		}
//		
		
		List<BaseSysFile> sysFileList = new ArrayList<BaseSysFile>();
		// 处理表单附件
		List<? extends BaseFormDefXml> bpmFormDefXmlList = definitionXml.getFormDefXmlList();
		if (BeanUtils.isNotEmpty(bpmFormDefXmlList)) {
			for (BaseFormDefXml bpmFormDefXml : bpmFormDefXmlList) {
				if (BeanUtils.isNotEmpty(bpmFormDefXml.getSysFileList()))
					sysFileList.addAll(bpmFormDefXml.getSysFileList());
			}
		}
		// 流程帮助
		if (BeanUtils.isNotEmpty(definition.getAttachment())) {
		    BaseSysFile sysFile =(BaseSysFile)sysFileService.getById(definition.getAttachment());
			if (BeanUtils.isNotEmpty(sysFile))
				sysFileList.add(sysFile);
		}
		// 附件
		if (BeanUtils.isNotEmpty(sysFileList)) {
			definitionXml.setSysFileList(sysFileList);
			String path = StringUtil
					.trimSufffix(AppUtil.getAttachPath().toString(),
							File.separator);
			for (BaseSysFile sysFile : sysFileList) {
				this.copySysFile(sysFile, filePath, path);
			}
		}
		
		

		return definitionXml;
	}

	private String parseScript(String script, boolean flag) {
		return StringUtil.convertScriptLine(script, Boolean.valueOf(flag));
	}
	

	private Definition parseDefinition(Definition definition,
			boolean flag) {
		definition.setDefXml(StringUtil.convertScriptLine(
				definition.getDefXml(), flag));
		return definition;
	}

	/**
	 * 导出自定义表
	 * 
	 * @param tableIdSet
	 * @param bpmDefinitionXml
	 */
	private void exportFormTable(Set<Long> tableIdSet,
			DefinitionXml bpmDefinitionXml) {
		Map<String, Boolean> map = formTableService
				.getDefaultExportMap(null);
		List<BaseFormTableXml> bpmFormTableXmlList = new ArrayList<BaseFormTableXml>();
		for (Long tableId : tableIdSet) {
			/*
			 * IFormTable formTable = formTableService.getById(tableId);
			
			IFormTableXml bpmFormTableXml = formTableService.exportTable(
					formTable, map);
			*/
		    BaseFormTableXml bpmFormTableXml = formTableService.exportTable(
					tableId, map);
			bpmFormTableXmlList.add(bpmFormTableXml);
		}
		if (BeanUtils.isNotEmpty(bpmFormTableXmlList)){
	          bpmDefinitionXml.setFormTableXmlList(bpmFormTableXmlList);
		}
	}
	
	/**
	 * 复制附件到指定文件
	 * 
	 * @param sysFile
	 * @param filePath
	 * @param path
	 *            复制到的路径
	 */
	public void copySysFile(BaseSysFile sysFile, String filePath, String path) {
		try {
			String realPath = path + File.separator
					+ sysFile.getFilepath().replace("/", File.separator);
			String fileName = sysFile.getFileId() + "." + sysFile.getExt();
			// 复制到指定文件
			File file = new File(realPath);
			if (file.exists()) {
				filePath = filePath + File.separator + fileName;
				FileOperator.createFolderFile(filePath);
				FileOperator.copyFile(realPath, filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理节点人员
	 * 
	 * @param bpmNodeUserList
	 */
	private void checkBpmNodeUserList(List<NodeUser> bpmNodeUserList) {
		for (NodeUser bpmNodeUser : bpmNodeUserList) {
			if (BeanUtils.isEmpty(bpmNodeUser.getAssignType()))
				continue;
			if (BeanUtils.isEmpty(bpmNodeUser.getCmpNames()))
				continue;
			if (NodeUser.ASSIGN_TYPE_USER.equals(bpmNodeUser.getAssignType() ) ) {
				String cmpIds = bpmNodeUser.getCmpIds();
				String[] cmpIdAry = cmpIds.split(",");
				String[] cmpNames = new String[cmpIdAry.length];
				for (int i = 0; i < cmpIdAry.length; i++) {
					ISysUser sysUser =(ISysUser) sysUserService.getById(new Long(cmpIdAry[i]));
					if (BeanUtils.isNotEmpty(sysUser))
						cmpNames[i] = sysUser.getUsername();
				}
				bpmNodeUser.setCmpNames(StringUtils.join(cmpNames, ","));
			}
		}
	}

	/**
	 * 导出自定义表单
	 * 
	 * @param bpmNodeSetList
	 * @param bpmDefinitionXml
	 * @return
	 */
	private Set<Long> exportBpmFormDef(List<NodeSet> bpmNodeSetList,
			DefinitionXml bpmDefinitionXml) {
		Map<String, Boolean> map = formDefService.getDefaultExportMap(null);
		// 不出现重复的formKey,取得流程定义key
		Set<Long> formKeySet = new HashSet<Long>();
		for (NodeSet bpmNodeSet : bpmNodeSetList) {
			if(BeanUtils.isNotEmpty(bpmNodeSet.getFormKey()) && bpmNodeSet.getFormKey().longValue() > 0L)
				formKeySet.add(bpmNodeSet.getFormKey());
		}

		Set<Long> tableIdSet = new HashSet<Long>();
		// 自定义表单
		List<?extends BaseFormDefXml> bpmFormDefXmlList =formDefService.getFormDefXmlList(map, formKeySet, tableIdSet);;
		if (BeanUtils.isNotEmpty(bpmFormDefXmlList))
			bpmDefinitionXml.setFormDefXmlList(bpmFormDefXmlList);
		return tableIdSet;
	}

	
	/**
	 * 处理流程定义权限的人员
	 * 
	 * @param bpmDefRightsList
	 * @return
	 */
	private List<DefRights> checkDefRights(
			List<DefRights> bpmDefRightsList) {

		// 处理流程定义权限的人员
		for (DefRights bpmDefRights : bpmDefRightsList) {
			if (bpmDefRights.getRightType().shortValue() == DefRights.RIGHT_TYPE_USER) {
				Long userId = bpmDefRights.getOwnerId();
				ISysUser sysUser = (ISysUser)sysUserService.getById(userId);
				if (BeanUtils.isNotEmpty(sysUser))
					bpmDefRights.setOwnerName(sysUser.getUsername());
			}
		}
		return bpmDefRightsList;
	}

	/**
	 * 导出内（外）部子流程
	 * 
	 * @param actDeployId
	 * @param bpmDefinitionXml
	 * @param map
	 * @throws Exception
	 */
	private void exportSubDefinition(Long actDeployId,
			DefinitionXml bpmDefinitionXml, Map<String, Boolean> map,
			String filePath) throws Exception {
		if (BeanUtils.isEmpty(actDeployId))
			return;
		List<DefinitionXml> subDefinitionXmlList = new ArrayList<DefinitionXml>();
		String xml = bpmDao.getDefXmlByDeployId(actDeployId.toString());
		Set<String> keySet = NodeCache.parseXmlBySubKey(xml);
		for (String flowKey : keySet) {
			Definition bpmDefinition = dao.getByActDefKeyIsMain(flowKey);
			subDefinitionXmlList.add(exportDefinition(bpmDefinition,
					Definition.MAIN, map, filePath));
		}

		if (BeanUtils.isNotEmpty(subDefinitionXmlList))
			bpmDefinitionXml
					.setSubDefinitionXmlList(subDefinitionXmlList);

	}

	/**
	 * 导出流程定义历史版本
	 * 
	 * @param defId
	 * @param bpmDefinitionXml
	 * @param map
	 * @throws Exception
	 */
	private void exportDefinitionHistory(Long defId,
			DefinitionXml bpmDefinitionXml, Map<String, Boolean> map,
			String filePath) throws Exception {
		List<Definition> bpmDefinitionList = this
				.getAllHistoryVersions(defId);
		if (BeanUtils.isEmpty(bpmDefinitionList))
			return;
		List<DefinitionXml> bpmDefinitionXmlList = new ArrayList<DefinitionXml>();
		for (Definition bpmDefinition : bpmDefinitionList) {
			DefinitionXml definitionXml = exportDefinition(bpmDefinition,
					Definition.NOT_MAIN, map, filePath);
			bpmDefinitionXmlList.add(definitionXml);
		}
		bpmDefinitionXml.setDefinitionXmlList(bpmDefinitionXmlList);

	}
	
	/**
	 * 根据流程key取得流程定义数据。
	 * @param flowKey
	 * @return
	 */
	public Definition getMainDefByActDefKey(String actDefKey){
		return dao.getByActDefKeyIsMain(actDefKey);
	}
	
	public List<Definition> getByUserId(QueryFilter queryFilter)
	{
		List<Definition> list= dao.getByUserId(queryFilter);
		return list;
	}
	
	/**
	 * 按用户Id及查询参数查找我能访问的所有流程定义
	 * @param queryFilter
	 * @return
	 */
	public List<Definition> getByUserIdFilter(QueryFilter queryFilter){
		return dao.getByUserIdFilter(queryFilter);
	}
	
	/**
	 * 判断流程key是否存在。
	 * @param key
	 * @return
	 */
	public boolean isActDefKeyExists(String key){
		return dao.isActDefKeyExists(key);
	}
	
	/**
	 * 判断defkey是否存在。
	 * @param key
	 * @return
	 */
	public boolean isDefKeyExists(String defkey){
		return dao.isDefKeyExists(defkey);
	}
	
	/**
	 * 通过标题模糊查询所有发布的、默认版本的流程
	 * @param subject
	 * @return
	 */
	public List<Definition> getAllPublished(String subject){
		return dao.getAllPublished(subject);
	}
	
	/**
	 * 通过类型ID查询所有发布的、默认版本的流程
	 * @param typeId
	 * @return
	 */
	public List<Definition> getPublishedByTypeId(String typeId){
		return dao.getPublishedByTypeId(typeId);
	}
	
	/**
	 * 根据流程定义key获得当前最新版本的流程定义
	 * @param defkey 
	 * @return
	 */
	public Definition getMainByDefKey(String defkey){
		return dao.getMainByDefKey(defkey);
	}
	
	/**
	 * 更新流程启动状态
	 * @param defId
	 * @param disableStatus
	 * @return
	 */
	public int updateDisableStatus(Long defId,Short disableStatus)
	{
		return dao.updateDisableStatus(defId, disableStatus);
	}

	/**
	 * 根据用户ID，获该用户所创建的流程定义
	 * @param userId 用户ID
	 * @param pb 分页Bean
	 * @return
	 */
	public List<Definition> getByUserId(Long userId,Map<String,Object> params,PagingBean pb) {
		return dao.getByUserId(userId,params,pb);
	}
	
	/**
	 * 清除流程相关数据
	 * @param defId
	 * @throws Exception 
	 */
	public void cleanData(Long defId) throws Exception {
		Definition bpmDefinition=dao.getById(defId);
		//版本号
		int versionNo = bpmDefinition.getVersionNo();
		List<Definition> definitionList = new ArrayList<Definition>(); 
		if(versionNo >1){
			 String actFlowKey=bpmDefinition.getActDefKey(); //版本不同流程发布Id唯一
			 definitionList = dao.getByActDefKey(actFlowKey);
		}else {
			definitionList.add(bpmDefinition);
		}
		for(Definition definition:definitionList){
			String actDefId=definition.getActDefId();
			//清除子流程数据
			this.cleanSubData(actDefId);
			//获取测试状态流程实例
			List<ProcessRun> processRunList=processRunService.getTestRunsByActDefId(actDefId);
			for(ProcessRun processRun:processRunList){
				Long runId=processRun.getRunId();
				String businessKey=processRun.getBusinessKey();
				String dsAlias=processRun.getDsAlias();
				String tableName=processRun.getTableName();
				//删除业务数据
				if(StringUtil.isNotEmpty(tableName)){
					if(StringUtil.isEmpty(dsAlias)||dsAlias.equals(ISysDataSource.DS_LOCAL)){
						tableName=tableName.replaceFirst(ITableModel.CUSTOMER_TABLE_PREFIX, "");
						//IFormTable bpmFormTable=formTableService.getByTableName(tableName);
						formHandlerService.delById(businessKey, tableName);
					}else{
						formHandlerService.delByDsAliasAndTableName(dsAlias,tableName,businessKey);
					}
				}
				//非草稿状态 清除act流程数据
				if(!ProcessRun.STATUS_FORM.equals(processRun.getStatus())){
					Long actInstId=Long.parseLong(processRun.getActInstId());
					if (ProcessRun.STATUS_FINISH != processRun.getStatus()) {
						executionDao.delVariableByProcInstId(actInstId);
						taskDao.delCandidateByInstanceId(actInstId);
						taskDao.delByInstanceId(actInstId);
						executionDao.delExecutionByProcInstId(actInstId);
					}
					//删除节点状态
					proStatusDao.delByActInstId(actInstId);
					//删除堆栈信息
					executionStackDao.delByActDefId(processRun.getActInstId());
					//删除审批意见
					taskOpinionDao.delByActInstId(processRun.getActInstId());
					//任务已读信息
					taskReadService.delByActInstId(actInstId);
				}
				//删除流程操作日志
				runLogService.delByRunId(runId);
				//删除抄送转发事宜
				proCopytoService.delByRunId(runId);
				//代理转办数据BPM_TASK_EXE删除 BPM_COMMU_RECEIVER:通知接收人，BPM_TASK_READ：任务是否已读
				List<TaskExe> bpmTaskExeList = taskExeService.getByRunId(processRun.getRunId());
				if(BeanUtils.isNotEmpty(bpmTaskExeList)){
					for(TaskExe bpmTaskExe:bpmTaskExeList){
						commuReceiverService.delByTaskId(bpmTaskExe.getTaskId());
					}
					//删除转办代理事宜
					taskExeService.delByRunId(runId);
				}
				//删除流程实例
				processRunService.delById(runId);
			}
			
		}

	}
	
	private void cleanSubData(String actDefId) throws Exception{
		if(NodeCache.hasExternalSubprocess(actDefId)){
			Map<String,FlowNode> flowNodes=NodeCache.getByActDefId(actDefId);
			Set<String> keyset=flowNodes.keySet();
			for(Iterator<String>it=keyset.iterator();it.hasNext();){
				FlowNode flowNode=flowNodes.get(it.next());
				if("callActivity".equals(flowNode.getNodeType())){
					String flowKey=flowNode.getAttribute("subFlowKey");
					Definition subDefinition=dao.getByDefKey(flowKey);
					this.cleanData(subDefinition.getDefId());
				}
			}
		}
	}

	/**
	 * 查找我能访问的所有流程定义
	 * 
	 * @param filter
	 * @param typeId
	 * @return
	 */
	public List<Definition> getList(QueryFilter filter, Long typeId) {
		if (typeId != 0) {
			IGlobalType globalType = globalTypeService.getById(typeId);
			if (BeanUtils.isNotEmpty(globalType)){
				// 查找某一分类下包含其子类的所有定义
				filter.addFilterForIB("nodePath",globalType.getNodePath() + "%");
			}
		}
		ISysUser curUser = (ISysUser)UserContextUtil.getCurrentUser();
		Long userId = curUser.getUserId();
		// 取得当前用户
		filter.addFilterForIB("userId", userId);
		// 取得当前用户所有的角色
		/*Set<ISysRole> roles = curUser.getRoles();*/
		List<?extends ISysRole> roles=sysRoleService.getByUserId(userId);
		if (BeanUtils.isNotEmpty(roles)) {
			StringBuilder rids = new StringBuilder();
			for(ISysRole role:roles){
				rids.append(",").append(role.getRoleId());
			}
			filter.addFilterForIB("roleIds", rids.substring(1)); 
		}
		ISysOrg currentOrg = (ISysOrg)UserContextUtil.getCurrentOrg();
		List<Long> orgGrantIds = new ArrayList<Long>();
		Long curOrgId = 0L;
		if (currentOrg != null) {
			orgGrantIds = replacePath(currentOrg.getPath());
			curOrgId = currentOrg.getOrgId();
		}
		// 取得当前组织(包含子组织)
		if (BeanUtils.isNotEmpty(orgGrantIds)) {
			filter.addFilterForIB("orgGrantIds", StringUtils.join(orgGrantIds,","));
			//filter.addFilter("orgGrantIds", orgGrantIds);
		}
		// 取得组织（本层级）
		if (curOrgId>0) {
			filter.addFilterForIB("curOrgId", curOrgId);
		}
		// 取得当前岗位
		/*Set<IPosition> positonList = curUser.getPositions();*/
		List<?extends IPosition> positonList=positionService.getByUserId(userId);
		if (BeanUtils.isNotEmpty(positonList)) {
			StringBuilder pids = new StringBuilder();
			for(IPosition pos:positonList){
				pids.append(",").append(pos.getPosId());
			}
			filter.addFilterForIB("positonIds", pids.substring(1)); 
		}

		// 根据流程授权获取流程。
		return this.getByUserIdFilter(filter);
	}

	@SuppressWarnings("unused")
	public List<Definition> getMyList(Long userId){
		Map<String,Object> params=new HashMap<String, Object>();
		// 取得当前用户
		params.put("userId", userId);
		// 取得当前用户所有的角色
		ISysUser curUser = sysUserService.getById(userId);
		/*Set<ISysRole> roles = curUser.getRoles();*/
		List<?extends ISysRole> roles=sysRoleService.getByUserId(userId);
		if (BeanUtils.isNotEmpty(roles)) {
			StringBuilder rids = new StringBuilder();
			for(ISysRole role:roles){
				rids.append(",").append(role.getRoleId());
			}
			params.put("roleIds", rids.substring(1)); 
		}
		ISysOrg currentOrg = (ISysOrg)UserContextUtil.getCurrentOrg();
		List<Long> orgGrantIds = new ArrayList<Long>();
		Long curOrgId = 0L;
		if (currentOrg != null) {
			orgGrantIds = replacePath(currentOrg.getPath());
			curOrgId = currentOrg.getOrgId();
		}
		// 取得当前组织(包含子组织)
		if (BeanUtils.isNotEmpty(orgGrantIds)) {
			params.put("orgGrantIds", StringUtils.join(orgGrantIds,","));
		}
		// 取得组织（本层级）
		if (curOrgId>0) {
			params.put("curOrgId", curOrgId);
		}
		// 取得当前岗位
		/*Set<IPosition> positonList = curUser.getPositions();*/
		List<?extends IPosition> positonList=positionService.getByUserId(userId);
		if (BeanUtils.isNotEmpty(positonList)) {
			StringBuilder pids = new StringBuilder();
			for(IPosition pos:positonList){
				pids.append(",").append(pos.getPosId());
			}
			params.put("positonIds", pids.substring(1)); 
		} 
		return dao.getByUserId(params);
		
	}
	
	private List<Long> replacePath(String path) {
		if(StringUtil.isEmpty(path)) return new ArrayList<Long>();
		path = StringUtil.trimSufffix(path, ".");
		String[] aryPath=path.split("\\.");
		List<Long> list=new ArrayList<Long>();
		for(String tmp:aryPath){
			list.add(new Long(tmp));
		}
		return list;
	}
	
	/**
	 * 判断流程是否允许转办。
	 * 
	 * @param actDefId
	 * @return
	 */
	public boolean allowDivert(String actDefId) {
		Definition bpmDefinition = dao.getByActDefId(actDefId);
		return bpmDefinition.getAllowDivert() == 1;
	}
	

	
	/**
	 * 根据流程定义获取表单结果。
	 * @param defId
	 * @return
	 */
	public FormResult getFormResult(Long defId){
		List<? extends IFormTable> list= formTableService.getTableNameByDefId(defId);
		FormResult bpmFormResult=new FormResult();
		//没有找到对应的表单
		if(list.size()==0){
			bpmFormResult.setResult(2);
		}
		else if(list.size()>1){
			bpmFormResult.setResult(1);
		}
		else{
			IFormTable bpmFormTable=list.get(0);
			String tableName=bpmFormTable.getTableName();
			String dsName=bpmFormTable.getDsAlias();
			Integer isexternal=bpmFormTable.getIsExternal();
			String name=tableName;
			if(isexternal==1){
				name=dsName +"_" + tableName;
			}
			bpmFormResult.setTableName(name);
		}
		
		return bpmFormResult;
	}

	public void updCategory(Long typeId, List<String> defKeylist) {
		dao.updCategory(typeId, defKeylist);
		
	}
	/**
	 * 根据流程定义key获得流程定义
	 * @param defkey 
	 * @return
	 */
	public Definition getByDefKey(String defkey){
		return dao.getByDefKey(defkey);
	}
	
	   /**
     * 根据流程定义key获得流程定义
     * @param defkey 
     * @return
     */
    public Definition getByDefKeyIsMain(String defkey){
        return dao.getByDefKeyIsMain(defkey,true);
    }
	
	public List<Definition> getDefinitionByFormKey(Long formKey){
		return dao.getDefinitionByFormKey(formKey);
	}
	/**
	 * @author Yangbo 2016-7-22
	 * 按用户授权获取
	 * @param actRights
	 * @return
	 */
	public List<Definition> getMyDefListForDesktop(String actRights)
	{
		return this.dao.getMyDefListForDesktop(actRights);
	}
	
	/**
	 * @author Yangbo 2016-8-31
	 * @param filter
	 * @param typeId
	 * @return
	 */
	public List<Definition> getMyDefList(QueryFilter filter, Long typeId)
	{
		if (typeId.longValue() != 0L) {
			IGlobalType globalType = (IGlobalType)this.globalTypeService.getById(typeId);
			if (BeanUtils.isNotEmpty(globalType))
			{
				filter.addFilterForIB("nodePath", globalType.getNodePath() + "%");
			}
		}

		return this.dao.getMyDefList(filter);
	}
	
	
	/**
	 * @throws Exception 
	 * @throws TransformerFactoryConfigurationError  
	* @Title: getMainActIDbySubDefinitionID 
	* @Description: TODO(通过子流程定义ID获取主流程节点) 
	* @param @param actdefId
	* @param @return    设定文件 
	* @return Definition    返回类型 
	* @throws 
	*/
	public String getMainActIDbySubDefinitionID(String subActdefId,String mainActdefId) throws Exception{
	    Definition subDefinition=this.getByActDefId(subActdefId);
	    Definition mainDefinition=this.getByActDefId(mainActdefId);
	    String defXml = null;
        if (mainDefinition.getActDeployId() != null) {
            defXml = bpmService.getDefXmlByDeployId(mainDefinition
                    .getActDeployId().toString());
        } else {
            defXml = BpmUtil.transform(mainDefinition.getDefKey(),
                mainDefinition.getSubject(), mainDefinition.getDefXml());
        }
        defXml = defXml.replace(
            "xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"", "");
        Document doc = Dom4jUtil.loadXml(defXml);
        Element root = doc.getRootElement();
        List<Node> callActivityList = root
                .selectNodes("./process//callActivity");
        String subExtActId="";
        for (Node node : callActivityList) {
            Element element = (Element) node;
            String subFlowKey = element.attributeValue("calledElement");
            if (subDefinition.getDefKey().equals(subFlowKey)) {
                return subExtActId;
            }
        }
	    return null;
	}

    public void updatePendingSetting(Long defId, String pendingSetting)
    {
        
        this.dao.updatePendingSetting(defId,pendingSetting);
    }
    
    /** 
    * @Title: getbyKeyPath 
    * @Description: TODO(根据keypath 获取子流程定义) 
    * @param @param defKey
    * @param @return     
    * @return List<? extends IDefinition>    返回类型 
    * @throws 
    */
    @Override
    public List<Definition> getbyKeyPath(String defKey)
    {
        return this.dao.getbyKeyPath(defKey);
    }
}
