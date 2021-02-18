package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.DefRights;

@Repository
public class DefRightsDao extends BaseDao<DefRights>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return DefRights.class;
	}
	

	/**
	 * 根据流程定义ID和权限类型得到流程定义权限
	 * @param defId
	 * @param typeId
	 * @return
	 */
	public List<DefRights> getDefRight(Long defId,Short rightType){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("defId", defId);
		params.put("rightType", rightType);
		params.put("searchType", DefRights.SEARCH_TYPE_DEF);
		return getBySqlKey("getDefRight",params);
	}
	
	
	/**
	 * 根据流程分类和权限类型获取权限数据。
	 * @param typeId
	 * @param rightType
	 * @return
	 */
	public List<DefRights> getTypeRight(Long typeId,Short rightType){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("typeId", typeId);
		params.put("rightType", rightType);
		params.put("searchType", DefRights.SEARCH_TYPE_GLT);
		return getBySqlKey("getTypeRight",params);
	}
	
	/**
	 * 根据分类ID删除流程定义权限
	 * @param typeId
	 * @return
	 */
	public void delByTypeId(Long typeId){
		getBySqlKey("delByTypeId", typeId);
	}
	/**
	 * 根据流程定义ID删除权限。
	 * @param defId
	 */
	public void delByDefKey(String defKey){
		this.delBySqlKey("delByDefKey", defKey);
	}
	
	/**
	 * 根据流程ID获取权限。
	 * @param defId
	 * @return
	 */
	public List<DefRights> getByDefKey(String defKey){
		return this.getBySqlKey("getByDefKey", defKey);
	}
	
	/**
	 * 根据分类id获取权限。
	 * @param typeId
	 * @return
	 */
	public List<DefRights> getByTypeId(Long typeId){
		return this.getBySqlKey( "getByTypeId", typeId);
	}
	
	
}