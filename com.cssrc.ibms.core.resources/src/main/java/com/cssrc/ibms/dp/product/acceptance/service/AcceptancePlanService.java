package com.cssrc.ibms.dp.product.acceptance.service;

import java.io.File;
import java.util.*;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.resources.io.bean.ArchiveFileBean;
import com.cssrc.ibms.core.resources.io.service.SecretLevelService;
import com.cssrc.ibms.core.resources.io.service.ServerExportService;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.ZipUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.dp.product.acceptance.bean.acceptanceReport;
import com.cssrc.ibms.core.resources.io.bean.AcceptancePlan;
import com.cssrc.ibms.dp.form.model.SignResult;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.dp.form.dao.SignResultDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceReportDao;
import com.cssrc.ibms.dp.sync.bean.PadPhotoInfo;
import com.cssrc.ibms.dp.sync.dao.PadPhotoInfoDao;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.util.SysFileUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceGroupDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptancePlanDao;
import com.cssrc.ibms.system.service.SerialNumberService;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @description 产品验收策划业务解析类
 * @author xie chen
 * @date 2019年12月11日 下午6:28:36
 * @version V1.0
 */
@Service
public class AcceptancePlanService {
	
	@Resource
	private AcceptancePlanDao dao;
	@Resource
	private AcceptanceGroupDao acceptanceGroupDao;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private SysFileDao sysFileDao;
	@Resource
	private IOTableInstanceDao ioTableInstanceDao;
	@Resource
	private SignResultDao signResultDao;
	@Resource
	private PadPhotoInfoDao padPhotoInfoDao;
	@Resource
	private AcceptanceReportDao acceptanceReportDao;
	@Resource
	private ServerExportService serverExportService;
	@Resource
	private SecretLevelService secretLevelService;


	private String packageTempFolder = "";

	public void clearTempFileFolder() {
		this.packageTempFolder = "";
	}

	/**
	 * @Desc 更新验收策划审批状态
	 * @param acceptancePlanId
	 * @param status
	 */
	public void updateApproveStatus(String acceptancePlanId, String status) {
		dao.updateApproveStatus(acceptancePlanId, status);
	}
	/**
	 * @Desc 更新验收策划归档状态
	 * @param acceptancePlanId
	 * @param status
	 */
	public void updateFileStatus(String acceptancePlanId, String status) {
		dao.updateFileStatus(acceptancePlanId, status);
	}
	/**
	 * @Desc 校验策划编号重复
	 * @param
	 * @param
	 */
	public String checkPlanNumber(String acceptanceNumber) {
		return dao.checkPlanNumber(acceptanceNumber);
	}
	/**
	 * @Desc 删除数据时同时删除草稿
	 * @param
	 */
	public void topDelete(String Ids) {
		 dao.topDelete(Ids);
	}
	
	/**
	 * @Desc 更新验收策划归档审批状态
	 * @param
	 * @param status
	 */
	public void updateFileDataStatus(String businessKey, String status) {
		dao.updateFileDataStatus(businessKey, status);
	}
	
	/**
	 * @Desc 自动添加验收策划的验收组组长信息
	 * @param acceptancePlanId
	 */
	public void autoAddGroupManager(String acceptancePlanId) {
		Map<String, Object> acceptancePlan = dao.getMapById(acceptancePlanId);
		Map<String, Object> managerMap = new HashMap<>();
		managerMap.put("position", "组长");
		managerMap.put("userName", acceptancePlan.get("F_YSZZ"));
		managerMap.put("userId", acceptancePlan.get("F_YSZZID"));
		managerMap.put("acceptancePlanId", acceptancePlanId);
		acceptanceGroupDao.addGroupManager(managerMap);
	}
	
	/**
	 * @Desc 获取批次下的所有验收策划
	 * @param productBatchId
	 * @return
	 */
	public List<Map<String, Object>> getPlansByProductBatchId(String productBatchId){
		return dao.getPlansByProductBatchId(productBatchId);
	}
	
	/**
	 * @Desc 绑定生成的pdf
	 * @param
	 * @return
	 */
	public void updateFile(String id,String context) {
		dao.updateFile(id, context);
	}
	
	/**
	 * @Desc 获取id对应的策划数据
	 * @param
	 * @return
	 */
	public Map<String, Object> getPlansById(String id){
		return dao.getMapById(id);
	}
	/**
	 * @Desc 获取批次下的所有审批通过的验收策划
	 * @param productBatchId
	 * @return
	 */
	public List<Map<String, Object>> getApprovedPlansByProductBatchId(String productBatchId){
		return dao.getApprovedPlansByProductBatchId(productBatchId);
	}
	
	/**
	 * @Desc 更新验收策划-依据文件下发状态
	 * @param acceptancePlanId
	 * @param status
	 */
	public void updateFilesAssignStatus(String acceptancePlanId, String status) {
		dao.updateFilesAssignStatus(acceptancePlanId, status);
	}
	/**
	 * @Desc 策划完成归档表插入策划数据
	 * @param data
	 * @param
	 */
	public void insertfiledata(Map<String,Object> data) {
		dao.insertfiledata(data);
	}
	/**
	 * @Desc 获取id对应的策划报告
	 * @param
	 * @return
	 */
	public Map<String, Object> getReportInfo(String id){
		return dao.getReportInfo(id);
	}
	
	public void insertReportfiledata(Map<String,Object> data) {
		 dao.insertReportfiledata(data);
	}
	
	public  String getPlanId(String productName) {
		
		String number="0";
		String sql="select count(*) from W_CPYSCHBGB where F_CHBGBBH='"+productName+"'";
		return null;
	}

	/**
	 * 根据传来的planId进行整合产品验收的压缩包文件
	 * @param planId
	 * @return
	 */
    public JSONObject saveAsArchiveForCPYS(String planId) throws JAXBException {
    	//所有的文件的存储中心(xml
		ArchiveFileBean archiveFileBean=new ArchiveFileBean();

    	JSONObject msgJson=new JSONObject();
    	//获取策划
    	AcceptancePlan acceptancePlan =dao.getBeanById(planId);
    	//获取策划的依据文件信息
		List<SysFile> planBasisFileList=new ArrayList<>();
		String planBasisFileString=acceptancePlan.getYsyjwj();
		JSONArray planBasisFileJsonArray=JSONArray.fromObject(planBasisFileString);
		Integer planBasisFileCount=planBasisFileJsonArray.size();
		for (int i=0;i<planBasisFileCount;i++){
			JSONObject planBasisFileJson=planBasisFileJsonArray.getJSONObject(i);
			SysFile planBasisFile=sysFileDao.getFileByFileId(planBasisFileJson.getString("id"));
			planBasisFileList.add(planBasisFile);
		}
		//把策划的文件信息存到xml里
		archiveFileBean.setPlanBasisFileList(planBasisFileList);
		//获取实例List
		List<TableInstance> tableInstanceList=ioTableInstanceDao.getByPlanId(planId);
		//获取实例绑定的签署和照片
		List<PadPhotoInfo> padPhotoInfoList=new ArrayList<>();
		if (tableInstanceList!=null){
			for (TableInstance tableInstance:tableInstanceList){
				//获取当前实例的照片和签署
				List<PadPhotoInfo> insPadPhotoInfoList=padPhotoInfoDao.getByInstanceId(tableInstance.getId());
				//把每一个实例的文件都集中到策划节点下的实例文件集合
				if (insPadPhotoInfoList!=null){
					padPhotoInfoList.addAll(insPadPhotoInfoList);
				}

			}
		}
		//获取所有实例文件的SYSFile类型
		List<SysFile> padPhotoInfoFileList=new ArrayList<>();
		if (padPhotoInfoList!=null){
			for (PadPhotoInfo padPhotoInfo:padPhotoInfoList){
				SysFile padPhotoInfoFile=sysFileDao.getFileByFileId(padPhotoInfo.getFileId());
				padPhotoInfoFileList.add(padPhotoInfoFile);
			}
		}
		//把所有实例的签署和拍照图片存到xml里
		archiveFileBean.setInstancePhotoAndSignList(padPhotoInfoFileList);
		//获取总结
		acceptanceReport report=acceptanceReportDao.getByPlanId(planId).get(0);

		//获取总结的附件
		List<SysFile> reportBasisFileList=new ArrayList<>();
		if (report!=null){
			String reportBasisFileString=report.getFj();
			if (reportBasisFileString!=""){
				JSONArray reportBasisFileJsonArray=JSONArray.fromObject(reportBasisFileString);
				Integer reportBasisFileCount=reportBasisFileJsonArray.size();
				for (int i=0;i<reportBasisFileCount;i++){
					JSONObject reportBasisFileJson=reportBasisFileJsonArray.getJSONObject(i);
					SysFile reportBasisFile=sysFileDao.getFileByFileId(reportBasisFileJson.getString("id"));
					reportBasisFileList.add(reportBasisFile);
				}
			}
		}

		//把所有的总结依据文件存到xml里
		archiveFileBean.setReportBasisFileList(reportBasisFileList);
		//获取当前所有文件的密级
		String secret=secretLevelService.getHigherSecret(planBasisFileList,reportBasisFileList);
		//创建临时文件夹
		initTempFileFolder("文件归档压缩包",secret);
		//拷贝文件
		if (planBasisFileList!=null){
			for (SysFile file:planBasisFileList){
				accordingFileDeal(file);
			}
		}
		if (reportBasisFileList!=null){
			for (SysFile file:reportBasisFileList){
				accordingFileDeal(file);
			}
		}
		if (padPhotoInfoFileList!=null){
			for (SysFile file:padPhotoInfoFileList){
				accordingFileDeal(file);
			}
		}
		//生成xml说明文件
		String xml = XmlBeanUtil.marshall(archiveFileBean, ArchiveFileBean.class);
		String filePath=this.packageTempFolder + File.separator +archiveFileBean.getXmlName()
				+ IOConstans.FILE_XML_UNIQUE + ".xml";
		FileOperator.writeFile(filePath, xml, System.getProperty("file.encoding"));
		//生成文件压缩包
		ZipUtil.zip(this.packageTempFolder, false, "GBK");
		FileOperator.delFoldsWithChilds(this.packageTempFolder);
		clearTempFileFolder();
		msgJson.put("status","1");
		msgJson.put("msg","归档文件生成完毕");
		return msgJson;
    }

	// 依据文件处理
	public void accordingFileDeal(SysFile sysFile) throws JAXBException {
		String sourcePath = SysConfConstant.UploadFileFolder + File.separator + sysFile.getFilepath();
		String filepaths = sysFile.getFilepath();
		String strpath = "";
		String filename = "";
		if (filepaths != null || !filepaths.equals("")) {
			int pos = filepaths.lastIndexOf("\\");
			strpath = filepaths.substring(0, pos);
			filename = filepaths.substring(pos + 1, filepaths.length());
		}
		String filePath = SysFileUtil.createFilePath(this.packageTempFolder + File.separator + strpath, filename);
		/* String str=this.packageTempFolder + File.separator+strpath+"\\"; */
		FileOperator.copyFile(sourcePath, filePath);
	}

	/**
	 * 根据系统上传附件的根目录，创建临时文件夹，附带密级
	 *
	 * @param name
	 */
	public void initTempFileFolder(String name, String security) {
		this.packageTempFolder = SysConfConstant.UploadFileFolder + File.separator + "temp" + File.separator + name
				+ DateUtil.getCurrentDate("yyyyMMddHHmmss") + "(" + security + ")";
		FileOperator.createFolder(this.packageTempFolder);
	}
	/**
	 * @Description 使用当前时间为当前策划添加结束时间,如果已有结束时间,则覆盖
	 * @Deprecated 20201225需求确认,应使用updateEndTimeBySignResultOfTeamLeader从签署中提取时间作为结束时间
	 * @Author ZMZ
	 * @Date 2020/12/4 8:57
	 * @param bussinessKey
	 * @Return void
	 */
    public void updateEndTime(String bussinessKey) {
		Date endTime=new Date();
    	dao.updateEndTime(bussinessKey,endTime);
    }

    /**
     * @Description 依据组长的签署时间,为当前策划添加结束时间
     * @Author ZMZ
     * @Date 2020/12/25 10:39
     * @param planId
	 * @param signTime
     * @Return void
     */
	public void updateEndTimeBySignResultOfTeamLeader(String planId,Date signTime) {
		dao.updateEndTime(planId,signTime);
	}

	/**
	 * @Description 为当前策划添加归档时间,如果已有结束时间,则覆盖
	 * @Author ZMZ
	 * @Date 2020/12/25 10:21
	 * @param acceptancePlanId
	 * @Return void
	 */
	public void updatePdfFileTime(String acceptancePlanId) {
		Date pdfFileTime=new Date();
		dao.updatePdfFileTime(acceptancePlanId,pdfFileTime);
	}
}
