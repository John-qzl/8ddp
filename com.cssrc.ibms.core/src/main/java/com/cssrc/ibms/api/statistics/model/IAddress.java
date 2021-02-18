package com.cssrc.ibms.api.statistics.model;

public interface IAddress {
	public static final String NAME = "address";
	Long getAddressId();
	void setAddressId(Long addressId);
	Long getToolId();
	void setToolId(Long toolId);
	String getUrl();
	void setUrl(String url);
	String getViewDef();
	void setViewDef(String viewDef);
	String getAddressDesc();
	void setAddressDesc(String addressdesc);
	String getAlias();
	void setAlias(String alias);
	public boolean equals(Object object);
}
