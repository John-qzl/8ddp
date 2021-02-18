package com.cssrc.ibms.core.user.model;
import org.springframework.web.multipart.MultipartFile;

public class SysUserPic {
	private MultipartFile imgFile;

	public MultipartFile getImgFile() {
		return imgFile;
	}

	public void setImgFile(MultipartFile imgFile) {
		this.imgFile = imgFile;
	}
}	
