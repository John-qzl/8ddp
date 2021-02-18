package com.cssrc.ibms.core.resources.product.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.core.form.dao.DataTemplateDao;
import com.cssrc.ibms.core.form.service.DataTemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cssrc.ibms.core.resources.product.service.ModuleBatchService;
import com.cssrc.ibms.core.resources.product.service.ModuleDispatchService;
import com.cssrc.ibms.core.resources.product.service.ModulePeriodService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

import net.sf.json.JSONArray;

/**
 * @description 型号维度结构树控制器
 * @author xie chen
 * @date 2019年11月29日 下午6:03:09
 * @version V1.0
 */
@Controller
@RequestMapping("/module/dimension/tree/")
public class ModuleDimensionController extends BaseController {
	@Resource
	private ModulePeriodService modulePeriodService;
	@Resource
	private ModuleBatchService moduleBatchService;
	@Resource
	private ModuleDispatchService moduleDispatchService;
	@Resource
	private DataTemplateService dataTemplateService;

	@RequestMapping({ "getTreeData" })
	@Action(description = "获取产品维度结构树")
	public void getTreeData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 型号id、型号名称
		String moduleId = request.getParameter("moduleId");
		String moduleName = java.net.URLDecoder.decode(request.getParameter("moduleName"), "UTF-8");
		// 型号维度结构树
		List<String> moduleDimensionTree = new ArrayList<String>();

		// 1）型号基本信息
		String moduleUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021170075\"";
		String moduleHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021350571\"";
		// 2）型号阶段信息
		String modulePeriodUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021350571\"";
		String modulePeriodHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021350608\"";
		// 3）型号批次信息
		String moduleBatchUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021350608\"";
		String moduleBatchHandleUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021350634\"";
		// 4）型号发次信息
		String moduleDispatchUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021350634\"";
		// 5）产品类别
		// 6）产品
		
		// 1.添加根节点--当前型号节点
		String rootNode = "{id:" + moduleId + ", parentId:-1" + ", name:\"" + moduleName
				+ "\" , type: \"module\" , tempUrl:" + moduleUrl + ", handleUrl:" + moduleHandleUrl + ", target : \"dataPackageFrame\",open:true}";
		moduleDimensionTree.add(rootNode);
		
		// 2.获取型号阶段节点
		List<Map<String, Object>> modulePeriods = modulePeriodService.getPeriodsByModuleId(moduleId);
		for (Map<String, Object> periodMap : modulePeriods) {
			String modulePeriodId = CommonTools.Obj2String(periodMap.get("ID"));
			if (StringUtil.isNotEmpty(modulePeriodId)) {
				String modulePeriodNode = "{id:" + modulePeriodId + ", parentId:" + moduleId + ", name:\"" + periodMap.get("F_XHJD")
				+ "\" , type: \"modulePeriod\" , tempUrl:" + modulePeriodUrl + ", handleUrl:" + modulePeriodHandleUrl + ", moduleId:" + moduleId +  ", target : \"dataPackageFrame\",open:true}";
				moduleDimensionTree.add(modulePeriodNode);
				
				// 3.获取型号批次节点
				List<Map<String, Object>> moduleBatches = moduleBatchService.getBatchesByModulePeriodId(modulePeriodId);
				for (Map<String, Object> batchMap : moduleBatches) {
					String moduleBatchId = CommonTools.Obj2String(batchMap.get("ID"));
					String moduleBatchKey = CommonTools.Obj2String(batchMap.get("F_PCDH"));
					String moduleBatchNode = "{id:" + moduleBatchId + ", parentId:" + batchMap.get("F_SSXHJD") + ", name:\"" + moduleBatchKey
					+ "\" , type: \"moduleBatch\" , tempUrl:" + moduleBatchUrl + ", handleUrl:" + moduleBatchHandleUrl + ", moduleId:" + moduleId + ", modulePeriodId:" + modulePeriodId + ", target : \"dataPackageFrame\",open:true}";
					moduleDimensionTree.add(moduleBatchNode);
					
					// 4.获取型号发次节点
					List<Map<String, Object>> moduleDispatches = moduleDispatchService.getDispatchesByModuleBatchId(moduleBatchId);
					for (Map<String, Object> dispatchMap : moduleDispatches) {
						String moduleDispatchId = CommonTools.Obj2String(dispatchMap.get("ID"));
						String moduleDispatchName = CommonTools.Obj2String(dispatchMap.get("F_FCBH"));
						String moduleDispatchNode = "{id:" + moduleDispatchId + ", parentId:" + dispatchMap.get("F_SSXHPC") + ", name:\"" + moduleDispatchName
						+ "\" , type: \"moduleDispatch\" , tempUrl:" + moduleDispatchUrl + ", handleUrl:" + moduleDispatchUrl + ", moduleId:" + moduleId + ", moduleName:\"" + moduleName + "\", modulePeriodId:" + modulePeriodId
						+ ", moduleBatchId:" + moduleBatchId + ", moduleBatchKey:\"" + moduleBatchKey + "\", target : \"dataPackageFrame\",open:true}";
						moduleDimensionTree.add(moduleDispatchNode);
						
						// 5.获取产品类别节点
					}
				}
			}
		}
		response.getWriter().print(JSONArray.fromObject(moduleDimensionTree).toString());
	}

}
