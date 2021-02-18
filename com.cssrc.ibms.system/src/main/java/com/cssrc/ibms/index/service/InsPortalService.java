package com.cssrc.ibms.index.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.sysuser.intf.ICurrentUserService;
import com.cssrc.ibms.api.sysuser.model.ISysObjRights;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.index.dao.InsPortalDao;
import com.cssrc.ibms.index.model.InsPortal;

/**
 * 首页布局管理Service层
 * @author YangBo
 *
 */
@Service
public class InsPortalService extends BaseService<InsPortal> {
	@Resource
	private InsPortalDao dao;
	
	@Resource
	private ICurrentUserService currentUserService;
	
	protected IEntityDao<InsPortal, Long> getEntityDao() {
		return this.dao;
	}
	
	/**
	 * 根据Key获取唯一对象
	 * @param key
	 * @param orgId
	 * @return
	 */
	public InsPortal getByKey(String key, String orgId)
	{
		return this.dao.getByKey(key, orgId);
	}
	
	/**
	 * 根据用户id和key值确定一条布局记录
	 * @param key
	 * @param orgId
	 * @param userId
	 * @return
	 */
	public InsPortal getByIdKey(String key, String orgId, String userId)
	{
		return this.dao.getByIdKey(key, orgId, userId);
	}
	
	/**
	 * 检查用户访问权限获取对应首页布局
	 * @return
	 */
	public InsPortal getPortalByRights(){
		InsPortal insPortal = null;
		Map<String, List<Long>> relationMap = this.currentUserService.getUserRelation();
		//建立权限顺序
		Map<String, List<Long>> linkMap = new LinkedHashMap<String, List<Long>>();
		linkMap.put("pos", relationMap.get("pos"));
		linkMap.put("orgSub", relationMap.get("orgSub"));
		linkMap.put("org", relationMap.get("org"));
		linkMap.put("role", relationMap.get("role"));
		linkMap.put("user", relationMap.get("user"));
		
		String objType = ISysObjRights.RIGHT_TYPE_INS_PORTAL;
		for(Map.Entry<String, List<Long>> entry : linkMap.entrySet()){
			List<Long> objList = entry.getValue();
			for(int i=0;i<objList.size();i++){
				insPortal = this.dao.getPortalByRights(objType, objList.get(i));
				if(insPortal != null)
					break;
			}
			if(insPortal != null)
				break;
		}
		
		return insPortal;
	}

}
