package com.cssrc.ibms.api.sysuser.model;

public interface IResources {
	// LOGO路径
	public final static String LOGO_PATH = "/styles/default/images/logo";
	// ICON路径
	public final static String ICON_PATH = "/styles/default/images/resicon";
	// 图标类型
	public final static String ICON_TYPE = "PNG|JPG|JPEG|GIF";
	// 扩展的功能按钮图标路径
	public final static String ICON_EXTEND = "/styles/oldDefault/images/extendIcon";
	// 扩展功能样式文件位置
	public final static String EXTEND_CSS_PATH = "/styles/default/css/form.css";

	public final static String ICON_DEFAULT_FOLDER = "/styles/default/images/icon/tree_folder.gif";
	public final static String ICON_DEFAULT_LEAF = "/styles/default/images/icon/tree_file.gif";

	/**
	 * 根结点的父ID
	 */
	public final static long ROOT_PID = -1;// 重要
	/**
	 * 根结点的ID
	 */
	public final static long ROOT_ID = 0;

	/**
	 * 根结点的icon
	 */
	public final static String ROOT_ICON = "/styles/default/images/icon/remoteupload.gif";

	/**
	 * 是否打开0否，1是。
	 */
	public final static Short IS_OPEN_N = 0;
	public final static Short IS_OPEN_Y = 1;
	/**
	 * 是否文件夹0否，1是(功能节点父节点为1)
	 */
	public final static Short IS_FOLDER_N = 0;
	public final static Short IS_FOLDER_Y = 1;
	/**
	 * 是否显示到菜单0否，1是
	 */
	public final static Short IS_DISPLAY_IN_MENU_N = 0;
	public final static Short IS_DISPLAY_IN_MENU_Y = 1;

	public final static String IS_CHECKED_N = "false";
	public final static String IS_CHECKED_Y = "true";
	/**
	 * 是否重新打开窗口0否，1是
	 */
	public static final Short IS_NEWOPEN_N = Short.valueOf((short) 0);
	public static final Short IS_NEWOPEN_Y = Short.valueOf((short) 1);
	
	
	public String getResName();
	public String getAlias();
	public String getDefaultUrl();
	public Long getParentId();

}