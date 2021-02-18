package com.cssrc.ibms.core.flow.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.XmlDateAdapter;

/**
 * 对象功能:流程定义扩展 Model对象 开发人员:zhulongchao
 */
@XmlRootElement(name = "definition")
@XmlAccessorType(XmlAccessType.NONE)
public class Definition extends BaseModel implements Cloneable, IDefinition {
	private static final long serialVersionUID = 5706682365392616269L;
	// 流程定义ID
	@XmlAttribute
	protected Long defId;
	// 分类ID
	@XmlAttribute
	protected Long typeId;
	// 流程标题
	@XmlAttribute
	protected String subject;
	// 流程定义Key
	@XmlAttribute
	protected String defKey;
	// 任务标题生成规则
	@XmlAttribute
	protected String taskNameRule;
	// 流程描述
	@XmlAttribute
	protected String descp;
	// 创建时间
	@XmlJavaTypeAdapter(XmlDateAdapter.class)
	@XmlAttribute
	protected java.util.Date createtime;
	/*
	 * 流程状态 0:未发布 1:发布 2.禁用 3.禁用,并禁用实例 4.测试状态
	 */
	@XmlAttribute
	protected Short status;
	// 流程定义XML(设计器)
	@XmlAttribute
	protected String defXml;
	// activiti流程发布ID
	@XmlAttribute
	protected Long actDeployId;
	// act流程定义Key
	@XmlAttribute
	protected String actDefKey;
	// actDefId流程定义ID
	@XmlAttribute
	protected String actDefId;
	// createBy
	@XmlAttribute
	protected Long createBy;
	// updateBy
	@XmlAttribute
	protected Long updateBy;
	// reason
	@XmlAttribute
	protected String reason;
	// versionNo
	@XmlAttribute
	protected Integer versionNo;
	// 隶属主定义ID
	@XmlAttribute
	protected Long parentDefId;
	// 是否为主版本
	@XmlAttribute
	protected Short isMain;
	// updatetime
	@XmlJavaTypeAdapter(XmlDateAdapter.class)
	@XmlAttribute
	protected java.util.Date updatetime;

	// 流程分类名称
	protected String typeName;

	// 跳过第一个任务节点。
	@XmlAttribute
	protected Short toFirstNode = 0;

	// 流程启动时 是否可以选择下一步的执行人
	@XmlAttribute
	protected Short showFirstAssignee = 0;

	// 可以选择条件同步路径的节点
	@XmlAttribute
	protected String canChoicePath;

	// 可选择路径的节点MAP
	private Map canChoicePathNodeMap;

	// 流程定义是否调用外部表单
	@XmlAttribute
	private Short isUseOutForm = 0;

	protected String formDetailUrl;

	// 是否开启打印模版功能
	@XmlAttribute
	protected Short isPrintForm = 0;

	// 是否允许流程结束时抄送
	@XmlAttribute
	protected Integer allowFinishedCc = 0;

	// 是否允许我的办结转发
	@XmlAttribute
	protected Integer allowFinishedDivert = 1;

	// 是否允许转办
	@XmlAttribute
	protected Integer allowDivert = 1;

	@XmlAttribute
	protected Integer allowMobile = 1;
	
	
	@XmlAttribute
	protected String keyPath;

	// 帮助附件
	@XmlAttribute
	protected Long attachment;
	/**
	 * 通知类型
	 */
	@XmlAttribute
	protected String informType;
	/**
	 * 归档时发送消息给发起人 类型
	 */
	@XmlAttribute
	protected String informStart;

	// 允许参考数
	@XmlAttribute
	protected Integer allowRefer = 0;
	// 允许实例数量
	@XmlAttribute
	protected Integer instanceAmount = 5;

	/**
	 * 相同任务节点是否允许跳转。 0：不允许 1：允许
	 */
	@XmlAttribute
	protected Short sameExecutorJump = 0;

	// 提交是否需要确认
	@XmlAttribute
	protected Integer submitConfirm = 0;
	// 直接启动不需要使用表单
	@XmlAttribute
	private Integer directstart = 0;
	// 抄送消息类型
	@XmlAttribute
	protected String ccMessageType;
	// 测试状态标签
	@XmlAttribute
	protected String testStatusTag;

	protected AuthorizeRight authorizeRight;
	// 节点跳过设定
	@XmlAttribute
	protected String skipSetting = "";
    // 代办已办模板设置
    @XmlAttribute
    protected String pendingSetting = "";
    
    
	public Map getCanChoicePathNodeMap() {
		if (canChoicePathNodeMap == null) {
			Map map = new HashMap();
			if (StringUtil.isEmpty(this.canChoicePath)) {
				canChoicePathNodeMap = map;
				return canChoicePathNodeMap;
			}
			Pattern regex = Pattern.compile("(\\w+):(\\w+)");
			Matcher regexMatcher = regex.matcher(this.canChoicePath);
			while (regexMatcher.find()) {
				map.put(regexMatcher.group(1), regexMatcher.group(2));
			}
			canChoicePathNodeMap = map;
		}
		return canChoicePathNodeMap;
	}

	public void setDefId(Long defId) {
		this.defId = defId;
	}

	public Short getIsUseOutForm() {
		return isUseOutForm;
	}

	public void setIsUseOutForm(Short isUseOutForm) {
		this.isUseOutForm = isUseOutForm;
	}

	/**
	 * 返回 流程定义ID
	 * 
	 * @return
	 */
	public Long getDefId() {
		return defId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getFormDetailUrl() {
		return formDetailUrl;
	}

	public void setFormDetailUrl(String formDetailUrl) {
		this.formDetailUrl = formDetailUrl;
	}

	/**
	 * 返回 分类ID
	 * 
	 * @return
	 */
	public Long getTypeId() {
		return typeId;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 返回 流程标题
	 * 
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	public void setDefKey(String defKey) {
		this.defKey = defKey;
	}

	/**
	 * 返回 流程定义Key
	 * 
	 * @return
	 */
	public String getDefKey() {
		return defKey;
	}

	public void setTaskNameRule(String taskNameRule) {
		this.taskNameRule = taskNameRule;
	}

	/**
	 * 返回 任务标题生成规则
	 * 
	 * @return
	 */
	public String getTaskNameRule() {
		return taskNameRule;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	/**
	 * 返回 流程描述
	 * 
	 * @return
	 */
	public String getDescp() {
		return descp;
	}

	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}

	/**
	 * 返回 创建时间
	 * 
	 * @return
	 */
	public java.util.Date getCreatetime() {
		return createtime;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getTestStatusTag() {
		return testStatusTag;
	}

	public void setTestStatusTag(String testStatusTag) {
		this.testStatusTag = testStatusTag;
	}

	/**
	 * 返回 流程状态
	 * 
	 * @return
	 */
	public Short getStatus() {
		return status;
	}

	public void setDefXml(String defXml) {
		this.defXml = defXml;
	}

	/**
	 * 返回 流程定义XML(设计器)
	 * 
	 * @return
	 */
	public String getDefXml() {
		return defXml;
	}

	public void setActDeployId(Long actDeployId) {
		this.actDeployId = actDeployId;
	}

	/**
	 * 返回 activiti流程定义ID
	 * 
	 * @return
	 */
	public Long getActDeployId() {
		return actDeployId;
	}

	public void setActDefKey(String actDefKey) {
		this.actDefKey = actDefKey;
	}

	/**
	 * 返回 act流程定义Key
	 * 
	 * @return
	 */
	public String getActDefKey() {
		return actDefKey;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	/**
	 * 返回 actDefId
	 * 
	 * @return
	 */
	public String getActDefId() {
		return actDefId;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	/**
	 * 返回 createBy
	 * 
	 * @return
	 */
	public Long getCreateBy() {
		return createBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 返回 updateBy
	 * 
	 * @return
	 */
	public Long getUpdateBy() {
		return updateBy;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * 返回 reason
	 * 
	 * @return
	 */
	public String getReason() {
		return reason;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	/**
	 * 返回 versionNo
	 * 
	 * @return
	 */
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setParentDefId(Long parentDefId) {
		this.parentDefId = parentDefId;
	}

	/**
	 * 返回 隶属主定义ID
	 * 
	 * @return
	 */
	public Long getParentDefId() {
		return parentDefId;
	}

	public void setIsMain(Short isMain) {
		this.isMain = isMain;
	}

	/**
	 * 返回 是否为主版本
	 * 
	 * @return
	 */
	public Short getIsMain() {
		return isMain;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	/**
	 * 返回 updatetime
	 * 
	 * @return
	 */
	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Short getShowFirstAssignee() {
		return showFirstAssignee;
	}

	public void setShowFirstAssignee(Short showFirstAssignee) {
		this.showFirstAssignee = showFirstAssignee;
	}

	public Short getToFirstNode() {
		// 为空默认跳过第一个节点
		if (toFirstNode == null)
			return 1;
		return toFirstNode;
	}

	public void setToFirstNode(Short toFirstNode) {
		this.toFirstNode = toFirstNode;
	}

	public String getCanChoicePath() {
		return canChoicePath;
	}

	public void setCanChoicePath(String canChoicePath) {
		this.canChoicePath = canChoicePath;
	}

	/**
	 * 更新可以选择的路径。 ORGateway1:UserTask1
	 */
	public void updateCanChoicePath() {
		if (this.canChoicePathNodeMap != null) {
			StringBuffer sb = new StringBuffer();
			Iterator iter = this.canChoicePathNodeMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				sb.append(",");
				sb.append(key);
				sb.append(":");
				sb.append(val);
			}
			this.canChoicePath = sb.toString().replaceFirst(",", "");
		}
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Definition)) {
			return false;
		}
		Definition rhs = (Definition) object;
		return new EqualsBuilder().append(this.defId, rhs.defId).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.defId)

		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("defId", this.defId)
				.append("typeId", this.typeId).append("subject", this.subject)
				.append("defKey", this.defKey)
				.append("taskNameRule", this.taskNameRule)
				.append("descp", this.descp)
				.append("createtime", this.createtime)
				.append("status", this.status).append("defXml", this.defXml)
				.append("actDeployId", this.actDeployId)
				.append("actDefKey", this.actDefKey)
				.append("actDefId", this.actDefId)
				.append("createBy", this.createBy)
				.append("updateBy", this.updateBy)
				.append("reason", this.reason)
				.append("versionNo", this.versionNo)
				.append("parentDefId", this.parentDefId)
				.append("isMain", this.isMain)
				.append("updatetime", this.updatetime)
				.append("ccMessageType", this.ccMessageType).toString();
	}

	public Object clone() {
		Definition obj = null;
		try {
			obj = (Definition) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public Short getIsPrintForm() {
		return isPrintForm;
	}

	public void setIsPrintForm(Short isPrintForm) {
		this.isPrintForm = isPrintForm;
	}

	public Integer getAllowFinishedDivert() {
		return allowFinishedDivert;
	}

	public void setAllowFinishedDivert(Integer allowFinishedDivert) {
		this.allowFinishedDivert = allowFinishedDivert;
	}

	public Integer getAllowFinishedCc() {
		return allowFinishedCc;
	}

	public void setAllowFinishedCc(Integer allowFinishedCc) {
		this.allowFinishedCc = allowFinishedCc;
	}

	public Integer getAllowDivert() {
		return allowDivert;
	}

	public void setAllowDivert(Integer allowDivert) {
		this.allowDivert = allowDivert;
	}

	public Long getAttachment() {
		return attachment;
	}

	public void setAttachment(Long attachment) {
		this.attachment = attachment;
	}

	public String getInformType() {
		return informType;
	}

	public void setInformType(String informType) {
		this.informType = informType;
	}

	public Short getSameExecutorJump() {
		return sameExecutorJump;
	}

	public void setSameExecutorJump(Short sameExecutorJump) {
		this.sameExecutorJump = sameExecutorJump;
	}

	public void setCanChoicePathNodeMap(Map canChoicePathNodeMap) {
		this.canChoicePathNodeMap = canChoicePathNodeMap;
	}

	public Integer getSubmitConfirm() {
		return submitConfirm;
	}

	public void setSubmitConfirm(Integer submitConfirm) {
		this.submitConfirm = submitConfirm;
	}

	public String getInformStart() {
		return informStart;
	}

	public void setInformStart(String informStart) {
		this.informStart = informStart;
	}

	public Integer getAllowRefer() {
		return allowRefer;
	}

	public void setAllowRefer(Integer allowRefer) {
		this.allowRefer = allowRefer;
	}

	public Integer getInstanceAmount() {
		return instanceAmount;
	}

	public void setInstanceAmount(Integer instanceAmount) {
		this.instanceAmount = instanceAmount;
	}

	public Integer getDirectstart() {
		return directstart;
	}

	public void setDirectstart(Integer directstart) {
		this.directstart = directstart;
	}

	public String getCcMessageType() {
		return ccMessageType;
	}

	public void setCcMessageType(String ccMessageType) {
		this.ccMessageType = ccMessageType;
	}

	public Integer getAllowMobile() {
		return this.allowMobile;
	}

	public void setAllowMobile(Integer allowMobile) {
		this.allowMobile = allowMobile;
	}

	public AuthorizeRight getAuthorizeRight() {
		return this.authorizeRight;
	}

	public void setAuthorizeRight(AuthorizeRight authorizeRight) {
		this.authorizeRight = authorizeRight;
	}

	public String getSkipSetting() {
		return this.skipSetting;
	}

	public void setSkipSetting(String skipSetting) {
		this.skipSetting = skipSetting;
	}

    public String getPendingSetting()
    {
        return pendingSetting;
    }

    public void setPendingSetting(String pendingSetting)
    {
        this.pendingSetting = pendingSetting;
    }

    public String getKeyPath()
    {
        return keyPath;
    }

    public void setKeyPath(String keyPath)
    {
        this.keyPath = keyPath;
    }
    

}