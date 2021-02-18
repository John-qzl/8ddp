package com.cssrc.ibms.system.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.cssrc.ibms.api.system.model.ISysParameter;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

/**
 * SysParameter entity. @author yangbo
 */

public class SysParameter  extends BaseModel implements ISysParameter{
    // Fields    
	@SysFieldDescription(detail="参数ID")
	protected Long id;
	@SysFieldDescription(detail="参数名称")
	protected String paramname;
	@SysFieldDescription(detail="参数值的数据类型",maps="{\"1\":\"Int\",\"0\":\"String\"}")
	protected String datatype="0";//‘0’代表参数值的数据类型为String,‘1’代表Int，默认为0
	@SysFieldDescription(detail="参数值")
	protected String paramvalue;
	@SysFieldDescription(detail="参数描述")
	protected String paramdesc;
	@SysFieldDescription(detail="分类的类型")
	protected String type;//分类的类型
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParamname() {
		return this.paramname;
	}

	public void setParamname(String paramname) {
		this.paramname = paramname;
	}

	public String getDatatype() {
		return this.datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getParamvalue() {
		return this.paramvalue;
	}

	public void setParamvalue(String paramvalue) {
		this.paramvalue = paramvalue;
	}

	public String getParamdesc() {
		return this.paramdesc;
	}

	public void setParamdesc(String paramdesc) {
		this.paramdesc = paramdesc;
	}
	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysParameter)) {
			return false;
		}
		SysParameter rhs = (SysParameter) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.paramname,
				rhs.paramname).append(this.datatype, rhs.datatype).append(this.paramvalue,
				rhs.paramvalue).append(this.paramdesc, rhs.paramdesc).append(this.type, rhs.type)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.paramname).append(this.datatype).append(this.paramvalue)
				.append(this.paramdesc).append(this.type).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("paramname",
				this.paramname).append("datatype", this.datatype).append("paramvalue",
				this.paramvalue).append("paramdesc", this.paramdesc).append("type", this.type).toString();
	}
    
    




   

    
    



}