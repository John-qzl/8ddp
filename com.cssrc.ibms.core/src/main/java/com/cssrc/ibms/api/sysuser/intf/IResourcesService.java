package com.cssrc.ibms.api.sysuser.intf;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.sysuser.model.IResources;

public interface IResourcesService {
    public static String menu_redis_prekey="menu.right.";
    public static String user_menu_redisset="user.menu.redisset";

    public List<IResources> getDatatemplateRes(Long userId);
	/**
	 * 通过父id获得父功能点
	 * @param parentId
	 * @return
	 */
	public abstract IResources getParentResourcesByParentId(long parentId);

	public abstract void delById(Long resId);

	/**
	 * 查找该角色选中的功能点
	 * @param roleId
	 * @return
	 */
	public abstract List<?extends IResources> getBySysRolResChecked(Long roleId);

	public abstract IResources getByUrl(String url);

	public abstract List<?extends IResources> getByParentId(Long id);

	public abstract void move(Long sourceId, Long targetId);

	public abstract String exportXml(long resId, Map<String, Boolean> map)
			throws Exception;

	public abstract void importXml(InputStream inputStream, long resId)
			throws Exception;

	public abstract void updSn(Long resId, long sn);

	public abstract IResources getByAlias(String alias);

	/** @deprecated */
	public abstract List<?extends IResources> getByParentUserId(Long resId, Long userId);

	public abstract List<?extends IResources> getNormMenuByAllRoleParentId(Long resId,
			String rolealias);
  
    /**
     * 将用户的菜单数据放到redis中
     * @param user
     * @param contextPath
     */
    void setUserMenuToRedis();

    /**
     * @param userId 用户ID
     * @return 返回对象为json串，可以直接转成List
     * 返回值不为空说明用户对sysname拥有权限
     */
    public abstract String getSysRightByUser(Long userId);

    /**
     * 将某一角色用户的菜单数据放到redis中
     * @param roleId
     */
    void setUserMenuToRedis(Long roleId);
    
    /**
     * 根据用户id更新redis中用户权限数据
     * @param userId
     */
    void updSysRightByUserId(Long userId);
}