package com.cssrc.ibms.init.servlet;

/*---------------------------------------------------------------------------------------------------------
 *
 *   Developer：廉福业 [Hacker]
 * Create Date：2008-4-14 下午04:38:24
 * Description：1.
 *              2.
 *              3.
 *   Copyright (C) 2008 Hacker. All rights reserved.
 *
 *---------------------------------------------------------------------------------------------------------
 */

import javax.servlet.http.HttpServlet;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.init.model.Config;

public class InitApplication extends HttpServlet{

	public void init(){
		System.out.println("=========================================");
		System.out.println("开始初始化!");
		this.initConfig();		
	}

	private void initConfig(){
		System.out.println("开始读取配置信息......");
		Config.getInstance();
		System.out.println("欢迎信息：" + SysConfConstant.WELCOME_MESSAGE);
		System.out.println(SysConfConstant.COMPANY_NAME+"-"+SysConfConstant.SYSTEM_TITLE+" 配置完毕!");		
	}

	public void destroy(){		
		System.out.println(SysConfConstant.COMPANY_NAME+"-"+SysConfConstant.SYSTEM_TITLE+" 销毁!");		
		super.destroy();
	}

}
