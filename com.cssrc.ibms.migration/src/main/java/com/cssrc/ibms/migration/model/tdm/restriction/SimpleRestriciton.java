package com.cssrc.ibms.migration.model.tdm.restriction;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.api.migration.model.IOutDictionary;
import com.cssrc.ibms.api.migration.model.IOutDicGlobalType;
import com.cssrc.ibms.api.system.model.IGlobalType;
@XmlRootElement(name = "枚举约束")
@XmlAccessorType(XmlAccessType.NONE)
public class SimpleRestriciton implements IOutDicGlobalType{
	
	@XmlAttribute(name="名称")
    private String name;
    
    @XmlAttribute(name="显示名")
    private String displayName;   
    
    /** 此约束的数据类型 */
    @XmlAttribute(name="数据类型")
    private String dataType;
    
    /** 是否多选，TRUE为当前记录可以选择多个枚举值，FALSE不可以多选 */
    @XmlAttribute(name="是否多选")
    private String isMultiSelected;
    
    /** The description. */
    @XmlAttribute(name="描述")
    private String description;
    
    /** 错误信息，用户出现错误操作弹出的错误信息*/
    @XmlAttribute(name="错误信息")
    private String errorInfo;
    
    @XmlAttribute(name="显示模式")
    private String displayType;
    
    @XmlElement(name="枚举")
    private List<Enum> enums;	
    
    private Long sn;
    private Long parentId;
    /**---------------------------------IOutGlobalType接口实现------------------------------------------*/
    
    public String getTypeName(){
    	return displayName;
    }
	public Long getSn(){
		return sn;
	}
	public String getNodeKey(){
		return name;
	}
	public List<IOutDictionary> getDicList(){
		List<IOutDictionary> list = new ArrayList();
		list.addAll(enums);
		return list;
	}
	public Long getDepId(){
		return 2L;
	}
    /**---------------------------------IOutGlobalType接口实现------------------------------------------*/
   
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getIsMultiSelected() {
		return isMultiSelected;
	}

	public void setIsMultiSelected(String isMultiSelected) {
		this.isMultiSelected = isMultiSelected;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public List<Enum> getEnums() {
		return enums;
	}

	public void setEnums(List<Enum> enums) {
		this.enums = enums;
	}
	public void setSn(Long sn) {
		this.sn = sn;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
