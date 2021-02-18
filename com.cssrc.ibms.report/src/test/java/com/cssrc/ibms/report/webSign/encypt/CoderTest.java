package com.cssrc.ibms.report.webSign.encypt;
import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import com.cssrc.ibms.core.encrypt.Coder;

/**
 * 
 * @author ����
 * @version 1.0
 * @since 1.0
 */
public class CoderTest {

	@Test
	public void test() throws Exception {
		String inputStr = "�򵥼���";
		System.err.println("ԭ��:\n" + inputStr);

		byte[] inputData = inputStr.getBytes();
		String code = Coder.encryptBASE64(inputData);

		System.err.println("BASE64���ܺ�:\n" + code);

		byte[] output = Coder.decryptBASE64(code);

		String outputStr = new String(output);

		System.err.println("BASE64���ܺ�:\n" + outputStr);

		// ��֤BASE64���ܽ���һ����
		assertEquals(inputStr, outputStr);

		// ��֤MD5����ͬһ���ݼ����Ƿ�һ��
		assertArrayEquals(Coder.encryptMD5(inputData), Coder
				.encryptMD5(inputData));

		// ��֤SHA����ͬһ���ݼ����Ƿ�һ��
		assertArrayEquals(Coder.encryptSHA(inputData), Coder
				.encryptSHA(inputData));

		String key = Coder.initMacKey();
		System.err.println("Mac��Կ:\n" + key);

		// ��֤HMAC����ͬһ���ݣ�ͬһ��Կ�����Ƿ�һ��
		assertArrayEquals(Coder.encryptHMAC(inputData, key), Coder.encryptHMAC(
				inputData, key));

		BigInteger md5 = new BigInteger(Coder.encryptMD5(inputData));
		System.err.println("MD5:\n" + md5.toString(16));

		BigInteger sha = new BigInteger(Coder.encryptSHA(inputData));
		System.err.println("SHA:\n" + sha.toString(32));

		BigInteger mac = new BigInteger(Coder.encryptHMAC(inputData, inputStr));
		System.err.println("HMAC:\n" + mac.toString(16));
	}
}
