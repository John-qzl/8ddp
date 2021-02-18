package com.cssrc.ibms.dp.form.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.form.model.CheckResult;
import com.cssrc.ibms.dp.form.model.CheckResultJgjg;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CheckResultJgjgDao extends BaseDao<CheckResultJgjg>{

	@Resource
	private JdbcDao jdbcDao;
	
	@Override
	public Class getEntityClass() {
		// TODO Auto-generated method stub
		return CheckResultJgjg.class;
	}
	
	public Map<String, Object> getId(String itemId, Long instanId) {
		String sql = "SELECT ID FROM W_CK_RESULT_JGJG WHERE F_ITEMDEF_ID=:itemId AND F_TB_INSTAN=:instanId";
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
	public CheckResultJgjg getById(String Id){
		Map map = new HashMap();
		map.put("Id", Id);
		return (CheckResultJgjg)this.getOne("getById", map);
	}
}
