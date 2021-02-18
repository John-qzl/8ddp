package com.cssrc.ibms.core.form.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IQuerySqlService;
import com.cssrc.ibms.api.form.model.IQuerySql;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.form.dao.QuerySqlDao;
import com.cssrc.ibms.core.form.model.QueryField;
import com.cssrc.ibms.core.form.model.QuerySetting;
import com.cssrc.ibms.core.form.model.QuerySql;
import com.cssrc.ibms.core.form.xml.form.SysQuerySqlDefXml;
import com.cssrc.ibms.core.form.xml.form.SysQuerySqlDefXmlList;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.XmlUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;

@Service
public class QuerySqlService extends BaseService<QuerySql> implements IQuerySqlService{

	@Resource
	private QuerySqlDao dao;
	@Resource
	QueryFieldService queryFieldService;
	@Resource
	QuerySettingService querySettingService;
	@Resource
	JdbcDao jdbcDao;

	protected IEntityDao<QuerySql, Long> getEntityDao() {

		return this.dao;
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

	public List<QuerySql> getAll(QueryFilter queryFilter) {
		return super.getAll(queryFilter);
	}

	public QuerySql getSysQuerySql(String json) {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd" }));
		if (StringUtil.isEmpty(json))
			return null;
		JSONObject obj = JSONObject.fromObject(json);
		QuerySql sysQuerySql = (QuerySql) JSONObject
				.toBean(obj, QuerySql.class);
		return sysQuerySql;
	}

	public void importXml(InputStream inputStream) throws Exception {
		Document doc = Dom4jUtil.loadXml(inputStream);
		Element root = doc.getRootElement();

		XmlUtil.checkXmlFormat(root, "querySqls", "querySqlDefs");

		String xmlStr = root.asXML();
		SysQuerySqlDefXmlList sysQuerySqlDefXmlList = (SysQuerySqlDefXmlList) XmlBeanUtil
				.unmarshall(xmlStr, SysQuerySqlDefXmlList.class);

		List<SysQuerySqlDefXml> list = sysQuerySqlDefXmlList
				.getSysQuerySqlDefXmlList();

		for (SysQuerySqlDefXml sysQuerySqlDefXml : list) {
			importSysQuerySqlDefXml(sysQuerySqlDefXml);
		}
	}

	private void importSysQuerySqlDefXml(SysQuerySqlDefXml sysQuerySqlDefXml)
			throws Exception {
		Long sqlId = Long.valueOf(UniqueIdUtil.genId());
		QuerySql sqlDef = sysQuerySqlDefXml.getQuerySql();

		if (BeanUtils.isEmpty(sqlDef)) {
			throw new Exception();
		}

		String alias = sqlDef.getAlias();
		QuerySql querySqlDef = this.dao.getByAlias(alias);
		if (BeanUtils.isNotEmpty(querySqlDef)) {
			MsgUtil.addMsg(2, "别名为‘" + alias + "’的Sql查询已经存在，没有导入，请检查你的xml文件！");
			return;
		}
		sqlDef.setId(sqlId);
		// queryFieldList 保存
		List<QueryField> queryFieldList = sysQuerySqlDefXml.getQueryFieldList();

		if (BeanUtils.isNotEmpty(queryFieldList)) {
			for (QueryField queryField : queryFieldList) {
				Long metaFieldId = Long.valueOf(UniqueIdUtil.genId());
				queryField.setId(metaFieldId);
				queryField.setSqlId(sqlId);
				this.queryFieldService.add(queryField);
			}
			sqlDef.setQueryFields(queryFieldList);
		}
		// querySettingList 保存
		List<QuerySetting> querySettingList = sysQuerySqlDefXml
				.getQuerySettingList();

		if (BeanUtils.isNotEmpty(querySettingList)) {
			for (QuerySetting querySetting : querySettingList) {
				Long viewId = Long.valueOf(UniqueIdUtil.genId());
				querySetting.setId(viewId);
				querySetting.setAlias(alias);
				querySetting.setSqlId(sqlId);
				this.querySettingService.add(querySetting);
			}
			sqlDef.setQuerySettingList(querySettingList);
		}

		this.dao.add(sqlDef);
		MsgUtil.addMsg(1, "别名为" + alias + "的自定义Sql导入成功！");
	}

	public String exportXml(Long[] tableIds) throws Exception {
		List querySqls = new ArrayList();
		for (int i = 0; i < tableIds.length; i++) {
			QuerySql querySql = (QuerySql) this.dao.getById(tableIds[i]);
			querySqls.add(querySql);
		}
		return exportXml(querySqls);
	}

	public String exportXml(List<QuerySql> querySqls) throws Exception {
		SysQuerySqlDefXmlList sysQuerySqlDefXmls = new SysQuerySqlDefXmlList();
		List list = new ArrayList();
		for (QuerySql querySql : querySqls) {
			SysQuerySqlDefXml sysQuerySqlDefXml = exportSysQuerySqlDefXml(querySql);
			list.add(sysQuerySqlDefXml);
		}
		sysQuerySqlDefXmls.setSysQuerySqlDefXmlList(list);
		return XmlBeanUtil.marshall(sysQuerySqlDefXmls,
				SysQuerySqlDefXmlList.class);
	}

	private SysQuerySqlDefXml exportSysQuerySqlDefXml(QuerySql sysQuerySqlDef)
			throws Exception {
		SysQuerySqlDefXml sysQuerySqlDefXml = new SysQuerySqlDefXml();

		Long id = sysQuerySqlDef.getId();
		String alias = sysQuerySqlDef.getAlias();

		List<QueryField> queryFieldList = this.queryFieldService
				.getListBySqlId(id);

		QuerySetting querySetting = this.querySettingService.getBySqlId(id);
		List<QuerySetting> querySettingList = new ArrayList();
		querySettingList.add(querySetting);

		sysQuerySqlDefXml.setQuerySql(sysQuerySqlDef);

		if (BeanUtils.isNotEmpty(queryFieldList)) {
			sysQuerySqlDefXml.setQueryFieldList(queryFieldList);
		}
		if (BeanUtils.isNotEmpty(querySettingList)) {
			sysQuerySqlDefXml.setQuerySettingList(querySettingList);
		}

		return sysQuerySqlDefXml;
	}
	/**
	 * 根据别名获取查询对象。
	 * 
	 * @param alias
	 *        查询别名。
	 * @return
	 */
	@Override
	public IQuerySql getByAlias(String alias) {
		return this.dao.getByAlias(alias);
	}
	
	
    /** 
    * @Title: validSql 
    * @Description: TODO(校验sql语句的有效性，必须回滚) 
    * @param @param sql
    * @param @return     
    * @return Boolean    返回类型 
    * @throws 
    */
    public Boolean handSql(String sql) {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        try
        {
            jdbcDao.exesql(sql,new HashMap<>());
            return true;
        }
        catch (Exception e)
        {
            // TODO: handle exception
            return false;
        }
    }
    
    // 验证sql语句，通过rollback判断是否回滚
    public Boolean validSql(String sql, boolean rollback)
    {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        if (!rollback)
        {
            jdbcDao.queryForList(sql,new HashMap<>());
            return Boolean.valueOf(true);
        }
        return true;
    }
    
    
}