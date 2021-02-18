package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.jms.intf.IMessageProducer;
import com.cssrc.ibms.api.jms.model.MessageModel;
import com.cssrc.ibms.api.system.intf.ISysTemplateService;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.flow.dao.DefinitionDao;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service
public class TaskMessageService
{
    Logger logger=Logger.getLogger(TaskMessageService.class);
    @Resource
    private IMessageProducer messageProducer;
    
    @Resource
    private ISysUserService sysUserService;
    
    @Resource
    private TaskUserService taskUserService;
    
    @Resource
    private ISysTemplateService sysTemplateService;
    
    @Resource
    private DefinitionDao definitionDao;
    
    @Resource
    private NodeSetService nodeSetService;
    
    @Resource
    private RuntimeService runtimeService;
    
    public void pushUser(Map<ISysUser, List<Task>> users, ISysUser user, Task task)
    {
        if (users.containsKey(user))
        {
            users.get(user).add(task);
        }
        else
        {
            List<Task> list = new ArrayList<Task>();
            list.add(task);
            users.put(user, list);
        }
    }
    
    /**
     * 发送 站内消息 短信 邮件 通知
     * @param taskList 任务列表
     * @param informTypes 通知方式
     * @param subject	标题
     * @param map		消息模版
     * @param opinion	意见
     * @throws Exception
     */
    public void notify(List<Task> taskList, String informTypes, String subject, Map<String, String> map, String opinion,
        String parentActDefId)
        throws Exception
    {
        // 通知类型为空。
        if (taskList == null)
            return;
        ISysUser curUser = UserContextUtil.getCurrentUser();
        for (Task task : taskList)
        {
            String actDefId = task.getProcessDefinitionId();
            Definition bpmDefinition = definitionDao.getByActDefId(actDefId);
            NodeSet bpmNodeSet = getCurentNodeSet(parentActDefId, task);
            // 判断通知方式是否为空
            if (StringUtil.isEmpty(informTypes))
            {
                informTypes = getInFormType(bpmDefinition, bpmNodeSet);
            }
            if (StringUtil.isEmpty(informTypes))
            {
                continue;
            }
            String assignee = task.getAssignee();
            // 获取通知的用户
            Set<? extends ISysUser> notifyUsers = getNotifyUser(task, assignee);
            for (ISysUser user : notifyUsers)
            {
                
                 if (user.getUserId().longValue() == curUser.getUserId().longValue()) { 
                     //当前用户与审核用户是否同一个人,如果为同一个人，则不需要发送消息 
                     //continue; 
                 }
                 
                if (TaskOpinion.STATUS_AGENT.toString().equals(task.getDescription()))
                {
                    // 是否为代理任务(代理任务发送消息给任务所属人)
                    user = sysUserService.getById(Long.parseLong(task.getOwner()));
                }
                try
                {
                    sendMessage(informTypes, subject, map, opinion, curUser, user, bpmNodeSet, task);
                }
                catch (Exception e)
                {
                    logger.error(e);
                }
            }
            
        }
        
    }
    
    public List<String> getMailCC(NodeSet bpmNodeSet, Map<String, Object> vars)
    {
        List<String> mailcc = new ArrayList<String>();
        try
        {
            String copytoID = bpmNodeSet.getCopyToID();
            String[] copytoIDs = copytoID.split(",");
            for (String toUser : copytoIDs)
            {
                if(StringUtil.isEmpty(toUser)) {
                    continue;
                }
                ISysUser user = sysUserService.getById(Long.valueOf(toUser));
                if (StringUtil.isNotEmpty(user.getEmail())&&!mailcc.contains(user.getEmail()))
                {
                    mailcc.add(user.getEmail());
                }
            }
            String formVarCopyTo = bpmNodeSet.getFormVarCopyTo();
            String[] formVarCopyTos = formVarCopyTo.split(",");
            for (String toUser : formVarCopyTos)
            {
                if(StringUtil.isEmpty(toUser)) {
                    continue;
                }
                long toUserId = Long.valueOf(vars.get(toUser).toString());
                ISysUser user = sysUserService.getById(toUserId);
                if (StringUtil.isNotEmpty(user.getEmail())&&!mailcc.contains(user.getEmail()))
                {
                    mailcc.add(user.getEmail());
                }
                
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mailcc;
    }
    
    public void sendMessage(String informTypes, String subject, Map<String, String> templateMap, String opinion,
        ISysUser curUser, ISysUser toUser, NodeSet bpmNodeSet, Task task)
        throws Exception
    {
        String infoTypeArray[] = informTypes.split(",");
        for (String infoType : infoTypeArray)
        {
            try
            {
                templateMap = getMsgTemplate(infoType, bpmNodeSet, task);
                Map<String, Object> vars = runtimeService.getVariables(task.getProcessInstanceId());

                MessageModel messageModel = new MessageModel(infoType);
                messageModel.setReceiveUser(toUser.getUserId());
                messageModel.setSendUser(curUser.getUserId());
                messageModel.setSubject(subject);
                messageModel.setTemplateMap(templateMap);
                messageModel.setExtId(Long.parseLong(task.getId()));
                messageModel.setIsTask(true);
                messageModel.setOpinion(opinion);
                messageModel.setVars(vars);
                messageModel.setCc(this.getMailCC(bpmNodeSet, vars));
                messageProducer.send(messageModel);
                Thread.sleep(100);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }
    
    public Map<String, String> getMsgTemplate(String informType, NodeSet bpmNodeSet, Task task)
        throws Exception
    {
        
        if (TaskOpinion.STATUS_AGENT.toString().equals(task.getDescription()))
        {
            return sysTemplateService.getTempByFun(ISysTemplate.USE_TYPE_NOTIFYOWNER_AGENT);
            
        }
        else if ("1".equals(informType) && StringUtil.isNotEmpty(bpmNodeSet.getInformConf()))
        {
            String title = bpmNodeSet.getMailSubject();
            String html = bpmNodeSet.getMailText();
            Map<String, String> map = new HashMap<String, String>();
            map.put(ISysTemplate.TEMPLATE_TITLE, title);
            map.put(ISysTemplate.TEMPLATE_TYPE_HTML, html);
            map.put(ISysTemplate.TEMPLATE_TYPE_PLAIN, "");
            return map;
        }
        else
        {
            return sysTemplateService.getDefaultTemp();
        }
        
    }
    
    public Set<? extends ISysUser> getNotifyUser(Task task, String assignee)
    {
        if (FlowUtil.isAssigneeNotEmpty(assignee))
        {
            Set<ISysUser> users = new HashSet<>();
            ISysUser user = sysUserService.getById(Long.parseLong(assignee));
            users.add(user);
            return users;
        }
        // 获取该任务的候选用户列表
        else
        {
            return taskUserService.getCandidateUsers(task.getId());
            
        }
    }
    
    public String getInFormType(Definition bpmDefinition, NodeSet bpmNodeSet)
    {
        // 如果流程不为测试状态并且审核节点的通知方式不为空，优先审核节点的通知方式
        if (!Definition.STATUS_TEST.equals(bpmDefinition.getStatus())
            && StringUtil.isNotEmpty(bpmNodeSet.getInformType()))
        {
            return bpmNodeSet.getInformType();
        }
        else
        {
            // 其他情况取得流程定义的通知方式；对应的是：设置-其他-状态（站内消息、邮件、RTX消息）
            return bpmDefinition.getInformType();
        }
    }
    
    public NodeSet getCurentNodeSet(String parentActDefId, Task task)
    {
        String actDefId = task.getProcessDefinitionId();
        String nodeId = task.getTaskDefinitionKey();
        // 取得当前审核节点信息
        if (StringUtil.isEmpty(parentActDefId))
        {
            return nodeSetService.getByMyActDefIdNodeId(actDefId, nodeId);
        }
        else
        {
            return nodeSetService.getByMyActDefIdNodeId(actDefId, nodeId, parentActDefId);
        }
    }
    
    /**
     * 发送消息接口
     * 
     * @param sendUser 发送者名称
     * @param receiverUserList 接收者用户列表
     * @param informTypes 发送消息类型
     * @param msgTempMap 消息模板
     * @param subject 事项名称
     * @param opinion 意见或原因
     * @param taskId 任务Id
     * @param runId 流程运行ID
     * @throws Exception
     */
    public void sendMessage(ISysUser sendUser, List<ISysUser> receiverUserList, String informTypes,
        Map<String, String> msgTempMap, String subject, String opinion, Long taskId, Long runId)
        throws Exception
    {
        if (StringUtil.isEmpty(informTypes))
            return;
        if (BeanUtils.isEmpty(sendUser))
            return;
        if (BeanUtils.isEmpty(receiverUserList))
            return;
        if (BeanUtils.isEmpty(msgTempMap))
            return;
        
        boolean isTask = true;
        if (BeanUtils.isNotEmpty(taskId))
        {
            isTask = true;
        }
        else
        {
            isTask = false;
        }
        int userSize = receiverUserList.size();
        
        for (int i = 0; i < userSize; i++)
        {
            ISysUser receiverUser = receiverUserList.get(i);
            // informTypes形如逗号分割字符串：1,2,3
            String infoTypeArray[] = informTypes.split(",");
            for (String infoType : infoTypeArray)
            {
                // infoType一定要指定，其值与handlersMap 中的key对应：1邮件，2短信，3内部消息
                MessageModel messageModel = new MessageModel(infoType);
                messageModel.setReceiveUser(receiverUser.getUserId());
                messageModel.setSendUser(sendUser.getUserId());
                messageModel.setSubject(subject);
                messageModel.setTemplateMap(msgTempMap);
                messageModel.setExtId(taskId);
                messageModel.setIsTask(isTask);
                messageModel.setOpinion(opinion);
                messageModel.setSendDate(new Date());
                messageProducer.send(messageModel);
            }
        }
    }
    
    /**
     * 直接发送消息
     * @param sendUser 发送者
     * @param receiverUserList 接受者
     * @param informTypes 消息方式
     * @param title 标题
     * @param content 内容
     */
    public void sendMessage(ISysUser sendUser, List<ISysUser> receiverUserList, String informTypes, String title,
        String content)
    {
        int userSize = receiverUserList.size();
        for (int i = 0; i < userSize; i++)
        {
            ISysUser receiverUser = receiverUserList.get(i);
            // informTypes形如逗号分割字符串：1,2,3
            String infoTypeArray[] = informTypes.split(",");
            for (String infoType : infoTypeArray)
            {
                // 循环,找到具体的实现类
                MessageModel messageModel = new MessageModel(infoType);
                messageModel.setReceiveUser(receiverUser.getUserId());
                messageModel.setSendUser(sendUser.getUserId());
                messageModel.setSubject(title);
                messageModel.setSendDate(new Date());
                messageModel.setContent(content);
                messageProducer.send(messageModel);
            }
            
        }
    }
}
