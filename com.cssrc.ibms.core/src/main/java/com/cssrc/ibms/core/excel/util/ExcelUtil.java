package com.cssrc.ibms.core.excel.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.file.FileOperator;

/**
 * 一些工具方法
 * 
 * @author zhulongchao
 * 
 */
public class ExcelUtil {

	/**
	 * 获取工作表的行数
	 * 
	 * @param sheet
	 *            HSSFSheet表对象
	 * @return 表行数
	 */
	public static int getLastRowNum(HSSFSheet sheet) {
		int lastRowNum = sheet.getLastRowNum();
		if (lastRowNum == 0) {
			lastRowNum = sheet.getPhysicalNumberOfRows() - 1;
		}
		return lastRowNum;
	}

	/**
	 * 获取该行第一个单元格的下标
	 * 
	 * @param row
	 *            行对象
	 * @return 第一个单元格下标，从0开始
	 */
	public static int getFirstCellNum(HSSFRow row) {
		return row.getFirstCellNum();
	}

	/**
	 * 获取该行最后一个单元格的下标
	 * 
	 * @param row
	 *            行对象
	 * @return 最后一个单元格下标，从0开始
	 */
	public static int getLastCellNum(HSSFRow row) {
		return row.getLastCellNum();
	}

	/**
	 * 获取POI的行对象
	 * 
	 * @param sheet
	 *            表对象
	 * @param row
	 *            行号，从0开始
	 * @return
	 */
	public static HSSFRow getHSSFRow(HSSFSheet sheet, int row) {
		if (row < 0) {
			row = 0;
		}
		HSSFRow r = sheet.getRow(row);
		if (r == null) {
			r = sheet.createRow(row);
		}
		return r;
	}

	/**
	 * 获取单元格对象
	 * 
	 * @param sheet
	 *            表对象
	 * @param row
	 *            行，从0开始
	 * @param col
	 *            列，从0开始
	 * @return row行col列的单元格对象
	 */
	public static HSSFCell getHSSFCell(HSSFSheet sheet, int row, int col) {
		HSSFRow r = getHSSFRow(sheet, row);
		return getHSSFCell(r, col);
	}

	/**
	 * 获取单元格对象
	 * 
	 * @param row
	 *            行，从0开始
	 * @param col
	 *            列，从0开始
	 * @return 指定行对象上第col行的单元格
	 */
	public static HSSFCell getHSSFCell(HSSFRow row, int col) {
		if (col < 0) {
			col = 0;
		}
		HSSFCell c = row.getCell(col);
		c = c == null ? row.createCell(col) : c;
		return c;
	}

	/**
	 * 获取工作表对象
	 * 
	 * @param workbook
	 *            工作簿对象
	 * @param index
	 *            表下标，从0开始
	 * @return
	 */
	public static HSSFSheet getHSSFSheet(HSSFWorkbook workbook, int index) {
		if (index < 0) {
			index = 0;
		}
		if (index > workbook.getNumberOfSheets() - 1) {
			workbook.createSheet();
			return workbook.getSheetAt(workbook.getNumberOfSheets() - 1);
		} else {
			return workbook.getSheetAt(index);
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param workBook
	 * @param fileName
	 * @param response
	 * @throws IOException
	 */
	public static void downloadExcel(HSSFWorkbook workBook, String fileName,
			HttpServletResponse response) throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ URLEncoder.encode(fileName, "UTF-8") + ".xls");
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			workBook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				os.close();
		}
	}

	/**
	 * 生成Excel 并下载
	 * 
	 * @param title
	 * @param headers
	 * @param list
	 * @param response
	 */
	public static void exportToExcel(String title, String[] headers,
			List<Object[]> list, HttpServletResponse response) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(title);
		// 标题
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell;
		for (int i = 0; i < headers.length; i++) {
			cell = row.createCell(i);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 数据
		String val;
		HSSFRichTextString richString;
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 1);
			Object[] objs = list.get(i);
			for (int j = 0; j < objs.length; j++) {
				cell = row.createCell(j);
				if (objs[j] instanceof Date) {
					Date date = (Date) objs[j];
					val=DateUtil.dateFormat(date);
				} else {
					val = objs[j].toString();
				}
				richString = new HSSFRichTextString(val);
				cell.setCellValue(richString);
			}

		}

		response.setContentType("application/vnd.ms-excel");
		String fileName = "task";
		try {
			fileName = new String((title + DateUtil.fileDateFormat(DateUtil.getCurrentDate()) + ".xls").getBytes("GBK"),"ISO8859_1");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setHeader("Content-disposition", "attachment; filename="+ fileName);
		try {
			ServletOutputStream ouputStream = response.getOutputStream();
			workbook.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void writeExcel(String fullPath,HSSFWorkbook workBook)  throws IOException {
		OutputStream os = null;
		try {
			FileOperator.createFolder(fullPath, true);
			os = new FileOutputStream(fullPath);
			workBook.write(os);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (os != null)
				os.close();
		}
	}
}
