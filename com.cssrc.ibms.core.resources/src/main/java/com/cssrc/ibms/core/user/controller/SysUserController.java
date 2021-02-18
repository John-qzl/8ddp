package com.cssrc.ibms.core.user.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.core.user.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.report.inf.ISignModelService;
import com.cssrc.ibms.api.system.intf.IAccountStrategyService;
import com.cssrc.ibms.api.system.intf.IDemensionService;
import com.cssrc.ibms.api.system.intf.ISysParamService;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.system.model.IAccountStrategy;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.system.model.ISysParameter;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.login.model.OnlineUser;
import com.cssrc.ibms.core.resources.product.service.ModuleManageService;
import com.cssrc.ibms.core.user.event.UserEvent;
import com.cssrc.ibms.core.user.listener.UserSessionListener;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysRole;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.user.model.UserRole;
import com.cssrc.ibms.core.user.model.UserUnder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.encrypt.PasswordUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.util.HtmlToPdf;

import net.sf.json.JSONArray;

/**
 * 用户管理
 * <p>
 * Title:SysUserController
 * </p>
 *
 * @author Yangbo
 * @date 2016-8-23下午03:24:46
 */
@Controller
@RequestMapping({ "/oa/system/sysUser/" })
@Action(ownermodel = SysAuditModelType.USER_MANAGEMENT)
@DataNote(beanName = SysUserController.class)
public class SysUserController extends BaseController {

	@Resource
	private SysRoleService sysRoleService;

	@Resource
	private ISysParameterService sysParameterService;

	@Resource
	private SysOrgService sysOrgService;

	@Resource
	private SysUserService sysUserService;

	@Resource
	private IDemensionService demensionService;

	@Resource
	private SysUserParamService sysUserParamService;

	@Resource
	private ISysParamService sysParamService;
	@Resource
	private UserRoleService userRoleService;

	@Resource
	private UserPositionService userPositionService;

	@Resource
	private OrgAuthService orgAuthService;

	@Resource
	private OrgServiceImpl orgServiceImpl;

	@Resource
	private UserUnderService userUnderService;

	@Resource
	private PwdStrategyService pwdStrategyService;
	@Resource
	private ISignModelService signModelService;

	@Resource
	private IAccountStrategyService accountStrategyService;
	@Resource
	private DataSynchronizationService dataSynchronizationService;
	@Resource
	private ModuleManageService moduleManageService;
	@Resource
	private PositionService positionService;

	@Resource
	Properties configproperties;
	private final String defaultUserImage = "styles/images/default_image_male.jpg";
	// 用户默认的电子签章 by zmz
	private final String defaultUserSignModel = "styles/images/default_sign_model.png";

	/**
	 * 用户管理列表
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "list" })
	@Action(description = "查看用户表分页列表", execOrder = ActionExecOrder.AFTER, detail = "查看用户表分页列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 是否显示密级列的参数
		Boolean isShowSecurity = false;
		// 判断用户
		boolean isRight = UserContextUtil.getCurrentUserId().equals(ISysUser.RIGHT_USER) ? true : false;
		boolean isSystem = UserContextUtil.getCurrentUserId().equals(ISysUser.SYSTEM_USER) ? true : false;
		// 获取密级参数数据
		List<? extends ISysParameter> spDatas = sysParameterService.getByParamName(ISysFile.IS_DISPLAY_SECURITY);
		if (spDatas.size() > 0 && "1".equals(spDatas.get(0).getParamvalue())) {
			isShowSecurity = true;
		}
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		boolean isSupportWeixin = PropertyUtil.getBooleanByAlias("supportWeiXin", false);
		List<SysUser> list = this.sysUserService.getUsersByQuery(new QueryFilter(request, "sysUserItem"));

		// 用户信息添加所属角色，组织，岗位信息 - by wjj
		for (SysUser sysUser : list) {
			Long _userId = Long.valueOf(sysUser.getUserId());
			List<UserRole> roleList = this.userRoleService.getByUserId(_userId);
			StringBuilder roleNameStr = new StringBuilder();
			for (UserRole role : roleList) {
				roleNameStr.append(role.getRoleName());
			}
			sysUser.setRoleNames(roleNameStr.toString());

			List<UserPosition> posList = this.userPositionService.getByUserId(_userId);
			StringBuilder posNameStr = new StringBuilder();
			for (UserPosition pos : posList) {
				posNameStr.append(pos.getPosName());
			}
			sysUser.setPosNames(posNameStr.toString());

			List<UserPosition> orgList = this.userPositionService.getOrgListByUserId(_userId);
			StringBuilder orgNameStr = new StringBuilder();
			for (UserPosition userPosition : orgList) {
				orgNameStr.append(userPosition.getOrgName());
			}
			sysUser.setOrgName(orgNameStr.toString());
		}

		// 设置操作结果为操作成功
		LogThreadLocalHolder.setResult((short) 1);
		ModelAndView mv = getAutoView().addObject("sysUserList", list).addObject("userId", userId)
				.addObject("securityUserMap", ISysUser.SECURITY_USER_MAP)
				.addObject("isSupportWeixin", Boolean.valueOf(isSupportWeixin))
				.addObject("isShowSecurity", isShowSecurity).addObject("isRight", isRight)
				.addObject("isSystem", isSystem);
		return mv;
	}

	@RequestMapping({ "del" })
	@Action(description = "删除用户表", execOrder = ActionExecOrder.BEFORE, detail = "删除用户<#list userId?split(\",\") as item><#assign entity=sysUserService.getById(Long.valueOf(item))/>${entity.fullname}【${entity.username}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage message = null;
		String preUrl = RequestUtil.getPrePage(request);
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "userId");
			delByIds(lAryId);
			message = new ResultMessage(1, "删除用户成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除用户失败:" + e.getMessage());
			e.printStackTrace();
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 相当于级联删除，删除有关用户的所有表信息
	 *
	 * @param lAryId
	 */
	private void delByIds(Long[] lAryId) {
		if (BeanUtils.isEmpty(lAryId))
			return;
		for (Long id : lAryId) {
			SysUser user = (SysUser) this.sysUserService.getById(id);
			EventUtilService.publishUserEvent(id, UserEvent.ACTION_DEL, user);
			this.userPositionService.delByUserId(id);
			this.sysUserParamService.delByUserId(id);
			this.userRoleService.delByUserId(id);
			this.orgAuthService.delByUserId(id);
			// 其它表引用外键，最后删除
			this.sysUserService.delById(id);
		}
		// 删除后更新redis中的人员信息
		this.sysUserService.setAllSysUserToRedis();
	}

	/**
	 * 编辑用户信息
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "edit" })
	@Action(description = "打开编辑用户信息界面", execOrder = ActionExecOrder.AFTER, detail = "打开编辑用户信息界面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		ModelAndView mv = getAutoView();
		mv.addObject("action", "global");
		List<?> demensionList = this.demensionService.getAll();
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		String tabid = RequestUtil.getString(request, "tabid");

		// 判断用户
		boolean isRight = UserContextUtil.getCurrentUserId().equals(ISysUser.RIGHT_USER) ? true : false;
		boolean isSystem = UserContextUtil.getCurrentUserId().equals(ISysUser.SYSTEM_USER) ? true : false;
		// 是否显示密级列的参数
		Boolean isShowSecurity = false;
		// 获取密级参数数据
		List<? extends ISysParameter> spDatas = sysParameterService.getByParamName(ISysFile.IS_DISPLAY_SECURITY);
		if (spDatas.size() > 0 && "1".equals(spDatas.get(0).getParamvalue())) {
			isShowSecurity = true;
		}
		// 旧签章模型(弃用 by zmz )
		// List<?extends ISignModel> signModelList =
		// this.signModelService.getByUserId(userId);
		// 返回当前签章
		String signModelPath = this.signModelService.getSignModelPath(userId, defaultUserSignModel);

		Map<?, ?> roles = getOrgRoles(userId);
		mv.addObject("sysOrgRoles", roles);
		mv.addObject("userId", userId);
		return getEditMv(request, mv).addObject("demensionList", demensionList)
				.addObject("signModelPath", signModelPath).addObject("securityUserMap", ISysUser.SECURITY_USER_MAP)
				.addObject("tabid", tabid).addObject("isShowSecurity", isShowSecurity).addObject("isRight", isRight)
				.addObject("isSystem", isSystem);
	}

	/**
	 * 设置-个人资料-修改信息
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "editCommon" })
	@Action(description = "打开设置个人资料信息界面", execOrder = ActionExecOrder.AFTER, detail = "打开设置个人资料信息界面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView editCommon(HttpServletRequest request) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		SysUser sysUser = (SysUser) this.sysUserService.getById(userId);
		String pictureLoad = "styles/images/default_image_male.jpg";
		if ((sysUser != null) && (StringUtil.isNotEmpty(sysUser.getPhoto()))) {
			pictureLoad = sysUser.getPhoto();
		}

		return getAutoView().addObject("sysUser", sysUser).addObject("returnUrl", returnUrl).addObject("pictureLoad",
				pictureLoad);
	}

	/**
	 * 分级组织用户编辑
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "editGrade" })
	@Action(description = "打开分级组织用户编辑界面", execOrder = ActionExecOrder.AFTER, detail = "打开分级组织用户编辑界面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView editGrade(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView();

		Long userId = UserContextUtil.getCurrentUserId().longValue();
		List orgAuthList = this.orgAuthService.getByUserId(userId);
		// 判断用户
		boolean isRight = userId.equals(ISysUser.RIGHT_USER) ? true : false;
		boolean isSystem = userId.equals(ISysUser.SYSTEM_USER) ? true : false;

		mv.setViewName("/oa/system/sysUserEdit.jsp");
		mv.addObject("action", "grade");
		mv.addObject("orgAuthList", orgAuthList);
		mv.addObject("isRight", isRight);
		mv.addObject("isSystem", isSystem);
		return getEditMv(request, mv);
	}

	/**
	 * 返回编辑页面
	 *
	 * @param request
	 * @param mv
	 * @return
	 */
	public ModelAndView getEditMv(HttpServletRequest request, ModelAndView mv) {
		String returnUrl = RequestUtil.getPrePage(request);
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		int bySelf = RequestUtil.getInt(request, "bySelf");
		SysUser sysUser = null;
		if (userId.longValue() != 0L) {
			sysUser = (SysUser) this.sysUserService.getById(userId);
			List roleList = this.userRoleService.getByUserId(userId);
			List<UserPosition> userPosList = this.userPositionService.getByUserId(userId);
			List userUnders = this.sysUserService.getMyLeaders(userId);

			List orgIdList = new ArrayList();
			String orgIds = "";
			for (UserPosition up : userPosList) {
				orgIdList.add(up.getOrgId().toString());
				if (up.getPosId() == null) {
					up.setPosId(Long.valueOf(-1L));
				}
			}

			orgIds = StringUtil.getArrayAsString(orgIdList);

			mv.addObject("roleList", roleList).addObject("userPosList", userPosList).addObject("orgIds", orgIds)
					.addObject("userUnders", userUnders);
		} else {
			sysUser = new SysUser();
			sysUser.setPassword(this.pwdStrategyService.getUsingInitPwd());
			/* sysUser.setPassword(this.pwdStrategyService.getUsingInitPwd()); */
			/*
			 * ======= //added by liqing 2015-10-26 电子签章 if(user.getSign_pic() != null) {
			 * SysFile file = sysFileService.getById(Long.valueOf(user.getSign_pic()));
			 * if(file!=null){ userE.setSign_picpath(file.getFilename()+"."+file.getExt());
			 * } } if(user.getOriginalsign_pic() != null) { SysFile file =
			 * sysFileService.getById(Long.valueOf(user.getOriginalsign_pic()));
			 * if(file!=null){ userE.setOriginalsign_picpath(file.getFilename()+"."
			 * +file.getExt()); } } if(user.getPhoto() != null) { SysFile file =
			 * sysFileService.getById(Long.valueOf(user.getPhoto())); if(file!=null){
			 * userE.setPhotopath(file.getFilename()+"."+file.getExt()); } }
			 * if(user.getOriginalphoto() != null) { SysFile file =
			 * sysFileService.getById(Long.valueOf(user.getOriginalphoto()));
			 * if(file!=null){
			 * userE.setOriginalphotopath(file.getFilename()+"."+file.getExt()); }
			 *
			 * } uelist.add(userE); >>>>>>> .r1176
			 */
		}
		// 默认头像图片
		String pictureLoad = "styles/images/default_image_male.jpg";
		if ((sysUser != null) && (StringUtil.isNotEmpty(sysUser.getPhoto()))) {
			pictureLoad = sysUser.getPhoto();
		}

		return mv.addObject("sysUser", sysUser).addObject("userId", userId).addObject("returnUrl", returnUrl)
				.addObject("pictureLoad", pictureLoad).addObject("bySelf", Integer.valueOf(bySelf));
	}

	/**
	 * 用户设置修改密码页面
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "modifyPwdView" })
	@Action(description = "进入修改密码页面", execOrder = ActionExecOrder.AFTER, detail = "进入修改密码页面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView modifyPwdView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long userId = RequestUtil.getLong(request, "userId");
		SysUser sysUser = (SysUser) this.sysUserService.getById(Long.valueOf(userId));

		// 若是直接访问更改密码路径则报错
		sysUser.getFullname();

		return getAutoView().addObject("sysUser", sysUser).addObject("userId", Long.valueOf(userId));
	}

	/**
	 * 用户密码修改方法
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "modifyPwd" })
	@Action(description = "修改密码", execOrder = ActionExecOrder.AFTER, detail = "<#assign entity=sysUserService.getById(Long.valueOf(userId))/> 修改 ${entity.fullname}【${entity.username}】 用户密码", exectype = SysAuditExecType.UPDATE_TYPE)
	public void modifyPwd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String primitivePassword = RequestUtil.getString(request, "primitivePassword");
		// 解码方式
		String enPassword = PasswordUtil.generatePassword(primitivePassword);
		String newPassword = RequestUtil.getString(request, "newPassword");
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		SysUser sysUser = (SysUser) this.sysUserService.getById(userId);
		String password = sysUser.getPassword();
		// 最短使用期限启用，
		IAccountStrategy accountStrategyMin = accountStrategyService.getById("4");
		// 密码修改到当前时间的间隔
		Date now = new Date();
		// 是否具有更改密码记录
		boolean isChange = false;
		long between = 0;
		if (sysUser.getPasswordSetTime() != null) {
			isChange = true;
			between = (now.getTime() - sysUser.getPasswordSetTime().getTime()) / 1000;// 除以1000是为了转换成秒
		}

		// 设置操作结果，默认为操作失败
		Short result = 0;
		String resultMsg = null;
		if ((StringUtil.isEmpty(newPassword)) || (StringUtil.isEmpty(primitivePassword)))
			resultMsg = "输入的密码不能为空";
		else if (isChange && accountStrategyMin.getIs_enable().equals("1")
				&& Integer.valueOf(accountStrategyMin.getStrategy_value().toString()) * 24 * 3600 > between)
			resultMsg = "没有达到密码使用的最短期限，不能修改密码，如需修改请联系管理员！";
		else if (!enPassword.equals(password))
			resultMsg = "输入的原始密码不正确";
		else if (primitivePassword.equals(newPassword))
			resultMsg = "修改的密码和原始密码相同";
		else {
			resultMsg = checkPassword(newPassword, sysUser.getUsername());
			if (StringUtil.isEmpty(resultMsg)) {
				try {
					this.sysUserService.updPwd(userId, newPassword);
					writeResultMessage(response.getWriter(), "修改密码成功", 1);
					result = 1;
				} catch (Exception ex) {
					String str = MessageUtil.getMessage();
					if (StringUtil.isNotEmpty(str)) {
						ResultMessage resultMessage = new ResultMessage(0, "修改密码失败:" + str);
						response.getWriter().print(resultMessage);
					} else {
						String message = ExceptionUtil.getExceptionMessage(ex);
						ResultMessage resultMessage = new ResultMessage(0, message);
						response.getWriter().print(resultMessage);
					}
				}
				LogThreadLocalHolder.setResult(result);
				return;
			}
		}
		writeResultMessage(response.getWriter(), resultMsg, 0);
		try {
			LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	private String checkPassword(String newPassword, String userName) {
		String resultMsg = "";
		String res = accountStrategyService.meetMinPasswordLen(newPassword);
		if (!res.equals("true")) {
			resultMsg = "输入的密码长度不符合要求: " + res;
			return resultMsg;
		}
		res = accountStrategyService.meetPasswordComplexity(userName, newPassword);
		if (!res.equals("true")) {
			resultMsg = "输入的密码不符合密码复杂性要求: " + res;
		}
		return resultMsg;
	}

	/**
	 * 系统管理员对用户密码重置
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "resetPwdView" })
	@Action(description = "进入对用户密码重置的页面", execOrder = ActionExecOrder.AFTER, detail = "进入对用户密码重置的页面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView resetPwdView(HttpServletRequest request) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		if (userId.longValue() == 0L) {
			return new ModelAndView("redirect:notExist.do");
		}
		SysUser sysUser = (SysUser) this.sysUserService.getById(userId);
		if (sysUser == null) {

		}
		return getAutoView().addObject("sysUser", sysUser).addObject("userId", userId).addObject("returnUrl",
				returnUrl);
	}

	/**
	 * 密码重置方法
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "resetPwd" })
	@Action(description = "重置密码", execOrder = ActionExecOrder.AFTER, detail = "<#assign entity=sysUserService.getById(Long.valueOf(userId))/>重置  ${entity.fullname}【${entity.username}】 用户的密码", exectype = SysAuditExecType.UPDATE_TYPE)
	public void resetPwd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String password = RequestUtil.getString(request, "password");
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		SysUser sysUser = (SysUser) this.sysUserService.getById(userId);
		String oldPassword = sysUser.getPassword();
		Short result = 0;
		String resultMsg = null;
		if (StringUtil.isEmpty(password))
			resultMsg = "输入的密码不能为空";
		else if (oldPassword.equals(password))
			resultMsg = "修改的密码和原始密码相同";
		else if (!accountStrategyService.meetMinPasswordLen(password).equals("true"))
			resultMsg = "输入的密码长度不符合要求:" + accountStrategyService.meetMinPasswordLen(password);
		else if (!accountStrategyService.meetPasswordComplexity(sysUser.getUsername(), password).equals("true"))
			resultMsg = "输入的密码不符合密码复杂性要求:"
					+ accountStrategyService.meetPasswordComplexity(sysUser.getUsername(), password);
		else {
			try {
				/*
				 * JSONObject json = this.pwdStrategyService.checkUser(sysUser, password); short
				 * status = Short.parseShort(json.getString("status")); if (status != 0) {
				 * String msg = json.getString("msg"); throw new Exception(msg); }
				 */
				this.sysUserService.updPwd(userId, password);
				writeResultMessage(response.getWriter(), "重置密码成功!", 1);
				result = 1;
			} catch (Exception ex) {
				String str = MessageUtil.getMessage();
				if (StringUtil.isNotEmpty(str)) {
					ResultMessage resultMessage = new ResultMessage(0, "重置密码失败:" + str);
					response.getWriter().print(resultMessage);
				} else {
					String message = ExceptionUtil.getExceptionMessage(ex);
					ResultMessage resultMessage = new ResultMessage(0, message);
					response.getWriter().print(resultMessage);
				}
			}
			LogThreadLocalHolder.setResult(result);
			return;
		}
		writeResultMessage(response.getWriter(), resultMsg, 0);
		try {
			LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * 用户设置状态页面
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "editStatusView" })
	@Action(description = "进入设置状态页面", execOrder = ActionExecOrder.AFTER, detail = "进入设置状态页面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView editStatusView(HttpServletRequest request) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		SysUser sysUser = (SysUser) this.sysUserService.getById(userId);
		return getAutoView().addObject("sysUser", sysUser).addObject("userId", userId).addObject("returnUrl",
				returnUrl);
	}

	/**
	 * 设置用户密级页面
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "editSecurityView" })
	@Action(description = "进入设置密级页面", execOrder = ActionExecOrder.AFTER, detail = "进入设置密级页面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView editSecurityView(HttpServletRequest request) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		SysUser sysUser = (SysUser) this.sysUserService.getById(userId);
		return getAutoView().addObject("sysUser", sysUser).addObject("userId", userId).addObject("returnUrl", returnUrl)
				.addObject("securityUserMap", ISysUser.SECURITY_USER_MAP);
	}

	/**
	 * 用户设置状态保存
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "editStatus" })
	@Action(description = "设置用户状态", execOrder = ActionExecOrder.AFTER, detail = "更新用户 <#assign entity=sysUserService.getById(Long.valueOf(userId))/> ${entity.fullname}【${entity.username}】", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { SysUser.class }, pkName = "userId")
	public void editStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		Long currentUserId = UserContextUtil.getCurrentUserId();
		// int delFlag = RequestUtil.getInt(request, "delFlag");
		int status = RequestUtil.getInt(request, "status");

		// 设置操作结果，默认为操作失败
		Short result = 0;
		try {
			this.sysUserService.updStatus(userId, currentUserId, Short.valueOf((short) (short) status));
			result = 1;
			writeResultMessage(response.getWriter(), "修改用户状态成功!", 1);
		} catch (Exception ex) {
			ex.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(0, "修改用户状态失败:" + str);
				response.getWriter().print(resultMessage);
			} else {

				String message = ExceptionUtil.getExceptionMessage(ex);
				ResultMessage resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * 用户管理锁定、解锁按钮
	 * 
	 * @author liubo
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "lockUser" })
	@Action(description = "锁定、解锁用户", execOrder = ActionExecOrder.AFTER, detail = "更新用户 <#assign entity=sysUserService.getById(Long.valueOf(userId))/> ${entity.fullname}【${entity.username}】", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { SysUser.class }, pkName = "userId")
	public void lockUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long userId = RequestUtil.getLong(request, "userId");
		IAccountStrategy lockStrategy = accountStrategyService.getById("5");
		Date now = new Date();
		// 设置操作结果，默认为操作失败
		Short result = 0;
		try {
			SysUser sysUser = (SysUser) this.sysUserService.getById(userId);

			if (sysUser.getLockState().equals(Short.valueOf((short) 1))) {
				// 解锁
				this.sysUserService.updLock(userId, "0", (short) 0, null, null);
			} else {
				// 锁定
				this.sysUserService.updLock(userId, lockStrategy.getStrategy_value(), (short) 1, now, now);
			}
			result = 1;
			response.sendRedirect(RequestUtil.getPrePage(request));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * 设置用户密级保存
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "editSecurity" })
	@Action(description = "设置用户密级", execOrder = ActionExecOrder.AFTER, detail = "更新用户 <#assign entity=sysUserService.getById(Long.valueOf(userId))/> ${entity.fullname}【${entity.username}】", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { SysUser.class }, pkName = "userId")
	public void editSecurity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		Long currentUserId = UserContextUtil.getCurrentUserId();
		String security = RequestUtil.getString(request, "security", "");
		// 设置操作结果，默认为操作失败
		Short result = 0;
		try {
			this.sysUserService.updSecurity(userId, currentUserId, security);
			result = 1;
			writeResultMessage(response.getWriter(), "修改用户密级成功!", 1);
		} catch (Exception ex) {
			ex.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(0, "修改用户密级失败:" + str);
				response.getWriter().print(resultMessage);
			} else {

				String message = ExceptionUtil.getExceptionMessage(ex);
				ResultMessage resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * 用户基本信息页面
	 * 
	 * @throws Exception
	 */
	@RequestMapping({ "getInfo" })
	@Action(description = "用户基本信息页面", execOrder = ActionExecOrder.AFTER, detail = "用户基本信息页面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView getInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		// ISysUser user =
		// sysUserService.findByUserId(RequestUtil.getLong(request,"userId"));
		boolean isRight = UserContextUtil.getCurrentUserId().equals(ISysUser.RIGHT_USER) ? true : false;

		return getView(request, response, mv, 0).addObject("isRight", isRight);
	}

	/**
	 * 用户明细页面
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "get" })
	@Action(description = "用户明细页面", execOrder = ActionExecOrder.AFTER, detail = "用户明细页面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		ISysUser currentUser = UserContextUtil.getCurrentUser();
		boolean isRight = UserContextUtil.getCurrentUserId().equals(ISysUser.RIGHT_USER) ? true : false;

		return getView(request, response, mv, 0).addObject("isRight", isRight);
	}

	/**
	 * 用户明细页面
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "getByUserId" })
	@Action(description = "用户明细页面", execOrder = ActionExecOrder.AFTER, detail = "用户明细页面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView getByUserId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/oa/system/sysUserGet.jsp");
		mv = getView(request, response, mv, 1);
		return mv;
	}

	public ModelAndView getView(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			int isOtherLink) throws Exception {
		long userId = RequestUtil.getLong(request, "userId");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);

		String openType = RequestUtil.getString(request, "openType", "");

		SysUser sysUser = (SysUser) this.sysUserService.getById(Long.valueOf(userId));
		String pictureLoad = "styles/images/default_image_male.jpg";
		if ((sysUser != null) && (StringUtil.isNotEmpty(sysUser.getPhoto()))) {
			pictureLoad = sysUser.getPhoto();
		}

		Map roles = getOrgRoles(Long.valueOf(userId));

		List roleList = this.userRoleService.getByUserId(Long.valueOf(userId));

		List posList = this.userPositionService.getByUserId(Long.valueOf(userId));

		List userPosList = this.userPositionService.getOrgListByUserId(Long.valueOf(userId));

		List userParamList = this.sysUserParamService.getByUserId(userId);
		String returnUrl = RequestUtil.getPrePage(request);

		String signModelPath = this.signModelService.getSignModelPath(userId, defaultUserSignModel);

		return mv.addObject("sysUser", sysUser).addObject("roleList", roleList).addObject("posList", posList)
				.addObject("orgList", userPosList).addObject("pictureLoad", pictureLoad)
				.addObject("userParamList", userParamList).addObject("canReturn", Long.valueOf(canReturn))
				.addObject("returnUrl", returnUrl).addObject("isOtherLink", Integer.valueOf(isOtherLink))
				.addObject("openType", openType).addObject("sysOrgRoles", roles)
				.addObject("signModelPath", signModelPath);
	}

	/**
	 * 用户对话框
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "dialog" })
	@Action(description = "查看用户对话框", execOrder = ActionExecOrder.AFTER, detail = "查看用户对话框", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView dialog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();

		List demensionList = this.demensionService.getAll();
		mv.addObject("demensionList", demensionList);

		String isSingle = RequestUtil.getString(request, "isSingle", "false");
		mv.addObject("isSingle", isSingle);
		handelUserSoruce(mv);
		return mv;
	}

	/**
	 * 用户展示内容
	 *
	 * @param mv
	 */
	private void handelUserSoruce(ModelAndView mv) {
		boolean isShowPos = PropertyUtil.getBooleanByAlias("userDialog.showPos", true);
		boolean isShowRole = PropertyUtil.getBooleanByAlias("userDialog.showRole", true);
		boolean isShowOnlineUser = PropertyUtil.getBooleanByAlias("userDialog.showOnlineUser", true);

		mv.addObject("isShowPos", Boolean.valueOf(isShowPos));
		mv.addObject("isShowRole", Boolean.valueOf(isShowRole));
		mv.addObject("isShowOnlineUser", Boolean.valueOf(isShowOnlineUser));
	}

	/**
	 * 分级用户对话框
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "gradeDialog" })
	@Action(description = "查看分级用户对话框", execOrder = ActionExecOrder.AFTER, detail = "查看分级用户对话框", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView GradeDialog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();

		long userId = UserContextUtil.getCurrentUserId().longValue();
		List orgAuthList = this.orgAuthService.getByUserId(userId);
		mv.addObject("orgAuthList", orgAuthList);

		String isSingle = RequestUtil.getString(request, "isSingle", "false");
		mv.addObject("isSingle", isSingle);
		handelUserSoruce(mv);
		return mv;
	}

	/**
	 * 流程中用户选择器
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "flowDialog" })
	@Action(description = "查看流程中用户选择器", execOrder = ActionExecOrder.AFTER, detail = "查看流程中用户选择器", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView flowDialog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		List demensionList = this.demensionService.getAll();
		mv.addObject("demensionList", demensionList);

		mv.addObject("isSingle", "false");
		handelUserSoruce(mv);
		return mv;
	}

	/**
	 * 用户选择框
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "selector" })
	public ModelAndView selector(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<SysUser> list = null;
		ModelAndView result = getAutoView();
		String searchBy = RequestUtil.getString(request, "searchBy");
		String type = RequestUtil.getString(request, "type");
		String typeVal = RequestUtil.getString(request, "typeVal");
		String relvalue = RequestUtil.getString(request, "relvalue");
		String dataId = RequestUtil.getString(request, "dataId");
		String dataType = RequestUtil.getString(request, "dataType");
		int includSub = RequestUtil.getInt(request, "includSub", 0);
		String isSingle = RequestUtil.getString(request, "isSingle", "false");
		QueryFilter queryFilter = new QueryFilter(request, "sysUserItem");

		Long orgId = (long) 0;

		// 定制，只显示当前登陆人所属部门的人员
		if (relvalue.equals("current")) {
			relvalue = UserContextUtil.getCurrentOrgId().toString();
		}

		// 判断是否存在级联关系
		if (!relvalue.isEmpty()) {
			orgId = Long.parseLong(relvalue);
			queryFilter.addFilterForIB("orgId", orgId);
		}
		if (dataType != null && dataType.equals("acceptanceFile")) {
			List<String> userList = moduleManageService.getByModuleIdDis(dataId);
			list = new ArrayList<>();
			if (userList.size() != 0) {
				String demId = RequestUtil.getString(request, "path");
				if (("2".equals(searchBy))) {
					String orgIdFilter = queryFilter.getFilters().get("orgId").toString();
					if (!orgIdFilter.equals("root")) {
						List<String> orgUserIds = new ArrayList<>();
						for (String id : userList) {
							String userOrgId = sysOrgService.getOrgIdsByUserId(Long.valueOf(id));
							if (orgIdFilter.equals(userOrgId)) {
								orgUserIds.add(id);
							}
						}
						if (orgUserIds.size() != 0) {
							queryFilter.addFilterForIB("userIds", orgUserIds);
							list = this.sysUserService.getByIds(queryFilter);
						}
					} else {
						queryFilter.addFilterForIB("userIds", userList);
						list = this.sysUserService.getByIds(queryFilter);
					}
				} else if (("3".equals(searchBy))) {
					String posIdFilter = queryFilter.getFilters().get("posId").toString();
					List<String> posUserIds = new ArrayList<>();
					for (String id : userList) {
						List<Long> posIds = positionService.getPositonIdsByUserId(Long.valueOf(id));
						if (posIds.contains(Long.valueOf(posIdFilter))) {
							posUserIds.add(id);
						}
					}
					if (posUserIds.size() != 0) {
						queryFilter.addFilterForIB("userIds", posUserIds);
						list = this.sysUserService.getByIds(queryFilter);
					}

				} else if (("1".equals(searchBy))) {
					String roleIdFilter = queryFilter.getFilters().get("roleId").toString();
					List<String> roleUserIds = new ArrayList<>();
					for (String id : userList) {
						String role = sysRoleService.getRoleIdsByUserId(Long.valueOf(id));
						if (role.indexOf(roleIdFilter) >= 0) {
							roleUserIds.add(id);
						}
					}
					if (roleUserIds.size() != 0) {
						queryFilter.addFilterForIB("userIds", roleUserIds);
						list = this.sysUserService.getByIds(queryFilter);
					}
				} else {
					/* queryFilter.addFilterForIB("userIds", ids); */
					queryFilter.addFilterForIB("userIds", userList);
					list = this.sysUserService.getByIds(queryFilter);

				}
			}
		} else if (dataType != null && dataType.equals("modelManageFilter")) { // 定制过滤型号管理员已选择人员
			List<String> userList = moduleManageService.getByModuleIdDis(dataId);
			list = new ArrayList<>();
			if (userList.size() != 0) {
				String demId = RequestUtil.getString(request, "path");
				if (("2".equals(searchBy))) {
					String orgIdFilter = queryFilter.getFilters().get("orgId").toString();
					if (!orgIdFilter.equals("root")) {
						queryFilter.addFilterForIB("userIds", userList);
						list = this.sysUserService.getByIdsFilter(queryFilter);
						List<String> orgUserIds = new ArrayList<>();
						for (String id : userList) {
							String userOrgId = sysOrgService.getOrgIdsByUserId(Long.valueOf(id));
							if (userOrgId.indexOf(orgIdFilter)>=0) {
								orgUserIds.add(id);
							}
						}
						if (orgUserIds.size() != 0) {
							queryFilter.addFilterForIB("userIds", orgUserIds);
							list = this.sysUserService.getByIdsFilter(queryFilter);
						}
					} else {
						queryFilter.addFilterForIB("userIds", userList);
						list = this.sysUserService.getByIdsFilter(queryFilter);
					}
				} else if (("3".equals(searchBy))) {
					String posIdFilter = queryFilter.getFilters().get("posId").toString();
					List<String> posUserIds = new ArrayList<>();
					for (String id : userList) {
						List<Long> posIds = positionService.getPositonIdsByUserId(Long.valueOf(id));
						if (posIds.contains(Long.valueOf(posIdFilter))) {
							posUserIds.add(id);
						}
					}
					if (posUserIds.size() != 0) {
						queryFilter.addFilterForIB("userIds", posUserIds);
						list = this.sysUserService.getByIdsFilter(queryFilter);
					}

				} else if (("1".equals(searchBy))) {
					String roleIdFilter = queryFilter.getFilters().get("roleId").toString();
					List<String> roleUserIds = new ArrayList<>();
					for (String id : userList) {
						String role = sysRoleService.getRoleIdsByUserId(Long.valueOf(id));
						if (role.indexOf(roleIdFilter) >= 0) {
							roleUserIds.add(id);
						}
					}
					if (roleUserIds.size() != 0) {
						queryFilter.addFilterForIB("userIds", roleUserIds);
						list = this.sysUserService.getByIdsFilter(queryFilter);
					}
				} else {
					/* queryFilter.addFilterForIB("userIds", ids); */
					queryFilter.addFilterForIB("userIds", userList);
					list = this.sysUserService.getByIdsFilter(queryFilter);
				}
			}else {
				list=this.sysUserService.getUserByQuery(queryFilter);
			}

		} else if (dataType != null && dataType.equals("roleResFilter")) { // 定制过滤型号管理员已选择人员
			List<UserRole> userList =userRoleService.getUserRoleByRoleId(Long.valueOf(dataId));
			list = new ArrayList<>();
			if (userList.size() != 0) {
				String demId = RequestUtil.getString(request, "path");
				if (("2".equals(searchBy))) {
					String orgIdFilter = queryFilter.getFilters().get("orgId").toString();
					if (!orgIdFilter.equals("root")) {
						List<String> orgUserIds = new ArrayList<>();
						for (UserRole userRole : userList) {
							String userOrgId = sysOrgService.getOrgIdsByUserId(userRole.getUserId());
							if (orgIdFilter.equals(userOrgId)) {
								orgUserIds.add(userRole.getUserId().toString());
							}
						}
						if (orgUserIds.size() != 0) {
							queryFilter.addFilterForIB("userIds", orgUserIds);
							list = this.sysUserService.getByIdsFilter(queryFilter);
						}
					} else {
						queryFilter.addFilterForIB("userIds", userList);
						list = this.sysUserService.getByIdsFilter(queryFilter);
					}
				} else if (("3".equals(searchBy))) {
					String posIdFilter = queryFilter.getFilters().get("posId").toString();
					List<String> posUserIds = new ArrayList<>();
					for (UserRole userRole : userList) {
						List<Long> posIds = positionService.getPositonIdsByUserId(userRole.getUserId());
						if (posIds.contains(Long.valueOf(posIdFilter))) {
							posUserIds.add(userRole.getUserId().toString());
						}
					}
					if (posUserIds.size() != 0) {
						queryFilter.addFilterForIB("userIds", posUserIds);
						list = this.sysUserService.getByIdsFilter(queryFilter);
					}

				} else if (("1".equals(searchBy))) {
					String roleIdFilter = queryFilter.getFilters().get("roleId").toString();
					List<String> roleUserIds = new ArrayList<>();
					for (UserRole userRole : userList) {
						String role = sysRoleService.getRoleIdsByUserId(userRole.getUserId());
						if (role.indexOf(roleIdFilter) >= 0) {
							roleUserIds.add(userRole.getUserId().toString());
						}
					}
					if (roleUserIds.size() != 0) {
						queryFilter.addFilterForIB("userIds", roleUserIds);
						list = this.sysUserService.getByIdsFilter(queryFilter);
					}
				} else {
					/* queryFilter.addFilterForIB("userIds", ids); */
					List<String> userIdlist=new ArrayList<>();
					for (UserRole userRole : userList) {
						userIdlist.add(userRole.getUserId().toString());
					}
					queryFilter.addFilterForIB("userIds", userIdlist);
					list = this.sysUserService.getByIdsFilter(queryFilter);
				}
			}
			else {
				if (orgId == 0) {
					SysOrg sysCurrentOrg = (SysOrg) UserContextUtil.getCurrentOrg();
					if ((StringUtil.isNotEmpty(type)) && (!"all".equals(typeVal))
							&& (BeanUtils.isNotEmpty(sysCurrentOrg))) {
						String path = this.orgServiceImpl.getSysOrgByScope(type, typeVal).getPath();
						queryFilter.addFilterForIB("path", path + "%");
						list = this.sysUserService.getDistinctUserByOrgPath(queryFilter);
					} else {
						list = this.sysUserService.getUserByQuery(queryFilter);
					}
				} else {
					list = this.sysUserService.getDistinctUserByOrgId(queryFilter);
				}
			}
		} 
		else if ("4".equals(searchBy)) {
			String demId = RequestUtil.getString(request, "path");
			if (demId.equals("-1")) {
				list = this.sysUserService.getUserNoOrg(queryFilter);
			} else {
				queryFilter.addFilterForIB("isPrimary", Integer.valueOf(1));
				list = this.sysUserService.getDistinctUserByOrgPath(queryFilter);
			}
			list = this.sysUserService.getOnlineUser(list);
		} else if ("2".equals(searchBy)) {
			if (includSub == 0)
				list = this.sysUserService.getDistinctUserByOrgId(queryFilter);
			else {
				list = this.sysUserService.getDistinctUserByOrgPath(queryFilter);
			}
		} else if ("3".equals(searchBy)) {
			if (includSub == 0)
				list = this.sysUserService.getDistinctUserByPosId(queryFilter);
			else {
				list = this.sysUserService.getDistinctUserByPosPath(queryFilter);
			}
		} else if ("1".equals(searchBy)) {
			list = this.sysUserService.getUserByRoleId(queryFilter);
		} else {
			if (orgId == 0) {
				SysOrg sysCurrentOrg = (SysOrg) UserContextUtil.getCurrentOrg();
				if ((StringUtil.isNotEmpty(type)) && (!"all".equals(typeVal))
						&& (BeanUtils.isNotEmpty(sysCurrentOrg))) {
					String path = this.orgServiceImpl.getSysOrgByScope(type, typeVal).getPath();
					queryFilter.addFilterForIB("path", path + "%");
					list = this.sysUserService.getDistinctUserByOrgPath(queryFilter);
				} else {
					list = this.sysUserService.getUserByQuery(queryFilter);
				}
			} else {
				list = this.sysUserService.getDistinctUserByOrgId(queryFilter);
			}
		}

		List userList = new ArrayList();
		String orgNames = "";

		for (SysUser user : list) {
			orgNames = this.userPositionService.getOrgnamesByUserId(user.getUserId());
			user.setOrgName(orgNames.toString());
			userList.add(user);
		}

		result.addObject("sysUserList", userList);
		result.addObject("isSingle", isSingle);
		result.addObject("type", type);
		result.addObject("typeVal", typeVal);
		result.addObject("relvalue", relvalue);
		result.addObject("dataId", dataId);
		result.addObject("dataType", dataType);
		return result;
	}

	@RequestMapping({ "getTreeData" })
	@ResponseBody
	@Action(description = "用户选择器查询", execOrder = ActionExecOrder.AFTER, detail = "用户选择器查询", exectype = SysAuditExecType.SELECT_TYPE)
	public List<OnlineUser> getTreeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Long, OnlineUser> onlineUsers = UserSessionListener.getOnLineUsers();
		List onlineList = new ArrayList();
		for (Long sessionId : onlineUsers.keySet()) {
			OnlineUser onlineUser = new OnlineUser();
			onlineUser = (OnlineUser) onlineUsers.get(sessionId);
			onlineList.add(onlineUser);

		}
		return onlineList;
	}

	@RequestMapping({ "getUserListByJobId" })
	@ResponseBody
	@Action(description = "根据职务ID取得用户List", detail = "根据职务ID【${jobId}】取得用户List", exectype = SysAuditExecType.SELECT_TYPE)
	public List<SysUser> getUserListByJobId(HttpServletRequest request) throws Exception {
		Long jobId = Long.valueOf(RequestUtil.getLong(request, "jobId"));
		List list = this.sysUserService.getUserListByJobId(jobId);
		return list;
	}

	@RequestMapping({ "getUserListByPosId" })
	@ResponseBody
	@Action(description = "根据岗位ID取得用户List", detail = "根据职务ID【${posId}】取得用户List", exectype = SysAuditExecType.SELECT_TYPE)
	public List<SysUser> getUserListByPosId(HttpServletRequest request) throws Exception {
		Long posId = Long.valueOf(RequestUtil.getLong(request, "posId"));
		List list = this.sysUserService.getUserListByPosId(posId);
		return list;
	}

	/**
	 * 获取该用户的组织角色
	 *
	 * @param userId
	 * @return
	 */
	public Map<SysOrg, List<SysRole>> getOrgRoles(Long userId) {
		List<SysOrg> sysOrgs = this.sysOrgService.getOrgsByUserId(userId);
		Map roles = new HashMap();
		if (BeanUtils.isNotEmpty(sysOrgs)) {
			for (SysOrg sysOrg : sysOrgs) {
				Long orgId = sysOrg.getOrgId();
				List<String> roleList = this.sysRoleService.getOrgRoles(orgId);
				List sysRoles = new ArrayList();
				for (String role : roleList) {
					SysRole sysRole = this.sysRoleService.getByRoleAlias(role);
					sysRoles.add(sysRole);
					roles.put(sysOrg, sysRoles);
				}
			}
		}
		return roles;
	}

	/**
	 * 编辑用户/新增用户
	 *
	 * @author YangBo
	 * @param request
	 * @param response
	 * @param sysUser
	 * @throws Exception
	 */
	@RequestMapping({ "save" })
	@Action(description = "添加或更新用户", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>用户 ${SysAuditLinkService.getSysUserLink(username)}", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { SysUser.class, UserPosition.class, UserRole.class, UserUnder.class }, pkName = "userId")
	public void save(HttpServletRequest request, HttpServletResponse response, SysUser sysUser, String signModelNewPath)
			throws Exception {
		String resultMsg = null;
		// 设置操作结果，默认为操作失败
		Short result = 0;
		Long selectedOrgId = Long.valueOf(RequestUtil.getLong(request, "selectedOrgId"));
		boolean isOrgAdd = selectedOrgId.longValue() > 0L;
		Map params = RequestUtil.getParameterValueMap(request);
		Integer bySelf = Integer.valueOf(RequestUtil.getInt(request, "bySelf"));
		Long[] posIdCharge = RequestUtil.getLongAry(request, "posIdCharge");
		Long[] posIds = RequestUtil.getLongAry(request, "posId");
		;
		Long posIdPrimary = Long.valueOf(RequestUtil.getLong(request, "posIdPrimary"));
		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		Long[] roleIds = RequestUtil.getLongAry(request, "roleId");
		Long[] aryOrgId = RequestUtil.getLongAry(request, "orgId");
		Long[] arySuperiorId = RequestUtil.getLongAryByStr(request, "superiorId");
		boolean isadd = true;
		// 第一次新增用户需明确主组织主岗位
		if ((BeanUtils.isNotEmpty(aryOrgId)) && (posIdPrimary.longValue() == 0L)) {
			resultMsg = "请选择主岗位！";
			isadd = userId == null;
			writeResultMessage(response.getWriter(), resultMsg, 0);
		} else {
			if (sysUser.getUserId() == null) {
				boolean isExist = this.sysUserService.isUsernameExist(sysUser.getUsername());
				if (isExist) {
					resultMsg = "该账号已经存在！";
					writeResultMessage(response.getWriter(), resultMsg, 0);
				} else {
					// 校验密码是否满足要求
					resultMsg = checkPassword(sysUser.getPassword(), sysUser.getUsername());
					if (StringUtil.isNotEmpty(resultMsg)) {
						writeResultMessage(response.getWriter(), resultMsg, 0);
					} else {
						String enPassword = PasswordUtil.generatePassword(sysUser.getPassword());
						sysUser.setPassword(enPassword);
						sysUser.setSuperiorIds(arySuperiorId);
						try {
							this.sysUserService.saveUser(bySelf, sysUser, posIdCharge, posIds, posIdPrimary, roleIds,
									aryOrgId);

							/*-----------------------------------更新签章(ie8样式不兼容,弃用)------------------------------------*/
							// 取刚刚保存的用户id
							/*
							 * Long savedUserId=sysUserService.getByUsernames(sysUser.getUsername()).get(0).
							 * getUserId();
							 * signModelService.saveAndUpdateSignModel(savedUserId,signModelNewPath);
							 */

							/*-------------------------------------END--------------------------------------*/

							// 新增人员后更新redis中的人员信息
							this.sysUserService.setAllSysUserToRedis();
							result = 1;
							resultMsg = "添加用户成功";
							writeResultMessage(response.getWriter(), resultMsg, 1);
						} catch (Exception e) {
							resultMsg = "添加用户失败:" + e.getMessage();
							writeResultMessage(response.getWriter(), resultMsg, 0);
							e.printStackTrace();
						}
					}

				}
			} else {
				if ((UserContextUtil.isSuperAdmin(sysUser)) && (!UserContextUtil.isSuperAdmin())) {
					resultMsg = "使用限制，不能编辑超级管理员！";
					writeResultMessage(response.getWriter(), resultMsg, 0);
				} else {
					boolean isExist = this.sysUserService.isUsernameExistForUpd(sysUser.getUserId(),
							sysUser.getUsername());
					if (isExist) {
						resultMsg = "该账号已经存在！";
						writeResultMessage(response.getWriter(), resultMsg, 0);
					} else {
						sysUser.setSuperiorIds(arySuperiorId);
						try {
							// 重新启用密码加密模块 20200818 by zmz
							String enPassword = PasswordUtil.generatePassword(sysUser.getPassword());// MD5解码
							sysUser.setPassword(enPassword);
							this.sysUserService.saveUser(bySelf, sysUser, posIdCharge, posIds, posIdPrimary, roleIds,
									aryOrgId);

							/*-----------------------------------更新签章(ie8样式不兼容,弃用)------------------------------------*/
							// 若传了新签章地址过来,则更新签章
							/*
							 * if(!signModelNewPath.equals("")){
							 * signModelService.saveAndUpdateSignModel(sysUser.getUserId(),signModelNewPath)
							 * ; }
							 */

							/*-------------------------------------END--------------------------------------*/

							// 更新人员信息后更新redis中的人员信息
							this.sysUserService.setAllSysUserToRedis();
							result = 1;
							resultMsg = "更新用户成功";
							isadd = false;
							writeResultMessage(response.getWriter(), resultMsg, 1);
						} catch (Exception e) {
							resultMsg = "添加用户失败:" + e.getMessage();
							writeResultMessage(response.getWriter(), resultMsg, 0);
							e.printStackTrace();
						}
					}
				}
			}
		}
		try {
			LogThreadLocalHolder.setResult(result);
			LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("username", sysUser.getUsername());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@RequestMapping({ "getUserData" })
	public void getUserData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		try {
			ISysUser user = (ISysUser) UserContextUtil.getCurrentUser();

			writer.println("{\"success\":\"true\",\"user\":{\"id\":\"" + user.getUserId() + "\",\"name\":\""
					+ user.getFullname() + "\",\"groupId\":\"" + user.getOrgId() + "\",\"groupName\":\""
					+ user.getOrgName() + "\" } }");
		} catch (Exception e) {
			this.logger.warn(e.getMessage());
			writer.println("{\"success\":\"false\",\"user\":\"\"}");
		} finally {
			writer.close();
		}
	}

	/**
	 * 首页个人资料修改
	 * 
	 * @param request
	 * @param response
	 * @param sysUser
	 * @throws Exception
	 */
	@RequestMapping({ "updateCommon" })
	@Action(description = "首页个人资料修改", execOrder = ActionExecOrder.AFTER, detail = "首页个人资料修改", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { SysUser.class }, pkName = "userId")
	public void updateCommon(HttpServletRequest request, HttpServletResponse response, SysUser sysUser)
			throws Exception {
		String resultMsg = null;
		try {
			this.sysUserService.updateCommon(sysUser);
			resultMsg = getText("更新", new Object[] { "用户信息" });
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), 0);
		}
	}

	/**
	 * 通过前台传过来的用户输入框的值对用户进行模糊查询
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "getFuzzySysUser" })
	public void getFuzzySysUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();

		String userJson = "";

		String userFuzzyName = RequestUtil.getString(request, "fuzzyName");
		String fieldName = RequestUtil.getString(request, "fieldName");

		String type = RequestUtil.getString(request, "type");
		String typeVal = RequestUtil.getString(request, "typeVal");
		String relvalue = RequestUtil.getString(request, "relvalue");

		if (fieldName == "") {
			// 若没有指定字段名称，默认为fullname
			fieldName = "fullname";
		}
		try {
			JSONArray userArr = this.sysUserService.getFuzzySysUserList(userFuzzyName, fieldName, relvalue, type,
					typeVal);
			userJson = userArr.toString();
			ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, userJson);
			resultMessage.addData("type", "user");

			out.print(resultMessage);
		} catch (Exception e) {
			// TODO: handle exception
			ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, e.getMessage());

			out.print(resultMessage);
		}
	}

	/**
	 * 根据
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "exportExcel" })
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String filePath = sysUserService.exportExcel(new QueryFilter(request, "sysUserItem"));
			writeResultMessage(response.getWriter(), filePath, ResultMessage.Success);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), e.getMessage(), ResultMessage.Fail);
		}
	}

	/**
	 * 根据
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "dataSynchronization" })
	public void dataSynchronization(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String syncUserUrl = "http://staffer.no8.sast.casc:8080/StaffService.asmx/WsGetStaff";
		// 3
		/*
		 * String
		 * syncCheck="http://staffer.no8.sast.casc:8080/StaffService.asmx/WsCheckUser";
		 * //
		 */ String syncDept = "http://staffer.no8.sast.casc:8080/StaffService.asmx/WsGetDepartment";// 24
		String strUserId = "";
		dataSynchronizationService.syncOrg(syncDept);
		dataSynchronizationService.syncUser(syncUserUrl);
	}

	/**
	 * 返回上传用户签章图片的页面 by zmz
	 * 
	 * @return
	 */
	@RequestMapping({ "getUploadSignModelView" })
	public ModelAndView getUploadSignModelView(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("/oa/system/uploadSignModel.jsp").addObject("userId", request.getParameter("userId"));
	}

	/**
	 * 保存用户签章图片
	 * 
	 * @param
	 * @param
	 * @param userId
	 * @throws IOException
	 */
	@RequestMapping("saveSignModel")
	public void saveSignModel(String signModelId, String userId) {
		// userID要从string转long
		long modUserId = Long.parseLong(userId);
		signModelService.saveAndUpdateSignModel(modUserId, signModelId);

	}

}
