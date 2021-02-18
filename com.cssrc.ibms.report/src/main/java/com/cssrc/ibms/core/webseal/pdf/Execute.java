package com.cssrc.ibms.core.webseal.pdf;

/**
 * @ClassName: xecute
 * @Description: pdf关键字定位model
 * @author zxg
 * @date 2016年12月5日 下午3:04:50
 * 
 */
public class Execute {
	private String key;
	private String imgpath;

	public  Execute(String key,String imgpath){
		this.key=key;
		this.imgpath=imgpath;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	
}
