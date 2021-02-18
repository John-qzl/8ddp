package com.cssrc.ibms.core.login.license;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.PublicKey;
import java.security.Signature;

public class ParseLicence {
	public static boolean validateSignInfo(String path){
    	XmlDomReader xmlDomReader=new XmlDomReader();
        Object obj = null;   
        boolean flag = true;
        try{   
            InputStream fis = ParseLicence.class.getClassLoader().getResourceAsStream("pubKey.key");   
            ObjectInputStream ois = new ObjectInputStream(fis);   
            obj = ois.readObject();   
            ois.close();   
        }catch(IOException ex){   
            System.err.println("读取公钥信息错误！");   
            ex.printStackTrace();
            return false;
        }catch(ClassNotFoundException ex){   
        	System.err.println("验证签名错误!");   
            ex.printStackTrace();   
            return false;
        }
    	try {            
            String info = xmlDomReader.readColumn("license.license");
            String security = xmlDomReader.readSignature("license.license");
    		Signature checkSignet = Signature.getInstance("DSA");   
            checkSignet.initVerify((PublicKey)obj);   
            checkSignet.update(info.getBytes());  
            if(!checkSignet.verify(HexString2Bytes(security))){   
            	flag = false;
            }       
		} catch (Exception e) {
			flag = false;
		    e.printStackTrace();
		}
		return flag;
    }
	
    public static byte[] HexString2Bytes(String src){ 
    	byte[] ret = new byte[src.length()/2]; 
    	byte[] tmp = src.getBytes(); 
    	for(int i=0; i<ret.length; ++i ){ 
    		ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]); 
    	}
    	return ret; 
    }
    private static byte uniteBytes(byte src0, byte src1) {
    	byte _b0 = Byte.decode("0x" + new String(new byte[] {src0})).byteValue();
    	_b0 = (byte) (_b0 << 4);
    	byte _b1 = Byte.decode("0x" + new String(new byte[] {src1})).byteValue();
    	byte ret = (byte) (_b0 ^ _b1);
    	return ret;
    }
}
