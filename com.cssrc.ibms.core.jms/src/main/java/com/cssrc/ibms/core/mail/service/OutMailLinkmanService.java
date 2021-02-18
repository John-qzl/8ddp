package com.cssrc.ibms.core.mail.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.mail.dao.OutMailLinkmanDao;
import com.cssrc.ibms.core.mail.model.OutMailLinkman;

@Service
public class OutMailLinkmanService extends BaseService<OutMailLinkman>
{

	@Resource
	private OutMailLinkmanDao dao;

	protected IEntityDao<OutMailLinkman, Long> getEntityDao()
	{
		return this.dao;
	}
	/**
	 * 根据address和userId查询最近联系人
	 *@author YangBo @date 2016年10月10日上午10:51:51
	 *@param address
	 *@param userId
	 *@return
	 *@throws Exception
	 */
	public OutMailLinkman findLinkMan(String address, long userId)
			throws Exception
	{
		return this.dao.findLinkMan(address, userId);
	}

	public List<OutMailLinkman> getAllByUserId(Long userId, String condition)
	{
		Map params = new HashMap();
		params.put("userId", userId);
		params.put("condition", condition);
		return this.dao.getAllByUserId(params);
	}
}

