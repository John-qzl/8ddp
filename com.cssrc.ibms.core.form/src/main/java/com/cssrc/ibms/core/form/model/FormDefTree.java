package com.cssrc.ibms.core.form.model;
 
 import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

 @XmlRootElement(name="formDefTree")
 @XmlAccessorType(XmlAccessType.NONE)
 public class FormDefTree extends BaseModel{
   public static Short LOADTYPE_SYNC = Short.valueOf((short)0);
 
   public static Short LOADTYPE_ASYNC = Short.valueOf((short)1);
 
   @XmlElement
   protected Long id;
 
   @XmlElement
   protected Long formDefId;
 
   @XmlElement
   protected String name;
 
   @XmlElement
   protected String alias;
   protected String formKey = "";
 
   @XmlElement
   protected String treeId;
 
   @XmlElement
   protected String parentId;
 
   @XmlElement
   protected String displayField;
 
   @XmlElement
   protected Short loadType;
 
   @XmlElement
   protected String rootId;
 
   public void setId(Long id) { this.id = id;
   }
 
   public Long getId()
   {
    return this.id;
   }
 
   public void setFormDefId(Long formDefId) {
     this.formDefId = formDefId;
   }
 
   public Long getFormDefId()
   {
     return this.formDefId;
   }
 
   public void setName(String name) {
     this.name = name;
   }
 
   public String getName()
   {
     return this.name;
   }
 
   public void setAlias(String alias) {
    this.alias = alias;
   }
 
   public String getAlias()
   {
     return this.alias;
   }
 
   public void setTreeId(String treeId) {
     this.treeId = treeId;
   }
 
   public String getTreeId()
   {
    return this.treeId;
   }
 
   public void setParentId(String parentId) {
     this.parentId = parentId;
   }
 
   public String getParentId()
   {
     return this.parentId;
   }
 
   public void setDisplayField(String displayField) {
     this.displayField = displayField;
   }
 
   public String getDisplayField()
   {
     return this.displayField;
   }
 
   public Short getLoadType()
   {
     return this.loadType;
   }
 
   public void setLoadType(Short loadType)
   {
     this.loadType = loadType;
   }
 
   public String getRootId()
   {
     return this.rootId;
   }
 
   public void setRootId(String rootId)
   {
     this.rootId = rootId;
   }
 }