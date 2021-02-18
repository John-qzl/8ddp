package com.cssrc.ibms.dp.util;


import java.io.File;

import javax.annotation.Resource;

import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.system.service.SysParameterService;
/**
 * @description HTML转pdf插件使用工具类
 * @author fuyong
 * @date 2020年9月12日 上午9:20:08
 * @version V1.0
 */
public class HtmlToPdf {
    //wkhtmltopdf在系统中的路径
    private static final String toPdfTool = AppConfigUtil.get("pluginproperties","wkhtmltopdf.path");;

    /**
     * html转pdf
     * @param srcPath html路径，可以是硬盘上的路径，也可以是网络路径
     * @param destPath pdf保存路径
     * @return 转换成功返回true
     */
    public static boolean convert(String srcPath, String destPath){
        File srcfile = new File(srcPath);
        File destPathfile = new File(destPath);
        File parent = destPathfile.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if(!parent.exists()){
            parent.mkdirs();
        }
        if(!srcfile.exists()) {
        	return false;
        }
        StringBuilder cmd = new StringBuilder();
        cmd.append(toPdfTool);
        cmd.append(" ");
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(" --enable-local-file-access");
        cmd.append(" --allow");
        cmd.append(" ");
        cmd.append(destPath);


        boolean result = true;
        try{                                                                        //D:\tool\wkhtmltopdf\bin\wkhtmltopdf.exe D:\test.html d:\csdn.pdf
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
        }catch(Exception e){
            result = false;
            e.printStackTrace();
        }

        return result;
    }
}