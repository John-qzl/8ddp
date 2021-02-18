package com.cssrc.ibms.index.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.util.RuleUtil;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.form.intf.IFormQueryService;
import com.cssrc.ibms.api.sysuser.intf.ICurrentUserService;
import com.cssrc.ibms.api.sysuser.intf.IOrgAuthService;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.IOrgAuth;
import com.cssrc.ibms.api.sysuser.model.ISysObjRights;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.page.PageList;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.index.dao.SysIndexColumnDao;
import com.cssrc.ibms.index.model.IndexColumn;
import com.cssrc.ibms.index.model.IndexColumns;
import com.cssrc.ibms.index.model.IndexTabList;
import com.cssrc.ibms.index.model.ObjectFactory;
import com.cssrc.ibms.index.model.SysIndexColumn;
import com.cssrc.ibms.init.model.Config;
import com.cssrc.ibms.system.model.GlobalType;
import com.cssrc.ibms.system.service.GlobalTypeService;
import com.cssrc.ibms.system.service.IndexService;

/**
 * 
 * @author Yangbo 2016-7-22
 *
 */
@Service
public class SysIndexColumnService extends BaseService<SysIndexColumn> {
	private static Log logger = LogFactory.getLog(SysIndexColumnService.class);

	@Resource
	private SysIndexColumnDao dao;

	@Resource
	private FreemarkEngine freemarkEngine;

	@Resource
	private ISysUserService sysUserService;

	@Resource
	private ISysRoleService sysRoleService;

	@Resource
	private IPositionService positionService;
	@Resource
	private ICurrentUserService currentUserService;
	@Resource
	private GlobalTypeService globalTypeService;
	@Resource
	private IFormQueryService formQueryService;
	@Resource
	private GroovyScriptEngine groovyScriptEngine;
	@Resource
	private IOrgAuthService orgAuthService;

	@Resource
	private IndexService indexService;

	protected IEntityDao<SysIndexColumn, Long> getEntityDao() {
		return this.dao;
	}

	private Class<?> getParameterTypes(String type) {
		Class claz = null;
		try {
			if (type.equalsIgnoreCase("string"))
				claz = String.class;
			else if (type.equalsIgnoreCase("int"))
				claz = Integer.class;
			else if (type.equalsIgnoreCase("float"))
				claz = Float.class;
			else if (type.equalsIgnoreCase("double"))
				claz = Double.class;
			else if (type.equalsIgnoreCase("byte"))
				claz = Byte.class;
			else if (type.equalsIgnoreCase("short"))
				claz = Short.class;
			else if (type.equalsIgnoreCase("long"))
				claz = Long.class;
			else if (type.equalsIgnoreCase("boolean"))
				claz = Boolean.class;
			else if (type.equalsIgnoreCase("date"))
				claz = Date.class;
			else
				claz = String.class;
		} catch (Exception localException) {
		}
		return claz;
	}

	private String parserHtml(String html, SysIndexColumn sysIndexColumn,
			PagingBean pageBean, Map<String, Object> params) {
		if (StringUtil.isEmpty(html))
			return "";
		Document doc = Jsoup.parseBodyFragment(html);
		Elements els = doc.body().children();
		if (BeanUtils.isEmpty(els))
			return doc.body().html();
		Element el = els.get(0);
		el.attr("template-alias", sysIndexColumn.getAlias());
		JSONObject json = new JSONObject();

		for (String key : params.keySet()) {
			json.accumulate(key, params.get(key));
		}
		if (BeanUtils.isNotEmpty(pageBean)) {
			json.element("currentPage", pageBean.getCurrentPage())
					.element("totalPage", pageBean.getTotalPage())
					.element("pageSize", pageBean.getPageSize());
		}
		el.attr("template-params", json.toString());
		html = doc.body().html();
		return html;
	}

	public String getHtmlById(Long id, Map<String, Object> params)
			throws Exception {
		SysIndexColumn sysIndexColumn = (SysIndexColumn) getById(id);
		return "<div template-alias=\"" + sysIndexColumn.getAlias()
				+ "\"></div>";
	}

	public SysIndexColumn getByColumnAlias(String alias) {
		return this.dao.getByColumnAlias(alias);
	}

	public String parserDesignHtml(String designHtml,
			List<SysIndexColumn> columnList) {
		if (StringUtil.isEmpty(designHtml))
			return null;
		Document doc = Jsoup.parseBodyFragment(designHtml);
		Elements els = doc.select("[template-alias]");
		for (Iterator it = els.iterator(); it.hasNext();) {
			Element el = (Element) it.next();
			String value = el.attr("template-alias");
			String h = getSysIndexColumn(value, columnList);
			Element parent = el.parent();
			el.remove();
			parent.append(h);
		}
		designHtml = doc.body().html();
		return designHtml;
	}

	public String parseTemplateHtml(SysIndexColumn sysIndexColumn,
			Map<String, Object> params) throws Exception {
		JSONObject json = parseTemplateJSON(sysIndexColumn, params);
		return json.getString("html");
	}

	private String getSysIndexColumn(String alias,
			List<SysIndexColumn> columnList) {
		for (SysIndexColumn sysIndexColumn : columnList) {
			if (alias.equals(sysIndexColumn.getAlias()))
				return sysIndexColumn.getTemplateHtml();
		}
		return "";
	}

	/**
	 * 将对应栏目数据分解并合成具有对应数据（getbean）和html元素的json
	 * 
	 * @param sysIndexColumn
	 *            首页栏目信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public JSONObject parseTemplateJSON(SysIndexColumn sysIndexColumn,
			Map<String, Object> params) throws Exception {
		String dataFrom = sysIndexColumn.getDataFrom();
		String html = sysIndexColumn.getTemplateHtml();
		Short colType = sysIndexColumn.getColType();
		short dataMode = sysIndexColumn.getDataMode().shortValue();

		String ctxPath = params.get("__ctx").toString();
		String dataParam = sysIndexColumn.getDataParam();
		Object data = null;
		try {
			Class[] parameterTypes = getParameterTypes(dataParam, params);
			// param代表传入的参数(当后续在数据库中可加入)
			Object[] param = getDataParam(dataParam, params);
			// dataMode为0代表服务方法（index.xml指定service）1代表自定义查询（动态数据）
			if (SysIndexColumn.DATA_MODE_SERVICE == dataMode) {
				data = getModelByHandler(dataFrom, param, parameterTypes);
			} else if (SysIndexColumn.DATA_MODE_QUERY == dataMode) {
				String alias = sysIndexColumn.getDataFrom();
				data = this.formQueryService.getData(alias, null,
						Integer.valueOf(1), Integer.valueOf(20));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long height = Long.valueOf(BeanUtils.isEmpty(sysIndexColumn
				.getColHeight()) ? 320L : sysIndexColumn.getColHeight()
				.longValue());
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("alias", sysIndexColumn.getAlias());
		model.put("title", sysIndexColumn.getName());
		model.put("url", sysIndexColumn.getColUrl());
		model.put("height", height);
		map.put("model", model);
		map.put("data", data);
		map.put("ctx", ctxPath);
		map.put("service", this.indexService);
		PagingBean pageBean = null;
		if (sysIndexColumn.getNeedPage().shortValue() == 1)
			pageBean = handerPageBean(data);
		map.put("pageBean", pageBean);

		html = "<#setting number_format=\"#\">" + html;
		html = this.freemarkEngine.parseByStringTemplate(map, html);
		html = parserHtml(html, sysIndexColumn, pageBean, params);

		JSONObject json = new JSONObject();

		if ((SysIndexColumn.COLUMN_TYPE_CHART == colType.shortValue())
				|| (SysIndexColumn.COLUMN_TYPE_CALENDAR == colType.shortValue())) {
			json.accumulate("option", data);
		}

		json.accumulate("isRefresh", sysIndexColumn.getSupportRefesh())
				.accumulate("refreshTime", sysIndexColumn.getRefeshTime())
				.accumulate("show", sysIndexColumn.getShowEffect())
				.accumulate("type", colType).accumulate("height", height)
				.accumulate("html", html);
		return json;
	}

	private Object[] getDataParam(String dataParam, Map<String, Object> params) {
		if ((JSONUtil.isEmpty(dataParam)) || (StringUtil.isEmpty(dataParam)))
			return null;
		JSONArray jary = JSONArray.fromObject(dataParam);
		Object[] args = new Object[jary.size()];
		for (int i = 0; i < jary.size(); i++) {
			JSONObject json = jary.getJSONObject(i);
			String name = (String) json.get("name");
			String type = (String) json.get("type");
			String mode = (String) json.get("mode");
			String value = (String) json.get("value");

			Object o = value;
			if (mode.equalsIgnoreCase("1")) {
				o = params.get(name);
				if ((JSONUtil.isEmpty(o)) && (BeanUtils.isNotEmpty(value)))
					o = value;
			} else if (mode.equalsIgnoreCase("2")) {
				o = this.groovyScriptEngine.executeString(value, params);
			}
			Object val = StringUtil.parserObject(o, type);

			args[i] = val;
		}
		return args;
	}

	/**
	 * 找到对应bean的方法（rtn=0则说明有此bean，如果未找到请查看对应xml文件是否进行配置）
	 * 
	 * @param handler
	 * @param args
	 * @param parameterTypes
	 * @return
	 * @throws Exception
	 */
	private Object getModelByHandler(String handler, Object[] args,
			Class<?>[] parameterTypes) throws Exception {
		Object model = null;
		if (StringUtil.isEmpty(handler))
			return model;
		int rtn = RuleUtil.isHandlerValidNoCmd(handler, parameterTypes);
		if (rtn != 0)
			return model;
		String[] aryHandler = handler.split("[.]");
		if (aryHandler == null)
			return model;
		String beanId = aryHandler[0];
		String method = aryHandler[1];

		Object serviceBean = AppUtil.getBean(beanId);

		if (serviceBean == null)
			return model;
		if ((args == null) || (args.length <= 0)) {
			parameterTypes = new Class[0];
		}
		Method invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
				parameterTypes);
		// 执行serviceBean的method的对应方法
		model = invokeMethod.invoke(serviceBean, args);
		if (BeanUtils.isEmpty(model))
			model = null;
		return model;
	}

	private Class<?>[] getParameterTypes(String dataParam,
			Map<String, Object> params) {
		if ((JSONUtil.isEmpty(dataParam)) || (StringUtil.isEmpty(dataParam)))
			return new Class[0];
		JSONArray jary = JSONArray.fromObject(dataParam);
		Class[] parameterTypes = new Class[jary.size()];
		for (int i = 0; i < jary.size(); i++) {
			JSONObject json = jary.getJSONObject(i);
			String type = (String) json.get("type");

			parameterTypes[i] = getParameterTypes(type);
		}
		return parameterTypes;
	}

	private PagingBean handerPageBean(Object data) {
		PagingBean pageBean = null;
		try {
			if ((data instanceof PageList)) {
				PageList pageList = (PageList) data;
				pageBean = pageList.getPageBean();
			} else if ((data instanceof IndexTabList)) {
				IndexTabList indexTablist = (IndexTabList) data;
				pageBean = getIndexTabPageBean(indexTablist);
			}
		} catch (Exception localException) {
		}
		return pageBean;
	}

	private PagingBean getIndexTabPageBean(IndexTabList indexTablist) {
		if (BeanUtils.isEmpty(indexTablist))
			return null;
		return indexTablist.getPageBean();
	}

	public Boolean isExistAlias(String alias, Long id) {
		if ((id == null) || (id.longValue() == 0L))
			id = null;
		return this.dao.isExistAlias(alias, id);
	}

	public List<SysIndexColumn> getDefaultList() {
		return this.dao.getBySqlKey("defaultList");
	}

	public List<SysIndexColumn> getHashRightColumnList(QueryFilter filter,
			Map<String, Object> params, Boolean isParse) throws Exception {
		List list;
		// 这里有用户权限的判断，除最高用户无权对首页栏目更改。此处通用（后期完善之）
		if (UserContextUtil.isSuperAdmin())
			list = this.dao.getAll(filter);
		else {
			list = getByUserIdFilter();
		}
		if (isParse.booleanValue())
			parseList(list, params);
		return list;
	}

	private void parseList(List<SysIndexColumn> list, Map<String, Object> params)
			throws Exception {
		if (BeanUtils.isEmpty(list))
			return;
		for (SysIndexColumn sysIndexColumn : list) {
			String templateHtml = parseTemplateHtml(sysIndexColumn, params);
			sysIndexColumn.setTemplateHtml(templateHtml);
		}
	}

	/**
	 * 上下级组织获取
	 * 
	 * @return
	 */
	public List<SysIndexColumn> getByUserIdFilter() {
		Map<String, List<Long>> map = this.currentUserService.getUserRelation();
		List<? extends IOrgAuth> orgAuthList = this.orgAuthService
				.getByUserId(UserContextUtil.getCurrentUserId().longValue());
		List authOrgIdList = new ArrayList();
		if (BeanUtils.isNotEmpty(orgAuthList)) {
			for (IOrgAuth orgAuth : orgAuthList) {
				authOrgIdList.add(orgAuth.getOrgId());
			}
			map.put("auth_org", authOrgIdList);
		}
		Map params = new HashMap();
		params.put("relationMap", map);
		params.put("objType", ISysObjRights.RIGHT_TYPE_INDEX_COLUMN);
		return this.dao.getByUserIdFilter(params);
	}

	public Map<String, List<SysIndexColumn>> getColumnMap(
			List<SysIndexColumn> columnList) {
		List<GlobalType> catalogList = this.globalTypeService.getByCatKey(
				GlobalType.CAT_INDEX_COLUMN, false);
		Map map = new LinkedHashMap();
		Long nullCatalog = Long.valueOf(0L);
		Long catalog;
		for (SysIndexColumn sysIndexColumn : columnList) {
			catalog = sysIndexColumn.getCatalog();
			if (BeanUtils.isEmpty(catalog))
				catalog = nullCatalog;
			List list = (List) map.get(catalog);

			if (BeanUtils.isEmpty(list))
				list = new ArrayList();
			list.add(sysIndexColumn);
			map.put(catalog, list);
		}

		Map map1 = new LinkedHashMap();
		for (GlobalType globalType : catalogList) {
			Long typeId = globalType.getTypeId();
			String name = globalType.getTypeName();
			List list = (List) map.get(typeId);
			if (BeanUtils.isNotEmpty(list)) {
				map1.put(name, list);
			}
		}
		List list = (List) map.get(nullCatalog);
		if (BeanUtils.isNotEmpty(list))
			map1.put("默认栏目", list);
		return map1;
	}

	/**
	 * 以json形式返回
	 * 
	 * @param sysIndexColumn
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String parseTemplateHtmlJSON(SysIndexColumn sysIndexColumn,
			Map<String, Object> params) throws Exception {
		JSONObject json = parseTemplateJSON(sysIndexColumn, params);
		return json.toString();
	}

	/**
	 * 通过栏目别名获得对应栏目数据，转译json(包含data和html)parseTemplateHtmlJSON
	 * 
	 * @param alias
	 *            栏目别名
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String getHtmlByColumnAlias(String alias, Map<String, Object> params)
			throws Exception {
		SysIndexColumn sysIndexColumn = getByColumnAlias(alias);
		if (BeanUtils.isEmpty(sysIndexColumn))
			return "";
		return parseTemplateHtmlJSON(sysIndexColumn, params);
	}

	/**
	 * 首页栏目初始化
	 */
	public void initIndex() {
		SysIndexColumnService sysIndexColumnService = (SysIndexColumnService) AppUtil
				.getBean(SysIndexColumnService.class);
		try {
			sysIndexColumnService.init();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	public void init() throws Exception {
		//Integer amount = this.dao.getCounts();
		// if (amount.intValue() == 0)
		initIndexColumn();
	}

	/**
	 * 构建初始化html
	 * 
	 * @throws Exception
	 */
	public void initIndexColumn() throws Exception {
		String templatePath = SysConfConstant.FTL_ROOT+"index"+File.separator;
		String path = templatePath+  "index.xml";
		try {
			List list = geneIndexColumns(path);
			for (int i = 0; i < list.size(); i++) {
				IndexColumn indexColumn = (IndexColumn) list.get(i);
				SysIndexColumn sysIndexColumn = new SysIndexColumn();
				BeanUtils.copyNotNullProperties(sysIndexColumn, indexColumn);
				sysIndexColumn.setId(Long.valueOf(i + 1L));
				String fileName = indexColumn.getAlias() + ".ftl";
				String templateHtml = FileUtil.readFile(templatePath
						+ "templates" + File.separator + fileName);
				sysIndexColumn.setTemplateHtml(templateHtml);
				SysIndexColumn column = this.dao
						.getByColumnAlias(sysIndexColumn.getAlias());
				if (BeanUtils.isNotEmpty(column))
					this.dao.delById(column.getId());
				this.dao.add(sysIndexColumn);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	// 从配置文件中读取生成indexcolumn
	public List geneIndexColumns(String path) {

		List<IndexColumn> indexColumns = new ArrayList<IndexColumn>();
		try {
			org.dom4j.Document doc = Dom4jUtil
					.loadXml(new FileInputStream(path));

			org.dom4j.Element root = doc.getRootElement();

			List<org.dom4j.Element> elements = root.elements();

			for (org.dom4j.Element ele : elements) {
				IndexColumn indexColumn = new IndexColumn();

				indexColumn.setAlias(ele.attributeValue("alias"));
				indexColumn.setName(ele.attributeValue("name"));
				indexColumn.setColType((Short.valueOf(ele
						.attributeValue("colType"))));
				indexColumn.setDataMode(Short.valueOf(ele
						.attributeValue("dataMode")));
				indexColumn.setDataFrom(ele.attributeValue("dataFrom"));
				indexColumn.setColUrl(ele.attributeValue("colUrl"));
				indexColumn.setIsPublic(Short.valueOf(ele
						.attributeValue("isPublic")));
				indexColumn.setSupportRefesh(Short.valueOf(ele
						.attributeValue("supportRefesh")));
				indexColumn.setShowEffect(Short.valueOf(ele
						.attributeValue("showEffect")));

				indexColumns.add(indexColumn);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return indexColumns;
	}

}
