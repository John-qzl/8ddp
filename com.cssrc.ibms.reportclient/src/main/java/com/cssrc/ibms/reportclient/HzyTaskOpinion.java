package com.cssrc.ibms.reportclient;

import java.util.ResourceBundle;

import com.cssrc.ibms.util.http.HttpClientUtil;
import com.fr.script.AbstractFunction;
import com.fr.third.org.hsqldb.lib.StringUtil;

/**
 * 任务表操作类.
 *
 * <p>detailed comment</p>
 * @author [创建人]  WeiLei <br/> 
 * 		   [创建时间] 2017年2月15日 上午9:31:18 <br/> 
 * 		   [修改人]  WeiLei <br/>
 * 		   [修改时间] 2017年2月15日 上午9:31:18
 * @see
 */
@SuppressWarnings("serial")
public class HzyTaskOpinion extends AbstractFunction {

	@Override
	public Object run(Object[] params) {
		
		String operationType = params[0].toString();
		if("getSignUserName".equals(operationType)){
			return getSignUserName(params);
		}else if("getTaskApproval".equals(operationType)){
			return getTaskApproval(params);
		}else if("getTaskIdByRoleName".equals(operationType)){
			return getTaskIdByRoleName(params);
		}else if("getTaskOptionByTaskId".equals(operationType)){
			return getTaskOptionByTaskId(params);
		}else if("getSignLeaderOption".equals(operationType)){
			return getSignLeaderOption(params);
		}else if("getProcessInstanceInfo".equals(operationType)){
			return getProcessInstanceInfo(params);
		}else if("getTechProfTaskId".equals(operationType)){
			return getTechProfTaskId(params);
		}else if("moneyChange".equals(operationType)){
			return moneyChange(params);
		}else{
			return null;
		}
	}
	
	/**
	 * 获取IBMS服务地址.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人]  WeiLei <br/> 
	 * 		   [创建时间] 2017年2月15日 上午9:38:36 <br/> 
	 * 		   [修改人]  WeiLei <br/>
	 * 		   [修改时间] 2017年2月15日 上午9:38:36
	 * @see
	 */
	private String getIBMSServer(String appclient, String port, String contextPath){
		
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ibms-server");
        String ibmsServer = resourceBundle.getString(appclient+".server.url");
        
//        SessionIDInfor info = SessionDealWith.getSessionIDInfor(sessionID);
//        String ip = info.getRemoteAddress();
//        String ibmsServer = "http://"+ip+":"+port+"/"+contextPath;
		return ibmsServer;
	}
	
	/**
     * 获取会签节点中审批人员名称.
     *
     * <p>detailed comment</p>
     * @author [创建人]  WeiLei <br/> 
     * 		   [创建时间] 2017年2月15日 上午8:45:34 <br/> 
     * 		   [修改人]  WeiLei <br/>
     * 		   [修改时间] 2017年2月15日 上午8:45:34
     * @param params
     * @see
     */
	private String getSignUserName(Object[] params){
    	
    	String signUserName = "";
    	try {
    		String dataKey = params[1].toString();
    		String flowKey = params[2].toString();
    		String taskId  = params[3].toString();
    		String appClient=params[4].toString();
            String ibmsServer = getIBMSServer(appClient, "", "");
    		String url = ibmsServer + "/taskOperation?operationType=getSignUserName"
    				   + "&dataKey=" + dataKey + "&flowKey=" + flowKey + "&taskId=" + taskId;
    		signUserName = HttpClientUtil.httpGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return signUserName;
    }
	
	/**
     * 获取流程常用语信息.
     *
     * <p>detailed comment</p>
	 * @author [创建人]  WeiLei <br/> 
	 * 		   [创建时间] 2017年2月15日 上午9:43:18 <br/> 
	 * 		   [修改人]  WeiLei <br/>
	 * 		   [修改时间] 2017年2月15日 上午9:43:18
     * @param params
     * @see
     */
	private String getTaskApproval(Object[] params){
		
		String taskApproval = "";
    	try {
    		String dataKey = params[1].toString();
    		String flowKey = params[2].toString();
    		String taskKey = params[3].toString();
    		String yesCode = params[4].toString();
    		String noCode  = params[5].toString();
    		String roleName= params[6].toString();
    		String appClient=params[7].toString();
            String ibmsServer = getIBMSServer(appClient, "", "");
    		String url = ibmsServer + "/taskOperation?operationType=getTaskApproval"
    				   + "&dataKey=" + dataKey + "&flowKey=" + flowKey 
    				   + "&taskKey=" + taskKey + "&yesCode=" + yesCode 
    				   + "&noCode=" + noCode + "&roleName=" + roleName;
    		taskApproval = HttpClientUtil.httpGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return taskApproval;
	}
	
	/**
     * 获取指定角色参与的任务ID集合.
     *
     * <p>detailed comment</p>
	 * @author [创建人]  WeiLei <br/> 
	 * 		   [创建时间] 2017年2月15日 上午9:43:18 <br/> 
	 * 		   [修改人]  WeiLei <br/>
	 * 		   [修改时间] 2017年2月15日 上午9:43:18
     * @param params
     * @see
     */
	private String getTaskIdByRoleName(Object[] params){
		
		String taskIds = "";
    	try {
    		String dataKey = params[1].toString();
    		String flowKey = params[2].toString();
    		String taskKey = params[3].toString();
    		String roleName= params[4].toString();
    		String keyName = params[5].toString();
    		String appClient=params[6].toString();
            String ibmsServer = getIBMSServer(appClient, "", "");
    		String url = ibmsServer + "/taskOperation?operationType=getTaskIdByRoleName"
    				   + "&dataKey=" + dataKey + "&flowKey=" + flowKey 
    				   + "&taskKey=" + taskKey + "&roleName=" + roleName
    				   + "&keyName=" + keyName ;
    		taskIds = HttpClientUtil.httpGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return taskIds;
	}
	
	/**
     * 获取技术专家审批任务信息.
     *
     * <p>detailed comment</p>
	 * @author [创建人]  WeiLei <br/> 
	 * 		   [创建时间] 2017年2月15日 上午9:43:18 <br/> 
	 * 		   [修改人]  WeiLei <br/>
	 * 		   [修改时间] 2017年2月15日 上午9:43:18
     * @param params
     * @see
     */
	private String getTechProfTaskId(Object[] params){
		
		String taskIds = "";
    	try {
    		String dataKey = params[1].toString();
    		String flowKey = params[2].toString();
    		String taskKey = params[3].toString();
    		String keyName= params[4].toString();
    		String appClient=params[5].toString();
            String ibmsServer = getIBMSServer(appClient, "", "");
    		String url = ibmsServer + "/taskOperation?operationType=getTechProfTaskId"
    				   + "&dataKey=" + dataKey + "&flowKey=" + flowKey 
    				   + "&taskKey=" + taskKey + "&keyName=" + keyName;
    		taskIds = HttpClientUtil.httpGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return taskIds;
	}
	
	/**
     * 根据任务ID获取审批任务.
     *
     * <p>detailed comment</p>
	 * @author [创建人]  WeiLei <br/> 
	 * 		   [创建时间] 2017年2月15日 上午9:43:18 <br/> 
	 * 		   [修改人]  WeiLei <br/>
	 * 		   [修改时间] 2017年2月15日 上午9:43:18
     * @param params
     * @see
     */
	private String getTaskOptionByTaskId(Object[] params){
		
		String taskOption = "";
    	try {
    		String taskId = params[1].toString().trim();
    		String appClient=params[2].toString();
            String ibmsServer = getIBMSServer(appClient, "", "");
    		String url = ibmsServer + "/taskOperation?operationType=getTaskOptionByTaskId" + "&taskId=" + taskId ;
    		taskOption = HttpClientUtil.httpGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return taskOption;
	}
	
	/**
     * 获取会签领导审批意见.
     *
     * <p>detailed comment</p>
	 * @author [创建人]  WeiLei <br/> 
	 * 		   [创建时间] 2017年2月15日 上午9:43:18 <br/> 
	 * 		   [修改人]  WeiLei <br/>
	 * 		   [修改时间] 2017年2月15日 上午9:43:18
     * @param params
     * @see
     */
	private String getSignLeaderOption(Object[] params){
		
		String taskOption = "";
    	try {
    		String dislpayId = params[1].toString();
    		String dataKey = params[2].toString();
    		String signTask = params[3].toString();
    		String signCode = params[4].toString();
    		String leaderTask = params[5].toString();
    		String appClient=params[6].toString();
            String ibmsServer = getIBMSServer(appClient, "", "");
    		String url = ibmsServer + "/taskOperation?operationType=getSignLeaderOption"
    				   + "&dislpayId=" + dislpayId + "&dataKey=" + dataKey 
    				   + "&signTask=" + signTask + "&leaderTask=" + leaderTask 
    				   + "&signCode=" + signCode;
    		taskOption = HttpClientUtil.httpGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return taskOption;
	}
	
	/**
	 * 获取流程实例信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人]  WeiLei <br/> 
	 * 		   [创建时间] 2017年3月20日 上午11:35:16 <br/> 
	 * 		   [修改人]  WeiLei <br/>
	 * 		   [修改时间] 2017年3月20日 上午11:35:16
	 * @param params
	 * @return
	 * @see
	 */
	private String getProcessInstanceInfo(Object[] params){
		
		String taskOption = "";
    	try {
    		String dislpayId = params[1].toString();
    		String dataKey = params[2].toString();
    		String appClient=params[3].toString();
            String ibmsServer = getIBMSServer(appClient, "", "");
    		String url = ibmsServer + "/taskOperation?operationType=getProcessInstanceInfo"
    				   + "&dislpayId=" + dislpayId + "&dataKey=" + dataKey;
    		taskOption = HttpClientUtil.httpGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return taskOption;
	}
	/**
	 *
	 * <p>detailed comment</p>
	 * @author [创建人]  YangLei <br/> 
	 * @version[创建时间] 2017年6月20日 <br/> 
	 * 		   [修改人]  YangLei <br/>
	 * 		   [修改时间] 2017年6月20日
	 * @param request  
	 * @param response sysUnit 系统设置的经费单位
	 * @return  		unit 个人统计的经费单位
	 * @see
	 */
	private String moneyChange(Object[] params) {
		String money = params[1].toString();
		String sysUnit = params[2].toString();
		String unit = params[3].toString();
		
		String twoMoney = "";
		String [] moneyList  = money.split(",");
		for (int i = 0; i < moneyList.length; i++) {
			String moneyArr = moneyList[i];
			
			if (StringUtil.isEmpty(moneyArr))
				return "";
			double dbMoney = Double.parseDouble(moneyArr);
			
			if ("万元".equals(unit)) {
				if ("万元".equals(sysUnit)) {
					twoMoney += dbMoney;
				} else {
					double moneys = (Math.round(dbMoney * 100)) / 1000000.00;
					twoMoney += String.format("%.2f", moneys);
				}
			} else {
				if ("万元".equals(sysUnit)) {
					double moneys = (Math.round(dbMoney)) / 10000.000000;
					twoMoney += String.format("%.6f", moneys);
				} else {
					twoMoney += dbMoney;
				}
			}
		}
		
		return twoMoney;
	}
}
