package com.cssrc.ibms.dp.form.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.dp.form.model.DefineCheckType;
import com.cssrc.ibms.dp.form.model.Head;
import com.cssrc.ibms.dp.form.model.Tcell;




/**
 * 根据新的标准表格模板解析
 *@Function Name:  StandExlParseUtil
 *@Description:
 *@Date Created:  2012-12-15 下午02:09:04
 *@Author: cxk
 *@Last Modified:    ,  Date Modified:
 */
public class StandExlParseUtil {
	private HSSFSheet tableInfo;
	private HSSFSheet tableContent;
	//检查表格定义
	private Map<String,String> checkDataMap = new HashMap<String, String>();
	//签署
	private List<Map<String,String>> signDataMapList = new ArrayList<Map<String,String>>();
	//检查条件
	private List<Map<String,String>> conditionMapList = new ArrayList<Map<String,String>>();
	//检查项内容行的集合
	private List<HSSFRow> mutileCellData = new ArrayList<HSSFRow>();
	//头信息结合
	private List<Head> headList = new ArrayList<Head>();
	private Map<Integer,String> cellSign = new HashMap<Integer, String>();

	//产品关联列索引
	private int relationIndex = -1; 
	
	private String check = "1";
	private String input = "2";
	private String remark = "3";
	private String replace = "---";
	
	private StringBuffer context = new StringBuffer();
	private int startIndex = -1;
	
	private String fileName;
	
	public static String tempName;
	
	//2016-7-25 11:19:59(liuyangchao)
	private int normalCellCount = 1;			//描述性Cell的表格数量
	
	
	//初始化解析实例，首先判断sheet数量
	public static StandExlParseUtil getInstanse(InputStream is)
	{
		StandExlParseUtil exlParse = null;
		try {
			HSSFWorkbook work = new HSSFWorkbook(is);
			int number = work.getNumberOfSheets();
			if(number > 1)
			{
				exlParse = new StandExlParseUtil();
				exlParse.setTableInfo(work.getSheetAt(0));
				exlParse.setTableContent(work.getSheetAt(1));
			}						
		} catch (IOException e) {
			e.printStackTrace();
		}
		return exlParse;
	}
	
	public static StandExlParseUtil getInstance(MultipartFile file)
	{
		StandExlParseUtil exlParse = null;
		try {
			HSSFWorkbook work = new HSSFWorkbook(file.getInputStream());
			exlParse = new StandExlParseUtil();
			exlParse.fileName = file.getOriginalFilename();
			
			int number = work.getNumberOfSheets();
			if(number > 1)
			{
				if(!"表格信息".equals(work.getSheetAt(0).getSheetName()))
					exlParse.setParseInfo("缺少表格信息", "error");
				
				if(!"表格内容".equals(work.getSheetAt(1).getSheetName()))
					exlParse.setParseInfo("缺少表格内容", "error");
				
				exlParse.setTableInfo(work.getSheetAt(0));
				exlParse.setTableContent(work.getSheetAt(1));
				Iterator<Row> ite = work.getSheetAt(0).iterator();
				while(ite.hasNext())
				{
					HSSFRow hssfRow = null;
					Row row = ite.next();
					if(row instanceof HSSFRow)
					{
						hssfRow = (HSSFRow)row;
					}
					if(hssfRow.getRowNum() == 1)
						tempName = getCellString(hssfRow.getCell(1));
				}
			}		
			else
				exlParse.setParseInfo("缺少表格信息或表格内容", "error");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return exlParse;
	}
	
	public static StandExlParseUtil getInstance(String filePath)
	{
		StandExlParseUtil exlParse = null;
		try {
			HSSFWorkbook work = new HSSFWorkbook(new FileInputStream(filePath));
			exlParse = new StandExlParseUtil();
			exlParse.fileName = new File(filePath).getName();
			
			int number = work.getNumberOfSheets();
			if(number > 1)
			{
				if(!"表格信息".equals(work.getSheetAt(0).getSheetName()))
					exlParse.setParseInfo("缺少表格信息", "error");
				
				if(!"表格内容".equals(work.getSheetAt(1).getSheetName()))
					exlParse.setParseInfo("缺少表格内容", "error");
				
				exlParse.setTableInfo(work.getSheetAt(0));
				exlParse.setTableContent(work.getSheetAt(1));
			}		
			else
				exlParse.setParseInfo("缺少表格信息或表格内容", "error");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return exlParse;
	}
	
	/*
	 * 解析检查表格定义、签署、检查条件
	 * 此方法后续的方法有：
	 * 				getCheckDataMap();
	 * 				getSignDataMapList();
	 * 				getConditionMapList();
	 */
	public void parseTableInfo()
	{
	//	String tabId_jcbdy = Constants.TABLETEMP_MODELID;
	//	String tabId_jcbdy="001";
		if(tableInfo.getLastRowNum()<3)
			setParseInfo("表格信息不完整", "error");
		
		Iterator<Row> ite = tableInfo.iterator();
		String head = "";
		while(ite.hasNext())
		{
			HSSFRow hssfRow = null;
			Row row = ite.next();
			if(row instanceof HSSFRow)
			{
				hssfRow = (HSSFRow)row;
			}
			if(hssfRow.getRowNum() == 0)
			{
				//表格编号
				head = getCellString(hssfRow.getCell(0));
				String bgbh = getCellString(hssfRow.getCell(1));
				
				if(!"表格编号".equals(head))
					setParseInfo("A1单元格的内容应为表格'表格编号'", "error");
				
				if("".equals(bgbh))
					setParseInfo("表格编号不可为空", "error");
				
				checkDataMap.put("NUMBER", bgbh);
			}
			else if(hssfRow.getRowNum() == 1)
			{
				//表格名称
				head = getCellString(hssfRow.getCell(0));
				String bgmc = getCellString(hssfRow.getCell(1));
				
				if(!"检查表名称".equals(head))
					setParseInfo("单元格A2的内容应为'检查表名称'", "error");
				
				if("".equals(bgmc))
					setParseInfo("检查表名称不可为空", "error");
				
				checkDataMap.put("NAME", bgmc);
			}
			else if(hssfRow.getRowNum() == 2)
			{
				//注意事项
				head = getCellString(hssfRow.getCell(0));
				String zysx = getCellString(hssfRow.getCell(1));
				
				if(!"注意事项".equals(head))
					setParseInfo("A3单元格的内容应为'注意事项'", "error");
				
				/*if("".equals(zysx))
					setParseInfo("注意事项不可为空", "error");*/
				
				checkDataMap.put("M_CZSM", zysx);
			}
			else if(hssfRow.getRowNum() == 3)
			{
				//签署岗位
				head = getCellString(hssfRow.getCell(0));
				if(!"签署".equals(head))
					setParseInfo("A4单元格的内容应为'签署'", "error");
				
				setSignDataMapList(hssfRow);
				
				if(signDataMapList.size()==0 )
					setParseInfo("签署项不可为空", "error");
				
			}
			else if(hssfRow.getRowNum() == 4)
			{
				//检查条件
				setConditionMapList(hssfRow);
			}else if(hssfRow.getRowNum() == 5){
				String mj = getCellString(hssfRow.getCell(1));
				checkDataMap.put("M_SECRECY", mj);
			}
		}
		
		//表格内容空校验
		boolean jcxExist = false;
		Iterator<Row> rows = tableContent.iterator();
		while(rows.hasNext())
		{
			HSSFRow row = (HSSFRow)rows.next();
			if(row.getRowNum()==0)
			{
				Iterator<Cell> cells = row.cellIterator();
				while(cells.hasNext())
				{
					HSSFCell cell = (HSSFCell)cells.next();
					String value = getCellString(cell);
					if(!"".equals(value))
					{
						String prefix = String.valueOf(value.charAt(0));
						if("#".equals(prefix))
						{
							startIndex = cell.getColumnIndex();
							jcxExist = true;
							break;
						}
					}
				}
			}
			
			validate(row);
		}
		if(!jcxExist){
			setParseInfo("检查表中不存在检查项标识（#）", "error");
		}
	}

	public List<Map<String,String>> getSignDataMapList() {
		return signDataMapList;
	}

	//解析签署信息
	private void setSignDataMapList(HSSFRow hssfRow) {
//		String tabId_qs = Constants.SIGNDEF_MODELID;
	//	String tabId_qs ="001";
		Iterator<Cell> ite = hssfRow.cellIterator();
		
		while(ite.hasNext())
		{
			HSSFCell cell = (HSSFCell)ite.next();
			if(cell.getColumnIndex()!=0)
			{
				Map<String,String> dataMap = new HashMap<String, String>();
				String order = CommonTools.Obj2String(cell.getColumnIndex());
				String signValue = getCellString(cell);
				if(!"".equals(signValue))
				{
					dataMap.put("ORDER", order);
					dataMap.put("NAME", signValue);				
					signDataMapList.add(dataMap);
				}
				
			}
		}
	}

	public List<Map<String, String>> getConditionMapList() {
		return conditionMapList;
	}

	//设置检查条件信息
	public void setConditionMapList(HSSFRow hssfRow) {
	//	String tabId_jcbtj = Constants.CONDITION_MODELID;
	//	String tabId_jcbtj ="001";
		Iterator<Cell> ite = hssfRow.cellIterator();
	
		while(ite.hasNext())
		{
			HSSFCell cell = (HSSFCell)ite.next();
			if(cell.getColumnIndex()!=0)
			{
				Map<String,String> dataMap = new HashMap<String, String>();
				String order = CommonTools.Obj2String(cell.getColumnIndex());
				String condition = getCellString(cell);
				if(!"".equals(condition))
				{
					dataMap.put("ORDER", order);
					dataMap.put("NAME", condition);				
					conditionMapList.add(dataMap);
				}
				
			}
		}
	}

	public HSSFSheet getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(HSSFSheet tableInfo) {
		this.tableInfo = tableInfo;
	}

	public HSSFSheet getTableContent() {
		return tableContent;
	}

	public void setTableContent(HSSFSheet tableContent) {
		this.tableContent = tableContent;
	}
	
	public Map<String, String> getCheckDataMap() {
		return checkDataMap;
	}

	//*************************检查内容的解析**************************
	/*
	 * 解析检查内容sheet2
	 * 涉及的表格：
	 * 			header 普通	#	$	
	 * 			检查项定义
	 * 			cell   
	 */
	
	
	/*
	 * excel表格转html
	 * 
	 */
	public String parseTableHtml()
	{
		StringBuffer tableStr = new StringBuffer();
		boolean parseflag = true;
		String tdwidth="style=\"width:"+30+"%;\"";
		List<String>head=new ArrayList<String>();//行标题
		List<Integer>headflag=new ArrayList<Integer>();//行标题标记
		List<String>checkflag=new ArrayList<String>();
		List<Integer>num=new ArrayList<Integer>();
		List<String>content=new ArrayList<String>();
		Iterator<Row> ite = tableContent.iterator();
		tableStr.append("<table border=\"1\" width=\"100%\">");		
		tableStr.append("<thead><tr>");
		while(ite.hasNext())
		{
			HSSFRow hssfRow = null;
			Row row = ite.next();
			if(row instanceof HSSFRow)
			{
				hssfRow = (HSSFRow)row;
			}
			if(hssfRow.getRowNum() == 0)
			{
				int cellNumber = hssfRow.getLastCellNum();
				for(int cellIndex=0;cellIndex<cellNumber;cellIndex++){
					HSSFCell cell = hssfRow.getCell(cellIndex);
					String value = getCellString(cell);
					String prefix = String.valueOf(value.charAt(0));
					if("#".equals(prefix)){
						headflag.add(1);
						head.add(value);
						tableStr.append("<td >"+value.substring(1)+"</td>");
					}else if(CommonTools.ifInStr(value, "是否I类单点,是否II类单点,是否易错难,是否有拧紧力矩要求,是否最后一次动作,是否多媒体项")){
						headflag.add(2);
						head.add(value);
					}
					else{
						headflag.add(0);
						head.add(value);
						tableStr.append("<td>"+value+"</td>");
					}
				}
				tableStr.append("</tr></thead>");
				tableStr.append("<tbody>");	
			}else{
				boolean test = isValidRow(hssfRow);
				if(test){
					num.clear();
					content.clear();
					String flag="";
					tableStr.append("<tr>");
					int cellNumber = hssfRow.getLastCellNum();
					for(int cellIndex=0;cellIndex<cellNumber;cellIndex++){
						HSSFCell cell = hssfRow.getCell(cellIndex);
						String value = getCellString(cell);
						if(headflag.get(cellIndex)==0)
							content.add("<td >"+value+"</td>");
						else if(headflag.get(cellIndex)==1){
							if(value.equals("填写")){
								num.add(cellIndex);
								checkflag.add(value);
							}else if(value.equals("对勾")){
								num.add(cellIndex);
								checkflag.add(value);
							}else if(value.indexOf("+")>=0){
								num.add(cellIndex);
								checkflag.add(value);
							}
						}else if(headflag.get(cellIndex)==2){
							if(head.get(cellIndex).equals("是否I类单点")&&value.equals("是"))
								flag+="ildd=\"1\" ";
							else if(head.get(cellIndex).equals("是否II类单点")&&value.equals("是"))
								flag+="iildd=\"1\" ";
							else if(head.get(cellIndex).equals("是否易错难")&&value.equals("是"))
								flag+="err=\"1\" ";
							else if(head.get(cellIndex).equals("是否有拧紧力矩要求")&&value.equals("是"))
								flag+="tighten=\"1\" ";
							else if(head.get(cellIndex).equals("是否最后一次动作")&&value.equals("是"))
								flag+="lastaction=\"1\" ";
							else if(head.get(cellIndex).equals("是否多媒体项")&&value.equals("是"))
								flag+="photo=\"1\" ";
						}
					}
					for(int i=0;i<num.size();i++){
						if(checkflag.get(i).equals("填写"))
							content.add(num.get(i), "<td  input=\"1\" "+flag+"><input  type=\"text\""+ tdwidth+" readonly=\"readonly\"/>"+"</td>");
						else if(checkflag.get(i).equals("对勾"))
							content.add(num.get(i), "<td  checkbox=\"1\" "+flag+"><input  type=\"checkbox\" "+ tdwidth+" disabled/>"+"</td>");
						else if(checkflag.get(i).indexOf("+")>=0)
							content.add(num.get(i), "<td style=\" white-space:nowrap;\"   input=\"1\" checkbox=\"1\" "+flag+"><input  type=\"text\" "+ tdwidth+" readonly=\"readonly\"/>"+"<input  type=\"checkbox\" "+ tdwidth+" disabled/>"+"</td>");
					}
					for(int i=0;i<content.size();i++)
						tableStr.append(content.get(i));
					tableStr.append("</tr>");
				}else{
					parseflag =false;
					break;
				}
			}
		}
		tableStr.append("</tbody></table>");
		return tableStr.toString();
	}
	
	
	
	public void parseTableContent()
	{
		boolean parseflag = true;
	//	String tabId_jcbdy = Constants.TABLETEMP_MODELID;
	//	String tabId_jcbdy="001";
		Iterator<Row> ite = tableContent.iterator();
		while(ite.hasNext())
		{
			HSSFRow hssfRow = null;
			Row row = ite.next();
			if(row instanceof HSSFRow)
			{
				hssfRow = (HSSFRow)row;
			}
			if(hssfRow.getRowNum() == 0)
			{
				//头信息
				Iterator<Cell> cellIte = hssfRow.cellIterator();
				while(cellIte.hasNext())
				{
					HSSFCell cell = (HSSFCell)cellIte.next();
					String value = getCellString(cell);
					if(!CommonTools.isNullString(value))
					{
						String prefix = String.valueOf(value.charAt(0));
						if("#".equals(prefix))
						{
							//cell信息
							Head head = new Head();
							head.setHeadName(value.substring(1));
							head.setHeadOrder(CommonTools.Obj2String(cell.getColumnIndex()+1));
							head.setHeadSign("#");
							head.setRowInExl(cell.getColumnIndex());
							headList.add(head);
							cellSign.put(cell.getColumnIndex(), "#");
							normalCellCount++;
						}
						else if("$".equals(prefix))
						{
							//备注信息
							Head head = new Head();
							head.setHeadName(value.substring(1));
							head.setHeadOrder(CommonTools.Obj2String(cell.getColumnIndex()+1));
							head.setHeadSign("$");
							head.setRowInExl(cell.getColumnIndex());
							headList.add(head);
							cellSign.put(cell.getColumnIndex(), "$");
							normalCellCount++;
						}
						else if(CommonTools.ifInStr(value, "是否I类单点,是否II类单点,是否易错难,是否有拧紧力矩要求,是否最后一次动作,是否多媒体项"))
						{
							//检查项定义类型给cellSign添加标志位，作为结果项类型的标志
							cellSign.put(cell.getColumnIndex(), "&");
							normalCellCount++;
						}
						else
						{
							//标准头信息
							Head head = new Head();
							head.setHeadName(value);
							head.setHeadOrder(CommonTools.Obj2String(cell.getColumnIndex()+1));
							head.setHeadSign("");
							head.setRowInExl(cell.getColumnIndex());
							headList.add(head);
							if(value.indexOf("[产品名称]")!=-1)
								relationIndex = cell.getColumnIndex();
						}
					}
				}
			}
			else
			{
				boolean test = isValidRow(hssfRow);
				if(test)
					mutileCellData.add(hssfRow);
				
				else{
					parseflag =false;
					break;
				}
			}
		}
		if(parseflag)
			//读取完内容后  插入检查表定义的行数
			checkDataMap.put("ROWNUM", CommonTools.Obj2String(mutileCellData.size()));
		else{
			setParseInfo("检查项不能被合并或者检查项不能为空", "error");
		}
	}
	
	
	private void validate(HSSFRow row)
	{
		Iterator<Cell> iter = row.cellIterator();
		while(iter.hasNext())
		{
			HSSFCell cell = (HSSFCell)iter.next();
			if(CommonTools.isNullString(getCellString(cell)))
			{
				String column = CellReference.convertNumToColString(cell.getColumnIndex());
				
				if(startIndex == -1 || cell.getColumnIndex() < startIndex){
					//setParseInfo("表格内容中"+column+(cell.getRowIndex()+1)+"单元格为空", "warn");
				}
				else
					setParseInfo("表格内容中"+column+(cell.getRowIndex()+1)+"单元格不可为空", "error");
				
				cell.setCellValue(replace);
			}
		}
		
	}
	
	private boolean isValidRow(HSSFRow hssfRow)
	{
		int iteCount = 0;
		boolean flag = false;
		Iterator<Cell> ite = hssfRow.cellIterator();
		List<String> contentList = new ArrayList<String>();
		while(ite.hasNext())
		{
			HSSFCell cell = (HSSFCell)ite.next();
			if(!CommonTools.isNullString(getCellString(cell)))
			{
				iteCount++;
//				if(!contentList.contains(getCellString(cell)))
//					contentList.add(getCellString(cell));
				flag = true;
			}
		}
//		if(contentList.size() <= normalCellCount){
//			flag = false;
//		}
		return flag;
	}
	
	/*
	 * 解析检查项内容
	 * 
	 */
	public List<List<Tcell>> getCellFromContent()
	{
		List<List<Tcell>> allCellList = new ArrayList<List<Tcell>>();
		for(HSSFRow hssfRow : mutileCellData)
		{
			List<Tcell> cellList = new ArrayList<Tcell>();
			String cellOrder = CommonTools.Obj2String(hssfRow.getRowNum());
			int cellNumber = hssfRow.getLastCellNum();
			int cellFlag = 0;
			int cellProperty = 0;
			String jcxms = "";
			for(int cellIndex=0;cellIndex<cellNumber;cellIndex++)
			{
				HSSFCell cell = hssfRow.getCell(cellIndex);
				String cellContent = getCellString(cell);
				String sign = cellSign.get(cellIndex);
				if("#".equals(sign))
				{
					//可能存在两个检查项 “填写+对勾”
					if(cellContent.indexOf("+")>=0)
					{
						String[] cellContentArray = cellContent.split("\\+");
						for(int i=0;i<cellContentArray.length;i++)
						{
							String con = cellContentArray[i];
							Tcell tcell = new Tcell();
							tcell.setIsResultItem("TRUE");
							tcell.setResultItemType(convertResultItem(con));
							tcell.setCellOrder(CommonTools.Obj2String(cellOrder));
							tcell.setJcxms(!jcxms.equals("")? jcxms+headList.get(cellIndex).getHeadName():"");
							tcell.setCellFlag(sign);
							tcell.setRowInExl(cell.getColumnIndex());
							cellList.add(tcell);
						}
					}
					else
					{
						Tcell tcell = new Tcell();
						tcell.setIsResultItem("TRUE");
						tcell.setResultItemType(convertResultItem(cellContent));
						tcell.setCellOrder(cellOrder);
						tcell.setJcxms(!jcxms.equals("")? jcxms+headList.get(cellIndex).getHeadName():"");
						tcell.setCellFlag(sign);
						tcell.setRowInExl(cell.getColumnIndex());
						cellList.add(tcell);
					}
				}
				else if("$".equals(sign))
				{
					Tcell tcell = new Tcell();
					tcell.setIsResultItem("TRUE");
					tcell.setResultItemType(remark);
					tcell.setCellOrder(cellOrder);
					tcell.setJcxms(!jcxms.equals("")? jcxms+headList.get(cellIndex).getHeadName():"");
					tcell.setCellFlag(sign);
					tcell.setRowInExl(cell.getColumnIndex());
					cellList.add(tcell);
				}
				else if("&".equals(sign))
				{
					
					if("是".equals(cellContent))
					{
						if(cellFlag == 0)
						{
							cellProperty += 2;
						}
						else if(cellFlag == 1)
						{
							cellProperty += 64;
						}
						else if(cellFlag == 2)
						{
							cellProperty += 4;
						}
						else if(cellFlag == 3)
						{
							cellProperty += 32;
						}
						else if(cellFlag == 4)
						{
							cellProperty += 8;
						}
						else if(cellFlag == 5)
						{
							cellProperty += 128;
						}
					}
					cellFlag++;
				}
				else
				{
					if(!CommonTools.isNullString(cellContent))
					{
						jcxms = jcxms+cellContent+"/";
					}
					Tcell tcell = new Tcell();
					tcell.setCellContent(cellContent);
					tcell.setIsResultItem("FALSE");
					tcell.setResultItemType("");
					tcell.setCellOrder(cellOrder);
					tcell.setCellFlag(sign);
					tcell.setRowInExl(cell.getColumnIndex());
					cellList.add(tcell);
				}
					
			}
			for(Tcell cell : cellList)
			{
				if("TRUE".equals(cell.getIsResultItem()))
				{
					cell.setCellProperty(String.valueOf(cellProperty));
				}
			}
			allCellList.add(cellList);
		}
		return allCellList;
	}
	
	public DefineCheckType getCheckIdentifyByTCell(Tcell cell)
	{
		DefineCheckType jcx = new DefineCheckType();
		String cellProperty = cell.getCellProperty();
		if(!CommonTools.isNullString(cellProperty))
		{
			jcx.setCheckItem(Integer.valueOf(cell.getCellProperty()));
		}
		jcx.setDescribe(cell.getJcxms());
		jcx.setName(cell.getCellContent_jcx());
		jcx.setType((Integer.parseInt((cell.getResultItemType()))));
		
		return jcx;
	}
	
	
	
	//转换结果项类型
	private String convertResultItem(String in)
	{
		String out = "";
		if("填写".equals(in))
		{
			out = input;
		}
		else if("对勾".equals(in))
		{
			out = check;
		}
		return out;
	}
	
	

	/**
	 * 取cell的值，以字符串返回所有类型的值
	 *@Function Name:  getCellString
	 *@Description: @param cell
	 *@Description: @return 
	 *@Date Created:  2012-8-21 上午10:07:58
	 *@Author:  cxk
	 *@Last Modified:     ,  Date Modified:
	 */
	private static String getCellString(HSSFCell cell) {
		Object result = null;
		if (cell != null) {
			// 单元格类型：Numeric:0,String:1,Formula:2,Blank:3,Boolean:4,Error:5
			int cellType = cell.getCellType();
			switch (cellType) {
			case HSSFCell.CELL_TYPE_STRING:
				result = cell.getStringCellValue();
				break;
			//case HSSFCell.CELL_TYPE_NUMERIC:
			//	result = cell.getNumericCellValue();
			//	break;
			case HSSFCell.CELL_TYPE_FORMULA:
				result = cell.getNumericCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				result = cell.getBooleanCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				result = null;
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				result = null;
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:      
				if (HSSFDateUtil.isCellDateFormatted((HSSFCell) cell)) {      
					double d = cell.getNumericCellValue();      
					Date date = HSSFDateUtil.getJavaDate(d);
					result = CommonTools.date2String(date);
					//tdList.add(DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", date));  
				} else {  
					//result = Math.round(cell.getNumericCellValue());
					result = cell.getNumericCellValue();
				}  
				break;
			default:
				System.out.println("枚举了所有类型");
				break;
			}
		}
		return CommonTools.Obj2String(result);
	}

	public void setParseInfo(String parseInfo, String level)
	{
		this.context.append("["+level+"]模版<<").append(fileName).append(">>").append(parseInfo).append("\n");
//		if("error".equals(level))
//			context.append("<span style='color:red '>[error]模板<<").append(fileName).append(">>").append(parseInfo).append("</span></br>");
//		else
//			context.append("<span style='color:black'>[warn]模板<<").append(fileName).append(">>").append(parseInfo).append("</span></br>");
		
		
	}
	
	/**
	 * 取消合并
	 * @param sheet
	 */
	public void unMergeCell(HSSFSheet sheet)
	{
		int cnt = sheet.getNumMergedRegions();
		for(int num=cnt-1;num>=0;num--)
		{
			CellRangeAddress region = sheet.getMergedRegion(num);
			int firstRow = region.getFirstRow();
			int lastRow = region.getLastRow();
			int firstCell = region.getFirstColumn();
			int lastCell = region.getLastColumn();
			HSSFCell src = sheet.getRow(firstRow).getCell(firstCell);

			sheet.removeMergedRegion(num);
			
			for(int m=firstRow;m<=lastRow;m++)
			{
				HSSFRow row = sheet.getRow(m);
				for(int n=firstCell;n<=lastCell;n++)
				{
					copyCell(src, row.getCell(n));
				}
			}
		}

	}
	
	/**
	 * 单元格拷贝
	 * @param src
	 * @param des
	 */
	private void copyCell(HSSFCell src, HSSFCell des)
	{
		des.setCellStyle(src.getCellStyle()); 

		if(src.getCellComment() != null) 
		{
			des.setCellComment(src.getCellComment());                } 

		int type = src.getCellType();
		des.setCellType(src.getCellType());
		if (type == HSSFCell.CELL_TYPE_NUMERIC) 
		{
			if (HSSFDateUtil.isCellDateFormatted(src)) 
			{
				des.setCellValue(src.getDateCellValue());
			} 
			else 
			{
				des.setCellValue(src.getNumericCellValue());
			}
		} 
		else if (type == HSSFCell.CELL_TYPE_STRING) 
		{
			des.setCellValue(src.getRichStringCellValue());
		} 
		else if (type == HSSFCell.CELL_TYPE_BOOLEAN) {
			des.setCellValue(src.getBooleanCellValue());
		} 
		else if (type == HSSFCell.CELL_TYPE_ERROR) {
			des.setCellErrorValue(src.getErrorCellValue());
		} 
		else if (type == HSSFCell.CELL_TYPE_FORMULA) {
			des.setCellFormula(src.getCellFormula());
		}
		else if (type == HSSFCell.CELL_TYPE_BLANK) {
//			System.out.println("Nothing");
		}     
	}

	public String getPraseInfo()
	{
		return this.context.toString();
	}


	public void setContext(StringBuffer context) {
		this.context = context;
	}

	public int getRelationIndex() {
		return relationIndex;
	}

	public void setRelationIndex(int relationIndex) {
		this.relationIndex = relationIndex;
	}
	
}
