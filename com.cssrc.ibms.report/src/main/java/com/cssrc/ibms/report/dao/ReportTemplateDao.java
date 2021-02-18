package com.cssrc.ibms.report.dao;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.report.model.ReportTemplate;
/**
 *<pre>
 * 对象功能:报表管理
 * 开发人员:zxg 
 *</pre>
 */
@Repository
public class ReportTemplateDao extends BaseDao<ReportTemplate>
{
	@Override
	public Class<?> getEntityClass()
	{
		return ReportTemplate.class;
	}
	
	/**
	 * 根据模板标题取得报告模板。
	 * @param title
	 */
	public ReportTemplate getByTitle(String title) {
		return  this.getUnique("getByTitle",title);
	}

}