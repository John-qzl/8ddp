package com.cssrc.ibms.api.form.intf;

import java.util.List;

import com.cssrc.ibms.api.form.model.IQueryField;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IQueryFieldService {

	public abstract List<? extends IQueryField> getSysQueryFieldArr(
			String jsonArr);

	public abstract List<? extends IQueryField> getListBySqlId(Long sqlId);

	public abstract List<? extends IQueryField> getDisplayFieldListBySqlId(
			Long sqlId);

	public abstract List<? extends IQueryField> getConditionFieldListBySqlId(
			Long sqlId);

	public abstract void delBySqlIds(Long[] lAryId);

	public abstract List<? extends IQueryField> getAll(QueryFilter filter);

}