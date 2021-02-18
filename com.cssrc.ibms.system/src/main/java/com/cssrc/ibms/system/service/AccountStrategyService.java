package com.cssrc.ibms.system.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.IAccountStrategyService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.system.dao.AccountStrategyDao;
import com.cssrc.ibms.system.model.AccountStrategy;

/**
 * AccountStrategyService
 * @author liubo
 * @date 2017年3月28日
 */
@Service
public class AccountStrategyService extends BaseService<AccountStrategy> implements IAccountStrategyService{
	
	@Resource
	private AccountStrategyDao accountStrategyDao;
	@Resource
	private ISysUserService sysUserService;
	
	protected IEntityDao<AccountStrategy, Long> getEntityDao() {
		return this.accountStrategyDao;
	}

	/**
	 * 获取所有的策略信息
	 * @return
	 */
	public List<AccountStrategy> getAll(){
		return accountStrategyDao.getAll();
	}
	
	/**
	 * 通过策略id获取策略信息
	 * @param id
	 * @return
	 */
	public AccountStrategy getById(String id){
		Long strategyId = Long.valueOf(id);
		AccountStrategy accountStrategy = accountStrategyDao.getById(strategyId);
		return accountStrategy;
	}
	
	/**
	 * 更新策略信息
	 * @param accountStrategy
	 */
	public void updateStrategy(AccountStrategy accountStrategy){
		accountStrategyDao.update(accountStrategy);
	}
	
	/**
	 * 检验密码是否符合“密码最小长度”策略，策略没有启用或满足策略返回true
	 * @param password
	 * @return
	 */
	public String meetMinPasswordLen(String password){
		String flag = "true";

    	AccountStrategy accountStrategy = this.getById("1");
    	
    	if(accountStrategy != null){
    		if(accountStrategy.getIs_enable().equalsIgnoreCase("1")){
    			if(password.length()<Integer.valueOf(accountStrategy.getStrategy_value())){
    				flag = accountStrategy.getStrategy_explain();
    			}
    		}
    	}
    	return flag;
	}
	
	/**
	 * 检验密码是否符合“密码复杂性要求”策略，策略没有启用或满足策略返回true
	 * @param userName
	 * @param password
	 * @return
	 */
	public String meetPasswordComplexity(String userName, String password){
		String flag = "true";
		AccountStrategy accountStrategy = this.getById("2");
    	
    	if(accountStrategy != null){
    		if(accountStrategy.getIs_enable().equalsIgnoreCase("1")){
    			String accountValue = accountStrategy.getStrategy_value();
    			String[] accountValues = accountValue.split(",");
    			//5位一次代表
    			//必须包含特殊字符;
    			//必须包含大写字母;
    			//必须包含小写字母;
    			//必须包含数字;
    			//不包含用户的用户名;
    			//数字
    			String numberCheck = "\\d";
    			//小写字母
    		    String lowercaseCheck = "[a-z]";
    		    //大写字母
    		    String capitalCheck = "[A-Z]";
    		    //特殊符号
    		    String specificCheck = "\\W|\\_";
    		    //必须包含特殊字符;
    			if(accountValues[0].equals("1")){
    				Pattern p=Pattern.compile(specificCheck);
    				Matcher m=p.matcher(password);
		            if(m.find()){
		    	    }else{
		    	    	flag = "false";
		    	    }
    			}
    			//必须包含大写字母;
    			if(accountValues[1].equals("1")){
    				Pattern p=Pattern.compile(capitalCheck);
    				Matcher m=p.matcher(password);
    				if(m.find()){
		    	    }else{
		    	    	flag = "false";
		    	    }
    			}
    			//必须包含小写字母;
    			if(accountValues[2].equals("1")){
    				Pattern p=Pattern.compile(lowercaseCheck);
    				Matcher m=p.matcher(password);
    				if(m.find()){
		    	    }else{
		    	    	flag = "false";
		    	    }
    			}
    			//必须包含数字;
    			if(accountValues[3].equals("1")){
    				Pattern p=Pattern.compile(numberCheck);
    				Matcher m=p.matcher(password);
    				if(m.find()){
		    	    }else{
		    	    	flag = "false";
		    	    }
    			}
    			//不要包含用户的用户名
    			if(accountValues[4].equals("1")){
    				if(password.indexOf(userName)>=0){
        				flag = "false";
        			}
    			}
    		}
    	}
    	if(flag.equals("false")){
    		flag=accountStrategy.getStrategy_explain();
    	}
    	return flag;
	}
	
	/**
	 * 检验密码是否符合“密码最长使用期限（天）”策略，策略没有启用或满足策略返回true
	 * @param sysUser
	 * @return
	 */
	public boolean meetMaxPasswordAge(ISysUser sysUser){
		boolean flag = true;
		AccountStrategy accountStrategy = this.getById("3");
    	if(accountStrategy != null){
    		if(accountStrategy.getIs_enable().equalsIgnoreCase("1")){
    			Date now = new Date();
				//如果当前时间和密码设定时间之间超过设定的使用期限，则返回false
				try {
					System.out.println("当前系统时间："+now.toString());
					System.out.println("密码更改时间："+sysUser.getPasswordSetTime().toString());
					long between=(now.getTime()-sysUser.getPasswordSetTime().getTime())/1000;//除以1000是为了转换成秒 
    				
    				System.out.println("密码已用时长（秒）："+between);
    				System.out.println("密码最长使用期限（天）："+accountStrategy.getStrategy_value());

    				if(between>=Integer.valueOf(accountStrategy.getStrategy_value().toString())*24*3600){
    					flag = false;
    				}
				}catch (Exception e){
					System.out.println(e.getMessage());
				}
    		}
    	}
    	return flag;
	}
	
	/**
	 * 检验密码是否符合“密码最短使用期限（天）”策略，策略没有启用或满足策略返回true
	 * @param sysUser
	 * @return
	 */
	public boolean meetMinPasswordAge(ISysUser sysUser){
		boolean flag = true;
		AccountStrategy accountStrategy = this.getById("3");
		
    	if(accountStrategy != null){
    		if(accountStrategy.getIs_enable().equalsIgnoreCase("1")){   				
				//如果当前时间和密码设定时间之间没有超过设定的使用期限，则返回false
				DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM,Locale.CHINA);
				try{ 
					java.util.Date begin=df.parse(sysUser.getPasswordSetTime().toString());      
    				long between=(System.currentTimeMillis()-begin.getTime())/1000;//除以1000是为了转换成秒   
    				
    				if(between<=Integer.valueOf(accountStrategy.getStrategy_value().toString())*24*3600){
    					flag = false;
    				}
				}catch (ParseException pe) {   
		            System.out.println(pe.getMessage());   
		        }catch (Exception e){   
					System.out.println(e.getMessage()); 
				}  
    		}
    	}
    	return flag;
	}
	
	
	/**
	 * 判断帐户是否锁定，“帐户锁定阈值”没有启用或帐户没有锁定则返回true
	 * @param sysUser
	 * @return
	 */
	public boolean meetAccountLock(ISysUser sysUser){
		boolean flag = true;		
		AccountStrategy accountStrategy = this.getById("5");
    	if(accountStrategy != null){
    		if(accountStrategy.getIs_enable().equalsIgnoreCase("1")){
    			
    			//如果“最近一次登录失败的时间”为空值，则将登录失败计算器归0
				if(sysUser.getLastFailureTime()==null || sysUser.getLastFailureTime().toString()==""){
					if(!sysUser.getLoginFailures().equalsIgnoreCase("0")){
						// 更新登录失败次数
						sysUserService.updateFailure(sysUser.getUserId(), "0", null);
					}
				} else {
					//如果“当前时间”减去“最近一次登录失败的时间”已超过“复位锁定计数器的时间”，则将登录失败计算器归0
					AccountStrategy  resetTimeStrategy = this.getById("7");
					
			    	if(resetTimeStrategy != null){
        				try{
        					Date begin = sysUser.getLastFailureTime();
            				long between=(System.currentTimeMillis()-begin.getTime())/1000;//除以1000是为了转换成秒   
            				long minute1=between/60;   
            				    
            				//如果“当前时间”减去“最近一次登录失败的时间”已超过“复位锁定计数器的时间”，则将登录失败计算器归0
            				if(minute1>=Integer.valueOf(resetTimeStrategy.getStrategy_value())){
            					// 更新登录失败次数
            					sysUserService.updateFailure(sysUser.getUserId(), "0", null);
            				}
        				}catch (Exception e){
        					e.printStackTrace();
        				}
    				}
				}
				
    			//如果“锁定时间”为空值，则将用户帐号解锁
				if(sysUser.getLockTime()==null || sysUser.getLockTime().toString()==""){
					if(sysUser.getLockState()!=0){
						// 更新帐号锁定状态
						sysUserService.updLock(sysUser.getUserId(),"0",(short) 0,null,null);
					}
				}else{
					//如果“当前时间”减去“锁定时间”已超过“帐户锁定时间”，则将用户帐号解锁
					AccountStrategy  lockTimeStrategy = this.getById("6");
					
			    	if(lockTimeStrategy != null){
        				try{ 
        					Date begin = sysUser.getLockTime();
        					
            				long between=(System.currentTimeMillis()-begin.getTime())/1000;//除以1000是为了转换成秒
            				
            				//如果“当前时间”减去“锁定时间”已超过“帐户锁定时间”，则将用户帐号解锁
            				if(between>=Integer.valueOf(lockTimeStrategy.getStrategy_value().toString())*60){
            					// 更新帐号锁定状态
            					sysUserService.updLock(sysUser.getUserId(),"0",(short) 0,null,null);
           				    }else{
            					flag = false;
            				}
        				}catch (Exception e){
        					e.printStackTrace();
        				}
    				}
				}
			}
    	}
    	return flag;
	}
}
