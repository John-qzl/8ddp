package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.user.service.PositionService;
import com.cssrc.ibms.core.user.service.UserPositionService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping( { "/oa/system/position/" })
@Action(ownermodel = SysAuditModelType.ORG_MANAGEMENT)
public class PositionFormController extends BaseFormController {

	@Resource
	private PositionService positionService;

	@Resource
	UserPositionService userPositionService;

	@Action(description="添加或更新系统岗位表", execOrder = ActionExecOrder.AFTER, detail="<#if isAdd>添加<#else>更新</#if>系统岗位表 ${SysAuditLinkService.getPositionLink(Long.valueOf(id))}",exectype=SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { Position.class }, pkName = "posId")
	public void save(HttpServletRequest request, HttpServletResponse response,
			Position position, BindingResult bindResult) throws Exception {
		ResultMessage resultMessage = validForm("position", position,
				bindResult, request);

		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		String resultMsg = null;
		List upList = getUserPositions(request);
		boolean isadd = true;
		if (position.getPosId() == null) {
			long parentId = RequestUtil.getLong(request, "parentId", 0L);

			position.setPosId(Long.valueOf(UniqueIdUtil.genId()));

			this.positionService.add(position, upList);

			resultMsg = "添加系统岗位表成功";
		} else {
			this.positionService.update(position);
			resultMsg = "更新系统岗位表成功";
			isadd = false;
		}
		writeResultMessage(response.getWriter(), resultMsg, 1);
		try {
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("id", position.getPosId().toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@ModelAttribute
	protected Position getFormObject(@RequestParam("posId") Long posId,
			Model model) throws Exception {
		this.logger.debug("enter Position getFormObject here....");
		Position position = null;
		if (posId != null)
			position = (Position) this.positionService.getById(posId);
		else {
			position = new Position();
		}
		return position;
	}

	private boolean getIsPrimary(Long userId, Long[] aryPrimary) {
		if (BeanUtils.isEmpty(aryPrimary))
			return false;
		for (int i = 0; i < aryPrimary.length; i++) {
			if (userId.equals(aryPrimary[i])) {
				return true;
			}
		}
		return false;
	}

	protected List<UserPosition> getUserPositions(HttpServletRequest request)
			throws Exception {
		List list = new ArrayList();
		Long[] aryUserId = RequestUtil.getLongAry(request, "userId");

		Long[] aryPrimary = RequestUtil.getLongAry(request, "chkPrimary");
		if (BeanUtils.isEmpty(aryPrimary))
			return list;

		for (int i = 0; i < aryUserId.length; i++) {
			Long userId = aryUserId[i];
			boolean isPrimary = getIsPrimary(userId, aryPrimary);
			UserPosition userPosition = new UserPosition();
			int a = isPrimary ? 1 : 0;
			Short primary = Short.valueOf((short) a);
			userPosition.setIsPrimary(primary);
			userPosition.setUserId(userId);
			list.add(userPosition);
		}

		return list;
	}
}
