package com.cssrc.ibms.core.resources.project.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.dao.PackageDao;
import com.cssrc.ibms.core.resources.io.bean.Project;
import com.cssrc.ibms.core.resources.io.dao.IOTableTempDao;
import com.cssrc.ibms.core.resources.io.dao.IOTemplateFLoderDao;
import com.cssrc.ibms.core.util.common.CommonTools;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

@Repository
public class ProjectDao {
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private PackageDao packageDao;
	@Resource
	private IOTemplateFLoderDao iOTemplateFLoderDao;
	@Resource
	private IOTableTempDao iOTableTempDao;
	
	/** 查询目录树所有信息 */
	public List<Map<String, Object>> queryProjectNodeById(String id) {
		String sql = "SELECT * FROM W_PROJECT where F_SSXH=" + id + " ORDER BY F_TCPX";
		return jdbcDao.queryForList(sql, null);
	}
	/** 查询目录树所有信息 */
	public List<Map<String, Object>> queryProjectById(String id) {
		String sql = "SELECT ID FROM W_PROJECT where F_SSXH IN (" + id + ")";
		return jdbcDao.queryForList(sql, null);
	}
	
	/** 根据id删除发次 */
	public void deleteById(String id) {
		String sql = "delete from W_PROJECT where ID=:ID";
		Map<String,Object> parameter = new HashMap();
		parameter.put("ID", id);
		jdbcDao.exesql(sql, parameter);	
	}

	/**
	 * 根据发次ID获取发次信息
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getById(Long id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_PROJECT where ID=:ID");
		Map params = new HashMap();
		params.put("ID", id);
		return jdbcDao.queryForMap(sql.toString(), params);
	}

	/**
	 * 查询
	 * 
	 * @param keyValueMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>> query(Map<String, Object> keyValueMap) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_PROJECT where 1=1 ");
		Set keys = keyValueMap.keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {
			String key = (String) it.next();
			Object value = keyValueMap.get(key);
			sql.append(" and ").append(key).append(" = '").append(value).append("'");
		}
		return jdbcDao.queryForList(sql.toString(), null);
	}

	public Project getBeanById(Long id) {
		return new Project(getById(id));
	}

	/**
	 * 获取唯一的发次信息
	 * 
	 * @param projectDh
	 *            : 发次代号
	 * @param productDh
	 *            ：型号代号
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Project getByDh(String projectDh, String productDh) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_PROJECT where F_FCDH=:F_FCDH and  ");
		sql.append(" F_SSXH in (select id from W_PRODUCT where F_XHDH =:F_XHDH)");
		Map params = new HashMap();
		params.put("F_FCDH", projectDh);
		params.put("F_XHDH", productDh);
		Map<String, Object> map = jdbcDao.queryForMap(sql.toString(), params);
		if (map == null) {
			return null;
		} else {
			return new Project(map);
		}
	}

	/**
	 * 清空数据库中发次下的所有关系表信息
	 * @param id
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteSon(String id) {
		//1.删除数据包节点信息
		Map<String, Object> keyValueMap = new HashMap();
		keyValueMap.put("F_SSFC", id);
		List<Map<String, Object>> list = packageDao.query(keyValueMap);
		if(list!=null) {
			for (Map<String, Object> map : list) {
				String packageID = CommonTools.Obj2String(map.get("ID"));
				if (!packageID.equals("")) {
					packageDao.deleteById(packageID, true);
				}
			}
		}
		//2.删除文件夹信息
		keyValueMap.clear();
		keyValueMap.put("F_PROJECT_ID", id);
		List<Map<String, Object>> floderList = iOTemplateFLoderDao.query(keyValueMap);
		if(floderList!=null) {
			for (Map<String, Object> floder : floderList) {
				String floderID = CommonTools.Obj2String(floder.get("ID"));
				this.iOTemplateFLoderDao.deleteById(floderID, true);
			}
		}
		//3.删除直属的模板
		keyValueMap.clear();
		keyValueMap.put("F_PROJECT_ID", id);
		List<Map<String, Object>> tempList = iOTableTempDao.query(keyValueMap);
		if(tempList!=null) {
			for (Map<String, Object> temp : tempList) {
				String tempID = CommonTools.Obj2String(temp.get("ID"));
				this.iOTableTempDao.deleteById(tempID, true);
			}
		}
	}
}
