package com.cssrc.ibms.core.flow.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.log4j.Logger;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.cssrc.ibms.api.activity.intf.ITaskReminderExtService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.jms.intf.IMessageProducer;
import com.cssrc.ibms.api.jms.model.MessageModel;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.constant.sysuser.SystemConst;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.flow.dao.ReminderStateDao;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.ReminderState;
import com.cssrc.ibms.core.flow.model.TaskReminder;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.TaskUserService;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.singlelogin.IbmsSinglePasswordEncoder;

public abstract class IRemindMsgSend
{
    public static Logger logger = Logger.getLogger(IRemindMsgSend.class);
    
    public abstract void handMsg(ProcessTask task, TaskReminder taskReminder)
        throws Exception;
    
    
    
    public void sendMsg(ProcessTask task, TaskReminder taskReminder) {
        try
        {
            Set<Long> userSet = getUserByTask(task);
            this.handMsg(task, taskReminder);
            HandlerDueTask dueTask=new HandlerDueTask();
            dueTask.handlerDueTask(task, taskReminder, userSet);
        }
        catch (Exception e)
        {
            logger.error(e.getCause(),e);
        }
        
    }
    /** 
    * @Title: exeCondition 
    * @Description: TODO(执行脚本判断是否发送消息) 
    * @param @param conditionExp
    * @param @param task
    * @param @return     
    * @return boolean    返回类型 
    * @throws 
    */
    public boolean exeCondition(String conditionExp, ProcessTask task,Long taskUserId)
    {
        if (StringUtil.isNotEmpty(conditionExp))
        {
            GroovyScriptEngine groovyScriptEngine = AppUtil.getBean(GroovyScriptEngine.class);
            Map<String, Object> vars = this.getVars(task);
            vars.put("taskUserId",taskUserId);
            return groovyScriptEngine.executeBoolean(conditionExp, vars);
        }
        else
        {
            return true;
        }
    }
    
    /**
     * 添加催办记录
     * @param taskId
     * @param actDefId
     * @param actInstanceId
     * @param userId
     * @param remindType
     * @throws Exception
     */
    public void saveReminderState(String taskId, String actDefId, String actInstanceId, Long userId, int remindType,
        Date dueDate)
        throws Exception
    {
        ReminderStateDao reminderStateDao = (ReminderStateDao)AppUtil.getBean(ReminderStateDao.class);
        ReminderState reminderState = new ReminderState();
        reminderState.setId(Long.valueOf(UniqueIdUtil.genId()));
        reminderState.setActInstanceId(actInstanceId);
        reminderState.setUserId(userId);
        reminderState.setActDefId(actDefId);
        reminderState.setTaskId(taskId);
        reminderState.setRemindType(remindType);
        reminderState.setCreatetime(new Date());
        reminderState.setReminderTime(dueDate);
        reminderStateDao.add(reminderState);
    }
    
    /**
     * 设置邮件模板
     * @param sysUser
     * @param mailContent
     * @param title
     * @param url
     * @param time
     * @param vars
     * @return
     */
    public MessageModel getMail(ISysUser sysUser, String mailContent, String title, String url, String time,
        Map<String, Object> vars)
    {
        String mail = sysUser.getEmail();
        if (StringUtil.isEmpty(mail))
        {
            return null;
        }
        if (StringUtil.isEmpty(url))
        {
            url = "javascript:;";
        }
        mailContent = mailContent.replace("${收件人}", sysUser.getFullname())
            .replace("${发件人}", "系统邮件")
            .replace("${剩余时间}", time)
            .replace("${事项名称}", "<a href='" + url + "'>" + title + "</a>");
        
        mailContent = MsgUtil.replaceVars(mailContent, vars);
        String[] toUser = {mail};
        MessageModel mailModel = new MessageModel("1");
        mailModel.setSubject(title);
        mailModel.setTo(toUser);
        mailModel.setContent(mailContent);
        mailModel.setSendDate(new Date());
        return mailModel;
    }
    
    /**
     * 设置消息模板
     * @param sysUser
     * @param smsContent
     * @param title
     * @param time
     * @param vars
     * @return
     */
    public MessageModel getSms(ISysUser sysUser, String smsContent, String title, String time, Map<String, Object> vars)
    {
        String mobile = sysUser.getMobile();
        if (StringUtil.isEmpty(mobile))
            return null;
        smsContent = smsContent.replace("${收件人}", sysUser.getFullname())
            .replace("${发件人}", "系统短信")
            .replace("${剩余时间}", time)
            .replace("${跳转地址}", "")
            .replace("${事项名称}", title);
        
        smsContent = MsgUtil.replaceVars(smsContent, vars);
        
        MessageModel smsModel = new MessageModel("2");
        smsModel.setReceiveUser(sysUser.getUserId());
        smsModel.setContent(smsContent);
        smsModel.setSendDate(new Date());
        return smsModel;
    }
    
    /**
     * 设置消息模板
     * @param sysUser
     * @param msgContent
     * @param title
     * @param url
     * @param time
     * @param vars
     * @return
     */
    public MessageModel getMsg(ISysUser sysUser, String msgContent, String title, String url, String time,
        Map<String, Object> vars)
    {
        String jumpUrl = "<a href='" + url + "'>" + title + "</a>";
        msgContent = msgContent.replace("${收件人}", sysUser.getFullname())
            .replace("${发件人}", "系统消息")
            .replace("${跳转地址}", jumpUrl)
            .replace("${剩余时间}", time)
            .replace("${事项名称}", title)
            .replace("${task.url}", url);
        
        msgContent = MsgUtil.replaceVars(msgContent, vars);
        
        MessageModel innerMessage = new MessageModel("3");
        innerMessage.setSubject(title);
        
        innerMessage.setContent(msgContent);
        innerMessage.setReceiveUser(sysUser.getUserId());
        innerMessage.setSendDate(new Date());
        ISysUser sendUser = (ISysUser)UserContextUtil.getCurrentUser();
        if (sendUser == null)
        {
            sendUser = UserContextUtil.createUser();
            // 若果当前人员获取为空就设置默认的消息发送人为----系统
            sendUser.setUserId(SystemConst.SYSTEMUSERID);
            sendUser.setFullname("系统");
        }
        innerMessage.setSendUser(sendUser.getUserId());
        return innerMessage;
    }
    
    public Set<Long> getUserByTask(ProcessTask task)
    {
        TaskUserService taskUserService = AppUtil.getBean(TaskUserService.class);
        Set<Long> set = new HashSet<Long>();
        String assignee = task.getAssignee();
        if ((StringUtil.isNotEmpty(task.getAssignee())) && (!"0".equals(assignee)))
        {
            set.add(Long.valueOf(Long.parseLong(assignee)));
        }
        else
        {
            Set<? extends ISysUser> users = taskUserService.getCandidateUsers(task.getId());
            for (ISysUser user : users)
            {
                set.add(user.getUserId());
            }
        }
        return set;
    }
    
    /**
     * 发送消息
     * @param userId
     * @param taskReminder
     * @param task
     * @param time
     * @param vars
     */
    public void sendMsg(Long userId, TaskReminder taskReminder, ProcessTask task, String time, Map<String, Object> vars)
    {
        String actDefId = task.getProcessDefinitionId();
        DefinitionService definitionService=AppUtil.getBean(DefinitionService.class);
        Definition definition=definitionService.getByActDefId(actDefId);
        String url="";
        try
        {
            IbmsSinglePasswordEncoder singlePasswordEncoder=AppUtil.getBean(IbmsSinglePasswordEncoder.class);
            String param="taskId="+task.getId()+"&userId="+userId;
            param=singlePasswordEncoder.getSignParam(param);
            String appurl=AppConfigUtil.get("appproperties", "app.url");
            String port=AppConfigUtil.get("appproperties", "app.port");
            port=StringUtil.isEmpty(port)?"80":port;
            String serviceurl="http://"+appurl+":"+port;
            url = serviceurl+AppUtil.getContextPath() +"/oa/flow/task/api/start.do?" + param;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        vars.put("process.subject",definition.getSubject());
        vars.put("task.name", task.getName());
        vars.put("task.url", url);

        IMessageProducer messageProducer = AppUtil.getBean(IMessageProducer.class);
        ProcessRunService processRunService = AppUtil.getBean(ProcessRunService.class);
        ISysUserService sysUserService = AppUtil.getBean(ISysUserService.class);
        
        ISysUser sysUser = sysUserService.getById(userId);
        String msgTemplate = taskReminder.getMsgContent();
        String mailTemplate = taskReminder.getMailText();
        String mobileTemplate = taskReminder.getSmsContent();
        ProcessRun processRun = processRunService.getByActInstanceId(new Long(task.getProcessInstanceId()));
        String subject = processRun.getSubject();
        
        if (StringUtil.isNotEmpty(mailTemplate))
        {
            String _subject = taskReminder.getMailSubject();
            if (StringUtil.isNotEmpty(_subject))
            {
                subject = _subject;
            }
            MessageModel mailModel = getMail(sysUser, mailTemplate, subject, url, time, vars);
            if (mailModel != null)
            {
                // 设置抄送人
                mailModel.setCc(getMailCC(taskReminder, vars));
                messageProducer.send(mailModel);
            }
        }
        if (StringUtil.isNotEmpty(msgTemplate))
        {
            MessageModel msgModel = getMsg(sysUser, msgTemplate, subject, url, time, vars);
            if (msgModel != null)
            {
                messageProducer.send(msgModel);
            }
        }
        if (StringUtil.isNotEmpty(mobileTemplate))
        {
            MessageModel smsMobile = getSms(sysUser, mobileTemplate, subject, time, vars);
            if (smsMobile != null)
                messageProducer.send(smsMobile);
        }
    }
    
    public List<String> getMailCC(TaskReminder taskReminder, Map<String, Object> vars)
    {
        ISysUserService sysUserService = AppUtil.getBean(ISysUserService.class);
        List<String> mailcc = new ArrayList<String>();
        try
        {
            String copytoID = taskReminder.getCopyToID();
            String[] copytoIDs = copytoID.split(",");
            for (String toUser : copytoIDs)
            {
                if (StringUtils.isNotEmpty(toUser))
                {
                    ISysUser user = sysUserService.getById(Long.valueOf(toUser));
                    if (StringUtil.isNotEmpty(user.getEmail()) && !mailcc.contains(user.getEmail()))
                    {
                        mailcc.add(user.getEmail());
                    }
                }
                
            }
            String formVarCopyTo = taskReminder.getFormVarCopyTo();
            String[] formVarCopyTos = formVarCopyTo.split(",");
            for (String toUser : formVarCopyTos)
            {
                if (StringUtils.isNotEmpty(toUser))
                {
                    long toUserId = Long.valueOf(vars.get(toUser).toString());
                    ISysUser user = sysUserService.getById(toUserId);
                    if (StringUtil.isNotEmpty(user.getEmail()) && !mailcc.contains(user.getEmail()))
                    {
                        mailcc.add(user.getEmail());
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mailcc;
    }
    
    public Map<String, Object> getVars(ProcessTask task)
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

    /** 
    * @Title: sendMsgExtCallBack 
    * @Description: TODO(消息发送后回调接口) 
    * @param @param taskId 任务Id
    * @param @param actDefId activiti 流程定义Id
    * @param @param actInstanceId 流程实例ID
    * @param @param userId 催办人
    * @param @param remindType 催办类型，1:邮件，3:站内信，4:手机短信
    * @param @param dueDate   发送时间  
    * @return void    返回类型 
    * @throws 
    */
    public void sendMsgExtCallBack(String taskId, String actDefId, String actInstanceId, Long userId, int remindType,
        Date dueDate)
    {
        ProcessRunService processRunService=AppUtil.getBean(ProcessRunService.class);
        TaskServiceImpl taskService=AppUtil.getBean(TaskServiceImpl.class);
        TaskEntity taskEnt=(TaskEntity)taskService.createTaskQuery().taskId(taskId).singleResult();
        ExecutionEntity excution = taskEnt.getExecution();
        try
        {
            ITaskReminderExtService taskReminderExtService = AppUtil.getBean(ITaskReminderExtService.class);
            if (taskReminderExtService != null)
            {
                ProcessRun processRun =processRunService.getByActInstanceId(Long.valueOf(actInstanceId));
                String businessKey = processRun.getBusinessKey();
                taskReminderExtService.sendMsgExtCallBack(excution, taskEnt, businessKey, userId, remindType,dueDate);
            }
        }
        catch (Exception e)
        {
        }
        
    
    }
}


