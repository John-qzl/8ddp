package com.cssrc.ibms.core.webseal.pdf;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: KeyExecute
 * @Description: pdf关键字定位
 * @author zxg
 * @date 2016年12月5日 下午3:04:50
 * 
 */
public class KeyExecute {
	private List<Execute> keyExecute = new ArrayList<Execute>();

	public KeyExecute() {
	}

	public void addExecute(String key, String signImg) {
		this.keyExecute.add(new Execute(key, signImg));
	}

	public List<Execute> getKeyExecute() {
		return keyExecute;
	}

	public void setKeyExecute(List<Execute> keyExecute) {
		this.keyExecute = keyExecute;
	}

}
