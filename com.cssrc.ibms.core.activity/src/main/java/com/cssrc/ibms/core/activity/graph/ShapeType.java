package com.cssrc.ibms.core.activity.graph;


/**
 * 对象功能:程序设计器 连接类型 
 * 开发人员:zhulongchao 
 */
public enum ShapeType {
	STRAIGHT("straight"),
	FREE("free"),
	ORTHOGONAL("orthogonal"),
	OBLIQUE("oblique");

	private String text;

	ShapeType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}

	public static ShapeType fromString(String text) {
		if (text != null) {
			for (ShapeType type : ShapeType.values()) {
				if (text.equalsIgnoreCase(type.text)) {
					return type;
				}
			}
		}
		return null;
	}
}
