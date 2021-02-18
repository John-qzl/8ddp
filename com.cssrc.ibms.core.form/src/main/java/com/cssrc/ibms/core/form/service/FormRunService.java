package com.cssrc.ibms.core.form.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IDefinitionService;
import com.cssrc.ibms.api.activity.intf.INodeSetService;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.activity.model.IFlowNode;
import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormRunService;
import com.cssrc.ibms.api.form.model.IFormRun;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.form.dao.FormRunDao;
import com.cssrc.ibms.core.form.model.FormRun;


/**
 * 对象功能:流程表单运行情况 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class FormRunService extends BaseService<FormRun> implements IFormRunService
{
	@Resource
	private FormRunDao dao;
	@Resource
	private INodeSetService nodeSetService;
	@Resource
	private IDefinitionService definitionService;
	
	public FormRunService()
	{
	}
	
	@Override
	protected IEntityDao<FormRun, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 添加表单运行情况。
	 * <pre>
	 * 将当前最新的表单配置信息添加到表单运行情况。
	 * 之后的流程表单从表单运行情况表中取值。
	 * </pre>
	 * @param actDefId	表单定义ID
	 * @param runId		process runID
	 * @param actInstanceId		流程实例ID
	 * @throws Exception
	 */
	public void addFormRun(String actDefId,Long runId,String actInstanceId) {
	    List<? extends INodeSet> list=nodeSetService.getOnlineFormByActDefId(actDefId);
	    for(INodeSet bpmNodeSet:list){
	    	FormRun bpmFormRun=getByNodeSet(runId, actInstanceId, bpmNodeSet);
	    	dao.add(bpmFormRun);
	    }
	}
	
	/**
	 * 获取默认或起始的流程表单。
	 * @param list
	 * @param setType
	 * @return
	 */
	private INodeSet getDefalutStartForm(List<INodeSet> list,Short setType){
		INodeSet bpmNodeSet=null;
		for(INodeSet node:list){
			if(node.getSetType().equals(setType)){
				bpmNodeSet=node;
				break;
			}
		}
		return bpmNodeSet;
	}
	
	
	/**
	 * 获取开始节点的表单运行情况。
	 * @param list
	 * @param setType
	 * @return
	 */
	private INodeSet getGlobalForm(List<INodeSet> list){
		INodeSet bpmNodeSet =getDefalutStartForm(list,INodeSet.SetType_GloabalForm);
		return bpmNodeSet;
	}
	
	/**
	 * 获取节点运行的form列表。
	 * @param list
	 * @return
	 */
	public Map<String, INodeSet> getTaskForm(List<INodeSet> list ){
		Map<String, INodeSet> map=new HashMap<String, INodeSet>();
		for(INodeSet node:list){
			if(node.getSetType().equals(INodeSet.SetType_TaskNode)){
				map.put(node.getNodeId(), node);
			}
		}
		return map;
	}
	
	
	/**
	 * 根据NodeSet对象构建FormRun对象。
	 * @param actDefId		流程定义id。
	 * @param runId			流程运行ID。
	 * @param actInstanceId	流程实例ID。
	 * @param bpmNodeSet	 流程节点对象。
	 * @return
	 * @throws Exception
	 */
	private FormRun getByNodeSet(Long runId,String actInstanceId,INodeSet bpmNodeSet) {
		FormRun bpmFormRun=new FormRun();
		bpmFormRun.setId(UniqueIdUtil.genId());
		bpmFormRun.setRunId(runId);
		bpmFormRun.setActInstanceId(actInstanceId);
		bpmFormRun.setActDefId(bpmNodeSet.getActDefId());
		bpmFormRun.setActNodeId(bpmNodeSet.getNodeId());
		bpmFormRun.setFormdefId(bpmNodeSet.getFormDefId());
		bpmFormRun.setFormdefKey(bpmNodeSet.getFormKey());
		bpmFormRun.setFormType(bpmNodeSet.getFormType());
		bpmFormRun.setFormUrl(bpmNodeSet.getFormUrl());
		bpmFormRun.setSetType(bpmNodeSet.getSetType()); 
		return bpmFormRun;
	}
	
	
	/**
	 * 获取启动节点信息
	 * @param actDefId
	 * @param toFirstNode
	 * @return
	 * @throws Exception 
	 */
	public INodeSet getStartNodeSet(Long defId,String actDefId) throws Exception{
		IFlowNode flowNode =nodeSetService.getFirstNodeIdFromCache(actDefId);
		String nodeId=flowNode.getNodeId();
		INodeSet globalNodeSet= nodeSetService.getByStartGlobal(defId);
		INodeSet firstNodeSet=nodeSetService.getByActDefIdNodeId(actDefId, nodeId);
		if(firstNodeSet!=null && !INodeSet.FORM_TYPE_NULL.equals(firstNodeSet.getFormType())){
			return firstNodeSet;
		}
		if(globalNodeSet!=null && !INodeSet.FORM_TYPE_NULL.equals(globalNodeSet.getFormType())){
			return globalNodeSet;
		}
		
		return null;
		
	}
	
	/**
	 * 判断是否可以直接启动。
	 * <pre>
	 * 	
	 * </pre>
	 * @param defId
	 * @return
	 */
	public boolean getCanDirectStart(Long defId){
		IDefinition definition=definitionService.getById(defId);
		Integer directStart=definition.getDirectstart();
		if(directStart==null)
			return true;
	
		return  directStart.intValue()==1;
		
	}
	
	
	/**
	 * 取得流程运行表单情况。
	 * @param actInstanceId
	 * @param actNodeId
	 * @return
	 */
	public FormRun getByInstanceAndNode(String actInstanceId,String actNodeId){
		//根据流程实例id和任务节点id获取表单。
		FormRun formRun=  dao.getByInstanceAndNode(actInstanceId, actNodeId);
		if(formRun!=null && formRun.getFormType()!=null && formRun.getFormType()!=-1){
			return formRun;
		}
		else{
			//没有获取到则再获取全局表单。
			formRun=dao.getGlobalForm(actInstanceId);
			if(formRun!=null && formRun.getFormType()!=null && formRun.getFormType()!=-1){
				return formRun;
			}
			return null;
		}
	}
	
	
	/**
	 * 取得流程运行表单情况，与getByInstanceAndNode不同，此方法不对取得的表单做任务判断和处理。
	 * @param actInstanceId
	 * @param actNodeId
	 * @return
	 */
	public FormRun getByInstanceAndNodeId(String actInstanceId,String actNodeId){
		//根据流程实例id和任务节点id获取表单。
		FormRun formRun=  dao.getByInstanceAndNode(actInstanceId, actNodeId);
		return formRun;
	}
	
	/**
	 * 根据流程实例ID，流程实例的运行表单列表
	 * @param actInstanceId
	 * @return
	 */
	public List<? extends IFormRun> getByInstanceId(String actInstanceId){
		return dao.getByInstanceId(actInstanceId);		
	}

	/**
	 * 根据流程实例删除数据。
	 * @param actInstanceId
	 */
	@Override
	public int delByInstanceId(String actInstanceId) {
		return dao.delBySqlKey("delByInstanceId", actInstanceId);
	}
	/**
	 * 根据act流程定义Id删除数据
	 * @param actDefId
	 */
	@Override
	public int delByActDefId(String actDefId) {
		return dao.delBySqlKey("delByActDefId", actDefId);
	}



}


