package com.cssrc.ibms.core.resources.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.resources.product.bean.ModuleManage;
import com.cssrc.ibms.core.resources.product.dao.ModuleBatchDao;
import com.cssrc.ibms.core.resources.product.dao.ModuleDao;
import com.cssrc.ibms.core.resources.product.dao.ModuleManageDao;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.MapUtil;

/**
 * @description 型号管理员控制service
 * @author fuyong
 * @date 2020年08月22日 下午3:53:26
 * @version V1.0
 */
@Service
public class ModuleManageService {
	@Resource
	ModuleManageDao moduleManageDao;
	
	/**
	 * @Desc 根据型号ID获取管理人员ID
	 * @param moduleId,type
	 * @return
	 */
	public List<String> getModuleManger(String moduleId,String type){
		return moduleManageDao.getModuleManger(moduleId,type);
	}

	/**
	 * @Desc 插入管理员数据
	 * @param moduleId,userid,fullName,type,
	 * @return
	 */
	public void  insert(String moduleId,String userid,String fullName,String type) {
		moduleManageDao.insert(moduleId, userid, fullName, type);
	}
	
	/**
	 * @Desc 通过moduleId和权限种类获取人员
	 * @param moduleId
	 * @return
	 */
	public List<String>  getByModuleId(String moduleId,String type) {
		return moduleManageDao.getByModuleId(moduleId,type);
	}

	/**
	 * @Desc 通过moduleId获取人员
	 * @param moduleId
	 * @return
	 */
	public List<String>  getByModuleId(String moduleId) {
		return moduleManageDao.getByModuleId(moduleId);
	}
	/**
	 * @Desc 通过moduleId获取人员去除重复
	 * @param moduleId
	 * @return
	 */
	public List<String>  getByModuleIdDis(String moduleId) {
		return moduleManageDao.getByModuleIdDic(moduleId);
	}
	
	/**
	 * @Desc 通过id获取
	 * @param id
	 * @return
	 */
	public Map<String, Object> getManageById(String id){
		
		return moduleManageDao.getManageById(id);
	}
	/**
	 * @Desc 插入数据
	 * @param moduleManage
	 * @return
	 */
	public void insert(ModuleManage moduleManage) {
		moduleManageDao.insert(moduleManage);
	}
	
	/**
	 * @Desc 更新数据
	 * @param moduleManage
	 * @return
	 */
	public void update(ModuleManage moduleManage) {
		moduleManageDao.update(moduleManage);
	}
	
	
	/**
	 * @Desc 通过moduleId和权限种类获取人员Bean
	 * @param moduleId
	 * @return
	 */
	public List<ModuleManage> getManageByModuleId(String moduleId){
		return moduleManageDao.getManageByModuleId(moduleId);
	}
	
	
	
	/**
	 * @Desc 删除指定id数据
	 * @param id
	 * @return
	 */
	public void deleteById(String id){
		 moduleManageDao.deleteById(id);
	}
}
