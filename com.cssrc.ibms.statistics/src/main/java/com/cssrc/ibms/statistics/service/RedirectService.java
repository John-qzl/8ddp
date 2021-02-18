package com.cssrc.ibms.statistics.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.statistics.intf.IAddressService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.statistics.dao.AddressDao;
import com.cssrc.ibms.statistics.model.Address;
import com.cssrc.ibms.statistics.model.Tool;


/**
 * <p>AddressService.java</p>
 * @author dengwenjie 
 * @date 2017年7月4日
 */
@Service
public class RedirectService{
	@Resource
	private ToolService toolService;
	@Resource
	private AddressService addressService;
	
	public String urlCheck(Map urlMap){
		String msg = "";
		if(urlMap.containsKey(Tool.NAME)&&urlMap.containsKey(Address.NAME)){
			String toolAlias = (String)urlMap.get(Tool.NAME);
			String addressAlias = (String)urlMap.get(Address.NAME);
			Integer res = toolService.isAliasExists(toolAlias);
			Integer res1 = addressService.isAliasExists(addressAlias);
			if(res==0||res1==0){
				msg = "数据库中不存在"+toolAlias+"或"+addressAlias+"别名！";
			}
		}else{
			msg = "url错误，url中应含有"+Tool.NAME+"和"+Address.NAME+"参数！";
		}
		return msg;
	}
	
}
