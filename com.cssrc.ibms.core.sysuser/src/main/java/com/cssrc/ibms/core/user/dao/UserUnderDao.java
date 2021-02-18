package com.cssrc.ibms.core.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.UserUnder;
/**
 * 用户上下关系DAO层(userid--上司,underuserid--下级)
 * <p>Title:UserUnderDao</p>
 * @author Yangbo 
 * @date 2016-8-20下午02:31:52
 */
@Repository
public class UserUnderDao extends BaseDao<UserUnder> {
	public Class getEntityClass() {
		return UserUnder.class;
	}
	/**
	 * 根据用户id获得下属
	 * @param userId
	 * @return
	 */
	public List<UserUnder> getMyUnderUser(Long userId) {
		return getBySqlKey("getMyUnderUser", userId);
	}
	/**
	 * 俩用户存在多少种上下级关系
	 * @param userUnder
	 * @return
	 */
	public Integer isExistUser(UserUnder userUnder) {
		return (Integer) getOne("isExistUser", userUnder);
	}
	/**
	 * 获得上司列表
	 * @param userId
	 * @return
	 */
	public List<UserUnder> getMyLeader(Long userId) {
		return getBySqlKey("getMyLeader", userId);
	}
	/**
	 * 消除该用户的下级关系
	 * @param userId
	 */
	public void delByUpUserId(Long userId) {
		delBySqlKey("delByUpUserId", userId);
	}
	/**
	 * 撤销两人的级别关系
	 * @param upUserId
	 * @param downUserId
	 */
	public void delByUpAndDown(Long upUserId, Long downUserId) {
		Map params = new HashMap();
		params.put("upUserId", upUserId);
		params.put("downUserId", downUserId);
		delBySqlKey("delByUpAndDown", params);
	}
	/**
	 * 消除该用户的上级关系
	 * @param underUserId
	 */
	public void delByUnderUserId(Long underUserId) {
		delBySqlKey("delByUnderUserId", underUserId);
	}
}
