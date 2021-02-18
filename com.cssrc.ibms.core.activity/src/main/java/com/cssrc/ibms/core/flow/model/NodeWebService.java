package com.cssrc.ibms.core.flow.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * <pre>
 * 对象功能:流程webservice节点 Model对象 
 * </pre>
 */
public class NodeWebService extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 主键
	protected Long id;
	// Act流程定义ID
	protected String actDefId;
	// 节点Id
	protected String nodeId;
	// 配置模板
	protected String document;

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 返回 主键
	 * 
	 * @return
	 */
	public Long getId() {
		return this.id;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	/**
	 * 返回 Act流程定义ID
	 * 
	 * @return
	 */
	public String getActDefId() {
		return this.actDefId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 返回 节点Id
	 * 
	 * @return
	 */
	public String getNodeId() {
		return this.nodeId;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof NodeWebService)) {
			return false;
		}
		NodeWebService rhs = (NodeWebService) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.actDefId, rhs.actDefId)
				.append(this.nodeId, rhs.nodeId).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id).append(this.actDefId).append(this.nodeId)
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("actDefId", this.actDefId)
				.append("nodeId", this.nodeId).toString();
	}

}