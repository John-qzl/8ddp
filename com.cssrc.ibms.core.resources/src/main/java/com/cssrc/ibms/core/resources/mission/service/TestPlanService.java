package com.cssrc.ibms.core.resources.mission.service;

import java.io.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.core.resources.io.bean.ArchiveFileBean;
import com.cssrc.ibms.core.resources.io.bean.ins.ConditionResult;
import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.template.CheckCondition;
/*import com.cssrc.ibms.core.resources.io.dao.*;*/
import com.cssrc.ibms.core.resources.io.dao.*;
import com.cssrc.ibms.core.resources.io.service.SecretLevelService;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.resources.mission.dao.DataConfirmDao;
import com.cssrc.ibms.core.resources.mission.model.DataConfirmMapToBean;
import com.cssrc.ibms.core.resources.mission.model.RangeTestPlanMapToBean;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.ZipUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.dp.form.service.FormService;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.bean.WorkBoard;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceGroupDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceMessageDao;

import com.cssrc.ibms.dp.product.acceptance.dao.WorkBoardDao;
import com.cssrc.ibms.dp.rangeTest.mission.dao.RangeTestSummaryDao;
import com.cssrc.ibms.dp.rangeTest.mission.model.RangeTestSummaryMapToBean;
import com.cssrc.ibms.dp.signModel.dao.SysSignModelDao;
import com.cssrc.ibms.dp.sync.bean.PadPhotoInfo;
import com.cssrc.ibms.dp.sync.dao.PadPhotoInfoDao;
import com.cssrc.ibms.dp.util.HtmlToPdf;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.util.SysFileUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.cssrc.ibms.core.resources.mission.dao.TestPlanDao;


/**
 * 策划任务service
 * @author zmz
 *
 */
@Service
public class TestPlanService {
	@Resource
	private TestPlanDao dao;
	@Resource
	private AcceptanceGroupDao acceptanceGroupDao;
	@Resource
	private AcceptanceMessageDao acceptanceMessageDao;
	@Resource
	private SysUserDao sysUserDao;
	@Resource
	private SysOrgDao sysOrgDao;
	@Resource
	private IOTableInstanceDao ioTableInstanceDao;
	@Autowired
	private FormService formService;
	@Resource
	private IOSignResultDao ioSignResultDao;
	@Resource
	private IOSignDefDao iOSignDefDao;
	@Resource
	private SysSignModelDao sysSignModelDao;
	@Resource
	private SysFileDao sysFileDao;
	@Resource
	private RangeTestSummaryDao rangeTestSummaryDao;
	@Resource
	private IOCheckConditionDao iocheckConditionDao;
	@Resource
	private IOConditionResultDao ioconditionResultDao;
	@Resource
	private RangeTestPlanDao rangeTestPlanDao;
	@Resource
	private WorkBoardDao workBoardDao;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private PadPhotoInfoDao padPhotoInfoDao;
	@Resource
	private SecretLevelService secretLevelService;
	@Resource
	DataConfirmDao dataConfirmDao;


	private String packageTempFolder = "";

	public void clearTempFileFolder() {
		this.packageTempFolder = "";
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
	 * 根据id获取所有审批通过的任务策划
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getApprovedPlansByModuleId(String moduleId) {
		return dao.getApprovedPlansByModuleId(moduleId);
	}

	/**
	 * 根据id获取所有审批通过的武器所检策划
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getApprovedWeaponCheckPlansByModuleId(String moduleId) {
		return dao.getApprovedWeaponCheckPlansByModuleId(moduleId);
	}


	/**
	 * 更新任务流程状态
	 * @param acceptancePlanId
	 * @param status
	 */
	public void updateApproveStatus(String acceptancePlanId, String status) {
		//更新状态
		dao.updateApproveStatus(acceptancePlanId, status);

		if ("审批通过".equals(status)){
			//发送消息给组长组员发起人
			sendMassageToRangeTestPlan(acceptancePlanId);
			//写看板
			insertWorkboard(acceptancePlanId);
			//保存组长信息
			/*saveTeamLeader(acceptancePlanId);*/
/*			Map<String,Object> rangeTestPlan=dao.getPlanById(acceptancePlanId).get(0);
			String userId=rangeTestPlan.get("F_SYFZRID").toString();
			SysUser sysUser=sysUserDao.getById(Long.valueOf(userId));
			acceptanceMessageDao.insertMessageByRangeTestPlan(sysUser,rangeTestPlan);*/
		}
	}

	/**
	 * 向工作项看板写入本策划信息
	 * @param missionId
	 */
	private void insertWorkboard(String missionId) {
		WorkBoard workBoard=new WorkBoard();
		Long workBoardId=UniqueIdUtil.genId();
		workBoard.setId(workBoardId.toString());
		//定位策划
		Map<String,Object> rangeTestPlan=dao.getPlanById(missionId).get(0);
		//策划id
		workBoard.setZjID(rangeTestPlan.get("ID").toString());
		//类型默认为3  是靶场
		workBoard.setLx("3");
		workBoard.setGzm(rangeTestPlan.get("F_CHBGBBH").toString()+"策划");
		//当前状态
		workBoard.setDqzt("策划审批通过");
		//下一步
		workBoard.setXyb("下发表单到PAD");
		//组长和组长id
		String teamLeaderId=rangeTestPlan.get("F_SYZZid").toString();
		workBoard.setZzId(teamLeaderId);
		String teamLeader=rangeTestPlan.get("F_SYZZ").toString();
		workBoard.setZz(teamLeader);
		//发起人和发起人id
		workBoard.setFqr(rangeTestPlan.get("F_FQR").toString());
		workBoard.setFqrId(rangeTestPlan.get("F_FQRID").toString());
		workBoardDao.insert(workBoard);
	}

	/**
	 * 给靶场策划相关的人发消息说策划通过了
	 * 如果一人多角色,只发一次消息  优先级为  组长>组员>发起人
	 * @param missionId
	 */
	public void sendMassageToRangeTestPlan(String missionId){
		//定位策划
		Map<String,Object> rangeTestPlan=dao.getPlanById(missionId).get(0);
		//定位组员表
		List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByMissionId(missionId);
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			SysUser teamMember=sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
			if ("组长".equals(acceptanceGroup.getZw())){
				//是组长   直接发消息
				acceptanceMessageDao.insertMessageByRangeTestPlan(teamMember,rangeTestPlan,"组长");
			}else {
				//不是组长
				//先看下这个人在组里是不是有担任组长的同时担任组员
				if (curUserIsTeamLeader(acceptanceGroupList,acceptanceGroup.getXmId())){
					//当前这个人既是组员也是组长
					//组长的消息是上面发的,这里直接跳过
				}else {
					//当前这个人不是组长,直接发消息
					acceptanceMessageDao.insertMessageByRangeTestPlan(teamMember,rangeTestPlan,"组员");
				}
			}
		}
		//确定发起人
		String sponsorId=rangeTestPlan.get("F_FQRID").toString();
		SysUser sponsor=sysUserDao.getById(Long.valueOf(sponsorId));
		if (curUserInTeam(acceptanceGroupList,sponsorId)){
			//发起人属于组员/组长
			//该消息已由上面发送
		}else {
			//发起人不属于组员/组长
			acceptanceMessageDao.insertMessageByRangeTestPlan(sponsor,rangeTestPlan,"发起人");
		}
	}


	/**
	 * 返回策划对应的型号id
	 * @param acceptancePlanId
	 * @return
	 */
    public String getModuleidByPlanid(String acceptancePlanId) {
    	return dao.getModuleidByPlanid(acceptancePlanId);
    }

	/**
	 * 通过策划id获取策划全部信息
	 * @param missionId
	 * @return
	 */
	public List<Map<String, Object>> getPlanById(String missionId) {
		return dao.getPlanById(missionId);
	}

	/**
	 * excel导入组员信息
	 * @param inputStream
	 * @param missionId
	 * 表格格式  岗位  姓名  单位  负责项目
	 */
    public String importTeamInfo(InputStream inputStream, String missionId) throws IOException, InvalidFormatException {
		JSONObject jsonObject=new JSONObject();
		List<Map<String,String>> teamList=new ArrayList<>();
		Map<String,String> teamInfo;
		Workbook workbook=null;
		String modelType="";
		workbook= WorkbookFactory.create(inputStream);
		//获取第一个表单
		Sheet sheet=workbook.getSheetAt(0);
		//获取行数
		Integer totalRow=sheet.getLastRowNum();
		//获取列数
		Integer totalColumn=sheet.getRow(0).getPhysicalNumberOfCells();

		//遍历表头,是否符合填写要求
		for (int j=0;j<totalColumn;j++){
			//从左到右 遍历第一行每一列
			sheet.getRow(0).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
			String data = sheet.getRow(0).getCell(j).getStringCellValue();
			switch (j){
				/*case 0:if (!"岗位".equals(data)){
					return "{\"success\":\"false\",\"context\":\"表格内容需严格符合以下顺序:岗位-姓名-单位-负责项目\"}";
				}
					break;*/
				case 0:if (!"姓名".equals(data)){
					return "{\"success\":\"false\",\"context\":\"表格内容需严格符合以下顺序:姓名-负责项目\"}";
				}
					break;
				/*case 2:if (!"单位".equals(data)){
					return "{\"success\":\"false\",\"context\":\"表格内容需严格符合以下顺序:岗位-姓名-单位-负责项目\"}";
				}
					break;*/
				case 1:if (!"负责项目".equals(data)){
					return "{\"success\":\"false\",\"context\":\"表格内容需严格符合以下顺序:姓名-负责项目\"}";
				}
					break;
			}
		}
		//开始取数据
		for (int i=1;i<=totalRow;i++){
			//每一行都是一个独立的组员信息
//			AcceptanceGroup acceptanceGroup=new AcceptanceGroup();
			//表格格式  姓名  负责项目
//			acceptanceGroup.setId(String.valueOf(UniqueIdUtil.genId()));
//			acceptanceGroup.setSSBCCH(missionId);
			teamInfo=new HashMap<>();
			for (int j=0;j<totalColumn;j++){
				//从上到下 只判断第一列
				try{
					sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
				}catch (NullPointerException e){
					if (j==0){
						//当前取的是名字
						e.printStackTrace();
						return "{\"success\":\"false\",\"context\":\"第"+(i+1)+"行,第"+(j+1)+"列为空！\"}";
					}else {
						//当前取的是工作项
						teamInfo.put("fzxm",null);
						continue;
					}

				}

				String data = sheet.getRow(i).getCell(j).getStringCellValue();
				switch (j){
					//case 0:teamInfo.put("gw",data); break;
					case 0:teamInfo.put("xm",data);
						List<? extends ISysUser> sysUser=sysUserDao.getByFullname(data);
						if (sysUser.size()==0){
							return "{\"success\":\"false\",\"context\":\""+data+" 用户不存在\"}";
						}else {
							teamInfo.put("userId", sysUser.get(0).getUserId().toString());
							//存放用户单位
							teamInfo.put("dw",sysOrgService.getPrimaryOrgByUserId(sysUser.get(0).getUserId()).getOrgName());
							teamInfo.put("dwId",sysOrgService.getPrimaryOrgByUserId(sysUser.get(0).getUserId()).getOrgId().toString());
							//存放用户岗位
							teamInfo.put("gw","组员");
						}
					break;
					/*case 2:teamInfo.put("dw",data);
					List<? extends SysOrg> sysOrg=sysOrgDao.getByOrgName(data);
					if (sysOrg.size()==0){
						return "{\"success\":\"false\",\"context\":\""+data+" 单位不存在\"}";
					}else {
						teamInfo.put("dwId", sysOrg.get(0).getOrgId().toString());
					}
					break;*/
					case 1:teamInfo.put("fzxm",data); break;
					/*case 1:acceptanceGroup.setZw(data);break;
					case 2:acceptanceGroup.setXm(data);break;
					case 3:acceptanceGroup.setDw(data);break;
					case 4:acceptanceGroup.setFzxm(data);break;*/
				}
			}

			teamList.add(teamInfo);
			//遍历完一行信息了,该存到数据库了
//			acceptanceGroupDao.insert(acceptanceGroup);
		}
		jsonObject.put("teamList",teamList);
		jsonObject.put("success","true");
		//"{\"success\":\"true\"}"
		String teamListJson=jsonObject.toString();
		return teamListJson;
    }

	/**
	 * 更新靶场数据确认的流程状态
	 * @param acceptancePlanId
	 * @param status
	 */
	public void updateDataConfirmStatus(String acceptancePlanId, String status) {
		//更新数据确认状态
		dao.updateDataConfirmStatus(acceptancePlanId,status);

		//如果审批通过
		if ("审批通过".equals(status)){
			//更新策划的pdf状态
			String planId=rangeTestSummaryDao.getConfirmDataById(acceptancePlanId).get("F_SSCH").toString();
			rangeTestPlanDao.updatePDFStatus(planId);
			//,则发消息给相关人员
			sendMassageToRangeTestSummary(acceptancePlanId);
			//改变看板内容
			workBoardDao.updatework(planId,"数据确认完毕","生成PDF归档");
		}
	}

	/**
	 * 给数据确认相关的人发消息说策划通过了
	 * 如果一人多角色,只发一次消息  优先级为  组长>组员>发起人
	 * @param missionId
	 */
	private void sendMassageToRangeTestSummary(String missionId) {
		//定位策划
		Map<String,Object> rangeTestSummary=rangeTestSummaryDao.getConfirmDataById(missionId);

		//定位组员表
		List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByMissionId(missionId);
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			SysUser teamMember=sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
			if ("组长".equals(acceptanceGroup.getZw())){
				//是组长   直接发消息
				acceptanceMessageDao.insertMessageByRangeTestSummary(teamMember,rangeTestSummary,"组长");
			}else {
				//不是组长
				//先看下这个人在组里是不是有担任组长的同时担任组员
				if (curUserIsTeamLeader(acceptanceGroupList,acceptanceGroup.getXmId())){
					//当前这个人既是组员也是组长
					//组长的消息是上面发的,这里直接跳过
				}else {
					//当前这个人不是组长,直接发消息
					acceptanceMessageDao.insertMessageByRangeTestSummary(teamMember,rangeTestSummary,"组员");
				}
			}
		}
		//确定发起人
		String sponsorId=rangeTestSummary.get("F_FQRID").toString();
		SysUser sponsor=sysUserDao.getById(Long.valueOf(sponsorId));
		if (curUserInTeam(acceptanceGroupList,sponsorId)){
			//发起人属于组员/组长
			//该消息已由上面发送
		}else {
			//发起人不属于组员/组长
			acceptanceMessageDao.insertMessageByRangeTestSummary(sponsor,rangeTestSummary,"发起人");
		}
	}

	private boolean curUserInTeam(List<AcceptanceGroup> acceptanceGroupList, String sponsorId) {
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
				if (acceptanceGroup.getXmId().equals(sponsorId)){
					//传来的这个人是组长/组员
					return true;
				}
		}
		//找完队列也没有找到
		//说明这个人不在组员表里
		return false;
	}

	/**
	 * 看看当前这个人是不是组长
	 * 如果是的话返回true
	 * @param acceptanceGroupList
	 * @return
	 */
	public boolean curUserIsTeamLeader(List<AcceptanceGroup> acceptanceGroupList,String xmId) {
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			if ("组长".equals(acceptanceGroup.getZw())){
				//先确定当前组的组长的id
				if (acceptanceGroup.getXmId().equals(xmId)){
					//传来的这个人是组长没跑了
					return true;
				}
			}
		}
		//找完队列也没有找到
		//说明这个人不担任组长
		return false;
	}

	/**
	 * 更新靶场数据确认的子流程状态
	 * @param acceptancePlanId
	 * @param status
	 */
	public void updateDataConfirmSubprocessStatus(String acceptancePlanId, String status) {
		dao.updateDataConfirmSubprocessStatus(acceptancePlanId,status);
		if ("审批通过".equals(status)){
			//回过去改一下策划的归档状态,让pdf页面可以看到这个策划
			rangeTestPlanDao.updatePDFStatus(acceptancePlanId);
		}
	}

	/**
	 * 获取当前策划对应的组长,存放在组员表里
	 * @param acceptancePlanId
	 */
    public void saveTeamLeader(String acceptancePlanId) {
		Map<String,Object> map=dao.getPlanByIdToMap(acceptancePlanId);
    	if(map==null){
			System.out.println("在CPYSZB新增组长记录时,获取不到策划里的组长");
			return;
		}
		acceptanceGroupDao.addRangeTestPlanGroupManager(map.get("F_SYZZ").toString(),
				map.get("F_SYZZID").toString(),
				map.get("ID").toString(),
				map.get("F_SYDW").toString(),
				map.get("F_SYDWID").toString());
    }

	/**
	 * 根据策划id找出所有实例转为pdf并返回文件流
	 * @param missionId
	 * @return
	 */
	public List<InputStream> transferAllInstanceToPDF(String missionId) throws FileNotFoundException {
		//文件上传地址(全局变量
		//String filePath=sysParameterDao.getById(4686L).getParamvalue();
		String filePath=AppUtil.getAttachPath();
		List<InputStream> pdfs=new ArrayList<>();
		List<TableInstance> tableInstanceList=ioTableInstanceDao.getByPlanIdExceptFile(missionId);
		//文件计数器
		Integer fileCount=0;
		for (TableInstance tableInstance:tableInstanceList){
			//排除掉废弃的实例
			if ("已完成".equals(tableInstance.getStatus())){
				fileCount++;
				//新建文件
				String htmlPath=filePath+"\\temp\\htmlToPDF\\"+fileCount+".html";
				File fileDir=new File(filePath+"\\temp\\htmlToPDF\\");
				if (!fileDir.exists()){
					//如果路径不存在,则创建路径
					fileDir.mkdir();
				}
//				String htmlPath="D:\\ibms\\attachFile\\temp\\htmlToPDF\\"+fileCount+".html";
				File file=new File(htmlPath);
				//如果文件存在,直接删掉
				if (file.exists()){
					file.delete();
				}
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("靶场试验抽取实例内容生成html时文件创建出错!错误码:257257");
				}
				if (!file.exists()){
					System.out.println("靶场试验抽取实例内容生成html时文件创建出错!错误码:260260");
				}
				//把实例的内容写入到文件
				try {
					OutputStreamWriter outputStream=new OutputStreamWriter(new FileOutputStream(file),"GBK");
					//处理实例的html的内容使之规范可以被转pdf
					String html= splicHtml(tableInstance.getContent());
					//拼接检查项
					String htmlWithCkeckRes=splicCheckRes(html,tableInstance.getId());
					//拼接签章
					String htmlWithSignModel=splicSignModel(htmlWithCkeckRes,tableInstance.getId());
					//写入文件
					outputStream.append(htmlWithSignModel);
					outputStream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("靶场试验抽取实例内容生成html时文件创建出错!错误码:265265");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("靶场试验抽取实例内容写入html时文件出错!错误码:270270");
				}

//				String PDFPath="D:\\ibms\\attachFile\\temp\\htmlToPDF\\"+fileCount+".pdf";
				String PDFPath=filePath+"\\temp\\htmlToPDF\\"+fileCount+".pdf";
				//如果pdf已存在,则删除文件重新生成
				File PDFfile=new File(PDFPath);
				if (PDFfile.exists()){
					PDFfile.delete();
				}
				////html转pdf
				HtmlToPdf.convert(htmlPath,PDFPath);
				//操作结束,删除html,只保留pdf
		//		file.delete();
				pdfs.add(new FileInputStream(new File(PDFPath)));
			}
		}
		return pdfs;
    }

	/**
	 * 拼接检查项
	 * @param html
	 * @return
	 */
	private String splicCheckRes(String html,String instanceId) {
		//截取table之前的内容  放在实例的上面
		String subHtmlHead=html.substring(0,html.indexOf("<body>")+6);
		String subHtmlLeft=html.substring(html.indexOf("<body>")+6);
		//根据实例id找检查项
		List<ConditionResult> conditionResultList=ioconditionResultDao.getByInsId(instanceId);
		//如果没有检查项,直接返回原网页
		if (conditionResultList.size()==0){
			return html;
		}
		//存检查结果的html
		String conditionHtml="<table width=\"100%\" ><tr><td>检查项</td><td>内容</td></tr>";
		for (ConditionResult conditionResult:conditionResultList){
			//根据ConditiionId找检查项描述
			CheckCondition checkCondition=iocheckConditionDao.getById(conditionResult.getCondition_id());
			conditionHtml=conditionHtml+"<tr><td>"+checkCondition.getName()+"</td><td>"+conditionResult.getValue()+"</td></tr>";
		}
		//拼头拼尾
		String htmlWithCheckRes=subHtmlHead+conditionHtml+"<br/>"+subHtmlLeft;
		return htmlWithCheckRes;
	}

	/**
	 * 拼接签章
	 * @param html
	 * @return
	 */
	private String splicSignModel(String html,String instanceId) {
		/**
		 * file里的dataId是signres的id的图片
		 */
		//截取</table>之前的内容
		String subHtml=html.substring(0,html.indexOf("</table>")+8);
		//实例id找签署结果id
		List<SignResult> signResultList=ioSignResultDao.getByInstantId(instanceId);
		//如果没有签署,直接返回原网页
		if (signResultList.size()==0){
			return html;
		}
		//存签署的html
		String signModelHtml="<table><tr><td>姓名</td><td>签署</td></tr>";
		for (SignResult signResult:signResultList){
			//取到签署的人(不一定是全名,还可能有前缀
			String signDefName=iOSignDefDao.getById(signResult.getSigndef_id()).getName();
			//拼接html
			signModelHtml=signModelHtml+"<tr><td>"+signDefName+"</td>";
			/*
			//确定当前人的真实姓名
			String realName="";
			//如果有组长或者组员的前缀____括号里的是真实姓名
			if (signDefName.indexOf("组长")!=-1||signDefName.indexOf("组员")!=-1){
				realName=signDefName.substring(signDefName.indexOf("(")+1,signDefName.indexOf(")"));
			}else {
				//直接就是真实姓名
				realName=signDefName;
			}
			//根据真实姓名获取userID
			String userId=sysUserDao.getByFullname(realName).get(0).getUserId().toString();
			//根据userid获取签章id
			String signModelFileId=sysSignModelDao.selectByUserId(userId).getImg_Path();
			*/

			//根据signModelId获取签章地址
			String signModelFilePath=sysFileDao.getFileByDataId(signResult.getId()).getFilepath();
			//获取图片绝对地址
//			String imgPath=filePath+"\\"+signModelFilePath;
			String imgPath="D:\\ibms\\attachFile\\"+signModelFilePath;
			//拼接html
			signModelHtml=signModelHtml+"<td><img height=80 width=120 src=\""+imgPath+"\"></td></tr>";
		}
		signModelHtml=signModelHtml+"</table>";
		//补上html结束标签
		String htmlWithSignModel=subHtml+"<br/>"+signModelHtml+"</body></html>";
		return htmlWithSignModel;
	}

	/**
	 * 改头部编码,加table格式
	 * @param content
	 * @return
	 */
	private String splicHtml(String content) {
		String noInputHtml=formService.conversionHTML(content);
		//将html标签之前的直接剪去,方便后面直接拼css和编码
		noInputHtml=noInputHtml.substring(noInputHtml.indexOf("html>")+5);
		String head="<html><meta http-equiv=\"content-type\" content=\"text/html;charset=GBK\"><style media=\"screen\">td {border:1px solid black;} table{border:1px solid black;} </style>";
		String html=head+noInputHtml;
		return html;
	}

	/**
	 * 判断转pdf的前置条件是否满足
	 * @param missionId
	 * @return
	 */
	public net.sf.json.JSONObject checkIfCouldConvertToPDF(String missionId) {
		net.sf.json.JSONObject jsonObject=new net.sf.json.JSONObject();
		Map<String,Object> map=rangeTestSummaryDao.getConfirmDataByPlanId(missionId);
		//没有发起数据确认
		if (map==null){
			jsonObject.put("msg","未进行数据确认,无法归档!");
			jsonObject.put("status", "2");
		}else {
			if ("正在审批".equals(map.get("F_SPZT").toString())||
			"审批中".equals(map.get("F_SPZT"))){
				//数据确认审批中
				jsonObject.put("msg","数据确认进行中,无法归档!");
				jsonObject.put("status", "2");
			}else if ("审批通过".equals(map.get("F_SPZT").toString())){
				//审批通过
				//成功提示语等归档完成再填入
				jsonObject.put("status", "1");
			}else {
				//防止不规范的结果,如审批失败,审批驳回等(防御性编程
				jsonObject.put("msg","无法归档,错误码417417,请联系管理员!");
				jsonObject.put("status", "2");
			}
		}
		return jsonObject;
	}

	/**
	 * 这是点击归档生成pdf之后的文件
	 * 在file表里注册当前文件并保存在相应的策划表里
	 * @param missionId
	 * @param relativePDFAddress
	 */
    public void savePDFfileInfo(String missionId, String relativePDFAddress) {
		SysFile sysFile=new SysFile();
		sysFile.setFileId(UniqueIdUtil.genId());
		sysFile.setFilename("数据确认PDF");
		sysFile.setFilepath(relativePDFAddress);
		sysFile.setCreatetime(new Date());
		sysFile.setFileType("归档文件");
		sysFile.setExt(".pdf");
		sysFile.setCreator(UserContextUtil.getCurrentUser().getFullname());
		//注册文件
		sysFileDao.insertFileInfo(sysFile);
		//[{"id":"10000031480146","name":"文件(机密).txt"}]
		String fileInfo="[{\"id\":\""+sysFile.getFileId()+"\",\"name\":\""+sysFile.getFilename()+"\"}]";
		//把文件带给策划数据
		rangeTestPlanDao.updatePDF(fileInfo,missionId);
    }

	/**
	 * 获取下一个靶场策划的编号的尾号
	 * @param xhId
	 * @return
	 */
	public String generatorReportNumber(String xhId) {
		//加1是下一个编号

		String nextNumber=dao.getNextRangeTestPlanNumber(xhId);
		return nextNumber;
	}

	/**
	 * 为靶场试验和武器所检生成归档压缩包
	 * @param planId
	 * @return
	 */
    public JSONObject saveAsArchiveForBCSY(String planId) throws ParseException, JAXBException {
		//所有的文件的存储中心(xml
		ArchiveFileBean archiveFileBean=new ArchiveFileBean();

		JSONObject msgJson=new JSONObject();
		//策划信息
		RangeTestPlanMapToBean rangeTestPlanMapToBean=dao.getById(planId);
		//获取策划依据文件
		List<SysFile> planBasisFileList=new ArrayList<>();
		String planBasisFileString=rangeTestPlanMapToBean.getSyyjwj();
		JSONArray planBasisFileJsonArray=JSONArray.parseArray(planBasisFileString);
		Integer planBasisFileCount=planBasisFileJsonArray.size();
		for (int i=0;i<planBasisFileCount;i++){
			JSONObject planBasisFileJson=planBasisFileJsonArray.getJSONObject(i);
			SysFile planBasisFile=sysFileDao.getFileByFileId(planBasisFileJson.getString("id"));
			planBasisFileList.add(planBasisFile);
		}
		//把策划的文件信息存到xml里
		archiveFileBean.setPlanBasisFileList(planBasisFileList);

		//获取实例
		List<TableInstance> tableInstanceList=ioTableInstanceDao.getByPlanId(planId);
		//获取实例绑定的签署和照片
		List<PadPhotoInfo> padPhotoInfoList=new ArrayList<>();
		if (tableInstanceList!=null){
			for (TableInstance tableInstance:tableInstanceList){
				//获取当前实例的照片和签署
				List<PadPhotoInfo> insPadPhotoInfoList=padPhotoInfoDao.getByInstanceId(tableInstance.getId());
				if (insPadPhotoInfoList!=null){
					padPhotoInfoList.addAll(insPadPhotoInfoList);
				}

			}
		}
		//获取所有实例文件的sysFile类型
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
		RangeTestSummaryMapToBean rangeTestSummaryMapToBean=rangeTestSummaryDao.getById(planId);
		//获取总结的子流程的附件
		List<SysFile> needToDoFileList=new ArrayList<>();
		//获取子流程bean
		if (rangeTestSummaryMapToBean!=null){
			String reportId=rangeTestSummaryMapToBean.getId();
			List<DataConfirmMapToBean> dataConfirmMapToBeanList=dataConfirmDao.getNeedTodoByReportId(reportId);
			if (dataConfirmMapToBeanList!=null){
				//获取每个子流程的附件
				for (DataConfirmMapToBean dataConfirmMapToBean:dataConfirmMapToBeanList){
					String needToDoFileString=dataConfirmMapToBean.getFj();
					if (needToDoFileString!=""){
						JSONArray reportBasisFileJsonArray=JSONArray.parseArray(needToDoFileString);
						Integer reportBasisFileCount=reportBasisFileJsonArray.size();
						for (int i=0;i<reportBasisFileCount;i++){
							JSONObject reportBasisFileJson=reportBasisFileJsonArray.getJSONObject(i);
							SysFile reportBasisFile=sysFileDao.getFileByFileId(reportBasisFileJson.getString("id"));
							needToDoFileList.add(reportBasisFile);
						}
					}

				}
			}
		}
		//把所有的总结依据文件存到xml里
		archiveFileBean.setReportBasisFileList(needToDoFileList);

		//获取当前所有文件的密级
		String secret=secretLevelService.getHigherSecret(planBasisFileList,needToDoFileList);
		//创建临时文件夹
		initTempFileFolder("文件归档压缩包",secret);
//拷贝文件
		if (planBasisFileList!=null){
			for (SysFile file:planBasisFileList){
				accordingFileDeal(file);
			}
		}
		if (needToDoFileList!=null){
			for (SysFile file:needToDoFileList){
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

	/**
	 * @Desc 删除数据时同时删除草稿
	 * @param
	 */
	public void topDelete(String Ids,String ofPart) {
		dao.topDelete(Ids,ofPart);
	}

	/**
	 * @Description 修改当前策划的结束时间为现在
	 * @Author ZMZ
	 * @Date 2020/12/4 9:41
	 * @param missionId
	 * @Return void
	 */
    public void updateEndTime(String missionId) {
    	Date endTime=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String endDate=sdf.format(endTime);
    	dao.updateEndTime(missionId,endDate);
    }
}
