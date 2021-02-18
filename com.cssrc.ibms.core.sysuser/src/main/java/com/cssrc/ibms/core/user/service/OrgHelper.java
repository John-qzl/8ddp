package com.cssrc.ibms.core.user.service;

import java.util.List;

import javax.annotation.Resource;

import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
/**
 * 
 * <p>Title:OrgHelper</p>
 * @author Yangbo 
 * @date 2016-8-4下午03:41:57
 */
public class OrgHelper {
	@Resource
	static
	SysOrgService sysOrgService;
	
	public static  SysOrg getTopOrg() {
		SysOrg org=null;
		//获取行政维度的最顶层的org节点。
		List<SysOrg> sysOrgList = getSysOrgService().getOrgByOrgSupId(Long.valueOf(0L));
		if(!CommonTools.isEmptyList(sysOrgList)){
			for(SysOrg sysOrg :sysOrgList){
				Long demId = sysOrg.getDemId();
				if(demId !=null && demId == 1001L){
					org = sysOrg;
				}
			}
		}
		/*if(org.getOrgId()==null){
			org.setOrgName("行政维度");
			org.setOrgId(Long.valueOf(1L));
			org.setDemId(Long.valueOf(1L));
			org.setOrgSupId(Long.valueOf(0L));
			org.setIsRoot(Short.valueOf((short) 1));
			org.setPath("1.");
		}*/
		
		return org;
	}
	private static   SysOrgService getSysOrgService(){
		if(sysOrgService==null){
			sysOrgService = (SysOrgService)AppUtil.getBean(SysOrgService.class);
		}
		return sysOrgService;
	}
	
}
