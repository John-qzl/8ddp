package com.cssrc.ibms.report.dao;


import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.report.model.OfficeTemplate;
@Repository
public class OfficeTemplateDao extends BaseDao<OfficeTemplate>{
	@Override
	public Class<?> getEntityClass() {
		return OfficeTemplate.class;
	}
	
	/**
	 * 根据模板标题取得报告模板。
	 * @param title
	 */
	public OfficeTemplate getByTitle(String title) {
		return  this.getUnique("getByTitle",title);
	}

    /**
     * 删除报表模板，逻辑删除.
     * @param lAryId
     * @return
     */
    public int updateDelete(OfficeTemplate officeTemplate)
    {
        return this.delBySqlKey("updateDelete", officeTemplate);
    }
}