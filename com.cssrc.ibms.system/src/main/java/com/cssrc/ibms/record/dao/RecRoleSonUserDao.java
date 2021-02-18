package com.cssrc.ibms.record.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.record.model.RecRoleSonUser;
@Repository
public class RecRoleSonUserDao extends BaseDao<RecRoleSonUser> {
	public Class getEntityClass() {
		return RecRoleSonUser.class;
	}
	public List<RecRoleSonUser> getRecUser(String sql,String userAdd,String userDel,Long roleSonId){
		Map map = new HashMap();
		userAdd = "'"+userAdd.replace(",", "','")+"'";
		userDel = "'"+userDel.replace(",", "','")+"'";
		map.put("roleSonId", roleSonId);
		map.put("filterSql", sql);
		map.put("userAdd", userAdd.replace("''", "'-100'"));
		map.put("userDel", userDel.replace("''", "'-100'"));
		if(getDbType().equals("oracle")){
			return getBySqlKey("getRecUser_oracle",map);
		}else{
			return getBySqlKey("getRecUser_mysql",map);
		}
	}
	public List<RecRoleSonUser> getRecUser(String sql,String def_sql,String userAdd,String userDel,Long roleSonId){
		Map map = new HashMap();
		userAdd = "'"+userAdd.replace(",", "','")+"'";
		userDel = "'"+userDel.replace(",", "','")+"'";
		map.put("roleSonId", roleSonId);
		map.put("filterSql", sql);
		map.put("defFilterSql", def_sql);
		map.put("userAdd", userAdd.replace("''", "'-100'"));
		map.put("userDel", userDel.replace("''", "'-100'"));
		if(getDbType().equals("oracle")){
			return getBySqlKey("getRecUser_oracle",map);
		}else{
			return getBySqlKey("getRecUser_mysql",map);
		}
	}
}
