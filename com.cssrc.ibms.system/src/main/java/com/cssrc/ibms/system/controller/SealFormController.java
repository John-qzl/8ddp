package com.cssrc.ibms.system.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.system.model.Seal;
import com.cssrc.ibms.system.model.SealRight;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SealRightService;
import com.cssrc.ibms.system.service.SealService;
import com.cssrc.ibms.system.service.SysFileService;

/**
 * 对象功能:电子印章 控制器类.
 * 
 * <p>
 * detailed comment
 * </p>
 * 
 * @author [创建人] WeiLei <br/>
 *         [创建时间] 2016-8-22 上午10:35:10 <br/>
 *         [修改人] WeiLei <br/>
 *         [修改时间] 2016-8-22 上午10:35:10
 * @see
 */
@Controller
@RequestMapping("/oa/system/seal/")
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class SealFormController extends BaseFormController {

	@Resource
	private SealService sealService;

	@Resource
	private SealRightService sealRightService;

	@Resource
	private SysFileService sysFileService;
	
	/**
	 * 
	 * 添加或更新电子印章.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-25 上午08:12:48 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-25 上午08:12:48
	 * @param request
	 * @param response
	 * @param seal
	 * @param bindResult
	 * @throws Exception
	 * @see
	 */
	@RequestMapping( { "save" })
	@Action(description = "添加或更新电子印章", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>电子印章【${SysAuditLinkService.getSealLink(Long.valueOf(sealId))}】")
	public void save(HttpServletRequest request, HttpServletResponse response,
			Seal seal, BindingResult bindResult) throws Exception {
		
		Long userId = UserContextUtil.getCurrentUserId();
		seal.setBelongId(userId);
		seal.setSealPath(((SysFile) this.sysFileService.getById(seal.getAttachmentId())).getFilepath());
		String rightType = RequestUtil.getString(request, "rightType");
		String rightIds = RequestUtil.getString(request, "rightIds");
		String rightNames = RequestUtil.getString(request, "rightNames");
		ResultMessage resultMessage = validForm("seal", seal, bindResult, request);
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		String resultMsg = null;
		if (seal.getSealId() == null) {
			seal.setSealId(Long.valueOf(UniqueIdUtil.genId()));
			this.sealService.add(seal);
			resultMsg = "添加电子印章成功";
		} else {
			this.sealService.update(seal);
			resultMsg = "更新电子印章成功";
		}
		this.sealRightService.saveSealRight(seal.getSealId(), rightType,
				rightIds, rightNames, userId, SealRight.CONTROL_TYPE_SEAL);
		writeResultMessage(response.getWriter(), resultMsg, 1);
	}

	/**
	 * 
	 * 根据印章Id，获取印章信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-25 上午08:13:25 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-25 上午08:13:25
	 * @param sealId
	 * @param model
	 * @return
	 * @throws Exception
	 * @see
	 */
	@ModelAttribute
	protected Seal getFormObject(@RequestParam("sealId") Long sealId,
			Model model) throws Exception {
		
		this.logger.debug("enter Seal getFormObject here....");
		Seal seal = null;
		if (sealId != null)
			seal = (Seal) this.sealService.getById(sealId);
		else {
			seal = new Seal();
		}
		return seal;
	}
	
}
