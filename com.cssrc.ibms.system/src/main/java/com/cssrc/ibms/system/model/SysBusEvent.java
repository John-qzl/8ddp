package com.cssrc.ibms.system.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.BaseSysBusEvent;
import com.cssrc.ibms.api.system.model.ISysBusEvent;

@XmlRootElement(name = "sysBusEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class SysBusEvent extends BaseSysBusEvent implements ISysBusEvent{

	private static final long serialVersionUID = 1L;

	@XmlElement
	protected Long id;

	@XmlElement
	protected String formkey;

	@XmlElement
	protected String jsPreScript;

	@XmlElement
	protected String jsAfterScript;

	@XmlElement
	protected String preScript;

	@XmlElement
	protected String afterScript;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setFormkey(String formkey) {
		this.formkey = formkey;
	}

	public String getFormkey() {
		return this.formkey;
	}

	public void setJsPreScript(String jsPreScript) {
		this.jsPreScript = jsPreScript;
	}

	public String getJsPreScript() {
		return this.jsPreScript;
	}

	public void setJsAfterScript(String jsAfterScript) {
		this.jsAfterScript = jsAfterScript;
	}

	public String getJsAfterScript() {
		return this.jsAfterScript;
	}

	public void setPreScript(String preScript) {
		this.preScript = preScript;
	}

	public String getPreScript() {
		return this.preScript;
	}

	public void setAfterScript(String afterScript) {
		this.afterScript = afterScript;
	}

	public String getAfterScript() {
		return this.afterScript;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysBusEvent)) {
			return false;
		}
		SysBusEvent rhs = (SysBusEvent) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.formkey,
				rhs.formkey)
		.append(this.jsPreScript, rhs.jsPreScript)
		.append(this.jsAfterScript, rhs.jsAfterScript)
		.append(this.preScript, rhs.preScript)
		.append(this.afterScript, rhs.afterScript)
		.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id)
		.append(this.formkey)
		.append(this.jsPreScript)
		.append(this.jsAfterScript)
		.append(this.preScript)
		.append(this.afterScript)
		.toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id)
		.append("formkey", this.formkey)
		.append("jsPreScript", this.jsPreScript)
		.append("jsAfterScript", this.jsAfterScript)
		.append("preScript", this.preScript)
		.append("afterScript", this.afterScript)
		.toString();
	}
}