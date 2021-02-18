package com.cssrc.ibms.core.flow.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.activity.model.INodeSql;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
 
	/**
	 * NodeSql 节点sql
	 * @author liubo
	 * @date 2017年2月16日
	 */
	@XmlRootElement(name="bpmNodeSql")
	@XmlAccessorType(XmlAccessType.NONE)
	public class NodeSql extends BaseModel implements INodeSql{
		public static String ACTION_SAVE = "save";
		
		public static String ACTION_SUBMIT = "submit";

		public static String ACTION_AGREE = "agree";

		public static String ACTION_OPPOSITE = "opposite";
 
		public static String ACTION_REJECT = "reject";
 
		public static String ACTION_DELETE = "delete";
 
		public static String ACTION_END = "end";
 
		
		/**
		 * id
		 */
		@XmlAttribute
		protected Long id;
 
		/**
		 * name
		 */
		@XmlAttribute
		protected String name;
 
		/**
		 * 数据源别名
		 */
		@XmlAttribute
		protected String dsAlias;
 
		/**
		 * 流程id
		 */
		@XmlAttribute
		protected String actdefId;
 
		/**
		 * 节点ID
		 */
		@XmlAttribute
		protected String nodeId;
 
		/**
		 * 触发时机
		 */
		@XmlAttribute
		protected String action;
 
		/**
		 * SQL语句
		 */
		@XmlElement
		protected String sql;
 
		/**
		 * 描述
		 */
		@XmlAttribute
		protected String desc;
 
		public void setId(Long id) {
			this.id = id;
		}
 
		public Long getId(){
			return this.id;
		}
 
		public void setName(String name) {
			this.name = name;
		}
 
		public String getName(){
			return this.name;
		}
  
		public void setAction(String action) {
			this.action = action;
		}
 
		public String getAction(){
			return this.action;
		}
 
		public void setSql(String sql) {
			this.sql = sql;
		}
 
		public String getSql(){
			return this.sql;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
 
		public String getDesc(){
			return this.desc;
		}
 
		public String getDsAlias(){
			return this.dsAlias;
		}
 
		public void setDsAlias(String dsAlias){
			this.dsAlias = dsAlias;
		}
 
		public String getActdefId(){
			return this.actdefId;
		}
 
		public void setActdefId(String actdefId){
			this.actdefId = actdefId;
		}
 
		public String getNodeId(){
			return this.nodeId;
		}
 
		public void setNodeId(String nodeId){
			this.nodeId = nodeId;
		}
 
		public boolean equals(Object object){
			if (!(object instanceof NodeSql)) {
				return false;
			}
			NodeSql rhs = (NodeSql)object;
			return new EqualsBuilder().append(this.id, rhs.id).append(this.name, rhs.name).append(this.dsAlias, rhs.dsAlias).append(this.actdefId, rhs.actdefId).append(this.nodeId, rhs.nodeId).append(this.action, rhs.action).append(this.sql, rhs.sql).append(this.desc, rhs.desc).isEquals();
		}
 
		public int hashCode(){
			return new HashCodeBuilder(-82280557, -700257973).append(this.id).append(this.name).append(this.dsAlias).append(this.actdefId).append(this.nodeId).append(this.action).append(this.sql).append(this.desc).toHashCode();
		}
 
		public String toString(){
			return new ToStringBuilder(this).append("id", this.id).append("name", this.name).append("dsAlias", this.dsAlias).append("actdefId", this.actdefId).append("nodeId", this.nodeId).append("action", this.action).append("sql", this.sql).append("desc", this.desc).toString();
		}
	}
