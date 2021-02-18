package com.cssrc.ibms.core.user.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient; 

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.dbom.ITreeNode;
import com.cssrc.ibms.core.util.json.JacksonDateSerializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.Expose;

@JsonIgnoreProperties( { "password" })
public class SysUserH extends BaseModel implements UserDetails, ITreeNode{
	/**
	 * 
	 */
	public static final String SEARCH_BY_ROL = "1";
	public static final String SEARCH_BY_ORG = "2";
	public static final String SEARCH_BY_POS = "3";
	public static final String SEARCH_BY_ONL = "4";

	private static final long serialVersionUID = 1L;

	public static final Long SYSTEM_USER = new Long(-1L);

	public static final Long SUPER_USER = new Long(1L);

	public static final Short DYNPWD_STATUS_BIND = Short.valueOf((short) 1);
	public static final Short DYNPWD_STATUS_UNBIND = Short.valueOf((short) 0);

	public static final Short UNDEL = Short.valueOf((short) 0);
	public static final Short DELED = Short.valueOf((short) 1);
	
	public static final Short NOTNORMAL = Short.valueOf((short) 0);
	public static final Short NORMAL = Short.valueOf((short) 1);
	
	
	
	@Expose
	protected Long userId;

	@Expose
	protected String username;

	protected String password;
	
    //性别
	@Expose
	protected Short sex;
	
	@Expose
	@JsonManagedReference
	protected SysOrg sysOrg;

	public SysOrg getSysOrg() {
		return sysOrg;
	}

	public void setSysOrg(SysOrg sysOrg) {
		this.sysOrg = sysOrg;
	}

	@Expose
	protected String email;

	@Expose
	protected String job;//职位

	@Expose
	protected String phone;

	@Expose
	protected String mobile;

	@Expose
	protected String fax;

	@Expose
	protected String address;

	@Expose
	protected String zip;

	@Expose
	protected String photo;//个人头像
	

	@Expose
	protected String sign_pic;//电子签名图片
	@Expose
	protected String originalsign_pic;//电子签名图片
	
	@Expose
	protected String originalphoto;//个人头像

	@Expose
	@JsonSerialize(using = JacksonDateSerializer.class)
	protected Date accessionTime;//个人入职时间
	
	@Expose
	@JsonSerialize(using = JacksonDateSerializer.class)
	protected Date birthDay;//出生日期

	@Expose
	protected Short status;//状态

	@Expose
	protected String education; //学历

	@Expose
	protected String fullname;//中文名

	@Expose     
	protected Short delFlag;//是否删除

	@Expose
	protected String dynamicPwd;

	@Expose
	protected Short dyPwdStatus;

	@Expose
	protected String depNames;

	@Expose
	protected String posNames;

	@Expose
	protected String roleNames;

	@Expose
	protected String primaryDep;

	@XmlTransient
	@JsonBackReference
	protected Set<SysRole> roles = new HashSet();



	@JsonBackReference
	protected Set<String> rights = new HashSet();

	@JsonBackReference
	protected Set orgs = new HashSet();

	@JsonBackReference
	protected Set positions = new HashSet();

   //非数据库字段
   protected Long orgId ;
   
   private String newUserId;

   //用户密级
   private String security;
   
	public SysUserH() {
	}

	public SysUserH(Long in_userId) {
		setUserId(in_userId);
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long aValue) {
		this.userId = aValue;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String aValue) {
		this.username = aValue;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String aValue) {
		this.password = aValue;
	}

	public Short getSex() {
		return sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}
	

	
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String aValue) {
		this.email = aValue;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
	
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String aValue) {
		this.phone = aValue;
	}
	
	public String getOriginalphoto() {
		return originalphoto;
	}

	public void setOriginalphoto(String originalphoto) {
		this.originalphoto = originalphoto;
	}
	
	public String getOriginalsign_pic() {
		return originalsign_pic;
	}

	public void setOriginalsign_pic(String originalsignPic) {
		originalsign_pic = originalsignPic;
	}

	public Date getAccessionTime() {
		return this.accessionTime;
	}

	public void setAccessionTime(Date aValue) {
		this.accessionTime = aValue;
	}
	
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String aValue) {
		this.mobile = aValue;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String aValue) {
		this.fax = aValue;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String aValue) {
		this.address = aValue;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String aValue) {
		this.zip = aValue;
	}

	public String getSign_pic() {
		return sign_pic;
	}

	public void setSign_pic(String signPic) {
		sign_pic = signPic;
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String aValue) {
		this.photo = aValue;
	}
	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short aValue) {
		this.status = aValue;
	}
	

	public String getEducation() {
		return this.education;
	}

	public void setEducation(String aValue) {
		this.education = aValue;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String aValue) {
		this.fullname = aValue;
	}

	public Short getDelFlag() {
		return this.delFlag;
	}
	
	public void setDelFlag(Short delFlag) {
		this.delFlag = delFlag;
	}

	public String getDynamicPwd() {
		return this.dynamicPwd;
	}

	public void setDynamicPwd(String dynamicPwd) {
		this.dynamicPwd = dynamicPwd;
	}
	
	public Short getDyPwdStatus() {
		return this.dyPwdStatus;
	}

	public void setDyPwdStatus(Short dyPwdStatus) {
		this.dyPwdStatus = dyPwdStatus;
	}
	
	public String getDepNames() {
		this.depNames = "";
			Iterator it = getOrgs().iterator();
			while (it.hasNext()) {
				SysOrg org = (SysOrg) it.next();
				this.depNames = (this.depNames + org.getOrgName() + ",");
			}
			if (this.depNames.length() > 0)
				this.depNames = this.depNames.substring(0,
						this.depNames.length() - 1);

		return this.depNames;
	}
	 
	public void setDepNames(String depNames) {
		this.depNames = depNames;
	}
	
	public String getPosNames()
	{
	  this.posNames = "";

	  Iterator it = getPositions().iterator();
	  while (it.hasNext()) {
	    Position p = (Position)it.next();
	    this.posNames = (this.posNames + p.getPosName() + ",");
	  }
	  if (this.posNames.length() > 0) this.posNames = this.posNames.substring(0, this.posNames.length() - 1);
	  return this.posNames;
	}

	public void setPosNames(String posNames) {
	  this.posNames = posNames;
	}
	
	public String getRoleNames() {
		this.roleNames = "";
		/*Iterator it = getRoles().iterator();
		while (it.hasNext()) {
			SysRole ar = (SysRole) it.next();
			this.roleNames = (this.roleNames + ar.getRoleName() + ",");
		}
		if (this.roleNames.length() > 0)
			this.roleNames = this.roleNames.substring(0, this.roleNames
					.length() - 1);*/
		return this.roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	
	public String getPrimaryDep() {
		return this.sysOrg != null ? this.sysOrg.getOrgName() : "";
	}

	public void setPrimaryDep(String primaryDep) {
		this.primaryDep = primaryDep;
	}
	
	public Set<SysRole> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<SysRole> roles) {
		this.roles = roles;
	}
	public Set<String> getRights() {
		return this.rights;
	}
	
	public void setRights(Set<String> rights) {
		this.rights = rights;
	}
	
	public Set getOrgs() {
		return this.orgs;
	}

	public void setOrgs(Set orgs) {
		this.orgs = orgs;
	}
	
	public Set getPositions() {
		return positions;
	}

	public void setPositions(Set positions) {
		this.positions = positions;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

    //非字段映射部分
	/**
	 * 重写UserDetails 的getAuthorities方法。
	 * 
	 * <pre>
	 * 
	 * 目前角色支持两种方式。
	 * 1.用户和角色的映射。
	 * 2.部门和角色的映射。
	 * 
	 * 两种角色进行合并构成当前用户的角色。
	 * </pre>
	 */
	public List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> rtnList = new ArrayList<GrantedAuthority>();
		for (SysRole role : roles) {
			rtnList.add(role);
		}
		rtnList.add(new GrantedAuthorityImpl("ROLE_PUBLIC"));
		return rtnList;
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		if (this.status.shortValue() == 1) {
			return true;
		}
		return false;
	} 

	public boolean isSystem() {
		Set roles = getRoles();
		boolean flag = false;
		for (Iterator it = roles.iterator(); it.hasNext();) {
			SysRole role = (SysRole) it.next();
			if (role.getRoleId().shortValue() == SysRole.SYSTEM_ROLEID
					.shortValue()) {
				flag = true;
			}
		}
		return flag;
	}

	public void init() {
	}

	public void initMenuRights() {
	}

	public String getFunctionRights() {
		StringBuffer sb = new StringBuffer();

		Iterator it = this.rights.iterator();

		while (it.hasNext()) {
			sb.append((String) it.next()).append(",");
		}

		if (this.rights.size() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();
	}

	public String getFirstKeyColumnName() {
		return "userId";
	}
	
	
	
	public String getNewUserId(){
		return "p"+getUserId();
	}

	@Override
	public Map<String, String> getRelation() {
		Map<String, String> propMap = new HashMap<String, String>();
		propMap.put("id", "newUserId");
		propMap.put("text", "username");	 
		propMap.put("parentId", "orgId");
		return propMap;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}
	
}
