package com.cssrc.ibms.core.form.intf;

public interface IFormWebService {

	public abstract String getFormData(String tableName, String pkValue);

	public abstract boolean saveFormData(String tableName, String data,
			String pkId);

	public abstract boolean deleteFormData(String tableName, String pkId);

	public abstract String queryFormData(String queryName, String queryData);

}