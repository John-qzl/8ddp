package com.cssrc.ibms.core.form.service;

 import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.form.model.FormQuery;
 
 @XmlRootElement(name="formQuerys")
 @XmlAccessorType(XmlAccessType.FIELD)
 public class FormQueryXml
 {
 
   @XmlElement(name="query", type=FormQuery.class)
   private FormQuery bpmFormQuery;
 
   public FormQuery getBpmFormQuery()
   {
     return this.bpmFormQuery;
   }
 
   public void setBpmFormQuery(FormQuery bpmFormQuery)
   {
     this.bpmFormQuery = bpmFormQuery;
   }
 }