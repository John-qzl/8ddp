package com.cssrc.ibms.api.form.intf;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cssrc.ibms.api.form.model.IQuerySetting;

public interface IQuerySettingService {

	public abstract IQuerySetting getBySqlId(Long sqlId) throws Exception;

	public abstract IQuerySetting getSysQuerySetting(String json);

	public abstract String getDisplay(Long id, Map<String, Object> params,
			Map<String, Object> queryParams) throws Exception;

	public abstract HSSFWorkbook export(Long id, int exportType,
			Map<String, Object> params) throws Exception;

	public abstract void delBySqlIds(Long[] lAryId);

}