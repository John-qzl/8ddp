package com.cssrc.ibms.system.service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.ISysDataSourceDefService;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.system.model.ISysDataSourceDef;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.XmlUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.system.dao.SysDataSourceDefDao;
import com.cssrc.ibms.system.model.SysDataSourceDef;
import com.cssrc.ibms.system.xml.SysDataSourceDefXml;
import com.cssrc.ibms.system.xml.SysDataSourceDefXmlList;

/**
 * SysDataSourceDefService
 * @author liubo
 * @date 2017年4月14日
 */
@Service
public class SysDataSourceDefService extends BaseService<SysDataSourceDef> implements ISysDataSourceDefService{
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(SysDataSourceService.class);
	
	@Resource
	private SysDataSourceDefDao dao;
	
	@Resource
	private SysDataSourceService service;
	
	protected IEntityDao<SysDataSourceDef, Long> getEntityDao(){
		return this.dao;
	}
	
	/**
	 * 根据数据源别名获取数据源信息
	 * @param alias
	 * @return
	 */
	public SysDataSourceDef getByAlias(String alias){
		return (SysDataSourceDef)this.dao.getUnique("getByAlias", alias);
	}
	
	/**
	 * 通过数据源信息检验是否能够连接
	 * @param sysDataSourceDef
	 * @return
	 */
	public boolean checkConnection(ISysDataSourceDef sysDataSourceDef){
		return checkConnection(getDsFromSysSource(sysDataSourceDef), sysDataSourceDef.getCloseMethod());
	}
	      
	private boolean checkConnection(DataSource dataSource, String closeMethod) {
		boolean b = false;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			b = true;
		}
		catch (Exception e) {
			if (!StringUtil.isEmpty(closeMethod)) {
				String cp = closeMethod.split("\\|")[0];
				String mn = closeMethod.split("\\|")[1];
				try {
					Class _class = Class.forName(cp);
					Method method = _class.getMethod(mn, null);
					method.invoke(null, null);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
	      
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}
		finally{
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	      
		return b;
	}
	  
	/**
	 * 通过数据源别名获取数据源信息
	 * @param alias
	 * @return
	 */	
	public ISysDataSource getSysDataSource(String alias){
		try {
			SysDataSourceDef sysDataSourceDef = getByAlias(alias);
			ISysDataSource sysDataSource = service.getSysDataSourceByDef(sysDataSourceDef);
			return sysDataSource;
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 通过系统配置的数据源返回JDBC数据源
	 * @param sysDataSourceDef
	 * @return
	 */	
	public DataSource getDsFromSysSource(ISysDataSourceDef sysDataSourceDef){
		try{
			Class _class = null;
			_class = Class.forName(sysDataSourceDef.getClassPath());
			DataSource sqldataSource = null;
			sqldataSource = (DataSource)_class.newInstance();
	      
			String settingJson = sysDataSourceDef.getSettingJson();
			JSONArray ja = JSONArray.fromObject(settingJson);
	      
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				Object value = BeanUtils.convertByActType(jo.getString("type"), jo.getString("value"));
				BeanUtils.setProperty(sqldataSource, jo.getString("name"), value);
			}
	      
			String initMethodStr = sysDataSourceDef.getInitMethod();
			if (!StringUtil.isEmpty(initMethodStr)) {
				Method method = _class.getMethod(initMethodStr, new Class[0]);
				method.invoke(sqldataSource, new Object[0]);
			}
	      
			return sqldataSource;
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
		}
	      
		return null;
	}
	/**
	 * 查询系统所有的数据源信息
	 * @return
	 */		      
	public List<SysDataSourceDef> getAllAndDefault(){
		List list = super.getAll();
	      
		SysDataSourceDef defaultSysDataSourceDef = new SysDataSourceDef();
		defaultSysDataSourceDef.setAlias("LOCAL");
		defaultSysDataSourceDef.setName("本地数据源");
		list.add(0, defaultSysDataSourceDef);
		return list;
	}
	
	/**
	 * 导出数据源信息
	 * @param sysDataSources
	 * @return
	 */		      
	public String exportXml(List<SysDataSourceDef> sysDataSources) throws Exception {
		SysDataSourceDefXmlList sysDataSourceDefXmlList = new SysDataSourceDefXmlList();
		List list = new ArrayList();
		for (SysDataSourceDef sysDataSourceDef : sysDataSources) {
			SysDataSourceDefXml sysDataSourceDefXml = exportsysDataSource(sysDataSourceDef);
			list.add(sysDataSourceDefXml);
		}
		sysDataSourceDefXmlList.setSysDataSourceDefXmlList(list);
		return XmlBeanUtil.marshall(sysDataSourceDefXmlList, SysDataSourceDefXmlList.class);
	}
	      
	public String exportXml(Long[] tableIds) throws Exception {
		SysDataSourceDefXmlList sysDataSourceDefXmlList = new SysDataSourceDefXmlList();
		List list = new ArrayList();
		for (int i = 0; i < tableIds.length; i++) {
			SysDataSourceDef sysDataSourceDef = (SysDataSourceDef)this.dao.getById(tableIds[i]);
			SysDataSourceDefXml sysDataSourceDefXml = exportsysDataSource(sysDataSourceDef);
			list.add(sysDataSourceDefXml);
		}
		sysDataSourceDefXmlList.setSysDataSourceDefXmlList(list);
		return XmlBeanUtil.marshall(sysDataSourceDefXmlList, SysDataSourceDefXmlList.class);
	}
	      
	private SysDataSourceDefXml exportsysDataSource(SysDataSourceDef sysDataSourceDef) throws Exception {
		SysDataSourceDefXml sysDataSourceDefXml = new SysDataSourceDefXml();
		Long id = sysDataSourceDef.getId();
		if (BeanUtils.isNotIncZeroEmpty(id)) {
			sysDataSourceDefXml.setSysDataSourceDef(sysDataSourceDef);
		}
		return sysDataSourceDefXml;
	}

	/**
	 * 导入数据源信息
	 * @param inputStream
	 * @return
	 */	
	public void importXml(InputStream inputStream) throws Exception {
		Document doc = Dom4jUtil.loadXml(inputStream);
		Element root = doc.getRootElement();
	      
		XmlUtil.checkXmlFormat(root, "system", "dataSources");
		String xmlStr = root.asXML();
		SysDataSourceDefXmlList sysDataSourceDefXmlList = (SysDataSourceDefXmlList)XmlBeanUtil.unmarshall(xmlStr, SysDataSourceDefXmlList.class);
		List<SysDataSourceDefXml> list = sysDataSourceDefXmlList.getSysDataSourceDefXmlList();
		for (SysDataSourceDefXml sysDataSourceDefXml : list) {
			importSysDataSourceXml(sysDataSourceDefXml);
		}
	}
	      
	private void importSysDataSourceXml(SysDataSourceDefXml sysDataSourceDefXml) throws Exception {
		Long id = Long.valueOf(UniqueIdUtil.genId());
		SysDataSourceDef sysDataSourceDef = sysDataSourceDefXml.getSysDataSourceDef();
		if (BeanUtils.isEmpty(sysDataSourceDef)) {
			throw new Exception();
		}
	      
		String alias = sysDataSourceDef.getAlias();
	      
		sysDataSourceDef.setId(id);
		this.dao.add(sysDataSourceDef);
		MsgUtil.addMsg(1, "别名为" + alias + "的数据源导入成功！");
	}
	
}
