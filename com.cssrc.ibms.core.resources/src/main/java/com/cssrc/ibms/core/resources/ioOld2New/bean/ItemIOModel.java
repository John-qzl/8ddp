package com.cssrc.ibms.core.resources.ioOld2New.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemIOModel{
	
	@XmlAttribute
	private String ildd;
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String iildd;
	@XmlAttribute
	private String ifmedia;
	@XmlAttribute
	private String tempid;
	@XmlAttribute
	private String description;
	@XmlAttribute
	private String zhycdz;				//最后一次操作
	@XmlAttribute
	private String njljyq;					//拧紧力矩
	@XmlAttribute
	private String ycn;					//易错难
	
	@XmlAttribute
	private String rowNumber;		//行号
	@XmlAttribute
	private String CellNumber;		//列号
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getCellNumber() {
		return CellNumber;
	}
	public void setCellNumber(String cellNumber) {
		CellNumber = cellNumber;
	}
	private List<CkIOModel> ckList = new ArrayList<CkIOModel>();
	private List<CellIOModel> cellList = new ArrayList<CellIOModel>();
	
	public List<CkIOModel> getCkList() {
		return ckList;
	}
	public void setCkList(List<CkIOModel> ckList) {
		this.ckList = ckList;
	}
	public List<CellIOModel> getCellList() {
		return cellList;
	}
	public void setCellList(List<CellIOModel> cellList) {
		this.cellList = cellList;
	}
	
	
	public String getIldd() {
		return ildd;
	}
	public void setIldd(String ildd) {
		this.ildd = ildd;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIfmedia() {
		return ifmedia;
	}
	public void setIfmedia(String ifmedia) {
		this.ifmedia = ifmedia;
	}
	public String getTempid() {
		return tempid;
	}
	public void setTempid(String tempid) {
		this.tempid = tempid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getZhycdz() {
		return zhycdz;
	}
	public void setZhycdz(String zhycdz) {
		this.zhycdz = zhycdz;
	}
	public String getNjljyq() {
		return njljyq;
	}
	public void setNjljyq(String njljyq) {
		this.njljyq = njljyq;
	}
	public String getYcn() {
		return ycn;
	}
	public void setYcn(String ycn) {
		this.ycn = ycn;
	}
	public String getIildd() {
		return iildd;
	}
	public void setIildd(String iildd) {
		this.iildd = iildd;
	}
	
	
}
