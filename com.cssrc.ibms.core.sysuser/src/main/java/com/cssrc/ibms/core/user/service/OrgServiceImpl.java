package com.cssrc.ibms.core.user.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.user.intf.IOrgHandler;
import com.cssrc.ibms.core.user.intf.IOrgService;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.util.bean.BeanUtils;
/**
 * 
 * <p>Title:OrgServiceImpl</p>
 * @author Yangbo 
 * @date 2016-8-4下午03:39:47
 */
public class OrgServiceImpl implements IOrgService {

	@Resource
	GroovyScriptEngine groovyScriptEngine;
	@Resource
	SysOrgService sysOrgService;
	public static Long TopOrgId = Long.valueOf(0L);

	private Map<String, IOrgHandler> handMap = new HashMap<String, IOrgHandler>();

	public void setHandMap(Map<String, IOrgHandler> map) {
		this.handMap = map;
	}

	public SysOrg getSysOrgByScope(String type, String value) {
		SysOrg sysOrg = null;

		if ("system".equals(type)) {
			IOrgHandler handler = (IOrgHandler) this.handMap.get(value);
			sysOrg = handler.getByType(value);
			
		}else {
			try {
				Long orgId = (Long) this.groovyScriptEngine.executeObject(
						value, null);
				if (TopOrgId.equals(orgId)) {
					sysOrg = OrgHelper.getTopOrg();
				} else
					sysOrg = (SysOrg) this.sysOrgService.getById(orgId);
			} catch (Exception e) {
				e.printStackTrace();
				sysOrg = OrgHelper.getTopOrg();
			}
		}
		if (BeanUtils.isEmpty(sysOrg))
			sysOrg = OrgHelper.getTopOrg();
		return sysOrg;
	}
}
