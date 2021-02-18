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
import com.cssrc.ibms.core.flow.model.DefVar;
import com.cssrc.ibms.core.flow.service.DefVarService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseFormController;

/**
 * <pre>
 * 对象功能:流程变量定义 控制器类
 * 开发人员:zhulongchao 
 * <pre>
 */
@Controller
@RequestMapping("/oa/flow/defVar/")
public class DefVarFormController extends BaseFormController
{
	@Resource
	private DefVarService defVarService;
	
	/**
	 * 添加或更新流程变量定义。
	 * @param request
	 * @param response
	 * @param bpmDefVar 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新流程变量定义")
	public void save(HttpServletRequest request, HttpServletResponse response, DefVar bpmDefVar,BindingResult bindResult) throws Exception
	{
		ResultMessage resultMessage=validForm("bpmDefVar", bpmDefVar, bindResult, request);
		if(resultMessage.getResult()==ResultMessage.Fail)
		{
			writeResultMessage(response.getWriter(),resultMessage);
			return;
		}
		String nodeId=null;
		String nodeName=null;
		/*if(bpmDefVar.getVarScope().equals("task")){
			if(StringUtils.isNotEmpty(bpmDefVar.getNodeId())){
				String [] nodeSets=bpmDefVar.getNodeId().split(",");
				nodeId=nodeSets[0];
				nodeName=nodeSets[1];
			}
		}*/
		String resultMsg=null;
			if(bpmDefVar.getVarId()==null){
				boolean isExist=defVarService.isVarNameExist(bpmDefVar.getVarName(),bpmDefVar.getVarKey(),bpmDefVar.getDefId());
				if(!isExist){
				bpmDefVar.setVarId(UniqueIdUtil.genId());
				bpmDefVar.setNodeId(nodeId);
				bpmDefVar.setNodeName(nodeName);
				defVarService.add(bpmDefVar);
				resultMsg=getText("record.added",getText("bpmDefVar.title"));
				writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
			 }else{
			    	resultMsg=getText("controller.bpmDefVar.save.IsExist.nameOrKey");
					writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
			    }
			}
			else{
				//1名称和key都不修改
				//2只修改名称，但不修改key
				//3只修改key,但不修改名称
				//4名称和key同时修改
				DefVar var=defVarService.getById(bpmDefVar.getVarId());
				if(var.getVarName().equals(bpmDefVar.getVarName())&&var.getVarKey().equals(bpmDefVar.getVarKey())){
					bpmDefVar.setNodeId(nodeId);
					bpmDefVar.setNodeName(nodeName);
					defVarService.update(bpmDefVar);
					resultMsg=getText("record.updated",getText("bpmDefVar.title"));
					writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
				}else if(var.getVarName().equals(bpmDefVar.getVarName())&&!var.getVarKey().equals(bpmDefVar.getVarKey())){
					boolean rtn=defVarService.isVarNameExist(null,bpmDefVar.getVarKey(),bpmDefVar.getDefId());
					if(rtn){
						resultMsg=getText("controller.bpmDefVar.save.IsExist.key");
						writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
					}else{
						bpmDefVar.setNodeId(nodeId);
						bpmDefVar.setNodeName(nodeName);
						defVarService.update(bpmDefVar);
						resultMsg=getText("record.updated",getText("bpmDefVar.title"));
						writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
					}
				}else if(!var.getVarName().equals(bpmDefVar.getVarName())&&var.getVarKey().equals(bpmDefVar.getVarKey())){
					boolean rtn=defVarService.isVarNameExist(bpmDefVar.getVarName(),null,bpmDefVar.getDefId());
					if(rtn){
						resultMsg=getText("controller.bpmDefVar.save.IsExist.name");
						writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
					}else{
						bpmDefVar.setNodeId(nodeId);
						bpmDefVar.setNodeName(nodeName);
						defVarService.update(bpmDefVar);
						resultMsg=getText("record.updated",getText("bpmDefVar.title"));
						writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
					}
				}else{
					bpmDefVar.setNodeId(nodeId);
					bpmDefVar.setNodeName(nodeName);
					defVarService.update(bpmDefVar);
					resultMsg=getText("record.updated",getText("bpmDefVar.title"));
					writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
				}
				
			}
}
	/**
	 * 在实体对象进行封装前，从对应源获取原实体
	 * @param varId
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute
    protected DefVar getFormObject(@RequestParam("varId") Long varId,Model model) throws Exception {
		logger.debug("enter DefVar getFormObject here....");
		DefVar bpmDefVar=null;
		if(varId!=null){
			bpmDefVar=defVarService.getById(varId);
		}else{
			bpmDefVar= new DefVar();
		}
		return bpmDefVar;
    }

}
