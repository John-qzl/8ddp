package com.cssrc.ibms.system.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.migration.model.IOutDicGlobalType;
import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class GlobalType extends BaseModel implements IGlobalType
{

	protected Long typeId = Long.valueOf(0L);
	protected String typeName;
	protected String nodePath;
	protected Integer depth;
	protected Long parentId;
	protected String nodeKey;
	protected String catKey;
	protected Long sn= Long.valueOf(0L);
	protected Long userId= Long.valueOf(0L);
	protected Long depId = Long.valueOf(0L);
	protected Integer type = Integer.valueOf(0);
	protected String open = "true";
	protected String isParent;
	protected Integer isLeaf;
	protected int childNodes = 0;
	/* protected Integer isLeaf;*/
	protected String nodeCode;
	protected Short nodeCodeType = Short.valueOf((short)0);
	protected String dataId;//数据记录Id
	protected String tableId;//业务表Id
	
	protected Short gltype_delFlag;// 是否删除
	
	protected Long gltype_creatorId;// 创建人ID
	
	protected Date gltype_createTime;// 创建时间
	
	protected Long gltype_updateId;// 更改人ID
	
	protected Date gltype_updateTime;// 更改时间
	
	public GlobalType(){}
	//SysFileType初始化成GlobalType
	public GlobalType(SysFileType fileType) {
			this.typeId = fileType.getTypeId();
			this.typeName = fileType.getTypeName();
			this.nodePath = fileType.getNodePath();
			this.depth = fileType.getDepth();
			this.parentId = fileType.getParentId();
			this.nodeKey = fileType.getNodeKey();
			this.catKey = fileType.getCatKey();
			this.sn = fileType.getSn();
			this.userId = fileType.getUserId();
			this.depId = fileType.getDepId();
			this.type = fileType.getType();
			this.open = fileType.getOpen();
			this.isParent = fileType.getIsParent();
			this.isLeaf = fileType.getIsLeaf();
			this.childNodes = fileType.getChildNodes();
			this.nodeCode = fileType.getNodeCode();
			this.nodeCodeType = fileType.getNodeCodeType();
			this.dataId = fileType.getDataId();
			this.tableId = fileType.getTableId();
	}

	public GlobalType(SysTypeKey sysTypeKey)
    {
        this.setTypeName(sysTypeKey.getTypeName());
        this.setCatKey(sysTypeKey.getTypeKey());
        this.setParentId(Long.valueOf(0L));
        this.setIsParent("true");
        this.setTypeId(sysTypeKey.getTypeKeyId());
        this.setType(sysTypeKey.getType());
        this.setNodePath(sysTypeKey.getTypeKeyId() + ".");
    }
    public GlobalType(SysTypeKey sysTypeKey, GlobalType globalType,IDefinition def, String isParent)
    {
        this.setTypeName(def.getSubject());
        this.setCatKey(sysTypeKey.getTypeKey());
        this.setParentId(globalType.getTypeId());
        this.setIsParent(isParent);
        this.setTypeId(def.getDefId());
        this.setIsLeaf(1);
        this.setType(sysTypeKey.getType());
        this.setNodePath(sysTypeKey.getTypeKeyId() + "."+globalType.getTypeId()+"."+def.getDefId()+".");
        
    }
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

	public Short getGltype_delFlag() {
		return gltype_delFlag;
	}
	public void setGltype_delFlag(Short gltype_delFlag) {
		this.gltype_delFlag = gltype_delFlag;
	}
	public Long getGltype_creatorId() {
		return gltype_creatorId;
	}
	public void setGltype_creatorId(Long gltype_creatorId) {
		this.gltype_creatorId = gltype_creatorId;
	}
	public Date getGltype_createTime() {
		return gltype_createTime;
	}
	public void setGltype_createTime(Date gltype_createTime) {
		this.gltype_createTime = gltype_createTime;
	}
	public Long getGltype_updateId() {
		return gltype_updateId;
	}
	public void setGltype_updateId(Long gltype_updateId) {
		this.gltype_updateId = gltype_updateId;
	}
	public Date getGltype_updateTime() {
		return gltype_updateTime;
	}
	public void setGltype_updateTime(Date gltype_updateTime) {
		this.gltype_updateTime = gltype_updateTime;
	}
	public boolean equals(Object object)
	{
		if (!(object instanceof GlobalType)) {
			return false;
		}
		GlobalType rhs = (GlobalType)object;
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
	public static GlobalType transDicGlobalType(IOutDicGlobalType globalType){
		GlobalType gl = new GlobalType();
		gl.setCatKey(IGlobalType.CAT_DIC);
		gl.setTypeName(globalType.getTypeName());
		gl.setNodeKey(globalType.getNodeKey());
		gl.setType(1);
		gl.setNodeCodeType(Short.valueOf((short)0));
		gl.setDepId(globalType.getDepId());
		return gl;
	}
}
 
