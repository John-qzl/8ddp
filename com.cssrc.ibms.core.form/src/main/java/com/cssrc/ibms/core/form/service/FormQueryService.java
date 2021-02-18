package com.cssrc.ibms.core.form.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormQueryService;
import com.cssrc.ibms.api.form.model.IFormQuery;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.util.JdbcHelperUtil;
import com.cssrc.ibms.core.form.dao.FormQueryDao;
import com.cssrc.ibms.core.form.model.DialogField;
import com.cssrc.ibms.core.form.model.FormQuery;
import com.cssrc.ibms.core.form.model.QueryResult;
import com.cssrc.ibms.core.form.util.SqlUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.file.XmlUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;


/**
 * 对象功能:通用表单查询 Service类 
 * 开发人员:zhulongchao
 */
@Service
public class FormQueryService extends BaseService<FormQuery> implements IFormQueryService{
	@Resource
	private FormQueryDao dao;

	public FormQueryService() {
	}

	@Override
	protected IEntityDao<FormQuery, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 根据别名获取对话框对象。
	 * 
	 * @param alias
	 * @return
	 */
	public FormQuery getByAlias(String alias) {
		return dao.getByAlias(alias);
	}

	/**
	 * 检查别名是否唯一
	 * 
	 * @param alias
	 * @return
	 */
	public boolean isExistAlias(String alias) {
		return dao.isExistAlias(alias) > 0;
	}

	/**
	 * 检查别名是否唯一。
	 * 
	 * @param alias
	 * @return
	 */
	public boolean isExistAliasForUpd(Long id, String alias) {
		return dao.isExistAliasForUpd(id, alias) > 0;
	}

	/**
	 * 根据别名获取对应查询的数据
	 * 
	 * @param bpmFormQuery
	 *            表单查询对象
	 * @param params
	 *            参数
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页记录条数
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public QueryResult getData(FormQuery bpmFormQuery, String queryData,
			Integer page, Integer pageSize) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(queryData)) {
			JSONObject jsonObj = JSONObject.fromObject(queryData);
			Iterator<?> it = jsonObj.keys();
			while (it.hasNext()) {
				String key = it.next().toString();
				String value = jsonObj.getString(key);
				params.put(key, value);
			}
		}

		JdbcHelper<Map<String, Object>, ?> jdbcHelper = JdbcHelperUtil
				.getJdbcHelper(bpmFormQuery.getDsalias());

		List<DialogField> resultList = bpmFormQuery.getReturnList();
		List<DialogField> conditionList = bpmFormQuery.getConditionList();
		String objectName = bpmFormQuery.getObjName();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("objectName", objectName);
		map.put("returnList", resultList);
		map.put("conditionList", conditionList);
		map.put("sortList", bpmFormQuery.getSortList());
		QueryResult queryResult = new QueryResult();
		// 是否需要分页。
		if (page > 0 && pageSize > 0) {
			String sql = SqlUtil.getSql(map, params);
			PagingBean pageBean = new PagingBean(page, pageSize);
			List<Map<String, Object>> list = jdbcHelper.getPage(page, pageSize,
					sql, params, pageBean);
			list = handList(list);
			queryResult.setList(list);
			queryResult.setIsPage(1);
			queryResult.setPage(page);
			queryResult.setPageSize(pageSize);
			int totalCount = pageBean.getTotalCount();
			int totalPage = pageBean.getTotalPage();
			queryResult.setTotalCount(totalCount);
			queryResult.setTotalPage(totalPage);
		} else {
			String sql = SqlUtil.getSql(map, params);
			List<Map<String, Object>> list = jdbcHelper.queryForList(sql,
					params);
			list = handList(list);
			queryResult.setList(list);
			queryResult.setTotalCount(list.size());
		}
		return queryResult;
	}

	/**
	 * 处理list
	 * 
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> handList(List<Map<String, Object>> list) {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) list.get(i);
			Map<String, Object> rtnMap = handMap(map);
			rtnList.add(rtnMap);
		}
		return rtnList;
	}

	/**
	 * 处理Map
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> handMap(Map<String, Object> map) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			// 把数据转换成小写
			String key = entry.getKey().toLowerCase();
			Object obj = entry.getValue();
			if (obj == null) {
				rtnMap.put(key, "");
				continue;
			}
			// 对时间字段单独处理。
			if (obj instanceof Date) {
				String format = "yyyy-MM-dd HH:mm:ss";
				String str = TimeUtil.getDateTimeString((Date) obj, format);
				rtnMap.put(key, str);
			} else {
				rtnMap.put(key, obj);
			}
		}
		return rtnMap;
	}

	public String exportXml(List<FormQuery> bpmFormQueries) throws Exception {
		FormQueryXmlList bpmFormQueryXmlList = new FormQueryXmlList();
		List list = new ArrayList();
		for (FormQuery bpmFormQuery : bpmFormQueries) {
			FormQueryXml bpmFormQueryXml = exportBpmFormQueryXml(bpmFormQuery);
			list.add(bpmFormQueryXml);
		}
		bpmFormQueryXmlList.setBpmFormQueryXmlList(list);
		return XmlBeanUtil
				.marshall(bpmFormQueryXmlList, FormQueryXmlList.class);
	}

	private FormQueryXml exportBpmFormQueryXml(FormQuery bpmFormQuery)
			throws Exception {
		FormQueryXml bpmFormQueryXml = new FormQueryXml();
		Long id = bpmFormQuery.getId();
		if (BeanUtils.isNotEmpty(id)) {
			bpmFormQueryXml.setBpmFormQuery(bpmFormQuery);
		}

		return bpmFormQueryXml;
	}

	public String exportXml(Long[] tableIds) throws Exception {
		FormQueryXmlList bpmFormQueryXmlList = new FormQueryXmlList();
		List list = new ArrayList();
		for (int i = 0; i < tableIds.length; i++) {
			FormQuery bpmFormQuery = (FormQuery) this.dao.getById(tableIds[i]);
			FormQueryXml bpmFormQueryXml = exportBpmFormQueryXml(bpmFormQuery);
			list.add(bpmFormQueryXml);
		}
		bpmFormQueryXmlList.setBpmFormQueryXmlList(list);
		return XmlBeanUtil
				.marshall(bpmFormQueryXmlList, FormQueryXmlList.class);
	}

	public void importXml(InputStream inputStream) throws Exception {
		Document doc = Dom4jUtil.loadXml(inputStream);
		Element root = doc.getRootElement();

		XmlUtil.checkXmlFormat(root, "bpm", "formQuerys");

		String xmlStr = root.asXML();
		FormQueryXmlList bpmFormQueryXmlList = (FormQueryXmlList) XmlBeanUtil
				.unmarshall(xmlStr, FormQueryXmlList.class);

		List<FormQueryXml> list = bpmFormQueryXmlList.getBpmFormQueryXmlList();

		for (FormQueryXml bpmFormQueryXml : list) {
			importBpmFormQueryXml(bpmFormQueryXml);
		}
	}

	private void importBpmFormQueryXml(FormQueryXml bpmFormQueryXml)
			throws Exception {
		Long queryId = Long.valueOf(UniqueIdUtil.genId());
		FormQuery bpmFormQuery = bpmFormQueryXml.getBpmFormQuery();
		if (BeanUtils.isEmpty(bpmFormQuery)) {
			throw new Exception();
		}
		String alias = bpmFormQuery.getAlias();
		FormQuery query = this.dao.getByAlias(alias);
		if (BeanUtils.isNotEmpty(query)) {
			MsgUtil.addMsg(2, "别名为‘" + alias + "’的自定义查询已经存在，请检查你的xml文件！");
			return;
		}
		bpmFormQuery.setId(queryId);
		this.dao.add(bpmFormQuery);
		MsgUtil.addMsg(1, "别名为" + alias + "的自定义查询导入成功！");
	}

	@Override
	public QueryResult getData(String alias, String queryData, Integer page,
			Integer pageSize) throws Exception {
		FormQuery formQuery = this.getByAlias(alias);
		return this.getData(formQuery, queryData, page, pageSize);
	}

}
