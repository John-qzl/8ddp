package com.cssrc.ibms.core.flow.service.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.activity.util.BpmUtil;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.engine.IScript;
import com.cssrc.ibms.core.flow.dao.DefinitionDao;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.service.NodeUserService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 流程任务人员分配处理类
 * 
 * @author zhulongchao
 *
 */
@Service
public class TaskUserAssignService implements IScript
{
    
    private Logger logger = LoggerFactory.getLogger(TaskUserAssignService.class);
    
    /**
     * 目标节点人员授权绑定 里面存的值为nodeId,List userIds。
     */
    private static ThreadLocal<Map<String, List<TaskExecutor>>> nodeUserMapLocal =
        new ThreadLocal<Map<String, List<TaskExecutor>>>();
    
    /**
     * 表单用户,节点未设置人员时获取该用户列表（所有后续节点用户相同）
     */
    private static ThreadLocal<List<TaskExecutor>> formUsers = new ThreadLocal<List<TaskExecutor>>();
    
    /**
     * 任务执行人。
     */
    private static ThreadLocal<List<TaskExecutor>> taskExecutors = new ThreadLocal<List<TaskExecutor>>();
    
    @Resource
    private NodeUserService nodeUserService;
    
    @Resource
    private DefinitionDao definitionDao;
    
    @Resource
    JdbcDao jdbcDao;
    
    /**
     * 动态设置下一步节点的执行人。
     * 
     * <pre>
     * 	可以设置多个节点，每一个节点对应一组人的ID。
     *  节点数组大小和人员的数组大小必须一致，人员的id使用逗号分隔。
     * </pre>
     * 
     * @param nodeIds
     * @param userIds
     */
    public void setNodeUser(Map<String, List<TaskExecutor>> map)
    {
        nodeUserMapLocal.set(map);
    }
    
    /**
     * 添加节点人员。
     * 
     * @param nodeId
     * @param userIds
     */
    public void addNodeUser(String nodeId, List<TaskExecutor> executors)
    {
        if (BeanUtils.isEmpty(executors))
            return;
        
        Map<String, List<TaskExecutor>> nodeUserMap = nodeUserMapLocal.get();
        if (nodeUserMap == null)
            nodeUserMap = new HashMap<String, List<TaskExecutor>>();
        nodeUserMap.remove(nodeId);
        nodeUserMap.put(nodeId, executors);
        nodeUserMapLocal.set(nodeUserMap);
    }
    
    /**
     * 设置某个节点的执行人。
     * 
     * @param nodeId 节点ID。
     * @param userIds 节点人员使用逗号进行分隔。
     */
    public void addNodeUser(String nodeId, String userIds)
    {
        if (StringUtil.isEmpty(userIds))
            return;
        List<TaskExecutor> executorList = BpmUtil.getTaskExecutors(userIds);
        addNodeUser(nodeId, executorList);
    }
    
    /**
     * 添加节点用户。
     * 
     * @param aryNodeId
     * @param aryUserIds
     */
    public void addNodeUser(String[] aryNodeId, String[] aryUserIds)
    {
        if (BeanUtils.isEmpty(aryUserIds))
            return;
        Map<String, List<TaskExecutor>> nodeUserMap = nodeUserMapLocal.get();
        if (nodeUserMap == null)
            nodeUserMap = new HashMap<String, List<TaskExecutor>>();
        for (int i = 0; i < aryNodeId.length; i++)
        {
            String nodeId = aryNodeId[i];
            String userIds = aryUserIds[i];
            if (userIds == null || userIds.isEmpty())
                continue;
            
            List<TaskExecutor> executorList = BpmUtil.getTaskExecutors(userIds);
            nodeUserMap.put(nodeId, executorList);
        }
        nodeUserMapLocal.set(nodeUserMap);
    }
    
    public Map<String, List<TaskExecutor>> getNodeUserMap()
    {
        return nodeUserMapLocal.get();
    }
    
    public void clearNodeUserMap()
    {
        nodeUserMapLocal.remove();
    }
    
    /**
     * 获取多实例子流程的执行用户集合
     * 
     * <pre>
     * 1.从上下文获取用户。
     * 2.为获取到则从数据库进行获取。
     * 
     * </pre>
     * 
     * @param execution
     * @return
     * @throws Execption
     */
    public List<TaskExecutor> getMultipleUser(ActivityExecution execution)
        throws Exception
    {
        
        String nodeId = execution.getActivity().getId();
        ExecutionEntity executionEnt = (ExecutionEntity)execution;
        
        String multiInstance = (String)executionEnt.getActivity().getProperty("multiInstance");
        String varName = nodeId + "_" + BpmConst.SUBPRO_MULTI_USERIDS;
        // 串行的情况，直接从流程变量中获取。
        if ("sequential".equals(multiInstance))
        {
            List<TaskExecutor> list = (List<TaskExecutor>)execution.getVariable(varName);
            if (list != null)
                return list;
        }
        
        Map<String, FlowNode> nodeMap = NodeCache.getByActDefId(executionEnt.getProcessDefinitionId());
        FlowNode subProcessNode = nodeMap.get(nodeId);
        FlowNode firstNode = subProcessNode.getSubFirstNode();
        FlowNode secondeNode = firstNode.getNextFlowNodes().get(0);
        // 获取任务执行人。
        List<TaskExecutor> userList = getExecutors();
        
        if (BeanUtils.isEmpty(userList) && nodeUserMapLocal.get() != null)
        {
            userList = nodeUserMapLocal.get().get(secondeNode.getNodeId());
        }
        
        // 从数据库获取
        if (BeanUtils.isEmpty(userList))
        {
            String actInstId = execution.getProcessInstanceId();
            Map<String, Object> variables = execution.getVariables();
            String startUserId = variables.get(BpmConst.StartUser).toString();
            String actDefId = execution.getProcessDefinitionId();
            String preTaskUser = UserContextUtil.getCurrentUserId().toString();
            
            userList = nodeUserService
                .getExeUserIds(actDefId, actInstId, secondeNode.getNodeId(), startUserId, preTaskUser, variables);
        }
        
        if (BeanUtils.isEmpty(userList))
        {
            MessageUtil.addMsg("请设置子流程:[" + secondeNode.getNodeName() + "]的人员!");
        }
        logger.debug("userList size:" + userList.size());
        // 人员不为空，并且为串行会签。
        if (BeanUtils.isNotEmpty(userList) && "sequential".equals(multiInstance))
        {
            executionEnt.setVariable(varName, userList);
        }
        
        return userList;
    }
    
    /**
     * 获取多实例外部子流程的用户数据。
     * 
     * <pre>
     * 这个接口由流程引擎调用， 获取并行和串行用户数据。
     * </pre>
     * 
     * @param execution
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<TaskExecutor> getExtSubProcessMultipleUser(ActivityExecution execution)
        throws Exception
    {
        
        String nodeId = execution.getActivity().getId();
        // String nodeName = execution.getCurrentActivityName();
        ExecutionEntity executionEnt = (ExecutionEntity)execution;
        
        String multiInstance = (String)executionEnt.getActivity().getProperty("multiInstance");
        
        String varName = executionEnt.getActivityId() + "_" + BpmConst.SUBPRO_EXT_MULTI_USERIDS;
        // 串行会签。
        if ("sequential".equals(multiInstance))
        {
            List<TaskExecutor> userIds = (List<TaskExecutor>)executionEnt.getParent().getVariable(varName);
            if (userIds != null)
            {
                return userIds;
            }
            
        }
        else
        {
            List<TaskExecutor> userIds = (List<TaskExecutor>)execution.getVariable(varName);
            if (userIds != null)
            {
                return userIds;
            }
            
        }
        
        // 获取流程的节点集合。
        Map<String, FlowNode> nodeMap = NodeCache.getByActDefId(executionEnt.getProcessDefinitionId());
        // 获取子流程节点。
        FlowNode subProcessNode = nodeMap.get(nodeId);
        
        String flowKey = subProcessNode.getAttribute("subFlowKey");
        // 获取子流程第一个流程节点。
        // Map<String, FlowNode> subProcessNodesMap = subProcessNode.getSubProcessNodes();
        
        // FlowNode startNode = NodeCache.getStartNode(subProcessNodesMap);
        // 第二个节点。
        // FlowNode secodeNode = startNode.getNextFlowNodes().get(0);
        
        // 从数据库中获取人员
        {
            
            // String actInstId = execution.getProcessInstanceId();
            // String actDefId=execution.getProcessDefinitionId();
            // Map<String, Object> variables = execution.getVariables();
            // String startUserId = variables.get(BpmConst.StartUser).toString();
            // String preTaskUser = UserContextUtil.getCurrentUserId().toString();
            // 获取子流程的流程定义。
            List<TaskExecutor> dataList = new ArrayList<TaskExecutor>();
            Object subbusinessKey = executionEnt.getProcessInstance().getVariable(BpmConst.SUBFLOW_BUSINESSKEY);
            // 如果bakExtSubBusKey 不为空 则是驳回到子流程，根据选择的业务数据主键来实现启动子例程实例数量
            if (subbusinessKey != null)
            {
                String[] pks = subbusinessKey.toString().split(",");
                for (String pk : pks)
                {
                    TaskExecutor task = new TaskExecutor();
                    task.setBusinessKey(pk);
                    task.setBackSub(true);
                    dataList.add(task);
                }
                executionEnt.setVariable(varName, dataList);
                return dataList;
            }
            Definition bpmDefinition = definitionDao.getByActDefKeyIsMain(flowKey);
            String actDefId = bpmDefinition.getActDefId();
            /*
             * 通过数据个数来决定 子流程的数量,流程task的执行人在 TaskCreateListener 重新根据nodeset 获取
             */
            IProcessRunService processRunService = AppUtil.getBean(IProcessRunService.class);
            IFormTableService formTableService = AppUtil.getBean(IFormTableService.class);
            IFormFieldService formFiledService = AppUtil.getBean(IFormFieldService.class);
            
            // 主流程key
            IProcessRun processRun =
                processRunService.getByActInstanceId(Long.valueOf(executionEnt.getProcessInstanceId()));
            // 主流程form
            IFormDef mainActForm = processRunService.gerFormDefByActDefId(executionEnt.getProcessDefinitionId());
            // 子流程form
            IFormDef subActForm = processRunService.gerFormDefByActDefId(actDefId);
            
            IFormTable subtable = formTableService.getTableById(subActForm.getTableId());
            
            List<? extends IFormField> fileds =
                formFiledService.getRelFieldsByTableId(subActForm.getTableId(), mainActForm.getTableId());
            if (fileds != null && fileds.size() == 1)
            {
                IFormField f = fileds.get(0);
                Map<String, String> param = new HashMap<>();
                String sql = "select ID as \"ID\" from " + subtable.getFactTableName() + " where "
                    + f.getFactFiledName() + "=:" + f.getFactFiledName();
                param.put(f.getFactFiledName(), processRun.getBusinessKey());
                
                List<Map<String, Object>> result = jdbcDao.queryForList(sql, param);
                for (Map<String, Object> dataid : result)
                {
                    String pk = dataid.get("ID").toString();
                    TaskExecutor task = new TaskExecutor();
                    task.setBusinessKey(pk);
                    dataList.add(task);
                }
                logger.debug("userList size:" + dataList.size());
                executionEnt.setVariable(varName, dataList);
                return dataList;
            }
            return null;
            
        }
        
    }
    
    /**
     * 获取会签用户，用于会签节点。
     * 
     * <pre>
     * 在流程定义中
     * &lt;xsl:attribute name="activiti:collection">${taskUserAssignService.getSignUser(execution)}&lt;/xsl:attribute>
     * 1.从上下文指定的用户中获取会签用户。
     * 2.如果获取不到，并且该节点是串行会签的节点，则从会签数据表当中获取。
     * 3.如果再获取不到，那么则从节点配置的数据库中获取用户数据。
     * </pre>
     * 
     * @param execution
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<TaskExecutor> getSignUser(ActivityExecution execution)
        throws Exception
    {
        String nodeId = execution.getActivity().getId();
        String nodeName = (String)execution.getActivity().getProperty("name");
        String multiInstance = (String)execution.getActivity().getProperty("multiInstance");
        
        List<TaskExecutor> userIds = null;
        
        String varName = nodeId + "_" + BpmConst.SIGN_USERIDS;
        // 串行会签人员首先从流程变量中获取。
        if ("sequential".equals(multiInstance))
        {
            userIds = (List<TaskExecutor>)execution.getVariable(varName);
            if (userIds != null)
            {
                return userIds;
            }
        }
        
        Map<String, List<TaskExecutor>> nodeUserMap = nodeUserMapLocal.get();
        
        // 会签任务用户来自前台的用户选择
        if (nodeUserMap != null && BeanUtils.isNotEmpty(nodeUserMap.get(nodeId)))
        {
            userIds = nodeUserMap.get(nodeId);
            saveExecutorVar(execution, userIds);
            return userIds;
        }
        
        userIds = getExecutors();
        
        if (BeanUtils.isNotEmpty(userIds))
        {
            saveExecutorVar(execution, userIds);
            addNodeUser(nodeId, userIds);
            return userIds;
        }
        // 从数据库配置中获取。
        ExecutionEntity ent = (ExecutionEntity)execution;
        String actDefId = ent.getProcessDefinitionId();
        // 获取发起用户。
        String actInstId = execution.getProcessInstanceId();
        Map<String, Object> variables = execution.getVariables();
        String startUserId = variables.get(BpmConst.StartUser).toString();
        String preTaskUser = UserContextUtil.getCurrentUserId().toString();
        List<TaskExecutor> list =
            nodeUserService.getExeUserIds(actDefId, actInstId, nodeId, startUserId, preTaskUser, variables);
        
        if (BeanUtils.isEmpty(list))
        {
            MessageUtil.addMsg("请设置会签节点:[" + nodeName + "]的人员!");
        }
        if (BeanUtils.isNotEmpty(list))
        {
            saveExecutorVar(execution, list);
        }
        addNodeUser(nodeId, list);
        return list;
        
    }
    
    /**
     * 
     * @param execution
     * @param userIds
     */
    private void saveExecutorVar(ActivityExecution execution, List<TaskExecutor> userIds)
    {
        String multiInstance = (String)execution.getActivity().getProperty("multiInstance");
        if ("sequential".equals(multiInstance))
        {
            String nodeId = execution.getActivity().getId();
            String varName = nodeId + "_" + BpmConst.SIGN_USERIDS;
            execution.setVariable(varName, userIds);
        }
        
    }
    
    /**
     * 设置的任务执行人。
     * 
     * @param users 用户ID使用逗号分隔。
     */
    public void setExecutors(String users)
    {
        if (StringUtil.isEmpty(users))
        {
            return;
        }
        String[] aryUsers = users.split(",");
        List<TaskExecutor> list = new ArrayList<TaskExecutor>();
        for (String userId : aryUsers)
        {
            TaskExecutor executor = new TaskExecutor(userId);
            list.add(executor);
        }
        taskExecutors.set(list);
    }
    
    /**
     * 设置设置任务执行人。
     * 
     * @param users 设置任务执行人。
     */
    public void setExecutors(List<TaskExecutor> users)
    {
        taskExecutors.set(users);
    }
    
    /**
     * 获取任务执行人。
     * 
     * @return
     */
    public List<TaskExecutor> getExecutors()
    {
        return taskExecutors.get();
    }
    
    /**
     * 清除执行人。
     */
    public void clearExecutors()
    {
        taskExecutors.remove();
    }
    
    /**
     * 清除所有的线程变量。
     */
    public static void clearAll()
    {
        formUsers.remove();
        taskExecutors.remove();
        nodeUserMapLocal.remove();
    }
    
}
