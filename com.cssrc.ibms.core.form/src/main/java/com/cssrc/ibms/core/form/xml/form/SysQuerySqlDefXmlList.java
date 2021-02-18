package com.cssrc.ibms.core.form.xml.form;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="querySqls")
@XmlAccessorType(XmlAccessType.FIELD)
public class SysQuerySqlDefXmlList
{

  @XmlElements({@javax.xml.bind.annotation.XmlElement(name="querySqlDefs", type=SysQuerySqlDefXml.class)})
  private List<SysQuerySqlDefXml> sysQuerySqlDefXmlList;

  public List<SysQuerySqlDefXml> getSysQuerySqlDefXmlList()
  {
     return this.sysQuerySqlDefXmlList;
  }

  public void setSysQuerySqlDefXmlList(List<SysQuerySqlDefXml> sysQuerySqlDefXmlList)
  {
     this.sysQuerySqlDefXmlList = sysQuerySqlDefXmlList;
  }
}
