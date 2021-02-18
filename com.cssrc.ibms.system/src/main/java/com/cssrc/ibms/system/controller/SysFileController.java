package com.cssrc.ibms.system.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.GlobalType;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.model.SysFileFolder;
import com.cssrc.ibms.system.model.SysParameter;
import com.cssrc.ibms.system.service.DictionaryService;
import com.cssrc.ibms.system.service.GlobalTypeService;
import com.cssrc.ibms.system.service.SolrService;
import com.cssrc.ibms.system.service.SysFileFolderService;
import com.cssrc.ibms.system.service.SysFileService;
import com.cssrc.ibms.system.service.SysParameterService;
import com.cssrc.ibms.system.service.SysTypeKeyService;
import com.cssrc.ibms.system.util.SysFileUtil;

/**
 * 附件 控制器类.
 *
 * <p>
 * detailed comment
 * </p>
 * 
 * @author [创建人] zhulongchao <br/>
 *         [创建时间] 2015-3-19 下午01:38:39 <br/>
 *         [修改时间] 2015-3-19 下午01:38:39
 * @see
 */
@Controller
@RequestMapping("/oa/system/sysFile")
@Action(ownermodel = SysAuditModelType.FILE_MANAGEMENT)
public class SysFileController extends BaseController {

	private Log logger = LogFactory.getLog(SysFileController.class);
	@Resource
	private SysFileService sysFileService;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private GlobalTypeService globalTypeService;
	@Resource
	private SysFileFolderService sysFileFolderService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private SysParameterService sysParameterService;
	@Resource
	private SysTypeKeyService sysTypeKeyService;
	@Resource
	private SolrService solrService;
	private String saveType = PropertyUtil.getSaveType();
	
	// 附件保存路径
	private String attachPath = AppUtil.getAttachPath();

	/**
	 * 表单新增后，更新附件表中的dataId
	 **/
	@RequestMapping("updateFileRecod")
	public void updateFileRecod(HttpServletRequest request,HttpServletResponse response) throws IOException {
		 Long tableId = RequestUtil.getLong(request,"tableId");
		 Long userId  = UserContextUtil.getCurrentUserId();
		 Long real_dataId = RequestUtil.getLong(request,"real_dataId");
		 Long virtual_dataId = RequestUtil.getLong(request,"virtual_dataId");
		 Map map = new HashMap();
		 map.put("tableId", tableId);
		 map.put("userId", userId);
		 map.put("real_dataId", real_dataId);
		 map.put("virtual_dataId", virtual_dataId);
		 //sysFileServiceExt.updateDataId(map);
	}
	/**
	 * 新增表单中，添加了附件，但未保存，进行删除
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("delFormFileData")
	public void delFormFileData(HttpServletRequest request,HttpServletResponse response) throws IOException {
		 Long tableId = RequestUtil.getLong(request,"tableId");
		 Long userId  = UserContextUtil.getCurrentUserId();
		 Long virtual_dataId = RequestUtil.getLong(request,"virtual_dataId");
		 Map map = new HashMap();
		 map.put("tableId", tableId);
		 map.put("userId", userId);
		 map.put("virtual_dataId", virtual_dataId);
		 //sysFileServiceExt.delByDataId(map);
	}
	/**
	 * 附件上传操作
	 **/
	@RequestMapping("fileUpload")
	@Action(description = "文件上传", exectype = SysAuditExecType.FILEUPLOAD_TYPE, detail = "上传文件:"
			+ "<#list sysFiles as item>" + "${item.filename}.${item.ext}" + "</#list>")
	public void fileUpload(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//返回上传结果
		String returnMsg="";
		PrintWriter writer = response.getWriter();
		try {
			long userId = UserContextUtil.getCurrentUserId(); // 获取当前用户的id
			ISysUser appUser = null;
			if (userId > 0) {
				appUser = sysUserService.getById(userId);
			}		
			String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
			if(StringUtil.isNotEmpty(rpcrefname)){
				//采用IOC方式，根据RPC远程过程调用服务调用数据
				CommonService commonService = (CommonService)  AppUtil.getContext().getBean(rpcrefname);
				returnMsg=commonService.uploadAttach(request,response,appUser,userId);
			}else{
				returnMsg=sysFileService.uploadAttach(request,response,appUser,userId);
			}
			writer.println(returnMsg);
		} catch (Exception e) { 
			e.printStackTrace();
			writer.println("{\"success\":\"false\"}");
		}
	}
	
	/**
	 * 附件删除操作
	 * **/
	@RequestMapping("deleteFile")
	@Action(description = "删除附件", execOrder = ActionExecOrder.BEFORE, exectype = SysAuditExecType.FILEDELETE_TYPE,
	detail = "删除文件如下：${sysAuditLinkService.getSysFileNameArr(String.valueOf(ids))}")
	public void deleteFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String ids = RequestUtil.getString(request, "ids");
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			SysFile sysFile = sysFileService.getById(Long.valueOf(id));
			String fileName = sysFile.getFilename();
			PrintWriter writer = response.getWriter();
			try {
				sysFileService.delFiles(sysFile, attachPath);
				writer.println("{\"success\":\"true\",\"fileId\":\""
						+ sysFile.getFileId() + "\",\"fileName\":\"" + fileName
						+ "\"}");
			} catch (Exception e) {
				e.printStackTrace();
				writer.println("{\"success\":\"false\",\"fileId\":\""
						+ sysFile.getFileId() + "\",\"fileName\":\"" + fileName
						+ "\"}");
			}

		}
	}

	@RequestMapping({ "del" })
	@Action(description = "删除附件", execOrder = ActionExecOrder.BEFORE, exectype = SysAuditExecType.FILEDELETE_TYPE,
	detail = "删除文件如下：${sysAuditLinkService.getSysFileNameArr(String.valueOf(fileId))}")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage message = null;
		String returnUrl = RequestUtil.getPrePage(request);
		Long[] lAryId = RequestUtil.getLongAryByStr(request, "fileId");
		Long isDialog = RequestUtil.getLong(request, "isDialog",0);
		try {
			if (!this.saveType.contains("database")) {
				for (Long id : lAryId) {
					SysFile sysFile = (SysFile) this.sysFileService.getById(id);
					String filePath = sysFile.getFilepath();
					//判断附件是本地附件还是服务器存储附件
					if(filePath.startsWith("group")){
						FastDFSFileOperator.deleteFile(filePath);
					}else{
						if (StringUtil.isEmpty(attachPath)) {
							filePath = AppUtil.getRealPath(filePath);
						}

						FileUtil.deleteFile(attachPath + File.separator + filePath);
					}
				}
			}

			this.sysFileService.delByIds(lAryId);
			//删除文件索引
			solrService.deleteFileIndex(lAryId);
			
			message = new ResultMessage(1, "删除附件成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除附件失败");
		}
		addMessage(message, request);
		if(isDialog==1){
			response.getWriter().write("OK");
		} else {
			response.sendRedirect(returnUrl);//tab页可重定向
		}
		
	}
	@RequestMapping("downLoadTempFile")
	@Action(description = "附件下载", exectype = SysAuditExecType.FILEDOWNLOAD_TYPE, detail = "下载临时附件")
	public void downLoadTempFile(HttpServletRequest request,HttpServletResponse response) throws IOException,Exception {
		String tempFilePath = RequestUtil.getString(request, "tempFilePath");
		String fileName = FileOperator.getFileName(tempFilePath);
		FileOperator.downLoadFile(request, response, tempFilePath, fileName);	
		FileOperator.deleteFile(tempFilePath);
	}
	/**
	 * 附件下载
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("download")
	@Action(description = "附件下载", exectype = SysAuditExecType.FILEDOWNLOAD_TYPE, detail = "下载附件如下：${SysAuditLinkService.getSysFileLink(String.valueOf(fileId))}")
	public void downloadFile(HttpServletRequest request,HttpServletResponse response) throws IOException,Exception {
		String fileId = RequestUtil.getString(request, "fileId");
		if ("".equals(fileId))
			return;
		String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
		if(StringUtil.isNotEmpty(rpcrefname)){
			//采用IOC方式，根据rpc远程接口调用数据
			CommonService commonService = (CommonService) AppUtil.getContext().getBean(rpcrefname);
			commonService.downAttach(request,response,fileId);
		}else{
		//当不是rpc远程接口  或者  远程调用超时失败，从本地调用
			sysFileService.downAttach(request, response, fileId);
		}
	}
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping("downLoadFileByType")
	@Action(description = "附件下载", exectype = SysAuditExecType.FILEDOWNLOAD_TYPE, detail = "根据文件类别进行文件下载")
	public void downLoadFileByType(HttpServletRequest request,HttpServletResponse response) throws IOException,Exception{
		String paramJson=RequestUtil.getString(request, "paramJson");
        Long typeId = Long.valueOf(RequestUtil.getLong(request, "typeId")); 
        if("".equals(paramJson)||typeId.equals(paramJson)){
        	return;
        }
        String[] path = sysFileService.downAttachByType(request,paramJson,typeId);
        // 下载文件到本地，文件源地址为 path[0]，下载后名称为 path[1]
 		FileOperator.downLoadFile(request, response, path[0], path[1]);
 		// 删除zip文件
 		File zipFile = new File(path[0]);
 		if (zipFile.exists())
 			zipFile.delete();
	}
	/**
	 * 分享文件
	 * */
	@RequestMapping("share")
	@Action(description = "分享文件", exectype = SysAuditExecType.FILESHRAE_TYPE, detail = "文件${SysAuditLinkService.getSysFileLink(String.valueOf(fileId))}分享成功！")
	public void shareFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Long id = RequestUtil.getLong(request, "fileId");
		Long[] ids = { id };
		sysFileService.saveShareFile(ids);
		ResultMessage resultMessage = new ResultMessage(ResultMessage.Success,
				"分享成功");
		writeResultMessage(response.getWriter(), resultMessage);
	}

	/**
	 * 关闭分享
	 * */
	@RequestMapping("closeShare")
	@Action(description = "分享文件", exectype = SysAuditExecType.FILESHRAE_TYPE, detail = "文件${SysAuditLinkService.getSysFileLink(String.valueOf(fileId))}关闭分享成功！")
	public void closeShare(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Long[] ids = RequestUtil.getLongAry(request, "fileId");
		sysFileService.saveCloseShare(ids);
		ResultMessage resultMessage = new ResultMessage(ResultMessage.Success,
				"关闭分享成功");
		writeResultMessage(response.getWriter(), resultMessage);
	}

	/**
	 * 详情
	 * */
	@RequestMapping("get")
	@ResponseBody
	public Map<String, Object> get(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Long filedId = RequestUtil.getLong(request, "fileId");
		SysFile sysfile = sysFileService.getById(filedId);
		map.put("success", "true");
		map.put("data", sysfile);
		return map;
	}


	@RequestMapping({ "getUserData" })
	public void getUserData(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		try {
			ISysUser user = (ISysUser) UserContextUtil.getCurrentUser();

			/*
			 * writer.println("{\"success\":\"true\",\"user\":{\"id\":\"" +
			 * user.getUserId() + "\",\"name\":\"" + user.getFullname() +
			 * "\",\"groupId\":\"" +
			 * (null!=user.getSysOrg()?user.getSysOrg().getOrgId():"") +
			 * "\",\"groupName\":\"" +
			 * (null!=user.getSysOrg()?user.getSysOrg().getOrgName():"") +
			 * "\" } }");
			 */
			writer.println("{\"success\":\"true\",\"user\":{\"id\":\""
					+ user.getUserId() + "\",\"name\":\"" + user.getFullname()
					+ "\",\"groupId\":\"" + user.getOrgId()
					+ "\",\"groupName\":\"" + user.getOrgName() + "\" } }");
		} catch (Exception e) {
			this.logger.warn(e.getMessage());
			writer.println("{\"success\":\"false\",\"user\":\"\"}");
		} finally {
			writer.close();
		}
	}

	/**
	 * 根据ID获取附件
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/file_{fileId}")
	@Action(description = "附件下载", exectype = SysAuditExecType.FILEDOWNLOAD_TYPE, detail = "下载附件如下：${SysAuditLinkService.getSysFileLink(String.valueOf(fileId))}")
	public void getById(@PathVariable("fileId") Long fileId,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		getFile(request, response, fileId);
	}

	/**
	 * 
	 * @param fileId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getFileById")
	@Action(description = "附件下载", exectype = SysAuditExecType.FILEDOWNLOAD_TYPE, detail = "下载附件如下：${SysAuditLinkService.getSysFileLink(String.valueOf(fileId))}")
	public void getFileById(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Long fileId = RequestUtil.getLong(request, "fileId");
		SysFile sysFile = sysFileService.getById(fileId);
		Boolean isNoGroup = sysFile.getFilepath().startsWith("group");//判断是否分布式文件 
		if(isNoGroup){
			String destFilePath = sysFileService.getDecodeFilePath(sysFile.getFilepath(), sysFile.getFilename(), sysFile.getIsEncrypt(),true);
			if (!"".equals(destFilePath)) {
				// 读取文件并输出
				OutputStream outp = response.getOutputStream();
				File file = new File(destFilePath);
				if (file.exists()) {
					response.setContentLength((int) file.length());
					FileInputStream in = null;
					try {
						in = new FileInputStream(destFilePath);
						byte[] b = new byte[1024];
						int i = 0;
						while ((i = in.read(b)) > 0) {
							outp.write(b, 0, i);
						}
						outp.flush();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (in != null) {
							in.close();
							in = null;
						}
						if (outp != null) {
							outp.close();
							outp = null;
							response.flushBuffer();
						}
						// 删除解密文件
						File destFile = new File(destFilePath);
						if (destFile.exists())
							destFile.delete();
					}
				} else {
					response.getWriter()
							.print("<font style='font-weight:800;color:#696969;font-size:14px;text-align:center;'>The file is not exists!</font>");
				}
				if (outp != null) {
					outp.close();
					outp = null;
					response.flushBuffer();
				}
			}else{
				byte[] content = FastDFSFileOperator.getFileByte(sysFile.getFilepath());
				response.getOutputStream().write(content);
				response.flushBuffer();
			}
		} else {
			getFile(request, response, fileId);
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("pictureShow")
	@Action(description = "文件预览",detail = "图片展示：${SysAuditLinkService.getSysFileLink(String.valueOf(fileId))}", exectype = SysAuditExecType.FILEPREVIEW_TYPE)
	public ModelAndView pictureShow(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView result = getAutoView();
		String id = RequestUtil.getString(request, "id");
		String title = RequestUtil.getString(request, "title");
		String type = RequestUtil.getString(request, "type");
		title = URLDecoder.decode(title, "utf-8");
		result.addObject("id", id);
		result.addObject("title", title);
		result.addObject("type", type);
		return result;
	}

	/**
	 * 根据ID获取照片(用于用户管理)
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/file_id{fileId}")
	@Action(description = "附件下载", exectype = SysAuditExecType.FILEDOWNLOAD_TYPE, detail = "下载照片如下：${SysAuditLinkService.getSysFileLink(String.valueOf(fileId))}")
	public void getPictureById(@PathVariable("fileId") Long fileId,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		getFile(request, response, fileId);
	}
	/**
	 * 
	   获取图片流
	 * getFile
	 * @param   name    
	 * @param  @return    设定文件    
	 * @return String    DOM对象    
	 * @Exception 异常对象    
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public void getFile(HttpServletRequest request,
			HttpServletResponse response, Long fileId) throws IOException {
		response.reset();
		String vers = request.getHeader("USER-AGENT");
		SysFile sysFile = sysFileService.getById(fileId);

		if (sysFile == null){
		    response.getWriter().print("<font style='font-weight:800;color:#696969;font-size:14px;text-align:center;'>The file is not exists!</font>");
		}
		
		String contextType = SysFileUtil.getContextType(sysFile.getExt(), false);
		OutputStream outp = response.getOutputStream();
		response.setContentType(contextType);
		response.setCharacterEncoding("utf-8");
		String fileName = sysFile.getFilename() + "." + sysFile.getExt();
		String isDownload = request.getParameter("download");
		if (vers.indexOf("Chrome") != -1 && vers.indexOf("Mobile") != -1) {
			fileName = fileName.toString();
		} else {
			fileName = StringUtil.encodingString(fileName, "GB2312",
					"ISO-8859-1");
		}
		if ("application/octet-stream".equals(contextType)
				|| StringUtils.isNotBlank(isDownload)) {
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
		} else {
			response.addHeader("Content-Disposition", "filename=" + fileName);
		}
		response.addHeader("Content-Transfer-Encoding", "binary");
		
		 // by --yangbo 加入database的文件获取
		if (this.saveType.contains("database")) {
			byte[] bytes = (byte[]) null;
			bytes = sysFile.getFileBlob();
			response.setContentLength(bytes.length);
			outp.write(bytes);
		} else {
			String filePath = StringUtil.trimSufffix(attachPath, File.separator);
			String realPath = "";
			realPath = filePath + File.separator + sysFile.getFilepath().replace("/", File.separator);
			
			// by weilei:对加密文件进行解密处理
			String destFilePath = sysFileService.getDecodeFilePath(realPath, fileName, sysFile.getIsEncrypt(),false);
			if (!"".equals(destFilePath)) {
				realPath = destFilePath;
			}
			// 读取文件并输出
			File file = new File(realPath);
			if (file.exists()) {
				response.setContentLength((int) file.length());
				FileInputStream in = null;
				try {
					in = new FileInputStream(realPath);
					byte[] b = new byte[1024*10];
					int i = 0;
					while ((i = in.read(b)) > 0) {
						outp.write(b, 0, i);
					}
					outp.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						in.close();
						in = null;
					}
					if (outp != null) {
						outp.close();
						outp = null;
						response.flushBuffer();
					}
					// 删除解密文件
					File destFile = new File(destFilePath);
					if (destFile.exists())
						destFile.delete();
				}
			} else {
				response.getWriter()
						.print("<font style='font-weight:800;color:#696969;font-size:14px;text-align:center;'>The file is not exists!</font>");
			}

			if (outp != null) {
				outp.close();
				outp = null;
				response.flushBuffer();
			}
		}

	}
	/**
	 * 附件列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("selector")
	@Action(description = "附件查询", exectype = SysAuditExecType.SELECT_TYPE, detail = "附件查询")
	public ModelAndView selector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView result = getAutoView();
		Map<String, Object> queryParams = RequestUtil.getQueryParams(request);
		QueryFilter filter = new QueryFilter(request, "sysFileItem");
		Enumeration<String> paramEnu = request.getParameterNames();
		while (paramEnu.hasMoreElements()) {
			String paramName = (String) paramEnu.nextElement();
			if (paramName.startsWith("Q_")) {
				String paramValue = request.getParameter(paramName);
				filter.addFilterForH(paramName, paramValue);
			}
		}
		List<SysFile> lis = new ArrayList<SysFile>();
		Long folderId = RequestUtil.getLong(request, "folderId");
		SysFileFolder sysFileFolder = null;
		// 默认获取用户所有文件夹
		if (null == folderId || folderId.equals(0L)) {
			Long userId = UserContextUtil.getCurrentUserId();
			sysFileFolder = sysFileFolderService.getRootFolderByUserId(userId);
		} else {
			sysFileFolder = sysFileFolderService.getById(folderId);
		}
		Boolean isRootFolder = false;
		if (sysFileFolder != null) {
			// 根节点为true
			isRootFolder = sysFileFolder.getDepth().equals(1L) ? true : false;
			Boolean isSharedFolder = null != sysFileFolder.getSharedNode() ? sysFileFolder
					.getSharedNode() : false;
			String folderPath = sysFileFolder.getPath();
			lis = sysFileService.getSysFileByFolder(
					sysFileFolder.getCreatorId(), isRootFolder, isSharedFolder,
					folderPath, filter);
		}

		filter.setForWeb("sysFileItem");
		result.addObject("sysFileList", lis);
		int isSingle = RequestUtil.getInt(request, "isSingle", 0);
		result.addObject("isSingle", isSingle);
		result.addObject("param", queryParams);
		return result;
	}

	@RequestMapping("loadByIds")
	@ResponseBody
	public Map<String, Object> loadByIds(HttpServletRequest request) {
		Long[] ids = RequestUtil.getLongAryByStr(request, "ids");
		List<SysFile> files = new ArrayList<SysFile>();
		if (null != ids) {
			for (Long id : ids) {
				SysFile file = sysFileService.getById(id);
				files.add(file);
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "true");
		map.put("fileAttachs", files);
		return map;
	}

	@RequestMapping("detail")
	@Action(description = "查看文件信息",detail = "查看文件${SysAuditLinkService.getSysFileLink(String.valueOf(fileId))}信息", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView detail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView result = getAutoView();
		Long fileId = RequestUtil.getLong(request, "fileId");
		SysFile file = sysFileService.getById(fileId);
		result.addObject("fileAttach", file);
		return result;
	}

	/**
	 * 取得附件明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getJson")
	@ResponseBody
	public Map<String, Object> getJson(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		int status = 1;
		try {
			long id = RequestUtil.getLong(request, "fileId");
			SysFile sysFile = sysFileService.getById(id);
			map.put("sysFile", sysFile);
		} catch (Exception e) {
			status = -1;
		}
		map.put("status", status);
		return map;
	}

	/**
	 * 根据文件数据Ids获取文件信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping("list")
	@Action(description = "查看文件列表",detail = "查看文件列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) {

		ModelAndView modelView = null;
		try {
			modelView = getAutoView();
			Map<String,Object> params = RequestUtil.getParameterValueMap(request);
			QueryFilter filter = new QueryFilter(request, "sysFileItem");
			List<SysFile> allFileList = sysFileService.getSysFileList(request);
			
			// 获取密级参数数据
			List<SysParameter> spDatas = sysParameterService
					.getByParamName(ISysFile.IS_DISPLAY_SECURITY);
			//是否显示密级列的参数
            Boolean isShowSecurity = false;
			//获取当前用户密级权限
			String userSecurity = UserContextUtil.getCurrentUserSecurity();
			if (spDatas.size() > 0
					&& "1".equals(spDatas.get(0).getParamvalue())) {
				isShowSecurity = true;
				modelView.addObject("isShowSecurity", isShowSecurity);
				modelView.addObject("securityChineseMap", ISysFile.SECURITY_CHINESE_MAP);
			}
			modelView.addObject("sysFileList", allFileList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelView;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("isExist")
	public void isExist(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		try {
			JSONObject result = new JSONObject();
			String fileId = RequestUtil.getString(request, "fileId");
			SysFile sysFile = sysFileService.getById(Long.valueOf(fileId));
			String filePath = sysFile.getFilepath();
			String fileType = sysFile.getExt();
			//检查本地或服务器上文件是否存在
			if(filePath.startsWith("group")){
				Boolean yesOrNo = FastDFSFileOperator.isExist(filePath);
				if(yesOrNo){
					result.put("filePath", filePath);
					result.put("fileType", fileType);
				}else{
					result.put("filePath", "none");
					result.put("fileType", fileType);
				}
			}else{
				filePath = attachPath + File.separator + filePath;
				File file = new File(filePath);
				if(file.exists()){
					result.put("filePath", filePath);
					result.put("fileType", fileType);
				}else{
					result.put("filePath", "none");
					result.put("fileType", fileType);
				}
			}
			response.getWriter().write(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件预览
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("preview")
	@Action(description = "文件预览",detail = "预览文件：${SysAuditLinkService.getSysFileNameArr(String.valueOf(fileId))}", exectype = SysAuditExecType.FILEPREVIEW_TYPE)
	public ModelAndView preview(HttpServletRequest request,
			HttpServletResponse response) {

		ModelAndView modelView = null;
		String contextPath =request.getContextPath();
		try {
			String fileId = RequestUtil.getString(request, "fileId");
			modelView = new ModelAndView(RequestUtil.getString(request, "url"));
			SysFile sysFile = sysFileService.getById(Long.valueOf(fileId));
			String filePath = sysFile.getFilepath();
			String fileType = sysFile.getExt();
			String fileName = sysFile.getFilename() + "." + sysFile.getExt();

			Boolean isNoGroup = filePath.startsWith("group");//判断是否分布式文件
			String interview_server = FastDFSFileOperator.getInterviewServer(); //分布式请求url端口
			if(isNoGroup){
				if(sysFile.getIsEncrypt() != 1L){
					filePath=interview_server+"/"+sysFile.getFilepath();
				}
			}else{
				filePath = attachPath + File.separator + filePath;
			}
			// by weilei:对加密文件进行解密处理
			String destFilePath = sysFileService.getDecodeFilePath(filePath, fileName, sysFile.getIsEncrypt(),isNoGroup);
			if (!"".equals(destFilePath)) {
				filePath = destFilePath;
				fileName = FileOperator.getFileNameByPath(filePath);
			}
			if ("txt".equals(fileType.toLowerCase())) {
				//分布式读取txt，无法换行
				String txtConcent = "";
				if(isNoGroup&&sysFile.getIsEncrypt() != 1L){
					byte[] cont = FastDFSFileOperator.getFileByte(sysFile.getFilepath());
					String text=new String(cont,"GBK");
					txtConcent="<textarea name = \"textarea\" >" + text + "</textarea>";
				}else{
					txtConcent = parseBinaryFile(filePath);
					if (!"".equals(destFilePath)) {
						File destFile = new File(destFilePath);
						if (destFile.exists())
							destFile.delete();
					}
				}
				modelView.addObject("textConcent", txtConcent);
				
			}
			if ("png,bmp,gif,jpg".contains(fileType.toLowerCase())) {
				
				String imgSrc = contextPath+"/oa/system/sysFile/getFileById.do?fileId="+fileId;
				modelView.addObject("imgSrc", imgSrc);
			}
			if("mp4,webm,ogv".contains(fileType.toLowerCase())){ //by YangBo 视频预览 <source src=\"http://127.0.0.1:8888/dataFile/001.mp4\" type='video/mp4' />
                String source="";
                if(isNoGroup){
                	if(sysFile.getIsEncrypt() != 1L){//未加密的分布式文件路径
                		source ="<source src=\""+interview_server+"/"+sysFile.getFilepath();
                	}else{//已加密的分布式文件
                		String localAddr=request.getLocalAddr();
                        String localPort=String.valueOf(request.getLocalPort());
                        filePath = filePath.substring(attachPath.length()+1, filePath.length());
                        source ="<source src=\""+localAddr+":"+localPort+"/dataFile/"+filePath; 
                	}
                }else{//获取本地文件路径
                    String localAddr=request.getLocalAddr();
                    String localPort=String.valueOf(request.getLocalPort());
                    source ="<source src=\""+localAddr+":"+localPort+"/dataFile/"+sysFile.getFilepath(); //目前本地输入流未解决
                    //source ="<source src=\"http://192.168.8.105:8888/dataFile/"+sysFile.getFilepath();
                   //source ="<source src=\"http://192.168.8.105:8888/ibms/oa/system/officeTemplate/readFile.do?opath="+sysFile.getFilepath();
                }   
                if(filePath.contains(".mp4")){
                    source+="\" type='video/mp4' />";
                }
                if(filePath.contains(".webm")){
                    source+="\" type='video/webm' />";
                }
                if(filePath.contains(".ogv")){
                    source+="\" type='video/ogg />";
                }
			    modelView.addObject("videoSource", source);
			}
			
			if("mp3".contains(fileType.toLowerCase())){
				String url = contextPath+"/oa/system/sysFile/getMusicStream.do?fileId="+fileId;
				if(isNoGroup){
					if(sysFile.getIsEncrypt() != 1L){//未加密的分布式文件路径
						url = filePath;
                	}else{//已加密的分布式文件
                		String localAddr=request.getLocalAddr();
                        String localPort=String.valueOf(request.getLocalPort());
                        filePath = filePath.substring(attachPath.length()+1, filePath.length());
                        url =localAddr+":"+localPort+"/dataFile/"+filePath; 
                	}
				}
				modelView.addObject("url", url);
			}
			
			//获取系统跟路径
			String root=FileUtil.getRootPath();
			//判断是否为pageoffice
			boolean isPageOffice = false;
			if(isNoGroup&&"doc,docx,ppt,pptx,xls,xlsx,pdf".contains(fileType.toLowerCase())){
				//by liubo 对于分布式文件存储时office在线预览处理
				String dirPath=root+File.separator+"attachFile";
				File dirFile=new File(dirPath);
				if(!dirFile.exists()) dirFile.mkdir();
				String filePathName=dirPath+File.separator+"fileView"+fileId+"."+fileType.toLowerCase();
				boolean existFile=FileOperator.isFileExist(filePathName);
				if(existFile)
					FileOperator.deleteFile(filePathName);
				//生成临时文件
				FileOperator.createFile(filePathName);
				//获取分布式文件存储文件的字节流
				byte[] fastDFSFileByte = FastDFSFileOperator.getFileByte(sysFile.getFilepath());
				FileOperator.writeByte(filePathName, fastDFSFileByte);
				isPageOffice =  true;
			}
			modelView.addObject("fileId", fileId);
			modelView.addObject("fileName", fileName);
			modelView.addObject("fileType", fileType);
			modelView.addObject("path", sysFile.getFilepath());
			modelView.addObject("dataId", sysFile.getDataId());
			if(isPageOffice){
				if(sysFile.getIsEncrypt() == 1L){
					modelView.addObject("filePath", filePath);
				}else{
					modelView.addObject("filePath", root+File.separator+"attachFile"+File.separator+"fileView"+fileId+"."+fileType.toLowerCase());
				}
			}else{
				modelView.addObject("filePath", filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelView;
	}
	
	/**
	 * 使用分布式文件存储时的在线编辑office后的处理
	 * @author liubo
	 * @param filePath
	 * @return
	 */
	public static void updateFastDFSFile(String fileId,String path,String fileType) {
		
		String root=FileUtil.getRootPath();
		String filePathName=root+"attachFile"+File.separator+"fileView"+fileId+"."+fileType;
		
		//删除文件服务器上原来的文件
		FastDFSFileOperator.deleteFile(fileId);
		
		//上传新的已经更新的文件并将数据库中的对应文件path更新
		byte[] fileBytes = FileUtil.readByte(filePathName);
		
		//获取系统是否加密的参数
		// 获取密级参数数据
		SysParameterService sysParameterService=(SysParameterService)AppUtil.getBean("sysParameterService");
		List<SysParameter> needEncrypt = sysParameterService.getByParamName("IS_SAVE_SECURITY");
		Long isEncrypt = 0L;
		if (needEncrypt.size() > 0 && "是".equals(needEncrypt.get(0).getParamvalue())) {
			//给文件是否加密属性设置为1
			isEncrypt = 1L;
		}
		String fileNewPath = FastDFSFileOperator.uploadFile(fileBytes, fileType);
		
		//判断是否需要进行加密
		if (needEncrypt.size() > 0 && "是".equals(needEncrypt.get(0).getParamvalue())) {
			needEncrypt.clear();
			//获取加密Key名称
			needEncrypt = sysParameterService.getByParamName("FILE_KEY_NAME");
			if (needEncrypt.size() > 0) {
				try {
					Cipher cipher = Cipher.getInstance("DES");
					Key key = FileOperator.getKey(needEncrypt.get(0).getParamvalue());
					//设置key值
					FileOperator.keyName = needEncrypt.get(0).getParamvalue();
					cipher.init(Cipher.ENCRYPT_MODE, key);
					
					InputStream is = new ByteArrayInputStream(fileBytes);
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					CipherInputStream cis = new CipherInputStream(is, cipher);
					
					byte[] buffer = new byte[2048];
					int r;
					while ((r = cis.read(buffer)) > 0) {
						out.write(buffer, 0, r);
					}
					byte[] byteFile = out.toByteArray();
					cis.close();
					is.close();
					
					//上传成功后返回路径
					fileNewPath = FastDFSFileOperator.uploadFile(byteFile,fileType);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		Map<String,Object> parma=new HashMap<String,Object>();
		parma.clear();
    	parma.put("filepath", fileNewPath);
    	parma.put("fileId", fileId);
    	parma.put("isEncrypt", isEncrypt);
    	SysFileDao sysFileDao=(SysFileDao)AppUtil.getBean(SysFileDao.class);
    	sysFileDao.update("updateFilePath", parma);
		
    	//删除临时文件
    	boolean existFile=FileOperator.isFileExist(filePathName);
		if(existFile){
			FileOperator.deleteFile(filePathName);
		}
	}
	
	/**
	 * 读取二进制文件的内容
	 * 
	 * @param filePath
	 * @return
	 */
	public String parseBinaryFile(String filePath) {

		StringBuffer buffer = new StringBuffer();
		try {
			// 解析文件
			InputStreamReader fileReader = new InputStreamReader(new FileInputStream(filePath), "GBK");
			LineNumberReader lineReader = new LineNumberReader(fileReader);
			while (lineReader.ready()) {
				buffer.append(lineReader.readLine() + "<br/>");
			}
			fileReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * 打开文件上传界面
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("upload")
	@Action(description = "打开文件上传界面",detail = "打开文件上传界面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView upload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView modelView = null;
		try {
			modelView = getAutoView();
			// 文件上传所属分类[当文件上传字段设置为：非直接上传时，此参数有效！]
			String typeId = RequestUtil.getString(request, "typeId");
			// 是否启用文件维度[1：启用]
			boolean useDimension = RequestUtil.getInt(request, "dimension", 0)==1;
			// isGlType用来标记是否是走分类文件的方式
			int isGlType = RequestUtil.getInt(request, "isGlType", 0);
			// 判断是否显示密级
			long dataId = RequestUtil.getLong(request, "dataId", 0); // 业务表记录的id

			long tableId = RequestUtil.getLong(request, "tableId", 0); // 业务表的id
			// 此参数判断是否是在更新版本
			long fileId = RequestUtil.getLong(request, "fileId", 0);

			boolean isDisplay = false;
			// 存储密级map
			Map<String, String> securityMap = ISysFile.SECURITY_CHINESE_MAP;
			// 获取密级参数数据
			List<SysParameter> spDatas = sysParameterService
					.getByParamName(ISysFile.IS_DISPLAY_SECURITY);
			//获取当前用户密级权限
			String currentUserSecurity = UserContextUtil.getCurrentUserSecurity();
			
			
			if (spDatas.size() > 0
					&& "1".equals(spDatas.get(0).getParamvalue())) {
				// 获取文件密级分类数据
				if(BeanUtils.isEmpty(currentUserSecurity)){
					//没有设置密级的人员只能查看公开及其以下的文件
					Set set = securityMap.keySet();
					Iterator it = set.iterator();
					while(it.hasNext()){
						int key = Integer.parseInt(it.next().toString());
						int userKey = Integer.parseInt(ISysUser.SECURITY_FEIMI);
						if(key>userKey){
							it.remove();
						}
					}
				}else{
					//非密的人员只能查看公开及其以下文件
					//一般，只能查看秘密及其以下的数据，包含没有设置密级的数据
					//重要，能够查看除绝密数据外的其他数据
					//核心，能够查看所有数据
					Double userKey = Double.parseDouble(currentUserSecurity);
					Set set = securityMap.keySet();
					Iterator it = set.iterator();
					while(it.hasNext()){
						Double key = Double.parseDouble(it.next().toString());
						if(key.compareTo(userKey)>0){
							it.remove();
						}
					}
				}
				
				isDisplay = true;
			}
			
			modelView.addObject("typeId", typeId);
			modelView.addObject("isDisplay", isDisplay);
			modelView.addObject("securityMap", securityMap);
			modelView.addObject("currentUserSecurity", currentUserSecurity);
			modelView.addObject("isGlType", isGlType);
			modelView.addObject("dataId", dataId);
			modelView.addObject("tableId", tableId);
			modelView.addObject("fileId", fileId);
			modelView.addObject("useDimension", useDimension);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelView;
	}

	/**
	 * 保存文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("save")
	@Action(description = "打开文件保存界面",detail = "打开文件保存界面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView save(HttpServletRequest request,HttpServletResponse response) {

		ModelAndView modelView = null;
		try {
			modelView = getAutoView();
			String fileId = RequestUtil.getString(request, "fileId");
			String fileType = RequestUtil.getString(request, "fileType");
			String path = RequestUtil.getString(request, "path");
			
			List<SysParameter> spDatas = sysParameterService.getByParamName("IS_SAVE_SECURITY");
			if (spDatas.size() > 0&& "是".equals(spDatas.get(0).getParamvalue())) {
				SysFile sysFile = sysFileService.getById(Long.valueOf(fileId));
				modelView.addObject("origFileName", sysFile.getFilepath());
				spDatas = sysParameterService.getByParamName("FILE_KEY_NAME");
				if (spDatas.size() > 0) {
					modelView.addObject("keyName", FileOperator.keyName);
				}
			}
			
			modelView.addObject("fileId", fileId);
			modelView.addObject("fileType", fileType);
			modelView.addObject("path", path);
			modelView.addObject("fileName",RequestUtil.getString(request, "fileName"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelView;
	}

	/**
	 * 电子签章-保存文件.
	 *
	 * <p>
	 * detailed comment
	 * </p>
	 * 
	 * @author [创建人] WeiLei <br/>
	 *         [创建时间] 2016-8-24 上午11:04:37 <br/>
	 *         [修改人] WeiLei <br/>
	 *         [修改时间] 2016-8-24 上午11:04:37
	 * @param request
	 * @param response
	 * @throws Exception
	 * @see
	 */
	@RequestMapping({ "saveFile" })
	@Action(description = "电子签章-文件上传", exectype = SysAuditExecType.FILEUPLOAD_TYPE, detail = "<#if isAdd>添加<#else>更新</#if>文件<#list sysFiles as item>【${item.fileName}】</#list>")
	public void saveFile(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
		long userId = UserContextUtil.getCurrentUserId(); // 获取当前用户的id
		long typeId = RequestUtil.getLong(request, "typeId");
		long fileId = RequestUtil.getLong(request, "fileId");
		Long fileTypeId = RequestUtil.getLong(request, "fileTypeId");
		String uploadName = RequestUtil.getString(request, "uploadName", "");
		String ajaxType = RequestUtil.getString(request, "ajaxType", "");
		String inputObjNum = RequestUtil.getString(request, "inputObjNum", "");
		String fileType = RequestUtil.getString(request, "fileType", "");
		try {
			ISysUser appUser = null;
			if (userId > 0) {
				appUser = sysUserService.getById(userId);
			}
			GlobalType globalType = null;
			if (typeId > 0) {
				globalType = globalTypeService.getById(typeId);
			}
			Map files = request.getFileMap();
			Iterator it = files.values().iterator();
			while (it.hasNext()) {
				MultipartFile f = (MultipartFile) it.next();
				String name = f.getName();
				String myFilePath = "";
				if (uploadName.equals(name)) {
					boolean isAdd = true;
					SysFile sysFile = null;
					if (fileId == 0L) {
						fileId = UniqueIdUtil.genId();
						sysFile = new SysFile();
						sysFile.setFileId(Long.valueOf(fileId));
					} else {
						sysFile = (SysFile) this.sysFileService.getById(Long
								.valueOf(fileId));
						isAdd = false;
						myFilePath = sysFile.getFilepath();
					}
					try {
						List sysFiles;
						if (LogThreadLocalHolder.getParamerter("sysFiles") == null) {
							sysFiles = new ArrayList();
							LogThreadLocalHolder.putParamerter("sysFiles",
									sysFiles);
						} else {
							sysFiles = (List) LogThreadLocalHolder
									.getParamerter("sysFiles");
						}
						sysFiles.add(sysFile);
						LogThreadLocalHolder.putParamerter("isAdd",
								Boolean.valueOf(fileId == 0L));
					} catch (Exception e) {
						e.printStackTrace();
						this.logger.error(e.getMessage());
					}
					String oriFileName = f.getOriginalFilename();
					String extName = FileOperator.getSuffix(oriFileName);
					String relativeFullPath = FileOperator
							.generateFilenameNoSemicolon(oriFileName);
					String filePath = SysFileUtil.createFilePath(attachPath
							+ File.separator, relativeFullPath);
					File fileInfo = new File(filePath);
					if (fileInfo.exists()) {
						fileInfo.delete();
					}
					FileOperator.writeByte(filePath, f.getBytes());
					sysFile.setFilename(oriFileName.substring(0,
							oriFileName.lastIndexOf('.')));
					sysFile.setFilepath(relativeFullPath); // 保存相对路径
					// 类型
					sysFile.setFileType(String.valueOf(typeId));
					// 当前用户的信息
					if (appUser != null) {
						sysFile.setCreatorId(appUser.getUserId());
						sysFile.setCreator(appUser.getUsername());
					} else {
						sysFile.setCreatorId(UserContextUtil.getCurrentUser()
								.getUserId());
						sysFile.setCreator(UserContextUtil.getCurrentUser()
								.getUsername());
					}
					sysFile.setCreatetime(new Date());
					sysFile.setExt(extName);
					sysFile.setTotalBytes(Long.valueOf(f.getSize()));
					sysFile.setNote(FileOperator.getSize(f.getSize()));
					sysFile.setDelFlag(SysFile.FILE_NOT_DEL);
					SysFileFolder folder = null;
					if (null == fileTypeId || fileTypeId.equals(0L)) {
						folder = sysFileFolderService
								.getRootFolderByUserId(userId);
						if (BeanUtils.isEmpty(folder)) {
							sysFileFolderService.saveFolder(userId);
							folder = sysFileFolderService
									.getRootFolderByUserId(userId);
						}

					} else {
						folder = sysFileFolderService.getById(fileTypeId);
					}
					sysFile.setFolderid(fileTypeId);
					sysFile.setFileatt(SysFile.FILEATT_TRUE);
					sysFile.setFolderPath(folder.getPath());
					sysFile.setShared(null != folder.getSharedNode()
							&& folder.getSharedNode() ? SysFile.SHARED_TRUE
							: SysFile.SHARED_FALSE);
					if (isAdd) {
						this.sysFileService.add(sysFile);
					} else {
						this.sysFileService.update(sysFile);
						boolean mark = true;
						String newFilePath = sysFile.getFilepath();
						if ((StringUtil.isNotEmpty(newFilePath))
								&& (StringUtil.isNotEmpty(myFilePath))
								&& (newFilePath.trim()
										.equals(myFilePath.trim()))) {
							mark = false;
						}
						if (mark) {
							if (StringUtil.isEmpty(attachPath)) {
								myFilePath = AppUtil.getRealPath(myFilePath);
							}
							File oldFileInfo = new File(attachPath
									+ File.separator + myFilePath);
							if (oldFileInfo.exists()) {
								oldFileInfo.delete();
							}
						}
					}
				}
				String reutrnStr = Long.toString(fileId);
				if (StringUtil.isNotEmpty(inputObjNum)) {
					reutrnStr = inputObjNum + "##" + reutrnStr;
				}
				if ((StringUtil.isNotEmpty(ajaxType))
						&& ("obj".equals(ajaxType))
						&& (BeanUtils.isNotIncZeroEmpty(Long.valueOf(fileId))))
					writeResultMessage(writer, reutrnStr, 1);
				else
					writer.print(reutrnStr);
			}
		} catch (Exception e) {
			this.logger.warn(e.getMessage());
			if ((StringUtil.isNotEmpty(ajaxType)) && ("obj".equals(ajaxType))
					&& (BeanUtils.isNotIncZeroEmpty(Long.valueOf(fileId))))
				writeResultMessage(writer, "-1", 1);
			else
				writer.print(-1);
		}
	}

	/**
	 * 
	 * 保存Info附件（web签章）.
	 *
	 * <p>
	 * detailed comment
	 * </p>
	 * 
	 * @author [创建人] WeiLei <br/>
	 *         [创建时间] 2016-8-25 上午11:39:46 <br/>
	 *         [修改人] WeiLei <br/>
	 *         [修改时间] 2016-8-25 上午11:39:46
	 * @param request
	 * @param response
	 * @throws Exception
	 * @see
	 */
	@RequestMapping({ "saveFileInfo" })
	@Action(description = "保存web签章附件", detail = "保存web签章附件", exectype = SysAuditExecType.FILEUPLOAD_TYPE)
	public void saveFileInfo(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
		try {
			long userId = UserContextUtil.getCurrentUserId().longValue();
			long typeId = RequestUtil.getLong(request, "typeId");
			long fileId = RequestUtil.getLong(request, "fileId");
			Long fileTypeId = RequestUtil.getLong(request, "fileTypeId");
			String uploadName = RequestUtil
					.getString(request, "uploadName", "");
			ISysUser appUser = null;
			if (userId > 0) {
				appUser = sysUserService.getById(userId);
			}
			GlobalType globalType = null;
			if (typeId > 0) {
				globalType = globalTypeService.getById(typeId);
			}
			Map files = request.getFileMap();
			Iterator it = files.values().iterator();
			while (it.hasNext()) {
				MultipartFile f = (MultipartFile) it.next();
				String name = f.getName();
				if (uploadName.equals(name)) {
					boolean isAdd = true;
					SysFile sysFile = null;
					String myFilePath = "";
					if (fileId == 0L) {
						fileId = UniqueIdUtil.genId();
						sysFile = new SysFile();
						sysFile.setFileId(Long.valueOf(fileId));
					} else {
						sysFile = (SysFile) this.sysFileService.getById(Long
								.valueOf(fileId));
						isAdd = false;
						myFilePath = sysFile.getFilepath();
					}
					try {
						List sysFiles;
						if (LogThreadLocalHolder.getParamerter("sysFiles") == null) {
							sysFiles = new ArrayList();
							LogThreadLocalHolder.putParamerter("sysFiles",
									sysFiles);
						} else {
							sysFiles = (List) LogThreadLocalHolder
									.getParamerter("sysFiles");
						}
						sysFiles.add(sysFile);
						LogThreadLocalHolder.putParamerter("isAdd",
								Boolean.valueOf(fileId == 0L));
					} catch (Exception e) {
						e.printStackTrace();
						this.logger.error(e.getMessage());
					}
					String oriFileName = f.getOriginalFilename();
					String extName = FileOperator.getSuffix(oriFileName);
					String relativeFullPath = FileOperator
							.generateFilenameNoSemicolon(oriFileName);
					String filePath = SysFileUtil.createFilePath(attachPath
							+ File.separator, relativeFullPath);
					File fileInfo = new File(filePath);
					if (fileInfo.exists()) {
						fileInfo.delete();
					}
					FileOperator.writeByte(filePath, f.getBytes());
					sysFile.setFilename(oriFileName.substring(0,
							oriFileName.lastIndexOf('.')));
					sysFile.setFilepath(relativeFullPath);
					// 类型
					sysFile.setFileType(String.valueOf(typeId));
					sysFile.setCreatetime(new Date());
					sysFile.setExt("info");
					sysFile.setTotalBytes(Long.valueOf(f.getSize()));
					sysFile.setNote(FileOperator.getSize(f.getSize()));
					if (appUser != null) {
						sysFile.setCreatorId(appUser.getUserId());
						sysFile.setCreator(appUser.getUsername());
					} else {
						sysFile.setCreatorId(UserContextUtil.getCurrentUser()
								.getUserId());
						sysFile.setCreator(UserContextUtil.getCurrentUser()
								.getUsername());
					}
					SysFileFolder folder = null;
					if (null == fileTypeId || fileTypeId.equals(0L)) {
						folder = sysFileFolderService
								.getRootFolderByUserId(userId);
						if (BeanUtils.isEmpty(folder)) {
							sysFileFolderService.saveFolder(userId);
							folder = sysFileFolderService
									.getRootFolderByUserId(userId);
						}
					} else {
						folder = sysFileFolderService.getById(fileTypeId);
					}
					sysFile.setDelFlag(SysFile.FILE_NOT_DEL);
					sysFile.setFolderid(fileTypeId);
					sysFile.setFileatt(SysFile.FILEATT_TRUE);
					sysFile.setFolderPath(folder.getPath());
					sysFile.setShared(null != folder.getSharedNode()
							&& folder.getSharedNode() ? SysFile.SHARED_TRUE
							: SysFile.SHARED_FALSE);
					if (isAdd) {
						this.sysFileService.add(sysFile);
					} else {
						this.sysFileService.update(sysFile);
						boolean mark = true;
						String newFilePath = sysFile.getFilepath();
						if ((StringUtil.isNotEmpty(newFilePath))
								&& (StringUtil.isNotEmpty(myFilePath))
								&& (newFilePath.trim()
										.equals(myFilePath.trim()))) {
							mark = false;
						}
						if (mark) {
							if (StringUtil.isEmpty(attachPath)) {
								myFilePath = AppUtil.getRealPath(myFilePath);
							}
							File oldFileInfo = new File(attachPath
									+ File.separator + myFilePath);
							if (oldFileInfo.exists()) {
								oldFileInfo.delete();
							}
						}
					}
				}
				writer.print(fileId);
			}
		} catch (Exception e) {
			this.logger.warn(e.getMessage());
			writer.print(-1);
		}
	}
	
	
	/**
	 * 返回附件选择页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("dialog")
	@Action(description = "附件查询", exectype = SysAuditExecType.SELECT_TYPE, detail = "打开附件选择页面")
	public ModelAndView dialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		ModelAndView result = getAutoView();
		return result;
	}
	
	
	 /**
	  * 获取音频流
	  * @author YangBo
	  * @param request
	  * @param response
	  * @param fileId
	  * @throws IOException 
	  */
	@RequestMapping("getMusicStream")
	@Action(description = "音频文件下载", exectype = SysAuditExecType.FILEDOWNLOAD_TYPE, detail = "获取音频文件${SysAuditLinkService.getSysFileLink(String.valueOf(fileId))}")
	public void getMusicStream(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		Long fileId = RequestUtil.getLong(request, "fileId");
		SysFile sysFile = sysFileService.getById(fileId);
		String opath = sysFile.getFilepath();
		OutputStream outp = new BufferedOutputStream(response.getOutputStream());
		File file = new File(attachPath+File.separator+opath);
		if(!file.exists()) response.getWriter().print("该音频文件丢失！");
		response.addHeader("Accept-Ranges","bytes");
		//注意Content-Type
		response.addHeader("Content-Type","audio/mp3;charset=UTF-8");
		FileInputStream in = new FileInputStream(attachPath+File.separator+opath);
		InputStream fis = new BufferedInputStream(in);
		    byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buf)) != -1) {
                outp.write(buf, 0, bytesRead);
            }
            fis.close();
            outp.flush();
            outp.close();  
        try {
              response.wait();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
				in = null;
			}
			if (outp != null) {
				outp.close();
				outp = null;
				response.flushBuffer();
			}
		}
	}
	
	/**
	 *  by YangBo
	 * 返回附件的基本信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getFile"})
	@Action(description="查看附件明细", detail="查看附件明细", exectype = SysAuditExecType.SELECT_TYPE)
	@ResponseBody
	public SysFile getFile(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		long id = RequestUtil.getLong(request, "fileId");
		SysFile sysFile = (SysFile)this.sysFileService.getById(Long.valueOf(id));
		return sysFile;
	}
	
	
	/**
	 *  by fuyong
	 * 返回附件的基本信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getFileByDataId"})
	@Action(description="查看附件明细", detail="查看附件明细", exectype = SysAuditExecType.SELECT_TYPE)
	@ResponseBody
	public SysFile getFileByDataId(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String dataId = RequestUtil.getString(request, "dataId");
		SysFile sysFile = this.sysFileService.getFileByDataId(dataId);
		return sysFile;
	}
	/**
	 * 展示应用的图片
	 * by YangBo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"appImages"})
	@Action(description="图片预览", detail="图片预览", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView appImages(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{	
		String fileType = request.getParameter("from");

		String single = request.getParameter("single");

		if (StringUtils.isEmpty(fileType)) {
			fileType = "news";
		}

		List<SysFile> imageFiles = this.sysFileService.getImagesByFileType(fileType, UserContextUtil.getCurrentUserId());

		return getAutoView().addObject("imageFiles", imageFiles)
				.addObject("single", single).addObject("from", fileType);
	}
	
	/**
	 *  by YangBo
	 * 新闻标题图片预览
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	 @RequestMapping({"imageView"})
	         public void imageView(HttpServletRequest request, HttpServletResponse response)
	           throws Exception
	 {/*
		 String fileId = request.getParameter("fileId");

		 String thumb = request.getParameter("thumb");

		 String view = request.getParameter("view");

		 SysFile sysFile = (SysFile)this.sysFileManager.get(fileId);
		 String filePath = null;

		 if (sysFile != null) {
			 String basePath = null;
			 if (sysFile.getFrom().equals("APPLICATION"))
				 basePath = WebAppUtil.getAppAbsolutePath();
			 else if (sysFile.getFrom().equals("ANONY"))
				 basePath = WebAppUtil.getAnonymusUploadDir();
			 else {
				 basePath = WebAppUtil.getUploadPath();
			 }

			 if ("true".equals(thumb))
				 filePath = basePath + "/" + sysFile.getThumbnail();
			 else {
				 filePath = basePath + "/" + sysFile.getPath();
			 }

			 response.setContentType("image/" + sysFile.getExt());
		 } else {
			 if ("true".equals(view))
			 {
				 filePath = WebAppUtil.getAppAbsolutePath() + "/styles/images/no-photo.png";
			 }
			 else {
				 filePath = WebAppUtil.getAppAbsolutePath() + "/styles/images/upload-file.png";
			 }

			 response.setContentType("image/PNG");
		 }

		 File file = new File(filePath);
		 response.setHeader("Pragma", "No-cache");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setDateHeader("Expires", 0L);

		 FileInputStream fis = null;
		 BufferedInputStream buff = null;
		 OutputStream out = null;
		 try {
			 fis = new FileInputStream(file);
			 buff = new BufferedInputStream(fis);
			 out = response.getOutputStream();

			 byte[] bs = new byte[1024];
			 int n = 0;
			 while ((n = buff.read(bs)) != -1)
				 out.write(bs, 0, n);
		 }
		 catch (Exception ex) {
			 this.logger.error(ex.getMessage());
		 } finally {
			 if (fis != null)
				 fis.close();
			 if (buff != null)
				 buff.close();
			 if (out != null)
				 out.close(); 
		 }
	 */}
	
	 /**
	  * 图片预览页面
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping({"imgPreview"})
	 @Action(description="图片预览", detail="图片预览", exectype = SysAuditExecType.FILEPREVIEW_TYPE)
	 public ModelAndView imgPreview(HttpServletRequest request, HttpServletResponse response)
			 throws Exception
	 {
		 String fileId = request.getParameter("fileId");
		 SysFile sysFile = (SysFile)this.sysFileService.getById(Long.valueOf(fileId));
		 return getAutoView().addObject("sysFile", sysFile);
	 }
	 
}
