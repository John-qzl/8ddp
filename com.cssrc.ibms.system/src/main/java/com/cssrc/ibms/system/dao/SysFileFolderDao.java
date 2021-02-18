/**
 * 
 */
package com.cssrc.ibms.system.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.model.SysFileFolder;

/**
 * 文件夹管理
 */
@Repository
public class SysFileFolderDao extends BaseDao<SysFileFolder> {

	@SuppressWarnings("unchecked")
	@Override
	public Class getEntityClass() {
		return SysFileFolder.class;
	}
	
    /**
     * 查找用户文件夹树
     * */
	public List<SysFileFolder> getFolderByUserId(Long userId){
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("creatorId", userId);
		map.put("delFlag", 0);
		return this.getBySqlKey("getFolderByUserId", map);
	}
	
	/**
	 * 查找当前文件夹的子文件夹
	 * */
	public List<SysFileFolder> getFolderByPath(String path){
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("path", path);
		map.put("delflag", 0);
		return this.getBySqlKey("getFolderByPath", map);
	}
	
	/**
	 * 根据用户获取该用户文件夹树的根
	 * */
	public Object getRootFolderByUserId(Long userId){
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("creatorId", userId);
		map.put("depth", SysFileFolder.root);
		map.put("delflag", 0);
		List<SysFileFolder> sysFileFolders = this.getBySqlKey("getRootFolderByUserId", map);
		return sysFileFolders.size()>0?sysFileFolders.get(0):null;
	}
	/**
	 * 获取临时文库
	 *@author YangBo @date 2016年10月27日下午10:19:26
	 *@param userId
	 *@return
	 */
	public SysFileFolder getTmpFolderByUserId(Long userId){
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("creatorId", userId);
		map.put("depth", 2);
		map.put("sharedNode", SysFile.SHARED_FALSE);
		return (SysFileFolder)getUnique("getTmpFolderByUserId", map);
	}
	

}
