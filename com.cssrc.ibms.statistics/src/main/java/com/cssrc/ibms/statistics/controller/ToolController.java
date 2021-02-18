package com.cssrc.ibms.statistics.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.statistics.model.Address;
import com.cssrc.ibms.statistics.model.Tool;
import com.cssrc.ibms.statistics.service.AddressService;
import com.cssrc.ibms.statistics.service.ToolService;

@Controller
@RequestMapping("/oa/statistics/tool/")
@Action(ownermodel = SysAuditModelType.DATA_STATISTICS)
public class ToolController extends BaseController {
	@Resource
	private ToolService toolService;
	@Resource
	private AddressService addressService;

	
	
	@RequestMapping({"getTreeData"})
	@ResponseBody
	public List<Tool> getTreeData(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		List<Tool> list = this.toolService.getAll();
		Tool root = Tool.getRootTool();
		list.add(root);	
		return list;
	}
	/**
	 * 工具管理
	 */
	@RequestMapping({ "list" })
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Tool> list = this.toolService.getAll(new QueryFilter(request,
				"toolItem"));
		ModelAndView mv = getAutoView().addObject("toolList", list);
		return mv;
	}

	/**
	 * 编辑
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "edit" })
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long toolId = Long.valueOf(RequestUtil.getLong(request, "toolId"));
		String returnUrl = RequestUtil.getPrePage(request);
		Tool tool = null;
		if (toolId.longValue() != 0L) {
			tool = (Tool) this.toolService.getById(toolId);
		} else {
			tool = new Tool();
		}
		return getAutoView().addObject("tool", tool).addObject("returnUrl",
				returnUrl);
	}

	/**
	 * 删除
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "del" })
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage message = null;
		String preUrl = RequestUtil.getPrePage(request);
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "toolId");
			delByIds(lAryId);
			message = new ResultMessage(1, "删除成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除失败:" + e.getMessage());
			e.printStackTrace();
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 相当于级联删除
	 * 
	 * @param lAryId
	 */
	private void delByIds(Long[] lAryId) {
		if (BeanUtils.isEmpty(lAryId))
			return;
		for (Long toolId : lAryId) {
			List<Address> list = this.addressService.getByToolId(toolId);
			for (Address address : list) {
				Long addressId = address.getAddressId();
				this.addressService.delById(addressId);
			}
			this.toolService.delById(toolId);
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param type
	 * @param bindResult
	 * @throws Exception
	 */
	@RequestMapping({ "save" })
	public void save(HttpServletRequest request, HttpServletResponse response,
			Tool tool, BindingResult bindResult) throws Exception {
		PrintWriter out = response.getWriter();
		ResultMessage resultMessage = validForm("tool", tool, bindResult,request);
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
		}
		boolean isadd = tool.getToolId() == null;

		if (tool.getToolId() == null) {
			Integer rtn = this.toolService.isAliasExists(tool.getAlias());
			if (rtn.intValue() > 0) {
				writeResultMessage(response.getWriter(), "别名在系统中已存在!", 0);
				return;
			}
			try {
				Long toolId = Long.valueOf(UniqueIdUtil.genId());
				tool.setToolId(toolId);
				this.toolService.add(tool);
				String result = "{result:1,operate:'add'}";
				out.print(result);
			} catch (Exception e) {
				e.printStackTrace();
				writeResultMessage(response.getWriter(), "添加表单类别失败!", 0);
			}
		} else {
			try {
				this.toolService.update(tool);
				String result = "{result:1,operate:'edit'}";
				out.print(result);
			} catch (Exception e) {
				String str = MessageUtil.getMessage();
				if (StringUtil.isNotEmpty(str)) {
					resultMessage = new ResultMessage(0, "更新表单类别失败:" + str);
					response.getWriter().print(resultMessage);
				} else {
					String message = ExceptionUtil.getExceptionMessage(e);
					resultMessage = new ResultMessage(0, message);
					response.getWriter().print(resultMessage);
				}
			}
		}
		try {
			LogThreadLocalHolder.putParamerter("tool", tool);
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("toolId", tool.getToolId().toString());
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}
	}
}
