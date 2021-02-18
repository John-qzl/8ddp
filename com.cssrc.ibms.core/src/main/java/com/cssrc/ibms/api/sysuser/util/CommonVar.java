package com.cssrc.ibms.api.sysuser.util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;

/**
 * 常用变量
 * @author zhulongchao
 *
 */
public class CommonVar {
	//字段名
	private String name;
	
	private String alias;
	
	//字段值
	private Object value;
	
	private boolean isNumber = false;
	
	public CommonVar() {
		super();
	}

	public CommonVar(String name, String alias, Object value, boolean isNumber_) {
		super();
		this.name = name;
		this.alias = alias;
		this.value = value;
		this.isNumber = isNumber_;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean isNumber() {
		return isNumber;
	}

	public void setNumber(boolean isNumber) {
		this.isNumber = isNumber;
	}
	
	public static List<CommonVar> getCurrentVars(boolean isRunTime)
	{
		List list = new ArrayList();
		Long userId = Long.valueOf(0L);
		Long orgId = Long.valueOf(0L);
		Long companyId = Long.valueOf(0L);
		Long posId = Long.valueOf(0L);
		String account = "";
		if (isRunTime) {
			ISysUser user = (ISysUser)UserContextUtil.getCurrentUser();
			userId = user.getUserId();
			account = user.getUsername();
			companyId = UserContextUtil.getCurrentCompanyId();
			orgId = UserContextUtil.getCurrentOrgId();
			posId = UserContextUtil.getCurrentPosId();
		}

		CommonVar varUser = new CommonVar("当前用户ID", "[CUR_USER]", userId, true);
		CommonVar varAccount = new CommonVar("当前用户帐号", "[CUR_ACCOUNT]", account, false);
		CommonVar varCompany = new CommonVar("当前公司", "[CUR_COMPANY]", companyId, true);
		CommonVar varOrg = new CommonVar("当前组织", "[CUR_ORG]", orgId, true);
		CommonVar varPos = new CommonVar("当前岗位", "[CUR_POS]", posId, true);

		list.add(varUser);
		list.add(varAccount);
		list.add(varCompany);
		list.add(varOrg);
		list.add(varPos);

		return list;
	}
	
	public static List<CommonVar> getCurrentVars(boolean isRunTime, ISysUser curUser, ISysOrg curOrg, IPosition curPos)
	{
		List list = new ArrayList();
		Long userId = Long.valueOf(0L);
		Long orgId = Long.valueOf(0L);
		Long companyId = Long.valueOf(0L);
		Long posId = Long.valueOf(0L);
		String account = "";
		if (isRunTime) {
			if(curUser != null){
				userId = curUser.getUserId();
				account = curUser.getUsername();
			}else{
				ISysUser user = (ISysUser)UserContextUtil.getCurrentUser();
				userId = user.getUserId();
				account = user.getUsername();
			}
			if(curOrg != null){
				orgId = curOrg.getOrgId();
			}else{
				orgId = UserContextUtil.getCurrentOrgId();
			}
			if(curPos != null){
				posId = curPos.getPosId();
			}else{
				posId = UserContextUtil.getCurrentOrgId();
			}
			companyId = UserContextUtil.getCurrentCompanyId();
		}

		CommonVar varUser = new CommonVar("当前用户ID", "[CUR_USER]", userId, true);
		CommonVar varAccount = new CommonVar("当前用户帐号", "[CUR_ACCOUNT]", account, false);
		CommonVar varCompany = new CommonVar("当前公司", "[CUR_COMPANY]", companyId, true);
		CommonVar varOrg = new CommonVar("当前组织", "[CUR_ORG]", orgId, true);
		CommonVar varPos = new CommonVar("当前岗位", "[CUR_POS]", posId, true);

		list.add(varUser);
		list.add(varAccount);
		list.add(varCompany);
		list.add(varOrg);
		list.add(varPos);

		return list;
	}

	public static void setCurrentVars(Map<String, Object> vars)
	{
		List<CommonVar> commonVars = getCurrentVars(true);
		for (CommonVar var : commonVars)
			vars.put(var.getAlias(), var.getValue());
	}
	
//	/**
//	 * 常用变量
//	 * 
//	 * @return
//	 */
//	public static List<CommonVar> geCommonVars() {
//		List<CommonVar> list = new ArrayList<CommonVar>();
//		list.add(new CommonVar("当前用户", "[CUR_USER]",userId, true));
//		list.add(new CommonVar("[CUR_ORG]", "当前组织"));
//		return list;
//	}
//	
//	/**
//	 * 设置常用变量
//	 * 
//	 * @param params 参数
//	 * @param curUserId 当前用户ID
//	 * @param curOrgId 当前组织ID
//	 */
//	public static void setCommonVar(Map<String, Object> params, Long curUserId,
//			Long curOrgId) {
//		params.put("[CUR_USER]", curUserId);
//		params.put("[CUR_ORG]", curOrgId);
//	}
	
}
