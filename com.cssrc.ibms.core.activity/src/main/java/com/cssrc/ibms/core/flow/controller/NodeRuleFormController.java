package com.cssrc.ibms.core.flow.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.flow.model.NodeRule;
import com.cssrc.ibms.core.flow.service.NodeRuleService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.core.util.result.ResultMessage;

/**
 * 对象功能:流程节点规则 控制器类
 * 开发人员:zhulongchao
 */
@Controller
@RequestMapping("/oa/flow/nodeRule/")
public class NodeRuleFormController extends BaseFormController
{
	@Resource
	private NodeRuleService nodeRuleService;
	
	/**
	 * 添加或更新流程节点规则。
	 * @param request
	 * @param response
	 * @param bpmNodeRule 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新流程节点规则")
	public void save(HttpServletRequest request, HttpServletResponse response, NodeRule bpmNodeRule,BindingResult bindResult) throws Exception
	{
		
		ResultMessage resultMessage=validForm("bpmNodeRule", bpmNodeRule, bindResult, request);
		//add your custom validation rule here such as below code:
		//bindResult.rejectValue("name","errors.exist.student",new Object[]{"jason"},"重复姓名");
		if(resultMessage.getResult()==ResultMessage.Fail)
		{
			writeResultMessage(response.getWriter(),resultMessage);
			return;
		}
		String resultMsg=null;
		String tmp=bpmNodeRule.getTargetNode();
		String[] aryTmp=tmp.split(",");
		bpmNodeRule.setTargetNode(aryTmp[0]);
		bpmNodeRule.setTargetNodeName(aryTmp[1]);
		if(bpmNodeRule.getRuleId()==0L){
			bpmNodeRule.setRuleId(UniqueIdUtil.genId());
			bpmNodeRule.setPriority(System.currentTimeMillis());
			nodeRuleService.add(bpmNodeRule);
			resultMsg=getText("record.added",getText("controller.bpmNodeRule"));
		}else{
			nodeRuleService.update(bpmNodeRule);
			resultMsg=getText("record.updated",getText("controller.bpmNodeRule"));
		}
		writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
	}
	
	/**
	 * 在实体对象进行封装前，从对应源获取原实体
	 * @param ruleId
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute
    protected NodeRule getFormObject(@RequestParam("ruleId") Long ruleId,Model model) throws Exception {
		logger.debug("enter NodeRule getFormObject here....");
		NodeRule bpmNodeRule=null;
		if(ruleId!=0L){
			bpmNodeRule=nodeRuleService.getById(ruleId);
		}else{
			bpmNodeRule= new NodeRule();
			
		}
		return bpmNodeRule;
    }

}
