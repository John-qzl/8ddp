package com.cssrc.ibms.core.user.event;

import org.springframework.context.ApplicationEvent;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
/**
 * 用户bean操作监听类
 * <p>Title:UserEvent</p>
 * @author Yangbo 
 * @date 2016-8-18下午03:47:12
 */
public class UserEvent extends ApplicationEvent {
	private static final long serialVersionUID = -2595904294669110938L;
	public static int ACTION_UPD = 1;

	public static int ACTION_DEL = 2;

	public static int ACTION_ADD = 0;

	private Long userId = Long.valueOf(0L);
	private ISysUser user;
	private int action = ACTION_UPD;

	public UserEvent(Long source) {
		super(source);
		this.userId = source;
	}

	public int getAction() {
		return this.action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public Long getUserId() {
		return this.userId;
	}

	public ISysUser getUser() {
		return this.user;
	}

	public void setUser(ISysUser user) {
		this.user = user;
	}
	
	public String getActionMsg(){
	    if(ACTION_UPD==action){
	        return "update ";
	    }else if(ACTION_DEL==action){
	        return "delete ";
	    }else if(ACTION_ADD==action){
            return "add ";
        }
	    return "action error";
	}
}
