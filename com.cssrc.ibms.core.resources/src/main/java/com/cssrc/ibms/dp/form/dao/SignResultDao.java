package com.cssrc.ibms.dp.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.form.model.CkConditionResult;
import com.cssrc.ibms.dp.form.model.SignResult;

@Repository
public class SignResultDao extends BaseDao<SignResult>{

	@Override
	public Class getEntityClass() {
		// TODO Auto-generated method stub
		return SignResult.class;
	}
	
	/**`
	 * @param Id
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SignResult> getByModelId(String Id){
		Map map = new HashMap();
		map.put("Id", Id);
		return (List<SignResult>)this.getBySqlKey("getBymodelid", map);
	}
	
	public SignResult getSignResultById(String id) {
		Map map=new HashMap<>();
		map.put("id", id);
		return this.getUnique("getSignResultById", map);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SignResult> getByInstanceId(String InstanceId){
		Map map = new HashMap();
		map.put("InstanceId", InstanceId);
		return  this.getListBySqlKey("getByInstanceId", map);
	}
	
	
}
