package com.cssrc.ibms.system.util;

public class FileVersionManage {
	public static int OCTAL_TYPE = 8;
	public static int TEN_TYPE = 10;
	public static int HEX_TYPE = 16;
	public static String START_VERSION = "0.1";
	
	
	/**
	 * 跳号
	 * @param version
	 * @return
	 */
	public static String jump(String version){
		String[] vs = version.split(".");
		Integer v1 = Integer.valueOf(vs[0]);
		v1=v1+1;
		return v1+"."+"0";
	}
	/**
	 * 获取下移版本号
	 * @param version
	 * @return
	 */
	public static String getNext(String version){
		return getNext(version,1);
	}
	/**
	 * 获取下一版本号
	 * @param version
	 * @param addNum
	 * @return
	 */
	public static String getNext(String version,int addNum){
		try{
			String[] vs = version.split("\\.");
			Integer v1 = Integer.valueOf(vs[0]);
			Integer v2 = Integer.valueOf(vs[1]);
			v2 = v2+addNum;
			return v1+"."+v2;
		}catch(Exception e){
			return START_VERSION;
		}
	}
	
	
	
	/*public static String getNext(String version){
		return getNext(version,TEN_TYPE,1);
	}*/
	/*public static String getNext(String version,int type){
		return getNext(version,type,1);
	}
	public static String getNext(String version,int type,int addNum){
		try{
			if(version.equals("")){
				return START_VERSION;
			}
			Double num = toTenNumber(version,type);
			return toVersion(num+addNum,type);
		}catch(Exception e){
			return START_VERSION;
		}
	}
	private static Double toTenNumber(String version,int type){
		Double number = 0d;
		String[] arr = version.split("\\.");
		switch(type){
		case 8:
			for(int i =0;i<arr.length;i++){
				number+= Math.pow(8, arr.length-1-i)*Double.valueOf(arr[i]);
			}
			break;
		case 16:
			for(int i =0;i<arr.length;i++){
				number+= Math.pow(16, arr.length-1-i)*Double.valueOf(arr[i]);
			}
			break;
		default :
			for(int i =0;i<arr.length;i++){
				number+= Math.pow(10, arr.length-1-i)*Double.valueOf(arr[i]);
			}
			break;	
		}
		return number;
	}
	public static String toVersion(Double number,int type){
		String version = "";
		String str = "";
		Integer num = number.intValue();
		switch(type){
		case 8:
			version = Integer.toOctalString(num);
			break;
		case 16:
			version = Integer.toHexString(num);
			break;
		default :
			version = Integer.toString(num);
			break;	
		}
		for(int i =0;i<version.length();i++){
			if(i==version.length()-1){
				str+="."+version.charAt(i);
			}else{
				str+=version.charAt(i);
			}
		}
		return str;
	}*/
}
