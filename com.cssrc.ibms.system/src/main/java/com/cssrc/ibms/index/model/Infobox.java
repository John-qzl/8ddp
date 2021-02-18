package com.cssrc.ibms.index.model;

public class Infobox {
	public static final String COLOR_BLUE = "blue";
	public static final String COLOR_BLUE2 = "blue2";
	public static final String COLOR_BLUE3 = "blue3";
	public static final String COLOR_RED = "red";
	public static final String COLOR_BROWN = "brown";
	public static final String COLOR_LIGHT_BROWN = "light-brown";
	public static final String COLOR_WOOD = "wood";
	public static final String COLOR_ORANGE = "orange";
	public static final String COLOR_ORANGE2 = "orange2";
	public static final String COLOR_GREEN = "green";
	public static final String COLOR_GREEN2 = "green2";
	public static final String COLOR_BLACK = "black";
	public static final String COLOR_DARK = "dark";
	public static final String COLOR_PINK = "pink";
	public static final int TYPE_ICON = 0;
	public static final int TYPE_PROGRESS = 1;
	public static final int TYPE_CHART = 2;
	public static final int STAT_TYPE_STAT = 1;
	public static final int STAT_TYPE_BADGE = 2;
	public static final int STAT_STATUS_UP = 1;
	public static final int STAT_STATUS_DOWN = 2;
	protected String icon;
	protected String color;
	protected String dataText;
	protected String dataContent;
	protected Integer type = Integer.valueOf(0);
	protected String data;
	protected Integer statType;
	protected String statData;
	protected Integer statStatus;
	protected String url;

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDataText() {
		return this.dataText;
	}

	public void setDataText(String dataText) {
		this.dataText = dataText;
	}

	public String getDataContent() {
		return this.dataContent;
	}

	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getStatType() {
		return this.statType;
	}

	public void setStatType(Integer statType) {
		this.statType = statType;
	}

	public String getStatData() {
		return this.statData;
	}

	public void setStatData(String statData) {
		this.statData = statData;
	}

	public Integer getStatStatus() {
		return this.statStatus;
	}

	public void setStatStatus(Integer statStatus) {
		this.statStatus = statStatus;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
