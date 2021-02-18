package com.cssrc.ibms.dp.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.dp.form.model.DefineSign;

@Repository
public class DefineSignDao extends BaseDao<DefineSign> {

	@Override
	public Class getEntityClass() {
		// TODO Auto-generated method stub
		return DefineSign.class;
	}
	
	public void deleteSign(String id){
		Map params = new HashMap();
		params.put("ID", id);
		delItemByID("deleteById",params);
	}
	
	public  List<DefineSign>getByModelId(Long mid){
		Map params = new HashMap();
		params.put("mid", mid);
		return (List<DefineSign>)this.getBySqlKey("getByModelId", params);
	}
}
