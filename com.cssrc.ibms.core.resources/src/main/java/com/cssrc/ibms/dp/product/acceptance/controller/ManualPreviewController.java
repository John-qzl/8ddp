package com.cssrc.ibms.dp.product.acceptance.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.product.acceptance.service.ManualPreviewService;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SysParameterService;

@Controller
@RequestMapping("/product/manual/file/")
public class ManualPreviewController extends BaseController{
	@Resource
	ManualPreviewService manualPreviewService;
	@Resource
	SysParameterService sysParameterService;
	@Resource
	SysFileDao sysFileDao;
	
	
	@RequestMapping("getFileId") 
	@ResponseBody
	public String getFileId(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		return manualPreviewService.getFileId(id);
	}
	 @RequestMapping("read")
	 @ResponseBody
	 public void readFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		 InputStream in = null;
	     OutputStream out = null;
	     String id = request.getParameter("id");
	     id=manualPreviewService.getFileId(id);
	     String filePath=sysParameterService.getOneParameter("UploadFileFolder");
	     SysFile sysFile=sysFileDao.getById(Long.valueOf(id));
	     filePath=filePath+"\\"+sysFile.getFilepath();
	     
	     if (filePath != null) {
             try {
				in = new FileInputStream(filePath);
				 response.setContentType("application/pdf");
		         out = response.getOutputStream();
		         byte[] b = new byte[1024];
		         int len = 0;
		         while ((len = in.read(b)) != -1) {
		             out.write(b);
		         }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
	            if (in != null) {
	                in.close();
	            }
	            if (out != null) {
	                out.close();
	            }
	        }
         }
        
	 }
}
