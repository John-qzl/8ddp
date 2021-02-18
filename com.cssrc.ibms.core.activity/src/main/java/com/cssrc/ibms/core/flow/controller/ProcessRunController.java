
package com.cssrc.ibms.core.flow.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.intf.IFormRunService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormRun;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.jms.intf.IMessageHandler;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.system.intf.ISysTemplateService;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.graph.ShapeMeta;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.activity.util.BpmUtil;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.excel.util.ExcelUtil;
import com.cssrc.ibms.core.flow.model.AuthorizeRight;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.RunLog;
import com.cssrc.ibms.core.flow.model.TaskExeStatus;
import com.cssrc.ibms.core.flow.model.TaskNodeStatus;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.BpmService;
import com.cssrc.ibms.core.flow.service.DefAuthorizeService;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.flow.service.ProCopytoService;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.RunLogService;
import com.cssrc.ibms.core.flow.service.TaskApprovalItemsService;
import com.cssrc.ibms.core.flow.service.TaskMessageService;
import com.cssrc.ibms.core.flow.service.TaskOpinionService;
import com.cssrc.ibms.core.flow.service.TaskReadService;
import com.cssrc.ibms.core.flow.service.TaskUserService;
import com.cssrc.ibms.core.flow.service.impl.TaskExecutorService;
import com.cssrc.ibms.core.flow.status.FlowStatus;
import com.cssrc.ibms.core.flow.status.FlowStatusService;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.encrypt.Base64;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * 对象功能:流程实例扩展控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/processRun/")
@Action(ownermodel = SysAuditModelType.FLOW_MANAGEMENT)
public class ProcessRunController extends BaseController
{
    protected Logger log = LoggerFactory.getLogger(ProcessRunService.class);
    
    @Resource
    private ProcessRunService processRunService;
    
    @Resource
    private FlowStatusService flowStatusService;
    
    @Resource
    private BpmService bpmService;
    
    @Resource
    private TaskOpinionService taskOpinionService;
    
    @Resource
    private TaskService taskService;
    
    @Resource
    private DefinitionService bpmDefinitionService;
    
    @Resource
    private NodeSetService bpmNodeSetService;
    
    @Resource
    private IFormDefService bpmFormDefService;
    
    @Resource
    private HistoryService historyService;
    
    @Resource
    private ISysTemplateService sysTemplateService;
    
    @Resource
    private TaskReadService taskReadService;
    
    @Resource
    private TaskUserService taskUserService;
    
    @Resource
    private ISysUserService sysUserService;
    
    @Resource
    private IFormHandlerService bpmFormHandlerService;
    
    @Resource
    private ProCopytoService bpmProCopytoService;
    
    @Resource
    private RunLogService bpmRunLogService;
    
    @Resource
    private IFormRunService bpmFormRunService;
    
    /*
     * @Resource private PrintTemplateService bpmPrintTemplateService;
     */
    @Resource
    private TaskExecutorService taskExecutorService;
    
    @Resource
    private TaskMessageService taskMessageService;
    
    @Resource
    Map<String, IMessageHandler> handlersMap;
    
    @Resource
    RuntimeService runtimeService;
    
    @Resource
    private DefAuthorizeService defAuthorizeService;
    
    @Resource
    IUserPositionService userPositionService;
    
    @Resource
    private TaskApprovalItemsService taskAppItemService;
    
    @Resource
    private NodeSetService nodeSetService;
    
    @Resource
    IDataTemplateService dataTemplateService;
    
    /**
     * 已完成任务
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("completedMatters")
    public ModelAndView completedMatters(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        return this.getAutoView();
    }
    
    /**
     * 取得流程实例扩展分页列表
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @Action(description = "查看流程实例扩展分页列表")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        
        QueryFilter filter = new QueryFilter(request, "processRunItem");
        // 过滤掉草稿实例
        filter.addFilterForIB("exceptStatus", 4);
        // 过滤流程定义状态为"禁用实例" 的流程实例
        filter.addFilterForIB("exceptDefStatus", 3);
        
        Long userId = UserContextUtil.getCurrentUserId();
        String isNeedRight = "";
        Map<?, ?> authorizeRightMap = null;
        if (userId.longValue() > 1L)
        {
            isNeedRight = "yes";
            
            Map<?, ?> actRightMap = defAuthorizeService.getActRightByUserMap(userId, "instance", true, false);
            
            String actRights = (String)actRightMap.get("authorizeIds");
            filter.addFilterForIB("actRights", actRights);
            
            authorizeRightMap = (Map<?, ?>)actRightMap.get("authorizeRightMap");
        }
        filter.addFilterForIB("isNeedRight", isNeedRight);
        
        List<ProcessRun> list = processRunService.getAll(filter);
        
        if ((userId.longValue() >= 1L) && (authorizeRightMap != null))
        {
            for (ProcessRun processRun : list)
                processRun.setAuthorizeRight((AuthorizeRight)authorizeRightMap.get(processRun.getFlowKey()));
        }
        else
        {
            AuthorizeRight authorizeRight = new AuthorizeRight();
            authorizeRight.setRightByAuthorizeType("Y", "instance");
            for (ProcessRun processRun : list)
            {
                processRun.setAuthorizeRight(authorizeRight);
            }
        }
        ModelAndView mv = this.getAutoView().addObject("processRunList", list);
        return mv;
    }
    
    /**
     * 是否允许撤销
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("checkRecover")
    public void checkRecover(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long runId = RequestUtil.getLong(request, "runId");
        // 是否追回到发起人，即撤销流程
        ResultMessage result = processRunService.checkRecover(runId);
        writeResultMessage(response.getWriter(), result);
    }
    
    /**
     * 是否允许追回
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("checkRedo")
    public void checkRedo(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long runId = RequestUtil.getLong(request, "runId");
        // 子流程是否撤销由父流程决定
        // ProcessRun processRun=processRunService.getById(runId);
        // 是否可以追回
        ResultMessage result = processRunService.checkRedo(runId);
        writeResultMessage(response.getWriter(), result);
    }
    
    @RequestMapping("monitor")
    @Action(description = "流程监控")
    public ModelAndView monitor(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        QueryFilter filter = new QueryFilter(request, "processRunItem");
        filter.addFilterForIB("curUser", UserContextUtil.getCurrentUserId());
        List<ProcessRun> list = processRunService.getMonitor(filter);
        return this.getAutoView().addObject("processRunList", list);
    }
    
    /**
     * 显示撤销对话框。
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("recoverDialog")
    public ModelAndView recoverDialog(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long runId = RequestUtil.getLong(request, "runId", 0);
        int backToStart = RequestUtil.getInt(request, "backToStart", 0);
        ModelAndView mv = getAutoView();
        mv.addObject("runId", runId);
        mv.addObject("backToStart", backToStart).addObject("handlersMap", handlersMap);
        
        return mv;
    }
    
    /**
     * 显示追回对话框。
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("redoDialog")
    public ModelAndView redoDialog(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long runId = RequestUtil.getLong(request, "runId", 0);
        int backToStart = RequestUtil.getInt(request, "backToStart", 0);
        ModelAndView mv = getAutoView();
        mv.addObject("runId", runId);
        mv.addObject("backToStart", backToStart).addObject("handlersMap", handlersMap);
        
        return mv;
    }
    
    /**
     * 任务追回,检查当前正在运行的任务是否允许进行追回。
     * <pre>
     * 需要传入的参数：
     * runId:任务执行Id。
     * backToStart:追回到发起人。
     * memo:追回原因。
     *  任务能够被追回的条件：
     *  1.流程实例没有结束。
     *  
     * 	任务追回包括两种情况。
     *  1.追回到发起人。
     *  4.如果这个流程实例有多个流程实例的情况，那么第一个跳转到驳回节点，其他的只完成当前任务，不进行跳转。
     *  
     * </pre>
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("recover")
    public void recover(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        PrintWriter out = response.getWriter();
        Long runId = RequestUtil.getLong(request, "runId");
        String informType = RequestUtil.getStringValues(request, "informType");
        String memo = RequestUtil.getString(request, "opinion");
        int backToStart = RequestUtil.getInt(request, "backToStart");
        ResultMessage resultMessage = null;
        try
        {
            if (backToStart == 1)
            {
                // 撤销
                resultMessage = processRunService.recover(runId, informType, memo);
            }
            else
            {
                // 追回
                resultMessage = processRunService.redo(runId, informType, memo, true, false);
            }
        }
        catch (Exception ex)
        {
            String str = MessageUtil.getMessage();
            if (StringUtil.isNotEmpty(str))
            {
                resultMessage = new ResultMessage(ResultMessage.Fail, str);
            }
            else
            {
                String message = ExceptionUtil.getExceptionMessage(ex);
                resultMessage = new ResultMessage(ResultMessage.Fail, message);
            }
        }
        writeResultMessage(out, resultMessage);
    }
    
    /**
     * 查看流程实例历史列表
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("history")
    @Action(description = "查看流程实例历史列表")
    public ModelAndView history(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        QueryFilter queryFilter = new QueryFilter(request, "processRunItem");
        List<ProcessRun> list = processRunService.getAllHistory(queryFilter);
        ModelAndView mv = this.getAutoView().addObject("processRunList", list);
        return mv;
    }
    
    /**
     * 取得流程实例扩展分页列表
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("myStart")
    @Action(description = "查看我发起的流程列表")
    public ModelAndView myStart(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        QueryFilter filter = new QueryFilter(request, "processRunItem");
        filter.addFilterForIB("creatorId", UserContextUtil.getCurrentUserId());
        List<ProcessRun> list = processRunService.getAll(filter);
        
        if (BeanUtils.isNotEmpty(list))
        {
            for (ProcessRun processRun : list)
            {
                Definition bpmDefinition = bpmDefinitionService.getByActDefId(processRun.getActDefId());
                if (bpmDefinition.getIsPrintForm() == 1)
                {
                    processRun.setIsPrintForm(bpmDefinition.getIsPrintForm());
                }
            }
        }
        
        ModelAndView mv = this.getAutoView().addObject("processRunList", list);
        
        return mv;
    }
    
    /**
     * 催促执行人、所属人（优先催促执行人，没有执行人就催促所属人）
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("urgeOwner")
    @Action(description = "打开催办界面")
    public ModelAndView urgeOwner(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String actInstId = RequestUtil.getString(request, "actInstId");
        String inner = "";
        String mail = "";
        String shortmsg = "";
        ProcessRun processRun = processRunService.getByActInstanceId(new Long(actInstId));
        
        ISysTemplate temp = sysTemplateService.getDefaultByUseType(ISysTemplate.USE_TYPE_URGE);
        if (BeanUtils.isNotEmpty(temp))
        {
            inner = temp.getHtmlContent();
            mail = temp.getHtmlContent();
            shortmsg = temp.getPlainContent();
        }
        ModelAndView mv = this.getAutoView()
            .addObject("processSubject", processRun.getSubject())
            .addObject("actInstId", actInstId)
            .addObject("inner", inner)
            .addObject("mail", mail)
            .addObject("shortMsg", shortmsg);
        return mv;
    }
    
    /**
     * 执行催办动作
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("urgeSubmit")
    @Action(description = "执行催办")
    public void urgeSubmit(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        try
        {
            String instanceId = RequestUtil.getString(request, "actInstId");
            String messgeType = RequestUtil.getStringAry(request, "messgeType");
            ProcessRun processRun = processRunService.getByActInstanceId(Long.parseLong(instanceId));
            String parentActDefId = this.processRunService.getParentProcessRunActDefId(processRun);
            String subject = RequestUtil.getString(request, "subject");
            String processSubject = RequestUtil.getString(request, "processSubject");
            String opinion = RequestUtil.getString(request, "opinion");
            Definition bpmDefinition = bpmDefinitionService.getByActDefId(processRun.getActDefId());
            if (Definition.STATUS_INST_DISABLED.equals(bpmDefinition.getStatus()))
            {
                response.getWriter()
                    .print(new ResultMessage(ResultMessage.Fail, getText("controller.processRun.urgeSubmit.msg")));
                return;
            }
            Boolean userProcessSubject = RequestUtil.getBoolean(request, "userProcessSubject");
            if (userProcessSubject || StringUtils.isEmpty(subject))
                subject = processSubject;
            Map<String, String> map = sysTemplateService.getTempByFun(ISysTemplate.USE_TYPE_URGE);
            
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(instanceId).list();
            taskMessageService.notify(taskList, messgeType, subject, map, opinion, parentActDefId);
            writeResultMessage(response.getWriter(),
                getText("controller.processRun.urgeSubmit.success"),
                ResultMessage.Success);
        }
        catch (Exception ex)
        {
            String str = MessageUtil.getMessage();
            if (StringUtil.isNotEmpty(str))
            {
                String msg = getText("controller.processRun.urgeSubmit.fail");
                ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, msg + ":" + str);
                response.getWriter().print(resultMessage);
            }
            else
            {
                String message = ExceptionUtil.getExceptionMessage(ex);
                ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
                response.getWriter().print(resultMessage);
            }
        }
    }
    
    /**
     * 查看我参与审批流程列表
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("myAttend")
    @Action(description = "查看我参与审批流程列表")
    public ModelAndView myAttend(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        QueryFilter filter = new QueryFilter(request, "processRunItem");
        filter.addFilterForIB("assignee", UserContextUtil.getCurrentUserId().toString());
        List<ProcessRun> list = processRunService.getMyAttend(filter);
        for (ProcessRun processRun : list)
        {
            if (processRun.getStatus().shortValue() != ProcessRun.STATUS_FINISH.shortValue())
            {
                // 1.查找当前用户是否有最新审批的任务
                TaskOpinion taskOpinion = taskOpinionService.getLatestUserOpinion(processRun.getActInstId(),
                    UserContextUtil.getCurrentUserId());
                if (BeanUtils.isNotEmpty(taskOpinion))
                    processRun.setRecover(ProcessRun.RECOVER);
            }
        }
        ModelAndView mv = this.getAutoView().addObject("processRunList", list);
        return mv;
    }
    
    /**
     * 删除流程实例扩展
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("del")
    @Action(description = "删除流程实例扩展", execOrder = ActionExecOrder.BEFORE, detail = "删除流程实例"
        + "<#list StringUtils.split(runId,\",\") as item>"
        + "<#assign entity=processRunService.getById(Long.valueOf(item))/>" + "【${entity.subject}】" + "</#list>")
    public void del(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ResultMessage message = null;
        String preUrl = RequestUtil.getPrePage(request);
        try
        {
            Long[] lAryId = RequestUtil.getLongAryByStr(request, "runId");
            processRunService.delByIds(lAryId);
            message = new ResultMessage(ResultMessage.Success, getText("controller.processRun.del.success"));
        }
        catch (Exception e)
        {
            message = new ResultMessage(ResultMessage.Fail, getText("controller.processRun.del.fail"));
        }
        addMessage(message, request);
        response.sendRedirect(preUrl);
    }
    
    /**
     * 编辑流程实例
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("edit")
    @Action(description = "编辑流程实例扩展")
    public ModelAndView edit(HttpServletRequest request)
        throws Exception
    {
        Long runId = RequestUtil.getLong(request, "runId");
        String returnUrl = RequestUtil.getPrePage(request);
        ProcessRun processRun = null;
        if (runId != 0)
        {
            processRun = processRunService.getById(runId);
        }
        else
        {
            processRun = new ProcessRun();
        }
        return getAutoView().addObject("processRun", processRun).addObject("returnUrl", returnUrl);
    }
    
    /**
     * 取得流程实例扩展明细
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("get")
    @Action(description = "查看流程实例扩展明细")
    public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        long runId = RequestUtil.getLong(request, "runId", 0L);
        String taskId = RequestUtil.getString(request, "taskId");
        long actInstId = RequestUtil.getLong(request, "actInstId", 0L);
        
        // String preUrl=RequestUtil.getPrePage(request);
        ProcessRun processRun = null;
        // String actInstId="";
        if (runId != 0L)
        {
            processRun = processRunService.getById(runId);
        }
        else if (actInstId != 0L)
        {
            processRun = processRunService.getByActInstanceId(actInstId);
        }
        else
        {
            TaskEntity task = bpmService.getTask(taskId);
            processRun = processRunService.getByActInstanceId(new Long(task.getProcessInstanceId()));
        }
        int link = RequestUtil.getInt(request, "link");
        if (processRun == null)
            throw new Exception("实例不存在");
        List<HistoricTaskInstance> hisTasks = bpmService.getHistoryTasks(processRun.getActInstId());
        return getAutoView().addObject("processRun", processRun)
            .addObject("isReturn", request.getParameter("isReturn"))
            .addObject("hisTasks", hisTasks)
            // .addObject("returnUrl", preUrl)
            .addObject("link", link);
    }
    
    /**
     * 取得流程实例扩展明细
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("detail")
    @Action(description = "查看流程实例扩展明细")
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        long runId = RequestUtil.getLong(request, "runId", 0L);
        String preUrl = RequestUtil.getPrePage(request);
        ProcessRun processRun = null;
        // String actInstId="";
        if (runId != 0L)
        {
            processRun = processRunService.getById(runId);
        }
        
        // List<HistoricTaskInstance> hisTasks=bpmService.getHistoryTasks(processRun.getActInstId());
        return getAutoView().addObject("processRun", processRun).addObject("returnUrl", preUrl);
    }
    
    /**
     * 任务办理页面的 流程示意图对话框
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("processImage")
    public ModelAndView processImage(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView modelAndView = getAutoView();
        
        String action = request.getParameter("action");
        String runId = request.getParameter("runId");
        String actInstanceId = null;
        ProcessRun processRun = null;
        if (StringUtils.isNotEmpty(runId))
        {
            processRun = processRunService.getById(new Long(runId));
            actInstanceId = processRun.getActInstId();
        }
        else
        {
            actInstanceId = request.getParameter("actInstId");
            if (StringUtil.isEmpty(actInstanceId))
            {
                log.error("actInstanceId is null :" + actInstanceId);
                return null;
            }
            processRun = processRunService.getByActInstanceId(new Long(actInstanceId));
            
        }
        String defXml = bpmService.getDefXmlByProcessDefinitionId(processRun.getActDefId());
        ExecutionEntity executionEntity = bpmService.getExecution(actInstanceId);
        
        if (executionEntity != null && executionEntity.getSuperExecutionId() != null)
        {
            ExecutionEntity superExecutionEntity = bpmService.getExecution(executionEntity.getSuperExecutionId());
            modelAndView.addObject("superInstanceId", superExecutionEntity.getProcessInstanceId());
        }
        List<FlowStatus> barFlowStatus = flowStatusService.getBarFlowStatus();
        
        ShapeMeta shapeMeta = BpmUtil.transGraph(defXml);
        modelAndView.addObject("notShowTopBar", request.getParameter("notShowTopBar"))
            .addObject("defXml", defXml)
            .addObject("processInstanceId", actInstanceId)
            .addObject("shapeMeta", shapeMeta)
            .addObject("processRun", processRun)
            .addObject("barFlowStatus", barFlowStatus)
            .addObject("action", action);
        return modelAndView;
        
    }
    /**
     * 流程图以及审批意见
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("processImageList")
    public ModelAndView processImageList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView modelAndView = getAutoView();
        
        String action = request.getParameter("action");
        String runId = request.getParameter("runId");
        String actInstanceId = null;
        ProcessRun processRun = null;
        if (StringUtils.isNotEmpty(runId))
        {
            processRun = processRunService.getById(new Long(runId));
            actInstanceId = processRun.getActInstId();
        }
        else
        {
            actInstanceId = request.getParameter("actInstId");
            if (StringUtil.isEmpty(actInstanceId))
            {
                log.error("actInstanceId is null :" + actInstanceId);
                return null;
            }
            processRun = processRunService.getByActInstanceId(new Long(actInstanceId));
            
        }
        String defXml = bpmService.getDefXmlByProcessDefinitionId(processRun.getActDefId());
        ExecutionEntity executionEntity = bpmService.getExecution(actInstanceId);
        
        if (executionEntity != null && executionEntity.getSuperExecutionId() != null)
        {
            ExecutionEntity superExecutionEntity = bpmService.getExecution(executionEntity.getSuperExecutionId());
            modelAndView.addObject("superInstanceId", superExecutionEntity.getProcessInstanceId());
        }
        List<FlowStatus> barFlowStatus = flowStatusService.getBarFlowStatus();
        List<TaskOpinion> list=taskOpinionService.getByActInstId(actInstanceId);
      //设置代码执行人
      		list = taskOpinionService.setTaskOpinionExecutor(list);
        ShapeMeta shapeMeta = BpmUtil.transGraph(defXml);
        modelAndView.addObject("notShowTopBar", request.getParameter("notShowTopBar"))
            .addObject("defXml", defXml)
            .addObject("processInstanceId", actInstanceId)
            .addObject("shapeMeta", shapeMeta)
            .addObject("processRun", processRun)
            .addObject("barFlowStatus", barFlowStatus)
            .addObject("taskOpinionList",list)
            .addObject("action", action);
        return modelAndView;
        
    }
    /**
     * 任务办理页面的 流程示意图对话框
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("processImageDiv")
    public ModelAndView processImageDiv(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
    	return processImage(request, response);
    }
    
    /**
     * 查看子流程。
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("subFlowImage")
    public ModelAndView subFlowImage(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String subProcessDefinitionId = null;
        List<String> subProcessInstanceId = new ArrayList<String>();
        String subDefXml = null;
        String actInstanceId = request.getParameter("processInstanceId");
        String processDefinitionId = request.getParameter("processDefinitionId");
        String nodeId = request.getParameter("nodeId");
        // 子流程是否已经运行 0-未运行，1-已运行
        int subProcessRun = 0;
        // 取得子外部子流程的key.
        String subFlowKey = null;
        String actDefId = null;
        if (StringUtil.isNotEmpty(actInstanceId))
        {
            actDefId = processRunService.getByActInstanceId(new Long(actInstanceId)).getActDefId();
        }
        else if (StringUtil.isNotEmpty(processDefinitionId))
        {
            actDefId = processDefinitionId;
        }
        
        Map<String, FlowNode> flowNodeMap = NodeCache.getByActDefId(actDefId);
        Iterator<Entry<String, FlowNode>> entrySet = flowNodeMap.entrySet().iterator();
        while (entrySet.hasNext())
        {
            Entry<String, FlowNode> entry = entrySet.next();
            String flowNodeId = entry.getKey();
            if (flowNodeId.equals(nodeId))
            {
                FlowNode flowNode = entry.getValue();
                subFlowKey = flowNode.getAttribute("subFlowKey");
                break;
            }
        }
        // 取得外部子流程的定义
        Definition subDefinition = bpmDefinitionService.getMainDefByActDefKey(subFlowKey);
        if (subDefinition.getActDeployId() != null)
        {
            subDefXml = bpmService.getDefXmlByDeployId(subDefinition.getActDeployId().toString());
        }
        else
        {
            subDefXml =
                BpmUtil.transform(subDefinition.getDefKey(), subDefinition.getSubject(), subDefinition.getDefXml());
        }
        
        // 取得所有的子流程实例
        List<HistoricProcessInstance> historicProcessInstances =
            historyService.createHistoricProcessInstanceQuery().superProcessInstanceId(actInstanceId).list();
        if (BeanUtils.isNotEmpty(historicProcessInstances))
        {
            // 筛选当选节点的子流程
            for (HistoricProcessInstance instance : historicProcessInstances)
            {
                String procDefId = instance.getProcessDefinitionId();
                Definition bpmDef = bpmDefinitionService.getByActDefId(procDefId);
                if (bpmDef.getDefKey().equals(subFlowKey))
                {
                    subProcessInstanceId.add(instance.getId());
                    subProcessRun = 1;
                }
            }
        }
        if (subProcessRun == 0)
        {
            subProcessDefinitionId = subDefinition.getActDefId();
        }
        
        ShapeMeta subShapeMeta = BpmUtil.transGraph(subDefXml);
        ModelAndView modelAndView = getAutoView();
        modelAndView.addObject("defXml", subDefXml);
        modelAndView.addObject("subProcessRun", subProcessRun);
        if (subProcessRun == 0)
        {
            modelAndView.addObject("processDefinitionId", subProcessDefinitionId);
        }
        else
        {
            modelAndView.addObject("processInstanceIds", subProcessInstanceId);
        }
        modelAndView.addObject("shapeMeta", subShapeMeta);
        return modelAndView;
        
    }
    
    /**
     * 根据流程实例id获取流程的状态。
     * @param request
     * @return
     */
    @RequestMapping("getFlowStatusByInstanceId")
    @ResponseBody
    public List<TaskNodeStatus> getFlowStatusByInstanceId(HttpServletRequest request)
    {
        String instanceId = RequestUtil.getString(request, "instanceId");
        List<TaskNodeStatus> list = bpmService.getNodeCheckStatusInfo(instanceId);
        return list;
    }
    
    /**
     * 根据流程实例id获取流程的状态。
     * @param request
     * @return
     */
    @RequestMapping("getTaskStatusByInstanceId")
    @ResponseBody
    public TaskNodeStatus getTaskStatusByInstanceId(HttpServletRequest request)
    {
        String instanceId = RequestUtil.getString(request, "instanceId");
        String nodeId = RequestUtil.getString(request, "nodeId");
        TaskNodeStatus taskNodeStatus = bpmService.getNodeCheckStatusInfo(instanceId, nodeId);
        return taskNodeStatus;
    }
    
    /**
     * 显示流程实例任务中某个任务节点上的执行人员。
     * <pre>
     * 根据节点ID和流程实例ID获取节点的执行人。
     * </pre>
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("taskUser")
    public ModelAndView taskUser(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        // 当前用户执行人员ID
        Set<TaskExecutor> assignees = new HashSet<TaskExecutor>();
        // 当前节点的候选执行人员ID
        Set<TaskExecutor> candidateUsers = new HashSet<TaskExecutor>();
        
        String nodeId = request.getParameter("nodeId");
        String processInstanceId = request.getParameter("processInstanceId");
        
        ModelAndView modelAndView = getAutoView();
        modelAndView.addObject("assignees", assignees).addObject("candidateUsers", candidateUsers);
        boolean found = false;
        
        // 1.检查该流程实例中的当前任务列表，若nodeId对应的任务存在，即直接从该任务里获取指定的人员或候选用户
        List<ProcessTask> taskList = bpmService.getTasks(processInstanceId);
        // nodeId对应的任务节点是否有任务实例
        for (ProcessTask task : taskList)
        {
            if (task.getTaskDefinitionKey().equals(nodeId))
            {
                found = true;
                if (FlowUtil.isAssigneeNotEmpty(task.getAssignee()))
                {// 存在指定的用户
                    Long assignee = Long.parseLong(task.getAssignee());
                    ISysUser sysUser = sysUserService.getById(assignee);
                    assignees.add(TaskExecutor.getTaskUser(task.getAssignee(), sysUser.getFullname()));
                }
                else
                {// 获取该任务的候选用户列表
                    Set<TaskExecutor> cUIds = taskUserService.getCandidateExecutors(task.getId());
                    for (TaskExecutor taskExecutor : cUIds)
                    {
                        candidateUsers.add(taskExecutor);
                    }
                }
            }
        }
        if (found)
            return modelAndView;
        // 2.检查审批历史表里是否存在该节点的审核人
        List<TaskOpinion> list = taskOpinionService.getByActInstIdTaskKey(new Long(processInstanceId), nodeId);
        for (TaskOpinion taskOpinion : list)
        {
            if (taskOpinion.getExeUserId() != null)
            {
                assignees
                    .add(TaskExecutor.getTaskUser(taskOpinion.getExeUserId().toString(), taskOpinion.getExeFullname()));
            }
        }
        return modelAndView;
    }
    
    /**
     * 返回流程表单列表包括全局表单
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getForm")
    public ModelAndView getForm(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        
        Long runId = RequestUtil.getLong(request, "runId", 0L);
        Long actInstId = RequestUtil.getLong(request, "actInstId", 0L);
        String ctxPath = request.getContextPath();
        Map<String, Object> formMap = null;
        ProcessRun processRun = null;
        if (runId.longValue() != 0L)
        {
            formMap = getFormByRunId(runId, ctxPath);
            processRun = processRunService.getById(runId);
            
        }
        else if (actInstId.longValue() != 0L)
        {
            processRun = processRunService.getByActInstanceId(actInstId);
            runId = processRun.getRunId();
            formMap = getFormByRunId(runId, ctxPath);
            
        }
        ModelAndView view = this.getAutoView()
            .addObject("processRun", processRun)
            .addObject("isFormEmpty", formMap.get("isFormEmpty"))
            .addObject("isExtForm", formMap.get("isExtForm"))
            .addObject("form", formMap.get("form"));
        return view;
    }
    
    /**
     * 获取流程表单明细，
     * 结束的流程取业务实例表单
     * 正在运行的实例取当前节点绑定的表单
     * 如果当前运行实例找到节点绑定的表单，这种情况在子流程中比较常见， 那么就取流程业务实例表单。
     * @param runId
     * @param ctxPath
     * @return
     * @throws Exception
     */
    private Map<String, Object> getFormByRunId(Long runId, String ctxPath)
        throws Exception
    {
        Map<String, Object> formMap = new HashMap<String, Object>();
        ProcessRun processRun = processRunService.getById(new Long(runId));
        String parentActDefId = processRunService.getParentProcessRunActDefId(processRun);
        String businessKey = processRun.getBusinessKey();
        Long currUserId = UserContextUtil.getCurrentUserId();
        Long defId = processRun.getDefId();
        // 结束的流程实例
        String formUrl = "";
        Long formDefId = 0L;
        boolean isExtForm = false;
        boolean isFormEmpty = false;
        String headHtml = "";
        String nodeId = "";
        // String taskId="";
        String form = "";
        if (processRun.getStatus() != 1)
        {
            if (processRun.getFormDefId() == 0L && StringUtil.isNotEmpty(processRun.getBusinessUrl()))
            {
                isExtForm = true;
                formUrl = processRun.getBusinessUrl();
            }
            else
            {
                formDefId = processRun.getFormDefId();
            }
            
        }
        else
        {
            //正在运行的流程实例 取当前任务的表单
            List<ProcessTask> taskList = processRunService.getTasksByRunId(runId);
            for (ProcessTask task : taskList)
            {
                if (StringUtil.isNotEmpty(task.getTaskDefinitionKey()))
                {
                    nodeId = task.getTaskDefinitionKey();
                    // taskId=task.getId();
                    break;
                }
            }
            // 可以取得节点的情况。
            NodeSet bpmNodeSet = null;
            bpmNodeSet = bpmNodeSetService.getByDefIdNodeId(defId, nodeId);
            if (bpmNodeSet == null && StringUtil.isEmpty(parentActDefId))
            {
                bpmNodeSet = bpmNodeSetService.getByDefIdNodeId(defId, nodeId);
                if (BeanUtils.isEmpty(bpmNodeSet) || bpmNodeSet.getFormType() == -1)
                {
                    bpmNodeSet = bpmNodeSetService.getBySetType(defId, NodeSet.SetType_GloabalForm);
                }
            }
            else if (bpmNodeSet == null)
            {
                bpmNodeSet = bpmNodeSetService.getByDefIdNodeId(defId, nodeId, parentActDefId);
                if ((BeanUtils.isEmpty(bpmNodeSet)) || (bpmNodeSet.getFormType().shortValue() == -1))
                {
                    bpmNodeSet =
                        this.bpmNodeSetService.getBySetType(defId, NodeSet.SetType_GloabalForm, parentActDefId);
                }
                
                if (BeanUtils.isNotEmpty(bpmNodeSet))
                {
                    if (NodeSet.FORM_TYPE_URL.equals(bpmNodeSet.getFormType()))
                    {
                        isExtForm = true;
                        formUrl =
                            bpmNodeSet.getDetailUrl() != null ? bpmNodeSet.getDetailUrl() : bpmNodeSet.getFormUrl();
                    }
                    else
                    {
                        formDefId = bpmNodeSet.getFormKey();
                    }
                }
            }
            else if (BeanUtils.isEmpty(bpmNodeSet) || bpmNodeSet.getFormType() == -1)
            {
                // 节点没有绑定表单就是用全局表单
                bpmNodeSet = bpmNodeSetService.getBySetType(defId, NodeSet.SetType_GloabalForm);
            }
            if (BeanUtils.isNotEmpty(bpmNodeSet))
            {
                if (NodeSet.FORM_TYPE_URL.equals(bpmNodeSet.getFormType()))
                {
                    isExtForm = true;
                    formUrl = bpmNodeSet.getDetailUrl() != null ? bpmNodeSet.getDetailUrl() : bpmNodeSet.getFormUrl();
                }
                else
                {
                    formDefId = bpmNodeSet.getFormKey();
                }
            }
            
        }
        if (isExtForm)
        {
            if (StringUtil.isNotEmpty(businessKey))
            {
                // 替换主键。
                form = formUrl.replaceFirst(BpmConst.FORM_PK_REGEX, businessKey);
                if (!formUrl.startsWith("http"))
                {
                    form = ctxPath + form;
                }
            }
        }
        else
        {
            // 解决某些流程子表不显示的问题。 主要问题是当流程状态为正在执行时 不能获取子表显示
            if (processRun.getStatus() != 2 && processRun.getStatus() != 3)
            {

                String tempNodeId = "";
                String tempTaskId = "";
                for (ProcessTask task : processRunService.getTasksByRunId(runId))
                {
                    if (StringUtil.isNotEmpty(task.getTaskDefinitionKey()))
                    {
                        tempNodeId = task.getTaskDefinitionKey();
                        tempTaskId = task.getId();
                        break;
                    }
                }
                if (StringUtil.isNotEmpty(tempTaskId))
                {
                    form =
                        this.bpmFormDefService
                            .getForm(processRun,
                                tempNodeId,
                                Long.valueOf(0L),
                                ctxPath,
                                this.taskService.getVariables(tempTaskId))
                            .getFormHtml();
                }
                else
                {
                    if(formDefId==0L){
                    	formDefId = processRun.getFormDefId();
                    }
                    form = this.bpmFormHandlerService
                        .getFormDetail(formDefId, businessKey, currUserId, processRun, ctxPath, false);
                }
                
            
                
            }
            else
            {
                form =
                    bpmFormHandlerService.getFormDetail(formDefId, businessKey, currUserId, processRun, ctxPath, false);
            }
            IDataTemplate bpmDataTemplate = dataTemplateService.getByFormKey(formDefId);
            if (bpmDataTemplate != null)
            {
                headHtml = dataTemplateService.getFormHeadHtml(bpmDataTemplate.getId(), ctxPath);
            }
            
        }
        
        if (StringUtil.isEmpty(form))
        {
            isFormEmpty = true;
        }
        formMap.put("form", form);
        formMap.put("isFormEmpty", isFormEmpty);
        formMap.put("isExtForm", isExtForm);
        formMap.put("headHtml", headHtml);
        
        return formMap;
    }
    
    /**
     * 我的流程草稿列表
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("myForm")
    @Action(description = "我的流程草稿列表")
    public ModelAndView myForm(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long userId = UserContextUtil.getCurrentUserId();
        QueryFilter filter = new QueryFilter(request, "processRunItem");
        filter.addFilterForIB("userId", userId);
        List<ProcessRun> list = processRunService.getMyDraft(filter);
        ModelAndView mv = this.getAutoView().addObject("processRunList", list);
        return mv;
    }
    
    /**
     * 删除流程草稿同时删除业务数据
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("delDraft")
    @Action(description = "删除流程草稿", execOrder = ActionExecOrder.BEFORE, detail = "删除流程草稿"
        + "<#list StringUtils.split(runId,\",\") as item>"
        + "<#assign entity=processRunService.getById(Long.valueOf(item))/>" + "【${entity.subject}】" + "</#list>")
    public void delDraft(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ResultMessage message = null;
        Long runId = RequestUtil.getLong(request, "runId", 0L);
        String preUrl = RequestUtil.getPrePage(request);
        try
        {
            ProcessRun processRun = processRunService.getById(runId);
            String dsAlias = processRun.getDsAlias();
            String tableName = processRun.getTableName();
            String businessKey = processRun.getBusinessKey();
            if (StringUtil.isNotEmpty(tableName))
            {
                if (StringUtil.isEmpty(dsAlias) || ISysDataSource.DS_LOCAL.equals(dsAlias))
                {
                    bpmFormHandlerService.delByIdTableName(businessKey, ITableModel.CUSTOMER_TABLE_PREFIX + tableName);
                }
                else
                {
                    bpmFormHandlerService.delByDsAliasAndTableName(dsAlias, tableName, businessKey);
                }
            }
            bpmRunLogService
                .addRunLog(runId, RunLog.OPERATOR_TYPE_DELETEFORM, getText("controller.processRun.delDraft"));
            processRunService.delById(runId);
            message = new ResultMessage(ResultMessage.Success,
                getText("record.deleted", getText("controller.processRun.delDraft.draft")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            message = new ResultMessage(ResultMessage.Fail,
                getText("record.delete.fail", getText("controller.processRun.delDraft.draft")) + "：" + e.getMessage());
        }
        addMessage(message, request);
        response.sendRedirect(preUrl);
    }
    
    @RequestMapping("checkForm")
    @Action(description = "检查草稿表单是否发生变化")
    @ResponseBody
    public boolean checkForm(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long runId = RequestUtil.getLong(request, "runId");
        ProcessRun processRun = processRunService.getById(runId);
        Long formDefId = processRun.getFormDefId();
        if (formDefId == 0L)
            return false;
        IFormDef bpmFormDef = bpmFormDefService.getById(formDefId);
        IFormDef defaultFormDef = bpmFormDefService.getDefaultPublishedByFormKey(bpmFormDef.getFormKey());
        if (defaultFormDef.getFormDefId().equals(formDefId))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /**
     * 复制我的草稿
     */
    @RequestMapping("copyDraft")
    @Action(description = "复制草稿", execOrder = ActionExecOrder.AFTER, detail = "复制草稿"
        + "<#assign entity=processRunService.getById(Long.valueOf(runId))/>" + "【${entity.subject}】"
        + "<#if isSuccess>成功<#else>失败</#if>")
    public void copyDraft(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long runId = RequestUtil.getLong(request, "runId");
        boolean isSuccess = true;
        try
        {
            processRunService.copyDraft(runId);
            writeResultMessage(response.getWriter(),
                new ResultMessage(ResultMessage.Success, getText("controller.processRun.copyDraft.success")));
            isSuccess = true;
        }
        catch (Exception e)
        {
            isSuccess = false;
            e.printStackTrace();
            writeResultMessage(response.getWriter(),
                new ResultMessage(ResultMessage.Fail,
                    getText("controller.processRun.copyDraft.fail") + e.getMessage()));
        }
        LogThreadLocalHolder.putParamerter("isSuccess", isSuccess);
    }
    
    /**
     * 我的请求列表
     * 
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("myRequestList")
    @Action(description = "查看我的请求列表")
    public ModelAndView myRequestList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        QueryFilter filter = new QueryFilter(request, "processRunItem");
        filter.addFilterForIB("creatorId", UserContextUtil.getCurrentUserId());
        String nodePath = RequestUtil.getString(request, "nodePath");
        if (StringUtils.isNotEmpty(nodePath))
            filter.getFilters().put("nodePath", nodePath + "%");
        List<ProcessRun> list = processRunService.getMyRequestList(filter);
        
        ModelAndView mv = this.getAutoView().addObject("processRunList", list);
        return mv;
    }
    
    /**
     * 我的办结 列表
     * 
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("myCompletedList")
    @Action(description = "查看我的办结列表")
    public ModelAndView myCompletedList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        QueryFilter filter = new QueryFilter(request, "processRunItem");
        filter.addFilterForIB("creatorId", UserContextUtil.getCurrentUserId());
        String nodePath = RequestUtil.getString(request, "nodePath");
        if (StringUtils.isNotEmpty(nodePath))
            filter.getFilters().put("nodePath", nodePath + "%");
        List<ProcessRun> list = processRunService.getMyCompletedList(filter);
        return this.getAutoView().addObject("processRunList", list);
    }
    
    /**
     * 查看已办事宜流程列表
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("alreadyMatters")
    public ModelAndView alreadyMatters(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        return getAutoView();
    }
    
    /**
     * 导出全部已办任务
     */
    @RequestMapping("downloadAlreadyMattersList")
    @Action(description = "导出全部已办任务")
    public void downloadAlreadyMattersList(HttpServletRequest request, HttpServletResponse response)
    {
        QueryFilter filter = new QueryFilter(request, "processRunItem");
        String nodePath = RequestUtil.getString(request, "nodePath");
        String defId = RequestUtil.getString(request, "defId");
        if (StringUtils.isNotEmpty(nodePath))
        {
            filter.getFilters().put("nodePath", nodePath + "%");
            
        }
        if (StringUtils.isNotEmpty(defId))
        {
            filter.getFilters().put("defId", defId);
        }
        filter.addFilterForIB("assignee", UserContextUtil.getCurrentUserId().toString());
        List<ProcessRun> list = processRunService.getAlreadyMattersList(filter);
        List<Object[]> vals = new ArrayList<Object[]>();
        for (int i = 0; i < list.size(); i++)
        {
            ProcessRun processRun = list.get(i);
            if (processRun.getStatus().shortValue() != ProcessRun.STATUS_FINISH.shortValue())
            {
                // 1.查找当前用户是否有最新审批的任务
                TaskOpinion taskOpinion = taskOpinionService.getLatestUserOpinion(processRun.getActInstId(),
                    UserContextUtil.getCurrentUserId());
                if (BeanUtils.isNotEmpty(taskOpinion))
                {
                    processRun.setRecover(ProcessRun.STATUS_RECOVER);
                }
            }
            Object[] objs = new Object[8];
            objs[0] = i + 1;
            objs[1] = processRun.getSubject();
            objs[2] = processRun.getProcessName();
            objs[3] = processRun.getCreator();
            objs[4] = processRun.getCreatetime();
            Short status = processRun.getStatus();
            if (status == 10 || status == 2 || status == 3)
            {
                objs[5] = TimeUtil.getTime(processRun.getDuration());
            }
            else
            {
                objs[5] = TimeUtil.getDurationTime(processRun.getCreatetime());
            }
            objs[6] = getProcessStatus(processRun.getStatus());
            if (status != 2 && status != 5)
            {
                objs[7] = "追回";
            }
            vals.add(objs);
        }
        String[] headers = {"序号", "请求标题", "流程名称", "创建人", "创建时间", "持续时间", "状态", "管理"};
        ExcelUtil.exportToExcel("全部已办任务", headers, vals, response);
    }
    
    private String getProcessStatus(Short status)
    {
        switch (status)
        {
            case 0:
                return "挂起";
            case 1:
                return "正在运行";
            case 2:
                return "结束";
            case 3:
                return "人工结束";
            case 4:
                return "草稿";
            case 5:
                return "已撤销";
            case 6:
                return "已驳回";
            case 7:
                return "已追回";
            case 10:
                return "逻辑删除";
            default:
                break;
        }
        return "";
    }
    
    /**
     * 查看已办事宜流程列表
     * 
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("alreadyMattersList")
    @Action(description = "查看已办事宜流程列表")
    public ModelAndView alreadyMattersList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        QueryFilter filter = new QueryFilter(request, "processRunItem");
        String nodePath = RequestUtil.getString(request, "nodePath");
        String defId = RequestUtil.getString(request, "defId");
        if (StringUtils.isNotEmpty(nodePath))
        {
            filter.getFilters().put("nodePath", nodePath + "%");
            
        }
        if (StringUtils.isNotEmpty(defId))
        {
            filter.getFilters().put("defId", defId);
        }
        filter.addFilterForIB("assignee", UserContextUtil.getCurrentUserId().toString());
        List<ProcessRun> list = processRunService.getAlreadyMattersList(filter);
        for (ProcessRun processRun : list)
        {
            if (processRun.getStatus().shortValue() != ProcessRun.STATUS_FINISH.shortValue())
            {
                // 1.查找当前用户是否有最新审批的任务
                TaskOpinion taskOpinion = taskOpinionService.getLatestUserOpinion(processRun.getActInstId(),
                    UserContextUtil.getCurrentUserId());
                if (BeanUtils.isNotEmpty(taskOpinion))
                {
                    processRun.setRecover(ProcessRun.STATUS_RECOVER);
                }
            }
        }
        
        return this.getAutoView().addObject("processRunList", list);
    }
    
    /**
     * 导出全部办结任务
     * @param request
     * @param response
     */
    @RequestMapping("downloadAllCompletedMattersList")
    @Action(description = "导出全部办结任务")
    public void downloadAllCompletedMattersList(HttpServletRequest request, HttpServletResponse response)
    {
        QueryFilter filter = new QueryFilter(request, "processRunItem");
        String nodePath = RequestUtil.getString(request, "nodePath");
        if (StringUtils.isNotEmpty(nodePath))
        {
            filter.getFilters().put("nodePath", nodePath + "%");
        }
        filter.addFilterForIB("assignee", UserContextUtil.getCurrentUserId().toString());
        List<ProcessRun> list = processRunService.getCompletedMattersList(filter);
        List<Object[]> vals = new ArrayList<Object[]>();
        
        String[] headers = {"序号", "请求标题", "流程名称", "创建人", "创建时间", "结束时间", "持续时间", "状态"};
        for (int i = 0; i < list.size(); i++)
        {
            ProcessRun processRun = list.get(i);
            Definition bpmDefinition = bpmDefinitionService.getByActDefId(processRun.getActDefId());
            if (BeanUtils.isNotEmpty(bpmDefinition) && bpmDefinition.getIsPrintForm() == 1)
            {
                processRun.setIsPrintForm(bpmDefinition.getIsPrintForm());
            }
            Object[] objs = new Object[headers.length];
            objs[0] = i + 1;
            objs[1] = processRun.getSubject();
            objs[2] = processRun.getProcessName();
            objs[3] = processRun.getCreator();
            objs[4] = processRun.getCreatetime();
            objs[5] = processRun.getEndTime();
            objs[6] = TimeUtil.getTime(processRun.getDuration());
            objs[7] = getProcessStatus(processRun.getStatus());
            vals.add(objs);
        }
        
        ExcelUtil.exportToExcel("全部办结任务", headers, vals, response);
    }
    
    /**
     * 查看办结事宜流程列表
     * 
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("completedMattersList")
    @Action(description = "查看办结事宜流程列表")
    public ModelAndView completedMattersList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        QueryFilter filter = new QueryFilter(request, "processRunItem");
        String nodePath = RequestUtil.getString(request, "nodePath");
        if (StringUtils.isNotEmpty(nodePath))
            filter.getFilters().put("nodePath", nodePath + "%");
        filter.addFilterForIB("assignee", UserContextUtil.getCurrentUserId().toString());
        List<ProcessRun> list = processRunService.getCompletedMattersList(filter);
        
        for (ProcessRun processRun : list)
        {
            Definition bpmDefinition = bpmDefinitionService.getByActDefId(processRun.getActDefId());
            if (BeanUtils.isNotEmpty(bpmDefinition) && bpmDefinition.getIsPrintForm() == 1)
            {
                processRun.setIsPrintForm(bpmDefinition.getIsPrintForm());
            }
        }
        
        ModelAndView mv = this.getAutoView().addObject("processRunList", list);
        return mv;
    }
    
    /**
     * 返回流程的详细信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("info")
    public ModelAndView info(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long curUserId = UserContextUtil.getCurrentUserId();
        String runId = request.getParameter("runId");
        String copyId = request.getParameter("copyId");
        String prePage = RequestUtil.getString(request, "prePage");
        String preUrl = RequestUtil.getPrePage(request);
        String ctxPath = request.getContextPath();
        ProcessRun processRun = processRunService.getById(Long.parseLong(runId));
        Long formKey = 0L;
        Map<String, Object> formMap = getFormByRunId(Long.parseLong(runId), ctxPath);
        if (BeanUtils.isNotEmpty(processRun.getFormDefId()))
        {
            IFormDef def = bpmFormDefService.getById(processRun.getFormDefId());
            if (BeanUtils.isNotEmpty(def))
            {
                formKey = def.getFormKey();
            }
        }
        Definition bpmDefinition = bpmDefinitionService.getById(processRun.getDefId());
        // 是否是第一个节点
        boolean isFirst = this.isFirst(processRun);
        // 是否允许办结转发
        boolean isFinishedDiver = this.isFinishedDiver(bpmDefinition, processRun);
        
        // 是否允许撤销
        boolean isCanRecover = this.isCanRecover(processRun, isFirst, curUserId);
        
        // 是否允许追回
        boolean isCanRedo = this.isCanRedo(processRun, isFirst, curUserId);
        // 节点审批意见模板--从常用语设置中读取
        boolean isCopy = this.isCopy(bpmDefinition);
        //是否结束
        boolean isEnd = processRun.getEndTime()==null?false:true;
        if (null == prePage || "".equals(prePage))
        {// 抄送事宜修改状态
            try
            {
                // 适用于冒泡不能传copyId
                if (null == copyId || "".equals(copyId))
                {
                    ProcessRun run = processRunService.getCopyIdByRunid(Long.parseLong(runId));
                    copyId = run.getCopyId().toString();
                }
                bpmProCopytoService.markCopyStatus(copyId);
            }
            catch (Exception e)
            {
                logger.info("update copy matter state failed!");
            }
        }
        
        // 是否允许打印
        boolean isPrintForm = this.isPrintForm(processRun, bpmDefinition, prePage, copyId);
        TaskOpinion taskOpinion = null;
        if (processRun.getStatus().shortValue() != ProcessRun.STATUS_FINISH.shortValue())
        {
            // 1.查找当前用户是否有最新审批的任务
            taskOpinion =
                taskOpinionService.getLatestUserOpinion(processRun.getActInstId(), UserContextUtil.getCurrentUserId());
        }
        // 判断是否显示意见框----bpmNodeSet.isHideOption
        NodeSet bpmNodeSet = null;
        if (taskOpinion != null)
        {
            String taskKey = taskOpinion.getTaskKey();
            if (StringUtils.isNotEmpty(taskKey))
            {
                bpmNodeSet = nodeSetService.getByActDefIdNodeId(taskOpinion.getActDefId(), taskKey);
            }
            else
            {
                bpmNodeSet = nodeSetService.getByStartGlobal(bpmDefinition.getDefId());
            }
        }
        
        // 是否是审批意见模板
        String spyjModel = taskAppItemService.getSpyjModel(bpmDefinition, taskOpinion);
        
        return this.getAutoView()
            .addObject("bpmDefinition", bpmDefinition)
            .addObject("processRun", processRun)
            .addObject("form", formMap.get("form"))
            .addObject("headHtml", formMap.get("headHtml"))
            .addObject("isPrintForm", isPrintForm)
            .addObject("isExtForm", formMap.get("isExtForm"))
            .addObject("isFirst", isFirst)
            .addObject("isCanRedo", isCanRedo)
            .addObject("isCanRecover", isCanRecover)
            .addObject("isFinishedDiver", isFinishedDiver)
            .addObject("isCopy", isCopy)
            .addObject(RequestUtil.RETURNURL, preUrl)
            .addObject("isOpenDialog", RequestUtil.getInt(request, "isOpenDialog", 0))
            .addObject("taskOpinion", taskOpinion)
            .addObject("spyjModel", StringUtil.isEmpty(spyjModel))
            .addObject("bpmNodeSet", bpmNodeSet)
            .addObject("isEnd",isEnd)
            .addObject("formKey", formKey);
        
    }
    
    private boolean isCopy(Definition bpmDefinition)
    {
        Short status = bpmDefinition.getStatus();
        if (Definition.STATUS_DISABLED.equals(status))
            return false;
        if (Definition.STATUS_INST_DISABLED.equals(status))
            return false;
        return true;
    }
    
    /**
     *是否允许办结转办
     * @param bpmDefinition
     * @param processRun
     * @return
     */
    private boolean isFinishedDiver(Definition bpmDefinition, ProcessRun processRun)
    {
        if (Definition.STATUS_INST_DISABLED.equals(bpmDefinition.getStatus()))
            return false;
        if (BeanUtils.isNotEmpty(bpmDefinition.getAllowFinishedDivert()))
            return bpmDefinition.getAllowFinishedDivert().shortValue() == Definition.ALLOW.shortValue()
                && processRun.getStatus().shortValue() == ProcessRun.STATUS_FINISH.shortValue();
        
        return false;
    }
    
    /**
     * 是否能撤销
     *  <pre>
     *  	不是第一节点，是当前执行人并且是正在运行的流程 对应流程定义不能为禁用流程实例状态
     *  </pre>
     * @param processRun
     * @param isFirst
     * @param curUserId
     * @return
     */
    private boolean isCanRecover(ProcessRun processRun, boolean isFirst, Long curUserId)
    {
        String actDefId = processRun.getActDefId();
        Definition bpmDefinition = bpmDefinitionService.getByActDefId(actDefId);
        if (Definition.STATUS_INST_DISABLED.equals(bpmDefinition.getStatus()))
            return false;
        return !isFirst && curUserId.equals(processRun.getCreatorId())
            && processRun.getStatus().shortValue() == ProcessRun.STATUS_RUNNING;
    }
    
    /**
     * 是否可以追回
     * <pre>
     * 是否可以追回，有以下几个判定条件：
     *  1、流程正在运行；
     *  2、流程非第一个节点；
     *  3、上一步执行人是当前用户；
     *  4、上一步操作是同意；
     *  5、目前该实例只有一个任务。
     *  6、 对应流程定义不能为禁用流程实例状态
     * </pre>
     * @param processRun
     * @param isFirst
     * @param curUserId
     * @return
     */
    private boolean isCanRedo(ProcessRun processRun, boolean isFirst, Long curUserId)
    {
        if (!processRun.getStatus().equals(ProcessRun.STATUS_RUNNING) || isFirst)
            return false;
        String actDefId = processRun.getActDefId();
        Definition bpmDefinition = bpmDefinitionService.getByActDefId(actDefId);
        if (Definition.STATUS_INST_DISABLED.equals(bpmDefinition.getStatus()))
            return false;
        String instanceId = processRun.getActInstId();
        TaskOpinion taskOpinion = taskOpinionService.getLatestUserOpinion(instanceId, curUserId);
        if (taskOpinion != null)
        {
            Short checkStatus = taskOpinion.getCheckStatus();
            if (TaskOpinion.STATUS_AGREE.equals(checkStatus))
            {
                String taskKey = taskOpinion.getTaskKey();
                FlowNode flowNode = NodeCache.getNodeByActNodeId(processRun.getActDefId(), taskKey);
                if (flowNode != null)
                {
                    List<FlowNode> nextNodes = flowNode.getNextFlowNodes();
                    List<String> nodeKeys = new ArrayList<String>();
                    for (FlowNode node : nextNodes)
                    {
                        nodeKeys.add(node.getNodeId());
                    }
                    List<ProcessTask> tasks = bpmService.getTasks(instanceId);
                    if (tasks.size() != 1)
                        return false;
                    if (nodeKeys.contains(tasks.get(0).getTaskDefinitionKey()))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * 是否是第一个节点
     * @param processRun
     * @return
     * @throws Exception
     */
    private boolean isFirst(ProcessRun processRun)
        throws Exception
    {
        boolean isFirst = false;
        if (BeanUtils.isEmpty(processRun))
            return isFirst;
        Long instId = Long.parseLong(processRun.getActInstId());
        String actDefId = processRun.getActDefId();
        List<TaskOpinion> taskOpinionList = taskOpinionService.getCheckOpinionByInstId(instId);
        String nodeId = "";
        
        if (NodeCache.isMultipleFirstNode(actDefId))
        {
            nodeId = processRun.getStartNode();
        }
        else
        {
            FlowNode flowNode = NodeCache.getFirstNodeId(actDefId);
            if (flowNode == null)
                return isFirst;
            nodeId = flowNode.getNodeId();
        }
        for (TaskOpinion taskOpinion : taskOpinionList)
        {
            isFirst = nodeId.equals(taskOpinion.getTaskKey());
            if (isFirst)
                break;
        }
        return isFirst;
    }
    
    /**
     * 是否允许打印表单
     * <pre>
     * 	1.允许打印表单
     * 		是我的办结
     * 	2.允许
     * 		办结打印
     * 	3 允许
     * 		抄送打印
     * 
     * </pre>
     * @param processRun
     * @param bpmDefinition
     * @param prePage
     * @param copyId 
     * @return
     */
    private boolean isPrintForm(ProcessRun processRun, Definition bpmDefinition, String prePage, String copyId)
    {
        if (bpmDefinition.getIsPrintForm() == null || bpmDefinition.getIsPrintForm().intValue() != 1
            || processRun.getStatus().shortValue() != ProcessRun.STATUS_FINISH.shortValue())
        {
            return false;
        }
        else
        {
            return true;
        }
        // 我的办结
        // if(("myComplete").equals(prePage) && UserContextUtil.getCurrentUserId().equals(processRun.getCreatorId()) )
        // return true;
        // //办结打印
        // if(("completedMatter").equals(prePage) && bpmDefinition.getAllowEndPrint().intValue() == 1 )
        // return true;
        // //抄送允许打印
        // if(("cc").equals(prePage) && bpmDefinition.getAllowCcPrint().intValue() == 1 )
        // return true;
        
    }
    
    /**
     * 逻辑删除流程实例。
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("logicDelete")
    @Action(description = "逻辑删除流程实例")
    public void logicDelete(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ResultMessage message = null;
        Long instanceId = RequestUtil.getLong(request, "instanceId");
        PrintWriter writer = response.getWriter();
        String memo = RequestUtil.getString(request, "opinion");
        if (instanceId != null && instanceId > 0)
        {
            try
            {
                ProcessRun processRun = bpmService.delProcessByInstanceId(instanceId, memo);
                Long runId = processRun.getRunId();
                String tmp = getText("controller.processRun.logicDelete", processRun.getSubject());
                bpmRunLogService.addRunLog(runId, RunLog.OPERATOR_TYPE_DELETEINSTANCE, tmp);
                message = new ResultMessage(ResultMessage.Success,
                    UserContextUtil.getMessages("controller.processRun.del.success"));
            }
            catch (Exception ex)
            {
                String msg = ExceptionUtil.getExceptionMessage(ex);
                message = new ResultMessage(ResultMessage.Fail, msg);
            }
        }
        else
        {
            message =
                new ResultMessage(ResultMessage.Fail, UserContextUtil.getMessages("controller.processRun.del.fail"));
        }
        writeResultMessage(writer, message);
    }
    
    /**
     * update 2013-1-1 start 打印流程表单
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("printForm")
    public ModelAndView printForm(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long runId = RequestUtil.getLong(request, "runId");
        // int genDefault =RequestUtil.getInt(request, "genDefault");
        ProcessRun processRun = processRunService.getById(runId);
        List<? extends IFormRun> bpmFormRunList = bpmFormRunService.getByInstanceId(processRun.getActInstId());
        Long formKey = 0L;
        if (BeanUtils.isNotEmpty(bpmFormRunList))
        {
            for (IFormRun bpmFormRun : bpmFormRunList)
            {
                if (BeanUtils.isNotEmpty(bpmFormRun.getFormdefKey()))
                {
                    formKey = bpmFormRun.getFormdefKey();
                }
            }
        }
        if (formKey == 0L)
        {
            String actDefId = processRun.getActDefId();
            @SuppressWarnings("unchecked")
            List<NodeSet> list = bpmNodeSetService.getByActDefId(actDefId);
            for (NodeSet bpmNodeSet : list)
            {
                if (bpmNodeSet.getFormKey() != 0L)
                {
                    formKey = bpmNodeSet.getFormKey();
                }
            }
        }
        /*
         * String ctx = request.getContextPath(); PrintTemplate bpmPrintTemplate = bpmPrintTemplateService
         * .getDefaultByFormKey(formKey); if (BeanUtils.isEmpty(bpmPrintTemplate)) { // if(genDefault==1){
         * bpmPrintTemplate = bpmPrintTemplateService.getDefaultPrintTemplateByFormKey(formKey); Long tableId =
         * bpmPrintTemplate.getTableId(); FormTable bpmFormTable = bpmFormTableService.getById(tableId); String
         * tableName = bpmFormTable.getTableName(); String tableComment = bpmFormTable.getTableDesc(); String
         * template=FormUtil.getPrintFreeMarkerTemplate(bpmPrintTemplate.getHtml(),tableName,tableComment);
         * bpmPrintTemplate.setTemplate(template); bpmPrintTemplate.setId(UniqueIdUtil.genId());
         * bpmPrintTemplate.setIsDefault((short)1); bpmPrintTemplateService.add(bpmPrintTemplate); // } // return
         * getAutoView().addObject("html", "").addObject("formKey", // formKey); } String html =
         * bpmPrintTemplateService.parseTempalte(bpmPrintTemplate, processRun, ctx);
         */
        return getAutoView().addObject("html", "");// html);
    }
    
    @RequestMapping("getRefList")
    @Action(description = "流程参考")
    public ModelAndView getRefList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        
        Long defId = RequestUtil.getLong(request, "defId", 0);
        Integer type = RequestUtil.getInt(request, "type", 0);
        Definition bpmDefinition = bpmDefinitionService.getById(defId);
        Long creatorId = UserContextUtil.getCurrentUserId();
        Integer instanceAmount = bpmDefinition.getInstanceAmount();
        if (instanceAmount == null || instanceAmount <= 0)
        {
            instanceAmount = 5;
        }
        
        List<ProcessRun> list = processRunService.getRefList(defId, creatorId, instanceAmount, type);
        return this.getAutoView().addObject("processRunList", list).addObject("type", type);
        
    }
    
    /**
     * 根据流程实例id获取流程的状态。
     * 
     * @param request
     * @return
     */
    @RequestMapping("getFlowStatusByInstanceIdAndNodeId")
    @ResponseBody
    public TaskNodeStatus getFlowStatusByInstanceIdAndNodeId(HttpServletRequest request)
    {
        String instanceId = RequestUtil.getString(request, "instanceId");
        String nodeId = RequestUtil.getString(request, "nodeId");
        TaskNodeStatus taskNodeStatus = bpmService.getNodeCheckStatusInfo(instanceId, nodeId);
        if (BeanUtils.isNotEmpty(taskNodeStatus.getTaskExecutorList()))
        {
            List<TaskExecutor> tempTaskExecutors = taskNodeStatus.getTaskExecutorList();
            for (TaskExecutor taskExecutor : tempTaskExecutors)
            {
                taskExecutor.setMainOrgName(getMainOrgName(taskExecutor.getExecuteId()));
            }
        }
        for (TaskOpinion taskOpinion : taskNodeStatus.getTaskOpinionList())
        {
            if (!TaskOpinion.STATUS_CHECKING.equals(taskOpinion.getCheckStatus()))
                continue;
            Long taskId = taskOpinion.getTaskId();
            ProcessTask processTask = processRunService.getByTaskId(taskId);
            // 执行人为空
            String assignee = processTask.getAssignee();
            if (FlowUtil.isAssigneeNotEmpty(assignee))
            {
                TaskExeStatus taskExeStatus = new TaskExeStatus();
                String fullname = sysUserService.getById(new Long(processTask.getAssignee())).getFullname();
                taskExeStatus.setExecutor(fullname);
                taskExeStatus.setExecutorId(assignee);
                taskExeStatus.setMainOrgName(getMainOrgName(assignee));
                boolean isRead = taskReadService.isTaskRead(Long.valueOf(processTask.getId()), Long.valueOf(assignee));
                taskExeStatus.setRead(isRead);
                taskOpinion.setTaskExeStatus(taskExeStatus);
            }
            // 获取候选人
            else
            {
                Set<TaskExecutor> set = taskUserService.getCandidateExecutors(processTask.getId());
                List<TaskExeStatus> candidateUserStatusList = new ArrayList<TaskExeStatus>();
                for (Iterator<TaskExecutor> it = set.iterator(); it.hasNext();)
                {
                    TaskExecutor taskExe = it.next();
                    TaskExeStatus taskExeStatus = new TaskExeStatus();
                    taskExeStatus.setExecutorId(taskExe.getExecuteId().toString());
                    taskExeStatus.setMainOrgName(getMainOrgName(taskExeStatus.getExecutorId()));
                    taskExeStatus.setType(taskExe.getType());
                    taskExeStatus.setCandidateUser(taskExe.getExecutor());
                    String executorId = taskExe.getExecuteId();
                    if (TaskExecutor.USER_TYPE_USER.equals(taskExe.getType()))
                    {
                        boolean isRead =
                            taskReadService.isTaskRead(Long.valueOf(processTask.getId()), new Long(executorId));
                        taskExeStatus.setRead(isRead);
                    }
                    candidateUserStatusList.add(taskExeStatus);
                }
                taskOpinion.setCandidateUserStatusList(candidateUserStatusList);
            }
        }
        return taskNodeStatus;
    }
    
    /**
     * 获取该用户主组织
     * @param userId
     * @return
     */
    public String getMainOrgName(String userId)
    {
        // 获取该用户所有带有负责人的组织岗位信息
        List<? extends IUserPosition> orgs = userPositionService.getOrgByUserId(Long.parseLong(userId));
        String mainOrgName = "";
        for (IUserPosition org : orgs)
        {
            if (org.getIsPrimary().equals(Short.valueOf((short)1)))
            {
                mainOrgName = org.getOrgName();
                break;
            }
        }
        return mainOrgName;
    }
    
    /**
     * 导出成word文档
     * @param request
     * @param response
     * @throws IOException 
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping("downloadToWord")
    @Action(description = "导出流程成word文档", exectype = SysAuditExecType.EXPORT_TYPE, detail = "导出流程【${subject}】")
    public void downloadToWord(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        response.reset();
        response.setCharacterEncoding("utf-8");
        String agent = (String)request.getHeader("USER-AGENT");
        String form = RequestUtil.getString(request, "form");
        long runId = RequestUtil.getLong(request, "runId");
        ProcessRun processRun = processRunService.getById(runId);
        String subject = processRun.getSubject().replaceAll(
            "<(?:(?:/([^>]+)>)|(?:!--([\\S|\\s]*?)-->)|(?:([^\\s/>]+)\\s*((?:(?:\"[^\"]*\")|(?:'[^']*')|[^\"'<>])*)/?>))",
            "");
        
        String attachPath = AppUtil.getAttachPath();
        String name = UniqueIdUtil.genId() + ".doc";
        
        String fileName = createFilePath(attachPath + File.separator + "tmpdownload", name);
        
        boolean isIe = true;
        if (agent != null && agent.indexOf("MSIE") == -1)
        {
            isIe = false;
        }
        // 生成文件
        genFile(form, fileName);
        
        // 下载文件
        donwload(fileName, response, subject, isIe);
        // 删除文件
        File file = new File(fileName);
        file.delete();
        
    }
    
    /**
     * 生成文件夹
     * @param dir
     * @param name
     * @return
     */
    private String createFilePath(String dir, String name)
    {
        File file = new File(dir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        return dir + File.separator + name;
    }
    
    /**
     * 生成文件。
     * @param content
     * @param fileName
     */
    private void genFile(String content, String fileName)
    {
        ByteArrayInputStream bais = null;
        FileOutputStream fos = null;
        POIFSFileSystem poifs = null;
        byte b[];
        try
        {
            String resultString = content.replaceAll("(?si)<script[\\s]*[^>]*>.*?</script>", "");
            b = resultString.getBytes("gbk");
            bais = new ByteArrayInputStream(b);
            poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            // DocumentEntry documentEntry =
            directory.createDocument("WordDocument", bais);
            fos = new FileOutputStream(fileName);
            poifs.writeFilesystem(fos);
            
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bais.close();
                fos.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 下载文件。
     * @param filePath
     * @param response
     * @param fileName
     * @param isIE
     */
    private void donwload(String filePath, HttpServletResponse response, String fileName, boolean isIE)
    {
        String filedisplay = fileName + ".doc";
        
        response.reset();
        response.setContentType("application/x-msdownload;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        try
        {
            
            if (!isIE)
            {
                String enableFileName = "=?UTF-8?B?" + (new String(Base64.getBase64(filedisplay))) + "?=";
                response.setHeader("Content-Disposition", "attachment;filename=" + enableFileName);
            }
            else
            {
                filedisplay = URLEncoder.encode(filedisplay, "utf-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
            }
            
            OutputStream sops = response.getOutputStream();// 不同类型的文件对应不同的MIME类型
            InputStream ips = new FileInputStream(filePath);
            byte[] content = new byte[ips.available()];
            int length = -1;
            while ((length = ips.read(content)) != -1)
            {
                sops.write(content, 0, length); // 读入流,保存在Byte数组中
            }
            ips.close();
            sops.flush();
            sops.close();
        }
        catch (UnsupportedEncodingException e)
        {
            log.debug(e.getMessage(), e);
            
        }
        catch (IOException e)
        {
            log.debug(e.getMessage(), e);
        }
        
    }
    
}
