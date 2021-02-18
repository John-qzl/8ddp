package com.cssrc.ibms.system.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.system.dao.SysFileTypeDao;
import com.cssrc.ibms.system.model.SysFileType;


/**
 * 文件分类记录关联Service层
 * @author YangBo
 *
 */
@Service
public class SysFileTypeService extends BaseService<SysFileType>{
	@Resource
	private SysFileTypeDao dao;
	@Resource
	private SysTypeKeyService typeKeyService;
	
	@Override
	protected IEntityDao<SysFileType, Long> getEntityDao() {
		return dao;
	}
	
	/**
	 * 获取记录分类树
	 * @param catKey
	 * @param userId
	 * @param dataId
	 * @return
	 */
	public List<SysFileType> getPersonType(String catKey, Long userId, String dataId) {
		List<SysFileType> list = this.dao.getPersonType(catKey, userId,dataId);
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
		return this.dao.getByNodePath(nodePath, dataId);
	}
	
	/**
	 * 根据节点Id获取关联的所有子节点
	 * @author YangBo
	 * @param typeId
	 * @param dataId TODO
	 * @return
	 */
	public String getIdsByParentId(Long typeId, Long dataId){
		SysFileType sysFileType = this.dao.getFileType(typeId, dataId);
		String typeIds ="";
		if(sysFileType != null ){
			String nodePath = sysFileType.getNodePath();
			List<SysFileType> list = this.getByNodePath(nodePath, dataId);
			for(SysFileType fileType: list){
				typeIds +=fileType.getTypeId()+",";
			}
			typeIds = typeIds.substring(0,typeIds.length()-1);
		}
		return typeIds;
	}
	/**
	 * 根据节点Id获取关联的所有子节点
	 * @param typeId
	 * @param dataId
	 * @return
	 */
	public List<SysFileType> getByParentId(Long typeId, Long dataId){
		SysFileType fileType =  this.dao.getFileType(typeId, dataId);
		String nodePath = fileType==null?"":fileType.getNodePath();
		List<SysFileType> list = this.getByNodePath(nodePath, dataId);
		return list;
	}
	/**
	 * 获取相同key的节点数
	 * @param typeId
	 * @param catKey
	 * @param nodeKey
	 * @param dataId TODO
	 * @return
	 */
	public boolean isNodeKeyExistsForUpdate(Long typeId, String catKey, String nodeKey, Long dataId) {
		return this.dao.isNodeKeyExistsForUpdate(typeId, catKey, nodeKey,  dataId);
	}
	
	/**
	 * 建立新的节点
	 * @param parentId
	 * @param dataId TODO
	 * @param isRoot
	 * @return
	 * @throws Exception
	 */
	public SysFileType getInitSysFileType(long parentId, Long dataId)throws Exception
	{
		SysFileType SysFileType = new SysFileType();
		Long typeId = Long.valueOf(UniqueIdUtil.genId());
		SysFileType = (SysFileType)this.dao.getFileType(parentId, dataId);
		String nodePath = SysFileType.getNodePath();
		SysFileType.setNodePath(nodePath + typeId + ".");
		SysFileType.setTypeId(typeId);
		return SysFileType;
	}
	
	/**
	 * 新增nodekey查重
	 * @param catKey
	 * @param nodeKey
	 * @param dataId TODO
	 * @return
	 */
	public boolean isNodeKeyExists(String catKey, String nodeKey, Long dataId) {
		return this.dao.isNodeKeyExists(catKey, nodeKey, dataId);
	}
	
	/**
	 * 查出唯一确定一条记录
	 * @param typeId
	 * @param dataId
	 * @return
	 */
	public SysFileType getFileType(Long typeId, long dataId) {
		SysFileType sysFileType = this.dao.getFileType(typeId, dataId);
		return sysFileType;
	}
	
	 /**
	  * 删除节点
	  * @param typeId
	  * @param dataId
	  */
	public void delFileType(Long typeId, long dataId){
		//排除空删除
		if (BeanUtils.isEmpty(typeId)||BeanUtils.isEmpty(dataId)) return;
		//获取当前节点位置
		SysFileType sysFileType = this.getFileType(typeId, dataId);
		String oldNodePath = sysFileType.getNodePath();
		//获取树
		List<SysFileType> childrenList = this.getByNodePath(oldNodePath,dataId);
		//遍历树删除
		for (SysFileType fileType : childrenList) {
			long Id = fileType.getTypeId().longValue();
			this.dao.delFileType(Long.valueOf(Id),Long.valueOf(dataId));
		}
	}
	
	/**
	 * 获取某类别下某条记录的节点树
	 * @param catKey
	 * @param dataId
	 * @return
	 */
	public List<SysFileType> getByCatKey(String catKey, Long dataId) {
		List<SysFileType> list = this.dao.getByCatKey(catKey, dataId);
		return list;
	}
}
