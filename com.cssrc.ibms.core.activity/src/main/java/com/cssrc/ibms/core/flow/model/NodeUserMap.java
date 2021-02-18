package com.cssrc.ibms.core.flow.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cssrc.ibms.core.activity.model.TaskExecutor;



/**
 * 流程节点Id及节点用户列表的映射实体类
 * @author zhulongchao
 *
 */
public class NodeUserMap
{	
	//任务节点定义key
	private String nodeId;
	//任务节点名
	private String nodeName;
	//bpm_node_set表主键
	private Long setId;
	//节点人员映射列表
	private List<NodeUser> nodeUserList;
	//节点人员
	private Set<TaskExecutor> taskExecutors=new HashSet<TaskExecutor>();
	
	//是否为多实例节点
	private boolean isMultipleInstance;
	//节点人员映射列表
	private List<UserCondition> userConditionList;
	public static short CHOOICETYPE_NO=0;		//不能指定跳转路径
	public static short CHOOICETYPE_RADIO=1;	//指定分支条件的跳转路径（单选）
	public static short CHOOICETYPE_CHECK=2;	//指定条件同步的跳转路径（复选）
	//指定跳转路径类型
	private short chooiceType=0;	
	
	public short getChooiceType() {
		return chooiceType;
	}

	public void setChooiceType(short chooiceType) {
		this.chooiceType = chooiceType;
	}

	public NodeUserMap()
	{
		// TODO Auto-generated constructor stub
	}
	
	public NodeUserMap(Long setId,String nodeId,String nodeName,List<NodeUser> nodeUserList)
	{
		this.setId=setId;
		this.nodeId=nodeId;
		this.nodeName=nodeName;
		this.nodeUserList=nodeUserList;
	}
	
	public NodeUserMap(String nodeId,String nodeName,Set<TaskExecutor> taskExecutors){
		this.nodeId=nodeId;
		this.nodeName=nodeName;
		this.taskExecutors=taskExecutors;
	}
	
	
	public NodeUserMap(String nodeId,String nodeName,Set<TaskExecutor> taskExecutors,Boolean isMultipleInstance){
		this.nodeId=nodeId;
		this.nodeName=nodeName;
		this.taskExecutors=taskExecutors;
		this.isMultipleInstance=isMultipleInstance;
	}
	

	public String getNodeId()
	{
		return nodeId;
	}
	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}
	public String getNodeName()
	{
		return nodeName;
	}
	public void setNodeName(String nodeName)
	{
		this.nodeName = nodeName;
	}
	public List<NodeUser> getNodeUserList()
	{
		return nodeUserList;
	}
	public void setNodeUserList(List<NodeUser> nodeUserList)
	{
		this.nodeUserList = nodeUserList;
	}

	public Long getSetId()
	{
		return setId;
	}

	public void setSetId(Long setId)
	{
		this.setId = setId;
	}

	public Set<TaskExecutor> getTaskExecutors() {
		return taskExecutors;
	}

	public void setTaskExecutors(Set<TaskExecutor> users) {
		this.taskExecutors = users;
	}

	public boolean isMultipleInstance() {
		return isMultipleInstance;
	}

	public void setMultipleInstance(boolean isMultipleInstance) {
		this.isMultipleInstance = isMultipleInstance;
	}
	public List<UserCondition> getUserConditionList() {
		return userConditionList;
	}

	public void setUserConditionList(List<UserCondition> bpmUserConditionList) {
		this.userConditionList = bpmUserConditionList;
	}
	
}
