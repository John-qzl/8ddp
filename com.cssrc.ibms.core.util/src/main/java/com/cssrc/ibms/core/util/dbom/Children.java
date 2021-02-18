package com.cssrc.ibms.core.util.dbom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Children {
	public List<Node> list = new ArrayList<Node>();

	public int getSize() {
		return this.list.size();
	}

	public void addChild(Node node) {
		this.list.add(node);
	}

	public String toString() {
		StringBuffer result = new StringBuffer("[");
		for (Iterator<Node> it = this.list.iterator(); it.hasNext();) {
			result.append(((Node) it.next()).toString()).append(",");
		}
		if (!this.list.isEmpty()) {
			result.deleteCharAt(result.length() - 1);
		}
		result.append("]");
		return result.toString();
	}

	public void sortChildren() {
		Collections.sort(this.list, new NodeIDComparator());

		for (Iterator<Node> it = this.list.iterator(); it.hasNext();)
			it.next().sortChildren();
	}

	public void sortSnChildren() {
		Collections.sort(this.list, new NodeSnComparator());

		for (Iterator<Node> it = this.list.iterator(); it.hasNext();)
			it.next().sortSnChildren();
	}
}
