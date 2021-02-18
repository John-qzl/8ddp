package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.jms.intf.IMessageProducer;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

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
/**
 * 组织表单Controller层
 * <p>Title:SysOrgFormController</p>
 * @author Yangbo 
 * @date 2016年9月13日上午10:17:37
 */
@Controller
@RequestMapping({ "/oa/system/sysOrg/" })
@Action(ownermodel = SysAuditModelType.ORG_MANAGEMENT)
public class SysOrgFormController extends BaseFormController {

	@Resource
	private SysOrgService sysOrgService;
    @Resource
    private IMessageProducer messageProducer;
	/**
	 * 添加或更新组织操作
	 * @param request
	 * @param response
	 * @param sysOrg
	 * @param bindResult
	 * @throws Exception
	 */
	@RequestMapping({"save"})
	@Action(description="添加或更新组织架构", execOrder = ActionExecOrder.AFTER, detail="<#if isAdd>添加<#else>更新</#if>组织 ${SysAuditLinkService.getSysOrgLink(Long.valueOf(id))}",exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { SysOrg.class }, pkName = "orgId")
	public void save(HttpServletRequest request, HttpServletResponse response, SysOrg sysOrg, BindingResult bindResult)
			throws Exception
	{
		ResultMessage resultMessage = validForm("sysOrg", sysOrg, bindResult, request);
		sysOrg.setCode(String.valueOf(UniqueIdUtil.genId()));
		//设置操作结果，默认为操作失败
		Short result = 0;
		boolean isAdd = true;
		String id = null;
		String resultMsg = null;
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}

		Integer rtn = this.sysOrgService.getCountByCode(sysOrg.getCode(), sysOrg.getOrgId());
		if (rtn.intValue() > 0) {
			resultMsg = "组织代码:" + sysOrg.getCode() + "已被使用,请检查!";
			id = sysOrgService.getByCode(sysOrg.getCode()).getOrgId().toString();
			writeResultMessage(response.getWriter(), resultMsg, 0);
		}else{
			if (BeanUtils.isZeroEmpty(sysOrg.getOrgId())) {
				Long orgId = Long.valueOf(UniqueIdUtil.genId());
				sysOrg.setOrgId(orgId);
				sysOrg.setCode(String.valueOf(orgId));
				sysOrg.setCreatorId(UserContextUtil.getCurrentUserId());
				Long supOrgId = Long.valueOf(RequestUtil.getLong(request, "orgSupId"));
				SysOrg supOrg = (SysOrg)this.sysOrgService.getById(supOrgId);

				if (supOrg == null) {
					sysOrg.setPath("." + supOrgId + "." + orgId + ".");
					sysOrg.setOrgPathname("/" + sysOrg.getOrgName());
				} else {
					sysOrg.setPath(supOrg.getPath() + "."+ sysOrg.getOrgId() + ".");
					sysOrg.setOrgPathname(supOrg.getOrgPathname() + "/" + sysOrg.getOrgName());
				}

				this.sysOrgService.addOrg(sysOrg);
				id = orgId.toString();
				result = 1;
				writeResultMessage(response.getWriter(), "{\"orgId\":\"" + orgId + "\",\"action\":\"add\"}", 1);
			} else {
				isAdd = false;
				
				sysOrg.setUpdateId(UserContextUtil.getCurrentUserId());
				String pathName = getOrgPathName(sysOrg.getPath(), sysOrg.getOrgName());
				sysOrg.setOrgPathname(pathName);
				this.sysOrgService.updOrg(sysOrg);
				upSysOrgBySupId(sysOrg.getOrgId().longValue(), pathName);
				result = 1;
				id = sysOrg.getOrgId().toString();
				writeResultMessage(response.getWriter(), "{\"orgId\":\"" + sysOrg.getOrgId() + "\",\"action\":\"upd\"}", 1);
			}
			//发送 同步消息
			messageProducer.sendMdm(sysOrg);
		}
		try {
			LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isAdd));
			LogThreadLocalHolder.putParamerter("id", id);
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	/**
	 * 更新子组织的pathname
	 * @param orgSupId
	 * @param supPathName
	 * @throws Exception
	 */
	private void upSysOrgBySupId(long orgSupId, String supPathName)
			throws Exception {
		List sysOrgs = this.sysOrgService.getOrgByOrgSupId(Long
				.valueOf(orgSupId));
		if ((sysOrgs != null) && (sysOrgs.size() != 0)) {
			SysOrg sysOrg = null;
			for (int i = 0; i < sysOrgs.size(); i++) {
				sysOrg = (SysOrg) sysOrgs.get(i);
				String pathName = supPathName + "/" + sysOrg.getOrgName();
				sysOrg.setOrgPathname(pathName);
				this.sysOrgService.updOrg(sysOrg);

				upSysOrgBySupId(sysOrg.getOrgId().longValue(), pathName);
			}
		}
	}
	/**
	 * 拼接新的pathname
	 * @param orgPath
	 * @param orgName
	 * @return
	 */
	private String getOrgPathName(String orgPath, String orgName) {
		orgPath = StringUtil.trimSufffix(orgPath, ".");
		orgPath = StringUtil.trimPrefix(orgPath, ".");
		String[] aryPath = orgPath.split("\\.");
		String pathName = "";
		for (int i = 0; i < aryPath.length-1; i++) {
			SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(new Long(
					aryPath[i]));
			pathName = pathName + "/" + sysOrg.getOrgName();
		}
		pathName = pathName + "/" + orgName;
		return pathName;
	}

	@ModelAttribute
	protected SysOrg getFormObject(@RequestParam("orgId") Long orgId,
			Model model) throws Exception {
		this.logger.debug("enter SysOrg getFormObject here....");
		SysOrg sysOrg = null;
		if (orgId != null)
			sysOrg = (SysOrg) this.sysOrgService.getById(orgId);
		else {
			sysOrg = new SysOrg();
		}
		return sysOrg;
	}
}
