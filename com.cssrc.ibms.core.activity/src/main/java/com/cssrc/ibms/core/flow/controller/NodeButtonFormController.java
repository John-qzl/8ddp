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
import com.cssrc.ibms.core.flow.model.NodeButton;
import com.cssrc.ibms.core.flow.service.NodeButtonService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseFormController;

/**
 * 对象功能:自定义工具条 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/nodeButton/")
public class NodeButtonFormController extends BaseFormController
{
	@Resource
	private NodeButtonService nodeButtonService;
	
	/**
	 * 添加或更新自定义工具条。
	 * @param request
	 * @param response
	 * @param bpmNodeButton 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新自定义工具条")
	public void save(HttpServletRequest request, HttpServletResponse response, NodeButton bpmNodeButton,BindingResult bindResult) throws Exception
	{
		
		ResultMessage resultMessage=validForm("bpmNodeButton", bpmNodeButton, bindResult, request);
		//add your custom validation rule here such as below code:
		//bindResult.rejectValue("name","errors.exist.student",new Object[]{"jason"},"重复姓名");
		if(resultMessage.getResult()==ResultMessage.Fail){
			writeResultMessage(response.getWriter(),resultMessage);
			return;
		}
		String resultMsg=null;
		if(bpmNodeButton.getId()==0){
			boolean rtn=nodeButtonService.isOperatorExist(bpmNodeButton);
			if(rtn){
				resultMsg=getText("controller.bpmNodeButton.def");
				writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
				return;
			}
			bpmNodeButton.setId(UniqueIdUtil.genId());
			bpmNodeButton.setSn(bpmNodeButton.getId());
			nodeButtonService.add(bpmNodeButton);
			
			resultMsg=getText("record.added",getText("controller.bpmNodeButton"));
		}else{
			boolean rtn=nodeButtonService.isOperatorExistForUpd(bpmNodeButton);
			if(rtn){
				resultMsg=getText("controller.bpmNodeButton.def");
				writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
				return;
			}
			nodeButtonService.update(bpmNodeButton);
			resultMsg=getText("record.updated",getText("controller.bpmNodeButton"));
		}
		writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
	}
	
	/**
	 * 在实体对象进行封装前，从对应源获取原实体
	 * @param ID
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute
    protected NodeButton getFormObject(@RequestParam("id") Long id,Model model) throws Exception {
		logger.debug("enter NodeButton getFormObject here....");
		NodeButton bpmNodeButton=null;
		if(id!=0){
			bpmNodeButton=nodeButtonService.getById(id);
		}else{
			bpmNodeButton= new NodeButton();
		}
		return bpmNodeButton;
    }

}
