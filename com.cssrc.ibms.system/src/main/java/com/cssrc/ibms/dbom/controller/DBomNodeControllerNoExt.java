/*package com.cssrc.ibms.dbom.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dbom.service.DBomNodeService;

*//**
 * 对象功能:DBom节点管理 控制器类.
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-7-14 上午08:05:04 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-7-14 上午08:05:04 <br/> 
 * @see
 *//*
@Controller
@RequestMapping("/oa/system/dbomNode/")
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class DBomNodeControllerNoExt extends BaseController{
	@Resource
	private DBomNodeService dbomNodeService;
	@Resource
	private IDataTemplateService dataTemplateService;
	
	
	*//**
	 * dbom node 编辑页面
	 * @param request
	 * @param response
	 * @return
	 *//*
	@RequestMapping("edit")
	public ModelAndView manage(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("oa/dbom/dbomNodeEdit.jsp");
		Long cnodeId = RequestUtil.getLong(request, "cnodeId");
		JSONObject object = dbomNodeService.get(cnodeId);
		return mv.addObject("dbomNode", object.get("data"));
	}
	*//**
	 * dbom node data source
	 * @param request
	 * @param response
	 * @return
	 *//*
	@RequestMapping("dataSource")
	public ModelAndView dataSource(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ModelAndView mv=new ModelAndView("oa/dbom/dbomNodeDataSource.jsp");
			QueryFilter filter = new QueryFilter(request,"dataItem");
			String modelType = RequestUtil.getString(request, "Q_modelType_SN");
			if(!"数据视图".equals(modelType)){
				filter.addFilterForIB("isPublished", "1");
				filter.addFilterForIB("isMain", "1");
				filter.addFilterForIB("tableName", "%" + RequestUtil.getString(request, "Q_tableName_SL") + "%");
				filter.addFilterForIB("tableDesc", "%" + RequestUtil.getString(request, "Q_tableName_SL") + "%");
				filter.addFilterForIB("orderField", "UPPER(tableName)");
				filter.addFilterForIB("orderSeq", "ASC");
			}else{
				filter.addFilterForIB("alias", "%" + RequestUtil.getString(request, "Q_tableName_SL") + "%");
				filter.addFilterForIB("name", "%" + RequestUtil.getString(request, "Q_tableName_SL") + "%");
				filter.addFilterForIB("orderField", "UPPER(alias)");
				filter.addFilterForIB("orderSeq", "ASC");
			}
			String mainTableName = RequestUtil.getString(request, "mainTableName");
			String relationType = RequestUtil.getString(request, "Q_relationType_SN");
			Map<String,Object> dataMap = dbomNodeService.getDataSource(filter, mainTableName, relationType, modelType);
			mv.addObject("dataList",dataMap.get("result"));
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	*//**
	 * 获取动态子节点
	 * @param request
	 * @param response
	 *//*
	@RequestMapping("dynamicNode")
	@Action(description="获取动态子节点")
	public ModelAndView dynamicNode(HttpServletRequest request, HttpServletResponse response){
		try {
			ModelAndView mv=new ModelAndView("oa/dbom/dynamicNode.jsp");
			QueryFilter filter = new QueryFilter(request,"dataItem");
			Map<String,Object> dataMap = dbomNodeService.getDynamicNode(request, filter);
			dataMap.get("result");
			mv.addObject("dataList",dataMap.get("result"));
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	*//**
	 * 业务数据模板
	 * @param request
	 * @param response
	 *//*
	@RequestMapping("formDefList")
	public ModelAndView getFormDefList(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mv=new ModelAndView("oa/dbom/dynamicNode.jsp");
		try {
			QueryFilter filter = new QueryFilter(request,"dataItem");
			String dataSource = RequestUtil.getString(request, "tableName");
			if(dataSource.startsWith("W_")){
				filter.addFilterForIB("isPublished", "1");
				filter.addFilterForIB("subject", "%" + RequestUtil.getString(request,"Q_subject_SL") + "%");
				filter.addFilterForIB("tableName", "%" + RequestUtil.getString(request,"Q_tableName_SL") + "%");
				filter.addFilterForIB("orderField", "UPPER(tableName)");
				filter.addFilterForIB("orderSeq", "ASC");
			}else{
				filter.addFilterForIB("name", "%" + RequestUtil.getString(request,"Q_subject_SL") + "%");
				filter.addFilterForIB("alias", "%" + RequestUtil.getString(request,"Q_tableName_SL") + "%");
				filter.addFilterForIB("orderField", "UPPER(alias)");
				filter.addFilterForIB("orderSeq", "ASC");
			}
			Map<String,Object> dataMap = dbomNodeService.getFormDefList(filter, dataSource);
			mv.addObject("dataList",dataMap.get("result"));
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
*/