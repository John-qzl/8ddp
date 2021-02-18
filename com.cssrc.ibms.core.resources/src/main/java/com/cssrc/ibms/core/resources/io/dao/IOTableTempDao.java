package com.cssrc.ibms.core.resources.io.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.template.CheckCondition;
import com.cssrc.ibms.core.resources.io.bean.template.CheckItemDef;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
@Repository
public class IOTableTempDao {
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private IOCheckConditionDao ioCheckConditionDao;
	@Resource
	private IOSignDefDao ioSignDefDao;
	@Resource
	private IOCheckItemDefDao ioCheckItemDefDao;

	public TableTemp getById(String id) {
		String sql = " select * from W_TABLE_TEMP where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			return new TableTemp(map);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TableTemp> getByProjectId(String projectId) {
		String sql = " select * from W_TABLE_TEMP where F_PROJECT_ID='"+projectId+"'";	
		
		List<Map<String,Object>> map = jdbcDao.queryForList(sql, null);
		if(map==null) {
			return null;
		}else {
			List<TableTemp> tableTempList=new ArrayList<>();
			for (Map<String,Object> tableTemp : map) {
				tableTempList.add(new TableTemp(tableTemp));
			}
			return tableTempList;
		}
	}
	 
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void update(TableTemp tableTemp) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_TABLE_TEMP set ");
		sql.append(" F_NAME=:F_NAME,F_SECRECY=:F_SECRECY,F_NUMBER=:F_NUMBER,");
		sql.append(" F_PROJECT_ID=:F_PROJECT_ID,F_CONTENTS=:F_CONTENTS,F_ROWNUM=:F_ROWNUM,");
		sql.append(" F_REMARK =:F_REMARK,F_TEMP_FILE_ID=:F_TEMP_FILE_ID ,F_STATUS=:F_STATUS,F_ZL=:F_ZL");
		sql.append(" where ID=:ID");
		Map map = new HashMap();
		map.put("ID", tableTemp.getId());
		map.put("F_NAME", tableTemp.getName());
		map.put("F_SECRECY", tableTemp.getSecrecy());
		map.put("F_NUMBER", tableTemp.getNumber());
		map.put("F_PROJECT_ID", tableTemp.getProject_id());
		map.put("F_CONTENTS", tableTemp.getContents());
		map.put("F_ROWNUM", tableTemp.getRownum());
		map.put("F_REMARK", tableTemp.getRemark());
		map.put("F_TEMP_FILE_ID", tableTemp.getTemp_file_id());
		map.put("F_STATUS", tableTemp.getStatus());
		map.put("F_ZL", tableTemp.getModelType());
		jdbcDao.exesql(sql.toString(), map);
	}
	/**
	 * 查询
	 * @param keyValueMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> query(Map<String,Object> keyValueMap){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_TABLE_TEMP where 1=1 ");
		Set keys = keyValueMap.keySet();
		for(Iterator it = keys.iterator();it.hasNext();) {
			String key = (String)it.next();
			Object value = keyValueMap.get(key);
			sql.append(" and ").append(key).append(" = '").append(value).append("'");
		}
		sql.append(" order by to_number(ID) ");
		return jdbcDao.queryForList(sql.toString(), null);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(TableTemp tableTemp) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_TABLE_TEMP (ID,F_NAME,F_SECRECY,F_NUMBER,F_PROJECT_ID,F_CONTENTS,F_ROWNUM,F_REMARK,F_TEMP_FILE_ID,F_STATUS,F_MODULE_ID,F_ZL)");
		sql.append(" values (:ID,:F_NAME,:F_SECRECY,:F_NUMBER,:F_PROJECT_ID,:F_CONTENTS,:F_ROWNUM,:F_REMARK,:F_TEMP_FILE_ID,:F_STATUS,:F_MODULE_ID,:F_ZL)");
		Map map = new HashMap();
		map.put("ID", tableTemp.getId());
		map.put("F_NAME", tableTemp.getName());
		map.put("F_SECRECY", tableTemp.getSecrecy());
		map.put("F_NUMBER", tableTemp.getNumber());
		map.put("F_PROJECT_ID", tableTemp.getProject_id());
		map.put("F_CONTENTS", tableTemp.getContents());
		map.put("F_ROWNUM", tableTemp.getRownum());
		map.put("F_REMARK", tableTemp.getRemark());
		map.put("F_TEMP_FILE_ID", tableTemp.getTemp_file_id());
		map.put("F_STATUS", tableTemp.getStatus());
		map.put("F_MODULE_ID", tableTemp.getModuleId());
		map.put("F_ZL", tableTemp.getModelType());
		jdbcDao.exesql(sql.toString(), map);
		return Long.valueOf(map.get("ID").toString());
	}

	/**
	 * 获取文件夹下所有的模板
	 * @param floderId
	 * @param initSon ： 是否初始化各种定义信息
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TableTemp> getByFloder(String floderId,boolean initSon){
		List<TableTemp> tableTempList = new ArrayList();
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_TEMP_FILE_ID", floderId);
		List<Map<String,Object>> list = this.query(keyValueMap);
		for(Map<String,Object> map : list) {
			TableTemp tableTemp = new TableTemp(map);
			if(initSon) { //初始化模板信息
				initTableTemp(tableTemp);
			}
			tableTempList.add(tableTemp);
		}
		return tableTempList;
	}
	/**
	 * 获取建在发次下所有的模板
	 * @param fcId ：发次Id
	 * @param initSon ： 是否初始化各种定义信息
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TableTemp> getByFc(Long fcId,boolean initSon){
		List<TableTemp> tableTempList = new ArrayList();
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_PROJECT_ID", fcId);
		keyValueMap.put("F_TEMP_FILE_ID", "1");
		List<Map<String,Object>> list = this.query(keyValueMap);
		for(Map<String,Object> map : list) {
			TableTemp tableTemp = new TableTemp(map);
			if(initSon) { //初始化模板信息
				initTableTemp(tableTemp);
			}
			tableTempList.add(tableTemp);
		}
		return tableTempList;
	}
	private void initTableTemp(TableTemp tableTemp) {
		String tempId = tableTemp.getId();
		tableTemp.setCheckConditionList(this.ioCheckConditionDao.getByTempId(tempId));
		tableTemp.setCheckItemDefList(this.ioCheckItemDefDao.getByTempId(tempId));
		tableTemp.setSignDefList(this.ioSignDefDao.getByTempId(tempId));
	}

	public void insert(TableTemp tableTemp,boolean insertSon) {
		//新增表单模板
		this.insert(tableTemp);
		//新增检查项\检查条件\签署表的数据
		if(insertSon) {
			List<CheckCondition>  ccList= tableTemp.getCheckConditionList();
			if(ccList!=null) {
				for(CheckCondition cc: ccList) {
					this.ioCheckConditionDao.insert(cc);
				}
			}
			List<CheckItemDef>  cidList= tableTemp.getCheckItemDefList();
			if(cidList!=null) {
				for(CheckItemDef cid: cidList) {
					this.ioCheckItemDefDao.insert(cid);
				}
			}
			List<SignDef>  sdList= tableTemp.getSignDefList();
			if(sdList!=null) {
				for(SignDef sd: sdList) {
					this.ioSignDefDao.insert(sd);
				}
			}
		}
	}

	/**
	 * @Author  shenguoliang
	 * @Description: 表单模板及其相关联数据新增(服务器导入)
	 * @Params [tableTemp, insertSon]
	 * @Date 2018/6/14 10:34
	 * @Return void
	 */
	public void insertImp(TableTemp tableTemp,boolean insertSon) {
		//新增表单模板
		this.insert(tableTemp);
		//新增检查项\检查条件\签署表的数据
		if(insertSon) {
			List<CheckCondition>  ccList= tableTemp.getCheckConditionList();
			if(ccList!=null) {
				for(CheckCondition cc: ccList) {
					cc.setId(cc.getId());
					cc.setTable_temp_id(tableTemp.getId());
					List<Map<String,Object>> CheckConditionList=this.getListById("W_CK_CONDITION",cc.getId());
					if(CheckConditionList.size()>0){
						this.ioCheckConditionDao.update(cc);
					}else {
						this.ioCheckConditionDao.insert(cc);
					}
				}
			}
			List<CheckItemDef>  cidList= tableTemp.getCheckItemDefList();
			if(cidList!=null) {
				for(CheckItemDef checkItemDef: cidList) {
					checkItemDef.setId(checkItemDef.getId());
					checkItemDef.setTable_temp_id(tableTemp.getId());
					List<Map<String,Object>> checkItemDefList=this.getListById("W_ITEMDEF",checkItemDef.getId());
					if(checkItemDefList.size()>0){
						this.ioCheckItemDefDao.update(checkItemDef);
					}else {
						this.ioCheckItemDefDao.insert(checkItemDef);
					}
				}
			}
			List<SignDef>  sdList= tableTemp.getSignDefList();
			if(sdList!=null) {
				for(SignDef sd: sdList) {
					sd.setId(sd.getId());
					sd.setTable_temp_id(tableTemp.getId());
					List<Map<String,Object>> signDefList=this.getListById("W_SIGNDEF",sd.getId());
					if(signDefList.size()>0){
						this.ioSignDefDao.update(sd);
					}else {
						this.ioSignDefDao.insert(sd);
					}
				}
			}
		}
	}

	/**
	 * 通过ID,获取当前行的记录
	 * @param modelName
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getListById(String modelName, String id) {
		String sql = "select *  from "+modelName+"  where ID="+id;
		return this.jdbcDao.queryForList(sql, null);
	}

	public void deleteById(String id ,boolean deleteSon) {
		if(deleteSon) {
			ioCheckConditionDao.deleteByTempId(id);
			ioSignDefDao.deleteByTempId(id);
			ioCheckItemDefDao.deleteByTempId(id);
		}
		this.delete(id);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void delete(String id) {
		String sql = "delete from W_TABLE_TEMP where ID=:ID";
		Map map = new HashMap();
		map.put("ID", id);
		this.jdbcDao.exesql(sql, map);
	}


}
