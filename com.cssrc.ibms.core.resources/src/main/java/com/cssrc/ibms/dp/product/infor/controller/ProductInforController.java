package com.cssrc.ibms.dp.product.infor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.core.form.dao.DataTemplateDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.product.infor.service.ProductInforService;

import net.sf.json.JSONArray;

/**
 * @description 产品信息控制器
 * @author xie chen
 * @date 2019年11月22日 下午4:01:38
 * @version V1.0
 */
@Controller
@RequestMapping("/product/infor/")
public class ProductInforController extends BaseController {
	@Resource
	private ProductInforService productInforService;
	@Resource
	private DataTemplateDao dataTemplateDao;
	
	@RequestMapping({ "getTreeData" })
	@ResponseBody
	@Action(description = "获取产品验收策划-产品结构树")
	public void getTreeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String productBatchId = request.getParameter("batchId");
		String productBatchKey = request.getParameter("batchKey");
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		
		List<String> planProductTree = new ArrayList<String>();
		// 1）产品批次基本信息
		String productBatchUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021200168\"";
		// 2）产品-验收策划
		String acceptancePlanUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021790753\"";
		// 验收策划下的产品
		List<Map<String, Object>> products = productInforService.getByAcceptancePlanId(acceptancePlanId);
		for (Map<String, Object> product : products) {
			String productNode = "{id:" + product.get("ID") + ",parentId:" + acceptancePlanId
			+ ", name:\"" + product.get("F_CPBH") + "\", type:\"product\""
			+ ", target : \"listFrame\",open:true}";
			planProductTree.add(productNode);
		}
		response.getWriter().print(JSONArray.fromObject(planProductTree).toString());
	}

}
