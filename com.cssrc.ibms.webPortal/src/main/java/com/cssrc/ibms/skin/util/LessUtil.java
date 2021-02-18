package com.cssrc.ibms.skin.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.lesscss.FileResource;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;
import org.lesscss.LessSource;

import com.cssrc.ibms.core.util.appconf.AppUtil;

public class LessUtil {

	/**
	 * Description: less文件编译为css
	 * Author: WJJ
	 * Date: 2018年5月11日 上午9:47:12
	 */
	public static void compile(String lessPath, String cssPath){
		LessCompiler lessCompiler = new LessCompiler();
		String less = AppUtil.getRealPath(lessPath);
		String css = AppUtil.getRealPath(cssPath);
		try {
			lessCompiler.compile(new File(less), new File(css));
		//} catch (IOException | LessException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	   
//		LessSource lessSource = new LessSource(new FileResource(new File(less)), Charset.forName("UTF-8"));
//	    lessCompiler.compile(lessSource, cssFile);
		
	}
	
	/**
	 * Description: 解析less文件
	 * Author: WJJ
	 * Date: 2018年5月11日 上午9:48:28
	 * @return
	 */
	public static JSONObject resolveLessToMap(String lessPath) {
		String less = AppUtil.getRealPath(lessPath);
		JSONObject jsonObject = new JSONObject();
		
		LessSource lessSource = null;
		try {
			lessSource = new LessSource(new FileResource(new File(less)), Charset.forName("UTF-8"));
			String lessContent = lessSource.getContent();
			
			Pattern pattern = Pattern.compile("(?<=\\n).*(?=;)");
			Matcher matcher = pattern.matcher(lessContent);

			while (matcher.find()) {
				String[] sAry = matcher.group().split(":");
				//json.put(sAry[0].trim(), sAry[1].replace("\"", "").trim());
				jsonObject.put(sAry[0].trim(), sAry[1].trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}
