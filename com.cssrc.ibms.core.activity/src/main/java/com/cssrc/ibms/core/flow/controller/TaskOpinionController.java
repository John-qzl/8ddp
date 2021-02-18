package com.cssrc.ibms.core.flow.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.jms.intf.IMessageHandler;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.TaskOpinionService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.web.controller.BaseController;


/**
 * 对象功能:流程任务审批意见控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/taskOpinion/")
public class TaskOpinionController extends BaseController
{
	@Resource
	private TaskOpinionService taskOpinionService;
	@Resource
	private ProcessRunService processRunService;
	@Resource
	Map<String, IMessageHandler> handlersMap;	
	/**
	 * 取得某个流程任务审批意见分页列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看流程任务审批意见分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	

		String preUrl=RequestUtil.getPrePage(request);
		long runId=RequestUtil.getLong(request, "runId", 0L);
		Long actInstId=RequestUtil.getLong(request, "actInstId",0L);
	    ProcessRun  processRun=null;
		if(runId!=0L){
            processRun = processRunService.getById(runId);
            actInstId=Long.valueOf(processRun.getActInstId());
        }else if(actInstId.longValue()!=0L){
        }else{
            return RequestUtil.getTipInfo(getText("controller.taskOpinion.list.InputActRunId"));
        }
		//获取该流程所有有关任务审批意见列表
		List<TaskOpinion> list=taskOpinionService.getByActInstId(actInstId.toString());
		//设置代码执行人
		list = taskOpinionService.setTaskOpinionExecutor(list);
		
		ModelAndView mv=this.getAutoView().addObject("taskOpinionList",list)
							.addObject("processRun",processRun)
							.addObject("returnUrl", preUrl);
						
		return mv;
	}
	
	/**
	 * 取得某个流程任务审批意见分页列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getList")
	@Action(description="查看流程任务审批意见分页列表")
	public ModelAndView getList(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		
		String runId=request.getParameter("runId");
		ProcessRun processRun=null;
		String actInstId="";
		if(StringUtils.isNotEmpty(runId)){
			processRun=processRunService.getById(new Long(runId));
			actInstId=processRun.getActInstId();
		}

		List<TaskOpinion> list=taskOpinionService.getByActInstId(actInstId);
		if(processRun==null){
			processRun=processRunService.getByActInstanceId(new Long(actInstId));
		}
		ModelAndView mv=this.getAutoView().addObject("taskOpinionList",list)
							.addObject("processRun",processRun);
		return mv;
	}
	
	

    /** 
    * @Title: getNodeList 
    * @Description: TODO(取得某个流程任务审批意见) 
    * @param @param request
    * @param @param response
    * @param @return
    * @param @throws Exception    
    * @return ModelAndView    返回类型 
    * @throws 
    */
    @RequestMapping("getNodeList")
    @Action(description = "查看流程任务审批意见")
    public ModelAndView getNodeList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv =this.getView("opinion", "getNodeList");
        String preUrl = RequestUtil.getPrePage(request);
        Long actInstId = RequestUtil.getLong(request, "actInstId");
        String nodeId = RequestUtil.getString(request, "nodeId");
        // 获取该流程所有有关任务审批意见列表
        List<TaskOpinion> list = taskOpinionService.getByActInstIdTaskKey(actInstId, nodeId);
        // 设置代码执行人
        list = taskOpinionService.setTaskOpinionExecutor(list);
        mv.addObject("taskOpinionList", list);
        mv.addObject("returnUrl", preUrl);
        return mv;
    }
    
	/**
	 * 在表单中显示审批历史
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("listform")
	@Action(description="在表单中显示审批历史")
	public ModelAndView listform(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		String actInstId=request.getParameter("actInstId");
		List<TaskOpinion> list=taskOpinionService.getByActInstId(actInstId);
		ModelAndView mv=this.getAutoView().addObject("taskOpinionList",list);
		return mv;
	}
	
	@RequestMapping("dialog")
	@Action(description="编辑流程抄送转发")
	public ModelAndView forward(HttpServletRequest request) throws Exception
	{
		return getAutoView()
				.addObject("handlersMap",handlersMap);
				
	}
	
}
