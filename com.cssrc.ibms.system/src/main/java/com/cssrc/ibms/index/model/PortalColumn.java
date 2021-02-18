package com.cssrc.ibms.index.model;

public class PortalColumn {
	protected String colName;
	protected int colNum;
	protected int sn;
	protected int height;
	protected String url;
	protected String moreUrl;
	protected String colId;
	protected String closeAllow;
	protected String html;
	protected String loadType;
	protected String iconCls;
	
	protected String key;
	PortalColumn()
	{
	}

	public PortalColumn(String colName, int colNum, int sn, int height, String url, String moreUrl, String colId)
	{
		this.colName = colName;
		this.colNum = colNum;
		this.sn = sn;
		this.height = height;
		this.url = url;
		this.moreUrl = moreUrl;
		this.colId = colId;
	}

	public PortalColumn(String colName, int colNum, int sn, int height, String url, String moreUrl, String colId, String closeAllow)
	{
		this.colName = colName;
		this.colNum = colNum;
		this.sn = sn;
		this.height = height;
		this.url = url;
		this.moreUrl = moreUrl;
		this.colId = colId;
		this.closeAllow = closeAllow;
	}

	public String getCloseAllow()
	{
		return this.closeAllow;
	}

	public void setCloseAllow(String closeAllow) {
		this.closeAllow = closeAllow;
	}

	public String getMoreUrl() {
		return this.moreUrl;
	}

	public void setMoreUrl(String moreUrl) {
		this.moreUrl = moreUrl;
	}

	public String getColName() {
		return this.colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public int getColNum() {
		return this.colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public int getSn() {
		return this.sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getColId() {
		return this.colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public String getHtml() {
		return this.html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getLoadType() {
		return this.loadType;
	}

	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}

	public String getIconCls() {
		return this.iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
}
