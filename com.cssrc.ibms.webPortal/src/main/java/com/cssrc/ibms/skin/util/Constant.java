package com.cssrc.ibms.skin.util;

public class Constant {
	//自定义skin.less文件存储位置
	public static String SKIN_LESS_ORG = "/styles/less/skin/default/skin" + System.currentTimeMillis() + ".less";
	//系统运行依赖的skin.less
	public static String SKIN_LESS_APP = "/styles/less/skin/skin.less";
	
	//系统运行依赖的frame.less
	public static String FRAME_LESS = "/styles/less/frame/default/frame.less";
	//系统运行前编译frame.less生成的css文件
	public static String FRAME_CSS= "/styles/sunplat-2.0/frame/def/frame" + System.currentTimeMillis() + ".css";
}
