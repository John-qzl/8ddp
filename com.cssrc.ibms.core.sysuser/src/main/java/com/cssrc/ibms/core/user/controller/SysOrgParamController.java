package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.system.intf.IDemensionService;
import com.cssrc.ibms.api.system.intf.ISysParamService;
import com.cssrc.ibms.api.system.model.ISysParam;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysOrgParam;
import com.cssrc.ibms.core.user.service.SysOrgParamService;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping( { "/oa/system/sysOrgParam/" })
@Action(ownermodel = SysAuditModelType.ORG_MANAGEMENT)
public class SysOrgParamController extends BaseController {

	@Resource
	private SysOrgParamService sysOrgParamService;

	@Resource
	private SysOrgService sysOrgService;

	@Resource
	private ISysParamService sysParamService;

	@Resource
	private SysUserService sysUserService;

	@Resource
	private IDemensionService demensionService;

	@RequestMapping( { "editByOrgId" })
	public ModelAndView editByOrgId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		long orgId = RequestUtil.getLong(request, "orgId");
		String paramTypeFilter = RequestUtil.getString(request, "paramType");
		boolean isFirst = false;

		SysOrg sysOrg = (SysOrg) this.sysOrgService
				.getById(Long.valueOf(orgId));
		List categoryList = this.sysParamService.getDistinctCategory(Integer
				.valueOf(2), sysOrg.getDemId());
		List list = this.sysOrgParamService.getByOrgId(Long.valueOf(orgId));
		List sysParamList = this.sysParamService.getOrgParam(sysOrg.getDemId()
				.longValue());

		isFirst = BeanUtils.isEmpty(list);
		List orgParam = convertByList(sysParamList, list, paramTypeFilter);
		ModelAndView mv = getAutoView().addObject("paramList", orgParam)
				.addObject("sysOrg", sysOrg).addObject("isFirst",Boolean.valueOf(isFirst))
				.addObject("categoryList",categoryList).addObject("paramType", paramTypeFilter)
				.addObject("returnUrl", returnUrl).addObject("orgId", orgId);
		return mv;
	}

	private List<SysOrgParam> convertByList(List<ISysParam> sysParamList,
			List<SysOrgParam> orgParaList, String paramTypeFilter) {
		List list = new ArrayList();
		if (orgParaList.size() == 0) {
			for (ISysParam sysParam : sysParamList) {
				if ((!StringUtil.isEmpty(paramTypeFilter))
						&& ((!StringUtil.isNotEmpty(sysParam.getCategory())) || (!paramTypeFilter
								.contains(sysParam.getCategory() + ",")))
						&& (!paramTypeFilter.contains("all,")))
					continue;
				list.add(new SysOrgParam(sysParam));
			}

			return list;
		}

		Set paramKey = new HashSet();
		convertToList(orgParaList, paramKey);

		for (ISysParam sysParam : sysParamList) {
			if (((!StringUtil.isNotEmpty(sysParam.getCategory())) || (!paramTypeFilter
					.contains(sysParam.getCategory() + ",")))
					&& (!paramTypeFilter.contains("all,")))
				continue;
			if (!paramKey.contains(sysParam.getParamKey())) {
				orgParaList.add(new SysOrgParam(sysParam));
			}
		}
		return orgParaList;
	}

	private void convertToList(List<SysOrgParam> orgParaList,
			Set<String> paramKey) {
		for (SysOrgParam param : orgParaList) {
			ISysParam sysParam = (ISysParam) this.sysParamService.getById(param
					.getParamId());
			param.setSourceType(sysParam.getSourceType());
			param.setSourceKey(sysParam.getSourceKey());
			param.setDescription(sysParam.getDescription());
			paramKey.add(sysParam.getParamKey());
		}
	}

	@RequestMapping( { "saveByOrgId" })
	@Action(description = "编辑组织参数属性", execOrder = ActionExecOrder.BEFORE, detail = "编辑组织参数属性")
	public void saveByOrgId(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		ResultMessage resultMessage = null;
		//设置操作结果，默认为操作失败
		Short result = 0;
		try {
			long orgId = RequestUtil.getLong(request, "orgId");
			String jsonParamData = request.getParameter("jsonParamData");

			List valueList = coverBean(orgId, jsonParamData);
			this.sysOrgParamService.add(orgId, valueList);
			result = 1;
			resultMessage = new ResultMessage(1, "编辑组织参数属性成功");
			out.print(resultMessage.toString());
		} catch (Exception e) {
			e.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "编辑组织参数属性失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(0, message);
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

	private List<SysOrgParam> coverBean(long orgId, String jsonData)
			throws Exception {
		Map orgParamMap = jsonToMap(jsonData);
		List list = new ArrayList();
		List<?extends ISysParam> paramList = this.sysParamService.getAll();
		for (ISysParam sysParam : paramList) {
			if (orgParamMap.containsKey(sysParam.getParamId().toString())) {
				long paramId = sysParam.getParamId().longValue();
				SysOrgParam orgParam = new SysOrgParam();
				orgParam.setValueId(Long.valueOf(UniqueIdUtil.genId()));
				orgParam.setParamId(sysParam.getParamId());
				orgParam.setParamValue((String) orgParamMap.get(sysParam
						.getParamId().toString()));
				orgParam.setOrgId(Long.valueOf(orgId));
				String dataType = ((ISysParam) this.sysParamService.getById(Long
						.valueOf(paramId))).getDataType();
				String sourceType = ((ISysParam) this.sysParamService
						.getById(Long.valueOf(paramId))).getSourceType();
				if (sourceType.equals("input")) {
					if ((ISysParam.DATA_TYPE_MAP.get(dataType) != null)
							&& (((String) ISysParam.DATA_TYPE_MAP.get(dataType))
									.equals("数字")))
						orgParam.setParamIntValue(Long.valueOf(Long
								.parseLong((String) orgParamMap.get(sysParam
										.getParamId().toString()))));
					else if ((ISysParam.DATA_TYPE_MAP.get(dataType) != null)
							&& (((String) ISysParam.DATA_TYPE_MAP.get(dataType))
									.equals("日期"))) {
						orgParam.setParamDateValue(ISysParam.PARAM_DATE_FORMAT
								.parse((String) orgParamMap.get(sysParam
										.getParamId().toString())));
					}
				}
				list.add(orgParam);
			}
		}
		return list;
	}

	private Map<String, String> jsonToMap(String jsonData) {
		Map map = null;
		JSONObject json = null;
		json = JSONObject.fromObject(jsonData);
		Iterator iter = json.keySet().iterator();
		map = new HashMap();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			String value = json.getString(key);
			map.put(key, value);
		}
		return map;
	}

	@RequestMapping( { "dialog" })
	public ModelAndView dialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();

		List sysParamList = this.sysParamService.getOrgParam();

		mv.addObject("sysParamList", sysParamList).addObject("conditionUS",
				ISysParam.CONDITION_US);
		return mv;
	}

	@RequestMapping( { "getByParamKey" })
	public ModelAndView getByParamKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		String orgParam = RequestUtil.getString(request, "orgParam");
		int postFlag = RequestUtil.getInt(request, "postflag");
		orgParam = new String(orgParam.getBytes("ISO8859_1"), "utf-8");
		List userList = this.sysUserService.getByOrgParam(orgParam);
		mv.addObject("userList", userList);
		mv.addObject("postFlag", Integer.valueOf(postFlag));
		return mv;
	}
}
