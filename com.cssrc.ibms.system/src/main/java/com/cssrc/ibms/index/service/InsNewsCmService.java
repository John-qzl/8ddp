package com.cssrc.ibms.index.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.index.dao.InsNewsCmDao;
import com.cssrc.ibms.index.model.InsNewsCm;

/**
 * 新闻评论内容Service层
 * @author YangBo
 *
 */
@Service
public class InsNewsCmService extends BaseService<InsNewsCm>{
	@Resource
	private InsNewsCmDao dao;
	
	protected IEntityDao<InsNewsCm, Long> getEntityDao() {
		return this.dao;
	}
	
	/**
	 * 根据新闻Id获取新闻内容
	 */
	public List<InsNewsCm> getByNewId(Long newId)
	{
		return this.dao.getByNewId(newId);
	}
	
	/**
	 * 根据评论Id获取评论内容
	 * @param repId
	 * @return
	 */
	public List<InsNewsCm> getByReplyId(Long repId)
	{
		return this.dao.getByReplyId(repId);
	}
	
}
