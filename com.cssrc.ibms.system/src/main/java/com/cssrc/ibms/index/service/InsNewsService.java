package com.cssrc.ibms.index.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.model.BaseInsPortalParams;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.index.dao.InsColNewDao;
import com.cssrc.ibms.index.dao.InsColumnDao;
import com.cssrc.ibms.index.dao.InsNewsDao;
import com.cssrc.ibms.index.model.InsColNew;
import com.cssrc.ibms.index.model.InsNews;
import com.cssrc.ibms.index.model.InsPortalParams;

/**
 * 新闻公告Service层
 * @author YangBo
 *
 */
@Service
public class InsNewsService extends BaseService<InsNews>{
	@Resource
	private InsNewsDao dao;

	@Resource
	private InsColNewDao insColNewDao;

	@Resource
	private InsColumnDao insColumnDao;


	@Resource
	InsColNewService insColNewService;

	protected IEntityDao<InsNews, Long> getEntityDao() {
		return this.dao;
	}
	
	/**
	 * 发布新闻栏目
	 * @param insNews
	 * @param selColIds
	 * @param startTime
	 * @param endTime
	 * @param isLongValid
	 */
	public void doPublish(InsNews insNews, String selColIds, Date startTime, Date endTime, String isLongValid)
	{
	    insNews.setStatus("Issued");
		this.dao.update(insNews);
		if ("Issued".equals(insNews.getStatus())) {
			String[] selIds = selColIds.split(",");
			int sn = 0;
			for (String colId : selIds) {
				if (this.insColNewDao.booleanByColIdNewId(Long.valueOf(colId), insNews.getNewId()).booleanValue()) 
				{
					this.insColNewDao.delByColIdNewId(Long.valueOf(colId), insNews.getNewId());
				}
				
				/*InsColumn column = (InsColumn)this.insColumnDao.getById(Long.valueOf(colId));*/
				
				InsColNew colNew = new InsColNew();
				Date currdate = new Date();
				String userId = UserContextUtil.getCurrentUserId().toString(); 
				String orgId = UserContextUtil.getCurrentOrgId().toString();
				
				insNews.setCreateBy(userId);
				insNews.setCreateTime(currdate);
				insNews.setUpdateBy(userId);
				insNews.setUpdateTime(currdate);
				insNews.setOrgId(orgId);
				
				colNew.setColId(Long.valueOf(colId));
				colNew.setNewId(insNews.getNewId());
				colNew.setId(UniqueIdUtil.genId());
				colNew.setStartTime(startTime);
				colNew.setEndTime(endTime);
				colNew.setSn(Integer.valueOf(sn++));
				colNew.setIsLongValid(isLongValid);
				this.insColNewDao.add(colNew);
			}
		}
	}

	/**
	 * 获取实时新闻
	 * @param params
	 * @return
	 */
	public List<InsNews> getPortalNews(BaseInsPortalParams params)
	{
		PagingBean page = new PagingBean();
		page.setCurrentPage(Integer.valueOf(0));
		page.setPageSize(params.getPageSize());

		List<InsNews> dataList = dao.getPortalNews("CREATE_TIME_", "DESC",page);
		return dataList;
	}
	
	
	
	
}
