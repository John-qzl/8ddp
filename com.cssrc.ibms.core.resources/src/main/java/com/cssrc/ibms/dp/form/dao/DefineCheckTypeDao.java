package com.cssrc.ibms.dp.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.dp.form.model.DefineCheckType;

@Repository
public class DefineCheckTypeDao  extends BaseDao<DefineCheckType> {

	@Override
	public Class getEntityClass() {
		// TODO Auto-generated method stub
		return DefineCheckType.class;
	}
	
	public void deleteTableItem(String id){
		Map params = new HashMap();
		params.put("ID", id);
		delItemByID("deleteById",params);
	}
	
	public  List<DefineCheckType>getByModelId(Long mid){
		Map params = new HashMap();
		params.put("mid", mid);
		return (List<DefineCheckType>)this.getBySqlKey("getByModelId", params);
	}
}
