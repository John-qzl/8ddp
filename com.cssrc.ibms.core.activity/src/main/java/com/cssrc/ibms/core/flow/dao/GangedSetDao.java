package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.GangedSet;
/**
 * 
 *<pre>
 * 对象功能:联动设置表 Dao类 
 *</pre>
 */
@Repository
public class GangedSetDao extends BaseDao<GangedSet>
{
	@Override
	public Class<?> getEntityClass()
	{
		return GangedSet.class;
	}
	
	public List<GangedSet> getByDefId(Long defId){
		return this.getBySqlKey("getByDefId", defId);
	}
	
	/**
	 * 获取某个节点的联动设置（包括全局联动设置）
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public List<GangedSet> getByDefIdAndNodeId(Long defId,String nodeId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("defId", defId);
		map.put("nodeId", nodeId);
		return this.getBySqlKey("getByDefIdAndNodeId",map);
	}
	
	/**
	 * 通过DEFID删除联动设置（除了指定setid的记录以外）
	 * @param defId
	 * @param setIds
	 */
	public void delByDefIdExceptSetId(Long defId,String setIds){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("defId", defId);
		map.put("setId", setIds);
		this.update("delByDefIdExceptSetId",map);
	}
	
	public void delByDefId(Long defId){
		this.update("delByDefId", defId);
	}
}