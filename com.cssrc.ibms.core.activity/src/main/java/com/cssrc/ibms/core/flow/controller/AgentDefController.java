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
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.AgentDef;
import com.cssrc.ibms.core.flow.service.AgentDefService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 *<pre>
 * 对象功能:代理的流程列表 控制器类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Controller
@RequestMapping("/oa/flow/agentDef/")
public class AgentDefController extends BaseController
{
	@Resource
	private AgentDefService agentDefService;
	
	
	/**
	 * 添加或更新代理的流程列表。
	 * @param request
	 * @param response
	 * @param agentDef 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新代理的流程列表")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String resultMsg=null;		
		AgentDef agentDef=getFormObject(request);
		try{
			if(agentDef.getId()==null||agentDef.getId()==0){
				agentDef.setId(UniqueIdUtil.genId());
				agentDefService.add(agentDef);
				resultMsg=getText("controller.add.success");
			}else{
			    agentDefService.update(agentDef);
				resultMsg=getText("controller.update.success");
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得 AgentDef 实体 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    protected AgentDef getFormObject(HttpServletRequest request) throws Exception {
    
    	JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
    
		String json=RequestUtil.getString(request, "json");
		JSONObject obj = JSONObject.fromObject(json);
		
		AgentDef agentDef = (AgentDef)JSONObject.toBean(obj, AgentDef.class);
		
		return agentDef;
    }
	
	/**
	 * 取得代理的流程列表分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看代理的流程列表分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<AgentDef> list=agentDefService.getAll(new QueryFilter(request,"agentDefItem"));
		ModelAndView mv=this.getAutoView().addObject("agentDefList",list);
		
		return mv;
	}
	
	/**
	 * 删除代理的流程列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除代理的流程列表")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			agentDefService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success,getText("controller.del.success"));
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, getText("controller.del.fail")+":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑代理的流程列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑代理的流程列表")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		String returnUrl=RequestUtil.getPrePage(request);
		AgentDef agentDef=agentDefService.getById(id);
		
		return getAutoView().addObject("agentDef",agentDef).addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得代理的流程列表明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看代理的流程列表明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"id");
		AgentDef agentDef = agentDefService.getById(id);	
		return getAutoView().addObject("agentDef", agentDef);
	}
	
}
