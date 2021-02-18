package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.ContextUtil;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.DefinitionDao;
import com.cssrc.ibms.core.flow.dao.NodeButtonDao;
import com.cssrc.ibms.core.flow.model.Button;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeButton;
import com.cssrc.ibms.core.flow.model.NodeButtonXml;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;

/**
 * 对象功能:自定义工具条 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class NodeButtonService extends BaseService<NodeButton> {
	@Resource
	private NodeButtonDao dao;
	@Resource
	private DefinitionDao bpmDefinitionDao;
	@Resource
	private IBpmService bpmService;
	 

	public NodeButtonService() {
	}
	@Override
	protected IEntityDao<NodeButton, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 根据流程定义ID和节点ID获取操作按钮。
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public List<NodeButton> getByDefNodeId(Long defId, String nodeId) {
		return dao.getByDefNodeId(defId, nodeId);
	}

	/**
	 * 根据流程定义ID获取起始表单的操作按钮数据。
	 * 
	 * @param actDefId
	 * @return
	 */
	public List<NodeButton> getByStartForm(Long defId) {
		List<NodeButton> list = dao.getByStartForm(defId);
		return list;
	}

	/**
	 * 获取流程定义的按钮。
	 * 
	 * @param defId
	 * @return
	 */
	public Map<String, List<NodeButton>> getMapByStartForm(Long defId) {
		Map<String, List<NodeButton>> map = new HashMap<String, List<NodeButton>>();
		List<NodeButton> list = dao.getByStartForm(defId);
		if (BeanUtils.isEmpty(list)) {
			return map;
		}
		for (NodeButton bpmNodeButton : list) { 
			addItem(map, bpmNodeButton, "button");
		}
		return map;
	}

	/**
	 * 根据流程定义ID和流程节点ID获取按钮。
	 * 
	 * @param defId
	 *            流程定义id。
	 * @param nodeId
	 *            流程节点id。
	 * @return
	 */
	public Map<String, List<NodeButton>> getMapByDefNodeId(Long defId,
			String nodeId) {
		Map<String, List<NodeButton>> map = new HashMap<String, List<NodeButton>>();
		List<NodeButton> list = dao.getByDefNodeId(defId, nodeId);
		if (BeanUtils.isEmpty(list)) {
			return map;
		}
		for (NodeButton bpmNodeButton : list) { 
			addItem(map, bpmNodeButton, "button");
		}
		return map;
	}

	/**
	 * 根据流程定义ID获取获取节点和操作按钮的映射。
	 * 
	 * @param actDefId
	 * @return
	 */
	public Map<String, List<NodeButton>> getMapByDefId(Long defId) {
		List<NodeButton> list = dao.getByDefId(defId);
		Map<String, List<NodeButton>> map = new HashMap<String, List<NodeButton>>();
		if (BeanUtils.isEmpty(list))
			return map;
		for (NodeButton bpmNodeButton : list) { 
			if (bpmNodeButton.getIsstartform() == 1) {
				addItem(map, bpmNodeButton, "start");
			} else {
				addItem(map, bpmNodeButton, bpmNodeButton.getNodeid());
			}
		}
		return map;
	}

 
	/**
	 * 添加操作按钮到
	 * 
	 * @param map
	 * @param bpmNodeButton
	 * @param key
	 */
	private void addItem(Map<String, List<NodeButton>> map,NodeButton bpmNodeButton, String key)
	{
		if (map.containsKey(key)) {
			map.get(key).add(bpmNodeButton);
		} else {
			List<NodeButton> list = new ArrayList<NodeButton>();
			list.add(bpmNodeButton);
			map.put(key, list);
		}
	}

	/**
	 * 根据流程定义和节点操作类型判断是否已经存在。
	 * 
	 * @param bpmNodeButton
	 *            按钮操作。
	 * @return
	 */
	public boolean isOperatorExist(NodeButton bpmNodeButton) {
		Long defId = bpmNodeButton.getDefId();
		Integer operatortype = bpmNodeButton.getOperatortype();
		if (bpmNodeButton.getIsstartform() == 1) {
			return dao.isStartFormExist(defId, operatortype) > 0;
		}
		String nodeId = bpmNodeButton.getNodeid();
		return dao.isExistByNodeId(defId, nodeId, operatortype) > 0;
	}

	/**
	 * 更新时根据流程定义和节点操作类型判断是否已经存在。
	 * 
	 * @param bpmNodeButton
	 * @return
	 */
	public boolean isOperatorExistForUpd(NodeButton bpmNodeButton) {
		Long defId = bpmNodeButton.getDefId();
		Long id = bpmNodeButton.getId();
		Integer operatortype = bpmNodeButton.getOperatortype();
		if (bpmNodeButton.getIsstartform() == 1) {
			return dao.isStartFormExistForUpd(defId, operatortype, id) > 0;
		}
		String nodeId = bpmNodeButton.getNodeid();
		return dao.isExistByNodeIdForUpd(defId, nodeId, operatortype, id) > 0;
	}

	/**
	 * 更新排序字段。
	 * 
	 * @param ids
	 */
	public void sort(String ids) {
		String[] aryId = ids.split(",");
		for (int i = 0; i < aryId.length; i++) {
			Long id = Long.parseLong(aryId[i]);
			dao.updSn(id, (long) (i + 1));
		}
	}
	/**
	 * 初始化起始节点
	 * @param actDefId
	 * @param defId
	 * @throws Exception
	 */
	private void initStartForm(String actDefId, Long defId , List<Button> list) throws Exception {
		for(Button button:list){
			if(0==button.getType() && 1==button.getInit()){
				NodeButton nodeButton=new NodeButton(actDefId, defId, button.getText(), button.getOperatortype());
				dao.add(nodeButton);
			}
		}
	}

	
	/**
	 * 初始化其它节点(不是起始节点)
	 * @param actDefId
	 * @param defId
	 * @param nodeId
	 * @param isSignTask
	 * @param isFirstNode 
	 * @throws Exception
	 */
	private void initNodeId(String actDefId, Long defId, String nodeId,
			boolean isSignTask,boolean isFirstNode, List<Button> list) throws Exception {
		int nodetype = (isSignTask) ? 1 : 0;
		//是否第一个节点
		if(isFirstNode){
			for(Button button:list){
				if(1==button.getType() && 1==button.getInit()){
					NodeButton nodeButton=new NodeButton(actDefId, defId, nodeId, button.getText(), button.getOperatortype(), nodetype);
					dao.add(nodeButton);
				}
			}
		}
		else{
			if(isSignTask){
				for(Button button:list){
					if((2==button.getType() || 4==button.getType()) && 1==button.getInit()){
						NodeButton nodeButton=new NodeButton(actDefId, defId, nodeId, button.getText(), button.getOperatortype(), nodetype);
						dao.add(nodeButton);
					}
				}
			}
			else{
				for(Button button:list){
					if((3==button.getType() || 4==button.getType()) && 1==button.getInit()){
						NodeButton nodeButton=new NodeButton(actDefId, defId, nodeId, button.getText(), button.getOperatortype(), nodetype);
						dao.add(nodeButton);
					}
				}
			}
	    }
	}
	


	/**
	 * 初始化按钮。
	 * 
	 * @param defId
	 * @param nodeId
	 * @throws Exception
	 */
	public void init(Long defId, String nodeId) throws Exception {
		Definition bpmDefinition = bpmDefinitionDao.getById(defId);
		String actDefId = bpmDefinition.getActDefId();
		//是否是开始节点
		Boolean isStartForm = StringUtil.isEmpty(nodeId) ? true : false; 
		//读button.xml
		String buttonPath = FlowUtil.getDesignButtonPath();
		String buttonFileName="button.xml";
		String xml = FileOperator.readFile(buttonPath + buttonFileName);
		Document document = Dom4jUtil.loadXml(xml);
		Element root = document.getRootElement();
		String xmlStr = root.asXML();
		NodeButtonXml bpmButtonList = (NodeButtonXml) XmlBeanUtil
				.unmarshall(xmlStr, NodeButtonXml.class);
		List<Button> list = bpmButtonList.getButtons();
		if(BeanUtils.isEmpty(list)) return;
		if (isStartForm) {
			dao.delByStartForm(defId);
			initStartForm(actDefId, defId, list);

		}
		else {
			dao.delByNodeId(defId, nodeId);
			boolean isSignTask = bpmService.isSignTask(actDefId, nodeId);
			boolean isFirstNode = NodeCache.isFirstNode(actDefId, nodeId);
			initNodeId(actDefId, defId, nodeId, isSignTask, isFirstNode, list);
		}
	}
	/**
	 * 初始化流程的全部按钮。
	 * 
	 * @param defId
	 *            流程定义ID
	 * @throws Exception
	 */
	public void initAll(Long defId) throws Exception {
		dao.delByDefId(defId);
		Definition bpmDefinition = bpmDefinitionDao.getById(defId);
		String actDefId = bpmDefinition.getActDefId();
		//获取当前语言环境
		String locale=ContextUtil.getLocale().toString();
		//读button.xml
		String buttonPath = FlowUtil.getDesignButtonPath();
		String buttonFileName="button.xml";
		String xml = FileOperator.readFile(buttonPath + buttonFileName);
		Document document = Dom4jUtil.loadXml(xml);
		Element root = document.getRootElement();
		String xmlStr = root.asXML();
		NodeButtonXml bpmButtonList = (NodeButtonXml) XmlBeanUtil
				.unmarshall(xmlStr, NodeButtonXml.class);
		List<Button> list = bpmButtonList.getButtons();
		if(BeanUtils.isEmpty(list)) return;
		// 起始表单按钮。
		initStartForm(actDefId, defId, list);
		
		Map<String,FlowNode> map= NodeCache.getByActDefId(actDefId);

		
		Set<Map.Entry<String, FlowNode>> set = map.entrySet();
		for (Iterator<Map.Entry<String, FlowNode>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, FlowNode> entry = (Map.Entry<String, FlowNode>) it.next();
			FlowNode flowNode=entry.getValue();
			boolean isSignTask = flowNode.getIsSignNode();
			boolean isFirstNode = flowNode.getIsFirstNode();
			// 节点按钮。
			initNodeId(actDefId, defId, entry.getKey(), isSignTask,isFirstNode, list);
		}
	}

	/**
	 * 根据流程定义ID删除按钮。
	 * 
	 * @param defId
	 * @throws Exception
	 */
	public void delByDefId(Long defId) throws Exception {
		dao.delByDefId(defId);
	}

	/**
	 * 删除节点或开始表单的节点按钮定义。
	 * 
	 * @param defId
	 * @param nodeId
	 */
	public void delByDefNodeId(Long defId, String nodeId) {
		if (StringUtil.isEmpty(nodeId)) {
			dao.delByStartForm(defId);
		} else {
			dao.delByNodeId(defId, nodeId);
		}
	}

	/**
	 * 根据流程定义ID获得操作按钮。
	 * 
	 * @param defId
	 * @return
	 */
	public List<NodeButton> getByDefId(Long defId) {
		return dao.getByDefId(defId);
	}
	
	
	/**
	 * 
	 * 获取默认的按钮的国际化
	 * @param button 
	 * @param operatortype
	 * @param type	0：表示开始节点
	 * 				1：表示默认按钮
	 * 				2: 表示原生态按钮
	 * @param lang 0:简体中文
	 * 			   1:繁体中文
	 * 				2：english
	 * @return
	 */
	/*public   String  getNodeButton(Integer operatortype, Integer type,String language){
		String buttonName = "";
		String[] lan = language.split("_");
		Locale locale = new Locale(lan[0], lan[1]);
		if (type == 0) {
			switch (operatortype) {
			case NodeButton.START_BUTTON_TYPE_START:// 启动流程（提交）
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.start", locale);
				break;
			case NodeButton.START_BUTTON_TYPE_IMAGE:// 流程示意图
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.image", locale);
				break;
			case NodeButton.START_BUTTON_TYPE_PRINT:// 打印
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.print", locale);
				break;
			case NodeButton.START_BUTTON_TYPE_SAVE:// 保存草稿
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.saveDraft", locale);
				break;
			default:
				break;
			}
		} else {
			switch (operatortype) {
			case NodeButton.NODE_BUTTON_TYPE_COMPLETE:// 同意
				if (type == 1) {// 提交
					buttonName = ContextUtil.getMessagesL("bpmNodeButton.submit", locale);
				} else {// 同意
					buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.complete", locale);
				}
				break;
			case NodeButton.NODE_BUTTON_TYPE_OPPOSE:// 反对
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.oppose", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_ABSTENT:// 弃权
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.abstent", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_BACK:// 驳回
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.back", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_BACKTOSTART:// 驳回发起人
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.backToStart", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_ASSIGNEE:// 转办
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.assignee", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_ADDSIGN:// 补签
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.addsign", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_SAVEFORM:// 保存表单
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.saveForm", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_IMAGE:// 流程示意图
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.image", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_PRINT:// 打印
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.print", locale);
				break;

			case NodeButton.NODE_BUTTON_TYPE_HIS:// 审批历史
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.history", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_WEBSIGN:// Web签章
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.webSignature", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_HANDSIGN:// 手写签章
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.handSignature", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_COMMU:// 沟通
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.commu", locale);
				break;
			case NodeButton.NODE_BUTTON_TYPE_PLUSSIGN:// 加签
				buttonName = ContextUtil.getMessagesL("bpmNodeButton.button.plusSign", locale);
				break;
			default:
				break;
			}
		}

		return buttonName;

	}*/
	
	 

}
