package com.cssrc.ibms.core.user.model;


public class SysUserEntity{
	
	private Long userId;
	
	private String username;

	private String password;
	
    //性别
	
	private Short sex;
	
	
	//@JsonManagedReference
	//private Department department;

	
	private String email;

	
	private String job;//职位

	
	private String phone;

	private String mobile;
	
	private String fax;
	
	private String address;

	private String zip;

	private String photo;//个人头像
	
	private String originalphoto;
	
	//private Date birthDay;//出生日期

	private String photopath;//个人头像
	
	private String originalphotopath;
	
	private String sign_pic;//电子签名

	private String originalsign_pic;//电子签名
	
	private String sign_picpath;//电子签名

	private String originalsign_picpath;//电子签名


	


	private Short status;//状态
	
	private String education; //学历
	
	private String fullname;//中文名

	     
	private Short delFlag;//是否删除

	
	private String dynamicPwd;

	
	private Short dyPwdStatus;

	
	private String depNames;

	
	private String posNames;

	
	private String roleNames;

	
	protected String primaryDep;
	
	//角色IDS
    String roleIds;

    //用户密级
    private String security;
    
	public String getRoleIds() {
		return roleIds;
	}


	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
	public String getOriginalphoto() {
		return originalphoto;
	}


	public void setOriginalphoto(String originalphoto) {
		this.originalphoto = originalphoto;
	}

	


	public String getPhotopath() {
		return photopath;
	}


	public void setPhotopath(String photopath) {
		this.photopath = photopath;
	}


	public String getOriginalphotopath() {
		return originalphotopath;
	}


	public void setOriginalphotopath(String originalphotopath) {
		this.originalphotopath = originalphotopath;
	}
	
//	public Date getBirthDay() {
//		return birthDay;
//	}
//
//
//	public void setBirthDay(Date birthDay) {
//		this.birthDay = birthDay;
//	}

//	@XmlTransient
//	@JsonBackReference
//	private Set<SysRole> roles = new HashSet();
//	
	//private Date accessionTime;//个人入职时间

	
//	
//	public Set<SysRole> getRoles() {
//		return roles;
//	}
//
//
//	public void setRoles(Set<SysRole> roles) {
//		this.roles = roles;
//	}



	public Long getUserId() {
		return userId;
	}


	public String getSign_picpath() {
		return sign_picpath;
	}


	public void setSign_picpath(String signPicpath) {
		sign_picpath = signPicpath;
	}


	public String getOriginalsign_picpath() {
		return originalsign_picpath;
	}


	public void setOriginalsign_picpath(String originalsignPicpath) {
		originalsign_picpath = originalsignPicpath;
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


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
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


	public String getFullname() {
		return fullname;
	}


	public void setFullname(String fullname) {
		this.fullname = fullname;
	}


	public Short getDelFlag() {
		return delFlag;
	}


	public void setDelFlag(Short delFlag) {
		this.delFlag = delFlag;
	}


	public String getDynamicPwd() {
		return dynamicPwd;
	}


	public void setDynamicPwd(String dynamicPwd) {
		this.dynamicPwd = dynamicPwd;
	}


	public Short getDyPwdStatus() {
		return dyPwdStatus;
	}


	public void setDyPwdStatus(Short dyPwdStatus) {
		this.dyPwdStatus = dyPwdStatus;
	}


	public String getDepNames() {
		return depNames;
	}


	public void setDepNames(String depNames) {
		this.depNames = depNames;
	}


	public String getPosNames() {
		return posNames;
	}


	public void setPosNames(String posNames) {
		this.posNames = posNames;
	}


	public String getRoleNames() {
		return roleNames;
	}


	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}


	public String getPrimaryDep() {
		return primaryDep;
	}


	public void setPrimaryDep(String primaryDep) {
		this.primaryDep = primaryDep;
	}


	public String getSecurity() {
		return security;
	}


	public void setSecurity(String security) {
		this.security = security;
	}

	

//	public Set<SysRole> getRoles() {
//		return roles;
//	}
//
//
//	public void setRoles(Set<SysRole> roles) {
//		this.roles = roles;
//	}
	
	
}
