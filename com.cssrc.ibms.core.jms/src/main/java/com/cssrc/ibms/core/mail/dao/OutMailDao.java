package com.cssrc.ibms.core.mail.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.mail.model.OutMail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
/**
 * 
 * <p>Title:OutMailDao</p>
 * @author Yangbo 
 * @date 2016年9月29日下午3:50:00
 */
@Repository
public class OutMailDao extends BaseDao<OutMail> {
	public Class getEntityClass() {
		return OutMail.class;
	}

	/**
	 * 获得邮件分页列表
	 *@author Yangbo @date 2016年10月9日上午8:40:48
	 */
	public List<OutMail> getFolderList(QueryFilter queryFilter) {
		return getBySqlKey("getFolderList", queryFilter);
	}
	/**
	 * 更改邮件类型  1:收件箱;2:发件箱;3:草稿箱;4:垃圾箱
	 *@author Yangbo @date 2016年10月9日上午8:42:19
	 */
	public int updateTypes(Long mailId, int types) {
		Map params = new HashMap();
		params.put("types", Integer.valueOf(types));
		params.put("mailId", mailId);
		return update("updateTypes", params);
	}
	
	/**
	 *获取指定用户邮件数量 
	 *@author Yangbo @date 2016年10月9日上午8:44:19
	 */
	public int getFolderCount(long id, int type) {
		Map params = new HashMap();
		params.put("types", Integer.valueOf(type));
		params.put("setId", Long.valueOf(id));
		return ((Integer) getOne("getFolderCount", params)).intValue();
	}
	
	/**
	 * 获取某个邮件信息
	 *@author Yangbo @date 2016年10月9日上午8:53:22
	 */
	public boolean getByEmailId(String uid, Long setId) {
		Map params = new HashMap();
		params.put("emailId", uid);
		params.put("setId", setId);
		int num = ((Integer) getOne("getByEmailId", params)).intValue();

		return num > 0;
	}
	
	/**
	 * 通过emailId删除邮件
	 *@author Yangbo @date 2016年10月9日上午8:54:19
	 */
	public void delByEmailid(String uid) {
		getBySqlKey("delByEmailid", uid);
	}
	
	/**
	 * 获取userid的未读邮件主题名
	 *@author Yangbo @date 2016年10月9日上午8:57:50
	 */
	public List<OutMail> getMailByUserId(long userId, PagingBean pb) {
		Map params = new HashMap();
		params.put("userId", Long.valueOf(userId));
		return getBySqlKey("getMailByUserId", params, pb);
	}
	
	/**
	 * 获取邮件id
	 *@author Yangbo @date 2016年10月9日上午8:58:41
	 */
	public List<String> getUIDBySetId(Long setId) {
		return getSqlSessionTemplate().selectList(
				getIbatisMapperNamespace() + ".getUIDBySetId", setId);
	}
	
	/**
	 * 删除setid指定邮件(setId是邮件用户配置的主键)
	 *@author Yangbo @date 2016年10月9日上午9:01:31
	 */
	public void delBySetId(Long setId) {
		delBySqlKey("delBySetId", setId);
	}
}
