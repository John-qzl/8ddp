package com.cssrc.ibms.api.report.inf;

import java.util.List;
import java.util.Map;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.report.model.IOfficeItem;
import com.cssrc.ibms.api.report.model.IOfficeTemplate;
import com.cssrc.ibms.core.util.result.ResultMessage;

public interface IOfficeTemplateService {

	/**
	 * 根据officeid 获取所有书签
	 * @param officeId
	 * @return
	 */
	public abstract List<?extends IOfficeItem> getItemByOfficeId(Long officeId);

	/**
	 * 获取所有表,只读取id,name,desc等重要数据
	 * @param params
	 * @return
	 */
	public abstract List<?> getTabs(Map<String, String> params);

	/**
	 * 获取所有表
	 * @param params
	 * @return
	 */
	public abstract List<Map<String, String>> getColumnsByTabIds(String[] tabids);

	/**
	 * 获取tab列表
	 * @param ids id数组
	 * @return
	 */
	public abstract List<?> getTablesByIds(String[] ids);


	public abstract String getDataByFiledName(Map<String, Object> data,
			IFormField field);

	/**
	 * 根据模板标题取得报告模板。
	 * @param tableId
	 */
	public abstract IOfficeTemplate getByTitle(String title);

	public abstract ResultMessage delOfficeById(Long[] lAryId);

}