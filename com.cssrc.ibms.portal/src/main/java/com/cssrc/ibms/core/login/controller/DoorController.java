package com.cssrc.ibms.core.login.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.model.ITaskListTab;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsColType;
import com.cssrc.ibms.index.service.InsColTypeService;
/**
 * DoorController
 * 门户页面控制层
 * @author liubo
 * @date 2017年4月20日
 */
@Controller
@RequestMapping("/oa/login/door/")
public class DoorController extends BaseController {
	
	@Resource
	private InsColTypeService insColTypeService;
	
	/**
	 * 门户界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "main" })
	public ModelAndView main(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ModelAndView mv = getAutoView();
		
		List<InsColType> insColTypeList = insColTypeService.getAll();
		String dataNum = null;
		for(InsColType insColType:insColTypeList){
			String memo = insColType.getMemo();
			String rpcrefname = null;
			//String rpcrefname = "htglConsumerProjectService";
			if(StringUtil.isNotEmpty(memo)&&memo.contains("rpc")){
				String[] rpc = memo.split("=");
				rpcrefname = rpc[1];
			}
			if(StringUtil.isNotEmpty(rpcrefname)){
				//采用IOC方式，根据RPC远程过程调用服务调用数据
				CommonService commonService = (CommonService)  AppUtil.getContext().getBean(rpcrefname);
				Long userId = UserContextUtil.getCurrentUserId();
				dataNum = commonService.pendingMattersNum(userId);
			}
		}
		mv.addObject("dataNum", dataNum);
		return mv;

	}
	
}
