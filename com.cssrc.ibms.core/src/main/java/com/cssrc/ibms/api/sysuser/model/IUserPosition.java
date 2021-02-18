package com.cssrc.ibms.api.sysuser.model;

public interface IUserPosition {

	public static final short PRIMARY_YES = 1;
	public static final short PRIMARY_NO = 0;
	public static final Short CHARRGE_YES = Short.valueOf((short) 1);
	public static final Short CHARRGE_NO = Short.valueOf((short) 0);
	public static final Short DELETE_YES = Short.valueOf((short) 1);
	public static final Short DELETE_NO = Short.valueOf((short) 0);
	public static final Short IS_GRADE_MANAGE = Short.valueOf((short) 1);
	public static final Short IS_NOT_GRADE_MANAGE = Short.valueOf((short) 0);

	public abstract String getFullname();

	public abstract void setFullname(String fullname);

	public abstract Long getJobId();

	public abstract void setJobId(Long jobId);

	public abstract String getJobName();

	public abstract void setJobName(String jobName);

	public abstract String getPosName();

	public abstract void setPosName(String posName);

	public abstract Long getUserPosId();

	public abstract void setUserPosId(Long userPosId);

	public abstract Long getOrgId();

	public abstract void setOrgId(Long orgId);

	public abstract Long getPosId();

	public abstract void setPosId(Long posId);

	public abstract Long getUserId();

	public abstract void setUserId(Long userId);

	public abstract Short getIsPrimary();

	public abstract void setIsPrimary(Short isPrimary);

	public abstract Short getIsCharge();

	public abstract void setIsCharge(Short isCharge);

	public abstract Short getIsDelete();

	public abstract void setIsDelete(Short isDelete);

	public abstract String getCompany();

	public abstract void setCompany(String company);

	public abstract Long getCompanyId();

	public abstract void setCompanyId(Long companyId);

	public abstract Short getStatus();

	public abstract void setStatus(Short status);

	public abstract String getUsername();

	public abstract void setUsername(String username);

	public abstract String getOrgName();

	public abstract void setOrgName(String orgName);

	public abstract String getChargeName();

	public abstract void setChargeName(String chargeName);

	public abstract boolean equals(Object object);

	public abstract int hashCode();

	public abstract String toString();

}