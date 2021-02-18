package com.cssrc.ibms.migration.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.migration.model.IOutDicGlobalType;
import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class MigrationGlobalType extends BaseModel implements IGlobalType
{

	protected Long typeId = Long.valueOf(0L);
	protected String typeName = "";
	protected String nodePath = "";
	protected Integer depth = 0;
	protected Long parentId = 0L;
	protected String nodeKey = "";
	protected String catKey = "";
	protected Long sn= Long.valueOf(0L);
	protected Long userId= Long.valueOf(0L);
	protected Long depId = Long.valueOf(0L);
	protected Integer type = Integer.valueOf(0);
	protected String open = "true";
	protected String isParent = "";
	protected Integer isLeaf = 0;
	protected int childNodes = 0;
	/* protected Integer isLeaf;*/
	protected String nodeCode = "";
	protected Short nodeCodeType = Short.valueOf((short)0);
	protected String dataId = "";//数据记录Id
	protected String tableId = "";//业务表Id

	public MigrationGlobalType(){}
	
	
    public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getNodePath() {
		return nodePath;
	}

	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}

	public Integer getDepth() {
		int i = this.nodePath.split("\\.").length - 1;
		return Integer.valueOf(i);
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getNodeKey() {
		return nodeKey;
	}

	public void setNodeKey(String nodeKey) {
		this.nodeKey = nodeKey;
	}

	public String getCatKey() {
		return catKey;
	}

	public void setCatKey(String catKey) {
		this.catKey = catKey;
	}

	public Long getSn() {
		return sn;
	}

	public void setSn(Long sn) {
		this.sn = sn;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public Integer getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public String getIsParent() {
		return this.childNodes > 0 ? "true" : "false";
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public int getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(int childNodes) {
		this.childNodes = childNodes;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public Short getNodeCodeType() {
		return nodeCodeType;
	}

	public void setNodeCodeType(Short nodeCodeType) {
		this.nodeCodeType = nodeCodeType;
	}
	
	

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public boolean equals(Object object)
	{
		if (!(object instanceof MigrationGlobalType)) {
			return false;
		}
		MigrationGlobalType rhs = (MigrationGlobalType)object;
		return new EqualsBuilder()
		.append(this.typeId, rhs.typeId)
		.append(this.typeName, rhs.typeName)
		.append(this.nodePath, rhs.nodePath)
		.append(this.depth, rhs.depth)
		.append(this.parentId, rhs.parentId)
		.append(this.nodeKey, rhs.nodeKey)
		.append(this.catKey, rhs.catKey)
		.append(this.sn, rhs.sn)
		.append(this.userId, rhs.userId)
		.append(this.depId, rhs.depId)
		.append(this.type, rhs.type)
		.append(this.nodeCode, rhs.nodeCode)
		.append(this.nodeCodeType, rhs.nodeCodeType)
		.append(this.dataId, rhs.dataId)
		.append(this.tableId, rhs.tableId)
		.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.typeId)
		.append(this.typeName)
		.append(this.nodePath)
		.append(this.depth)
		.append(this.parentId)
		.append(this.nodeKey)
		.append(this.catKey)
		.append(this.sn)
		.append(this.type)
		.append(this.userId)
		.append(this.depId)
		.append(this.type)
		.append(this.nodeCode)
		.append(this.nodeCodeType)
		.append(this.dataId)
		.append(this.tableId)
		.toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("typeId", this.typeId)
		.append("typeName", this.typeName)
		.append("nodePath", this.nodePath)
		.append("depth", this.depth)
		.append("parentId", this.parentId)
		.append("nodeKey", this.nodeKey)
		.append("catKey", this.catKey)
		.append("sn", this.sn)
		.append("userId", this.userId)
		.append("depId", this.depId)
		.append("type", this.type)
		.append("nodeCode", this.nodeCode)
		.append("nodeCodeType", this.nodeCodeType)
		.append("dataId", this.dataId)
		.append("tableId", this.tableId)
		.toString();
	}
	public static MigrationGlobalType transDicGlobalType(IOutDicGlobalType globalType){
		MigrationGlobalType gl = new MigrationGlobalType();
		gl.setCatKey(IGlobalType.CAT_DIC);
		gl.setTypeName(globalType.getTypeName());
		gl.setNodeKey(globalType.getNodeKey());
		gl.setType(1);
		gl.setNodeCodeType(Short.valueOf((short)0));
		gl.setDepId(globalType.getDepId());
		return gl;
	}
}
 
