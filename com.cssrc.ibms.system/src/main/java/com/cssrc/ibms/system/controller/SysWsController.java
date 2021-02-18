package com.cssrc.ibms.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.soap.WebserviceHelper;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.model.SysWs;
import com.cssrc.ibms.system.model.SysWsParams;
import com.cssrc.ibms.system.service.SysWsService;
/**
 *<pre>
 * 对象功能:通用webservice调用设置 控制器类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Controller
@RequestMapping("/oa/system/sysWs/")
@Action(ownermodel=SysAuditModelType.SYSTEM_SETTING)
public class SysWsController extends BaseController
{
	@Resource
	private SysWsService sysWsService;
	
	
	/**
	 * 添加或更新通用webservice调用设置。
	 * @param request
	 * @param response
	 * @param bpmCommonWsSet 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject result = new JSONObject();
		try{
			SysWs bpmCommonWsSet = getFormObject(request);
			
			if(bpmCommonWsSet.getId()==null||bpmCommonWsSet.getId()==0){
				bpmCommonWsSet.setId(UniqueIdUtil.genId());
				sysWsService.addAll(bpmCommonWsSet);			
			}else{
			    sysWsService.updateAll(bpmCommonWsSet);
			}
			result.accumulate("result", ResultMessage.Success).accumulate("message", getText("controller.save.success"));
		}catch(Exception e){
			result.accumulate("result", ResultMessage.Fail).accumulate("message", e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * 取得 BpmCommonWsSet 实体 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings({ "deprecation", "unchecked" })
	protected SysWs getFormObject(HttpServletRequest request) throws Exception {
		String json = RequestUtil.getString(request, "setObj");
		String customParams = RequestUtil.getString(request, "customParams");
		
		if(StringUtil.isEmpty(json))return null;
		JSONObject jobject = JSONObject.fromObject(json);
		SysWs sysWs = (SysWs)JSONObject.toBean(jobject,SysWs.class);
		
		if(StringUtil.isNotEmpty(customParams)){
			JSONArray obj = JSONArray.fromObject(customParams);
			if(BeanUtils.isNotEmpty(obj)){
				List<SysWsParams> list = JSONArray.toList(obj,SysWsParams.class);
				sysWs.setSysWsParamsList(list);
			}
		}
		return sysWs;
    }
	
	/**
	 * 取得通用webservice调用设置分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看通用webservice调用设置分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<SysWs> list=sysWsService.getAll(new QueryFilter(request,"bpmCommonWsSetItem"));
		ModelAndView mv=this.getAutoView().addObject("bpmCommonWsSetList",list);
		
		return mv;
	}
	
	/**
	 * 删除通用webservice调用设置
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除通用webservice调用设置")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			sysWsService.delAll(lAryId);
			message=new ResultMessage(ResultMessage.Success,getText("record.deleted",getText("controller.webservice")));
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, getText("controller.del.fail")+":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑通用webservice调用设置
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑通用webservice调用设置")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id",0L);
		String returnUrl=RequestUtil.getPrePage(request);
		SysWs bpmCommonWsSet=sysWsService.getById(id);
		
		return getAutoView().addObject("bpmCommonWsSet",bpmCommonWsSet)
							.addObject("returnUrl",returnUrl);
	}

	/**
	 * webservice测试
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("test")
	@Action(description="查看通用webservice调用设置明细")
	public ModelAndView test(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"setId");
		SysWs bpmCommonWsSet = sysWsService.getById(id);	
		return getAutoView().addObject("bpmCommonWsSet",bpmCommonWsSet);
	}
	
	/**
	 * 获取webservice调用的 自定义参数
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getWsParams")
	@ResponseBody
	public String getWsParams(HttpServletRequest request, HttpServletResponse response) throws Exception{
		long setId = RequestUtil.getLong(request, "setId");
		List<SysWsParams> list = sysWsService.getWsParamsList(setId);
		JSONArray ary = JSONArray.fromObject(list);
		return ary.toString();
	}
	
	
	@RequestMapping("doExecute")
	@ResponseBody
	public String doExecute(HttpServletRequest request, HttpServletResponse response) throws Exception{
		long setId = RequestUtil.getLong(request, "setId");
		String json = RequestUtil.getString(request, "json");
		JSONObject jobject = new JSONObject();
		SysWs bpmCommonWsSet = sysWsService.getById(setId);
		if(bpmCommonWsSet==null){
			jobject.accumulate("result", ResultMessage.Fail)
				   .accumulate("message", getText("controller.bpmCommonWsSet.noGetWsSet"));
		}
		else{
			try{
				JSONArray jarray = JSONArray.fromObject(json);
				Map<String,Object> map = new HashMap<String, Object>();
				for(Object obj : jarray){
					JSONObject jObj = (JSONObject)obj;
					String bindingVal = jObj.getString("bindingVal");
					map.put(bindingVal, getTestVal(jObj));
				}
				String result = WebserviceHelper.executeXml(bpmCommonWsSet.getAlias(), map);
				jobject.accumulate("result", ResultMessage.Success)
				       .accumulate("message", result);
			}
			catch(Exception ex){
				ex.printStackTrace();
				jobject.accumulate("result", ResultMessage.Fail)
				   	   .accumulate("message", ex.getMessage());
			}
		}
		return jobject.toString();
	}
	
	@SuppressWarnings("deprecation")
	private Object getTestVal(JSONObject obj) throws Exception{
		Integer javaType = obj.getInt("javaType");
		Object testVal = obj.get("testVal");
		switch(javaType){
			//列表
			case 3:
				if(testVal instanceof JSONArray){
					List<?> list = JSONArray.toList((JSONArray)testVal,String.class);
					testVal = list;
				}
				break;
			//日期
			case 4:
				String[] formatter = new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","HH:mm:ss","yyyy-MM-dd HH:mm:00"};
				if(testVal instanceof String){
					testVal = DateUtils.parseDate(testVal.toString(), formatter);
				}
				break;
		}
		return testVal;
	}
}
