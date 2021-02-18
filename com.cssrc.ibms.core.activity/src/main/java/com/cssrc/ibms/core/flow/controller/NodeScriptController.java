package com.cssrc.ibms.core.flow.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.core.flow.model.NodeScript;
import com.cssrc.ibms.core.flow.service.NodeScriptService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:节点运行脚本 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/nodeScript/")
@Action(ownermodel=SysAuditModelType.FLOW_MANAGEMENT)
public class NodeScriptController extends BaseController
{
	@Resource
	private NodeScriptService nodeScriptService;

	/**
	 * 添加或更新节点运行脚本。
	 * @param request
	 * @param response
	 * @param bpmNodeScript 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新节点运行脚本",
			detail="保存流程定义 【${SysAuditLinkService.getDefinitionLink(actDefId)}】" +
					"的节点 【${SysAuditLinkService.getNodeName(actDefId,nodeId)}】 的脚本设置（事件设置）"
	)
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String nodeId=RequestUtil.getString(request, "nodeId");
		String actDefId=RequestUtil.getString(request, "actDefId");
		
		String[] aryScript=request.getParameterValues("script");
		String[] aryScriptType=request.getParameterValues("scriptType");
		List<NodeScript> list=new ArrayList<NodeScript>();
		for(int i=0;i<aryScriptType.length;i++){
			String script=aryScript[i];
			Integer type=Integer.parseInt(aryScriptType[i]);
			if(StringUtil.isEmpty(script)) continue;
			
			NodeScript bpmNodeScript=new NodeScript();
			bpmNodeScript.setScript(script);
			bpmNodeScript.setScriptType(type);
			list.add(bpmNodeScript);
			
		}
		try{
			nodeScriptService.saveScriptDef(actDefId, nodeId, list);
			writeResultMessage(response.getWriter(),getText("controller.save.success"),ResultMessage.Success);
		}
		catch ( Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.save.fail")+":" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	@RequestMapping("edit")
	@Action(description="编辑节点运行脚本")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long defId=RequestUtil.getLong(request, "defId");
		String nodeId=RequestUtil.getString(request, "nodeId");
		String actDefId=RequestUtil.getString(request, "actDefId");
		String type=RequestUtil.getString(request, "type");
		 String parentActDefId = RequestUtil.getString(request, "parentActDefId", "");
		ModelAndView mv=getAutoView();
		String vers= request.getHeader("USER-AGENT");
		if(vers.indexOf("MSIE 6")!=-1){
			mv= new ModelAndView("/oa/flow/nodeScriptEdit_ie6.jsp");
		}
		Map<String,NodeScript> map= nodeScriptService.getMapByNodeScriptId(nodeId, actDefId);
		return mv.addObject("map",map)
				.addObject("type", type)
				.addObject("nodeId", nodeId)
				.addObject("actDefId", actDefId)
				.addObject("defId", defId)
		        .addObject("parentActDefId", parentActDefId);
				
	}

}
