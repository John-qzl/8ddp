package com.cssrc.ibms.core.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.dao.SysOrgTypeDao;
import com.cssrc.ibms.core.user.dao.UserPositionDao;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysOrgType;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.core.util.redis.RedisClient;
import com.cssrc.ibms.core.util.redis.RedisKey;
import com.cssrc.ibms.core.util.string.StringUtil;


@Service
public class SysOrgService extends BaseService<SysOrg> implements ISysOrgService{
	public static JSONArray allSysOrgInf = new JSONArray();
	@Resource
	private SysOrgTacticService sysOrgTacticService;
	
	@Resource
	private UserPositionService userPositionService;

	@Resource
	private SysOrgRoleService sysOrgRoleService;

	@Resource
	private UserPositionDao userPositionDao;

	@Resource
	private SysOrgTypeDao sysOrgTypeDao;

	@Resource
	private SysOrgRoleManageService sysOrgRoleManageService;

	@Resource
	private PositionService positionService;

	@Resource
	private OrgAuthService orgAuthService;
	@Resource
    private SysUserService sysUserService;

	@Resource
	private SysOrgDao dao;
	protected static Logger logger = LoggerFactory
			.getLogger(SysOrgService.class);

	protected IEntityDao<SysOrg, Long> getEntityDao() {
		return this.dao;
	}
	
	/**
	 * 获取组织信息
	 * @param queryFilter
	 * @return
	 */
	public List<SysOrg> getOrgByOrgId(QueryFilter queryFilter) {
		return this.dao.getOrgByOrgId(queryFilter);
	}
	
	/**
	 * 获得维度为demid的组织架构
	 * @param demId
	 * @return
	 */
	public List<SysOrg> getOrgsByDemIdOrAll(Long demId)
	{
	    return this.dao.getOrgsByDemIdOrAll(demId);
	}
	
	/**
	 * 根据组织名获得组织信息
	 * @param orgName
	 * @return
	 */
	public List<SysOrg> getByOrgName(String orgName) {
		return this.dao.getByOrgName(orgName);
	}
	/**
	 * 根据path查出组织
	 * @param path
	 * @return
	 */
	public List<SysOrg> getOrgByPath(String path) {
		return this.dao.getOrgByPath(path);
	}
	
	/**
	 * 
	 * @param demId
	 * @return
	 */
	public Map getOrgMapByDemId(Long demId) {
		String userNameStr = "";
		String userNameCharge = "";
		Map orgMap = new HashMap();
		List<SysOrg> list = this.dao.getOrgsByDemIdOrAll(demId);
		for (SysOrg sysOrg : list) {
			List<UserPosition> userlist = this.userPositionDao
					.getByOrgId(sysOrg.getOrgId());
			for (UserPosition userOrg : userlist) {
				String isCharge = "";
				if (BeanUtils.isNotEmpty(userOrg.getIsCharge())) {
					isCharge = userOrg.getIsCharge().toString();
				}

				if (UserPosition.CHARRGE_YES.equals(isCharge)) {
					if (userNameCharge.isEmpty())
						userNameCharge = userOrg.getFullname();
					else {
						userNameCharge = userNameCharge + ","
								+ userOrg.getFullname();
					}
				}
			}

			sysOrg.setOwnUserName(userNameCharge);
			if (sysOrg.getOrgSupId().longValue() != 0L)
				orgMap.put(sysOrg.getOrgId(), sysOrg);
		}
		return orgMap;
	}
	
	/**
	 * code查询组织
	 * @param code
	 * @return
	 */
	public SysOrg getByCode(String code) {
		SysOrg sysOrg = (SysOrg) this.dao.getUnique("getByCode", code);
		return sysOrg;
	}
	
	/**
	 * 判断该组织是否分配
	 * @param id
	 * @return
	 */
	public boolean isUserExistFromOrg(Long id) {
		boolean flag = false;
		SysOrg sysOrg = (SysOrg) this.dao.getById(id);
		String path = sysOrg.getPath();
		path = StringUtil.isNotEmpty(path) ? path + "%" : "";

		List sysOrgs = this.dao.getByOrgPath(path);
		for (int i = 0; i < sysOrgs.size(); i++) {
			if (this.userPositionDao.isUserExistFromOrg(((SysOrg) sysOrgs
					.get(i)).getOrgId())) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	/**
	 * 逻辑删除该组织所有相关联的数据
	 */
	public void delLogicById(Long id) {
		SysOrg sysOrg = (SysOrg) this.dao.getById(id);
		String path = sysOrg.getPath();
		Long currentUserId = UserContextUtil.getCurrentUserId();
		//子组织路径
		path = StringUtil.isNotEmpty(path) ? path + "%" : "";
		List sysOrgs = getByOrgPath(path);

		this.userPositionService.delLogicByOrgPath(path);

		this.sysOrgRoleService.delByOrgPath(path);

		this.dao.delLogicByPath(path);

		this.positionService.delLogicByOrgId(sysOrgs,currentUserId);

		for (int i = 0; i < sysOrgs.size(); i++) {
			this.sysOrgRoleManageService.delByOrgId(((SysOrg) sysOrgs.get(i))
					.getOrgId());

			this.orgAuthService
					.delByOrgId(((SysOrg) sysOrgs.get(i)).getOrgId());
		}
	}
	/**
	 * 物理删除该组织所有相关联的数据
	 */
	public void delById(Long id) {
		SysOrg sysOrg = (SysOrg) this.dao.getById(id);
		String path = sysOrg.getPath();
		//子组织路径
		path = StringUtil.isNotEmpty(path) ? path + "%" : "";
		List sysOrgs = getByOrgPath(path);

		this.userPositionService.delByOrgPath(path);

		this.sysOrgRoleService.delByOrgPath(path);

		this.dao.delByPath(path);

		this.positionService.delByOrgId(sysOrgs);

		for (int i = 0; i < sysOrgs.size(); i++) {
			this.sysOrgRoleManageService.delByOrgId(((SysOrg) sysOrgs.get(i))
					.getOrgId());

			this.orgAuthService
					.delByOrgId(((SysOrg) sysOrgs.get(i)).getOrgId());
		}
	}
	/**
	 * 还原被逻辑删除的组织架构以及所有相关联的数据
	 */
	public void restoreLogicByPath(Long id) {
		SysOrg sysOrg = (SysOrg) this.dao.getById(id);
		String path = sysOrg.getPath();
		Long currentUserId = UserContextUtil.getCurrentUserId();
		//子组织路径
		path = StringUtil.isNotEmpty(path) ? path + "%" : "";
		List sysOrgs = getByOrgPath(path);

		this.userPositionService.restoreLogicByOrgPath(path);

		this.dao.restoreLogicOrg(path);

		this.positionService.restoreLogicByOrgId(sysOrgs,currentUserId);
	}
	/**
	 * 获得用户分配的组织
	 * @param userId
	 * @return
	 */
	public List<SysOrg> getOrgsByUserId(Long userId) {
		return this.dao.getOrgsByUserId(userId);
	}
	/**
	 * 用户所在组织id集合
	 * @param userId
	 * @return
	 */
	public String getOrgIdsByUserId(Long userId) {
		StringBuffer sb = new StringBuffer();
		List<SysOrg> orgList = this.dao.getOrgsByUserId(userId);
		for (SysOrg org : orgList) {
			sb.append(org.getOrgId() + ",");
		}
		if (orgList.size() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	/**
	 * 获取用户默认所属组织
	 * @param userId
	 * @return
	 */
	public Long getOrgIdByUserId(Long userId) {
		PositionService positionService = (PositionService) AppUtil
				.getBean(PositionService.class);
		SysOrgService sysOrgService = (SysOrgService) AppUtil
				.getBean(SysOrgService.class);
		Position position = positionService.getDefaultPosByUserId(userId);
		if (position != null) {
			Long orgId = position.getOrgId();
			SysOrg sysOrg = (SysOrg) sysOrgService.getById(orgId);
			if (sysOrg != null)
				return sysOrg.getOrgId();
		}
		return null;
	}

	
	private Map<Long, List<SysOrg>> coverTreeData(Long rootId, List<SysOrg> instList)
	{
		Map dataMap = new HashMap();
		dataMap.put(Long.valueOf(rootId.longValue()), new ArrayList());
		if ((instList != null) && (instList.size() > 0)) {
			for (SysOrg sysOrg : instList) {
				long parentId = sysOrg.getOrgSupId().longValue();
				if (dataMap.get(Long.valueOf(parentId)) == null) {
					dataMap.put(Long.valueOf(parentId), new ArrayList());
				}
				((List)dataMap.get(Long.valueOf(parentId))).add(sysOrg);
			}
		}
		  return dataMap;
	}

	public List<SysOrg> coverTreeList(Long rootId, List<SysOrg> instList) {
		Map dataMap = coverTreeData(rootId, instList);

		List list = new ArrayList();

		list.addAll(getChildList(rootId, dataMap));

		return list;
	}

	private List<SysOrg> getChildList(Long parentId,
			Map<Long, List<SysOrg>> dataMap) {
		List<SysOrg> list = new ArrayList<SysOrg>();

		List<SysOrg> orgList = (List<SysOrg>) dataMap.get(Long.valueOf(parentId
				.longValue()));
		if ((orgList != null) && (orgList.size() > 0)) {
			for (SysOrg sysOrg : orgList) {
				list.add(sysOrg);
				List<SysOrg> childList = getChildList(sysOrg.getOrgId(), dataMap);
				list.addAll(childList);
			}
		}
		return list;
	}
	
	/**
	 * 获取用户指定维度的组织架构
	 * @param userId
	 * @param demId
	 * @return
	 */
	public List<SysOrg> getByUserIdAndDemId(Long userId, Long demId) {
		return this.dao.getByUserIdAndDemId(userId, demId);
	}

	public void move(Long targetId, Long dragId, String moveType) {
		SysOrg target = (SysOrg) this.dao.getById(targetId);
		SysOrg dragged = (SysOrg) this.dao.getById(dragId);

		if (!target.getDemId().equals(dragged.getDemId())) {
			return;
		}
		String nodePath = dragged.getPath();

		List<SysOrg> list = this.dao.getByOrgPath(nodePath);

		for (SysOrg org : list) {
			if (("prev".equals(moveType)) || ("next".equals(moveType))) {
				String targetPath = target.getPath();
				String parentPath = targetPath.endsWith(".") ? targetPath
						.substring(0, targetPath.length() - 1) : targetPath;

				parentPath = parentPath.substring(0, parentPath
						.lastIndexOf(".") + 1);

				if (org.getOrgId().equals(dragId)) {
					org.setOrgSupId(target.getOrgSupId());
					org.setPath(parentPath + dragId + ".");
				} else {
					String path = org.getPath();
					String tmpPath = parentPath + dragId + "."
							+ path.replaceAll(nodePath, "");
					org.setPath(tmpPath);
				}

				if ("prev".equals(moveType))
					org.setSn(Long.valueOf(target.getSn().longValue() - 1L));
				else {
					org.setSn(Long.valueOf(target.getSn().longValue() + 1L));
				}

			} else if (org.getOrgId().equals(dragId)) {
				org.setOrgSupId(targetId);

				org.setPath(target.getPath() + org.getOrgId() + ".");
			} else {
				String path = org.getPath();

				String tmpPath = path.replaceAll(nodePath, "");

				String targetPath = target.getPath();

				String tmp = targetPath + dragged.getOrgId() + "." + tmpPath;
				org.setPath(tmp);
			}
			org.setUpdatetime(new Date());
			org.setUpdateId(UserContextUtil.getCurrentUserId());
			this.dao.update(org);
		}
	}
	/**
	 * 新增组织
	 *@author Yangbo @date 2016年10月9日下午2:37:18
	 */
	public void addOrg(SysOrg sysOrg) throws Exception {
		sysOrg.setUpdatetime(new Date());
		sysOrg.setUpdateId(UserContextUtil.getCurrentUserId());
		sysOrg.setCreatorId(UserContextUtil.getCurrentUserId());
		sysOrg.setCreatetime(new Date());
		sysOrg.setSn(sysOrg.getOrgId());
		sysOrg.setDemId(Long.valueOf(1001));
		sysOrg.setIsDelete(Short.valueOf((short) 0));
		
		this.dao.add(sysOrg);
	}
	
	/**
	 * 更新组织
	 * @param sysOrg
	 * @throws Exception
	 */
	public void updOrg(SysOrg sysOrg) throws Exception {
		sysOrg.setUpdatetime(new Date());
		sysOrg.setUpdateId(UserContextUtil.getCurrentUserId());
		this.dao.update(sysOrg);
	}
	
	/**
	 *用户主组织(只有一个) 
	 * @param userId
	 * @return
	 */
	public SysOrg getPrimaryOrgByUserId(Long userId) {
		return this.dao.getPrimaryOrgByUserId(userId);
	}
	
	/**
	 * 获取用户负责的组织列表
	 * @param userId
	 * @return
	 */
	public List<SysOrg> getChargeOrgByUserId(Long userId) {
		return this.dao.getChargeOrgByUserId(userId);
	}
	/**
	 * 获取path下的所有父子组织树列表
	 * @param path
	 * @return
	 */
	public List<SysOrg> getByOrgPath(String path) {
		return this.dao.getByOrgPath(path);
	}
	/**
	 * 获得有类型的组织
	 * @param sysOrg
	 * @return
	 */
	public SysOrg getParentWithType(SysOrg sysOrg) {
		Long parentOrgId = sysOrg.getOrgSupId();
		if (parentOrgId.equals(Long.valueOf(Long.parseLong("1"))))
			return null;
		SysOrg parentOrg = (SysOrg) this.dao.getById(parentOrgId);
		if (parentOrg == null)
			return null;
		if ((parentOrg.getOrgType() != null)
				&& (this.sysOrgTypeDao.getById(parentOrg.getOrgType()) != null)) {
			return parentOrg;
		}
		parentOrg = getParentWithType(parentOrg);

		return parentOrg;
	}

	public SysOrg getParentWithTypeLevel(SysOrg sysOrg, SysOrgType sysOrgType) {
		SysOrg parentOrg = getParentWithType(sysOrg);
		if (parentOrg == null)
			return parentOrg;
		SysOrgType currentSysOrgType = (SysOrgType) this.sysOrgTypeDao
				.getById(parentOrg.getOrgType());

		if ((currentSysOrgType != null)
				&& (sysOrgType.getLevels().longValue() < currentSysOrgType
						.getLevels().longValue())) {
			parentOrg = getParentWithTypeLevel(parentOrg, sysOrgType);
		}
		return parentOrg;
	}

	public SysOrg getDefaultOrgByUserId(Long userId)
	{
		List<UserPosition> list = this.userPositionDao.getByUserId(userId);
		Long orgId = Long.valueOf(0L);

		if (BeanUtils.isEmpty(list)) return null;
		if (list.size() == 1) {
			        orgId = ((UserPosition)list.get(0)).getOrgId();
		}
		else {
			for (UserPosition userPosition : list) {
				if (userPosition.getIsPrimary().shortValue() == 1) {
					orgId = userPosition.getOrgId();
					break;
				}
			}

			if (orgId.longValue() == 0L) orgId = ((UserPosition)list.get(0)).getOrgId();
		}
		return (SysOrg)this.dao.getById(orgId);
	}

	public void updSn(Long orgId, long sn) {
		this.dao.updSn(orgId, sn);
	}
	/**
	 * 获得下级组织
	 * @param orgSupId
	 * @return
	 */
	public List<SysOrg> getOrgManageByOrgSupId(Long orgSupId) {
		List<SysOrg> list = this.dao.getOrgManageByOrgSupId(orgSupId);
		return list;
	}
	/**
	 * 获得组织管理界面的下级组织
	 * @param orgSupId
	 * @return
	 */
	public List<SysOrg> getOrgByOrgSupId(Long orgSupId) {
		List<SysOrg> list = this.dao.getOrgByOrgSupId(orgSupId);
		return list;
	}
	/**
	 * 获得包含已逻辑删除的下级组织
	 * @param orgSupId
	 * @return
	 */
	public List<SysOrg> getLogicOrgByOrgSupId(Long orgSupId) {
		List<SysOrg> list = this.dao.getLogicOrgByOrgSupId(orgSupId);
		return list;
	}

	public List<SysOrg> getOrgByOrgSupIdAndLevel(Long orgSupId) {
		List<SysOrg> childList = this.dao.getOrgByOrgSupId(orgSupId);

		int level = PropertyUtil.getIntByAlias("orgExpandLevel",
				Integer.valueOf(0)).intValue();
		int childSize = childList.size();
		if (level == 0) {
			for (int i = 0; i < childSize; i++) {
				List<SysOrg> MoreList = getOrgByOrgSupIdAndLevel(((SysOrg) childList
						.get(i)).getOrgId());
				childList.addAll(MoreList);
			}
		}
		if (level > 1) {
			level--;
			for (int i = 0; i < childSize; i++) {
				List<SysOrg> MoreList = getOrgByOrgSupIdAndLevel(((SysOrg) childList
						.get(i)).getOrgId(), level);
				childList.addAll(MoreList);
			}
		}
		return childList;
	}
	
	public List<SysOrg> getOrgByOrgSupIdAndLevel(Long orgSupId, int level) {
		List<SysOrg> childList = new ArrayList<SysOrg>();
		if (level > 0) {
			childList = this.dao.getOrgByOrgSupId(orgSupId);
			level--;
			int childSize = childList.size();
			for (int i = 0; i < childSize; i++) {
				List<SysOrg> MoreList = getOrgByOrgSupIdAndLevel(((SysOrg) childList
						.get(i)).getOrgId(), level);
				childList.addAll(MoreList);
			}
		}
		return childList;
	}
	
	/**
	 * 分组组织信息
	 * @param groupId
	 * @return
	 */
	public List<SysOrg> getByOrgMonGroup(Long groupId) {
		return this.dao.getByOrgMonGroup(groupId);
	}
	
	/**
	 * organem非空下可获得对应组织(父子)树
	 * @param filter
	 * @return
	 */
	public List<SysOrg> getOrgForMobile(QueryFilter filter) {
		return this.dao.getBySqlKey("getBySupId4MobileOrg", filter);
	}
	/**
	 * 获得组织类型orgType的组织
	 * @param orgType
	 * @return
	 */
	public List<SysOrg> getByOrgType(Long orgType) {
		return this.dao.getByOrgType(orgType);
	}
	/**
	 * List<SysOrg> TO Map<String, SysOrg>
	 * @param companyList
	 * @return
	 */
	private Map<String, SysOrg> convertToMap(List<SysOrg> companyList) {
		Map map = new HashMap();
		for (SysOrg org : companyList) {
			map.put(org.getOrgId().toString(), org);
		}
		return map;
	}
	/**
	 * 更加和添加组织分公司
	 */
	public void updCompany() {
		List<SysOrg> companyList = this.sysOrgTacticService.getSysOrgListByOrgTactic();
		Map companyMap = convertToMap(companyList);
		List<SysOrg> list = this.dao.getAll();
		for (SysOrg sysOrg : list) {
			String path = sysOrg.getPath();
			path = StringUtil.trimPrefix(path, "." + SysOrg.BEGIN_ORGID + ".");
			path = StringUtil.trimPrefix(path, ".");
			String[] aryPath = path.split("[.]");
			for (int i = aryPath.length - 1; i >= 0; i--)
				if (companyMap.containsKey(aryPath[i])) {
					SysOrg org = (SysOrg) this.dao
							.getById(new Long(aryPath[i]));
					sysOrg.setCompanyId(org.getOrgId());
					sysOrg.setCompany(org.getOrgName());
					sysOrg.setUpdatetime(new Date());
					sysOrg.setUpdateId(UserContextUtil.getCurrentUserId());
					this.dao.update(sysOrg);
					break;
				}
		}
	}
	/**
	 * 获取有公司的组织
	 * @return
	 */
	public List<Map<String, Object>> getCompany() {
		return this.dao.getCompany();
	}
	/**
	 * 该组织是否存在code
	 * @param code
	 * @param id
	 * @return
	 */
	public Integer getCountByCode(String code, Long id) {
		return this.dao.getCountByCode(code, id);
	}
	/**
	 * 获得下级组织
	 * @param parentId
	 * @param params
	 * @return
	 */
	public List getByParentId(Long parentId, Map<String, Object> params) {
		return this.dao.getOrgByOrgSupId(parentId);
	}
	
	/**
	 *重置所有组织code
	 */
	public void syncAllOrg() {
		List<SysOrg> orgs = getByParentId(Long.valueOf(1L), new HashMap());
		for (SysOrg rootOrg : orgs) {
			rootOrg.setSupCode("1");
			syncOrgsByParentId(rootOrg.getOrgId(), rootOrg.getCode());
		}
	}
	/**
	 * 根据父组织id迭代更新子组织的父code
	 * @param parentId
	 * @param parentCode
	 */
	public void syncOrgsByParentId(Long parentId, String parentCode) {
		Map map = new HashMap();
		List<SysOrg> orgs = getByParentId(parentId, map);
		SysOrg org;
		for (Iterator localIterator = orgs.iterator(); localIterator.hasNext(); org
				.setSupCode(parentCode))
			org = (SysOrg) localIterator.next();

		for (SysOrg org1 : orgs)
			syncOrgsByParentId(org1.getOrgId(), org1.getCode());
	}
	/**
	 * 分级组织节点数Json对象
	 * @param orgId
	 * @return
	 */
	public String getOrgJsonByAuthOrgId(Long orgId) {
		SysOrg org = (SysOrg) this.dao.getById(orgId);
		if (org == null)
			return "";
		String json = getJson(org);
		return json;
	}
	
	/**
	 * 给组织拼接树字符串Json对象
	 * @param sysOrg
	 * @return
	 */
	private String getJson(SysOrg sysOrg) {
		StringBuffer sb = new StringBuffer();
		List<SysOrg> list = this.dao.getByOrgPath(sysOrg.getPath());

		for (SysOrg org : list) {
			org.setTopOrgId(sysOrg.getOrgId());
		}

		Long orgId = sysOrg.getOrgId();
		Long demId = sysOrg.getDemId();
		sb.append("{orgId:\"" + orgId + "\", orgName:\"" + sysOrg.getOrgName()
				+ "\",demId:\"" + demId + "\",isRoot:\"0\",orgSupId:\""
				+ sysOrg.getOrgSupId() + "\",path:\"" + sysOrg.getPath()
				+ "\",topOrgId:" + orgId);
		List tmpList = getByParentId(orgId, list);
		if (tmpList.size() > 0) {
			sb.append(",children:[");
			getChildren(sb, tmpList, list);

			sb.append("]");
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 遍历组织子节点
	 * @param sb
	 * @param list
	 * @param allList
	 */
	private void getChildren(StringBuffer sb, List<SysOrg> list,
			List<SysOrg> allList) {
		for (int i = 0; i < list.size(); i++) {
			SysOrg sysOrg = (SysOrg) list.get(i);
			Long orgId = sysOrg.getOrgId();
			Long demId = sysOrg.getDemId();
			sb.append("{orgId:\"" + orgId + "\",orgName:\""
					+ sysOrg.getOrgName() + "\",demId:\"" + demId
					+ "\",isRoot:0,orgSupId:\"" + sysOrg.getOrgSupId()
					+ "\",path:\"" + sysOrg.getPath() + "\",topOrgId:"
					+ sysOrg.getTopOrgId());
			List tmpList = getByParentId(orgId, allList);
			if (tmpList.size() > 0) {
				sb.append(",children:[");
				getChildren(sb, tmpList, allList);
				sb.append("]");
			}
			sb.append("}");
			if (i < list.size() - 1)
				sb.append(",");
		}
	}
	
	/**
	 * 遍历组织子节点的父id
	 * @param parentId
	 * @param list
	 * @return
	 */
	private List<SysOrg> getByParentId(Long parentId, List<SysOrg> list) {
		List<SysOrg> rtnList = new ArrayList<SysOrg>();
		for (Iterator it = list.iterator(); it.hasNext();) {
			SysOrg org = (SysOrg) it.next();
			if (parentId.equals(org.getOrgSupId())) {
				rtnList.add(org);
				it.remove();
			}
		}
		return rtnList;
	}

	   
    /**
     * 根据组织id获取组织分管领导
     * 
     * @param orgId 组织Id
     * @param type 领导类型，all、leader、viceLeader(所有领导、主领导、副领导)
     * @param upslope 如果当前组织分管领导为空，是否递归往上查找
     * @return
     */
    public List<SysUser> getLeaderUserByOrgId(Long orgId, String type, boolean upslope)
    {
        SysOrg sysOrg = this.dao.getById(orgId);
        if (sysOrg == null)
        {
            return new ArrayList<SysUser>();
        }
        String leader = sysOrg.getLeader();
        String viceLeader = sysOrg.getLeader();
        List<SysUser> result=new ArrayList<SysUser>();

        if ("all".equals(type))
        {
            if (StringUtil.isNotEmpty(leader) || StringUtil.isNotEmpty(viceLeader))
            {
                // 已经找到，返回
                if(StringUtil.isNotEmpty(leader)){
                    List<SysUser>  leaders=sysUserService.getByIdSet(viceLeader);
                    for(SysUser suser:leaders){
                        if(!result.contains(suser)){
                            result.add(suser);
                        }
                    }
                }
                if(StringUtil.isNotEmpty(viceLeader)){
                    List<SysUser>  viceLeaders=sysUserService.getByIdSet(viceLeader);
                    for(SysUser suser:viceLeaders){
                        if(!result.contains(suser)){
                            result.add(suser);
                        }
                    }
                }
                return result;
            }
        }
        else if ("leader".equals(type))
        {
            if (StringUtil.isNotEmpty(leader))
            {
                // 已经找到，返回
                result.addAll(sysUserService.getByIdSet(leader));
                return result;
            }
        }
        else if ("viceLeader".equals(type))
        {
            if (StringUtil.isNotEmpty(viceLeader))
            {
                // 已经找到，返回
                result.addAll(sysUserService.getByIdSet(viceLeader));
                return result;
            }
        }
        else
        {
            return new ArrayList<SysUser>();
        }
        // 如果没有找到则继续通过父组织递归查找
        Long parentOrgId = sysOrg.getOrgSupId();
        SysOrg sysOrgParent = this.dao.getById(parentOrgId);
        if (sysOrgParent == null)
        {
            // 如果父组织为空返回
            return new ArrayList<SysUser>();
        }
        // 如果查找策略为递归查找，则继续
        if (upslope)
        {
            return getLeaderUserByOrgId(parentOrgId, type, upslope);
        }
        else
        {
            return new ArrayList<SysUser>();
        }
        
    }
 
	@Override
	public List<? extends ISysOrg> getOrgByUserIdPath(Long userId, String path) {
		return this.dao.getOrgByUserIdPath(userId, path);
	}

	@Override
	public ISysOrg getOrgByUsername(String account) {
		return dao.getOrgByUsername(account);
	}

	@Override
	public List<? extends ISysOrg> getByUserId(Long userId) {
		return this.dao.getByUserId(userId);
	}
	
	/**
	 *获取父组织及有关子节点
	 * @param orgId
	 * @return
	 */
	public String getIdsBySupId(Long orgId){
		String ids = getChildIds(orgId);
		ids +=orgId.toString();
		return ids;
	}
	
	/**
	 * 遍历添加子组织id
	 * @param orgId
	 * @return
	 */
	public String getChildIds(Long orgId){
		List<SysOrg> list = this.dao.getOrgByOrgSupId(orgId);
		String ids = "";
		for(SysOrg sysOrg : list){
			ids += ""+sysOrg.getOrgId()+",";
			String childIds = getChildIds(sysOrg.getOrgId());
			if(childIds != ""){
				ids +=childIds;
			}
		}
		return ids;
	}

    /**
     * 获取用户负责的组织
     * @param userId
     * @return
     */
    public List<Long> getChargeOrg(Long userId)
    {
        return this.dao.getChargeOrg(userId);
    }

    @Override
    public List<Long> getByAllLeader(Long userId)
    {
        return dao.getByAllLeader(userId);
    }

    @Override
    public List<Long> getByLeader(Long userId)
    {
        return dao.getByLeader(userId);
    }

    @Override
    public List<Long> getByViceLeader(Long userId)
    {
        return dao.getByViceLeader(userId);
    }

    @Override
    public List<SysOrg> getByParentIdAndType(Long orgId, String orgType)
    {
        return dao.getByParentIdAndType(orgId,orgType);
    }
    
	/**
	 * 将所有组织放到redis中
	 */
	public void setAllSysOrgToRedis(){
		//获取所有的组织
		List<SysOrg> sysOrgList = getAll();
		if(BeanUtils.isEmpty(sysOrgList)){
        	return;
        }
		JSONArray orgArr = new JSONArray();
		for(SysOrg sysOrg:sysOrgList){
			
			JSONObject orgObj = new JSONObject();
			orgObj.put("orgId", sysOrg.getOrgId());
			orgObj.put("orgName", sysOrg.getOrgName());
			orgObj.put("orgPath", sysOrg.getPath());
			orgArr.add(orgObj);
		}
		
		try {
   		 	//RedisClient.set(RedisKey.ALL_SYSORG_INF, orgArr.toString());
			allSysOrgInf=orgArr;
        } catch (Exception e) {
            logger.error("组织放到redis中初始化出错");
        }
	}
	
	/**
	 * 通过前台传过来的组织输入框的值进行模糊查询
	 * @param orgFuzzyName
	 * @return
	 */
	public JSONArray getFuzzySysOrgList(String orgFuzzyName,String fieldName,String relvalue,String type,String typeVal){
		//Object allSysOrg = RedisClient.get(RedisKey.ALL_SYSORG_INF);
		//JSONArray allSysOrgInf = JSONArray.fromObject(allSysOrg);
		JSONArray orgArr = new JSONArray();
		
		//获取当前用户组织
		SysOrg sysCurrentOrg = (SysOrg) UserContextUtil.getCurrentOrg();
		
		Iterator<Object> it = allSysOrgInf.iterator();
		
		//其次判断是否需要通过typeVal进行数据过滤
		if((StringUtil.isNotEmpty(type)) && (!"all".equals(typeVal))
				&& (BeanUtils.isNotEmpty(sysCurrentOrg))){
			
			//处理当前组织path
			String path = sysCurrentOrg.getPath();
			//上级组织path
			String upOrgPath = path.substring(0,path.length()-1);
			if(upOrgPath.indexOf(".")>0){
				upOrgPath = upOrgPath.substring(0,upOrgPath.lastIndexOf(".")+1);
			}else{
				upOrgPath = "";
			}
			
			while(it.hasNext()){
				JSONObject orgObj = (JSONObject) it.next();
				Object fieldValue = orgObj.get(fieldName);
				Object orgPath = orgObj.get("orgPath");
				
				//当前组织
				if(typeVal.equals("self")){
					if(orgPath!=null&&orgPath.toString().contains(path)){
						//判断是否需要模糊查询 .
						if(fieldValue!=null){
							if(StringUtil.isChinese(orgFuzzyName)){
								if(fieldValue.toString().contains(orgFuzzyName)){
									orgArr.add(orgObj);
									//限制查询时的数据数量
									if(orgArr.size()>20)
										break;
								}
							}else{
								if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(orgFuzzyName)){
									orgArr.add(orgObj);
									//限制查询时的数据数量
									if(orgArr.size()>20)
										break;
								}
							}
						}else{
							orgArr.add(orgObj);
							//限制查询时的数据数量
							if(orgArr.size()>20)
								break;
						}
					}
				}else if(typeVal.equals("up")&&StringUtil.isNotEmpty(upOrgPath)){
					//上级组织
					if(orgPath!=null&&orgPath.toString().contains(upOrgPath)){
						//判断是否需要模糊查询 .
						if(fieldValue!=null){
							if(StringUtil.isChinese(orgFuzzyName)){
								if(fieldValue.toString().contains(orgFuzzyName)){
									orgArr.add(orgObj);
									//限制查询时的数据数量
									if(orgArr.size()>20)
										break;
								}
							}else{
								if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(orgFuzzyName)){
									orgArr.add(orgObj);
									//限制查询时的数据数量
									if(orgArr.size()>20)
										break;
								}
							}
						}else{
							orgArr.add(orgObj);
							//限制查询时的数据数量
							if(orgArr.size()>20)
								break;
						}
					}
				}
				
			}
		}else{
			while(it.hasNext()){
				JSONObject orgObj = (JSONObject) it.next();
				Object fieldValue = orgObj.get(fieldName);
				
				//判断是否需要模糊查询 .
				if(fieldValue!=null){
					if(StringUtil.isChinese(orgFuzzyName)){
						if(fieldValue.toString().contains(orgFuzzyName)){
							orgArr.add(orgObj);
							//限制查询时的数据数量
							if(orgArr.size()>20)
								break;
						}
					}else{
						if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(orgFuzzyName)&&!orgObj.getString("orgId").equals("1001")){
							orgArr.add(orgObj);
							//限制查询时的数据数量
							if(orgArr.size()>20)
								break;
						}
					}
				}else{
					orgArr.add(orgObj);
					//限制查询时的数据数量
					if(orgArr.size()>20)
						break;
				}
			}
		}

		return orgArr;
	}
}
