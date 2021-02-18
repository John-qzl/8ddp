package com.cssrc.ibms.core.user.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgTacticService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.SysOrgTacticDao;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysOrgTactic;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 组织策略Service
 * <p>Title:SysOrgTacticService</p>
 * @author Yangbo 
 * @date 2016-8-4下午08:49:02
 */
@Service
public class SysOrgTacticService extends BaseService<SysOrgTactic> implements ISysOrgTacticService{

	@Resource
	private SysOrgTacticDao dao;

	@Resource
	private SysOrgService sysOrgService;

	protected IEntityDao<SysOrgTactic, Long> getEntityDao() {
		return this.dao;
	}

	public SysOrgTactic getOrgTactic() {
		SysOrgTactic sysOrgTactic = (SysOrgTactic) this.dao
				.getById(SysOrgTactic.DEFAULT_ID);
		if (BeanUtils.isEmpty(sysOrgTactic)) {
			sysOrgTactic = new SysOrgTactic();
			sysOrgTactic.setOrgTactic(Short
					.valueOf((short) SysOrgTactic.ORG_TACTIC_WITHOUT));
		}
		return sysOrgTactic;
	}

	public List<SysOrg> getSysOrgListByOrgTactic() {
		SysOrgTactic sysOrgTactic = getOrgTactic();
		List list = new ArrayList();
		if (SysOrgTactic.ORG_TACTIC_LEVEL == sysOrgTactic.getOrgTactic()
				.shortValue())
			list = this.sysOrgService.getByOrgType(sysOrgTactic.getOrgType());
		else if (SysOrgTactic.ORG_TACTIC_SELECT == sysOrgTactic.getOrgTactic()
				.shortValue())
			list = getOrgLstByOrgSelectId(sysOrgTactic.getOrgSelectId());
		else if (SysOrgTactic.ORG_TACTIC_COMBINATION == sysOrgTactic
				.getOrgTactic().shortValue())
			list = getCombination(sysOrgTactic.getOrgType(), sysOrgTactic
					.getOrgSelectId());
		else {
			return list;
		}
		return list;
	}

	public List<SysOrg> getSysOrgListByOrgName(String orgName) {
		List<SysOrg> list = getSysOrgListByOrgTactic();
		if (BeanUtils.isEmpty(orgName))
			return list;
		List list1 = new ArrayList();
		for (SysOrg sysOrg : list) {
			if (sysOrg.getOrgName().contains(orgName)) {
				list1.add(sysOrg);
			}
		}
		return list1;
	}
	/**
	 * 获取选定组织详细
	 * @param orgType
	 * @param orgSelectId
	 * @return
	 */
	private List<SysOrg> getCombination(Long orgType, String orgSelectId) {
		List list = this.sysOrgService.getByOrgType(orgType);
		if (BeanUtils.isEmpty(list))
			list = new ArrayList();
		List list2 = getOrgLstByOrgSelectId(orgSelectId);
		if (BeanUtils.isEmpty(list2))
			list.addAll(list2);
		return list;
	}
	/**
	 * 获取选定组织的信息
	 * @param orgSelectId
	 * @return
	 */
	private List<SysOrg> getOrgLstByOrgSelectId(String orgSelectId) {
		List list = new ArrayList();
		if (BeanUtils.isEmpty(orgSelectId))
			return list;
		JSONArray jsonAry = JSONArray.fromObject(orgSelectId);
		for (Iterator localIterator = jsonAry.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject json = (JSONObject) obj;
			String id = (String) json.get("id");
			SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(new Long(id));
			list.add(sysOrg);
		}
		return list;
	}

	public Long getByCurOrgId(SysOrg currentOrg) {
		List<SysOrg> list = getSysOrgListByOrgTactic();
		if (BeanUtils.isEmpty(list))
			return currentOrg.getOrgId();
		Set<Long> orgIds = replacePath(currentOrg.getPath());
		for (SysOrg sysOrg : list) {
			Long id = sysOrg.getOrgId();
			for (Long orgId : orgIds) {
				if (orgId.longValue() == id.longValue()) {
					return id;
				}
			}
		}
		return null;
	}

	private Set<Long> replacePath(String path) {
		if (StringUtil.isEmpty(path))
			return new HashSet();
		path = StringUtil.trimSufffix(path, ".");
		String[] aryPath = path.split("\\.");
		Set list = new HashSet();
		for (String tmp : aryPath) {
			list.add(new Long(tmp));
		}
		return list;
	}
}
