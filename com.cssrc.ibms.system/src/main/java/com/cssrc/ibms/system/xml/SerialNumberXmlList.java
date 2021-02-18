package com.cssrc.ibms.system.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="system")
@XmlAccessorType(XmlAccessType.FIELD)
public class SerialNumberXmlList {

	@XmlElements({@javax.xml.bind.annotation.XmlElement(name="identities", type=SerialNumberXml.class)})
	private List<SerialNumberXml> serialNumberXmlList;

	public List<SerialNumberXml> getSerialNumberXmlList()
	{
		return this.serialNumberXmlList;
	}

	public void setSerialNumberXmlList(List<SerialNumberXml> serialNumberXmlList) {
		this.serialNumberXmlList = serialNumberXmlList;
	}
}

