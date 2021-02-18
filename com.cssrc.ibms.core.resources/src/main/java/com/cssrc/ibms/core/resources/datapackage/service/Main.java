package com.cssrc.ibms.core.resources.datapackage.service;

public class Main {

	public static void main(String[] args) {
		//String order="1.1";
		String order="1.2.3";
		int lastd=order.lastIndexOf(".");
		String pre=order.substring(0,lastd);
		String last=order.substring(lastd+1,order.length());
		System.out.println(pre+"."+(Integer.valueOf(last)+1));
	}

}
