package com.cssrc.ibms.core.resources.io.bean.ins;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.resources.io.bean.DpFile;
import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * @author user
 * 检查结果W_CK_RESULT
 */
@XmlRootElement(name = "checkResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckResult {
	public CheckResult() {}
	public CheckResult(Map<String, Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.result = CommonTools.Obj2String(map.get("F_RESULT"));
		this.value = CommonTools.Obj2String(map.get("F_VALUE"));
		this.ifNumm = CommonTools.Obj2String(map.get("F_IFNUMM"));
		this.sketchMap = CommonTools.Obj2String(map.get("F_SKETCHMAP"));
		this.itemDef_id = CommonTools.Obj2String(map.get("F_ITEMDEF_ID"));
		this.tb_instan_id = CommonTools.Obj2String(map.get("F_TB_INSTAN"));
	}
	@XmlAttribute
	private String id; //主键 检查结果图片
	@XmlAttribute
	private String result; //检查结果
	@XmlAttribute
	private String value; //检查至 
	@XmlAttribute
	private String ifNumm;  //是否为空
	@XmlAttribute
	private String sketchMap;  //检查结果示意图 
	@XmlAttribute
	private String itemDef_id; //所属检查项定义
	@XmlAttribute(name="tbInstanId")
	private String tb_instan_id;  //所属表格实例
	
	@XmlElement(name ="images")
	private List<DpFile>  images;//检查结果图片  通过id对应cwm_sys_file的tableId
	
	@XmlElement(name ="sketchImage")
	private DpFile  sketchImage;//检查结果示意图  sketchMap存的是fileId
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getIfNumm() {
		return ifNumm;
	}
	public void setIfNumm(String ifNumm) {
		this.ifNumm = ifNumm;
	}
	public String getSketchMap() {
		return sketchMap;
	}
	public void setSketchMap(String sketchMap) {
		this.sketchMap = sketchMap;
	}
	public String getItemDef_id() {
		return itemDef_id;
	}
	public void setItemDef_id(String itemDef_id) {
		this.itemDef_id = itemDef_id;
	}
	public String getTb_instan_id() {
		return tb_instan_id;
	}
	public void setTb_instan_id(String tb_instan_id) {
		this.tb_instan_id = tb_instan_id;
	}
	public List<DpFile> getImages() {
		return images;
	}
	public void setImages(List<DpFile> images) {
		this.images = images;
	}
	public DpFile getSketchImage() {
		return sketchImage;
	}
	public void setSketchImage(DpFile sketchImage) {
		this.sketchImage = sketchImage;
	}
}
