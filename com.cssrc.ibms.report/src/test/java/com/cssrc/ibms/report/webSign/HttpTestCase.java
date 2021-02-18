package com.cssrc.ibms.report.webSign;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.util.http.HttpClientUtil;

public class HttpTestCase {

	@Test
	public void readFileByte() throws Exception {
		OutputStream outp = null;
		try {
			byte[] bt = HttpClientUtil
					.httpGetByte("http://127.0.0.1:8080/ibms-Report/getReport?reportlet=fbzh/TEST_REC.cpt&id=10000005471612");
			outp = new FileOutputStream("D:\\ibms\\reportlets\\test1.pdf");
			outp.write(bt);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not yet implemented");
		} finally {
			if (outp != null) {
				outp.close();
			}
		}
	}
	

    @Test
    public void readFile() throws Exception {
        SysConfConstant.CONF_ROOT="E:/workspace/ibms/com.cssrc.ibms/ibms.conf";
        FastDFSFileOperator.getFileByte("group1/M00/00/00/wKgDBVhHxSqAJM6YAAAYEE9xz98189.png");
    }

}
