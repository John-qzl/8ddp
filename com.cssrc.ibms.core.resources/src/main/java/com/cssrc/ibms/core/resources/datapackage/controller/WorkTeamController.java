package com.cssrc.ibms.core.resources.datapackage.controller;

import com.cssrc.ibms.core.resources.datapackage.service.WorkTeamService;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User :  sgl.
 * Date : 2018/6/27.
 * Time : 15:34.
 */
@Controller
@RequestMapping("/dataPackage/workTeam/")
public class WorkTeamController extends BaseController {
    @Resource
    private WorkTeamService workTeamService;


        /**
         * @Author  shenguoliang
         * @Description: 返回使用中的工作队，删除未使用的工作队
         * @Params [request, response]
         * @Date 2018/6/28 8:34
         * @Return void
         */
    @RequestMapping({ "checkWorkTeamIfUsed" })
    public void checkWorkTeamIfUsed(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResultMessage message = null;
        List<String> usedteamList = new ArrayList<>();
        try {
            String ids = RequestUtil.getString(request, "ID");
            Long sssjb = RequestUtil.getLong(request, "sssjb");

            usedteamList = workTeamService.checkWorkTeamIfUsed(sssjb, ids);
            message = new ResultMessage(ResultMessage.Success, "true");
            message.addData("teamList",usedteamList);

        } catch (Exception e) {
            e.printStackTrace();
            message = new ResultMessage(ResultMessage.Fail, e.getMessage());
        }
        writeResultMessage(response.getWriter(), message);
    }

}
