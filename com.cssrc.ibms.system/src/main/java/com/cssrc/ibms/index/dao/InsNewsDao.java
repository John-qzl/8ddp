package com.cssrc.ibms.index.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.index.model.InsNews;


/**
 * 新闻公告DAO层
 * @author YangBo
 *
 */
@Repository
public class InsNewsDao extends BaseDao<InsNews>{

	public Class<InsNews> getEntityClass() {
		return InsNews.class;
	}
	
	/**
	 * 获取实时新闻
	 * @param orgId
	 * @param orderField
	 * @param orderSeq
	 * @param pb
	 * @return
	 */
	public List<InsNews> getPortalNews(String orderField,String orderSeq,PagingBean pb){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("orderField", orderField);
		params.put("orderSeq", orderSeq);
		params.put("status", "Issued");
		List<InsNews> list=getBySqlKey("getAll",params,pb);
		return list;
	}
}
