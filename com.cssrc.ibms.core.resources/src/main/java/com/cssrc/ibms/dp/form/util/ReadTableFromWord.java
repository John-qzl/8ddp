package com.cssrc.ibms.dp.form.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ReadTableFromWord {
	
	String filepath;//文件路径
	List<String> tableTitle;//表格表头

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public List<String> getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(List<String> tableTitle) {
		this.tableTitle = tableTitle;
	}

	public ReadTableFromWord(String filepath, List<String> tableTitle){
		this.filepath = filepath;
		this.tableTitle = tableTitle;
	}
	
	public XWPFTable getExcelFromTable() throws Exception{
		XWPFTable tempTable = null;
		InputStream is = new FileInputStream(filepath);
		XWPFDocument doc = new XWPFDocument(is);
	/*	List<XWPFParagraph> paras = doc.getParagraphs();
		for (XWPFParagraph para : paras) {
		     //当前段落的属性
		//	CTPPr pr = para.getCTP().getPPr();
		     System.out.println(para.getText());
		}*/
		//获取文档中所有的表格
		List<XWPFTable> tables = doc.getTables();
		/*List<XWPFTableRow> rows;
		List<XWPFTableCell> cells;*/
		for (XWPFTable table : tables) {
			boolean searchFlag = searchTable(table,tableTitle);
			//校验通过则获取行列
			if(searchFlag){
				/*//获取表格对应的行
				rows = table.getRows();
				for (XWPFTableRow row : rows) {
					//获取行对应的单元格
					cells = row.getTableCells();
		            for (XWPFTableCell cell : cells) {
		                System.out.println(cell.getText());
		            }
				}*/
				tempTable = table;
				break;
			}
		}
		close(is);
		return tempTable;
	}
	
	/**
	 * 通过获取表格中第一行表头与传入表头字段进行匹配，匹配成功则获取
	 * @param table
	 * @param tableTitle
	 * @return
	 * @throws Exception
	 */
	public static boolean searchTable(XWPFTable table,List<String> tableTitle) throws Exception{
		boolean flag = true;
		XWPFTableRow row = table.getRows().get(0);
		List<XWPFTableCell>	cells = row.getTableCells();
		//表头数量不一致直接返回false
        if(cells.size()!=tableTitle.size()){
        	return false;
        }
		int cindex = 0;
        for (XWPFTableCell cell : cells) {
        	System.out.println(cell.getText());
        	if(cell.getText().trim().equalsIgnoreCase(tableTitle.get(cindex))){
        		cindex++;
        	}
        	else{
        		flag = false;
        		break;
        	}
        }
		return flag;
	}
	
	
	/**
    * 通过XWPFDocument对内容进行访问。对于XWPF文档而言，用这种方式进行读操作更佳。
    * @throws Exception
    */
	public static void testReadByDoc() throws Exception {
      InputStream is = new FileInputStream("E:\\123123.doc");
//      XWPFDocument doc = new XWPFDocument(OPCPackage.open(is));
      XWPFDocument doc = new XWPFDocument(POIXMLDocument.openPackage("E:\\123123.doc"));
//      XWPFDocument doc = new XWPFDocument(is);
      List<XWPFParagraph> paras = doc.getParagraphs();
      for (XWPFParagraph para : paras) {
         //当前段落的属性
//		       CTPPr pr = para.getCTP().getPPr();
         System.out.println(para.getText());
      }
      //获取文档中所有的表格
      List<XWPFTable> tables = doc.getTables();
      List<XWPFTableRow> rows;
      List<XWPFTableCell> cells;
      for (XWPFTable table : tables) {
         //表格属性
//		       CTTblPr pr = table.getCTTbl().getTblPr();
         //获取表格对应的行
         rows = table.getRows();
         for (XWPFTableRow row : rows) {
            //获取行对应的单元格
            cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                System.out.println(cell.getText());;
            }
         }
      }
      close(is);
   }
  
   /**
    * 关闭输入流
    * @param is
    */
   private static void close(InputStream is) {
      if (is != null) {
         try {
            is.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
		
   /**
    * 将table对象转换为jsonArray对象
    * @param table
    * @return
    */
   public JSONArray convertXWPFTableToJSONArray(XWPFTable table){
	   JSONArray array = new JSONArray();
	   List<XWPFTableRow> rows;
	   List<XWPFTableCell> cells;
	   rows = table.getRows();
	   int rowIndex =0;
		for (XWPFTableRow row : rows) {
		   if(rowIndex == 0){
			   rowIndex++;
			   continue;
		   }
		   int lineIndex = 0;
		   //获取行对应的单元格
		   cells = row.getTableCells();
		   JSONObject object = new JSONObject();
           for (XWPFTableCell cell : cells) {
        	   object.put(rowIndex+"_"+lineIndex, cell.getText());
        	   lineIndex++;
               System.out.println(cell.getText());
           }
           array.add(object);
           rowIndex++;
		}
		Collections.reverse(array);
		return array;
   }
   
   public static void main(String[] args){
	   try {
		   testReadByDoc();
		} catch (Exception e) {
			e.printStackTrace();
		}
   }
}  

