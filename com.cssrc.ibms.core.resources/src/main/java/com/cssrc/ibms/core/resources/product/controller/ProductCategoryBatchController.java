package com.cssrc.ibms.core.resources.product.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.resources.product.service.ProductCategoryBatchService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.product.acceptance.service.AcceptancePlanService;
import com.cssrc.ibms.system.service.SerialNumberService;

/**
 * @description 产品类别/批次控制器类
 * @author xie chen
 * @date 2019年11月29日 下午2:22:01
 * @version V1.0
 */
@Controller
@RequestMapping("/product/category/batch/")
public class ProductCategoryBatchController extends BaseController {
	
	@Resource
	private ProductCategoryBatchService service;
	@Resource
	private AcceptancePlanService acceptancePlanService;
	@Resource
	private SerialNumberService serialNumberService;
	@RequestMapping("getBatchAndModule")
    @ResponseBody
    public Map<String, Object> getBatchAndModule(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> batchModule = new HashMap<>();
        try {
            // 产品批次id
            String batchId = request.getParameter("batchId");
            batchModule = this.service.getBatchAndModule(batchId);
            String number=this.service.getPlanNumber(batchModule.get("F_SSXH").toString());
            batchModule.put("success", true);
            batchModule.put("Number",number) ;
        } catch (Exception e) {
            batchModule.put("success", false);
            batchModule.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return batchModule;
    }
	
	@RequestMapping("categoryImportView")
    public ModelAndView categoryImportView(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("/product/category/productCategoryImport.jsp").addObject("moduleId", request.getParameter("moduleId"));
    }
	
	
	@RequestMapping("importTemplate")
    public ModelAndView importTemplate(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("/product/category/templateImport.jsp");
    }
	
	
	
	@RequestMapping("importCategories")
    @Action(description = "导入产品类别", detail = "导入产品类别")
    public void importCategories(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
        MultipartFile file = request.getFile("file");
        String moduleId = request.getParameter("moduleId");
        ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, "导入成功");
        try {
            resultMessage = service.importCategories(file,moduleId);
        } catch (Exception e) {
            resultMessage = new ResultMessage(ResultMessage.Fail, "导入失败：\r\n"+e.getMessage());
            System.err.println("--------------------by xiechen-----------------导入失败的错误信息为:"
                    +e.getCause().getMessage()+ "," + "当前输出的所在类为:ProductCategoryBatchController.importCategories()");
            writeResultMessage(response.getWriter(), resultMessage);
        }
        writeResultMessage(response.getWriter(), resultMessage);
    }
	
	@RequestMapping("getMapById")
    @ResponseBody
    public Map<String, Object> getMapById(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 产品类别id
            String categoryId = request.getParameter("categoryId");
            map = service.getById(categoryId);
            map.put("success", true);
        } catch (Exception e) {
        	map.put("success", false);
        	map.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
	
	@RequestMapping("formAssignCheck")
    @ResponseBody
    public Map<String, Object> formAssignCheck(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 产品批次id
            String batchId = request.getParameter("batchId");
            map = service.formAssignCheck(batchId);
        } catch (Exception e) {
        	map.put("success", false);
        	map.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
	
	@RequestMapping("dataReturnViewCheck")
    @ResponseBody
    public Map<String, Object> dataReturnViewCheck(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 产品批次id
            String batchId = request.getParameter("batchId");
            map = service.dataReturnViewCheck(batchId);
        } catch (Exception e) {
        	map.put("success", false);
        	map.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return map;
    }
	@RequestMapping("getBathByModuleId")
	@ResponseBody
	public List<Map<String, Object>> getBathByModuleId(HttpServletRequest request, HttpServletResponse response){
		String moduleId = request.getParameter("moduleId");
		List<Map<String, Object>> list=new ArrayList<>();
		if(moduleId.equals("")||moduleId==null) {
			list=service.getAllBatch();
		}
		else {
			list=service.getCategoriesByModuleId(moduleId);
		}
		
		return list;
		
	}
	
	@RequestMapping("getBathByModuleIds")
	@ResponseBody
	public List<Map<String, Object>> getBathByModuleIds(HttpServletRequest request, HttpServletResponse response){
		String moduleId = request.getParameter("moduleId");
		List<Map<String, Object>> list=new ArrayList<>();
		if(moduleId!=null&&!moduleId.equals("")) {
			String moduleIds=moduleId.substring(0,moduleId.length()-1);
			list=service.getCategoriesByModuleIds(moduleIds);
		}
		return list;
		
	}
	@RequestMapping("getBathBycategoryId")
	@ResponseBody
	public List<Map<String, Object>> getBathBycategoryId(HttpServletRequest request, HttpServletResponse response){
		String categoryId = request.getParameter("categoryId");
		List<Map<String, Object>> list=new ArrayList<>();
		list=service.getBatchesByCategoryId(categoryId);
		return list;
		
	}
	@RequestMapping("getBathBycategoryIds")
	@ResponseBody
	public List<Map<String, Object>> getBathBycategoryIds(HttpServletRequest request, HttpServletResponse response){
		String categoryIds = request.getParameter("categoryIds");
		List<Map<String, Object>> list=new ArrayList<>();
		if(categoryIds!=null&&!categoryIds.equals("")) {
			categoryIds=categoryIds.substring(0,categoryIds.length()-1);
			list=service.getBatchesByCategoryIds(categoryIds);
		}
		return list;
		
	}
}
