package com.cssrc.ibms.dp.form.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cssrc.ibms.core.form.util.FormUtil;
import com.cssrc.ibms.dp.form.util.FormUtils;


/**
*@author vector
*@date 2017年10月13日 上午10:30:00
*@Description 
*/
public class Test {
	public static  String titleStyle="color: rgb(255, 0, 0)";
	public static  String inputStyle="input";
	public static String[][] title=new String[100][100];//行列标题
	public static List<String> inputTitles=new ArrayList<String>();
	public  static String read(String pathname){
		StringBuffer stringBuffer=new StringBuffer();
		 try {
             File filename = new File(pathname);
             InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); 
             BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
             String line = "";  
             line = br.readLine();
             while (line != null) {  
                 stringBuffer.append(line.trim());
                 line = br.readLine();
             }  
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
		 return stringBuffer.toString();
	}
	@SuppressWarnings("unchecked")
/*	public static void main(String[] args) {
      String pathname = "C:\\Users\\User\\Desktop\\tt1.html";   
        String str=read(pathname);
       str=str.replaceAll("&","#");
        int rowSize=0;
        int colSize=0;
        str="<html>"+str+"</html>";
   //   String s= FormUtils.getTableRes(str);
  //    System.out.println(s);
//        try {
//			Document html=DocumentHelper.parseText(str);
//			//List<Element> tables=html.getRootElement().selectNodes(".//table");
//			List<Element> divs=html.getRootElement().selectNodes(".//div");
//			for(Element div:divs){
//				FormUtils.checkForm1Properties(div);
//			}
//        
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
		       
	}
	*/
	
	public static void main(String[] args) {
        String pathname = "C:\\Users\\User\\Desktop\\tt1.html";   
        String str=read(pathname);
       str=str.replaceAll("&","#");
        int rowSize=0;
        int colSize=0;
        str="<html>"+str+"</html>";
        try {
			Document html=DocumentHelper.parseText(str);
			List<Element> tables=html.getRootElement().selectNodes(".//table");
			for(Element table:tables){
				List<Element> trs=table.selectNodes(".//tr");
			
				for(int i=0;i<trs.size();i++){
					//第i行
					Element tr=trs.get(i);
					List<Element> tds=tr.selectNodes(".//td");
				
					for(int j=0;j<tds.size();j++){
						//第j个单元格
						Element td=tds.get(j);
						//获取其内容
						String string=td.asXML();
						String content="#";
						//判断是否为标题
						if(string.contains(titleStyle)){
							content=getTitleContent(td);
						}
						//判断是否为输入项
						else if(string.contains(inputStyle)){
							content=ifInput(td);
						}
						/*
						if(string.contains(inputStyle)){
							content=ifInput(td);
						}else{
							content=getTitleContent(td);
						}
						*/
						//如果为某一行中的合并 //如果为某一列的合并列合并
						Attribute colspan=td.attribute("colspan");
						colSize=colspan==null?1:Integer.valueOf(colspan.getValue());
						Attribute rowspan=td.attribute("rowspan");
						rowSize=rowspan==null?1:Integer.valueOf(rowspan.getValue());
						setTableContent(i,j,colSize,rowSize,content);
					}
				}
				showTitle();
				List<Element> inputs=table.selectNodes(".//input");
				for(int i=0;i< inputs.size();i++){
					inputs.get(i).addAttribute("title", inputTitles.get(i));
				}
				
			}
			System.out.println(html.asXML().replaceAll("#", "&"));
			
			for (int i=0;i<100;i++){
				for(int j=0;j<100;j++)
					System.out.print(title[i][j]+"\t");
				System.out.println();
			}
			for(int i=0;i<inputTitles.size();i++)
				System.out.println(inputTitles.get(i));
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void showTitle() {
		for(int i=0;i<100;i++){
			if(title[i][0]==null){
				break;
			}
			for(int j=0;j<100;j++){
				if(title[i][j]==null){
					break;
				}
				System.out.print(title[i][j]+"\t");
			}
			System.out.println();
		}
		
	}
	//从i j开始往后的cols 往下的rows内 填充content
	private static void setTableContent(int i, int j, int colSize, int rowSize, String content) {
		while(title[i][j]!=null){
			j++;
		}
		for(int x=i;x<i+rowSize;x++){
			for(int y=j;y<j+colSize;y++){
				
				if(content.equals("input")){
					inputTitles.add(getInputTitle(x,y));
				}
				title[x][y]=content;
			}
		}
	}
	//第x行左侧和第y列上侧是否有标题
	private static String getInputTitle(int x, int y) {
		String inputTitle="";
		for(int k=0;k<y;k++){
			if(title[x][k].equals("#")||title[x][k].equals("input")){
				break;
			}else{
			//	inputTitle=title[x][k]+inputTitle;
				inputTitle=inputTitle+title[x][k]+"/";
			}
		}
		inputTitle= inputTitle.substring(0, inputTitle.length()-1)+"，";
		/*for(x--;x>=0;x--){
			if(title[x][y].equals("#")||title[x][y].equals("input")){
				break;
			}else{
				inputTitle=title[x][y]+inputTitle;
			}
		}*/
		for(int k=0;k<x;k++){
			if(title[k][y].equals("#")||title[k][y].equals("input")){
				break;
			}else{
				//inputTitle=title[k][y]+inputTitle;
				inputTitle=inputTitle+title[k][y]+"/";
			}
		}
		return inputTitle.substring(0,inputTitle.length()-1);
	}
	@SuppressWarnings("unchecked")
	private static String ifInput(Element td) {
	//	String s="";
		List<Element> inputs=td.selectNodes(".//input");
		if(inputs!=null&&inputs.size()>0){
			return "input";
			/*Attribute value=inputs.get(0).attribute("value");
			s=value.getValue();
			return s;*/
		}
		return "#";
	}
	@SuppressWarnings("unchecked")
	private static String getTitleContent(Element td) {
	/*	List<Element> spans=td.selectNodes(".//span");
		if(spans!=null){
			for(Element span:spans){
				if(span.asXML().contains(titleStyle)){
					return span.getStringValue().trim();
				}
			}
		}
		return null;*/
	//	List<Element> spans=td.selectNodes(".//span");
		List<Element> spans=td.selectNodes(".//p");
		String s="";
		if(spans!=null){
			for(Element span:spans){
				s=s+span.getStringValue().trim();
			}
			return s;
		}
		return null;
	}
}
