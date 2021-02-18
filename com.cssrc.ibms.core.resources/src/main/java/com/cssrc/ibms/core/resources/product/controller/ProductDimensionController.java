package com.cssrc.ibms.core.resources.product.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.core.form.dao.DataTemplateDao;
import com.cssrc.ibms.core.form.model.DataTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cssrc.ibms.core.resources.product.service.ProductCategoryBatchService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptancePlanDao;
import com.cssrc.ibms.dp.product.infor.service.ProductInforService;

import net.sf.json.JSONArray;

/**
 * @description 产品维度结构树控制器
 * @author xie chen
 * @date 2019年11月21日 下午7:05:59
 * @version V1.0
 */
@Controller
@RequestMapping("/product/dimension/tree/")
public class ProductDimensionController extends BaseController {
	@Resource
	private ProductCategoryBatchService productCategoryBatchService;
	@Resource
	private ProductInforService productInforService;
	@Resource
	private AcceptancePlanDao acceptancePlanDao;
	@Resource
	private DataTemplateDao dataTemplateDao;

 	@RequestMapping({ "getTreeData" })
	@Action(description = "获取产品维度结构树")
	public void getTreeData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 型号id、型号名称
		String moduleId = request.getParameter("moduleId");
		String moduleName = java.net.URLDecoder.decode(request.getParameter("moduleName"), "UTF-8");
		// 产品维度结构树
		List<String> productDimensionTree = new ArrayList<String>();

		// 1）型号基本信息
		String moduleUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021170075\"";
		String moduleHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021190185\"";
		// 2）产品类别基本信息
		String productCategoryUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021190185\"";
		String productCategoryBuilderUrl = "\"/dataPackage/tree/productCategory/builder.do?\"";
		String productCategoryHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021200168\"";
		// 3）产品批次基本信息
		String productBatchUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021200168\"";
		String productBatchBuilderUrl = "\"/dataPackage/tree/productBatch/builder.do?\"";
		String productBatchHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021200549\"";
		// 4）产品验收策划
		String acceptancePlanBuilderUrl="\"/dataPackage/tree/productAcceptancePlan/builder.do?\"";
		// 5）产品-数据包url
		String dataPackageUrl = "\"/dataPackage/tree/dataPackageShow/main.do?\"";
		String productUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021200549\"";
		//6)产品策划-url
		String productmoduleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021341344\"";
		String tempmoduleHandleUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021341344\"";
		
		//FY 去除产品维度型号
//		// 1.添加根节点--当前型号节点
//		String rootNode = "{id:" + moduleId + ", parentId:-1" + ", name:\"" + moduleName
//				+ "\" , type: \"module\" , tempUrl:" + moduleUrl + ", handleUrl:" + moduleHandleUrl + ", target : \"dataPackageFrame\",open:true}";
//		productDimensionTree.add(rootNode);
		
		// 2.获取产品类别节点
		List<Map<String, Object>> productCategories = productCategoryBatchService.getCategoriesByModuleId(moduleId);
		for (Map<String, Object> categoryMap : productCategories) {
			String categoryId = CommonTools.Obj2String(categoryMap.get("ID"));
			String categoryName = CommonTools.Obj2String(categoryMap.get("F_CPMC"));
			if (StringUtil.isNotEmpty(categoryId)) {
				String productCategoryNode = "{id:" + categoryId + ", parentId:" + moduleId + ", name:\"" + categoryMap.get("F_CPDH") + "," + categoryMap.get("F_CPMC")
				+ "\" , type: \"category\" , tempUrl:" + productCategoryUrl + ", handleUrl:" + productCategoryHandleUrl + ", categoryBuilderUrl:" + productCategoryBuilderUrl + ", moduleId:" + moduleId +  ", target : \"dataPackageFrame\",open:true}";
				productDimensionTree.add(productCategoryNode);
				
				// 3.获取产品批次节点
				List<Map<String, Object>> productBatches = productCategoryBatchService.getBatchesByCategoryId(categoryMap.get("ID").toString());
				for (Map<String, Object> batchMap : productBatches) {
					String batchId = CommonTools.Obj2String(batchMap.get("ID"));
					String batchKey = CommonTools.Obj2String(batchMap.get("F_PCH"));
					String productBatchNode = "{id:" + batchId + ", parentId:" + batchMap.get("F_SSCPLB") + ", name:\"" + batchKey
					+ "\" , type: \"batch\" , tempUrl:" + productBatchUrl + ", handleUrl:" + productBatchHandleUrl + ", batchBuilderUrl:" + productBatchBuilderUrl + ", moduleId:" + moduleId + ", categoryId:" + categoryId + ", target : \"dataPackageFrame\",open:true}";
					productDimensionTree.add(productBatchNode);
					
					// 4.获取产品验收策划节点
					List<Map<String, Object>> acceptancePlans = acceptancePlanDao.getApprovedPlansByProductBatchId(batchId);
					for (Map<String, Object> planMap : acceptancePlans) {
						// 策划报告表编号
						String planCode = planMap.get("F_CHBGBBH") + "";
						String planNode = "{id:" + planMap.get("ID") +", tempUrl:" + tempmoduleHandleUrl + ", handleUrl:" + productmoduleUrl + ", parentId:" + planMap.get("F_SSCPPC") + ", name:\"" + planCode
								+ "\" , type: \"acceptancePlan\" , acceptancePlanBuilderUrl:" + acceptancePlanBuilderUrl + ", moduleId:" + moduleId + ", moduleName:\"" + moduleName + "\", categoryId:" + categoryId+ ", categoryName:\"" + categoryName
								+ "\", batchId:" + batchId + ", batchKey:\"" + batchKey + "\", target : \"dataPackageFrame\",open:true}";
						productDimensionTree.add(planNode);
					}
					
					// 5.获取产品-（数据包明细属于产品）（暂时废弃）
					List<Map<String, Object>> productInfors = productInforService.getByProductBatchId(batchId);
					for (Map<String, Object> productMap : productInfors) {
						String productName = productMap.get("F_CPBH") + "," + productMap.get("F_CPMC");
						String productNode = "{id:" + productMap.get("ID") + ", parentId:" + productMap.get("F_SSCPPC") + ", name:\"" + productName
						+ "\" , type: \"product\" , tempUrl:" + dataPackageUrl + ", handleUrl:" + productUrl + ", moduleId:" + moduleId + ", moduleName:\"" + moduleName + "\", categoryId:" + categoryId+ ", categoryName:\"" + categoryName
						+ "\", batchId:" + batchId + ", batchKey:\"" + batchKey + "\", target : \"dataPackageFrame\",open:true}";
						/* 去除产品层级--移动至pad端*/
						// productDimensionTree.add(productNode);
					}
					
				}
			}
		}
		response.getWriter().print(JSONArray.fromObject(productDimensionTree).toString());
	}

}
