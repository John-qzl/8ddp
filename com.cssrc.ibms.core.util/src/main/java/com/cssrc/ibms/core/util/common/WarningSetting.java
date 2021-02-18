package com.cssrc.ibms.core.util.common;
/**
 * 
 *@author Yangbo 2016-7-22
 *
 */
public class WarningSetting {
	private Integer level;
	private String name;
	private String color;

	public WarningSetting(Integer level, String name, String color) {
		this.level = level;
		this.name = name;
		this.color = color;
	}

	public WarningSetting() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
