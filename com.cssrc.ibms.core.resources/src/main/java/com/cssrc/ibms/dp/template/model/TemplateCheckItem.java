package com.cssrc.ibms.dp.template.model;

/**
 * @description 模板检查项model
 * @author xie chen
 * @date 2019年12月5日 上午11:28:11
 * @version V1.0
 */
public class TemplateCheckItem {
	
	private Long id;

	private String name;
	// 简称
	private String shortName;
	// 检查类型
	private String type;
	// 检查项描述
	private String description;
	// 一类单点
	private String ILdd;
	// 二类单点
	private String IILdd;
	// 易错难
	private String ycn;
	// 拧紧力矩要求
	private String njljyq;
	// 最后一次动作
	private String zhycdz;
	// 是否多媒体项目
	private String ifMedia;
	// 所属模板id
	private String templateId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getILdd() {
		return ILdd;
	}
	public void setILdd(String iLdd) {
		ILdd = iLdd;
	}
	public String getIILdd() {
		return IILdd;
	}
	public void setIILdd(String iILdd) {
		IILdd = iILdd;
	}
	public String getYcn() {
		return ycn;
	}
	public void setYcn(String ycn) {
		this.ycn = ycn;
	}
	public String getNjljyq() {
		return njljyq;
	}
	public void setNjljyq(String njljyq) {
		this.njljyq = njljyq;
	}
	public String getZhycdz() {
		return zhycdz;
	}
	public void setZhycdz(String zhycdz) {
		this.zhycdz = zhycdz;
	}
	public String getIfMedia() {
		return ifMedia;
	}
	public void setIfMedia(String ifMedia) {
		this.ifMedia = ifMedia;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
}
