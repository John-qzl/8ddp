package com.cssrc.ibms.core.resources.product.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.core.resources.product.service.ModulePeriodService;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * @description 型号阶段控制器类
 * @author xie chen
 * @date 2019年11月30日 下午2:05:07
 * @version V1.0
 */
@Controller
@RequestMapping("/module/period/")
public class ModulePeriodController extends BaseController {
	@Resource
	private ModulePeriodService service;

	@RequestMapping("getPeriodAndModule")
    @ResponseBody
    public Map<String, Object> getPeriodAndModule(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> periodModule = new HashMap<>();
        try {
            // 型号阶段id
            String modulePeriodId = request.getParameter("modulePeriodId");
            periodModule = this.service.getPeriodAndModule(modulePeriodId);
            periodModule.put("success", true);
        } catch (Exception e) {
        	periodModule.put("success", false);
        	periodModule.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return periodModule;
    }
	
}
