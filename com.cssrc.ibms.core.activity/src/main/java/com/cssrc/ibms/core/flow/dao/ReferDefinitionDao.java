/**
 * 
 */
package com.cssrc.ibms.core.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.ReferDefinition;
 
@Repository
public class ReferDefinitionDao extends BaseDao<ReferDefinition> {

	@Override
	public Class<ReferDefinition> getEntityClass(){
		return ReferDefinition.class;
	}
	
	public void saveReferDef(ReferDefinition refers){
		add(refers);
	}
	
	public int delReferDef(Long refers){
		return delById(refers);
	}
	
	public int delByDefId(Long defId){
		return this.delBySqlKey("delByDefId", defId);
	}
	
	public int updateReferDef(ReferDefinition refers){
		return update("update", refers);
	}

	public List<ReferDefinition> getByDefId(Long defId) {
		return this.getBySqlKey("getByDefId", defId);
	}
}
