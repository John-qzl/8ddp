package com.cssrc.ibms.index.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.index.model.InsColumn;

/**
 * 首页布局栏目DAO层
 * @author YangBo
 *@date 2017-3-3上午09:47:14
 */
@Repository
public class InsColumnDao extends BaseDao<InsColumn>{

	public Class<InsColumn> getEntityClass() {
		return InsColumn.class;
	}
	
	/**
	 * 根据栏目名称获取栏目集合
	 * by YangBo
	 * @param name
	 * @param page
	 * @return
	 */
	public List<InsColumn> getByName(String name, PagingBean page){
		List<InsColumn> InsColumns = getBySqlKey("getByName", name,page);
		return InsColumns;
	}
	
	/**
	 * 获取可关闭和可用的栏目
	 * @return
	 */
	 public List<InsColumn> getByEnable(){
		 List<InsColumn> InsColumns = getBySqlKey("getByEnable",null);
		 return InsColumns;
	 }
	 
	 /**
	  * 获取使用某模板的栏目
	  * @param typeId
	  * @param orgId
	  * @return
	  */
	 public List<InsColumn> getByColType(Long typeId, String orgId){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("typeId", typeId);
			params.put("orgId", orgId);
			return getBySqlKey("getByColType", params);
	 }
	 
}
