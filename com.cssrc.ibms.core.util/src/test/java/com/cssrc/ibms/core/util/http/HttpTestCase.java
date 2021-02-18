package com.cssrc.ibms.core.util.http;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.alibaba.fastjson.JSON;

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
    public void testTrans() throws Exception {
        OutputStream outp = null;
        try {
            Map<String,String> data=new HashMap<String,String>();
            data.put("identify", "d27b785e-0b22-43aa-a1d7-605dca86c413");
            data.put("userName", "ibms");
            data.put("password", "ibms");
            data.put("systemName", "ibms");
            data.put("amt", "10");
            String result = HttpClientUtil.postBody("http://192.168.8.22:8188/kdi/rest/service.shtml",JSON.toJSONString(data));
            System.out.println(result);
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
    public void testJob() throws Exception {
        OutputStream outp = null;
        try {
            Map<String,String> data=new HashMap<String,String>();
            data.put("identify", "48ed67bb-db83-48d9-a690-fa153f06df33");
            data.put("userName", "ibms");
            data.put("password", "ibms");
            data.put("systemName", "ibms");
            data.put("curpath", new String("E:/temp"));
            String result = HttpClientUtil.postBody("http://192.168.8.22:8188/kdi/rest/service.shtml",JSON.toJSONString(data));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Not yet implemented");
        } finally {
            if (outp != null) {
                outp.close();
            }
        }
    }
}
