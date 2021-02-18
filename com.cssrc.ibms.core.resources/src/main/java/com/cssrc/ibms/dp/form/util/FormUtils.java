package com.cssrc.ibms.dp.form.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.dp.formvalidate.service.TableReduction;


public class FormUtils {
	/**
	 * 在线表单设计器最大支持的行数
	 */
	public static final int largestRows=500;
	/**
	 * 在线表单设计器最大支持的列数
	 */
	public static final int largestCols=300;
	public static final String titleStyle="color: rgb(255, 0, 0)";
	public static final String inputStyle="input";
	@SuppressWarnings("unchecked")
	public static  Map<String, Object>  getTableRes(String html, String type) {
		Map<String, Object>message=new TreeMap<String, Object>();
		List<Map<String, Object>>formcontent=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>>colorformcontent=new ArrayList<Map<String, Object>>();
		boolean flag=true;
		html = html.replaceAll("&", "#");
		try {
			Document tableDoc = DocumentHelper.parseText(html);
			List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");
			for (Element table : tables) {
			List<Integer>checkinputs=new ArrayList<Integer>();
			List<String> inputTitles=new ArrayList<String>();
			List<String> color_inputTitles=new ArrayList<String>();
				int rowSize = 0;
				int colSize = 0;
				String[][] title=new String[largestRows][largestCols];//行列标题
				String[][] color_title=new String[largestRows][largestCols];//行列标题
				List<Element> trs = table.selectNodes(".//tr");
				int ii=0;
				//根据表单模板的类型，获取表单的表头位置和主体内容的截止位置
				int [] indexs=getTitleAndContentIndex(trs,type);
				for (int k=indexs[0];k<indexs[1];k++,ii++) {
					// 第k行
					Element tr = trs.get(k);
					List<Element> tds = tr.selectNodes(".//td");
					for (int j = 0; j < tds.size(); j++) {
						// 第j个单元格
						Element td = tds.get(j);
						// 获取其内容
						String string = td.asXML();
						//获取第j个单元格input的个数
						int inputnum=0;
						List<Element> tdinputs=td.selectNodes(".//input");
						if(tdinputs!=null){
							boolean picflag=false;
							inputnum=tdinputs.size();
							for(int w=0;w<tdinputs.size();w++){
								if(tdinputs.get(w).attributeValue("type")!=null) {
									if(tdinputs.get(w).attributeValue("type").equals("button")){
										picflag=true;
										break;
									}
								} else {
									tdinputs.get(w).addAttribute("type", "text");
								}
							}
							if(picflag){
								for(int w=0;w<tdinputs.size();w++){
									if(tdinputs.get(w).attributeValue("type")!=null) {
										if(!tdinputs.get(w).attributeValue("type").equals("button"))	
											tdinputs.get(w).addAttribute("photo", "是");
									} /*else {
										tdinputs.get(w).addAttribute("type", "text");
									}*/
								}
							}
						}
						
						String content = "#";
						String color_content="*";
						if(string.contains(inputStyle)){
							content=ifInput(td);
						}else{
							content=getTitleContent(td);
						}
						
						//判断是否为标记的标题
						if(string.contains(titleStyle)){
							color_content=getColorTitleContent(td);
						}
						//判断是否为输入项
						else if(string.contains(inputStyle)){
							color_content=ifInput(td);
						}
						// 如果为某一行中的合并 //如果为某一列的合并列合并
						Attribute colspan = td.attribute("colspan");
						colSize = colspan == null ? 1 : Integer.valueOf(colspan
								.getValue());
						Attribute rowspan = td.attribute("rowspan");
						rowSize = rowspan == null ? 1 : Integer.valueOf(rowspan
								.getValue());
						setTableContent(title,inputTitles,ii, j, colSize, rowSize, content,checkinputs,inputnum);
						setColorTableContent(color_title, color_inputTitles, ii, j, colSize, rowSize, color_content,inputnum);
					}
				}
				
				//showTitle(title);
					
				  // System.out.println(new_html);
				List<Element> inputs = table.selectNodes(".//input");
				if(inputs.size()==0){
					int index1=tableDoc.asXML().indexOf("<html>");
					int index2=tableDoc.asXML().indexOf("</html>");
					String new_html=tableDoc.asXML().substring(index1+6, index2);
					new_html=new_html.replaceAll("#", "&");
					message.put("html", new_html);
					message.put("error", "请在表单模板的单元格内插入输入框或者确认框。");
					return message;
				}
				for(int i=0;i<inputs.size();i++){
				//	String input=inputs.get(i).asXML();
					if(inputs.get(i).attributeValue("type")!=null)
					if(inputs.get(i).attributeValue("type").equals("button")){
						inputs.remove(i);
						i--;
					}
				}
				
				for(int i=0;i<inputTitles.size();i++){
					if(inputTitles.get(i)=="#"){
						inputTitles.remove(i);
						i--;
					}
				}
				
				for(int i=0;i<color_inputTitles.size();i++){
					if(color_inputTitles.get(i)=="#"){
						color_inputTitles.remove(i);
						i--;
					}
				}
				
				for (int i = 0; i < inputs.size(); i++) {
					Long ID=UniqueIdUtil.genId();
					Map inputcontent=new TreeMap();
					Map colorinputcontent=new TreeMap();
					inputs.get(i).addAttribute("title", inputTitles.get(i));
					inputs.get(i).addAttribute("id", ID.toString());
				/*	if(checkinputs.get(i)==-1){
						if(flag){
							flag=false;
							inputs.get(i).addAttribute("style", "border-color:red;");
							String error="红色区域内的input标签左侧不能为非输入项";
							message.put("error", error);
						}
					}
					if(checkinputs.get(i)==-2){
						if(flag){
							flag=false;
							inputs.get(i).addAttribute("style", "border-color:red;");
							 String error="红色区域内的input标签上侧不能为非输入项";
							 message.put("error", error);
						}
					}*/
					inputcontent.put("id", ID);
					String input=inputs.get(i).asXML();
					if(input.contains("checkbox"))
						inputcontent.put("type", 2);
					else
						inputcontent.put("type", 1);
					if(input.contains("photo"))
						inputcontent.put("photo", "true");
					else
						inputcontent.put("photo", "false");
					inputcontent.put("describe",inputTitles.get(i).replaceAll("#nbsp;", ""));
					colorinputcontent.put("colordescribe",color_inputTitles.get(i).replaceAll("#nbsp;", ""));
					formcontent.add(inputcontent);
					colorformcontent.add(colorinputcontent);
					//System.out.println( inputTitles.get(i));
				}
				
				message.put("titles", tableDoc.asXML().replaceAll("#", "&"));
				message.put("formcontent", formcontent);
				message.put("colorformcontent", colorformcontent);
				if(flag==false){
					int index1=tableDoc.asXML().indexOf("<html>");
					int index2=tableDoc.asXML().indexOf("</html>");
				   String new_html=tableDoc.asXML().substring(index1+6, index2);
				   new_html=new_html.replaceAll("#", "&");
				   message.put("html", new_html);
				}
				else
					message.put("html", "");
			}
			
		//	System.out.println(tableDoc.asXML().replaceAll("#", "&"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		//return "";
		//message.put("html", "");
		return message;
	}
	
	private static int[] getTitleAndContentIndex(List<Element> trs, String type) {
		int titleIndex=-1;
		int contentLastIndex=-1;
		if(type.equals("1")){
			titleIndex=0;
			contentLastIndex=trs.size();
		}else if(type.equals("2")){
			titleIndex=getSignIndex(trs,TableReduction.title);
			contentLastIndex=trs.size();
		}else if(type.equals("3")){
			titleIndex=0;
			contentLastIndex=getSignIndex(trs,TableReduction.tail);
		}else if(type.equals("4")){
			titleIndex=getSignIndex(trs, TableReduction.title);
			contentLastIndex=getSignIndex(trs, TableReduction.tail);
		}else{
			contentLastIndex=trs.size();
			titleIndex=getSignIndex(trs,TableReduction.title);
			int tempLast=getSignIndex(trs,TableReduction.tail);
			if(tempLast!=-1){
				contentLastIndex=tempLast;
			}
		}
		return new int[]{titleIndex,contentLastIndex};
	}
	/**
	 * 根据行标记类型获取行所在的下标
	 * @param trs
	 * @param typeStr
	 * @return
	 */
	private static int getSignIndex(List<Element> trs, String typeStr) {
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
	private static int appearNumber(String srcText,String findText){
		int count=0;
		int index=0;
		while((index=srcText.indexOf(findText, index))!=-1){
			index=index+findText.length();
			count++;
		}
		return count;
	}
	
	private static void showTitle(String [][]title) {
		for(int i=0;i<100;i++){
			if(title[i][0]==null){
			//	break;
			}
			for(int j=0;j<100;j++){
				if(title[i][j]==null){
			//		break;
				}
				System.out.print(title[i][j]+"\t");
			}
			System.out.println();
		}
		
	}
	//从i j开始往后的cols 往下的rows内 填充content
	private static void setTableContent(String[][] title, List<String> inputTitles, int i, int j, int colSize, int rowSize, String content,List<Integer>checkinputs, int inputnum) {
		while(title[i][j]!=null){
			j++;
		}
		for(int x=i;x<i+rowSize;x++){
			for(int y=j;y<j+colSize;y++){
				
				if(x==i&&y==j){
					if(content.equals("input")){
						if(inputnum==1){
							if(i>0&&j>0){
								boolean flag1=rowcheck(i,j,title);
								boolean flag2=colcheck(i, j, title);
									if(!(title[i][j-1].contains("input"))&&flag1)
										checkinputs.add(-1);
									else if(!(title[i-1][j].contains("input"))&&flag2)
										checkinputs.add(-2);
									else
										checkinputs.add(0);
							}else{
								checkinputs.add(0);
							}
							inputTitles.add(getInputTitle(title,x,y));
						}else if(inputnum==2){
							if(i>0&&j>0){
								boolean flag1=rowcheck(i,j,title);
								boolean flag2=colcheck(i, j, title);
									if(!(title[i][j-1].contains("input"))&&flag1){
										checkinputs.add(-1);
										checkinputs.add(-1);
									}
									else if(!(title[i-1][j].contains("input"))&&flag2){
										checkinputs.add(-2);
										checkinputs.add(-2);
									}
									else{
										checkinputs.add(0);
										checkinputs.add(0);
									}
							}else{
								checkinputs.add(0);
								checkinputs.add(0);
							}
							inputTitles.add(getInputTitle(title,x,y));
							inputTitles.add(getInputTitle(title,x,y));
						}
					}else if(content.contains("photo")){
						 if(inputnum==2){
							if(i>0&&j>0){
								boolean flag1=rowcheck(i,j,title);
								boolean flag2=colcheck(i, j, title);
									if(!(title[i][j-1].contains("input"))&&flag1){
										checkinputs.add(-1);
									}
									else if(!(title[i-1][j].contains("input"))&&flag2){
										checkinputs.add(-2);
									}
									else{
										checkinputs.add(0);
									}
							}else{
								checkinputs.add(0);
							}
							inputTitles.add(getInputTitle(title,x,y));
						}else if(inputnum==3){
							if(i>0&&j>0){
								boolean flag1=rowcheck(i,j,title);
								boolean flag2=colcheck(i, j, title);
									if(!(title[i][j-1].contains("input"))&&flag1){
										checkinputs.add(-1);
										checkinputs.add(-1);
									}
									else if(!(title[i-1][j].contains("input"))&&flag2){
										checkinputs.add(-2);
										checkinputs.add(-2);
									}
									else{
										checkinputs.add(0);
										checkinputs.add(0);
									}
							}else{
								checkinputs.add(0);
								checkinputs.add(0);
							}
							inputTitles.add(getInputTitle(title,x,y));
							inputTitles.add(getInputTitle(title,x,y));
						}
					}
				}
				else{
					if(content.equals("input")){
						if(inputnum==1){
							inputTitles.add("#");
						}else if(inputnum==2){
							inputTitles.add("#");
							inputTitles.add("#");
						}
					}else if(content.contains("photo")){
						if(inputnum==2){
							inputTitles.add("#");
						}else if(inputnum==3){
							inputTitles.add("#");
							inputTitles.add("#");
						}
					}
				}
				title[x][y]=content;
			}
		}
	}
	
	private static boolean rowcheck(int i,int j,String[][] title){
		for(int u=0;u<j;u++){
			if(title[i][u].contains("input"))
				return true;
		}
		return false;
	}
	
	private static boolean colcheck(int i,int j,String[][] title){
		for(int u=0;u<i;u++){
			if(title[u][j].contains("input"))
				return true;
		}
		return false;
	}
	//从i j开始往后的cols 往下的rows内 填充content
		private static void setColorTableContent(String[][] title, List<String> inputTitles, int i, int j, int colSize, int rowSize, String content, int inputnum) {
			while(title[i][j]!=null){
				j++;
			}
			for(int x=i;x<i+rowSize;x++){
				for(int y=j;y<j+colSize;y++){
					
					if(x==i&&y==j){
						if(content.equals("input")){
							if(inputnum==1){
								inputTitles.add(getColorInputTitle(title,x,y));
							}else if(inputnum==2){
								inputTitles.add(getColorInputTitle(title,x,y));
								inputTitles.add(getColorInputTitle(title,x,y));
							}
						}else if(content.contains("photo")){
							if(inputnum==2){
								inputTitles.add(getColorInputTitle(title,x,y));
							}else if(inputnum==3){
								inputTitles.add(getColorInputTitle(title,x,y));
								inputTitles.add(getColorInputTitle(title,x,y));
							}
						}
					}
					else{
						if(content.equals("input")){
							if(inputnum==1){
								inputTitles.add("#");
							}else if(inputnum==2){
								inputTitles.add("#");
								inputTitles.add("#");
							}
						}else if(content.contains("photo")){
							if(inputnum==2){
								inputTitles.add("#");
							}else if(inputnum==3){
								inputTitles.add("#");
								inputTitles.add("#");
							}
						}
					}
					title[x][y]=content;
				}
			}
		}
	
	
	private static void setTableContent1(String[][] title, int i, int j, int colSize, int rowSize, String content) {
		while(title[i][j]!=null){
			j++;
		}
		for(int x=i;x<i+rowSize;x++){
			for(int y=j;y<j+colSize;y++){
				title[x][y]=content;
			}
		}
	}
	
	
	//第x行左侧和第y列上侧是否有标题
	private static String getInputTitle(String[][] title,int x, int y) {
		String inputTitle="";
		for(int k=0;k<y;k++){
			if(title[x][k].equals("#")||title[x][k].contains("input")){
				break;
			}else{
			//	inputTitle=title[x][k]+inputTitle;
				
				if(inputTitle.indexOf(title[x][k])<0)
					inputTitle=inputTitle+title[x][k]+"/";
			}
		}
		if(inputTitle.length()>0)
			inputTitle= inputTitle.substring(0, inputTitle.length()-1)+",";
	/*	for(x--;x>=0;x--){
			if(title[x][y].equals("#")||title[x][y].equals("input")){
				break;
			}else{
				inputTitle=title[x][y]+inputTitle;
			}
		}*/
		for(int k=0;k<x;k++){
			if(title[k][y].equals("#")||title[k][y].contains("input")){
				break;
			}else{
				//inputTitle=title[k][y]+inputTitle;
				if(inputTitle.indexOf(title[k][y])<0)
				inputTitle=inputTitle+title[k][y]+"/";
			}
		}
		
		//return inputTitle;
		if(inputTitle.length()>0)
			inputTitle=inputTitle.substring(0,inputTitle.length()-1);
		return inputTitle;
		//return inputTitle.substring(0,inputTitle.length()-1);
	}
	
	//第x行左侧和第y列上侧是否有标题
		private static String getColorInputTitle(String[][] title,int x, int y) {
			String inputTitle="";
			for(int k=0;k<y;k++){
				if(title[x][k].equals("#")||title[x][k].contains("input")){
					break;
				}else{
					if(inputTitle.indexOf(title[x][k])<0)
						inputTitle=inputTitle+title[x][k]+"/";
				}
			}
			if(StringUtil.isNotEmpty(inputTitle)){
				inputTitle= inputTitle.substring(0, inputTitle.length()-1)+",";
			}
			
			for(int k=0;k<x;k++){
				if(title[k][y].equals("#")||title[k][y].contains("input")){
					break;
				}else{
					if(inputTitle.indexOf(title[k][y])<0)
					inputTitle=inputTitle+title[k][y]+"/";
				}
			}
			if(StringUtil.isNotEmpty(inputTitle)){
				inputTitle.substring(0,inputTitle.length()-1);
			}
			return inputTitle;
		}
	
	@SuppressWarnings("unchecked")
	private static String ifInput(Element td) {
		List<Element> inputs=td.selectNodes(".//input");
		if(inputs!=null&&inputs.size()>0){
			//return "input";
			for(int i=0;i<inputs.size();i++){
			//	String input=inputs.get(i).asXML();
				if(inputs.get(i).attributeValue("type")!=null)
				if(inputs.get(i).attributeValue("type").equals("button")){
					return "input"+"photo";
				}
			}
			return "input";
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
		
		List<Element> ps=td.selectNodes(".//p");
		String s="";
		if(ps!=null&&ps.size()>0){
			for(Element p:ps){
				s=s+p.getStringValue().trim();
			}
			//return s;
		}else{
			s=s+td.getStringValue().trim();
		}
		return s;
	}
	
	@SuppressWarnings("unchecked")
	private static String getColorTitleContent(Element td) {
		List<Element> spans=td.selectNodes(".//span");
		if(spans!=null){
			for(Element span:spans){
				if(span.asXML().contains(titleStyle)){
					return span.getStringValue().trim();
				}
			}
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public static  Map<String, Object>  updateHtml(String html,List<Map<String, String>>contentslist){
		html = html.replaceAll("&", "#");
		Map<String, Object>message=new HashMap<String, Object>();
		try{
			Document tableDoc = DocumentHelper.parseText(html);
			List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");
			//前端用户添入的值 list to map 
			Map<String, String> contentMap = listToMap(contentslist);
			for (Element table : tables){
				List<Element> inputs = table.selectNodes(".//input");
				//获取一个table里的所有非button(拍照)类型的input
				for(int i=0;i<inputs.size();i++){
				//	String input=inputs.get(i).asXML();
					if(inputs.get(i).attributeValue("type")!=null)
					if(inputs.get(i).attributeValue("type").equals("button")){
						inputs.remove(i);
						i--;
					}
				}
//				if(inputs.size()!=contentslist.size()){
//					message.put("html", "");
//					return message;
//				}
				
				//将非拍照类型的input 添入值，根据前端值，将html标签改变
				for(int i=0;i<inputs.size();i++){
					String resId = inputs.get(i).attributeValue("id");
					if(StringUtil.isNotEmpty(resId)
							&& contentMap.containsKey(resId)){
						String input=inputs.get(i).asXML();
						String val = contentMap.get(resId);
						if(input.contains("text"))
							inputs.get(i).addAttribute("value", val);
						else if(input.contains("checkbox")){
							if(val.equals("true"))
								inputs.get(i).addAttribute("checked", "checked");
							else {
								//没有勾选的时候, 也需要 自己添加checked 属性,避免 下面查询不到该属性,无法移除
								inputs.get(i).addAttribute("checked", "checked");
								Attribute checkAttr = inputs.get(i).attribute("checked") ;
								//判断是否是二次输入,checked 属性已经自己携带
								inputs.get(i).remove(checkAttr) ;
							}


						}
					}		
				}
			}
			message.put("html", tableDoc.asXML().replaceAll("#", "&"));
		}catch(Exception e){
			e.printStackTrace();
			message.put("html", "");
		}
		return message;
	}
	
	private static Map<String, String> listToMap(
			List<Map<String, String>> contentslist) {
		Map<String, String> contentMap = new HashMap<String, String>();
		for (Map<String, String> content : contentslist) {
			contentMap.put(content.get("id").toString(),
					content.get("value").toString());
		}
		return contentMap;
	}
}
