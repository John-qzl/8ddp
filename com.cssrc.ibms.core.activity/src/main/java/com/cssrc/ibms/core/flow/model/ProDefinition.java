package com.cssrc.ibms.core.flow.model;
 

import java.util.Date;

import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

import flexjson.JSON;

public class ProDefinition extends BaseModel
{
  private static final long serialVersionUID = 4174463917070121090L;
  public static final Short MAIN = Short.valueOf((short)1);

  public static final Short NOT_MAIN = Short.valueOf((short)0);

  public static final Short STATUS_ENABLE = Short.valueOf((short)1);

  public static final Short STATUS_DISABLE = Short.valueOf((short)0);

  public static final Short IS_DEFAULT = Short.valueOf((short)1);

  public static final Short IS_NOT_DEFAULT = Short.valueOf((short)0);

  public static final Short IS_SKIP_FIRST = Short.valueOf((short)1);

  public static final Short IS_NOT_SKIP_FIRST = Short.valueOf((short)0);
  protected Long defId;
  protected String processName;
  protected String name;
  protected String description;
  protected Date createtime;
  protected Date updatetime;
  protected String deployId;
  protected String pdId;
  protected String defKey;
  protected Integer newVersion;
  protected String defXml;
  protected String drawDefXml;
  protected Short status;
  protected Long parentId;
  protected Short isMain;
  protected Short isDefault = IS_NOT_DEFAULT;

  protected Short skipFirstNode = IS_NOT_SKIP_FIRST;
  protected IGlobalType proType;

  @JSON
  public String getDefXml()
  {
    return this.defXml;
  }

  public void setDefXml(String defXml) {
    this.defXml = defXml;
  }

  public ProDefinition()
  {
  }

  public ProDefinition(Long in_defId)
  {
    setDefId(in_defId);
  }

  public IGlobalType getProType() {
    return this.proType;
  }

  public void setProType(IGlobalType in_proType) {
    this.proType = in_proType;
  }

  public void setProTypeId(Long proTypeId) {
    this.proType.setTypeId(proTypeId);
  }

  public Long getProTypeId() {
    return this.proType == null ? null : this.proType.getTypeId();
  }

  public Long getDefId()
  {
    return this.defId;
  }

  public void setDefId(Long aValue)
  {
    this.defId = aValue;
  }

  public Long getTypeId()
  {
    return getProType() == null ? null : getProType().getTypeId();
  }

  public void setTypeId(Long aValue)
  {
    if (aValue == null)
      this.proType = null;
    else
      this.proType.setTypeId(aValue);
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String aValue)
  {
    this.name = aValue;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String aValue)
  {
    this.description = aValue;
  }

  public Date getCreatetime()
  {
    return this.createtime;
  }

  public void setCreatetime(Date aValue)
  {
    this.createtime = aValue;
  }

  public String getDeployId()
  {
    return this.deployId;
  }

  public void setDeployId(String aValue)
  {
    this.deployId = aValue;
  }

  public String getDrawDefXml()
  {
    return this.drawDefXml;
  }

  public void setDrawDefXml(String drawDefXml) {
    this.drawDefXml = drawDefXml;
  }

  public Short getIsDefault() {
    return this.isDefault;
  }

  public String getProcessName() {
    return this.processName;
  }

  public void setProcessName(String processName) {
    this.processName = processName;
  }

  public Integer getNewVersion() {
    return this.newVersion;
  }

  public void setNewVersion(Integer newVersion) {
    this.newVersion = newVersion;
  }

  public void setIsDefault(Short isDefault) {
    this.isDefault = isDefault;
  }

  public Short getStatus() {
    return this.status;
  }

  public void setStatus(Short status) {
    this.status = status;
  }

  public String getPdId() {
    return this.pdId;
  }

  public void setPdId(String pdId) {
    this.pdId = pdId;
  }

  public String getDefKey() {
    return this.defKey;
  }

  public void setDefKey(String defKey) {
    this.defKey = defKey;
  }

  public Long getParentId() {
    return this.parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public Short getIsMain() {
    return this.isMain;
  }

  public void setIsMain(Short isMain) {
    this.isMain = isMain;
  }

  public Date getUpdatetime() {
    return this.updatetime;
  }

  public void setUpdatetime(Date updatetime) {
    this.updatetime = updatetime;
  }

  public Short getSkipFirstNode() {
    return this.skipFirstNode;
  }

  public void setSkipFirstNode(Short skipFirstNode) {
    this.skipFirstNode = skipFirstNode;
  }
}
 
