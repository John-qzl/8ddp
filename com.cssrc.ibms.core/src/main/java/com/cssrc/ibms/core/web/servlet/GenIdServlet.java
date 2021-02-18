package com.cssrc.ibms.core.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;

@WebServlet(name = "genIdServlet", urlPatterns = "/genId")
public class GenIdServlet extends HttpServlet
{
    private static final long serialVersionUID = -7370959264102009921L;
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        try
        {
            response.getWriter().println(UniqueIdUtil.genId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
