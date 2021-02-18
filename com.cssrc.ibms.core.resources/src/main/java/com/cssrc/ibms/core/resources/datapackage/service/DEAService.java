package com.cssrc.ibms.core.resources.datapackage.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.resources.datapackage.dao.DEADao;

@Repository
public class DEAService {
	
	@Resource
	private DEADao deaDao;
	
	/** 根据型号id查询发次所有信息  */
	public List<Map<String, Object>> queryProjectNodeById(String id){
		return deaDao.queryProjectNodeById(id);
	}
	
	public List<Map<String, Object>> query(String productId, String projectId, String args
			, String ckResultId, String slid, String resultTableName) {
		String ckName = "";
		String ckShortName = "";
		String ckDescription = "";
		
		if (!args.equals("")) {
			JSONObject json = JSONObject.fromObject(args);
			ckName = json.getString("ckName");
			ckShortName = json.getString("ckShortName");
			ckDescription = json.getString("ckDescription").replaceAll("%", "\\\\%");
		}
		if (!ckResultId.equals("") && !slid.equals("")) {
			String itemDefId = (String) deaDao.queryItemDefByIdAndResultTableName(ckResultId, resultTableName).get("F_ITEMDEF_ID");
			Map<String, Object> itemInfo = deaDao.queryItemInfoById(itemDefId);
			ckName = (String) (itemInfo.get("F_NAME") == null ? "" : itemInfo.get("F_NAME"));
			ckShortName = (String) (itemInfo.get("F_SHORTNAME") == null ? "" : itemInfo.get("F_SHORTNAME"));
			ckDescription = (String) (itemInfo.get("F_DESCRIPTION") == null ? "" : itemInfo.get("F_DESCRIPTION"));
			
			/*Map<String, Object> map = deaDao.queryDataPackageInfo(slid);
			productId = (String) map.get("F_SSXH");
			projectId = (String) map.get("F_SSFC");*/
		}
		
		List<Map<String, Object>> data = deaDao.query(productId, projectId, ckName, ckShortName, ckDescription,resultTableName);
		
		return data;
	}

	/**
	 * 求平均值（保留三位小数）
	 * @param arr
	 * @return
	 */
	private double getAvg(double[] arr) {
		double sum = 0.0;
		for (double d : arr) {
			sum += d;
		}
		double avg = sum / arr.length;
		//保留三位小数
		avg = new BigDecimal(avg).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		return avg;
	}
	
	/**
	 * 求极差平均（保留三位小数）
	 * @param arr
	 * @return
	 */
	private double getAvgR(double[] arr) {
		double sum = 0.0;
		for (int i = 0; i < arr.length-1; i++) {
			sum += Math.abs(arr[i+1] - arr[i]);
		}
		double avg = sum / (arr.length-1);
		//保留三位小数
		avg = new BigDecimal(avg).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		return avg;
	}
	
	/**
	 * string -> doubleArray
	 * @param arr
	 * @return
	 */
	private double[] strToDoubleArray(String str) {
		String[] strArr = str.split(",");
		double[] data = new double[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			data[i] = Double.parseDouble(strArr[i]);
		}
		return data;
	}
	private double[] strToDoubleArray1(String str) {
		String[] strArr = str.split(",");
		double[] data = new double[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			data[i] = Double.parseDouble(strArr[i].substring(1,strArr[i].length()-1));
		}
		return data;
	}
	/**
	 * 单值控制图
	 * @param succData
	 * @return
	 */
	public Map<String, Object> getIControlChart(String data) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		double[] dataDouble = strToDoubleArray(data);
		//平均值
		double avg = getAvg(dataDouble);
		map.put("avg", avg);
		//极差平均
		double avgR = getAvgR(dataDouble);
		
		//排序
		double k = 2.66;
		Arrays.sort(dataDouble);
		double dataMin = dataDouble[0];
		double dataMax = dataDouble[dataDouble.length-1];
		double upperLimit = Math.min(avg + k*avgR, dataMax);
		map.put("upperLimit", upperLimit);
		double lowerLimit = Math.max(avg - k*avgR, dataMin);
		map.put("lowerLimit", lowerLimit);
		
		return map;
	}
	
	/**
	 * 均值控制图
	 * @param succData
	 * @return
	 */
	public Map<String, Object> getXControlChart(String succData, String analyData) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (!succData.equals("")) {
			JSONObject succDataJson = JSONObject.fromObject(succData);
			//成功数据
			Map<String, Object> succMap = getRange(succDataJson);
			
			//极差平均
			double avgR = Double.parseDouble(succMap.get("avgR").toString());
			//平均值平均
			double avgAvg = Double.parseDouble(succMap.get("avgAvg").toString());
			//极差
			map.put("rByGroupSuc", succMap.get("rByGroup"));
			map.put("avgR", avgR);
			//平均值
			map.put("avgByGroupSuc", succMap.get("avgByGroup"));
			map.put("avgAvg", avgAvg);
			
			//极差包络范围
			/*double upperRB = D4 * avgR;
			map.put("upperRB", upperRB);
			double lowerRL = D3 * avgR;
			map.put("lowerRL", lowerRL);*/
			
			//均值包络范围
			/*double upperUB = avgAvg + A2 * avgR;
			map.put("upperUB", upperUB);
			double lowerUL = avgAvg - A2 * avgR;
			map.put("lowerUL", lowerUL);*/
		}
		
		if (!analyData.equals("")) {
			JSONObject analyDataJson = JSONObject.fromObject(analyData);
			//待分析数据
			Map<String, Object> analyMap = getRange(analyDataJson);
			//极差
			map.put("rByGroupAnaly", analyMap.get("rByGroup"));
			//平均值
			map.put("avgByGroupAnaly", analyMap.get("avgByGroup"));
		}
		
		return map;
	}
	
	//获得极差数组,极差平均值,平均值,平均值平均
	private Map<String, Object> getRange(JSONObject obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		double[] rByGroup = new double[obj.size()];
		double[] avgByGroup = new double[obj.size()];
		
		if (obj.size() > 0) {
			for (int i = 0; i < obj.size(); i++) {
				//每个子组数据
				String group = obj.getString(i + "").substring(1, obj.getString(i + "").length()-1);
				//类型转换
				double[] data = strToDoubleArray1(group);
				//平均值
				double avg = getAvg(data);
				avgByGroup[i] = avg;
				rByGroup[i] = data[data.length-1] - data[0];
			}
			
			//极差平均
			double avgR = getAvg(rByGroup);
			//平均值平均
			double avgAvg = getAvg(avgByGroup);
			
			map.put("rByGroup", doubleArrayToStr(rByGroup));
			map.put("avgR", avgR);
			map.put("avgByGroup", doubleArrayToStr(avgByGroup));
			map.put("avgAvg", avgAvg);
		}
		return map;
	}



	private String doubleArrayToStr(double[] dataR) {
		String string = "";
		for (int i = 0; i < dataR.length; i++) {
			string += dataR[i] + ",";
		}
		return string.substring(0, string.length()-1);
	}
	
	/**
	 * 正态校验
	 * @param data
	 * @return
	 */
	/*public boolean isNormalDistribution(String data) {
		boolean flag = false;
		double[] dataDouble = strToDoubleArray(data);
		//平均值
		double avg = getAvg(dataDouble);
		//样本二阶中心矩
		double sum = 0.0;
		for (int i = 0; i < dataDouble.length-1; i++) {
			sum += Math.pow(dataDouble[i]-avg, 2);
		}
		double m2 = sum / dataDouble.length;
		//保留六位小数
		m2 = new BigDecimal(m2).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
		//A
		double aSum = 0.0;
		for (int i = 0; i < dataDouble.length-1; i++) {
			aSum += Math.exp(-Math.pow(dataDouble[i]-avg, 2)/4*m2);
		}
		double a = Math.sqrt(2) * aSum;
		//B
		double bSum = 0.0;
		for (int k = 2; k < dataDouble.length; k++) {
			for (int j = 0; j < k-1; j++) {
				bSum += Math.exp(-Math.pow(dataDouble[j]-dataDouble[k-1], 2)/2*m2);
			}
		}
		double b = 2 / dataDouble.length * bSum;
		//统计量
		double tep = 1 + dataDouble.length/Math.sqrt(3) + b - a;
		//保留四位小数
		tep = new BigDecimal(tep).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		//与n对应的p分位数比较（默认显著性水平=0.05）-查表
		double p = 1;
		if (tep > p) {
			flag = false; // 拒绝零假设
		} else {
			flag = true; // 符合正态分布
		}
		return flag;
	}*/
}
