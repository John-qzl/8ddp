/**
 * 
 */
package com.cssrc.ibms.system.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.ISysFileFolderService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.system.dao.SysFileFolderDao;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.model.SysFileFolder;

/**
 * 文件夹管理
 */
@Service
public class SysFileFolderService extends BaseService<SysFileFolder> implements ISysFileFolderService{
    
	@Resource
    private SysFileFolderDao dao;
    @Resource
    private SysFileService sysFileService;
	
	@Override
	protected IEntityDao<SysFileFolder, Long> getEntityDao() {
		return dao;
	}
	
	/**
	 * 获取用户的文件夹树
	 * */
	public List<SysFileFolder> getFolderByUserId(Long userId){
		List<SysFileFolder> list = dao.getFolderByUserId(userId);
		return list;
	}
	/**
	 * 创建用户文件夹树
	 * */
	public List<SysFileFolder> saveFolder(Long userId){
		List<SysFileFolder> list = new ArrayList<SysFileFolder>();
		SysFileFolder rootFolder = new SysFileFolder();
		rootFolder.setId(UniqueIdUtil.genId());
		rootFolder.setPid(-1L);
		rootFolder.setName("我的文库");
		rootFolder.setPath(null);
		rootFolder.setDepth(1L);
		rootFolder.setCreatorId(userId);
		rootFolder.setCreatetime(new java.util.Date());
		rootFolder.setDelflag(SysFileFolder.DELFLAG_FALSE);
		rootFolder.setSystemNode(SysFileFolder.systemNode_TRUE);
		rootFolder.setSharedNode(SysFileFolder.sharedNode_FALSE);
		rootFolder.setNotes("系统创建");
		dao.add(rootFolder);
		
		SysFileFolder tmpFolder = new SysFileFolder();
		tmpFolder.setId(UniqueIdUtil.genId());
		tmpFolder.setPid(rootFolder.getId());
		tmpFolder.setName("临时文库");
		tmpFolder.setPath(null);
		tmpFolder.setDepth(2L);
		tmpFolder.setCreatorId(userId);
		tmpFolder.setCreatetime(new java.util.Date());
		tmpFolder.setDelflag(SysFileFolder.DELFLAG_FALSE);
		tmpFolder.setNotes("系统创建");
		tmpFolder.setSystemNode(SysFileFolder.systemNode_TRUE);
		tmpFolder.setSharedNode(SysFileFolder.sharedNode_FALSE);
		dao.add(tmpFolder);
		
		SysFileFolder shareFolder = new SysFileFolder();
		shareFolder.setId(UniqueIdUtil.genId());
		shareFolder.setPid(rootFolder.getId());
		shareFolder.setName("共享文库");
		shareFolder.setPath(null);
		shareFolder.setDepth(2L);
		shareFolder.setCreatorId(userId);
		shareFolder.setCreatetime(new java.util.Date());
		shareFolder.setDelflag(SysFileFolder.DELFLAG_FALSE);
		shareFolder.setNotes("系统创建");
		shareFolder.setSystemNode(SysFileFolder.systemNode_TRUE);
		shareFolder.setSharedNode(SysFileFolder.sharedNode_TRUE);
		dao.add(shareFolder);
		
		rootFolder.setPath(rootFolder.getId()+".");
		tmpFolder.setPath(rootFolder.getId()+"." + tmpFolder.getId() + ".");
		shareFolder.setPath(rootFolder.getId()+"." + shareFolder.getId() + ".");
		
		dao.update(rootFolder);
		dao.update(tmpFolder);
		dao.update(shareFolder);
		list.add(rootFolder);
		list.add(tmpFolder);
		list.add(shareFolder);
		return list;
	}
	
	/**
	 * 删除文件夹
	 * */
	public void deleteFolder(Long id){
		
		SysFileFolder folder = dao.getById(id);
		List<SysFileFolder> childeList = dao.getFolderByPath(folder.getPath());
		for(SysFileFolder sysFolder : childeList){
			if(sysFolder.getId().equals(id)){
				continue;
			}
			deleteFolder(sysFolder.getId());
		}
		List<SysFile> fileList = sysFileService.getSysFileByFolder(id);
		for(SysFile file : fileList){
			file.setDelFlag(SysFile.FILE_DEL);
			sysFileService.update(file);
		}
		folder.setDelflag(SysFileFolder.DELFLAG_TRUE);
		folder.setUpdatetime(new java.util.Date() );
		folder.setUpdateId(UserContextUtil.getCurrentUser().getUserId());
		dao.update(folder);
	}
	
	/**
	 * 新增文件夹节点
	 * */
	public void saveNewFolder(SysFileFolder folder,Long userId){
		SysFileFolder parent = dao.getById(folder.getPid());
		dao.add(folder);
		folder.setPath(parent.getPath() + folder.getId() + ".");
		folder.setDepth(parent.getDepth() +1);
		folder.setDelflag(SysFileFolder.DELFLAG_FALSE);
		folder.setCreatorId(userId==null? UserContextUtil.getCurrentUser().getUserId() : userId);
		folder.setCreatetime(new java.util.Date());
	}

	/**
	 * 根据用户获取该用户文件夹树的根
	 * */
	public SysFileFolder getRootFolderByUserId(Long userId){
		SysFileFolder root =  (SysFileFolder)dao.getRootFolderByUserId(userId);
		return root;
	}
	
	/**
	 * 获取用户临时文库
	 *@author YangBo @date 2016年10月27日下午10:20:55
	 *@param userId
	 *@return
	 */
	public SysFileFolder getTmpFolderByUserId(Long userId){
		SysFileFolder Tmp =  (SysFileFolder)dao.getTmpFolderByUserId(userId);
		return Tmp;
	}


}
