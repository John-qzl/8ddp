package com.cssrc.ibms.core.resources.product.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.core.resources.product.service.ModuleBatchService;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * @description 型号批次控制器类
 * @author xie chen
 * @date 2019年11月30日 下午11:53:27
 * @version V1.0
 */
@Controller
@RequestMapping("/module/batch/")
public class ModuleBatchController extends BaseController {
	@Resource
	private ModuleBatchService service;

	@RequestMapping("getBatchPeriodModule")
    @ResponseBody
    public Map<String, Object> getBatchPeriodModule(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> batchPeriodModule = new HashMap<>();
        try {
            // 型号批次id
            String moduleBatchId = request.getParameter("moduleBatchId");
            batchPeriodModule = this.service.getBatchPeriodModule(moduleBatchId);
            batchPeriodModule.put("success", true);
        } catch (Exception e) {
        	batchPeriodModule.put("success", false);
        	batchPeriodModule.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return batchPeriodModule;
    }
	
}
