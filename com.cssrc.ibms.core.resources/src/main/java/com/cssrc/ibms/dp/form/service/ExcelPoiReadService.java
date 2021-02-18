package com.cssrc.ibms.dp.form.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import com.cssrc.ibms.core.resources.io.dao.IOCheckItemDefDao;
import com.cssrc.ibms.core.resources.io.dao.IOSignDefDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableTempDao;
import com.cssrc.ibms.dp.form.dao.CheckConditionDao;
import com.cssrc.ibms.dp.form.model.CheckCondition;
@Service
public class ExcelPoiReadService {
	@Resource
	IOTableTempDao tableTempDao;
	@Resource
	IOSignDefDao signDefDao;
	@Resource
	CheckConditionDao checkConditionDao;
	@Resource
	IOCheckItemDefDao ioCheckItemDefDao;
	@Resource
	FormService formService;
	
	
	public  String readExcel(InputStream input,String fcid,long userId,String pid,String exType) throws Exception  {
		String msg = "{\"success\":\"true\",\"context\":\"上传成功\"}";//返回执行情况
		Workbook wb = null;
	try {
		wb = WorkbookFactory.create(input);
		Sheet sheet = wb.getSheetAt(0); // 获得第一个表单-表单的签署标题检查条件注意事项
		int totalRow = sheet.getLastRowNum();// 得到excel的总记录条数
		int columtotal = sheet.getRow(0).getPhysicalNumberOfCells();// 表头总共的列数
		if(columtotal!=3) {
			msg = "{\"success\":\"false\",\"context\":\"当前格式不正确请重新上传！\"}";
			return msg;
		}
		Long tempId=UniqueIdUtil.genId();
		System.out.println("总行数:" + totalRow + ",总列数:" + columtotal);
		int check=0;
		TableTemp tableTemp=new TableTemp();
		List<SignDef> signList=new ArrayList<>();
		String id=String.valueOf(UniqueIdUtil.genId());
		List<CheckCondition> checkConditionList=new ArrayList<>();
		//是否 验收那边的  功能性能验收表模板
		//如果是这个模板, 则会自动更正序号
		boolean ifFunCheckFtl=false;
		//表单是否含序号列
		boolean ifHasOrder=false;
		for (int i = 0; i <= totalRow; i++) {// 遍历行
			for (int j = 0; j < columtotal-2; j++) {
				sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
				System.out.print(sheet.getRow(i).getCell(j).getStringCellValue() + "   ");
				String data=sheet.getRow(i).getCell(j).getStringCellValue();
				if(data.indexOf("表单标题")>=0) {
					String title=sheet.getRow(i).getCell(j+1).getStringCellValue();
					tableTemp.setId(id);
					tableTemp.setName(title); 	
					check++;
				}
				else if(data.indexOf("检查条件")>=0) {
					String conditions=sheet.getRow(i).getCell(j+1).getStringCellValue();
					conditions=conditions.replace("；", ";");
					String conditionses[]=conditions.split(";");
					for(int c=0;c<conditionses.length;c++) {
						CheckCondition checkCondition=new CheckCondition();
						checkCondition.setCheckconditionId(UniqueIdUtil.genId());
						checkCondition.setModule(Long.valueOf(tableTemp.getId()));
						checkCondition.setSequence(c+1);
						checkCondition.setName(conditionses[c]);
						checkConditionList.add(checkCondition);
					}
				}
				else if(data.indexOf("签署人员")>=0) {
					String signed=sheet.getRow(i).getCell(j+1).getStringCellValue();
					signed=signed.replace("；", ";");
					String signs[]=signed.split(";");
					for(int c=0;c<signs.length;c++) {
						SignDef signDef=new SignDef();
						signDef.setName(signs[c]);
						signDef.setTable_temp_id(tableTemp.getId());
						signDef.setId(String.valueOf(UniqueIdUtil.genId()));
						signDef.setOrder(String.valueOf(c+1));
						signList.add(signDef);
					}
					check++;
				}
				else if(data.indexOf("操作人员")>=0) {
					String signed=sheet.getRow(i).getCell(j+1).toString();
					String[] str=signed.split("\\.");
					if(str.length<1) {
						msg = "{\"success\":\"false\",\"context\":\"验收人员个人只能输入数字!!\"}";
						return msg;
					}
					Integer number=Integer.valueOf(str[0]);
					if((Pattern.compile("[0-9]*")).matcher(String.valueOf(number)).matches()) {
						for(int c=0;c<number;c++) {
							SignDef signDef=new SignDef();
							signDef.setName("操作人员");
							signDef.setTable_temp_id(tableTemp.getId());
							signDef.setId(String.valueOf(UniqueIdUtil.genId()));
							signDef.setOrder(String.valueOf(c+1));
							signList.add(signDef);
						}
					}else {
						msg = "{\"success\":\"false\",\"context\":\"操作人员个人只能输入数字!!\"}";
						return msg;
					}
				}
				else if(data.indexOf("验收人员")>=0) {
					String signed=sheet.getRow(i).getCell(j+1).toString();
					String[] str=signed.split("\\.");
					if(str.length<2) {
						msg = "{\"success\":\"false\",\"context\":\"验收人员个人只能输入数字!!\"}";
						return msg;
					}
					Integer number=Integer.valueOf(str[0]);
					if((Pattern.compile("[0-9]*")).matcher(String.valueOf(number)).matches()) {
						for(int c=0;c<number;c++) {
							SignDef signDef=new SignDef();
							signDef.setName("验收人员");
							signDef.setTable_temp_id(tableTemp.getId());
							signDef.setId(String.valueOf(UniqueIdUtil.genId()));
							signDef.setOrder(String.valueOf(c+1));
							signList.add(signDef);
						}
					}else {
						msg = "{\"success\":\"false\",\"context\":\"验收人员个人只能输入数字!!\"}";
						return msg;
					}
				}
				else if(data.indexOf("注意事项")>=0) {
					String attention=sheet.getRow(i).getCell(j+1).getStringCellValue();
					tableTemp.setRemark(attention);
				}else if(data.indexOf("模板种类")>=0) {
					String modelType=sheet.getRow(i).getCell(j+1).getStringCellValue();
					switch (modelType) {
					case "常规验收项目表模板":
						modelType="1";
						break;
					case "功能性能验收项目表模板":
						ifFunCheckFtl=true;
						modelType="2";
						break;
					case "验收报告表模板":
						modelType="6";
						break;
					case "靶场试验问题表":
						modelType="10";
						break;
					case "武器系统所检问题表":
						modelType="13";
						break;
					default:
						modelType="0";
						break;
					}
					tableTemp.setModelType(modelType);
				}
			}
			System.out.println();
		} 
		if(check!=2&&check!=1) {
			msg = "{\"success\":\"false\",\"context\":\"签署或者标题未填!!\"}";
			return msg;
		}
		Sheet sheet2 = wb.getSheetAt(1); // 获得第一个表单-表单的表格
		int totalRow2 = sheet2.getLastRowNum();// 得到excel的总记录条数
		int columtotal2 = sheet2.getRow(0).getPhysicalNumberOfCells();// 表头总共的列数
		for(int i=0;i < columtotal2; i++) {
			String titleData=sheet2.getRow(0).getCell(i).getStringCellValue();
			if(titleData!=null&&titleData.indexOf("注:")>=0) {
				columtotal2=columtotal2-1;
				continue;
			}
		}
		String html="<!?xml version=\"1.0\" encoding=\"UTF-8\"?><html> <table width=\"100%\" class=\"layui-table\"><tbody><tr class=\"firstRow\">";
		int requireval=1;
		List<Integer> actualval=new ArrayList<>();
		//用来自动填充模板第一列的序号
		Integer countRecoder=0;

		for (int i = 0; i <= totalRow2; i++) {// 遍历行 
			for (int j = 0; j < columtotal2; j++) {
				sheet2.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
				String data=sheet2.getRow(i).getCell(j).getStringCellValue();
				System.out.print(sheet2.getRow(i).getCell(j).getStringCellValue() + "   ");
				
				
				if(i==0) {  //i=0表头
					//确定当前表单是不是有序号,  如果有的话,会自动填充序号
					if (data.indexOf("序号")!=-1){
						ifHasOrder=true;
					}
					if(data.indexOf("要求值")>=0) {
						data=data.substring(0, data.indexOf("("));
						html+="<td valign=\"top\" class=\"requireval\" align=\"center\">";
						if (data.indexOf("<")!=-1){
							System.out.println("错误代码:183");
						}

						html+=data+"<i class=\"markup\">(要求值)</i>";
						html+="</td>";
						requireval=j;
					}
					else if(data.indexOf("实测值")>=0) {
						data=data.substring(0, data.indexOf("("));
						html+="<td valign=\"top\" class=\"actualval\" align=\"center\">";
						if (data.indexOf("<")!=-1){
							System.out.println("错误代码:193");
						}
						html+=data+"<i class=\"markup\">(实测值)</i>";
						html+="</td>";
						actualval.add(j);
					}
				
					else{
						html+="<td valign=\"top\" align=\"center\">";
						if (data.indexOf("<")!=-1){
							System.out.println("错误代码:202");
						}
						html+=data;
						html+="</td>";
					}

					if(j==columtotal2-1) {
						html+="</tr>";
					}
				}
				else {
					if(j==0) {
						//如果是功能性能验收表的话, 自动填充序号
						if (ifFunCheckFtl){
							if (i>1){
								countRecoder++;
								html=html+"<tr><td>"+countRecoder+"</td>";
								continue;
							}
						}else if (ifHasOrder){
							if (i>0){
								countRecoder++;
								html=html+"<tr><td>"+countRecoder+"</td>";
								continue;
							}
						}


						html+="<tr>";
					}
					String tagStr="";
					if(j==requireval) {
						tagStr="class=\"requireval\"";
					}
					else if(actualval.contains(j)) {
						tagStr="class=\"actualval\"";
					}
					String title=sheet2.getRow(0).getCell(j).getStringCellValue();
					if(title.indexOf("序号")>=0||title.indexOf("规定值")>=0) {
						html+="<td "+tagStr+" valign=\"top\">";
						if (data.indexOf("<")!=-1){
							data=data.replace("<","&lt;");
						}
						if (data.indexOf(">")!=-1){
							data=data.replace(">","&gt;");
						}
						if (data.indexOf(">")!=-1){
							System.out.println("错误代码:227");
						}
						//如果表达式的值不合法
						if (!ifCheckItemLegal(data)){
							msg= "{\"success\":\"false\",\"context\":\"模板导入失败，请检查"+(i+1)+"行要求值的区间合法性\"}";//返回执行情况
							return msg;
						}
						html+=data;
						html+="</td>";
					}
					else if(title.indexOf("附件")>=0) {
						if(tableTemp.getModelType().equals("2")) {
							if(i==1) {
								html+="<td align=\"center\" valign=\"top\">";
								html+="附件";
								html+="</td>";
							}
							else {
								String fileId=String.valueOf(UniqueIdUtil.genId());
								html+="<td valign=\"middle\" align=\"center\" colspan=\"1\" rowspan=\"1\" photo=\"1\" input=\"1\" class=\"selectTdClass\">";
								html+="<input class=\"dpInputBtn\" type=\"button\" disabled=\"true\" value=\"附件\" onclick=\"addAndShowPhoto(this)\" style=\"width: 45px; height: 24px;\"/>";
								html+="<input type=\"text\" class=\"dpInputText\" style=\"width: 60px;\"/>";
								html+="</td>";
								
							}
						}else {
							String fileId=String.valueOf(UniqueIdUtil.genId());
							html+="<td valign=\"middle\" align=\"center\" colspan=\"1\" rowspan=\"1\" photo=\"1\" input=\"1\" class=\"selectTdClass\">";
							html+="<input class=\"dpInputBtn\" type=\"button\" disabled=\"true\" value=\"附件\" onclick=\"addAndShowPhoto(this)\" style=\"width: 45px; height: 24px;\"/>";
							html+="<input type=\"text\" class=\"dpInputText\" style=\"width: 60px;\"/>";
							html+="</td>";
						}
						
					}
					else if(!"".equals(data)) {
						html+="<td "+tagStr+ " valign=\"top\">";
						if (data.indexOf("<")!=-1){
							System.out.println("错误代码:242");
						}
						html+=data;
						html+="</td>";
					}
					else{
						String str="";
						for(int c=0;c<j;c++) {
							String cell=sheet2.getRow(i).getCell(c).getStringCellValue();
							if(!"".equals(cell)) {
								str+=cell+"/";
							}
						}
						if(!str.equals("")) {
							str=str.substring(0,str.length()-1);
						}
						
						str+=","+title;
						html+="<td valign=\"top\" "+tagStr+" input=\"1\">";
						html+="<input type=\"text\" class=\"dpInputText\" style=\"width: 60px;\"";
						html+=" id="+"\""+UniqueIdUtil.genId()+"\"";
						html+=" /></td>";	
					}
					if(j==columtotal2-1) {
						html+="</tr>";
					}
				}
			}
		}
		html+="</tbody></table></html>";
		tableTemp.setContents(html);
		tableTemp.setStatus("已完成");
		
		tableTemp.setType("1");
		tableTemp.setNumber(id);
		tableTemp.setModuleId("0");
		if(pid!=null||!"".equals(pid)) {
			tableTemp.setProject_id(pid);
		}
		if(!exType.equals("batch")) {
			tableTemp.setTemp_file_id(fcid);
		
		}
		else {
			tableTemp.setTemp_file_id("1");
			tableTemp.setModuleId("");
		}
		
		tableTempDao.insert(tableTemp);
        int index1 = html.indexOf("<table");
        int index2 = html.lastIndexOf("</table>");
        html = html.substring(index1, index2 + 8);
        formService.save(html, id, "1");
		for (SignDef row : signList) {
			signDefDao.insert(row);
		}
		for (CheckCondition row : checkConditionList) {
			checkConditionDao.add(row);
		}
		return msg;
		
	} catch (Exception ex) {
		msg = "{\"success\":\"false\",\"context\":\"模板导入失败，请检查模板格式是否正确\"}";//返回执行情况
		ex.printStackTrace();
		throw new Exception(ex);
		
	}finally {
		 try {
			input.close();
			return msg;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

	/**
	 * 判断当前传来的检查项是不是表达式
	 * 如果是表达式的话,给定的区间是不是合理
	 * 如果合理返回true
	 * 不合理返回false
	 * @param checkItem
	 * @return
	 */
	private boolean ifCheckItemLegal(String checkItem){
		//返回标识符,默认为false
		boolean ifCheckItemLegal=false;
		//如果不含中文
		if (!ifCheckItemIsString(checkItem)){
			//开始校验表达式合理性
				//如果是区间形式
			if (checkItem.indexOf("[")!=-1||checkItem.indexOf("(")!=-1){
				//校验区间是否合理
					//获取第一个值(小)
				String leftNumberString=checkItem.substring(1,checkItem.indexOf(","));
					//获取右操作数结束位置
				Integer rightNumberAddr=checkItem.indexOf("]")==-1?checkItem.indexOf(")"):checkItem.indexOf("]");
				String rightNumberString=checkItem.substring(checkItem.indexOf(",")+1,rightNumberAddr);
				//将获取的左右操作数转为double型
				double leftNumber=Double.parseDouble(leftNumberString);
				double rightNumber=Double.parseDouble(rightNumberString);
				//如果左边小于右边,则返回true
				if (leftNumber<rightNumber){
					ifCheckItemLegal=true;
				}
			}else if(checkItem.indexOf("~")!=-1){
				//	如果是 xx~xx表示法
				String leftNumberString=checkItem.substring(0,checkItem.indexOf("~"));
				//获取右操作数结束位置
				Integer rightNumberAddr=checkItem.length();
				String rightNumberString=checkItem.substring(checkItem.indexOf("~")+1,rightNumberAddr);
				//将获取的左右操作数转为double型
				double leftNumber=Double.parseDouble(leftNumberString);
				double rightNumber=Double.parseDouble(rightNumberString);
				//如果左边小于右边,则返回true
				if (leftNumber<rightNumber){
					ifCheckItemLegal=true;
				}
			}else {
				//如果不是区间, 直接返回true
				ifCheckItemLegal=true;
			}
		}else {
			//如果含有中文
			ifCheckItemLegal=true;
		}
		return ifCheckItemLegal;
	}

	/**
	 * 用正则来判断当前传来的检查项是不是汉字(含中文字符
	 * 如果不是的话返回false
	 * 纯文字的话返回true
	 *
	 * @param checkItem
	 * @return
	 */
	private boolean ifCheckItemIsString(String checkItem){
		//返回标识符,默认为false
		boolean ifCheckItemIsString=false;
		Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
		Matcher m = p.matcher(checkItem);
		if (m.find()) {
			ifCheckItemIsString=true;
		}
		return ifCheckItemIsString;
	}

}
