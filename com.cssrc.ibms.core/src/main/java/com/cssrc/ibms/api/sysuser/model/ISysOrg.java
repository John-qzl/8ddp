package com.cssrc.ibms.api.sysuser.model;
/**
 * 组织接口
 * @author Yangbo 2016-7-26
 *
 */
public abstract interface ISysOrg
{
	public static final Long BEGIN_DEMID = Long.valueOf(1L);
	public static final Long BEGIN_ORGID = Long.valueOf(1L);
	public static final Integer BEGIN_DEPTH = Integer.valueOf(0);
	public static final String BEGIN_PATH = "1";
	public static final Short BEGIN_TYPE = 1;
	public static final Long BEGIN_ORGSUPID = Long.valueOf(-1L);
	public static final Short BEGIN_SN = 1;
	public static final Short BEGIN_FROMTYPE = 0;

	public static final Short FROMTYPE_AD = 1;
	public static final Short FROMTYPE_DB = 0;

	public static final Integer IS_LEAF_N = Integer.valueOf(1);
	public static final Integer IS_LEAF_Y = Integer.valueOf(0);
	public static final String IS_PARENT_N = "false";
	public static final String IS_PARENT_Y = "true";

	public abstract void setOrgId(Long paramLong);

	public abstract Long getOrgId();

	public abstract void setDemId(Long paramLong);

	public abstract Long getDemId();

	public abstract String getDemName();

	public abstract void setDemName(String paramString);

	public abstract void setOrgName(String paramString);

	public abstract String getOrgName();

	public abstract void setOrgDesc(String paramString);

	public abstract String getOrgDesc();

	public abstract void setOrgPathname(String paramString);

	public abstract String getOrgPathname();

	public abstract void setOrgSupId(Long paramLong);

	public abstract Long getOrgSupId();

	public abstract String getOrgSupName();

	public abstract void setOrgSupName(String paramString);

	public abstract void setPath(String paramString);

	public abstract String getPath();

	public abstract void setDepth(Integer paramInteger);

	public abstract Integer getDepth();

	public abstract void setOrgType(Long paramLong);

	public abstract Long getOrgType();

	public abstract Short getFromType();

	public abstract void setFromType(Short paramShort);

	public abstract Long getCompanyId();

	public abstract String getCode();

    public abstract String getViceLeader();

    public abstract String getLeader();
}

