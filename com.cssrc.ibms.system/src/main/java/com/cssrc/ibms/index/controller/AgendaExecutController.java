package com.cssrc.ibms.index.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.service.AgendaExecutService;

/**
 *  日程人员关系Controller
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/calendar/agendaExecut/" })
public class AgendaExecutController   extends BaseController{

	@Resource
	private AgendaExecutService agendaExecutService;
	
	

}
