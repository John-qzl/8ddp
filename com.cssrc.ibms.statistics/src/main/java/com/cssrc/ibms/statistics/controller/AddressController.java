package com.cssrc.ibms.statistics.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.statistics.model.Address;
import com.cssrc.ibms.statistics.service.AddressService;

@Controller
@RequestMapping("/oa/statistics/address/")
@Action(ownermodel = SysAuditModelType.DATA_STATISTICS)
public class AddressController extends BaseController {
	@Resource
	private AddressService addressService;
	
	
	
	/**
	 * 统计工具树
	 */
	@RequestMapping("tree")
	public ModelAndView tree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return getAutoView();
	}
	/**
	 * 工具管理
	 */
	@RequestMapping({ "list" })
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long toolId = RequestUtil.getLong(request, "toolId");
		QueryFilter qf = new QueryFilter(request, "toolItem");
		if(!qf.equals(0L)){
			qf.getFilters().put("toolId", toolId);
		}
		List<Address> list = this.addressService.getAll(qf);
		return getAutoView().addObject("addressList", list)
				.addObject("toolId", toolId);
	}
	/**
	 * 编辑
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "edit" })
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long addressId = Long.valueOf(RequestUtil.getLong(request, "addressId"));
		Long toolId = Long.valueOf(RequestUtil.getLong(request, "toolId"));
		String returnUrl = RequestUtil.getPrePage(request);
		Address address = null;
		if (addressId != 0L) {
			address = this.addressService.getById(addressId);
		}else{
			address = new Address();
			address.setToolId(toolId);
		}
		return getAutoView().addObject("address", address).addObject("returnUrl",
				returnUrl);
	}

	/**
	 * 删除
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
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "addressId");
			this.addressService.delByIds(lAryId);
			message = new ResultMessage(1, "删除成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除失败:" + e.getMessage());
			e.printStackTrace();
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
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
			Address address, BindingResult bindResult) throws Exception {
		PrintWriter out = response.getWriter();
		ResultMessage resultMessage = validForm("address", address, bindResult,request);
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
		}
		boolean isadd = address.getAddressId() == null;

		if (address.getAddressId() == null) {
			Integer rtn = this.addressService.isAliasExists(address.getAlias());
			if (rtn.intValue() > 0) {
				writeResultMessage(response.getWriter(), "别名在系统中已存在!", 0);
				return;
			}
			try {
				Long addressId = Long.valueOf(UniqueIdUtil.genId());
				address.setAddressId(addressId);
				this.addressService.add(address);
				String result = "{result:1,operate:'add'}";
				out.print(result);
			} catch (Exception e) {
				e.printStackTrace();
				writeResultMessage(response.getWriter(), "添加访问路径失败!", 0);
			}
		} else {
			try {
				this.addressService.update(address);
				String result = "{result:1,operate:'edit'}";
				out.print(result);
			} catch (Exception e) {
				String str = MessageUtil.getMessage();
				if (StringUtil.isNotEmpty(str)) {
					resultMessage = new ResultMessage(0, "更新访问路径失败:" + str);
					response.getWriter().print(resultMessage);
				} else {
					String message = ExceptionUtil.getExceptionMessage(e);
					resultMessage = new ResultMessage(0, message);
					response.getWriter().print(resultMessage);
				}
			}
		}
		try {
			LogThreadLocalHolder.putParamerter("address", address);
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("addressId", address.getAddressId().toString());
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}
	}
}
