package com.cssrc.ibms.core.flow.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.system.intf.ISysTemplateService;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeMessage;
import com.cssrc.ibms.core.flow.model.UserCondition;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.NodeMessageService;
import com.cssrc.ibms.core.flow.service.UserConditionService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;

/**
 * 对象功能:流程消息控制器类  
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/nodeMessage/")
public class NodeMessageController extends BaseController{
	@Resource
	private NodeMessageService bpmNodeMessageService;
	@Resource
	private ISysTemplateService sysTempplateService;
	@Resource
	private DefinitionService bpmDefinitionService;
	@Resource
	private UserConditionService bpmUserConditionService;
	
	/**
	 * 取得流程节点邮件分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看流程节点邮件分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<NodeMessage> list=bpmNodeMessageService.getAll(new QueryFilter(request,"bpmNodeMessageItem"));
		ModelAndView mv=this.getAutoView().addObject("bpmNodeMessageList",list);
		return mv;
	}
	
	/**
	 * 删除流程节点邮件
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除流程节点邮件")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
		bpmNodeMessageService.delByIds(lAryId);
		response.sendRedirect(preUrl);
	}

	
	@RequestMapping("edit")
	@Action(description="编辑流程节点邮件")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		List<?extends ISysTemplate> tempList=sysTempplateService.getAll(new QueryFilter(request,"sysTemplateItem"));
		String actDefId=RequestUtil.getString(request,"actDefId");
		String nodeId=RequestUtil.getString(request,"nodeId");	
		
		List<UserCondition> userConditions = bpmUserConditionService.getByActDefIdAndNodeId(actDefId, nodeId);
		
		List<UserCondition> receiverMailConds = new ArrayList<UserCondition>();
		List<UserCondition> copyToMailConds = new ArrayList<UserCondition>();
		List<UserCondition> bccMailConds = new ArrayList<UserCondition>();
		List<UserCondition> receiverMobileConds = new ArrayList<UserCondition>();
		List<UserCondition> receiverInnerConds = new ArrayList<UserCondition>();
		
		for(UserCondition condition:userConditions){
			if(condition.getConditionType()==null){
				continue;
			}
			switch (condition.getConditionType().intValue()) {
			case UserCondition.CONDITION_TYPE_MSG_MAIL_RECEIVER:
				receiverMailConds.add(condition);
				break;
			case UserCondition.CONDITION_TYPE_MSG_MAIL_COPYTO:
				copyToMailConds.add(condition);
				break;
			case UserCondition.CONDITION_TYPE_MSG_MAIL_BCC:
				bccMailConds.add(condition);
				break;
			case UserCondition.CONDITION_TYPE_MSG_MOBILE_RECEIVER:
				receiverMobileConds.add(condition);
				break;
			case UserCondition.CONDITION_TYPE_MSG_INNER_RECEIVER:
				receiverInnerConds.add(condition);
				break;
			default:
				break;
			}
		}
		List<NodeMessage> bpmNodeMessages= bpmNodeMessageService.getListByActDefIdNodeId(actDefId, nodeId);
		Definition bpmDefinition=bpmDefinitionService.getByActDefId(actDefId);
		NodeMessage mailMessage=null;
		NodeMessage innerMessage=null;
		NodeMessage smsMessage=null;
		
		for(NodeMessage message:bpmNodeMessages){
			Short messageType = message.getMessageType();
			if(messageType==null){
				messageType = -1;
			}
			switch (messageType) {
			case NodeMessage.MESSAGE_TYPE_MAIL:
				mailMessage=message;
				break;
			case NodeMessage.MESSAGE_TYPE_INNER:
				innerMessage = message;
				break;
			case NodeMessage.MESSAGE_TYPE_SMS:
				smsMessage=message;
				break;
			default:
				break;
			}
		}
		return getAutoView()
			.addObject("defId", bpmDefinition.getDefId())
			.addObject("actDefId",actDefId)
			.addObject("nodeId", nodeId)
			/////////////////////
			.addObject("mailMessage",mailMessage)
			.addObject("innerMessage",innerMessage)
			.addObject("smsMessage",smsMessage)
			/////////////////////
			.addObject("receiverMailConds",receiverMailConds)
			.addObject("copyToMailConds",copyToMailConds)
			.addObject("bccMailConds",bccMailConds)
			.addObject("receiverInnerConds",receiverInnerConds)
			.addObject("receiverMobileConds",receiverMobileConds)
			/////////////////
			.addObject("receiverMailCondJsons",JSONArray.fromObject(receiverMailConds).toString())
			.addObject("bccMailCondJsons",JSONArray.fromObject(bccMailConds).toString())
			.addObject("copyToMailCondJsons",JSONArray.fromObject(copyToMailConds).toString())
			.addObject("receiverInnerCondJsons",JSONArray.fromObject(receiverInnerConds).toString())
			.addObject("receiverMobileCondJsons",JSONArray.fromObject(receiverMobileConds).toString())
			.addObject("tempList",tempList);
		
	}
	/**
	 * 取得流程节点邮件明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看流程节点邮件明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"id");
		NodeMessage bpmNodeMessage = bpmNodeMessageService.getById(id);		
		return getAutoView().addObject("bpmNodeMessage", bpmNodeMessage);
	}
	
	@RequestMapping("getFlowVars")
	@ResponseBody
	@Action(description="编辑消息参数流程变量设置") 
	public Map<String,Object> getFlowVars(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Long defId=RequestUtil.getLong(request, "defId");
		Map<String,Object> map=new HashMap<String, Object>();
		
		IFormFieldService bpmFormFieldService=(IFormFieldService)AppUtil.getBean(IFormFieldService.class);
		List<?extends IFormField> flowVars= bpmFormFieldService.getFlowVarByFlowDefId(defId);
		map.put("flowVars", flowVars);
		return map;
		
	}
	
	/**
	 * 消息节点  消息接收人员   人员规则设置
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("receiverSetting")
	public ModelAndView receiverSetting(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Definition bpmDefinition = bpmDefinitionService.getByActDefId(actDefId);
		int type = RequestUtil.getInt(request, "type");
		List<UserCondition> receiverSettings=null;
		switch (type) {
		case UserCondition.CONDITION_TYPE_MSG_MAIL_RECEIVER:
			receiverSettings = bpmUserConditionService.getReceiverMailConditions(actDefId, nodeId);
			break;
		case UserCondition.CONDITION_TYPE_MSG_MAIL_COPYTO:
			receiverSettings = bpmUserConditionService.getCopyToMailConditions(actDefId, nodeId);
			break;
		case UserCondition.CONDITION_TYPE_MSG_MAIL_BCC:
			receiverSettings = bpmUserConditionService.getBccMailConditions(actDefId, nodeId);
			break;
		case UserCondition.CONDITION_TYPE_MSG_INNER_RECEIVER:
			receiverSettings = bpmUserConditionService.getReceiverInnerConditions(actDefId, nodeId);
			break;
		
		case UserCondition.CONDITION_TYPE_MSG_MOBILE_RECEIVER:
			receiverSettings = bpmUserConditionService.getReceiverSmsConditions(actDefId, nodeId);
			break;
		default:
			receiverSettings=new ArrayList<UserCondition>();
			break;
		}
		ModelAndView mv = getAutoView();
		mv.addObject("receiverSettings", receiverSettings)
		  .addObject("bpmDefinition", bpmDefinition)
		  .addObject("nodeId",nodeId)
		  .addObject("conditionType",type);
		return mv;
	}
	
	
	@RequestMapping("getReceiverUserCondition")
	@ResponseBody
	public Map<String,Object> getReceiverUserCondition(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		int status = 0;
		String msg = "";
		try{
			String actDefId = RequestUtil.getString(request, "actDefId");
			String nodeId = RequestUtil.getString(request, "nodeId");
			int type = RequestUtil.getInt(request, "receiverType");
			List<UserCondition> conditions  = null;
			switch (type) {
			case UserCondition.CONDITION_TYPE_MSG_MAIL_RECEIVER:
				conditions = bpmUserConditionService.getReceiverMailConditions(actDefId, nodeId);
				break;
			case UserCondition.CONDITION_TYPE_MSG_MAIL_COPYTO:
				conditions = bpmUserConditionService.getCopyToMailConditions(actDefId, nodeId);
				break;
			case UserCondition.CONDITION_TYPE_MSG_MAIL_BCC:
				conditions = bpmUserConditionService.getBccMailConditions(actDefId, nodeId);
				break;
			case UserCondition.CONDITION_TYPE_MSG_INNER_RECEIVER:
				conditions = bpmUserConditionService.getReceiverInnerConditions(actDefId, nodeId);
				break;
			case UserCondition.CONDITION_TYPE_MSG_MOBILE_RECEIVER:
				conditions = bpmUserConditionService.getReceiverSmsConditions(actDefId, nodeId);
				break;
			default:
				conditions = new ArrayList<UserCondition>();
				break;
			}
			map.put("conditions",conditions);
		}catch (Exception e) {
			status = -1;
			msg = e.getMessage();
		}
		map.put("status", status);
		map.put("msg", msg);
		return map;
	}
}
