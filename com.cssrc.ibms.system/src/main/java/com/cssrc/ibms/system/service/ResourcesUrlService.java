package com.cssrc.ibms.system.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.IResourcesUrlService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.system.dao.ResourcesUrlDao;
import com.cssrc.ibms.system.model.ResourcesUrl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
/**
 * 
 * <p>Title:ResourcesUrlService</p>
 * @author Yangbo 
 * @date 2016-8-22下午09:44:34
 */
@Service
public class ResourcesUrlService extends BaseService<ResourcesUrl> implements IResourcesUrlService{

	@Resource
	private ResourcesUrlDao resourcesUrlDao;

	protected IEntityDao<ResourcesUrl, Long> getEntityDao() {
		return this.resourcesUrlDao;
	}

	public List<ResourcesUrl> getByResId(long resId) {
		return this.resourcesUrlDao.getByResId(resId);
	}

	public void update(long resId, List<ResourcesUrl> resourcesUrlList) {
		this.resourcesUrlDao.delByResId(resId);
		if ((resourcesUrlList != null) && (resourcesUrlList.size() > 0))
			for (ResourcesUrl url : resourcesUrlList)
				add(url);
	}
}
