package com.cssrc.ibms.dp.formvalidate.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.dp.form.util.FormUtils;
import com.cssrc.ibms.dp.formvalidate.model.Cell;

/**
 * 单个table的行或列的约简
 * @author scc
 *
 */
public class TableReduction {
	/**
	 * 页眉
	 */
	public static final String front = "tablefront";
	/**
	 * 表头
	 */
	public static final String title = "tabletitle";
	/**
	 * 页脚
	 */
	public static final String tail = "tabletail";
	
	public static final String input = "input";
	public static final String checkbox = "checkbox";
	public static final String photo = "photo";
	/**
	 * 首先：将形式上（用户看上去的）表格，拆分，去除相同的合并
	 * 【比如第一行的td都有相同的行合并，形式上为一行，但是实际上是两行】
	 * 【比如某一列的td都是相同的列合并，形式上为一列，但实际上为两列】
	 * 去除相同的行或列合并后，再判断
	 */
	@SuppressWarnings("unchecked")
	public  static Map<String, Object> reduction(Map<String, Object> msg) {
		List<Element> tables=(List<Element>) msg.get("tables");
		Element table=tables.get(0);
		//获取所有的行
		List<Element> trs = table.selectNodes(".//tr");
		reduceTable(trs,0,trs.size());
		msg.put("html", table.asXML());
		return msg;
	}
	/**
	 * 约简表
	 * @param trs
	 */
	@SuppressWarnings("unchecked")
	public static void reduceTable(List<Element> trs,int startIndex,int lastIndex) {
		String[][] keys=new String[FormUtils.largestRows][FormUtils.largestCols];
		Map<String,Cell> cellMaps=new HashMap<String,Cell>();
		
		Element tr;
		List<Element> tds;
	
		/**************************构造全部数据************************/
		for (int i = startIndex; i < lastIndex; i++) {
			// 第i行
			tr = trs.get(i);
			//获取所有的列
			tds = tr.selectNodes(".//td");
			for (int j = 0; j < tds.size(); j++) {
				// 第j个td
				Element td = tds.get(j);
				String key="c-"+i+"-"+j;
				Cell cell=TdToCell(td,key);
				cellMaps.put(key, cell);
				putKey(keys,i,j,cell.getColspan(),cell.getRowspan(),key);
			}
		}
		/*******************进行数据比对，去除相同行或列的数据*****************/
	//	show(keys);
		//移除相同的列
		removeCols(cellMaps,keys);
	//	System.out.println(table.asXML());
	//	show(keys);
		//移除相同的行
		removeRows(cellMaps,keys);
	//	System.out.println(table.asXML());
		show(keys);
	}
	/**
	 * 如果两行整体合并，进行约简
	 */
	private static void removeRows(Map<String, Cell> cellMaps,String[][] keys) {
		String[] preRow=keys[0];
		for(int i=1;keys[i][0]!=null;i++){
			System.out.println(i);
			//比较每一行
			String[] curRow=keys[i];
			if(common(preRow,curRow)){
				//将preRow中对应的td的rowSize-1
				subRow(keys,i-1,cellMaps);
			}
			preRow=curRow;
		}
	}
	/**
	 * 减去行合并数
	 */
	private static void subRow(String[][] keys,int row, Map<String, Cell> cellMaps) {
		int j=0;
		while(keys[row][j]!=null){
			if(keys[row][j].equals("-1")){
				j++;
				continue;
			}
			Cell cell=cellMaps.get(keys[row][j]);
			keys[row][j]="-1";
			cell.setRowspan(cell.getRowspan()-1);
			try {
				cell.getTd().addAttribute("rowspan", cell.getRowspan()+"");
			} catch (Exception e) {
				System.out.println(keys[row][j]);
				e.printStackTrace();
			}
			j++;
		}
		
	}
	/**
	 *判断两行数据是否相等
	 */
	private static boolean common(String[] preRow, String[] curRow) {
		for(int i=0;i<preRow.length;i++){
			if(preRow[i]==null){
				break;
			}
			if(!preRow[i].equals(curRow[i])){
				return false;
			}
		}
		return true;
	}
	/**
	 * 移除相同列的数据
	 */
	private static void removeCols(Map<String, Cell> cellMaps, String[][] keys) {
		int preCol=0;
		for(int i=1;keys[i][0]!=null;i++){
			//比较每一行
			int  curCol=i;
			if(common(keys,preCol,curCol)){
				//将preCol中对应的td的colSize-1
				subCol(keys,preCol,cellMaps);
			}
			preCol=curCol;
		}
		
	}
	/**
	 * 减去列合并数
	 */
	private static void subCol(String[][] keys, int preCol, Map<String, Cell> cellMaps) {
		int i=0;
		String pre="";
		while(keys[i][preCol]!=null){
			
			Cell cell=cellMaps.get(keys[i][preCol]);
			if(pre.equals(keys[i][preCol])){
				
			}else{
				cell.setColspan(cell.getColspan()-1);
				cell.getTd().addAttribute("colspan", cell.getColspan()+"");
			}
			pre=keys[i][preCol];
			keys[i][preCol]="-1";
			i++;
		}
		
	}

	/**
	 * 判断某两列的数据是否相同
	 */
	private static boolean common(String[][] keys, int preCol, int curCol) {
		int i=0;
		while(keys[i][preCol]!=null){
			if(!keys[i][preCol].equals(keys[i][curCol])){
				return false;
			}
			i++;
		}
		return true;
	}
	/**
	 * 将全部的TD转换成Cell 便于使用
	 */
	private static Cell TdToCell(Element td, String key) {
		Cell cell=new Cell();
		cell.setTd(td);
		if(td.attribute(input) != null){
			cell.setIfInput(true);
		}
		if(td.attribute(checkbox) != null){
			cell.setIfCheckbox(true);
		}
		if(td.attribute(photo)!=null){
			cell.setIfPhoto(true);
		}
		//文本项 获取其内容
		if(!cell.isType()){
			// 获取其内容
			String str = td.getStringValue().trim();//getText()方法不能获取二次标签内的文本，但这个可以
			if(StringUtil.isNotEmpty(str)){
				cell.setContent(str);
			}
		}
		cell.setKey(key);
		// 列合并数
		Attribute colspan = td.attribute("colspan");
		int colSize = colspan == null ? 1 : Integer.valueOf(colspan.getValue());
		// 行合并数
		Attribute rowspan = td.attribute("rowspan");
		int rowSize = rowspan == null ? 1 : Integer.valueOf(rowspan.getValue());
		cell.setColspan(colSize);
		cell.setRowspan(rowSize);
		return cell;
	}
	/**
	 * 打印数据
	 * 
	 */
	private static void show(String[][] keys) {
		for(int i=0;i<15;i++){
			for(int j=0;j<30;j++){
				if(keys[i][j]==null){
					System.out.print("#\t");
				}else{
					System.out.print(keys[i][j]+"\t");
				}
			}
			System.out.println();
		}
	}
	/**
	 * 从(i,j)开始，将str放入行数为rowSize,列数为colSize的矩阵中
	 * 
	 */
	private static void putKey(String[][] keys,int i, int j, int colSize,int rowSize, String str) {
		while(keys[i][j]!=null){
			j++;
		}
		for(int m=i;m<i+rowSize;m++){
			for(int n=j;n<j+colSize;n++){
				keys[m][n]=str;
			}
		}
		
	}
	
}
