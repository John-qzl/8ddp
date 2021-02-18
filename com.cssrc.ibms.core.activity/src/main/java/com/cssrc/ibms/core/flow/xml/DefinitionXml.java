package com.cssrc.ibms.core.flow.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.api.form.model.BaseFormDefXml;
import com.cssrc.ibms.api.form.model.BaseFormRights;
import com.cssrc.ibms.api.form.model.BaseFormTableXml;
import com.cssrc.ibms.api.form.model.IFormRights;
import com.cssrc.ibms.api.system.model.BaseSysFile;
import com.cssrc.ibms.core.flow.model.DefRights;
import com.cssrc.ibms.core.flow.model.DefVar;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.GangedSet;
import com.cssrc.ibms.core.flow.model.NodeButton;
import com.cssrc.ibms.core.flow.model.NodeMessage;
import com.cssrc.ibms.core.flow.model.NodeRule;
import com.cssrc.ibms.core.flow.model.NodeScript;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.NodeSign;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.model.NodeUserUplow;
import com.cssrc.ibms.core.flow.model.ReferDefinition;
import com.cssrc.ibms.core.flow.model.TaskApprovalItems;
import com.cssrc.ibms.core.flow.model.TaskReminder;
import com.cssrc.ibms.core.flow.model.UserCondition;

/**
 * <pre>
 * 对象功能:流程定义的XMl配置 
 * </pre>
 */
@XmlRootElement(name = "definitions")
@XmlAccessorType(XmlAccessType.FIELD)
public class DefinitionXml {
	/**
	 * 流程定义
	 */
	@XmlElement(name = "definition", type = Definition.class)
	private Definition definition;

	/**
	 * 流程定义其它版本
	 */
	@XmlElementWrapper(name = "definitionList")
	@XmlElements({ @XmlElement(name = "definitions", type = DefinitionXml.class) })
	private List<DefinitionXml> definitionXmlList;

	/**
	 * 流程定义权限
	 */
	@XmlElementWrapper(name = "defRightsList")
	@XmlElements({ @XmlElement(name = "defRights", type = DefRights.class) })
	private List<DefRights> defRightsList;

	/**
	 * 流程定义规则
	 */
	@XmlElementWrapper(name = "nodeRuleList")
	@XmlElements({ @XmlElement(name = "nodeRule", type = NodeRule.class) })
	private List<NodeRule> nodeRuleList;

	/**
	 * 流程事件脚本
	 */
	@XmlElementWrapper(name = "nodeScriptList")
	@XmlElements({ @XmlElement(name = "nodeScript", type = NodeScript.class) })
	private List<NodeScript> nodeScriptList;

	/**
	 * 流程变量
	 */
	@XmlElementWrapper(name = "defVarList")
	@XmlElements({ @XmlElement(name = "defVar", type = DefVar.class) })
	private List<DefVar> defVarList;

	/**
	 * 流程会签规则
	 */
	@XmlElementWrapper(name = "nodeSignList")
	@XmlElements({ @XmlElement(name = "nodeSign", type = NodeSign.class) })
	private List<NodeSign> nodeSignList;

	/**
	 * 流程消息
	 */
	@XmlElementWrapper(name = "nodeMessageList")
	@XmlElements({ @XmlElement(name = "nodeMessage", type = NodeMessage.class) })
	private List<NodeMessage> nodeMessageList;

	/**
	 * 流程节点设置
	 */
	@XmlElementWrapper(name = "nodeSetList")
	@XmlElements({ @XmlElement(name = "nodeSet", type = NodeSet.class) })
	private List<NodeSet> nodeSetList;

	/**
	 * 流程节点条件设置
	 */
	@XmlElementWrapper(name = "userConditionList")
	@XmlElements({ @XmlElement(name = "userCondition", type = UserCondition.class) })
	private List<UserCondition> userConditionList;

	/**
	 * 节点下的人员设置
	 */
	@XmlElementWrapper(name = "nodeUserList")
	@XmlElements({ @XmlElement(name = "nodeUser", type = NodeUser.class) })
	private List<NodeUser> nodeUserList;
	/**
	 * 用户节点的上下级设置
	 */
	@XmlElementWrapper(name = "nodeUserUplowList")
	@XmlElements({ @XmlElement(name = "nodeUserUplow", type = NodeUserUplow.class) })
	private List<NodeUserUplow> nodeUserUplowList;

	/**
	 * 流程按钮设置
	 */
	@XmlElementWrapper(name = "nodeButtonList")
	@XmlElements({ @XmlElement(name = "nodeButton", type = NodeButton.class) })
	private List<NodeButton> nodeButtonList;

	/**
	 * 常用语设置
	 */
	@XmlElementWrapper(name = "taskApprovalItemsList")
	@XmlElements({ @XmlElement(name = "taskApprovalItems", type = TaskApprovalItems.class) })
	private List<TaskApprovalItems> taskApprovalItemsList;

	/**
	 * 任务节点催办时间设置
	 */
	@XmlElementWrapper(name = "taskReminderList")
	@XmlElements({ @XmlElement(name = "taskReminder", type = TaskReminder.class) })
	private List<TaskReminder> taskReminderList;


	/**
	 * 流程联动设置
	 */
	@XmlElementWrapper(name = "gangedSetList")
	@XmlElements({ @XmlElement(name = "gangedSet", type = GangedSet.class) })
	private List<GangedSet> gangedSetList;
	
	/**
	 * 子流程
	 */
	@XmlElementWrapper(name = "subDefinitionList")
	@XmlElements({ @XmlElement(name = "subDefinitions", type = DefinitionXml.class) })
	private List<DefinitionXml> subDefinitionXmlList;

	
	/**
	 * 对应的自定义表单
	 */
	@XmlElementWrapper(name = "formDefXmlList")
	@XmlElements({ @XmlElement(name = "formDefs") })
	private List<?extends BaseFormDefXml> formDefXmlList;
	
	/**
	 * 表单权限List
	 */
	@XmlElementWrapper(name = "formRightsList")
	@XmlElements({ @XmlElement(name = "formRights") })
	private List<? extends BaseFormRights> formRightsList;
	
	/**
	 * 对应的自定义表
	 */
	@XmlElementWrapper(name = "formTableXmlList")
	@XmlElements({ @XmlElement(name = "tables") })
	private List<BaseFormTableXml> formTableXmlList;
	
	/**
	 * 附件或者帮助文档
	 */
	@XmlElementWrapper(name = "sysFileList")
	@XmlElements({ @XmlElement(name = "sysFile") })
	private List<BaseSysFile> sysFileList;
	
	/**
	 * 流程引用
	 */
	@XmlElementWrapper(name = "referDefinitionList")
	@XmlElements({ @XmlElement(name = "referDefinition", type = ReferDefinition.class) })
	private List<ReferDefinition> referDefinitionList;
	
	
	// ==========以下是getting和setting的方法================
	public Definition getDefinition() {
		return definition;
	}

	public void setDefinition(Definition bpmDefinition) {
		this.definition = bpmDefinition;
	}

	public List<DefinitionXml> getDefinitionXmlList() {
		return definitionXmlList;
	}

	public void setDefinitionXmlList(
			List<DefinitionXml> bpmDefinitionXmlList) {
		this.definitionXmlList = bpmDefinitionXmlList;
	}

	public List<DefRights> getDefRightsList() {
		return defRightsList;
	}

	public void setDefRightsList(List<DefRights> bpmDefRightsList) {
		this.defRightsList = bpmDefRightsList;
	}

	public List<NodeRule> getNodeRuleList() {
		return nodeRuleList;
	}

	public void setNodeRuleList(List<NodeRule> bpmNodeRuleList) {
		this.nodeRuleList = bpmNodeRuleList;
	}

	public List<NodeScript> getNodeScriptList() {
		return nodeScriptList;
	}

	public void setNodeScriptList(List<NodeScript> bpmNodeScriptList) {
		this.nodeScriptList = bpmNodeScriptList;
	}

	public List<DefVar> getDefVarList() {
		return defVarList;
	}

	public void setDefVarList(List<DefVar> bpmDefVarList) {
		this.defVarList = bpmDefVarList;
	}

	public List<NodeSign> getNodeSignList() {
		return nodeSignList;
	}

	public void setNodeSignList(List<NodeSign> bpmNodeSignList) {
		this.nodeSignList = bpmNodeSignList;
	}

	public List<NodeMessage> getNodeMessageList() {
		return nodeMessageList;
	}

	public void setNodeMessageList(List<NodeMessage> bpmNodeMessageList) {
		this.nodeMessageList = bpmNodeMessageList;
	}

	public List<NodeSet> getNodeSetList() {
		return nodeSetList;
	}

	public void setNodeSetList(List<NodeSet> bpmNodeSetList) {
		this.nodeSetList = bpmNodeSetList;
	}

	public List<UserCondition> getUserConditionList() {
		return userConditionList;
	}

	public void setUserConditionList(
			List<UserCondition> bpmUserConditionList) {
		this.userConditionList = bpmUserConditionList;
	}

	public List<NodeUser> getNodeUserList() {
		return nodeUserList;
	}

	public void setNodeUserList(List<NodeUser> bpmNodeUserList) {
		this.nodeUserList = bpmNodeUserList;
	}

	public List<NodeUserUplow> getNodeUserUplowList() {
		return nodeUserUplowList;
	}

	public void setNodeUserUplowList(
			List<NodeUserUplow> bpmNodeUserUplowList) {
		this.nodeUserUplowList = bpmNodeUserUplowList;
	}

	public List<NodeButton> getNodeButtonList() {
		return nodeButtonList;
	}

	public void setNodeButtonList(List<NodeButton> bpmNodeButtonList) {
		this.nodeButtonList = bpmNodeButtonList;
	}

	public List<TaskApprovalItems> getTaskApprovalItemsList() {
		return taskApprovalItemsList;
	}

	public void setTaskApprovalItemsList(
			List<TaskApprovalItems> taskApprovalItemsList) {
		this.taskApprovalItemsList = taskApprovalItemsList;
	}

	public List<TaskReminder> getTaskReminderList() {
		return taskReminderList;
	}

	public void setTaskReminderList(List<TaskReminder> taskReminderList) {
		this.taskReminderList = taskReminderList;
	}

	public List<DefinitionXml> getSubDefinitionXmlList() {
		return subDefinitionXmlList;
	}

	public void setSubDefinitionXmlList(
			List<DefinitionXml> subDefinitionXmlList) {
		this.subDefinitionXmlList = subDefinitionXmlList;
	}

	public List<?extends BaseFormDefXml> getFormDefXmlList() {
		return formDefXmlList;
	}

	public void setFormDefXmlList(List<?extends BaseFormDefXml> bpmFormDefXmlList) {
		this.formDefXmlList = bpmFormDefXmlList;
	}

	public List<BaseFormTableXml> getFormTableXmlList() {
		return formTableXmlList;
	}

	public void setFormTableXmlList(List<BaseFormTableXml> bpmFormTableXmlList) {
		this.formTableXmlList = bpmFormTableXmlList;
	}

	public List<? extends IFormRights> getFormRightsList() {
		return formRightsList;
	}

	public void setFormRightsList(List<? extends BaseFormRights> bpmFormRightsList) {
		this.formRightsList = bpmFormRightsList;
	}

	public List<BaseSysFile> getSysFileList() {
		return sysFileList;
	}

	public void setSysFileList(List<BaseSysFile> sysFileList) {
		this.sysFileList = sysFileList;
	}

	public List<GangedSet> getGangedSetList() {
		return gangedSetList;
	}

	public void setGangedSetList(List<GangedSet> bpmGangedSetList) {
		this.gangedSetList = bpmGangedSetList;
	}

	public List<ReferDefinition> getReferDefinitionList() {
		return referDefinitionList;
	}

	public void setReferDefinitionList(List<ReferDefinition> bpmReferDefinitionList) {
		this.referDefinitionList = bpmReferDefinitionList;
	}
	
	
}
