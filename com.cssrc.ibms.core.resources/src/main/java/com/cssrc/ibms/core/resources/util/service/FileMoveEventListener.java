package com.cssrc.ibms.core.resources.util.service;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.resources.util.listener.FileMoveEvenet;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.util.SysFileUtil;
import com.sun.xml.internal.xsom.impl.parser.Patch;


@Component
public class FileMoveEventListener implements ApplicationListener<FileMoveEvenet> {

	@Override
	public void onApplicationEvent(FileMoveEvenet event) {
		// TODO Auto-generated method stub
		System.out.println("收到了通知 " + event.getMessage());
		
	}
	//依据文件处理
    public void accordingFileDeal(SysFile sysFile,String path) throws JAXBException {
    	String sourcePath = SysConfConstant.UploadFileFolder + File.separator + sysFile.getFilepath();
    	String filepaths=sysFile.getFilepath();
    	String strpath="";
    	String filename="";
    	if(filepaths!=null||!filepaths.equals("")) {
    		int pos=filepaths.lastIndexOf("\\");
    		strpath=filepaths.substring(0,pos);
    		filename=filepaths.substring(pos+1,filepaths.length());
    	}
    	String filePath = SysFileUtil.createFilePath(path + File.separator+strpath,filename);
/*    	String str=this.packageTempFolder + File.separator+strpath+"\\";*/
        FileOperator.copyFile(sourcePath,filePath);
    }

}
