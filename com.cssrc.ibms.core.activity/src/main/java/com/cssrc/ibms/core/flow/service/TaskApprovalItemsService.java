package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.TaskApprovalItemsDao;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.TaskApprovalItems;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:常用语管理 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class TaskApprovalItemsService extends BaseService<TaskApprovalItems>
{
	@Resource
	private TaskApprovalItemsDao dao;
	
	@Resource
	private IGlobalTypeService globalTypeService;
	
	public TaskApprovalItemsService()
	{
	}
	
	@Override
	protected IEntityDao<TaskApprovalItems, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 根据流程定义ID取常用语
	 * @param defId
	 * @return
	 */
	public TaskApprovalItems getFlowApproval(String actDefId, int isGlobal){
		
		return dao.getFlowApproval(actDefId, isGlobal);
	}
	
	/**
	 * 根据节点ID取常用语
	 * @param nodeId
	 * @return
	 */
	public TaskApprovalItems getTaskApproval(String actDefId, String nodeId, int isGlobal){
		
		return dao.getTaskApproval(actDefId, nodeId, isGlobal);
	}
	
	/**
	 * actDefId删除流程常用语
	 * @return
	 */
	public void delFlowApproval(String actDefId, int isGlobal){
		
		dao.delFlowApproval(actDefId, isGlobal);
	}
	
	/**
	 * actDefId, nodeId删除节点常用语
	 * @return
	 */
	public void delTaskApproval(String actDefId, String nodeId, int isGlobal){
		
		dao.delTaskApproval(actDefId, nodeId, isGlobal);
	}
	
	/**
	 * 添加常用语
	 * @param exp
	 * @param isGlobal
	 * @param actDefId
	 * @param setId
	 * @param nodeId
	 * @throws Exception
	 */
	public void addTaskApproval(String exp, Short type, String typeIdArr, String defKeyArr,TaskApprovalItems taskApprovalItems, Long curUserId) throws Exception{
		
		String[] expressions = exp.split("\n");
	    TaskApprovalItems taItem = null;
	    if ((type.shortValue() == 1) || (type.shortValue() == 4)) {
	      for (String expression : expressions)
	        if (StringUtil.isNotEmpty(expression)) { 
	          taItem = new TaskApprovalItems();
	          taItem.setItemId(Long.valueOf(UniqueIdUtil.genId()));
	          taItem.setType(type);
	          taItem.setExpression(expression);
	          taItem.setUserId(curUserId);
	          this.dao.add(taItem);
	        }
	    }
	    else if (type.shortValue() == 2) {
	      String[] typeIds = typeIdArr.split(",");
	      for (String typeId : typeIds) {
	        for (String expression : expressions)
	          if (StringUtil.isNotEmpty(expression)) { 
	            taItem = new TaskApprovalItems();
	            taItem.setItemId(Long.valueOf(UniqueIdUtil.genId()));
	            taItem.setTypeId(Long.valueOf(Long.parseLong(typeId)));
	            taItem.setType(type);
	            taItem.setExpression(expression);
	            taItem.setUserId(curUserId);
	            this.dao.add(taItem);
	          }
	      }
	    }
	    else if (type.shortValue() == 3) {
	      String[] defKeys = defKeyArr.split(",");
	      for (String defKey : defKeys)
	        for (String expression : expressions)
	          if (StringUtil.isNotEmpty(expression)) { 
	            taItem = new TaskApprovalItems();
	            taItem.setItemId(Long.valueOf(UniqueIdUtil.genId()));
	            taItem.setDefKey(defKey);
	            taItem.setType(type);
	            taItem.setExpression(expression);
	            taItem.setUserId(curUserId);
	            this.dao.add(taItem);
	          }
        }
        else if (type.shortValue() == 5)
        {
            // 根据流程节点设置的审批意见模板
            String[] defNodeKeys = taskApprovalItems.getDefNodeKey().split(",");
            for (String defNode : defNodeKeys)
            {
                if (StringUtil.isNotEmpty(exp))
                {
                    taItem = new TaskApprovalItems();
                    taItem.setItemId(Long.valueOf(UniqueIdUtil.genId()));
                    taItem.setDefNodeKey(defNode);
                    taItem.setType(type);
                    taItem.setCode(taskApprovalItems.getCode());
                    taItem.setDefault_(taskApprovalItems.getDefault_());
                    taItem.setExpression(exp);
                    taItem.setUserId(curUserId);
                    this.dao.add(taItem);
                }
            }
            
        }
	}
	
	/**
	 * 取流程常用语。
	 * @param actDefId	流程定义ID。
	 * @param nodeId	活动节点ID。
	 * @return
	 */
	public List<String> getApprovalByActDefId(String defKey, Long typeId){
		List<String> taskAppItemsList = new ArrayList<String>();
	    Long curUserId = UserContextUtil.getCurrentUserId();

	    List<TaskApprovalItems> taskAppItem1 = dao.getByDefKeyAndUserAndSys(defKey, curUserId);

	    List<TaskApprovalItems> taskAppItem2 = this.dao.getByType(TaskApprovalItems.TYPE_FLOWTYPE);

	    if (BeanUtils.isNotEmpty(taskAppItem1)) {
	      for (TaskApprovalItems taskAppItem : taskAppItem1) {
	        taskAppItemsList.add(taskAppItem.getExpression());
	      }
	    }
	    if (BeanUtils.isNotEmpty(taskAppItem2))
	    {
	      String typeIdPath = globalTypeService.getById(typeId).getNodePath();
	      String[] typeIds = typeIdPath.split("\\.");
	      for (int i = 1; i < typeIds.length; i++) {
	        for (TaskApprovalItems taskAppItem : taskAppItem2) {
	          if (taskAppItem.getTypeId().toString().equals(typeIds[i])) {
	            taskAppItemsList.add(taskAppItem.getExpression());
	          }
	        }
	      }
	    }

	    removeDuplicate(taskAppItemsList);
	    return taskAppItemsList;
	}
	
	
	/**
	 * 根据流程定义ID取常用语
	 * 
	 * @param actDefId 流程定义ID
	 * @return
	 */
	public List<TaskApprovalItems> getByActDefId(String actDefId) {
		return dao.getByActDefId(actDefId);
	}
	
	public List<String> getApprovalByDefKeyAndTypeId(String defKey, Long typeId) {
		List<String> taskAppItemsList = new ArrayList<String>();
		Long curUserId = UserContextUtil.getCurrentUserId();

		List<TaskApprovalItems> taskAppItem1 = dao.getByDefKeyAndUserAndSys(defKey, curUserId);

		List<TaskApprovalItems> taskAppItem2 = dao.getByType(TaskApprovalItems.TYPE_FLOWTYPE);

		if (BeanUtils.isNotEmpty(taskAppItem1)) {
			for (TaskApprovalItems taskAppItem : taskAppItem1) {
				taskAppItemsList.add(taskAppItem.getExpression());
			}
		}
		if (BeanUtils.isNotEmpty(taskAppItem2)) {
			String typeIdPath = globalTypeService.getById(typeId).getNodePath();
			String[] typeIds = typeIdPath.split("\\.");
			for (int i = 1; i < typeIds.length; i++) {
				for (TaskApprovalItems taskAppItem : taskAppItem2) {
					if (taskAppItem.getTypeId().toString().equals(typeIds[i])) {
						taskAppItemsList.add(taskAppItem.getExpression());
					}
				}
			}
		}

		removeDuplicate(taskAppItemsList);
		return taskAppItemsList;
	}
	
	  private void removeDuplicate(List<?> list){
	    HashSet h = new HashSet(list);
	    list.clear();
	    list.addAll(h);
	  }

    /**
     * 读取审批意见模板，目前是从常用语设置中读取的
     * @param bpmDefinition
     * @param taskEntity
     * @return
     */
    public List<TaskApprovalItems> getSpyjModel(Definition bpmDefinition, TaskEntity taskEntity)
    {
        String taskKey=taskEntity.getTaskDefinitionKey();
        String defKey=bpmDefinition.getDefKey();
        List<TaskApprovalItems> list=this.dao.getByDefNodeKey(defKey+":"+taskKey);
       return list;
    }

    public String getSpyjModel(Definition bpmDefinition, TaskOpinion taskOpinion)
    {
        if(taskOpinion==null){
            return null;
        }
        String taskKey=taskOpinion.getTaskKey();
        String defKey=bpmDefinition.getDefKey();
        List<TaskApprovalItems> list=this.dao.getByDefNodeKey(defKey+":"+taskKey);
        if(list!=null&&list.size()>0){
            return list.get(0).getExpression();
        }else{
            return null;
        }
    }
}
