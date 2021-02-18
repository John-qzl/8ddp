package com.cssrc.ibms.core.util.dbom;

import java.util.Comparator;

public class NodeIDComparator implements Comparator<Node> {
	public int compare(Node o1, Node o2) {
		try{
			Long j1 = Long.parseLong(o1.id);
			Long j2 = Long.parseLong(o2.id);
			return j1 == j2 ? 0 : j1 < j2 ? -1 : 1;
		}catch(Exception e){
			return 0;
		}
		
	}
}
