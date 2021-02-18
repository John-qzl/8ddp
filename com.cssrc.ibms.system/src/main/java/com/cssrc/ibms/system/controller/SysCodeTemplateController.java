package com.cssrc.ibms.system.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.SysCodeTemplate;
import com.cssrc.ibms.system.service.SysCodeTemplateService;
 
/**
 * 自定义表代码模版 控制器类
 * @author zhulongchao
 *
 */
@Controller
@RequestMapping("/oa/system/sysCodeTemplate/")
@Action(ownermodel=SysAuditModelType.SYSTEM_SETTING)
public class SysCodeTemplateController extends BaseController
{
	@Resource
	private SysCodeTemplateService sysCodeTemplateService;
	
	/**
	 * 添加或更新自定义表代码模版。
	 * @param request
	 * @param response
	 * @param sysCodeTemplate 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新自定义表代码模版",
			execOrder=ActionExecOrder.AFTER,
			detail="<#if isAdd>添加新<#else>更新</#if>自定义表代码模版："  
	)
	public void save(HttpServletRequest request, HttpServletResponse response,SysCodeTemplate sysCodeTemplate) throws Exception
	{
		String resultMsg=null;		
		try{
			boolean isadd=true;
			if(sysCodeTemplate.getId()==null){
				Long id=UniqueIdUtil.genId();
				sysCodeTemplate.setId(id);
				sysCodeTemplateService.add(sysCodeTemplate);
				resultMsg="添加代码模版成功";
			}else{
			    sysCodeTemplateService.update(sysCodeTemplate);
				resultMsg="更新代码模版成功";
				isadd=false;
			}
			LogThreadLocalHolder.putParamerter("isAdd", isadd);
			LogThreadLocalHolder.putParamerter("codetemId", sysCodeTemplate.getId().toString());
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	
	/**
	 * 取得自定义表代码模版分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看自定义表代码模版分页列表",detail="查看自定义表代码模版分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<SysCodeTemplate> list=sysCodeTemplateService.getAll(new QueryFilter(request,"sysCodeTemplateItem"));
		ModelAndView mv=this.getAutoView().addObject("sysCodeTemplateList",list);
		
		return mv;
	}
	
	/**
	 * 删除自定义表代码模版
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除自定义表代码模版",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除自定义表代码模版" +
					"<#list StringUtils.split(id,\",\") as item>" +
					"<#assign entity=sysCodeTemplateService.getById(Long.valueOf(item))/>" +
					"【${entity.templateName}】"+
				"</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			sysCodeTemplateService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, getText("controller.del.success") + ":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑自定义表代码模版
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="添加或编辑自定义表代码模版",
	detail="<#if isAdd>添加自定义表代码模版<#else>" +
			"编辑自定义表代码模版" +
			"<#assign entity=sysCodeTemplateService.getById(Long.valueOf(id))/>" +
			"【entity.templateName】" +
			"</#if>")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");

		String returnUrl=RequestUtil.getPrePage(request);
		SysCodeTemplate sysCodeTemplate=sysCodeTemplateService.getById(id);
		LogThreadLocalHolder.putParamerter("isAdd", sysCodeTemplate==null);
		return getAutoView().addObject("sysCodeTemplate",sysCodeTemplate).addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得自定义表代码模版明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看自定义表代码模版明细",detail="查看自定义表代码模版明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		long canReturn=RequestUtil.getLong(request, "canReturn",0);
		SysCodeTemplate sysCodeTemplate = sysCodeTemplateService.getById(id);	
		return getAutoView().addObject("sysCodeTemplate", sysCodeTemplate).addObject("canReturn",canReturn);
	}
	
	/**
	 * 取得初始化模板信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("init")
	@Action(description="初始化模板",detail="初始化模板")
	public void init(HttpServletRequest request,HttpServletResponse response)throws Exception
	{	
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			sysCodeTemplateService.initAllTemplate();
			message=new ResultMessage(ResultMessage.Success, "模板初始化成功");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "模板初始化失败" + ":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
}
