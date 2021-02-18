package com.cssrc.ibms.core.user.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.OrgAuth;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.user.service.OrgAuthService;
import com.cssrc.ibms.core.user.service.PositionService;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.user.service.UserPositionService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 用户与岗位关联(是职务，组织，岗位，用户四张表的映射表)
 * <p>Title:UserPositionController</p>
 * @author Yangbo 
 * @date 2016-8-4下午04:36:32
 */
@Controller
@RequestMapping({"/oa/system/userPosition/"})
@Action(ownermodel = SysAuditModelType.ORG_MANAGEMENT)
@DataNote(beanName = UserPositionController.class)
public class UserPositionController extends BaseController
{

	@Resource
	private SysOrgService sysOrgService;
	@Resource
	private UserPositionService userPositionService;
	@Resource
	private OrgAuthService orgAuthService;
	@Resource
	private PositionService positionService;

	/**
	 * 添加和编辑用户组织岗位关联信息
	 * @param request
	 * @param response
	 * @param userPosition
	 * @throws Exception
	 */
	@RequestMapping({"save"})
	@Action(description="添加或更新用户组织岗位关联信息", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>用户组织岗位关联信息",exectype = SysAuditExecType.UPDATE_TYPE)
	public void save(HttpServletRequest request, HttpServletResponse response, UserPosition userPosition)
	throws Exception
	{
		String resultMsg = null;
		//设置操作结果，默认为操作失败
		Short result = 0;
		boolean isadd = true;
		try {
			if ((userPosition.getUserPosId() == null) || (userPosition.getUserPosId().longValue() == 0L)) {
				userPosition.setUserPosId(Long.valueOf(UniqueIdUtil.genId()));
				this.userPositionService.add(userPosition);
				resultMsg = getText("添加", new Object[] { "SYS_USER_POS" });
			} else {
				this.userPositionService.update(userPosition);
				isadd = false;
				resultMsg = getText("更新", new Object[] { "SYS_USER_POS" });
			}
			result = 1;
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), 0);
		}
		try {
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	/**
	 * 组织岗位分页列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"list"})
	@Action(description="查看组织岗位分页列表", detail ="查看组织岗位分页列表",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		List list = this.userPositionService.getAll(new QueryFilter(request, "userPositionItem"));
		ModelAndView mv = getAutoView().addObject("userPositionList", list);

		return mv;
	}
	/**
	 * 可批量删除组织岗位关联信息列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({"del"})
	@Action(description="删除组织岗位关联信息", detail ="删除组织岗位关联信息",exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "userPosId");
			this.userPositionService.delByIds(lAryId);
			message = new ResultMessage(1, "删除SYS_USER_POS成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({"edit"})
	@Action(description="编辑组织岗位关联信息", detail ="编辑组织岗位关联信息",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request)
	throws Exception
	{
		Long userPosId = Long.valueOf(RequestUtil.getLong(request, "userPosId", 0L));
		String returnUrl = RequestUtil.getPrePage(request);
		UserPosition userPosition = (UserPosition)this.userPositionService.getById(userPosId);

		return getAutoView().addObject("userPosition", userPosition)
		.addObject("returnUrl", returnUrl);
	}

	@RequestMapping({"get"})
	@Action(description="查看组织岗位关联信息明细", detail ="查看组织岗位关联信息明细",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long userPosId = Long.valueOf(RequestUtil.getLong(request, "userPosId"));
		UserPosition userPosition = (UserPosition)this.userPositionService.getById(userPosId);
		return getAutoView().addObject("userPosition", userPosition);
	}
	/**
	 * 给岗位批量分配人员
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({"addPosUser"})
	@Action(description="向岗位添加人员", execOrder=ActionExecOrder.AFTER, detail="向岗位<#assign entity=positionService.getById(Long.valueOf(posId))/>${entity.posName}【${entity.posId}】添加用户：<#list userIds?split(\",\") as item><#assign entity=sysUserService.getById(Long.valueOf(item))/>${entity.fullname}【${entity.username}】 </#list>",exectype = SysAuditExecType.ADD_TYPE)
	public void addPosUser(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long[] userIds = RequestUtil.getLongAryByStr(request, "userIds");
		Long posId = Long.valueOf(RequestUtil.getLong(request, "posId"));
		ResultMessage resultMessage = null;
		//设置操作结果，默认为操作失败
		Short result = 0;
		try {
			this.userPositionService.addPosUser(userIds, posId);
			result = 1;
			resultMessage = new ResultMessage(1, "在岗位添加用户成功!");
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "在岗位添加用户中失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
		writeResultMessage(response.getWriter(), resultMessage);
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	/**
	 * 岗位为posid的关联列表
	 * @param request
	 * @return
	 */
	@RequestMapping({"getUserListByPosId"})
	@ResponseBody
	@Action(description="根据岗位ID取得用户List", detail="根据岗位ID取得用户List",exectype = SysAuditExecType.SELECT_TYPE)
	public List<UserPosition> getUserListByPosId(HttpServletRequest request) { Long posId = Long.valueOf(RequestUtil.getLong(request, "posId"));
	return this.userPositionService.getByPosId(posId);
	}
	/**
	 * 组织orgid的查询列表
	 * @param request
	 * @return
	 */
	@RequestMapping( { "getUserListByOrgId" })
	@ResponseBody
	@Action(description = "根据组织ID取得用户List", detail = "根据组织ID取得用户List",exectype = SysAuditExecType.SELECT_TYPE)
	public List<UserPosition> getUserListByOrgId(HttpServletRequest request) {
		String orgId = RequestUtil.getString(request, "orgId");
		List<UserPosition> userPositionList = this.userPositionService.getUserByOrgIds(orgId);
		return userPositionList;
	}
	
	/**
	 * 组织管理组织人员
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "userList" })
	public ModelAndView userList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		boolean isRight = UserContextUtil.getCurrentUserId().equals(ISysUser.RIGHT_USER)?true:false;
		mv.addObject("action", "global")
		  .addObject("isRight", isRight);
		return getUserByOrgId(request, mv);
	}
	/**
	 * 通过orgid指定组织人员
	 * @param request
	 * @param mv
	 * @return
	 */
	private ModelAndView getUserByOrgId(HttpServletRequest request,
			ModelAndView mv) {
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
		SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(orgId);
		try{
			QueryFilter filter = new QueryFilter(request,"userPositionItem");
			String orgIds= sysOrgService.getIdsBySupId(orgId);
			filter.addFilterForIB("orgId", orgIds);
			List<UserPosition> list = this.userPositionService.getAll(filter);
			mv.addObject("userPositionList", list).addObject("orgId", orgId)
			.addObject("sysOrg", sysOrg);
		
		}
			catch (Exception e) {
				e.printStackTrace();
			
		}
	
		return mv;
		
	}
	/**
	 * 分级组织管理组织人员
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "userGradeList" })
	public ModelAndView userGradeList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		Long authId = Long.valueOf(RequestUtil.getLong(request, "authId", 0L));
		OrgAuth orgAuth = (OrgAuth) this.orgAuthService.getById(authId);
		boolean isRight = UserContextUtil.getCurrentUserId().equals(ISysUser.RIGHT_USER)?true:false;
		mv.addObject("orgAuth", orgAuth)
		  .addObject("action", "grade")
		  .addObject("isRight", isRight);
		mv.setViewName("/oa/system/userPositionUserList.jsp");
		return getUserByOrgId(request, mv);
	}
	/**
	 * 组织人员人员列表人员管理--设置主岗位(主组织)
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping( { "setIsPrimary" })
	@Action(description = "设置主岗位", execOrder = ActionExecOrder.AFTER, detail = "<#assign entity=SysAuditLinkService.getByUserPosId(Long.valueOf(userPosId))/>设置人员：${entity.fullname}【${entity.userName}】的主岗位为组织${entity.orgName}【${entity.orgId}】",exectype = SysAuditExecType.UPDATE_TYPE)
	public void setIsPrimary(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Long userPosId = Long.valueOf(RequestUtil.getLong(request, "userPosId",
				0L));
		ResultMessage message = null;
		String preUrl = RequestUtil.getPrePage(request);
		//设置操作结果，默认为操作失败
		Short result = 0;
		try {
			this.userPositionService.setIsPrimary(userPosId);
			result = 1;
			message = new ResultMessage(1, "设置组织成功");
		} catch (Exception ex) {
			ex.printStackTrace();
			message = new ResultMessage(0, "设置组织失败");
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	/**
	 * 组织人员人员列表人员管理--设置组织负责人
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping( { "setIsCharge" })
	@Action(description = "设置是主管", execOrder = ActionExecOrder.AFTER, detail = "<#assign entity=SysAuditLinkService.getByUserPosId(Long.valueOf(userPosId))/>设置人员：${entity.fullname}【${entity.userName}】 为组织${entity.orgName}【${entity.orgId}】的主管 ",exectype = SysAuditExecType.UPDATE_TYPE)
	public void setIsCharge(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String preUrl = RequestUtil.getPrePage(request);
		Long userPosId = Long.valueOf(RequestUtil.getLong(request, "userPosId",
				0L));
		ResultMessage message = null;
		//设置操作结果，默认为操作失败
		Short result = 0;
		try {
			this.userPositionService.setIsCharge(userPosId);
			result = 1;
			message = new ResultMessage(1, "设置主管成功");
		} catch (Exception ex) {
			message = new ResultMessage(0, "设置主管失败");
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	/**
	 * 组织管理组织人员加入用户(直接组织添加用户,则岗位为“无”)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping( { "add" })
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long orgId=Long.valueOf(RequestUtil.getLong(request, "orgId",0L));
		Long[] userIds = RequestUtil.getLongAryByStr(request, "userIds");
		//只添加用户和组织关系
		this.userPositionService.addOrgUser(userIds,orgId);
		response.sendRedirect(RequestUtil.getPrePage(request));
		}
	
}
