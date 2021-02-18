package com.cssrc.ibms.core.flow.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cssrc.ibms.core.flow.model.NodePrivilege;
import com.cssrc.ibms.core.flow.model.NodeSign;
import com.cssrc.ibms.core.flow.service.NodeSignService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:会签任务投票规则 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/nodeSign/")
public class NodeSignFormController extends BaseFormController
{
	@Resource
	private NodeSignService nodeSignService;

	
	/**
	 * 添加或更新会签任务投票规则。
	 * @param request
	 * @param response
	 * @param bpmNodeSign 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新会签任务投票规则")
	public void save(HttpServletRequest request, HttpServletResponse response, NodeSign bpmNodeSign,BindingResult bindResult) throws Exception{
		ResultMessage resultMessage=validForm("bpmNodeSign", bpmNodeSign, bindResult, request);
		if(resultMessage.getResult()==ResultMessage.Fail){
			writeResultMessage(response.getWriter(),resultMessage);
			return;
		}
		
		//处理特权列表
		List<NodePrivilege> privileges = new ArrayList<NodePrivilege>();
		String[] usertypes = request.getParameterValues("userType");
		String[] cmpIds = request.getParameterValues("cmpIds");
		String[] cmpNames = request.getParameterValues("cmpNames");
		for (int i = 0; i < usertypes.length; i++) {
			String usertype = usertypes[i];
			String cmpId = cmpIds[i];
			String cmpName = cmpNames[i];
			if (StringUtil.isEmpty(usertype) || StringUtil.isEmpty(cmpName)) {
				continue;
			}

			NodePrivilege vo = new NodePrivilege();
			vo.setPrivilegemode(Long.valueOf(i));
			vo.setUsertype(Long.valueOf(usertype));
			vo.setCmpids(cmpId);
			vo.setCmpnames(cmpName);
			privileges.add(vo);
		}
		
		nodeSignService.addOrUpdateSignAndPrivilege(bpmNodeSign, privileges);
		
		String resultMsg=getText("record.saved",getText("controller.bpmNodeSign"));
		writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
	}
	
	/**
	 * 在实体对象进行封装前，从对应源获取原实体
	 * @param signId
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute
    protected NodeSign getFormObject(@RequestParam("signId") Long signId,Model model) throws Exception {
		logger.debug("enter NodeSign getFormObject here....");
		NodeSign bpmNodeSign=null;
		if(signId!=0L){
			bpmNodeSign=nodeSignService.getById(signId);
		}else{
			bpmNodeSign= new NodeSign();
		}
		return bpmNodeSign;
    }

}
