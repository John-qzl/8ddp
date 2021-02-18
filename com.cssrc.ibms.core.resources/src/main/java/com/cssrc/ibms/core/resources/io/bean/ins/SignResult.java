package com.cssrc.ibms.core.resources.io.bean.ins;

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
 * 签署结果 W_SIGNRESULT
 */
@XmlRootElement(name = "signResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignResult {
	public SignResult() {}
	public SignResult(Map<String, Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.signUser = CommonTools.Obj2String(map.get("F_SIGNUSER"));
		this.signTime = CommonTools.Obj2String(map.get("F_SIGNTIME"));
		this.remark = CommonTools.Obj2String(map.get("F_REMARK"));
		this.signdef_id = CommonTools.Obj2String(map.get("F_SIGNDEF_ID"));
		this.tb_instan_id = CommonTools.Obj2String(map.get("F_TB_INSTANT_ID"));
	}
	@XmlAttribute
	private String id; //主键
	@XmlAttribute
	private String signUser; //签署用户
	@XmlAttribute
	private String signTime; //签署时间
	@XmlAttribute
	private String remark;  //备注
	@XmlAttribute(name="signDefId")
	private String signdef_id; //所属签署定义
	@XmlAttribute(name="tbInstanId")
	private String tb_instan_id;  //所属表格实例
	
	@XmlElement(name ="sketchImage")
	private DpFile image;//签署图片 通过dataid找到图片
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSignUser() {
		return signUser;
	}
	public void setSignUser(String signUser) {
		this.signUser = signUser;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTb_instan_id() {
		return tb_instan_id;
	}
	public void setTb_instan_id(String tb_instan_id) {
		this.tb_instan_id = tb_instan_id;
	}
	public String getSigndef_id() {
		return signdef_id;
	}
	public void setSigndef_id(String signdef_id) {
		this.signdef_id = signdef_id;
	}
	public DpFile getImage() {
		return image;
	}
	public void setImage(DpFile image) {
		this.image = image;
	}
}
