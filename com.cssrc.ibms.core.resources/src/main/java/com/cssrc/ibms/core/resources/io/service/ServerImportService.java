package com.cssrc.ibms.core.resources.io.service;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.resources.datapackage.dao.*;
import com.cssrc.ibms.core.resources.datapackage.model.DataPackage;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.core.resources.io.bean.*;
import com.cssrc.ibms.core.resources.io.bean.datapackageModel.AcceptanceplanModel;
import com.cssrc.ibms.core.resources.io.bean.datapackageModel.ModelBean;
import com.cssrc.ibms.core.resources.io.bean.datapackageModel.OrgUserInfo;
import com.cssrc.ibms.core.resources.io.bean.datapackageModel.RangeTestPlanModel;
import com.cssrc.ibms.core.resources.io.bean.ins.CheckResult;
import com.cssrc.ibms.core.resources.io.bean.ins.ConditionResult;
import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.pack.SimplePackage;
import com.cssrc.ibms.core.resources.io.bean.template.CheckCondition;
import com.cssrc.ibms.core.resources.io.bean.template.CheckItemDef;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import com.cssrc.ibms.core.resources.io.bean.template.TemplateFLoder;
import com.cssrc.ibms.core.resources.io.dao.*;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.resources.mission.dao.TestPlanDao;
import com.cssrc.ibms.core.resources.product.bean.ModuleManage;
import com.cssrc.ibms.core.resources.product.dao.ModuleDao;
import com.cssrc.ibms.core.resources.product.dao.ProductCategoryBatchDao;
import com.cssrc.ibms.core.resources.product.dao.ProductDao;
import com.cssrc.ibms.core.resources.product.service.ModuleManageService;
import com.cssrc.ibms.core.resources.project.dao.ProjectDao;
import com.cssrc.ibms.core.user.dao.*;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysRole;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.user.model.UserRole;
import com.cssrc.ibms.core.user.service.SysRoleService;
import com.cssrc.ibms.core.user.service.UserRoleService;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.ZipUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.dp.form.dao.*;
import com.cssrc.ibms.dp.form.model.CkConditionResult;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.bean.ActualData;
import com.cssrc.ibms.dp.product.acceptance.bean.ProductInfo;
import com.cssrc.ibms.dp.product.acceptance.bean.WorkBoard;
import com.cssrc.ibms.dp.product.acceptance.dao.*;
import com.cssrc.ibms.dp.product.infor.dao.ProductInforDao;
import com.cssrc.ibms.dp.signModel.dao.CwmSysFileDao;
import com.cssrc.ibms.dp.signModel.dao.PADreturnSignModelDao;
import com.cssrc.ibms.dp.signModel.dao.SysSignModelDao;
import com.cssrc.ibms.dp.signModel.entity.CwmSysFile;
import com.cssrc.ibms.dp.signModel.entity.CwmSysSignModel;
import com.cssrc.ibms.dp.signModel.entity.SignModelDataPackage;
import com.cssrc.ibms.dp.signModel.entity.WPadhcqzb;
import com.cssrc.ibms.dp.sync.bean.Conventional;
import com.cssrc.ibms.dp.sync.bean.PadPhotoInfo;
import com.cssrc.ibms.dp.sync.dao.PadPhotoInfoDao;
import com.cssrc.ibms.dp.sync.util.PasswordUtil;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.util.SysFileUtil;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import javax.xml.bind.JAXBException;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ServerImportService {
	protected Logger logger = LoggerFactory.getLogger(ServerImportService.class);

	@Resource
	private ProductDao productDao;
	@Resource
	private ProjectDao projectDao;
	@Resource
	private DataPackageDao dataPackageDao;
	@Resource
	private PackageDao packageDao;
	@Resource
	private IOTemplateFLoderDao iOTemplateFLoderDao;
	@Resource
	private ServerExportService serverExportService;
	@Resource
	private IOTableTempDao iOTableTempDao;
	@Resource
	private IOSignDefDao iOSignDefDao;
	@Resource
	private IOCheckConditionDao iOCheckConditionDao;
	@Resource
	private SysFileDao sysFileDao;
	@Resource
	private AcceptancePlanDao acceptancePlanDao;
	@Resource
	private TeamDao teamDao;
	@Resource
	private ProductCategoryBatchDao productCategoryBatchDao;
	@Resource
	private ModuleDao moduleDao;
	@Resource
	private IOCheckItemDefDao ioCheckItemDefDao;
	@Resource
	AcceptanceGroupDao acceptanceGroupDao;
	@Resource
	IOTableInstanceDao instanceDao;
	@Resource
	IOSignResultDao ioSignResultDao;
	@Resource
	CkConditionResultDao ckConditionResultDao;
	@Resource
	FileDataDao fileDataDao;
	@Resource
	ProductInforDao productInforDao;
	@Resource
	SysUserDao sysUserDao;
	@Resource
	SysOrgDao sysOrgDao;
	@Resource
	PositionDao positionDao;
	@Resource
	UserPositionDao userPositionDao;
	@Resource
	ActualDataDao actualDataDao;
	@Resource
	IOConventionalDao ioConventionalDao;
	@Resource
	WorkBoardDao workBoardDao;
	@Resource
	private PADreturnSignModelDao pADreturnSignModelDao;
	@Resource
	private SysSignModelDao sysSignModelDao;
	@Resource
	private CwmSysFileDao cwmSysFileDao;
	@Resource
	private RangeTestPlanDao rangeTestPlanDao;
	@Resource
	private ModuleManageService moduleManageService;
	@Resource
	private IOTableInstanceDao ioTableInstanceDao;
	@Resource
	private CheckResultDao checkResultDao;
	@Resource
	private CheckResultBeanDao checkResultBeanDao;
	@Resource
	IOSignResultDao iOSignResultDao;
	@Resource
	private IOConditionResultDao iOConditionResultDao;
	@Resource
	private UserRoleService userRoleService;
	@Resource
	private SysRoleService sysRoleService;
	@Resource
	private UserRoleDao userRoleDao;
	@Resource
	private ImportHistoryService importHistoryService;
	@Resource
	private PadPhotoInfoDao padPhotoInfoDao;
	@Resource
	private AcceptanceMessageDao acceptanceMessageDao;
	@Resource
	private TestPlanDao testPlanDao;
	
	private String packageTempFolder = "";

	private void clearTempFileFolder() {
		this.packageTempFolder = "";
	}

	private void initTempFileFolder(String name) {
		this.packageTempFolder = SysConfConstant.UploadFileFolder + File.separator + "temp" + File.separator + name
				+ DateUtil.getCurrentDate("yyyyMMddHHmmss");
		FileOperator.createFolder(this.packageTempFolder);
	}

	/**
	 * @Author shenguoliang
	 * @Description: 服务器导入(数据包节点导入)
	 * @Params [file, fcId, xhId]
	 * @Date 2018/6/9 17:16
	 * @Return java.lang.String
	 */
	/*
	 * public ResultMessage importPakage(MultipartFile file, String fcId, String
	 * xhId) throws Exception { ResultMessage message = null; StringBuffer log = new
	 * StringBuffer(); FileInputStream is = null; try {
	 * initTempFileFolder(IOConstans.IMPORT_FLODER_NAME); ZipUtil.unZipFile(file,
	 * this.packageTempFolder); File mainFile =
	 * findMainFile(this.packageTempFolder); is = new FileInputStream(mainFile);
	 * Project project = XmlBeanUtil.unmarshall(is, Project.class);
	 * //数据库同步(只进行新增和更新操作) message = this.asynDb(project, fcId, log, xhId); } catch
	 * (Exception e) { e.printStackTrace(); message = new
	 * ResultMessage(ResultMessage.Fail, e.getMessage());
	 * 
	 * } finally { if (is != null) { is.close(); } clearTempFileFolder(); } return
	 * message; }
	 */

	/**
	 * 靶场试验 全部任务 数据导入 rangeTestPlanDataImport.jsp 数据导入逻辑
	 * 这个是pad把数据回传、中转机导出了验收数据，到服务器接收数据的逻辑
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public ResultMessage rangeTestPlanDataImport(MultipartFile file) throws Exception {
		ResultMessage message = null;
		StringBuffer log = new StringBuffer();
		FileInputStream is = null;
		try {
			initTempFileFolder(IOConstans.IMPORT_FLODER_NAME);
			//解压文件
			ZipUtil.unZipFile(file, this.packageTempFolder);
			//定位xml文件
			File mainFile = findMainFile(this.packageTempFolder);
			//取压缩包密级
			String archiveOriginalFileName=file.getOriginalFilename();
			String secretLevel=archiveOriginalFileName.substring(archiveOriginalFileName.indexOf("(")+1,
					archiveOriginalFileName.indexOf(")"));
			is = new FileInputStream(mainFile);
			//xml转entity
			RangeTestPlanBeanOuter outer=XmlBeanUtil.unmarshall(is, RangeTestPlanBeanOuter.class);
			//同步数据
			message = this.rangeTestPlanDataSynDb(outer,log,secretLevel);

		}catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());

		} finally {
			if (is != null) {
				is.close();
			}
			clearTempFileFolder();
		}
		return message;
	}
	
	public ResultMessage serverImportInstance(MultipartFile file) throws Exception {
		ResultMessage message = null;
		StringBuffer log = new StringBuffer();
		FileInputStream is = null;
		try {
			initTempFileFolder(IOConstans.IMPORT_FLODER_NAME);
			ZipUtil.unZipFile(file, this.packageTempFolder);
			File mainFile = findMainFile(this.packageTempFolder);
			int pos=this.packageTempFolder.lastIndexOf("(");
			String security=this.packageTempFolder.substring(pos+1,this.packageTempFolder.length()-1);
			is = new FileInputStream(mainFile);
			TranserDataBean transerDataBean=XmlBeanUtil.unmarshall(is, TranserDataBean.class);
			message = this.serverDataSynDb(transerDataBean,log,security);
			
		}catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());

		} finally {
			if (is != null) {
				is.close();
			}
			clearTempFileFolder();
		}
		return message;
	}

	/**
	 * 把传来的压缩包的内容更新到本地
	 * 		这是靶场试验策划表单下发的导入按钮事件的后台
	 * 	by zmz
	 * 	20200826
	 * @param multipartFile
	 * @return
	 */
	public ResultMessage importRangeTestPackagedata(MultipartFile multipartFile) {
		ResultMessage message=null;
		StringBuffer log=new StringBuffer();
		FileInputStream in=null;
		//临时文件夹目录
		initTempFileFolder(IOConstans.IMPORT_FLODER_NAME);
		try {
			//解压文件
			//别问为什么抛exception,这个借口就是这么抛的
			ZipUtil.unZipFile(multipartFile,this.packageTempFolder);
			//取压缩包密级
			String archiveOriginalFileName=multipartFile.getOriginalFilename();
			String secretLevel=archiveOriginalFileName.substring(archiveOriginalFileName.indexOf("(")+1,
					archiveOriginalFileName.indexOf(")"));
			//找到解压后的以 ###.xml 结尾的文件
			File file=findMainFile(this.packageTempFolder);
			in=new FileInputStream(file);
			//xml转类
			RangeTestPlanModel rangeTestPlanModel=XmlBeanUtil.unmarshall(in, RangeTestPlanModel.class);
			//开始同步数据
			message=this.syncRangeTestPackagedata(rangeTestPlanModel,log,secretLevel);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		if (in!=null){
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//清空临时文件夹
		clearTempFileFolder();
		return message;
	}

	/**
	 * 靶场试验表单下发  中转机导入的数据库同步操作
	 * by zmz
	 * 20200821
	 * @param rangeTestPlanModel
	 * @param log
	 * @return
	 * @throws Exception
	 */
	/*@Transactional*/
	public ResultMessage syncRangeTestPackagedata(RangeTestPlanModel rangeTestPlanModel,StringBuffer log,String secretLevel)
			throws Exception {
		ResultMessage message = null;
		try {
			if (rangeTestPlanModel == null) {
				//如果xml转来的类为空,则直接报null错
				message = new ResultMessage(ResultMessage.Fail, "请联系工程师检查xml是否有误");
				message.addData("log", log);
				return message;
			}
			//分解一个集合类到各个小类
			//缺一个tableTempBean的类
			List<DataPackage> dataPackageList=rangeTestPlanModel.getDataPackageList();
			List<AcceptanceGroup> acceptanceGroupList=rangeTestPlanModel.getAcceptanceGroupList();
			List<SysUser> sysUserList=rangeTestPlanModel.getUserList();
			List<SysFile> sysFileList=rangeTestPlanModel.getSysFileList();
			List<TableTempBean> tableTempBeanList=rangeTestPlanModel.getTableTempBeanList();
			RangeTestPlan rangeTestPlan=rangeTestPlanModel.getRangeTestPlan();
			WorkBoard workBoard=rangeTestPlanModel.getWorkBoard();
			ModelBean modelBean=rangeTestPlanModel.getModelBean();
			List<ModuleManage> moduleManageList=rangeTestPlanModel.getModuleManageList();
			if(moduleManageList!=null){
				for (ModuleManage moduleManage : moduleManageList) {
					if(moduleManageService.getManageById(moduleManage.getId())==null) {
						moduleManageService.insert(moduleManage);
					}
					else {
						moduleManageService.update(moduleManage);
					}
				}
			}
/*
			//防止id重复,重新生成id
			String fileInfo=rangeTestPlan.getF_SYYJWJ();
			if(fileInfo!=null&&!fileInfo.equals("")) {
				JSONArray jsonArray=JSONArray.fromObject(fileInfo);
				for(int i=0;i<jsonArray.size();i++) {
					//先拿到旧的id
					JSONObject jsonObject=jsonArray.optJSONObject(i);
					for (SysFile file:sysFileList){
						if (file.getFileId().equals(jsonObject.optString("id"))){
							//生成新的id
							Long longFileId=UniqueIdUtil.genId();
							String stringFileId=longFileId.toString();
							//sysFile更换新的id
							file.setFileId(longFileId);
							//rangeTestPlan更换新的id
							jsonObject.put("id",longFileId);
							//逐一更换关联rangeTestPlan.id的rangeTestDataPackage.F_SSCH(所属策划)
							for (RangeTestDataPackage rangeTestDataPackage:rangeTestDataPackageList){
								rangeTestDataPackage.setF_SSCH(stringFileId);
							}
							//逐一更换关联rangeTestPlan.id的rangeTestGroup.F_SSRW(所属任务(任务就是策划))
							for (RangeTestGroup rangeTestGroup:rangeTestGroupList){
								rangeTestGroup.setF_SSRW(stringFileId);
							}
							//这个任务暂时搁置
						}
					}
*/
			/**
			 * id可能重复的当下解决方案为:
			 * 		如果id存在,则认为条目存在,不更新,不插入
			 * 		如果id不存在,直接插入
			 * 	by zmz 20200826
			 */
			//rangeTestDataPackageList  下发的表单
			if (dataPackageList!=null){
				for (DataPackage dataPackage:dataPackageList){
					if (dataPackageDao.getById(Long.valueOf(dataPackage.getId()))==null){
						//当前表里没有记录
						dataPackageDao.insertBean(dataPackage);
					}else {
						//有记录就更新
						dataPackageDao.update(dataPackage);
					}
				}
			}

			//rangeTestGroupList  每个策划的工作组
			if (acceptanceGroupList!=null){
				for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
					if (acceptanceGroupDao.getById(acceptanceGroup.getId())==null){
						//当前表里没有该用户的记录
						acceptanceGroupDao.insert(acceptanceGroup);
					}else {
						acceptanceGroupDao.update(acceptanceGroup);
					}
				}
			}

			//sysUserList  每个策划的工作组的组员/组长
			if (sysUserList!=null){
				for (SysUser sysUser:sysUserList){
					if (sysUserDao.getById(sysUser.getUserId())==null){
						//当前表里没有该用户的记录
						sysUserDao.insert("add",sysUser);
					}else {
						//当前表里有记录,更新
						sysUserDao.update(sysUser);
					}
					//查看当前用户是否有普通用户权限
					List<Long> userIds=userRoleDao.getUserIdsByRoleId(10000028520142L);
					boolean ifAlreadyHasRight=false;
					for (Long userId:userIds){
						if (userId.equals(sysUser.getUserId())){
							//当前用户已经是普通用户
							ifAlreadyHasRight=true;
							break;
						}
					}
					if (!ifAlreadyHasRight){
						//授权(普通角色权限(为了看到最顶上的菜单栏
						//给当前用户添加在susRoleUser表里加RoleId=10000028520142  userRoleId是自动生成的随机id
						UserRole userRole=new UserRole();
						userRole.setUserRoleId(UniqueIdUtil.genId());
						userRole.setRoleId(10000028520142L);
						userRole.setUserId(sysUser.getUserId());
						userRoleDao.add(userRole);
					}

				}
			}

			//sysFileList  每个文件
			if (sysFileList!=null){
				for (SysFile sysFile:sysFileList){
					//转移文件
					fileCopy(sysFile,secretLevel);
					//检查id情况
					if(!cwmSysFileDao.checkFileidExist(sysFile.getFileId())){
						//id不存在
						sysFileDao.insert("add",sysFile);
					}else {
						//如果存在,则更新(以服务器为准
						sysFileDao.update(sysFile);
					}
				}
			}

			//策划
			//表单是挂在策划下的,所以策划一定不为空

			if (rangeTestPlanDao.selectById(rangeTestPlan.getID())!=null){
				//存在
				rangeTestPlanDao.update(rangeTestPlan);
			}else {
				rangeTestPlanDao.insert(rangeTestPlan);
			}
			//工作板
			//有可能为空	dao层会判断并直接返回null
			if (workBoard!=null){
				if (workBoardDao.selectById(workBoard.getId())!=null){
					//如果本地存在了这个看板数据
					//那么就覆盖这条记录
					workBoardDao.update(workBoard);
				}else {
					workBoardDao.insert(workBoard);
				}
			}
			//型号
			//型号不可能为空,型号是策划的上一级
			//	即:有表单就必有策划,有策划就必有信号
			if (moduleDao.getById(modelBean.getId())!=null){
				//存在
				moduleDao.update(modelBean);
			}else {
				moduleDao.insert(modelBean);
			}

			//导入检查表
			if (tableTempBeanList!=null){
				for (TableTempBean tableTempBean:tableTempBeanList){
					TableTemp tableTemp=tableTempBean.getTableTemp();
					List<SignDef> signDefList=tableTempBean.getSignDefList();
					List<CheckItemDef> checkItemDefList=tableTempBean.getCheckItemDefList();
					List<CheckCondition> checkConditionList=tableTempBean.getCheckConditionList();
					if(tableTemp!=null){
						if (iOTableTempDao.getById(tableTemp.getId())==null){
							//没有这条数据,插入本数据
							iOTableTempDao.insert(tableTemp);
						}else {
							iOTableTempDao.update(tableTemp);
						}
					}
					if(signDefList!=null){
						for (SignDef signDef:signDefList){
							if (iOSignDefDao.getById(signDef.getId())==null){
								//没有这条记录,插入本数据
								iOSignDefDao.insert(signDef);
							}else {
								iOSignDefDao.update(signDef);
							}
						}
					}
					if (checkItemDefList!=null){
						for (CheckItemDef checkItemDef:checkItemDefList){
							if(ioCheckItemDefDao.getById(checkItemDef.getId())==null){
								//没有这条数据,插入本数据
								ioCheckItemDefDao.insert(checkItemDef);
							}else {
								ioCheckItemDefDao.update(checkItemDef);
							}
						}
					}
					if (checkConditionList!=null){
						for (CheckCondition checkCondition:checkConditionList){
							if (iOCheckConditionDao.getById(checkCondition.getId())==null){
								//没有这条数据,插入本数据
								iOCheckConditionDao.insert(checkCondition);
							}else {
								iOCheckConditionDao.update(checkCondition);
							}
						}
					}

				}
			}
			//发消息
			sendMassageWhenPCImportForBCSY(rangeTestPlan.getID());

			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("log", "导入成功");
			message.setMessage("导入成功");
		}
		catch (Exception e) {
			log.append(e.getMessage() + "\r\n" + "数据包导入异常");
			message = new ResultMessage(ResultMessage.Fail, "false");
			message.addData("log", log);
			message.setMessage(log.toString());
			e.printStackTrace();
		}
		return message;
	}


	/**
	 * @Description 服务器生成靶场试验压缩包的时候发消息
	 * @Author ZMZ
	 * @Date 2020/12/7 9:22
	 * @param missionId
	 * @Return void
	 */
	public void sendMassageWhenPCImportForBCSY(String missionId){
		//定位策划
		Map<String,Object> rangeTestPlan=testPlanDao.getPlanById(missionId).get(0);
		//定位组员表
		List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByMissionId(missionId);
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			SysUser teamMember=sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
			if ("组长".equals(acceptanceGroup.getZw())){
				//是组长   直接发消息
				acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"表单导入成功","组长");
			}else {
				//不是组长
				//先看下这个人在组里是不是有担任组长的同时担任组员
				if (curUserIsTeamLeader(acceptanceGroupList,acceptanceGroup.getXmId())){
					//当前这个人既是组员也是组长
					//组长的消息是上面发的,这里直接跳过
				}else {
					//当前这个人不是组长,直接发消息
					acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"表单导入成功","组员");
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
			acceptanceMessageDao.insertMessageForCommon(sponsor,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"表单导入成功","发起人");
		}
	}

	/**
	 * @Description 看看传来的人是不是组员或者组长 换句话说,是不是非发起人
	 * @Author ZMZ
	 * @Date 2020/12/7 9:39
	 * @param acceptanceGroupList
	 * @param sponsorId
	 * @Return boolean
	 */
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
	 * 把传来的压缩包的内容更新到本地
	 * 		这是签章界面的导入按钮事件的后台
	 * 	by zmz
	 * 	20200821
	 * @param multipartFile
	 * @return
	 */
	public ResultMessage importSignModelInfoFromZip(MultipartFile multipartFile) {
		ResultMessage message=null;
		StringBuffer log=new StringBuffer();
		FileInputStream in=null;
		//临时文件夹目录
		initTempFileFolder(IOConstans.IMPORT_FLODER_NAME);
		try {
			//解压文件
			//别问为什么抛exception,这个借口就是这么抛的
			ZipUtil.unZipFile(multipartFile,this.packageTempFolder);
			//找到解压后的以 ###.xml 结尾的文件
			File file=findMainFile(this.packageTempFolder);
			in=new FileInputStream(file);
			//xml转类
			SignModelDataPackage signModelDataPackage=XmlBeanUtil.unmarshall(in, SignModelDataPackage.class);
			//开始同步数据
			message=this.syncSignModel(signModelDataPackage,log);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}
		if (in!=null){
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//清空临时文件夹
		clearTempFileFolder();
		return message;
	}

	/**
	 * 签章导入的数据库同步操作
	 * by zmz
	 * 20200821
	 * @param signModelDataPackage
	 * @param log
	 * @return
	 * @throws Exception
	 */
	/*@Transactional*/
	public ResultMessage syncSignModel(SignModelDataPackage signModelDataPackage,StringBuffer log)
			throws Exception {
		ResultMessage message = null;
		try {
			if (signModelDataPackage == null) {
				//如果xml转来的类为空,则直接报null错
				message = new ResultMessage(ResultMessage.Fail, "");
				message.addData("log", log);
				return message;
			}
			//分解一个集合类到各个小类
			List<WPadhcqzb> wPadhcqzbList=signModelDataPackage.getwPadhcqzbsList();
			List<CwmSysSignModel> cwmSysSignModelList=signModelDataPackage.getCwmSysSignModelsList();
			List<CwmSysFile> cwmSysFileList=signModelDataPackage.getCwmSysFileList();
			/**
			 * 同步PAD回传签章表信息
			 * 	以用户的id为检索条件
			 * 		当一个用户在本表中存在多个签章时:
			 * 			直接删除本表中该用户所有条目
			 * 			再新建一个条目给他
			 */
			for (WPadhcqzb wPadhcqzb:wPadhcqzbList){
				if(wPadhcqzb.getF_Yhid()!=null){
					if (pADreturnSignModelDao.CountByUserid(wPadhcqzb.getF_Yhid())>0){
						//当前表里有该用户的记录
						//清空该用户的条目
						pADreturnSignModelDao.deleteByUserid(wPadhcqzb.getF_Yhid());
					}
				}

				//生成一个新的id防止摆渡机传来的id在服务器已存在
				Long id=UniqueIdUtil.genId();
				wPadhcqzb.setId(id.toString());
				//为该用户新建记录
				if (wPadhcqzb.getF_Yhid()!=null){
					//当前签章时是已经分配了用户的,可以保存所有信息
					pADreturnSignModelDao.insert(wPadhcqzb);
				}else {
					//当前签章无人认领,只保存签章本身信息,不保存人的信息
					pADreturnSignModelDao.insertWithoutUser(wPadhcqzb);
				}

			}
			/**
			 * 同步CWM_SYS_SIGN_MODEL表的信息	
			 * 	以用户id为检索条件
			 * 		这个表不会出现一个用户对应多个签章的情况
			 * 	本表中有记录的就直接更新
			 * 	么有记录的新建
			 */
			for (CwmSysSignModel cwmSysSignModel:cwmSysSignModelList){
				//当前用户有签章
				if (sysSignModelDao.countByUserid(cwmSysSignModel.getId())>0){
					//更新签章
					sysSignModelDao.updateByUserid(cwmSysSignModel.getUser_Id(),cwmSysSignModel.getImg_Path());
				}else {
					//生成一个新的id防止摆渡机传来的id在服务器已存在
					Long id=UniqueIdUtil.genId();
					cwmSysSignModel.setId(new BigDecimal(id));
					//当前用户没有签章,新建签章
					sysSignModelDao.insert(cwmSysSignModel);
				}

			}
			/**
			 * 同步CWM_SYS_FILE表的信息
			 * 直接新增
			 */
			for (CwmSysFile cwmSysFile:cwmSysFileList){
				//转移文件
				copySignModelFromZip(cwmSysFile);
				//检查id情况
				if(cwmSysFileDao.checkFileidExist(cwmSysFile.getFileid())){
					//id存在
					/**
					 * 想要解决这个BUG就需要建一个全局变量fileid,在保存三个entity的数据之前,生成一个未使用的id
					 * 使用这个id覆盖cwm_sys_sign_model的img_path
					 * 				PADHCQZB的qzid
					 * 				cwm_sys_file的fileId
					 * 	然后再保存数据
					 * 	不过付勇说用户数据量不大,不用担心这个问题
					 * 	by zmz 20200822
					 */
					message = new ResultMessage(ResultMessage.Success, "false");
					message.addData("log", "导入失败,当前fileid已被占用:"+cwmSysFile.getFileid());
					message.setMessage("导入失败");
					return message;
				}
				//保存当前文件信息
				cwmSysFileDao.insert(cwmSysFile);
			}
			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("log", "导入成功");
			message.setMessage("导入成功");
		}
		catch (Exception e) {
			log.append(e.getMessage() + "\r\n" + "数据包导入异常，数据已全部回滚！");
			message = new ResultMessage(ResultMessage.Fail, "false");
			message.addData("log", log);
			message.setMessage(log.toString());
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 复制zip的文件到本地对应的目录
	 * @param cwmSysFile
	 * @throws JAXBException
	 */
	public void copySignModelFromZip(CwmSysFile cwmSysFile){

		String filepaths=cwmSysFile.getFilepath();
		String strpath="";
		String filename="";
		if(filepaths!=null||!filepaths.equals("")) {
			int pos=filepaths.lastIndexOf("\\");
			strpath=filepaths.substring(0,pos);
			filename=filepaths.substring(pos+1,filepaths.length());
		}
		String sourcePath = this.packageTempFolder+"\\"+filepaths;
		String filePath = SysFileUtil.createFilePath(SysConfConstant.UploadFileFolder + File.separator + strpath,filename);
		FileOperator.copyFile(sourcePath, filePath);
	}

	/**
	 * 	 服务器点击下发表单到PAD后
	 * 	 到摇摆机上导入的按钮逻辑
	 * 	 全部型号 - 型号列表 - 数据导入
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public ResultMessage cornerImportPakage(MultipartFile file) throws Exception {
		ResultMessage message = null;
		StringBuffer log = new StringBuffer();
		FileInputStream is = null;
		try {
			initTempFileFolder(IOConstans.IMPORT_FLODER_NAME);
			ZipUtil.unZipFile(file, this.packageTempFolder);
			File mainFile = findMainFile(this.packageTempFolder);
			int pos=this.packageTempFolder.lastIndexOf("(");
			String security=this.packageTempFolder.substring(pos+1,this.packageTempFolder.length()-1);
			is = new FileInputStream(mainFile);
			/* Project project = XmlBeanUtil.unmarshall(is, Project.class); */
			AcceptanceplanModel acceptanceplanModel = XmlBeanUtil.unmarshall(is, AcceptanceplanModel.class);
			message = this.datasynDb(acceptanceplanModel,log,security);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());
		}finally {
			if (is != null) {
				is.close();
			}
			clearTempFileFolder();
		}
		return message;
	}

	/**
	 * 靶场试验 - 全部任务 - 数据导入 - 数据导入同步数据库的逻辑
	 * @param outer
	 * @param log
	 * @return
	 */
	public ResultMessage rangeTestPlanDataSynDb(RangeTestPlanBeanOuter outer,StringBuffer log,String secretLevel) throws JAXBException, ParseException {
		ResultMessage message = null;

			if (outer==null) {
				message = new ResultMessage(ResultMessage.Fail, "请联系工程师检查压缩包中的xml是否有误");
				message.addData("log", log);
				return message;
			}
			//因为xml不能直接获取list,所以这里是把list放在了一个bean里
			List<RangeTestPlanBean> rangeTestPlanBeanList=outer.getRangeTestPlanBeanList();
			if (rangeTestPlanBeanList!=null){
				for (RangeTestPlanBean rangeTestPlanBean:rangeTestPlanBeanList){
					RangeTestTransferData rangeTestTransferData=rangeTestPlanBean.getRangeTestTransferData();
					//bu同步策划

					RangeTestPlan rangeTestPlan=rangeTestTransferData.getRangeTestPlan();
					//给相关的人发消息
					sendMessageWhenSeverImportDataPackage(rangeTestPlan.getID(),"BCSY");
					//添加导入历史
					importHistoryService.addImportHistoryForBCSY(rangeTestPlan);
					sendMassageWhenServerImportForBCSY(rangeTestPlan.getID());
					//更新策划的试验依据文件
					if (rangeTestPlan.getF_SYBGHCSJ()!=""||rangeTestPlan.getF_SYBGHCSJ()!=null){
						rangeTestPlanDao.updateBackData(rangeTestPlan.getID(),rangeTestPlan.getF_SYBGHCSJ());
					}
				/*if(!"".equals(rangeTestPlan.getF_SYBGHCSJ())) {
					if(rangeTestPlanDao.selectById(rangeTestPlan.getID())==null) {
						rangeTestPlanDao.insert(rangeTestPlan);
					}

				}*/
					workBoardDao.updatework(rangeTestPlan.getID(),"数据已采集","发起数据确认");
					//同步数据包
					List<DataPackage> dataPackageList=rangeTestTransferData.getDataPackageList();
					if (dataPackageList!=null){
						for (DataPackage dataPackage : dataPackageList) {
							if(dataPackageDao.getById(Long.valueOf(dataPackage.getId()))==null) {
								dataPackageDao.insertBean(dataPackage);
							}
							else {
								//只同步状态
								dataPackageDao.changeStatus(dataPackage.getId(),dataPackage.getZxzt());
							}
						}
					}

					//同步策划文件
					List<FileData> fileDataList=rangeTestTransferData.getFileDataList();
					if (fileDataList!=null){
						for (FileData fileData : fileDataList) {
							if(fileDataDao.getById(fileData.getId())==null) {
								fileDataDao.insert(fileData);
							}
						}
					}

					//同步实例
					List<RangeTestInstanceBean> rangeTestInstanceBeanList=rangeTestTransferData.getRangeTestInstanceBeanList();
					if (rangeTestInstanceBeanList!=null){
						for (RangeTestInstanceBean rangeTestInstanceBean:rangeTestInstanceBeanList) {
							List<SignResult> signResultList=rangeTestInstanceBean.getSignResultList();
							List<SysFile> sysFileList=rangeTestInstanceBean.getSysFileList();
							List<CkConditionResult> ckConditionResultList=rangeTestInstanceBean.getCkConditionResultList();
							TableInstance tableInstance=rangeTestInstanceBean.getTableInstance();
							//取pad拍照信息表
							List<PadPhotoInfo> padPhotoInfoList=rangeTestInstanceBean.getPadPhotoInfoList();
							if(padPhotoInfoList!=null){
								for (PadPhotoInfo padPhotoInfo:padPhotoInfoList){
									if (padPhotoInfoDao.getById(padPhotoInfo.getId())==null){
										//如果没有记录,新增
										padPhotoInfoDao.insert(padPhotoInfo);
									}else {
										//如果有这条记录,也不更新
										//除非手动改数据库,否则不可能出现记录相同的情况
									}
								}
							}

							//保存signResultList
							if (signResultList!=null){
								for (SignResult signResult : signResultList) {
									if(ioSignResultDao.getById(signResult.getId())==null) {
										ioSignResultDao.insert(signResult);
									}else {
										//服务器已有相同的实例,对服务器的实例进行覆盖
										ioSignResultDao.update(signResult);
									}
								}
							}

							//保存sysFileList
							if (sysFileList!=null){
								for (SysFile sysFile : sysFileList) {
									if(sysFileDao.getById(sysFile.getFileId())==null) {
										sysFileDao.add(sysFile);
										this.fileCopy(sysFile,secretLevel);
									}else {
										//服务器已有相同的实例,对服务器的实例进行覆盖
										sysFileDao.update(sysFile);
										this.fileCopy(sysFile,secretLevel);
									}
								}
							}

							//保存ckConditionResultList
							if (ckConditionResultList!=null){
								for (CkConditionResult ckConditionResult:ckConditionResultList){
									if (ckConditionResultDao.getById(ckConditionResult.getID())==null){
										//没有这个数据,保存本条目
										ckConditionResultDao.add(ckConditionResult);
									}else {
										//服务器已有相同的实例,对服务器的实例进行覆盖
										ckConditionResultDao.update(ckConditionResult);
									}
								}
							}

							//处理最麻烦的tableInstance
							//tableInstance可以直接存
							if (ioTableInstanceDao.getById(tableInstance.getId())==null){
								//没有这条数据,保存此记录
								ioTableInstanceDao.insert(tableInstance);
							}else {
								//服务器已有相同的实例,对服务器的实例进行覆盖
								ioTableInstanceDao.update(tableInstance);
							}
							//保存tableInstance的checkResultList
							List<CheckResult> checkResultList=tableInstance.getCheckResultList();
							if(checkResultList!=null) {
								for (CheckResult checkResult:checkResultList){
									if (checkResultDao.getById(checkResult.getId())==null){
										//没有数据,应该插入
										checkResultBeanDao.insert(checkResult);
									}else {
										checkResultBeanDao.update(checkResult);
									}
								}
							}

							List<ConditionResult> conditionResultList=tableInstance.getConditionResultList();
							if(conditionResultList!=null) {
								for (ConditionResult conditionResult:conditionResultList){
									if (iOConditionResultDao.getById(conditionResult.getId())==null){
										//没有这个记录,需要新增
										iOConditionResultDao.insert(conditionResult);
									}else {
										//有记录,需要覆盖
										iOConditionResultDao.update(conditionResult);
									}
								}
							}

							List<SignResult> signResults=tableInstance.getSignResultList();
							if (signResults!=null){
								for (SignResult signResult:signResults){
									if (iOSignResultDao.getById(signResult.getId())==null){
										//没有这个记录,需要新增
										iOSignResultDao.insert(signResult);
									}else {
										//有这个记录,需要覆盖
										iOSignResultDao.update(signResult);
									}
								}
							}


							/*//取pad拍照文件(非针对性 ,已废弃)
							List<SysFile> photoFileList=rangeTestPlanBean.getPhotoFileList();
							if (photoFileList!=null){
								for (SysFile sysFile : photoFileList) {
									if(sysFileDao.getById(sysFile.getFileId())==null) {
										sysFileDao.add(sysFile);
										try {
											this.fileCopy(sysFile);
										} catch (JAXBException e) {
											log.append(e.getMessage() + "\r\n" + "数据包导入异常，数据已全部回滚！");
											message = new ResultMessage(ResultMessage.Fail, "false");
											message.addData("log", log);
											message.setMessage(log.toString());
											e.printStackTrace();
											e.printStackTrace();
											return message;
										}
									}
								}
							}*/

							//取表单模板
							List<TableTempBean> tableTempBeanList=rangeTestPlanBean.getTableTempBeanList();
							if (tableTempBeanList!=null){
								for (TableTempBean tableTempBean: tableTempBeanList) {
									if(iOTableTempDao.getById(tableTempBean.getTableTemp().getId())==null) {
										iOTableTempDao.insert(tableTempBean.getTableTemp());
										List<SignDef> signDefList=tableTempBean.getSignDefList();
										if (signDefList!=null){
											for (SignDef signDef : signDefList) {
												if(iOSignDefDao.getById(signDef.getId())==null) {
													iOSignDefDao.insert(signDef);
												}
											}
										}

										List<CheckCondition> checkConditionList=tableTempBean.getCheckConditionList();
										if (checkConditionList!=null){
											for (CheckCondition checkCondition : checkConditionList) {
												if(iOCheckConditionDao.getById(checkCondition.getId())==null) {
													iOCheckConditionDao.insert(checkCondition);
												}
											}
										}

										List<CheckItemDef> checkItemDefList=tableTempBean.getCheckItemDefList();
										if (checkItemDefList!=null){
											for (CheckItemDef checkItemDef : checkItemDefList) {
												if(ioCheckItemDefDao.getById(checkItemDef.getId())==null) {
													ioCheckItemDefDao.insert(checkItemDef);
												}
											}
										}

									}
								}
							}

						}
					}

					message = new ResultMessage(ResultMessage.Success, "true");
					message.addData("log", "导入成功");
					message.setMessage("导入成功");
				}
			}

		return message;
	}
	/**
	 * @Description 服务器导入中转机的压缩包之后给你相关的人发消息
	 * @Author ZMZ
	 * @Date 2020/12/16 14:46
	 * @param planId
	 * @param ofPart BCSY  YSCH
	 * @Return void
	 */
	private void sendMessageWhenSeverImportDataPackage(String planId, String ofPart) {
		//策划
		Map<String,Object> plan;
		//组员表
		List<AcceptanceGroup> acceptanceGroupList;
		if ("BCSY".equals(ofPart)){
			//找靶场的策划
			plan=testPlanDao.getPlanById(planId).get(0);
			//定位组员表
			acceptanceGroupList=acceptanceGroupDao.getByMissionId(planId);
		}else {
			//找验收的策划
			plan=acceptancePlanDao.getMapById(planId);
			//定位组员表
			acceptanceGroupList=acceptanceGroupDao.getByAcceptancePanId(planId);
		}
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList) {
			SysUser teamMember = sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
			if ("组长".equals(acceptanceGroup.getZw())) {
				//是组长   直接发消息
				if ("BCSY".equals(ofPart)) {
					acceptanceMessageDao.insertMessageForCommon(teamMember, planId, plan.get("F_CHBGBBH") + "-" + plan.get("F_SYRWMC"), "数据导入成功", "组长");
				} else {
					acceptanceMessageDao.insertMessageForCommon(teamMember, planId, plan.get("F_CHBGBBH") + "-" + plan.get("F_CPMC"), "数据导入成功", "组长");
				}

			} else {
				//不是组长
				//先看下这个人在组里是不是有担任组长的同时担任组员
				if (curUserIsTeamLeader(acceptanceGroupList, acceptanceGroup.getXmId())) {
					//当前这个人既是组员也是组长
					//组长的消息是上面发的,这里直接跳过
				} else {
					//当前这个人不是组长,直接发消息
					if ("BCSY".equals(ofPart)) {
						acceptanceMessageDao.insertMessageForCommon(teamMember, planId, plan.get("F_CHBGBBH") + "-" + plan.get("F_SYRWMC"), "数据导入成功", "组员");
					} else {
						acceptanceMessageDao.insertMessageForCommon(teamMember, planId, plan.get("F_CHBGBBH") + "-" + plan.get("F_CPMC"), "数据导入成功", "组员");
					}

				}
			}
		}
		if ("BCSY".equals(ofPart)) {
			//确定发起人
			String sponsorId=plan.get("F_FQRID").toString();
			SysUser sponsor=sysUserDao.getById(Long.valueOf(sponsorId));
			if (curUserInTeam(acceptanceGroupList,sponsorId)){
				//发起人属于组员/组长
				//该消息已由上面发送
			}else {
				//发起人不属于组员/组长
				acceptanceMessageDao.insertMessageForCommon(sponsor, planId, plan.get("F_CHBGBBH") + "-" + plan.get("F_SYRWMC"), "数据导入成功", "发起人");
			}
		}else {
			//验收没有发起人

		}

	}



	/**
	 * @Description 服务器生成靶场试验压缩包的时候发消息
	 * @Author ZMZ
	 * @Date 2020/12/7 9:22
	 * @param missionId
	 * @Return void
	 */
	public void sendMassageWhenServerImportForBCSY(String missionId){
		//定位策划
		Map<String,Object> rangeTestPlan=testPlanDao.getPlanById(missionId).get(0);
		//定位组员表
		List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByMissionId(missionId);
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			SysUser teamMember=sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
			if ("组长".equals(acceptanceGroup.getZw())){
				//是组长   直接发消息
				acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"服务器导入成功","组长");
			}else {
				//不是组长
				//先看下这个人在组里是不是有担任组长的同时担任组员
				if (curUserIsTeamLeader(acceptanceGroupList,acceptanceGroup.getXmId())){
					//当前这个人既是组员也是组长
					//组长的消息是上面发的,这里直接跳过
				}else {
					//当前这个人不是组长,直接发消息
					acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"服务器导入成功","组员");
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
			acceptanceMessageDao.insertMessageForCommon(sponsor,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"服务器导入成功","发起人");
		}
	}




	public ResultMessage serverDataSynDb(TranserDataBean transerDataBean,StringBuffer log,String security) {
		ResultMessage message = null;
		try {
			if (transerDataBean == null) {
				message = new ResultMessage(ResultMessage.Fail, "");
				message.addData("log", log);
				return message;
			}
			List<TableTempBean> tableTempBeanList=transerDataBean.getTableTempBeanList();
			for (TableTempBean tableTempBean: tableTempBeanList) {
				if(iOTableTempDao.getById(tableTempBean.getTableTemp().getId())==null) {
					iOTableTempDao.insert(tableTempBean.getTableTemp());
					List<SignDef> signDefList=tableTempBean.getSignDefList();
					for (SignDef signDef : signDefList) {
						if(iOSignDefDao.getById(signDef.getId())==null) {
							iOSignDefDao.insert(signDef);
						}
					}
					List<CheckCondition> checkConditionList=tableTempBean.getCheckConditionList();
					for (CheckCondition checkCondition : checkConditionList) {
						if(iOCheckConditionDao.getById(checkCondition.getId())==null) {
							iOCheckConditionDao.insert(checkCondition);
						}
					}
					List<CheckItemDef> checkItemDefList=tableTempBean.getCheckItemDefList();
					for (CheckItemDef checkItemDef : checkItemDefList) {
						if(ioCheckItemDefDao.getById(checkItemDef.getId())==null) {
							ioCheckItemDefDao.insert(checkItemDef);
						}
					}
					
				}
			}

			List<TransferData> transferDataList=transerDataBean.getTransferDataList();
			//取全部的照片信息,  已被废弃,改用padpzxx表
			/*List<SysFile> phontoFileList=transerDataBean.getPhotoFileList();
			for (SysFile sysFile : phontoFileList) {
				if(sysFileDao.getById(sysFile.getFileId())==null) {
					sysFileDao.add(sysFile);
					this.fileCopy(sysFile);
				}
			}*/
			for (TransferData transferData : transferDataList) {
				List<DataPackage> dataPackageList=transferData.getDataPackageList();
				List<TableInstanceBean> tableInstanceBeanList=transferData.getTableInstanceBeanList();
				List<FileData> fileDataList=transferData.getFileDataList();
				List<ProductInfoBean> productInfoBeanList=transferData.getProductInfoBeanList();
				List<Conventional> conventionalList=transferData.getpConventionalList();
				WorkBoard workBoard=transferData.getWorkBoard();
				AcceptancePlan acceptancePlan=transferData.getAcceptancePlan();
				//添加导入历史
				importHistoryService.addImportHistoryForCPYS(acceptancePlan);
				//为验收策划添加结束时间(这个结束时间来自pad回传的验收报告表的组长的签署时间,这个时间已经关联在了策划的字段上,只是需要给服务器同步一下)
				acceptancePlanDao.updateEndTime(acceptancePlan.getId(),acceptancePlan.getYsjssj());

				if(workBoardDao.getByPlanId(workBoard.getZjID())==null) {
					workBoardDao.insert(workBoard);
				}else {
					workBoardDao.update(workBoard);
				}
				for (Conventional conventional : conventionalList) {
					if(ioConventionalDao.getById(conventional.getId())==null) {
						ioConventionalDao.insert(conventional);
					}else {
						ioConventionalDao.update(conventional);
					}
				}
				
				for (ProductInfoBean productInfoBean : productInfoBeanList) {
					ProductInfo productInfo=productInfoBean.getProductInfo();
					if(productInforDao.getById(productInfo.getId())==null) {
						List<ActualData> actualDataList=productInfoBean.getActualDataList();
						productInforDao.insert(productInfo);
						for (ActualData actualData : actualDataList) {
							actualDataDao.insert(actualData);
						}
					}
					else {
						List<ActualData> actualDataList=productInfoBean.getActualDataList();
						productInforDao.update(productInfo);
						for (ActualData actualData : actualDataList) {
							actualDataDao.update(actualData);
						}
					}

				}

				for (FileData fileData : fileDataList) {
					if(fileDataDao.getById(fileData.getId())==null) {
						fileDataDao.insert(fileData);
					}
					else {
						fileDataDao.update(fileData);
					}
				}
				for (DataPackage dataPackage : dataPackageList) {
					if(dataPackageDao.getById(Long.valueOf(dataPackage.getId()))==null) {
						dataPackageDao.insertBean(dataPackage);
					}
					else {
						dataPackageDao.update(dataPackage);
					}
				}
				if(!"".equals(transferData.getAcceptancePlan().getYsbghcsj())) {
					if(acceptancePlanDao.getMapById(transferData.getAcceptancePlan().getId())!=null) {
						acceptancePlanDao.updateReportData(transferData.getAcceptancePlan());
					}
					
				}
				for (TableInstanceBean tableInstanceBean : tableInstanceBeanList) {
					List<SignResult> signResultList=tableInstanceBean.getSignResultList();
					TableInstance tableInstance=tableInstanceBean.getTableInstance();
					List<SysFile> sysFileList=tableInstanceBean.getSysFileList();


					//取pad拍照信息表
					List<PadPhotoInfo> padPhotoInfoList=tableInstanceBean.getPadPhotoInfoList();
					if(padPhotoInfoList!=null){
						for (PadPhotoInfo padPhotoInfo:padPhotoInfoList){
							if (padPhotoInfoDao.getById(padPhotoInfo.getId())==null){
								//如果没有记录,新增
								padPhotoInfoDao.insert(padPhotoInfo);
							}else {
								//如果有这条记录,也不更新
								//除非手动改数据库,否则不可能出现记录相同的情况
							}
						}
					}

					List<CkConditionResult> ckConditionResultList=tableInstanceBean.getCkConditionResultList();
					if(instanceDao.getById(tableInstanceBean.getTableInstance().getId())==null) {
						instanceDao.insert(tableInstanceBean.getTableInstance());
					}
					for (SysFile sysFile : sysFileList) {
						if(sysFileDao.getById(sysFile.getFileId())==null) {
							sysFileDao.add(sysFile);
							this.fileCopy(sysFile,security);
						}
					}
					for (SignResult signResult : signResultList) {
						if(ioSignResultDao.getById(signResult.getId())==null) {
							ioSignResultDao.insert(signResult);
						}
					}
					for (CkConditionResult ckConditionResult : ckConditionResultList) {
						if(ckConditionResultDao.getById(Long.valueOf(ckConditionResult.getID()))==null) {
							ckConditionResultDao.add(ckConditionResult);
						}
					}
				}
				//发消息
				sendMassageWhenServerImportForYSCH(acceptancePlan.getId());
			}

			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("log", "导入成功");
			message.setMessage("导入成功");
		}
		catch (Exception e) {
			log.append(e.getMessage() + "\r\n" + "数据包导入异常，数据已全部回滚！");
			message = new ResultMessage(ResultMessage.Fail, "false");
			message.addData("log", log);
			message.setMessage(log.toString());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return message;
		
	}



	public ResultMessage datasynDb(AcceptanceplanModel acceptanceplanModel,StringBuffer log,String security)
			throws Exception {
		ResultMessage message = null;
		try {
			if (acceptanceplanModel == null) {
				message = new ResultMessage(ResultMessage.Fail, "");
				message.addData("log", log);
				return message;
			}
			ModelBean modelBean=acceptanceplanModel.getModelBean();
			ProductCategoryBath productCategory=acceptanceplanModel.getProductCategory();
			ProductCategoryBath productCategoryBath=acceptanceplanModel.getProductCategoryBath();
			AcceptancePlan acceptancePlan=acceptanceplanModel.getAcceptancePlan();
			
			
			List<TableTempBean> tableTempBeanList=acceptanceplanModel.getTableTempBeanList();
			List<DataPackage> dataPackageList=acceptanceplanModel.getDataPackageList();
			List<AcceptanceGroup> acceptanceGroupList=acceptanceplanModel.getAcceptanceGroupList();
			List<SysFile> sysFileList=acceptanceplanModel.getSysFileList();
			List<SysUser> sysUserList=acceptanceplanModel.getUserList();
			List<ModuleManage> moduleManageList=acceptanceplanModel.getModuleManageList();
			if(moduleManageList!=null){
				for (ModuleManage moduleManage : moduleManageList) {
					if(moduleManageService.getManageById(moduleManage.getId())==null) {
						moduleManageService.insert(moduleManage);
					}
					else {
						moduleManageService.update(moduleManage);
					}
				}
			}
			WorkBoard workBoard=acceptanceplanModel.getWorkBoard();
			if(workBoard!=null) {
				if(workBoardDao.getByPlanId(workBoard.getZjID())==null){
					workBoardDao.insert(workBoard);
				}
				else{
					workBoardDao.update(workBoard);
				}
			}
			List<SysRole> roleList=sysRoleService.getAll();
			SysRole sysRole=null;
			for (SysRole Role : roleList) {
				if(Role.getRoleName().equals("普通用户")){
					sysRole=Role;
				}
			}
			for (SysUser sysUser : sysUserList) {
				if(sysUser!=null) {
					if(sysUserDao.isUsernameExist(sysUser.getUsername())) {
						sysUserDao.update(sysUser);
					}
					else {
						sysUserDao.add(sysUser);
						if(sysRole!=null) {
							UserRole userRole=new UserRole();
							userRole.setUserId(sysUser.getUserId());
							userRole.setRoleId(sysRole.getRoleId());
							userRole.setUserRoleId(UniqueIdUtil.genId());
							userRoleService.add(userRole);
						}
					}
				}
			}
			for (SysFile sysFile : sysFileList) {
				if(sysFileDao.getById(sysFile.getFileId())==null) {
					sysFileDao.add(sysFile);
					/*FileOperator.copyFile(this.packageTempFolder+"\\"+sysFile.getFilename()+"."+sysFile.getExt(),SysConfConstant.UploadFileFolder + File.separator + sysFile.getFilepath());*/
					fileCopy(sysFile,security);
				}
			}
			if(productCategoryBatchDao.getById(String.valueOf(productCategory.getId()))==null) {
				productCategoryBatchDao.insert(productCategory);
			}
			if(productCategoryBatchDao.getById(String.valueOf(productCategoryBath.getId()))==null){
				productCategoryBatchDao.insert(productCategoryBath);
			}
			if(moduleDao.getById(modelBean.getId())==null) {
				moduleDao.insert(modelBean);
			}
			if(acceptancePlanDao.getMapById(String.valueOf(acceptancePlan.getId()))==null) {
				acceptancePlanDao.insert(acceptancePlan);
				for (AcceptanceGroup acceptanceGroup : acceptanceGroupList) {
					acceptanceGroupDao.insert(acceptanceGroup);
				}
			}
			for (DataPackage dataPackage : dataPackageList) {
				if(dataPackageDao.getById(Long.valueOf(dataPackage.getId()))==null) {
					dataPackageDao.insertBean(dataPackage);
				}
			}
			for (TableTempBean tableTempBean: tableTempBeanList) {
				if(iOTableTempDao.getById(tableTempBean.getTableTemp().getId())==null) {
					iOTableTempDao.insert(tableTempBean.getTableTemp());
					List<SignDef> signDefList=tableTempBean.getSignDefList();
					for (SignDef signDef : signDefList) {
						if(iOSignDefDao.getById(signDef.getId())==null) {
							iOSignDefDao.insert(signDef);
						}
					}
					List<CheckCondition> checkConditionList=tableTempBean.getCheckConditionList();
					for (CheckCondition checkCondition : checkConditionList) {
						if(iOCheckConditionDao.getById(checkCondition.getId())==null) {
							iOCheckConditionDao.insert(checkCondition);
						}
					}
					List<CheckItemDef> checkItemDefList=tableTempBean.getCheckItemDefList();
					for (CheckItemDef checkItemDef : checkItemDefList) {
						if(ioCheckItemDefDao.getById(checkItemDef.getId())==null) {
							ioCheckItemDefDao.insert(checkItemDef);
						}
					}
					
				}
			}
			//发消息
			sendMassageWhenPCImportForYSCH(acceptancePlan.getId());
			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("log", "导入成功");
			message.setMessage("导入成功");
		}
		catch (Exception e) {
			log.append(e.getMessage() + "\r\n" + "数据包导入异常，数据已全部回滚！");
			message = new ResultMessage(ResultMessage.Fail, "false");
			message.addData("log", log);
			message.setMessage(log.toString());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return message;
		
	}
	/**
	 * @Description 服务器生成靶场试验压缩包的时候发消息
	 * @Author ZMZ
	 * @Date 2020/12/7 9:22
	 * @param missionId
	 * @Return void
	 */
	public void sendMassageWhenServerImportForYSCH(String missionId){
		//定位策划
		Map<String,Object> rangeTestPlan=acceptancePlanDao.getMapById(missionId);
		//定位组员表
		List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByAcceptancePanId(missionId);
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			SysUser teamMember=sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
			if ("组长".equals(acceptanceGroup.getZw())){
				//是组长   直接发消息
				acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"服务器导入成功","组长");
			}else {
				//不是组长
				//先看下这个人在组里是不是有担任组长的同时担任组员
				if (curUserIsTeamLeader(acceptanceGroupList,acceptanceGroup.getXmId())){
					//当前这个人既是组员也是组长
					//组长的消息是上面发的,这里直接跳过
				}else {
					//当前这个人不是组长,直接发消息
					acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"服务器导入成功","组员");
				}
			}
		}
		//确定发起人
		/*String sponsorId=rangeTestPlan.get("F_FQRID").toString();
		SysUser sponsor=sysUserDao.getById(Long.valueOf(sponsorId));
		if (curUserInTeam(acceptanceGroupList,sponsorId)){
			//发起人属于组员/组长
			//该消息已由上面发送
		}else {
			//发起人不属于组员/组长
			acceptanceMessageDao.insertMessageForCommon(sponsor,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"服务器导入成功","发起人");
		}*/
	}
	/**
	 * @Description 服务器生成靶场试验压缩包的时候发消息
	 * @Author ZMZ
	 * @Date 2020/12/7 9:22
	 * @param missionId
	 * @Return void
	 */
	public void sendMassageWhenPCImportForYSCH(String missionId){
		//定位策划
		Map<String,Object> rangeTestPlan=acceptancePlanDao.getMapById(missionId);
		//定位组员表
		List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByAcceptancePanId(missionId);
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			SysUser teamMember=sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
			if ("组长".equals(acceptanceGroup.getZw())){
				//是组长   直接发消息
				acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"表单导入成功","组长");
			}else {
				//不是组长
				//先看下这个人在组里是不是有担任组长的同时担任组员
				if (curUserIsTeamLeader(acceptanceGroupList,acceptanceGroup.getXmId())){
					//当前这个人既是组员也是组长
					//组长的消息是上面发的,这里直接跳过
				}else {
					//当前这个人不是组长,直接发消息
					acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"表单导入成功","组员");
				}
			}
		}
		//确定发起人
		/*String sponsorId=rangeTestPlan.get("F_FQRID").toString();
		SysUser sponsor=sysUserDao.getById(Long.valueOf(sponsorId));
		if (curUserInTeam(acceptanceGroupList,sponsorId)){
			//发起人属于组员/组长
			//该消息已由上面发送
		}else {
			//发起人不属于组员/组长
			acceptanceMessageDao.insertMessageForCommon(sponsor,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"表单导入成功","发起人");
		}*/
	}

	//用户信息导入
	public ResultMessage userOrgImport(MultipartFile file) throws Exception {
		ResultMessage message = null;
		StringBuffer log = new StringBuffer();
		FileInputStream is = null;
		try {
			initTempFileFolder(IOConstans.IMPORT_FLODER_NAME);
			ZipUtil.unZipFile(file, this.packageTempFolder);
			File mainFile = findMainFile(this.packageTempFolder);
			is = new FileInputStream(mainFile);
			/* Project project = XmlBeanUtil.unmarshall(is, Project.class); */
			OrgUserInfo orgUserInfo = XmlBeanUtil.unmarshall(is, OrgUserInfo.class);
			message = this.syncUserInfo(orgUserInfo);
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());

		} finally {
			if (is != null) {
				is.close();
			}
			clearTempFileFolder();
		}
		return message;
	}
	/**
	 * @Author fuyong
	 * @Description: 服务器导入(数据包节点导入)
	 * @Params [file, fcId, xhId]
	 * @Date 2020/6/7 13:16
	 * @Return java.lang.String
	 */
	public ResultMessage importPakage(MultipartFile file, String fcId, String xhId) throws Exception {
		ResultMessage message = null;
		StringBuffer log = new StringBuffer();
		FileInputStream is = null;
		try {
			initTempFileFolder(IOConstans.IMPORT_FLODER_NAME);
			ZipUtil.unZipFile(file, this.packageTempFolder);
			File mainFile = findMainFile(this.packageTempFolder);
			is = new FileInputStream(mainFile);
			/* Project project = XmlBeanUtil.unmarshall(is, Project.class); */
			ExportData exportData = XmlBeanUtil.unmarshall(is, ExportData.class);
			message = this.datasynDb(exportData, fcId, log, xhId);
			// 数据库同步(只进行新增和更新操作)
			/* message = this.asynDb(project, fcId, log, xhId); */
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail, e.getMessage());

		} finally {
			if (is != null) {
				is.close();
			}
			clearTempFileFolder();
		}
		return message;
	}
//this.packageTempFolder+"\\"+sysFile.getFilename()+sysFile.getExt(),SysConfConstant.UploadFileFolder + File.separator + sysFile.getFilepath()
    //文件导入处理
    public void fileCopy(SysFile sysFile) throws JAXBException {
    	
    	String filepaths=sysFile.getFilepath();
    	String strpath="";
    	String filename="";
    	if(filepaths!=null||!filepaths.equals("")) {
    		int pos=filepaths.lastIndexOf("\\");
    		strpath=filepaths.substring(0,pos);
    		filename=filepaths.substring(pos+1,filepaths.length());
    	}
    	String sourcePath = this.packageTempFolder+"\\"+filepaths;
    	String filePath = SysFileUtil.createFilePath(SysConfConstant.UploadFileFolder + File.separator + strpath,filename);
        FileOperator.copyFile(sourcePath, filePath);
    }
    
    public void fileCopy(SysFile sysFile,String security) throws JAXBException {
    	
    	String filepaths=sysFile.getFilepath();
    	String strpath="";
    	String filename="";
    	String sourceName="";
    	if(filepaths!=null||!filepaths.equals("")) {
    		int pos=filepaths.lastIndexOf("\\");
    		strpath=filepaths.substring(0,pos);
    		filename=filepaths.substring(pos+1,filepaths.length());
    		pos = filename.lastIndexOf(".");
    		sourceName= filename.substring(0,pos)+"("+security+")"+filename.substring(pos,filename.length());
    	}
    	String sourcePath = this.packageTempFolder+"\\"+strpath+"\\"+sourceName;
    	String filePath = SysFileUtil.createFilePath(SysConfConstant.UploadFileFolder + File.separator + strpath,filename);
        FileOperator.copyFile(sourcePath, filePath);
    }
    
    

	public ResultMessage datasynDb(ExportData exportData, String localFcId, StringBuffer log, String xhId)
			throws Exception {
		ResultMessage message = null;
		try {
			if (exportData == null) {
				message = new ResultMessage(ResultMessage.Fail, "");
				message.addData("log", log);
				return message;
			}
			List<ProductCategoryBathBean> productCategoryBathBeanList = exportData.getProductCategoryBathBean();
			List<TableTempBean> tableTempBeanList = exportData.getTableTempBeanList();

			// 处理模板导入
			for (TableTempBean tableTempBean : tableTempBeanList) {
				// 判断当前模板是否存在
				if (iOTableTempDao.getById(tableTempBean.getTableTemp().getId()) != null) {
					iOTableTempDao.deleteById(tableTempBean.getTableTemp().getId(), true);
				}
				tableTempBean.getTableTemp().setProject_id(localFcId);
				iOTableTempDao.insert(tableTempBean.getTableTemp());

				List<SignDef> signDefList = tableTempBean.getSignDefList();
				for (int i = 0; i < signDefList.size(); i++) {
					iOSignDefDao.insert(signDefList.get(i));
				}
				List<CheckCondition> checkConditionList = tableTempBean.getCheckConditionList();
				for (int i = 0; i < checkConditionList.size(); i++) {
					iOCheckConditionDao.insert(checkConditionList.get(i));
				}
			}
			for (ProductCategoryBathBean productCategoryBathBean : productCategoryBathBeanList) {
				List<SysFile> sysFileList = productCategoryBathBean.getSysFileList();
				for (SysFile sysFile : sysFileList) {
					if(sysFileDao.getById(sysFile.getFileId())!=null) {
						sysFileDao.delById(sysFile.getFileId());
					}
					sysFileDao.add(sysFile);
					/*FileOperator.copyFile(this.packageTempFolder+"\\"+sysFile.getFilename()+sysFile.getExt(),SysConfConstant.UploadFileFolder + File.separator + sysFile.getFilepath());*/
					fileCopy(sysFile);
				}
				List<AcceptancePlanBean> acceptancePlanBeanList=productCategoryBathBean.getAcceptancePlanBeanList();
				for (AcceptancePlanBean acceptancePlanBean : acceptancePlanBeanList) {
					if(acceptancePlanDao.getMapById(String.valueOf(acceptancePlanBean.getAcceptancePlan().getId()))!=null) {
						acceptancePlanDao.deleteById(String.valueOf(acceptancePlanBean.getAcceptancePlan().getId()));	
					}
					
				}
			}
		}catch (Exception e) {
			log.append(e.getMessage() + "\r\n" + "数据包导入异常，数据已全部回滚！");
			message = new ResultMessage(ResultMessage.Fail, "false");
			message.addData("log", log);
			message.setMessage(log.toString());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return message;
	}

	public ResultMessage syncUserInfo(OrgUserInfo orgUserInfo) throws Exception{
		ResultMessage message = null;
		String log="导入用户信息成功！";
		message = new ResultMessage(ResultMessage.Success, "");
		message.addData("log", log);
		try {
			if (orgUserInfo == null) {
				message = new ResultMessage(ResultMessage.Fail, "");
				message.addData("log", "数据包格式不正确,请联系管理员!");
				return message;
			}
			List<SysUser> sysUserlist=orgUserInfo.getUserList();
			List<SysOrg> sysOrgList=orgUserInfo.getOrgList();
			List<Position> positionList=orgUserInfo.getPositionList();
			List<UserPosition> userPositionList=orgUserInfo.getUserPositionList();
			for (UserPosition userPosition : userPositionList) {
				if(userPositionDao.getById(userPosition.getUserPosId())!=null) {
					userPositionDao.update(userPosition);
				}
				else {
					userPositionDao.add(userPosition);
				}
			}
			for (SysUser sysUser : sysUserlist) {
				sysUser.setPassword(PasswordUtil.generatePassword(sysUser.getPassword()));
				if(sysUserDao.getById(sysUser.getUserId())!=null) {
					sysUserDao.update(sysUser);
				}
				else {
					sysUserDao.add(sysUser);
				}
			}
			for (SysOrg sysOrg : sysOrgList) {
				if(sysOrgDao.getById(sysOrg.getOrgId())!=null) {
					sysOrgDao.update(sysOrg);
				}
				else {
					sysOrgDao.add(sysOrg);
				}
			}
			for (Position position : positionList) {
				if(positionDao.getById(position.getPosId())!=null) {
					positionDao.update(position);
				}
				else {
					positionDao.add(position);
				}
			}
		}catch (Exception e) {
			log=e.getMessage() + "\r\n" + "数据包导入异常，数据已全部回滚！";
			message = new ResultMessage(ResultMessage.Fail, "false");
			message.addData("log", log);
			message.setMessage(log.toString());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return message;
	}
	/**
	 * @Author shenguoliang
	 * @Description: 数据库同步(更新版)
	 * @Params [xmlProject, log]
	 * @Date 2018/6/9 10:55
	 * @Return void
	 */
	public ResultMessage asynDb(Project xmlProject, String localFcId, StringBuffer log, String xhId) throws Exception {
		ResultMessage message = null;
		try {
			if (xmlProject == null) {
				message = new ResultMessage(ResultMessage.Fail, "");
				message.addData("log", log);
				return message;
			}
			// 在本地数据库中查询选中的的发次是否与导入的发次匹配
			// 发次信息
			Map projectMap = projectDao.getById(Long.valueOf(localFcId));
			// 本地的发次代号
			String localFcdh = CommonTools.Obj2String(projectMap.get("F_FCDH"));
			// 型号信息
			Map productMap = productDao.getByFcId(Long.valueOf(localFcId));
			// 本地型号代号
			String localXhdh = CommonTools.Obj2String(productMap.get("F_XHDH"));
			// 导入的发次代号、型号代号
			String remoteFcdh = xmlProject.getFcdh();
			String remoteXhdh = xmlProject.getProductDh();
			if (!localFcdh.equals(remoteFcdh) || !localXhdh.equals(remoteXhdh)) {
				String error = "未选择型号代号为 [" + xmlProject.getProductDh() + "]-发次代号为 [" + xmlProject.getFcdh() + "] 的发次，"
						+ "您必须先选择或者新增对应的型号及发次！";
				message = new ResultMessage(ResultMessage.Fail, "false");
				message.addData("log", log);
				throw new RuntimeException(error);
			}
			// 根据本地的型号代号以及发次代号获取本地数据库的发次ID
			Project oldProject = projectDao.getByDh(xmlProject.getFcdh(), xmlProject.getProductDh());
			String newFcId = oldProject.getId();
			// 1.发次下-文件夹树(多层子文件夹情况)
			// 通过判定来进行新增与更新操作
			List<TemplateFLoder> floderList = xmlProject.getFloders();// 导入的模板文件夹
			if (floderList != null) {
				// 暂存本地数据库中不包含的文件夹
				List<TemplateFLoder> addFloderList = new ArrayList<>();
				// 暂存本地数据库需要更新的文件夹
				List<TemplateFLoder> updateFloderList = new ArrayList<>();

				Map tempFloderMap = new HashMap();
				tempFloderMap.put("F_PROJECT_ID", newFcId);
				tempFloderMap.put("F_TEMP_FILE_ID", "1");
				// 根据发次以及父文件夹名获取相对应子文件夹
				List<Map<String, Object>> queryFloderList = iOTemplateFLoderDao.query(tempFloderMap);

				List<TemplateFLoder> querytempFloderList = new ArrayList<>();
				HashSet querytempFloderSet = new HashSet<>();
				for (Map<String, Object> map : queryFloderList) {
					querytempFloderList.add(iOTemplateFLoderDao.getById(map.get("ID").toString()));
				}
				if (querytempFloderList.size() == 0 || querytempFloderList == null) {
					for (TemplateFLoder floder : floderList) {
						addFloderList.add(floder);
					}
				} else {
					// 区分新增或是修改的文件夹(导入的模板文件夹与本地模板文件夹对比)
					Map<String, Integer> localFloderMap = new HashMap<>();
					for (TemplateFLoder localFloder : floderList) {
						localFloderMap.put(localFloder.getName(), 1);
					}
					for (TemplateFLoder remoteFloder : querytempFloderList) {
						if (localFloderMap.get(remoteFloder.getName()) == null) {
							addFloderList.add(remoteFloder);
						} else {
							updateFloderList.add(remoteFloder);
						}
					}
				}

				// 新增文件夹以及该文件夹下面的表单模板
				if (addFloderList != null && addFloderList.size() > 0) {
					for (TemplateFLoder floder : addFloderList) {
						addFloder(floder, newFcId);
					}
				}
				// 更新文件夹以及该文件夹下面的表单模板
				if (updateFloderList != null && addFloderList.size() > 0) {
					for (TemplateFLoder floder : updateFloderList) {
						updateFloder(floder, newFcId);
					}
				}
			}

			// 2.发次下-表单模板(直接跟在发次下)
			List<TableTemp> tempList = xmlProject.getTableTempList();
			// 根据发次以及表单模板的编号校验是否能够新增表单模板
			Map queryTempMap = new HashMap();
			queryTempMap.put("F_TEMP_FILE_ID", "1");
			queryTempMap.put("F_PROJECT_ID", newFcId);
			// 存放本地数据库的表单模板
			List<TableTemp> localTableTempList = new ArrayList<>();

			// 根据过滤条件获取本地数据库的表单模板
			List<Map<String, Object>> localTempList = iOTableTempDao.query(queryTempMap);
			for (Map<String, Object> map : localTempList) {
				localTableTempList.add(iOTableTempDao.getById(map.get("ID").toString()));
			}

			// 表单模板校验,只能进行更新与新增操作
			if (tempList != null) {
				// 用于保存新增的表单模板(修改了表单模板的一些数据)
				List<TableTemp> updateTableTempList = new ArrayList<>();
				for (TableTemp temp : tempList) {
					if (localTableTempList.size() > 0) {
						// 本地存在表单模板时,需要先校验是否存在相同的模板
						for (TableTemp localTemp : localTableTempList) {
							if (temp.getId().equals(localTemp.getId())) {
								// 如果存在相同的编号,则删除本地数据库的该表单模板,以及相对应的检查项\检查条件等
								iOTableTempDao.deleteById(localTemp.getId(), true);
							}
						}
						// 不在文件夹内的表单模板的所属文件夹为"1"
						temp.setTemp_file_id("1");
						temp.setProject_id(newFcId);
						temp.setStatus("已完成");
						updateTableTempList.add(temp);
						this.iOTableTempDao.insertImp(temp, true);
					} else {
						// 去查询本地数据库中是否存在了编号为导入过来的表单模板
						queryTempMap.put("ID", temp.getId());
						// 根据过滤条件获取本地数据库的表单模板
						List<Map<String, Object>> localTabTempList = iOTableTempDao.query(queryTempMap);
						if (!(localTabTempList.size() > 0)) {
							// 新增表单模板
							// Long tempId = UniqueIdUtil.genId();
							Long tempId = Long.valueOf(temp.getId());
							temp.setId(tempId + "");
							// 不在文件夹内的表单模板的所属文件夹为"1"
							temp.setTemp_file_id("1");
							temp.setProject_id(newFcId);
							temp.setStatus("已完成");
							updateTableTempList.add(temp);

							this.iOTableTempDao.insertImp(temp, true);
						}
					}
				}
				// 更新表单模板的ID
				xmlProject.setTableTempList(updateTableTempList);
			}
			// 3.发次下-数据包节点树
			List<SimplePackage> packageList = xmlProject.getList();
			if (packageList != null) {
				for (SimplePackage pack : packageList) {
					pack.setSsfc(localFcId);
					pack.setSsxh(xhId);
					pack.setParentName(oldProject.getFcmc());
					// 同步数据包数据(只能进行新增与更新)
					this.asynPackage(pack, localFcId, xhId);
				}
			}
			message = new ResultMessage(ResultMessage.Success, "true");
			message.addData("log", "导入成功");
			message.setMessage("导入成功");
		} catch (Exception e) {
			log.append(e.getMessage() + "\r\n" + "数据包导入异常，数据已全部回滚！");
			message = new ResultMessage(ResultMessage.Fail, "false");
			message.addData("log", log);
			message.setMessage(log.toString());
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return message;
	}

	/**
	 * @param floder
	 *            ： xml中的文件夹对象
	 * @param newFcId
	 *            : 被更新的发次节点的主键
	 * @throws Exception
	 * 			@数据包同步策略： 1.插入xml数据包详细信息（包括数据包详细信息所有的关系表数据） 2.递归 3.进行新增操作
	 */
	private void addFloder(TemplateFLoder floder, String newFcId) throws Exception {

		floder.setProject_id(newFcId);
		this.iOTemplateFLoderDao.insert(floder);
		// 获取文件夹下的表单模板
		List<TableTemp> sonTempList = floder.getTableTempList();
		if (sonTempList != null) {
			for (TableTemp table : sonTempList) {
				table.setProject_id(newFcId);// 发次信息更新
				// 所属文件夹ID
				table.setTemp_file_id(floder.getId());
				this.iOTableTempDao.insertImp(table, true);
			}
		}
		// 获取子文件夹
		List<TemplateFLoder> sonList = floder.getSonFloderList();
		if (sonList != null) {
			for (TemplateFLoder son : sonList) {
				this.addFloder(son, newFcId);
			}
		}
	}

	/**
	 * @param floder
	 *            ： xml中的文件夹对象
	 * @param newFcId
	 *            : 被更新的发次节点的主键
	 * @throws Exception
	 * 			@数据包同步策略： 1.插入xml数据包详细信息（包括数据包详细信息所有的关系表数据） 2.递归 3.进行更新操作
	 */
	private void updateFloder(TemplateFLoder floder, String newFcId) throws Exception {

		floder.setProject_id(newFcId);
		this.iOTemplateFLoderDao.update(floder);
		List<TableTemp> sonTempList = floder.getTableTempList();
		if (sonTempList != null) {
			for (TableTemp table : sonTempList) {
				table.setProject_id(newFcId);// 发次信息更新
				// 所属文件夹ID
				table.setTemp_file_id(floder.getId());
				this.iOTableTempDao.insertImp(table, true);
			}
		}
		List<TemplateFLoder> sonList = floder.getSonFloderList();
		if (sonList != null) {
			for (TemplateFLoder son : sonList) {
				// 判断本地数据库中有无该文件夹，若没有新增，有则更新
				Map queryFloderMap = new HashMap();
				queryFloderMap.put("F_PROJECT_ID", newFcId);
				queryFloderMap.put("F_NAME", son.getName());
				// 根据发次以及父文件夹名获取相对应子文件夹
				List<Map<String, Object>> queryFloderList = iOTemplateFLoderDao.query(queryFloderMap);
				if (queryFloderList.size() > 0) {
					this.updateFloder(son, newFcId);
				} else {
					this.addFloder(son, newFcId);
				}
			}
		}
	}

	/**
	 * @Author shenguoliang
	 * @Description:数据包同步策略： 1.新增以及更新xml数据包详细信息（包括数据包详细信息所有的关系表数据） 2.递归
	 * @Params [pack]
	 * @Date 2018/6/11 10:06
	 * @Return void
	 */
	private void asynPackage(SimplePackage pack, String localFcId, String xhId) throws Exception {
		// 1.校验本地数据库中是否有相关的数据包节点
		// 2.确定是新增还是更新数据包节点数据

		Map keyMap = new HashMap();
		keyMap.put("F_JDLX", pack.getJdlx());
		keyMap.put("F_JDMC", pack.getJdmc());
		keyMap.put("F_PARENTNAME", pack.getParentName());
		keyMap.put("F_SSXH", pack.getSsxh());
		keyMap.put("F_SSFC", pack.getSsfc());

		List<Map<String, Object>> list = packageDao.query(keyMap);
		//
		List<SimplePackage> localPacklist = serverExportService.listToPackages(list);
		if (localPacklist.size() > 0) {
			// 此刻进行更新操作
			// 替换Id 以及ParentId
			pack.setId(localPacklist.get(0).getId());
			pack.setParentID(localPacklist.get(0).getParentID());
			// 更新数据包节点W_PACKAGE
			packageDao.update(pack);

			// 删除原有的数据包详细信息(包括检查条件结果,检查结果,签署结果),再进行新增
			dataPackageDao.deleteByPackage(localPacklist.get(0).getId(), true);

			// 删除原有的工作队信息,再进行新增
			this.teamDao.deleteByPackage(localPacklist.get(0).getId());

			List<TestTeam> teams = pack.getTeams();
			if (teams != null) {
				for (TestTeam team : teams) {
					// Long dataId = UniqueIdUtil.genId();
					// team.setId(dataId+"");

					// 设置所属数据包节点
					team.setSssjb(pack.getId() + "");
					this.teamDao.insert(team);
				}
			}

			List<DataObject> datas = pack.getDatas();

			if (datas != null) {
				for (DataObject data : datas) {

					// 设置所属数据包
					data.setSssjb(pack.getId());
					// 设置所属表单实例
					data.setSsmb(UniqueIdUtil.genId() + "");
					data.setId(UniqueIdUtil.genId() + "");
					this.dataPackageDao.insertDataObjectImp(data, pack, this.packageTempFolder);
				}
			}
		} else {
			// 新增数据包节点
			Long newPackId = UniqueIdUtil.genId();
			pack.setId(newPackId + "");
			// 新增数据包节点的时候,父节点需要提前获取
			Map map = new HashMap();
			map.put("F_JDLX", "普通分类节点");
			map.put("F_JDMC", pack.getParentName());
			map.put("F_SSXH", pack.getSsxh());
			map.put("F_SSFC", pack.getSsfc());

			List<Map<String, Object>> dataList = packageDao.query(map);
			List<SimplePackage> localPacklistCopy = serverExportService.listToPackages(dataList);
			if (localPacklistCopy.size() > 0 && !"0".equals(pack.getParentID())) {
				pack.setParentID(localPacklistCopy.get(0).getId());
			} else {
				pack.setParentID(0 + "");
			}
			packageDao.insert(pack);
			// 新增工作队记录
			List<TestTeam> teams = pack.getTeams();
			if (teams != null) {
				for (TestTeam team : teams) {
					// Long dataId = UniqueIdUtil.genId();
					// team.setId(dataId+"");
					// 设置所属数据包节点
					team.setSssjb(pack.getId() + "");
					this.teamDao.insert(team);
				}
			}
			List<DataObject> datas = pack.getDatas();
			if (datas != null) {
				for (DataObject data : datas) {
					Long dataId = UniqueIdUtil.genId();
					data.setId(dataId + "");
					// 设置所属数据包节点
					data.setSssjb(pack.getId() + "");
					// 设置所属数据包表单实例
					data.setSsmb(UniqueIdUtil.genId() + "");
					// 数据包详细信息(导入)
					this.dataPackageDao.insertDataObjectImp(data, pack, this.packageTempFolder);
				}
			}
		}
		// 递归
		List<SimplePackage> sonList = pack.getList();
		if (sonList != null) {
			for (SimplePackage son : sonList) {
				son.setSsfc(pack.getSsfc());
				son.setSsxh(pack.getSsxh());
				// 设置所属父节点
				son.setParentID(pack.getId());
				son.setParentName(pack.getJdmc());
				asynPackage(son, localFcId, xhId);
			}
		}
	}

	/**
	 * @param baseFloder
	 *            ：
	 * @return @返回主文件： 主文件格式 - xxx###.xml
	 */
	private File findMainFile(String baseFloder) {
		File floder = new File(baseFloder);
		File floder2 = floder.listFiles()[0];
		this.packageTempFolder = this.packageTempFolder + File.separator + floder2.getName();
		File[] files = floder2.listFiles();
		if (files.length == 0) {
			throw new RuntimeException("导入的压缩包中没有文件！");
		}
		File mainFile = null;
		boolean flag = false;
		for (File file : files) {
			if (file.isFile()) {
				String name = file.getName();
				if (name.endsWith(IOConstans.FILE_XML_UNIQUE + ".xml")) {
					flag = true;
					mainFile = file;
					break;
				}
			}
		}
		if (!flag) {
			throw new RuntimeException("导入的压缩包中没有发现主文件（以###.xml结尾的文件）！");
		}
		return mainFile;
	}

	/**
	 * 中转机-->服务器 通用导入方法(已废弃
	 * 会自动分流到合适的导入方法里
	 * @param file
	 * @return
	 */
	public ResultMessage commonImportDataEntrance(MultipartFile file){
		ResultMessage message=null;
		//先尝试转换为产品验收的
		try {
			message= serverImportInstance(file);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		if (message.getResult()==0){
			//如果产品验收导入失败,则试图导入靶场所检
			try {
				message=rangeTestPlanDataImport(file);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		return message;
	}


}
