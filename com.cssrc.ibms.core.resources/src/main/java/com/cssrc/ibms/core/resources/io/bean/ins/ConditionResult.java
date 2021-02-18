package com.cssrc.ibms.core.resources.io.bean.ins;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * @author user
 * 检查条件结果W_CONDI_RES
 */
@XmlRootElement(name = "conditionResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionResult {
	public ConditionResult() {}
	public ConditionResult(Map<String, Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.value = CommonTools.Obj2String(map.get("F_VALUE"));
		this.condition_id = CommonTools.Obj2String(map.get("F_CONDITION_ID"));
		this.conditionDefId = CommonTools.Obj2String(map.get("F_CONDITION_ID"));
		this.tb_instan_id = CommonTools.Obj2String(map.get("F_TB_INSTAN_ID"));
	}
	@XmlAttribute
	private String id; //主键
	@XmlAttribute
	private String value; //值
	@XmlAttribute(name="conditionId")
	private String condition_id; //所属检查条件
	 @XmlAttribute(name="conditionDefId")
	private String conditionDefId; //所属模板检查条件
	@XmlAttribute(name="tbInstanId")
	private String tb_instan_id;  //所属检查实例
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCondition_id() {
		return condition_id;
	}
	public void setCondition_id(String condition_id) {
		this.condition_id = condition_id;
	}
	public String getTb_instan_id() {
		return tb_instan_id;
	}
	public void setTb_instan_id(String tb_instan_id) {
		this.tb_instan_id = tb_instan_id;
	}

	public String getConditionDefId() {
		return conditionDefId;
	}

	public void setConditionDefId(String conditionDefId) {
		this.conditionDefId = conditionDefId;
	}
}
