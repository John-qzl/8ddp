package com.cssrc.ibms.index.model;

public class PortalItem {
	protected String id;
	protected String name;
	protected String url;
	protected String moreUrl;
	protected String closeAllow;
	protected String html;
	protected String type;
	protected String iconCls;
	protected String key;
	
	PortalItem()
	{
	}
	
	public PortalItem(String id, String name, String type)
	{
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMoreUrl() {
		return moreUrl;
	}
	public void setMoreUrl(String moreUrl) {
		this.moreUrl = moreUrl;
	}
	public String getCloseAllow() {
		return closeAllow;
	}
	public void setCloseAllow(String closeAllow) {
		this.closeAllow = closeAllow;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIconCls() {
		return iconCls;
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
