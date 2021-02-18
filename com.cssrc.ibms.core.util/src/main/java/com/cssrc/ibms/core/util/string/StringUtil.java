package com.cssrc.ibms.core.util.string;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateFormatUtil;

/**
 * 字符串操作类
 * 
 */
public class StringUtil {
	public static String[] split(String str,String regex){
		return str.split(regex);
	}
	public static String EnglishRemove(String str){
		str = str==null?"":str;
		String regEx = "[a-zA-Z]";//英文reg
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);	
		String result = str;
		if(matcher.find()){
			result = matcher.replaceAll("");
			matcher = pattern.matcher(result);
		}
		return result;
	}
	/**
	 * el表达式替换
	 * @param str :需要替换的文本
	 * @param map：匹配到的key，替换成map中对应的value
	 * @return 
	 */
	public static String elReplace(String str,Map<String,Object> map){
		str = str==null?"":str;
		String regEx = "\\$\\{.*\\}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);		
		if(map.isEmpty()){
			return str;
		}
		String result = str;
		while(matcher.find()){
			String groups = matcher.group();
			String gg = groups.replaceAll("\\$\\{", "").replaceAll("\\}", "");
			if(map.containsKey(gg)){
				result = matcher.replaceFirst(CommonTools.Obj2String(map.get(gg)));
			}else{
				result = matcher.replaceFirst(gg);
			}
			matcher = pattern.matcher(result);
		}
		return result;
	}
	/**
	 * 对字符串 escape 编码
	 * 
	 * @param src
	 * @return
	 */
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);

		for (i = 0; i < src.length(); i++) {

			j = src.charAt(i);

			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static String replaceVariable(String template,
			Map<String, String> map) throws Exception {
		Pattern regex = Pattern.compile("\\{(.*?)\\}");
		Matcher regexMatcher = regex.matcher(template);
		while (regexMatcher.find()) {
			String key = regexMatcher.group(1);
			String toReplace = regexMatcher.group(0);
			String value = (String) map.get(key);
			if (value != null)
				template = template.replace(toReplace, value);
			// else
			// throw new
			// Exception(ContextUtil.getMessages("StringUtil.replaceVariable.throwMsg",
			// new Object[]{key}));
		}

		return template;
	}

	/**
	 * 对编码的字符串解码
	 * 
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	/**
	 * 判断指定的内容是否存在
	 * 
	 * @param content
	 *            内容
	 * @param begin
	 *            开始标签
	 * @param end
	 *            结束标签
	 * @return
	 */
	public static boolean isExist(String content, String begin, String end) {
		String tmp = content.toLowerCase();
		begin = begin.toLowerCase();
		end = end.toLowerCase();
		int beginIndex = tmp.indexOf(begin);
		int endIndex = tmp.indexOf(end);
		if (beginIndex != -1 && endIndex != -1 && beginIndex < endIndex)
			return true;
		return false;
	}

	/**
	 * 去掉前面的指定字符
	 * 
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trimPrefix(String toTrim, String trimStr) {
		while (toTrim.startsWith(trimStr)) {
			toTrim = toTrim.substring(trimStr.length());
		}
		return toTrim;
	}

	/**
	 * 删除后面指定的字符
	 * 
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trimSufffix(String toTrim, String trimStr) {
		while (toTrim.endsWith(trimStr)) {
			toTrim = toTrim.substring(0, toTrim.length() - trimStr.length());
		}
		return toTrim;
	}

	/**
	 * 删除指定的字符
	 * 
	 * @param toTrim
	 * @param trimStr
	 * @return
	 */
	public static String trim(String toTrim, String trimStr) {
		return trimSufffix(trimPrefix(toTrim, trimStr), trimStr);
	}

	/**
	 * 编码html
	 * 
	 * @param content
	 * @return
	 */
	public static String escapeHtml(String content) {
		return StringEscapeUtils.escapeHtml(content);
	}

	/**
	 * 反编码html
	 * 
	 * @param content
	 * @return
	 */
	public static String unescapeHtml(String content) {
		return StringEscapeUtils.unescapeHtml(content);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		if (str.trim().equals(""))
			return true;
		return false;
	}

	/**
	 * 判断字符串非空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String replaceVariable(String template, String repaceStr) {
		Pattern regex = Pattern.compile("\\{(.*?)\\}");
		Matcher regexMatcher = regex.matcher(template);
		if (regexMatcher.find()) {
			String toReplace = regexMatcher.group(0);
			template = template.replace(toReplace, repaceStr);
		}
		return template;
	}

	/**
	 * 截取字符串 中文为两个字节，英文为一个字节。 两个英文为一个中文。
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String subString(String str, int len, String chopped) {
		if (str == null || "".equals(str))
			return "";
		char[] chars = str.toCharArray();
		int cnLen = len * 2;
		String tmp = "";
		boolean isOver = false;
		int iLen = 0;
		for (int i = 0; i < chars.length; i++) {
			int iChar = (int) chars[i];
			if (iChar <= 128)
				iLen = iLen + 1;
			else
				iLen = iLen + 2;
			if (iLen >= cnLen) {
				isOver = true;
				break;
			}

			tmp += String.valueOf(chars[i]);
		}
		if (isOver) {
			tmp += chopped;
		}
		return tmp;
	}

	/**
	 * 截取字符串 中文为两个字节，英文为一个字节。 两个英文为一个中文。
	 * 
	 * @param str
	 * @return
	 */
	public static String subString(String str) {
		int len = 25;
		String tmp = AppConfigUtil.get("titleLen");
		String regex = "<(?:(?:/([^>]+)>)|(?:!--([\\S|\\s]*?)-->)|(?:([^\\s/>]+)\\s*((?:(?:\"[^\"]*\")|(?:'[^']*')|[^\"'<>])*)/?>))";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return str;
		} else {
			if (StringUtil.isNotEmpty(tmp)) {
				len = Integer.parseInt(tmp);
			}
			return subString(str, len, "...");
		}
	}

	/**
	 * 判读输入字符是否是数字
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNumberic(String s) {
		if (StringUtils.isEmpty(s))
			return false;
		boolean rtn = validByRegex("^[-+]{0,1}\\d*\\.{0,1}\\d+$", s);
		if (rtn)
			return true;

		return validByRegex("^0[x|X][\\da-eA-E]+$", s);
	}

	/**
	 * 是否是整数。
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isInteger(String s) {
		boolean rtn = validByRegex("^[-+]{0,1}\\d*$", s);
		return rtn;

	}

	/**
	 * 是否是电子邮箱
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmail(String s) {
		boolean rtn = validByRegex(
				"(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)*", s);
		return rtn;
	}

	/**
	 * 手机号码
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isMobile(String s) {
		boolean rtn = validByRegex(
				"^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", s);
		return rtn;
	}

	/**
	 * 电话号码
	 * 
	 * @param
	 * @return
	 */
	public static boolean isPhone(String s) {
		boolean rtn = validByRegex(
				"(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?", s);
		return rtn;
	}

	/**
	 * 邮编
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isZip(String s) {
		boolean rtn = validByRegex("^[0-9]{6}$", s);
		return rtn;
	}

	/**
	 * qq号码
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isQq(String s) {
		boolean rtn = validByRegex("^[1-9]\\d{4,9}$", s);
		return rtn;
	}

	/**
	 * ip地址
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isIp(String s) {
		boolean rtn = validByRegex(
				"^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
				s);
		return rtn;
	}

	/**
	 * 判断是否中文
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isChinese(String s) {
		boolean rtn = validByRegex("^[\u4e00-\u9fa5]+$", s);
		return rtn;
	}

	/**
	 * 字符和数字
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isChrNum(String s) {
		boolean rtn = validByRegex("^([a-zA-Z0-9]+)$", s);
		return rtn;
	}

	/**
	 * 判断是否是URL
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url) {
		return validByRegex(
				"(http://|https://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?",
				url);
	}

	/**
	 * 判断是否json格式
	 * 
	 * @param json
	 * @return
	 */
	public static Boolean isJson(String json) {
		if (isEmpty(json))
			return false;
		try {
			JSONObject.parseObject(json);
			return true;
		} catch (Exception e) {
			try {
				JSONArray.parseArray(json);
				return true;
			} catch (JSONException ex) {
				return false;
			}
		}
	}

	/**
	 * 判断是否JSONArray格式
	 * 
	 * @param json
	 * @return
	 */
	public static Boolean isJSONArray(String json) {
		if (isEmpty(json))
			return false;
		try {
			JSONArray.parseObject(json);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 使用正则表达式验证。
	 * 
	 * @param regex
	 * @param input
	 * @return
	 */
	public static boolean validByRegex(String regex, String input) {
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher regexMatcher = p.matcher(input);
		return regexMatcher.find();
	}

	/**
	 * 判断某个字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 判断某个字符串是否为为指定的日期格式
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static boolean isFormatDate(String dateStr,String format){
		boolean isdate =false;
		try{
			DateFormat dft= new SimpleDateFormat(format);
			Date date = dft.parse(dateStr);
			if(dft.format(date).equals(dateStr)){
				isdate = true; 
			}
		}catch(Exception e){
			return isdate;
		}
		return isdate;
	}
	/**
	 * 把字符串的第一个字母转为大写
	 * 
	 * @param newStr
	 * @return
	 */
	public static String makeFirstLetterUpperCase(String newStr) {
		if (newStr.length() == 0)
			return newStr;

		char[] oneChar = new char[1];
		oneChar[0] = newStr.charAt(0);
		String firstChar = new String(oneChar);
		return (firstChar.toUpperCase() + newStr.substring(1));
	}

	/**
	 * 把字符串的第一字每转为小写
	 * 
	 * @param newStr
	 * @return
	 */
	public static String makeFirstLetterLowerCase(String newStr) {
		if (newStr.length() == 0)
			return newStr;

		char[] oneChar = new char[1];
		oneChar[0] = newStr.charAt(0);
		String firstChar = new String(oneChar);
		return (firstChar.toLowerCase() + newStr.substring(1));
	}

	/**
	 * 格式化带参数的字符串，如 /param/detail.do?a={0}&b={1}
	 * 
	 * @param message
	 * @param args
	 * @return
	 */
	public static String formatParamMsg(String message, Object... args) {
		for (int i = 0; i < args.length; i++) {
			message = message.replace("{" + i + "}", args[i].toString());
		}
		return message;
	}

	/**
	 * 格式化如下字符串 http://www.bac.com?a=${a}&b=${b}
	 * 
	 * @param message
	 * @param params
	 */
	public static String formatParamMsg(String message, Map<String, ?> params) {
		if (params == null)
			return message;
		Iterator<String> keyIts = params.keySet().iterator();
		while (keyIts.hasNext()) {
			String key = keyIts.next();
			Object val = params.get(key);
			if (val != null) {
				message = message.replace("${" + key + "}", val.toString());
			}
		}
		return message;
	}

	/**
	 * 简单的字符串格式化，性能较好。支持不多于10个占位符，从%1开始计算，数目可变。参数类型可以是字符串、Integer、Object，
	 * 甚至int等基本类型
	 * 、以及null，但只是简单的调用toString()，较复杂的情况用String.format()。每个参数可以在表达式出现多次。
	 * 
	 * @param msgWithFormat
	 * @param autoQuote
	 * @param args
	 * @return
	 */
	public static StringBuilder formatMsg(CharSequence msgWithFormat,
			boolean autoQuote, Object... args) {
		int argsLen = args.length;
		boolean markFound = false;

		StringBuilder sb = new StringBuilder(msgWithFormat);

		if (argsLen > 0) {
			for (int i = 0; i < argsLen; i++) {
				String flag = "%" + (i + 1);
				int idx = sb.indexOf(flag);
				// 支持多次出现、替换的代码
				while (idx >= 0) {
					markFound = true;
					sb.replace(idx, idx + 2, toString(args[i], autoQuote));
					idx = sb.indexOf(flag);
				}
			}

			if (args[argsLen - 1] instanceof Throwable) {
				StringWriter sw = new StringWriter();
				((Throwable) args[argsLen - 1])
						.printStackTrace(new PrintWriter(sw));
				sb.append("\n").append(sw.toString());
			} else if (argsLen == 1 && !markFound) {
				sb.append(args[argsLen - 1].toString());
			}
		}
		return sb;
	}

	public static StringBuilder formatMsg(String msgWithFormat, Object... args) {
		return formatMsg(new StringBuilder(msgWithFormat), true, args);
	}

	public static String toString(Object obj, boolean autoQuote) {
		StringBuilder sb = new StringBuilder();
		if (obj == null) {
			sb.append("NULL");
		} else {
			if (obj instanceof Object[]) {
				for (int i = 0; i < ((Object[]) obj).length; i++) {
					sb.append(((Object[]) obj)[i]).append(", ");
				}
				if (sb.length() > 0) {
					sb.delete(sb.length() - 2, sb.length());
				}
			} else {
				sb.append(obj.toString());
			}
		}
		if (autoQuote
				&& sb.length() > 0
				&& !((sb.charAt(0) == '[' && sb.charAt(sb.length() - 1) == ']') || (sb
						.charAt(0) == '{' && sb.charAt(sb.length() - 1) == '}'))) {
			sb.insert(0, "[").append("]");
		}
		return sb.toString();
	}

	public static String returnSpace(String str) {
		String space = "";
		if (!str.isEmpty()) {
			String path[] = str.split("\\.");
			for (int i = 0; i < path.length - 1; i++) {
				space += "&nbsp;&emsp;";
			}
		}
		return space;
	}

	/**
	 * 输出明文按sha-256加密后的密文
	 * 
	 * @param inputStr
	 *            明文
	 * @return
	 */
	public static synchronized String encryptSha256(String inputStr) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte digest[] = md.digest(inputStr.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(digest));
		} catch (Exception e) {
			return null;
		}
	}

	public static synchronized String encryptMd5(String inputStr) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(inputStr.getBytes());
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(Integer.toHexString((int) (b & 0xff)));
			}

			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 把字符串List转成带，的字符串
	 * 
	 * @param arr
	 * @return 返回字符串，格式如1,2,3
	 */
	public static String getArrayAsString(List<String> arr) {
		if (arr == null || arr.size() == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.size(); i++) {
			if (i > 0)
				sb.append(",");
			sb.append(arr.get(i));
		}
		return sb.toString();
	}

	/**
	 * 把字符串数组转成带，的字符串
	 * 
	 * @param arr
	 * @return 返回字符串，格式如1#2#3
	 */
	public static String getArrayAsString(String[] arr) {
		if (arr == null || arr.length == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		// 当第一个流程节点跳转多个执行人时进行判断 yangbo
		String[] strs = arr[0].split(",");
		if (arr.length == 1 && strs.length > 1) {
			for (int i = 0; i < strs.length; i++) {
				if (i > 0)
					sb.append("#");
				sb.append(strs[i]);
			}
			return sb.toString();
		}
		// 首节点无跳转选择和其他节点选择跳转时
		for (int i = 0; i < arr.length; i++) {
			if (i > 0)
				sb.append("#");
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	public static String getStringFromArray(String[] arr, String split) {
		if (arr == null || arr.length == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0)
				sb.append(split);
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	/**
	 * 把Set转成带，的字符串
	 * 
	 * @param Set
	 * @return 返回字符串，格式如1,2,3
	 */
	public static String getSetAsString(Set<?> set) {
		if (set == null || set.size() == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		int i = 0;
		Iterator<?> it = set.iterator();
		while (it.hasNext()) {
			if (i++ > 0)
				sb.append(",");
			sb.append(it.next().toString());
		}
		return sb.toString();
	}

	/**
	 * 将人民币转成大写。
	 * 
	 * @param amount
	 * @return
	 */
	public static String hangeToBig(double value) {

		char[] hunit = { '拾', '佰', '仟' }; // 段内位置表示
		char[] vunit = { '万', '亿' }; // 段名表示
		char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
		String zheng = "整";
		String jiao = "角";
		String fen = "分";
		char yuan = '圆';
		long midVal = (long) (value * 100); // 转化成整形
		String valStr = String.valueOf(midVal); // 转化成字符串

		String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
		int len = head.length();
		if (len > 12)
			return "值过大";

		String rail = valStr.substring(valStr.length() - 2); // 取小数部分

		String prefix = ""; // 整数部分转化的结果
		String suffix = ""; // 小数部分转化的结果
		// 处理小数点后面的数
		if (rail.equals("00")) { // 如果小数部分为0
			suffix = zheng;
		} else {
			suffix = digit[rail.charAt(0) - '0'] + jiao
					+ digit[rail.charAt(1) - '0'] + fen; // 否则把角分转化出来
		}
		// 处理小数点前面的数
		char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
		char zero = '0'; // 标志'0'表示出现过0
		byte zeroSerNum = 0; // 连续出现0的次数
		for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
			int idx = (chDig.length - i - 1) % 4; // 取段内位置
			int vidx = (chDig.length - i - 1) / 4; // 取段位置
			if (chDig[i] == '0') { // 如果当前字符是0
				zeroSerNum++; // 连续0次数递增
				if (zero == '0') { // 标志
					zero = digit[0];
				} else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
					prefix += vunit[vidx - 1];
					zero = '0';
				}
				continue;
			}
			zeroSerNum = 0; // 连续0次数清零
			if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
				prefix += zero;
				zero = '0';
			}
			prefix += digit[chDig[i] - '0']; // 转化该数字表示
			if (idx > 0)
				prefix += hunit[idx - 1];
			if (idx == 0 && vidx > 0) {
				prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
			}
		}

		if (prefix.length() > 0)
			prefix += yuan; // 如果整数部分存在,则有圆的字样
		return prefix + suffix; // 返回正确表示
	}

	/**
	 * 替换json特殊字符转码
	 * 
	 * @param str
	 * @return
	 */
	public static String jsonUnescape(String str) {
		return str.replace("&quot;", "\"").replace("&nuot;", "\n");
	}

	/**
	 * HTML实体编码转成普通的编码
	 * 
	 * @param dataStr
	 * @return
	 */
	public static String htmlEntityToString(String dataStr) {
		dataStr = dataStr.replace("&apos;", "'").replace("&quot;", "\"")
				.replace("&gt;", ">").replace("&lt;", "<")
				.replace("&amp;", "&");

		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();

		while (start > -1) {
			int system = 10;// 进制
			if (start == 0) {
				int t = dataStr.indexOf("&#");
				if (start != t) {
					start = t;
				}
				if (start > 0) {
					buffer.append(dataStr.substring(0, start));
				}
			}
			end = dataStr.indexOf(";", start + 2);
			String charStr = "";
			if (end != -1) {
				charStr = dataStr.substring(start + 2, end);
				// 判断进制
				char s = charStr.charAt(0);
				if (s == 'x' || s == 'X') {
					system = 16;
					charStr = charStr.substring(1);
				}
			}
			// 转换
			try {
				if (isNotEmpty(charStr)) {
					char letter = (char) Integer.parseInt(charStr, system);
					buffer.append(new Character(letter).toString());
				}
			} catch (NumberFormatException e) {
				// e.printStackTrace();

			}

			// 处理当前unicode字符到下一个unicode字符之间的非unicode字符
			start = dataStr.indexOf("&#", end);
			if (start - end > 1) {
				buffer.append(dataStr.substring(end + 1, start));
			}

			// 处理最后面的非unicode字符
			if (start == -1) {
				int length = dataStr.length();
				if (end + 1 != length) {
					buffer.append(dataStr.substring(end + 1, length));
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * 把String转成html实体字符
	 * 
	 * @param str
	 * @return
	 */
	public static String stringToHtmlEntity(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			switch (c) {
			case 0x0A:
				sb.append(c);
				break;

			case '<':
				sb.append("&lt;");
				break;

			case '>':
				sb.append("&gt;");
				break;

			case '&':
				sb.append("&amp;");
				break;

			case '\'':
				sb.append("&apos;");
				break;

			case '"':
				sb.append("&quot;");
				break;

			default:
				if ((c < ' ') || (c > 0x7E)) {
					sb.append("&#x");
					sb.append(Integer.toString(c, 16));
					sb.append(';');
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 字符串 编码转换
	 * 
	 * @param str
	 *            字符串
	 * @param from
	 *            原來的編碼
	 * @param to
	 *            轉換后的編碼
	 * @return
	 */
	public static String encodingString(String str, String from, String to) {
		String result = str;
		try {
			result = new String(str.getBytes(from), to);
		} catch (Exception e) {
			result = str;
		}
		return result;
	}

	/**
	 * 将字符串数字转成千分位显示。
	 */
	public static String comdify(String value) {
		DecimalFormat df = null;
		if (value.indexOf(".") > 0) {
			int i = value.length() - value.indexOf(".") - 1;
			switch (i) {
			case 0:
				df = new DecimalFormat("###,##0");
				break;
			case 1:
				df = new DecimalFormat("###,##0.0");
				break;
			case 2:
				df = new DecimalFormat("###,##0.00");
				break;
			case 3:
				df = new DecimalFormat("###,##0.000");
				break;
			case 4:
				df = new DecimalFormat("###,##0.0000");
				break;
			default:
				df = new DecimalFormat("###,##0.00000");
				break;
			}

		} else {
			df = new DecimalFormat("###,##0");
		}
		double number = 0.0;
		try {
			number = Double.parseDouble(value);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}

	/**
	 * 转换换行符（不删除空格）
	 * 
	 * @param arg
	 *            要转换的字符串
	 * @param flag
	 *            标志是正向转换（true），还是逆向转换（false）
	 * @return
	 */
	public static String convertScriptLine(String arg, Boolean flag) {
		if (StringUtils.isEmpty(arg))
			return arg;
		String origStr = "\n", targStr = "/n";
		if (!flag) {
			origStr = "/n";
			targStr = "\n";
		}
		String[] args = arg.split(origStr);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			if (args.length != i + 1)
				sb.append(targStr);
		}
		return sb.toString();
	}

	/**
	 * 转换换行符（删除空格）
	 * 
	 * @param arg
	 *            要转换的字符串
	 * @param flag
	 *            标志是正向转换（true），还是逆向转换（false）
	 * @return
	 */
	public static String convertLine(String arg, Boolean flag) {
		if (StringUtils.isEmpty(arg))
			return arg;
		String origStr = "\n", targStr = "/n";
		if (!flag) {
			origStr = "/n";
			targStr = "\n";
		}
		String[] args = arg.split(origStr);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			sb.append(StringUtils.deleteWhitespace(args[i]));
			if (args.length != i + 1)
				sb.append(targStr);
		}
		return sb.toString();
	}

	/**
	 * 转换换行符
	 * 
	 * @param arg
	 *            要转换的字符串
	 * @return
	 */
	public static String deleteWhitespaceLine(String arg) {
		if (StringUtils.isEmpty(arg))
			return arg;
		String origStr = "\n";
		String[] args = arg.split(origStr);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			sb.append(StringUtils.deleteWhitespace(args[i]));
			if (args.length != i + 1)
				sb.append(origStr);
		}
		return sb.toString();
	}

	/**
	 * 格式化换行，页面展示
	 * 
	 * @param arg
	 * @return
	 */
	public static String parseText(String arg) {
		if (StringUtils.isEmpty(arg))
			return arg;
		String[] args = arg.split("\n");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			if (args.length != i + 1)
				sb.append("</br>");
		}
		return sb.toString();
	}

	/**
	 * 去掉不可见字符。
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceNotVisable(String str) {
		char[] ary = str.toCharArray();
		List<Character> list = new ArrayList<Character>();
		for (int i = 0; i < ary.length; i++) {
			int c = (int) ary[i];
			if (!isViable(c))
				continue;
			list.add((char) c);
		}
		Object[] aryc = (Object[]) list.toArray();
		char[] arycc = new char[aryc.length];
		for (int i = 0; i < aryc.length; i++) {
			arycc[i] = (Character) aryc[i];
		}
		String out = new String(arycc);
		return out;
	}

	private static boolean isViable(int i) {
		if (i == 0 || i == 13 || (i >= 9 && i <= 10) || (i >= 11 && i <= 12)
				|| (i >= 28 && i <= 126) || (i >= 19968 && i <= 40869)) {
			return true;
		}
		return false;
	}

	/**
	 * 替换美元符号。
	 * 
	 * @param toReplace
	 * @param replace
	 * @param replaceBy
	 * @return
	 */
	public static String replaceAll(String toReplace, String replace,
			String replaceBy) {
		replaceBy = replaceBy.replaceAll("\\$", "\\\\\\$");
		return toReplace.replaceAll(replace, replaceBy);
	}

	public static String stringFormat2Json(String json) {
		StringBuilder sb = new StringBuilder();
		int size = json.length();
		for (int i = 0; i < size; i++) {
			char c = json.charAt(i);
			switch (c) {
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 根据格式取得数据显示。
	 * 
	 * @param value
	 *            需要处理的值
	 * @param isShowComdify
	 *            是否千分位显示
	 * @param decimalValue
	 *            保留小数的位数
	 * @param coinValue
	 *            货币显示
	 * @return
	 */
	public static String getNumber(Object value, Object isShowComdify,
			Object decimalValue, Object coinValue) {
		if (value == null)
			return "";
		String val = value.toString();

		if (isShowComdify != null) {
			boolean result = false;
			if(isShowComdify.equals("1")){
				result = true;
			}
			//boolean result = Boolean.valueOf(isShowComdify.toString());
			Double douvalue = Double.parseDouble(val);
			DecimalFormat df = new DecimalFormat("");
			val = df.format(douvalue);
			if (!result) {
				val = val.replace(",", "");
			}
		}
		// 位数
		if (decimalValue != null) {
			int len = Integer.parseInt(decimalValue.toString());
			// 小数位 为0
			if (len > 0) {
				int idx = val.indexOf(".");
				if (idx == -1) {
					val = val + "." + getZeroLen(len);
				} else {
					String intStr = val.substring(0, val.indexOf("."));
					String decimal = val.substring(val.indexOf(".") + 1);
					if (decimal.length() > len) {
						Double douvalue = Double.parseDouble(val.replace(",",
								""));
						DecimalFormat df = new DecimalFormat("");
						df.setMaximumFractionDigits(len);
						String tmp = df.format(douvalue);
						if (tmp.indexOf(".") == -1) {
							val = intStr + "." + getZeroLen(len);
						} else {
							decimal = tmp.substring(tmp.indexOf(".") + 1);
							val = intStr + "." + decimal;
						}
					} else if (decimal.length() < len) {
						int tmp = len - decimal.length();
						val += getZeroLen(tmp);
					}
				}
			}
		}
		if (coinValue != null) {
			val = coinValue.toString() + val;
		}
		return val;
	}

	private static String getZeroLen(int len) {
		String str = "";
		for (int i = 0; i < len; i++) {
			str += "0";
		}
		return str;
	}

	/**
	 * 去除html标签
	 * 
	 * <pre>
	 * StringUtil
	 * 		.removeHTMLTag(
	 * 				null,
	 * 				&quot;&lt;span style=\&quot;color:#ff0000\&quot;&gt;&lt;span style=\&quot;font-size:16px\&quot;&gt;330&lt;20and ss&gt;sss&lt;span style=\&quot;background-color:#ffff00\&quot;&gt;&quot;);
	 * </pre>
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String removeHTMLTag(String htmlStr) {
		if (StringUtil.isEmpty(htmlStr))
			return "";
		htmlStr = Jsoup.clean(htmlStr, Whitelist.none());
		htmlStr = htmlEntityToString(htmlStr);
		return htmlStr.trim(); // 返回文本字符串
	}

	/**
	 * 判定的逗号（,）分隔字符串是否包括指定的字符。
	 * 
	 * @param str
	 * @param searchStr
	 * @return
	 */
	public static boolean contain(String str, String searchStr) {
		return contain(str, searchStr, ",", true);
	}

	/**
	 * 判定的指定分隔符的字符串是否包括指定的字符。
	 * 
	 * @param str
	 * @param searchStr
	 * @return
	 */
	public static boolean contain(String str, String searchStr,
			String argumentSeparator, boolean isIgnoreCase) {
		if (isEmpty(str))
			return false;
		if (isEmpty(argumentSeparator))
			argumentSeparator = ",";
		String[] aryStr = str.split(argumentSeparator);
		return contain(aryStr, searchStr, isIgnoreCase);
	}

	/**
	 * 判定的字符串数组是否包括指定的字符。
	 * 
	 * @param ids
	 * @param curId
	 * @return
	 */
	public static boolean contain(String[] aryStr, String searchStr,
			boolean isIgnoreCase) {
		if (BeanUtils.isEmpty(aryStr))
			return false;
		for (String str : aryStr) {
			if (isIgnoreCase) {
				if (str.equalsIgnoreCase(searchStr))
					return true;
			} else {
				if (str.equals(searchStr))
					return true;
			}

		}
		return false;
	}
	
	/**
	 * 获取字符串中某种字符的个数
	 * 
	 * @param ids
	 * @param curId
	 * @return
	 */
	public static int getCount(String str, int type) {
		int len = str.length();
		int chineseCount = 0; // 中文字符个数
		int letterCount = 0; // 英文字母个数
		int blankCount = 0; // 空格个数
		int numCount = 0; // 数字个数
		int otherCount = 0; // 其他字符个数
		for (int i = 0; i < len; i++) {
			char tem = str.charAt(i);
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(tem);
			if ((tem > 'A' && tem < 'Z') || (tem > 'a' && tem < 'z'))// 英文字母
				letterCount++;
			else if (tem == ' ') // 空格
				blankCount++;
			else if (tem > '0' && tem < '9')// 数字
				numCount++;
			else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
					|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
					|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
					|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
				chineseCount++;
			} else { // 其他
				otherCount++;
			}
		}
		switch (type) {
		case -1:
			return chineseCount; // 中文字符个数:
		case 0:
			return letterCount; // 英文字母个数:
		case 1:
			return blankCount; // 空格个数:
		case 2:
			return numCount; // 数字个数:
		case 3:
			return otherCount; // 其他字符个数:
		}
		return otherCount;
	}

	// 用于获取字符串的总的像素单位
	public static int getTotalSize(String str) {
		int chineseCount = getCount(str, -1);
		int letterCount = getCount(str, 0);
		int blankCount = getCount(str, 1);
		int numCount = getCount(str, 2);
		int otherCount = getCount(str, 3);
		return chineseCount + (letterCount + numCount) / 3 + blankCount / 4
				+ otherCount * 3 / 4;
	}

	/**
	 * 获取拼接的url
	 * 
	 * @param url
	 * @param params
	 *            参数
	 * @return
	 */
	public static String getUrl(String url, String params) {
		if (isEmpty(url))
			return url;
		if (url.indexOf("?") > 0) {
			if (isNotEmpty(params))
				url += "&" + params;
			else
				url += "?" + params;
		} else if (isNotEmpty(params))
			url += "?" + params;
		return url;
	}

	/**
	 * 获取拼接的url
	 * 
	 * @param url
	 * @param params
	 *            参数
	 * @return
	 */
	public static String getDefaultDatas(String name, String defaults) {
		// 添加类型与默认值
		// String
		// callbacksDefaults="{beforeAsync:null, beforeClick:null, beforeDblClick:null, beforeRightClick:null, beforeMouseDown:null, beforeMouseUp:null, beforeExpand:null, beforeCollapse:null, beforeRemove:null, onAsyncError:null, onAsyncSuccess:null, onNodeCreated:null, onClick:null, onDblClick:null, onRightClick:null, onMouseDown:null, onMouseUp:null, onExpand:null, onCollapse:null, onRemove:null}";
		// String
		// asyncsDefaults="{enable: true, contentType: \"application/x-www-form-urlencoded\", type: \"post\", dataType: \"text\", url: \"\", autoParam: [\"typeId\",\"parentId\"], otherParam: {\"treeType\":\"JSON.stringify(TreeType)\"}, dataFilter: null }";
		// String
		// datasDefaults="{key: {children: \"children\", name: \"typeName\", title: \"\", url: \"url\"}, simpleData: {enable: false, idKey: \"typeId\", pIdKey: \"parentId\", rootPId: 1 }, keep: {parent: false, leaf: false } }";
		// String
		// viewsDefaults="{addDiyDom: null, autoCancelSelected: true, dblClickExpand: true, expandSpeed: \"fast\", fontCss: {}, nameIsHTML: false, selectedMulti: true, showIcon: true, showLine: true, showTitle: true }";
		// String
		// checksDefaults="{enable: false, autoCheckTrigger: false, chkStyle: \"_consts.checkbox.STYLE\", nocheckInherit: false, chkDisabledInherit: false, radioType: \"_consts.radio.TYPE_LEVEL\", chkboxType: {\"Y\": \"ps\", \"N\": \"ps\"} }";
		// String
		// editsDefaults="{enable: false, editNameSelectAll: false, showRemoveBtn: true, showRenameBtn: true, removeTitle: \"remove\", renameTitle: \"rename\", drag: {autoExpandTrigger: false, isCopy: true, isMove: true, prev: true, next: true, inner: true, minMoveSize: 5, borderMax: 10, borderMin: -5, maxShowNodeNum: 5, autoOpenTime: 500 } }";
		String callbacksDefaults = "{beforeAsync: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], defaults: null }, beforeClick: {type: \"function\", arguments: [\"treeId\", \"treeNode\", \"clickFlag\"], defaults: null }, beforeDblClick: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], defaults: null }, beforeRightClick: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], defaults: null }, beforeMouseDown: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], defaults: null }, beforeMouseUp: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], defaults: null }, beforeExpand: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], defaults: null }, beforeCollapse: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], defaults: null }, beforeRemove: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], defaults: null }, onAsyncError: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\", \"XMLHttpRequest\", \"textStatus\", \"errorThrown\"], defaults: null }, onAsyncSuccess: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\", \"msg\"], defaults: null }, onNodeCreated: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\"], defaults: null }, onClick: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\"], defaults: null }, onDblClick: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\"], defaults: null }, onRightClick: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\"], defaults: null }, onMouseDown: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\"], defaults: null }, onMouseUp: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\"], defaults: null }, onExpand: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\"], defaults: null }, onCollapse: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\"], defaults: null }, beforeCheck: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], from: \"excheck\", defaults: null }, onCheck: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\"], from: \"excheck\", defaults: null }, beforeDrag: {type: \"function\", arguments: [\"treeId\", \"treeNodes\"], from: \"exedit\", defaults: null }, beforeDragOpen: {type: \"function\", arguments: [\"treeId\", \"treeNodes\"], from: \"exedit\", defaults: null }, beforeEditName: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], from: \"exedit\", defaults: null }, beforeDrop: {type: \"function\", arguments: [\"treeId\", \"treeNodes\", \"targetNode\", \"moveType\"], from: \"exedit\", defaults: null }, onDrag: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNodes\"], from: \"exedit\", defaults: null }, onDrop: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNodes\", \"targetNode\", \"moveType\"], from: \"exedit\", defaults: null }, onRemove: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\"], from: \"exedit\", defaults: null }, onRename: {type: \"function\", arguments: [\"event\", \"treeId\", \"treeNode\", \"isCancel\"], from: \"exedit\", defaults: null } }";
		String asyncsDefaults = "{enable: {type: \"boolean\", values: [true, false], defaults: true }, contentType: {type: \"text\", defaults: \"application/x-www-form-urlencoded\"}, type: {type: \"select\", values: [\"post\", \"get\"], defaults: \"post\"}, dataType: {type: \"select\", values: [\"String\", \"xml\", \"html\", \"script\", \"json\", \"JSONP\", \"text\"], defaults: \"text\"}, url: {type: \"text\", defaults: \"{ctx}/demo/ztree/sysZtree/getTreeDatas.do\"}, autoParam: {type: \"array\", defaults: [\"typeId\", \"parentId\"] }, otherParam: {type: \"arrayOrJson\", defaults: {\"treeType\": \"JSON.stringify(TreeType)\"} }, dataFilter: {type: \"function\", defaults: null, arguments: [\"treeId\", \"parentNode\", \"responseData\"] } }";
		String checksDefaults = "{enable: {type: \"boolean\", from: \"excheck\", values: [true, false], defaults: false }, autoCheckTrigger: {type: \"boolean\", from: \"excheck\", values: [true, false], defaults: false }, chkStyle: {type: \"select\", from: \"excheck\", values: [\"checkbox\", \"radio\"], defaults: \"checkbox\"}, nocheckInherit: {type: \"boolean\", from: \"excheck\", values: [true, false], defaults: false }, chkDisabledInherit: {type: \"boolean\", from: \"excheck\", values: [true, false], defaults: false }, radioType: {type: \"select\", from: \"excheck\", values: [\"level\", \"all\"], defaults: \"level\"}, chkboxType: {type: \"jsonChildren\", from: \"excheck\", value: {Y: {type: \"select\", from: \"excheck\", values: [\"p\", \"ps\", \"s\", \"\"], defaults: \"ps\"}, N: {type: \"select\", from: \"excheck\", values: [\"p\", \"ps\", \"s\", \"\"], defaults: \"ps\"} } } }";
		String datasDefaults = "{key: {type: \"jsonChildren\", value: {checked: {type: \"text\", from: \"excheck\", defaults: \"checked\"}, children: {type: \"text\", defaults: \"children\"}, name: {type: \"text\", defaults: \"typeName\"}, title: {type: \"text\", defaults: \"\"}, url: {type: \"text\", defaults: \"\"} } }, simpleData: {type: \"jsonChildren\", value: {enable: {type: \"boolean\", values: [true, false], defaults: true }, idKey: {type: \"text\", defaults: \"typeId\"}, pIdKey: {type: \"text\", defaults: \"parentId\"}, rootPId: {type: \"text\", defaults: 1 } } }, keep: {type: \"jsonChildren\", value: {parent: {type: \"boolean\", values: [true, false], defaults: false }, leaf: {type: \"boolean\", values: [true, false], defaults: false } } } }";
		String editsDefaults = "{enable: {type: \"boolean\", from: \"exedit\", values: [true, false], defaults: false }, editNameSelectAll: {type: \"boolean\", from: \"exedit\", values: [true, false], defaults: false }, showRemoveBtn: {type: \"boolean\", from: \"exedit\", values: [true, false], defaults: true }, showRenameBtn: {type: \"boolean\", from: \"exedit\", values: [true, false], defaults: true }, removeTitle: {type: \"text\", from: \"exedit\", defaults: \"remove\"}, renameTitle: {type: \"text\", from: \"exedit\", defaults: \"rename\"}, drag: {type: \"jsonChildren\", from: \"excheck\", value: {autoExpandTrigger: {type: \"boolean\", values: [true, false], defaults: true }, isCopy: {type: \"boolean\", from: \"exedit\", values: [true, false], defaults: true }, isMove: {type: \"boolean\", from: \"exedit\", values: [true, false], defaults: true }, prev: {type: \"boolean\", from: \"exedit\", values: [true, false], defaults: true }, next: {type: \"boolean\", from: \"exedit\", values: [true, false], defaults: true }, inner: {type: \"boolean\", from: \"exedit\", values: [true, false], defaults: true }, minMoveSize: {type: \"number\", from: \"exedit\", defaults: 5 }, borderMax: {type: \"number\", from: \"exedit\", defaults: 10 }, borderMin: {type: \"number\", from: \"exedit\", defaults: -5 }, maxShowNodeNum: {type: \"number\", from: \"exedit\", defaults: 5 }, autoOpenTime: {type: \"number\", from: \"exedit\", defaults: 500 } } } }";
		String viewsDefaults = "{addDiyDom: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], defaults: null }, addHoverDom: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], from: \"exedit\", defaults: null }, removeHoverDom: {type: \"function\", arguments: [\"treeId\", \"treeNode\"], from: \"exedit\", defaults: null }, autoCancelSelected: {type: \"boolean\", values: [true, false], defaults: false }, dblClickExpand: {type: \"boolean\", values: [true, false], defaults: true }, expandSpeed: {type: \"select\", values: [\"slow\", \"normal\", \"fast\", \"\"], defaults: \"fast\"}, fontCss: {type: \"json\", defaults: \"\"}, nameIsHTML: {type: \"boolean\", values: [true, false], defaults: false }, selectedMulti: {type: \"boolean\", values: [true, false], defaults: true }, showIcon: {type: \"boolean\", values: [true, false], defaults: true }, showLine: {type: \"boolean\", values: [true, false], defaults: true }, showTitle: {type: \"boolean\", values: [true, false], defaults: true }, txtSelectedEnable: {type: \"boolean\", values: [true, false], defaults: false } }";

		JSONObject obj = new JSONObject();
		if (name.equals("callbacks")) {
			obj = JSONObject.parseObject(callbacksDefaults);
		} else if (name.equals("asyncs")) {
			obj = JSONObject.parseObject(asyncsDefaults);
		} else if (name.equals("checks")) {
			obj = JSONObject.parseObject(checksDefaults);
		} else if (name.equals("datas")) {
			obj = JSONObject.parseObject(datasDefaults);
		} else if (name.equals("edits")) {
			obj = JSONObject.parseObject(editsDefaults);
		} else if (name.equals("views")) {
			obj = JSONObject.parseObject(viewsDefaults);
		}
		if (StringUtil.isNotEmpty(defaults)) {
			JSONObject defaultsObj = JSONObject.parseObject(defaults);
			Iterator<?> it = defaultsObj.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (obj.containsKey(key)) {
					obj.put(key, defaultsObj.getString(key));
				}
			}
		}
		return obj.toString();
	}

	/**
	 * 文件夹中字符串替换
	 *
	 * <p>
	 * detailed comment
	 * </p>
	 * 
	 * @author [创建人] zhangxin <br/>
	 *         [创建时间] 2015年5月4日 上午11:15:54 <br/>
	 *         [修改人] Administrator <br/>
	 *         [修改时间] 2015年5月4日 上午11:15:54
	 * @param string
	 * @param oldString
	 * @param newString
	 * @return
	 * @see
	 */
	public static String replace(String string, String oldString,
			String newString) {
		return innerReplace(string, oldString, newString, true);
	}

	private static String innerReplace(String string, String oldString,
			String newString, boolean isAll) {
		if (string == null)
			return "";
		int index = string.indexOf(oldString);
		if (index == -1)
			return string;
		int start = 0, len = oldString.length();
		if (len == 0)
			return string;
		StringBuilder buffer = new StringBuilder(string.length() + len);
		do {
			buffer.append(string.substring(start, index));
			buffer.append(newString);
			start = index + len;
			if (!isAll)
				break;
			index = string.indexOf(oldString, start);
		} while (index != -1);
		buffer.append(string.substring(start));
		return buffer.toString();
	}

	public static int indexOf(String org, String[] arr) {
		int i = -1;
		for (int m = 0; m < arr.length; m++) {
			if (arr[m].equals(org)) {
				i = m;
				break;
			}
		}
		return i;

	}
	
    public static boolean indexOf(String str1, String str2) {
        return str1.indexOf(str2)>=0;
    }
	// public static void main(String[] args) {
	// String s =
	// "<span style=\"color:#ff0000\"><span style=\"font-size:16px\">330<20and ss>sss<span style=\"background-color:#ffff00\">";
	// s = removeHTMLTag(s);
	// logger.info(s);
	//
	// // String s2 = convertLine(s1,false);
	// // logger.info(s2);
	// // String
	// //
	// str="${scriptImpl.getStartUserPos(startUser)==\"&#37096;&#38376;&#32463;&#29702;\"}";
	// // logger.info(htmlEntityToString(str));
	// }

	/**
	 * @author Yangbo 2016-7-22 根据type类型转换对象
	 */
	public static Object parserObject(Object obj, String type) {
		if (BeanUtils.isEmpty(obj))
			return null;
		Object val = obj;
		try {
			String str = obj.toString();
			if (type.equalsIgnoreCase("string")) {
				val = str;
			} else if (type.equalsIgnoreCase("int")) {
				val = Integer.valueOf(Integer.parseInt(str));
			} else if (type.equalsIgnoreCase("float")) {
				val = Float.valueOf(Float.parseFloat(str));
			} else if (type.equalsIgnoreCase("double")) {
				val = Double.valueOf(Double.parseDouble(str));
			} else if (type.equalsIgnoreCase("byte")) {
				val = Byte.valueOf(Byte.parseByte(str));
			} else if (type.equalsIgnoreCase("short")) {
				val = Short.valueOf((short) Short.parseShort(str));
			} else if (type.equalsIgnoreCase("long")) {
				val = Long.valueOf(Long.parseLong(str));
			} else if (type.equalsIgnoreCase("boolean")) {
				if (StringUtils.isNumeric(str))
					val = Boolean.valueOf(Integer.parseInt(str) == 1);
				val = Boolean.valueOf(Boolean.parseBoolean(str));
			} else if (type.equalsIgnoreCase("date")) {
				val = DateFormatUtil.parse(str);
			} else {
				val = str;
			}
		} catch (Exception e) {
		}
		return val;
	}

	public static List<String> stringToList(String str) {
		List list = new ArrayList();
		if ((str != null) && (!str.equals(""))) {
			if ((str.contains("[")) || (str.contains("]"))) {
				String[] strs = str.split(",");
				for (String str1 : strs) {
					if (str1.contains("[")) {
						str1 = str1.replace("[", "");
					}
					if (str1.contains("]")) {
						str1 = str1.replace("]", "");
					}
					str1 = str1.replaceAll("\"", "");
					list.add(str1);
				}
				return list;
			}
			list.add(str);
		}

		return list;
	}

	/**
	 * 根据字符串获取不重复的ID列表。
	 * 
	 * @param ids
	 * @return
	 */
	public static List<Long> getListByStr(String ids) {
		String[] aryUid = ids.split("[,]");
		Set<Long> uidSet = new LinkedHashSet<Long>();
		for (String id : aryUid) {
			Long orgId = Long.parseLong(id);
			uidSet.add(orgId);
		}
		List<Long> list = new ArrayList<Long>();
		list.addAll(uidSet);
		return list;
	}
	/**
	 * 将"id1,id2,id3..."转换成 long[] ids={id1,id2,id3}
	 * @param ids
	 * @param regex
	 * @return
	 * @author scc
	 */
	public static Long[] getLongFromString(String id_Str, String regex) {
		if(isEmpty(id_Str)||isEmpty(regex)){
			return null;
		}
		Long[] ids;
		try {
			String[] ids_Str=id_Str.split(regex);
			ids=new Long[ids_Str.length];
			for(int i=0;i<ids_Str.length;i++){
				ids[i]=Long.valueOf(ids_Str[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return ids;
	}
	/**
	 * 添加单引号
	 * @param ids = "12,23,45";
	 * @return "'12','23','45'"
	 */
	public static String addSingleQuotes(String ids){
		String rtn = ids;
		if(ids.contains(",")){
			rtn = "'"+rtn.replaceAll(",", "','")+"'";
		}
		return ids;		
	}
	/**
	 * 字符串截取
	 * @param content:字符串
	 * @param cutLength：开始截取的值
	 * @return
	 */
	public static Object contentCut(Object content,int cutLength) {
		Object value = "";
		try {
			if(content.toString().length()>cutLength&&!(content.toString().contains("<")&&content.toString().contains(">"))) {
				value = content.toString().substring(0, cutLength) + "...";
			}else {
				value = content;
			}
		}catch(Exception e) {
			value = content;
		}
		return value;
	}

}
