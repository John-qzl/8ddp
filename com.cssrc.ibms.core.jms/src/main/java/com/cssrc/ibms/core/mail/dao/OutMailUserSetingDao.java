package com.cssrc.ibms.core.mail.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.mail.model.OutMailUserSeting;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
/**
 * 
 * <p>Title:OutMailUserSetingDao</p>
 * @author Yangbo 
 * @date 2016年9月29日下午3:51:42
 */
@Repository
public class OutMailUserSetingDao extends BaseDao<OutMailUserSeting>
{
	public Class getEntityClass()
	{
		return OutMailUserSeting.class;
	}
	
	/**
	 * 通过mailaddress获取该外部邮件用户设置信息
	 *@author Yangbo @date 2016年10月10日上午8:57:54
	 */
	public OutMailUserSeting getMailByAddress(String address)
	{
		return (OutMailUserSeting)getUnique("getMailByAddress", address);
	}
	
	public List<OutMailUserSeting> getMailByUserId(Long userId)
	{
		Map params = new HashMap();
		params.put("userId", userId);
		return getBySqlKey("getMailByUserId", params);
	}

	public OutMailUserSeting getByIsDefault(long userId)
	{
		return (OutMailUserSeting)getUnique("getByIsDefault", Long.valueOf(userId));
	}

	public List<OutMailUserSeting> getAllByUserId(QueryFilter queryFilter)
	{
		return getBySqlKey("getAllByUserId", queryFilter);
	}

	public int getCountByAddress(String address)
	{
		return ((Integer)getOne("getCountByAddress", address)).intValue();
	}

	public int updateDefault(OutMailUserSeting mail)
	{
		return update("updateDefault", mail);
	}

	public int getCountByUserId(long userId)
	{
		return ((Integer)getOne("getCountByUserId", Long.valueOf(userId))).intValue();
	}
}

