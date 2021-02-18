package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormDataScript;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.model.IFormData;
import com.cssrc.ibms.api.job.intf.IJobService;
import com.cssrc.ibms.api.job.model.IJob;
import com.cssrc.ibms.api.system.model.IDemension;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgParamService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgTypeService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserParamService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.intf.IUserUnderService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysOrgParam;
import com.cssrc.ibms.api.sysuser.model.ISysOrgType;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.ISysUserParam;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.api.sysuser.model.IUserUnder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.ProcessCmd;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.activity.util.BpmUtil;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.engine.IScript;
import com.cssrc.ibms.core.flow.dao.HistoryTaskInstanceDao;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.TaskOpinionService;
import com.cssrc.ibms.core.flow.service.thread.TaskThreadService;
import com.cssrc.ibms.core.flow.service.thread.TaskUserAssignService;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;

/**
 * 实现这个接口可以在groovy脚本中直接使用。
 * 
 * @author hhj。
 * 
 */
public class ScriptImpl implements IScript, IFormDataScript {
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	TaskOpinionService taskOpinionService;
	@Resource
	private ISysRoleService sysRoleService;
	@Resource
	private IPositionService positionService;
	@Resource
	private IUserUnderService userUnderService;
	@Resource
	private ISysOrgTypeService sysOrgTypeService;
	@Resource
	private ISysUserParamService sysUserParamService;
	@Resource
	private ProcessRunService processRunService;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private IFormHandlerService formHandlerService;
	@Resource
	private IJobService jobService;
	@Resource
	IUserPositionService userPositionService;
	@Resource
	IBpmService bpmService;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private ISysOrgParamService sysOrgParamService;
	@Resource
	private HistoryTaskInstanceDao historyTaskInstanceDao;
	@Resource
	private TaskExecutorService taskExecutorService;

	public static final String PreStepUpserApproverOrgId = "preStepUpserApproverOrgId";

	/**
	 * 判断当前用户是否属于该角色。
	 * 
	 * <pre>
	 * 在脚本中使用方法:
	 * scriptImpl.hasRole(name)
	 * </pre>
	 * 
	 * @param alias
	 *            角色别名
	 * @return
	 */
	public boolean hasRole(String alias) {
		long userId = UserContextUtil.getCurrentUserId().longValue();
		List<? extends ISysRole> roleList = this.sysRoleService
				.getByUserId(Long.valueOf(userId));
		for (ISysRole role : roleList) {
			if (role.getAlias().equals(alias)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断当前用户是否属于某组织。
	 * 
	 * @param userId
	 *            用户id。
	 * @param org
	 *            组织名 在脚本中使用方法: scriptImpl.isCurUserInOrg(String orgName);
	 * @return
	 */
	public boolean isCurUserInOrg(String orgName) {
		Long userId = UserContextUtil.getCurrentUserId();
		return isUserInOrg(userId.toString(), orgName);
	}

	/**
	 * 判断用户是否属于某组织（存在多级组织）。
	 * 
	 * <pre>
	 * 在脚本中使用方法: scriptImpl.isUserInOrgs(String userId, String orgName);
	 * </pre>
	 * 
	 * @param userId
	 *            用户ID
	 * @param org
	 *            组织名称
	 * @return
	 */
	public boolean isUserInOrgs(String userId, String orgName) {
		Long lUserId = Long.parseLong(userId);
		List<? extends ISysOrg> sysOrgList = sysOrgService
				.getByOrgName(orgName);
		if (sysOrgList == null) {
			return false;
		}
		ISysOrg sysOrg = sysOrgList.get(0);
		String path = "%" + sysOrg.getPath() + "%";
		List<? extends ISysOrg> list = sysOrgService.getOrgByUserIdPath(
				lUserId, path.trim());

		if (BeanUtils.isEmpty(list)) {
			return false;
		}

		return true;
	}

	/**
	 * 判断用户是否属于某组织。
	 * 
	 * <pre>
	 * 在脚本中使用方法: scriptImpl.isUserInOrg(String userId, String orgName);
	 * </pre>
	 * 
	 * @param userId
	 *            用户ID
	 * @param org
	 *            组织名称
	 * @return
	 */
	public boolean isUserInOrg(String userId, String orgName) {
		Long lUserId = Long.parseLong(userId);
		List<? extends ISysOrg> list = sysOrgService.getOrgsByUserId(lUserId);
		if (BeanUtils.isEmpty(list))
			return false;
		for (ISysOrg sysOrg : list) {
			if (sysOrg.getOrgName().equals(orgName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取当前登录用户的ID
	 */
	public String getCurrentUserID() {
		return UserContextUtil.getCurrentUserId().toString();
	}

	/**
	 * 更新表记录字段值。where id=? 表记录_主键_名称 为id
	 * 
	 * @param dataId
	 *            表记录_主键_值
	 * @param tableName
	 *            表名称
	 * @param fieldNameArray
	 *            字段_名称 数组
	 * @param valueArray
	 *            字段_值 数组
	 */
	public void setFormDataValue(String dataId, String tableName,
			String[] fieldNameArray, Object[] valueArray) {

		String updatePartSql = "";
		Object[] valueObjArray = new Object[fieldNameArray.length + 1];
		for (int j = 0; j < fieldNameArray.length; j++) {
			valueObjArray[j] = valueArray[j];
			updatePartSql += fieldNameArray[j] + "=? ";
			if (j < fieldNameArray.length - 1) {
				updatePartSql += ",";
			}
		}
		valueObjArray[fieldNameArray.length] = dataId;
		String upSql = "update " + tableName + " SET " + updatePartSql
				+ " where id=?";
		jdbcTemplate.update(upSql, valueObjArray);
	}

	/**
	 * 更新表记录字段值。where keyName=?
	 * 
	 * @param dataId
	 *            表记录_主键_值
	 * @param tableName
	 *            表名称
	 * @param fieldNameArray
	 *            字段_名称 数组
	 * @param valueArray
	 *            字段_值 数组
	 * @param keyName
	 *            表记录_主键_名称
	 */
	public void setFormDataValue(String dataId, String tableName,
			String[] fieldNameArray, Object[] valueArray, String keyName) {

		String updatePartSql = "";
		Object[] valueObjArray = new Object[fieldNameArray.length + 1];
		for (int j = 0; j < fieldNameArray.length; j++) {
			valueObjArray[j] = valueArray[j];
			updatePartSql += fieldNameArray[j] + "=? ";
			if (j < fieldNameArray.length - 1) {
				updatePartSql += ",";
			}
		}
		valueObjArray[fieldNameArray.length] = dataId;
		String upSql = "update " + tableName + " SET " + updatePartSql
				+ " where " + keyName + "=?";
		jdbcTemplate.update(upSql, valueObjArray);
	}

	/**
	 * 按照日期格式要求，格式化日期字段
	 * 
	 * @param dataId
	 *            表记录_主键_值
	 * @param tableName
	 *            表名称
	 * @param dateFieldName
	 *            日期字段_名称
	 * @param dateValue
	 *            日期字段_值
	 * @param dateFormat
	 *            日期_转换格式
	 * @param keyName
	 *            表记录_主键_名称
	 */
	public void setFormDataValue(String dataId, String tableName,
			String dateFieldName, String dateValue, String dateFormat,
			String keyName) {
		jdbcTemplate.update("update " + tableName + " SET " + dateFieldName
				+ "=TO_DATE('" + dateValue + "','" + dateFormat + "')  where "
				+ keyName + "=" + dataId);
	}

	/**
	 * 给关联表中的“日期”字段赋值。
	 * 
	 * @param curTableName
	 *            当前表_名称
	 * @param curDataId
	 *            当前表_记录id
	 * @param curKeyName
	 *            当前表_主键字段 名称
	 * @param refTableName
	 *            需要更新的表_名称
	 * @param refDateFieldName
	 *            需要更新的表_日期字段
	 * @param refDateFieldValue
	 *            需要更新的表_日期字段值
	 * @param refDateFormat
	 *            需要更新的表_日期字段 格式
	 */
	public void setRefFormDataValue(String curTableName, String curDataId,
			String curKeyName, String refTableName, String refDateFieldName,
			String refDateFieldValue, String refDateFormat) {
		String sourceData = CommonTools.Obj2String(this.getFormDataValue(
				curDataId, curTableName, curKeyName));
		if (!sourceData.equals("")) {
			String[] sourceData_arr = sourceData.split(",");
			for (String para : sourceData_arr) {
				String sql = "update " + refTableName + " SET "
						+ refDateFieldName + "=TO_DATE('" + refDateFieldValue
						+ "','" + refDateFormat + "')  where ID =? ";
				jdbcTemplate.update(sql, new Object[] { para });
			}
		}

	}

	/**
	 * 给关联表中的“多个”字段赋值。
	 * 
	 * @param curTableName
	 *            当前表_名称
	 * @param dataId
	 *            当前表_记录id
	 * @param curField
	 *            当前表_字段 单个
	 * @param refTableName
	 *            需要更新的表_名称
	 * @param refFieldNameArray
	 *            需要更新的表_字段 数组
	 * @param refValueArray
	 *            需要更新的表_字段值 数组
	 */
	public void setRefFormDataValue(String curTableName, String dataId,
			String curField, String refTableName, String[] refFieldNameArray,
			Object[] refValueArray) {

		String updatePartSql = "";
		Object[] valueObjArray = new Object[refFieldNameArray.length + 1];
		for (int j = 0; j < refFieldNameArray.length; j++) {
			valueObjArray[j] = refValueArray[j];
			updatePartSql += refFieldNameArray[j] + "=? ";
			if (j < refFieldNameArray.length - 1) {
				updatePartSql += ",";
			}
		}

		String sourceData = CommonTools.Obj2String(this.getFormDataValue(
				dataId, curTableName, curField));
		if (!sourceData.equals("")) {
			String[] sourceData_arr = sourceData.split(",");
			for (String para : sourceData_arr) {
				valueObjArray[refFieldNameArray.length] = para;
				String upSql = "update " + refTableName + " SET "
						+ updatePartSql + " where ID = ?";
				jdbcTemplate.update(upSql, valueObjArray);
			}
		}
	}

	/**
	 * 根据主键ID值获取记录的其他字段值
	 * 
	 * @param tableName
	 *            当前表_名称
	 * @param dataId
	 *            当前表_记录id
	 * @param fieldName
	 *            当前表_字段 单个
	 */
	public Object getFormDataValue(String dataId, String tableName,
			String fieldName) {
		String selSql = "select " + fieldName + " from " + tableName
				+ " where id=?";
		Map<String, Object> map = jdbcTemplate.queryForMap(selSql,
				new Object[] { dataId });
		return map.get(fieldName.toUpperCase());
	}

	/**
	 * 根据表tableName的字段fieldName的值fieldValue，查询所有列表记录。
	 * 
	 * @param tableName
	 *            当前表_名称
	 * @param fieldName
	 *            当前表_查询的字段_名称
	 * @param fieldValue
	 *            当前表_查询的字段_值
	 * @param orderFieldName
	 *            当前表_查询的排序规则
	 */
	public List getRefDataValue(String tableName, String fieldName,
			String fieldValue, String orderFieldName) {
		String selSql = "select * from " + tableName + " where " + fieldName
				+ "=? ";
		if (orderFieldName != null && !orderFieldName.equals("")) {
			selSql += " order by " + orderFieldName;
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(selSql,
				new Object[] { fieldValue });
		return list;
	}

	/**
	 * 根据表tableName的字段fieldName的值fieldValue，查询所有列表记录后，获取返回字段的集合。
	 * 
	 * @param tableName
	 *            当前表_名称
	 * @param fieldName
	 *            当前表_查询的字段_名称
	 * @param fieldValue
	 *            当前表_查询的字段_值
	 * @param orderFieldName
	 *            当前表_查询的排序规则
	 * @param retrunField
	 *            获取返回字段
	 */
	public Set<String> getFieldValues(String tableName, String fieldName,
			String fieldValue, String orderFieldName, String retrunField) {
		List<Map<String, Object>> list = this.getRefDataValue(tableName,
				fieldName, fieldValue, orderFieldName);
		Set<String> userSet = new HashSet<String>();
		for (Map<String, Object> u : list) {
			userSet.add(CommonTools.Obj2String(u.get(retrunField)));
		}
		return userSet;
	}

	/**
	 * 获取“当前系统登录”帐号用户“名称”。
	 */
	public String getUsername() {
		ISysUser sysUser = (ISysUser) UserContextUtil.getCurrentUser();
		if (sysUser == null)
			return "";
		return sysUser.getUsername();
	}

	/**
	 * 获取“当前系统登录”帐号用户id。
	 */
	public long getCurrentUserId() {
		long userId = UserContextUtil.getCurrentUserId().longValue();
		return userId;
	}

	/**
	 * 获取“当前系统登录”帐号用户 “帐号”。
	 */
	public String getCurrentFullName() {
		ISysUser sysUser = (ISysUser) UserContextUtil.getCurrentUser();
		if (sysUser == null)
			return "";
		return sysUser.getFullname();
	}

	/**
	 * 获取“当前系统登录”帐号用户SysUser对象。
	 */
	public ISysUser getCurrentUser() {
		return (ISysUser) UserContextUtil.getCurrentUser();
	}

	/**
	 * 获取当前服务器日期，格式为yyyy-MM-dd。
	 */
	public String getCurrentDate() {
		return TimeUtil.getCurrentDate();
	}

	/**
	 * 获取当前服务器日期。
	 */
	public String getCurrentDate(String style) {
		if (StringUtils.isEmpty(style)) {
			style = "yyyy-MM-dd";
		}
		return TimeUtil.getCurrentDate(style);
	}

	/**
	 * 获取“当前系统登录”用户的组织SysOrg对象。
	 */
	public ISysOrg getCurrentOrg() {
		ISysOrg sysOrg = (ISysOrg) UserContextUtil.getCurrentOrg();
		return sysOrg;
	}

	/**
	 * 获取“当前系统登录”用户的组织OrgId。
	 */
	public Long getCurrentOrgId() throws Exception {
		ISysOrg sysOrg = (ISysOrg) UserContextUtil.getCurrentOrg();
		if (sysOrg == null) {
			return Long.valueOf(0L);
		}
		return sysOrg.getOrgId();
	}

	/**
	 * 获取“当前系统登录”用户的组织OrgName，组织名称。
	 */
	public String getCurrentOrgName() {
		ISysOrg sysOrg = (ISysOrg) UserContextUtil.getCurrentOrg();
		if (sysOrg == null) {
			return "";
		}

		return sysOrg.getOrgName();
	}

	/**
	 * 获取“当前系统登录”用户的"主"组织PrimaryOrgName，"主"组织名称。
	 */
	public String getCurrentPrimaryOrgName() {
		Long userId = UserContextUtil.getCurrentUserId();
		ISysOrg sysOrg = this.sysOrgService.getPrimaryOrgByUserId(userId);
		if (sysOrg == null)
			return "";
		return sysOrg.getOrgName();
	}

	/**
	 * 获取用户的"主"组织PrimaryOrgId，"主"组织id。
	 */
	public Long getCurrentPrimaryOrgId() {
		Long userId = UserContextUtil.getCurrentUserId();
		ISysOrg sysOrg = this.sysOrgService.getPrimaryOrgByUserId(userId);
		if (sysOrg == null)
			return Long.valueOf(0L);
		return sysOrg.getOrgId();
	}

	/**
	 * 获取用户userId的"主"组织PrimaryOrgId，"主"组织id。
	 */
	public Long getUserOrgId(Long userId) {
		ISysOrg sysOrg = this.sysOrgService.getPrimaryOrgByUserId(userId);
		if (sysOrg == null)
			return Long.valueOf(0L);
		return sysOrg.getOrgId();
	}

	/**
	 * 获取用户userId的"主"组织PrimaryOrgName，"主"组织名称。
	 */
	public String getUserOrgName(Long userId) {
		ISysOrg sysOrg = this.sysOrgService.getPrimaryOrgByUserId(userId);
		if (sysOrg == null)
			return "";
		return sysOrg.getOrgName();
	}

	/**
	 * 获取“当前系统登录”用户的角色列表。
	 */
	public List<ISysRole> getCurrentUserRoles() {
		long userId = UserContextUtil.getCurrentUserId().longValue();
		List list = this.sysRoleService.getByUserId(Long.valueOf(userId));
		return list;
	}

	/**
	 * 获取用户userId的角色列表。
	 */
	public List<ISysRole> getUserRoles(String strUserId) {
		if (StringUtil.isEmpty(strUserId)) {
			return Collections.EMPTY_LIST;
		}
		Long userId = Long.valueOf(Long.parseLong(strUserId));
		List list = this.sysRoleService.getByUserId(userId);
		return list;
	}

	/**
	 * 判断用户userId是否有该角色role。
	 */
	public boolean isUserInRole(String userId, String role) {
		Long lUserId = Long.valueOf(Long.parseLong(userId));
		List<? extends ISysRole> list = this.sysRoleService
				.getByUserId(lUserId);
		if (BeanUtils.isEmpty(list))
			return false;
		for (ISysRole sysRole : list) {
			if (sysRole.getAlias().equals(role)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取用户userId的主岗位名称。
	 */
	public String getUserPos(Long userId) {
		String posName = "";
		IPosition position = this.positionService.getPosByUserId(userId);
		if (!BeanUtils.isEmpty(position)) {
			posName = position.getPosName();
		}
		return posName;
	}

	/**
	 * 获取用户userId的主岗位id，posId。
	 */
	public String getUserPosId(Long userId) {
		String posId = "";
		IPosition position = this.positionService.getPosByUserId(userId);
		if (!BeanUtils.isEmpty(position)) {
			posId = position.getPosId().toString();
		}
		return posId;
	}

	/**
	 * 获取用户帐户名称（account），获取主岗位id，posId。
	 */
	public String getPosIdByAccount(String account) {
		ISysUser sysUser = this.sysUserService.getByUsername(account);
		if (sysUser == null) {
			return "";
		}
		String posId = "";
		IPosition position = this.positionService.getPosByUserId(sysUser
				.getUserId());
		if (!BeanUtils.isEmpty(position)) {
			posId = position.getPosId().toString();
		}
		return posId;
	}

	/**
	 * 获取用户帐户名称（account），获取主岗位名称。
	 */
	public String getPosNameByAccount(String account) {
		ISysUser sysUser = this.sysUserService.getByUsername(account);
		if (sysUser == null) {
			return "";
		}
		String name = "";
		IPosition position = this.positionService.getPosByUserId(sysUser
				.getUserId());
		if (!BeanUtils.isEmpty(position)) {
			name = position.getPosName();
		}
		return name;
	}

	/**
	 * 获取“当前系统登录”用户的主岗位名称。
	 */
	public String getCurUserPos() {
		long userId = UserContextUtil.getCurrentUserId().longValue();
		String posName = getUserPos(Long.valueOf(userId));
		return posName;
	}

	/**
	 * 获取“当前系统登录”用户的主岗位名称。
	 */
	public String getCurUserPosName() {
		long userId = UserContextUtil.getCurrentUserId().longValue();
		String posName = getUserPos(Long.valueOf(userId));
		return posName;
	}

	/**
	 * 获取“当前系统登录”用户的主岗位id，posId。
	 */
	public String getCurUserPosId() {
		long userId = UserContextUtil.getCurrentUserId().longValue();
		String posId = getUserPosId(Long.valueOf(userId));
		return posId;
	}

	/**
	 * 获取“当前系统登录”用户的主岗位id，posId。
	 */
	public String getCurrentPosName() {
		IPosition position = (IPosition) UserContextUtil.getCurrentPos();
		if ((position != null)
				&& (StringUtil.isNotEmpty(position.getPosName()))) {
			return position.getPosName();
		}
		return "";
	}

	/**
	 * 获取“当前系统登录”用户的主岗位id，posId。
	 */
	public Long getCurrentPosId() {
		IPosition position = (IPosition) UserContextUtil.getCurrentPos();
		if ((position != null) && (position.getPosId() != null)) {
			return position.getPosId();
		}
		return null;
	}

	/**
	 * 获取上下文的ProcessCmd对象。
	 */
	public ProcessCmd getProcessCmd() {
		return TaskThreadService.getProcessCmd();
	}

	/**
	 * 流程：根据用户帐户list， 新指定流程执行人。
	 *
	 * @param task
	 *            当前任务task
	 * @param userAccout
	 *            用户帐号串，用“，”分开
	 */
	public void setAssigneeByAccount(TaskEntity task, String userAccout) {
		String[] aryAccount = userAccout.split(",");
		List userIds = new ArrayList();
		for (String str : aryAccount) {
			ISysUser sysUser = this.sysUserService.getByUsername(str);
			userIds.add(sysUser.getUserId().toString());
		}
		if (userIds.size() == 0) {
			return;
		}
		if (userIds.size() == 1) {
			task.setAssignee((String) userIds.get(0));
		} else {
			task.addCandidateUsers(userIds);
		}
	}

	/**
	 * 流程：启动流程
	 *
	 * @param flowKey
	 *            流程定义key
	 * @param businnessKey
	 *            业务数据key
	 * @param vars
	 *            流程变量map
	 */
	public ProcessRun startFlow(String flowKey, String businnessKey,
			Map<String, Object> vars) throws Exception {
		ProcessCmd processCmd = new ProcessCmd();
		processCmd.setFlowKey(flowKey);
		if (StringUtils.isEmpty(businnessKey)) {
			businnessKey = Long.toString(UniqueIdUtil.genId());
		}
		processCmd.setBusinessKey(businnessKey);
		if (BeanUtils.isNotEmpty(vars)) {
			processCmd.setVariables(vars);
		}
		return this.processRunService.startProcess(processCmd);
	}

	/**
	 * 获取 用户名称
	 *
	 * @param accout
	 *            用户帐号
	 */
	public String getFullNameByAccount(String accout) {
		ISysUser sysUser = this.sysUserService.getByUsername(accout);
		if (sysUser == null) {
			return "";
		}
		return sysUser.getFullname();
	}

	/**
	 * 获取 用户名称，用“,”撇开的字符串
	 *
	 * @param accouts
	 *            用户帐号 ，用“,”撇开的字符串
	 */
	public String getFullNameByAccounts(String accouts) {
		List<? extends ISysUser> sysUsers = this.sysUserService
				.getByUsernames(accouts);
		StringBuilder fullNames = new StringBuilder();
		for (ISysUser s : sysUsers) {
			fullNames.append(s.getFullname());
			fullNames.append(",");
		}
		if (fullNames.length() > 1) {
			fullNames.deleteCharAt(fullNames.length() - 1);
		}
		return fullNames.toString();
	}

	/**
	 * 获取 用户ID，用“,”撇开的字符串
	 *
	 * @param accouts
	 *            用户帐号 ，用“,”撇开的字符串
	 */
	public String getUserIdsByAccounts(String accouts) {
		List<? extends ISysUser> sysUsers = this.sysUserService
				.getByUsernames(accouts);
		StringBuilder ids = new StringBuilder();
		for (ISysUser s : sysUsers) {
			ids.append(s.getUserId());
			ids.append(",");
		}
		if (ids.length() > 1) {
			ids.deleteCharAt(ids.length() - 1);
		}
		return ids.toString();
	}

	/**
	 * 获取 用户ID
	 *
	 * @param accout
	 *            用户帐号
	 */
	public String getUserIdByAccount(String account) {
		ISysUser sysUser = this.sysUserService.getByUsername(account);
		if (sysUser == null) {
			return "";
		}
		return sysUser.getUserId().toString();
	}

	/**
	 * 获取 用户ID集合set
	 *
	 * @param accout
	 *            用户帐号
	 */
	public Set<String> getUserIdSetByAccount(String account) {
		Set userSet = new HashSet();
		String userId = getUserIdByAccount(account);
		userSet.add(userId);
		return userSet;
	}

	/**
	 * 根据用户帐号获取组织名称
	 *
	 * @param accout
	 *            用户帐号
	 */
	public String getOrgNameByAccount(String account) {
		ISysOrg sysOrg = this.sysOrgService.getOrgByUsername(account);
		return sysOrg.getOrgName();
	}

	/**
	 * 根据用户帐号获取组织名称orgid
	 *
	 * @param accout
	 *            用户帐号
	 */
	public String getOrgIdByAccount(String account) {
		ISysOrg sysOrg = this.sysOrgService.getOrgByUsername(account);
		return sysOrg.getOrgId().toString();
	}

	/**
	 * 判断两个组织是否为父子关系。（中间可以隔开多层父节点）
	 *
	 * @param sonOrgId
	 *            子组织id
	 * @param parentOrgId
	 *            父组织id
	 */
	public boolean getOrgBelongTo(String sonOrgId, Long parentOrgId) {
		ISysOrg sonSysOrg = (ISysOrg) this.sysOrgService.getById(Long
				.valueOf(Long.parseLong(sonOrgId)));
		ISysOrg parentSysOrg = (ISysOrg) this.sysOrgService
				.getById(parentOrgId);
		if ((BeanUtils.isEmpty(sonSysOrg)) || (BeanUtils.isEmpty(parentSysOrg)))
			return false;
		String sonPath = sonSysOrg.getPath();
		String parentPath = parentSysOrg.getPath();
		int result = sonPath.indexOf(parentPath);
		return result > -1;
	}

	/**
	 * 根据组织id获取组织名称
	 *
	 * @param orgId
	 *            组织id
	 */
	public String getOrgNameById(Long orgId) {
		String orgName = "";
		try {
			ISysOrg sysOrg = (ISysOrg) this.sysOrgService.getById(orgId);
			orgName = sysOrg.getOrgName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgName;
	}

	/**
	 * 根据组织id获取组织id,即：判断当前组织id是否存在数据库中。
	 * 
	 * @param orgId
	 *            组织id
	 */
	public String getOrgIdById(String orgId) {
		String rOrgId = "";
		try {
			ISysOrg sysOrg = (ISysOrg) this.sysOrgService.getById(Long
					.valueOf(orgId));
			rOrgId = String.valueOf(sysOrg.getOrgId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rOrgId;
	}

	/**
	 * 根据组织id获取组织名称,即：判断当前组织id是否存在数据库中。
	 * 
	 * @param orgId
	 *            组织id
	 */
	public String getOrgNameById(String orgId) {
		String orgName = "";
		try {
			ISysOrg sysOrg = (ISysOrg) this.sysOrgService.getById(Long
					.valueOf(orgId));
			orgName = sysOrg.getOrgName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgName;
	}

	/**
	 * 获取"当前组织"的组织类别
	 */
	public ISysOrgType getCurrentOrgType() {
		ISysOrg sysOrg = (ISysOrg) UserContextUtil.getCurrentOrg();
		if (sysOrg == null)
			return null;
		Long orgType = sysOrg.getOrgType();
		if (orgType == null)
			return null;
		ISysOrgType sysOrgType = this.sysOrgTypeService.getById(orgType);
		return sysOrgType;
	}

	/**
	 * 获取"当前组织"的组织类别的名称
	 */
	public String getCurrentOrgTypeName() {
		ISysOrg sysOrg = (ISysOrg) UserContextUtil.getCurrentOrg();
		if (sysOrg == null)
			return "";
		Long orgType = sysOrg.getOrgType();
		if (orgType == null)
			return "";
		ISysOrgType sysOrgType = (ISysOrgType) this.sysOrgTypeService
				.getById(orgType);
		return sysOrgType.getName();
	}

	/**
	 * 获取"当前组织"的组织类别的名称
	 */
	public Object getParaValue(String paramKey) {
		Long currentUserId = UserContextUtil.getCurrentUserId();

		return getParaValueByUser(currentUserId, paramKey);
	}

	/**
	 * 获取用户userid的扩展属性paramkey的值。
	 */
	public Object getParaValueByUser(Long userId, String paramKey) {
		ISysUserParam sysUserParam = this.sysUserParamService
				.getByParamKeyAndUserId(paramKey, userId.longValue());
		if (sysUserParam == null) {
			return null;
		}
		String dataType = sysUserParam.getDataType();
		if ("String".equals(dataType))
			return sysUserParam.getParamValue();
		if ("Integer".equals(dataType)) {
			return sysUserParam.getParamIntValue();
		}
		return sysUserParam.getParamDateValue();
	}

	/**
	 * 获取“当前系统登录”用户的主岗位负责人的岗位名称。
	 */
	public String getCurDirectLeaderPos() {
		String userId = UserContextUtil.getCurrentUserId().toString();
		String posName = getDirectLeaderPosByUserId(userId);
		return posName;
	}

	/**
	 * 获取用户userid的主岗位负责人的岗位名称。
	 */
	public String getDirectLeaderPosByUserId(String userId) {
		String posName = "";
		posName = this.taskExecutorService.getLeaderPosByUserId(Long
				.valueOf(Long.parseLong(userId)));
		return posName;
	}

	/**
	 * 获取用户userId的组织的直属领导
	 */
	public Set<String> getDirectLeaderByUserId(String userId) {
		Set userSet = new HashSet();
		List<TaskExecutor> userList = this.taskExecutorService
				.getLeaderByUserId(Long.valueOf(Long.parseLong(userId)));

		if (BeanUtils.isEmpty(userList))
			return userSet;
		for (TaskExecutor user : userList) {
			userSet.add(user.getExecuteId());
		}

		return userSet;
	}

	//

	/**
	 * 根据组织id获取组织负责人,构造taskExecutor列表
	 */
	public List<TaskExecutor> getExecutor(Long orgId) {
		// 根据组织id获取组织负责人
		List<? extends IUserPosition> userPositionList = this.userPositionService
				.getChargeByOrgId(orgId);
		if (BeanUtils.isEmpty(userPositionList))
			return null;
		List executors = new ArrayList();
		for (IUserPosition userPosition : userPositionList) {
			TaskExecutor taskExecutor = TaskExecutor.getTaskUser(userPosition
					.getUserId().toString(), userPosition.getUsername());
			executors.add(taskExecutor);
		}
		return executors;
	}
	
	public Set<String> getChargeByOrgId(Long orgId) {
		// 根据组织id获取组织负责人
		List<? extends IUserPosition> userPositionList = this.userPositionService.getChargeByOrgId(orgId);
		if (BeanUtils.isEmpty(userPositionList))
			return null;
		Set executors = new HashSet();
		for (IUserPosition userPosition : userPositionList) {
			TaskExecutor taskExecutor = TaskExecutor.getTaskUser(userPosition
					.getUserId().toString(), userPosition.getUsername());
			executors.add(taskExecutor.getExecuteId());
		}
		return executors;
	}

	
	/**
	 * 逐级审批跳转规则 @ orgType 所队，科处 ，小组，其他组织
	 * */
	public boolean isTopUpserApprove(Long orgType) {
		// 获取上下文的ProcessCmd对象。
		ProcessCmd cmd = getProcessCmd();
		String taskId = cmd.getTaskId();
		// 获取流程变量中的orgId
		Long orgId = (Long) this.bpmService.getVarsByTaskId(taskId).get(
				"preStepUpserApproverOrgId");

		if (BeanUtils.isEmpty(orgId))
			return true;
		// 根据orgId 获取组织sysorg对象
		ISysOrg sysOrg = (ISysOrg) this.sysOrgService.getById(orgId);
		if ((BeanUtils.isEmpty(sysOrg))
				|| (orgType.equals(sysOrg.getOrgType()))) {
			TaskEntity task = this.bpmService.getTask(taskId);
			String actInstId = task.getProcessInstanceId();
			this.runtimeService.removeVariable(actInstId,
					"preStepUpserApproverOrgId");
			return true;
		}
		return false;
	}

	/**
	 * 因为脚本 表中有对应的数据，所以 新增一个方法
     * 通过当前用户的组织类型 判断流程变量中的组织是不是符合
     * */
    public boolean isTopUpserApproveForCurUser() {
        List<?extends ISysOrg> orgs=sysOrgService.getByUserId(UserContextUtil.getCurrentUserId());
        for(ISysOrg org:orgs){
            if(isTopUpserApprove(org.getOrgType())){
                return true;
            }
        }
        return false;
    }
    
	/**
	 * 判断用户是否该部门的负责人
	 * */
	public boolean isDepartmentLeader(String userId, String orgId) {
		List<TaskExecutor> leaders = this.taskExecutorService
				.getLeaderByOrgId(Long.valueOf(Long.parseLong(orgId)));
		for (TaskExecutor leader : leaders) {
			if (userId.equals(leader.getExecuteId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断用户是否有该职务名称
	 */
	public boolean isJobName(String userId, String jobName) {
		String Id = "";
		if (StringUtils.isEmpty(userId)) {
			Id = UserContextUtil.getCurrentUserId().toString();
		} else {
			Id = userId;
		}

		String selectSql = "select b.jobname from cwm_sys_job b,CWM_SYS_USER_POSITION p ";
		selectSql = selectSql + "where b.jobid=p.jobid and p.userid=?";
		List<Map<String, Object>> map = this.jdbcTemplate
				.queryForList(selectSql,Id);
		for (Map p : map) {
			String posname = p.get("jobname").toString();
			if (posname.contains(jobName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获得上司执行人集合Set
	 * */
	public Set<String> getLeaderByUserId(Long userId) {
		// 获得上司执行人列表
		Set set = new HashSet();
		List<? extends IUserUnder> userList = this.userUnderService
				.getMyLeader(userId);
		for (IUserUnder user : userList) {
			set.add(TaskExecutor.getTaskUser(user.getUserid().toString(),
					user.getLeaderName()));
		}
		Set userSet = new HashSet();
		for (Iterator it = set.iterator(); it.hasNext();) {
			userSet.add(((TaskExecutor) it.next()).getExecuteId());
		}
		return userSet;
	}

	/**
	 * 获得当前用户的上司执行人集合Set
	 * */
	public Set<String> getMyLeader() {
		Long userId = UserContextUtil.getCurrentUserId();
		Set userSet = getLeaderByUserId(userId);
		return userSet;
	}

	/**
	 * 获得当前用户的的下属id *
	 */
	public Set<String> getMyUnderUserId() {
		Long userId = UserContextUtil.getCurrentUserId();
		return this.userUnderService.getMyUnderUserId(userId);
	}

	/**
	 * 更新记录的属性值。
	 * 
	 * @param businessKey
	 *            主记录id值
	 * @param tableName
	 *            表名
	 * @param map
	 *            更新记录的map键值对。
	 */
	public void updateByTableName(String businessKey, String tableName,
			Map<String, Object> map) {
		String sql = "";
		if (map.size() == 0)
			return;
		Object[] objs = new Object[map.size() + 1];
		int count = 0;
		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry obj = (Map.Entry) it.next();
			sql = sql + ", " + (String) obj.getKey() + "=?";
			objs[count] = obj.getValue();
			count++;
		}
		objs[count] = businessKey;
		sql = sql.replaceFirst(",", "");
		sql = "update " + tableName + " set " + sql + " where ID=?";
		this.jdbcTemplate.update(sql, objs);
	}

	/**
	 * 获得当前用户的的负责人。
	 */
	public String getMgrByOrgIds() {
		Long userId = UserContextUtil.getCurrentUserId();
		List sysUser = this.sysUserService.getOrgMainUser(userId);
		if (BeanUtils.isEmpty(sysUser))
			return "暂时没有负责人";
		return ((ISysUser) sysUser.get(0)).getFullname();
	}

	/**
	 * //担任某组织岗位和具有某角色身份的用户(包括之前担任的)
	 * */
	public ISysUser getUserByCurOrgRoleAlias(String roleNameAlias) {
		ISysOrg curOrg = (ISysOrg) UserContextUtil.getCurrentOrg();
		ISysRole sysRole = this.sysRoleService.getByRoleAlias(roleNameAlias);
		if ((curOrg != null) && (sysRole != null)) {
			// 担任某组织岗位和具有某角色身份的用户(包括之前担任的)
			List sysUserList = this.sysUserService.getUserByRoleIdOrgId(
					sysRole.getRoleId(), curOrg.getOrgId());
			if (BeanUtils.isNotEmpty(sysUserList))
				return (ISysUser) sysUserList.get(0);
		}
		return null;
	}

	/**
	 * //担任某组织岗位和具有某角色身份的用户(包括之前担任的) 全名称
	 * */
	public String getUserFullnameByCurOrgRoleAlias(String roleNameAlias) {
		ISysUser sysUser = getUserByCurOrgRoleAlias(roleNameAlias);
		return sysUser == null ? "" : sysUser.getFullname();
	}

	/**
	 * 在组织里担当该岗位的用户集合
	 * */
	public Set<String> getUsersByOrgAndPos(Long startOrgId, String posName) {
		Set users = new HashSet();
		if (startOrgId == null)
			return users;
		ISysOrg sysOrg = (ISysOrg) this.sysOrgService.getById(startOrgId);
		List posList = this.positionService.getByPosName(posName);
		if (BeanUtils.isEmpty(posList)) {
			return users;
		}
		Long posId = ((IPosition) posList.get(0)).getPosId();
		String[] upOrgPaths = sysOrg.getPath().split("[.]");
		for (int i = upOrgPaths.length - 1; i > 0; i--) {
			ISysOrg tempOrg = (ISysOrg) this.sysOrgService.getById(new Long(
					upOrgPaths[i]));
			List<? extends ISysUser> sysUserList = this.sysUserService
					.getByOrgIdPosId(tempOrg.getOrgId(), posId);
			for (ISysUser sysUser : sysUserList) {
				users.add(sysUser.getUserId().toString());
			}
		}
		return users;
	}

	/**
	 * 在组织里担当该职务的用户集合
	 * */
	public Set<String> getLeaderByOrgName(String orgId, String jobCode) {
		Set users = new HashSet();
		if ((orgId == null) || (jobCode == null))
			return users;
		ISysOrg sysOrg = (ISysOrg) this.sysOrgService.getById(Long.valueOf(Long
				.parseLong(orgId)));
		List<? extends IPosition> orgPosListByOrgIds = this.positionService
				.getOrgPosListByOrgIds(sysOrg.getOrgId().toString());
		IJob byJobCode = this.jobService.getByJobCode(jobCode);
		Long jobId = byJobCode.getJobid();

		for (IPosition position : orgPosListByOrgIds) {
			Long id = position.getJobId();
			if (id.equals(jobId)) {
				List<Long> userIdsByPosId = this.userPositionService
						.getUserIdsByPosId(position.getPosId());
				for (Long long1 : userIdsByPosId) {
					users.add(long1.toString());
				}
			}
		}
		return users;
	}

	/**
	 * TODO 这个方法有问题！！！
	 * */
	public Set<String> getChargeOfOrgId(String orgId) throws Exception {
		Set users = new HashSet();
		if (orgId == null)
			return users;
		String fgbm = "[{\"fgbm\":\"" + orgId + "\"}]";
		List<? extends ISysUser> byUserParam = this.sysUserService
				.getByUserParam(fgbm);
		for (ISysUser sysUser : byUserParam) {
			users.add(sysUser.getUserId().toString());
		}
		return users;
	}

	/***
	 * 获取用户的职务代号code。
	 * */
	public String getJobCodeByUserId(String userId) {
		Long id = Long.valueOf(Long.parseLong(userId));
		IPosition posByUserId = this.positionService.getPosByUserId(id);
		Long jobId = posByUserId.getJobId();
		IJob job = (IJob) this.jobService.getById(jobId);
		String jobKey = "";
		if (BeanUtils.isNotEmpty(job)) {
			jobKey = job.getJobcode();
		}
		return jobKey;
	}

	/***
	 * 获取当前登陆用户的职务job。
	 * */
	public IJob getCurrentJob() {
		IPosition pos = (IPosition) UserContextUtil.getCurrentPos();
		if ((pos == null) || (pos.getJobId() == null)) {
			return null;
		}
		Long jobId = pos.getJobId();
		IJob job = (IJob) this.jobService.getById(jobId);
		return job;
	}

	/***
	 * 获取当前登陆用户的职务job名称。
	 * */
	public String getCurrentJobName() {
		IJob job = getCurrentJob();
		if ((job != null) && (job.getJobname() != null)) {
			return job.getJobname();
		}
		return "";
	}

	/***
	 * 获取当前登陆用户的职务代号code。
	 * */
	public String getCurrentJobCode() {
		IJob job = getCurrentJob();
		if ((job != null) && (job.getJobcode() != null)) {
			return job.getJobcode();
		}
		return "";
	}

	/***
	 * 获取当前登陆用户的职务等级 //默认为0是公共职务（例如每个部门都有老板这个职务），1是该组织独有职务 *
	 */
	public Short getCurrentJobGrade() {
		IJob job = getCurrentJob();
		if ((job == null) || (job.getGrade() == null)) {
			return null;
		}
		return job.getGrade();
	}

	/***
	 * 判断子表是否有数据
	 * 
	 * @param tableName
	 *            子表表名
	 * @param fk
	 *            主表id
	 * */
	public boolean isSubTableHasData(String tableName, Long fk) {
		List list = this.formHandlerService.getByFk(tableName, fk);
		return list.size() > 0;
	}

	/***
	 * 获取子表数据
	 * 
	 * @param tableName
	 *            子表表名
	 * @param fk
	 *            主表id
	 * */
	public List<Map<String, Object>> getByFk(String subTableName, Long fk) {
		List list = this.formHandlerService.getByFk(subTableName, fk);
		return list;
	}

	/***
	 * 获取主流程的某节点，最后一条审批意见的执行人。
	 * 
	 * @param flowRunId
	 *            流程
	 * @param nodeId
	 *            节点id
	 * */
	public Set<String> getAuditByMainInstId(Long flowRunId, String nodeId) {
		Set set = new LinkedHashSet();
		ProcessRun processRun = (ProcessRun) this.processRunService
				.getById(flowRunId);

		if (processRun == null) {
			return set;
		}
		if (processRun.getParentId() != null) {
			processRun = (ProcessRun) this.processRunService.getById(processRun
					.getParentId());
			TaskOpinion taskOpinion = this.taskOpinionService
					.getLatestTaskOpinion(Long.valueOf(Long
							.parseLong(processRun.getActInstId())), nodeId);
			set.add(taskOpinion.getExeUserId().toString());
		}
		return set;
	}

	/***
	 * 获取当前用户组织的 指定组织类别（3L室处）的 组织id
	 * */
	public Long getCurrUserDeptId() {
		return getCurrUserOryByTypeId(Long.valueOf(3L));
	}

	/***
	 * 获取当前用户组织的 指定组织类别（2L所队）的 组织id
	 * */
	public Long getCurrUserUnitId() {
		return getCurrUserOryByTypeId(Long.valueOf(2L));
	}

	/**
	 * 获取当前用户组织的 指定组织类别的 组织id
	 * */
	private Long getCurrUserOryByTypeId(Long orgType) {
		Long id = Long.valueOf(0L);
		Long orgId = UserContextUtil.getCurrentOrgId();
		if (orgId == null)
			return id;
		ISysOrg sysOrg = (ISysOrg) this.sysOrgService.getById(orgId);
		String path = sysOrg.getPath();
		String[] pathArr = path.split("\\.");

		for (int i = pathArr.length - 1; i >= 0; i--) {
			String orgId2 = pathArr[i];
			if (StringUtil.isNotEmpty(orgId2)) {
				ISysOrg sysOrg2 = (ISysOrg) this.sysOrgService.getById(Long
						.valueOf(orgId2));
				if ((sysOrg2 != null) && (sysOrg2.getOrgType() == orgType.longValue())) {
					id = sysOrg2.getOrgId();
					break;
				}
			}
		}
		return id;
	}

	/**
	 * 获取当前用户组织的 指定组织类别的 组织名称
	 * */
	public String getCurrUserOrgNameByTypeId(Long orgType) {
		Long currUserOryByTypeId = getCurrUserOryByTypeId(orgType);
		ISysOrg sysOrg = (ISysOrg) this.sysOrgService
				.getById(currUserOryByTypeId);
		String orgName = "";
		if (BeanUtils.isNotEmpty(sysOrg)) {
			orgName = sysOrg.getOrgName();
		}
		return orgName;
	}

	/**
	 * 执行用户自定义的sql语句
	 * */
	public String executeSql(String sql) {
		try {
			if (StringUtil.isEmpty(sql)) {
				return "";
			}
			Long userId = UserContextUtil.getCurrentUserId();
			Long orgId = UserContextUtil.getCurrentOrgId();
			Long posId = UserContextUtil.getCurrentPosId();
			sql = sql.replaceAll("''", "'");
			if (userId != null) {// 替换为当前用户
				sql = sql.toUpperCase()
						.replace("[CUR_USER]", userId.toString());
			}
			if (orgId != null) {// 替换为当前组织
				sql = sql.toUpperCase().replace("[CUR_ORG]", orgId.toString());
			}
			if (posId != null) {// 替换为当前岗位
				sql = sql.toUpperCase().replace("[CUR_POS]", posId.toString());
			}
			List list = this.jdbcTemplate.queryForList(sql, String.class);
			if ((list != null) && (list.size() > 0)) {
				return (String) list.get(0);
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "请检查sql 语句是否正确";
	}

	/**
     * 执行用户自定义的sql语句
     * */
    public void updateSql(String sql) {
        try {
            if (StringUtil.isEmpty(sql)) {
                return;
            }
            Long userId = UserContextUtil.getCurrentUserId();
            Long orgId = UserContextUtil.getCurrentOrgId();
            Long posId = UserContextUtil.getCurrentPosId();
            sql = sql.replaceAll("''", "'");
            if (userId != null) {// 替换为当前用户
                sql = sql.toUpperCase()
                        .replace("[CUR_USER]", userId.toString());
            }
            if (orgId != null) {// 替换为当前组织
                sql = sql.toUpperCase().replace("[CUR_ORG]", orgId.toString());
            }
            if (posId != null) {// 替换为当前岗位
                sql = sql.toUpperCase().replace("[CUR_POS]", posId.toString());
            }
            this.jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
	/**
	 * 执行替换主表中的字段值
	 * */
	public void executeSql(IFormData formData, String sql) {
		Map dataMap = formData.getMainFields();
		if ((dataMap != null) && (!dataMap.isEmpty())) {
			Pattern pattern = Pattern.compile("<#(.*?)#>");
			Matcher matcher = pattern.matcher(sql);
			while (matcher.find()) {
				String str = matcher.group();
				String key = matcher.group(1);
				String val = MapUtil.getString(dataMap, key);
				sql = sql.replace(str, val);
			}
		}
		try {
			this.jdbcTemplate.execute(sql);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 获取系统登陆用户行政维度下，该组织类别下，该职务下， 对应岗位下的 人员。
	 * */
	public List<? extends ISysUser> getByOrgGradeAndJob(int grade, Long jobId) {
		ISysOrg sysOrg = (ISysOrg) UserContextUtil.getCurrentOrg();
		// 获取行政维度的组织类别
		List typeList = this.sysOrgTypeService
				.getByDemId(IDemension.ADMINSTRATION.longValue());
		Map map = convertToMap(typeList);
		// 获取当前组织的组织类别
		int curGrade = ((Integer) map.get(sysOrg.getOrgType())).intValue();

		while (grade != curGrade) {
			// 获取父组织的组织类别
			Long parentOrgId = sysOrg.getOrgSupId();
			if (parentOrgId.equals(ISysOrg.BEGIN_ORGID))
				break;
			sysOrg = (ISysOrg) this.sysOrgService.getById(parentOrgId);
			curGrade = ((Integer) map.get(sysOrg.getOrgType())).intValue();
		}
		if (curGrade != grade) {
			return new ArrayList();
		}
		// 获取该组织和该职务对应的岗位
		IPosition pos = this.positionService.getByOrgJobId(sysOrg.getOrgId(),
				jobId);
		// 获取该岗位的人员列表
		List users = this.sysUserService.getByPosId(pos.getPosId());

		return users;
	}

	/**
	 * 将list 转化为map
	 * */
	private Map<Long, Integer> convertToMap(List<ISysOrgType> typeList) {
		Map map = new HashMap();
		for (ISysOrgType orgType : typeList) {
			map.put(orgType.getId(),
					Integer.valueOf(orgType.getLevels().intValue()));
		}
		return map;
	}

	/**
	 * 判断系统登陆用户的组织orgid是否有该组织参数属性。
	 * */
	public Boolean hasParamKeyForOrg(String paramKey) {
		Long orgId = UserContextUtil.getCurrentOrgId();
		if (orgId == null) {
			return Boolean.valueOf(false);
		}
		return hasParamKey(orgId.toString(), paramKey);
	}

	/**
	 * 判断系统登陆用户的企业companyId是否有该组织参数属性。
	 * */
	public Boolean hasParamKeyForCompany(String paramKey) {
		Long companyId = UserContextUtil.getCurrentCompanyId();
		if (companyId == null) {
			return Boolean.valueOf(false);
		}
		return hasParamKey(companyId.toString(), paramKey);
	}

	/**
	 * 判断系统登陆用户的组织orgid或者企业companyId是否有该组织参数属性。
	 * */
	public Boolean hasParamKeyForOrgAndCompany(String paramKey) { // 判断系统登陆用户的组织orgid是否有该组织参数属性。
		boolean flag = hasParamKeyForOrg(paramKey).booleanValue();
		if (flag) {
			return Boolean.valueOf(flag);
		}
		flag = hasParamKeyForCompany(paramKey).booleanValue();

		return Boolean.valueOf(flag);
	}

	/**
	 * 判断该组织orgid是否有该组织参数属性。
	 * */
	public Boolean hasParamKey(String orgId, String paramKey) {
		if ((StringUtil.isEmpty(orgId)) || (StringUtil.isEmpty(paramKey))) {
			return Boolean.valueOf(false);
		}
		ISysOrgParam sysOrgParam = this.sysOrgParamService
				.getByParamKeyAndOrgId(paramKey, Long.valueOf(orgId));
		if (BeanUtils.isEmpty(sysOrgParam)) {
			ISysOrg org = (ISysOrg) this.sysOrgService.getById(Long
					.valueOf(orgId));
			if ((org != null) && (org.getCompanyId() != null)) {
				sysOrgParam = this.sysOrgParamService.getByParamKeyAndOrgId(
						paramKey, org.getCompanyId());
			}
			if (BeanUtils.isEmpty(sysOrgParam)) {
				return Boolean.valueOf(false);
			}
		}
		return Boolean.valueOf(true);
	}

	/**
	 * 获取该组织orgid是否有该组织参数属性 值。
	 * */
	public String getParamValueByOrgIdKey(String orgId, String paramKey) {
		if ((StringUtil.isEmpty(orgId)) || (StringUtil.isEmpty(paramKey))) {
			return null;
		}
		ISysOrgParam sysOrgParam = this.sysOrgParamService
				.getByParamKeyAndOrgId(paramKey, Long.valueOf(orgId));
		if (BeanUtils.isEmpty(sysOrgParam)) {
			return null;
		}
		return sysOrgParam.getParamValue();
	}

	/**
	 * 拥有该用户参数属性和用户参数属性值的用户id集合。
	 * */
	public Set<String> getUserByParamKeyValue(String paramKey, String paramValue) {
		Set userSet = new HashSet();
		List<? extends ISysUserParam> userList = this.sysUserParamService
				.getByParamKeyValue(paramKey, paramValue);
		if (BeanUtils.isEmpty(userList))
			return userSet;
		for (ISysUserParam user : userList) {
			userSet.add(user.getUserId().toString());
		}
		return userSet;
	}

	/**
	 * 一参数与另一组织参数值相同的用户id集合。
	 * */
	public Set<String> getFgUsers(String orgId, String fgDept, String fgUser) { // 获取该组织orgid是否有该组织参数属性
																				// 值。
		String paramValue = getParamValueByOrgIdKey(orgId, fgDept);
		if (paramValue == null) {
			ISysOrg org = (ISysOrg) this.sysOrgService.getById(Long
					.valueOf(orgId));
			if ((org != null) && (org.getCompanyId() != null)) {
				paramValue = getParamValueByOrgIdKey(org.getCompanyId()
						.toString(), fgDept);
			}
		}
		Set userSet = getUserByParamKeyValue(fgUser, paramValue);
		return userSet;
	}

	public Set<String> getByCompanyRole(String companyId, Long roleId) {
		return getByCompanyRole(companyId, roleId, false);
	}

	/**
	 * 获取属于该公司，并拥有该角色的（主管）用户列表
	 * */
	public Set<String> getByCompanyRole(String companyId, Long roleId,
			boolean ignoreCharge) {
		Set userSet = new HashSet();
		if ((StringUtils.isEmpty(companyId)) || (roleId == null)) {
			return userSet;
		}
		// 获取属于该公司，并拥有该角色的（主管）用户列表
		List<? extends ISysUser> userList = this.sysUserService
				.getByCompanyRole(Long.valueOf(companyId), roleId, false);
		if (BeanUtils.isEmpty(userList))
			return userSet;
		if ((userList.size() > 1) && (!ignoreCharge)) {
			userList = this.sysUserService.getByCompanyRole(
					Long.valueOf(companyId), roleId, true);
		}
		for (ISysUser user : userList) {
			userSet.add(user.getUserId().toString());
		}
		return userSet;
	}

	/**
	 * 判断系统登陆用户，是否拥有该用户参数属性。
	 * */
	public Boolean hasParamKeyForUser(String paramKey) {
		return hasParamKeyForUser(
				UserContextUtil.getCurrentUserId().toString(), paramKey);
	}

	/**
	 * 判断用户userId，是否拥有该用户参数属性。
	 * */
	public Boolean hasParamKeyForUser(String userId, String paramKey) {
		ISysUserParam sysUserParam = this.sysUserParamService
				.getByParamKeyAndUserId(paramKey, Long.valueOf(userId));
		if (BeanUtils.isEmpty(sysUserParam)) {
			return Boolean.valueOf(false);
		}
		return Boolean.valueOf(true);
	}

	/**
	 * 获取当前用户所在的公司（组织）。
	 * */
	public ISysOrg getCurrentCompany() {
		ISysOrg org = (ISysOrg) UserContextUtil.getCurrentCompany();
		if (org == null) {
			return null;
		}
		return (ISysOrg) UserContextUtil.getCurrentCompany();
	}

	/**
	 * 获取当前用户所在的公司（组织）的组织 类型。
	 * */
	public Long getCurrentCompanyType() {
		ISysOrg org = (ISysOrg) UserContextUtil.getCurrentCompany();
		if (org == null) {
			return null;
		}
		return UserContextUtil.getCurrentCompany().getOrgType();
	}

	/**
	 * 获取当前用户所在的公司（组织）的组织 orgid。
	 * */
	public Long getCurrentCompanyOrgId() {
		ISysOrg org = (ISysOrg) UserContextUtil.getCurrentCompany();
		if (org == null) {
			return null;
		}
		return UserContextUtil.getCurrentCompany().getOrgId();
	}

	/**
	 * 获取当前用户所在的公司（组织）的组织 名称orgname。
	 * */
	public String getCurrentCompanyOrgName() {
		ISysOrg org = (ISysOrg) UserContextUtil.getCurrentCompany();
		if (org == null) {
			return null;
		}
		return UserContextUtil.getCurrentCompany().getOrgName();
	}

	/**
	 * 获取系统登陆用户行政维度下，的该组织类别下，指定角色下的 人员。
	 * */
	public List<? extends ISysUser> getByOrgGradeAndRole(int grade, Long roleId) {
		ISysOrg sysOrg = (ISysOrg) UserContextUtil.getCurrentOrg();

		List typeList = this.sysOrgTypeService
				.getByDemId(IDemension.ADMINSTRATION.longValue());
		Map map = convertToMap(typeList);
		int curGrade = ((Integer) map.get(sysOrg.getOrgType())).intValue();

		while (grade != curGrade) {
			Long parentOrgId = sysOrg.getOrgSupId();
			if (parentOrgId.equals(ISysOrg.BEGIN_ORGID))
				break;
			sysOrg = (ISysOrg) this.sysOrgService.getById(parentOrgId);
			curGrade = ((Integer) map.get(sysOrg.getOrgType())).intValue();
		}
		if (curGrade != grade) {
			return new ArrayList();
		}
		List sysUsers = this.sysUserService.getByOrgRole(sysOrg.getOrgId(),
				roleId);
		return sysUsers;
	}

	/**
	 * 清理流程当前节点的人员选择的流程变量信息。
	 * */
	public void clearNodeUserMap() {
		ProcessCmd cmd = TaskThreadService.getProcessCmd();

		if ((cmd.isBack().intValue() == 1) || (cmd.isBack().intValue() == 2)) {
			HistoryTaskInstanceDao historyTaskInstanceDao = (HistoryTaskInstanceDao) AppUtil
					.getBean(HistoryTaskInstanceDao.class);
			RuntimeService runtimeService = (RuntimeService) AppUtil
					.getBean(RuntimeService.class);
			Long taskId = new Long(cmd.getTaskId());
			HistoricTaskInstanceEntity taskEnt = historyTaskInstanceDao
					.getById(taskId);
			String actInstanceId = taskEnt.getProcessInstanceId();
			TaskUserAssignService userAssignService = (TaskUserAssignService) AppUtil
					.getBean(TaskUserAssignService.class);
			userAssignService.clearNodeUserMap();

			runtimeService.removeVariable(actInstanceId,
					"preStepUpserApproverOrgId");
		}
	}

	/**
	 * 判断表formData.getFullTableName()
	 * 中，指定字段fieldName的值formData.getMainField(fieldName)，的数据 是否存在。
	 * */
	public void validDataExist(IFormData formData, String fieldName,
			String messages) {
		boolean isAdd = formData.isAdd();
		String sql = "";
		Object[] aryObj = (Object[]) null;
		Object obj = formData.getMainField(fieldName);
		String name = "";
		if (BeanUtils.isNotEmpty(obj)) {
			name = obj.toString().trim();
		}

		if (isAdd) {
			aryObj = new Object[1];
			aryObj[0] = name;
			sql = "select count(*) from " + formData.getFullTableName()
					+ " where " + fieldName + "=?";
		} else {
			aryObj = new Object[2];
			aryObj[0] = name;
			aryObj[1] = formData.getPkValue().getValue();
			sql = "select  count(*) from " + formData.getFullTableName()
					+ " where " + fieldName + "=? and "
					+ formData.getFormTable().getPkField() + "!=?";
		}
		Integer rtn = Integer.valueOf(this.jdbcTemplate
				.queryForInt(sql, aryObj));
		if (rtn.intValue() > 0) {
			if (StringUtil.isNotEmpty(messages)) {
				throw new RuntimeException(messages);
			}
			throw new RuntimeException(name + "数据已经存在,请检查表单数据!");
		}
	}

	/**
	 * 判断表formData.getFullTableName()
	 * 中，指定字段fieldName的值formData.getMainField(fieldName)，的数据 是否存在。
	 * */
	public void validDataExist(IFormData formData, String fieldName) {
		validDataExist(formData, fieldName, null);
	}

	/**
	 * 获取人员currentUserId，当前组织类别orgType的组织 负责人的user id集合
	 * */
	private Set<String> getUserStep(ISysOrg startOrg, Long currentUserId,
			Long orgType) {
		ISysOrg sysOrg = null;
		Set userSet = new LinkedHashSet();
		List<TaskExecutor> executorList = new ArrayList();
		ProcessCmd cmd = getProcessCmd();

		if (cmd == null)
			return userSet;
		GroovyScriptEngine groovyScriptEngine = (GroovyScriptEngine) AppUtil
				.getBean(GroovyScriptEngine.class);
		String actInstId = (String) groovyScriptEngine.getVariable("actInstId");
		Long preStepUpserApproverOrgId = (Long) this.runtimeService
				.getVariable(actInstId, "preStepUpserApproverOrgId");

		TaskExecutor executor;
		if (BeanUtils.isEmpty(preStepUpserApproverOrgId)) {
			sysOrg = startOrg;

			if (cmd.isBack().intValue() == 1) {
				Long approverOrgId = (Long) this.runtimeService.getVariable(
						actInstId, "ApproverOrgId");
				sysOrg = (ISysOrg) this.sysOrgService.getById(approverOrgId);
			}

			Long orgId = sysOrg.getOrgId();
			executorList = getExecutor(orgId);

			if (executorList.size() == 1) {
				executor = (TaskExecutor) executorList.get(0);

				if ((currentUserId.toString().equals(executor.getExecuteId()))
						&& (!sysOrg.getOrgType().equals(orgType))) {
					executorList = getExecutor(sysOrg.getOrgSupId());
					orgId = sysOrg.getOrgSupId();
				}
			}

			this.runtimeService.setVariable(actInstId,
					"preStepUpserApproverOrgId", orgId);
			this.runtimeService.setVariable(actInstId, "ApproverOrgId", orgId);
		} else {
			sysOrg = (ISysOrg) this.sysOrgService
					.getById(preStepUpserApproverOrgId);
			executorList = getExecutor(sysOrg.getOrgSupId());
			this.runtimeService.setVariable(actInstId,
					"preStepUpserApproverOrgId", sysOrg.getOrgSupId());
		}

		if (BeanUtils.isNotEmpty(executorList)) {
			for (TaskExecutor user : executorList) {
				userSet.add(user.getExecuteId());
			}
		}
		return userSet;
	}

	/**
	 * 获取当前系统登陆用户，当前组织类别orgType的组织 负责人的user id集合
	 * */
	public Set<String> getUserByStep(Long orgType) {
		ISysOrg sysOrg = (ISysOrg) UserContextUtil.getCurrentOrg();
		Long currentUserId = UserContextUtil.getCurrentUserId();
		Set userSet = getUserStep(sysOrg, currentUserId, orgType);
		return userSet;
	}

	/**
	 * 获取当前流程变量，中参数fieldName，（组织或者人员值），当前组织类别orgType的组织 负责人的user id集合
	 * */
	public Set<String> getUserByFieldStep(String fieldName, Boolean isOrg,
			Long orgType) {
		GroovyScriptEngine groovyScriptEngine = (GroovyScriptEngine) AppUtil
				.getBean(GroovyScriptEngine.class);
		String actInstId = (String) groovyScriptEngine.getVariable("actInstId");

		String str = (String) this.runtimeService.getVariable(actInstId,
				fieldName);
		Long objId = Long.valueOf(Long.parseLong(str));
		ISysOrg org = null;
		Long orgId = Long.valueOf(0L);
		// objId为组织值
		if (isOrg.booleanValue()) {
			orgId = objId;
		} else {// objId为人员值
			IUserPosition position = this.userPositionService
					.getPrimaryUserPositionByUserId(objId);
			orgId = position.getOrgId();
		}
		org = (ISysOrg) this.sysOrgService.getById(orgId);

		Long userId = Long.valueOf(0L);
		if (!isOrg.booleanValue()) {
			userId = objId;
		}

		Set userSet = getUserStep(org, userId, orgType);
		return userSet;
	}

	/**
	 * 获取当前流程变量，中存储的子表的字段的值。
	 * */
	public Set<String> getSubFieldUser(String tableName, String field) {
		Set userSet = new LinkedHashSet();
		ProcessCmd cmd = getProcessCmd();
		Map map = cmd.getFormDataMap();
		String json = (String) map.get("formData");
		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONArray subTable = jsonObj.getJSONArray("sub");
		for (Iterator localIterator1 = subTable.iterator(); localIterator1
				.hasNext();) {
			Object tableJson = localIterator1.next();
			JSONObject tableObj = (JSONObject) tableJson;
			String tbName = (String) tableObj.get("tableName");
			if (tableName.equals(tbName)) {
				JSONArray jsonRows = tableObj.getJSONArray("fields");
				for (Iterator localIterator2 = jsonRows.iterator(); localIterator2
						.hasNext();) {
					Object obj = localIterator2.next();
					JSONObject row = (JSONObject) obj;
					String userId = row.getString(field);
					userSet.add(userId);
				}
			}
		}
		return userSet;
	}

	/**
	 * 根据主表记录id，获取子表记录中，用户字段值集合。
	 * 
	 * @param tableName
	 *            子表 表名
	 * @param fieldName
	 *            子表 字段名称（用户字段）
	 * @param businessKey
	 *            主表记录id
	 * */
	public Set<String> getSubTableUser(String tableName, String fieldName,
			String businessKey) {
		Set userSet = new LinkedHashSet();
		List<Map<String, Object>> values = getByFk(tableName, new Long(
				businessKey));
		for (Map r : values) {
			Object userId = r.get(fieldName);
			if (userId != null) {
				userSet.add(userId.toString());
			}
		}
		return userSet;
	}

	public Set<String> getChargerStep(Long orgType) {
		return getChargerStepByOrgId(UserContextUtil.getCurrentOrgId(), orgType);
	}

	/**
	 * 获取组织（orgId），该组织类别下，的主负责人。
	 * */
	public Set<String> getChargerStepByOrgId(Long orgId, Long orgType) {
		Set userSet = new LinkedHashSet();
		userSet = getChargerStepByOrgId(orgId, orgType, userSet);
		if (userSet.isEmpty()) {
			userSet.add(UserContextUtil.getCurrentUserId().toString());
		}
		if (userSet.size() > 1) {
			userSet.remove(UserContextUtil.getCurrentUserId().toString());
		}
		return userSet;
	}

	/**
	 * 获取组织（orgId），该组织类别下的，主负责人。
	 * */
	private Set<String> getChargerStepByOrgId(Long orgId, Long orgType,
			Set<String> userSet) {
		List<? extends IUserPosition> userPositionList = this.userPositionService
				.getChargeByOrgId(orgId);
		if (BeanUtils.isEmpty(userPositionList))
			return userSet;
		for (IUserPosition userPosition : userPositionList) {
			userSet.add(userPosition.getUserId().toString());
		}
		ISysOrg org = (ISysOrg) this.sysOrgService.getById(orgId);
		if (!orgType.equals(org.getOrgType())) {
			getChargerStepByOrgId(org.getOrgSupId(), orgType, userSet);
		}
		return userSet;
	}

	/**
	 * 获取系统登陆用户的组织（orgId），该组织类别（orgType）下的，主负责人。
	 * */
	public Set<String> getLeaderStep(Long orgType) {
		Set userSet = new LinkedHashSet();
		userSet = getLeaderStepByUserId(UserContextUtil.getCurrentOrgId(),
				UserContextUtil.getCurrentUserId(), orgType, userSet);
		if (userSet.isEmpty()) {
			userSet.add(UserContextUtil.getCurrentUserId().toString());
		}
		if (userSet.size() > 1) {
			userSet.remove(UserContextUtil.getCurrentUserId().toString());
		}
		return userSet;
	}

	/**
	 * 获取系统登陆用户的组织（orgId），该组织类别（orgType）下的，主负责人。
	 * */
	public Set<String> getLeaderStepByUserId(Long userId, Long orgType) {
		Set userSet = new LinkedHashSet();
		IUserPosition userPos = this.userPositionService
				.getPrimaryUserPositionByUserId(userId);
		userSet = getLeaderStepByUserId(userPos.getOrgId(), userId, orgType,
				userSet);
		if (userSet.isEmpty()) {
			userSet.add(userId.toString());
		}
		if (userSet.size() > 1) {
			userSet.remove(userId.toString());
		}
		return userSet;
	}

	/**
	 * 获取系统登陆用户的组织（orgId），该组织类别（orgType）下的，主负责人。
	 * */
	private Set<String> getLeaderStepByUserId(Long orgId, Long userId,
			Long orgType, Set<String> userSet) {
		ISysOrg currOrg = (ISysOrg) this.sysOrgService.getById(orgId);
		Long currOrgType = currOrg.getOrgType();
		Long currOrgId = orgId;
		Long currOrgSupId = currOrg.getOrgSupId();

		Set<String> leaderSet = getLeaderByUserId(userId);

		for (String id : leaderSet) {
			Long leaderId = Long.valueOf(id);

			List userPosList = this.userPositionService.getByUserId(leaderId);
			List orgIdList = getLeaderOrgId(userPosList);

			if (orgIdList.contains(currOrgId)) {
				userSet.add(id);
				getLeaderStepByUserId(currOrgId, leaderId, orgType, userSet);
			} else if ((!orgType.equals(currOrgType))
					&& (orgIdList.contains(currOrgSupId))) {
				userSet.add(id);
				getLeaderStepByUserId(currOrgSupId, leaderId, orgType, userSet);
			}
		}
		return userSet;
	}

	/**
	 * 获取用户组织id，orgid列表。
	 * */
	private List<Long> getLeaderOrgId(List<IPosition> userPosList) {
		List list = new ArrayList();
		for (IPosition userPosition : userPosList) {
			list.add(userPosition.getOrgId());
		}
		return list;
	}

	/**
	 * 获取用户组织id，orgid列表。
	 * */
	public Set<String> getLeaderAndChargerStep(Long orgType) {
		Set userSet = new LinkedHashSet();
		Set leaderSet = getLeaderStep(orgType);
		Set chargeSet = getChargerStep(orgType);
		userSet.addAll(leaderSet);
		userSet.addAll(chargeSet);
		if (userSet.size() > 1) {
			userSet.remove(UserContextUtil.getCurrentUserId().toString());
		}
		return userSet;
	}

	/**
	 * 获取系统登陆用户的组织（orgId），该组织类别（orgType）下的，主负责人。
	 * */
	public Set<String> getLeaderAndChargerStepByUserId(Long userId, Long orgType) {
		Set userSet = new LinkedHashSet();
		Set leaderSet = getLeaderStepByUserId(userId, orgType);
		IUserPosition userPos = this.userPositionService
				.getPrimaryUserPositionByUserId(userId);
		if (BeanUtils.isEmpty(userPos)) {
			return leaderSet;
		}
		Set chargeSet = getChargerStepByOrgId(userPos.getOrgId(), orgType);
		userSet.addAll(leaderSet);
		userSet.addAll(chargeSet);
		if (userSet.size() > 1) {
			userSet.remove(UserContextUtil.getCurrentUserId().toString());
		}
		return userSet;
	}

	// add by honghuajun
	/***
	 * 根据外键获取关联表的数据。
	 * 
	 * @param relTableName
	 *            关联表 表名
	 * @param fkFieldName
	 *            外键字段
	 * @param fkFieldValue外键值
	 * @param orderFieldName
	 *            排序字段
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getRelDataByFk(String relTableName,
			String fkFieldName, Object fkFieldValue, String orderFieldName) {
		List list = this.formHandlerService.getRelDataByFk(relTableName,
				fkFieldName, fkFieldValue, orderFieldName);
		return list;
	}

	/**
	 * 根据主表记录id，获取关联表记录中，用户字段值集合。 * @param relTableName 关联表 表名
	 * 
	 * @param fkFieldName
	 *            外键字段
	 * @param userField
	 *            用户字段
	 * @param businessKey
	 *            主表记录id
	 * */
	public Set<String> getRelTableUser(String relTableName, String fkFieldName,
			String businessKey, String userField) {
		Set userSet = new LinkedHashSet();
		List<Map<String, Object>> values = getRelDataByFk(relTableName,
				fkFieldName, businessKey, null);
		for (Map r : values) {
			Object userId = r.get(userField);
			if (userId != null) {
				userSet.add(userId.toString());
			}
		}
		return userSet;
	}

	/**
	 * 根据主表记录id，获取某一特定表中，用户字段值集合。
	 * @author liubo
	 * @param tableName 需要查询的表名
	 * @param fieldName 表中的用户字段
	 * @param pk 主表记录id
	 * */
	public Set<String> getTableUser(String tableName, String fieldName, String pk) {
		Set userSet = new LinkedHashSet();
		//根据主键查找记录
		Map<String, Object> values = this.formHandlerService.getByKey(tableName,pk);
		Object userIdObject = values.get(fieldName);
		String userIdString = userIdObject.toString();
		if(StringUtil.isNotEmpty(userIdString)){
			String userIds = userIdString.substring(0, userIdString.length());
			String[] userIdList = userIds.split(",");
			for(int i=0;i<userIdList.length;i++){
				userSet.add(userIdList[i]);
			}
		}
		return userSet;
	}
	
	/**
	 * 获取当前流程变量，中存储的关联表的用户字段的值。
	 * */
	public Set<String> getRelFieldUserByVrocessCmdVars(String relTableName,
			String userField) {
		Set userSet = new LinkedHashSet();
		ProcessCmd cmd = getProcessCmd();
		Map map = cmd.getFormDataMap();
		String json = (String) map.get("formData");
		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONArray relTable = jsonObj.getJSONArray("rel");
		for (Iterator localIterator1 = relTable.iterator(); localIterator1
				.hasNext();) {
			Object tableJson = localIterator1.next();
			JSONObject tableObj = (JSONObject) tableJson;
			String tbName = (String) tableObj.get("tableName");
			if (relTableName.equals(tbName)) {
				JSONArray jsonRows = tableObj.getJSONArray("fields");
				for (Iterator localIterator2 = jsonRows.iterator(); localIterator2
						.hasNext();) {
					Object obj = localIterator2.next();
					JSONObject row = (JSONObject) obj;
					String userId = row.getString(userField);
					userSet.add(userId);
				}
			}
		}
		return userSet;
	}

	/**
	 * 获取关联表记录中字段F_zxrID值和任务的签收人task.getOwner()值一致的关联表记录ID的集合, 用*隔开,结构如
	 * *10000000520118*10000000520343*。
	 */
	public String getRelRecordIdsByTaskOwner(String relTableName,
			String fkFieldName, Object businessKey, String owner,
			String ownerField, String relTablePKField) {
		String relRecordIds = "";
		List<Map<String, Object>> map = this.getRelDataByFk(relTableName,
				fkFieldName, businessKey, null);
		for (Map p : map) {
			String zxr = p.get(ownerField).toString();
			if (zxr.contains(owner)) {
				String id = p.get(relTablePKField).toString();
				relRecordIds += "*" + id;
			}
		}
		if (relRecordIds.length() > 0) {
			relRecordIds = relRecordIds + "*";
		}
		/*
		 * task.setVariable("relRecordIds",
		 * scriptImpl.getRelRecordIdsByTaskOwner("w_test_rel" , "F_test_id" ,
		 * businessKey, task.getOwner(),"F_zxrID","ID"));
		 * task.setVariable("owner_zlc", task.getOwner());
		 */
		return relRecordIds;
	}

	/**
	 * 获取任务执行人ID.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人]  WeiLei <br/> 
	 * 		   [创建时间] 2017年2月22日 下午12:48:25 <br/> 
	 * 		   [修改人]  WeiLei <br/>
	 * 		   [修改时间] 2017年2月22日 下午12:48:25
	 * @param executors:'user^用户ID^用户名,org^组织id^组织名称,role^角色ID^角色名'
	 * @return
	 * @see
	 */
	public String getExecuteId(String executors) {
		
		String userId = "";
		List<TaskExecutor> executorList = BpmUtil.getTaskExecutors(executors);
		for(TaskExecutor taskExecutor : executorList){
			userId += taskExecutor.getExecuteId() + ",";
		}
		userId = "".equals(userId)?"":userId.substring(0, userId.length()-1);
		return userId;
	}

}
