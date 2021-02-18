package com.cssrc.ibms.dp.formvalidate.inf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;





import com.cssrc.ibms.dp.formvalidate.service.TableReduction;

public abstract class IFormValidate implements IValidate {
	

	@SuppressWarnings("unchecked")
	public Map<String, Object> readHtml(String html) {
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("success", "true");
		msg.put("html", html);
		Document tableDoc = null;
		String info = "";

		// 去除html中的空格 空行
		html = html.replaceAll("&nbsp;", " ");
		html = html.replaceAll("&nbsp", " ");
		html = html.replaceAll("<br/>", " ");
		html = html.replaceAll("<BR/>", " ");
		
		int index1 = html.indexOf("<table");
		int index2 = html.lastIndexOf("</table>");
		if (index1 == -1 || index2 == -1) {
			info = "无法识别模板中的表单数据，请确认已粘贴或插入表格。";
			msg.put("success", "false");
			msg.put("info", info);
			return msg;
		}
		try {
			html = "<html>"+html.substring(index1, index2 + 8)+"</html>";
		} catch (Exception e) {
			info = "无法识别模板中的表单数据，请确认已粘贴或插入表格。";
			msg.put("success", "false");
			msg.put("info", info);
			return msg;
		}

		try {
			tableDoc = DocumentHelper.parseText(html);
		} catch (DocumentException e) {
			e.printStackTrace();
			info = "无法解析模板：请检查表单文件的闭合情况" + e.getMessage();
			System.err.println(html);
			msg.put("success", "false");
			msg.put("info", info);
			return msg;
		}
		try {
			List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");
			if (tables.size() != 1) {
				info = "单个模板中有且仅有单个表单，请不要设计或粘贴两个表单";
				msg.put("success", "false");
				msg.put("info", info);
				return msg;
			}
			msg.put("tables", tables);
		} catch (Exception e) {
			info = "从模板数据中获取表单失败，请确认表单模板中包含表单数据" + e.getMessage();
			msg.put("success", "false");
			msg.put("info", info);
			return msg;
		}
		msg.put("tableDoc", tableDoc);
		return msg;
	}

	/**
	 * 如果发现返回的是失败信息 ，则返回true
	 */
	protected boolean validateMsg(Map<String, Object> msg) {
		if (msg.get("success").toString().equals("false")) {
			return true;
		}
		return false;
	}

	public Map<String, Object> baseValidate(String html) {
		// 读取html数据 转换成dom对象
		Map<String, Object> msg = readHtml(html);
		if (validateMsg(msg)) {
			return msg;
		}
		// 检查是否含有输入项，若无输入项给出提示
		boolean containsInput = checkInput(html);
		Map<String, Object> containsCell = checkCell(html);//判断是否有空白单元格
		Object result = containsCell.get("result");
		Object checkcellTr = containsCell.get("checkcellTr");
		Object checkcellTd = containsCell.get("checkcellTd");
		Map<String, Object> containsColumn = checkColumn(html);//判断有检查项列是否有非检查项
		Object Columnresult = containsColumn.get("result");
		Object checkcolumnTr = containsColumn.get("checkcolumnTr");
		Object checkcolumnTd = containsColumn.get("checkcolumnTd");
		Map<String, Object> checkTitle = checkTitle(html);//判断表单第一行是否有检查项
		Object titleResult = checkTitle.get("result");
		if (containsInput) {
			String info = "请插入文本框或确认框";
			msg.put("success", "false");
			msg.put("info", info);
			return msg;
		}else{
			if (result=="1") {
				String info = "第"+checkcellTr+"行，第"+checkcellTd+"列存在空白单元格";
				msg.put("success", "false");
				msg.put("info", info);
				return msg;
			}else{
//				if(Columnresult=="1"){
//					String info = "第"+checkcolumnTr+"行，第"+checkcolumnTd+"列存在非检查项";
//					msg.put("success", "false");
//					msg.put("info", info);
//					return msg;
//				}
			/*	else{
					if(titleResult=="1"){
						String info = "表单第一行不允许存在检查项，请检查";
						msg.put("success", "false");
						msg.put("info", info);
						return msg;
					}
				}*/
			}
		}
		return msg;
	}

	private boolean checkInput(String html) {
		// input="1" class="selectTdClass" checkbox="1"
		if (html.contains("input=\"1\"") || html.contains("checkbox=\"1\"")) {
			return false;
		}
		return true;
	}
	/**
	 * Description : 判断是否有空白单元格
	 * Author : XYF
	 * Date : 2018年8月20日下午6:08:08
	 * Return : Map<String,Object>
	 */
	private Map<String, Object> checkCell(String html) {
		Document tableDoc;
		String result ="0";
		Map<String, Object> checkcells = new HashMap<String, Object>();
		String checkcellTr="";
		String checkcellTd="";
		html = html.replaceAll("&nbsp;", " ");
		html = html.replaceAll("&nbsp", " ");
		html = html.replaceAll("<br/>", " ");
		html = html.replaceAll("<BR/>", " ");
		int index1 = html.indexOf("<table");
		int index2 = html.lastIndexOf("</table>");
		html = "<html>"+html.substring(index1, index2 + 8)+"</html>";
		try {
			
			tableDoc = DocumentHelper.parseText(html);
			List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");
			for (Element table : tables) {
				List<Element> trs = table.selectNodes(".//tr");
				for(int i=0;i<trs.size();i++){
					Element tr = trs.get(i);
					List<Element> tds = tr.selectNodes(".//td");
					for(int j=0;j<tds.size();j++){
						// 第j个单元格
						Element td = tds.get(j);
						// 获取其内容
						String string = td.asXML(); 
						//获取第j个单元格input的个数
						List<Element> tdinputs=td.selectNodes(".//input");
						String tdval= td.getStringValue();
						if(tdinputs.size()==0){
							if(" ".equals(tdval)){
								result = "1";
								checkcellTr=Long.toString(i+1);
								checkcellTd=Long.toString(j+1);
								td.addAttribute("style", "color:red");
								break;
							}
							
						}
					}
					if(result=="1"){
						break;
					}
				}
			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkcells.put("result", result);
		checkcells.put("checkcellTr", checkcellTr);
		checkcells.put("checkcellTd", checkcellTd);
		return checkcells;
	}

	/**
	 * Description : 判断有检查项的列是否有非检查项
	 * Author : XYF
	 * Date : 2018年8月20日下午6:08:45
	 * Return : Map<String,Object>
	 */
	private Map<String, Object> checkColumn(String html) {
		Document tableDoc;
		String result ="0";
		Map<String, Object> checkcolumns = new HashMap<String, Object>();
		List<String> checkcolumnTds = new ArrayList<String>();
		String checkcolumnTr="";
		String checkcolumnTd="";
		html = html.replaceAll("&nbsp;", " ");
		html = html.replaceAll("&nbsp", " ");
		html = html.replaceAll("<br/>", " ");
		html = html.replaceAll("<BR/>", " ");
		int index1 = html.indexOf("<table");
		int index2 = html.lastIndexOf("</table>");
		html = "<html>"+html.substring(index1, index2 + 8)+"</html>";
		try {
			
			tableDoc = DocumentHelper.parseText(html);
			List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");
			for (Element table : tables) {
				List<Element> trs = table.selectNodes(".//tr");
				for(int i=1;i<trs.size();i++){
					Element tr = trs.get(i);
					List<Element> tds = tr.selectNodes(".//td");
					for(int j=0;j<tds.size();j++){
						String J = Long.toString(j);
						// 第j个单元格
						Element td = tds.get(j);
						// 获取其内容
						String string = td.asXML();
						//获取第j个单元格input的个数
						List<Element> tdinputs=td.selectNodes(".//input");
						if(tdinputs.size()!=0){
							String column = "";
							for(int n=0;n<checkcolumnTds.size();n++){
								String Td=checkcolumnTds.get(n);
								if(Td.equals(J)){
									column="yes";
									break;
								}
							}
							if("".equals(column)){
								checkcolumnTds.add(Long.toString(j));
							}
							
						}
					}
				}
				for(int t=0;t<checkcolumnTds.size();t++){
					checkcolumnTd = checkcolumnTds.get(t);
					for(int i=1;i<trs.size();i++){
						Element tr = trs.get(i);
						List<Element> tds = tr.selectNodes(".//td");
						Element td = tds.get(Integer.parseInt(checkcolumnTd));
						List<Element> tdinputs=td.selectNodes(".//input");
						if(tdinputs.size()==0){
							result = "1";
							checkcolumnTr=Long.toString(i+1);
							checkcolumnTd=Long.toString(Integer.parseInt(checkcolumnTd)+1);
							td.addAttribute("style", "color:red");
							break;
						}
						
					}
					if(result=="1"){
						break;
					}
				}
			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkcolumns.put("result", result);
		checkcolumns.put("checkcolumnTr", checkcolumnTr);
		checkcolumns.put("checkcolumnTd", checkcolumnTd);
		return checkcolumns;
	
		
		
	}
	
	/**
	 * Description : 校验表单模版第一行是否有检查项
	 * Author : XYF
	 * Date : 2018年9月6日下午6:17:06
	 * Return : Map<String,Object>
	 */
	private Map<String, Object> checkTitle(String html) {
		Document tableDoc;
		String titleResult ="0";
		Map<String, Object> checktitle = new HashMap<String, Object>();
		html = html.replaceAll("&nbsp;", " ");
		html = html.replaceAll("&nbsp", " ");
		html = html.replaceAll("<br/>", " ");
		html = html.replaceAll("<BR/>", " ");
		int index1 = html.indexOf("<table");
		int index2 = html.lastIndexOf("</table>");
		html = "<html>"+html.substring(index1, index2 + 8)+"</html>";
		try {
			
			tableDoc = DocumentHelper.parseText(html);
			List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");
			for (Element table : tables) {
				List<Element> trs = table.selectNodes(".//tr");
				Element tr = trs.get(0);
				List<Element> tds = tr.selectNodes(".//td");
				for(int j=0;j<tds.size();j++){
					String J = Long.toString(j);
					// 第j个单元格
					Element td = tds.get(j);
					// 获取其内容
					String string = td.asXML();
					//获取第j个单元格input的个数
					List<Element> tdinputs=td.selectNodes(".//input");
					if(tdinputs.size()!=0){
						titleResult = "1";
						break;
					}
				}
				
			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checktitle.put("result", titleResult);
		return checktitle;
	}
	@Override
	public Map<String, Object> validate(String html, String formtempId) {
		//首先进行基础校验
		Map<String, Object> msg;
		msg = baseValidate(html);
		if (validateMsg(msg)) {
			return msg;
		}
		//将html进行行或者列的约简
		TableReduction.reduction(msg);
		
		//针对不同的表单类别进行特殊部分的校验
		specialValidate(msg);
		return msg;
	}
	/**
	 * 根据表单模板的类型进行特殊部分的校验
	 * @param msg
	 * @return
	 */
	public abstract Map<String, Object> specialValidate(Map<String, Object> msg);
	/**
	 * 判断标题行是否含有行或列合并
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> validateTitleTr(Map<String, Object> msg,Element tr) {
		//tds 内的行或列colspan和rowspan不能大于1
		List<Element> tds = tr.selectNodes(".//td");
		for (int j = 0; j < tds.size(); j++) {
			// 第j个td
			Element td = tds.get(j);
			// 列合并数
			Attribute colspan = td.attribute("colspan");
			int colSize = colspan == null ? 1 : Integer.valueOf(colspan.getValue());
			if(colSize>1){
				String info="标题中含有"+colSize+"列合并，请修改红色框的单元格(可将单元格拆分)";
				td.addAttribute("style", "color:red");
				msg.put("success", "false");
				msg.put("info", info);
				msg.put("html", ((List<Element>) msg.get("tables")).get(0).asXML());
				return msg;
			}
			// 行合并数
			Attribute rowspan = td.attribute("rowspan");
			int rowSize = rowspan == null ? 1 : Integer.valueOf(rowspan.getValue());
			if(rowSize>1){
				String info="标题中含有"+rowSize+"行合并，请修改红色框的单元格(可将单元格拆分)";
				td.addAttribute("style", "color:red");
				msg.put("success", "false");
				msg.put("info", info);
				msg.put("html", ((List<Element>) msg.get("tables")).get(0).asXML());
				return msg;
			}
		}
		return msg;
	}
	/**
	 * 根据行标记类型获取行所在的下标
	 * @param trs
	 * @param typeStr
	 * @return
	 */
	protected int getSignIndex(List<Element> trs, String typeStr) {
		int index=-1;
		for(int i=0;i<trs.size();i++){
			Element tr=trs.get(i);
			if(tr.attribute("type") != null&&tr.attribute("type").getValue().equals(typeStr)){
				index=i;
				break;
			}
		}
		return index;
	}
}
