package com.cssrc.ibms.system.model;

/**
 * 文件类别记录表(分类与记录Id关联)
 * @author YangBo
 *
 */
public class SysFileType extends GlobalType{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SysFileType(){
		
	}
	
	public SysFileType(GlobalType globalType){
		this.typeId = globalType.getTypeId();
		this.typeName = globalType.getTypeName();
		this.nodePath = globalType.getNodePath();
		this.depth = globalType.getDepth();
		this.parentId = globalType.getParentId();
		this.nodeKey = globalType.getNodeKey();
		this.catKey = globalType.getCatKey();
		this.sn = globalType.getSn();
		this.userId = globalType.getUserId();
		this.depId = globalType.getDepId();
		this.type = globalType.getType();
		this.open = globalType.getOpen();
		this.isParent = globalType.getIsParent();
		this.isLeaf = globalType.getIsLeaf();
		this.childNodes = globalType.getChildNodes();
		this.nodeCode = globalType.getNodeCode();
		this.nodeCodeType = globalType.getNodeCodeType();
	}

}
