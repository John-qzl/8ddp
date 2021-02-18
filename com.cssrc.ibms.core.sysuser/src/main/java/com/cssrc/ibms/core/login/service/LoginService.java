package com.cssrc.ibms.core.login.service;

import java.io.File;
import java.util.Date;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.login.intf.ILoginService;
import com.cssrc.ibms.api.login.model.ILoginLog;
import com.cssrc.ibms.api.system.intf.IAccountStrategyService;
import com.cssrc.ibms.api.system.model.IAccountStrategy;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.login.model.LoginLog;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.encrypt.PasswordUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;

/*import zlicense.verify.VerifyLicense;*/

@Service
public class LoginService implements ILoginService
{
    protected Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Resource
    LoginLogService loginLogService;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private IAccountStrategyService accountStrategyService;

    public boolean checkLicesne(String licensePath, String clientType, String IPAddress, StringBuffer errorInfo)
    {
        try
        {
//            String license =
//                SysConfConstant.CONF_ROOT + File.separator + "properties" + File.separator + "lincense.lic";
//            VerifyLicense.verifyLicense(license);
            return true;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return false;
        }

    }

    public ILoginLog checkLoginUser(String username, String password, ISysUser sysUser, HttpServletRequest request)
    {
        String msg = "";
        // 初始化LoginLog
        LoginLog loginLog = new LoginLog();
        loginLog.setAccount(username);
        loginLog.setIp(RequestUtil.getIpAddr(request));
        loginLog.setStatus(LoginLog.SUCCESS);
        loginLog.setDesc("登陆成功");
        // 检测登陆输入为空报错
        if (org.apache.commons.lang.StringUtils.isEmpty(username))
        {
            msg = "用户名为空";
            loginLog.setStatus(LoginLog.ACCOUNT_PWD_EMPTY);
            loginLog.setDesc(msg);
        }
        else if (org.apache.commons.lang.StringUtils.isEmpty(password))
        {
            msg = "密码为空";
            loginLog.setStatus(LoginLog.ACCOUNT_PWD_EMPTY);
            loginLog.setDesc(msg);
        }
        else if (null == sysUser || SysUser.DELED == sysUser.getDelFlag())
        {
            msg = "用户不存在!";
            loginLog.setStatus(LoginLog.ACCOUNT_PWD_ERR);
            loginLog.setDesc(msg);
        }
        else if (sysUser.getStatus() == 0)
        {
            msg = "用户被禁用!";
            loginLog.setStatus(LoginLog.ACCOUNT_DISABLED);
            loginLog.setDesc(msg);
        }
        else if (sysUser.getStatus() == -1)
        {
            msg = "用户被删除!";
            loginLog.setStatus(LoginLog.ACCOUNT_DELETE);
            loginLog.setDesc(msg);
        }
        else if (sysUser.getStatus() == 2)
        {
            msg = "用户已离职!";
            loginLog.setStatus(LoginLog.ACCOUNT_OVERDUE);
            loginLog.setDesc(msg);
        }
        else if (!sysUser.getPassword().equals(PasswordUtil.generatePassword(password)))
        {
            msg = "用户密码有误!";
            // 获取当前时间
            Date now = new Date();
            int failueNum = (sysUser.getLoginFailures()==null||sysUser.getLoginFailures()=="")?0:Integer.valueOf(sysUser.getLoginFailures());
            int newFailueNum = failueNum + 1;

            // 获取锁定阈值
            IAccountStrategy accountStrategy = accountStrategyService.getById("5");
            String strategyValue = accountStrategy.getStrategy_value();
            //判断是否为整数
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");

            if (BeanUtils.isNotEmpty(strategyValue)&&pattern.matcher(strategyValue).matches())
            {
                if (newFailueNum >= Integer.valueOf(strategyValue))
                {
                    // 超过锁定阀值就将用户锁定
                    sysUserService.updLock(sysUser.getUserId(), strategyValue, (short)1, now, now);
                }
            }
            // 更新用户出错信息
            sysUserService.updateFailure(sysUser.getUserId(), String.valueOf(newFailueNum), now);

            // 根据账户策略--账户是否锁定进行登录验证
            boolean tacticCheck = checkUserLock(loginLog, sysUser);
            if (tacticCheck)
            {
                loginLog.setStatus(LoginLog.ACCOUNT_PWD_ERR);
                loginLog.setDesc(msg);
            }

        }
        else if (sysUser.getPassword().equals(PasswordUtil.generatePassword(password)))
        {
            // 根据账户策略进行登录验证
            if (checkUserLock(loginLog, sysUser))
            {
                checkUserEnable(loginLog, sysUser);
            }
        }

        return loginLog;
    }

    /**
     * 校验账户策略--账户是否锁定
     * @author liubo
     * @param errorInfo
     * @param sysUser
     * @return
     */
    public boolean checkUserLock(LoginLog loginLog, ISysUser sysUser)
    {

        boolean checkRes = accountStrategyService.meetAccountLock(sysUser);
        if (!checkRes)
        {
            Date begin = sysUser.getLastFailureTime();
            IAccountStrategy resetTimeStrategy = accountStrategyService.getById("6");
            long between = (System.currentTimeMillis() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
            long remainTime = Integer.valueOf(resetTimeStrategy.getStrategy_value()) * 60 - between;
            // begin.setMinutes();\
            long minute = remainTime / 60;
            long second = remainTime % 60;
            String errorInfo = "账户已被锁定，请联系系统管理员解锁或等待账户<br/>自动解锁！<span style=\"color:black\">剩余时间为：" + minute + "分钟"
                + second + "秒</span>";
            short status = LoginLog.ACCOUNT_LOCKED;
            loginLog.setDesc(errorInfo);
            loginLog.setStatus(status);
            return false;
        }

        return true;
    }

    /**
     * 校验账户策略--密码最长使用期限
     * @author liubo
     * @param errorInfo
     * @param sysUser
     * @return
     */
    public boolean checkUserEnable(LoginLog loginLog, ISysUser sysUser)
    {

        boolean checkRes = accountStrategyService.meetMaxPasswordAge(sysUser);
        if (!checkRes)
        {
            String userId = sysUser.getUserId().toString();
            String errorInfo = "<span style=\"color:black;size:50px;\">" + sysUser.getFullname()
                + "</span>&nbsp&nbsp的密码已过期，是否更新 ?<a class=\"login_btn changePassword\" "
                + "style=\"color:white;\" onclick=\"changePassword(" + userId + ")\">更新</a>";
            short status = LoginLog.ACCOUNT_OVERDUE;
            loginLog.setDesc(errorInfo);
            loginLog.setStatus(status);
            return false;
        }
        return true;
    }
}
