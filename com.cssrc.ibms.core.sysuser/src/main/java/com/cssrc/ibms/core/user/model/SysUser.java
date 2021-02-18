package com.cssrc.ibms.core.user.model;

import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.sysuser.SystemConst;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.user.service.SysOrgRoleService;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.user.service.SysRoleService;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.json.GrantedAuthorityAdapter;
import com.cssrc.ibms.core.util.json.JacksonDateSerializer;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
@XmlRootElement(name = "sysUser")
@XmlAccessorType(XmlAccessType.FIELD)
public class SysUser extends BaseModel implements UserDetails, ISysUser {
	private static final long serialVersionUID = 1L;

	@SysFieldDescription(detail="用户id")
	protected Long userId;

	@SysFieldDescription(detail="用户姓名")
	protected String fullname;

	@SysFieldDescription(detail="用户帐户")
	protected String username;//用户帐户 等同于 account

	@SysFieldDescription(detail="用户密码")
	protected String password;

	@SysFieldDescription(detail="性别",maps="{\"1\":\"男\",\"0\":\"女\"}")
	protected Short sex;// 性别

	@SysFieldDescription(detail="邮箱")
	protected String email;

	@SysFieldDescription(detail="职务")
	protected String job;// 职务

	@SysFieldDescription(detail="电话")
	protected String phone;

	@SysFieldDescription(detail="手机")
	protected String mobile;

	@SysFieldDescription(detail="传真")
	protected String fax; // 传真

	@SysFieldDescription(detail="住址")
	protected String address;

	@SysFieldDescription(detail="邮编")
	protected String zip;// 邮编

	@SysFieldDescription(detail="个人头像")
	protected String photo;// 个人头像

	@SysFieldDescription(detail="电子签名图片")
	protected String sign_pic;// 电子签名图片

	@SysFieldDescription(detail="原始照片电子签名图片")
	protected String originalsign_pic;// 电子签名图片

	@SysFieldDescription(detail="原始个人头像")
	protected String originalphoto;// 个人头像

	@SysFieldDescription(detail="入职时间")
	@JsonSerialize(using = JacksonDateSerializer.class)
	protected Date accessionTime;// 个人入职时间

	@SysFieldDescription(detail="出生日期")
	@JsonSerialize(using = JacksonDateSerializer.class)
	protected Date birthDay;// 出生日期

	@SysFieldDescription(detail="状态",maps="{\"1\":\"激活\",\"0\":\"禁用\",\"-1\":\"离职\"}")
	protected Short status;// 状态1=激活0=禁用-1=离职

	@SysFieldDescription(detail="学历")
	protected String education; // 学历

	//TODO 此处用于数据同步功能未实现
	@SysFieldDescription(detail="组织id")
	protected Long[] orgId;

	@SysFieldDescription(detail="组织名")
	protected String orgName;

	@SysFieldDescription(detail="用户密级",maps="{\"3\":\"非密\",\"6\":\"一般\",\"9\":\"重要\",\"12\":\"核心\"}")
	protected String security;

	@SysFieldDescription(detail="是否删除",maps="{\"1\":\"删除\",\"0\":\"未删除\"}")
	protected Short delFlag;// 是否删除

	@SysFieldDescription(detail="创建人ID")
	protected Long user_creatorId;// 创建人ID

	@SysFieldDescription(detail="创建时间")
	protected Date user_createTime;// 创建时间

	@SysFieldDescription(detail="更改人ID")
	protected Long user_updateId;// 更改人ID

	@SysFieldDescription(detail="更改时间")
	protected Date user_updateTime;// 更改时间

	@SysFieldDescription(detail="数据来源")
	protected String fromType = "系统添加"; //数据来源

	@SysFieldDescription(detail="最后一次登录失败时间")
	protected Date lastFailureTime;// 最后一次登录失败时间

	@SysFieldDescription(detail="登录失败次数")
	protected String loginFailures;// 登录失败次数

	@SysFieldDescription(detail="锁定状态",maps="{\"1\":\"已锁定\",\"0\":\"未锁定\"}")
	protected Short lockState;// 锁定状态--1表示已锁定，0表示未锁定

	@SysFieldDescription(detail="锁定时间")
	protected Date lockTime;// 锁定时间

	@SysFieldDescription(detail="密码更改时间")
	protected Date passwordSetTime;// 密码更改时间

	@SysFieldDescription(detail="技术职称",maps="{\"zlgcs\":\"助理工程师\",\"gcs\":\"工程师\",\"gjgcsf\":\"高级工程师（副高）\",\"gjgcsz\":\"高级工程师（正高）\",\"yjy\":\"研究员\",\"zlyjy\":\"助理研究员\"}")
	protected String skilltitle;//技术职称

	@SysFieldDescription(detail="专业")
	protected String major;//专业

	@SysFieldDescription(detail="是否锁定",maps="{\"true\":\"已锁定\",\"false\":\"未锁定\"}")
	protected Boolean accountNonLocked;// 锁定状态

	@SysFieldDescription(detail="是否过期",maps="{\"true\":\"已过期\",\"false\":\"未过期\"}")
	protected Boolean accountNonExpired;// 是否过期

	@SysFieldDescription(detail="是否有效",maps="{\"true\":\"有效\",\"false\":\"无效\"}")
	protected Boolean enabled;// 是否有效

	@SysFieldDescription(detail="证书是否过期",maps="{\"true\":\"过期\",\"false\":\"未过期\"}")
	protected Boolean credentialsNonExpired;// 证书是否过期

	@SysFieldDescription(detail="人员所属角色")
	protected String roleNames;

    public String getPosNames() {
        return posNames;
    }

    public void setPosNames(String posNames) {
        this.posNames = posNames;
    }

    @SysFieldDescription(detail="人员所属岗位")
    protected String posNames;

	//人员密级map，value为密级对应的中文名拼音
	static {
		SECURITY_USER_MAP.put(ISysUser.SECURITY_FEIMI, ISysUser.FEIMI);
		SECURITY_USER_MAP.put(ISysUser.SECURITY_YIBAN, ISysUser.YIBAN);
		SECURITY_USER_MAP.put(ISysUser.SECURITY_ZHONGYAO, ISysUser.ZHONGYAO);
		SECURITY_USER_MAP.put(ISysUser.SECURITY_HEXIN, ISysUser.HEXIN);
	}
	public Date getPasswordSetTime() {
		return passwordSetTime;
	}

	public void setPasswordSetTime(Date passwordSetTime) {
		this.passwordSetTime = passwordSetTime;
	}

	public Short getLockState() {
		return lockState;
	}

	public void setLockState(Short lockState) {
		this.lockState = lockState;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public Date getLastFailureTime() {
		return lastFailureTime;
	}

	public void setLastFailureTime(Date lastFailureTime) {
		this.lastFailureTime = lastFailureTime;
	}

	public String getLoginFailures() {
		return loginFailures;
	}

	public void setLoginFailures(String loginFailures) {
		this.loginFailures = loginFailures;
	}

	public String getSecurity() {
		return security;
	}
	public String getFormatSecurity() {
		if(SECURITY_USER_MAP.containsKey(security)) {
			return SECURITY_USER_MAP.get(security);
		}else {
			return ISysUser.FEIMI;
		}
	}
	public void setSecurity(String security) {
		this.security = security;
	}
	private Long[] superiorIds;
	public SysUser() {
	}

	public SysUser(Long in_userId) {
		setUserId(in_userId);
	}
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Short getSex() {
		return sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getSign_pic() {
		return sign_pic;
	}

	public void setSign_pic(String signPic) {
		sign_pic = signPic;
	}

	public String getOriginalsign_pic() {
		return originalsign_pic;
	}

	public void setOriginalsign_pic(String originalsignPic) {
		originalsign_pic = originalsignPic;
	}

	public String getOriginalphoto() {
		return originalphoto;
	}

	public void setOriginalphoto(String originalphoto) {
		this.originalphoto = originalphoto;
	}

	public Date getAccessionTime() {
		return accessionTime;
	}

	public void setAccessionTime(Date accessionTime) {
		this.accessionTime = accessionTime;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public Short getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Short delFlag) {
		this.delFlag = delFlag;
	}

	public Long[] getOrgId() {
		return orgId;
	}

	public void setOrgId(Long[] orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long[] getSuperiorIds() {
		return superiorIds;
	}

	public void setSuperiorIds(Long[] superiorIds) {
		this.superiorIds = superiorIds;
	}

	public Long getUser_creatorId() {
		return user_creatorId;
	}

	public void setUser_creatorId(Long user_creatorId) {
		this.user_creatorId = user_creatorId;
	}

	public Date getUser_createTime() {
		return user_createTime;
	}

	public void setUser_createTime(Date user_createTime) {
		this.user_createTime = user_createTime;
	}

	public Long getUser_updateId() {
		return user_updateId;
	}

	public void setUser_updateId(Long user_updateId) {
		this.user_updateId = user_updateId;
	}

	public Date getUser_updateTime() {
		return user_updateTime;
	}

	public void setUser_updateTime(Date user_updateTime) {
		this.user_updateTime = user_updateTime;
	}



	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getSkilltitle() {
		return skilltitle;
	}

	public void setSkilltitle(String skilltitle) {
		this.skilltitle = skilltitle;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public boolean equals(Object rhs) {
		if ((rhs instanceof SysUser)) {
			return this.username.equals(((SysUser) rhs).username);
		}
		return false;
	}

	public int hashCode() {
		return this.username.hashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("userId", this.userId).append(
				"fullname", this.fullname).append("username", this.username)
				.append("password", this.password).append("delFlag",this.delFlag)
				.append("job", this.job).append("status",this.status)
				.append("email", this.email).append("mobile", this.mobile)
				.append("phone", this.phone).append("orgName", this.orgName)
				.append("sex", this.sex).append("photo", this.photo)
				.append("accessionTime",this.accessionTime)
				.append("fromType",this.fromType)
				.append("skilltitle",this.skilltitle)
				.append("major",this.major).toString();
	}

	public String getRoles() {
		String str = "";
		List<SysRole> roles = getUserRoleAuthorities();
		for (SysRole role : roles) {
			if ("".equals(str)) {
				str = role.getAuthority();
			} else {
				str = str + "," + role.getAuthority();
			}
		}
		return str;
	}
	/**
	 * 获取用户拥有的权限
	 */
	public List<SysRole> getUserRoleAuthorities() {
		List<SysRole> roleList = new ArrayList();
		SysRoleService sysRoleService = (SysRoleService) AppUtil.getBean(SysRoleService.class);
		roleList = sysRoleService.getByUserId(this.userId);

		SysOrgService sysOrgService = (SysOrgService) AppUtil.getBean(SysOrgService.class);
		SysOrgRoleService sysOrgRoleService = (SysOrgRoleService) AppUtil.getBean(SysOrgRoleService.class);
		List<? extends ISysOrg> sysOrgList = sysOrgService.getByUserId(this.userId);
		for(ISysOrg sysOrg:sysOrgList){
			Long orgId = sysOrg.getOrgId();
			List<SysOrgRole> orgRoleList = sysOrgRoleService.getRolesByOrgId(orgId);
			if (BeanUtils.isNotEmpty(orgRoleList)) {
				for (SysOrgRole sysOrgRole : orgRoleList) {
					SysRole sysRole = sysOrgRole.getRole();
					if(!roleList.contains(sysRole)){
						roleList.add(sysRole);
					}
				}
			}
		}
		return roleList;
	}
	/**
	 * UserDetails
	 * 获取当前用户拥有的权限
	 */
	@XmlJavaTypeAdapter(GrantedAuthorityAdapter.class)
	public Collection<GrantedAuthority> getAuthorities() {
		if (roleList.get() != null)
			return (Collection) roleList.get();
		Collection rtnList = new ArrayList();
		SysRoleService sysRoleService = (SysRoleService) AppUtil
				.getBean(SysRoleService.class);
		SysOrg curOrg = (SysOrg) UserContextUtil.getCurrentOrg();
		Long orgId = Long.valueOf(curOrg == null ? 0L : curOrg.getOrgId().longValue());

		Collection<String> totalRoleCol = sysRoleService
				.getRolesByUserIdAndOrgId(this.userId, orgId);
		if (BeanUtils.isNotEmpty(totalRoleCol)) {
			for (String role : totalRoleCol) {
				rtnList.add(new GrantedAuthorityImpl(role));
			}
		}
		String admin = getAdminUsername();

		if (admin.contains(this.username)) {
			rtnList.add(SystemConst.ROLE_GRANT_SUPER);
		}
		roleList.set(rtnList);
		return rtnList;
	}
	/**
	 * 管理员用户获得
	 * @return
	 */
	public static String getAdminUsername() {
		String admin = PropertyUtil.getByAlias("username");
		if (StringUtil.isEmpty(admin)) {
			admin = "system,check,right";
		}
		return admin;
	}
	/**
	 * 是否过期
	 */
	public boolean isAccountNonExpired() {
		return true;
	}
	/**
	 * 用户是否被锁定
	 */
	public boolean isAccountNonLocked() {
		return true;
	}
	/**
	 * 用户密码是否过期
	 */
	public boolean isCredentialsNonExpired() {
		return true;
	}
	/**
	 * 用户是否可用
	 */
	public boolean isEnabled() {
		if (this.status.shortValue() == 1) {
			return true;
		}
		return false;
	}
	public static void removeRoleList()
	{
	   roleList.remove();
	 }

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
}
