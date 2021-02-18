package com.cssrc.ibms.index.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.json.JsonPageResult;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsColType;
import com.cssrc.ibms.index.service.InsColTypeService;

/**
 * 栏目类型Controller层
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/portal/insColType/" })
public class InsColTypeController extends BaseController {
	@Resource
	private InsColTypeService insColTypeService;

	@RequestMapping({ "del" })
	@ResponseBody
	public ResultMessage del(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String uId = request.getParameter("ids");
		if (StringUtils.isNotEmpty(uId)) {
			String[] ids = uId.split(",");
			for (String id : ids) {
				this.insColTypeService.delById(Long.valueOf(id));
			}
		}
		ResultMessage message = new ResultMessage(1, "成功删除！");
		return message;
		/*addMessage(message, request);*/
	}

	@RequestMapping({ "get" })
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pkId = request.getParameter("pkId");
		InsColType insColType = null;
		if (StringUtils.isNotEmpty(pkId))
			insColType = (InsColType) this.insColTypeService.getById(Long.valueOf(pkId));
		else {
			insColType = new InsColType();
		}
		return getAutoView().addObject("insColType", insColType);
	}

	@RequestMapping({ "getAll" })
	@ResponseBody
	public List<InsColType> getAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<InsColType> insColTypes = this.insColTypeService.getAll();
		return insColTypes;
	}
	
	 
	 @RequestMapping({"listData"})
	 public void listData(HttpServletRequest request, HttpServletResponse response) throws Exception
	 {		 
		 response.setContentType("application/json");
		 QueryFilter queryFilter = new QueryFilter(request);
		 List<InsColType> list = insColTypeService.getAll(queryFilter);
		 JsonPageResult result = new JsonPageResult(list, queryFilter.getPagingBean().getTotalCount());
		 ObjectMapper mapper=new ObjectMapper();
		 String jsonResult = mapper.writeValueAsString(result);
		 PrintWriter pw = response.getWriter();
		 pw.println(jsonResult);
		 pw.close();

	 }

	@RequestMapping({ "edit" })
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pkId = request.getParameter("pkId");

		String forCopy = request.getParameter("forCopy");
		InsColType insColType = null;
		if (StringUtils.isNotEmpty(pkId)) {
			insColType = (InsColType) this.insColTypeService.getById(Long.valueOf(pkId));
			if ("true".equals(forCopy))
				insColType.setTypeId(null);
		} else {
			insColType = new InsColType();
		}
		return getAutoView().addObject("insColType", insColType);
	}

}
