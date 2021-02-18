package com.cssrc.ibms.core.user.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.PositionDao;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.dao.SysOrgTypeDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.dao.UserPositionDao;
import com.cssrc.ibms.core.user.event.UserPositionEvent;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 用户组织岗位关系Service层
 * <p>
 * Title:UserPositionService
 * </p>
 * 
 * @author Yangbo
 * @date 2016-8-2下午04:26:09
 */
@Service
public class UserPositionService extends BaseService<UserPosition> implements
		IUserPositionService {

	@Resource
	private UserPositionDao userPositionDao;
	@Resource
	private PositionService positionService;
	@Resource
	private SysOrgDao sysOrgDao;
	@Resource
	private SysUserDao sysUserDao;
	@Resource
	private PositionDao positionDao;

	@Resource
	private SysOrgTypeDao sysOrgTypeDao;

	protected IEntityDao<UserPosition, Long> getEntityDao() {
		return this.userPositionDao;
	}

	/**
	 * 担任posid岗位的用户id集合
	 * 
	 * @param posId
	 * @return
	 */
	public List<Long> getUserIdsByPosId(Long posId) {
		return this.userPositionDao.getUserIdsByPosId(posId);
	}

	/**
	 * 获得该用户组织岗位信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserPosition> getByUserId(Long userId) {
		return this.userPositionDao.getByUserId(userId);
	}

	/**
	 * 获取该用户组织岗位分组列表（orgId,userId）
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserPosition> getOrgListByUserId(Long userId) {
		return this.userPositionDao.getOrgListByUserId(userId);
	}

	/**
	 * 获取该岗位的组织岗位列表
	 * 
	 * @param posId
	 * @return
	 */
	public List<UserPosition> getByPosId(Long posId) {
		return this.userPositionDao.getByPosId(posId);
	}

	/**
	 * 删除某用户的岗位关系
	 * 
	 * @param userId
	 */
	public void delByUserId(Long userId) {
		this.userPositionDao.delByUserId(userId);
	}

	/**
	 * 删除posid关联信息
	 * 
	 * @param posId
	 */
	public void delByPosId(Long posId) {
		this.userPositionDao.delByPosId(posId);
	}

	/**
	 * 获取用户的主组织岗位
	 * 
	 * @param userId
	 * @return
	 */
	public UserPosition getPrimaryUserPositionByUserId(Long userId) {
		return this.userPositionDao.getPrimaryUserPositionByUserId(userId);
	}

	/**
	 * 组织获取用户信息
	 * 
	 * @param orgIds
	 * @return
	 */
	public List<UserPosition> getUserByOrgIds(String orgIds) {
		return this.userPositionDao.getUserByOrgIds(orgIds);
	}

	/**
	 * 根据组织id获取组织负责人
	 * 
	 * @param orgId
	 * @param upslope
	 * @return
	 */
	public List<SysUser> getLeaderUserByOrgId(Long orgId, boolean upslope) {
		List<UserPosition> list = this.userPositionDao.getChargeByOrgId(orgId);
		if (BeanUtils.isNotEmpty(list)) {
			List users = new ArrayList();
			for (UserPosition userPosition : list) {
				SysUser user = (SysUser) this.sysUserDao.getById(userPosition
						.getUserId());
				users.add(user);
			}
			return users;
		}
		SysOrg sysOrg = (SysOrg) this.sysOrgDao.getById(orgId);
		if (sysOrg == null)
			return new ArrayList();
		Long parentOrgId = sysOrg.getOrgSupId();
		SysOrg sysOrgParent = (SysOrg) this.sysOrgDao.getById(parentOrgId);
		if (sysOrgParent == null) {
			return new ArrayList();
		}
		if (upslope) {
			return getLeaderUserByOrgId(parentOrgId, true);
		}
		return new ArrayList();
	}

	/**
	 * 获取用户负责的组织岗位
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserPosition> getChargeOrgByUserId(Long userId) {
		return this.userPositionDao.getChargeOrgByUserId(userId);
	}

	/**
	 * 保存用户组织岗位结构
	 * 
	 * @param userId
	 * @param posIds
	 * @param posIdPrimary
	 * @param posIdCharge
	 * @throws Exception
	 */
	public void saveUserPos(Long userId, Long[] posIds, Long posIdPrimary,
			Long[] posIdCharge, Long[] aryOrgId) throws Exception {
		// 移除所有关系
		this.userPositionDao.delByUserId(userId);
		// 岗位列表没有组织时则返回
		if (BeanUtils.isEmpty(aryOrgId)) {
			return;
		}
		List<Long> posIdList = Arrays.asList(posIds);
		List<Long> orgIdList = Arrays.asList(aryOrgId);
		for (int i = 0; i < posIdList.size(); i++) {
			// 遍历添加岗位组织关系
			UserPosition userPosition = new UserPosition();
			if (posIdList.get(i) != -1) {
				userPosition.setPosId(posIdList.get(i));
				Position position = (Position) this.positionService
						.getById(posIdList.get(i));
				userPosition.setJobId(position.getJobId());
			}
			userPosition.setOrgId(orgIdList.get(i));
			userPosition.setUserId(userId);
			userPosition.setUserPosId(Long.valueOf(UniqueIdUtil.genId()));
			userPosition.setPosition_creatorId(UserContextUtil.getCurrentUserId());
			userPosition.setPosition_createTime(new Date());
			userPosition.setPosition_updateId(UserContextUtil.getCurrentUserId());
			userPosition.setPosition_updateTime(new Date());
			// posIdPrimary值等于orgId时则设置为“是”
			if ((posIdPrimary != null)&&posIdList.get(i).longValue()== posIdPrimary.longValue()) {
				userPosition.setIsPrimary(Short.valueOf((short) 1));
				//发送同步数据
				int event = UserPositionEvent.ACTION_ADD;
				if(userPosition.getUserPosId()!=null){
		             event = UserPositionEvent.ACTION_UPD;
				}
				SysOrg sysOrg=this.sysOrgDao.getById(userPosition.getOrgId());
				userPosition.setOrgName(sysOrg.getOrgName());
		        EventUtilService.publishUserPositionEvent(userPosition.getUserPosId(),event, userPosition);
			}
			if ((posIdCharge != null) && (posIdCharge.length > 0)) {
				for (int y = 0; y < posIdCharge.length; y++) {
					if ((posIdCharge[y] != null)
							&& (posIdCharge[y].longValue() == ((Long) posIdList
									.get(i)))) {
						userPosition.setIsCharge(UserPosition.CHARRGE_YES);
					}
				}
			}
			// 添加新增的岗位
			this.userPositionDao.add(userPosition);
			
			
		}

	}

	/**
	 * 获取用户所属的组织名集合
	 * 
	 * @param userId
	 * @return
	 */
	public String getOrgnamesByUserId(Long userId) {
		StringBuffer orgNames = new StringBuffer();

		List<UserPosition> userPositionList = getOrgListByUserId(userId);
		if (BeanUtils.isNotEmpty(userPositionList)) {
			for (UserPosition userPosition : userPositionList) {
				if ((userPosition.getIsPrimary() != null)
						&& (userPosition.getIsPrimary().shortValue() == 1))
					orgNames.append(userPosition.getOrgName() + "(主)  ");
				else {
					orgNames.append(userPosition.getOrgName() + "  ");
				}
			}
		}
		return orgNames.toString();
	}

	/**
	 * 给岗位批量添加用户
	 * 
	 * @param userIds
	 * @param posId
	 * @throws Exception
	 */
	public void addPosUser(Long[] userIds, Long posId) throws Exception {
		if ((BeanUtils.isEmpty(userIds))
				|| (StringUtil.isEmpty(posId.toString())))
			return;
		Position position = (Position) this.positionService.getById(posId);
		for (Long userId : userIds) {
			UserPosition userPosition = this.userPositionDao.getUserPosModel(
					userId, posId);// 校验存在的岗位用户关系

			if (userPosition != null)
				continue;
			userPosition = new UserPosition();
			userPosition.setUserPosId(Long.valueOf(UniqueIdUtil.genId()));
			userPosition.setOrgId(position.getOrgId());
			userPosition.setUserId(userId);
			userPosition.setPosId(posId);
			userPosition.setJobId(position.getJobId());
			userPosition.setPosition_creatorId(UserContextUtil.getCurrentUserId());
			userPosition.setPosition_createTime(new Date());
			userPosition.setPosition_updateId(UserContextUtil.getCurrentUserId());
			userPosition.setPosition_updateTime(new Date());
			this.userPositionDao.add(userPosition);
		}
	}

	/**
	 * 没有岗位直接添加用户和组织关系 //jobId和posId为空
	 * 
	 * @param userIds
	 * @param orgId
	 * @throws Exception
	 */
	public void addOrgUser(Long[] userIds, Long orgId) throws Exception {
		if ((BeanUtils.isEmpty(userIds))
				|| (StringUtil.isEmpty(orgId.toString())))
			return;
		for (Long userId : userIds) {
			// 确定唯一一条记录
			Long posId = null;
			UserPosition userPosition = this.userPositionDao.getUserOrgModel(
					userId, orgId, posId);
			// 校验组织下用户是否已添加
			if (userPosition != null)
				continue;
			userPosition = new UserPosition();
			userPosition.setUserPosId(Long.valueOf(UniqueIdUtil.genId()));
			userPosition.setOrgId(orgId);
			userPosition.setUserId(userId);
			userPosition.setPosition_creatorId(UserContextUtil.getCurrentUserId());
			userPosition.setPosition_createTime(new Date());
			userPosition.setPosition_updateId(UserContextUtil.getCurrentUserId());
			userPosition.setPosition_updateTime(new Date());
			this.userPositionDao.add(userPosition);
		}
	}

	/**
	 * 批量删除用户与岗位关联关系记录
	 * */
	public void delUserPositionByPosId(String posId, Long userId) {
		this.userPositionDao.delUserPositionByPosId(posId, userId);
	}

	/**
	 * 删除指定用户指定岗位的信息
	 * 
	 * @param userId
	 * @param posId
	 */
	public void delByUserIdAndPosId(Long userId, Long posId) {
		this.userPositionDao.delByUserIdAndPosId(userId, posId);
	}

	/**
	 * 批量删除职务id相关记录（例如将“组长”相关所有用户的岗位停用）
	 * 
	 * @param jobId
	 */
	public void delByJobId(Long jobId) {
		this.userPositionDao.delByJobId(jobId);
	}

	/**
	 * 设置组织岗位主管负责人
	 * 
	 * @param userPosId
	 */
	public void setIsCharge(Long userPosId) {
		UserPosition userPosition = (UserPosition) this.userPositionDao
				.getById(userPosId);
		userPosition.setPosition_updateId(UserContextUtil.getCurrentUserId());
		userPosition.setPosition_updateTime(new Date());
		if (userPosition.getIsCharge() == UserPosition.CHARRGE_NO) {
			userPosition.setIsCharge(UserPosition.CHARRGE_YES);
		} else {
			userPosition.setIsCharge(UserPosition.CHARRGE_NO);
		}
		this.userPositionDao.update(userPosition);
	}

	/**
	 * 根据组织id获取组织负责人
	 * 
	 * @param orgId
	 * @return
	 */
	public List<UserPosition> getChargeByOrgId(Long orgId) {
		List userPositionList = this.userPositionDao.getChargeByOrgId(orgId);
		return userPositionList;
	}
	
	/**
	 * 根据组织id获取组织下所有人
	 * 
	 * @param orgId
	 * @return
	 */
	public List<Long> getAllUserByOrgId(Long orgId) {
		List userList = this.userPositionDao.getUserIdsByPosId(orgId);
		return userList;
	}

	/**
	 * 还原被逻辑删除的组织架构以及该组织下的所有子组织与岗位的关系
	 * 
	 * @author Liubo 2017-4-7
	 * @param path
	 */
	public void restoreLogicByOrgPath(String path) {
		this.userPositionDao.restoreLogicByOrgPath(path);
	}
	
	/**
	 * 逻辑删除该组织下的所有子组织与岗位的关系
	 * 
	 * @author Liubo 2017-2-23
	 * @param path
	 */
	public void delLogicByOrgPath(String path) {
		this.userPositionDao.delLogicByOrgPath(path);
	}
	
	/**
	 * 物理删除该组织下的所有子组织与岗位的关系
	 * 
	 * @author Yangbo 2016-8-5
	 * @param path
	 */
	public void delByOrgPath(String path) {
		this.userPositionDao.delByOrgPath(path);
	}

	/**
	 * 删除某组织关联的关系
	 * 
	 * @param orgId
	 */
	public void delByOrgId(Long orgId) {
		this.userPositionDao.delByOrgId(orgId);
	}

	/**
	 * 获取用户主组织岗位信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserPosition getPrimaryByUserId(Long userId) {
		UserPosition userPosition = this.userPositionDao
				.getPrimaryUserPositionByUserId(userId);
		return userPosition;
	}

	/**
	 * 获取指定组织用户岗位列表
	 * 
	 * @author Yangbo 2016-8-5
	 * @param orgId
	 * @return
	 */
	public List<UserPosition> getByOrgId(Long orgId) {
		List<UserPosition> userPositionList = this.userPositionDao
				.getByOrgId(orgId);
		return userPositionList;
	}

	/**
	 * 获取用户组织信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserPosition> getOrgByUserId(Long userId) {
		/* List list = new ArrayList(); */
		List<UserPosition> upList = this.userPositionDao.getByUserId(userId);

		for (UserPosition userPosition : upList) {
			Long orgId = userPosition.getOrgId();
			SysOrg sysOrg = getChargeNameByOrgId(orgId);
			userPosition.setChargeName(sysOrg.getOwnUserName());
		}
		/* return list; */// by yangbo，这里原来返回空值
		return upList;

	}

	/**
	 * 通过组织ID给该组织添加所有负责人
	 * 
	 * @author Yangbo 2016-8-5
	 * @param orgId
	 * @return
	 */
	public SysOrg getChargeNameByOrgId(Long orgId) {
		SysOrg sysOrg = new SysOrg();
		List<UserPosition> chargeList = this.userPositionDao
				.getChargeByOrgId(orgId);

		String chargeId = "";
		String chargeName = "";

		for (UserPosition charge : chargeList) {
			chargeId = chargeId + charge.getUserId() + ",";
			chargeName = chargeName + charge.getFullname() + ",";
		}

		if (chargeName.length() > 0) {
			chargeId = chargeId.substring(0, chargeId.length() - 1);
			chargeName = chargeName.substring(0, chargeName.length() - 1);
		}
		sysOrg.setOwnUser(chargeId);
		sysOrg.setOwnUserName(chargeName);
		return sysOrg;
	}

	/**
	 * 通过userPosIdS设置该岗位为主岗位或去除主岗位，对应的用户其他主岗位消除
	 * 
	 * @param userPosId
	 */
	public void setIsPrimary(Long userPosId) {
		UserPosition userPosition = (UserPosition) userPositionDao
				.getById(userPosId);
		userPosition.setPosition_updateId(UserContextUtil.getCurrentUserId());
		userPosition.setPosition_updateTime(new Date());
		if (userPosition.getIsPrimary().shortValue() == 0) {
			userPosition.setIsPrimary(Short.valueOf((short) 1));
			userPositionDao.updNotPrimaryByUserId(userPosition.getUserId());
		} else {
			userPosition.setIsPrimary(Short.valueOf((short) 0));
		}
		userPositionDao.update(userPosition);
	}

    @Override
    public List<Long> getOrgIdByUserAndJob(Long userId, String jobId)
    {
        Map param = new HashMap();
        param.put("userId", userId);
        param.put("jobId", jobId);
        return userPositionDao.getListBySqlKey("getOrgIdByUserAndJob", param);
        
    }
    
    public List<UserPosition> getByOrgIds(String orgIds,Long isCharge)
    {
        Map param = new HashMap();
        param.put("orgId", orgIds);
    	if(isCharge!=null){
    		 param.put("isCharge", isCharge);
    	}
        return userPositionDao.getListBySqlKey("getByOrgIds", param);
       
    }
}
