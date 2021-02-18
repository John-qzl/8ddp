package com.cssrc.ibms.dp.form.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.form.model.CheckResultCarry;

@Repository
public class CheckResultCarryDao extends BaseDao<CheckResultCarry>{

	@Resource
	private JdbcDao jdbcDao;
	
	@Override
	public Class getEntityClass() {
		// TODO Auto-generated method stub
		return CheckResultCarry.class;
	}
	
	public Map<String, Object> getId(String itemId, Long instanId) {
		String sql = "SELECT ID FROM W_CK_RESULT_CARRY WHERE F_ITEMDEF_ID=:itemId AND F_TB_INSTAN=:instanId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("itemId", itemId);
		params.put("instanId", instanId);
		return jdbcDao.queryForMap(sql, params);
		
	}
	/**
	 * @param Id
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CheckResultCarry getById(String Id){
		Map map = new HashMap();
		map.put("Id", Id);
		return (CheckResultCarry)this.getOne("getById", map);
	}
}
