package com.cssrc.ibms.core.user.service.handler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cssrc.ibms.api.core.util.ContextUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.user.intf.IOrgHandler;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.service.OrgHelper;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.util.bean.BeanUtils;
/**
 * 
 * <p>Title:GradeOrgHandler</p>
 * @author Yangbo 
 * @date 2016-8-8下午01:48:02
 */
public class GradeOrgHandler implements IOrgHandler {
	protected Map<String, String> aliasMap = new HashMap();

	@Resource
	SysOrgService orgService;

	public void setAliasMap(Map<String, String> aliasMap) {
		this.aliasMap = aliasMap;
	}

	public SysOrg getByType(String type) {
		SysOrg sysOrg = (SysOrg) UserContextUtil.getCurrentOrg();
		if (BeanUtils.isNotEmpty(sysOrg)) {
			Long gradeValue = getGradeByValue(type);
			while (!gradeValue.equals(sysOrg.getOrgType())) {
				sysOrg = (SysOrg) this.orgService.getById(sysOrg.getOrgSupId());
				if (BeanUtils.isEmpty(sysOrg)) {
					sysOrg = OrgHelper.getTopOrg();
					break;
				}
			}
		} else {
			sysOrg = OrgHelper.getTopOrg();
		}
		return sysOrg;
	}

	public Long getGradeByValue(String value) {
		return Long.valueOf(Long.parseLong((String) this.aliasMap.get(value)));
	}
}
