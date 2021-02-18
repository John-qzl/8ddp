package com.cssrc.ibms.core.resources.product.util;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.core.excel.reader.ExcelReader;
import com.cssrc.ibms.core.excel.reader.ExcelReader2007;
import com.cssrc.ibms.core.excel.reader.TableEntity;

/**
 * @description excel读取工具类
 * @author xie chen
 * @date 2019年12月6日 下午3:55:38
 * @version V1.0
 */
public class ExcelReadUtil {
	
	/**
	 * @Desc 判断是否为2007版本（xlsx：2007，xls：2003）
	 * @param fileName
	 * @return
	 */
	public static boolean isExcel2007(String fileName) {
		return fileName.endsWith(".xlsx");
	}
	
	/**
	 * @Desc
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static TableEntity readFile(MultipartFile file) throws Exception{
		boolean isExcel2007 = isExcel2007(file.getOriginalFilename());
		InputStream inputStream = file.getInputStream();
		if (isExcel2007) {
			// 2007版本
			ExcelReader2007 excel2007 = new ExcelReader2007();
			return excel2007.readFile(inputStream);
		} else {
			// 2003版本
			ExcelReader excel = new ExcelReader();
			return excel.readFile(inputStream);
		}
	}

}
