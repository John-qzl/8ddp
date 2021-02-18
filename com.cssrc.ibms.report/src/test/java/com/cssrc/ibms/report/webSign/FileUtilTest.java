package com.cssrc.ibms.report.webSign;


import java.io.File;

import org.junit.Test;

import com.cssrc.ibms.core.util.file.FileUtil;


public class FileUtilTest {

	@Test
	public void readFileByte() throws Exception {
	    File file=FileUtil.generateTempFile();
	    if(file!=null&&file.exists()){
	        file.delete();
	    }
	}
	
}
