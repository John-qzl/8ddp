package com.cssrc.ibms.core.form.service;

 
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IQuerySettingService;
import com.cssrc.ibms.api.system.intf.IDictionaryService;
import com.cssrc.ibms.api.system.model.IDictionary;
import com.cssrc.ibms.api.sysuser.util.CommonVar;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.db.mybatis.page.PageUtils;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.util.JdbcHelperUtil;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.excel.Excel;
import com.cssrc.ibms.core.form.dao.FormTemplateDao;
import com.cssrc.ibms.core.form.dao.QuerySettingDao;
import com.cssrc.ibms.core.form.model.FormTemplate;
import com.cssrc.ibms.core.form.model.QueryField;
import com.cssrc.ibms.core.form.model.QuerySetting;
import com.cssrc.ibms.core.form.model.QuerySql;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import freemarker.template.TemplateException;

@Service
public class QuerySettingService extends BaseService<QuerySetting> implements IQuerySettingService
{

  @Resource
  private QuerySettingDao dao;

  @Resource
  private FormTemplateDao bpmFormTemplateDao;

  @Resource
  private QuerySqlService sysQuerySqlService;

  @Resource
  private QueryFieldService sysQueryFieldService;

  @Resource
  private FreemarkEngine freemarkEngine;

  @Resource
  private GroovyScriptEngine groovyScriptEngine;

  @Resource
  private QueryRightParseService sysQueryRightParseService;

  @Resource
  private QueryDisplayFieldParseService sysQueryDisplayFieldParseService;

  @Resource
  private QueryConditionFieldParseService sysQueryConditionFieldParseService;

  @Resource
  private QuerySortFieldParseService sysQuerySortFieldParseService;

  @Resource
  private QueryFilterFieldParseService sysQueryFilterFieldParseService;

  @Resource
  private QueryExportFieldParseService sysQueryExportFieldParseService;

  @Resource
  private QueryManageFieldParseService sysQueryManageFieldParseService;

  @Resource
  private IDictionaryService dictionaryService;
  
  @Resource
  private FormControlService formControlService;

  protected IEntityDao<QuerySetting, Long> getEntityDao()
  {
    return this.dao;
  }

  public QuerySetting getBySqlId(Long sqlId)
    throws Exception
  {
    QuerySetting sysQuerySetting = this.dao.getBySqlId(sqlId);

    if (sysQuerySetting.getNeedPage() == null) {
      sysQuerySetting.setNeedPage(QuerySetting.NO_NEED_PAGE);
    }

    if (!this.sysQueryDisplayFieldParseService.hasDisplayField(sysQuerySetting
      .getDisplayField())) {
      sysQuerySetting.setDisplayField(this.sysQueryDisplayFieldParseService
        .getDefaultDisplayField(sqlId));
    }

    if (!this.sysQueryConditionFieldParseService
      .hasConditionField(sysQuerySetting.getConditionField())) {
      sysQuerySetting
        .setConditionField(this.sysQueryConditionFieldParseService
        .getDefaultConditionField(sqlId));
    }

    if (!this.sysQueryExportFieldParseService.hasExportField(sysQuerySetting
      .getExportField())) {
      sysQuerySetting.setExportField(this.sysQueryExportFieldParseService
        .getDefaultExportField(sqlId));
    }
    if (!this.sysQueryManageFieldParseService.hasManageField(sysQuerySetting.getManageField())){
    	sysQuerySetting.setManageField(
      		  this.sysQueryManageFieldParseService.getDefaultManageButton(sqlId,sysQuerySetting.getManageField()));
    }

    return sysQuerySetting;
  }

  public QuerySetting getSysQuerySetting(String json)
  {
    JSONUtils.getMorpherRegistry().registerMorpher(
      new DateMorpher(new String[] { "yyyy-MM-dd" }));
    if (StringUtil.isEmpty(json))
      return null;
    JSONObject obj = JSONObject.fromObject(json);
    QuerySetting sysQuerySetting = (QuerySetting)JSONObject.toBean(
      obj, QuerySetting.class);
    return sysQuerySetting;
  }

  public void save(QuerySetting sysQuerySetting, boolean flag)
    throws Exception
  {
    String templateHtml = generateTemplate(sysQuerySetting);
    sysQuerySetting.setTemplateHtml(templateHtml);
    if (flag) {
      sysQuerySetting.setId(Long.valueOf(UniqueIdUtil.genId()));
      add(sysQuerySetting);
    } else {
      update(sysQuerySetting);
    }
  }

  public String generateTemplate(QuerySetting sysQuerySetting)
    throws Exception
  {
    FormTemplate bpmFormTemplate = this.bpmFormTemplateDao
      .getByTemplateAlias(sysQuerySetting.getTemplateAlias());

    boolean hasCondition = this.sysQueryConditionFieldParseService
      .hasConditionField(sysQuerySetting.getConditionField());

    List<QueryField> sysQueryFieldList = this.sysQueryFieldService
      .getListBySqlId(sysQuerySetting.getSqlId());

    Map<String, Object> rightMap = this.sysQueryRightParseService.getRightMap(
      UserContextUtil.getCurrentUserId(), UserContextUtil.getCurrentOrgId());

    boolean hasManageButton = this.sysQueryManageFieldParseService
      .hasManageButton(sysQuerySetting.getManageField(), rightMap);

    String manageJsonAry = this.sysQueryManageFieldParseService.getRightManage(
      sysQuerySetting, rightMap);

    Map<String, Object> formatDataMap = getFormatDataMap(sysQueryFieldList);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("manageJsonAry", manageJsonAry);
    map.put("hasManageButton", Boolean.valueOf(hasManageButton));
    map.put("sysQuerySetting", sysQuerySetting);
    map.put("hasCondition", Boolean.valueOf(hasCondition));
    map.put("formatData", formatDataMap);
    String templateHtml = this.freemarkEngine.parseByStringTemplate(map, 
      bpmFormTemplate.getHtml());
    if (this.logger.isDebugEnabled())
      this.logger.debug("第一次解析html:" + templateHtml);
    return templateHtml;
  }

  private Map<String, Object> getFormatDataMap(List<QueryField> sysQueryFieldList)
  {
    Map<String,Object> map = new HashMap<String,Object>();

    for (QueryField sysQueryField : sysQueryFieldList) {
      String fieldName = sysQueryField.getName();
      Short controlType = Short.valueOf(sysQueryField.getControlType() == null ? 0 : 
        sysQueryField.getControlType().shortValue());
      String fieldType = sysQueryField.getType();

      if ("date".equalsIgnoreCase(fieldType)) {
        map.put(fieldName, sysQueryField.getFormat());
      }
      else
      {
        Iterator localIterator2;
        if ((11 == controlType.shortValue()) || 
          (14 == controlType.shortValue()) || 
          (13 == controlType.shortValue())) {
          String options = sysQueryField.getControlContent();
          if (StringUtil.isNotEmpty(options)) {
            Map<String,Object> optionMap = new HashMap<String,Object>();
            JSONArray jarray = JSONArray.fromObject(options);
            for (localIterator2 = jarray.iterator(); localIterator2.hasNext(); ) { 
              Object obj = localIterator2.next();
              JSONObject json = (JSONObject)obj;
              String key = (String)json.get("key");
              String value = (String)json.get("val");
              optionMap.put(key, value);
            }
            map.put(fieldName, optionMap);
          }
        } else if (controlType.shortValue() == 3)
        {
          String dicId = sysQueryField.getControlContent();
          //TODO CHECK RIGHT OR WRONG dictionaryService.getByTypeId BY YANGBO
          List<?extends IDictionary> dictionarieList = this.dictionaryService.getByTypeId(Long.valueOf(dicId));
          Map<Object,Object> dicMap = new HashMap<Object,Object>();
          for (IDictionary dictionary : dictionarieList) {
            dicMap.put(dictionary.getItemName(), dictionary.getItemName());
          }
          map.put(fieldName, dicMap);
        }
      }
    }
    return map;
  }

  public String getDisplay(Long id, Map<String, Object> params, Map<String, Object> queryParams)
    throws Exception
  {
	// 设置常用变量
    CommonVar.setCurrentVars(params);
    
    // 获取权限map
    Map<String, Object> rightMap = this.sysQueryRightParseService.getRightMap(
      UserContextUtil.getCurrentUserId(), UserContextUtil.getCurrentOrgId());
    QuerySetting sysQuerySetting = (QuerySetting)this.dao.getById(id);

    if (!params.containsKey("__isQueryData")) {
      params.put("__isQueryData", 
        Boolean.valueOf(sysQuerySetting.getIsQuery().shortValue() == 0));
    }else{
    	params.put("__isQueryData", Boolean.valueOf(CommonTools.Obj2String(params.get("__isQueryData"))));
    }
    String baseURL = (String)params.get("__baseURL");

    String filterJsonAry = this.sysQueryFilterFieldParseService.getRightFilterField(sysQuerySetting, rightMap, baseURL);
    sysQuerySetting.setFilterField(filterJsonAry);

    String filterKey = this.sysQueryFilterFieldParseService.getFilterKey(sysQuerySetting, params);
    params.put("__filterKey__", filterKey);

    String tableIdCode = (String)params.get("__tic");
    if (tableIdCode == null) {
      tableIdCode = "";
      params.put("__tic", "");
    }

    Map<String, String> sortMap = this.sysQuerySortFieldParseService.getSortMap(params, tableIdCode);

    JSONObject filterJson = this.sysQueryFilterFieldParseService.getFilterFieldJson(sysQuerySetting, rightMap, params);
    //查询字段
    List<QueryField> sysQueryFieldList = this.sysQueryFieldService.getListBySqlId(sysQuerySetting.getSqlId());

    Map<String, Object> formatData =  getFormatDataMap(sysQueryFieldList);
    //判断是否存在查询
    boolean isQueryData =  ((Boolean)params.get("__isQueryData")).booleanValue();

    if (isQueryData)
    {
      sysQuerySetting = getData(sysQuerySetting, rightMap, params, sortMap, formatData, filterJson);
    }

    List<String> excludes = getExcludes(params, queryParams);

    String pageURL = addParametersToUrl(baseURL, params, excludes);
    String searchFormURL = baseURL;

    String templateHtml = sysQuerySetting.getTemplateHtml();

    if (StringUtil.isEmpty(templateHtml)) {
      templateHtml = generateTemplate(sysQuerySetting);
    }
    Map<String, Object> map = new HashMap<String, Object>();

    String pageHtml = getPageHtml(sysQuerySetting, map, tableIdCode, 
      pageURL);

    map.clear();
    map.put("sysQuerySetting", sysQuerySetting);
    String manageJsonAry = this.sysQueryManageFieldParseService.getRightManage(
    	      sysQuerySetting, rightMap);
    map.put("manageJsonAry", manageJsonAry);
    map.put("pageHtml", pageHtml);
    map.put("pageURL", pageURL);
    map.put("sort", sortMap);
    map.put("sortField", sortMap.get("sortField"));
    map.put("orderSeq", sortMap.get("orderSeq"));
    map.put("tableIdCode", tableIdCode);
    map.put("searchFormURL", searchFormURL);

    map.put("permission", this.sysQueryDisplayFieldParseService
      .getDisplayFieldPermission(0, 
      sysQuerySetting.getDisplayField(), rightMap));

    map.put("filterKey", filterKey);
    map.put("formatData", formatData);
    map.put("param", queryParams);
	map.put("dbomSql", CommonTools.Obj2String(params.get("__dbomSql__")));
    map.put("ctx", params.get("__ctx"));
    map.put("service", formControlService);
    String html = this.freemarkEngine.parseByStringTemplate(map, templateHtml);
    //this.logger.info(html);
    return html;
  }

  private String getPageHtml(QuerySetting sysQuerySetting, Map<String, Object> map, String tableIdCode, String pageURL)
    throws IOException, TemplateException
  {
    String pageHtml = "";
    if (BeanUtils.isEmpty(sysQuerySetting.getList())) {
      return pageHtml;
    }
    if (sysQuerySetting.getNeedPage().shortValue() == 1) {
      PagingBean pageBean = sysQuerySetting.getPageBean();
      map.put("tableIdCode", tableIdCode);
      map.put("pageBean", pageBean);
      map.put("showExplain", Boolean.valueOf(true));
      map.put("showPageSize", Boolean.valueOf(true));
      map.put("baseHref", pageURL);
      map.put("pageURL", pageURL);
      
      String pageTempl = "pageAjax.ftl";
      int type = SysConfConstant.SHOW_TYPE;
      if(type == 0){
		pageTempl = "oldpageAjax.ftl";//旧版本分页模板 by YangBo
      }
      pageHtml = this.freemarkEngine.mergeTemplateIntoString(pageTempl, map);
    }
    return pageHtml;
  }

  private String addParametersToUrl(String url, Map<String, Object> params, List<String> excludes)
  {
    StringBuffer sb = new StringBuffer();
    int idx1 = url.indexOf("?");
    if (idx1 > 0)
      sb.append(url.substring(0, idx1));
    else {
      sb.append(url);
    }
    sb.append("?");

    Map map = getQueryStringMap(url);
    if (BeanUtils.isNotEmpty(params)) {
      map.putAll(params);
    }

    if (excludes != null) {
      for (String ex : excludes) {
        map.remove(ex);
      }
    }

    for (Object obj : map.entrySet()) {
    	Map.Entry entry = (Map.Entry)obj;
      String key = (String)entry.getKey();
      Object val = entry.getValue();
      if (!BeanUtils.isEmpty(val))
      {
        sb.append(key);
        sb.append("=");
        sb.append(val);
        sb.append("&");
      }
    }
    return sb.substring(0, sb.length() - 1);
  }

  private static Map<String, Object> getQueryStringMap(String url)
  {
    Map map = new HashMap();
    int idx1 = url.indexOf("?");
    if (idx1 > 0) {
      String queryStr = url.substring(idx1 + 1);
      String[] queryNodeAry = queryStr.split("&");
      for (String queryNode : queryNodeAry) {
        String[] strAry = queryNode.split("=");
        if (strAry.length == 1)
          map.put(strAry[0], null);
        else {
          map.put(strAry[0].trim(), strAry[1]);
        }
      }
    }
    return map;
  }

  private List<String> getExcludes(Map<String, Object> params, Map<String, Object> queryParams)
  {
    List<String> excludes = new ArrayList<String>();
    for (String key : params.keySet()) {
      if (key.endsWith("__ns__")) {
        excludes.add(key);
      }
      if (key.endsWith("__pk__")) {
        excludes.add(key);
      }
    }
    excludes.add("rand");
    excludes.add("__baseURL");
    excludes.add("__tic");
    excludes.add("__displayId");

    for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
      String key = (String)entry.getKey();
      if (key.startsWith("Q_"))
      {
        String[] aryParaKey = key.split("_");
        if (aryParaKey.length >= 3)
        {
          String paraName = key.substring(2, key.lastIndexOf("_"));
          excludes.add(paraName);
        }
      }
    }
    return excludes;
  }

  private QuerySetting getData(QuerySetting sysQuerySetting, Map<String, Object> rightMap, Map<String, Object> params, Map<String, String> sortMap, Map<String, Object> formatData, JSONObject filterJson)
    throws Exception
  {
    QuerySql sysQuerySql = (QuerySql)this.sysQuerySqlService.getById(sysQuerySetting
      .getSqlId());

    JdbcHelper jdbcHelper = getJdbcHelper(sysQuerySql.getDsname());
    String tableIdCode = "";
    if (params.get("__tic") != null) {
      tableIdCode = (String)params.get("__tic");
    }
    List list = new ArrayList();
    String sql = "";

    // 是否需要分页。
    if (sysQuerySetting.getNeedPage().shortValue() == QuerySetting.NEED_PAGE.shortValue()) {
      int currentPage = 1;
      Object pageObj = params.get(tableIdCode + "p");
      if (pageObj != null) {
        currentPage = Integer.parseInt(params.get(
          tableIdCode + "p").toString());
      }
      int pageSize = sysQuerySetting.getPageSize().shortValue();
      Object pageSizeObj = params.get(tableIdCode + 
        "z");
      if (pageSizeObj != null) {
        pageSize = Integer.parseInt(params.get(
          tableIdCode + "z").toString());
      }

      sql = getSQL(filterJson, sysQuerySetting, sysQuerySql, 
        rightMap, params, sortMap);
      Object oldPageSizeObj = params.get(tableIdCode + "oz");
      int oldPageSize = sysQuerySetting.getPageSize().shortValue();
      if (oldPageSizeObj != null) {
        oldPageSize = Integer.parseInt(params.get(tableIdCode + "oz")
          .toString());
      }
      if (pageSize != oldPageSize) {
        int first = PageUtils.getFirstNumber(currentPage, oldPageSize);
        currentPage = first / pageSize + 1;
      }
      PagingBean pageBean = new PagingBean(currentPage, pageSize);

      list = jdbcHelper.getPage(currentPage, pageSize, sql, params, 
        pageBean);
      sysQuerySetting.setPageBean(pageBean);
    }
    else {
    //拼接查询sql，关于选择器这里拼接的是ID
      sql = getSQL(filterJson, sysQuerySetting, sysQuerySql, 
        rightMap, params, sortMap);
      list = jdbcHelper.queryForList(sql, params);
    }

    sysQuerySetting.setList(list);
    this.logger.debug("查询的SQL：" + sql);
    this.logger.debug("查询的params：" + params.toString());
    return sysQuerySetting;
  }

    private String getSQL(JSONObject filterJson, QuerySetting sysQuerySetting, QuerySql sysQuerySql,
        Map<String, Object> rightMap, Map<String, Object> params, Map<String, String> sortMap)
    {
        if (JSONUtil.isNotEmpty(filterJson))
        {
            String whereStr = "";
            String type = (String)filterJson.get("type");
            if ("2".equals(type))
            {
                StringBuffer sql = new StringBuffer("SELECT * FROM ( ");
                String condition = (String)filterJson.get("condition");
                Map<String, Object> paramsMap = new HashMap<String, Object>();
                paramsMap.put("map", params);
                
                // 表单 字段过滤条件
                whereStr = this.sysQueryConditionFieldParseService.getQuerySQL(sysQuerySetting, whereStr, params);
                //业务数据模板过滤条件
                sql.append(this.groovyScriptEngine.executeString(condition, paramsMap));
                
                sql.append(")tt");
                if(StringUtil.isNotEmpty(whereStr)){
                    sql.append(" WHERE "+whereStr);
                }
                return sql.toString();
            }
        }
        
        StringBuffer sql =
            new StringBuffer("SELECT * FROM ( ").append(sysQuerySql.getSql()).append(" ) T").append(" WHERE 1=1 ");
        
        String whereStr = "";
        
        whereStr = this.sysQueryFilterFieldParseService.getFilterSQL(filterJson);
        
        whereStr = this.sysQueryConditionFieldParseService.getQuerySQL(sysQuerySetting, whereStr, params);
        String orderStr = this.sysQuerySortFieldParseService.getOrderBySql(sysQuerySetting.getSortField(), sortMap);
        if (StringUtil.isNotEmpty(whereStr))
        {
            whereStr = "AND " + whereStr;
        }
        // By weilei：针对DBom数据查询添加过滤条件
        String dbomSql = CommonTools.Obj2String(params.get("__dbomSql__"));
        dbomSql = "".equals(dbomSql) ? "" : dbomSql.replace("<DBom>", "'");
        if (!"".equals(dbomSql))
        {
            if (!sql.toString().toUpperCase().contains("WHERE"))
            {
                sql.append(" WHERE " + dbomSql);
            }
            else
            {
                sql.append(" AND " + dbomSql);
            }
        }
        
        return sql + whereStr + orderStr;
    }

  private JdbcHelper getJdbcHelper(String dsAlias)
    throws Exception
  {
    return JdbcHelperUtil.getJdbcHelper(dsAlias);
  }

  public HSSFWorkbook export(Long id, int exportType, Map<String, Object> params)
    throws Exception
  {
    Long curUserId = UserContextUtil.getCurrentUserId();
    Long curOrgId = UserContextUtil.getCurrentOrgId();
    CommonVar.setCurrentVars(params);
    QuerySetting sysQuerySetting = (QuerySetting)this.dao.getById(id);
    if (BeanUtils.isEmpty(sysQuerySetting)) {
      return null;
    }
    Map rightMap = this.sysQueryRightParseService.getRightMap(
      curUserId, curOrgId);

    Map exportFieldMap = this.sysQueryExportFieldParseService
      .getExportFieldPermission(2, 
      sysQuerySetting.getExportField(), rightMap);
    String[] sortFieldKey = sysQueryExportFieldParseService.getSortedExportField
    		(sysQuerySetting.getExportField(), exportFieldMap, false);
    String[] sortFieldDesc = sysQueryExportFieldParseService.getSortedExportField
    		(sysQuerySetting.getExportField(), exportFieldMap, true);
    if (exportType == 0) {
      sysQuerySetting.setNeedPage(Short.valueOf((short)0));
    }
    String filterJsonAry = this.sysQueryFilterFieldParseService
      .getRightFilterField(sysQuerySetting, rightMap, "/getDisplay.do");
    sysQuerySetting.setFilterField(filterJsonAry);

    JSONObject filterJson = this.sysQueryFilterFieldParseService.getFilterFieldJson(
      sysQuerySetting, rightMap, params);

    sysQuerySetting = getData(sysQuerySetting, rightMap, params, null, 
      new HashMap(), filterJson);

    List rightDataList = this.sysQueryExportFieldParseService
      .getRightDataList(sysQuerySetting.getList(), exportFieldMap);

    Excel excel = this.sysQueryExportFieldParseService.getExcel(rightDataList,sortFieldKey,sortFieldDesc);
    return excel.getWorkBook();
  }

	  /**
	   * 同步配置表信息（删除、修改，新增同步）
	 * @param sysQuerySetting 自定sql编辑
	 * @param fields 字段信息
	 */
	public void syncSettingAndField(QuerySetting sysQuerySetting, List<QueryField> fields,QuerySql sysQuerySql)
	  {
	    syncConditionAndField(sysQuerySetting, fields);
	    syncDisplayAndField(sysQuerySetting, fields);
	    syncManageField(sysQuerySetting,sysQuerySql);
	  }

	/**
	 * 按钮信息的删除、更新同步，按钮的信息以sys_query_sql表的为准
	 * @param sysQuerySetting
	 */
	private void syncManageField(QuerySetting sysQuerySetting,QuerySql sysQuerySql){
		if(BeanUtils.isEmpty(sysQuerySql)){
			return;
		}
		 JSONArray jsonArray = new JSONArray();
		 if (BeanUtils.isNotEmpty(sysQuerySql.getUrlParams())) {
			 jsonArray = JSONArray.fromObject(sysQuerySql.getUrlParams());
		 }
		 JSONArray buttonContain = new JSONArray();
		 if(BeanUtils.isNotEmpty(sysQuerySql.getUrlParams())){
			 for (int i = 0; i < jsonArray.size(); i++) {
			    JSONObject jsonObject = jsonArray.getJSONObject(i);
			    JSONObject newButton = setButtonRight(jsonObject,sysQuerySetting);
			    buttonContain.add(newButton);
			 }
			 sysQuerySetting.setManageField(buttonContain.toString());
		 }else{
			 sysQuerySetting.setManageField("");
		 }
		 update(sysQuerySetting);	
	}
	
	/**
	 * @param button
	 * @param sysQuerySetting
	 * @return
	 */
	private JSONObject setButtonRight(JSONObject button,QuerySetting sysQuerySetting){
		String name = button.getString("name");
		boolean noRight = true;
		if(BeanUtils.isNotEmpty(sysQuerySetting.getManageField())){
			JSONArray jsonArray = JSONArray.fromObject(sysQuerySetting.getManageField());
			for(int i = 0; i < jsonArray.size(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String name2 = jsonObject.getString("name");
				if(name.equals(name2)){
					button.accumulate("right", jsonObject.getString("right"));
					button.accumulate("paramscript", CommonTools.Obj2String(jsonObject.get("paramscript")));
					button.accumulate("prescript", CommonTools.Obj2String(jsonObject.get("prescript")));
					button.accumulate("afterscript", CommonTools.Obj2String(jsonObject.get("afterscript")));
					noRight =false;
					break;
				}
			}
		}
		/*添加默认权限*/
		if(noRight){
			button.accumulate("right", sysQueryManageFieldParseService.getDefaultManageFieldRight());
		}
		return button;
	}
  private void syncDisplayAndField(QuerySetting sysQuerySetting, List<QueryField> fields)
  {
    JSONUtils.getMorpherRegistry().registerMorpher(
      new DateMorpher(new String[] { "yyyy-MM-dd" }));

    JSONArray jsonArray = new JSONArray();

    if ((sysQuerySetting.getDisplayField() != null) && 
      (!sysQuerySetting.getDisplayField().equals("null")) && 
      (!sysQuerySetting.getDisplayField().equals(""))) {
      jsonArray = JSONArray.fromObject(sysQuerySetting.getDisplayField());
    }

    boolean change = false;
    List newJsons = new ArrayList();

    for (QueryField field : fields)
    {
      boolean exit = false;
      int exitIndex = 0;
      for (int i = 0; i < jsonArray.size(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);

        if ((jsonObject != null) && 
          (jsonObject.getLong("id") == field.getId().longValue())) {
          exitIndex = i;
          exit = true;
        }
      }

      if ((exit) && 
        (field.getIsShow().equals(QueryField.IS_NOT_SHOW))) {
        change = true;
        jsonArray.remove(exitIndex);
      }

      if ((!exit) && 
        (field.getIsShow().equals(QueryField.IS_SHOW))) {
        change = true;
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("id", field.getId());
        jsonObject.accumulate("name", field.getName());
        jsonObject.accumulate("desc", field.getFieldDesc());
        jsonObject.accumulate("right", 
          this.sysQueryDisplayFieldParseService
          .getDefaultDisplayFieldRight());

        newJsons.add(jsonObject);
      }

    }

    if (change) {
      if (!newJsons.isEmpty()) {
        jsonArray.addAll(newJsons);
      }
      sysQuerySetting.setDisplayField(jsonArray.toString());
      //System.out.println(jsonArray.toString());
      update(sysQuerySetting);
    }
  }

  private void syncConditionAndField(QuerySetting sysQuerySetting, List<QueryField> fields)
  {
    JSONUtils.getMorpherRegistry().registerMorpher(
      new DateMorpher(new String[] { "yyyy-MM-dd" }));
    JSONArray jsonArray = new JSONArray();
    if ((sysQuerySetting.getConditionField() != null) && 
      (!sysQuerySetting.getConditionField().equals("null")) && 
      (!sysQuerySetting.getConditionField().equals(""))) {
      jsonArray = JSONArray.fromObject(sysQuerySetting
        .getConditionField());
    }

    boolean change = false;
    List newJsons = new ArrayList();
    for (QueryField field : fields) {
      boolean exit = false;
      int exitIndex = 0;
      for (int i = 0; i < jsonArray.size(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        try
        {
          if ((jsonObject != null) && 
            (jsonObject.getLong("id") == field.getId().longValue())) {
            exitIndex = i;
            exit = true;
            break;
          }
        }
        catch (Exception localException) {
        }
      }
      if ((exit) && 
        (field.getIsSearch().equals(QueryField.IS_NOT_SEARCH))) {
        change = true;
        jsonArray.remove(exitIndex);
      }
      if ((!exit) && 
        (field.getIsSearch().equals(QueryField.IS_SEARCH))) {
        change = true;
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("id", field.getId());
        jsonObject.accumulate("na", field.getName());
        if (field.getType().equals("VARCHAR2"))
          jsonObject.accumulate("ty", "varchar");
        else {
          jsonObject.accumulate("ty", field.getType()
            .toLowerCase());
        }
        jsonObject.accumulate("op", "1");
        jsonObject.accumulate("cm", field.getFieldDesc());
        jsonObject.accumulate("va", "");
        jsonObject.accumulate("vf", "1");
        jsonObject.accumulate("ct", field.getControlType());
        jsonObject.accumulate("qt", "S");

        newJsons.add(jsonObject);
      }
    }
    
    if (change) {
      if (!newJsons.isEmpty()) {
        jsonArray.addAll(newJsons);
      }
      sysQuerySetting.setConditionField(jsonArray.toString());
      update(sysQuerySetting);
    }
  }

  public void delBySqlIds(Long[] lAryId)
  {
    if ((lAryId != null) && (lAryId.length > 0))
      for (Long sqlId : lAryId)
        this.dao.delBySqlId(sqlId);
  }
  
}