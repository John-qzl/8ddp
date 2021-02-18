package com.cssrc.ibms.dp.sync.bean;

import java.util.HashSet;
import java.util.Set;

public class ConditionsBean implements java.io.Serializable{
	
	private Set condition = new HashSet(0);

	public Set getCondition() {
		return condition;
	}

	public void setCondition(Set condition) {
		this.condition = condition;
	}
	
}
