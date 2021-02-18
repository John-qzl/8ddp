package com.cssrc.ibms.report.webSign;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.cssrc.ibms.core.webseal.Cert.PKCS;

public class CertTest{

	@Test
	public void extCert() throws Exception {
		try{
			PKCS cert=new PKCS();
			cert.setKeyPass("123456");
			cert.setStorePass("888888");
			cert.ExecPfx("D:\\ibms\\reportlets\\Lee.pfx", "Lee");
		}catch(Exception e){
			fail("Not yet implemented");
		}
		
	}

}
