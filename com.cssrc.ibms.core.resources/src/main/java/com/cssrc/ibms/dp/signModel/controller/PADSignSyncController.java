package com.cssrc.ibms.dp.signModel.controller;

import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.dp.signModel.entity.CwmSysSignModel;
import com.cssrc.ibms.dp.signModel.service.PADSignSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 签章管理页面的controller
 */
@Controller
@RequestMapping("/signModel/mission/plan/")
public class PADSignSyncController {
    @Resource
    PADSignSyncService padSignSyncService;

    @RequestMapping("DeleteSyncToSignModel")
    public void DeleteSyncToSignModel(HttpServletRequest request, HttpServletResponse response){
        String signModelIds = RequestUtil.getString(request, "signModelIds");
        padSignSyncService.SyncSignModelInfo(signModelIds);
    }
}
