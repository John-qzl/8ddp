package com.cssrc.ibms.dp.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.model.CheckDataPackageInfo;
import com.cssrc.ibms.dp.form.model.CheckForm;
import com.cssrc.ibms.dp.form.model.CkConditionResult;

@Repository
public class CkConditionResultDao  extends BaseDao<CkConditionResult>{
	
	@Override
	public Class getEntityClass() {
		// TODO Auto-generated method stub
		return CkConditionResult.class;
	}
	/**
	 * 根据instanceId来查找CkConditionResult
	 * @param Id
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CkConditionResult> getByModelId(String Id){
		Map map = new HashMap();
		map.put("Id", Id);
		return (List<CkConditionResult>)this.getBySqlKey("getBymodelid", map);
	}

}
