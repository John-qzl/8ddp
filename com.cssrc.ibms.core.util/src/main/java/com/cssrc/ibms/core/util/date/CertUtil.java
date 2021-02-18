package com.cssrc.ibms.core.util.date;
import java.math.BigInteger;

public class CertUtil {

	public static BigInteger getNextSerialNumber() {
        StringBuffer sb = new StringBuffer();
		Long time = DateUtil.getCurrentDate().getTime();
        sb.append(time).append(generateRandom(6));
		return BigInteger.valueOf(Long.valueOf(sb.toString()));
	}

	public static int generateRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}
	
	public static String getNextVersion() {
        StringBuffer sb = new StringBuffer();
		Long time = DateUtil.getCurrentDate().getTime();
        sb.append(time).append(generateRandom(6));
		return sb.toString();
	}
	

}
