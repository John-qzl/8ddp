package com.cssrc.ibms.api.form.intf;

import java.util.List;

import com.cssrc.ibms.api.form.model.IFormTemplate;
import com.cssrc.ibms.api.system.model.IMessageSend;

public interface IFormTemplateService {

	/**
	 * 根据模版别名取得模版。
	 * @param alias
	 * @return
	 */
	public abstract IFormTemplate getByTemplateAlias(String alias);

	/**
	 * 获取所有的系统原始模板
	 * @return
	 * @throws Exception 
	 */
	public abstract void initAllTemplate() throws Exception;

	/**
	 * 当模版数据为空时，将form目录下的模版添加到数据库中。
	 */
	public abstract void init() throws Exception;

	/**
	 * 检查模板别名是否唯一
	 * @param alias
	 * @return
	 */
	public abstract boolean isExistAlias(String alias);

	/**
	 * 将用户自定义模板备份
	 * @param id
	 */
	public abstract void backUpTemplate(Long id);

	/**
	 * 根据模版类型取得模版列表。
	 * @param type
	 * @return
	 */
	public abstract List<?extends IFormTemplate> getTemplateType(String type);

	/**
	 * 获取主表模版
	 * @return
	 */
	public abstract List<?extends IFormTemplate> getAllMainTableTemplate();

	/**
	 * 获取子表模版。
	 * @return
	 */
	public abstract List<?extends IFormTemplate> getAllSubTableTemplate();

	/**
	 * 获取关系表模版。
	 * @return
	 */
	public abstract List<?extends IFormTemplate> getAllRelTableTemplate();

	/**
	 * 获取宏模版。
	 * @return
	 */
	public abstract List<?extends IFormTemplate> getAllMacroTemplate();

	/**
	 * 获取表管理模版。
	 * @return
	 */
	public abstract List<?extends IFormTemplate> getAllTableManageTemplate();

	/**
	 * 获取列表模版。
	 * @return
	 */
	public abstract List<?extends IFormTemplate> getListTemplate();

	/**
	 * 获取明细模版。
	 * @return
	 */
	public abstract List<?extends IFormTemplate> getDetailTemplate();

	/**
	 * 获取数据模版。
	 * @return
	 */
	public abstract List<?extends IFormTemplate> getDataTemplate();

	public abstract List<?extends IFormTemplate> getQueryDataTemplate();

	/**
	 * 系统模板初始化
	 */
	public abstract void initTemplate();
	
	
	public abstract IFormTemplate getById(Long valueOf);


}