package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.jms.intf.IMessageProducer;
import com.cssrc.ibms.api.job.intf.IJobService;
import com.cssrc.ibms.api.job.model.IJob;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.system.intf.IDemensionService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.dao.UserPositionDao;
import com.cssrc.ibms.core.user.model.OrgAuth;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.user.service.OrgAuthService;
import com.cssrc.ibms.core.user.service.OrgServiceImpl;
import com.cssrc.ibms.core.user.service.PositionService;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.user.service.UserPositionService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
/**
 * 
 * <p>Title:PositionController</p>
 * @author Yangbo 
 * @date 2016-8-4下午04:06:57
 */
@Controller
@RequestMapping( { "/oa/system/position/" })
@Action(ownermodel = SysAuditModelType.ORG_MANAGEMENT)
public class PositionController extends BaseController {

	@Resource
	private PositionService positionService;

	@Resource
	private UserPositionService userPositionService;

	@Resource
	private SysOrgService sysOrgService;

	@Resource
	private IJobService jobService;

	@Resource
	private UserPositionDao userPositionDao;

	@Resource
	private IDemensionService demensionService;

	@Resource
	private OrgServiceImpl orgServiceImpl;

	@Resource
	private OrgAuthService orgAuthService;
	
	@Resource
    private IMessageProducer messageProducer;

	@Resource
	private ISysUserService sysUserService;
	   
	@RequestMapping( { "save" })
	@Action(description = "添加或更新系统岗位表，实际是部门和职务的对应关系表", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>系统岗位表 ${SysAuditLinkService.getPositionLink(Long.valueOf(id))}",exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { Position.class }, pkName = "posId")
	public void save(HttpServletRequest request, HttpServletResponse response,
			Position position) throws Exception {
		String resultMsg = null;
		Long currentUserId = UserContextUtil.getCurrentUserId();
		//设置操作结果，默认为操作失败
		Short result = 0;
		boolean isadd = true;
		try {
			if ((position.getPosId() == null)
					|| (position.getPosId().longValue() == 0L)) {
				if (this.positionService.isPoscodeUsed(position.getPosCode())) {
					position.setPosId(Long.valueOf(UniqueIdUtil.genId()));
					position.setPos_creatorId(currentUserId);
					position.setPos_createTime(new Date());
					position.setPos_updateId(currentUserId);
					position.setPos_updateTime(new Date());
					this.positionService.add(position);
					result = 1;
					resultMsg = getText("添加成功",
							new Object[] { "系统岗位表，实际是部门和职务的对应关系表" });
					writeResultMessage(response.getWriter(), resultMsg, 1);
				} else {
					resultMsg = getText("添加失败，岗位代码已被使用",
							new Object[] { "系统岗位表，实际是部门和职务的对应关系表" });
					writeResultMessage(response.getWriter(), resultMsg, 0);
				}
			} else if (this.positionService.getPosCode(position.getPosId())
					.equals(position.getPosCode())) {
				position.setPos_updateId(currentUserId);
				position.setPos_updateTime(new Date());
				this.positionService.update(position);

				updateUserPosition(position);
				result = 1;
				isadd = false;
				resultMsg = getText("更新成功",
						new Object[] { "系统岗位表，实际是部门和职务的对应关系表" });
				writeResultMessage(response.getWriter(), resultMsg, 1);
			} else if (this.positionService
					.isPoscodeUsed(position.getPosCode())) {
				position.setPos_updateId(currentUserId);
				position.setPos_updateTime(new Date());
				this.positionService.update(position);

				updateUserPosition(position);
				result = 1;
				isadd = false;
				resultMsg = getText("更新成功",
						new Object[] { "系统岗位表，实际是部门和职务的对应关系表" });
				writeResultMessage(response.getWriter(), resultMsg, 1);
			} else {
				resultMsg = getText("更新失败，岗位代码已被使用",
						new Object[] { "系统岗位表，实际是部门和职务的对应关系表" });
				writeResultMessage(response.getWriter(), resultMsg, 0);
			}
			
	        //发送 同步消息
            messageProducer.sendMdm(position);

            LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
    		LogThreadLocalHolder.putParamerter("id", position.getPosId().toString());
    		LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	public void updateUserPosition(Position position) {
		List<UserPosition> userPositionList = this.userPositionDao
				.getByPosId(position.getPosId());
		for (UserPosition userPosition : userPositionList) {
			userPosition.setJobId(position.getJobId());
			this.userPositionDao.update(userPosition);
		}
	}

	@RequestMapping( { "list" })
	@Action(description = "查看系统岗位表，实际是部门和职务的对应关系表分页列表", detail = "查看系统岗位列表",exectype=SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
		List<Position> list = this.positionService.getAll(new QueryFilter(
				request, "positionItem"));
		for (Position position : list) {
			position.setUserNames(this.sysUserService
					.getUsernamesByPosId(position.getPosId()));
		}
		ModelAndView mv = getAutoView().addObject("positionList", list)
				.addObject("action", "global")
				.addObject("orgId", orgId);
		return mv;
	}

	@RequestMapping( { "gradeList" })
	@Action(description = "查看系统岗位表，实际是部门和职务的对应关系表分页列表", detail = "查看系统岗位列表",exectype=SysAuditExecType.SELECT_TYPE)
	public ModelAndView gradeList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = list(request, response);
		Long authId = Long.valueOf(RequestUtil.getLong(request, "authId"));
		OrgAuth orgAuth = (OrgAuth) this.orgAuthService.getById(authId);
		mv.setViewName("/oa/system/positionList.jsp");
		mv.addObject("action", "grade")
		  .addObject("orgAuth", orgAuth);
		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除系统岗位表，实际是部门和职务的对应关系表", execOrder=ActionExecOrder.BEFORE, detail = "删除系统岗位表<#list posId?split(\",\") as item><#assign entity=positionService.getById(Long.valueOf(item))/> ${entity.posName}【${entity.posCode}】</#list>",exectype=SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		Long currentUserId = UserContextUtil.getCurrentUserId();
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "posId");

			int i = 0;
			for (int n = lAryId.length; i < n; i++) {
				this.positionService.deleteByUpdateFlag(lAryId[i],currentUserId);
			}
			message = new ResultMessage(1, "删除岗位成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑系统岗位表，实际是部门和职务的对应关系表", detail = "编辑系统岗位表",exectype=SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long posid = Long.valueOf(RequestUtil.getLong(request, "posId", 0L));
		String returnUrl = RequestUtil.getPrePage(request);
		Position position = (Position) this.positionService.getById(posid);

		Long orgId = Long.valueOf(0L);
		orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
		if ((orgId == null) || (orgId.longValue() == 0L)) {
			orgId = position.getOrgId();
		}
		SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(orgId);

		List jobList = this.jobService.getAll();
		return getAutoView().addObject("position", position).addObject(
				"returnUrl", returnUrl).addObject("sysOrg", sysOrg).addObject(
				"jobList", jobList);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看系统岗位表，实际是部门和职务的对应关系表明细", detail = "查看系统岗位表明细",exectype=SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long posid = Long.valueOf(RequestUtil.getLong(request, "posId"));
		Position position = (Position) this.positionService.getById(posid);

		Long orgId = Long.valueOf(0L);
		orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
		if ((orgId == null) || (orgId.longValue() == 0L)) {
			orgId = position.getOrgId();
		}
		SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(orgId);

		IJob job = (IJob) this.jobService.getById(position.getJobId());
		position.setJobName(job.getJobname());
		return getAutoView().addObject("position", position).addObject(
				"sysOrg", sysOrg);
	}
	/**
	 * 组织岗位树数据封装
	 *@author YangBo
	 *@param request
	 *@param response
	 *@return
	 *@throws Exception
	 */
	@RequestMapping( { "getOrgPosTreeData" })
	@ResponseBody
	public List<Position> getOrgPosTreeData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String orgIds = RequestUtil.getString(request, "orgIds");

		List<Position> positionList = this.positionService.getOrgListByOrgIds(orgIds);

		List<Position> positionList2 = this.positionService.getOrgPosListByOrgIds(orgIds);

		List treeList = new ArrayList();

		treeList.addAll(positionList);
		treeList.addAll(positionList2);

		return treeList;
	}

	@RequestMapping( { "getTreeData" })
	@ResponseBody
	public List<Position> getTreeData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Position> posList = this.positionService.getAll();
		Position pos = new Position();
		pos.setPosId(new Long(0L));
		pos.setPosName("全部");
		pos.setPosDesc("岗位");
		posList.add(pos);
		return posList;
		
	}

	@RequestMapping( { "selector" })
	public ModelAndView selector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request, "positionItem", true);
		Long demId = Long.valueOf(RequestUtil.getLong(request, "demId"));
		String type = RequestUtil.getString(request, "type");
		String typeVal = RequestUtil.getString(request, "typeVal");
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
		SysOrg sysOrgTemp = (SysOrg) this.sysOrgService.getById(orgId);
		if (BeanUtils.isEmpty(sysOrgTemp)||sysOrgTemp.getOrgSupId()==0) {
			SysOrg sysOrg = this.orgServiceImpl.getSysOrgByScope(type, typeVal);
			Map filters = filter.getFilters();
			filters.remove("orgId");
			filter.setFilters(filters);
			filter.addFilterForIB("orgPath", "%." + sysOrg.getOrgId() + ".%");
		}
		if (demId.longValue() != 0L) {
			filter.addFilterForIB("demId", demId);
		}
		filter.addFilterForIB("nodekey", "zwjb");
		List positionList = this.positionService.getBySupOrgId(filter);

		String isSingle = RequestUtil.getString(request, "isSingle", "false");
		ModelAndView mv = getAutoView().addObject("positionList", positionList)
				.addObject("type", type).addObject("typeVal", typeVal)
				.addObject("isSingle", isSingle);
		return mv;
	}

	@RequestMapping( { "getBySupOrgId" })
	@ResponseBody
	@Action(description = "获取岗位信息", execOrder = ActionExecOrder.AFTER, detail = "获取岗位信息", exectype = SysAuditExecType.SELECT_TYPE)
	public List<Position> getBySupOrgId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request, "positionItem", true);
		String type = RequestUtil.getString(request, "type");
		String typeVal = RequestUtil.getString(request, "typeVal");
		SysOrg sysOrg = this.orgServiceImpl.getSysOrgByScope(type, typeVal);
		filter.addFilterForIB("orgPath", "%." + sysOrg.getOrgId() + ".%");
		List list = this.positionService.getBySupOrgId(filter);
		return list;
	}

	@RequestMapping( { "getAll" })
	@ResponseBody
	public List<Position> getAll(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.positionService
				.getAll(new QueryFilter(request, false));
		return list;
	}

	@RequestMapping( { "getByPosId" })
	public ModelAndView getByPosId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/oa/system/positionGet.jsp");
		return getView(request, response, mv, 1);
	}

	public ModelAndView getView(HttpServletRequest request,
			HttpServletResponse response, ModelAndView mv, int isOtherLink)
			throws Exception {
		long id = RequestUtil.getLong(request, "posId");
		String canReturn = RequestUtil.getString(request, "canReturn", "0");
		Position position = (Position) this.positionService.getById(Long
				.valueOf(id));
		if (BeanUtils.isNotEmpty(position)) {
			SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(position
					.getOrgId());
			IJob job = (IJob) this.jobService.getById(position.getJobId());
			position.setOrgName(sysOrg.getOrgName());
			position.setJobName(job.getJobname());
		}
		return mv.addObject("position", position).addObject("isOtherLink",
				Integer.valueOf(isOtherLink)).addObject("canReturn", canReturn);
	}

	@RequestMapping( { "dialog" })
	@Action(description = "岗位对话框", execOrder = ActionExecOrder.AFTER, detail = "岗位对话框", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView dialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List demensionList = this.demensionService.getAll();
		ModelAndView mv = getAutoView().addObject("demensionList",
				demensionList);

		return mv;
	}
	
	/**
	 * 通过前台传过来的岗位输入框的值对岗位进行模糊查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({"getFuzzyPosition"})
	public void getFuzzyPosition(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		
		String positionJson = "";
		
		String posFuzzyName = RequestUtil.getString(request, "fuzzyName");
		String fieldName = RequestUtil.getString(request, "fieldName");
		
		String type = RequestUtil.getString(request, "type");
		String typeVal = RequestUtil.getString(request, "typeVal");
		String relvalue = RequestUtil.getString(request, "relvalue");
		
		if(fieldName==""){
			//若没有指定字段名称，默认为positionName
			fieldName="posName";
		}
		try {
			JSONArray positionArr = this.positionService.getFuzzyPositionList(posFuzzyName, fieldName, relvalue,type ,typeVal);
			positionJson = positionArr.toString();
			
			ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, positionJson);
			resultMessage.addData("type", "position");
			out.print(resultMessage);
		} catch (Exception e) {
			// TODO: handle exception
			ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, e.getMessage());
			out.print(resultMessage);
		}
	}
}
