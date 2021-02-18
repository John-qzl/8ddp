/**
 * 参数
 */
package com.cssrc.ibms.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.cache.intf.ICache;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.dao.SysParameterDao;
import com.cssrc.ibms.system.model.CurrentSystem;
import com.cssrc.ibms.system.model.SysParameter;

/**
 * 参数service
 * 
 * @see
 */
@Service
public class SysParameterService extends BaseService<SysParameter> implements
		ISysParameterService {
	@Resource
	private SysParameterDao dao;

	protected IEntityDao<SysParameter, Long> getEntityDao() {
		return this.dao;
	}

	/**
	 * 根据参数名称获取参数数据
	 * 
	 * @param paramName
	 * @return
	 */
	public List<SysParameter> getByParamName(String paramName) {
		return this.dao.getByParamName(paramName);
	}

	/**
	 * 判断参数名称是否存在
	 * 
	 * @param jobCode
	 * @return
	 */
	public boolean isExistParamName(String paramname) {
		return this.dao.isExistParamName(paramname);
	}

	public boolean isExistParam(String paramname, Long id) {
		return this.dao.isExistParam(paramname, id);
	}

	private static Map<String, String> reloadProperty() {
		ICache cache = (ICache) AppUtil.getBean(ICache.class);
		SysParameterService propertyService = (SysParameterService) AppUtil
				.getBean(SysParameterService.class);
		Map map = new HashMap();
		List<SysParameter> list = propertyService.getAll();
		for (SysParameter property : list) {
			map.put(property.getParamname().toLowerCase(),
					property.getParamvalue());
		}
		cache.add("PropertyCache", map);

		return map;
	}

	public String getByAlias(String alias) {
		ICache cache = (ICache) AppUtil.getBean(ICache.class);
		Map map = (Map) cache.getByKey("PropertyCache");
		if (BeanUtils.isEmpty(map)) {
			map = reloadProperty();
		}
		return (String) map.get(alias.toLowerCase());
	}
	
	@SuppressWarnings("unchecked")
	public void updateCache(SysParameter sysParameter){
		ICache cache = (ICache) AppUtil.getBean(ICache.class);
		Map<String,Object> map = (Map<String,Object>) cache.getByKey("PropertyCache");
		if(map==null){
			map=new HashMap<String,Object>();
		}
		map.put(sysParameter.getParamname().toLowerCase(),
				sysParameter.getParamvalue());
		cache.add("PropertyCache", map);
	}
	
	
	public String getByAlias(String alias, String defaultValue) {
		String val = getByAlias(alias);
		if (StringUtil.isEmpty(val))
			return defaultValue;
		return val;
	}

	public Integer getIntByAlias(String alias) {
		String val = getByAlias(alias);
		if (StringUtil.isEmpty(val))
			return Integer.valueOf(0);
		Integer rtn = Integer.valueOf(Integer.parseInt(val));
		return rtn;
	}

	public Integer getIntByAlias(String alias, Integer defaulValue) {
		String val = getByAlias(alias);
		if (StringUtil.isEmpty(val))
			return defaulValue;
		Integer rtn = Integer.valueOf(Integer.parseInt(val));
		return rtn;
	}

	public Long getLongByAlias(String alias) {
		String val = getByAlias(alias);
		Long rtn = Long.valueOf(Long.parseLong(val));
		return rtn;
	}

	public boolean getBooleanByAlias(String alias) {
		String val = getByAlias(alias);
		return Boolean.parseBoolean(val);
	}

	public boolean getBooleanByAlias(String alias, boolean defaulValue) {
		String val = getByAlias(alias);
		if (StringUtil.isEmpty(val))
			return defaulValue;

		if ("1".equals(val))
			return true;

		return Boolean.parseBoolean(val);
	}

	/**
	 * 获取当前项目系统基本参数
	 * 
	 * @return
	 */
	public CurrentSystem getCurrentSystem() {
		List<SysParameter> lists = this.getAll();
		Map<String, String> map = new HashMap();
		CurrentSystem cur = new CurrentSystem();
		for (SysParameter syspara : lists) {
			String parametername = syspara.getParamname();
			if (parametername.equals("SYSTEM_TITLE")) {
				//
				cur.setSystemName(syspara.getParamvalue());
			}
			if (parametername.equals("COMPANY_NAME")) {
				//
				map.put("COMPANY_NAME", syspara.getParamvalue());
				cur.setCompanyName(syspara.getParamvalue());
			}
			if (parametername.equals("version")) {
				//
				map.put("version", syspara.getParamvalue());
				cur.setVersion(syspara.getParamvalue());
			}
			if (parametername.equals("SYSTEM_TITLE_LOGO")) {
				//首页top栏左侧图标
				String theme = PropertyUtil.getValueByName("SYS_THEME");//获取主题
				int type = SysConfConstant.SHOW_TYPE;
				String systemLogStr = syspara.getParamvalue();
				if(StringUtil.isNotEmpty(systemLogStr) && type==1){
					systemLogStr = "/styles/"+theme+systemLogStr;
				}
				cur.setSystemLog(systemLogStr);
			}
			if(parametername.equals("SYS_THEME")){
				//default  获取主题
				cur.setSkinStyle(syspara.getParamvalue());
			}
			//界面显示参数
			if(parametername.equals("SYS_UITYPE")){
				SysConfConstant.SHOW_TYPE =Integer.parseInt(syspara.getParamvalue());
			}
			//系统主页地址
			if(parametername.equals("SYSTEM_URL")){
				cur.setSystemUrl(syspara.getParamvalue());
			}
		}
		return cur;
	}
	
    public boolean isSyncMdm(Object model)
    {
        if (model instanceof ISysUser)
        {
            // 增加系统参数 是否开启用户数据同步功能
            return getIntByAlias("mdm.user.sync", 0) == 1 ? true : false;
        }
        else if (model instanceof ISysRole)
        {
            // 增加系统参数 是否开启角色数据同步功能
            return getIntByAlias("mdm.role.sync", 0) == 1 ? true : false;

        }
        else if (model instanceof IPosition)
        {
            // 增加系统参数 是否开启岗位数据同步功能
            return getIntByAlias("mdm.pos.sync", 0) == 1 ? true : false;

        }
        else if (model instanceof ISysOrg)
        {
            // 增加系统参数 是否开启用户数据同步功能
            return getIntByAlias("mdm.org.sync", 0) == 1 ? true : false;

        }
        else if (model instanceof IUserPosition)
        {
            // 增加系统参数 是否开启用户数据同步功能
            return getIntByAlias("mdm.userpos.sync", 0) == 1 ? true : false;

        }
        return false;
    }
	
	public String getOneParameter(String name){
		SysParameter sysParameter = this.dao.getOneByParamName(name);
		String value = sysParameter.getParamvalue();
		return value;
	}
	
    public void updCategory(String type, List<Long> parameterIdList)
    {
        dao.updCategory(type, parameterIdList);
    }
    
    public List<Map<String, Object>> getType()
    {
        return dao.getType();
    }
}
