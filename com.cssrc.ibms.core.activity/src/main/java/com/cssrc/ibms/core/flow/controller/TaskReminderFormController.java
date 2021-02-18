package com.cssrc.ibms.core.flow.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.flow.model.TaskReminder;
import com.cssrc.ibms.core.flow.service.TaskReminderService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;

/**
 * 对象功能:任务节点催办时间设置 控制器类 开发人员:zhulongchao
 */
@Controller
@RequestMapping("/oa/flow/taskReminder/")
public class TaskReminderFormController extends BaseFormController
{
    @Resource
    private TaskReminderService taskReminderService;
    
    /**
     * 添加或更新任务节点催办时间设置。
     * 
     * @param request
     * @param response
     * @param taskReminder 添加或更新的实体
     * @param bindResult
     * @param viewName
     * @return
     * @throws Exception
     */
    @RequestMapping("save")
    @Action(description = "添加或更新任务节点催办时间设置")
    public void save(HttpServletRequest request, HttpServletResponse response, TaskReminder taskReminder,
        BindingResult bindResult)
        throws Exception
    {
        
        ResultMessage resultMessage = validForm("taskReminder", taskReminder, bindResult, request);
        // add your custom validation rule here such as below code:
        // bindResult.rejectValue("name","errors.exist.student",new Object[]{"jason"},"重复姓名");
        if (resultMessage.getResult() == ResultMessage.Fail)
        {
            writeResultMessage(response.getWriter(), resultMessage);
            return;
        }
        String mailText =RequestUtil.getString(request, "mailText");
        String mailSubject =RequestUtil.getString(request, "mailSubject");
        String copyToID =RequestUtil.getString(request, "copyToID");
        String copyTo =RequestUtil.getString(request, "copyTo");
        String formVarCopyTo =RequestUtil.getString(request, "formVarCopyTo");
        
        String resultMsg = null;
        int reminderStartDay = RequestUtil.getInt(request, "reminderStartDay");
        int reminderStartHour = RequestUtil.getInt(request, "reminderStartHour");
        int reminderStartMinute = RequestUtil.getInt(request, "reminderStartMinute");
        // 计算任务催办开始时间为多少个工作日，以分钟计算
        int reminderStart = (reminderStartDay * 24 + reminderStartHour) * 60 + reminderStartMinute;
        
        int reminderEndDay = RequestUtil.getInt(request, "reminderEndDay");
        int reminderEndHour = RequestUtil.getInt(request, "reminderEndHour");
        int reminderEndMinute = RequestUtil.getInt(request, "reminderEndMinute");
        // 计算任务催办结束时间为多少个工作日，以分钟计算
        int reminderEnd = (reminderEndDay * 24 + reminderEndHour) * 60 + reminderEndMinute;
        
        int completeTimeDay = RequestUtil.getInt(request, "completeTimeDay");
        int completeTimeHour = RequestUtil.getInt(request, "completeTimeHour");
        int completeTimeMinute = RequestUtil.getInt(request, "completeTimeMinute");
        // 计算办结时间为多少个工作日，以分钟计算
        int completeTime = (completeTimeDay * 24 + completeTimeHour) * 60 + completeTimeMinute;
        
        taskReminder.setReminderStart(reminderStart);
        taskReminder.setReminderEnd(reminderEnd);
        taskReminder.setCompleteTime(completeTime);
        JSONObject jmailContent=new JSONObject();
        jmailContent.put("mailText", mailText);
        jmailContent.put("mailSubject", mailSubject);
        jmailContent.put("copyTo", copyTo);
        jmailContent.put("copyToID", copyToID);
        jmailContent.put("formVarCopyTo", formVarCopyTo);

        taskReminder.setMailContent(jmailContent.toString());
        try
        {
            if (taskReminder.getTaskDueId() == null || taskReminder.getTaskDueId() == 0)
            {
                taskReminder.setTaskDueId(UniqueIdUtil.genId());
                taskReminderService.add(taskReminder);
                resultMsg = getText("record.added", getText("controller.taskReminder"));
            }
            else
            {
                taskReminderService.update(taskReminder);
                resultMsg = getText("record.updated", getText("controller.taskReminder"));
            }
            writeResultMessage(response.getWriter(), resultMsg, ResultMessage.Success);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), ResultMessage.Fail);
        }
    }
}
