package com.cssrc.ibms.api.rec.intf;

import java.util.List;

import com.cssrc.ibms.api.rec.model.IRecRole;

public interface IRecRoleService {
	/**根据类别、及用户ID，得到用户的所属 项目角色别名*/
	public String getAllRoleAliasByType(Long userId,String typeAlias);
	/**根据类别、及用户ID、项目角色别名，得到是否属于该项目角色*/
	public boolean isRecRole(Long userId,String typeAlias,String recRoleAlias);
	/**根据类别、及用户ID、项目角色id，得到是否属于该项目角色*/
	public boolean isRecRole(Long userId,String typeAlias,Long recRoleId);  
	/**根据类别、及用户ID,得到用户的默认项目角色*/	
	public List<? extends IRecRole> getRolesByUT (Long userId,String typeAlias);  
}
