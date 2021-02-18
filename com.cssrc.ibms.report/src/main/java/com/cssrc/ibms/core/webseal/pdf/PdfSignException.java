package com.cssrc.ibms.core.webseal.pdf;

import com.cssrc.ibms.core.util.string.StringUtil;

/** 
* @ClassName: SignatureException 
* @Description: 异常类
* @author zxg 
* @date 2016年12月5日 下午3:02:38 
*  
*/
public class PdfSignException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int nCode;
	public static final int RCE_SING = -1;// 签名异常
	public static final int RCE_UNKNOWN = -2;// 未知错误
	public static final int RCE_NOKEY = -3;// 没有找到签名的key
	public static final int RCE_INVALID = -4;// invalid request xml

	// found
	public int getNCode() {
		return nCode;
	}

	public void setNCode(int code) {
		nCode = code;
	}

	public PdfSignException(int code) {
		super("");
		this.nCode = code;
	}

	public PdfSignException(int code, String message) {
		super(message);
		this.nCode = code;
	}

	public PdfSignException(int code, Exception cause) {
		super(cause);
		this.nCode = code;
	}

	public String getMessage() {
		String str = super.getMessage();
		if (StringUtil.isNotEmpty(str))
			return str;
		switch (this.nCode) {
			case RCE_SING:
				return "签名异常.";
			case RCE_NOKEY:
				return "没有找到签名用户";
			case RCE_INVALID:
				return "不合理的请求.";
			default:
				return "未知异常";
		}

	}

	public String toString() {
		return super.toString() + " with error code: " + this.nCode;
	}
}
