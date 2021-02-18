package com.cssrc.ibms.migration.util;

public class LogUtil {
	public static final String ARROW = "---->";
	public static final String LOG_LINE = 
			"-----------------------------------------------------\r\n";
	public static String getStartLine(String lineName){
		return "start:"+lineName+LOG_LINE;
	}
	public static String getEndLine(String lineName){
		return "end  :"+lineName+LOG_LINE+"\r\n\r\n";
	}
	public static String getLS(int level,int no){
		String levelString = "";
		String noString = String.valueOf(no);
		switch(level){
		case 1:
			noString = noString+"、";
			break;
		case 2:
			noString = noString+")、";
			break;
		case 3:
			noString = "("+noString+")、";
			break;
		case 4:
			noString = "["+noString+"]、";
			break;
		default:
			noString = "<"+noString+">、";
			break;
		}
		if(no==0){
			noString = "";
		}
		for(int i=0; i< level;i++){
			levelString+="  ";
		}
		levelString +=noString;
		
		return levelString;
	}
}
