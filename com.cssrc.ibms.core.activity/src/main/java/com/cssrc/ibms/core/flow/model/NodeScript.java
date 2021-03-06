package com.cssrc.ibms.core.flow.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * 对象功能:节点运行脚本 Model对象  
 */
@XmlRootElement(name = "nodeScript")
@XmlAccessorType(XmlAccessType.NONE)
public class NodeScript extends BaseModel implements Cloneable {
	// 表名称
	public static final String TABLE_NAME = "BPM_NODE_SCRIPT";


	public static final Integer SCRIPT_TYPE_1 = 1;

	public static final Integer SCRIPT_TYPE_2 = 2;
	
	public static final Integer SCRIPT_TYPE_3 = 3;
	
	public static final Integer SCRIPT_TYPE_4 = 4;

	// 主键
	@XmlAttribute
	protected Long id;
	// memo
	@XmlAttribute
	protected String memo;
	// 流程节点ID
	@XmlAttribute
	protected String nodeId;
	// 流程定义ID
	@XmlAttribute
	protected String actDefId;
	// 脚本
	@XmlAttribute
	protected String script;
	// 脚本类型
	@XmlAttribute
	protected Integer scriptType;

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 返回 主键
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 返回 memo
	 * 
	 * @return
	 */
	public String getMemo() {
		return memo;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 返回 流程节点ID
	 * 
	 * @return
	 */
	public String getNodeId() {
		return nodeId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	/**
	 * 返回 流程定义ID
	 * 
	 * @return
	 */
	public String getActDefId() {
		return actDefId;
	}

	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * 返回 脚本
	 * 
	 * @return
	 */
	public String getScript() {
		return script;
	}

	public void setScriptType(Integer scriptType) {
		this.scriptType = scriptType;
	}

	/**
	 * 返回 脚本类型
	 * 
	 * @return
	 */
	public Integer getScriptType() {
		return scriptType;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof NodeScript)) {
			return false;
		}
		NodeScript rhs = (NodeScript) object;
		return new EqualsBuilder().append(this.id, rhs.id)
				.append(this.memo, rhs.memo).append(this.nodeId, rhs.nodeId)
				.append(this.actDefId, rhs.actDefId)
				.append(this.script, rhs.script)
				.append(this.scriptType, rhs.scriptType).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.memo).append(this.nodeId).append(this.actDefId)
				.append(this.script).append(this.scriptType).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("memo", this.memo).append("nodeId", this.nodeId)
				.append("actDefId", this.actDefId)
				.append("script", this.script)
				.append("scriptType", this.scriptType).toString();
	}

	public Object clone() {
		NodeScript obj = null;
		try {
			obj = (NodeScript) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}

}