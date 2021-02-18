package com.cssrc.ibms.core.user.service;

import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.login.service.LoginLogService;
import com.cssrc.ibms.core.user.dao.PwdStrategyDao;
import com.cssrc.ibms.core.user.model.PwdStrategy;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.util.encrypt.PasswordUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service
public class PwdStrategyService extends BaseService<PwdStrategy> {

	@Resource
	private PwdStrategyDao dao;

	@Resource
	private LoginLogService loginLogService;

	@Resource
	private SysUserService sysUserService;

	protected IEntityDao<PwdStrategy, Long> getEntityDao() {
		return this.dao;
	}

	public void updateEnable(String[] ids, Short enable) {
		Map map = new HashMap();

		closeAll();

		map.put("ids", ids);
		map.put("enable", enable);
		this.dao.update("updateEnable", map);
	}

	public void closeAll() {
		Map map = new HashMap();

		map.put("enable", Integer.valueOf(0));
		this.dao.update("updateEnable", map);
	}

	public void save(PwdStrategy pwdStrategy) {
		Long id = pwdStrategy.getId();
		if (pwdStrategy.getEnable().shortValue() == 1) {
			closeAll();
		}

		if ((id == null) || (id.longValue() == 0L)) {
			id = Long.valueOf(UniqueIdUtil.genId());
			pwdStrategy.setId(id);
			add(pwdStrategy);
		} else {
			update(pwdStrategy);
		}
	}

	public PwdStrategy getUsing() {
		Map map = new HashMap();
		map.put("enable", Integer.valueOf(1));
		return (PwdStrategy) this.dao.getOne("getByEnable", map);
	}

	public boolean checkUserVcodeEnabled(String account) {
		PwdStrategy pwdStrategy = getUsing();
		if (pwdStrategy == null)
			return false;
		int failCount = this.loginLogService.getTodayFailCount(account);

		return (pwdStrategy.getVerifyCodeAppear().shortValue() > 0)
				&& (failCount >= pwdStrategy.getVerifyCodeAppear().shortValue());
	}

	public boolean checkUserLockable(String username) {
		SysUser user = this.sysUserService.getByUsername(username);
		if ((user == null) || (user.getDelFlag() == SysUser.LOCKED)) {
			return false;
		}
		PwdStrategy pwdStrategy = getUsing();
		if (pwdStrategy == null)
			return false;

		int failCount = this.loginLogService.getTodayFailCount(username);
		if ((pwdStrategy.getErrLockAccount().shortValue() > 0)
				&& (failCount >= pwdStrategy.getErrLockAccount().shortValue())) {
			return true;
		}

/*		if (pwdStrategy.getValidity().shortValue() > 0) {
			long validity = Long
					.parseLong(pwdStrategy.getValidity().toString()) * 2592000L * 1000L;
			long pwdUpdTime = user.getPwdUpdTime().getTime();
			long now = new Date().getTime();
			if (now - pwdUpdTime > validity) {
				return true;
			}
		}*/
		return false;
	}

	public String getUsingInitPwd() {
		PwdStrategy pwdStrategy = getUsing();
		if (pwdStrategy == null)
			return "";
		return pwdStrategy.getInitPwd();
	}

	public JSONObject checkUser(SysUser user, String pwd) {
		return checkUser(getUsing(), user, pwd);
	}

	private JSONObject checkUser(PwdStrategy pwdStrategy, SysUser user,
			String pwd) {
		JSONObject result = new JSONObject();
		if (pwdStrategy == null) {
			result.put("status", Integer.valueOf(0));
			result.put("msg", "登录成功");
			return result;
		}

		if ((StringUtil.isEmpty(user.getPassword()))
				&& (StringUtil.isNotEmpty(pwdStrategy.getInitPwd()))) {
			String enPassword = PasswordUtil.generatePassword(pwdStrategy
					.getInitPwd());
			user.setPassword(enPassword);
			result.put("status", Integer.valueOf(7));
			result.put("msg", "初始化密码成功");
			return result;
		}

		if (pwd.equals(pwdStrategy.getInitPwd())) {
			result.put("status", Integer.valueOf(1));
			result.put("msg", "密码与初始化密码一致，需要强制修改");
			return result;
		}

		if (pwdStrategy.getPwdRule().shortValue() != 0) {
			if ((pwdStrategy.getPwdRule().shortValue() == 1)
					&& (!pwd
							.matches("^(?!^\\d+$)(?!^[a-zA-Z]+$)[0-9a-zA-Z]{2,}$"))) {
				result.put("status", Integer.valueOf(3));
				result.put("msg", "密码不符合规则：字母跟数字");
				return result;
			}
			if (pwdStrategy.getPwdRule().shortValue() == 2) {
				boolean b = false;
				String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(pwd);
				if (m.find()) {
					String temp = m.replaceAll("").trim();
					b = temp
							.matches("^(?!^\\d+$)(?!^[a-zA-Z]+$)[0-9a-zA-Z]{2,}$");
				}
				if (!b) {
					result.put("status", Integer.valueOf(4));
					result.put("msg", "密码不符合规则：字母跟数字跟特殊字符");
					return result;
				}

			}

		}

		if ((pwdStrategy.getPwdLength().shortValue() > 0)
				&& (pwd.length() < pwdStrategy.getPwdLength().shortValue())) {
			result.put("status", Integer.valueOf(2));
			result.put("msg", "密码太短：长度至少为 " + pwdStrategy.getPwdLength());
			return result;
		}

/*		if (pwdStrategy.getHandleOverdue().shortValue() != 0) {
			if (pwdStrategy.getValidity().shortValue() > 0) {
				long validity = Long.parseLong(pwdStrategy.getValidity()
						.toString()) * 2592000L * 1000L;
				long pwdUpdTime = user.getPwdUpdTime().getTime();
				long now = new Date().getTime();
				if (now - pwdUpdTime > validity) {
					result.put("status", Integer.valueOf(5));
					result.put("msg", "密码已过期，您的密码更新在："
							+ TimeUtil.getDateTimeString(pwdUpdTime)
							+ "，而有效日期是 " + pwdStrategy.getValidity() + " 个月");
					return result;
				}

				if (pwdStrategy.getOverdueRemind().shortValue() > 0) {
					long remind = pwdStrategy.getOverdueRemind().shortValue()
							* 1000L * 3600L * 24L * 7L;
					if (validity - (now - pwdUpdTime) < remind) {
						result.put("remind", Boolean.valueOf(true));
						result.put("remindMsg", "您的密码即将过期，您的密码更新时间在："
								+ TimeUtil.getDateTimeString(pwdUpdTime)
								+ "，而有效日期是 " + pwdStrategy.getValidity()
								+ " 个月");
					} else {
						result.put("remind", Boolean.valueOf(false));
					}
				}
			}

		}*/

		result.put("status", Integer.valueOf(0));
		result.put("msg", "登录成功");
		return result;
	}

	public static void main(String[] args) {
		System.out.println(new Date(new Long("1435801763092").longValue()));
	}
}
