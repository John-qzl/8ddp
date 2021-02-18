package com.cssrc.ibms.core.util.dbom;

import java.util.Comparator;

public class NodeSnComparator implements Comparator<Node> {
	public int compare(Node o1, Node o2) {
		int j1 = Integer.parseInt(o1.sn);
		int j2 = Integer.parseInt(o2.sn);
		return j1 == j2 ? 0 : j1 < j2 ? -1 : 1;
	}
}
