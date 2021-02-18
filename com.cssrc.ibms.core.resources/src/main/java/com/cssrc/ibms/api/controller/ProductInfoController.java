package com.cssrc.ibms.api.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.mission.dao.TestPlanDao;
import com.cssrc.ibms.core.resources.mission.model.RangeTestPlanMapToBean;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.dp.product.acceptance.bean.ActualData;
import com.cssrc.ibms.dp.product.acceptance.bean.ProductInfo;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceReportDao;
import com.cssrc.ibms.dp.product.acceptance.dao.ActualDataDao;
import com.cssrc.ibms.dp.product.infor.dao.ProductInforDao;
import com.cssrc.ibms.dp.rangeTest.mission.dao.RangeTestSummaryDao;


import net.sf.json.JSONObject;

@RequestMapping("/restful/api")
@Controller
public class ProductInfoController extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Resource
	private ProductInforDao productInforDao;
	@Resource
	private ActualDataDao actualDataDao;
	@Resource
	private IOTableInstanceDao ioTableInstanceDao;
	@Resource
	private AcceptanceReportDao acceptanceReportDao;
	@Resource
	private TestPlanDao testPlanDao;
	@Resource
	private RangeTestSummaryDao rangeTestSummaryDao;
	
	@RequestMapping("/getProductInfo")
	@ResponseBody
	public JSONObject getProductInfo(HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject=new JSONObject();
		 String productId=request.getParameter("productId");
		 if(productId==null) {
			 jsonObject.put("status", "500");
			 jsonObject.put("message", "请输入产品编号!");
			 return jsonObject;
		 }
		 List<ProductInfo> productInfoList=productInforDao.getProudctById(productId);
		 if(productInfoList==null||productInfoList.size()==0) {
			 jsonObject.put("status", "404");
			 jsonObject.put("message", "不存在此产品!");
			 return jsonObject;
		 }
		 Iterator<ProductInfo> it = productInfoList.iterator();
		 while(it.hasNext()){
			 ProductInfo productInfo = it.next();
			 TableInstance tableInstance=ioTableInstanceDao.getById(productInfo.getSsslId());
			 if(tableInstance.getStatus().equals("废弃")) {
				it.remove();
			 }
			 else {
				 List<Map<String, Object>> acceptanceReport=acceptanceReportDao.getAcceptanceReport(productInfo.getPlanId());
				 if(acceptanceReport==null||acceptanceReport.size()==0||!CommonTools.Obj2String(acceptanceReport.get(0).get("F_SPZT")).equals("审批通过")) {
					 it.remove();
				 }
			}
		 }
		 if(productInfoList==null||productInfoList.size()==0) {
			 jsonObject.put("status", "404");
			 jsonObject.put("message", "不存在此产品!");
			 return jsonObject;
		 }
		 List<ActualData> list=new ArrayList<>();
		 for (ProductInfo productInfo : productInfoList) {
			  List<ActualData> actualDatas=actualDataDao.getByPlanId(productInfo.getId());
			  list.addAll(actualDatas);
		}
		 jsonObject.put("status", "200");
		 jsonObject.put("message", "访问成功!");
		 jsonObject.put("data", list);
		return jsonObject;
	}
	@RequestMapping("/getMissileInfo")
	@ResponseBody
	public JSONObject getMissileInfo(HttpServletRequest request, HttpServletResponse response) throws ParseException{
		JSONObject jsonObject=new JSONObject();
		 String missileId=request.getParameter("missileId");
		 if(missileId==null) {
			 jsonObject.put("status", "500");
			 jsonObject.put("message", "请输入导弹编号!");
			 return jsonObject;
		 }
		 List<RangeTestPlanMapToBean>  rangeTestList=testPlanDao.getByMissileId(missileId);
		 if(rangeTestList.size()==0) {
			 jsonObject.put("status", "404");
			 jsonObject.put("message", "不存在此导弹!");
			 return jsonObject;
		 }
		 Iterator<RangeTestPlanMapToBean> it = rangeTestList.iterator();
		 while(it.hasNext()){
			 RangeTestPlanMapToBean rangeTestPlanMapToBean=it.next();
			 Map<String, Object> map=rangeTestSummaryDao.getReportByPlanId(rangeTestPlanMapToBean.getId());
			 if(map==null||rangeTestPlanMapToBean.getChbgbbh().indexOf("WQSJ")<0){
				 it.remove();
			 }
		 }
		 if(rangeTestList.size()==0) {
			 jsonObject.put("status", "404");
			 jsonObject.put("message", "不存在此导弹!");
			 return jsonObject;
		 }
		 List<TableInstance>  tableInstanceList=new ArrayList<>();
		 for (RangeTestPlanMapToBean rangeTestPlanMapToBean : rangeTestList) {
			 tableInstanceList.addAll(ioTableInstanceDao.getCompleByPlanId(rangeTestPlanMapToBean.getId()));
		 }
		 jsonObject.put("status", "200");
		 jsonObject.put("message", "访问成功!");
		 jsonObject.put("data", tableInstanceList);
		 return jsonObject;
	}
	
	@RequestMapping("/getTestInfo")
	@ResponseBody
	public JSONObject getTestInfo(HttpServletRequest request, HttpServletResponse response) throws ParseException{
		JSONObject jsonObject=new JSONObject();
		 String testId=request.getParameter("testId");
		 if(testId==null) {
			 jsonObject.put("status", "500");
			 jsonObject.put("message", "请输入实验编号!");
			 return jsonObject;
		 }
		 List<RangeTestPlanMapToBean>  rangeTestList=testPlanDao.getByMissileId(testId);
		 if(rangeTestList.size()==0) {
			 jsonObject.put("status", "404");
			 jsonObject.put("message", "不存在此实验!");
			 return jsonObject;
		 }
		 Iterator<RangeTestPlanMapToBean> it = rangeTestList.iterator();
		 while(it.hasNext()){
			 RangeTestPlanMapToBean rangeTestPlanMapToBean=it.next();
			 Map<String, Object> map=rangeTestSummaryDao.getReportByPlanId(rangeTestPlanMapToBean.getId());
			 if(map==null||rangeTestPlanMapToBean.getChbgbbh().indexOf("BCSY")<0){
				 it.remove();
			 }
		 }
		 if(rangeTestList.size()==0) {
			 jsonObject.put("status", "404");
			 jsonObject.put("message", "不存在此实验!");
			 return jsonObject;
		 }
		 List<TableInstance>  tableInstanceList=new ArrayList<>();
		 for (RangeTestPlanMapToBean rangeTestPlanMapToBean : rangeTestList) {
			 tableInstanceList.addAll(ioTableInstanceDao.getCompleByPlanId(rangeTestPlanMapToBean.getId()));
		 }
		 jsonObject.put("status", "200");
		 jsonObject.put("message", "访问成功!");
		 jsonObject.put("data", tableInstanceList);
		 return jsonObject;
	}
	
	
}
