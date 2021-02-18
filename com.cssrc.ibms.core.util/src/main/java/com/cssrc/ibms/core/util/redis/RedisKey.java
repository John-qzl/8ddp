package com.cssrc.ibms.core.util.redis;



/**
 * Description:Top-key管理类(Top-key)
 * <p>RedisKey.java</p>
 * @author dengwenjie 
 * @date 2017年6月8日
 */
public class RedisKey{
	/** 用户个性化设置信息 */
	public final static String CUSTOM_INFO_PREFIX = "user.custom.info.";
	public final static String CUSTOM_INFO_SET = "user.custom.infoset";
	/** 所有用户信息*/
	public final static String ALL_SYSUSER_INF = "allSysUserInf";
	/** 所有组织信息*/
	public final static String ALL_SYSORG_INF = "allSysOrgInf";
	/** 所有岗位信息*/
	public final static String ALL_POSITION_INF = "allPositionInf";
	/** 所有角色信息*/
	public final static String ALL_SYSROLE_INF = "allSysRoleInf";
}