package com.cssrc.ibms.core.login.license;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.core.util.common.CommonTools;

public class ValidateLicence {
	public static String delim = "\\.";
	public String validate(Map license, Map localInfo) {
		String message = "success";
		String mac_license = CommonTools.null2String((String) license
				.get("macaddress"));
		int num_license = 0;
		if (license.get("userNum") != null) {
			num_license = Integer.parseInt((String) license.get("userNum"));
		} else {
			num_license = Integer.parseInt((String) license.get("TDMUserNum"));
		} 
		String date_license = CommonTools.null2String((String) license
				.get("endDate")); 
		String lowIPAddress = CommonTools.null2String((String) license
				.get("lowIPAddress"));
		String highIPAddress = CommonTools.null2String((String) license
				.get("highIPAddress"));
		String userIP = CommonTools.null2String((String) localInfo
				.get("userIP"));
		String blackIPs = CommonTools.null2String((String) license
				.get("blackIPs"));
		List<String> mac_local = (List<String>) localInfo.get("sysmacaddress");
		int num_local = Integer.parseInt((String) localInfo.get("sysnum")); 
		String date_local = CommonTools.null2String((String) localInfo
				.get("systime"));
		if (!mac_local.contains(mac_license)) {
			message = "License验证无效,请更新License!!!";
		} else if (num_local >= num_license) {
			message = "服务器登录人数已满,请稍候登陆!!!";
		} else if (compareDate(date_license, date_local)) {
			message = "授权日期已过期,请更新License!!!";
		} else if (!lowIPAddress.equals("") && !highIPAddress.equals("")
				&& !"127.0.0.1".equals(userIP)
				&& !compareIP(lowIPAddress, highIPAddress, userIP)) {
			message = "IP地址无效，无法访问系统!!!";
		} else if (blackIPs != null && blackIPs.indexOf(userIP) >= 0) {
			message = "IP地址不合法，无法访问系统!!!";
		} else {
			message = "success";
		}
		return "success";
		//return message;

	}
	private boolean compareDate(String date_license, String date_local) {
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dateInLicense = sdf.parse(date_license);
			Date dateInLocal = sdf.parse(date_local);
			if (dateInLocal.compareTo(dateInLicense) == 1) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	public static boolean compareIP(String startIP, String endIP, String userIP) {
		String[] strIp = userIP.split(delim);
		userIP = "";
		for (int i = 0; i < strIp.length; i++) {
			userIP = userIP + getZeroString(strIp[i], 3);
		}
		int equalFlag = 0;
		String strIp1 = "";
		String strIp2 = "";
		if (startIP.equals(endIP)) {
			String[] strIpBegin = startIP.split(delim);
			for (int j = 0; j < strIpBegin.length; j++) {
				strIp1 = strIp1 + getZeroString(strIpBegin[j], 3);
			}
			if (userIP.compareTo(strIp1) == 0) {
				equalFlag = 1;
			}
		} else {
			String[] strIpBegin = startIP.split(delim);
			String[] strIpEnd = endIP.split(delim);
			for (int j = 0; j < strIpBegin.length; j++) {
				strIp1 = strIp1 + getZeroString(strIpBegin[j], 3);
				strIp2 = strIp2 + getZeroString(strIpEnd[j], 3);
			}
			if (userIP.compareTo(strIp1) >= 0 && userIP.compareTo(strIp2) <= 0) {
				equalFlag = 1;
			}
		}
		if (equalFlag == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public static String getZeroString(String str, int cnt) {
		String strZeroS = "";
		if (str.trim().length() >= cnt) {
			return str;
		}
		int intStrCnt = cnt - str.length();
		for (int i = 0; i < intStrCnt; i++) {
			strZeroS = strZeroS + "0";
		}
		strZeroS = strZeroS + str;
		return strZeroS;
	}
	
	public String validateRCP(Map license, Map localInfo) {
		String message = "success";
		String RCPType = CommonTools.null2String((String) localInfo
				.get("type"));
		/*List<Map> rcpUserInfos = LoginCheckingImpl.doQueryForRcp(RCPType);
		String date_license = CommonTools.null2String((String) license
				.get("endDate"));
		String lowIPAddress = CommonTools.null2String((String) license
				.get("lowIPAddress"));
		String highIPAddress = CommonTools.null2String((String) license
				.get("highIPAddress"));
		String userIP = CommonTools.null2String((String) localInfo
				.get("userIP"));
		String blackIPs = CommonTools.null2String((String) license
				.get("blackIPs"));
		String date_local = this.getSystemTime();
		if(RCPType.equals("rcp1")){
			if(license.get("DSUserNum")!=null){
				int ds_license = Integer.parseInt((String) license.get("DSUserNum"));
				if(rcpUserInfos.size()>ds_license||ds_license==0)message = "超出数量限制！！";
			}
		}else if(RCPType.equals("rcp2")){
			if(license.get("TBOMUserNum")!=null){
				int tbom_license = Integer.parseInt((String) license.get("TBOMUserNum"));
				if(rcpUserInfos.size()>tbom_license||tbom_license==0)message = "超出数量限制！！";
			}
		}else if(RCPType.equals("rcp3")){
			if(license.get("ETLUserNum")!=null){
				int etl_license = Integer.parseInt((String) license.get("ETLUserNum"));
				if(rcpUserInfos.size()>etl_license||etl_license==0)message = "超出数量限制！！";
			}
		}else if(RCPType.equals("rcp4")){
			if(license.get("WFUserNum")!=null){
				int wf_license = Integer.parseInt((String) license.get("WFUserNum"));
				if(rcpUserInfos.size()>wf_license||wf_license==0)message = "超出数量限制！！";
			}
		}
		
		if (compareDate(date_license, date_local)) {
			message = "授权日期已过期,请更新License!!!";
		} else if (!lowIPAddress.equals("") && !highIPAddress.equals("")
				&& !"127.0.0.1".equals(userIP)
				&& !compareIP(lowIPAddress, highIPAddress, userIP)) {
			message = "IP地址无效，无法访问系统!!!";
		} else if (!blackIPs.equals("") && blackIPs.indexOf(userIP) >= 0) {
			message = "IP地址不合法，无法访问系统!!!";
		}*/
		return message;
	}
	
	private String getSystemTime(){
		Calendar  cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String day = String.valueOf(cal.get(Calendar.DATE));
		if(day.length() == 1){
			day = "0" + day;
		}
		String time = year + "-" + month + "-" + day;
		return time;
	}

}
