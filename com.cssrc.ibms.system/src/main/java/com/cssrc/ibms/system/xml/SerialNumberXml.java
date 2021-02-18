package com.cssrc.ibms.system.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.system.model.SerialNumber;

@XmlRootElement(name = "identities")
@XmlAccessorType(XmlAccessType.FIELD)
public class SerialNumberXml {

	@XmlElement(name = "SerialNumber", type = SerialNumber.class)
	private SerialNumber serialNumber;

	public SerialNumber getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(SerialNumber serialNumber) {
		this.serialNumber = serialNumber;
	}
}
