package com.cssrc.ibms.core.form.model; 

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.api.form.model.IQueryField;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;


@SuppressWarnings("serial")
@XmlRootElement(name = "queryField")
@XmlAccessorType(XmlAccessType.NONE)
public class QueryField extends BaseModel implements IQueryField
{

  @XmlAttribute
  protected Long id;
  @XmlAttribute
  protected Long sqlId;
  @XmlAttribute
  protected String name;
  @XmlAttribute
  protected String type;
  @XmlAttribute
  protected String fieldDesc;
  @XmlAttribute
  protected Short isShow;
  @XmlAttribute
  protected Short isSearch;
  @XmlAttribute
  protected Short controlType;
  @XmlAttribute
  protected String controlContent;
  @XmlAttribute
  protected String format;

  public Long getId()
  {
    return this.id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public Long getSqlId() {
    return this.sqlId;
  }
  public void setSqlId(Long sqlId) {
    this.sqlId = sqlId;
  }
  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getType() {
    return this.type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getFieldDesc() {
    return this.fieldDesc;
  }
  public void setFieldDesc(String fieldDesc) {
    this.fieldDesc = fieldDesc;
  }
  public Short getIsShow() {
    return this.isShow;
  }
  public void setIsShow(Short isShow) {
    this.isShow = isShow;
  }
  public Short getIsSearch() {
    return this.isSearch;
  }
  public void setIsSearch(Short isSearch) {
    this.isSearch = isSearch;
  }
  public Short getControlType() {
    return this.controlType;
  }
  public void setControlType(Short controlType) {
    this.controlType = controlType;
  }
  public String getControlContent() {
    return this.controlContent;
  }
  public void setControlContent(String controlContent) {
    this.controlContent = controlContent;
  }
  public String getFormat() {
    return this.format;
  }
  public void setFormat(String format) {
    this.format = format;
  }
}