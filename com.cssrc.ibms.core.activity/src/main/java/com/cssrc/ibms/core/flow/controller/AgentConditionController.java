package com.cssrc.ibms.core.flow.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.AgentCondition;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.service.AgentConditionService;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 *<pre>
 * 对象功能:条件代理的配置 控制器类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Controller
@RequestMapping("/oa/flow/agentCondition/")
public class AgentConditionController extends BaseController
{
	@Resource
	private AgentConditionService agentConditionService;
	@Resource
	private IFormFieldService formFieldService;

	@Resource
	private DefinitionService definitionService;

	@Resource
	private IFormDefService formDefService;
	
	/**
	 * 添加或更新条件代理的配置。
	 * @param request
	 * @param response
	 * @param agentCondition 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新条件代理的配置")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String resultMsg=null;		
		AgentCondition agentCondition=getFormObject(request);
		try{
			if(agentCondition.getId()==null||agentCondition.getId()==0){
				agentCondition.setId(UniqueIdUtil.genId());
				agentConditionService.add(agentCondition);
				resultMsg=getText("controller.add.success");
			}else{
			    agentConditionService.update(agentCondition);
				resultMsg=getText("controller.update.success");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得 AgentCondition 实体 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    protected AgentCondition getFormObject(HttpServletRequest request) throws Exception {
    
    	JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
    
		String json=RequestUtil.getString(request, "json");
		JSONObject obj = JSONObject.fromObject(json);
		
		AgentCondition agentCondition = (AgentCondition)JSONObject.toBean(obj, AgentCondition.class);
		
		return agentCondition;
    }
	
	/**
	 * 取得条件代理的配置分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看条件代理的配置分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<AgentCondition> list=agentConditionService.getAll(new QueryFilter(request,"agentConditionItem"));
		ModelAndView mv=this.getAutoView().addObject("agentConditionList",list);
		
		return mv;
	}
	
	/**
	 * 删除条件代理的配置
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除条件代理的配置")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			agentConditionService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success,getText("controller.del.success"));
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail,  getText("controller.del.fail")+":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑条件代理的配置
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑条件代理的配置")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		String returnUrl=RequestUtil.getPrePage(request);
		String flowKey=RequestUtil.getString(request, "flowKey");
		
		Definition bpmDefinition = definitionService.getMainByDefKey(flowKey);
		
		List<? extends IFormField> flowVars = formFieldService.getFlowVarByFlowDefId(bpmDefinition.getDefId(),true);
		
		Long tableId = formDefService.getTableIdByDefId(bpmDefinition.getDefId());
		
		return getAutoView()
				.addObject("tableId",tableId)
				.addObject("flowVars", flowVars)
				.addObject("defId",bpmDefinition.getDefId())
				.addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得条件代理的配置明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看条件代理的配置明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"id");
		AgentCondition agentCondition = agentConditionService.getById(id);	
		return getAutoView().addObject("agentCondition", agentCondition);
	}
	
}
