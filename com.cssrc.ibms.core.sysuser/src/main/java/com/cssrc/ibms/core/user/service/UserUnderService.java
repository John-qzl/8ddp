package com.cssrc.ibms.core.user.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.IUserUnderService;
import com.cssrc.ibms.api.sysuser.model.IUserUnder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.dao.UserUnderDao;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.UserUnder;
/**
 * 用户上下级关系Service
 * <p>Title:UserUnderService</p>
 * @author Yangbo 
 * @date 2016-8-20下午03:15:26
 */
@Service
public class UserUnderService extends BaseService<UserUnder> implements IUserUnderService{

	@Resource
	private UserUnderDao dao;

	@Resource
	private SysUserDao sysUserDao;

	protected IEntityDao<UserUnder, Long> getEntityDao() {
		return this.dao;
	}
	/**
	 * 获得下级列表
	 * @param userId
	 * @return
	 */
	public List<UserUnder> getMyUnderUser(Long userId) {
		return this.dao.getMyUnderUser(userId);
	}
	/**
	 * 给用户添加下属
	 * @param userId  某用户id
	 * @param userIds 下属id
	 * @param userNames 下属用户名
	 * @throws Exception
	 */
	public void addMyUnderUser(Long userId, String userIds, String userNames)
			throws Exception {
		String[] idArray = userIds.split(",");
		String[] nameArray = userNames.split(",");
		UserUnder userUnder = new UserUnder();
		userUnder.setUserid(userId);
		for (int i = 0; i < idArray.length; i++) {
			userUnder.setId(Long.valueOf(UniqueIdUtil.genId()));
			userUnder.setUserunder_delFlag((short)0);
			userUnder.setUnderuserid(Long.valueOf(Long.parseLong(idArray[i])));
			userUnder.setUnderusername(nameArray[i]);
			userUnder.setUserunder_creatorId(UserContextUtil.getCurrentUserId());
			userUnder.setUserunder_createTime(new Date());
			userUnder.setUserunder_updateId(UserContextUtil.getCurrentUserId());
			userUnder.setUserunder_updateTime(new Date());
			if (this.dao.isExistUser(userUnder).intValue() <= 0)
				this.dao.add(userUnder);
		}
	}
	
	/**
	 * 成为超级用户的下属
	 * @param userId
	 * @param arySuperiorId超级用户id
	 */
	public void saveSuperior(Long userId, Long[] arySuperiorId) {
		//该用户的下属身份清除
		this.dao.delByUnderUserId(userId);

		if ((arySuperiorId == null) || (arySuperiorId.length == 0)) {
			return;
		}

		SysUser sysUser = this.sysUserDao.getById(userId);

		UserUnder userUnder = new UserUnder();
		userUnder.setUnderuserid(userId);
		userUnder.setUnderusername(sysUser.getFullname());
		for (int i = 0; i < arySuperiorId.length; i++) {
			userUnder.setId(Long.valueOf(UniqueIdUtil.genId()));
			userUnder.setUserunder_delFlag((short)0);
			userUnder.setUserid(arySuperiorId[i]);
			userUnder.setUserunder_creatorId(UserContextUtil.getCurrentUserId());
			userUnder.setUserunder_createTime(new Date());
			userUnder.setUserunder_updateId(UserContextUtil.getCurrentUserId());
			userUnder.setUserunder_updateTime(new Date());
			this.dao.add(userUnder);
		}
	}
	/**
	 * 获得用户的下属id
	 * @param userId
	 * @return
	 */
	public Set<String> getMyUnderUserId(Long userId) {
		Set list = new HashSet();
		List<UserUnder> listUser = this.dao.getMyUnderUser(userId);
		for (UserUnder user : listUser) {
			list.add(user.getUnderuserid().toString());
		}
		return list;
	}

	/**
	 * 俩用户是否存在上下级用户
	 * @param upUserId
	 * @param downUserId
	 * @return
	 */
	public boolean getByUpAndDown(Long upUserId, Long downUserId) {
		UserUnder userUnder = new UserUnder();
		userUnder.setUserid(upUserId);
		userUnder.setUnderuserid(downUserId);
		return this.dao.isExistUser(userUnder).intValue() > 0;
	}
	/**
	 * 删除某用户的上级身份
	 * @param userId
	 */
	public void delByUpUserId(Long userId) {
		this.dao.delBySqlKey("delByUpUserId", userId);
	}
	/**
	 * 撤销两人的上下级关系
	 * @param upUserId
	 * @param downUserId
	 */
	public void delByUpAndDown(Long upUserId, Long downUserId) {
		this.dao.delByUpAndDown(upUserId, downUserId);
	}
	
	public List<SysUser> getMySuperior(Long userId, int level) {
		//上级用户列表
		List list = this.sysUserDao.getUserByUnderUserId(userId);
		if (level > 1) {
			SysUser localSysUser;
			for (Iterator localIterator = list.iterator(); localIterator
					.hasNext(); localSysUser = (SysUser) localIterator.next());
		}
		return null;
	}
	@Override
	public List<? extends IUserUnder> getMyLeader(Long userId) {
		return this.dao.getMyLeader(userId);
	}
}
