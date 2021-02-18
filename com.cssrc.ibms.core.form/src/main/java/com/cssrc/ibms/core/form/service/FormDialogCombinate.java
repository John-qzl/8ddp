package com.cssrc.ibms.core.form.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.api.form.intf.IFormDialogCombinate;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.form.model.FormDialog;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/*     */ 
/*     */ @XmlRootElement(name="dialogCombinate")
/*     */ @XmlAccessorType(XmlAccessType.NONE)
/*     */ public class FormDialogCombinate extends BaseModel implements IFormDialogCombinate
/*     */ {
/*     */ 
/*     */   @XmlElement
/*     */   protected Long id;
/*     */ 
/*     */   @XmlElement
/*     */   protected String name;
/*     */ 
/*     */   @XmlElement
/*     */   protected String alias;
/*     */ 
/*     */   @XmlElement
/*     */   protected int width;
/*     */ 
/*     */   @XmlElement
/*     */   protected int height;
/*     */ 
/*     */   @XmlElement
/*     */   protected Long treeDialogId;
/*     */ 
/*     */   @XmlElement
/*     */   protected String treeDialogName;
/*     */ 
/*     */   @XmlElement
/*     */   protected Long listDialogId;
/*     */ 
/*     */   @XmlElement
/*     */   protected String listDialogName;
/*     */ 
/*     */   @XmlElement
/*     */   protected String field;
/*     */ 
/*     */   @XmlElement
/*     */   protected FormDialog treeDialog;
/*     */ 
/*     */   @XmlElement
/*     */   protected FormDialog listDialog;
/*     */ 
/*     */   public void setId(Long id)
/*     */   {
/*  51 */     this.id = id;
/*     */   }
/*     */ 
/*     */   public Long getId()
/*     */   {
/*  60 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  71 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/*  79 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public String getAlias()
/*     */   {
/*  90 */     return this.alias;
/*     */   }
/*     */ 
/*     */   public void setAlias(String alias)
/*     */   {
/*  98 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 109 */     return this.width;
/*     */   }
/*     */ 
/*     */   public void setWidth(int width)
/*     */   {
/* 117 */     this.width = width;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 128 */     return this.height;
/*     */   }
/*     */ 
/*     */   public void setHeight(int height)
/*     */   {
/* 136 */     this.height = height;
/*     */   }
/*     */ 
/*     */   public void setTreeDialogId(Long treeDialogId) {
/* 140 */     this.treeDialogId = treeDialogId;
/*     */   }
/*     */ 
/*     */   public Long getTreeDialogId()
/*     */   {
/* 149 */     return this.treeDialogId;
/*     */   }
/*     */ 
/*     */   public void setTreeDialogName(String treeDialogName) {
/* 153 */     this.treeDialogName = treeDialogName;
/*     */   }
/*     */ 
/*     */   public String getTreeDialogName()
/*     */   {
/* 162 */     return this.treeDialogName;
/*     */   }
/*     */ 
/*     */   public void setListDialogId(Long listDialogId) {
/* 166 */     this.listDialogId = listDialogId;
/*     */   }
/*     */ 
/*     */   public Long getListDialogId()
/*     */   {
/* 175 */     return this.listDialogId;
/*     */   }
/*     */ 
/*     */   public void setListDialogName(String listDialogName) {
/* 179 */     this.listDialogName = listDialogName;
/*     */   }
/*     */ 
/*     */   public String getListDialogName()
/*     */   {
/* 188 */     return this.listDialogName;
/*     */   }
/*     */ 
/*     */   public void setField(String field) {
/* 192 */     this.field = field;
/*     */   }
/*     */ 
/*     */   public String getField()
/*     */   {
/* 201 */     return this.field;
/*     */   }
/*     */ 
/*     */   public FormDialog getTreeDialog()
/*     */   {
/* 212 */     if (this.treeDialog == null) {
/* 213 */       this.treeDialog = ((FormDialog)((FormDialogService)AppUtil.getBean(FormDialogService.class)).getById(this.treeDialogId));
/*     */     }
/* 215 */     return this.treeDialog;
/*     */   }
/*     */ 
/*     */   public FormDialog getListDialog()
/*     */   {
/* 226 */     if (this.listDialog == null) {
/* 227 */       this.listDialog = ((FormDialog)((FormDialogService)AppUtil.getBean(FormDialogService.class)).getById(this.listDialogId));
/*     */     }
/* 229 */     return this.listDialog;
/*     */   }
/*     */ }


