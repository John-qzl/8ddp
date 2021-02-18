package com.cssrc.ibms.dp.sync.bean;

import java.util.HashSet;
import java.util.Set;

public class UserBean  implements java.io.Serializable{
	private Set userset = new HashSet(0);

	public Set getUserset() {
		return userset;
	}

	public void setUserset(Set userset) {
		this.userset = userset;
	}
}
