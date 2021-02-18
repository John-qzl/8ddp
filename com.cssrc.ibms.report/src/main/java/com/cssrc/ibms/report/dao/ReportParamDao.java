package com.cssrc.ibms.report.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.report.model.ReportParam;
/**
 *<pre>
 * 对象功能:报表管理
 * 开发人员:zxg 
 *</pre>
 */
@Repository
public class ReportParamDao extends BaseDao<ReportParam>
{
	@Override
	public Class<?> getEntityClass()
	{
		return ReportParam.class;
	}
	
	/**
	 * 根据表单的tableId取得报告模板。
	 * @param tableId
	 */
	public ReportParam getByName(String name) {
		return  this.getUnique("getByName",name);
	}

	/**
	 * 获取报表参数列表
	 * @param reportid
	 * @return
	 */
	public List<ReportParam> getByReportid(Long reportid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("reportid", reportid);
		return  this.getBySqlKey("getByReportid",params);
	}

	/**
	 * 根据报表模板删除参数
	 * @param reportid
	 */
	public int delByReportId(Long reportid) {
		return this.delBySqlKey("delByReportId",reportid);
	}
}