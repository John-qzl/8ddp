package com.cssrc.ibms.system.model;

import com.cssrc.ibms.api.system.model.BaseSerialNumber;
import com.cssrc.ibms.api.system.model.ISerialNumber;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 流水号
 * <p>Title:SerialNumber</p>
 * @author Yangbo 
 * @date 2016-8-31下午02:49:01
 */
@XmlRootElement(name="SerialNumber")
@XmlAccessorType(XmlAccessType.NONE)
public class SerialNumber extends BaseSerialNumber implements ISerialNumber{
    private static final long serialVersionUID = 3546643147610913849L;
    @XmlAttribute
	protected Long id= Long.valueOf(0L);
	@XmlAttribute
	protected String name;
	@XmlAttribute
	protected String alias;
	@XmlAttribute
	protected String rule;
	@XmlAttribute
	protected Short genType = Short.valueOf((short) 1);
	@XmlAttribute
	protected Integer noLength;
	@XmlAttribute
	protected Integer initValue;
	@XmlAttribute
	protected Long curValue;
	protected String curDate = "";

	protected Short step = Short.valueOf((short) 1);
	
	protected Long newCurValue = Long.valueOf(1L);
	

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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public Short getGenType() {
		return genType;
	}

	public void setGenType(Short genType) {
		this.genType = genType;
	}

	public Integer getNoLength() {
		return noLength;
	}

	public void setNoLength(Integer noLength) {
		this.noLength = noLength;
	}

	public Integer getInitValue() {
		return initValue;
	}

	public void setInitValue(Integer initValue) {
		this.initValue = initValue;
	}

	public Long getCurValue() {
		if (this.curValue == null) return Long.valueOf(-1L);
		return curValue;
	}

	public void setCurValue(Long curValue) {
		this.curValue = curValue;
	}

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	public Short getStep() {
		return step;
	}

	public void setStep(Short step) {
		this.step = step;
	}

	public Long getNewCurValue() {
		return newCurValue;
	}

	public void setNewCurValue(Long newCurValue) {
		this.newCurValue = newCurValue;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SerialNumber)) {
			return false;
		}
		SerialNumber rhs = (SerialNumber) object;
		return new EqualsBuilder().  append(this.id, rhs.id)
		.append(this.name, rhs.name)
		.append(this.alias, rhs.alias)
		.append(this.rule, rhs.rule)
		.append(this.genType, rhs.genType)
		.append(this.noLength, rhs.noLength)
		.append(this.initValue, rhs.initValue)
		.append(this.curValue, rhs.curValue)
		.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
		.append(this.name).append(this.alias).append(this.rule)
		.append(this.genType).append(this.noLength)
		.append(this.initValue).append(this.curValue).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append(
				"name", this.name).append("alias", this.alias).append(
						"rule", this.rule).append("genType", this.genType)
						.append("noLength", this.noLength).append("initValue",
								this.initValue).append("curValue", this.curValue)
								.toString();
	}

}
