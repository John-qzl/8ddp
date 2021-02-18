package com.cssrc.ibms.system.dao;



import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.GlobalType;
import com.cssrc.ibms.system.model.SysFileType;
/**
 * 文件分类记录关联Dao层
 * @author YangBo
 *
 */
@Repository
public class SysFileTypeDao extends BaseDao<SysFileType>{
	
	@Override
	public Class<SysFileType> getEntityClass() {
		return SysFileType.class;
	}
	
	
	/**
	 * 控制用户分类
	 *@author YangBo 
	 *@param catKey
	 *@param userId
	 *@return
	 */
	public List<SysFileType> getPersonType(String catKey, Long userId, String dataId)
	{
		Map<String, Object> params = new Hashtable<String, Object>();
		params.put("catkey", catKey);
		params.put("userId", userId);
		params.put("dataId", dataId);
		List<SysFileType> list = getBySqlKey("getPersonType", params);
		return list;
	}
	
	/**
	 * 根据nodePath获取节点树
	 * @param nodePath
	 * @param dataId TODO
	 * @return
	 */
	public List<SysFileType> getByNodePath(String nodePath, Long dataId)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("nodePath", nodePath + "%");
		params.put("dataId", dataId.toString());
		return getBySqlKey("getByNodePath", params);
	}
	
	/**
	 * 查询相同分类相同key的数量
	 * @param typeId
	 * @param catKey
	 * @param nodeKey
	 * @param dataId TODO
	 * @return
	 */
	public boolean isNodeKeyExistsForUpdate(Long typeId, String catKey, String nodeKey, Long dataId)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("typeId", typeId);
		params.put("catkey", catKey);
		params.put("nodeKey", nodeKey);
		params.put("dataId", dataId.toString());
		int rtn = ((Integer)getOne("isNodeKeyExistsForUpdate", params)).intValue();
		return rtn > 0;
	}
	
	/**
	 * 某记录下nodekey重复数
	 * @param catKey
	 * @param nodeKey
	 * @param dataId TODO
	 * @return
	 */
	public boolean isNodeKeyExists(String catKey, String nodeKey, Long dataId)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("catkey", catKey);
		params.put("nodeKey", nodeKey);
		params.put("dataId", dataId.toString());
		int rtn = ((Integer)getOne("isNodeKeyExists", params)).intValue();
		return rtn > 0;
	}
	
	/**
	 * 查询唯一确定的一条文件节点
	 * @param typeId
	 * @param dataId
	 * @return
	 */
	public SysFileType getFileType(Long typeId, Long dataId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("typeId", typeId);
		params.put("dataId", dataId);
		SysFileType sysFileType = null;
		List<SysFileType> fileTypeList = this.getBySqlKey("getFileType", params);
		if(fileTypeList.size() > 0){
			sysFileType = fileTypeList.get(0);
		}
		return sysFileType;
	}
	
	/**
	 * 删除文件节点
	 * @param valueOf
	 * @param dataId
	 */
	public void delFileType(Long typeId, Long dataId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("typeId", typeId);
		params.put("dataId", dataId);
		delBySqlKey("delFileType", params);
	}
	
	/**
	 * 获取记录某类别节点树
	 * @param catKey
	 * @param dataId
	 * @return
	 */
	public List<SysFileType> getByCatKey(String catKey, Long dataId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("catKey", catKey);
		params.put("dataId", dataId);
		return getBySqlKey("getByCatKey", catKey);
	}
	
}