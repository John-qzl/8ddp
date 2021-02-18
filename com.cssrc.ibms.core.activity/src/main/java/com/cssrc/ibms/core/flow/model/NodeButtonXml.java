package com.cssrc.ibms.core.flow.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
* <pre>
* 对象功能:按钮List的XMl配置
* 开发人员:zhulongchao 
* </pre>
*/

@XmlRootElement(name = "bpm")
@XmlAccessorType(XmlAccessType.FIELD)
public class NodeButtonXml {
	
	@XmlElementWrapper(name = "buttons")
	@XmlElements({ @XmlElement(name = "button", type = Button.class) })
    private List<Button> buttons;

	public List<Button> getButtons() {
		return buttons;
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}
	
	
}
