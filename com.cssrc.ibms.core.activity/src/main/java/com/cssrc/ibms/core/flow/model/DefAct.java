package com.cssrc.ibms.core.flow.model;

import com.cssrc.ibms.api.activity.model.IDefAct;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class DefAct extends BaseModel implements Cloneable, IDefAct {
	protected Long id;
	protected Long authorizeId;
	protected String defKey;
	protected String defName;
	protected String rightContent;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAuthorizeId() {
		return this.authorizeId;
	}

	public void setAuthorizeId(Long authorizeId) {
		this.authorizeId = authorizeId;
	}

	public String getDefKey() {
		return this.defKey;
	}

	public void setDefKey(String defKey) {
		this.defKey = defKey;
	}

	public String getDefName() {
		return this.defName;
	}

	public void setDefName(String defName) {
		this.defName = defName;
	}

	public String getRightContent() {
		return this.rightContent;
	}

	public void setRightContent(String rightContent) {
		this.rightContent = rightContent;
	}
}
