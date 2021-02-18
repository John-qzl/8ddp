package com.cssrc.ibms.core.activity.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.activity.intf.ITaskReminderService;
import com.cssrc.ibms.api.activity.model.IProcessTask;
import com.cssrc.ibms.api.activity.model.ITaskReminder;
import com.cssrc.ibms.api.system.intf.worktime.ICalendarAssignService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.flow.model.ExecutionStack;
import com.cssrc.ibms.core.flow.service.ExecutionStackService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 流程任务。
 * 
 * @author zhulongchao
 *
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class ProcessTask implements IProcessTask, Serializable
{
    // 任务ID
    private String id;
    
    // 任务名称
    private String name;
    
    // 流程事项标题
    private String subject;
    
    // 父任务Id
    private String parentTaskId;
    
    // 任务描述
    private String description;
    
    // 任务优先级
    private String priority;
    
    // 创建时间
    private Date createTime;
    
    // 任务持有人
    private String owner;
    
    // 任务执行人
    private String assignee;
    
    // 代理状态
    private String delegationState;
    
    // 执行ID
    private String executionId;
    
    // 流程实例ID
    private String processInstanceId;
    
    // 流程定义ID
    private String processDefinitionId;
    
    // 流程任务定义Key
    private String taskDefinitionKey;
    
    // 到期时间
    private Date dueDate;
    
    // 修正版本
    private Integer revision;
    
    // 流程定义名称
    private String processName;
    
    // 流程任务url
    private String taskUrl;
    
    // 状态
    private String status;
    
    private String type;
    
    // 是否允许转办
    private Integer allowDivert;
    
    // 是否查看
    private Integer ischeck;
    
    // 流程编号
    private Long defId;
    
    private Integer allowBatchApprove;
    
    // runId
    private Long runId;
    
    // add by jc
    
    private Long typeId;
    
    // 类型名称
    private String typeName;
    
    // 组织名称
    private String orgName;
    
    // 标签编号
    private String tagIds;
    
    private Long creatorId;
    
    private String creator;
    
    /**
     * 是否代理任务
     */
    private Boolean isAgent = false;
    
    /**
     * 是否转办任务
     */
    private Boolean isDivert = false;
    
    /**
     * 是否已经查看
     */
    private Short taskStatus;
    
    private String codebefore;
    
    /**
     * 是否已读。
     */
    private Integer hasRead = 0;
    
    /**
     * 到期日期（表单中没有该字段，只是用于临时存储某些属性）
     */
    protected Date expireDate;
    
    /**
     * 是否已催办，1表示已超期，0表示没有（表单中没有该字段，只是用于临时存储某些属性）
     */
    protected int isReminder = 0;
    
    /**
     * 当前任务节点上一步任务节点执行人。 （表单中没有该字段，只是用于临时存储某些属性）
     */
    protected ISysUser preNodeUser = null;
    
    /**
     * 当前任务节点上一步任务节点。 （表单中没有该字段，只是用于临时存储某些属性）
     */
    protected ExecutionStack executionStack = null;
    
    
    /** 
    * @Fields warnSet : TODO(任务预警信息) 
    */ 
    protected String warnSet;
    
    public Date getExpireDate()
    {
        return expireDate;
    }
    
    public void setExpireDate(Date expireDate)
    {
        this.expireDate = expireDate;
    }
    
    public int getIsReminder()
    {
        return isReminder;
    }
    
    public void setIsReminder(int isReminder)
    {
        this.isReminder = isReminder;
    }
    
    public ProcessTask()
    {
        
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getParentTaskId()
    {
        return parentTaskId;
    }
    
    public void setParentTaskId(String parentTaskId)
    {
        this.parentTaskId = parentTaskId;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public String getPriority()
    {
        return priority;
    }
    
    public void setPriority(String priority)
    {
        this.priority = priority;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    public String getOwner()
    {
        return owner;
    }
    
    public void setOwner(String owner)
    {
        this.owner = owner;
    }
    
    public String getAssignee()
    {
        return assignee;
    }
    
    public void setAssignee(String assignee)
    {
        this.assignee = assignee;
    }
    
    public String getDelegationState()
    {
        return delegationState;
    }
    
    public void setDelegationState(String delegationState)
    {
        this.delegationState = delegationState;
    }
    
    public String getExecutionId()
    {
        return executionId;
    }
    
    public void setExecutionId(String executionId)
    {
        this.executionId = executionId;
    }
    
    public String getProcessInstanceId()
    {
        return processInstanceId;
    }
    
    public void setProcessInstanceId(String processInstanceId)
    {
        this.processInstanceId = processInstanceId;
    }
    
    public String getProcessDefinitionId()
    {
        return processDefinitionId;
    }
    
    public void setProcessDefinitionId(String processDefinitionId)
    {
        this.processDefinitionId = processDefinitionId;
    }
    
    public String getTaskDefinitionKey()
    {
        return taskDefinitionKey;
    }
    
    public void setTaskDefinitionKey(String taskDefinitionKey)
    {
        this.taskDefinitionKey = taskDefinitionKey;
    }
    
    public Date getDueDate()
    {
        return dueDate;
    }
    
    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }
    
    public String getSubject()
    {
        return subject;
    }
    
    public void setSubject(String subject)
    {
        this.subject = subject;
    }
    
    public Integer getRevision()
    {
        return revision;
    }
    
    public void setRevision(Integer revision)
    {
        this.revision = revision;
    }
    
    public String getProcessName()
    {
        return processName;
    }
    
    public void setProcessName(String processName)
    {
        this.processName = processName;
    }
    
    public String getTaskUrl()
    {
        return taskUrl;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public Integer getAllowDivert()
    {
        return allowDivert;
    }
    
    public void setAllowDivert(Integer allowDivert)
    {
        this.allowDivert = allowDivert;
    }
    
    public Integer getIscheck()
    {
        return ischeck;
    }
    
    public void setIscheck(Integer ischeck)
    {
        this.ischeck = ischeck;
    }
    
    public Long getDefId()
    {
        return defId;
    }
    
    public void setDefId(Long defId)
    {
        this.defId = defId;
    }
    
    public Integer getAllowBatchApprove()
    {
        return allowBatchApprove;
    }
    
    public void setAllowBatchApprove(Integer allowBatchApprove)
    {
        this.allowBatchApprove = allowBatchApprove;
    }
    
    public Long getRunId()
    {
        return runId;
    }
    
    public void setRunId(Long runId)
    {
        this.runId = runId;
    }
    
    public Long getTypeId()
    {
        return typeId;
    }
    
    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }
    
    public String getTypeName()
    {
        return typeName;
    }
    
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
    
    public String getOrgName()
    {
        return orgName;
    }
    
    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }
    
    public String getTagIds()
    {
        return tagIds;
    }
    
    public void setTagIds(String tagIds)
    {
        this.tagIds = tagIds;
    }
    
    public Long getCreatorId()
    {
        return creatorId;
    }
    
    public void setCreatorId(Long creatorId)
    {
        this.creatorId = creatorId;
    }
    
    public String getCreator()
    {
        return creator;
    }
    
    public void setCreator(String creator)
    {
        this.creator = creator;
    }
    
    public Boolean getIsAgent()
    {
        return isAgent;
    }
    
    public void setIsAgent(Boolean isAgent)
    {
        this.isAgent = isAgent;
    }
    
    public Boolean getIsDivert()
    {
        return isDivert;
    }
    
    public void setIsDivert(Boolean isDivert)
    {
        this.isDivert = isDivert;
    }
    
    public Short getTaskStatus()
    {
        return taskStatus;
    }
    
    public void setTaskStatus(Short taskStatus)
    {
        this.taskStatus = taskStatus;
    }
    
    public String getCodebefore()
    {
        return codebefore;
    }
    
    public void setCodebefore(String codebefore)
    {
        this.codebefore = codebefore;
    }
    
    public Integer getHasRead()
    {
        return hasRead;
    }
    
    public void setHasRead(Integer hasRead)
    {
        this.hasRead = hasRead;
    }
    
    public void setTaskUrl(String taskUrl)
    {
        this.taskUrl = taskUrl;
    }
    
    
    /**
     * 工作台 代办任务扩展数据
     * 获取当前节点数据的 任务名称
     */
    public Object getCurNodeName()
    {
        return name;
    }
    
    /**
     * 获取当前节点上一步任务执行人账号
     */
    public Object getPreUserName()
    {
        if(executionStack==null){
            ExecutionStackService executionStackService =(ExecutionStackService) AppUtil.getBean("executionStackService");
            this.executionStack =executionStackService.getLastestStack(processInstanceId, taskDefinitionKey);
            this.executionStack =executionStackService.getById(executionStack.getParentId());

        }
        String exeUserId=executionStack.getAssignees();
        if(StringUtil.isNotEmpty(exeUserId)&&preNodeUser==null){
            ISysUserService sysUserService=(ISysUserService)AppUtil.getBean("sysUserService");
            preNodeUser=sysUserService.getById(Long.valueOf(exeUserId));
        }
        if(preNodeUser!=null){
            return preNodeUser.getUsername();
        }else{
            return null;
        }
    }
    
    /**
     * 工作台 代办任务扩展数据
     * 获取当前节点上一步任务执行人姓名
     */
    public Object getPreUserFullName()
    {
        if(executionStack==null){
            ExecutionStackService executionStackService =(ExecutionStackService) AppUtil.getBean("executionStackService");
            this.executionStack =executionStackService.getLastestStack(processInstanceId, taskDefinitionKey);
            this.executionStack =executionStackService.getById(executionStack.getParentId());

        }
        String exeUserId=executionStack.getAssignees();
        if(StringUtil.isNotEmpty(exeUserId)&&preNodeUser==null){
            ISysUserService sysUserService=(ISysUserService)AppUtil.getBean("sysUserService");
            preNodeUser=sysUserService.getById(Long.valueOf(exeUserId));
        }
        if(preNodeUser!=null){
            return preNodeUser.getFullname()==null?"":preNodeUser.getFullname();
        }else{
            return "";
        }
    }
    
    /**
     * 工作台 代办任务扩展数据
     * 获取当前节点上一步任务提交时间
     */
    public Object getPreSubmitTime()
    {
        if(executionStack==null){
            ExecutionStackService executionStackService =(ExecutionStackService) AppUtil.getBean("executionStackService");
            this.executionStack =executionStackService.getLastestStack(processInstanceId, taskDefinitionKey);
        }
        if(executionStack!=null){
            return DateUtil.getDateString(executionStack.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        }else{
            return null;
        }
    }
    
    /**
     * 工作台 代办任务扩展数据
     * 获取当前节点到期时间
     */
    public Object getDateDue()
    {
        ITaskReminderService taskReminderService = (ITaskReminderService)AppUtil.getBean("taskReminderService");
        ICalendarAssignService calendarAssignService = (ICalendarAssignService)AppUtil.getBean("calendarAssignService");
        
        List<? extends ITaskReminder> taskReminders =
            taskReminderService.getByActDefAndNodeId(processDefinitionId, taskDefinitionKey);
        for (ITaskReminder taskReminder : taskReminders)
        {
            Date relativeStartTime =
                calendarAssignService.getRelativeStartTime(processInstanceId,
                    taskReminder.getRelativeNodeId(),
                    taskReminder.getRelativeNodeType());
            if (relativeStartTime == null)
            {
                return "";
            }
            Date dueDate =
                new Date(
                    TimeUtil.getNextTime(1, taskReminder.getCompleteTime().intValue(), relativeStartTime.getTime()));
            return DateUtil.getDateString(dueDate, "yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }

    @Override
    public void setWarnSet(String warnSet){
        this.warnSet=warnSet;
    }
    /** 
    * @Title: getColor 
    * @Description: TODO(工作台获取待办任务预警信息显示颜色) 
    * @param @return     
    * @return String    返回类型 
    * @throws 
    */
    public String getColor(){
        JSONObject jconf=JSONObject.parseObject(warnSet);
        try{
            return jconf.getString("color");
        }catch(Exception e){
            return "";
        }
    }
}
