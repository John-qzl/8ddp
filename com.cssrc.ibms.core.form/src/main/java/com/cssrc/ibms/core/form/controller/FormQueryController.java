package com.cssrc.ibms.core.form.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.system.intf.ISysDataSourceDefService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceService;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.system.model.ISysDataSourceDef;
import com.cssrc.ibms.core.db.datasource.DbContextHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.form.model.FormQuery;
import com.cssrc.ibms.core.form.model.QueryResult;
import com.cssrc.ibms.core.form.service.FormQueryService;
import com.cssrc.ibms.core.table.BaseTableMeta;
import com.cssrc.ibms.core.table.IDbView;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.table.impl.TableMetaFactory;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;

/**
 * 对象功能:通用表单查询 控制器类
 * 开发人员:zhulongchao
 */
@Controller
@RequestMapping("/oa/form/formQuery/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class FormQueryController extends BaseController
{
	@Resource
	private FormQueryService bpmFormQueryService;
	
	@Resource
	private ISysDataSourceService sysDataSourceService;
	
	@Resource
	private ISysDataSourceDefService sysDataSourceDefService;
	
	/**
	 * 取得通用表单查询分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看通用表单查询分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<FormQuery> list=bpmFormQueryService.getAll(new QueryFilter(request,"bpmFormQueryItem"));
		ModelAndView mv=this.getAutoView().addObject("bpmFormQueryList",list);
		
		return mv;
	}
	
	/**
	 * 通用表单查询对话框
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("dialog")
	@Action(description="通用表单查询对话框")
	public ModelAndView dialog(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<FormQuery> list=bpmFormQueryService.getAll(new QueryFilter(request,"bpmFormQueryItem"));
		ModelAndView mv=this.getAutoView().addObject("bpmFormQueryList",list);
		
		return mv;
	}
	
	/**
	 * 删除通用表单查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除通用表单自定义查询",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除自定义查询" +
			   "<#list StringUtils.split(id,\",\") as item>" +
				   " <#assign enity=bpmFormQueryService.getById(Long.valueOf(item)) />" +
				   " 【${enity.name}】"+
			   "</#list>"
	)
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage resultMessage=null;
		try{
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			bpmFormQueryService.delByIds(lAryId);
			resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
		}catch(Exception ex){
			resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.del.ail")+":"+ex.getMessage() );
		}
		addMessage(resultMessage, request);
		response.sendRedirect(preUrl);
	}
	
	@RequestMapping("queryObj")
	@Action(description = "查看通用表单查询明细")
	@ResponseBody
	public Map<String, Object> queryObj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String alias = RequestUtil.getString(request, "alias");
		Map<String, Object> map = new HashMap<String, Object>();
		FormQuery bpmFormQuery = bpmFormQueryService.getByAlias(alias);
		if (bpmFormQuery != null) {
			map.put("bpmFormQuery", bpmFormQuery);
			map.put("success", 1);
		} else {
			map.put("success", 0);
		}
		return map;
	}
	
	/**
	 * 显示查询条件构建界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String alias = RequestUtil.getString(request, "query_alias_");
		
		FormQuery bpmFormQuery = bpmFormQueryService.getByAlias(alias);
		ModelAndView mv = this.getAutoView();
		mv.addObject("bpmFormQuery", bpmFormQuery);
		return mv;
	}
	
	/**
	 * 进行查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("doQuery")
	@Action(description = "进行查询")
	@ResponseBody
	public QueryResult doQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {
		QueryResult result=new QueryResult();
		String alias = RequestUtil.getString(request, "alias");
		String queryData = RequestUtil.getString(request, "querydata");
		Integer page = RequestUtil.getInt(request, "page", 0);
		Integer pageSize = RequestUtil.getInt(request, "pagesize", 0);
		
		FormQuery bpmFormQuery = bpmFormQueryService.getByAlias(alias);
		
		if(bpmFormQuery != null){
			result = bpmFormQueryService.getData(bpmFormQuery, queryData, page, pageSize);
		}
		else
			result.setErrors(getText("controller.bpmFormQuery.doQuery.notData"));
		 DbContextHolder.clearDataSource();
		 DbContextHolder.setDefaultDataSource();
		return result;
	}
	
	/**
	 * 	编辑通用表单查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑通用表单查询")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		Long canReturn=RequestUtil.getLong(request,"canReturn",0);
		String returnUrl=RequestUtil.getPrePage(request);
		
		FormQuery bpmFormQuery = null;
		if (id != 0) {
			bpmFormQuery = bpmFormQueryService.getById(id);
		} else {
			bpmFormQuery = new FormQuery();
		}
		List<?extends ISysDataSourceDef> dsList = sysDataSourceDefService.getAllAndDefault();
		
		return getAutoView().addObject("bpmFormQuery",bpmFormQuery)
							.addObject("returnUrl", returnUrl)
							.addObject("dsList", dsList)
							.addObject("canReturn", canReturn);
	}
	
	/**
	 * 设置字段
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("setting")
	public ModelAndView setting(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		String dsName = "";
		String objectName = "";
		int istable = 0;
		ModelAndView mv = this.getAutoView();
		if (id == 0) {
			dsName = RequestUtil.getString(request, "dsName");
			istable = RequestUtil.getInt(request, "istable");
			objectName = RequestUtil.getString(request, "objectName");
		} else {
			FormQuery bpmFormQuery = bpmFormQueryService.getById(id);
			istable = bpmFormQuery.getIsTable();
			dsName = bpmFormQuery.getDsalias();
			objectName = bpmFormQuery.getObjName();
			mv.addObject("bpmFormQuery", bpmFormQuery);
		}

		TableModel tableModel;
		// 表
		if (istable == 1) {
			BaseTableMeta meta = TableMetaFactory.getMetaData(dsName);
			tableModel = meta.getTableByName(objectName);
		}
		// 视图处理
		else {
			IDbView dbView = TableMetaFactory.getDbView(dsName);
			tableModel = dbView.getModelByViewName(objectName);
		}

		mv.addObject("tableModel", tableModel);

		return mv;
	}
	
	/**
	 * 取得所有的自定义查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAllQueries")
	@ResponseBody
	public List<FormQuery> getAllDialogs(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<FormQuery> list= bpmFormQueryService.getAll();		
		return list;
	}
	 @RequestMapping({"exportXml"})
	 /*     */   @Action(description="导出自定义查询", detail="导出自定义查询:<#list StringUtils.split(tableIds,\",\") as item><#assign entity=bpmFormTableService.getById(Long.valueOf(item))/>【${entity.tableDesc}(${entity.tableName})】</#list>")
	 /*     */   public void exportXml(HttpServletRequest request, HttpServletResponse response)
	 /*     */     throws Exception
	 /*     */   {
	 /* 309 */     String strXml = null;
	 /* 310 */     String fileName = null;
	 /* 311 */     Long[] tableIds = RequestUtil.getLongAryByStr(request, "tableIds");
	 /* 312 */     List list = this.bpmFormQueryService.getAll();
	 /*     */     try {
	 /* 314 */       if (BeanUtils.isEmpty(tableIds)) {
	 /* 315 */         if (BeanUtils.isNotEmpty(list)) {
	 /* 316 */           strXml = this.bpmFormQueryService.exportXml(list);
	 /* 317 */           fileName = "全部自定义查询记录_" + 
	 /* 318 */             DateFormatUtil.getNowByString("yyyyMMddHHmmdd") + 
	 /* 319 */             ".xml";
	 /*     */         }
	 /*     */       } else {
	 /* 322 */         strXml = this.bpmFormQueryService.exportXml(tableIds);
	 /* 323 */         fileName = DateFormatUtil.getNowByString("yyyyMMddHHmmdd") + 
	 /* 324 */           ".xml";
	 /* 325 */         if (tableIds.length == 1) {
	 /* 326 */           FormQuery bpmFormQuery = 
	 /* 327 */             (FormQuery)this.bpmFormQueryService
	 /* 327 */             .getById(tableIds[0]);
	 /* 328 */           fileName = bpmFormQuery.getName() + "_" + fileName;
	 /* 329 */         } else if (tableIds.length == list.size()) {
	 /* 330 */           fileName = "全部自定义查询记录_" + fileName;
	 /*     */         }
	 /*     */         else {
	 /* 333 */           fileName = "多条自定义查询记录_" + fileName;
	 /*     */         }
	 /*     */       }
	 /* 336 */       FileUtil.downLoad(request, response, strXml, fileName);
	 /*     */     } catch (Exception e) {
	 /* 338 */       e.printStackTrace();
	 /*     */     }
	 /*     */   }
	 /*     */ 
	 /*     */   @RequestMapping({"importXml"})
	 /*     */   @Action(description="导入自定义查询")
	 /*     */   public void importXml(MultipartHttpServletRequest request, HttpServletResponse response)
	 /*     */     throws Exception
	 /*     */   {
	 /* 352 */     MultipartFile fileLoad = request.getFile("xmlFile");
	 /* 353 */     ResultMessage message = null;
	 /*     */     try {
	 /* 355 */       this.bpmFormQueryService.importXml(fileLoad.getInputStream());
	 /* 356 */       message = new ResultMessage(1, 
	 /* 357 */         MsgUtil.getMessage());
	 /*     */     } catch (Exception e) {
	 /* 359 */       e.printStackTrace();
	 /* 360 */       message = new ResultMessage(0, "导入文件异常，请检查文件格式！");
	 /*     */     }
	 /* 362 */     writeResultMessage(response.getWriter(), message);
	 /*     */   }
	 /*     */ }

