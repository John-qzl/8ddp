package com.cssrc.ibms.api.sysuser.intf;

import java.util.List;
import java.util.Set;

import com.cssrc.ibms.api.sysuser.model.IUserUnder;


/**
 * 
 * <p>
 * Title:IUserUnderService
 * </p>
 * 
 * @author Yangbo
 * @date 2016-8-4下午03:44:39
 */
public abstract interface IUserUnderService {

	List<?extends IUserUnder> getMyLeader(Long userId);

	Set<String> getMyUnderUserId(Long userId);

    List<? extends IUserUnder> getMyUnderUser(Long userId);

	
	
}
