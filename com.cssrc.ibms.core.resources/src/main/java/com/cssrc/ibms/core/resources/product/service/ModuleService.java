package com.cssrc.ibms.core.resources.product.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.core.resources.product.dao.ModuleDao;

/**
 * @description 型号管理业务解析类
 * @author xie chen
 * @date 2019年11月21日 下午2:02:37
 * @version V1.0
 */
@Service
public class ModuleService {
	
	@Resource
	private ModuleDao moduleDao;
	
	/**
	 * @Desc 获取所有型号数据
	 * @return
	 */
	public List<Map<String, Object>> getAllModules(){
		return moduleDao.getAllModules();
	}
	public List<Map<String, Object>> getModelTeamUser(String teamId){
		return moduleDao.getModelTeamUser(teamId);
	}
	public Map<String, Object> getModelTeam(String teamId){
		return moduleDao.getModelTeam(teamId);
	}
	public String isEditRole(String UserId,String typeId) 
	{
		return  moduleDao.isEditRole(UserId,typeId);
	}
	public Map<String, Object>  getByModelId(String modelId) 
	{
		return  moduleDao.getById(modelId);
	}

}
