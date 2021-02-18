package com.cssrc.ibms.system.util;

import java.io.File;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.string.StringUtil;


public class CssUtil {
	public static void refreshCssFile(String basePath,String dirPath,
			StringBuilder cssBuilder,String[] extArr,boolean subDir){
		File[] _files = new File(basePath,dirPath).listFiles();
		for(File _sub:_files){
			String _sortName = StringUtil.replaceAll(FileUtil.getPath(_sub), FileUtil.getPath(basePath), "");
			if(_sub.isDirectory()){
				if(subDir)
					refreshCssFile(basePath,_sortName,cssBuilder,extArr,subDir);
			   continue;
			}else{
				String name = FileUtil.extractFilenameNoExt(_sub.getName());
				if(name.indexOf(".")>0){
					_sub.delete();
					continue;
				}
				String _extStr = FileUtil.extractFileExt(_sub.getName()); 
				if(StringUtil.indexOf(_extStr, extArr)!=-1){
					String _tmp = ".%s_icon{background:url('../%s')  no-repeat !important}\r\n";
					cssBuilder.append(String.format(_tmp, FileUtil.extractFilenameNoExt(_sub.getName()),_sortName));
				}
			}
			  
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		StringBuilder _sb = new StringBuilder();
		String path = "images\\function\\small",
		basePath="F:\\IBMS\\SourceCode\\trunk\\ibms\\WebRoot\\";
		refreshCssFile(basePath,path,_sb,new String[]{"png","gif"},false);
		FileUtil.writeUtfText(new File(basePath,"css\\small.css"), _sb.toString());
	}

}
