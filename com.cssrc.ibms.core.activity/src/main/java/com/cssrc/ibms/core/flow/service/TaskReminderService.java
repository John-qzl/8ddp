package com.cssrc.ibms.core.flow.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.activity.intf.ITaskReminderService;
import com.cssrc.ibms.api.activity.model.IProcessTask;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.system.intf.worktime.ICalendarAssignService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.flow.dao.TaskReminderDao;
import com.cssrc.ibms.core.flow.model.TaskReminder;
import com.cssrc.ibms.core.flow.model.TaskWarningSet;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.WarningSetting;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:任务节点催办时间设置 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class TaskReminderService extends BaseService<TaskReminder> implements ITaskReminderService
{
    @Resource
    private TaskReminderDao dao;
    
    @Resource
    ISysParameterService sysParameterService;
    
    @Resource
    ICalendarAssignService calendarAssignService;
    
    @Resource
    GroovyScriptEngine groovyScriptEngine;
    
    @Resource
    RuntimeService runtimeService;
    
    public TaskReminderService()
    {
    }
    
    @Override
    protected IEntityDao<TaskReminder, Long> getEntityDao()
    {
        return dao;
    }
    
    /**
     * 根据流程定义Id和节点Id获取催办信息。
     * @param actDefId
     * @param nodeId
     * @return
     */
    public List<TaskReminder> getByActDefAndNodeId(String actDefId, String nodeId)
    {
        return dao.getByActDefAndNodeId(actDefId, nodeId);
    }
    
    /**
     * 根据流程定义Id获取催办信息。
     * @param actDefId
     * @return
     */
    public List<TaskReminder> getByActDefId(String actDefId)
    {
        return dao.getByActDefId(actDefId);
    }
    
    /**
     * 判断节点是否已经定义催办信息。
     * @param actDefId
     * @param nodeId
     * @return
     */
    public boolean isExistByActDefAndNodeId(String actDefId, String nodeId)
    {
        Integer rtn = dao.isExistByActDefAndNodeId(actDefId, nodeId);
        return rtn > 0;
    }
    
    /**
     * 保存催办信息。
     * @param taskReminder
     */
    @Override
    public void add(TaskReminder taskReminder)
    {
        this.dao.add(taskReminder);
    }
    
    /**
     * 保存催办信息。
     * @param taskReminder
     */
    @Override
    public void update(TaskReminder taskReminder)
    {
        this.dao.update(taskReminder);
    }
    
    /**
     * @author Yangbo 2016-7-22 
     * @return
     */
    public List<WarningSetting> getWaringSettingList()
    {
        String json = sysParameterService.getByAlias("task.WarnLevel");
        if (StringUtil.isEmpty(json))
            return Collections.emptyList();
        List warningSettingList = JSONObject.parseArray(json, WarningSetting.class);
        return warningSettingList;
    }
    
    /**
     * @author Yangbo 2016-7-22
     * @return
     */
    public Map<Integer, WarningSetting> getWaringSetMap()
    {
        Map map = new HashMap();
        map.put(Integer.valueOf(50), new WarningSetting(Integer.valueOf(50), "普通", "blue"));
        
        List<WarningSetting> warningSettingList = getWaringSettingList();
        if (BeanUtils.isEmpty(warningSettingList))
            return map;
        for (WarningSetting set : warningSettingList)
        {
            map.put(set.getLevel(), set);
        }
        return map;
    }
    
    @Override
    public TaskWarningSet getWarningSet(IProcessTask task, Date curDate)
    {
        
        List<TaskReminder> reminders =
            this.getByActDefAndNodeId(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
        int level = -999;
        TaskWarningSet taskWset = null;
        for (TaskReminder r : reminders)
        {
            if (r.getTaskWarningSetList() == null)
            {
                continue;
            }
            for (TaskWarningSet wset : r.getTaskWarningSetList())
            {
                if (wset.getLevel() < level)
                {
                    continue;
                }
                String actInsId = task.getProcessInstanceId();
                String nodeId = task.getTaskDefinitionKey();
                int relType = r.getRelativeNodeType();
                Date baseTime = calendarAssignService.getRelativeStartTime(actInsId, nodeId, relType);
                
                Integer deadLine = wset.getRelativeDueTime();
                Date deadDate = null;
                String conditionExp = r.getCondExp();
                if (StringUtil.isNotEmpty(conditionExp))
                {
                    // 如果条件表达式不为空，那么通过条件表达式来判断是否需要报警
                    Map<String, Object> vars = this.getVars(task);
                    vars.put("taskUserId", UserContextUtil.getCurrentUserId());
                    vars.put("_taskCreateTime", task.getCreateTime());
                    try
                    {
                        boolean result = groovyScriptEngine.executeBoolean(conditionExp, vars);
                        if (result)
                        {
                            level = wset.getLevel();
                            taskWset = wset;
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                    }
                    
                }
                else
                {
                    if (new Integer(1).equals(r.getRelativeTimeType()))
                    {
                        deadDate = new Date(TimeUtil.getNextTime(1, deadLine, baseTime.getTime()));
                    }
                    else
                    {
                        deadDate = calendarAssignService
                            .getTaskTimeByUser(baseTime, deadLine, UserContextUtil.getCurrentUserId());
                    }
                    
                    if (TaskWarningSet.RELATIVE_TYPE_BEFOR.equals(wset.getRelativeType()))
                    {
                        // 在任务相对时间之前
                        if (curDate.compareTo(deadDate) < 0)
                        {
                            taskWset = wset;
                            level = wset.getLevel();
                        }
                    }
                    else if (TaskWarningSet.RELATIVE_TYPE_AFTER.equals(wset.getRelativeType()))
                    {
                        // 在任务相对时间之后
                        if (deadDate.compareTo(curDate) < 0)
                        {
                            taskWset = wset;
                            level = wset.getLevel();
                        }
                    }
                }
                
            }
        }
        
        if (level > -999)
        {
            return taskWset;
        }
        else
        {
            return null;
        }
        
    }
    
    public Map<String, Object> getVars(IProcessTask task)
    {
        RuntimeService runtimeService = AppUtil.getBean(RuntimeService.class);
        TaskServiceImpl taskService = AppUtil.getBean(TaskServiceImpl.class);
        TaskEntity taskEntity = (TaskEntity)taskService.createTaskQuery().taskId(task.getId()).singleResult();
        Map<String, Object> vars = runtimeService.getVariables(task.getProcessInstanceId());
        try
        {
            ExecutionEntity curExecution = (ExecutionEntity)runtimeService.createExecutionQuery()
                .processInstanceId(taskEntity.getProcessInstanceId())
                .singleResult();
            ExecutionEntity superExecution = (ExecutionEntity)runtimeService.createExecutionQuery()
                .executionId(curExecution.getSuperExecutionId())
                .singleResult();
            Map<String, Object> superVars = runtimeService.getVariables(superExecution.getProcessInstanceId());
            Map<String, Object> variables = marginVariables(superVars, vars);
            variables.put("_taskCreateTime", task.getCreateTime());
            return variables;
        }
        catch (Exception e)
        {
            vars.put("_taskCreateTime", task.getCreateTime());
            return vars;
        }
        
    }
    
    private Map<String, Object> marginVariables(Map<String, Object> parentVariables,
        Map<String, Object> curentVariables)
    {
        Map<String, Object> variables = new HashMap<String, Object>();
        String putKey = "";
        for (String key : curentVariables.keySet())
        {
            if (key.indexOf(".") > -1)
            {
                putKey = key.split("\\.")[1];
            }
            else
            {
                putKey = key;
            }
            if (!variables.containsKey(putKey))
            {
                variables.put(putKey, curentVariables.get(key));
            }
            
        }
        for (String key : parentVariables.keySet())
        {
            if (key.indexOf(".") > -1)
            {
                putKey = key.split("\\.")[1];
            }
            else
            {
                putKey = key;
            }
            if (!variables.containsKey(putKey))
            {
                variables.put(putKey, parentVariables.get(key));
            }
        }
        
        return variables;
    }
}
