package com.cssrc.ibms.core.flow.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.ActivitiInclusiveGateWayException;
import org.activiti.engine.ActivitiOptimisticLockingException;
import org.activiti.engine.ActivitiVarNotFoundException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.intf.IFormRunService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormData;
import com.cssrc.ibms.api.form.model.IFormModel;
import com.cssrc.ibms.api.jms.intf.IMessageHandler;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.intf.IDictionaryService;
import com.cssrc.ibms.api.system.intf.ISysErrorLogService;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.system.model.IDictionary;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.system.model.ISysParameter;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.activity.model.ProcessCmd;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.activity.model.ProcessTaskHistory;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.activity.util.BpmConst;
import com.cssrc.ibms.core.activity.util.BpmUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.excel.util.ExcelUtil;
import com.cssrc.ibms.core.flow.dao.CommuReceiverDao;
import com.cssrc.ibms.core.flow.dao.TaskDao;
import com.cssrc.ibms.core.flow.model.CommuReceiver;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.ExecutionStack;
import com.cssrc.ibms.core.flow.model.GangedSet;
import com.cssrc.ibms.core.flow.model.NodeButton;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.NodeSign;
import com.cssrc.ibms.core.flow.model.NodeTranUser;
import com.cssrc.ibms.core.flow.model.ProTransTo;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.RunLog;
import com.cssrc.ibms.core.flow.model.TaskApprovalItems;
import com.cssrc.ibms.core.flow.model.TaskFork;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.model.TaskSignData;
import com.cssrc.ibms.core.flow.service.BpmService;
import com.cssrc.ibms.core.flow.service.CommuReceiverService;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.ExecutionStackService;
import com.cssrc.ibms.core.flow.service.GangedSetService;
import com.cssrc.ibms.core.flow.service.NodeButtonService;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.flow.service.NodeSignService;
import com.cssrc.ibms.core.flow.service.NodeSignService.NodePrivilegeType;
import com.cssrc.ibms.core.flow.service.NodeUserService;
import com.cssrc.ibms.core.flow.service.ProTransToService;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.RunLogService;
import com.cssrc.ibms.core.flow.service.TaskApprovalItemsService;
import com.cssrc.ibms.core.flow.service.TaskExeService;
import com.cssrc.ibms.core.flow.service.TaskHistoryService;
import com.cssrc.ibms.core.flow.service.TaskOpinionService;
import com.cssrc.ibms.core.flow.service.TaskReadService;
import com.cssrc.ibms.core.flow.service.TaskReminderService;
import com.cssrc.ibms.core.flow.service.TaskSignDataService;
import com.cssrc.ibms.core.flow.service.TaskUserService;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.web.security.IbmsPasswordEncoder;
import com.cssrc.ibms.core.web.singlelogin.IbmsSingleLogin;
import com.cssrc.ibms.core.web.singlelogin.IbmsSinglePasswordEncoder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * 后台任务管理控制类
 * @author zhulongchao
 * 
 */
@Controller
@RequestMapping("/oa/flow/task/")
@Action(ownermodel = SysAuditModelType.FLOW_MANAGEMENT)
public class TaskController extends BaseController
{
    protected Logger logger = LoggerFactory.getLogger(TaskController.class);
    
    @Resource
    private BpmService bpmService;
    
    @Resource
    private TaskDao taskDao;
    
    @Resource
    private CommuReceiverDao commuReceiverDao;
    
    @Resource
    private TaskService taskService;
    
    @Resource
    private ProcessRunService processRunService;
    
    @Resource
    private DefinitionService bpmDefinitionService;
    
    @Resource
    private NodeSetService nodeSetService;
    
    @Resource
    private TaskSignDataService taskSignDataService;
    
    @Resource
    private NodeSignService bpmNodeSignService;
    
    @Resource
    private ExecutionStackService executionStackService;
    
    @Resource
    private ISysUserService sysUserService;
    
    @Resource
    private IFormHandlerService bpmFormHandlerService;
    
    @Resource
    private IFormDefService bpmFormDefService;
    
    @Resource
    private NodeUserService bpmNodeUserService;
    
    @Resource
    private TaskUserService taskUserService;
    
    @Resource
    private IFormRunService bpmFormRunService;
    
    @Resource
    private TaskApprovalItemsService taskAppItemService;
    
    @Resource
    private IFormTableService bpmFormTableService;
    
    @Resource
    private NodeButtonService bpmNodeButtonService;
    
    @Resource
    private RunLogService bpmRunLogService;
    
    @Resource
    private RuntimeService runtimeService;
    
    @Resource
    private TaskReadService taskReadService;
    
    @Resource
    private CommuReceiverService commuReceiverService;
    
    @Resource
    private GangedSetService bpmGangedSetService;
    
    @Resource
    private TaskExeService bpmTaskExeService;
    
    @Resource
    private TaskOpinionService taskOpinionService;
    
    @Resource
    private ISysFileService sysFileService;
    
    @Resource
    private TaskHistoryService taskHistoryService;
    
    @Resource
    private ProTransToService bpmProTransToService;
    
    @Resource
    Map<String, IMessageHandler> handlersMap;
    
    @Resource
    private ISysErrorLogService sysErrorLogService;
    
    @Resource
    private IDataTemplateService dataTemplateService;
    
    @Resource
    private ISysParameterService sysParameterService;
    
    @Resource
    private TaskReminderService reminderService;
    
    /**
     * 跳转到启动流程页面。<br/>
     * 
     * <pre>
     * 传入参数流程定义id：defId。 
     * 实现方法： 
     * 1.根据流程对应ID查询流程定义。 
     * 2.获取流程定义的XML。
     * 3.获取流程定义的第一个任务节点。
     * 4.获取任务节点的流程表单定义。 
     * 5.显示启动流程表单页面。
     * </pre>
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("startFlowForm")
    @Action(description = "跳至启动流程页面")
    public ModelAndView startFlowForm(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String jsonStr = RequestUtil.getStringAry(request, "jsonStr");
        String flowKey = "";
        if (StringUtil.isNotEmpty(jsonStr))
        {
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            flowKey = JSONUtil.getString(jsonObject, "flowKey");
        }
        
        Long userId = UserContextUtil.getCurrentUserId();
        String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
        ModelAndView mv = getAutoView();
        if (StringUtil.isNotEmpty(rpcrefname))
        {
            // 采用IOC方式，根据rpc远程接口调用数据
            CommonService commonService = (CommonService)AppUtil.getContext().getBean(rpcrefname);
            // 获取业务数据列表显示
            mv = commonService.startFlowView(request, mv, flowKey, userId);
        }
        else
        {
            // 当不是rpc远程接口 或者 远程调用超时失败，从本地调用
            mv = processRunService.startFlowView(request, mv, flowKey, userId);
        }
        return mv;
    }
    
    /**
     * 用户启动流程。
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "userStartFlow")
    @Action(description = "用户启动流程")
    @ResponseBody
    public void userStartFlow(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        startFlow(request, response);
        // ProcessCmd processCmd = BpmUtil.getProcessCmd(request);
        // return "123";
    }
    
    /**
     * 启动流程。
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "startFlow", method = RequestMethod.POST)
    @Action(description = "启动流程",execOrder = ActionExecOrder.AFTER, 
    detail = "启动流程，业务数据<#assign entity=dataTemplateService.getById(Long.valueOf(__displayId__))/>" + "${sysAuditLinkService.getFormTableDesc(entity.tableId,pk)}", 
    exectype = SysAuditExecType.FLOW_START,
    ownermodel=SysAuditModelType.FLOW_MANAGEMENT)
    public void startFlow(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        
        PrintWriter out = response.getWriter();
        Long runId = RequestUtil.getLong(request, "runId", 0L);RequestUtil.getString(request, "__displayId__");
        try
        {
            ProcessCmd processCmd = BpmUtil.getProcessCmd(request);
            Long userId = UserContextUtil.getCurrentUserId();
            processCmd.setCurrentUserId(userId.toString());
            String msg = "";// 返回结果
            
            // 判断是否有rpc远程接口 &rpcrefname="interfacesImplConsumerCommonService"
            String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
            if (runId != 0L)
            {
                ProcessRun processRun = null;
                if (StringUtil.isNotEmpty(rpcrefname))
                {
                    // 采用IOC方式，根据rpc远程接口调用数据
                    CommonService commonService = (CommonService)AppUtil.getContext().getBean(rpcrefname);
                    processRun = (ProcessRun)commonService.getProcessRun(runId);
                }
                else
                {
                    processRun = processRunService.getById(runId);
                }
                if (BeanUtils.isEmpty(processRun))
                {
                    out.print(new ResultMessage(ResultMessage.Fail,
                        getText("controller.task.startFlow.draft.deleteOrNotExist")));
                    return;
                }
                processCmd.setProcessRun(processRun);
            }
            if (StringUtil.isNotEmpty(rpcrefname))
            {
                // 采用IOC方式，根据rpc远程接口调用数据
                CommonService commonService = (CommonService)AppUtil.getContext().getBean(rpcrefname);
                msg = commonService.startProcess(processCmd);
            }
            else
            {
                processRunService.startProcess(processCmd);
                msg = processCmd.getBusinessKey();//220000002650033 220000002650033
                try {
        			LogThreadLocalHolder.putParamerter("pk", msg);
        		} catch (Exception e) {
        			e.printStackTrace();
        			logger.error(e.getMessage());
        		}
                
            }
            ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, msg);
            out.print(resultMessage);
        }
        catch (Exception ex)
        {
            logger.debug("startFlow:" + ex.getMessage());
            ex.printStackTrace();
            String str = MessageUtil.getMessage();
            if (StringUtil.isNotEmpty(str))
            {
                ResultMessage resultMessage =
                    new ResultMessage(ResultMessage.Fail, getText("controller.task.startFlow.fail") + ":\r\n" + str);
                out.print(resultMessage);
            }
            else
            {
                // String message = ExceptionUtil.getExceptionMessage(ex);
                ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, ex.getMessage());
                out.print(resultMessage);
            }
        }
    }
    
    /**
     * 保存草稿
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("saveForm")
    @Action(description = "保存草稿")
    public void saveForm(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        PrintWriter out = response.getWriter();
        ProcessCmd processCmd = BpmUtil.getProcessCmd(request);
        String runId=RequestUtil.getString(request,"runId");
        if(StringUtil.isNotEmpty(runId)) {
            processCmd.setRunId(Long.valueOf(runId));
        }
        Long userId = UserContextUtil.getCurrentUserId();
        processCmd.setCurrentUserId(userId.toString());
        
        ResultMessage resultMessage = null;
        try
        {
            Map<String, Object> result = processRunService.saveForm(processCmd);
            resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.task.startFlow.saveDraft"));
            resultMessage.addData("runId", result.get("runId"));
            resultMessage.addData("businessKey", result.get("businessKey"));
        }
        catch (Exception e)
        {
            String message = ExceptionUtil.getExceptionMessage(e);
            resultMessage = new ResultMessage(ResultMessage.Fail, message);
        }
        out.print(resultMessage);
    }
    
    /**
     * 保存表单数据。
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("saveData")
    @Action(description = "保存表单数据", detail = "<#if StringUtils.isNotEmpty(taskId)>"
        + "保存流程【${SysAuditLinkService.getProcessRunLink(taskId)}】的表单数据" + "<#elseif StringUtils.isNotEmpty(defId)>"
        + "保存流程定义【${SysAuditLinkService.getDefinitionLink(Long.valueOf(defId))}】至草稿" + "</#if>")
    public void saveData(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        PrintWriter out = response.getWriter();
        String userId = UserContextUtil.getCurrentUserId().toString();
        ProcessCmd processCmd = BpmUtil.getProcessCmd(request);
        Long runId = RequestUtil.getLong(request, "runId", 0L);
        request.getParameter("voteContent");
        if (runId != 0L)
        {
            ProcessRun processRun = processRunService.getById(runId);
            processCmd.setProcessRun(processRun);
        }
        processCmd.setCurrentUserId(userId);
        ResultMessage resultMessage = null;
        try
        {
            processRunService.saveData(processCmd);
            resultMessage =
                new ResultMessage(ResultMessage.Success, getText("controller.task.startFlow.saveFormData.success"));
        }
        catch (Exception ex)
        {
            String str = MessageUtil.getMessage();
            if (StringUtil.isNotEmpty(str))
            {
                resultMessage = new ResultMessage(ResultMessage.Fail,
                    getText("controller.task.startFlow.saveFormData.success") + ":" + str);
            }
            else
            {
                String message = ExceptionUtil.getExceptionMessage(ex);
                resultMessage = new ResultMessage(ResultMessage.Fail, message);
            }
        }
        out.print(resultMessage);
    }
    
    @RequestMapping("saveOpinion")
    @ResponseBody
    @Action(description = "保存流程沟通或流转意见")
    public String saveOpinion(HttpServletRequest request, HttpServletResponse response, TaskOpinion taskOpinion)
        throws Exception
    {
        JSONObject jobject = new JSONObject();
        String informType = RequestUtil.getString(request, "informType");
        boolean isAgree = RequestUtil.getBoolean(request, "isAgree");
        taskOpinion.setOpinionId(UniqueIdUtil.genId());
        ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
        ProcessCmd taskCmd = BpmUtil.getProcessCmd(request);
        try
        {
            TaskEntity taskEntity = bpmService.getTask(taskOpinion.getTaskId().toString());
            taskOpinion.setSuperExecution(processRunService.getSuperActInstId(taskEntity.getProcessInstanceId()));
            ProcessRun processRun = processRunService.getByActInstanceId(new Long(taskEntity.getProcessInstanceId()));
            
            String description = taskEntity.getDescription();
            Integer sysTemplateType = 0;
            taskOpinion.setActDefId(taskEntity.getProcessDefinitionId());
            taskOpinion.setActInstId(taskEntity.getProcessInstanceId());
            taskOpinion.setStartTime(taskEntity.getCreateTime());
            taskOpinion.setEndTime(new Date());
            Long duration = new Date().getTime() - taskEntity.getCreateTime().getTime();
            /*
             * calendarAssignService.getRealWorkTime(taskEntity.getCreateTime(), new Date(), sysUser.getUserId())
             */
            taskOpinion.setDurTime(duration);
            taskOpinion.setExeUserId(sysUser.getUserId());
            taskOpinion.setExeFullname(sysUser.getFullname());
            taskOpinion.setTaskKey(taskEntity.getTaskDefinitionKey());
            taskOpinion.setTaskName(taskEntity.getName());
            if (description.equals(TaskOpinion.STATUS_TRANSTO.toString()))
            {// 流转
                if (taskEntity.getAssignee().equals(sysUser.getUserId().toString()))
                {
                    taskOpinion.setCheckStatus(TaskOpinion.STATUS_REPLACE_SUBMIT);
                }
                else
                {
                    taskOpinion.setCheckStatus(TaskOpinion.STATUS_INTERVENE);
                    String opinion = taskOpinion.getOpinion();
                    Long userId = Long.valueOf(taskEntity.getAssignee());
                    ISysUser assignee = sysUserService.getById(userId);
                    opinion += getText("controller.task.saveOpinion.execution", assignee.getFullname());
                    taskOpinion.setOpinion(opinion);
                }
                sysTemplateType = ISysTemplate.USE_TYPE_TRANSTO_FEEDBACK;
                taskCmd.setVoteAgree(isAgree ? TaskOpinion.STATUS_AGREE : TaskOpinion.STATUS_REFUSE);
            }
            else
            {// 沟通
                taskOpinion.setCheckStatus(TaskOpinion.STATUS_NOTIFY);
                sysTemplateType = ISysTemplate.USE_TYPE_FEEDBACK;
            }
            
            // 保存反馈信息
            processRunService.saveOpinion(taskEntity, taskOpinion);
            // 设置沟通人员或流转人员的状态为已反馈
            commuReceiverService.setCommuReceiverStatusToFeedBack(taskEntity, sysUser);
            
            // 向原执行人发送任务完成提醒消息
            Map<Long, Long> usrIdTaskIds = new HashMap<Long, Long>();
            ProcessTask parentTask = processRunService.getByTaskId(Long.valueOf(taskEntity.getParentTaskId()));
            usrIdTaskIds.put(Long.valueOf(parentTask.getAssignee()), Long.valueOf(taskEntity.getParentTaskId()));
            processRunService.notifyCommu(processRun
                .getSubject(), usrIdTaskIds, informType, sysUser, taskOpinion.getOpinion(), sysTemplateType);
            
            // 添加已办历史
            processRunService.addActivityHistory(taskEntity);
            
            // 判断是否流转任务
            if (description.equals(TaskOpinion.STATUS_TRANSTO.toString()))
            {// 流转任务
                
                String parentTaskId = taskEntity.getParentTaskId();
                ProTransTo bpmProTransTo = bpmProTransToService.getByTaskId(Long.valueOf(parentTaskId));
                String voteContent = getText("controller.task.saveOpinion.message", sysUser.getFullname());
                if (BeanUtils.isNotEmpty(bpmProTransTo))
                {// 流转
                    if (bpmProTransTo.getTransType() == 1)
                    {// 非会签
                        if (bpmProTransTo.getAction() == 1)
                        {// 返回
                            // 更改初始执行人状态
                            processRunService.updateTaskDescription(TaskOpinion.STATUS_CHECKING.toString(),
                                parentTaskId);
                            IFormData bpmFormData = processRunService
                                .handlerFormData(taskCmd, processRun, taskEntity.getTaskDefinitionKey());
                            
                            if (bpmFormData != null)
                            {
                                Map<String, String> optionsMap = new HashMap<String, String>();
                                optionsMap.put("option", taskOpinion.getOpinion());
                                // 记录意见
                                updOption(optionsMap, taskCmd);
                            }
                        }
                        else
                        {// 提交
                            taskCmd.setTaskId(parentTaskId);
                            taskCmd.setVoteAgree(isAgree ? TaskOpinion.STATUS_AGREE : TaskOpinion.STATUS_REFUSE);
                            taskCmd.setVoteContent(voteContent);
                            // 更改初始执行人状态为正常流转
                            processRunService.updateTaskDescription(TaskOpinion.STATUS_COMMON_TRANSTO.toString(),
                                parentTaskId);
                            // processRunService.nextProcess(taskCmd);
                            processRunService.nextProcess(taskCmd, true);
                            
                        }
                        // 根据parentTaskId和description获取剩余流转任务
                        List<?> list = processRunService.getByParentTaskIdAndDesc(parentTaskId,
                            TaskOpinion.STATUS_TRANSTO.toString());
                        for (int i = 0; i < list.size(); i++)
                        {
                            // 设置流转的转办代理事宜为完成
                            ProcessTask task = (ProcessTask)list.get(i);
                            Long taskId = Long.valueOf(task.getId());
                            bpmTaskExeService.complete(taskId);
                        }
                        
                        processRunService.delTransToTaskByParentTaskId(parentTaskId);// 删除其余流转任务
                        bpmProTransToService.delById(bpmProTransTo.getId());// 删除流转状态
                    }
                    else
                    {// 会签
                     // 记录被流转人意见
                        Integer transResult = bpmProTransTo.getTransResult();
                        if (transResult == 1 && !isAgree)
                        {
                            bpmProTransTo.setTransResult(2);
                            bpmProTransToService.update(bpmProTransTo);
                            transResult = 2;
                        }
                        
                        // 根据parentTaskId和description获取剩余流转任务
                        List<TaskEntity> list = processRunService.getByParentTaskIdAndDesc(parentTaskId,
                            TaskOpinion.STATUS_TRANSTO.toString());
                        // IFormData bpmFormData=handlerFormData(taskCmd, processRun,taskEntity.getTaskDefinitionKey());
                        IFormData bpmFormData =
                            processRunService.handlerFormData(taskCmd, processRun, taskEntity.getTaskDefinitionKey());
                        
                        if (bpmFormData != null)
                        {
                            Map<String, String> optionsMap = new HashMap<String, String>();
                            optionsMap.put("option", taskOpinion.getOpinion());
                            // 记录意见
                            updOption(optionsMap, taskCmd);
                        }
                        if (list.size() == 0)
                        {// 做完流转任务
                            if (bpmProTransTo.getAction() == 1)
                            {// 返回
                                // 更改初始执行人状态
                                processRunService.updateTaskDescription(TaskOpinion.STATUS_CHECKING.toString(),
                                    parentTaskId);
                            }
                            else
                            {// 提交
                                taskCmd.setTaskId(parentTaskId);
                                taskCmd.setVoteAgree(
                                    transResult == 1 ? TaskOpinion.STATUS_AGREE : TaskOpinion.STATUS_REFUSE);
                                taskCmd.setVoteContent(voteContent);
                                // 更改初始执行人状态为正常流转
                                processRunService.updateTaskDescription(TaskOpinion.STATUS_COMMON_TRANSTO.toString(),
                                    parentTaskId);
                                // processRunService.nextProcess(taskCmd);
                                // 上面的代码已经调用handlerFormData对表单数据进行保存。故第二个参数为false，不对表单进行保存。
                                processRunService.nextProcess(taskCmd, false);
                            }
                            bpmProTransToService.delById(bpmProTransTo.getId());// 删除流转状态
                        }
                    }
                }
                else
                {
                    throw new Exception(getText("controller.task.saveOpinion.fail"));
                }
            }
            
            Long runId = processRun.getRunId();
            
            String memo = getText("controller.task.saveOpinion.memo",
                processRun.getSubject(),
                taskEntity.getName(),
                taskOpinion.getOpinion());
            bpmRunLogService.addRunLog(runId, RunLog.OPERATOR_TYPE_ADDOPINION, memo);
            
            jobject.accumulate("result", ResultMessage.Success).accumulate("message",
                getText("controller.task.saveOpinion.addOpin.success"));
            return jobject.toString();
        }
        catch (Exception ex)
        {
            String str = MessageUtil.getMessage();
            if (StringUtil.isNotEmpty(str))
            {
                jobject.accumulate("result", ResultMessage.Fail).accumulate("message",
                    getText("controller.task.saveOpinion.addOpin.fail") + ":" + str);
                return jobject.toString();
            }
            else
            {
                String message = ExceptionUtil.getExceptionMessage(ex);
                jobject.accumulate("result", ResultMessage.Fail).accumulate("message", message);
                return jobject.toString();
            }
        }
    }
    
    /**
     * 更新意见。
     * 
     * <pre>
     * 1.首先根据任务id查询意见，应为意见在task创建时这个意见就被产生出来，所以能直接获取到该意见。
     * 2.获取意见，注意一个节点只允许一个意见填写，不能在同一个任务节点上同时允许两个意见框的填写，比如同时科员意见，局长意见等。
     * 3.更新意见。
     * </pre>
     * 
     * @param optionsMap
     * @param taskId
     */
    private void updOption(Map<String, String> optionsMap, ProcessCmd cmd)
    {
        if (BeanUtils.isEmpty(optionsMap))
            return;
        
        Set<String> set = optionsMap.keySet();
        String key = set.iterator().next();
        String value = optionsMap.get(key);
        cmd.setVoteFieldName(key);
        cmd.setVoteContent(value);
        
    }
    
    /**
     * 处理在线表单数据。
     * 
     * @param processRun
     * @param processCmd
     * @return
     * @throws Exception
     */
    /*
     * private IFormData handlerFormData(ProcessCmd processCmd,ProcessRun processRun,String nodeId) throws Exception {
     * String json = processCmd.getFormData();
     * 
     * if (StringUtils.isEmpty(json)) { return null; }
     * 
     * IFormData bpmFormData = null; IFormTable bpmFormTable=null; String businessKey=""; //判断节点Id是否为空，为空表示开始节点。 boolean
     * isStartFlow=false; if(StringUtil.isEmpty(nodeId)){ businessKey = processCmd.getBusinessKey(); isStartFlow=true;
     * }else{ businessKey = processRun.getBusinessKey(); }
     * if(isStartFlow&&ProcessRun.STATUS_FORM.equals(processRun.getStatus())){ Long formDefId=processRun.getFormDefId();
     * IFormDef bpmFormDef=bpmFormDefService.getById(formDefId); bpmFormTable=
     * bpmFormTableService.getByTableId(bpmFormDef.getTableId(), 1); }else{ bpmFormTable=
     * bpmFormTableService.getByDefId(processRun.getDefId()); } // 有主键的情况,表示数据已经存在。 if
     * (StringUtil.isNotEmpty(businessKey)) { String pkName=bpmFormTable.isExtTable()?bpmFormTable.getPkField()
     * :ITableModel.PK_COLUMN_NAME; IPkValue pkValue=this.bpmFormHandlerService.getNewPkValue(pkName, businessKey,
     * false); bpmFormData=bpmFormHandlerService.getFormFromJson(json, pkValue,bpmFormTable); } else {
     * bpmFormData=bpmFormHandlerService.getFormFromJson(json, null,bpmFormTable); }
     * 
     * processCmd.putVariables(bpmFormData.getVariables()); // 生成的主键 IPkValue pkValue = bpmFormData.getPkValue();
     * businessKey = pkValue.getValue().toString();
     * 
     * String pk=processRun.getBusinessKey();
     * 
     * processRun.setBusinessKey(businessKey); processCmd.setBusinessKey(businessKey); //启动流程。 if(isStartFlow){ //
     * 保存表单数据,存取表单数据 bpmFormHandlerService.handFormData(bpmFormData,processRun); }else{
     * bpmFormHandlerService.handFormData(bpmFormData,processRun,nodeId); //业务主键为空的情况，设置流程主键。设置流程变量。
     * if(StringUtil.isEmpty(pk)){ runtimeService.setVariable(processRun.getActInstId(), BpmConst.FLOW_BUSINESSKEY,
     * businessKey); } } return bpmFormData; }
     */
    
    /**
     * 显示任务回退的执行路径
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("back")
    public ModelAndView back(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = request.getParameter("taskId");
        
        if (StringUtils.isEmpty(taskId))
            return getAutoView();
        
        TaskEntity taskEntity = bpmService.getTask(taskId);
        String taskToken = (String)taskService.getVariableLocal(taskEntity.getId(), TaskFork.TAKEN_VAR_NAME);
        ExecutionStack executionStack = executionStackService
            .getLastestStack(taskEntity.getProcessInstanceId(), taskEntity.getTaskDefinitionKey(), taskToken);
        if (executionStack != null && executionStack.getParentId() != null && executionStack.getParentId() != 0)
        {
            ExecutionStack parentStack = executionStackService.getById(executionStack.getParentId());
            String assigneeNames = "";
            if (StringUtils.isNotEmpty(parentStack.getAssignees()))
            {
                String[] uIds = parentStack.getAssignees().split("[,]");
                int i = 0;
                for (String uId : uIds)
                {
                    ISysUser sysUser = sysUserService.getById(new Long(uId));
                    if (sysUser == null)
                        continue;
                    if (i++ > 0)
                    {
                        assigneeNames += ",";
                    }
                    assigneeNames += sysUser.getFullname();
                }
            }
            request.setAttribute("assigneeNames", assigneeNames);
            request.setAttribute("parentStack", parentStack);
        }
        
        request.setAttribute("taskId", taskId);
        
        return getAutoView();
    }
    
    /**
     * 任务回退
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("jumpBack")
    public ModelAndView jumpBack(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ProcessCmd processCmd = BpmUtil.getProcessCmd(request);
        processCmd.setCurrentUserId(UserContextUtil.getCurrentUserId().toString());
        // processRunService.nextProcess(processCmd);
        processRunService.nextProcess(processCmd, true);
        return new ModelAndView("redirect:list.do");
    }
    
    /**
     * 跳至会签页
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("toSign")
    public ModelAndView toSign(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = request.getParameter("taskId");
        ModelAndView modelView = getAutoView();
        
        if (StringUtils.isNotEmpty(taskId))
        {
            TaskEntity taskEntity = bpmService.getTask(taskId);
            String nodeId = bpmService.getExecution(taskEntity.getExecutionId()).getActivityId();
            String actInstId = taskEntity.getProcessInstanceId();
            
            List<TaskSignData> signDataList = taskSignDataService.getByNodeAndInstanceId(actInstId, nodeId);
            
            // 获取会签规则
            NodeSign bpmNodeSign = bpmNodeSignService.getByDefIdAndNodeId(taskEntity.getProcessDefinitionId(), nodeId);
            
            modelView.addObject("signDataList", signDataList);
            modelView.addObject("task", taskEntity);
            modelView.addObject("curUser", (ISysUser)UserContextUtil.getCurrentUser());
            modelView.addObject("bpmNodeSign", bpmNodeSign);
        }
        
        return modelView;
    }
    
    /**
     * 补签
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("addSign")
    @ResponseBody
    public String addSign(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = request.getParameter("taskId");
        String signUserIds = request.getParameter("signUserIds");
        String opinion = request.getParameter("opinion");
        String informType = RequestUtil.getString(request, "informType");
        if (StringUtils.isNotEmpty(taskId) && StringUtils.isNotEmpty(signUserIds) && StringUtils.isNotEmpty(opinion))
        {
            taskSignDataService.addSign(signUserIds, taskId, opinion, informType);
        }
        return SUCCESS;
    }
    
    /**
     * 任务自由跳转
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("jump")
    @ResponseBody
    public String jump(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = request.getParameter("taskId");
        String destTask = request.getParameter("destTask");
        bpmService.transTo(taskId, destTask);
        
        return SUCCESS;
    }
    
    /**
     * 跳至会签页
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("saveSign")
    @ResponseBody
    public String saveSign(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = request.getParameter("taskId");
        String isAgree = request.getParameter("isAgree");
        String content = request.getParameter("content");
        ProcessCmd processCmd = new ProcessCmd(taskId, content, new Short(isAgree));
        taskSignDataService.signVoteTask(taskId, processCmd);
        
        return SUCCESS;
    }
    
    @RequestMapping("list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        QueryFilter filter = new QueryFilter(request, "taskItem");
        List<TaskEntity> list = bpmService.getTasks(filter);
        request.getSession().setAttribute("isAdmin", true);
        ModelAndView mv = getAutoView().addObject("taskList", list);
        
        return mv;
    }
    
    @SuppressWarnings("rawtypes")
    @RequestMapping("forMe")
    public ModelAndView forMe(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        QueryFilter filter = new QueryFilter(request, "taskItem");
        List<?> list = bpmService.getMyTasks(filter);
        Map<String, String> candidateMap = new HashMap<String, String>();
        StringBuffer sb = new StringBuffer();
        if (BeanUtils.isNotEmpty(list))
        {
            for (int i = 0; i < list.size(); i++)
            {
                ProcessTask task = (ProcessTask)list.get(i);
                if (i == 0)
                {
                    sb.append(task.getId());
                }
                else
                {
                    sb.append("," + task.getId());
                }
            }
            List<Map> userMapList = bpmService.getHasCandidateExecutor(sb.toString());
            for (Iterator<Map> it = userMapList.iterator(); it.hasNext();)
            {
                Map map = it.next();
                candidateMap.put(map.get("TASKID").toString(), "1");
            }
        }
        ModelAndView mv = getAutoView().addObject("taskList", list).addObject("candidateMap", candidateMap);
        return mv;
    }
    
    /**
     * 待办事项 flex 返回格式eg: [ { "id":"10000005210157", // 项id "type":"1", //
     * 类型，如任务、消息 "startTime":"12/07/2012 00:00:00 AM", // 开始时间
     * "endTime":"12/08/2012 00:00:00 AM", // 结束时间
     * "title":"测试流程变量-admin-2012-10-17 11:55:07", // 标题 } ]
     * 
     * @throws Exception
     */
    @RequestMapping("myEvent")
    public void myEvent(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("mode", RequestUtil.getString(request, "mode"));
        param.put("startDate", RequestUtil.getString(request, "startDate"));
        param.put("endDate", RequestUtil.getString(request, "endDate"));
        response.getWriter().print(bpmService.getMyEvents(param));
    }
    
    @RequestMapping("detail")
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long taskId = RequestUtil.getLong(request, "taskId");
        Task task = taskService.createTaskQuery().taskId(taskId.toString()).singleResult();
        if (task == null)
        {
            return new ModelAndView("redirect:notExist.do");
        }
        String returnUrl = RequestUtil.getPrePage(request);
        // get the process run instance from task
        ProcessRun processRun = processRunService.getByActInstanceId(new Long(task.getProcessInstanceId()));
        
        Definition processDefinition = bpmDefinitionService.getByActDefId(processRun.getActDefId());
        ModelAndView modelView = getAutoView();
        modelView.addObject("taskEntity", task)
            .addObject("processRun", processRun)
            .addObject("processDefinition", processDefinition)
            .addObject("returnUrl", returnUrl);
        if (StringUtils.isNotEmpty(processRun.getBusinessUrl()))
        {
            String businessUrl =
                StringUtil.formatParamMsg(processRun.getBusinessUrl(), processRun.getBusinessKey()).toString();
            modelView.addObject("businessUrl", businessUrl);
        }
        return modelView;
    }
    
    /**
     * 启动任务界面。 根据任务ID获取流程实例，根据流程实例获取表单数据。
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("toStart")
    public ModelAndView toStart(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = this.getAutoView();
        // 移除消息：由于消息信息显示通过session存储获取，这里移除历史消息
        this.removeMessage(request);
        String taskId = RequestUtil.getString(request, "taskId");
        String instanceId = RequestUtil.getString(request, "instanceId");
        return getToStartView(taskId,instanceId, response, mv, 0);
    }
    
    /**
     * task启动api地址。第三方使用。比如邮件系统，或者其他系统
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("api/start")
    public ModelAndView apiStart(@RequestParam(value = "token", required = true) String token,
        @RequestParam(value = "userId", required = true) String userId,
        @RequestParam(value = "taskId", required = true) String taskId,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        //伪单点登录不需要 密码校验
        IbmsSinglePasswordEncoder singlePasswordEncoder=AppUtil.getBean("ibmsSinglePasswordEncoder", IbmsSinglePasswordEncoder.class);
        singlePasswordEncoder.authenticate(request);
        //系统默认的security 用户校验
        IbmsPasswordEncoder ibmsPasswordEncoder=AppUtil.getBean("ibmsPasswordEncoder", IbmsPasswordEncoder.class);
        ProviderManager authenticationManager=AppUtil.getBean("authenticationManager", ProviderManager.class);
        IbmsSingleLogin singleLogin=singlePasswordEncoder.getSingleLogin();
        
        
        userId=singleLogin.decrypt("userId",userId);
        taskId=singleLogin.decrypt("taskId",taskId);

        ISysUser sysUser=this.sysUserService.getById(Long.valueOf(userId));
        
        List<AuthenticationProvider> aa=authenticationManager.getProviders();
        DaoAuthenticationProvider p=(DaoAuthenticationProvider)aa.get(0);
        //设置为伪单点登录的 用户密码校验
        p.setPasswordEncoder(singlePasswordEncoder);
        
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword());
        authRequest.setDetails(new WebAuthenticationDetails(request));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        //调用app-security.xml方法中authenticationManager user-service-ref="sysUserDao"方法获取user
        Authentication auth = authenticationManager.authenticate(authRequest);
        securityContext.setAuthentication(auth);
        
        RequestUtil.getHttpServletRequest().getSession().setAttribute("userInfo", sysUser);
        
        //还原为原来的登录校验
        p.setPasswordEncoder(ibmsPasswordEncoder);

        ModelAndView mv = new ModelAndView("oa/flow/taskToStart.jsp");
        // 移除消息：由于消息信息显示通过session存储获取，这里移除历史消息
        this.removeMessage(request);
        return getToStartView(taskId,null, response, mv, 0);
    }
    
    /**
     * 管理员使用的页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("doNext")
    public ModelAndView doNext(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("/oa/flow/taskToStart.jsp");
        String taskId = RequestUtil.getString(request, "taskId");
        String instanceId = RequestUtil.getString(request, "instanceId");
        mv = getToStartView(taskId,instanceId ,response, mv, 1);
        return mv;
    }
    
    /**
     * 流程启动页面（修改这个方法请修改手机的页面MobileTaskController.getMyTaskForm()）
     * @param request
     * @param response
     * @param mv
     * @param isManage
     * @return
     * @throws Exception
     */
    private ModelAndView getToStartView(String taskId,String instanceId, HttpServletResponse response, ModelAndView mv,
        int isManage)
        throws Exception
    {
        String ctxPath = AppUtil.getContextPath();
        ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
        if (StringUtil.isEmpty(taskId) && StringUtil.isEmpty(instanceId))
        {
            return RequestUtil.getTipInfo(getText("controller.task.toStart.notTaskId"));
        }
        
        // 根据流程实例获取流程任务。
        if (StringUtil.isNotEmpty(instanceId))
        {
            List<ProcessTask> list = bpmService.getTasks(instanceId);
            if (BeanUtils.isNotEmpty(list))
            {
                taskId = list.get(0).getId();
            }
        }
        // 查找任务节点
        TaskEntity taskEntity = bpmService.getTask(taskId);
        if (taskEntity == null)
        {
            ProcessTaskHistory taskHistory = taskHistoryService.getById(Long.valueOf(taskId));
            if (taskHistory == null)
            {
                if (StringUtil.isEmpty(taskId) && StringUtil.isEmpty(instanceId))
                {
                    return RequestUtil.getTipInfo(getText("controller.task.taskIdError"));
                }
            }
            String actInstId = taskHistory.getProcessInstanceId();
            if (StringUtils.isEmpty(actInstId)
                && taskHistory.getDescription().equals(TaskOpinion.STATUS_COMMUNICATION.toString()))
            {
                return RequestUtil.getTipInfo(getText("controller.task.taskHandled"));
            }
            ProcessRun processRun = processRunService.getByActInstanceId(new Long(actInstId));
            String url = AppUtil.getContextPath() + "/oa/flow/processRun/info.do?link=1&runId=" + processRun.getRunId();
            response.sendRedirect(url);
            return null;
        }
        
        if (TaskOpinion.STATUS_TRANSTO_ING.toString().equals(taskEntity.getDescription())
            && taskEntity.getAssignee().equals(sysUser.getUserId().toString()))
        {
            return RequestUtil.getTipInfo(getText("controller.task.taskcircuing"));
        }
        
        instanceId = taskEntity.getProcessInstanceId();
        if (isManage == 0)
        {
            boolean hasRights = processRunService.getHasRightsByTask(new Long(taskEntity.getId()), sysUser.getUserId());
            if (!hasRights)
            {
                return RequestUtil.getTipInfo(getText("controller.task.notExecutors"));
            }
        }
        // 更新任务为已读。
        taskReadService.saveReadRecord(Long.parseLong(instanceId), Long.parseLong(taskId));
        // 设置沟通人员或流转人员查看状态。
        commuReceiverService.setCommuReceiverStatus(taskEntity, sysUser);
        
        String nodeId = taskEntity.getTaskDefinitionKey();
        String actDefId = taskEntity.getProcessDefinitionId();
        Long userId = UserContextUtil.getCurrentUserId();
        
        Definition bpmDefinition = bpmDefinitionService.getByActDefId(actDefId);
        // 不能按照 instanceId 来查找 应该根据 EXECUTION_ID_ 查找processrun
        ProcessRun processRun = processRunService.getByActInstanceId(new Long(instanceId));
        // ProcessRun processRun = processRunService.getByActInstanceId(Long.valueOf(taskEntity.getExecutionId()));
        
        Long defId = bpmDefinition.getDefId();
        
        // 通过defid和nodeId获取联动设置
        List<GangedSet> bpmGangedSets = bpmGangedSetService.getByDefIdAndNodeId(defId, nodeId);
        JSONArray gangedSetJarray = JSONArray.fromObject(bpmGangedSets);
        /**
         * 使用API调用的时候获取表单的url进行跳转。
         */
        if (bpmDefinition.getIsUseOutForm() == 1)
        {
            String formUrl = bpmFormDefService.getFormUrl(taskId, defId, nodeId, processRun.getBusinessKey(), ctxPath);
            if (StringUtils.isEmpty(formUrl))
            {
                ModelAndView rtnModel = RequestUtil.getTipInfo(getText("controller.task.setFormUrl"));
                return rtnModel;
            }
            response.sendRedirect(formUrl);
        }
        
        NodeSet bpmNodeSet = nodeSetService.getByActDefIdNodeId(actDefId, nodeId);
        
        String toBackNodeId = NodeCache.getFirstNodeId(actDefId).getNodeId();
        String form = "";
        
        Map<String, Object> variables = taskService.getVariables(taskId);
        Long tempLaunchId = userId;
        // 在沟通和加签的时候 把当前用户对于当前表单的权限设置为传递者的权限。
        if (StringUtils.isEmpty(taskEntity.getExecutionId()))
        {
            if (taskEntity.getDescription().equals(TaskOpinion.STATUS_TRANSTO.toString()))
            {
                List<TaskOpinion> taskOpinionList = taskOpinionService.getByActInstId(instanceId);
                if (BeanUtils.isNotEmpty(taskOpinionList))
                {
                    TaskOpinion taskOpinion = taskOpinionList.get(taskOpinionList.size() - 1);
                    List<CommuReceiver> commuReceiverList =
                        commuReceiverService.getByOpinionId(taskOpinion.getOpinionId());
                    if (BeanUtils.isNotEmpty(commuReceiverList))
                    {
                        tempLaunchId = taskOpinion.getExeUserId();
                    }
                }
            }
        }
        IFormModel formModel = bpmFormDefService.getForm(processRun, nodeId, tempLaunchId, ctxPath, variables);
        // 如果是沟通任务 那么不允许沟通者有编辑表单的权限
        if (taskEntity.getDescription().equals(TaskOpinion.STATUS_COMMUNICATION.toString()))
        {
            form = bpmFormHandlerService.getFormDetail(processRun
                .getFormDefId(), processRun.getBusinessKey(), tempLaunchId, processRun, ctxPath, false);
            formModel.setFormHtml(form);
        }
        if (!formModel.isValid())
        {
            ModelAndView rtnModel = RequestUtil.getTipInfo(getText("controller.task.formIsChange"));
            return rtnModel;
        }
        
        String detailUrl = formModel.getDetailUrl();
        
        Boolean isExtForm = (Boolean)(formModel.getFormType() > 0);
        String headHtml = "";
        if (formModel.getFormType() == 0)
        {
            form = formModel.getFormHtml();
            IDataTemplate bpmDataTemplate = dataTemplateService.getByFormKey(processRun.getFormDefId());
            if (bpmDataTemplate != null)
            {
                headHtml = dataTemplateService.getFormHeadHtml(bpmDataTemplate.getId(), ctxPath);
            }
        }
        else
        {
            form = formModel.getFormUrl();
        }
        
        Boolean isEmptyForm = formModel.isFormEmpty();
        
        // 是否会签任务
        boolean isSignTask = bpmService.isSignTask(taskEntity);
        if (isSignTask)
        {
            handleSignTask(mv, instanceId, nodeId, actDefId, userId);
        }
        
        // 是否支持回退
        boolean isCanBack = bpmService.getIsAllowBackByTask(taskEntity);
        // 是否转办
        boolean isCanAssignee = bpmTaskExeService.isAssigneeTask(taskEntity, bpmDefinition);
        
        // 是否执行隐藏路径
        boolean isHidePath = getIsHidePath(bpmNodeSet.getIsHidePath());
        
        boolean isHandChoolse = false;
        if (!isHidePath)
        {
            boolean canChoicePath = this.bpmService.getCanChoicePath(actDefId, taskId);
            Long startUserId = UserContextUtil.getCurrentUserId();
            List<NodeTranUser> nodeTranUserList =
                this.bpmService.getNodeTaskUserMap(taskId, startUserId, canChoicePath);
            if (nodeTranUserList.size() > 1)
            {
                isHandChoolse = true;
            }
            
        }
        // 自定义跳转设置路径
        if (bpmNodeSet.getJumpType() != null && bpmNodeSet.getJumpType().equals("4"))
        {
            NodeSet nodeSet = nodeSetService.getByDefIdNodeId(bpmDefinition.getDefId(), nodeId);
            JSONArray jumpSetting = JSONArray.fromObject(nodeSet.getJumpSetting());
            /*
             * for(Object obj:jumpSetting){ JSONObject obj_=(JSONObject)obj; TaskOpinion
             * op=this.taskOpinionService.getLatestTaskOpinion(Long.valueOf(instanceId), obj_.getString("nodeId"));
             * obj_.put("opinion", op==null?false:op.getExeFullname()+","+op.getExeUserId()); }
             */
            mv.addObject("customJumpNodeMap", jumpSetting);
        }
        ExecutionEntity executionEntity = bpmService.getExecution(instanceId);
        if (executionEntity != null && executionEntity.getSuperExecutionId() != null)
        {
            ExecutionEntity superExecutionEntity = bpmService.getExecution(executionEntity.getSuperExecutionId());
            mv.addObject("superInstanceId", superExecutionEntity.getProcessInstanceId());
        }
        // 获取页面显示的按钮
        Map<String, List<NodeButton>> mapButton = bpmNodeButtonService.getMapByDefNodeId(defId, nodeId);
        
        // 取常用语
        List<String> taskAppItems =
            taskAppItemService.getApprovalByDefKeyAndTypeId(bpmDefinition.getDefKey(), bpmDefinition.getTypeId());
        // 节点审批意见模板--从常用语设置中读取
        List<TaskApprovalItems> spyjModels = taskAppItemService.getSpyjModel(bpmDefinition, taskEntity);
        
        // 获取保存的意见
        TaskOpinion taskOpinion = taskOpinionService.getOpinionByTaskId(Long.parseLong(taskId), userId);
        
        // 帮助文档
        ISysFile sysFile = null;
        if (BeanUtils.isNotEmpty(bpmDefinition.getAttachment()))
            sysFile = sysFileService.getById(bpmDefinition.getAttachment());
        
        JSONObject processRunMap = JSONObject.fromObject(processRun);

        //20200921 测试子流程时zmz对headhtml进行了判空
        if (formModel.getHeadHtml()==null){
            headHtml="";
        }else {
            String preHeadHtml=formModel.getHeadHtml().substring(0,formModel.getHeadHtml().indexOf("${ctx}"));
            String afterHeadHtml=formModel.getHeadHtml().substring(formModel.getHeadHtml().indexOf("${ctx}")+6);
            formModel.setHeadHtml(preHeadHtml+ctxPath+afterHeadHtml);
            headHtml=formModel.getHeadHtml();
        }


        return mv.addObject("task", taskEntity)
            .addObject("bpmNodeSet", bpmNodeSet)
            .addObject("processRun", processRun)
            .addObject("bpmDefinition", bpmDefinition)
            .addObject("isSignTask", isSignTask)
            .addObject("isCanBack", isCanBack)
            .addObject("isCanAssignee", isCanAssignee)
            .addObject("isHidePath", isHidePath)
            .addObject("toBackNodeId", toBackNodeId)
            .addObject("form", form)
            .addObject("isExtForm", isExtForm)
            .addObject("isEmptyForm", isEmptyForm)
            .addObject("taskAppItems", taskAppItems)
            .addObject("spyjModels", JSON.toJSON(spyjModels))
            .addObject("mapButton", mapButton)
            .addObject("detailUrl", detailUrl)
            .addObject("isManage", isManage)
            .addObject("bpmGangedSets", gangedSetJarray)
            .addObject("sysFile", sysFile)
            .addObject("taskOpinion", taskOpinion)
            .addObject("isHandChoolse", Boolean.valueOf(isHandChoolse))
            .addObject("processRunMap", processRunMap.toString())
            .addObject("curUserId", sysUser.getUserId().toString())
            .addObject("curUserName", sysUser.getFullname())
            .addObject("curUserLoginName", sysUser.getUsername())
            .addObject("headHtml", headHtml)
            .addObject("curDate", DateUtil.getDateString(new Date(), "yyyy-MM-dd"))
            .addObject("curDateTime", DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"))
            .addObject("variables", JSONObject.fromObject(variables))// 添加流程变量到variables中;
        	.addObject("tableId", bpmNodeSet.getFormDef().getTableId());
    }
    
    /**
     * 产生的沟通意见任务，并发送到沟通人
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("toStartCommunication")
    public void toStartCommunicatexttion(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        PrintWriter out = response.getWriter();
        ResultMessage resultMessage = null;
        String cmpIds = request.getParameter("cmpIds");
        if (StringUtil.isEmpty(cmpIds))
        {
            resultMessage =
                new ResultMessage(ResultMessage.Fail, getText("controller.task.toStartComm.inputCommunicaer"));
            out.print(resultMessage);
            return;
        }
        try
        {
            String taskId = request.getParameter("taskId");
            String opinion = request.getParameter("opinion");
            String informType = RequestUtil.getString(request, "informType");
            // 保存意见
            TaskEntity taskEntity = bpmService.getTask(taskId);
            ProcessRun processRun = processRunService.getByActInstanceId(new Long(taskEntity.getProcessInstanceId()));
            
            processRunService.saveCommuniCation(taskEntity, opinion, informType, cmpIds, processRun.getSubject());
            ProcessCmd taskCmd = BpmUtil.getProcessCmd(request);
            // handlerFormData(taskCmd, processRun,taskEntity.getTaskDefinitionKey());
            processRunService.handlerFormData(taskCmd, processRun, taskEntity.getTaskDefinitionKey());
            
            Long runId = processRun.getRunId();
            
            String memo =
                getText("controller.task.saveOpinion.memo", processRun.getSubject(), taskEntity.getName(), opinion);
            bpmRunLogService.addRunLog(runId, RunLog.OPERATOR_TYPE_ADDOPINION, memo);
            
            resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.task.complete.complete"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.task.complete.complete.fail"));
        }
        out.print(resultMessage);
    }
    
    /**
     * 产生的流转任务，并发送到流转人
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("toStartTransTo")
    public void toStartTransTo(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        PrintWriter out = response.getWriter();
        ResultMessage resultMessage = null;
        String cmpIds = request.getParameter("cmpIds");
        if (StringUtil.isEmpty(cmpIds))
        {
            resultMessage =
                new ResultMessage(ResultMessage.Fail, getText("controller.task.toStartComm.inputCommunicaer"));
            out.print(resultMessage);
            return;
        }
        try
        {
            String taskId = request.getParameter("taskId");
            String opinion = request.getParameter("opinion");
            String informType = RequestUtil.getString(request, "informType");
            String transType = request.getParameter("transType");
            String action = request.getParameter("action");
            // 保存意见
            TaskEntity taskEntity = bpmService.getTask(taskId);
            ProcessRun processRun = processRunService.getByActInstanceId(new Long(taskEntity.getProcessInstanceId()));
            
            processRunService.saveTransTo(taskEntity, opinion, informType, cmpIds, transType, action, processRun);
            
            ProcessCmd taskCmd = BpmUtil.getProcessCmd(request);
            // handlerFormData(taskCmd, processRun,taskEntity.getTaskDefinitionKey());
            processRunService.handlerFormData(taskCmd, processRun, taskEntity.getTaskDefinitionKey());
            
            Long runId = processRun.getRunId();
            
            String memo =
                getText("controller.task.saveOpinion.memo", processRun.getSubject(), taskEntity.getName(), opinion);
            bpmRunLogService.addRunLog(runId, RunLog.OPERATOR_TYPE_ADDOPINION, memo);
            
            resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.task.complete.complete"));
        }
        catch (Exception e)
        {
            resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.task.complete.complete.fail"));
            e.printStackTrace();
        }
        out.print(resultMessage);
    }
    
    /**
     * 是否执行隐藏路径
     * 
     * @param isHidePath
     * @return
     */
    private boolean getIsHidePath(Short isHidePath)
    {
        if (BeanUtils.isEmpty(isHidePath))
            return false;
        if (NodeSet.HIDE_PATH.shortValue() == isHidePath.shortValue())
            return true;
        return false;
    }
    
    /**
     * 处理会签
     * 
     * @param mv
     * @param instanceId
     * @param nodeId
     * @param actDefId
     * @param userId
     *            当前用户
     */
    private void handleSignTask(ModelAndView mv, String instanceId, String nodeId, String actDefId, Long userId)
    {
        
        List<TaskSignData> signDataList = taskSignDataService.getByNodeAndInstanceId(instanceId, nodeId);
        // 获取会签规则
        NodeSign bpmNodeSign = bpmNodeSignService.getByDefIdAndNodeId(actDefId, nodeId);
        
        mv.addObject("signDataList", signDataList);
        mv.addObject("bpmNodeSign", bpmNodeSign);
        mv.addObject("curUser", (ISysUser)UserContextUtil.getCurrentUser());
        // 获取当前组织
        Long orgId = UserContextUtil.getCurrentOrgId();
        
        // "允许直接处理"特权
        boolean isAllowDirectExecute =
            bpmNodeSignService.checkNodeSignPrivilege(actDefId, nodeId, NodePrivilegeType.ALLOW_DIRECT, userId, orgId);
        // "允许补签"特权
        boolean isAllowRetoactive = bpmNodeSignService
            .checkNodeSignPrivilege(actDefId, nodeId, NodePrivilegeType.ALLOW_RETROACTIVE, userId, orgId);
        // "一票决断"特权
        boolean isAllowOneVote = bpmNodeSignService
            .checkNodeSignPrivilege(actDefId, nodeId, NodePrivilegeType.ALLOW_ONE_VOTE, userId, orgId);
        mv.addObject("isAllowDirectExecute", isAllowDirectExecute)
            .addObject("isAllowRetoactive", isAllowRetoactive)
            .addObject("isAllowOneVote", isAllowOneVote);
        
    }
    
    @RequestMapping("getForm")
    public ModelAndView getForm(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String ctxPath = request.getContextPath();
        String taskId = RequestUtil.getString(request, "taskId");
        String returnUrl = RequestUtil.getPrePage(request);
        // 查找任务节点
        TaskEntity taskEntity = bpmService.getTask(taskId);
        String action = RequestUtil.getString(request, "action", "");
        if (taskEntity == null)
        {
            return new ModelAndView("redirect:notExist.do");
        }
        String instanceId = taskEntity.getProcessInstanceId();
        String taskName = taskEntity.getTaskDefinitionKey();
        String actDefId = taskEntity.getProcessDefinitionId();
        Long userId = UserContextUtil.getCurrentUserId();
        
        Definition bpmDefinition = bpmDefinitionService.getByActDefId(actDefId);
        
        ProcessRun processRun = processRunService.getByActInstanceId(new Long(instanceId));
        
        String form = "";
        Boolean isExtForm = false;
        Boolean isEmptyForm = false;
        
        Map<String, Object> variables = taskService.getVariables(taskId);
        
        if (bpmDefinition != null)
        {
            IFormModel formModel = bpmFormDefService.getForm(processRun, taskName, userId, ctxPath, variables);
            
            isExtForm = formModel.getFormType() > 0;
            if (formModel.getFormType() == 0)
            { // 在线表单
                form = formModel.getFormHtml();
            }
            else if (formModel.getFormType() == 1)
            { // url表单
                form = formModel.getFormUrl();
            }
            else if (formModel.getFormType() == 2)
            { // 有明细url
                form = formModel.getDetailUrl();
            }
            
            isEmptyForm = formModel.isFormEmpty();
        }
        
        return getAutoView().addObject("task", taskEntity)
            .addObject("form", form)
            .addObject("bpmDefinition", bpmDefinition)
            .addObject("isExtForm", isExtForm)
            .addObject("isEmptyForm", isEmptyForm)
            .addObject("action", action)
            .addObject("processRun", processRun)
            .addObject("returnUrl", returnUrl);
    }
    
    /**
     * 完成任务
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("complete")
    @Action(description = "执行流程任务",execOrder = ActionExecOrder.AFTER, 
    detail = "执行流程任务，业务数据<#assign entity=dataTemplateService.getById(Long.valueOf(__displayId__))/>" + "${sysAuditLinkService.getFormTableDesc(entity.tableId,pk)}", 
    exectype = SysAuditExecType.EXE_TASK,
    ownermodel=SysAuditModelType.FLOW_MANAGEMENT)
    public void complete(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        
        logger.debug(getText("controller.task.complete.loggermsg"));
        ISysUser curUser = (ISysUser)UserContextUtil.getCurrentUser();
        PrintWriter out = response.getWriter();
        ResultMessage resultMessage = null;
        String taskId = RequestUtil.getString(request, "taskId");
        TaskEntity task = bpmService.getTask(taskId);
        if (task == null)
        {
            resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.task.complete.hasExecuted"));
            out.print(resultMessage);
            return;
        }
        String actDefId = task.getProcessDefinitionId();
        Definition bpmDefinition = bpmDefinitionService.getByActDefId(actDefId);
        if (Definition.STATUS_INST_DISABLED.equals(bpmDefinition.getStatus()))
        {
            resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.task.complete.flowProhibited"));
            out.print(resultMessage);
            return;
        }
        Long userId = curUser.getUserId();
        // add by hxl 20151205
        // 如果前台传过来用户ID，则用这个ID。
        // 此功能用作自动任务时，当前人员和任务人员不符的情况
        String sUserId = RequestUtil.getString(request, "userId");
        if (!sUserId.equals(""))
        {
            userId = Long.parseLong(sUserId);
        }
        // request 参数
        ProcessCmd taskCmd = BpmUtil.getProcessCmd(request);
        
        taskCmd.setCurrentUserId(userId.toString());
        
        String assignee = task.getAssignee();
        // 非管理员,并且没有任务的权限。
        boolean isAdmin = taskCmd.getIsManage().shortValue() == 1;
        if (!isAdmin)
        {
            boolean rtn = processRunService.getHasRightsByTask(new Long(taskId), userId);
            if (!rtn)
            {
                resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.task.notExecutors"));
                out.print(resultMessage);
                return;
            }
        }
        
        // 记录日志。
        logger.info(taskCmd.toString());
        if (FlowUtil.isAssigneeNotEmpty(assignee) && !task.getAssignee().equals(userId.toString()) && !isAdmin)
        {
            resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.task.complete.lock"));
        }
        else
        {
            String errorUrl = RequestUtil.getErrorUrl(request);
            String ip = RequestUtil.getIpAddr(request);
            try
            {
                // processRunService.nextProcess(taskCmd);
                processRunService.nextProcess(taskCmd, true);
                resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.task.complete.complete"));
            }
            catch (ActivitiVarNotFoundException ex)
            {
                resultMessage = new ResultMessage(ResultMessage.Fail,
                    getText("controller.task.complete.varIsExist") + ":" + ex.getMessage());
                // 添加错误消息到日志
                this.sysErrorLogService.addError(curUser.getFullname(), curUser.getUsername(), ip, ex.getMessage(), errorUrl);
            }
            catch (ActivitiInclusiveGateWayException ex)
            {
                resultMessage = new ResultMessage(ResultMessage.Fail, ex.getMessage());
                // 添加错误消息到日志
                this.sysErrorLogService.addError(curUser.getFullname(), curUser.getUsername(), ip, ex.getMessage(), errorUrl);
            }
            catch (ActivitiOptimisticLockingException ex)
            {// 流程最后一个节点的收到沟通消息的人员没有反馈
                String curTaskNode = task.getTaskDefinitionKey();
                String notBackUsers = "";
                // 获取沟通意见任务
                List<ProcessTask> taskEntityList = taskDao.getCommunicationTaskByParentTaskId(taskId);
                Integer receiveStstus = 2;
                int i = 0;
                for (ProcessTask taskEntity : taskEntityList)
                {
                    // 校验是否为当前节点的沟通任务
                    if (taskEntity.getTaskDefinitionKey().equals(curTaskNode))
                    {
                        // 获取每个沟通接收人的沟通意见信息
                        CommuReceiver commuReceiver = commuReceiverDao.getByTaskId(new Long(taskEntity.getId()));
                        if (commuReceiver.getStatus() != 2)
                        {
                            i++;
                            receiveStstus = commuReceiver.getStatus();
                            String receiverName1 = commuReceiver.getReceivername();
                            if (i == 1)
                            {
                                notBackUsers = receiverName1;
                            }
                            else
                            {
                                String receiverName2 = "," + receiverName1;
                                if (!notBackUsers.contains(receiverName1))
                                {
                                    notBackUsers += receiverName2;
                                }
                            }
                        }
                    }
                }
                // 若是没有提交反馈信息的，以人性化界面显示
                if (receiveStstus != 2)
                {
                    ex.printStackTrace();
                    String message = ExceptionUtil.getExceptionMessage(ex);
                    message = "用户：" + notBackUsers + " " + "还没有及时反馈信息，请核对！";
                    resultMessage = new ResultMessage(ResultMessage.Fail, message);
                    // 添加错误消息到日志
                    this.sysErrorLogService.addError(curUser.getFullname(), curUser.getUsername(), ip, message, errorUrl);
                }
                else
                {
                    resultMessage = new ResultMessage(ResultMessage.Fail, ex.getMessage());
                    // 添加错误消息到日志
                    this.sysErrorLogService.addError(curUser.getFullname(), curUser.getUsername(), ip, ex.getMessage(), errorUrl);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                String str = MessageUtil.getMessage();
                if (StringUtil.isNotEmpty(str))
                {
                    resultMessage = new ResultMessage(ResultMessage.Fail, str);
                    // 添加错误消息到日志
                    this.sysErrorLogService.addError(curUser.getFullname(), curUser.getUsername(), ip, str, errorUrl);
                }
                else
                {
                    String message = ExceptionUtil.getExceptionMessage(ex);
                    resultMessage = new ResultMessage(ResultMessage.Fail, message);
                    // 添加错误消息到日志
                    this.sysErrorLogService.addError(curUser.getFullname(), curUser.getUsername(), ip, message, errorUrl);
                }
            }
        }
        resultMessage.addData("businessKey", taskCmd.getBusinessKey());
        LogThreadLocalHolder.putParamerter("pk", taskCmd.getBusinessKey());
        out.print(resultMessage);
    }
    
    /**
     * 外部子流程中表单数据保存
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("subTaskComplete")
    public void subTaskComplete(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        
        ISysUser curUser = (ISysUser)UserContextUtil.getCurrentUser();
        PrintWriter out = response.getWriter();
        ResultMessage resultMessage = null;
        String taskId = RequestUtil.getString(request, "taskId");
        TaskEntity task = bpmService.getTask(taskId);
        if (task == null)
        {
            resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.task.complete.hasExecuted"));
            out.print(resultMessage);
            return;
        }
        String actDefId = task.getProcessDefinitionId();
        Definition bpmDefinition = bpmDefinitionService.getByActDefId(actDefId);
        if (Definition.STATUS_INST_DISABLED.equals(bpmDefinition.getStatus()))
        {
            resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.task.complete.flowProhibited"));
            out.print(resultMessage);
            return;
        }
        Long userId = curUser.getUserId();
        // add by hxl 20151205
        // 如果前台传过来用户ID，则用这个ID。
        // 此功能用作自动任务时，当前人员和任务人员不符的情况
        String sUserId = RequestUtil.getString(request, "userId");
        if (!sUserId.equals(""))
        {
            userId = Long.parseLong(sUserId);
        }
        
        ProcessCmd taskCmd = BpmUtil.getProcessCmd(request);
        
        taskCmd.setCurrentUserId(userId.toString());
        
        String assignee = task.getAssignee();
        // 非管理员,并且没有任务的权限。
        boolean isAdmin = taskCmd.getIsManage().shortValue() == 1;
        if (!isAdmin)
        {
            boolean rtn = processRunService.getHasRightsByTask(new Long(taskId), userId);
            if (!rtn)
            {
                resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.task.notExecutors"));
                out.print(resultMessage);
                return;
            }
        }
        
        // 记录日志。
        logger.info(taskCmd.toString());
        if (FlowUtil.isAssigneeNotEmpty(assignee) && !task.getAssignee().equals(userId.toString()) && !isAdmin)
        {
            resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.task.complete.lock"));
        }
        else
        {
            String opName = "完成任务";
            String errorUrl = RequestUtil.getErrorUrl(request);
            String ip = RequestUtil.getIpAddr(request);
            try
            {
                processRunService.nextProcess(taskCmd, true);
                resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.task.complete.complete"));
            }
            catch (ActivitiVarNotFoundException ex)
            {
                resultMessage = new ResultMessage(ResultMessage.Fail,
                    getText("controller.task.complete.varIsExist") + ":" + ex.getMessage());
                // 添加错误消息到日志
                this.sysErrorLogService.addError(curUser.getFullname(), curUser.getUsername(), ip, ex.getMessage(), errorUrl);
            }
            catch (ActivitiInclusiveGateWayException ex)
            {
                resultMessage = new ResultMessage(ResultMessage.Fail, ex.getMessage());
                // 添加错误消息到日志
                this.sysErrorLogService.addError(curUser.getFullname(), curUser.getUsername(), ip, ex.getMessage(), errorUrl);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                String str = MessageUtil.getMessage();
                if (StringUtil.isNotEmpty(str))
                {
                    resultMessage = new ResultMessage(ResultMessage.Fail, str);
                    // 添加错误消息到日志
                    this.sysErrorLogService.addError(curUser.getFullname(), curUser.getUsername(), ip, str, errorUrl);
                }
                else
                {
                    String message = ExceptionUtil.getExceptionMessage(ex);
                    resultMessage = new ResultMessage(ResultMessage.Fail, message);
                    // 添加错误消息到日志
                    this.sysErrorLogService.addError(curUser.getFullname(), curUser.getUsername(), ip, message, errorUrl);
                }
            }
        }
        out.print(resultMessage);
    }
    
    /**
     * 锁定任务
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("claim")
    @Action(description = "锁定任务")
    public void claim(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = RequestUtil.getString(request, "taskId");
        // int isAgent = RequestUtil.getInt(request, "isAgent");
        String preUrl = RequestUtil.getPrePage(request);
        String assignee = UserContextUtil.getCurrentUserId().toString();
        // 代理任务，则设置锁定的assignee为代理人
        
        try
        {
            TaskEntity taskEntity = bpmService.getTask(taskId);
            ProcessRun processRun = processRunService.getByActInstanceId(new Long(taskEntity.getProcessInstanceId()));
            Long runId = processRun.getRunId();
            taskService.claim(taskId, assignee);
            String memo = getText("controller.task.lock.memo", processRun.getSubject(), taskEntity.getName());
            bpmRunLogService.addRunLog(runId, RunLog.OPERATOR_TYPE_LOCK, memo);
            saveSuccessResultMessage(request.getSession(), getText("controller.task.claim.lock"));
        }
        catch (Exception ex)
        {
            saveSuccessResultMessage(request.getSession(), getText("controller.task.claim.otherLock"));
        }
        response.sendRedirect(preUrl);
    }
    
    /**
     * 解锁任务
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("unlock")
    @Action(description = "解锁任务")
    public ModelAndView unlock(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = request.getParameter("taskId");
        
        if (StringUtils.isNotEmpty(taskId))
        {
            TaskEntity taskEntity = bpmService.getTask(taskId);
            ProcessRun processRun = processRunService.getByActInstanceId(new Long(taskEntity.getProcessInstanceId()));
            Long runId = processRun.getRunId();
            bpmService.updateTaskAssigneeNull(taskId);
            String memo = getText("controller.task.unlock.memo", processRun.getSubject(), taskEntity.getName());
            bpmRunLogService.addRunLog(runId, RunLog.OPERATOR_TYPE_UNLOCK, memo);
            saveSuccessResultMessage(request.getSession(), getText("controller.task.unlock.success"));
        }
        return new ModelAndView("redirect:forMe.do");
    }
    
    /**
     * 任务跳转窗口显示
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("freeJump")
    public ModelAndView freeJump(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = RequestUtil.getString(request, "taskId");
        Map<String, Map<String, String>> jumpNodesMap = bpmService.getJumpNodes(taskId);
        ModelAndView view = this.getAutoView();
        view.addObject("jumpNodeMap", jumpNodesMap);
        return view;
    }
    
    /** 
    * @Title: customJump 
    * @Description: TODO(自定义跳转路径) 
    * @param @param request
    * @param @param response
    * @param @return
    * @param @throws
    * @return ModelAndView    返回类型 
    * @throws 
    */
    @RequestMapping("customJump")
    public ModelAndView customJump(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = RequestUtil.getString(request, "taskId");
        NodeSet nodeSet = this.nodeSetService.getByTaskId(taskId);
        ModelAndView view = this.getAutoView();
        view.addObject("jumpNodeMap", JSONArray.fromObject(nodeSet.getJumpSetting()));
        return view;
    }
    
    /**
     * 动态创建任务加载显示页
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("dynamicCreate")
    public ModelAndView dynamicCreate(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = request.getParameter("taskId");
        TaskEntity task = bpmService.getTask(taskId);
        return this.getAutoView().addObject("task", task);
    }
    
    /**
     * 获取某个流程实例上某个节点的配置执行人员
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getTaskUsers")
    @ResponseBody
    public List<TaskExecutor> getTaskUsers(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        
        // 任务Id
        String taskId = request.getParameter("taskId");
        
        TaskEntity taskEntity = bpmService.getTask(taskId);
        
        String nodeId = RequestUtil.getString(request, "nodeId"); // 所选择的节点Id
        
        if (StringUtil.isEmpty(nodeId))
        {
            nodeId = taskEntity.getTaskDefinitionKey(); // 目标节点Id
        }
        
        String actDefId = taskEntity.getProcessDefinitionId();
        
        String actInstId = taskEntity.getProcessInstanceId();
        
        Map<String, Object> vars = runtimeService.getVariables(taskEntity.getExecutionId());
        
        String startUserId = vars.get(BpmConst.StartUser).toString();
        
        @SuppressWarnings("unchecked")
        List<TaskExecutor> taskExecutorList =
            bpmNodeUserService.getExeUserIds(actDefId, actInstId, nodeId, startUserId, "", vars);
        
        return taskExecutorList;
    }
    
    /**
     * 指派任务所属人
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Action(description = "任务指派所属人", detail = "<#if StringUtils.isNotEmpty(taskIds)>" + "流程"
        + "<#list StringUtils.split(taskIds,\",\") as item>" + "【${SysAuditLinkService.getProcessRunLink(item)}】"
        + "</#list>" + "的任务指派给【${SysAuditLinkService.getSysUserLink(Long.valueOf(userId))}】" + "</#if>")
    @RequestMapping("assign")
    public ModelAndView assign(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskIds = request.getParameter("taskIds");
        String userId = request.getParameter("userId");
        
        if (StringUtils.isNotEmpty(taskIds))
        {
            String[] tIds = taskIds.split("[,]");
            if (tIds != null)
            {
                for (String tId : tIds)
                {
                    bpmService.assignTask(tId, userId);
                }
            }
        }
        saveSuccessResultMessage(request.getSession(), getText("controller.task.assign.success"));
        return new ModelAndView("redirect:list.do");
    }
    
    /**
     * 任务交办设置任务的执行人。
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("delegate")
    @Action(description = "任务交办", detail = "<#if StringUtils.isNotEmpty(taskId) && StringUtil.isNotEmpty(userId)>"
        + "交办流程【${SysAuditLinkService.getProcessRunLink(taskId)}】的任务【${taskName}】"
        + "给用户【${SysAuditLinkService.getSysUserLink(Long.valueOf(userId))}】" + "</#if>")
    public void delegate(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        PrintWriter writer = response.getWriter();
        String taskId = request.getParameter("taskId");
        String userId = request.getParameter("userId");
        // String delegateDesc = request.getParameter("memo");
        ResultMessage message = null;
        // TODO ZYG 任务交办
        if (StringUtils.isNotEmpty(taskId) && StringUtil.isNotEmpty(userId))
        {
            LogThreadLocalHolder.putParamerter("taskName", bpmService.getTask(taskId).getName());
            // processRunService.delegate(taskId, userId, delegateDesc);
            message = new ResultMessage(ResultMessage.Success, getText("controller.task.setAssignee.success"));
        }
        else
        {
            message = new ResultMessage(ResultMessage.Fail, getText("controller.task.setAssignee.notParam"));
        }
        writer.print(message);
    }
    
    @RequestMapping("changeAssignee")
    @Action(description = "更改任务执行人")
    public ModelAndView changeAssignee(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = request.getParameter("taskId");
        TaskEntity taskEntity = bpmService.getTask(taskId);
        ISysUser curUser = (ISysUser)UserContextUtil.getCurrentUser();
        return getAutoView().addObject("taskEntity", taskEntity).addObject("curUser", curUser).addObject("handlersMap",
            handlersMap);
    }
    
    @RequestMapping("setAssignee")
    @Action(description = "任务指派")
    public void setAssignee(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        PrintWriter writer = response.getWriter();
        String taskId = request.getParameter("taskId");
        String userId = request.getParameter("userId");
        
        String voteContent = RequestUtil.getString(request, "voteContent");
        String informType = RequestUtil.getString(request, "informType");
        ResultMessage message = null;
        try
        {
            message = processRunService.updateTaskAssignee(taskId, userId, voteContent, informType);
        }
        catch (Exception e)
        {
            message = new ResultMessage(ResultMessage.Fail, e.getMessage());
        }
        writer.print(message);
    }
    
    /**
     * 设置任务的执行人
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("setDueDate")
    @Action(description = "设置任务到期时间")
    public ModelAndView setDueDate(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskIds = request.getParameter("taskIds");
        String dueDates = request.getParameter("dueDates");
        if (StringUtils.isNotEmpty(taskIds) && StringUtils.isNotEmpty(dueDates))
        {
            String[] tIds = taskIds.split("[,]");
            String[] dates = dueDates.split("[,]");
            if (tIds != null)
            {
                for (int i = 0; i < dates.length; i++)
                {
                    if (StringUtils.isNotEmpty(dates[i]))
                    {
                        Date dueDate = DateUtils.parseDate(dates[i], new String[] {"yyyy-MM-dd HH:mm:ss"});
                        bpmService.setDueDate(tIds[i], dueDate);
                    }
                }
            }
        }
        return new ModelAndView("redirect:list.do");
    }
    
    /**
     * 删除任务
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("delete")
    @Action(description = "删除任务", execOrder = ActionExecOrder.BEFORE, detail = "<#if StringUtils.isNotEmpty(taskId)>"
        + "<#assign entity1=bpmService.getTask(taskId)/>"
        + "用户删除了任务【${entity1.name}】,该任务属于流程【${SysAuditLinkService.getProcessRunLink(taskId)}】"
        + "</#elseif StringUtils.isNotEmpty(id)>" + "<#list StringUtils.split(id,\",\") as item>"
        + "<#assign entity2=bpmService.getTask(item)/>"
        + "用户删除了任务【${entity2.name}】,该任务属于流程【${SysAuditLinkService.getProcessRunLink(item)}】" + "</#list>" + "</#if>")
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ResultMessage message = null;
        try
        {
            String taskId = request.getParameter("taskId");
            String[] taskIds = request.getParameterValues("id");
            if (StringUtils.isNotEmpty(taskId))
            {
                bpmService.deleteTask(taskId);
                
                TaskEntity task = bpmService.getTask(taskId);
                ProcessRun processRun = processRunService.getByActInstanceId(new Long(task.getProcessInstanceId()));
                String memo = getText("controller.task.delete.memo", task.getName(), processRun.getProcessName());
                bpmRunLogService.addRunLog(processRun.getRunId(), RunLog.OPERATOR_TYPE_DELETETASK, memo);
                taskService.deleteTask(taskId);
                
            }
            else if (taskIds != null && taskIds.length != 0)
            {
                bpmService.deleteTasks(taskIds);
                for (int i = 0; i < taskIds.length; i++)
                {
                    String id = taskIds[i];
                    TaskEntity task = bpmService.getTask(id);
                    ProcessRun processRun = processRunService.getByActInstanceId(new Long(task.getProcessInstanceId()));
                    String memo = getText("controller.task.delete.memo", task.getName(), processRun.getProcessName());
                    bpmRunLogService.addRunLog(processRun.getRunId(), RunLog.OPERATOR_TYPE_DELETETASK, memo);
                    taskService.deleteTask(id);
                }
            }
            message = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
        }
        catch (Exception e)
        {
            message = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail"));
        }
        addMessage(message, request);
        return new ModelAndView("redirect:list.do");
    }
    
    /**
     * 返回某个某个用户代理给当前用户的任务列表
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     *//*
         * / @RequestMapping("forAgent") public ModelAndView forAgent(HttpServletRequest request, HttpServletResponse
         * response) throws Exception { ModelAndView mv = getAutoView(); Long userId = RequestUtil.getLong(request,
         * "userId"); QueryFilter filter = new QueryFilter(request, "taskItem"); Calendar cal = Calendar.getInstance();
         * Date curTime = cal.getTime(); cal.add(Calendar.DATE, -1); Date yesterday = cal.getTime();
         * 
         * filter.addFilter("curTime", curTime); filter.addFilter("yesterday", yesterday); List<TaskEntity> list = null;
         * SysUserAgent sysUserAgent = null; // 具体人员的代理 if (userId != 0) { sysUserAgent =
         * sysUserAgentService.getById(userId); } if (sysUserAgent != null) {
         * 
         * // 代理设置是否过期 if (sysUserAgent.getStarttime() != null) { int result =
         * sysUserAgent.getStarttime().compareTo(curTime); if (result > 0) { list = new ArrayList<TaskEntity>(); mv =
         * getAutoView().addObject("taskList", list).addObject( "userId", userId); return mv; } } if
         * (sysUserAgent.getEndtime() != null) { cal.add(Calendar.DATE, -1); int result =
         * sysUserAgent.getEndtime().compareTo(yesterday); if (result <= 0) { list = new ArrayList<TaskEntity>(); mv =
         * getAutoView().addObject("taskList", list).addObject( "userId", userId); return mv; } } if
         * (sysUserAgent.getIsall().intValue() == SysUserAgent.IS_ALL_FLAG) {// 全部代理 list = bpmService.getTaskByUserId(
         * sysUserAgent.getAgentuserid(), filter); } else {// 部分代理 StringBuffer actDefId = new StringBuffer("");
         * List<String> notInBpmAgentlist = bpmAgentService .getNotInByAgentId(sysUserAgent.getAgentid()); for (String
         * ba : notInBpmAgentlist) { actDefId.append("'").append(ba).append("',"); } if (notInBpmAgentlist.size() > 0) {
         * actDefId.deleteCharAt(actDefId.length() - 1); } list =
         * bpmService.getAgentTasks(sysUserAgent.getAgentuserid(), actDefId.toString(), filter); } } else { list =
         * bpmService.getAllAgentTask(UserContextUtil.getCurrentUserId(), filter); } mv =
         * getAutoView().addObject("taskList", list).addObject("userId", userId);
         * 
         * return mv; }
         */
    
    /**
     * 返回目标节点及其节点的处理人员映射列表。
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("tranTaskUserMap")
    public ModelAndView tranTaskUserMap(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        int isStart = RequestUtil.getInt(request, "isStart", 0);
        String taskId = request.getParameter("taskId");
        String actDefId = request.getParameter("actDefId");
        int selectPath = RequestUtil.getInt(request, "selectPath", 1);
        
        boolean canChoicePath = bpmService.getCanChoicePath(actDefId, taskId);
        
        Long startUserId = UserContextUtil.getCurrentUserId();
        List<NodeTranUser> nodeTranUserList = null;
        if (isStart == 1)
        {
            Map<String, Object> vars = new HashMap<String, Object>();
            nodeTranUserList = bpmService.getStartNodeUserMap(actDefId, startUserId, vars);
        }
        else
        {
            nodeTranUserList = bpmService.getNodeTaskUserMap(taskId, startUserId, canChoicePath);
        }
        
        return getAutoView().addObject("nodeTranUserList", nodeTranUserList)
            .addObject("selectPath", selectPath)
            .addObject("canChoicePath", canChoicePath);
    }
    
    /**
     * 结合前台任务管理列表，点击某行任务时，显示的任务简单明细
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("miniDetail")
    public ModelAndView miniDetail(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = request.getParameter("taskId");
        
        TaskEntity taskEntity = bpmService.getTask(taskId);
        
        if (taskEntity == null)
        {
            return new ModelAndView("/oa/flow/taskNotExist.jsp");
        }
        
        // 取到任务的侯选人员
        Set<TaskExecutor> candidateUsers = taskUserService.getCandidateExecutors(taskId);
        
        ProcessRun processRun = processRunService.getByActInstanceId(new Long(taskEntity.getProcessInstanceId()));
        
        Definition definition = bpmDefinitionService.getByActDefId(taskEntity.getProcessDefinitionId());
        
        List<ProcessTask> curTaskList = bpmService.getTasks(taskEntity.getProcessInstanceId());
        
        return getAutoView().addObject("taskEntity", taskEntity)
            .addObject("processRun", processRun)
            .addObject("candidateUsers", candidateUsers)
            .addObject("processDefinition", definition)
            .addObject("curTaskList", curTaskList);
    }
    
    /**
     * 准备更新任务的路径
     * 
     * @param request
     * @param response
     * @returngetTaskUsers
     * @throws Exception
     */
    @RequestMapping("changePath")
    public ModelAndView changePath(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String taskId = request.getParameter("taskId");
        TaskEntity taskEntity = bpmService.getTask(taskId);
        Map<String, String> taskNodeMap =
            bpmService.getTaskNodes(taskEntity.getProcessDefinitionId(), taskEntity.getTaskDefinitionKey());
        return this.getAutoView()
            .addObject("taskEntity", taskEntity)
            .addObject("taskNodeMap", taskNodeMap)
            .addObject("curUser", (ISysUser)UserContextUtil.getCurrentUser())
            .addObject("handlersMap", handlersMap);
    }
    
    /**
     * 保存变更路径
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("saveChangePath")
    @ResponseBody
    public String saveChangePath(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ProcessCmd processCmd = BpmUtil.getProcessCmd(request);
        processRunService.nextProcess(processCmd, true);
        saveSuccessResultMessage(request.getSession(), getText("controller.task.saveChangePath"));
        return SUCCESS;
    }
    
    /**
     * 结束流程任务
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("end")
    @Action(description = "结束流程任务", detail = "结束流程【${SysAuditLinkService.getProcessRunLink(taskId)}】的任务")
    public void end(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ResultMessage resultMessage = null;
        try
        {
            String taskId = request.getParameter("taskId");
            TaskEntity taskEntity = bpmService.getTask(taskId);
            ProcessRun processRun = processRunService.getByActInstanceId(new Long(taskEntity.getProcessInstanceId()));
            String voteContent =
                getText("controller.task.end.compile", ((ISysUser)UserContextUtil.getCurrentUser()).getFullname());
            ProcessCmd cmd = new ProcessCmd();
            cmd.setTaskId(taskId);
            cmd.setVoteAgree((short)0);
            cmd.setVoteContent(voteContent);
            cmd.setOnlyCompleteTask(true);
            // processRunService.nextProcess(cmd);
            processRunService.nextProcess(cmd, true);
            Long runId = processRun.getRunId();
            String memo = getText("controller.task.end.ended", processRun.getSubject());
            bpmRunLogService.addRunLog(runId, RunLog.OPERATOR_TYPE_ENDTASK, memo);
            resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.task.end.success"));
        }
        catch (Exception ex)
        {
            String str = MessageUtil.getMessage();
            if (StringUtil.isNotEmpty(str))
            {
                resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.task.end.fail") + ":" + str);
            }
            else
            {
                String message = ExceptionUtil.getExceptionMessage(ex);
                resultMessage = new ResultMessage(ResultMessage.Fail, message);
            }
        }
        response.getWriter().print(resultMessage);
    }
    
    /**
     * 根据任务结束流程实例。
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("endProcess")
    @Action(description = "根据任务结束流程实例")
    public void endProcess(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        PrintWriter out = response.getWriter();
        
        Long taskId = RequestUtil.getLong(request, "taskId");
        String memo = RequestUtil.getString(request, "memo");
        // Long curUserId=UserContextUtil.getCurrentUserId();
        TaskEntity taskEnt = bpmService.getTask(taskId.toString());
        if (taskEnt == null)
        {
            writeResultMessage(out, getText("controller.task.isTaskExsit.completed"), ResultMessage.Fail);
        }
        String instanceId = taskEnt.getProcessInstanceId();
        ResultMessage message = null;
        try
        {
            String nodeId = taskEnt.getTaskDefinitionKey();
            ProcessRun processRun = bpmService.endProcessByInstanceId(new Long(instanceId), nodeId, memo);
            bpmFormHandlerService.updateProcessData(IFieldPool.FLOW_END_KEY,processRun.getBusinessKey(),processRun.getFormDefId());
            memo = getText("controller.task.endProcess.memo", processRun.getSubject(), memo);
            bpmRunLogService.addRunLog(processRun.getRunId(), RunLog.OPERATOR_TYPE_ENDTASK, memo);
            message = new ResultMessage(ResultMessage.Success, getText("controller.task.endProcess.success"));
            writeResultMessage(out, message);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            message = new ResultMessage(ResultMessage.Fail, ExceptionUtil.getExceptionMessage(ex));
            writeResultMessage(out, message);
        }
    }
    
    /**
     * 代办任务
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("pendingMatters")
    public ModelAndView pendingMatters(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        return getAutoView();
    }
    
    /**
     * 待办列表
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("pendingMattersList")
    public ModelAndView pendingMattersList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        QueryFilter filter = new QueryFilter(request, "taskItem");
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
        // 对已进行催办的任务做特殊处理
        List<TaskEntity> list = bpmService.getMySysTasks(filter);
       
        return getAutoView().addObject("taskList", list);
        
    }
    /**
     * 导出选择的数据
     */
    @RequestMapping("exportSelectData")
    public void exportSelectData(HttpServletRequest request,HttpServletResponse response){
    	 String tasksValue = RequestUtil.getString(request, "tasksValue");
    	 String headersValue = RequestUtil.getString(request, "headersValue");
    	 String fileName=RequestUtil.getString(request, "fileName");
    	 String[] headers=headersValue.split(",");
    	 String[] tasks=tasksValue.split(",,");
         List<Object[]> content=new ArrayList<Object[]>();
         for(String task:tasks){
         	Object[] objs=task.split("##");
         	content.add(objs);
         }
         ExcelUtil.exportToExcel(fileName,headers,content,response);
    }
    /**
     * 导出全部待办
     */
    @RequestMapping("exportAllPendingMatters")
    public void exportAllPendingMatters(HttpServletRequest request, HttpServletResponse response){
    	QueryFilter filter = new QueryFilter(request, "taskItem",false);
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
        List<TaskEntity> list = bpmService.getMySysTasks(filter);
        
        String[] headers={"序号","事项名称","流程名称","创 建 人","创建时间","状态","待办类型"};
        List<Object[]> content=new ArrayList<Object[]>();
        for(int i=0;i<list.size();i++){
        	Object obj=list.get(i);
        	ProcessTask task=(ProcessTask)obj;
        	Object[] objs=new Object[7];
        	objs[0]=i+1;
        	objs[1]=task.getSubject();
        	objs[2]=task.getProcessName();
        	objs[3]=task.getCreator();
        	objs[4]=task.getCreateTime();
        	objs[5]=getTaskStatus(task.getTaskStatus());
        	objs[6]=getPendingType(task.getDescription());
        	content.add(objs);
        }
        ExcelUtil.exportToExcel("全部待办任务",headers,content,response);
        
    }
    private String getTaskStatus(short t){
    	if(t==1){
    		return "审批中";
    	}else if(t==5){
    		return "已撤销";
    	}else if(t==6){
    		return "已驳回";
    	}else if(t==7){
    		return "已追回";
    	}
    	return "";
    }
    private String getPendingType(String description){
    	if(description.equals("-1")){
    		return "待办";
    	}else if(description.equals("21")){
    		return "转办";
    	}else if(description.equals("15")){
    		return "沟通意见";
    	}else if(description.equals("26")){
    		return "代理";
    	}else if(description.equals("38")){
    		return "流转意见";
    	}
    	return "";
    }
   
    /**
     * 批量审批任务.
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("batComplte")
    public void batComplte(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        PrintWriter out = response.getWriter();
        ResultMessage resultMessage = null;
        String taskIds = RequestUtil.getString(request, "taskIds");
        String opinion = RequestUtil.getString(request, "opinion");
        try
        {
            // processRunService.nextProcessBat(taskIds, opinion);
            processRunService.nextProcessBat(taskIds, opinion, true);
            
            String message = MessageUtil.getMessage();
            resultMessage = new ResultMessage(ResultMessage.Success, message);
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
        out.print(resultMessage);
    }
    
    /**
     * 检测任务是否存在。
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("isTaskExsit")
    public void isTaskExsit(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        Long taskId = RequestUtil.getLong(request, "taskId");
        PrintWriter out = response.getWriter();
        TaskEntity taskEnt = bpmService.getTask(taskId.toString());
        if (taskEnt == null)
        {
            writeResultMessage(out, getText("controller.task.isTaskExsit.completed"), ResultMessage.Fail);
        }
        else
        {
            writeResultMessage(out, getText("controller.task.isTaskExsit.exist"), ResultMessage.Success);
        }
    }
    
    @RequestMapping("dialog")
    @Action(description = "编辑流程抄送转发")
    public ModelAndView forward(HttpServletRequest request)
        throws Exception
    {
        return getAutoView().addObject("handlersMap", handlersMap);
        
    }
    
    @RequestMapping("toStartCommunicate")
    @Action(description = "编辑流程抄送转发")
    public ModelAndView toStartCommunicate(HttpServletRequest request)
        throws Exception
    {
        String btnname = RequestUtil.getString(request, "btnname");
        Map<String, IMessageHandler> map = new HashMap<String, IMessageHandler>();
        // 获取系统参数
        List<ISysParameter> communicateEmail =
            (List<ISysParameter>)sysParameterService.getByParamName("IS_COMMUNICATE_EMAIL");
        String[] paramvalue = communicateEmail.get(0).getParamvalue().split("#");
        int count = 0;
        Boolean isSendOne = false;
        Boolean isSendMore = false;
        for (String times : paramvalue)
        {
            if (times.equals("1"))
                count++;
        }
        if (count == 1)
            isSendOne = true;
        if (count >= 2)
            isSendMore = true;
        // by liubo 根据参数判断是流程沟通采用什么方式
        if (paramvalue.length != 0)
        {
            if (StringUtil.isNotEmpty(paramvalue[0]) && paramvalue[0].equals("1"))
            {
                Object email = handlersMap.get("1");
                map.put("1", (IMessageHandler)email);
            }
            if (StringUtil.isNotEmpty(paramvalue[1]) && paramvalue[1].equals("1"))
            {
                Object station = handlersMap.get("3");
                map.put("3", (IMessageHandler)station);
            }
            if (StringUtil.isNotEmpty(paramvalue[2]) && paramvalue[2].equals("1"))
            {
                Object rtx = handlersMap.get("4");
                map.put("4", (IMessageHandler)rtx);
            }
        }
        
        return getAutoView().addObject("handlersMap", map)
            .addObject("isSendOne", isSendOne)
            .addObject("isSendMore", isSendMore)
            .addObject("btnname", btnname);
    }
    
    @RequestMapping("toTransTo")
    @Action(description = "编辑流程抄送转发")
    public ModelAndView toTransTo(HttpServletRequest request)
        throws Exception
    {
        return getAutoView().addObject("handlersMap", handlersMap);
        
    }
    
    @RequestMapping("transToOpinionDialog")
    @Action(description = "编辑流程抄送转发")
    public ModelAndView transToOpinionDialog(HttpServletRequest request)
        throws Exception
    {
        return getAutoView().addObject("handlersMap", handlersMap);
        
    }
    
    @RequestMapping({"selExecutors"})
    @Action(description = "启动流程时可以配置下一个执行人获取第一个节点的配置")
    public ModelAndView selExecutors(HttpServletRequest request)
        throws Exception
    {
        String actDefId = RequestUtil.getString(request, "actDefId");
        List<FlowNode> firstNode = NodeCache.getFirstNode(actDefId);
        String scope = "";
        if (BeanUtils.isNotEmpty(firstNode))
        {
            FlowNode flowNode = (FlowNode)firstNode.get(0);
            String nodeId = flowNode.getNodeId();
            NodeSet bpmNodeSet = this.nodeSetService.getByActDefIdNodeId(nodeId, actDefId, "");
            // if (BeanUtils.isNotEmpty(bpmNodeSet))
            // scope = bpmNodeSet.getScope();
        }
        return getAutoView().addObject("scope", scope);
    }
    
    @RequestMapping("pendingMattersListBatchApprovalCfm")
    public ModelAndView pendingMattersListBatchApprovalCfm(HttpServletRequest request)
        throws Exception
    {
        
        return getAutoView();
    }
    
	/**
	 * 流程联动--数据字典通过name获取value
	 * @author liubo
	 * @date 2017年10月8日下午3:05:30
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("getDicValue")
	public void getDicValue(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String dicName = RequestUtil.getString(request, "dicName");
		String nodeKey = RequestUtil.getString(request, "nodeKey");
		
		String dicValue = "";
		IDictionaryService dicService=AppUtil.getBean(IDictionaryService.class);
		List<? extends IDictionary> dicList = dicService.getByNodeKey(nodeKey);
		for(IDictionary dictionary:dicList){
			if(dictionary.getItemName().equals(dicName)){
				dicValue = dictionary.getItemValue();
				break;
			}
		}
		response.getWriter().write(dicValue);
	}
}