package com.cssrc.ibms.api.form.model;

public interface IFormRights {
	
	public static final short FieldRights=1;//字段
	
	public static final short TableRights=2;//子表 字段
	
	public static final short OpinionRights=3;//意见
	
	public static final short TableShowRights=4; //子表是否显示
	
	public static final short TableRelRights=5;//关联表字段
	
	public static final short TableRelShowRights=6;//
	
	public static final short AttachFileRights=7;//附件文件权限 by YangBo
	
	public static final short tableGroupRights=8;//表字段分组权限

	/** 无*/
	public static String TYPE_NONE = "none";
	/**所有人*/
	public static String TYPE_EVERYONE = "everyone";
	/**隐藏显示*/
	public static String TYPE_HIDDENFIELD = "hiddenfield";
	/**用户*/
	public static String TYPE_USER = "user";
	/**角色*/
	public static String TYPE_ROLE = "role";
	/**组织*/
	public static String TYPE_ORG = "org";
	/**组织负责人*/
	public static String TYPE_ORGMGR ="orgMgr";
	/**岗位*/
	public static String TYPE_POS ="pos";
	Long getFormDefId();
	Long getId();
	void setFormDefId(Long formKey);
	void setActDefId(String actDefId);
	short getType();
	String getPermission();
	void setPermission(String parsePermission);
	
	
}