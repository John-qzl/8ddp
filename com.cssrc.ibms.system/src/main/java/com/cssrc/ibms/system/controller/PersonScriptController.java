package com.cssrc.ibms.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.engine.IScript;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.PersonScript;
import com.cssrc.ibms.system.service.PersonScriptService;
/**
 *<pre>
 * 对象功能:系统人员脚本 控制器类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Controller
@RequestMapping("/oa/system/personScript/")
@Action(ownermodel=SysAuditModelType.SYSTEM_SETTING)
public class PersonScriptController extends BaseController
{
	@Resource
	private PersonScriptService personScriptService;
	
	
	/**
	 * 添加或更新系统人员脚本。
	 * @param request
	 * @param response
	 * @param personScript 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新系统人员脚本",
			detail="<#if isAdd>添加<#else>更新</#if>" +
					"系统人员脚本【${personScript.methodDesc}】"
	)
	public void save(HttpServletRequest request, HttpServletResponse response,PersonScript personScript) throws Exception
	{
		//添加系统日志信息 -B
		try {
			LogThreadLocalHolder.putParamerter("personScript", personScript);
			LogThreadLocalHolder.putParamerter("isAdd", personScript.getId()==null||personScript.getId()==0);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		//添加系统日志信息 -E
		String resultMsg=null;		
		try{
			if(personScript.getId()==null||personScript.getId()==0){
				personScript.setId(UniqueIdUtil.genId());
				personScriptService.add(personScript);
				resultMsg=getText("record.added",getText("controller.personScript"));
			}else{
			    personScriptService.update(personScript);
				resultMsg=getText("record.updated",getText("controller.personScript"));
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得 PersonScript 实体 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    protected PersonScript getFormObject(HttpServletRequest request) throws Exception {
    
    	JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
    
		String json=RequestUtil.getString(request, "json");
		JSONObject obj = JSONObject.fromObject(json);
		
		PersonScript personScript = (PersonScript)JSONObject.toBean(obj, PersonScript.class);
		
		return personScript;
    }
	
	/**
	 * 取得系统人员脚本分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看系统人员脚本分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<PersonScript> list=personScriptService.getAll(new QueryFilter(request,"personScriptItem"));
		ModelAndView mv=this.getAutoView().addObject("personScriptList",list);
		
		return mv;
	}
	
	/**
	 * 删除系统人员脚本
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除系统人员脚本",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除系统人员脚本" +
					"<#list StringUtils.split(id,\",\") as item>" +
						"<#assign entity=personScriptService.getById(Long.valueOf(item))/>" +
						"【${entity.methodDesc}】"+
					"</#list>"
	)
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			personScriptService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, getText("record.deleted",getText("controller.personScript")));
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, getText("controller.del.fail")+":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑系统人员脚本
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑系统人员脚本")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		//获取IScript的实现类
		List<IScript> implClasses = AppUtil.getAllImplByBeanType(IScript.class);
		Long id=RequestUtil.getLong(request,"id");
		String returnUrl=RequestUtil.getPrePage(request);
		PersonScript personScript=personScriptService.getById(id);
		
		return getAutoView().addObject("personScript",personScript)
							.addObject("implClasses",implClasses)
							.addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得系统人员脚本明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看系统人员脚本明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"id");
		PersonScript personScript = personScriptService.getById(id);	
		return getAutoView().addObject("personScript", personScript);
	}
	
	
	/**
	 * 取得系统人员脚本明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getJson")
	@Action(description="取系统人员脚本")
	@ResponseBody
	public Map<String,Object> getJson(HttpServletRequest request, HttpServletResponse response)
	{	
		Map<String,Object> map = new HashMap<String, Object>();
		
		int status = 0;
		try{
		long id=RequestUtil.getLong(request,"id");
			PersonScript personScript = personScriptService.getById(id);
			map.put("personScript", personScript);
		}catch (Exception e) {
			status=-1;
			e.printStackTrace();
		}
		map.put("status",status);
		return map;
	}
	
	
	/**
	 * 通过类名获取类的所有方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getMethodsByName")
	@ResponseBody	
	public String getMethodsByName(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String name = RequestUtil.getString(request, "name");
		JSONObject jobject = new JSONObject(); 		
		try{
			JSONArray jarray = personScriptService.getMethodsByName(name);
			jobject.accumulate("result", true).accumulate("methods", jarray);
		}
		catch(Exception ex){
			jobject.accumulate("result", false).accumulate("message", ex.getMessage());
		}
		return jobject.toString();
	}
	
	/**
	 * 验证脚本是否有效
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("validScriptIsEnable")
	@ResponseBody
	public String validScriptIsEnable(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject jobject = new JSONObject(); 		
		try{
			String message = personScriptService.validScriptIsEnable();
			jobject.accumulate("result", true).accumulate("message", message);
		}
		catch(Exception ex){
			jobject.accumulate("result", false).accumulate("message", ex.getMessage());
		}
		return jobject.toString();
	}
	
	/**
	 * 获取所有 有效的人员脚本
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getPersonScript")
	@ResponseBody
	public String getPersonScript(HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<PersonScript> list = personScriptService.getPersonScript();
		JSONArray jarray = JSONArray.fromObject(list);
		return jarray.toString();
	}
	
	/**
	 * 与dialog页面一起使用。 获取所有 有效的人员脚本，供选择。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("selector")
	@Action(description="选择器")
	public ModelAndView selector(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<PersonScript> personScriptList=personScriptService.getAll(new QueryFilter(request,"personScriptItem"));
		ModelAndView mv=this.getAutoView().addObject("personScriptList",personScriptList);
		
		return mv;
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addDialog")
	@Action(description="添加对话框")
	public ModelAndView addDialog(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		return this.getAutoView();
	}
}
