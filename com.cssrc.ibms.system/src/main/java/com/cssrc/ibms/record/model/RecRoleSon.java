package com.cssrc.ibms.record.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

import com.cssrc.ibms.api.rec.model.IRecRoleSon;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
public class RecRoleSon  extends BaseModel implements GrantedAuthority, Cloneable ,IRecRoleSon{	
	
	/** 主键 * */
	@SysFieldDescription(detail="主键")
	protected Long roleSonId;
	/**外键（项目默认角色的ID） */
	@SysFieldDescription(detail="角色外键")
	protected Long roleId;
	/** 外键（ibms_rec_type） * */
	@SysFieldDescription(detail="类型外键")
	protected Long typeId;
	/** 角色类型  * */
	@SysFieldDescription(detail="角色类型")
	protected String typeName;
	/** 外键（ibms_data_template） * */
	@SysFieldDescription(detail="外键")
	protected Long dataTemplateId;
	/** 记录ID * */
	@SysFieldDescription(detail="记录ID")
	protected Long dataId;	
	/** 记录角色名称 * */
	@SysFieldDescription(detail="色名称")
	protected String roleSonName;
	/** 记录角色别名 * */
	@SysFieldDescription(detail="角色别名")
	protected String alias;
	/** 记录角色描述 * */
	@SysFieldDescription(detail="角色描述")
	protected String roleSonDesc;
	/*继承自基本项目角色的过滤条件*/
	@SysFieldDescription(detail="项目角色的过滤条件")
	protected String filter;
	/*客户添加的人员ID*/
	@SysFieldDescription(detail="添加的人员ID")
	protected String userAdd;
	/*客户删除的人员ID*/
	@SysFieldDescription(detail="删除的人员ID")
	protected String userDel;
	/*角色下对应的用户*/
	@SysFieldDescription(detail="用户列表")
	protected List<RecRoleSonUser> userList = new ArrayList();
	/*add by dwj 2017年5月31日15:34:55*/
	@SysFieldDescription(detail="允许删除",maps="{\"1\":\"允许\",\"0\":\"不允许\"}")
	protected Short allowDel;// 允许删除(0,不允许,1,允许)
	@SysFieldDescription(detail="是否隐藏",maps="{\"1\":\"隐藏\",\"0\":\"不隐藏\"}")
	protected Short isHide;  // 是否隐藏(0,不隐藏,1,隐藏)
	@SysFieldDescription(detail="过滤条件")
	protected String def_filter;  // (用于开发人员添加过滤条件，格式与filter相同)
	
	public RecRoleSon(){
		
	}	
	
	public Long getRoleSonId() {
		return roleSonId;
	}

	public void setRoleSonId(Long roleSonId) {
		this.roleSonId = roleSonId;
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

	public Long getDataTemplateId() {
		return dataTemplateId;
	}

	public void setDataTemplateId(Long dataTemplateId) {
		this.dataTemplateId = dataTemplateId;
	}

	public Long getDataId() {
		return dataId;
	}

	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}

	public String getRoleSonName() {
		return roleSonName;
	}

	public void setRoleSonName(String roleSonName) {
		this.roleSonName = roleSonName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getRoleSonDesc() {
		return roleSonDesc;
	}

	public void setRoleSonDesc(String roleSonDesc) {
		this.roleSonDesc = roleSonDesc;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getUserAdd() {
		if(userAdd==null||userAdd.equals("")){
			userAdd = ",0";
		}
		return userAdd;
	}

	public void setUserAdd(String userAdd) {
		this.userAdd = userAdd;
	}

	public String getUserDel() {
		if(userDel==null||userDel.equals("")){
			userDel = ",0";
		}
		return userDel;
	}

	public void setUserDel(String userDel) {
		this.userDel = userDel;
	}

	public List<RecRoleSonUser> getUserList() {
		return userList;
	}

	public void setUserList(List<RecRoleSonUser> userList) {
		this.userList = userList;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Short getAllowDel() {
		return allowDel;
	}

	public void setAllowDel(Short allowDel) {
		this.allowDel = allowDel;
	}

	public Short getIsHide() {
		return isHide;
	}

	public void setIsHide(Short isHide) {
		this.isHide = isHide;
	}

	public String getDef_filter() {
		return def_filter;
	}

	public void setDef_filter(String def_filter) {
		this.def_filter = def_filter;
	}

	public boolean equals(Object object) {
		if (!(object instanceof RecRoleSon)) {
			return false;
		}
		RecRoleSon rrs = (RecRoleSon) object;
		return new EqualsBuilder().append(this.alias, rrs.alias).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.roleSonId)
				.append(this.typeId).append(this.dataTemplateId)
				.append(this.dataId).append(this.roleSonName).append(
						this.alias).append(this.roleSonDesc).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("roleSonId", this.roleSonId).append("typeId", this.typeId)
				.append("dataTemplateId", this.dataTemplateId).append("dataId", this.dataId)
				.append("roleSonName", this.roleSonName).append("alias",this.alias)
				.append("roleSonDesc", this.roleSonDesc).append("filter", this.filter)
				.append("userAdd", this.userAdd).append("userDel", this.userDel)
				.append("userList", this.userList).append("allowDel", this.allowDel)
				.append("isHide", this.isHide).append("def_filter", this.def_filter).toString();
	}
	public Object clone() {
		RecRoleSon obj = null;
		try {
			obj = (RecRoleSon) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
	public String getAuthority() {
		return this.alias;
	}
}
