package com.cssrc.ibms.core.util.string;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

/**
 * 数组相关的帮助类
 * <p>
 * 		可以先到ArrayUtils（cpmmoms-lang-*.jar）找
 * </p>
 * @author zhulongchao
 *
 */
public class ArrayUtil {

	/**
	 * 字符串数组转换为Long数组
	 * @param aryStr  字符串数组
	 * @return Long数组
	 */
	public static Long[] convertArray(String[] aryStr) {
		if(ArrayUtils.isEmpty(aryStr))
			return ArrayUtils.EMPTY_LONG_OBJECT_ARRAY;
		Long[] aryLong = new Long[aryStr.length];
		for (int i = 0; i < aryStr.length; i++) {
			aryLong[i] = Long.parseLong(aryStr[i]);
		}
		return aryLong;
	}
	
	
	/**
	 * Long数组转换为字符串数组
	 * @param aryLong Long数组
	 * @return 字符串数组
	 */
	public static String[] convertArray(Long[] aryLong) {
		if(ArrayUtils.isEmpty(aryLong))
			return ArrayUtils.EMPTY_STRING_ARRAY;
		String[] aryStr = new String[aryLong.length];
		for (int i = 0; i < aryStr.length; i++) {
			aryStr[i] = String.valueOf(aryStr[i]);
		}
		return aryStr;
	}
	
	/**
	 * Long数组转换为字符串数组
	 * @param aryLong Long数组
	 * @return 字符串数组
	 */
	public static String[] convertArray(List<String> strlis) {
		String[] aryStr = new String[strlis.size()];
		for (int i = 0; i < aryStr.length; i++) {
			aryStr[i] = String.valueOf(strlis.get(i));
		}
		return aryStr;
	}
	
}
