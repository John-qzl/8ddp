package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.system.intf.IDictionaryService;
import com.cssrc.ibms.api.system.intf.ISysParamService;
import com.cssrc.ibms.api.system.model.ISysParam;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.SysUserParam;
import com.cssrc.ibms.core.user.service.SysUserParamService;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.DataNote;
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
import java.util.Enumeration;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 用户自定义属性参数
 * <p>Title:SysUserParamController</p>
 * @author Yangbo 
 * @date 2016年9月11日下午10:00:45
 */
@Controller
@RequestMapping({ "/oa/system/sysUserParam/" })
@Action(ownermodel = SysAuditModelType.USER_MANAGEMENT)
@DataNote(beanName = SysUserParamController.class)
public class SysUserParamController extends BaseController {
	@Resource
	private SysUserParamService sysUserParamService;
	@Resource
	private ISysParamService sysParamService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private IDictionaryService dictionaryService;
	protected Logger logger = LoggerFactory
			.getLogger(SysUserParamController.class);

	@RequestMapping({ "editByUserId" })
	public ModelAndView editByUserId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		long userId = RequestUtil.getLong(request, "userId");
		String paramTypeFilter = RequestUtil.getString(request, "paramType");
		boolean isFirst = false;
		List categoryList = this.sysParamService.getDistinctCategory(
				Integer.valueOf(1), null);

		SysUser user = (SysUser) this.sysUserService.getById(Long
				.valueOf(userId));
		List sysParamList = this.sysParamService.getStatusParam();
		List list = this.sysUserParamService.getByUserId(userId);

		List userParam = convertByList(sysParamList, list, paramTypeFilter);
		ModelAndView mv = getAutoView().addObject("paramList", userParam)
				.addObject("user", user)
				.addObject("isFirst", Boolean.valueOf(isFirst))
				.addObject("categoryList", categoryList)
				.addObject("paramType", paramTypeFilter)
				.addObject("returnUrl", returnUrl);
		return mv;
	}

	private List<SysUserParam> convertByList(List<ISysParam> sysParamList,
			List<SysUserParam> userParaList, String paramTypeFilter) {
		List list = new ArrayList();
		if (userParaList.size() == 0) {
			for (ISysParam sysParam : sysParamList) {
				if ((!StringUtil.isEmpty(paramTypeFilter))
						&& ((!StringUtil.isNotEmpty(sysParam.getCategory())) || (!paramTypeFilter
								.contains(sysParam.getCategory() + ",")))
						&& (!paramTypeFilter.contains("all,")))
					continue;
				list.add(new SysUserParam(sysParam));
			}

			return list;
		}

		Set paramKey = new HashSet();
		convertToList(userParaList, paramKey);

		for (ISysParam sysParam : sysParamList) {
			if (((!StringUtil.isNotEmpty(sysParam.getCategory())) || (!paramTypeFilter
					.contains(sysParam.getCategory() + ",")))
					&& (!paramTypeFilter.contains("all,")))
				continue;
			if (!paramKey.contains(sysParam.getParamKey())) {
				userParaList.add(new SysUserParam(sysParam));
			}
		}
		return userParaList;
	}

	private void convertToList(List<SysUserParam> userParaList,
			Set<String> paramKey) {
		for (SysUserParam param : userParaList) {
			ISysParam sysParam = (ISysParam) this.sysParamService.getById(param
					.getParamId());
			param.setSourceType(sysParam.getSourceType());
			param.setSourceKey(sysParam.getSourceKey());
			param.setDescription(sysParam.getDescription());
			paramKey.add(sysParam.getParamKey());
		}
	}

	@RequestMapping({ "saveByUserId" })
	@Action(description = "编辑人员参数属性",detail = "编辑人员参数属性")
	public void saveByUserId(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		//设置操作结果，默认为操作失败
		Short result = 0;
		try {
			long userId = RequestUtil.getLong(request, "userId");
			String jsonParamData = request.getParameter("jsonParamData");

			List valueList = coverBean(userId, jsonParamData);
			this.sysUserParamService.add(userId, valueList);
			result = 1;
			ResultMessage message = new ResultMessage(1, "编辑人员参数属性成功");
			out.print(message.toString());
		} catch (Exception e) {
			e.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(0,
						"编辑人员参数属性失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
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

	private List<SysUserParam> coverBean(long uesrId, String jsonData)
			throws Exception {
		Map userParamMap = jsonToMap(jsonData);
		List<?extends ISysParam> paramList = this.sysParamService.getAll();
		List list = new ArrayList();
		for (ISysParam sysParam : paramList) {
			if (userParamMap.containsKey(sysParam.getParamId().toString())) {
				long paramId = sysParam.getParamId().longValue();
				SysUserParam userParam = new SysUserParam();
				userParam.setValueId(Long.valueOf(UniqueIdUtil.genId()));
				userParam.setParamId(sysParam.getParamId());
				userParam.setParamValue((String) userParamMap.get(sysParam
						.getParamId().toString()));
				userParam.setUserId(Long.valueOf(uesrId));
				String dataType = ((ISysParam) this.sysParamService.getById(Long
						.valueOf(paramId))).getDataType();
				String sourceType = ((ISysParam) this.sysParamService
						.getById(Long.valueOf(paramId))).getSourceType();
				if (sourceType.equals("input")) {
					if ((ISysParam.DATA_TYPE_MAP.get(dataType) != null)
							&& (((String) ISysParam.DATA_TYPE_MAP.get(dataType))
									.equals("数字")))
						userParam.setParamIntValue(Long.valueOf(Long
								.parseLong((String) userParamMap.get(sysParam
										.getParamId().toString()))));
					else if ((ISysParam.DATA_TYPE_MAP.get(dataType) != null)
							&& (((String) ISysParam.DATA_TYPE_MAP.get(dataType))
									.equals("日期"))) {
						userParam.setParamDateValue(ISysParam.PARAM_DATE_FORMAT
								.parse((String) userParamMap.get(sysParam
										.getParamId().toString())));
					}
				}
				list.add(userParam);
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

	@RequestMapping({ "dialog" })
	public ModelAndView dialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Enumeration attrNames = request.getAttributeNames();
		while (attrNames.hasMoreElements()) {
			this.logger.info(attrNames.nextElement().toString());
		}

		ModelAndView mv = getAutoView();

		List sysParamList = this.sysParamService.getUserParam();
		mv.addObject("sysParamList", sysParamList);
		return mv;
	}

	@RequestMapping({ "getByParamKey" })
	public ModelAndView getByParamKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		int postFlag = RequestUtil.getInt(request, "postflag");
		String userParam = RequestUtil.getString(request, "userParam");
		List userList = this.sysUserService.getByUserParam(userParam);
		mv.addObject("userList", userList);
		mv.addObject("postFlag", Integer.valueOf(postFlag));
		return mv;
	}
}
