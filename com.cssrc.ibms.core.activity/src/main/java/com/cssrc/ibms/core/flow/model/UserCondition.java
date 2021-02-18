package com.cssrc.ibms.core.flow.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
import com.cssrc.ibms.core.util.string.StringUtil;


/**
 * 对象功能:USER_CONDITION  Model对象 
 * 开发人员:zhulongchao 
 */
@XmlRootElement(name = "bpmUserCondition")
@XmlAccessorType(XmlAccessType.NONE)
public class UserCondition extends BaseModel implements Cloneable,Comparable<UserCondition>
{
	//等于
	public static final  short OPERATE_TYPE_EQUAL= 0; 
	//不等于
	public static final  short OPERATE_TYPE_UNEQUAL= 1; 
	//大于
	public static final  short OPERATE_TYPE_MORE_THAN = 2; 
	//大于等于
	public static final  short OPERATE_TYPE_MORE_EQUAL_THAN = 3; 
	//小于
	public static final  short OPERATE_TYPE_LESS_THAN = 4; 
	//小于
	public static final  short OPERATE_TYPE_LESS_EQUAL_THAN = 5; 
	//字符串的等于
	public static final  short OPERATE_TYPE_LIKE = 6;
	//字符串的等于
	public static final  short OPERATE_TYPE_UNLIKE = 7;
	
	//变量为数字类型
	public static final  String VARIABLE_TYPE_NUMBER = "number";
	//变量为日期类型
	public static final  String VARIABLE_TYPE_DATE = "date";
	//变量为日期类型
	public static final  String VARIABLE_TYPE_STRING= "varchar";
	
	/**
	 * 执行人员规则 
	 **/
	public static final  int CONDITION_TYPE_EXEUSER= 0;
	/**
	 * 抄送人员规则
	 **/
	public static final  int CONDITION_TYPE_COPYUSER= 1; 
	/** 
	 * 消息任务接收人规则设置 正常邮件 
	 */
	public static final  int CONDITION_TYPE_MSG_MAIL_RECEIVER				= 3;
	/**
	 * 消息任务接收人规则设置 抄送
	 */
	public static final  int CONDITION_TYPE_MSG_MAIL_COPYTO				= 4;
	/**
	 * 消息任务接收人规则设置 密送
	 */
	public static final  int CONDITION_TYPE_MSG_MAIL_BCC			= 5;
	/**
	 * 消息任务接收人规则设置 内部消息
	 */
	public static final  int CONDITION_TYPE_MSG_INNER_RECEIVER						= 6;
	/**
	 * 消息任务接收人规则设置 手机短信
	 */
	public static final  int CONDITION_TYPE_MSG_MOBILE_RECEIVER						= 7;
	
	
	// ID
	@XmlAttribute
	@SysFieldDescription(detail="ID")
	protected Long  id=0L;
	// ACTDEFID
	@XmlAttribute
	@SysFieldDescription(detail="流程定义ID")
	protected String  actdefid="";
	// NODEID
	@XmlAttribute
	@SysFieldDescription(detail="节点ID")
	protected String  nodeid="";
	// 条件规则
	@XmlAttribute
	@SysFieldDescription(detail="条件规则")
	protected String  condition="";
	// SN
	@XmlAttribute
	@SysFieldDescription(detail="创建人名")
	protected Long  sn=0L;
	//表单变量(标识所挂表单是否发生变化)
	@XmlAttribute
	@SysFieldDescription(detail="表单变量")
	protected String formIdentity="";
	//规则名称
	@XmlAttribute
	@SysFieldDescription(detail="规则名称")
	protected String  conditionname="";
	//规则显示字段
	@XmlAttribute
	@SysFieldDescription(detail="规则显示字段")
	protected String  conditionShow="" ;
	//setId
	@XmlAttribute
	@SysFieldDescription(detail="setId")
	protected Long  setId=0L;
	/*
	 * 规则的类型 
	 * 0：执行人员规则 
	 * 1：抄送人员规则,
	 */
	@XmlAttribute
	@SysFieldDescription(detail="规则的类型 ",maps="{\"1\":\"抄送人员规则\",\"0\":\"执行人员规则\"}")
	protected Integer conditionType=0;
	
	//分组号
	@XmlAttribute
	@SysFieldDescription(detail="分组号")
	protected Integer groupNo=1;
	

    @XmlAttribute
    @SysFieldDescription(detail="父流程id")
	protected String parentActDefId = "";
	
	
	public String getConditionShow() {
		return conditionShow;
	}	
	public void setConditionShow(String conditionShow) {
		this.conditionShow = conditionShow;
	}
	public void setId(Long id) 
	{
		this.id = id;
	}
	/**
	 * 返回 ID
	 * @return
	 */
	public Long getId() 
	{
		return this.id;
	}
	public void setActdefid(String actdefid) 
	{
		this.actdefid = actdefid;
	}
	/**
	 * 返回 ACTDEFID
	 * @return
	 */
	public String getActdefid() 
	{
		return this.actdefid;
	}
	public void setNodeid(String nodeid) 
	{
		this.nodeid = nodeid;
	}
	/**
	 * 返回 NODEID
	 * @return
	 */
	public String getNodeid() 
	{
		return this.nodeid;
	}
	public void setCondition(String condition) 
	{
		this.condition = condition;
	}

   
   	public String getCondition() {
		return condition;
	}
	public Long getSn() {
		return sn;
	}
	public void setSn(Long sn) {
		this.sn = sn;
	}
	public String getFormIdentity() {
		return formIdentity;
	}
	public void setFormIdentity(String formIdentity) {
		this.formIdentity = formIdentity;
	}
	public String getConditionname() {
		return conditionname;
	}
	public void setConditionname(String conditionname) {
		this.conditionname = conditionname;
	}
	public Long getSetId() {
		return setId;
	}
	public void setSetId(Long setId) {
		this.setId = setId;
	}
	public Integer getConditionType() {
		return conditionType;
	}
	public void setConditionType(Integer conditionType) {
		this.conditionType = conditionType;
	}
	public Integer getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(Integer groupNo) {
		this.groupNo = groupNo;
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof UserCondition)) 
		{
			return false;
		}
		UserCondition rhs = (UserCondition) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id) 
		.append(this.actdefid) 
		.append(this.nodeid) 
		.append(this.condition) 
		.append(this.sn) 
		
		.append(this.conditionname)
			.append(this.conditionShow)
				.append(this.setId)
				.append(this.groupNo)
				.append(this.parentActDefId)
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("actdefid", this.actdefid) 
		.append("nodeid", this.nodeid) 
		.append("condition", this.condition) 
		.append("sn", this.sn) 
		
		.append("conditionname",this.conditionname)
		.append("conditionShow",this.conditionShow)
		.append("setId",this.setId)
		.append("groupNo",this.groupNo)
		.toString();
	}

	@Override
	public Object clone() {
		UserCondition obj = null;
		try {
			obj = (UserCondition) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
  
	public List<ConditionJsonStruct> getConditionJson(){
		if(StringUtil.isEmpty(this.condition)){
			return new  ArrayList<ConditionJsonStruct>();
		}
		List<ConditionJsonStruct> conditionJson = JSONArray.parseArray(this.condition, ConditionJsonStruct.class);
		return conditionJson;
	}
	
	public static void main(String[] args) {
		UserCondition cond = new UserCondition();
		Logger logger = LoggerFactory.getLogger(UserCondition.class);
		cond.getConditionJson();
		logger.info(cond.getConditionJson().toString());
	}
	
	@Override
	public int compareTo(UserCondition condition) {
		if(this.getGroupNo()==null){
			return -1;
		}else if(condition.getGroupNo()==null){
			return 1;
		}else{
			return this.getGroupNo().compareTo(condition.getGroupNo());
		}
	}
	public String getParentActDefId() {
		return parentActDefId;
	}
	public void setParentActDefId(String parentActDefId) {
		this.parentActDefId = parentActDefId;
	}
}