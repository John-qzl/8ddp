package com.cssrc.ibms.core.activity.graph;


/**
 * 对象功能:程序设计器 端口类型 
 * 开发人员:zhulongchao 
 */
public enum PortType {
	POSITION("position"),
	NODE_PART_REFERENCE("nodePartReference"),
	AUTOMATIC_SIDE("automaticSide");

	private String text;

	PortType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}

	public static PortType fromString(String text) {
		if (text != null) {
			for (PortType type : PortType.values()) {
				if (text.equalsIgnoreCase(type.text)) {
					return type;
				}
			}
		}
		return null;
	}
}
