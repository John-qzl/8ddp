package com.cssrc.ibms.core.resources.io.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.ins.ConditionResult;
@Repository
public class IOConditionResultDao {
	@Resource
	private JdbcDao jdbcDao;

	public ConditionResult getById(String id) {
		String sql = " select * from W_CONDI_RES where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			return new ConditionResult(map);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void update(ConditionResult conditionResult) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_CONDI_RES set ");
		sql.append(" F_VALUE=:F_VALUE,F_CONDITION_ID=:F_CONDITION_ID,F_TB_INSTAN_ID=:F_TB_INSTAN_ID");
		sql.append(" where id=:ID");
		Map map = new HashMap();
		map.put("ID", conditionResult.getId());
		map.put("F_VALUE", conditionResult.getValue());
		map.put("F_CONDITION_ID", conditionResult.getCondition_id());
		map.put("F_TB_INSTAN_ID", conditionResult.getTb_instan_id());
		jdbcDao.exesql(sql.toString(), map);		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(ConditionResult conditionResult) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_CONDI_RES (ID,F_VALUE,F_CONDITION_ID,F_TB_INSTAN_ID)");
		sql.append(" values (:ID,:F_VALUE,:F_CONDITION_ID,:F_TB_INSTAN_ID)");
		Map map = new HashMap();
		map.put("ID", conditionResult.getId());
		map.put("F_VALUE", conditionResult.getValue());
		map.put("F_CONDITION_ID", conditionResult.getCondition_id());
		map.put("F_TB_INSTAN_ID", conditionResult.getTb_instan_id());
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
		sql.append(" select * from W_CONDI_RES where 1=1 ");
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
	public List<ConditionResult> getByInsId(String insId){
		List<ConditionResult> conditionResultList = new ArrayList();
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_TB_INSTAN_ID", insId);
		List<Map<String,Object>> list = this.query(keyValueMap);
		for(Map<String,Object> map : list) {
			ConditionResult c = new ConditionResult(map);
			conditionResultList.add(c);
		}
		return conditionResultList;
	}
	/**
	 * @Author  shenguoliang
	 * @Description:根据表单实例删除检查条件结果
	 * @Params [insId]
	 * @Date 2018/4/4 14:54
	 * @Return void
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteByIns(String insId) {

		String sql = "delete from W_CONDI_RES where F_TB_INSTAN_ID=:F_TB_INSTAN_ID";
		Map<String,Object> parameter = new HashMap();
		parameter.put("F_TB_INSTAN_ID", insId);
		jdbcDao.exesql(sql, parameter);
	}
}
