package com.cssrc.ibms.core.form.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.form.model.IDialogField;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormDialog;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceDefService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceService;
import com.cssrc.ibms.core.db.mybatis.page.PageUtils;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.form.model.DialogField;
import com.cssrc.ibms.core.form.model.FormDialog;
import com.cssrc.ibms.core.form.service.FormDialogService;
import com.cssrc.ibms.core.table.BaseTableMeta;
import com.cssrc.ibms.core.table.IDbView;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.table.impl.TableMetaFactory;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.redis.RedisClient;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * 对象功能:通用表单对话框 控制器类
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/form/formDialog/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class FormDialogController extends BaseController {
	protected Logger logger = LoggerFactory.getLogger(FormDialogController.class);
	@Resource
	private FormDialogService formDialogService;

	@Resource
	private ISysDataSourceService sysDataSourceService;
	
	@Resource
	private ISysDataSourceDefService sysDataSourceDefService;
	 
	private static String DEFAULT_ORDER_SEQ="DESC";

	/**
	 * 取得通用表单对话框分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = "查看通用表单对话框分页列表")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String returnUrl=RequestUtil.getUrl(request);
		returnUrl=returnUrl.replace("&", "@");
		List<FormDialog> list = formDialogService.getAll(new QueryFilter(request, "bpmFormDialogItem"));
		ModelAndView mv = this.getAutoView().addObject("bpmFormDialogList", list).addObject("returnUrl", returnUrl);

		
		return mv;
	}

	/**
	 * 删除通用表单对话框
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description = "删除通用表单对话框",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除通用表单对话框"+
					"<#list StringUtils.split(id,\",\") as item>" +
					"<#assign entity=bpmFormDialogService.getById(Long.valueOf(item))/>" +
					"【${entity.name}】" +
					"</#list>"
	)
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			formDialogService.delByIds(lAryId);
			message = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
		} catch (Exception ex) {
			message = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail")+":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping("edit")
	@Action(description = "编辑通用表单对话框")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = RequestUtil.getLong(request, "id");
		String returnUrl=request.getParameter("returnUrl");
		if(returnUrl!=null){
			returnUrl=returnUrl.replace("@", "&");
			}
		String preUrl = request.getHeader("Referer");
		FormDialog bpmFormDialog = null;
		if (id != 0) {
			bpmFormDialog = formDialogService.getById(id);
		} else {
			bpmFormDialog = new FormDialog();
		}
		List sysDataSourceList = this.sysDataSourceDefService.getAllAndDefault();

		return getAutoView().addObject("bpmFormDialog", bpmFormDialog)
							.addObject("returnUrl", returnUrl)
							.addObject("preUrl", preUrl)
							.addObject("dsList", sysDataSourceList);
	}

	/**
	 * 取得通用表单对话框明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description = "查看通用表单对话框明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		long canReturn=RequestUtil.getLong(request, "canReturn",0);
		FormDialog bpmFormDialog = formDialogService.getById(id);
		return getAutoView().addObject("bpmFormDialog", bpmFormDialog).addObject("canReturn", canReturn);
	}

	
	
	@RequestMapping("dialogObj")
	@Action(description = "查看通用表单对话框明细")
	@ResponseBody
	public Map<String, Object> dialogObj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String alias = RequestUtil.getString(request, "alias");
		Map<String, Object> map = new HashMap<String, Object>();
		
		FormDialog bpmFormDialog = null;
		try{
		//判断是否有rpc远程接口
		String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
		if(StringUtil.isNotEmpty(rpcrefname)){
			//Class<?>[] dd =Class.forName(AppUtil.getContext().getBean(rpcrefname).getClass().getName()).getInterfaces();
			//Class<?> FF = dd[2];
			//采用IOC方式，根据rpc远程接口调用数据
			CommonService commonService = (CommonService) AppUtil.getBean(rpcrefname);
			bpmFormDialog = (FormDialog)commonService.getByFormDialogAlias(alias);
		}else{
			//if(bpmFormDialog == null){
			bpmFormDialog = formDialogService.getByAlias(alias);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		//当不是rpc远程接口  或者  远程调用超时失败，从本地调用
		
				
		if (bpmFormDialog != null) { 		 
			map.put("bpmFormDialog", bpmFormDialog);
			map.put("success", 1);
		} else {
			map.put("success", 0);
		} 
		return map;
	}

	/**
	 * 根据数据源，输入的对象类型，对象名称获取对象列表。
	 * 
	 * <pre>
	 *  1.对象类型为表。
	 *  	返回表的map对象。
	 *  2.对象为视图
	 *  	返回视图列表对象。
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("getByDsObjectName")
	@Action(description = "根据对象名称对象类型")
	@ResponseBody
	public Map getByDsObjectName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String dsName = RequestUtil.getString(request, "dsName");
		String objectName = RequestUtil.getString(request, "objectName");
		int istable = RequestUtil.getInt(request, "istable");
		Map map = new HashMap();
		try {
			if (istable == 1) {
				BaseTableMeta meta = TableMetaFactory.getMetaData(dsName);
				Map<String, String> tableMap = meta.getTablesByName(objectName);
				map.put("tables", tableMap);
			} else {
				IDbView dbView = TableMetaFactory.getDbView(dsName);
				List<String> views = dbView.getViews(objectName);
				map.put("views", views);
			}
			map.put("success", "true");
		} catch (Exception ex) {
			logger.info("getByDsObjectName:" + ex.getMessage());
			map.put("success", "false");
		}
		return map;
	}

	/**
	 * 取得表或者视图的元数据对象。
	 * 
	 * <pre>
	 * 	根据数据源，对象名称，是否视图获取表或者视图的元数据对象。
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("getObjectByDsObjectName")
	@Action(description = "取得表或者视图的元数据对象")
	@ResponseBody
	public Map getObjectByDsObjectName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String dsName = RequestUtil.getString(request, "dsName");
		String objectName = RequestUtil.getString(request, "objectName");
		int istable = RequestUtil.getInt(request, "istable");
		Map map = new HashMap();
		TableModel tableModel;
		try {
			// 加载表
			if (istable == 1) {
				BaseTableMeta meta = TableMetaFactory.getMetaData(dsName);
				tableModel = meta.getTableByName(objectName);
			} else {
				IDbView dbView = TableMetaFactory.getDbView(dsName);
				tableModel = dbView.getModelByViewName(objectName);
			}
			map.put("tableModel", tableModel);
			map.put("success", "true");
		} catch (Exception ex) {
			map.put("success", "false");
		}
		return map;
	}

	/**
	 * 设置字段对话框。
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
		int style = 0;
		ModelAndView mv = this.getAutoView();
		if (id == 0) {
			dsName = RequestUtil.getString(request, "dsName");
			objectName = RequestUtil.getString(request, "objectName");
			istable = RequestUtil.getInt(request, "istable");
			style = RequestUtil.getInt(request, "style");
		} else {
			FormDialog bpmFormDialog = formDialogService.getById(id);
			dsName = bpmFormDialog.getDsalias();
			objectName = bpmFormDialog.getObjname();
			istable = bpmFormDialog.getIstable();
			style = bpmFormDialog.getStyle();
			mv.addObject("bpmFormDialog", bpmFormDialog);
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

		mv.addObject("tableModel", tableModel).addObject("style", style);

		return mv;
	}

	/**
	 * 取得树形数据。
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("getTreeData")
	@ResponseBody
	public List getTreeData(HttpServletRequest request) throws Exception {
		List list = new ArrayList();
		
		String alias = RequestUtil.getString(request, "alias");
		String pvalue = RequestUtil.getString(request, "idKey");
		String pname = RequestUtil.getString(request, "pidKeyName");
		int isRoot = RequestUtil.getInt(request, "isRoot");
		Map<String,Object> params = new HashMap<String, Object>();
		boolean isroot =false;
		if(isRoot==1){
			params = RequestUtil.getQueryMap(request);
			isroot = true;
		}else{
			params.put("pname", pname);
			params.put("pvalue", pvalue);
			params.put(pname, pvalue);
			isroot = false;
		}
		
		//判断是否有rpc远程接口
		String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
		if(StringUtil.isNotEmpty(rpcrefname)){
			//采用IOC方式，根据rpc远程接口调用数据
			CommonService commonService = (CommonService) AppUtil.getContext().getBean(rpcrefname);
			list = commonService.getFormDialogTreeData(alias,params,isroot);
		}else{
			//当不是rpc远程接口  从本地调用
			list = formDialogService.getTreeData(alias,params,isroot);
		}			
		
		for(Object obj:list){
			Map<String,Object> map = (Map<String, Object>) obj;
			Object isParent = map.get("ISPARENT");
			if(isParent!=null){
				if("true".equals(isParent.toString())){
					map.put("isParent",true);
				}else{
					map.put("isParent",false);
				}
			}
		}
		return list;

	}

	/**
	 * 选择自定义对话框
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAllDialogs")
	@ResponseBody
	public List<FormDialog> getAllDialogs(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<FormDialog> list= formDialogService.getAll();		
		return list;
	}

	/**
	 * 显示对话框。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("showFrame")
	public ModelAndView showFrame(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> paramsMap = RequestUtil.getQueryMap(request);
		String alias = RequestUtil.getString(request, "dialog_alias_");
		String dialogValue = RequestUtil.getString(request, alias);
		String dialogKeyName = RequestUtil.getString(request, "dialogKeyName");
		String nextUrl = RequestUtil.getUrl(request);
		String urlPara=request.getQueryString();
		FormDialog bpmFormDialog = null;
		//判断是否有rpc远程接口
		String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
		if(StringUtil.isNotEmpty(rpcrefname)){
			//采用IOC方式，根据rpc远程接口调用数据
			CommonService commonService = (CommonService) AppUtil.getBean(rpcrefname);
			bpmFormDialog = (FormDialog)commonService.getFormDialogData(alias, paramsMap);
		}else{
		//当不是rpc远程接口  或者  远程调用超时失败，从本地调用
		//if(bpmFormDialog == null){
			bpmFormDialog = formDialogService.getData(alias, paramsMap);
		}
		ModelAndView mv = this.getAutoView();
		try {
			mv.addObject("bpmFormDialog", bpmFormDialog);
			//rpcrefname传参
			mv.addObject(IFieldPool.rpcrefname, rpcrefname==null?"":rpcrefname.toString());
			//需要排序
			if(bpmFormDialog!=null && bpmFormDialog.getStyle()==0){
				String sortField=RequestUtil.getString(request, "sortField");
				String orderSeq=RequestUtil.getString(request, "orderSeq", DEFAULT_ORDER_SEQ);
				String newSortField=RequestUtil.getString(request, "newSortField");
				if(StringUtil.isNotEmpty(sortField)){
					paramsMap.put("sortField", sortField);
					paramsMap.put("orderSeq", orderSeq);
				}
				if(StringUtil.isEmpty(sortField)){
					DialogField dialogField;
					if(BeanUtils.isNotEmpty(bpmFormDialog.getSortList())){
						dialogField = bpmFormDialog.getSortList().get(0);
						sortField = dialogField.getFieldName();
						orderSeq = dialogField.getComment();
					}else{
						dialogField = bpmFormDialog.getDisplayList().get(0);
						sortField = dialogField.getFieldName();
					}
				}
				
				if(newSortField.equals(sortField)){
					if(orderSeq.equals("ASC")){
						orderSeq="DESC";
					}else{
						orderSeq="ASC";
					}
				}
				if(!StringUtil.isEmpty(newSortField)){
					sortField=newSortField;
				}
				
				Map<String,Object> parameters=new HashMap<String, Object>();
				parameters.put("sortField", StringUtil.isEmpty(newSortField)?sortField:newSortField);
				parameters.put("newSortField",null);
				parameters.put("orderSeq", orderSeq);
				nextUrl = addParametersToUrl(nextUrl, parameters);
				
				mv.addObject("sortField",sortField);
				mv.addObject("orderSeq",orderSeq);
				mv.addObject("baseHref", nextUrl);
				
				// 需要分页
				if (bpmFormDialog.getNeedpage() == 1) {
					PagingBean pageBean = bpmFormDialog.getPagingBean();
					String pageHtml = PageUtils.getPageHtml(pageBean, nextUrl, "", true, true);
					pageHtml = pageHtml.replaceAll("确定", "GO");
					mv.addObject("pageHtml", pageHtml);
				}
				mv.addObject("paramsMap", paramsMap) ;
				mv.addObject("dialogValue", dialogValue.toString()) ;
				mv.addObject("dialogKeyName", dialogKeyName.toString()) ;
			} else {
				//树 对话框
				mv.addObject("urlPara", urlPara) ;
				mv.addObject("dialogValue", dialogValue.toString()) ;
				mv.addObject("dialogKeyName", dialogKeyName.toString()) ;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return mv;
	}
	
	private static Map<String,Object> getQueryStringMap(String url){
		Map<String,Object> map = new  HashMap<String, Object>();
		int idx1=url.indexOf("?");
		if(idx1>0){
			String queryStr=url.substring(idx1+1);
			String[] queryNodeAry = queryStr.split("&");
			for(String queryNode:queryNodeAry){
				String[] strAry = queryNode.split("=");
				if(strAry.length>=2)
					map.put(strAry[0].trim(),strAry[1]);
			}
		}
		return map;
	}
	
	private static String addParametersToUrl(String url,Map<String, Object> params){
		StringBuffer sb=new StringBuffer();
		int idx1=url.indexOf("?");
		if(idx1>0){
			sb.append(url.substring(0, idx1));
		}
		sb.append("?");
		
		Map<String,Object> map=getQueryStringMap(url);
		map.putAll(params);
		
		for(Entry<String, Object> entry:map.entrySet()){
			if(BeanUtils.isEmpty(entry.getValue()))
				continue;
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("&");
		}
		return sb.substring(0, sb.length()-1);
	}
	
	/**
	 * 返回   自定义对话框  fk外键显示值
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("getFKColumnShow")
	@ResponseBody
	public Map<String, Object> getFKColumnShow(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map paramsMap = RequestUtil.getQueryMap(request);
		String alias = RequestUtil.getString(request, "dialog_alias_");
		//必须从paramsMap,移除非数据库字段参数，否则查询数据库会报错。
		paramsMap.remove("dialog_alias_");
		paramsMap.remove("rand");
		Map<String, Object> resultMap = null;
		//判断是否有rpc远程接口
		String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
		if(StringUtil.isNotEmpty(rpcrefname)){
			//必须从paramsMap,移除非数据库字段参数，否则查询数据库会报错。
			paramsMap.remove(IFieldPool.rpcrefname);
			//采用IOC方式，根据rpc远程接口调用数据
			CommonService commonService = (CommonService) AppUtil.getContext().getBean(rpcrefname);
			resultMap = commonService.getFKColumnShowData(alias, paramsMap);
		}else{
		//当不是rpc远程接口  或者  远程调用超时失败，从本地调用
		//if(resultMap == null){
			//必须从paramsMap,移除非数据库字段参数，否则查询数据库会报错。
			paramsMap.remove(IFieldPool.rpcrefname);
			resultMap = formDialogService.getFKColumnShowData(alias, paramsMap);
		}	
		
		Map<String, Object> map = new HashMap<String, Object>();

		if (resultMap != null) { 		 
			map.put("resultMap", resultMap);
			map.put("success", 1);
		} else {
			map.put("success", 0);
		} 
		return map;
	}
	/**
	 * 导出选择导出xml
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    @RequestMapping({"export"})
    @Action(description = "导出选择导出xml")
    public ModelAndView export(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String tableIds = RequestUtil.getString(request, "tableIds");
        ModelAndView mv = getAutoView();
        mv.addObject("tableIds", tableIds);
        return mv;
    }
    
    @RequestMapping({"exportXml"})
    @Action(description = "导出自定义对话框", detail = "导出自定义对话框:<#list StringUtils.split(tableIds,\",\") as item><#assign entity=bpmFormTableService.getById(Long.valueOf(item))/>【${entity.tableDesc}(${entity.tableName})】</#list>")
    public void exportXml(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String strXml = null;
        String fileName = null;
        Long[] tableIds = RequestUtil.getLongAryByStr(request, "tableIds");
        List<FormDialog> list = this.formDialogService.getAll();
        try
        {
            if (BeanUtils.isEmpty(tableIds))
            {
                if (BeanUtils.isNotEmpty(list))
                {
                    strXml = this.formDialogService.exportXml(list);
                    fileName = "全部自定义对话框记录_" + DateFormatUtil.getNowByString("yyyyMMddHHmmdd") + ".xml";
                }
            }
            else
            {
                strXml = this.formDialogService.exportXml(tableIds);
                fileName = DateFormatUtil.getNowByString("yyyyMMddHHmmdd") + ".xml";
                if (tableIds.length == 1)
                {
                    FormDialog bpmFormDialog = (FormDialog)this.formDialogService.getById(tableIds[0]);
                    fileName = bpmFormDialog.getName() + "_" + fileName;
                }
                else if (tableIds.length == list.size())
                {
                    fileName = "全部自定义对话框记录_" + fileName;
                }
                else
                {
                    fileName = "多个自定义对话框记录_" + fileName;
                }
            }
            FileUtil.downLoad(request, response, strXml, fileName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @RequestMapping({"importXml"})
    @Action(description = "导入自定义对话框")
    public void importXml(MultipartHttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        MultipartFile fileLoad = request.getFile("xmlFile");
        ResultMessage message = null;
        try
        {
            this.formDialogService.importXml(fileLoad.getInputStream());
            message = new ResultMessage(1, MsgUtil.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            message = new ResultMessage(0, "导入文件异常，请检查文件格式！");
        }
        writeResultMessage(response.getWriter(), message);
    }
    
    /**
     * 取得通用表单对话框分页列表
     * 
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("dialogData")
    @ResponseBody
    @Action(description = "查看通用表单对话框分页列表")
    public Object dialogData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rpcbeaName=request.getParameter("rpcbeaName");
        Map<String,Object> result=new HashMap<String,Object>();
        List<?extends IFormDialog> list=null;
        if(StringUtil.isNotEmpty(rpcbeaName)){
    		Map<String, Object> map = RequestUtil.getQueryMap(request);
			int page = RequestUtil.getInt(request, "page", 1);
			int pageSize = RequestUtil.getInt(request, "pageSize", 15);
			String requestURI=request.getRequestURI();
            CommonService commonService = (CommonService) AppUtil.getBean(rpcbeaName);
            list =commonService.getAllFormDialoggetAll(new QueryFilter(page,pageSize, map,requestURI));
        }else{
            list = formDialogService.getAll(new QueryFilter(request, false));
        }
        result.put("Rows", list);
        result.put("Total", list.size());
        return result;
    }
    
    
    
    /**
     * 取得通用表单对话框分页列表
     * 
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("dialogResultFiled")
    @ResponseBody
    @Action(description = "查看通用表单对话框分页列表")
    public JSONObject dialogResultFiled(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        JSONObject json=new JSONObject();
        String alias=request.getParameter("alias");
        String rpcbeaName=request.getParameter("rpcbeaName");
        IFormDialog formDialog=null;
        if(StringUtil.isNotEmpty(rpcbeaName)){
            CommonService commonService=(CommonService)AppUtil.getBean(Class.forName(rpcbeaName));
            formDialog=commonService.getByFormDialogAlias(alias);
        }else{
            formDialog = formDialogService.getByAlias(alias);
        }
        List<? extends IDialogField> fileds=formDialog.getReturnList();
        json.put("fileds", fileds);
        return json;
    }
	 
	
	/**
	 * 获取自定义对话框数据
	 * 
	 * @param request
	 * @param response
	 * @return bpmFormDialog
	 * @throws Exception
	 */
	@RequestMapping("getDialogData")
	public void getDialogData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Map<String, Object> paramsMapNull = new HashMap<String, Object>();
		
		String dialogName = RequestUtil.getString(request, "dialogName");
		String showField = RequestUtil.getString(request, "showField");
		String fuzzyName = RequestUtil.getString(request, "fuzzyName");
		paramsMap.put(showField, fuzzyName);
		
		//首先判断redis中有没有该数据
		//若是则从redis中取数据，否则从数据库中读取并存到redis中,并将过期时间设置为15s
		
		//判断是否存在key
		boolean isExist = RedisClient.exists(dialogName);
		JSONArray dialogData = new JSONArray();
		if(isExist){
			Object dialogDataString = RedisClient.get(dialogName);
			JSONArray dialogDataAll = JSONArray.fromObject(dialogDataString);
			for(int i=0;i<dialogDataAll.size();i++){
				Map dd = (Map) dialogDataAll.get(i);
				Object obj = dd.get(showField);
				if(obj!=null&&obj.toString().contains(fuzzyName)){
					dialogData.add(dd);
					
					//保证模糊查询的数据数量不超过20条
					if(dialogData.size()>20)
						break;
				}
			}
		}else{
			FormDialog bpmFormDialog = formDialogService.getData(dialogName, paramsMap);
			List<Map<String, Object>> paramsMap02 = bpmFormDialog.getList();
			if(paramsMap02.size()>20){
				paramsMap02 = paramsMap02.subList(0, 20);
			}
			dialogData = JSONArray.fromObject(paramsMap02);
			
			//设置新的对话框数据到redis中，并设置过期时间为12s
			try {
				//redis中存放的是该对话框中所有的数据
				FormDialog formDialogRedis = formDialogService.getData(dialogName, paramsMapNull);
				List<Map<String, Object>> redisMap = formDialogRedis.getList();
				JSONArray dialogDataRedis = JSONArray.fromObject(redisMap);
				
		   		RedisClient.set(dialogName, dialogDataRedis.toString());
		   		RedisClient.expired(dialogName, 12);
	        } catch (Exception e) {
	            logger.error("dialogArr放到redis中初始化出错");
	        }
		}
		
		response.getWriter().write(dialogData.toString());
	}
}
