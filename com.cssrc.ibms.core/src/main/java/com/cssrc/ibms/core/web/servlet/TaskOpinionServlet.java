package com.cssrc.ibms.core.web.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.api.activity.intf.ITaskOpinionService;
import com.cssrc.ibms.core.util.appconf.AppUtil;

@WebServlet(name="taskOpinionServlet",urlPatterns="/getOpinion")
public class TaskOpinionServlet extends HttpServlet
{
    private static final long serialVersionUID = -971105111688029231L;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> params=new HashMap<String,String>();
        Enumeration<String> enumeration=request.getParameterNames();
        while(enumeration.hasMoreElements()){
            String name=enumeration.nextElement();
            params.put(name, request.getParameter(name));
        }
        ITaskOpinionService taskOpinionService= AppUtil.getBean(ITaskOpinionService.class);
        String  opinion=taskOpinionService.getOpinion(params);
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(opinion);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }


}
