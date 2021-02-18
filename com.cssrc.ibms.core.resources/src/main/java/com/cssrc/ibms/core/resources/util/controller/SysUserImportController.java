package com.cssrc.ibms.core.resources.util.controller;

import com.cssrc.ibms.core.resources.util.service.SysUserImportService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用户管理-人员导入功能
 */
@Controller
@RequestMapping({"/sysUser/import"})
@Action(ownermodel = SysAuditModelType.USER_MANAGEMENT)
public class SysUserImportController extends BaseController {

    @Resource
    private SysUserImportService service;

    @Resource
    protected JdbcTemplate jdbcTemplate;

    /**
     * Description : 打开导入用户界面
     * Author : XX
     * Date : 2019年7月15日 上午10:58:20
     * Return : ModelAndView
     */
    @RequestMapping("importUsers")
    @Action(description = "导入用户信息", detail = "导入用户信息")
    public ModelAndView importUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView("/oa/system/sysUserImport.jsp");
        return mv;
    }

    /**
     * Description : 导入用户
     * Author : XX
     * Date : 2019年7月15日 上午10:58:36
     * Return : void
     */
    @RequestMapping("importSysUser")
    @Action(description = "导入用户保存", detail = "导入用户保存")
    public void importSysUser(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
        MultipartFile file = request.getFile("file");
        ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, "导入成功");
        try {
            resultMessage = service.importSysUser(file.getInputStream());
        } catch (Exception e) {
            resultMessage = new ResultMessage(ResultMessage.Fail, "导入失败：\r\n"+e.getMessage());
            System.err.println("--------------------by SGL-----------------导入失败的错误信息为:"
                    +e.getCause().getMessage()+ "," + "当前输出的所在类为:SysUserImportController.importSysUser()");
            writeResultMessage(response.getWriter(), resultMessage);
        }
        writeResultMessage(response.getWriter(), resultMessage);
    }

    /**
     * excel导入团队人员信息
     */
    @RequestMapping({"importModuleTeamFromExcel"})
    public void importModuleTeamFromExcel(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {
        MultipartFile multipartFile=request.getFile("file");
        String moduleId=request.getParameter("moduleId");
        String msg=service.importMuduleTeam(multipartFile.getInputStream(),moduleId);
        PrintWriter writer = response.getWriter();
        writer.println(msg);
        //	writer.println("{\"success\":\"true\"}");
    }
}
