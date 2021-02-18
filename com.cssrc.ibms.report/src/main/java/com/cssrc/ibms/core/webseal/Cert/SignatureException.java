package com.cssrc.ibms.core.webseal.Cert;

import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * @ClassName: SignatureException
 * @Description: 异常类
 * @author zxg
 * @date 2016年12月5日 下午3:02:38
 * 
 */
public class SignatureException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    private int nCode;
    
    public static final int RCE_SINGATURE = -1;// 签名异常
    
    public static final int RCE_UNKNOWN = -2;// 未知错误
    
    public static final int RCE_NOKEY = -3;// no secure key to develop request
    
    public static final int RCE_INVALID = -4;// invalid request xml
    
    public static final int RCE_VERIFY = -5;// 验证异常
    
    public static final int RCE_PARAMETER = -6;// parameter exception
    
    public static final int RCE_ENCRYPT = -7;// 加密数据异常
    
    public static final int RCE_DECRYPT = -8;// 加密数据异常
    
    public static final int RCE_CERT = -9;// 证书异常
    
    public static final int RCE_FAILED = -12;// failed
    
    public static final int RCE_OUTOFMEMORY = -13;// 内存溢出
    
    public static final int RCE_IO = -14;// IO 错误
    
    public static final int RCE_UNAUTHORIZED = -15;// the requested operation is
    
    // out of the authority
    public static final int RCE_NOMETHOD = -16;// the requested method is not
    
    // found
    
    public int getNCode()
    {
        return nCode;
    }
    
    public void setNCode(int code)
    {
        nCode = code;
    }
    
    public SignatureException(int code)
    {
        super("");
        this.nCode = code;
    }
    
    public SignatureException(int code, String message)
    {
        super(message);
        this.nCode = code;
    }
    
    public SignatureException(int code, Exception cause)
    {
        super(cause);
        this.nCode = code;
    }
    
    public String getMessage()
    {
        String str = super.getMessage();
        if (StringUtil.isNotEmpty(str))
            return str;
        switch (this.nCode)
        {
            case RCE_SINGATURE:
                return "签名异常.";
            case RCE_NOKEY:
                return "没有找到可以解密的密钥.";
            case RCE_INVALID:
                return "不合理的请求.";
            case RCE_VERIFY:
                return "数据库操作出现问题.";
            case RCE_PARAMETER:
                return "不合理的调用参数.";
            case RCE_ENCRYPT:
                return "加密应答出现问题.";
            case RCE_DECRYPT:
                return "解密请求出现问题.";
            case RCE_FAILED:
                return "调用结果为失败.";
            case SignatureException.RCE_OUTOFMEMORY:
                return "内存不足，无法完成相应的操作.";
            case SignatureException.RCE_IO:
                return "IO错误发生.";
            case SignatureException.RCE_UNAUTHORIZED:
                return "操作超出权限，无法完成.";
            case SignatureException.RCE_NOMETHOD:
                return "没有找到这个函数供调用.";
            case SignatureException.RCE_CERT:
                return "调用证书异常.";
            default:
                return "未知异常";
        }
        
    }
    
    public String toString()
    {
        return super.toString() + " with error code: " + this.nCode;
    }
}
