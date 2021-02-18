package com.cssrc.ibms.core.flow.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.activity.util.BpmUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeRule;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.NodeRuleService;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:流程节点规则 控制器类
 * 开发人员:zhulongchao
 */
@Controller
@RequestMapping("/oa/flow/nodeRule/")
@Action(ownermodel=SysAuditModelType.FLOW_MANAGEMENT)
public class NodeRuleController extends BaseController
{
	@Resource
	private NodeRuleService nodeRuleService;
	@Resource
	private IBpmService bpmService;
	@Resource
	private DefinitionService definitionService;
	
	@Resource
	private NodeSetService nodeSetService;
	
	
	/**
	 * 取得流程节点规则分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看流程节点规则分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<NodeRule> list=nodeRuleService.getAll(new QueryFilter(request,"bpmNodeRuleItem"));
		ModelAndView mv=this.getAutoView().addObject("bpmNodeRuleList",list);
		return mv;
	}
	
	/**
	 * 删除流程节点规则
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除流程节点规则",
			execOrder=ActionExecOrder.BEFORE,
			detail="<#list StringUtils.split(ruleId,\",\") as item>" +
							"<#assign entity = bpmNodeRuleService.getById(Long.valueOf(item))/>" +
							"<#if item_index==0>" +
							    "删除流程定义【${SysAuditLinkService.getDefinitionLink(entity.actDefId)}】的节点 【${SysAuditLinkService.getNodeName(entity.actDefId,entity.nodeId)}】 自定义工具按钮：" +
							"</#if>"+
							"【${entity.ruleName}】 " +
					"</#list>"
	)
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ResultMessage resultMessage = null;
//		String preUrl= RequestUtil.getPrePage(request);
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "ruleId");
			nodeRuleService.delByIds(lAryId);
			resultMessage=new ResultMessage(ResultMessage.Success,getText("record.deleted",getText("controller.bpmNodeRule")));
		}
		catch (Exception e) {
			resultMessage = new ResultMessage(ResultMessage.Fail,getText("record.delete.fail",getText("controller.bpmNodeRule"))+":" + e.getMessage());
		}
		
		response.getWriter().print(resultMessage);
		//addMessage(resultMessage, request);
		//response.sendRedirect(preUrl);	
	}

	@RequestMapping("edit")
	@Action(description="编辑流程节点规则")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		String deployId=RequestUtil.getString(request, "deployId");
		String actDefId=RequestUtil.getString(request, "actDefId");
		String nodeId=RequestUtil.getString(request, "nodeId");
		String nodeName=RequestUtil.getString(request, "nodeName");
		
		FlowNode flowNode= NodeCache.getByActDefId(actDefId).get(nodeId);
		
		NodeRule bpmNodeRule=new NodeRule();
		String defXml = bpmService.getDefXmlByDeployId(deployId);
		Definition bpmDefinition= definitionService.getByActDefId(actDefId);
		//取得可跳转活动节点
		List<String> nodeList=new ArrayList<String>();
		//nodeList.add(nodeId);
		Map<String, Map<String, String>> activityList= BpmUtil.getTranstoActivitys(defXml, nodeList);
		
		NodeSet bpmNodeSet=nodeSetService.getByMyActDefIdNodeId(actDefId, nodeId);
		
		bpmNodeRule.setActDefId(actDefId);
		bpmNodeRule.setNodeId(nodeId);
		
		ModelAndView mv=getAutoView();
		String vers= request.getHeader("USER-AGENT");
		if(vers.indexOf("MSIE 6")!=-1){
			mv= new ModelAndView("/oa/flow/nodeRuleEdit_ie6.jsp");
		}
		mv.addObject("activityList", activityList)
			.addObject("nodeName",nodeName)
			.addObject("bpmNodeRule",bpmNodeRule)
			.addObject("deployId",deployId)
			.addObject("actDefId",actDefId)
			.addObject("nodeId",nodeId)
			.addObject("bpmNodeSet",bpmNodeSet)
			.addObject("defId", bpmDefinition.getDefId())
			.addObject("nextNodes", flowNode.getNextFlowNodes());
		
		return mv;
	}
	
	/**
	 * 根据流程定义id和节点ID获取流程规则列表。
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getByDefIdNodeId")
	public void getByDefIdNodeId(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String actDefId=RequestUtil.getString(request, "actDefId");
		String nodeId=RequestUtil.getString(request, "nodeId");
		PrintWriter out=response.getWriter();
		List<NodeRule> ruleList= nodeRuleService.getByDefIdNodeId(actDefId, nodeId);
		String str=JSONArray.fromObject(ruleList).toString();
		out.print(str);
	}
	
	/**
	 * 排序
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("sortRule")
	public void sortRule(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		try{
			String ruleIds=RequestUtil.getString(request, "ruleids");
			nodeRuleService.reSort(ruleIds);
			ResultMessage resObj=new ResultMessage(ResultMessage.Success,getText("controller.bpmNodeRule.sort.success"));
			response.getWriter().print(resObj);
			
		}
		catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.bpmNodeRule.sort.fail")+":" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
		
		
	}

	/**
	 * 取得流程节点规则明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看流程节点规则明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"ruleId");
		NodeRule bpmNodeRule = nodeRuleService.getById(id);		
		return getAutoView().addObject("bpmNodeRule", bpmNodeRule);
	}
	
	/**
	 * 根据ID获取json对象
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getById")
	public void getById(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		long id=RequestUtil.getLong(request,"ruleId");
		NodeRule bpmNodeRule = nodeRuleService.getById(id);	
		String rtn=JSONObject.fromObject(bpmNodeRule).toString();
	
		response.getWriter().print(rtn);
	}
	
	@RequestMapping("updateIsJumpForDef")
	@ResponseBody
	@Action(description="设置流程定义节点的跳转规则",
	detail="流程定义【${SysAuditLinkService.getDefinitionLink(actDefId)}的节点 ${nodeId}】的跳转规则为" +
			"<#if NodeSet.RULE_INVALID_NORMAL.equals(Short.valueOf(isJumpForDef))> 规则不符合条件时，任务按定义正常跳转" +
			"<#elseif NodeSet.RULE_INVALID_NO_NORMAL.equals(Short.valueOf(isJumpForDef))> 规则不符合条件时，任务仅是完成当前节点，不作跳转处理" +
			"</#if>" 
    )
	public String updateIsJumpForDef(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String nodeId=request.getParameter("nodeId");
		String actDefId=request.getParameter("actDefId");
		String isJumpForDef=request.getParameter("isJumpForDef");
		logger.debug("nodeId:" + nodeId + " actDefId:" + actDefId + " isJumpForDef:" + isJumpForDef);
		nodeSetService.updateIsJumpForDef(nodeId, actDefId, new Short(isJumpForDef));
		return "{success:true}";
	}
	
	

}
