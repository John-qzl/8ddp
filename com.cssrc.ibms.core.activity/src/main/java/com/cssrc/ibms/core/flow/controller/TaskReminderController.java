package com.cssrc.ibms.core.flow.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.DefVar;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.TaskReminder;
import com.cssrc.ibms.core.flow.service.DefVarService;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.TaskReminderService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;

/**
 * 对象功能:任务节点催办时间设置 控制器类 开发人员:zhulongchao
 */
@Controller
@RequestMapping("/oa/flow/taskReminder/")
@Action(ownermodel = SysAuditModelType.FLOW_MANAGEMENT)
public class TaskReminderController extends BaseController
{
    @Resource
    private TaskReminderService taskReminderService;
    
    @Resource
    private DefinitionService bpmDefinitionService;
    
    @Resource
    private DefVarService bpmDefVarService;
    @Resource
    private ISysParameterService sysParameterService;
    /**
     * 取得任务节点催办时间设置分页列表
     * 
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @Action(description = "查看任务节点催办时间设置分页列表")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        List<TaskReminder> list = taskReminderService.getAll(new QueryFilter(request, "taskReminderItem"));
        ModelAndView mv = this.getAutoView().addObject("taskReminderList", list);
        
        return mv;
    }
    
    /**
     * 删除任务节点催办时间设置
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("del")
    @Action(description = "删除任务节点催办时间设置", execOrder = ActionExecOrder.BEFORE, detail = "<#list StringUtils.split(taskDueId,\",\") as item>"
        + "<#assign entity = taskReminderService.getById(Long.valueOf(item))/>"
        + "<#if item_index==0>"
        + "删除流程定义【${SysAuditLinkService.getDefinitionLink(entity.actDefId)}】的节点"
        + "【${SysAuditLinkService.getNodeName(entity.actDefId,entity.nodeId)}】"
        + "的任务节点催办时间设置："
        + "</#if>"
        + "【 ${entity.name}】 " + "</#list>")
    public void del(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String preUrl = RequestUtil.getPrePage(request);
        ResultMessage message = null;
        try
        {
            Long[] lAryId = RequestUtil.getLongAryByStr(request, "taskDueId");
            taskReminderService.delByIds(lAryId);
            message = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
        }
        catch (Exception ex)
        {
            message = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail") + ":" + ex.getMessage());
        }
        addMessage(message, request);
        response.sendRedirect(preUrl);
    }
    
    @RequestMapping("edit")
    @Action(description = "编辑任务节点催办时间设置")
    public ModelAndView eidt(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long id = RequestUtil.getLong(request, "id");
        String actDefId = RequestUtil.getString(request, "actDefId");
        String nodeId = RequestUtil.getString(request, "nodeId");
        String returnUrl = RequestUtil.getPrePage(request);
        
        List<TaskReminder> taskReminders = taskReminderService.getByActDefAndNodeId(actDefId, nodeId);
        Definition bpmDefinition = bpmDefinitionService.getByActDefId(actDefId);
        Long defId = bpmDefinition.getDefId();
        
        Map<String, FlowNode> nodeMaps = NodeCache.getByActDefId(actDefId);
        List<FlowNode> nodes = new ArrayList<FlowNode>();
        Iterator<Entry<String, FlowNode>> it = nodeMaps.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, FlowNode> entry = it.next();
            FlowNode node = entry.getValue();
            if ("userTask".equals(node.getNodeType()) || "startEvent".equals(node.getNodeType()))
            {
                // 排序中将当前节点排到第一个，这样页面默认选中当前节点为相对节点
                if (nodeId.equals(node.getNodeId()))
                    nodes.add(0, node);
                else
                    nodes.add(node);
            }
            else
                continue;
        }
        
        IFormFieldService bpmFormFieldService = (IFormFieldService)AppUtil.getBean(IFormFieldService.class);
        List<? extends IFormField> flowVars = bpmFormFieldService.getFlowVarByFlowDefId(defId);
        List<DefVar> bpmdefVars = bpmDefVarService.getVarsByFlowDefId(defId);
        
        int reminderStartDay = 0;
        int reminderStartHour = 0;
        int reminderStartMinute = 0;
        int reminderEndDay = 0;
        int reminderEndHour = 0;
        int reminderEndMinute = 0;
        int completeTimeDay = 0;
        int completeTimeHour = 0;
        int completeTimeMinute = 0;
        
        TaskReminder taskReminder = taskReminderService.getById(id);
        if (id == 0 || taskReminder == null)
        {
            taskReminder = new TaskReminder();
        }
        else
        {
            int reminderStart = taskReminder.getReminderStart();
            reminderStartDay = reminderStart / (60 * 24);
            reminderStartHour = (reminderStart - reminderStartDay * (60 * 24)) / 60;
            reminderStartMinute = reminderStart - reminderStartDay * (60 * 24) - reminderStartHour * 60;
            
            int reminderEnd = taskReminder.getReminderEnd();
            reminderEndDay = reminderEnd / (60 * 24);
            reminderEndHour = (reminderEnd - reminderEndDay * (60 * 24)) / 60;
            reminderEndMinute = reminderEnd - reminderEndDay * (60 * 24) - reminderEndHour * 60;
            
            int complateTime = taskReminder.getCompleteTime();
            completeTimeDay = complateTime / (60 * 24);
            completeTimeHour = (complateTime - completeTimeDay * (60 * 24)) / 60;
            completeTimeMinute = complateTime - completeTimeDay * (60 * 24) - completeTimeHour * 60;
        }
        
        String  taskReminderConfs=this.sysParameterService.getByAlias(ISysParameterService.taskReminderConf);
        
        
        return getAutoView().addObject("taskReminder", taskReminder)
            .addObject("taskReminders", taskReminders)
            .addObject("returnUrl", returnUrl)
            .addObject("defId", defId)
            .addObject("actDefId", actDefId)
            .addObject("nodeId", nodeId)
            .addObject("flowVars", flowVars)
            .addObject("defVars", bpmdefVars)
            .addObject("reminderStartDay", reminderStartDay)
            .addObject("reminderStartHour", reminderStartHour)
            .addObject("reminderStartMinute", reminderStartMinute)
            .addObject("reminderEndDay", reminderEndDay)
            .addObject("reminderEndHour", reminderEndHour)
            .addObject("reminderEndMinute", reminderEndMinute)
            .addObject("completeTimeDay", completeTimeDay)
            .addObject("completeTimeHour", completeTimeHour)
            .addObject("completeTimeMinute", completeTimeMinute)
            .addObject("nodes", nodes)
            .addObject("taskReminderConfs", JSONArray.parseArray(taskReminderConfs));
        
    }
    
    @RequestMapping("getFlowVars")
    @ResponseBody
    @Action(description = "编辑任务节点催办时间设置")
    public Map<String, Object> getFlowVars(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long defId = RequestUtil.getLong(request, "defId");
        Map<String, Object> map = new HashMap<String, Object>();
        
        IFormFieldService bpmFormFieldService = (IFormFieldService)AppUtil.getBean(IFormFieldService.class);
        List<? extends IFormField> flowVars = bpmFormFieldService.getFlowVarByFlowDefId(defId);
        map.put("flowVars", flowVars);
        return map;
        
    }
}
