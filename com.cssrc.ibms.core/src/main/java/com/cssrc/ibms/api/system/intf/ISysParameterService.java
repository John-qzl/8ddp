package com.cssrc.ibms.api.system.intf;

import java.util.List;

import com.cssrc.ibms.api.system.model.ICurrentSystem;
import com.cssrc.ibms.api.system.model.ISysParameter;

public interface ISysParameterService {
    public static String taskReminderConf="task.WarnLevel";

	/**
	 * 根据参数名称获取参数数据
	 * 
	 * @param paramName
	 * @return
	 */
	public abstract List<? extends ISysParameter> getByParamName(
			String paramName);

	/**
	 * 判断参数名称是否存在
	 * 
	 * @param jobCode
	 * @return
	 */
	public abstract boolean isExistParamName(String paramname);

	public abstract boolean isExistParam(String paramname, Long id);

	public abstract String getByAlias(String alias);

	public abstract String getByAlias(String alias, String defaultValue);

	public abstract Integer getIntByAlias(String alias);

	public abstract Integer getIntByAlias(String alias, Integer defaulValue);

	public abstract Long getLongByAlias(String alias);

	public abstract boolean getBooleanByAlias(String alias);

	public abstract boolean getBooleanByAlias(String alias, boolean defaulValue);

	public ICurrentSystem getCurrentSystem();
	
	public abstract String getOneParameter(String name);
	
	public abstract ISysParameter getById(Long id);
	
	/**
	 * 根据数据类型判断是否开启数据同步功能
	 * @param model
	 * @return
	 */
	public boolean isSyncMdm(Object model);
}