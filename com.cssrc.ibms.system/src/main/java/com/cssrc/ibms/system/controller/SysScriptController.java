package com.cssrc.ibms.system.controller;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.SysScript;
import com.cssrc.ibms.system.service.SysScriptService;
 
/**
 * 系统脚本 控制器类
 * @author hxl
 *
 */
@Controller
@RequestMapping("/oa/system/sysScript/")
@Action(ownermodel=SysAuditModelType.SYSTEM_SETTING)
public class SysScriptController extends BaseController
{
	@Resource
	private SysScriptService sysScriptService;
	
	/**
	 * 添加或更新自定义代码模版。
	 * @param request
	 * @param response
	 * @param sysScript 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新自定义代码模版",
			execOrder=ActionExecOrder.AFTER,
			detail="<#if isAdd>添加新<#else>更新</#if>自定义系统脚本："  
	)
	public void save(HttpServletRequest request, HttpServletResponse response,SysScript sysScript) throws Exception
	{
		String resultMsg=null;		
		try{
			boolean isadd=true;
			if(sysScript.getId()==null){
				Long id=UniqueIdUtil.genId();
				sysScript.setId(id);
				sysScriptService.add(sysScript);
				resultMsg="添加系统脚本成功";
			}else{
				sysScriptService.update(sysScript);
				resultMsg="更新系统脚本成功";
				isadd=false;
			}
			LogThreadLocalHolder.putParamerter("isAdd", isadd);
			LogThreadLocalHolder.putParamerter("codetemId", sysScript.getId().toString());
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	
	/**
	 * 取得自定义系统脚本分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看自定义系统脚本分页列表",detail="查看自定义系统脚本分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List categoryList = this.sysScriptService.getDistinctCategory();
		List<SysScript> list=sysScriptService.getAll(new QueryFilter(request,"sysScriptItem"));
		ModelAndView mv=this.getAutoView().addObject("sysScriptList",list);
		mv.addObject("categoryList", categoryList);
		return mv;
	}
	
	/**
	 * 删除自定义系统脚本
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除自定义系统脚本",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除自定义系统脚本" +
					"<#list StringUtils.split(id,\",\") as item>" +
					"<#assign entity=SysScriptService.getById(Long.valueOf(item))/>" +
					"【${entity.name}】"+
				"</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			sysScriptService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, getText("controller.del.success") + ":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑自定义系统脚本
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="添加或编辑自定义系统脚本",
	detail="<#if isAdd>添加自定义系统脚本<#else>" +
			"编辑自定义系统脚本" +
			"<#assign entity=SysScriptService.getById(Long.valueOf(id))/>" +
			"【entity.templateName】" +
			"</#if>")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		List<String> categoryList = this.sysScriptService.getDistinctCategory();
		String returnUrl=RequestUtil.getPrePage(request);
		SysScript sysScript=sysScriptService.getById(id);
		LogThreadLocalHolder.putParamerter("isAdd", sysScript==null);
		return getAutoView().addObject("sysScript",sysScript)
				.addObject("categoryList", categoryList)
				.addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得自定义系统脚本明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看自定义系统脚本明细",detail="查看自定义系统脚本明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		long canReturn=RequestUtil.getLong(request, "canReturn",0);
		SysScript sysScript = sysScriptService.getById(id);	
		return getAutoView().addObject("sysScript", sysScript).addObject("canReturn",canReturn);
	}
	
	/**
	 * 取得初始化脚本信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("init")
	@Action(description="初始化脚本",detail="初始化脚本")
	public void init(HttpServletRequest request,HttpServletResponse response)throws Exception
	{	
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			sysScriptService.initAllTemplate();
			message=new ResultMessage(ResultMessage.Success, "脚本初始化成功");
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, "脚本初始化失败" + ":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	@RequestMapping("getScripts")
	@ResponseBody
	public List<SysScript> getScripts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<SysScript> list=sysScriptService.getAll();		
		return list;
	}
	        @RequestMapping({"export"})
	        @Action(description="导出脚本", execOrder=ActionExecOrder.AFTER, detail="导出脚本:【${SysAuditLinkService.getScriptLink(Long.valueOf(id))}】")
	        public void export(HttpServletRequest request, HttpServletResponse response)
	          throws Exception
	        {
	       Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
	       if (BeanUtils.isNotEmpty(lAryId)) {
	         Calendar now = Calendar.getInstance();
	         String localTime = now.get(1) + "-" + now.get(2) + "-" + now.get(5);
	         String strXml = this.sysScriptService.exportXml(lAryId);
	         response.setContentType("application/octet-stream");
	         response.setHeader("Content-Disposition", "attachment;filename=Script" + localTime + ".xml");
	         response.getWriter().write(strXml);
	         response.getWriter().flush();
	         response.getWriter().close();
	          }
	        }
	      
	        @RequestMapping({"importXml"})
	        @Action(description="导入自定义表", execOrder=ActionExecOrder.AFTER, detail="导入自定义表")
	        public void importXml(MultipartHttpServletRequest request, HttpServletResponse response)
	          throws Exception
	        {
	       MultipartFile fileLoad = request.getFile("xmlFile");
	       this.sysScriptService.importXml(fileLoad.getInputStream());
	       ResultMessage message = null;
	       message = new ResultMessage(1, "导入成功!");
	       writeResultMessage(response.getWriter(), message);
	        }
}
