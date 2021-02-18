package com.cssrc.ibms.core.user.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
import com.cssrc.ibms.core.util.dbom.ITreeNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 组织架构，替换organization
 * <p>Title:SysOrg</p>
 * @author Yangbo 
 * @date 2016-8-1上午10:43:34
 */
@XmlRootElement(name = "sysOrg")
@XmlAccessorType(XmlAccessType.FIELD)
public class SysOrg extends BaseModel implements ISysOrg ,ITreeNode{
	private static final long serialVersionUID = 1L;
	public static final Long BEGIN_DEMID = Long.valueOf(1L);
	public static final Long BEGIN_ORGID = Long.valueOf(1L);
	public static final Integer BEGIN_DEPTH = Integer.valueOf(0);
	public static final String BEGIN_PATH = "1";
	public static final Short BEGIN_TYPE = Short.valueOf((short) 1);
	public static final Long BEGIN_ORGSUPID = Long.valueOf(-1L);
	public static final Short BEGIN_SN = Short.valueOf((short) 1);
	public static final Short BEGIN_FROMTYPE = Short.valueOf((short) 0);

	public static final Short FROMTYPE_AD = Short.valueOf((short) 1);
	public static final Short FROMTYPE_DB = Short.valueOf((short) 0);

	public static final Integer IS_LEAF_N = Integer.valueOf(1);
	public static final Integer IS_LEAF_Y = Integer.valueOf(0);
	public static final String IS_PARENT_N = "false";
	public static final String IS_PARENT_Y = "true";
	@SysFieldDescription(detail="组织ID")
	private Long orgId;
	@SysFieldDescription(detail="维度编号")
	private Long demId;
	@SysFieldDescription(detail="维度名称")
	private String demName;
	@SysFieldDescription(detail="组织名")
	private String orgName;
	@SysFieldDescription(detail="组织简称")
	private String orgShortName;
	@SysFieldDescription(detail="组织描述")
	private String orgDesc;
	@SysFieldDescription(detail="组织名称路径")
	private String orgPathname;
	@SysFieldDescription(detail="上级组织id")
	private Long orgSupId;
	@SysFieldDescription(detail="分管领导")
	private String leader;//分管领导
	@SysFieldDescription(detail="分管副领导")
	private String viceLeader;//分管副领导
	@SysFieldDescription(detail="上级组织编码")
	private String supCode;
	@SysFieldDescription(detail="上级组织名")
	private String orgSupName;
	@SysFieldDescription(detail="路径")
	private String path;
	@SysFieldDescription(detail="depth")
	private Integer depth;
	@SysFieldDescription(detail="类别",maps="{\"1\":\"部\",\"2\":\"所队\",\"3\":\"室处\",\"4\":\"小组\",\"5\":\"其他组织\"}")
	private Long orgType;
	@SysFieldDescription(detail="创建人ID")
	private Long creatorId;
	@SysFieldDescription(detail="创建时间")
	private Date createtime;
	@SysFieldDescription(detail="更改人ID")
	private Long updateId;
	@SysFieldDescription(detail="更新时间")
	private Date updatetime;
	@SysFieldDescription(detail="拥有者")
	private String ownUser;
	@SysFieldDescription(detail="拥有者名")
	private String ownUserName;
	@SysFieldDescription(detail="创建人名")
	private String createName;
	@SysFieldDescription(detail="更新人名")
	private String updateName;
	@SysFieldDescription(detail="sn")
	private Long sn = Long.valueOf(0L);
	@SysFieldDescription(detail="onlineNum")
	private Integer onlineNum = Integer.valueOf(0);
	@SysFieldDescription(detail="是否主",maps="{\"1\":\"是\",\"0\":\"否\"}")
	private Short isPrimary;
	@SysFieldDescription(detail="是否为根节点",maps="{\"1\":\"是\",\"0\":\"否\"}")
	private Short isRoot = Short.valueOf((short) 0);
	@SysFieldDescription(detail="表单类型")
	private Short fromType = Short.valueOf((short) 0);
	@SysFieldDescription(detail="图标")
	private String iconPath = "";
	@SysFieldDescription(detail="是否打开",maps="{\"true\":\"是\",\"false\":\"否\"}")
	private String open = "true";
	@SysFieldDescription(detail="是否叶子",maps="{\"1\":\"是\",\"0\":\"否\"}")
	private Integer isLeaf;
	@SysFieldDescription(detail="是否父节点",maps="{\"1\":\"是\",\"0\":\"否\"}")
	private String isParent;
	@SysFieldDescription(detail="是否删除",maps="{\"1\":\"是\",\"0\":\"否\"}")
	private Short isDelete = Short.valueOf((short) 0);
	@SysFieldDescription(detail="公司id")
	private Long companyId = Long.valueOf(0L);
	@SysFieldDescription(detail="公司")
	private String company = "";
	@SysFieldDescription(detail="组织代码")
	private String code;
	@SysFieldDescription(detail="上级组织id")
	private Long topOrgId = Long.valueOf(0L);
	@SysFieldDescription(detail="编制人数")
	private Integer orgStaff;

	public Short getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(Short isDelete) {
		this.isDelete = isDelete;
	}

	public String getOpen() {
		return this.open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public Integer getIsLeaf() {
		return this.isLeaf;
	}

	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
		if ((isLeaf != null) && (isLeaf.intValue() == 0))
			this.isParent = "false";
		else if ((isLeaf != null) && (isLeaf.intValue() > 0))
			this.isParent = "true";
		else
			this.isParent = null;
	}

	public String getIsParent() {
		if (this.isLeaf == null)
			return "true";

		return this.isLeaf.intValue() > 0 ? "true" : "false";
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
		if ((isParent != null) && (isParent.equals("false")))
			this.isLeaf = IS_LEAF_Y;
		else if ((isParent != null) && (isParent.equals("true")))
			this.isLeaf = IS_LEAF_N;
		else
			this.isLeaf = null;
	}

	public String getIconPath() {
		return this.iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public Short getIsPrimary() {
		return this.isPrimary;
	}

	public void setIsPrimary(Short isPrimary) {
		this.isPrimary = isPrimary;
	}

	public Long getSn() {
		return this.sn;
	}

	public void setSn(Long sn) {
		this.sn = sn;
	}

	public String getOwnUserName() {
		return this.ownUserName;
	}

	public void setOwnUserName(String ownUserName) {
		this.ownUserName = ownUserName;
	}

	public String getCreateName() {
		return this.createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getUpdateName() {
		return this.updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public void setDemId(Long demId) {
		this.demId = demId;
	}

	public Long getDemId() {
		return this.demId;
	}

	public String getDemName() {
		return this.demName;
	}

	public void setDemName(String demName) {
		this.demName = demName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public String getOrgShortName() {
		return orgShortName;
	}

	public void setOrgShortName(String orgShortName) {
		this.orgShortName = orgShortName;
	}

	public void setOrgDesc(String orgDesc) {
		this.orgDesc = orgDesc;
	}

	public String getOrgDesc() {
		return this.orgDesc;
	}

	public void setOrgPathname(String orgPathname) {
		this.orgPathname = orgPathname;
	}

	public String getOrgPathname() {
		return this.orgPathname;
	}

	public void setOrgSupId(Long orgSupId) {
		this.orgSupId = orgSupId;
	}

	public Long getOrgSupId() {
		return this.orgSupId;
	}

	public String getOrgSupName() {
		return this.orgSupName;
	}

	public void setOrgSupName(String orgSupName) {
		this.orgSupName = orgSupName;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Integer getDepth() {
		return this.depth;
	}

	public void setOrgType(Long orgType) {
		this.orgType = orgType;
	}

	public Long getOrgType() {
		return this.orgType;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getCreatorId() {
		return this.creatorId;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Long getUpdateId() {
		return this.updateId;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setOwnUser(String ownUser) {
		this.ownUser = ownUser;
	}

	public String getOwnUser() {
		return this.ownUser;
	}

	public Integer getOnlineNum() {
		return this.onlineNum;
	}

	public void setOnlineNum(Integer onlineNum) {
		this.onlineNum = onlineNum;
	}

	public Short getIsRoot() {
		return this.isRoot;
	}

	public void setIsRoot(Short isRoot) {
		this.isRoot = isRoot;
	}

	public Short getFromType() {
		return this.fromType;
	}

	public void setFromType(Short fromType) {
		this.fromType = fromType;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSupCode() {
		return this.supCode;
	}

	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysOrg)) {
			return false;
		}
		SysOrg rhs = (SysOrg) object;
		return new EqualsBuilder().append(this.orgId, rhs.orgId).append(
				this.demId, rhs.demId).append(this.orgName, rhs.orgName)
				.append(this.orgDesc, rhs.orgDesc).append(this.orgPathname,
						rhs.orgPathname).append(this.orgSupId, rhs.orgSupId)
				.append(this.path, rhs.path).append(this.depth, rhs.depth)
				.append(this.orgType, rhs.orgType).append(this.creatorId,
						rhs.creatorId).append(this.createtime, rhs.createtime)
				.append(this.updateId, rhs.updateId).append(this.updatetime,
						rhs.updatetime).append(this.ownUser, rhs.ownUser)
				.append(this.sn, rhs.sn).append(this.supCode, rhs.supCode)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.orgId)
				.toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("orgId", this.orgId).append(
				"demId", this.demId).append("orgName", this.orgName).append(
				"orgSupName", this.orgSupName).toString();
	}

	public void setOrgLevel(Integer orgLevel) {
	}

	public Integer getOrgLevel() {
		return null;
	}

	public Long getTopOrgId() {
		return this.topOrgId;
	}

	public void setTopOrgId(Long topOrgId) {
		this.topOrgId = topOrgId;
	}

	public Long getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getOrgStaff() {
		return this.orgStaff;
	}

	public void setOrgStaff(Integer orgStaff) {
		this.orgStaff = orgStaff;
	}
	public SysOrg() {
	}

	public SysOrg(Long orgId) {
		setOrgId(orgId);
	}
	
	public String getLeader()
    {
        return leader;
    }

    public void setLeader(String leader)
    {
        this.leader = leader;
    }

    public String getViceLeader()
    {
        return viceLeader;
    }

    public void setViceLeader(String viceLeader)
    {
        this.viceLeader = viceLeader;
    }

    @Override
	public Map<String, String> getRelation() {
		Map<String, String> propMap = new HashMap<String, String>();
		propMap.put("id", "orgId");
		propMap.put("text", "orgName");	 
		propMap.put("parentId", "orgSupId");
		return propMap;
	}
}
