package com.cssrc.ibms.api.rec.intf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cssrc.ibms.api.rec.model.IRecRoleSon;
import com.cssrc.ibms.api.rec.model.IRecRoleSonUser;

public interface IRecRoleSonService {
	/**
	 * 根据类别、及用户ID，得到用户的所属 项目角色别名
	 * @param userId ： 用户ID
	 * @param typeAlias : 类别别名
	 * @return
	 */
	public String getAllRoleSonAliasByType(Long userId,Long dataId);
	/**
	 *判断该记录是否进行了记录角色的设置
	 * @param dataId
	 * @return
	 */
	public boolean isExistRoleSon(Long dataId);
	/**
	 * 根据业务数据模板、记录ID、类型
	 * 获取记录的相关角色信息，如果没有记录级的，则获取默认角色的
	 * @param dataId
	 * @return
	 */
	public List<? extends IRecRoleSon> getRecRoleSonInfo(Long dataTemplateId,Long dataId,String typeAlias) throws Exception;
	/**
	 * @param userId : 用户ID
	 * @param dataId : 记录ID
	 * @return ： 用户的所有默认项目角色
	 */
	public List<? extends IRecRoleSon> getRoleSonsByUT (Long userId,Long dataId);  
	
	/**
	 * @param roleSonId ：IBMS_REC_ROLESON表主键
	 * @return
	 */
	public List<? extends IRecRoleSonUser> getByRoleSonId(Long roleSonId);
	
	/**
	 * def_filter的格式：{"type":"role","id":"10000000290007,-1,-2"}
	 * type对应的类型为：[none\\everyone\\user\\role\\org\\orgMgr\\pos]
	 * id:对应的主键集合，逗号分隔
	 * 增加自定义过滤
	 * @param def_filter
	 * @param roleSonId
	 */
	public Object[] addDefFilter(String def_filter,Long roleSonId);
}
