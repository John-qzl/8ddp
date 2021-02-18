package com.cssrc.ibms.core.form.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.util.appconf.AppUtil;

@Service
public class RelTableService extends BaseService<DataTemplate> {

	@Resource
	private FormFieldService formFieldService;

	@Override
	protected IEntityDao<DataTemplate, Long> getEntityDao() {
		// TODO Auto-generated method stub
		return null;
	}

	// 测试 写一个方法 获取主表的表头 存入String 类型的 List中
//	public ArrayList<String> getMainTableFields(Long mainTableId) {
//		List<FormField> mainTableFields = formFieldService
//				.getByTableId(mainTableId);
//		// List mainTableFieldsDesc = new ArrayList();
//		ArrayList<String> mainTableFieldsDesc = new ArrayList<String>();
//		for (int i = 0; i < mainTableFields.size(); i++) {
//			String fieldDesc = mainTableFields.get(i).getFieldDesc();
//			mainTableFieldsDesc.add(fieldDesc);
//		}
//		return mainTableFieldsDesc;
//	}

	/**
	 * 获取主表与关联表的表头
	 * 
	 * @param mainTableId
	 * @param relTableId
	 * @return
	 */
	public ArrayList<String> getMainRelTableFields(Long mainTableId,
			Long relTableId) {
		ArrayList<String> allTableFieldsDesc = new ArrayList<String>();
		List<FormField> mainTableFields = formFieldService
				.getByTableId(mainTableId);
		allTableFieldsDesc.add("主表ID");
		for (int i = 0; i < mainTableFields.size(); i++) {
			String mainFieldsDesc = mainTableFields.get(i).getFieldDesc();
			allTableFieldsDesc.add(mainFieldsDesc);
		}
		List<FormField> relTableFields = formFieldService
				.getByTableId(relTableId);
		allTableFieldsDesc.add("关联表ID");
		for (int j = 0; j < relTableFields.size(); j++) {
			String relFieldsDesc = relTableFields.get(j).getFieldDesc();
			allTableFieldsDesc.add(relFieldsDesc);
		}
		return allTableFieldsDesc;
	}

	/**
	 * 获取主表的表头 (存英文 fieldName)
	 * 
	 * @param mainTableId
	 * @return
	 */
	public ArrayList<String> getMainTableFieldsName(Long mainTableId) {
		ArrayList<String> mainTableFieldsName = new ArrayList<String>();
		List<FormField> mainTableFields = formFieldService
				.getByTableId(mainTableId);
		mainTableFieldsName.add("ID");
		for (int i = 0; i < mainTableFields.size(); i++) {
			String mainFieldsName = mainTableFields.get(i).getFieldName();
			mainTableFieldsName.add(mainFieldsName);
		}
		return mainTableFieldsName;
	}

	/**
	 * 获取关联表的表头 (存英文 fieldName)
	 * 
	 * @param relTableId
	 * @return
	 */
	public ArrayList<String> getRelTableFieldsName(Long relTableId) {

		ArrayList<String> relTableFieldsName = new ArrayList<String>();
		List<FormField> relTableFields = formFieldService
				.getByTableId(relTableId);
		relTableFieldsName.add("ID");
		for (int j = 0; j < relTableFields.size(); j++) {
			String relFieldsName = relTableFields.get(j).getFieldName();
			relTableFieldsName.add(relFieldsName);
		}
		return relTableFieldsName;
	}

	/**
	 * 导出主表、关联表Excel
	 * @param mainTableDesc
	 * @param mainRelTableFieldsDesc
	 * @param formatRecords
	 * @return
	 * @throws Exception
	 */
	public String exportExcel(String mainTableDesc,
			ArrayList<String> mainRelTableFieldsDesc,
			List<Map<String, Object>> formatRecords) throws Exception {

		// 带有当前时间的表头
		// String title ="主表关联表" + DateFormatUtil.getNowByString("") + ".xls";
		// 创建工作簿对象
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建工作表
		HSSFSheet sheet = workbook.createSheet(mainTableDesc);
		Row header_row = sheet.createRow(0);
		// header_row.setHeight((short)(20*24));高度先去除
		// 表头
		ArrayList<String> headers = mainRelTableFieldsDesc;
		// 表头 循环赋值
		for (int i = 0; i < headers.size(); i++) {
			// 设置列宽 基数为256
			sheet.setColumnWidth(i, 30 * 256);
			Cell cell = header_row.createCell(i);// 动态创建表头行的单元格
			cell.setCellValue(headers.get(i));
		}

		List<List<String>> data = new ArrayList<List<String>>();
		for (Map<String, Object> map : formatRecords) // 一条主表、关联表记录
		{
			List<String> noKey = new ArrayList<String>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String value;
				if (entry.getValue() == null) {
					value = "";
				} else {
					value = entry.getValue().toString();
				}
				noKey.add(value);
			}
			data.add(noKey);
		}
		// for 循环向Excel表格中插入数据（建好表头的情况下）
		for (int i = 0; i < data.size(); i++) {
			Row row = sheet.createRow(i + 1);// 表名、表头2行，数据内容从第3行开始
			// row.setHeight((short)(20*20));
			for (int j = 0; j < data.get(i).size(); j++) {
				Cell cell = row.createCell(j);// 根据测试数据数组的每条记录的长度动态创建相应的单元格
				cell.setCellValue(data.get(i).get(j).toString());// 循环动态插入数据
			}
		}
		String attachPath = AppUtil.getAttachPath();
		FileOutputStream fileOut = new FileOutputStream(attachPath
				+ File.separator + "" + mainTableDesc + "" + ".xls");
		workbook.write(fileOut);
		fileOut.close();
		String fullPath = attachPath + File.separator + "" + mainTableDesc
				+ ".xls";
		return fullPath;
	}

}
