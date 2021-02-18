package com.cssrc.ibms.core.login.license;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetSystemInfo {
	InetAddress addr = null;
	
	private static GetSystemInfo _instance = null;

	public GetSystemInfo() {
		try {
			addr = InetAddress.getLocalHost();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static GetSystemInfo getIntance(){
		if(_instance==null){
			return new GetSystemInfo();
		}
		return _instance;
	}

	private String getSystemIp() {
		String ip = addr.getHostAddress().toString();
		return ip;
	}

	private String getSystemTime() {
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		String day = String.valueOf(cal.get(Calendar.DATE));
		if (day.length() == 1) {
			day = "0" + day;
		}
		String time = year + "-" + month + "-" + day;
		return time;
	}

	static String hexByte(byte b) {
		String s = "000000" + Integer.toHexString(b);
		return s.substring(s.length() - 2);
	}

	private List<String> getSystemMacAddress() {
		List<String> address = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> el = NetworkInterface
					.getNetworkInterfaces();
			while (el.hasMoreElements()) {
				byte[] mac = el.nextElement().getHardwareAddress();
				if (mac == null || mac.length <= 0)
					continue;
				StringBuilder builder = new StringBuilder();
				for (byte b : mac) {
					builder.append(hexByte(b));
					builder.append("-");
				}
				builder.deleteCharAt(builder.length() - 1);
				address.add(builder.toString().toUpperCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return address;
		}
	}

	public Map<String, Object> getSystemInfo() {
		Map<String, Object> map = new HashMap();
		map.put("sysip", this.getSystemIp());
		map.put("systime", this.getSystemTime());
		map.put("sysmacaddress", this.getSystemMacAddress());
		return map;
	}
	
	public static String getLocalIp(){
		return getIntance().getSystemIp();
	}

}
