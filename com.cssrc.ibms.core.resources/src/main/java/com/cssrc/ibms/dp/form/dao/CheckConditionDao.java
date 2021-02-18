package com.cssrc.ibms.dp.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.dp.form.model.CheckCondition;

@Repository
public class CheckConditionDao extends BaseDao<CheckCondition>{

	public Class getEntityClass() {
		return CheckCondition.class;
	}
	
	public void deleteCondition(String id){
		Map params = new HashMap();
		params.put("ID", id);
		delItemByID("deleteById",params);
	}
	public  List<CheckCondition>getByModelId(Long mid){
		Map params = new HashMap();
		params.put("mid", mid);
		return this.getBySqlKey("getBymodelid", params);
	}

}
