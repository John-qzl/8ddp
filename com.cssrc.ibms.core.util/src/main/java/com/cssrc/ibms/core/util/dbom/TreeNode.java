package com.cssrc.ibms.core.util.dbom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1886985187091914457L;

	private String id;
	
	private String iconCls;
	
	private String iconSkin;

	private String url;
	
	private String text;
	
	private Boolean leaf = false; 
	
	private Boolean expanded = false;
	
	private Boolean isParent = true;
	
	private List<TreeNode> children = new ArrayList<TreeNode>();
	
	private String parentId;
	
	private String systemNode;//是否是系统节点<用于附件管理，不可删除，只可编辑>
	
	private String midIcon;
	
	private String bigIcon;

	private String code; //代号
	
	private String type; //节点类型；-1:DBom节点；0：静态节点；1：动态节点。
	
	//对应外键字段名称
	private String fieldName;
	
	//对应外键字段值
	private String fieldValue;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public Boolean getExpanded() {
		return expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSystemNode() {
		return systemNode;
	}

	public void setSystemNode(String systemNode) {
		this.systemNode = systemNode;
	}

	/**
	 * @return the midIcon
	 */
	public String getMidIcon() {
		return midIcon;
	}

	/**
	 * @param midIcon the midIcon to set
	 */
	public void setMidIcon(String midIcon) {
		this.midIcon = midIcon;
	}

	/**
	 * @return the bigIcon
	 */
	public String getBigIcon() {
		return bigIcon;
	}

	/**
	 * @param bigIcon the bigIcon to set
	 */
	public void setBigIcon(String bigIcon) {
		this.bigIcon = bigIcon;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}
	
}
