package com.cssrc.ibms.core.resources.io.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import com.cssrc.ibms.core.resources.io.bean.template.TemplateFLoder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
@Repository
public class IOTemplateFLoderDao {
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private IOTableTempDao ioTableTempDao;
	
	public TemplateFLoder getById(String id) {
		String sql = " select * from W_TEMP_FILE where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			return new TemplateFLoder(map);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void update(TemplateFLoder templateFLoder) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_TEMP_FILE set ");
		sql.append(" F_NAME=:F_NAME,F_DESC=:F_DESC,F_PROJECT_ID=:F_PROJECT_ID,");
		sql.append(" F_TEMP_FILE_ID=:F_TEMP_FILE_ID ");		
		sql.append(" where id=:ID");
		Map map = new HashMap();
		map.put("ID", templateFLoder.getId());
		map.put("F_NAME", templateFLoder.getName());
		map.put("F_DESC", templateFLoder.getDesc());
		map.put("F_PROJECT_ID", templateFLoder.getProject_id());
		map.put("F_TEMP_FILE_ID", templateFLoder.getTemp_file_id());
		jdbcDao.exesql(sql.toString(), map);		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(TemplateFLoder templateFLoder) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_TEMP_FILE (ID,F_NAME,F_DESC,F_PROJECT_ID,F_TEMP_FILE_ID)");
		sql.append(" values (:ID,:F_NAME,:F_DESC,:F_PROJECT_ID,:F_TEMP_FILE_ID)");
		Map map = new HashMap();
		map.put("ID", templateFLoder.getId());
		map.put("F_NAME", templateFLoder.getName());
		map.put("F_DESC", templateFLoder.getDesc());
		map.put("F_PROJECT_ID", templateFLoder.getProject_id());
		map.put("F_TEMP_FILE_ID", templateFLoder.getTemp_file_id());
		jdbcDao.exesql(sql.toString(), map);
		return Long.valueOf(map.get("ID").toString());
	}
	
	/**
	 * 查询
	 * @param keyValueMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> query(Map<String,Object> keyValueMap){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_TEMP_FILE where 1=1 ");
		Set keys = keyValueMap.keySet();
		for(Iterator it = keys.iterator();it.hasNext();) {
			String key = (String)it.next();
			Object value = keyValueMap.get(key);
			sql.append(" and ").append(key).append(" = '").append(value).append("'");
		}
		sql.append(" order by to_number(ID) ");
		return jdbcDao.queryForList(sql.toString(), null);
	}
	/**
	 * 根据发次Id,获取第一层表单模板文件夹集合
	 * @param fcId : 发次Id 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TemplateFLoder> getFloder(Long fcId,boolean initSonfloder,boolean initTableTemp){
		List<TemplateFLoder> floderList = new ArrayList();
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_PROJECT_ID", fcId);
		keyValueMap.put("F_TEMP_FILE_ID", "1");
		List<Map<String,Object>> list = this.query(keyValueMap);
		for(Map<String,Object> map : list) {
			TemplateFLoder floder = new TemplateFLoder(map);
			if(initTableTemp) { //初始化模板信息
				List<TableTemp> tableTempList = ioTableTempDao.getByFloder(floder.getId(), true);
				floder.setTableTempList(tableTempList);
			}
			if(initSonfloder) { //初始化子集文件夹
				setSonFloder(floder,initTableTemp);
			}
			floderList.add(floder);
		}
		return floderList;
	}
	/**
	 * @param floder ： 需设置子文件夹的文件夹
	 * @param initTableTemp: 是否初始化表单模板
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setSonFloder(TemplateFLoder floder,boolean initTableTemp){
		List<TemplateFLoder> floderList = new ArrayList();
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_TEMP_FILE_ID", floder.getId());
		List<Map<String,Object>> list = this.query(keyValueMap);
		for(Map<String,Object> map : list) {
			TemplateFLoder sonfloder = new TemplateFLoder(map);
			if(initTableTemp) { //初始化模板信息
				List<TableTemp> tableTempList = ioTableTempDao.getByFloder(sonfloder.getId(), true);
				sonfloder.setTableTempList(tableTempList);
			}
			this.setSonFloder(sonfloder, initTableTemp);
			floderList.add(sonfloder);
		}
		floder.setSonFloderList(floderList);
	}
	public void deleteById(String id ,boolean deleteSon) {
		if(deleteSon) {
			List<TableTemp> list = ioTableTempDao.getByFloder(id, false);
			for(TableTemp tt : list) {
				ioTableTempDao.deleteById(tt.getId(), true);
			}
		}
		this.delete(id);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void delete(String id) {
		String sql = "delete from W_TEMP_FILE where ID=:ID";
		Map map = new HashMap();
		map.put("ID", id);
		this.jdbcDao.exesql(sql, map);
	}
}
