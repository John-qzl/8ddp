package com.cssrc.ibms.core.util.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.binary.Base64;

/**
 * 2016-7-19
 * 
 * @author Yangbo
 *
 */
public class EncryptUtil {
	private static final String key = "@#$%^6a7";

	public static String encryptMd5(String inStr) throws Exception {
		MessageDigest md = null;
		String out = null;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(inStr.getBytes());
			return new String(Base64.encodeBase64(digest));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static synchronized String encryptSha256(String inputStr) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(inputStr.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(digest));
		} catch (Exception e) {
		}
		return null;
	}

	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else {
				hs = hs + stmp;
			}
		}

		return hs.toLowerCase();
	}

	public static String decrypt(String message) throws Exception {
		byte[] bytesrc = stringToBytes(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec("@#$%^6a7".getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec("@#$%^6a7".getBytes("UTF-8"));

		cipher.init(2, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte, "UTF-8");
	}

	public static String encrypt(String message) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		DESKeySpec desKeySpec = new DESKeySpec("@#$%^6a7".getBytes("UTF-8"));

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec("@#$%^6a7".getBytes("UTF-8"));
		cipher.init(1, secretKey, iv);

		String str = bytesToString(cipher.doFinal(message.getBytes("UTF-8")));
		return str;
	}

	private static byte[] stringToBytes(String temp) {
		byte[] digest = new byte[temp.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = temp.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}

	private static String bytesToString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xFF & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}

		return hexString.toString();
	}
}
