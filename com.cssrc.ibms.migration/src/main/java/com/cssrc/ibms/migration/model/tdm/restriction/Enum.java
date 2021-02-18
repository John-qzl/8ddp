package com.cssrc.ibms.migration.model.tdm.restriction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.api.migration.model.IOutDictionary;
@XmlRootElement(name = "枚举")
@XmlAccessorType(XmlAccessType.NONE)
public class Enum implements IOutDictionary{	     
	@XmlAttribute(name="枚举值")
    private String value;
    
	@XmlAttribute(name="枚举值显示名")
    private String displayValue;
      
	@XmlAttribute(name="枚举值描述")
    private String description;
    
	@XmlAttribute(name="启用标志")
    private String isopen;

	private Long sn;
    /**---------------------------------IOutDictionary接口实现------------------------------------------*/

	public String getItemName(){
		return displayValue;
	}
	public Long getSn(){
		return sn;
	}
	public String getItemValue(){
		return value;
	}
	public String getItemKey(){
		return value;
	}
	public String getDescp(){
		return description;
	}
    /**---------------------------------IOutDictionary接口实现------------------------------------------*/

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsopen() {
		return isopen;
	}

	public void setIsopen(String isopen) {
		this.isopen = isopen;
	}
	public void setSn(Long sn) {
		this.sn = sn;
	} 
}
