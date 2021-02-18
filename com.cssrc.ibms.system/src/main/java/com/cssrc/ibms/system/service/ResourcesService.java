package com.cssrc.ibms.system.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.util.SysContextUtil;
import com.cssrc.ibms.api.sysuser.intf.IResourcesService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.IResources;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.file.XmlUtil;
import com.cssrc.ibms.core.util.redis.RedisClient;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.system.dao.ResourcesDao;
import com.cssrc.ibms.system.dao.ResourcesUrlDao;
import com.cssrc.ibms.system.dao.RoleResourcesDao;
import com.cssrc.ibms.system.model.CurrentSystem;
import com.cssrc.ibms.system.model.Resources;
import com.cssrc.ibms.system.model.ResourcesUrl;
import com.cssrc.ibms.system.model.RoleResources;
import com.cssrc.ibms.system.xml.ResourcesXml;
import com.cssrc.ibms.system.xml.ResourcesXmlList;

/**
 * 功能点管理
 * <p>
 * Title:ResourcesService
 * </p>
 * 
 * @author Yangbo
 * @date 2016-8-26下午03:42:24
 */
@Service
public class ResourcesService extends BaseService<Resources> implements IResourcesService
{
    @Resource
    private ResourcesDao resourcesDao;
    
    @Resource
    private ResourcesUrlDao resourcesUrlDao;
    
    @Resource
    private RoleResourcesDao roleResourcesDao;
    
    @Resource
    SysParameterService sysParameterService;
    
    @Resource
    ISysRoleService sysRoleService;
    protected IEntityDao<Resources, Long> getEntityDao()
    {
        return this.resourcesDao;
    }
    
    public List<Map<String, Object>> getIcons(String iconPath, String type) throws IOException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			BufferedReader br =new BufferedReader(new InputStreamReader(new FileInputStream(new File(iconPath)),"UTF-8"));
			
			String lineTxt = null;
			while((lineTxt = br.readLine()) != null){
				Map<String, Object> map = new HashMap<String, Object>();
				if(lineTxt.indexOf("before") > 0 && lineTxt.indexOf(type) > 0){
					String iconClassName = lineTxt.substring(lineTxt.indexOf(".")+1, lineTxt.indexOf(":"));
					map.put("iconClassName", iconClassName);
					
					String iconContent = lineTxt.substring(lineTxt.indexOf("\"")+1, lineTxt.indexOf("\";"));
					map.put("iconContent", iconContent);
					list.add(map);
				}
			}
			br.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<Map<String, Object>> getIconImgClassNameAndContent(String iconPath) throws IOException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			BufferedReader br =new BufferedReader(new InputStreamReader(new FileInputStream(new File(iconPath)),"UTF-8"));
			
			String lineTxt = null;
			while((lineTxt = br.readLine()) != null){
				Map<String, Object> map = new HashMap<String, Object>();
				if(lineTxt.indexOf("{") > 0 && lineTxt.indexOf("-") > 0){
					String iconImgClassName = lineTxt.substring(lineTxt.indexOf(".")+1, lineTxt.indexOf("{"));
					map.put("iconImgClassName", iconImgClassName);
					
					String iconImgUrl = lineTxt.substring(lineTxt.indexOf("../i")+2, lineTxt.indexOf("')"));
					map.put("iconImgUrl", iconImgUrl);
					list.add(map);
				}
			}
			br.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return list;
	}
    
    public Long addRes(Resources resources, String[] aryName, String[] aryUrl)
        throws Exception
    {
        Long resId = Long.valueOf(UniqueIdUtil.genId());
        resources.setResId(resId);
        String path = "";
        Long parentId = resources.getParentId();
        Resources parentRes = (Resources)this.resourcesDao.getById(parentId);
        if (BeanUtils.isNotEmpty(parentRes))
        {
            if (StringUtil.isNotEmpty(parentRes.getPath()))
                path = parentRes.getPath() + ":" + resId;
        }
        else
        {
            path = resId.toString();
        }
        resources.setPath(path);
        this.resourcesDao.add(resources);
        
        if (BeanUtils.isEmpty(aryName))
            return resId;
        
        for (int i = 0; i < aryName.length; i++)
        {
            String url = aryUrl[i];
            if (!StringUtil.isEmpty(url))
            {
                ResourcesUrl resouceUrl = new ResourcesUrl();
                resouceUrl.setResId(resId);
                resouceUrl.setResUrlId(Long.valueOf(UniqueIdUtil.genId()));
                resouceUrl.setName(aryName[i]);
                resouceUrl.setUrl(url);
                this.resourcesUrlDao.add(resouceUrl);
            }
        }
        return resId;
    }
    
    public void updRes(Resources resources, String[] aryName, String[] aryUrl)
        throws Exception
    {
        Long resId = resources.getResId();
        String path = "";
        Long parentId = resources.getParentId();
        Resources parentRes = (Resources)this.resourcesDao.getById(parentId);
        if (BeanUtils.isNotEmpty(parentRes))
        {
            if (StringUtil.isNotEmpty(parentRes.getPath()))
                path = parentRes.getPath() + ":" + resId;
        }
        else
        {
            path = resId.toString();
        }
        resources.setPath(path);
        this.resourcesDao.update(resources);
        
        this.resourcesUrlDao.delByResId(resId.longValue());
        
        if (BeanUtils.isEmpty(aryName))
            return;
        
        for (int i = 0; i < aryName.length; i++)
        {
            String url = aryUrl[i];
            if (!StringUtil.isEmpty(url))
            {
                ResourcesUrl resouceUrl = new ResourcesUrl();
                resouceUrl.setResId(resId);
                resouceUrl.setResUrlId(Long.valueOf(UniqueIdUtil.genId()));
                resouceUrl.setName(aryName[i]);
                resouceUrl.setUrl(url);
                this.resourcesUrlDao.add(resouceUrl);
            }
        }
    }
    
    /**
     * 通过父id获得父功能点
     * 
     * @param parentId
     * @return
     */
    public Resources getParentResourcesByParentId(long parentId)
    {
        Resources parent = (Resources)this.resourcesDao.getById(Long.valueOf(parentId));
        if (parent != null)
            return parent;
        parent = new Resources();
        parent.setResId(Long.valueOf(0L));
        parent.setParentId(Long.valueOf(-1L));
        parent.setSn(Integer.valueOf(0));
        // 给功能节点加父节点
        CurrentSystem currentSystem = sysParameterService.getCurrentSystem();
        parent.setResName(currentSystem.getSystemName());
        
        parent.setIsDisplayInMenu(Resources.IS_DISPLAY_IN_MENU_Y);
        parent.setIsFolder(Resources.IS_FOLDER_Y);
        parent.setIsOpen(Resources.IS_OPEN_Y);
        
        return parent;
    }
    
    /**
     * 由resid获得其下子功能点
     * 
     * @param resId
     * @param allRes
     * @return
     */
    private List<Resources> getChildsByResId(Long resId, List<Resources> allRes)
    {
        List<Resources> rtnList = new ArrayList<Resources>();
        for (Iterator<Resources> it = allRes.iterator(); it.hasNext();)
        {
            Resources res = (Resources)it.next();
            if (resId.equals(res.getParentId()))
            {
                rtnList.add(res);
                recursiveChilds(res.getResId(), rtnList, allRes);
            }
        }
        return rtnList;
    }
    
    /**
     * 子节点遍历
     * 
     * @param resId
     * @param rtnList
     * @param allRes
     */
    private void recursiveChilds(Long resId, List<Resources> rtnList, List<Resources> allRes)
    {
        for (Iterator<Resources> it = allRes.iterator(); it.hasNext();)
        {
            Resources res = (Resources)it.next();
            if (res.getParentId().equals(resId))
            {
                rtnList.add(res);
                recursiveChilds(res.getResId(), rtnList, allRes);
            }
        }
    }
    
    public void delById(Long resId)
    {
        Resources res = (Resources)this.resourcesDao.getById(resId);
        List allRes = this.resourcesDao.getAll();
        List allChilds = getChildsByResId(resId, allRes);
        
        for (Iterator it = allChilds.iterator(); it.hasNext();)
        {
            Resources resources = (Resources)it.next();
            Long childId = resources.getResId();
            
            this.resourcesUrlDao.delByResId(childId.longValue());
            
            this.roleResourcesDao.delByResId(childId);
            
            this.resourcesDao.delById(childId);
        }
        
        this.resourcesUrlDao.delByResId(resId.longValue());
        
        this.roleResourcesDao.delByResId(resId);
        
        this.resourcesDao.delById(resId);
    }
    
    /**
     * 查找该角色选中的功能点
     * 
     * @param roleId
     * @return
     */
    public List<Resources> getBySysRolResChecked(Long roleId)
    {
        List<Resources> resourcesList = this.resourcesDao.getAll();
        List<RoleResources> roleResourcesList = this.roleResourcesDao.getByRole(roleId);
        
        Set set = new HashSet();
        
        if (BeanUtils.isNotEmpty(roleResourcesList))
        {
            for (RoleResources rores : roleResourcesList)
            {
                set.add(rores.getResId());
            }
        }
        
        if (BeanUtils.isNotEmpty(resourcesList))
        {
            for (Resources res : resourcesList)
            {
                if (set.contains(res.getResId()))
                    res.setChecked("true");
                else
                {
                    res.setChecked("false");
                }
            }
        }
        return resourcesList;
    }
    
    /**
     * 对应用户的功能点菜单
     * 
     * @param user
     * @return
     */
    public List<Resources> getSysMenu(ISysUser user)
    {
        String rolealias = user.getRoles();
        if (StringUtil.isNotEmpty(rolealias))
        {
            String[] arrys = rolealias.split(",");
            rolealias = "";
            if (arrys.length > 0)
            {
                for (int i = 0; i < arrys.length; i++)
                {
                    rolealias = rolealias + "'" + arrys[i] + "',";
                }
                rolealias = rolealias.substring(0, rolealias.length() - 1);
            }
        }
        
        List<Resources> resourcesList = new ArrayList<Resources>();
        
        if (UserContextUtil.isSuperAdmin())
        {
            resourcesList = this.resourcesDao.getSuperMenu(user.getUserId());
        }
        else if (StringUtil.isNotEmpty(rolealias))
            // 通过角色别名获得显示菜单的功能点
            resourcesList = this.resourcesDao.getNormMenuByAllRole(rolealias);
        else
        {
            // 通过用户id获取显示菜单的功能点
            resourcesList = this.resourcesDao.getNormMenuByUser(user.getUserId());
        }
        
        return resourcesList;
    }
    
    /**
     * 是否存在该别名的功能点
     * 
     * @param resources
     * @return
     */
    public Integer isAliasExists(Resources resources)
    {
        String alias = resources.getAlias();
        return this.resourcesDao.isAliasExists(alias);
    }
    
    /**
     * 通过返回的整数判断该功能点是否别名重复或者是更新了新的
     * 
     * @param resources
     * @return
     */
    public Integer isAliasExistsForUpd(Resources resources)
    {
        String alias = resources.getAlias();
        Long resId = resources.getResId();
        Resources res = resourcesDao.getById(resId);
        if (alias.equals(res.getAlias()))
        { // 未更改直接返回
            return 0;
        }
        return this.resourcesDao.isAliasExistsForUpd(resId, alias);
    }
    
    public Resources getByUrl(String url)
    {
        List<Resources> list = this.resourcesDao.getByUrl(url);
        if (list.size() != 0)
        {
            return (Resources)list.get(0);
        }
        return null;
    }
    
    public List<Resources> getByParentId(Long id)
    {
        return this.resourcesDao.getByParentId(id.longValue());
    }
    
    public void move(Long sourceId, Long targetId)
    {
        Resources source = (Resources)this.resourcesDao.getById(sourceId);
        if (targetId.longValue() == 0L)
        {
            source.setParentId(Long.valueOf(0L));
            source.setPath(sourceId.toString());
        }
        else
        {
            Resources target = (Resources)this.resourcesDao.getById(targetId);
            
            source.setParentId(target.getResId());
            
            if (StringUtil.isNotEmpty(target.getPath()))
                source.setPath(target.getPath() + ":" + sourceId);
            else
            {
                source.setPath(sourceId.toString());
            }
        }
        this.resourcesDao.update(source);
    }
    
    /**
     * 图标路径
     * 
     * @param list
     * @param ctxPath
     */
    public static void addIconCtxPath(List<Resources> list, String ctxPath)
    {
        for (Iterator it = list.iterator(); it.hasNext();)
        {
            Resources res = (Resources)it.next();
            String icon = res.getIcon();
            String path = ctxPath + "/";
            if (StringUtil.isNotEmpty(icon))
                if (icon.contains(path))
                {
                    res.setIcon(icon);
                }
                else
                {
                    res.setIcon(ctxPath + icon);
                }
            if (res.getChildMenuList().size() != 0)
            {
                addIconCtxPath(res.getChildMenuList(), ctxPath);
            }
            
        }
    }
    
    public void updSn(Long resId, long sn)
    {
        this.resourcesDao.updSn(resId, sn);
    }
    
    public Resources getByAlias(String alias)
    {
        return this.resourcesDao.getByAlias(alias);
    }
    
    /** @deprecated */
    public List<Resources> getByParentUserId(Long resId, Long userId)
    {
        return this.resourcesDao.getByParentUserId(resId, userId);
    }
    
    public List<Resources> getNormMenuByAllRoleParentId(Long resId, String rolealias)
    {
        if (StringUtil.isNotEmpty(rolealias))
        {
            String[] arrys = rolealias.split(",");
            rolealias = "";
            if (arrys.length > 0)
            {
                for (int i = 0; i < arrys.length; i++)
                {
                    rolealias = rolealias + "'" + arrys[i] + "',";
                }
                rolealias = rolealias.substring(0, rolealias.length() - 1);
            }
        }
        
        return this.resourcesDao.getNormMenuByAllRoleParentId(resId, rolealias);
    }
    
    public String exportXml(long resId, Map<String, Boolean> map)
        throws Exception
    {
        Resources resources = (Resources)this.resourcesDao.getById(Long.valueOf(resId));
        ResourcesXmlList resXmlList = new ResourcesXmlList();
        ResourcesXml resourcesXml = new ResourcesXml();
        if (BeanUtils.isNotEmpty(resources))
        {
            List resList = new ArrayList();
            resourcesXml = getResourcesXml(resourcesXml, resources);
            resList.add(resourcesXml);
            resXmlList.setResourcesXmlList(resList);
        }
        return XmlBeanUtil.marshall(resXmlList, ResourcesXmlList.class);
    }
    
    private ResourcesXml getResourcesXml(ResourcesXml resXml, Resources res)
    {
        resXml.setResour(res);
        List<Resources> resList = getByParentId(res.getResId());
        if (BeanUtils.isNotEmpty(resList))
        {
            List resourcesXmls = resXml.getResourcesList();
            for (Resources resource : resList)
            {
                ResourcesXml resourcesXml = new ResourcesXml();
                resourcesXml = getResourcesXml(resourcesXml, resource);
                resourcesXmls.add(resourcesXml);
            }
            resXml.setResourcesList(resourcesXmls);
        }
        return resXml;
    }
    
    public void importXml(InputStream inputStream, long resId)
        throws Exception
    {
        String xml = FileUtil.inputStream2String(inputStream);
        Document doc = Dom4jUtil.loadXml(xml);
        Element root = doc.getRootElement();
        XmlUtil.checkXmlFormat(root, "res", "resources");
        ResourcesXmlList resXmlList = (ResourcesXmlList)XmlBeanUtil.unmarshall(xml, ResourcesXmlList.class);
        addResource((ResourcesXml)resXmlList.getResourcesXmlList().get(0), resId);
    }
    
    private void addResource(ResourcesXml resXml, long parentId)
    {
        Resources res = resXml.getResour();
        Long genId = Long.valueOf(UniqueIdUtil.genId());
        res.setParentId(Long.valueOf(parentId));
        res.setResId(genId);
        add(res);
        List<ResourcesXml> resXmlList = resXml.getResourcesList();
        for (ResourcesXml resourcesXml : resXmlList)
            addResource(resourcesXml, genId.longValue());
    }
    
    /**
     * 获取首页树菜单 by YangBo
     * 
     * @param resourcesList
     */
    public List<Resources> getMenuTree(List<Resources> resourcesList)
    {
        List<Resources> menuTree = new ArrayList<Resources>();
        for (Resources resources : resourcesList)
        {
            if (resources.getParentId() == 2)
            {
                // 获取该节点下子节点
                this.getChildMenus(resources.getResId(), resourcesList, resources);
                menuTree.add(resources);
            }
        }
        return menuTree;
    }
    
    /**
     * 遍历添加子节点 by YangBo
     * 
     * @param resourcesList supResources
     */
    private void getChildMenus(Long resId, List<Resources> resourcesList, Resources supResources)
    {
        List<Resources> menuTree = new ArrayList<Resources>();
        for (Resources resources : resourcesList)
        {
            if (resources.getParentId().equals(resId))
            {
                supResources.setChildMenuList(menuTree);
                this.getChildMenus(resources.getResId(), resourcesList, resources);
                menuTree.add(resources);
            }
        }
    }
    
    @Override
    public void setUserMenuToRedis()
    {
        //TransactionAspectSupport.currentTransactionStatus();
        // 通过参数判断是否需要放置到redis中
        int setRedis = sysParameterService.getIntByAlias(user_menu_redisset);
        if (setRedis == 0)
        {
            return;
        }
        String dataSource = SysContextUtil.getTableSpace();
        // 获取所有用户
        ISysUserService sysUserService = AppUtil.getBean(ISysUserService.class);
        List<? extends ISysUser> sysUsers = sysUserService.getAll();
        for (ISysUser user : sysUsers)
        {
            String key = menu_redis_prekey + dataSource + "." + user.getUserId();
            //String menus = RedisClient.get(key, String.class);
            //if(menus!=null){ //如果redis中已经存在，不需要在获取menu重新放一遍 continue; }
            /*if(user.getUserId()==-3) {
                logger.error(user.getUsername() + ":菜单权限初始化出错");
            }*/
            List<Resources> resourcesList = this.getSysMenu(user);
            List<Resources> menuList = this.getMenuTree(resourcesList);
            if (menuList == null || menuList.size() < 1)
            {
                RedisClient.del(key);
                continue;
            }
            try
            {
                RedisClient.set(key, JSON.toJSONString(menuList));
            }
            catch (Exception e)
            {
                logger.error(user.getUsername() + ":菜单权限初始化出错");
            }
        }
        
    }
    
    @Override
    public void setUserMenuToRedis(Long roleId)
    {
        // 通过参数判断是否需要放置到redis中
        int setRedis = sysParameterService.getIntByAlias(user_menu_redisset);
        if (setRedis == 0)
        {
            return;
        }
        String dataSource = SysContextUtil.getTableSpace();
        // 获取所有用户
        ISysUserService sysUserService = AppUtil.getBean(ISysUserService.class);
        List<? extends ISysUser> sysUsers = sysUserService.getByRoleId(roleId);
        for (ISysUser user : sysUsers)
        {
            String key = menu_redis_prekey + dataSource + "." + user.getUserId();
            List<Resources> resourcesList = this.getSysMenu(user);
            List<Resources> menuList = this.getMenuTree(resourcesList);
            //ResourcesService.addIconCtxPath(menuList, AppUtil.getContextPath());
            try
            {
                RedisClient.set(key, JSON.toJSONString(menuList));
            }
            catch (Exception e)
            {
                logger.error(user.getUsername() + ":菜单权限初始化出错");
            }
        }
        
    }
    
    /**
     * @param userId 用户ID
     * @return 返回对象为json串，可以直接转成List 返回值不为空说明用户对sysname拥有权限
     */
    @Override
    public String getSysRightByUser(Long userId)
    {
        String dataSource = SysContextUtil.getTableSpace();
        String key = menu_redis_prekey + dataSource + "." + userId;
        logger.info("---get user system right by key---:"+key);
        String menus = RedisClient.get(key, String.class);
        logger.info("---get user system right by key result---:"+menus);
        // 如果为空是否要从数据库中查找
        return menus;
    }
    public List<IResources> getDatatemplateRes(Long userId){
    	String roleIds = sysRoleService.getRoleIdsByUserId(userId);
    	return (List<IResources>)(List)(resourcesDao.getDatatemplateRes(roleIds));
    }
    /**
     * 根据用户id更新redis中用户权限数据
     * @date 2017年10月10日上午10:55:58
     * @param userId
     * @return
     */
    public void updSysRightByUserId(Long userId)
    {
        String dataSource = SysContextUtil.getTableSpace();
        String key = menu_redis_prekey + dataSource + "." + userId;
        ISysUserService sysUserService = AppUtil.getBean(ISysUserService.class);
        ISysUser sysUser = sysUserService.getById(userId);
        List<Resources> resourcesList = this.getSysMenu(sysUser);
        List<Resources> menuList = this.getMenuTree(resourcesList);
        if (menuList == null || menuList.size() < 1)
        {
            RedisClient.del(key);
            return;
        }
        try
        {
            RedisClient.set(key, JSON.toJSONString(menuList));
        }
        catch (Exception e)
        {
            logger.error(sysUser.getUsername() + ":菜单权限更新出错");
        }
    }
}
