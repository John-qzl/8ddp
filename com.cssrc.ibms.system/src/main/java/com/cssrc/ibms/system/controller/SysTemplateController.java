package com.cssrc.ibms.system.controller;

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
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.system.model.SysTemplate;
import com.cssrc.ibms.system.service.SysTemplateService;
 
/**
 * 系统脚本 控制器类
 * @author hxl
 *
 */
@Controller
@RequestMapping("/oa/system/sysTemplate/")
@Action(ownermodel=SysAuditModelType.SYSTEM_SETTING)
public class SysTemplateController extends BaseController
{
	@Resource
	private SysTemplateService sysTemplateService;
	
	/**
	 * 添加或更新自定义代码模版。
	 * @param request
	 * @param response
	 * @param sysTemplate 添加或更新的实体
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
	public void save(HttpServletRequest request, HttpServletResponse response,SysTemplate sysTemplate) throws Exception
	{
		String resultMsg=null;		
		try{
			boolean isadd=true;
			if(sysTemplate.getId()==null){
				Long id=UniqueIdUtil.genId();
				sysTemplate.setId(id);
				sysTemplateService.add(sysTemplate);
				resultMsg="添加系统脚本成功";
			}else{
				sysTemplateService.update(sysTemplate);
				resultMsg="更新系统脚本成功";
				isadd=false;
			}
			LogThreadLocalHolder.putParamerter("isAdd", isadd);
			LogThreadLocalHolder.putParamerter("codetemId", sysTemplate.getId().toString());
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
		List<SysTemplate> list=sysTemplateService.getAll(new QueryFilter(request,"sysTemplateItem"));
		ModelAndView mv=this.getAutoView().addObject("sysTemplateList",list);
		return mv;
	}
	
	@RequestMapping({"dialog"})
	@Action(description="查看模版管理分页列表", detail="查看模版管理分页列表")
	public ModelAndView dialog(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		List list = this.sysTemplateService.getAll(new QueryFilter(request, "sysTemplateItem"));
		ModelAndView mv = getAutoView().addObject("sysTemplateList", list);
		return mv;
	}
	/**
	 * 删除自定义系统脚本
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除模版管理",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除短信邮件模版：" +
					"<#list StringUtils.split(id,\",\") as item>" +
					"<#assign entity=SysTemplateService.getById(Long.valueOf(item))/>" +
					"【${entity.name}】"+
				"</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			sysTemplateService.delByIds(lAryId);
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
	@Action(description="添加或编辑模版管理", exectype=SysAuditExecType.UPDATE_TYPE, 
	detail="<#if isAdd>添加新模板<#else>编译模板:" + 
			"<#assign entity=SysTemplateService.getById(Long.valueOf(id))/>" +
			"【entity.templateName】" +
			"</#if>")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		String returnUrl=RequestUtil.getPrePage(request);
		SysTemplate sysTemplate=sysTemplateService.getById(id);
		LogThreadLocalHolder.putParamerter("isAdd", sysTemplate==null);
		return getAutoView().addObject("sysTemplate",sysTemplate).addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得自定义系统脚本明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看模版管理明细", exectype=SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		long canReturn=RequestUtil.getLong(request, "canReturn",0);
		SysTemplate sysTemplate = sysTemplateService.getById(id);	
		return getAutoView().addObject("sysTemplate", sysTemplate).addObject("canReturn",canReturn);
	}
	
	
	@RequestMapping("getScripts")
	@ResponseBody
	public List<SysTemplate> getScripts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<SysTemplate> list=sysTemplateService.getAll();		
		return list;
	}
	@RequestMapping({"setDefault"})
	@Action(description="设置每种模板类型的默认模板", detail="设置模板【${sysTemplateService.getById(Long.valueOf(templateId)).name}】为默认模板")
	public void setDefault(HttpServletRequest request, HttpServletResponse response)
	  throws Exception
	{
	     String preUrl = RequestUtil.getPrePage(request);
	     ResultMessage message = null;
	     Long templateId = Long.valueOf(RequestUtil.getLong(request, "id"));
	  try {
	       this.sysTemplateService.setDefault(templateId);
	       message = new ResultMessage(1, "设置成功!");
	  } catch (Exception ex) {
	       message = new ResultMessage(0, "设置失败:" + ex.getMessage());
	  }
	     addMessage(message, request);
	     response.sendRedirect(preUrl);
	}
	 
	@RequestMapping({"exportXml"})
	@Action(description="导出系统模板")
	public void exportXml(HttpServletRequest request, HttpServletResponse response)
	  throws Exception
	{
	     Long[] ids = RequestUtil.getLongAryByStr(request, "ids");
	 
	     if (BeanUtils.isNotEmpty(ids)) {
	       String fileName = "SysTemplate_" + DateFormatUtil.getNowByString("yyyyMMddHHmmss") + ".xml";
	      String strXml = this.sysTemplateService.exportXml(ids);
	 
	      FileUtil.downLoad(request, response, strXml, fileName);
	  }
	}
	 
	@RequestMapping({"importXml"})
	@Action(description="导入系统模板")
	public void importXml(MultipartHttpServletRequest request, HttpServletResponse response)
	  throws Exception
	{
	  MultipartFile fileLoad = request.getFile("xmlFile");
	 boolean clearAll = RequestUtil.getBoolean(request, "clearAll", false);
	  boolean setDefault = RequestUtil.getBoolean(request, "setDefault", false);
	   ResultMessage message = null;
	  try {
	     List<SysTemplate> sysTemplates = this.sysTemplateService.unmarshal(fileLoad.getInputStream());
	     for (SysTemplate sysTemplate : sysTemplates) {
	    	 sysTemplate.setId(Long.valueOf(UniqueIdUtil.genId()));
	     }
	 
	      if (clearAll) {
	         for (SysTemplate sysTemplate : sysTemplates) {
	           this.sysTemplateService.delByUseType(sysTemplate.getUseType());
	      }
	    }
	 
	       for (SysTemplate sysTemplate : sysTemplates) {
	        if ((sysTemplate.getIsDefault() != null) && (SysTemplate.IS_DEFAULT_YES.intValue() == sysTemplate.getIsDefault().intValue())) {
	          SysTemplate temp = this.sysTemplateService.getDefaultByUseType(sysTemplate.getUseType());
	           if (temp != null) {
	             sysTemplate.setIsDefault(SysTemplate.IS_DEFAULT_NO);
	        }
	      }
	       this.sysTemplateService.add(sysTemplate);
	         MsgUtil.addMsg(1, "模板：【" + sysTemplate.getName() + "】成功导入！");
	    }
	 
	      if (setDefault) {
	        for (SysTemplate sysTemplate : sysTemplates) {
	         this.sysTemplateService.setDefault(sysTemplate.getId());
	      }
	    }
	      message = new ResultMessage(1, MsgUtil.getMessage());
	  } catch (Exception e) {
	       e.printStackTrace();
	      e.getStackTrace();
	     message = new ResultMessage(0, "导入出错了，请检查导入格式是否正确或者导入的数据是否有问题！");
	  }
	   writeResultMessage(response.getWriter(), message);
	}
}
