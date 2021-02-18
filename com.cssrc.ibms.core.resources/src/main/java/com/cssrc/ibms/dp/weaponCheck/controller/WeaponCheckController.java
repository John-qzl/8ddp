package com.cssrc.ibms.dp.weaponCheck.controller;

import com.cssrc.ibms.core.resources.mission.service.TestPlanService;
import com.cssrc.ibms.dp.weaponCheck.service.WeaponCheckService;
import net.sf.json.JSONArray;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 武器所检策划controller
 * @author zmz
 *
 */
@Controller
@RequestMapping("/weapon/check/plan/")
public class WeaponCheckController {
    @Resource
    private WeaponCheckService service;
    @Resource
    private TestPlanService testPlanService;

    /**
     * 获取下一个策划编号除代号外的剩余部分
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("getNextWeaponCheckPlanNumber")
    public void getNextWeaponCheckPlanNumber(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String xhId=request.getParameter("xhId");
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
        String time=dateFormat.format(calendar.getTime());
        String nextNumber="[-WQSJ-"+time+"-"+service.generatorReportNumber(xhId)+"]";
        nextNumber= JSONArray.fromObject(nextNumber).toString();
        response.getWriter().print(nextNumber);
    }

    /**
     * excel导入组员信息
     */
    @RequestMapping({"importTeamInfoFromExcelForWeaponCheck"})
    public void importTeamInfo(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {
        MultipartFile multipartFile=request.getFile("file");
        String missionId=request.getParameter("missionId");
        String msg=testPlanService.importTeamInfo(multipartFile.getInputStream(),missionId);
        PrintWriter writer = response.getWriter();
        writer.println(msg);
        //	writer.println("{\"success\":\"true\"}");
    }

}
