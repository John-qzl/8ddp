package com.cssrc.ibms.api.report.model;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SignModel
 * @Description: 签名参数model
 * @author zxg
 * @date 2016年12月9日 下午1:39:52
 * 
 */
public class IbmsSign {
	/**
	 * 需要求签名的用户Id
	 */
	private List<Long> signUsers;
	/**
	 * 需要签名的report报表的title
	 */
	private String reportname;
	
	private Float signW;
	
	private Float signH;
	
	private Float offSetH;

	/**
	 * 报表的参数
	 */
	private Map<String, String> paramMap;

	public IbmsSign(List<Long> signUsers, String reportname) {
		this.signUsers = signUsers;
		this.reportname = reportname;
	}
	
	public IbmsSign(List<Long> signUsers, String reportname,Float signW,Float signH) {
		this.signUsers = signUsers;
		this.reportname = reportname;
		this.signW=signW;
		this.signH=signH;

	}
	

	public IbmsSign(List<Long> signUsers, String reportname,
			Map<String, String> paramMap) {
		this.signUsers = signUsers;
		this.reportname = reportname;
		this.paramMap = paramMap;
	}
	
	public IbmsSign(List<Long> signUsers, String reportname,
			Map<String, String> paramMap,Float signW,Float signH,Float offSetH) {
		this.signUsers = signUsers;
		this.reportname = reportname;
		this.paramMap = paramMap;
		this.signW=signW;
		this.signH=signH;
		this.offSetH=offSetH;
	}

	public List<Long> getSignUsers() {
		return signUsers;
	}

	public void setSignUsers(List<Long> signUsers) {
		this.signUsers = signUsers;
	}

	public String getReportname() {
		return reportname;
	}

	public Map<String, String> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	public Float getSignW() {
		return signW;
	}

	public void setSignW(Float signW) {
		this.signW = signW;
	}

	public Float getSignH() {
		return signH;
	}

	public void setSignH(Float signH) {
		this.signH = signH;
	}

	public Float getOffSetH() {
		return offSetH;
	}

	public void setOffSetH(Float offSetH) {
		this.offSetH = offSetH;
	}

	
	

}
