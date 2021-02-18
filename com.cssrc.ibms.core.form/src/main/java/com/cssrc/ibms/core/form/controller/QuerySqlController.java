package com.cssrc.ibms.core.form.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceDefService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceService;
import com.cssrc.ibms.core.constant.system.GlobalTypeConstant;
import com.cssrc.ibms.core.db.datasource.DbContextHolder;
import com.cssrc.ibms.core.db.datasource.JdbcTemplateUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.form.model.QueryField;
import com.cssrc.ibms.core.form.model.QuerySetting;
import com.cssrc.ibms.core.form.model.QuerySql;
import com.cssrc.ibms.core.form.service.QueryFieldService;
import com.cssrc.ibms.core.form.service.QuerySettingService;
import com.cssrc.ibms.core.form.service.QuerySqlService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping("/oa/form/querySql/")
public class QuerySqlController extends BaseController {
	@Resource
	private QuerySqlService sysQuerySqlService;

	@Resource
	private ISysDataSourceService sysDataSourceService;

	@Resource
	private QueryFieldService sysQueryFieldService;

	@Resource
	private QuerySettingService sysQuerySettingService;

	@Resource
	private IGlobalTypeService globalTypeService;
	
	@Resource
	private ISysDataSourceDefService sysDataSourceDefService;

	@RequestMapping("manage")
	public ModelAndView manage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		return mv;
	}

	@RequestMapping("manageTree")
	public ModelAndView manageTree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		return mv;
	}

	@RequestMapping("save")
	@Action(description = "添加或更新SYS_QUERY_SQL")
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resultMsg = null;
		try {
			String jsonStr = RequestUtil.getString(request, "jsonStr", "",
					false);
			String fieldJson = RequestUtil
					.getString(request, "fieldJson", null);
			QuerySql sysQuerySql = this.sysQuerySqlService
					.getSysQuerySql(jsonStr);
			if ((sysQuerySql.getId() == null)
					|| (sysQuerySql.getId().longValue() == 0L)) {
				sysQuerySql.setId(Long.valueOf(UniqueIdUtil.genId()));
				//////////////////start
				String alias=sysQuerySql.getAlias();
				boolean isExist=this.sysQuerySqlService.isExistAlias(alias);
				if(isExist){
					resultMsg=getText("record.add.fail",getText("controller.bpmFormQuery.alias"));
					writeResultMessage(response.getWriter(),resultMsg,resultMsg,ResultMessage.Fail);
					return;
				}
				//////////////////end
				
				//设置需要使用的数据源
				DbContextHolder.setDataSource(sysQuerySql.getDsname());
				//将需要的fields取出来
				List<QueryField> fieldList = this.sysQueryFieldService.getSqlField(sysQuerySql.getId(),
						sysQuerySql.getDsname(), sysQuerySql.getSql());
				//还原默认的数据源
				DbContextHolder.setDefaultDataSource();
				
				this.sysQuerySqlService.add(sysQuerySql);
				for (QueryField sysQueryField : fieldList){
					sysQueryFieldService.add(sysQueryField);
				}
				resultMsg = getText("添加成功", new Object[] { "SYS_QUERY_SQL" });
			} else {
				
				//////////////////start
				String alias=sysQuerySql.getAlias();
				Long id=sysQuerySql.getId();
				boolean isExist=this.sysQuerySqlService.isExistAliasForUpd(id, alias);
				if(isExist){
					resultMsg=getText("record.add.fail",getText("controller.bpmFormQuery.alias"));
					writeResultMessage(response.getWriter(),resultMsg,resultMsg,ResultMessage.Fail);
					return;
				}
				//////////////////end

				this.sysQuerySqlService.update(sysQuerySql);
				List sysQueryFields = this.sysQueryFieldService.getSysQueryFieldArr(fieldJson);
				this.sysQueryFieldService.saveOrUpdate(sysQueryFields);
				if ((sysQueryFields != null) && (!sysQueryFields.isEmpty())) {
					QuerySetting sysQuerySetting = this.sysQuerySettingService
							.getBySqlId(sysQuerySql.getId());
					if (BeanUtils.isNotEmpty(sysQuerySetting.getId())) {
						this.sysQuerySettingService.syncSettingAndField(sysQuerySetting, sysQueryFields,sysQuerySql);
					}

				}

				resultMsg = getText("更新成功", new Object[] { "SYS_QUERY_SQL" });
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	@RequestMapping("list")
	@Action(description = "查看SYS_QUERY_SQL分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.sysQuerySqlService.getAll(new QueryFilter(request,
				"sysQuerySqlItem"));
		return getAutoView().addObject("sysQuerySqlList", list);
	}

	@RequestMapping("del")
	@Action(description = "删除SYS_QUERY_SQL")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysQuerySqlService.delByIds(lAryId);

			this.sysQueryFieldService.delBySqlIds(lAryId);

			this.sysQuerySettingService.delBySqlIds(lAryId);

			message = new ResultMessage(1, "删除成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping("edit")
	@Action(description = "编辑SYS_QUERY_SQL")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		String returnUrl=RequestUtil.getPrePage(request);
		Long id = Long.valueOf(RequestUtil.getLong(request, "id", 0L));
		QuerySql sysQuerySql = (QuerySql) this.sysQuerySqlService.getById(id);
		List sysDataSourceList = this.sysDataSourceDefService.getAllAndDefault();
		List sysQueryFields = new ArrayList();
		List globalTypeList = this.globalTypeService.getByCatKey(GlobalTypeConstant.CAT_FORM,true);
		if (id.longValue() != 0L) {
			sysQueryFields = this.sysQueryFieldService.getListBySqlId(id);
		}
		return getAutoView().addObject("sysQuerySql", sysQuerySql).addObject(
				"sysQueryFields", sysQueryFields).addObject("dsList",
				sysDataSourceList).addObject("globalTypeList", globalTypeList).addObject("returnUrl", returnUrl);
	}

	@RequestMapping("get")
	@Action(description = "查看SYS_QUERY_SQL明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		QuerySql sysQuerySql = (QuerySql) this.sysQuerySqlService.getById(id);
		return getAutoView().addObject("sysQuerySql", sysQuerySql);
	}

	@RequestMapping("validSql")
	@ResponseBody
	public boolean validSql(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String sql = RequestUtil.getString(request, "sql", "", false);
		String dsname = RequestUtil.getString(request, "dsname");
		Boolean rollback = Boolean.valueOf(RequestUtil.getBoolean(request, "rollback", false));
		try {
			DbContextHolder.setDataSource(dsname);
			Boolean b = sysQuerySqlService.validSql(sql, rollback.booleanValue());
			DbContextHolder.setDefaultDataSource();
			return b.booleanValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 节点sql的sql语句查询验证
	 * 
	 * @author liubo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"validNodeSql"})
	@ResponseBody
	public boolean validNodeSql(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		String sql = request.getParameter("sql");
		String dsalias = RequestUtil.getString(request, "dsalias", "");
		String mapStr = RequestUtil.getString(request, "map", "{}");
		net.sf.json.JSONObject map = net.sf.json.JSONObject.fromObject(mapStr);
		//Boolean rollback = Boolean.valueOf(RequestUtil.getBoolean(request, "rollback", true));
		try {
			DbContextHolder.setDataSource(dsalias);
			sql = explainSql(sql, map);
			//Boolean b = JdbcTemplateUtil.executeSql(sql, rollback.booleanValue());
			Boolean b = this.sysQuerySqlService.handSql(sql);
			DbContextHolder.setDefaultDataSource();
			return b.booleanValue(); } catch (Exception e) {
		}
		return false;
	}
	
	private String explainSql(String sql, net.sf.json.JSONObject jsonObject){
		for (Iterator localIterator = jsonObject.keySet().iterator(); localIterator.hasNext(); ) { 
			Object obj = localIterator.next();
			String key = obj.toString();
			String val = jsonObject.getString(key);
			sql = sql.replace(key, val);
		}
		return sql;
	}

	@RequestMapping("urlParamsDialog")
	@Action(description = "urlParams对话框")
	public ModelAndView urlParamsDialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id", 0L));
		List sysQueryFields = new ArrayList();
		if (id.longValue() != 0L) {
			sysQueryFields = this.sysQueryFieldService.getListBySqlId(id);
		}
		return getAutoView().addObject("sysQueryFields", sysQueryFields);
	}
	
	  @RequestMapping({"exportXml"})
	  @Action(description="导出自定义Sql查询表", detail="导出自定义Sql查询表:<#list StringUtils.split(tableIds,\",\") as item><#assign entity=sysQuerySqlService.getById(Long.valueOf(item))/>【${entity.tableDesc}(${entity.tableName})】</#list>")
	  public void exportXml(HttpServletRequest request, HttpServletResponse response)
	    throws Exception
	  {
	    String strXml = null;
	    String fileName = null;
	    Long[] tableIds = RequestUtil.getLongAryByStr(request, "tableIds");
	    try
	    {
	      if (BeanUtils.isEmpty(tableIds)) {
	        List list = this.sysQuerySqlService.getAll();
	        if (BeanUtils.isNotEmpty(list)) {
	          strXml = this.sysQuerySqlService.exportXml(list);
	          fileName = "全部自定义Sql查询记录_" + 
	            DateFormatUtil.getNowByString("yyyyMMddHHmmdd") + ".xml";
	        }
	      } else {
	        strXml = this.sysQuerySqlService.exportXml(tableIds);
	        fileName = DateFormatUtil.getNowByString("yyyyMMddHHmmdd") + ".xml";
	        if (tableIds.length == 1) {
	        	QuerySql sysQuerySql = 
	            (QuerySql)this.sysQuerySqlService
	            .getById(tableIds[0]);
	          fileName = sysQuerySql.getName() + "_" + fileName;
	        } else {
	          fileName = "自定义Sql查询记录_" + fileName;
	        }
	      }
	      //FileUtil.downLoad(request, response, strXml, fileName);
	     response.setContentType("application/octet-stream");
		 response.setHeader("Content-Disposition", "attachment;filename="
					+ StringUtil.encodingString(fileName, "GBK", "ISO-8859-1")
					+ ".xml");
		 response.getWriter().write(strXml);
		 response.getWriter().flush();
		 response.getWriter().close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

	  @RequestMapping({"importXml"})
	  @Action(description="导入自定义Sql查询")
	  public void importXml(MultipartHttpServletRequest request, HttpServletResponse response)
	    throws Exception
	  {
	    MultipartFile fileLoad = request.getFile("xmlFile");
	    ResultMessage message = null;
	    try {
	      this.sysQuerySqlService.importXml(fileLoad.getInputStream());
	      message = new ResultMessage(1, MsgUtil.getMessage());
	    } catch (Exception e) {
	      e.printStackTrace();
	      message = new ResultMessage(0, "导入文件异常,请检查文件格式!");
	    }
	    writeResultMessage(response.getWriter(), message);
	  }
}
