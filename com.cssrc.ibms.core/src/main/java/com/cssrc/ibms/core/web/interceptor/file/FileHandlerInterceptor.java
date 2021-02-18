package com.cssrc.ibms.core.web.interceptor.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.FileResult;
import com.cssrc.ibms.core.util.string.StringUtil;

public class FileHandlerInterceptor extends HandlerInterceptorAdapter {

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception e)
			throws Exception {
		System.out.println("afterCompletion");
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj, ModelAndView  mv)
			throws Exception {
		System.out.println("postHandle");
	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj) throws Exception {
		if(request  instanceof MultipartHttpServletRequest){
			MultipartHttpServletRequest mrequest=(MultipartHttpServletRequest)request;
			Map<String, MultipartFile> files = mrequest.getFileMap();
			Iterator<MultipartFile> it = files.values().iterator();
			String fastDFS=PropertyUtil.getByAlias("IS_FILE_FASTDFS");
			//附件保存本地根目录
			String attachPath=AppUtil.getAttachPath();
			//文件结果集合
			List<FileResult> resultFile = new ArrayList<FileResult>();

			while (it.hasNext()) {
				MultipartFile f = it.next();
				//文件相对路径
				String oriFileName = f.getOriginalFilename();
				//文件扩展名
				String extName = FileOperator.getSuffix(oriFileName);
				//文件最终存储目录
				String storePath="";
				Integer storeType=0;
				if(StringUtil.isNotEmpty(fastDFS) && fastDFS.equals("1")){
					storePath= FastDFSFileOperator.uploadFile(f,extName);
					storeType=1;
				}else{
					String relativeFullPath = FileOperator.generateFilenameNoSemicolon(oriFileName);
					storePath=createFilePath(attachPath + File.separator , relativeFullPath);
					FileOperator.writeByte(storePath, f.getBytes()); 
					storeType=0;
				}
				FileResult rileResult=new FileResult(oriFileName,storePath,extName,f.getBytes(),storeType);
				resultFile.add(rileResult);
			}
			mrequest.getFileMap().clear();
			mrequest.setAttribute(FileResult.FILES, resultFile);
		}
		return true;
	}
	/**
	 * 创建文件目录
	 * @param tempPath
	 * @param fileName  文件名称
	 * @return 文件的完整目录
	 */
	private String createFilePath(String tempPath, String fileName) {
		File one = new File(tempPath);
		if (!one.exists()) {
			one.mkdirs();
		}
		return one.getPath() + File.separator + fileName;
	}
}
