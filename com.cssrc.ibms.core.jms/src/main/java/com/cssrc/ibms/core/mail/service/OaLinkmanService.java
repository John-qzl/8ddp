package com.cssrc.ibms.core.mail.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.mail.dao.OaLinkmanDao;
import com.cssrc.ibms.core.mail.model.OaLinkman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class OaLinkmanService extends BaseService<OaLinkman> {

	@Resource
	private OaLinkmanDao dao;

	protected IEntityDao<OaLinkman, Long> getEntityDao() {
		return this.dao;
	}

	public void save(OaLinkman oaLinkman) {
		Long id = oaLinkman.getId();
		if ((id == null) || (id.longValue() == 0L)) {
			Long userid = UserContextUtil.getCurrentUserId();
			id = Long.valueOf(UniqueIdUtil.genId());
			oaLinkman.setId(id);
			oaLinkman.setUserid(userid);
			add(oaLinkman);
		} else {
			update(oaLinkman);
		}
	}

	public List<OaLinkman> getByUserId(QueryFilter queryFilter, Long userId) {
		queryFilter.addFilterForIB("userId", userId);
		List list = this.dao.getBySqlKey("getByUserId", queryFilter);
		return list;
	}

	public List<OaLinkman> getSelectorList(QueryFilter queryFilter, Long userId) {
		queryFilter.addFilterForIB("userId", userId);
		List list = this.dao.getBySqlKey("getSelectorList", queryFilter);
		return list;
	}

	public List<OaLinkman> getMailLinkMan(Long userId) {
		List list = this.dao.getBySqlKey("getMailLinkMan", userId);
		return list;
	}

	public Map<String, String> getLinkManMap(Long userId) {
		List<OaLinkman> linkManList = getMailLinkMan(userId);
		Map linkManMap = new HashMap();
		for (OaLinkman linkMan : linkManList) {
			linkManMap.put(linkMan.getName(), linkMan.getEmail());
		}
		return linkManMap;
	}
	
	/**
	 * 是否存在联系人
	 *@author Yangbo @date 2016年10月10日上午10:09:01
	 */
	public boolean isOaLinkExist(Long userId, String email) {
		return this.dao.getByUserEmail(userId, email).size() > 0;
	}

	public List<OaLinkman> getByUserEmail(Long userId, String email) {
		return this.dao.getByUserEmail(userId, email);
	}
}
