package com.cssrc.ibms.core.user.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysObjRightsService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.SysObjRightsDao;
import com.cssrc.ibms.core.user.model.SysObjRights;
/**
 * 对象权限service层
 * @author Yangbo 2016-7-22
 *
 */
@Service
public class SysObjRightsService  extends BaseService<SysObjRights> implements ISysObjRightsService
{

	@Resource
	private SysObjRightsDao dao;

	protected IEntityDao<SysObjRights, Long> getEntityDao()
	{
		return this.dao;
	}
	
	/**
	 * 删除某类权限
	 * @param objType
	 * @param objectId
	 */
	public void deleteByObjTypeAndObjectId(String objType, String objectId) {
		this.dao.deleteByObjTypeAndObjectId(objType, objectId);
	}
	
	/**
	 * 保存权限和更新权限
	 * @param sysObjRights
	 */
	public void save(SysObjRights sysObjRights)
	{
		Long id = sysObjRights.getId();
		if ((id == null) || (id.longValue() == 0L)) {
			id = Long.valueOf(UniqueIdUtil.genId());
			sysObjRights.setId(id);
			add(sysObjRights);
		} else {
			update(sysObjRights);
		}
	}
	 /**
	  * 获取某类权限记录
	  * @param objType 权限类别
	  * @param objectId 对应的对象id 多对多关系
	  * @return
	  */
	public List<SysObjRights> getByObjTypeAndObjectId(String objType, String objectId) {
		return this.dao.getObject(objType, objectId);
	}

	public List<SysObjRights> getHashRights(String objType)
	{
/*		Map userMap = this.currentUserService.getUserRelation(ServiceUtil.getCurrentUser());

		Map map = new HashMap();
		map.put("objType", objType);

		map.put("userMap", userMap);

		return this.dao.getBySqlKey("getHashRights", map);*/
		
		return null;
	}
}


