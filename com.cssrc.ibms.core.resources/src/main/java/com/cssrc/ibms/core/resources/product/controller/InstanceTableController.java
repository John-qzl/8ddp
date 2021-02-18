package com.cssrc.ibms.core.resources.product.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.dp.rangeTest.mission.service.RangeTestSummaryService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.resources.io.bean.FileData;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import com.cssrc.ibms.core.resources.io.dao.FileDataDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableTempDao;
import com.cssrc.ibms.core.resources.io.service.RangeTestInstanceService;
import com.cssrc.ibms.core.resources.product.service.InstanceTableService;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.form.service.FormService;
import com.cssrc.ibms.dp.product.acceptance.bean.acceptanceReport;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceReportDao;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SysFileService;
/**
 * @author fuyong
 * 表格实例控制器
 */
@Controller
@RequestMapping("/model/instance/")
public class InstanceTableController extends BaseController{
	@Resource
	InstanceTableService instanceTableService;
	@Resource
	FormService formService;
	@Resource
	SysFileService sysFileService;
	@Resource
	IOTableTempDao ioTableTempDao;
	@Resource
	AcceptanceReportDao acceptanceReportDao;
	@Resource
	FileDataDao fileDataDao;
	@Resource
	RangeTestPlanDao rangeTestPlanDao;
	@Resource
	RangeTestSummaryService rangeTestSummaryService;
	@Resource
	SysUserService sysUserService;
	@Resource
	RangeTestInstanceService rangeTestInstanceService;
	
    // 附件保存路径
    private String attachPath = AppUtil.getAttachPath();
	
	@RequestMapping({ "getInstanceHTML" })
	@ResponseBody
	@Action(description = "获取实例表单的html")
	public Map<String,Object> getInstanceHTML(HttpServletRequest request, HttpServletResponse response) {
		String id=request.getParameter("id");
		Map<String,Object> tableins=instanceTableService.getInstanceHTML(id);
		Map<String,Object> data=new HashedMap();
        String content = tableins.get("F_CONTENT").toString();
        int index1 = content.indexOf("<table");
        int index2 = content.lastIndexOf("</table>");
        content = content.substring(index1, index2 + 8);
        content = content.replaceAll(
                "<input class=\"dpInputBtn\" type=\"button\" disabled=\"true\"",
                "<input class=\"dpInputBtn\" type=\"button\"");
        //可能存在未能将按钮的disabled属性正确替换的情况（因为构建表单模板时的一些顺序因素），这里需要重新替换
        content = content.replaceAll("disabled=\"true\""," ");
        List<Map<String, Object>> signresult = formService
                .getSignResultById(id);
        if (signresult.size() > 0) {
            for (int i = 0; i < signresult.size(); i++) {
                try {
                    String contextPath = request.getContextPath();
                    String fileId = signresult.get(i).get("FILEID").toString();
                    SysFile sysFile = sysFileService.getById(Long
                            .valueOf(fileId));
                    String filePath = sysFile.getFilepath();
                    String fileType = sysFile.getExt();
                    String fileName = sysFile.getFilename() + "."
                            + sysFile.getExt();

                    Boolean isNoGroup = filePath.startsWith("group");// 判断是否分布式文件
                    String interview_server = FastDFSFileOperator
                            .getInterviewServer(); // 分布式请求url端口
                    if (isNoGroup) {
                        if (sysFile.getIsEncrypt() != 1L) {
                            filePath = interview_server + "/"
                                    + sysFile.getFilepath();
                        }
                    } else {
                        filePath = attachPath + File.separator + filePath;
                    }
                    String destFilePath = sysFileService.getDecodeFilePath(
                            filePath, fileName, sysFile.getIsEncrypt(),
                            isNoGroup);
                    if (!"".equals(destFilePath)) {
                        filePath = destFilePath;
                        fileName = FileOperator.getFileNameByPath(filePath);
                    }
                    if ("png,bmp,gif,jpg".contains(fileType.toLowerCase())) {

                        String imgSrc = contextPath
                                + "/oa/system/sysFile/getFileById.do?fileId="
                                + fileId;
                        signresult.get(i).put("imgSrc", imgSrc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            signresult = formService.getSignResById(id);
        }
        data.put("signresult", signresult);
        data.put("content", content);
		return data==null?null:data;
	}
	@RequestMapping({ "copyModel" })
	@ResponseBody
	@Action(description = "通过模板id进行模板复制")
	public String copyModel(HttpServletRequest request, HttpServletResponse response) {
		String tableId=request.getParameter("tableId");
		String acceptancePlanId=request.getParameter("acceptancePlanId");
		TableTemp tableTemp=ioTableTempDao.getById(tableId);
		tableTemp.setModuleId(null);
		String Id=String.valueOf(UniqueIdUtil.genId());
		tableTemp.setId(Id);
		tableTemp.setProject_id("0");
		tableTemp.setNumber("0");
		ioTableTempDao.insert(tableTemp);
		return Id;
		
	}

	/** auth:zmz
	 * 实例页面的数据excel导入
	 **/
	@RequestMapping("rangeTestinstantfileUpload")
	@Action(description = "文件上传", exectype = SysAuditExecType.FILEUPLOAD_TYPE, detail = "上传文件:"
			+ "<#list sysFiles as item>"
			+ "${item.filename}.${item.ext}"
			+ "</#list>")
	public void rangeTestinstantfileUpload(MultipartHttpServletRequest request,
								  HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		String returnMsg = "";
		try {
			String missionId = RequestUtil.getString(request, "missionId");
			Map<String, MultipartFile> files = request.getFileMap();
			Iterator<MultipartFile> it = files.values().iterator();
			//这里的循环是怕一次上传多个文件
			//挨个处理
			while (it.hasNext()) {
				MultipartFile f = it.next();
				returnMsg=instanceTableService.importRangeTestFromExcel(f.getInputStream(),missionId);
			}
			writer.println(returnMsg);
		} catch (Exception e) {
			e.printStackTrace();
			writer.println("{\"success\":\"false\",\"context\":\"数据导入失败，请联系系统管理员\"}");
		}
	}
	/** auth:fuyong
	 * 实例文件上传
	 **/
	@RequestMapping("rangeInstantfileUpload")
	@Action(description = "文件上传", exectype = SysAuditExecType.FILEUPLOAD_TYPE, detail = "上传文件:"
			+ "<#list sysFiles as item>"
			+ "${item.filename}.${item.ext}"
			+ "</#list>")
	public void rangeInstantfileUpload(MultipartHttpServletRequest request,
								  HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		String returnMsg = "";
		try {
			String missionId = RequestUtil.getString(request, "missionId");
			long userId = UserContextUtil.getCurrentUserId(); // 获取当前用户的id
			ISysUser appUser = null;
			if (userId > 0) {
				appUser = sysUserService.getById(userId);
			}		
			returnMsg=sysFileService.uploadAttach(request,response,appUser,userId);
			JSONObject fileInfo=JSONObject.parseObject(returnMsg);
			if(fileInfo.getBoolean("success")) {
				JSONArray jsonArray=new JSONArray();
				JSONObject file=new JSONObject();
				file.put("id", fileInfo.get("fileId"));
				file.put("name", fileInfo.get("fileName"));
				jsonArray.add(file);
				rangeTestInstanceService.uploadFileInstace(missionId,jsonArray.toString());
			}
			writer.println(returnMsg);
		} catch (Exception e) {
			e.printStackTrace();
			writer.println("{\"success\":\"false\",\"context\":\"数据导入失败，请联系系统管理员\"}");
		}
	}
	
	 /** auth:fuyong
     * 数据excel导入
     **/
    @RequestMapping("instantfileUpload")
    @Action(description = "文件上传", exectype = SysAuditExecType.FILEUPLOAD_TYPE, detail = "上传文件:"
            + "<#list sysFiles as item>"
            + "${item.filename}.${item.ext}"
            + "</#list>")
    public void instantfileUpload(MultipartHttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    	PrintWriter writer = response.getWriter();
    	String returnMsg = "";
    	 try {
    		 String acceptancePlanId = RequestUtil.getString(request, "acceptancePlanId");
    		 String productBatchId = RequestUtil.getString(request, "productBatchId");
    		 Map<String, MultipartFile> files = request.getFileMap();
             Iterator<MultipartFile> it = files.values().iterator();
             while (it.hasNext()) {
            	 MultipartFile f = it.next();
            	 returnMsg=instanceTableService.readExcel(f.getInputStream(),acceptancePlanId,productBatchId);
             }
             writer.println(returnMsg);
    	 } catch (Exception e) {
             e.printStackTrace();
             writer.println("{\"success\":\"false\",\"context\":\"数据导入失败，请联系系统管理员\"}");
         }
    }
	@RequestMapping({ "depletedById" })
	@ResponseBody
	public String depletedById(HttpServletRequest request, HttpServletResponse response) {
		 String acceptancePlanId = RequestUtil.getString(request, "acceptancePlanId");
		 String instantId = RequestUtil.getString(request, "instantId");
		 List<acceptanceReport> acceptanceReportList=acceptanceReportDao.getByPlanId(acceptancePlanId);
		 for (acceptanceReport acceptanceReport : acceptanceReportList) {
			if(acceptanceReport.getSpzt().equals("正在审批")
					||acceptanceReport.getSpzt().equals("审批通过")
					||acceptanceReport.getSpzt().equals("审批中")) {
				return "1";
			}
		}
		 instanceTableService.updateStatus(instantId, "废弃");
		 if(acceptanceReportList.size()==0) {
			List<FileData> fileDataList=fileDataDao.getByDataId(instantId);
			for (FileData fileData : fileDataList) {
				fileData.setPlanId("0");
				fileDataDao.update(fileData);
			}
		 }
		 return "2";
	}



	/**
	 * 靶场试验的表单实例废弃按钮
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "scrapRangeTestInstanceById" })
	@ResponseBody
	public String scrapRangeTestInstanceById(HttpServletRequest request, HttpServletResponse response) {
		String missionId = RequestUtil.getString(request, "missionId");
		String instantId = RequestUtil.getString(request, "instantId");

		String status=rangeTestSummaryService.getConfirmStatusByPlanId(missionId);
		if(status.equals("正在审批")
				||status.equals("审批通过")
				||status.equals("审批中")) {
				return "1";
		}
		instanceTableService.updateStatus(instantId, "废弃");
			List<FileData> fileDataList=fileDataDao.getByDataId(instantId);
			for (FileData fileData : fileDataList) {
				fileData.setPlanId("0");
				fileDataDao.update(fileData);
			}
		return "2";
	}
}
