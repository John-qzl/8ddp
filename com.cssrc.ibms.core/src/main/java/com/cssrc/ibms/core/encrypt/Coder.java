package com.cssrc.ibms.core.encrypt;

import java.security.MessageDigest;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 基础加密组件
 * 
 * @author zxg
 * @version 1.0
 * @since 1.0
 */
public class Coder extends DefaultEncrypt
{
    private final static String[] hexDigits =
        {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    
    public static final String KEY_SHA = "SHA";
    
    public static final String KEY_MD5 = "MD5";
    
    /**
     * MAC算法可选以下多种算法
     * 
     * <pre>
     * HmacMD5 
     * HmacSHA1 
     * HmacSHA256 
     * HmacSHA384 
     * HmacSHA512
     * </pre>
     */
    public static final String KEY_MAC = "HmacMD5";
    
    /**
     * BASE64解密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key)
        throws Exception
    {
        return (new BASE64Decoder()).decodeBuffer(key);
    }
    
    /**
     * BASE64加密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key)
        throws Exception
    {
        return (new BASE64Encoder()).encodeBuffer(key);
    }
    
    public static byte[] encryptMD5(byte[] data)
        throws Exception
    {
        
        return encryptMD5(data, KEY_MD5);
        
    }
    
    /**
     * MD5加密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data, String key)
        throws Exception
    {
        
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        
        return md5.digest();
        
    }
    
    public static String byteArrayToHexString(byte[] b)
    {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
        {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    
    private static String byteToHexString(byte b)
    {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    
    /**
     * SHA加密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data)
        throws Exception
    {
        
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        
        return sha.digest();
        
    }
    
    /**
     * 初始化HMAC密钥
     * 
     * @return
     * @throws Exception
     */
    public static String initMacKey()
        throws Exception
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
        
        SecretKey secretKey = keyGenerator.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }
    
    /**
     * HMAC加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptHMAC(byte[] data, String key)
        throws Exception
    {
        
        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        
        return mac.doFinal(data);
        
    }
    
}
