package com.cssrc.ibms.api.rec.intf;

import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.rec.model.IRecRoleFun;
import com.cssrc.ibms.api.rec.model.IRecRoleSonFun;
/**
 * Description:
 * <p>IRecRoleFunService.java</p>
 * @author dengwenjie 
 * @date 2017年3月27日
 */
public interface IRecRoleFunService {
	public List<? extends IRecRoleFun> getByRoleAliasFun(String roleAlias, Long funId);
	/**
	 * @param roleAlias :角色别名集合
	 * @param funId： 功能点id
	 * @return:此功能点对应的按钮权限集合
	 */
	public Map<String,Boolean> getButtonPemission(String roleAlias, Long funId);
}
