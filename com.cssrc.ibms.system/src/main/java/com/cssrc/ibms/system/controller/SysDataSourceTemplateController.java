package com.cssrc.ibms.system.controller;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JSONObjectUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.SysDataSourceTemplate;
import com.cssrc.ibms.system.service.SysDataSourceTemplateService;

/**
 * SysDataSourceTemplateController
 * @author liubo
 * @date 2017年4月14日
 */
@Controller
@RequestMapping({"/oa/system/sysDataSourceTemplate/"})
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class SysDataSourceTemplateController extends BaseController{

	@Resource
	private SysDataSourceTemplateService sysDataSourceTemplateService;
	
	@RequestMapping({"save"})
	@Action(description="添加或更新SYS_DATA_SOURCE_TEMPLATE")
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String json = FileUtil.inputStream2String(request.getInputStream());
		SysDataSourceTemplate sysDataSourceTemplate = 
				(SysDataSourceTemplate)JSONObjectUtil.toBean(json, SysDataSourceTemplate.class);
		try {
			if(sysDataSourceTemplate.getId() == null){
				sysDataSourceTemplate.setId(UniqueIdUtil.genId());
				sysDataSourceTemplateService.add(sysDataSourceTemplate);
				writeResultMessage(response.getWriter(), "添加成功", 1);
			}else{
				sysDataSourceTemplateService.update(sysDataSourceTemplate);
				writeResultMessage(response.getWriter(), "更新成功", 1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			writeResultMessage(response.getWriter(), e.getMessage(), 0);
		}
		
	}
	
	@RequestMapping({"list"})
	@Action(description="查看SYS_DATA_SOURCE_TEMPLATE分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception{
		ModelAndView mv = getAutoView();
		List list = this.sysDataSourceTemplateService.getAll(new QueryFilter(request, "sysDataSourceTemplateItem"));
		//List<SysDataSourceTemplate> sysDataSourceTemplateList = sysDataSourceTemplateService.getAll();
		mv.addObject("sysDataSourceTemplateList", list);
		return mv;
	}
	
	@RequestMapping({"del"})
	@Action(description="删除SYS_DATA_SOURCE_TEMPLATE")
	public void del(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysDataSourceTemplateService.delByIds(lAryId);
			message = new ResultMessage(1, "删除SYS_DATA_SOURCE_DEF成功!");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除失败" + e.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	@RequestMapping({"edit"})
	@Action(description="编辑SYS_DATA_SOURCE_TEMPLATE")
	public ModelAndView edit(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String returnUrl = RequestUtil.getPrePage(request);
		return getAutoView().addObject("returnUrl", returnUrl);
	}
	
	@RequestMapping({"get"})
	@Action(description="查看SYS_DATA_SOURCE_TEMPLATE明细")
	public ModelAndView get(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysDataSourceTemplate sysDataSourceTemplate = (SysDataSourceTemplate)this.sysDataSourceTemplateService.getById(id);
		return getAutoView().addObject("sysDataSourceTemplate", sysDataSourceTemplate);
	}	
	
	@RequestMapping({"getSetterFields"})
	@ResponseBody
	public JSONArray getSetterFields(HttpServletRequest request, HttpServletResponse response){
		String classPath = RequestUtil.getString(request, "classPath");
		return this.sysDataSourceTemplateService.getHasSetterFieldsJsonArray(classPath);
	}
	
	@RequestMapping({"getById"})
	@Action(description="通过模板id获取数据源模板")
	@ResponseBody
	public SysDataSourceTemplate getById(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysDataSourceTemplate sysDataSourceTemplate = (SysDataSourceTemplate)this.sysDataSourceTemplateService.getById(id);
		return sysDataSourceTemplate;
	} 
	
	@RequestMapping({"getAll"})
	@Action(description="获取所有的模板")
	@ResponseBody
	public List<SysDataSourceTemplate> getAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List sysDataSourceTemplateList = this.sysDataSourceTemplateService.getAll();
		return sysDataSourceTemplateList;
	}
	
	@RequestMapping({"detail"})
	@Action(description="表单明细")
	public ModelAndView detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		SysDataSourceTemplate sysDataSourceTemplate = (SysDataSourceTemplate)this.sysDataSourceTemplateService.getById(Long.valueOf(id));
		return getAutoView().addObject("sysDataSourceTemplate", sysDataSourceTemplate);
	}
	
	@RequestMapping({"modify"})
	public ModelAndView modify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		SysDataSourceTemplate sysDataSourceTemplate = (SysDataSourceTemplate)this.sysDataSourceTemplateService.getById(Long.valueOf(id));
		return getAutoView().addObject("sysDataSourceTemplate", sysDataSourceTemplate);
	}
}
