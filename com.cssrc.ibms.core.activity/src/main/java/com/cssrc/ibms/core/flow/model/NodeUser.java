package com.cssrc.ibms.core.flow.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

/**
 * 对象功能:流程定义节点用户 
 * 开发人员:zhulongchao  
 */
@XmlRootElement(name = "nodeUser")
@XmlAccessorType(XmlAccessType.NONE)
public class NodeUser extends BaseModel implements Cloneable {
	/**
	 * 发起人=0
	 */
	public static final short ASSIGN_TYPE_START_USER = 0;
	/**
	 * 用户=1
	 */
	public static final String ASSIGN_TYPE_USER = "users";
	/**
	 * 角色=2
	 */
	public static final String ASSIGN_TYPE_ROLE = "role";
	/**
	 * 组织=3
	 */
	public static final String ASSIGN_TYPE_ORG = "org";
	/**
	 * 组织负责人=4
	 */
	public static final String ASSIGN_TYPE_ORG_CHARGE = "orgCharge";
	/**
	 * 岗位=5
	 */
	public static final String ASSIGN_TYPE_POS = "pos";
	/**
	 * 上下级=6
	 */
	public static final String ASSIGN_TYPE_UP_LOW = "upLow";
	/**
	 * 用户属性=7
	 */
	public static final String ASSIGN_TYPE_USER_ATTR = "userAttr";
	/**
	 * 组织属性=8
	 */
	public static final String ASSIGN_TYPE_ORG_ATTR = "orgAttr";
	/**
	 * 本部门=9 即与发起人相同部门
	 */
	public static final short ASSIGN_TYPE_SAME_DEP = 9;
	/**
	 * 跟某个节相同执行人
	 */
	public static final String ASSIGN_TYPE_SAME_NODE = "sameNode";
	/**
	 * 发起人的直属领导
	 */
	public static final short ASSIGN_TYPE_DIRECT_LED = 11;
	/**
	 * 脚本
	 */
	public static final short ASSIGN_TYPE_SCRIPT = 12;
	/**
	 * 上个任务执行人的直属领导(组织)
	 */
	public static final short ASSIGN_TYPE_PREUSER_ORG_LEADER=13;
	/**
	 * 发起人的领导
	 */
	public static final short ASSIGN_TYPE_STARTUSER_LEADER=14;
	/**
	 * 上个任务执行人的领导
	 */
	public static final short ASSIGN_TYPE_PREVUSER_LEADER=15;
	
	/**
	 * 执行者部门的上级类型部门的负责人
	 */
	public static final short ASSIGN_TYPE_PREVTYPEUSER_LEADER=16;
	
	
	/**
	 * 职务
	 */
	public static final String ASSIGN_TYPE_JOB = "job";
	/**
	 * 18 即与发起人相同岗位
	 */
	public static final short ASSIGN_TYPE_SAME_POS = 18;
	
	/**
	 * 19 即与发起人相同职务
	 */
	public static final short ASSIGN_TYPE_SAME_JOB = 19;
	
	/**
	 * 运算类型为 or
	 */
	public static final short COMP_TYPE_OR = 0;
	/**
	 * 运算类型为 and
	 */
	public static final short COMP_TYPE_AND = 1;
	/**
	 * 运算类型为 exclude
	 */
	public static final short COMP_TYPE_EXCLUDE = 2;
	
	
	/**
	 * 通知任务执行人。
	 */
//	public static final String USER_TYPE_NOTIFY="NOTIFY";
	
	/**
	 * 归档触发的任务节点id
	 */
	public static final String NODE_ID_SYS_PROCESS_END_TASK="SYS_PROCESS_END_TASK";

	// nodeUserId
	@XmlAttribute
	@SysFieldDescription(detail="主键")
	protected Long nodeUserId;
	
	// 指派人员类型(技术类型)
	@XmlAttribute
	@SysFieldDescription(detail="指派人员类型",maps="{\"0\":\"发起人\",\"1\":\"用户\",\"2\":\"角色\",\"3\":\"组织\",\"4\":\"组织负责人\",\"5\":\"岗位\",\"6\":\"上下级\",\"7\":\"用户属性\",\"8\":\"组织属性\",\"9\":\"本部门\",\"10\":\"即与发起人相同部门跟某个节相同执行人 \",\"11\":\"发起人的直属领导\",\"12\":\"脚本\",\"13\":\"上个任务执行人的直属领导\",\"14\":\"发起人的领导\",\"15\":\"上个任务执行人的领导\",\"16\":\"执行者部门的上级类型部门的负责人 \",\"17\":\"职务\",\"18\":\"即与发起人相同岗位\",\"19\":\"即与发起人相同职务\"}")
	protected String assignType="";

	@XmlAttribute
	@SysFieldDescription(detail="比较对象的ID")
	protected String cmpIds;
	@XmlAttribute
	@SysFieldDescription(detail="比较对象显示名")
	protected String cmpNames;

	// 运算类型（与、或、非）
	@XmlAttribute
	@SysFieldDescription(detail="运算类型",maps="{\"0\":\"与\",\"1\":\"或\",\"2\":\"非\"}")
	protected Short compType;
	//
	@XmlAttribute
	@SysFieldDescription(detail="排序")
	protected Integer sn;
	//条件ID
	@XmlAttribute
	@SysFieldDescription(detail="条件ID")
	protected Long conditionId;
	//是否抽取用户 0:不抽取 1:抽取
	@XmlAttribute
	@SysFieldDescription(detail="是否抽取用户",maps="{\"1\":\"抽取\",\"0\":\"不抽取\"}")
	protected Short extractUser;

	public void setNodeUserId(Long nodeUserId) {
		this.nodeUserId = nodeUserId;
	}
	
	

	/**
	 * 返回 nodeUserId
	 * 
	 * @return
	 */
	public Long getNodeUserId() {
		return nodeUserId;
	}

	

	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	/**
	 * 返回 指派人员类型
	 * 
	 * @return
	 */
	public String getAssignType() {
		return assignType;
	}



	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof NodeUser)) {
			return false;
		}
		NodeUser rhs = (NodeUser) object;
		return new EqualsBuilder().append(this.nodeUserId, rhs.nodeUserId).
				append(this.cmpIds, rhs.cmpIds)
				.append(this.conditionId, rhs.conditionId).append(this.cmpNames, rhs.cmpNames).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.nodeUserId).append(this.assignType)
				.append(this.cmpIds)	.append(this.conditionId).append(this.cmpNames).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("nodeUserId", this.nodeUserId)
				.append("assignType", this.assignType).append("cmpIds", this.cmpIds).append("cmpNames", this.cmpNames)				
					.append("conditionId",this.conditionId).toString();
	}

	public String getCmpIds() {
		return cmpIds;
	}

	public void setCmpIds(String cmpIds) {
		this.cmpIds = cmpIds;
	}

	public String getCmpNames() {
		return cmpNames;
	}

	public void setCmpNames(String cmpNames) {
		this.cmpNames = cmpNames;
	}

	public Short getCompType() {
		return compType;
	}

	public void setCompType(Short compType) {
		this.compType = compType;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	
	
	public Long getConditionId() {
		return conditionId;
	}

	public void setConditionId(Long conditionId) {
		this.conditionId = conditionId;
	}
	

	
	

	public Short getExtractUser() {
		return extractUser;
	}

	public void setExtractUser(Short extractUser) {
		this.extractUser = extractUser;
	}

	public Object clone() {
		NodeUser obj = null;
		try {
			obj = (NodeUser) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
    
    public boolean hasPosition()
    {
        
        return this.assignType.equals(ASSIGN_TYPE_ROLE) || this.assignType.equals(ASSIGN_TYPE_ORG)
            || this.assignType.equals(ASSIGN_TYPE_POS)||this.assignType.equals(ASSIGN_TYPE_JOB);
        
    }



    public boolean hasPass()
    {
        return COMP_TYPE_EXCLUDE==this.compType;
    }
	
}