package com.cssrc.ibms.core.form.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.api.form.model.IFormDialogXml;
import com.cssrc.ibms.core.form.service.FormDialogCombinate;

 @XmlRootElement(name="formDialogs")
 @XmlAccessorType(XmlAccessType.FIELD)
 public class FormDialogXml implements IFormDialogXml
 {
 
   @XmlElement(name="dialog", type=FormDialog.class)
   private FormDialog bpmFormDialog;
 
   @XmlElement(name="dialogCombinate", type=FormDialogCombinate.class)
   private FormDialogCombinate bpmFormDialogCombinate;
 
   public FormDialog getBpmFormDialog()
   {
     return this.bpmFormDialog;
   }
 
   public void setBpmFormDialog(FormDialog bpmFormDialog) {
     this.bpmFormDialog = bpmFormDialog;
   }
   public FormDialogCombinate getBpmFormDialogCombinate() {
     return this.bpmFormDialogCombinate;
   }
 
   public void setBpmFormDialogCombinate(FormDialogCombinate bpmFormDialogCombinate) {
     this.bpmFormDialogCombinate = bpmFormDialogCombinate;
   }
 }

          
