package com.cssrc.ibms.core.user.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * 
 * <p>Title:PwdStrategy</p>
 * @author Yangbo 
 * @date 2016-8-23上午09:37:21
 */
public class PwdStrategy extends BaseModel {
	protected Long id;
	protected String initPwd;
	protected Short forceChangeInitPwd;
	protected Short pwdRule;
	protected Short pwdLength;
	protected Short validity;
	protected Short handleOverdue;
	protected Short overdueRemind;
	protected Short verifyCodeAppear;
	protected Short errLockAccount;
	protected Short enable;
	protected String desc;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setInitPwd(String initPwd) {
		this.initPwd = initPwd;
	}

	public String getInitPwd() {
		return this.initPwd;
	}

	public void setForceChangeInitPwd(Short forceChangeInitPwd) {
		this.forceChangeInitPwd = forceChangeInitPwd;
	}

	public Short getForceChangeInitPwd() {
		return this.forceChangeInitPwd;
	}

	public void setPwdRule(Short pwdRule) {
		this.pwdRule = pwdRule;
	}

	public Short getPwdRule() {
		return this.pwdRule;
	}

	public void setPwdLength(Short pwdLength) {
		this.pwdLength = pwdLength;
	}

	public Short getPwdLength() {
		return this.pwdLength;
	}

	public void setValidity(Short validity) {
		this.validity = validity;
	}

	public Short getValidity() {
		return this.validity;
	}

	public void setHandleOverdue(Short handleOverdue) {
		this.handleOverdue = handleOverdue;
	}

	public Short getHandleOverdue() {
		return this.handleOverdue;
	}

	public void setOverdueRemind(Short overdueRemind) {
		this.overdueRemind = overdueRemind;
	}

	public Short getOverdueRemind() {
		return this.overdueRemind;
	}

	public void setVerifyCodeAppear(Short verifyCodeAppear) {
		this.verifyCodeAppear = verifyCodeAppear;
	}

	public Short getVerifyCodeAppear() {
		return this.verifyCodeAppear;
	}

	public void setErrLockAccount(Short errLockAccount) {
		this.errLockAccount = errLockAccount;
	}

	public Short getErrLockAccount() {
		return this.errLockAccount;
	}

	public void setEnable(Short enable) {
		this.enable = enable;
	}

	public Short getEnable() {
		return this.enable;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return this.desc;
	}

	public boolean equals(Object object) {
		if (!(object instanceof PwdStrategy)) {
			return false;
		}
		PwdStrategy rhs = (PwdStrategy) object;
		return rhs.getId() == getId();
	}

	public static class Status {
		public static final int SUCCESS = 0;
		public static final int NEED_TO_CHANGE_PWD = 1;
		public static final int LENGTH_TOO_SHORT = 2;
		public static final int NO_MATCH_NUMANDWORD = 3;
		public static final int NO_MATCH_NUMANDWORDANDSPECIAL = 4;
		public static final int PWD_OVERDUE = 5;
		public static final int ERR_TOO_MUCH_LOCKED = 6;
		public static final int PWD_INIT = 7;
	}
}
