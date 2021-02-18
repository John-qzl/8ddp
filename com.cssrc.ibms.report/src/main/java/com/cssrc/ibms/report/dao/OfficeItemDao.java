package com.cssrc.ibms.report.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.report.model.OfficeItem;

@Repository
public class OfficeItemDao extends BaseDao<OfficeItem>{

	@Override
	public Class<?> getEntityClass() {
		return OfficeItem.class;
	}

	public List<OfficeItem> getItemByOfficeId(Long officeId) {
		return  this.getBySqlKey("getItemByOfficeId",officeId);
	}

	/**
	 * 删除所有老的书签
	 * @param officeid
	 */
	public int deleteByOfficeId(Long officeId) {
		return this.delBySqlKey("deleteByOfficeId", officeId);
	}

    /**
     * 删除 标签 逻辑删除
     * @param id
     * @return
     */
    public int updateDelete(OfficeItem officeItem)
    {
        return this.update("updateDelete", officeItem);
    }

    /**
     * 删除 标签 逻辑删除 根据officeId删除书签
     * @param item
     */
    public int updateDeleteByOfficeId(OfficeItem officeItem)
    {
        return this.update("updateDeleteByOfficeId", officeItem);
    }
	
	
}