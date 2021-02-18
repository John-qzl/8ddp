package com.cssrc.ibms.core.form.service;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
/*    */ 
/*    */ @XmlRootElement(name="bpm")
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ public class FormQueryXmlList
/*    */ {
/*    */ 
/*    */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="formQuerys", type=FormQueryXml.class)})
/*    */   private List<FormQueryXml> bpmFormQueryXmlList;
/*    */ 
/*    */   public List<FormQueryXml> getBpmFormQueryXmlList()
/*    */   {
/* 22 */     return this.bpmFormQueryXmlList;
/*    */   }
/*    */ 
/*    */   public void setBpmFormQueryXmlList(List<FormQueryXml> bpmFormQueryXmlList) {
/* 26 */     this.bpmFormQueryXmlList = bpmFormQueryXmlList;
/*    */   }
/*    */ }