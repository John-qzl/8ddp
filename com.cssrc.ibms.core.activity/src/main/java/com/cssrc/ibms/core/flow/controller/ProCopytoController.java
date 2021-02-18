package com.cssrc.ibms.core.flow.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.ProCopyto;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.RunLog;
import com.cssrc.ibms.core.flow.service.ProCopytoService;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.RunLogService;
import com.cssrc.ibms.api.jms.intf.IMessageHandler;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
/**
 *<pre>
 * 对象功能:流程抄送转发 控制器类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Controller
@RequestMapping("/oa/flow/proCopyto/")
@Action(ownermodel=SysAuditModelType.FLOW_MANAGEMENT)
public class ProCopytoController extends BaseController
{
	@Resource
	private ProCopytoService proCopytoService;
	
	@Resource
	private ProcessRunService  processRunService;
	
	@Resource
	private RunLogService  runLogService;
	
	@Resource
	Map<String, IMessageHandler> handlersMap;
	
	
	/**
	 * 添加或更新流程抄送转发。
	 * @param request
	 * @param response
	 * @param bpmProCopyto 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新流程抄送转发",
			detail="<#if>添加<#else>更新</#if>流程" +
			"【${SysAuditLinkService.getProcessRunLink(Long.valueOf(runId)),subject}】的抄送转发")
	@Deprecated
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String resultMsg=null;		
		ProCopyto bpmProCopyto=getFormObject(request);
		
		
		//添加系统日志信息 -E
		boolean isAdd=true;
		try{
			if(bpmProCopyto.getCopyId()==null||bpmProCopyto.getCopyId()==0){
				bpmProCopyto.setCopyId(UniqueIdUtil.genId());
				proCopytoService.add(bpmProCopyto);
				resultMsg=getText("record.added",getText("controller.bpmProCopyto"));
			}else{
			    proCopytoService.update(bpmProCopyto);
				resultMsg=getText("record.updated",getText("controller.bpmProCopyto"));
				isAdd=false;
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
		//添加系统日志信息 -B
		try {
			LogThreadLocalHolder.putParamerter("isAdd", isAdd);
			LogThreadLocalHolder.putParamerter("runId", bpmProCopyto.getRunId().toString());
			LogThreadLocalHolder.putParamerter("subject", bpmProCopyto.getSubject().toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 取得 ProCopyto 实体 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    protected ProCopyto getFormObject(HttpServletRequest request) throws Exception {
    
    	JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
    
		String json=RequestUtil.getString(request, "json");
		JSONObject obj = JSONObject.fromObject(json);
		
		ProCopyto bpmProCopyto = (ProCopyto)JSONObject.toBean(obj, ProCopyto.class);
		
		return bpmProCopyto;
    }
	
	/**
	 * 取得流程抄送转发分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看流程抄送转发分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		QueryFilter queryFilter = new QueryFilter(request,"bpmProCopytoItem");
		Long runId = RequestUtil.getLong(request, "runId",0);
		List<ProCopyto> list=proCopytoService.getAll(queryFilter);
		ModelAndView mv=this.getAutoView().addObject("bpmProCopytoList",list).addObject("runId",runId);
		
		return mv;
	}
	
	/**
	 * 查看我的流程抄送转发列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("myList")
	@Action(description="查看我的流程抄送转发列表")
	public ModelAndView myList(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		QueryFilter filter = new QueryFilter(request,"bpmProCopytoItem");
		String nodePath = RequestUtil.getString(request, "nodePath");
		if(StringUtils.isNotEmpty(nodePath))
			filter.getFilters().put("nodePath",nodePath + "%");
		
		filter.getFilters().put("ccUid", UserContextUtil.getCurrentUserId());
		List<ProCopyto> list=proCopytoService.getMyList(filter);
	        Long porIndex = RequestUtil.getLong(request,"porIndex");
		Long tabIndex = RequestUtil.getLong(request,"tabIndex");
		ModelAndView mv=this.getAutoView()
			.addObject("bpmProCopytoList",list)
			 .addObject("porIndex",porIndex)
			        .addObject("tabIndex",tabIndex);
		
		return mv;
	}
	
	/**
	 * 查看我的流程抄送转发列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("browse")
	@Action(description="查看我的流程抄送转发列表")
	public ModelAndView browse(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
	      Long porIndex = RequestUtil.getLong(request,"porIndex");
	      Long tabIndex = RequestUtil.getLong(request,"tabIndex");
	      ModelAndView mv=this.getAutoView()
		        .addObject("porIndex",porIndex)
		        .addObject("tabIndex",tabIndex);
	      return mv;
	}
	
	/**
	 * 标记任务抄送消息为已读
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("mark")
	@ResponseBody
	@Action(description="标记任务抄送消息为已读",
			detail="标记任务抄送消息为已读:" +
			"<#list RequestUtil.getLongAryByStr(request, \"copyIds\") as item>" +
				"<#assign entity=bpmProCopytoService.getById(item)>"+
				"【${SysAuditLinkService.getSysUserLink(entity.ccUid,entity.ccUname)}】对流程【${SysAuditLinkService.getProcessRunLink(entity.runId,entity.subject)}】任务抄送消息为已读;" +
			"</#list>"	
	)
	public String mark(HttpServletRequest request,HttpServletResponse response){
		String copyIds = RequestUtil.getString(request, "copyIds");
		JSONObject jobject = new JSONObject();
		try{
			proCopytoService.updateReadStatus(copyIds);
			jobject.accumulate("result", true)
				   .accumulate("message", getText("controller.bpmProCopyto.mark"));
		}
		catch(Exception ex){
			jobject.accumulate("result", false)
			   .accumulate("message", ex.getMessage());
		
		}
		return jobject.toString();
	}
	
	/**
	 * 删除流程抄送转发
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除流程抄送转发",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除流程抄送转发:" +
			"<#list RequestUtil.getLongAryByStr(request, \"copyId\") as item>" +
				"<#assign entity=bpmProCopytoService.getById(item)>"+
				"删除 【${SysAuditLinkService.getSysUserLink(entity.ccUid,entity.ccUname)}】对流程【${SysAuditLinkService.getProcessRunLink(entity.runId,entity.subject)}】任务抄送消息;" +
			"</#list>"	
	)
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "copyId");
			proCopytoService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, getText("record.deleted",getText("controller.bpmProCopyto")));
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, getText("record.delete.fail",getText("controller.bpmProCopyto")) + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑流程抄送转发
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑流程抄送转发")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long copyId=RequestUtil.getLong(request,"copyId");
		String returnUrl=RequestUtil.getPrePage(request);
		ProCopyto bpmProCopyto=proCopytoService.getById(copyId);
		
		return getAutoView().addObject("bpmProCopyto",bpmProCopyto).addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得流程抄送转发明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看流程抄送转发明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long copyId=RequestUtil.getLong(request,"copyId");
		ProCopyto bpmProCopyto = proCopytoService.getById(copyId);	
		return getAutoView().addObject("bpmProCopyto", bpmProCopyto);
	}
	
	
	@RequestMapping("getCopyUserByInstId")
	@Action(description = "查看办结事宜流程列表")
	public ModelAndView getCopyUserByInstId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String returnUrl=RequestUtil.getPrePage(request);
		Long runId = RequestUtil.getLong(request, "runId");
		ProcessRun processRun = processRunService.getById(runId);
		QueryFilter filter = new QueryFilter(request,"bpmProCopytoItem");
		List<ProCopyto> list=proCopytoService.getUserInfoByRunId(filter);
		ModelAndView mv=this.getAutoView().addObject("bpmProCopytoList",list);
		mv.addObject(RequestUtil.RETURNURL, returnUrl)
			.addObject("isOpenDialog",RequestUtil.getInt(request, "isOpenDialog",0))
			.addObject("link",RequestUtil.getInt(request, "link"))
			.addObject("processRun",processRun);
		return mv;
	}
	
	/**
	 * 办结转发
	 * 
	 * @Methodname: finishDivert
	 * @Discription:
	 * @param request
	 * @param response
	 * @throws Exception
	 * @Author HH
	 * @Time 2012-11-16 下午9:06:57
	 */
	@RequestMapping("finishDivert")
	public void finishDivert(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResultMessage resultMessage;
		Long runId = RequestUtil.getLong(request, "runId", 0);
		String users = RequestUtil.getString(request, "assigneeId");
		String informType = RequestUtil.getStringAry(request, "informType");
		String suggestion =RequestUtil.getStringAry(request, "suggestion");
		
		
		if (runId == 0 || BeanUtils.isEmpty(users)) {
			resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.bpmProCopyto.finishDivert.fail"));
		} else {
			String[] userArray = users.split(",");
			List<String> userList = Arrays.asList(userArray);

			try {
				ProcessRun processRun = processRunService.getById(runId);
				ISysUser currUser = (ISysUser)UserContextUtil.getCurrentUser();
				processRunService.divertProcess(processRun, userList, currUser,informType,suggestion);
				String memo = getText("controller.bpmProCopyto.finishDivert.memo1") + processRun.getSubject();
				if (suggestion!=null || suggestion!="") {
					memo=memo+getText("controller.bpmProCopyto.finishDivert.memo2")+suggestion;
				}
				runLogService.addRunLog(runId,
						RunLog.OPERATOR_TYPE_FINISHDIVERT, memo);
			} catch (Exception e) {
				logger.error(getText("controller.bpmProCopyto.finishDivert.error"), e);
				resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.bpmProCopyto.finishDivert.fail"));
			}
			resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.bpmProCopyto.finishDivert.success"));
		}
		response.getWriter().print(resultMessage);
		
		LogThreadLocalHolder.putParamerter("curUser", (ISysUser)UserContextUtil.getCurrentUser());
	}
	
	
	@RequestMapping("forward")
	@Action(description="编辑流程抄送转发")
	public ModelAndView forward(HttpServletRequest request) throws Exception
	{
//		Long runId=RequestUtil.getLong(request,"runId");
//		List<Long> paramList=new ArrayList<Long>();
//		paramList.add(runId);
		return getAutoView()
				//.addObject("param",paramList)
				.addObject("handlersMap",handlersMap);
				
	}
}
