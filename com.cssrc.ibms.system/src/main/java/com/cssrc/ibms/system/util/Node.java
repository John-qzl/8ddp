package com.cssrc.ibms.system.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.core.util.dbom.Children;

public class Node {
	public String id;
	public String text;
	public String iconCls;
	public String parentId;
	public String sn;
	public List<Map<String, Object>> otherList = new ArrayList<Map<String, Object>>();

	public Children children = new Children();

	public String toString() {
		StringBuffer result = new StringBuffer("{id : '" + this.id
				+ "', text : '" + this.text + "', iconCls : '" + this.iconCls + "', expanded : true");

		if ((this.otherList != null) && (this.otherList.size() > 0)) {
			for (Map<String, Object> map : this.otherList) {
				result.append(", ").append(map.get("key")).append(" : '")
						.append(map.get("value")).append("'");
			}
		}

		if ((this.children != null) && (this.children.getSize() != 0))
			result.append(", children : ").append(this.children.toString());
		else {
			result.append(", leaf : true");
		}
		result.append("}");
		return result.toString();
	}

	public void sortChildren() {
		if ((this.children != null) && (this.children.getSize() != 0))
			this.children.sortChildren();
	}

	public void sortSnChildren() {
		if ((this.children != null) && (this.children.getSize() != 0))
			this.children.sortSnChildren();
	}
}
