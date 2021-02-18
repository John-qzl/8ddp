package com.cssrc.ibms.core.flow.controller;

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

import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.DefVar;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.service.DefVarService;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
/**
 * <pre>
 * 对象功能:流程变量定义 控制器类
 * 开发人员:zhulongchao 
 * <pre>
 */
@Controller
@RequestMapping("/oa/flow/defVar/")
@Action(ownermodel=SysAuditModelType.FLOW_MANAGEMENT)
public class DefVarController extends BaseController {
	@Resource
	private DefVarService defVarService;
	@Resource
	private IBpmService bpmService;
	@Resource
	public DefinitionService definitionService;
	@Resource
	private IFormFieldService formFieldService;

	/**
	 * 取得流程变量定义分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = "查看流程变量定义分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		Definition bpmDefinition = definitionService.getById(defId);
		String actDefId = bpmDefinition.getActDefId();
		Long actDeployId = bpmDefinition.getActDeployId();
		QueryFilter q = new QueryFilter(request, "bpmDefVarItem", false);
		if (defId != 0) {
			q.getFilters().put("defId", defId);
		}

		List<DefVar> list = defVarService.getAll(q);
		ModelAndView mv = this.getAutoView().addObject("bpmDefVarList", list)
				.addObject("defId", defId)
				.addObject("actDeployId", actDeployId)
				.addObject("actDefId", actDefId)
				.addObject("bpmDefinition", bpmDefinition);

		return mv;
	}

	/**
	 * 删除流程变量定义
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description = "删除流程变量定义",
			execOrder=ActionExecOrder.BEFORE,
			detail="<#list StringUtils.split(varId,\",\") as item>" +
						"<#assign entity = bpmDefVarService.getById(Long.valueOf(item))/>" +
						"<#if item_index==0>" +
							"删除流程定义【${SysAuditLinkService.getDefinitionLink(entity.defId)}】" +
						"</#if>" +
						"节点"+
						"<#if !StringUtil.isEmpty(entity.nodeName)>" +
							"【${entity.nodeName}】" +
						"</#if>" +
						"的变量:【 ${entity.varName}】、" +
				   "</#list>"
	)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage resultMessage = null;
		String preUrl = RequestUtil.getPrePage(request);
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "varId");
			defVarService.delByIds(lAryId);
			resultMessage = new ResultMessage(ResultMessage.Success, getText("record.deleted",getText("bpmDefVar.title")));
		} catch (Exception ex) {
			resultMessage = new ResultMessage(ResultMessage.Fail, getText("record.delete.fail",getText("bpmDefVar.title"))+":"+ex.getMessage());
		}
		addMessage(resultMessage, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping("getVars")
	@ResponseBody
	public List<DefVar> getVars(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		List<DefVar> list = defVarService.getVarsByFlowDefId(defId);
		return list;
	}

	@RequestMapping("edit")
	@Action(description = "编辑流程变量定义")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		Long varId = RequestUtil.getLong(request, "varId");
		Definition bpmDefinition = definitionService.getById(defId);
		String actDefId = bpmDefinition.getActDefId();
		Long actDeployId = bpmDefinition.getActDeployId();

		String returnUrl = RequestUtil.getPrePage(request);
		DefVar bpmDefVar = null;
		if (varId != 0) {
			bpmDefVar = defVarService.getById(varId);
		} else {
			bpmDefVar = new DefVar();
		}
		Map<String, String> nodeMap = bpmService.getExecuteNodesMap(actDefId,
				true);
		return getAutoView().addObject("bpmDefVar", bpmDefVar)
				.addObject("returnUrl", returnUrl).addObject("defId", defId)
				.addObject("nodeMap", nodeMap)
				.addObject("actDeployId", actDeployId)
				.addObject("actDefId", actDefId);
	}

	/**
	 * 取得流程变量定义明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description = "查看流程变量定义明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		long id = RequestUtil.getLong(request, "varId");
		DefVar bpmDefVar = defVarService.getById(id);
		return getAutoView().addObject("bpmDefVar", bpmDefVar).addObject(
				"actDefId", actDefId);
	}

	/**
	 * 根据流程的deployId和节点ID获取流程变量。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getByDeployNode")
	public ModelAndView getByDeployNode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String deployId = RequestUtil.getString(request, "deployId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		List<DefVar> varList = null;
		Long defId = RequestUtil.getLong(request, "defId");
		if (defId != 0) {
			varList = defVarService.getVarsByFlowDefId(defId);
		} else {
			varList = defVarService.getByDeployAndNode(deployId, nodeId);
		}
		return getAutoView().addObject("bpmDefVarList", varList);
	}

	/**
	 * 获取流程变量树
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getTree")
	@ResponseBody
	public String getTree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long defId = RequestUtil.getLong(request, "defId");
		JSONArray jarray = new JSONArray();
		List<DefVar> list = defVarService.getVarsByFlowDefId(defId);
		if(BeanUtils.isNotEmpty(list)){
			JSONObject jobject = new JSONObject().accumulate("name", getText("taskReminder.customVar"))
												 .accumulate("children", convertList2Json(list));
			jarray.add(jobject);
		}
		
		List<DefVar> list2 = getFormVars(defId);
		if(BeanUtils.isNotEmpty(list2)){
			JSONObject jobject = new JSONObject().accumulate("name", getText("taskReminder.flowVar"))
												 .accumulate("children", convertList2Json(list2));
			jarray.add(jobject);
		}
		return jarray.toString();
	}
	
	private JSONArray convertList2Json(List<DefVar> list){
		JSONArray jarray = new JSONArray();
		String name;
		JSONObject jobject;
		for (DefVar bpmDefVar : list) {
			name = bpmDefVar.getVarName();
			if (StringUtil.isNotEmpty(bpmDefVar.getVarDataType())) {
				name += "(" + bpmDefVar.getVarDataType() + ")";
			}
			jobject = new JSONObject().accumulate("id", bpmDefVar.getVarId())
									  .accumulate("varName", bpmDefVar.getVarName())
									  .accumulate("varKey", bpmDefVar.getVarKey())
									  .accumulate("type", bpmDefVar.getVarDataType())
									  .accumulate("name", name);
			jarray.add(jobject);
		}
		return jarray;
	}
	
	/**
	 * 获取表单变量
	 * @param defId
	 * @return
	 */
	private List<DefVar> getFormVars(Long defId){
		List<?extends IFormField> fieldList= formFieldService.getFlowVarByFlowDefId(defId);
		List<DefVar> list = new ArrayList<DefVar>();
		
		for(IFormField bpmFormField:fieldList){
			DefVar bpmDefVar = new DefVar();
			bpmDefVar.setVarId(bpmFormField.getFieldId());
			bpmDefVar.setVarName(bpmFormField.getFieldDesc());
			bpmDefVar.setVarKey(bpmFormField.getFieldName());
			bpmDefVar.setVarDataType(bpmFormField.getFieldType());
			list.add(bpmDefVar);
		}
		return list;
	}

}
