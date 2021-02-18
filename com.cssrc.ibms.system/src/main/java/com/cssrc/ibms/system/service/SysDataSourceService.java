package com.cssrc.ibms.system.service;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceService;
import com.cssrc.ibms.api.system.model.ISysDataSourceDef;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.dao.SysDataSourceDao;
import com.cssrc.ibms.system.model.SysDataSource;
import com.cssrc.ibms.system.model.SysDataSourceDef;

/**
 * 对象功能:系统数据源管理 Service类  
 * 开发人员:zhulongchao  
 */
@Service
public class SysDataSourceService extends BaseService<SysDataSource> implements ISysDataSourceService{
	@Resource
	private SysDataSourceDao dao;
	
	public SysDataSourceService() {
	}

	@Override
	protected IEntityDao<SysDataSource, Long> getEntityDao() {
		return dao;
	}
	
	/**
	 * 根据id集合删除数据源。
	 */
	@Override
	public void delByIds(Long[] ids){
		if(BeanUtils.isEmpty(ids)) return;
		for (Long p : ids){
			SysDataSource sysDataSource = dao.getById(p);
			//删除JdbcHelper中的数据源。
			JdbcHelper.getInstance().removeAlias(sysDataSource.getAlias());
			delById(p);
		}
	}

	/**
	 * 测试数据源是否可以连接
	 * 
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> testConnectById(Long[] ids) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (long id : ids) {
			SysDataSource sysDataSource = dao.getById(id);
			result.addAll(testConnectByForm(sysDataSource));
		}

		return result;
	}

	/**
	 * 测试数据源是否可以连接
	 * 
	 * @param sysDataSource
	 * @return
	 */
	public List<Map<String, Object>> testConnectByForm(SysDataSource sysDataSource) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		// 连接信息
		Map<String, Object> connectResult = new HashMap<String, Object>();
		connectResult.put("name", sysDataSource.getName());
		try {
			Class.forName(sysDataSource.getDriverName());
			// 尝试连接
			DriverManager.getConnection(sysDataSource.getUrl(), sysDataSource.getUserName(),sysDataSource.getPassword()).close();
			// 连接成功
			connectResult.put("success", true);
		} catch (ClassNotFoundException e) {
			// 连接失败
			connectResult.put("msg", "ClassNotFoundException: " + sysDataSource.getDriverName());
			connectResult.put("success", false);
		} catch (SQLException e) {
			// 连接失败
			connectResult.put("msg", e.getMessage());
			connectResult.put("success", false);
		}
		result.add(connectResult);

		return result;
	}
	
	/**
	 * 根据别名获取数据源
	 * @param alias
	 * @return
	 */
	public SysDataSource getByAlias(String alias){
		return dao.getByAlias(alias);
	}
	
	/**
	 * 别名是否已存在
	 * @param alias
	 * @return
	 */
	public boolean isAliasExisted(String alias) {
		return dao.isAliasExisted(alias);
	}
	
	/**
	 * 更新的别名是否已存在
	 * @param sysDataSource
	 * @return
	 */
	public boolean isAliasExistedByUpdate(SysDataSource sysDataSource) {
		return dao.isAliasExistedByUpdate(sysDataSource);
	}
	
	/**
	 * 通过数据源别名获取数据源管理类
	 * @param alias
	 * @return
	 */
	public DriverManagerDataSource getDriverMangerDataSourceByAlias(String dsAlias){
		return dao.getDriverMangerDataSourceByAlias(dsAlias);
	}
	
    /**
     * 获取系统名称
     * @return
     */
    @Override
    public String getAppName()
    {
        SysDataSource sysDataSource = AppUtil.getBean(SysDataSource.class);
        return sysDataSource.getAppName();
    }
    
    /**
     * 获取表空间
     * @return
     */
    @Override
    public String getTableSpace()
    {
        SysDataSource sysDataSource = AppUtil.getBean(SysDataSource.class);
        return sysDataSource.getTableSpace();
    }
    
    /**
     * 获取 dbType
     * @return
     */
    @Override
    public String getJdbcType()
    {
        SysDataSource sysDataSource = AppUtil.getBean(SysDataSource.class);
        return sysDataSource.getDbType();
    }
    
	/**
	 * 获取数据源
	 * @author liubo
	 * @param sysDataSource
	 * @return
	 */
	public DataSource getDsFromSysSource(SysDataSource sysDataSource) {
		try {
			Class _class = null;
			_class = Class.forName(sysDataSource.getClassPath());
			DataSource sqldataSource = null;
			sqldataSource = (DataSource)_class.newInstance();
 
			String settingJson = sysDataSource.getSettingJson();
			JSONArray ja = JSONArray.fromObject(settingJson);
 
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				Object value = BeanUtils.convertByActType(jo.getString("type"), jo.getString("value"));
				BeanUtils.setProperty(sqldataSource, jo.getString("name"), value);
			}
 
			String initMethodStr = sysDataSource.getInitMethod();
			if (!StringUtil.isEmpty(initMethodStr)) {
				Method method = _class.getMethod(initMethodStr, new Class[0]);
				method.invoke(sqldataSource, new Object[0]);
			}
 
			return sqldataSource;
		} catch (Exception e) {
			//LOGGER.debug(e.getMessage());
		}
 
		return null;
	}
    
	/**
	 * 检验连接情况
	 * @author liubo
	 * @param sysDataSource
	 * @return
	 */
	public boolean checkConnection(SysDataSource sysDataSource) {
		return checkConnection(getDsFromSysSource(sysDataSource), sysDataSource.getCloseMethod());
	}
    
	private boolean checkConnection(DataSource dataSource, String closeMethod) {
		boolean b = false;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			b = true;
		} catch (Exception e) {
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
		} finally {
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
	 * 获取系统所有的数据源（包括 本地数据源）
	 * @author liubo
	 * @return
	 */
	public List<SysDataSource> getAllAndDefault() {
		List list = super.getAll();
		SysDataSource defaultSysDataSource = new SysDataSource();
		defaultSysDataSource.setAlias("dataSource_Default");
		defaultSysDataSource.setName("本地数据源");
		list.add(0, defaultSysDataSource);
		return list;
	}
	
	
	/**
	 * 获取系统所有的数据源（包括 本地数据源）
	 * @author liubo
	 * @return
	 */
	public SysDataSource getSysDataSourceByDef(ISysDataSourceDef sysDataSourceDef) {
		SysDataSource ds=new SysDataSource();
		ds.setAlias(sysDataSourceDef.getAlias());
		ds.setDbType(sysDataSourceDef.getDbType());
		String settingJson = sysDataSourceDef.getSettingJson();
		JSONArray ja = JSONArray.fromObject(settingJson);
		for (int i = 0; i < ja.size(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			Object value = BeanUtils.convertByActType(jo.getString("type"), jo.getString("value"));
			BeanUtils.setProperty(sysDataSourceDef, jo.getString("name"), value);
			if("driverClassName".equals(jo.getString("name"))){
				ds.setDriverName(jo.getString("value"));
			} else if("url".equals(jo.getString("name"))){
				ds.setUrl(jo.getString("value"));
			} else if("username".equals(jo.getString("name"))){
				ds.setUserName(jo.getString("value"));
			} else if("password".equals(jo.getString("name"))){
				ds.setPassword(jo.getString("value"));
			}
		}
		return ds;
	}
    
}
