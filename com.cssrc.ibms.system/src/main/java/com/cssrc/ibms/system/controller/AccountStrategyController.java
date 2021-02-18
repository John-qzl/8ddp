package com.cssrc.ibms.system.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.AccountStrategy;
import com.cssrc.ibms.system.service.AccountStrategyService;

/**
 * 账户策略管理
 * @author liubo
 * @date 2017年3月30日
 */
@Controller
@RequestMapping( { "/oa/system/accountStrategy/" })
@Action(ownermodel = SysAuditModelType.ACCOUNTSTRATEGY_MANAGEMENT)
public class AccountStrategyController extends BaseController{
	
	@Resource
	private AccountStrategyService accountStrategyService;

	/**
	 * 策略信息列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = "策略信息列表", execOrder = ActionExecOrder.AFTER, detail = "查看策略信息列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		ModelAndView mv = getAutoView();
		List<AccountStrategy> accountStrategyList = accountStrategyService.getAll();
		mv.addObject("accountStrategyList", accountStrategyList);
		return mv;
	}
	
	/**
	 * 编辑策略信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description = "编辑策略信息", execOrder = ActionExecOrder.AFTER, detail = "打开编辑策略信息页面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request,HttpServletResponse response) 
			throws Exception{
		ModelAndView mv = getAutoView();
		String strategyId = RequestUtil.getString(request, "strategyId");
		String returnUrl = RequestUtil.getPrePage(request);
		AccountStrategy accountStrategy = accountStrategyService.getById(strategyId);
		mv.addObject("returnUrl", returnUrl)
		  .addObject("accountStrategy", accountStrategy);
		return mv;
	}
	
	/**
	 * 保存策略信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("save")
	@Action(description = "更新策略信息", execOrder = ActionExecOrder.AFTER, detail = "更新策略 <#assign entity=accountStrategyService.getById(Long.valueOf(strategyId))/> 【${entity.strategy_name}】的信息",exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { AccountStrategy.class }, pkName = "strategyId")
	public void save(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String strategyId = RequestUtil.getString(request,"strategyId");
		String strategyExplain = RequestUtil.getString(request,"strategyExplain");
		String isEnable = RequestUtil.getString(request,"isEnable");
		String strategyValue = RequestUtil.getString(request,"strategyValue");
		String resultMsg = null;
		//设置操作结果，默认为操作失败
		Short result = 0;
		//帐户锁定阈值
		AccountStrategy accountStrategyLock = accountStrategyService.getById("5");
		//若是最短使用期限，并且最长使用期限启用，则最短使用期限必须小于最长使用期限
		AccountStrategy accountStrategyMax = accountStrategyService.getById("3");
		//若是最长使用期限，并且最短使用期限启用，则最长使用期限必须大于最短使用期限
		AccountStrategy accountStrategyMin = accountStrategyService.getById("4");
		//帐户锁定的时间长度，如果定义了帐户锁定阈值，则帐户锁定时间必须大于或等于重置时间。
		AccountStrategy accountStrategyLockTime = accountStrategyService.getById("6");
		//帐户锁定的时间长度，如果定义了帐户锁定阈值，则帐户锁定时间必须大于或等于重置时间。
		AccountStrategy accountStrategyResetTime = accountStrategyService.getById("7");
		if (StringUtil.isEmpty(strategyValue))
			resultMsg = "策略值不能为空";
		else if(isEnable.equals("1")&&strategyId.equals("4")&&accountStrategyMax.getIs_enable().equals("1")&&Long.valueOf(strategyValue)>=Long.valueOf(accountStrategyMax.getStrategy_value())){
			resultMsg = "密码最短使用期限--必须小于--密码最长使用期限";
		}else if(isEnable.equals("1")&&strategyId.equals("3")&&accountStrategyMin.getIs_enable().equals("1")&&Long.valueOf(strategyValue)<=Long.valueOf(accountStrategyMin.getStrategy_value())){
			resultMsg = "密码最长使用期限--必须大于--密码最短使用期限";
		}else if(strategyId.equals("7")&&isEnable.equals("1")&&accountStrategyLockTime.getIs_enable().equals("1")&&Long.valueOf(strategyValue)>=Long.valueOf(accountStrategyLockTime.getStrategy_value())){
			resultMsg = "帐户锁定时间必须大于或等于重置时间";
		}else if(strategyId.equals("6")&&isEnable.equals("1")&&accountStrategyResetTime.getIs_enable().equals("1")&&Long.valueOf(strategyValue)<=Long.valueOf(accountStrategyResetTime.getStrategy_value())){
			resultMsg = "帐户锁定时间必须大于或等于重置时间";
		}else{
			AccountStrategy accountStrategy = accountStrategyService.getById(strategyId);
			accountStrategy.setStrategy_explain(strategyExplain);
			accountStrategy.setIs_enable(isEnable);
			accountStrategy.setStrategy_value(strategyValue);
			accountStrategyService.update(accountStrategy);
			result = 1;
		}
		if(result.equals(0)){
			ResultMessage resultObj = new ResultMessage(0,resultMsg);
			response.getWriter().print(resultObj);
		}else{
			ResultMessage resultObj = new ResultMessage(1,"编辑策略信息成功");
			response.getWriter().print(resultObj);
		}
		try {
			LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
}
