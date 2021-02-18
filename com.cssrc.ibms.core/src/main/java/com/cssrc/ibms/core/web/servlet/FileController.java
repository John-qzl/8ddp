package com.cssrc.ibms.core.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

@WebServlet(name = "fileController", urlPatterns = "/file/read")
public class FileController extends HttpServlet
{
    private static final long serialVersionUID = -4217804414002363018L;
    
    public static String urlPatterns = "/file/read";
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String ext=request.getParameter("ext");
        if(StringUtil.isEmpty(ext)){
            try
            {
                this.readFileBypath(request,response);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }else{
            String attachPath = AppUtil.getAttachPath();
            String filePath = request.getParameter("opath");
            String fullPath = StringUtil.trimSufffix(attachPath, File.separator) + File.separator + filePath.replace("/", File.separator);
            File file=new File(fullPath);
            String fileName=file.getName();
            FileOperator.downLoadFile(request, response, fullPath, fileName);
        }
    }
    
    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public void readFileBypath(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String opath = request.getParameter("opath");
        writeFileByte(response, opath);
    }
    
    private void writeFileByte(HttpServletResponse response, String opath)
        throws IOException
    {
        OutputStream outp = response.getOutputStream();
        byte[] b =null;
        if(opath.indexOf(SysConfConstant.UploadFileFolder)>=0){
        	b = FileUtil.readByte(opath);
        }else{
        	b = FileUtil.readByte(SysConfConstant.UploadFileFolder + File.separator + opath);
        }
        FileInputStream in = null;
        try
        {
            outp.write(b);
            outp.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (in != null)
            {
                in.close();
                in = null;
            }
            if (outp != null)
            {
                outp.close();
                outp = null;
                response.flushBuffer();
            }
        }
    }
}
