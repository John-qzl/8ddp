package com.cssrc.ibms.core.form.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
 
 @XmlRootElement(name="bpm")
 @XmlAccessorType(XmlAccessType.FIELD)
 public class FormDialogXmlList
 {
 
   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="formDialogs", type=FormDialogXml.class)})
   private List<FormDialogXml> bpmFormDialogXmlList;
 
   public List<FormDialogXml> getBpmFormDialogXmlList()
   {
     return this.bpmFormDialogXmlList;
   }
 
   public void setBpmFormDialogXmlList(List<FormDialogXml> bpmFormDialogXmlList) {
     this.bpmFormDialogXmlList = bpmFormDialogXmlList;
   }
 }