package com.cssrc.ibms.api.report.inf;

import java.util.List;

import com.cssrc.ibms.api.report.model.IReportParam;

public interface IReportParamService {
	/**
	 * 获取报表参数列表
	 * @param reportid
	 * @return
	 */
	public abstract List<?extends IReportParam> getByReportid(Long reportid);

	public abstract IReportParam getById(Long valueOf);

}