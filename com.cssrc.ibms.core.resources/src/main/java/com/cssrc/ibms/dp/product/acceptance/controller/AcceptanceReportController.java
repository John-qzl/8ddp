package com.cssrc.ibms.dp.product.acceptance.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.dp.product.acceptance.service.AcceptanceReportService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.core.resources.io.bean.FileData;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.dao.FileDataDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.product.dao.ProductDao;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptancePlanDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceReportDao;

@Controller
@RequestMapping("/product/acceptance/report/")
public class AcceptanceReportController extends BaseController{
	@Resource
	AcceptanceReportDao acceptanceReportDao;
	@Resource
	AcceptancePlanDao acceptancePlanDao;
	@Resource
	ProductDao productDao;
	@Resource
	FileDataDao fileDataDao;
	@Resource
	IOTableInstanceDao iOTableInstanceDao;
	@Autowired
	AcceptanceReportService acceptanceReportService;
	
	
	/**
	 * @Desc 更新验收报告审批状态
	 * @param request
	 * @param response
	 */
	@RequestMapping("updateApproveStatus")
    public void updateApproveStatus(HttpServletRequest request, HttpServletResponse response) {
        String acceptanceReportId = request.getParameter("acceptanceReportId");
        String status = request.getParameter("status");
        acceptanceReportDao.updataStatus(acceptanceReportId, status);
    }
	/**
	 * @Desc 查看当前策划下面是否有策划报告
	 * @param request
	 * @param response
	 */
	@RequestMapping("getAcceptanceReport")
	@ResponseBody
    public String getAcceptanceReport(HttpServletRequest request, HttpServletResponse response) {
        String acceptancePlanId = request.getParameter("acceptancePlanId");
        List<Map<String, Object>> list=acceptanceReportDao.getAcceptanceReport(acceptancePlanId);
        if(list!=null&&list.size()>0) {
        	return "0";
        }
       
        List<TableInstance> instanceList=iOTableInstanceDao.getByPlanId(acceptancePlanId);
        for (TableInstance tableInstance : instanceList) {
			if(tableInstance.getName().indexOf("报告")>=0) {
				return "1";
			}
		}
        return "2";
    }
	/**
	 * @Desc 更新验收报告
	 * @param request
	 * @param response
	 */
	@RequestMapping("updateReport")
	@ResponseBody
    public void updateReport(HttpServletRequest request, HttpServletResponse response) {
        String acceptanceReportId = request.getParameter("acceptanceReportId");
        Map<String, Object> acceptanceReport=acceptancePlanDao.getReportById(acceptanceReportId);
        String planId=acceptanceReport.get("F_PLANID").toString();
        String productNames=productDao.getCpMc(planId);
        String productNumber=productDao.getCpNumber(planId);
        List<FileData> fileDataList=fileDataDao.getByPlanId(planId);
        for (FileData fileData : fileDataList) {
			if(fileData.getSjlb().equals("2")||fileData.getSjlb().equals("3")||fileData.getSjlb().equals("4")) {
				TableInstance tableInstance=iOTableInstanceDao.getById(fileData.getSjId());
				if(tableInstance!=null&&tableInstance.getStatus().equals("废弃")) {
					fileData.setPlanId("0");
					fileDataDao.update(fileData);
				}
			}
		}
        acceptanceReportDao.updateReportProduct(acceptanceReportId, productNames, productNumber);
    }

	/**
	 * 获取下一个总结编号的尾号
	 * @param request
	 * @param response
	 * @throws IOException
	 */
    @RequestMapping("getNextNumber")
    public void getNextNumber(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String xhId=request.getParameter("xhId");
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
		String time=dateFormat.format(calendar.getTime());
		String nextNumber="[-YSBG-"+time+"-"+acceptanceReportService.generatorReportNumber(xhId)+"]";
		nextNumber=JSONArray.fromObject(nextNumber).toString();
		response.getWriter().print(nextNumber);
	}
}
