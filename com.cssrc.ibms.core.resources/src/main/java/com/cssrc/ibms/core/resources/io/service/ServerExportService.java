package com.cssrc.ibms.core.resources.io.service;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;


import com.cssrc.ibms.core.resources.mission.dao.TestPlanDao;
import com.cssrc.ibms.dp.product.acceptance.dao.*;
import com.cssrc.ibms.dp.sync.bean.PadPhotoInfo;
import com.cssrc.ibms.dp.sync.dao.PadPhotoInfoDao;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.resources.datapackage.dao.DataPackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.PackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import com.cssrc.ibms.core.resources.datapackage.dao.TeamDao;
import com.cssrc.ibms.core.resources.datapackage.model.DataPackage;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.core.resources.io.bean.AcceptancePlan;
import com.cssrc.ibms.core.resources.io.bean.AcceptancePlanBean;
import com.cssrc.ibms.core.resources.io.bean.DataObject;
import com.cssrc.ibms.core.resources.io.bean.DpFile;
import com.cssrc.ibms.core.resources.io.bean.ExportData;
import com.cssrc.ibms.core.resources.io.bean.FileData;
import com.cssrc.ibms.core.resources.io.bean.Product;
import com.cssrc.ibms.core.resources.io.bean.ProductCategoryBath;
import com.cssrc.ibms.core.resources.io.bean.ProductCategoryBathBean;
import com.cssrc.ibms.core.resources.io.bean.ProductInfoBean;
import com.cssrc.ibms.core.resources.io.bean.RangeTestInstanceBean;
import com.cssrc.ibms.core.resources.io.bean.RangeTestPlanBean;
import com.cssrc.ibms.core.resources.io.bean.RangeTestPlanBeanOuter;
import com.cssrc.ibms.core.resources.io.bean.RangeTestTransferData;
import com.cssrc.ibms.core.resources.io.bean.TableInstanceBean;
import com.cssrc.ibms.core.resources.io.bean.TableTempBean;
import com.cssrc.ibms.core.resources.io.bean.TestTeam;
import com.cssrc.ibms.core.resources.io.bean.TranserDataBean;
import com.cssrc.ibms.core.resources.io.bean.TransferData;
import com.cssrc.ibms.core.resources.io.bean.datapackageModel.AcceptanceplanModel;
import com.cssrc.ibms.core.resources.io.bean.datapackageModel.ModelBean;
import com.cssrc.ibms.core.resources.io.bean.datapackageModel.OrgUserInfo;
import com.cssrc.ibms.core.resources.io.bean.datapackageModel.RangeTestPlanModel;
import com.cssrc.ibms.core.resources.io.bean.ins.CheckResult;
import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.pack.SimplePackage;
import com.cssrc.ibms.core.resources.io.bean.template.CheckCondition;
import com.cssrc.ibms.core.resources.io.bean.template.CheckItemDef;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import com.cssrc.ibms.core.resources.io.dao.FileDataDao;
import com.cssrc.ibms.core.resources.io.dao.IOCheckConditionDao;
import com.cssrc.ibms.core.resources.io.dao.IOCheckItemDefDao;
import com.cssrc.ibms.core.resources.io.dao.IOConditionResultDao;
import com.cssrc.ibms.core.resources.io.dao.IOConventionalDao;
import com.cssrc.ibms.core.resources.io.dao.IOSignDefDao;
import com.cssrc.ibms.core.resources.io.dao.IOSignResultDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableTempDao;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.resources.product.bean.ModuleManage;
import com.cssrc.ibms.core.resources.product.dao.ModuleDao;
import com.cssrc.ibms.core.resources.product.dao.ProductCategoryBatchDao;
import com.cssrc.ibms.core.resources.product.dao.ProductDao;
import com.cssrc.ibms.core.resources.product.service.ModuleManageService;

import com.cssrc.ibms.core.resources.util.listener.FileMoveEvenet;
import com.cssrc.ibms.core.user.dao.PositionDao;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.dao.UserPositionDao;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.ZipUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.dp.form.dao.CheckResultDao;
import com.cssrc.ibms.dp.form.dao.CkConditionResultDao;
import com.cssrc.ibms.dp.form.dao.ProductTypeDao;
import com.cssrc.ibms.dp.form.model.CkConditionResult;
import com.cssrc.ibms.dp.form.service.FormService;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.bean.ActualData;
import com.cssrc.ibms.dp.product.acceptance.bean.ProductInfo;
import com.cssrc.ibms.dp.product.acceptance.bean.WorkBoard;
import com.cssrc.ibms.dp.product.acceptance.bean.acceptanceReport;
import com.cssrc.ibms.dp.product.infor.dao.ProductInforDao;
import com.cssrc.ibms.dp.signModel.dao.CwmSysFileDao;
import com.cssrc.ibms.dp.signModel.dao.PADreturnSignModelDao;
import com.cssrc.ibms.dp.signModel.dao.SysSignModelDao;
import com.cssrc.ibms.dp.signModel.entity.CwmSysFile;
import com.cssrc.ibms.dp.signModel.entity.CwmSysSignModel;
import com.cssrc.ibms.dp.signModel.entity.SignModelDataPackage;
import com.cssrc.ibms.dp.signModel.entity.WPadhcqzb;
import com.cssrc.ibms.dp.sync.bean.Conventional;
import com.cssrc.ibms.dp.sync.dao.DataSyncDao;
import com.cssrc.ibms.dp.sync.util.SyncBaseFilter;
import com.cssrc.ibms.dp.template.dao.TemplateCheckItemDao;
import com.cssrc.ibms.dp.template.model.TemplateCheckItem;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SysFileService;
import com.cssrc.ibms.system.util.SysFileUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author user
 *         服务器间的导出
 */
@Service
public class ServerExportService implements ApplicationEventPublisherAware {
	protected Logger logger = LoggerFactory.getLogger(ServerExportService.class);

	@Resource
	private ProductDao productDao;
	@Resource
	private DataPackageDao dataPackageDao;
	@Resource
	private PackageDao packageDao;
	@Resource
	private TeamDao teamDao;
	@Resource
	private SysFileService sysFileService;
	@Resource
	private IOTableInstanceDao ioTableInstanceDao;
	@Resource
	private IOTableTempDao ioTableTempDao;
	@Resource
	DataSyncDao dataSyncDao;
	@Resource
	ProductTypeDao productTypeDao;
	@Resource
	private FormService formService;
	@Resource
	private AcceptancePlanDao acceptancePlanDao;
	@Resource
	private ProductCategoryBatchDao productCategoryBatchDao;
	@Resource
	private AcceptanceGroupDao acceptanceGroupDao;
	@Resource
	private IOSignDefDao signDefDao;
	@Resource
	private IOSignResultDao ioSignResultDao;
	@Resource
	private IOCheckConditionDao ioCheckConditionDao;
	@Resource
	private IOCheckItemDefDao ioCheckItemDefDao;
	@Resource
	private AcceptanceReportDao acceptanceReportDao;
	@Resource
	private IOTableTempDao iOTableTempDao;
	@Resource
	private SysFileDao sysFileDao;
	@Resource
	private ModuleDao moduleDao;
	@Resource
	private CkConditionResultDao conditionResultDao;
	@Resource
	private FileDataDao fileDataDao;
	@Resource
	private ProductInforDao productInforDao;
	@Resource
	private ActualDataDao actualDataDao;
	@Resource
	private IOConventionalDao ioConventionalDao;
	@Resource
	private SysUserDao sysUserDao;
	@Resource
	private SysOrgDao sysOrgDao;
	@Resource
	private PositionDao positionDao;
	@Resource
	private UserPositionDao userPositionDao;
	@Resource
	private WorkBoardDao workBoardDao;
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
	private CheckResultDao checkResultDao;
	@Resource
	private IOConditionResultDao iOConditionResultDao;
	@Resource
	private TemplateCheckItemDao templateCheckItemDao;
	@Resource
	private SecretLevelService secretLevelService;
	@Resource
	private PadPhotoInfoDao padPhotoInfoDao;
	@Resource
	private AcceptanceMessageDao acceptanceMessageDao;
	@Resource
	private TestPlanDao testPlanDao;

	private ApplicationEventPublisher applicationEventPublisher;


	private String packageTempFolder = "";

	public void clearTempFileFolder() {
		this.packageTempFolder = "";
	}

	/**
	 * 根据系统上传附件的根目录，创建临时文件夹
	 *
	 * @param name
	 */
	public void initTempFileFolder(String name) {
		this.packageTempFolder = SysConfConstant.UploadFileFolder + File.separator + "temp" + File.separator + name
				+ DateUtil.getCurrentDate("yyyyMMddHHmmss");
		FileOperator.createFolder(this.packageTempFolder);
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

	/*   *//**
			 * @Author shenguoliang
			 * @Description: 服务器导出(更新)
			 * @Params [fcId, nodeIds]
			 * @Date 2018/6/25 8:45
			 * @Return java.lang.String
			 */
	/*
	 * public String exportPackage(Long fcId, String nodeIds) {
	 *
	 * String zipFilePath = ""; try { String filePath = ""; Product product =
	 * this.getProductByFc(fcId); Project project = projectDao.getBeanById(fcId);
	 * project.setProductDh(product.getXhdh());
	 * project.setProductName(product.getXhdh()); //创建临时文件夹，用于后续生成压缩包
	 * initTempFileFolder(project.getFcmc()); zipFilePath = this.packageTempFolder +
	 * ".zip";
	 *
	 * //获取第一层数据包信息 List<SimplePackage> packs =
	 * this.getPackagesByIdsAndParId(nodeIds);
	 *
	 */
	/**
	 * 自动关联数据包节点的子节点(关联前台选中的子节点);
	 * 设置pack的子节点(根据前台选中的数据包节点)，同时保存表单实例的xml和附件的文件，以及检查表单检查项中照片。
	 *
	 * @throws ParseException
	 *//*
		 * for (Iterator it = packs.iterator(); it.hasNext(); ) { SimplePackage sonPack
		 * = (SimplePackage) it.next(); setSonPackagesByNodeIds(sonPack, nodeIds); }
		 * project.setList(packs); //模板文件夹
		 * project.setFloders(ioTemplateFLoderDao.getFloder(fcId, true, true));
		 *
		 * //发次下的表单模板 project.setTableTempList(ioTableTempDao.getByFc(fcId, true));
		 *
		 * String xml = XmlBeanUtil.marshall(project, Project.class); filePath =
		 * this.packageTempFolder + File.separator + project.getFcmc() +
		 * IOConstans.FILE_XML_UNIQUE + ".xml"; //
		 * FileOperator.writeFile(filePath,xml,"UTF-8");
		 * FileOperator.writeFile(filePath, xml, System.getProperty("file.encoding")); }
		 * catch (Exception e) { e.printStackTrace(); } finally { //压缩文件，并返回zip路径
		 * ZipUtil.zip(this.packageTempFolder, false, "GBK");
		 * FileOperator.delFoldsWithChilds(this.packageTempFolder);
		 * clearTempFileFolder(); } return zipFilePath;
		 *
		 * }
		 */

	/**
	 * 导出从pad回传到中转机的签章
	 * 	包括PADHCQZB和sign_model表的信息
	 * 		打包成zip返回zip物理地址给上层
	 * 	by zmz
	 * 	date 20200820
	 * @param ids
	 * @return
	 */
	public String exportPADSignModelToZip(String ids) {
		// zip物理地址
		String zipFilepath = "";
		String filePath = "";
		// 拆分id为String数组
		String[] id = ids.split(",");
		// 根据系统上传附件的根目录，创建临时文件夹

		// 确定了zip物理地址

		initTempFileFolder("PAD回传签章摆渡机数据导出", "公开");
		List<WPadhcqzb> wPadhcqzbList = new ArrayList<>();
		List<CwmSysSignModel> cwmSysSignModelList = new ArrayList<>();
		List<CwmSysFile> cwmSysFileList = new ArrayList<>();

		for (String signModelId : id) {
			// 获取W_PADHCQZB信息
			WPadhcqzb wPadhcqzb = pADreturnSignModelDao.selectById(signModelId);
			wPadhcqzbList.add(wPadhcqzb);
			// 根据用户id获取Sign_model表的信息
			cwmSysSignModelList.add(sysSignModelDao.selectByUserId(wPadhcqzb.getF_Yhid()));
			// 根据签章id(文件id)获取SYS_FILE文件信息
			cwmSysFileList.add(cwmSysFileDao.selectById(wPadhcqzb.getF_Qzid()));
		}
		// 整合到一个节点里
		SignModelDataPackage signModelDataPackage = new SignModelDataPackage();
		signModelDataPackage.setCwmSysFileList(cwmSysFileList);
		signModelDataPackage.setCwmSysSignModelsList(cwmSysSignModelList);
		signModelDataPackage.setwPadhcqzbsList(wPadhcqzbList);
		// 拷贝相关文件到压缩目录
		for (CwmSysFile sysFile : cwmSysFileList) {
			String annexSourcePath = SysConfConstant.UploadFileFolder + File.separator + sysFile.getFilepath();
			String filepaths = sysFile.getFilepath();
			String strpath = "";
			String filename = "";
			if (filepaths != null || !filepaths.equals("")) {
				int pos = filepaths.lastIndexOf("\\");
				strpath = filepaths.substring(0, pos);
				filename = filepaths.substring(pos + 1, filepaths.length());
			}
			String annexFilePath = SysFileUtil.createFilePath(this.packageTempFolder + File.separator + strpath, filename);
			FileOperator.copyFile(annexSourcePath, annexFilePath);
		}
		try {
			// entity转XML
			String xml = XmlBeanUtil.marshall(signModelDataPackage, SignModelDataPackage.class);
			filePath = this.packageTempFolder + File.separator + "摆渡机数据导出" + IOConstans.FILE_XML_UNIQUE + ".xml";
			FileOperator.writeFile(filePath, xml, System.getProperty("file.encoding"));
		} catch (JAXBException e) {
			e.printStackTrace();
		} finally {
			// 压缩文件，并返回zip路径
			ZipUtil.zip(this.packageTempFolder, false, "GBK");
			// 删除临时目录
			FileOperator.delFoldsWithChilds(this.packageTempFolder);
			//确定了zip物理地址
			zipFilepath = this.packageTempFolder + ".rar";
			clearTempFileFolder();
		}
		return zipFilepath;
	}

	/**
	 * 靶场试验 - 型号 - 全部任务 - 数据导出 的 导出按钮的逻辑
	 *
	 * @param ids
	 * @return
	 * @throws ParseException
	 *             by zmz 20200907
	 */
	public String ExportRangeTestPackages(String ids) {
		// 前置操作
		String zipFilepath = "";

		String filePath = "";



		//返回的数据集
		List<RangeTestPlanBean> rangeTestPlanBeanList=new ArrayList<>();
		//获取策划id
		String[] id=ids.split(",");
		for (String missionId:id){
			//根据系统上传附件的根目录，创建临时文件夹
			String secret=secretLevelService.getAllPlanSecretLevel(id);
			initTempFileFolder("摆渡机数据导出",secret);
			//以策划id为循环key,逐渐获取RangeTestPlanBean,最后合并到list里
			RangeTestPlanBean rangeTestPlanBean=new RangeTestPlanBean();
			//获取策划信息
			RangeTestTransferData rangeTestTransferData=new RangeTestTransferData();
			RangeTestPlan rangeTestPlan=rangeTestPlanDao.selectById(missionId);
			//获取数据包
			List<DataPackage> dataPackageList=new ArrayList<>();
			dataPackageList.addAll(dataPackageDao.getByPlanId(rangeTestPlan.getID()));
			// 获取策划的文件
			List<FileData> fileDataList = fileDataDao.getByPlanId(rangeTestPlan.getID());
			// 实例的bean
			List<RangeTestInstanceBean> rangeTestInstanceBeanList = new ArrayList<>();
			// 获取策划所有的实例
			List<TableInstance> tableInstanceList = ioTableInstanceDao.getByPlanId(rangeTestPlan.getID());
			if (tableInstanceList!=null){
				for (TableInstance tableInstance : tableInstanceList) {
					// 不需要废弃的表单
				/*if ("废弃".equals(tableInstance.getStatus())) {
					continue;
				}*/

					RangeTestInstanceBean rangeTestInstanceBean = new RangeTestInstanceBean();
					List<CkConditionResult> ckConditionResultList = new ArrayList<>();
					ckConditionResultList.addAll(conditionResultDao.getByModelId(tableInstance.getId()));
					List<SignResult> signResultList = ioSignResultDao.getByInsId(tableInstance.getId());

					//取签署  放在了和拍照信息一起取
					/*if(signResultList!=null){
						for (SignResult signResult : signResultList) {
							sysFileList.add(sysFileDao.getFileByDataId(signResult.getId()));
						}
					}
					if (sysFileList!=null){
						for (SysFile sysFile : sysFileList) {
							try {
								accordingFileDeal(sysFile);
							} catch (JAXBException e) {
								e.printStackTrace();
							}
						}
					}*/

					//获取实例下挂着的拍照签署信息
					List<PadPhotoInfo> padPhotoInfoList=padPhotoInfoDao.getByInstanceId(tableInstance.getId());
					//对应的sysFile类信息
					List<SysFile> sysFileList = new ArrayList<>();
					//取拍照和签署信息
					if (padPhotoInfoList!=null){
						for(PadPhotoInfo padPhotoInfo:padPhotoInfoList){
							sysFileList.add(sysFileDao.getFileByFileId(padPhotoInfo.getFileId()));
						}
					}

					if (sysFileList!=null){
						for (SysFile sysFile : sysFileList) {
							try {
								accordingFileDeal(sysFile,secret);
							} catch (JAXBException e) {
								e.printStackTrace();
							}
						}
					}

					rangeTestInstanceBean.setPadPhotoInfoList(padPhotoInfoList);
					rangeTestInstanceBean.setSysFileList(sysFileList);
					rangeTestInstanceBean.setSignResultList(signResultList);
					rangeTestInstanceBean.setCkConditionResultList(ckConditionResultList);
					rangeTestInstanceBean.setTableInstance(tableInstance);
					rangeTestInstanceBeanList.add(rangeTestInstanceBean);
				}
			}

			// 获取实例的签署信息
			String moduleId = rangeTestPlan.getF_XHID();
			// 获取检查表模板
			List<TableTemp> tableTempList = iOTableTempDao.getByProjectId(moduleId);
			if (tableInstanceList!=null){
				for (TableInstance tableInstance : tableInstanceList) {
					tableInstance.setSignResultList(ioSignResultDao.getByInsId(tableInstance.getId()));
					tableInstance.setConditionResultList(iOConditionResultDao.getByInsId(tableInstance.getId()));
					List<CheckResult> checkResultList=new ArrayList<>();
					//TableTemp.id->itemDef->id->itemId
					if (tableTempList!=null){
						for (TableTemp tableTemp:tableTempList){
							if (tableInstance.getTableTempID().equals(tableTemp.getId())){
								//确定是当前实例对应的检查表模板  取到了List<ItemDef>
								List<TemplateCheckItem> templateCheckItemList=templateCheckItemDao.getByTemplateId(tableTemp.getId());
								if (templateCheckItemList!=null){
									for (TemplateCheckItem templateCheckItem:templateCheckItemList){
										checkResultList.add((CheckResult)checkResultDao.getId(templateCheckItem.getId().toString(),Long.valueOf(tableInstance.getId())));
									}
								}


							}
						}
					}

					tableInstance.setCheckResultList(checkResultList);
				}
			}

			//下面被注释掉的这个代码块是取数据库内所有的拍照信息,上面的padPhotoInfo可以做到精准的取拍照信息.所以不需要了

			// 抽取pad拍照信息
			/*List<SysFile> photoFileList = sysFileDao.getPhoto();
			if (photoFileList!=null){
				for (SysFile sysFile : photoFileList) {
					try {
						accordingFileDeal(sysFile);
					} catch (JAXBException e) {
						e.printStackTrace();
					}
				}
			}*/

			// 取检查表模板
			List<TableTempBean> tableTempBeanList = new ArrayList<>();
			// 处理检查表信息
			if (tableTempList!=null){
				for (TableTemp tableTemp : tableTempList) {
					TableTempBean tableTempBean = new TableTempBean();
					tableTempBean.setTableTemp(tableTemp);
					tableTempBean.setCheckConditionList(ioCheckConditionDao.getByTempId(tableTemp.getId()));// 处理检查条件
					tableTempBean.setCheckItemDefList(ioCheckItemDefDao.getByTempId(tableTemp.getId()));
					tableTempBean.setSignDefList(signDefDao.getByTempId(tableTemp.getId()));
					tableTempBeanList.add(tableTempBean);
				}
			}

			// 保存策划
			rangeTestTransferData.setRangeTestPlan(rangeTestPlan);
			// 保存数据包
			rangeTestTransferData.setDataPackageList(dataPackageList);
			// 保存策划的文件
			rangeTestTransferData.setFileDataList(fileDataList);
			// 保存策划的实例
			rangeTestTransferData.setRangeTestInstanceBeanList(rangeTestInstanceBeanList);
			// 保存RangeTestTransferData信息
			rangeTestPlanBean.setRangeTestTransferData(rangeTestTransferData);
			// 保存pad拍照信息(非针对性,已废弃)
		//	rangeTestPlanBean.setPhotoFileList(photoFileList);
			// 保存模板集合
			rangeTestPlanBean.setTableTempBeanList(tableTempBeanList);
			// 当前根节点数据抽取完成
			rangeTestPlanBeanList.add(rangeTestPlanBean);
			//给相关的人发消息
			sendMessageWhenTransferExportDataPackage(missionId,"BCSY");
		}


		zipFilepath = this.packageTempFolder+ ".rar";

		RangeTestPlanBeanOuter outer = new RangeTestPlanBeanOuter();
		outer.setRangeTestPlanBeanList(rangeTestPlanBeanList);
		try {
			String xml = XmlBeanUtil.marshall(outer, RangeTestPlanBeanOuter.class);
			filePath = this.packageTempFolder + File.separator + "摆渡机数据导出" + IOConstans.FILE_XML_UNIQUE + ".xml";
			FileOperator.writeFile(filePath, xml, System.getProperty("file.encoding"));
		} catch (JAXBException e) {
			e.printStackTrace();
		} finally {
			// 压缩文件，并返回zip路径
			ZipUtil.zip(this.packageTempFolder, false, "GBK");
			FileOperator.delFoldsWithChilds(this.packageTempFolder);
			clearTempFileFolder();
		}

		return zipFilepath;
	}
	/**
	 * @Description 中转机生成压缩包之后给你相关的人发消息
	 * @Author ZMZ
	 * @Date 2020/12/16 14:46
	 * @param planId
	 * @param ofPart BCSY  YSCH
	 * @Return void
	 */
	private void sendMessageWhenTransferExportDataPackage(String planId, String ofPart) {
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
					acceptanceMessageDao.insertMessageForCommon(teamMember, planId, plan.get("F_CHBGBBH") + "-" + plan.get("F_SYRWMC"), "数据导出成功", "组长");
				} else {
					acceptanceMessageDao.insertMessageForCommon(teamMember, planId, plan.get("F_CHBGBBH") + "-" + plan.get("F_CPMC"), "数据导出成功", "组长");
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
						acceptanceMessageDao.insertMessageForCommon(teamMember, planId, plan.get("F_CHBGBBH") + "-" + plan.get("F_SYRWMC"), "数据导出成功", "组员");
					} else {
						acceptanceMessageDao.insertMessageForCommon(teamMember, planId, plan.get("F_CHBGBBH") + "-" + plan.get("F_CPMC"), "数据导出成功", "组员");
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
				acceptanceMessageDao.insertMessageForCommon(sponsor, planId, plan.get("F_CHBGBBH") + "-" + plan.get("F_SYRWMC"), "数据导出成功", "发起人");
			}
		}else {
			//验收没有发起人

		}

	}





	/**
	 * 摇摆机导出(产品批次的数据导出 displayId__=10000025850008
	 * 的业务逻辑
	 * @param ids
	 * @return
	 * @throws ParseException
	 * by fuyong
	 */
	public String exportTransferdata(String ids) throws ParseException {
		String zipFilepath = "";
		try {
			String filePath = "";
			TranserDataBean transerDataBean = new TranserDataBean();
			List<TransferData> transferDataList = new ArrayList<>();
			String[] id = ids.split(",");
			// 根据系统上传附件的根目录，创建临时文件夹

			List<AcceptancePlan> acceptancePlanList = new ArrayList<>();
			String security="";
			List<SysFile> fileList=new ArrayList<>();
			for (String acceptancePlanId : id) {
				//给相关人员发消息
				sendMessageWhenTransferExportDataPackage(acceptancePlanId,"YSCH");
				acceptancePlanList.add(new AcceptancePlan(acceptancePlanDao.getMapById(acceptancePlanId)));
			}
			for (AcceptancePlan acceptancePlan : acceptancePlanList) {
				JSONArray jsonArray=JSONArray.fromObject(acceptancePlan.getYsyjwj());
				for(int i=0;i<jsonArray.size();i++) {
					JSONObject jsonObject = jsonArray.optJSONObject(i);
					fileList.add(sysFileService.getById(CommonTools.Obj2Long(jsonObject.optString("id"))));
				}
			}
			if (fileList.size() != 0) { // 取最高密级
				for (SysFile sysFile : fileList) {
					if (sysFile.getSecurity() != null) {
						if (sysFile.getSecurity().equals("12")) {
							security = "机密";
						} else if (sysFile.getSecurity().equals("9")) {
							if (!security.equals("机密")) {
								security = "秘密";
							}
						} else if (sysFile.getSecurity().equals("6")) {
							if (!security.equals("机密") || !security.equals("秘密")) {
								security = "内部";
							}
						} else if (sysFile.getSecurity().equals("3")) {
							if (!security.equals("机密") || !security.equals("秘密") || !security.equals("内部")) {
								security = "公开";
							}
						}
					}
				}
			}
			initTempFileFolder("摆渡机数据导出",security);
			zipFilepath = this.packageTempFolder + ".rar";
			List<WorkBoard> workBoardList = new ArrayList<>();
		
			//非针对性的导出全部照片,  已废弃
			/*List<SysFile> photoFileList = new ArrayList<>();
			photoFileList = sysFileDao.getPhoto();
			for (SysFile sysFile : photoFileList) {
				accordingFileDeal(sysFile);
			}*/
			List<TableTempBean> tableTempBeanList = new ArrayList<>();
		//	transerDataBean.setPhotoFileList(photoFileList);
			for (AcceptancePlan acceptancePlan : acceptancePlanList) {
				Map<String, Object> productCategoryBathMap = productCategoryBatchDao
						.getById(acceptancePlan.getSscppc());
				Map<String, Object> productcategoryBathMap1 = productCategoryBatchDao
						.getById(productCategoryBathMap.get("F_SSCPLB").toString());
				List<TableTemp> tableTempList = iOTableTempDao
						.getByProjectId(productcategoryBathMap1.get("ID").toString());
				for (TableTemp tableTemp : tableTempList) {
					TableTempBean tableTempBean = new TableTempBean();
					tableTempBean.setCheckConditionList(ioCheckConditionDao.getByTempId(tableTemp.getId()));// 处理检查条件
					tableTempBean.setTableTemp(tableTemp);
					tableTempBean.setSignDefList(signDefDao.getByTempId(tableTemp.getId()));
					tableTempBean.setCheckItemDefList(ioCheckItemDefDao.getByTempId(tableTemp.getId()));
					tableTempBeanList.add(tableTempBean);
				}
				TransferData transferData = new TransferData();
				List<ProductInfoBean> productInfoBeanList = new ArrayList<>();
				transferData.setAcceptancePlan(acceptancePlan);
				transferData.setWorkBoard(workBoardDao.getByPlanId(acceptancePlan.getId()));
				List<DataPackage> dataPackageList = new ArrayList<>();
				dataPackageList.addAll(dataPackageDao.getByPlanId(acceptancePlan.getId()));
				List<FileData> fileDataList = fileDataDao.getByPlanId(acceptancePlan.getId());
				List<TableInstanceBean> tableInstanceBeanList = new ArrayList<>();
				List<TableInstance> tableInstanceList = ioTableInstanceDao.getByPlanId(acceptancePlan.getId());
				List<ProductInfo> productInfoList = productInforDao.getByPlanId(acceptancePlan.getId());
				List<Conventional> conventionalList = ioConventionalDao.getByPlanId(acceptancePlan.getId());
				transferData.setpConventionalList(conventionalList);
				for (ProductInfo productInfo : productInfoList) {
					ProductInfoBean productInfoBean = new ProductInfoBean();
					productInfoBean.setProductInfo(productInfo);
					List<ActualData> actualDataList = actualDataDao.getByPlanId(productInfo.getId());
					productInfoBean.setActualDataList(actualDataList);
					productInfoBeanList.add(productInfoBean);
				}
				transferData.setProductInfoBeanList(productInfoBeanList);
				//处理实例相关的信息
				for (TableInstance tableInstance : tableInstanceList) {
					TableInstanceBean tableInstanceBean = new TableInstanceBean();
					List<CkConditionResult> ckConditionResultList = new ArrayList<>();
					ckConditionResultList.addAll(conditionResultDao.getByModelId(tableInstance.getId()));
					tableInstanceBean.setTableInstance(tableInstance);
					List<SignResult> signResultList = ioSignResultDao.getByInsId(tableInstance.getId());
					//单独取签署图片,  这个走Padpzxx表,和照片一起取了
/*					for (SignResult signResult : signResultList) {
						sysFileList.add(sysFileDao.getFileByDataId(signResult.getId()));
					}
					for (SysFile sysFile : sysFileList) {
						accordingFileDeal(sysFile);
					}
					*/

					//取pad拍照信息及签署
					//获取实例下挂着的拍照签署信息
					List<PadPhotoInfo> padPhotoInfoList=padPhotoInfoDao.getByInstanceId(tableInstance.getId());
					//对应的sysFile类信息
					List<SysFile> sysFileList = new ArrayList<>();
					//取拍照和签署信息
					if (padPhotoInfoList!=null){
						for(PadPhotoInfo padPhotoInfo:padPhotoInfoList){
							sysFileList.add(sysFileDao.getFileByFileId(padPhotoInfo.getFileId()));
						}
					}
					//拷贝文件
					if (sysFileList!=null){
						for (SysFile sysFile : sysFileList) {
							accordingFileDeal(sysFile,security);
						}
					}

					tableInstanceBean.setPadPhotoInfoList(padPhotoInfoList);
					tableInstanceBean.setSignResultList(signResultList);
					tableInstanceBean.setSysFileList(sysFileList);

					tableInstanceBean.setCkConditionResultList(ckConditionResultList);
					tableInstanceBeanList.add(tableInstanceBean);
				}
				transferData.setDataPackageList(dataPackageList);
				transferData.setTableInstanceBeanList(tableInstanceBeanList);
				transferData.setFileDataList(fileDataList);
				transferDataList.add(transferData);
			}
			transerDataBean.setTableTempBeanList(tableTempBeanList);
			transerDataBean.setTransferDataList(transferDataList);
			String xml = XmlBeanUtil.marshall(transerDataBean, TranserDataBean.class);
			filePath = this.packageTempFolder + File.separator + "摆渡机数据导出" + IOConstans.FILE_XML_UNIQUE + ".xml";
			FileOperator.writeFile(filePath, xml, System.getProperty("file.encoding"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 压缩文件，并返回zip路径
			ZipUtil.zip(this.packageTempFolder, false, "GBK");
			FileOperator.delFoldsWithChilds(this.packageTempFolder);
			clearTempFileFolder();
		}
		return zipFilepath;
	}

	/**
	 * 抽取产品验收策划的表单下发的相关信息打包成zip
	 * note by zmz 20200826
	 * @param ids
	 * @return
	 */
	public String exportPackagedata(String ids) {
    	String zipFilePath = "";
    	try {
    		String filePath="";
    		String security="";
        	AcceptanceplanModel acceptanceplanModel=new AcceptanceplanModel();

			String[] id = ids.split(",");
			List<DataPackage> dataPackageList = new ArrayList<>();

			for (String string : id) {
				dataPackageList.add(new DataPackage(dataPackageDao.getById(Long.valueOf(string))));
			}
			AcceptancePlan acceptancePlan = new AcceptancePlan(
					acceptancePlanDao.getMapById(dataPackageList.get(0).getAcceptancePlanId()));
			WorkBoard workBoard = workBoardDao.getByPlanId(acceptancePlan.getId());
			workBoard.setXyb("下发表单到PAD");
			workBoard.setDqzt("策划审批通过");
			/* FileMoveEvenet fileMoveEvenet=new FileMoveEvenet(this, "收到了消息"); */
			/* applicationEventPublisher.publishEvent(fileMoveEvenet); */
			List<AcceptanceGroup> acceptanceGroupList = acceptanceGroupDao
					.getByAcceptancePanId(String.valueOf(acceptancePlan.getId()));
			List<SysUser> userList = new ArrayList<>();
			for (AcceptanceGroup acceptanceGroup : acceptanceGroupList) {
				if (acceptanceGroup.getXmId() != null && !acceptanceGroup.getXmId().equals("")) {
					userList.add(sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId())));
				}
			}
			ProductCategoryBath productCategoryBath = new ProductCategoryBath(
					productCategoryBatchDao.getById(acceptancePlan.getSscppc()));
			ProductCategoryBath productCategory = new ProductCategoryBath(
					productCategoryBatchDao.getById(productCategoryBath.getSscplb()));
			ModelBean modelBean = new ModelBean(moduleDao.getById(productCategory.getSsxh()));
			List<ModuleManage> moduleManageList = moduleManageService.getManageByModuleId(modelBean.getId());
			List<SysFile> sysFileList = new ArrayList<>();
			String fileInfo = acceptancePlan.getYsyjwj();
			if (fileInfo != null && !fileInfo.equals("")) { // 把验收文件添加到sysfile表中
				JSONArray jsonArray = JSONArray.fromObject(fileInfo);
				String fileId = "";
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jsonObject = jsonArray.optJSONObject(i);
					SysFile sysFile = sysFileDao.getFileByFileId(jsonObject.optString("id"));
					sysFileList.add(sysFile);
				}
			}
			if (sysFileList.size() != 0) { // 取最高密级
				for (SysFile sysFile : sysFileList) {
					if (sysFile.getSecurity() != null) {
						if (sysFile.getSecurity().equals("12")) {
							security = "机密";
						} else if (sysFile.getSecurity().equals("9")) {
							if (!security.equals("机密")) {
								security = "秘密";
							}
						} else if (sysFile.getSecurity().equals("6")) {
							if (!security.equals("机密") || !security.equals("秘密")) {
								security = "内部";
							}
						} else if (sysFile.getSecurity().equals("3")) {
							if (!security.equals("机密") || !security.equals("秘密") || !security.equals("内部")) {
								security = "公开";
							}
						}
					}
				}
			}
			// 设置压缩包文件名
			acceptanceplanModel.setName("刻盘压缩包");
			initTempFileFolder(acceptanceplanModel.getName(), security);
			zipFilePath = this.packageTempFolder + ".rar";
			for (SysFile sysFile : sysFileList) {
				accordingFileDeal(sysFile,security); // 移动文件
			}

			List<TableTempBean> tableTempBeanList = new ArrayList<>();
			for (DataPackage dataPackage : dataPackageList) {
				TableTempBean tableTempBean = new TableTempBean();
				TableTemp tableTemp = ioTableTempDao.getById(dataPackage.getTemplateId());
				List<SignDef> signDefList = signDefDao.getByTempId(tableTemp.getId());
				List<CheckCondition> checkConditionList = ioCheckConditionDao.getByTempId(tableTemp.getId());
				List<CheckItemDef> checkItemDefList = ioCheckItemDefDao.getByTempId(tableTemp.getId());
				tableTempBean.setCheckConditionList(checkConditionList);
				tableTempBean.setCheckItemDefList(checkItemDefList);
				tableTempBean.setSignDefList(signDefList);
				tableTempBean.setTableTemp(tableTemp);
				tableTempBeanList.add(tableTempBean);
			}
			acceptanceplanModel.setAcceptanceGroupList(acceptanceGroupList);
			acceptanceplanModel.setAcceptancePlan(acceptancePlan);
			acceptanceplanModel.setDataPackageList(dataPackageList);
			acceptanceplanModel.setProductCategory(productCategory);
			acceptanceplanModel.setProductCategoryBath(productCategoryBath);
			acceptanceplanModel.setSysFileList(sysFileList);
			acceptanceplanModel.setTableTempBeanList(tableTempBeanList);
			acceptanceplanModel.setModelBean(modelBean);
			acceptanceplanModel.setUserList(userList);
			acceptanceplanModel.setModuleManageList(moduleManageList);
			acceptanceplanModel.setWorkBoard(workBoard);
			//发消息
			sendMassageWhenServerExportForYSCH(acceptancePlan.getId());
			String xml = XmlBeanUtil.marshall(acceptanceplanModel, AcceptanceplanModel.class);
			filePath = this.packageTempFolder + File.separator + acceptanceplanModel.getName()
					+ IOConstans.FILE_XML_UNIQUE + ".xml";
			FileOperator.writeFile(filePath, xml, System.getProperty("file.encoding"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 压缩文件，并返回zip路径
			ZipUtil.zip(this.packageTempFolder, false, "GBK");
			FileOperator.delFoldsWithChilds(this.packageTempFolder);
			clearTempFileFolder();
		}
		return zipFilePath;

	}

	/**
	 * @Description 服务器生成靶场试验压缩包的时候发消息
	 * @Author ZMZ
	 * @Date 2020/12/7 9:22
	 * @param missionId
	 * @Return void
	 */
	public void sendMassageWhenServerExportForYSCH(String missionId){
		//定位策划
		Map<String,Object> rangeTestPlan=acceptancePlanDao.getMapById(missionId);
		//定位组员表
		List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByAcceptancePanId(missionId);
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			SysUser teamMember=sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
			if ("组长".equals(acceptanceGroup.getZw())){
				//是组长   直接发消息
				acceptanceMessageDao.insertMessageWhenServerExportZip(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"组长");
			}else {
				//不是组长
				//先看下这个人在组里是不是有担任组长的同时担任组员
				if (curUserIsTeamLeader(acceptanceGroupList,acceptanceGroup.getXmId())){
					//当前这个人既是组员也是组长
					//组长的消息是上面发的,这里直接跳过
				}else {
					//当前这个人不是组长,直接发消息
					acceptanceMessageDao.insertMessageWhenServerExportZip(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"组员");
				}
			}
		}
		//确定发起人
/*		String sponsorId=rangeTestPlan.get("F_FQRID").toString();
		SysUser sponsor=sysUserDao.getById(Long.valueOf(sponsorId));
		if (curUserInTeam(acceptanceGroupList,sponsorId)){
			//发起人属于组员/组长
			//该消息已由上面发送
		}else {
			//发起人不属于组员/组长
			acceptanceMessageDao.insertMessageWhenServerExportZip(sponsor,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"发起人");
		}*/
	}

	/**
	 * 抽取靶场实验策划的表单下发的相关数据打包
	 * by zmz
	 * 20200826
	 * 对数据包表单进行修正(因为pad开发需要,原来的"靶场数据包详细信息表"变更为"数据包详细信息表(产品验收)"
	 * 20200908
	 * @param datapackIds
	 * @return
	 */
	public String exportRangeTestPackagedata(String datapackIds) {
		String zipFilePath = "";
		String filePath="";
		//取id
		String[] id=datapackIds.split(",");
		//zip根节点
		RangeTestPlanModel rangeTestPlanModel=new RangeTestPlanModel();
		//数据包详细信息集合
		List<DataPackage> dataPackageList=new ArrayList<>();
		//设置压缩包文件名
		rangeTestPlanModel.setName("刻盘压缩包");

		/*String secretLevel=*/

		//取数据包的信息
		if (id!=null){
			for (String dataPackageId:id){
				Map<String,Object> map=dataPackageDao.getById(Long.valueOf(dataPackageId));
				DataPackage dataPackage =new DataPackage(map);
				dataPackageList.add(dataPackage);
			}
		}

		if (dataPackageList!=null){
			for (DataPackage dataPackage:dataPackageList){
				//修改表单状态为未开始
				dataPackage.setZxzt("未开始");
			}
		}

		//下发表单的操作是针对某一策划的,所以只会有一个rangeTestPlan,
		//这里虽然字段上取的是验收策划id,但实际是靶场试验策划id(物理表复用)
		RangeTestPlan rangeTestPlan=rangeTestPlanDao.selectById(dataPackageList.get(0).getAcceptancePlanId());


		//获取看板信息
		WorkBoard workBoard=workBoardDao.getByPlanId(rangeTestPlan.getID());
		workBoard.setDqzt("策划审批通过");;
		workBoard.setXyb("下发表单到PAD");
		//更改当前策划的服务器的看板信息为已生成压缩包之后的看板信息
		//	 * 当前状态:策划审批通过-->表单已下发
		//	 * 下一步:下发表单到中转机-->PAD采集数据
		workBoardDao.updatework(rangeTestPlan.getID(),"表单已下发","PAD数据采集");
		//获取实验组信息
		List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByMissionId(rangeTestPlan.getID());
		//获取实验组的人员信息
		List<SysUser> userList=new ArrayList<>();
		if (acceptanceGroupList!=null){
			for (AcceptanceGroup acceptanceGroup : acceptanceGroupList) {
				//这个xmid是姓名id,对应的是user表的人员id
				userList.add(sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId())));
			}
		}

		//获取型号信息
		ModelBean modelBean=new ModelBean(moduleDao.getById(rangeTestPlan.getF_XHID()));

		List<ModuleManage> moduleManageList=moduleManageService.getManageByModuleId(modelBean.getId());
		/**
		 * 拷贝靶场试验依据文件  W_BCSYCHBGB的物理磁盘文件
		 * 这里这么复杂应该是这个文件字段是json形式的,可以在一个字段里存很多个文件的那种
		 * 		写这里的时候数据库还没有相应的数据可供参考
		 */
		List<SysFile> sysFileList=new ArrayList<>();
		String fileInfo=rangeTestPlan.getF_SYYJWJ();
		if(fileInfo!=null&&!fileInfo.equals("")) {
			JSONArray jsonArray=JSONArray.fromObject(fileInfo);
			String fileId="";
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject jsonObject=jsonArray.optJSONObject(i);
				SysFile sysFile=sysFileDao.getFileByFileId(jsonObject.optString("id"));
				sysFileList.add(sysFile);
			}
		}
		String security=secretLevelService.getSecretLevel(rangeTestPlan.getF_SYYJWJ());
		initTempFileFolder(rangeTestPlanModel.getName(),security);
		zipFilePath = this.packageTempFolder + ".rar";
		if (sysFileList!=null){
			for (SysFile sysFile : sysFileList) {
				try {
					accordingFileDeal(sysFile,security);  //移动文件
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			}
		}


		List<TableTempBean> tableTempBeanList=new ArrayList<>();
		if (dataPackageList!=null){
			for (DataPackage dataPackage:dataPackageList){
				TableTempBean tableTempBean=new TableTempBean();
				TableTemp tableTemp=ioTableTempDao.getById(dataPackage.getTemplateId());
				List<SignDef> signDefList=signDefDao.getByTempId(tableTemp.getId());

				List<CheckCondition> checkConditionList=ioCheckConditionDao.getByTempId(tableTemp.getId());
				List<CheckItemDef> checkItemDefList=ioCheckItemDefDao.getByTempId(tableTemp.getId());
				tableTempBean.setCheckConditionList(checkConditionList);
				tableTempBean.setCheckItemDefList(checkItemDefList);
				tableTempBean.setSignDefList(signDefList);
				tableTempBean.setTableTemp(tableTemp);
				tableTempBeanList.add(tableTempBean);
			}
		}


		rangeTestPlanModel.setAcceptanceGroupList(acceptanceGroupList);
		rangeTestPlanModel.setRangeTestPlan(rangeTestPlan);
		rangeTestPlanModel.setDataPackageList(dataPackageList);
		rangeTestPlanModel.setSysFileList(sysFileList);
		rangeTestPlanModel.setTableTempBeanList(tableTempBeanList);
		rangeTestPlanModel.setModelBean(modelBean);
		rangeTestPlanModel.setUserList(userList);
		rangeTestPlanModel.setWorkBoard(workBoard);
		rangeTestPlanModel.setModuleManageList(moduleManageList);
		//发消息
		sendMassageWhenServerExportForBCSY(rangeTestPlan.getID());

		String xml = null;
		try {
			xml = XmlBeanUtil.marshall(rangeTestPlanModel, RangeTestPlanModel.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		filePath = this.packageTempFolder + File.separator + rangeTestPlanModel.getName()+IOConstans.FILE_XML_UNIQUE+ ".xml";
		FileOperator.writeFile(filePath, xml, System.getProperty("file.encoding"));


		//压缩文件，并返回zip路径

		ZipUtil.zip(this.packageTempFolder, false, "GBK");
		FileOperator.delFoldsWithChilds(this.packageTempFolder);
		clearTempFileFolder();

		return zipFilePath;
	}

	/**
	 * @Description 服务器生成靶场试验压缩包的时候发消息
	 * @Author ZMZ
	 * @Date 2020/12/7 9:22
	 * @param missionId
	 * @Return void
	 */
	public void sendMassageWhenServerExportForBCSY(String missionId){
		//定位策划
		Map<String,Object> rangeTestPlan=testPlanDao.getPlanById(missionId).get(0);
		//定位组员表
		List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByMissionId(missionId);
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			SysUser teamMember=sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
			if ("组长".equals(acceptanceGroup.getZw())){
				//是组长   直接发消息
				acceptanceMessageDao.insertMessageWhenServerExportZip(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"组长");
			}else {
				//不是组长
				//先看下这个人在组里是不是有担任组长的同时担任组员
				if (curUserIsTeamLeader(acceptanceGroupList,acceptanceGroup.getXmId())){
					//当前这个人既是组员也是组长
					//组长的消息是上面发的,这里直接跳过
				}else {
					//当前这个人不是组长,直接发消息
					acceptanceMessageDao.insertMessageWhenServerExportZip(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"组员");
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
			acceptanceMessageDao.insertMessageWhenServerExportZip(sponsor,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"发起人");
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
	 * 抽取靶场实验策划的表单下发的相关数据打包
	 * by zmz
	 * 20200826
	 * 对数据包表单进行修正(因为pad开发需要,原来的"靶场数据包详细信息表"变更为"数据包详细信息表(产品验收)"
	 * 20200908
	 * @param datapackIds
	 * @return
	 */
	public String exportWeaponCheckPackagedata(String datapackIds) {
		String zipFilePath = "";
		String filePath="";
		//取id
		String[] id=datapackIds.split(",");
		//zip根节点
		RangeTestPlanModel rangeTestPlanModel=new RangeTestPlanModel();
		//数据包详细信息集合
		List<DataPackage> dataPackageList=new ArrayList<>();
		//设置压缩包文件名
		rangeTestPlanModel.setName("刻盘压缩包");
		initTempFileFolder(rangeTestPlanModel.getName());
		zipFilePath = this.packageTempFolder + ".rar";
		//取数据包的信息
		for (String dataPackageId:id){
			Map<String,Object> map=dataPackageDao.getById(Long.valueOf(dataPackageId));
			DataPackage dataPackage =new DataPackage(map);
			dataPackageList.add(dataPackage);
		}
		for (DataPackage dataPackage:dataPackageList){
			//修改表单状态为未开始
			dataPackage.setZxzt("未开始");
		}
		//下发表单的操作是针对某一策划的,所以只会有一个rangeTestPlan,
		//这里虽然字段上取的是验收策划id,但实际是靶场试验策划id(物理表复用)
		RangeTestPlan rangeTestPlan=rangeTestPlanDao.selectById(dataPackageList.get(0).getAcceptancePlanId());

		//更改当前策划的服务器的看板信息为已生成压缩包之后的看板信息
		//	 * 当前状态:策划审批通过-->表单已下发
		//	 * 下一步:下发表单到中转机-->PAD采集数据
		//workBoardDao.updatework(rangeTestPlan.getID(),"表单已下发","PAD数据采集");
		//获取中转机看板信息
		WorkBoard workBoard=workBoardDao.getByPlanId(rangeTestPlan.getID());
		//改变服务器看板状态
		workBoardDao.updatework(rangeTestPlan.getID(),"表单已下发","PAD数据采集");
		//获取实验组信息
		List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByMissionId(rangeTestPlan.getID());
		//获取实验组的人员信息
		List<SysUser> userList=new ArrayList<>();
		for (AcceptanceGroup acceptanceGroup : acceptanceGroupList) {
			//这个xmid是姓名id,对应的是user表的人员id
			userList.add(sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId())));
		}
		//获取型号信息
		ModelBean modelBean=new ModelBean(moduleDao.getById(rangeTestPlan.getF_XHID()));

		/**
		 * 拷贝靶场试验依据文件  W_BCSYCHBGB的物理磁盘文件
		 * 这里这么复杂应该是这个文件字段是json形式的,可以在一个字段里存很多个文件的那种
		 * 		写这里的时候数据库还没有相应的数据可供参考
		 */
		List<SysFile> sysFileList=new ArrayList<>();
		String fileInfo=rangeTestPlan.getF_SYYJWJ();
		if(fileInfo!=null&&!fileInfo.equals("")) {
			JSONArray jsonArray=JSONArray.fromObject(fileInfo);
			String fileId="";
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject jsonObject=jsonArray.optJSONObject(i);
				SysFile sysFile=sysFileDao.getFileByFileId(jsonObject.optString("id"));
				sysFileList.add(sysFile);
			}
		}
		String security=secretLevelService.getSecretLevel(rangeTestPlan.getF_SYYJWJ());
		initTempFileFolder(rangeTestPlanModel.getName(),security);
		zipFilePath = this.packageTempFolder + ".rar";
		for (SysFile sysFile : sysFileList) {
			try {
				accordingFileDeal(sysFile);  //移动文件
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}

		List<TableTempBean> tableTempBeanList=new ArrayList<>();
		for (DataPackage dataPackage:dataPackageList){
			TableTempBean tableTempBean=new TableTempBean();
			TableTemp tableTemp=ioTableTempDao.getById(dataPackage.getTemplateId());
			List<SignDef> signDefList=signDefDao.getByTempId(tableTemp.getId());

			List<CheckCondition> checkConditionList=ioCheckConditionDao.getByTempId(tableTemp.getId());
			List<CheckItemDef> checkItemDefList=ioCheckItemDefDao.getByTempId(tableTemp.getId());
			tableTempBean.setCheckConditionList(checkConditionList);
			tableTempBean.setCheckItemDefList(checkItemDefList);
			tableTempBean.setSignDefList(signDefList);
			tableTempBean.setTableTemp(tableTemp);
			tableTempBeanList.add(tableTempBean);
		}

		rangeTestPlanModel.setAcceptanceGroupList(acceptanceGroupList);
		rangeTestPlanModel.setRangeTestPlan(rangeTestPlan);
		rangeTestPlanModel.setDataPackageList(dataPackageList);
		rangeTestPlanModel.setSysFileList(sysFileList);
		rangeTestPlanModel.setTableTempBeanList(tableTempBeanList);
		rangeTestPlanModel.setModelBean(modelBean);
		rangeTestPlanModel.setUserList(userList);
		rangeTestPlanModel.setWorkBoard(workBoard);

		/*initTempFileFolder(rangeTestPlanModel.getName());*/
		/*this.packageTempFolder=this.packageTempFolder+"("+secretLevelService.getSecretLevel(rangeTestPlan.getF_SYYJWJ())+")";
		zipFilePath = this.packageTempFolder + ".zip";*/

		String xml = null;
		try {
			xml = XmlBeanUtil.marshall(rangeTestPlanModel, RangeTestPlanModel.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		filePath = this.packageTempFolder + File.separator + rangeTestPlanModel.getName()+IOConstans.FILE_XML_UNIQUE+ ".xml";
		FileOperator.writeFile(filePath, xml, System.getProperty("file.encoding"));


		//压缩文件，并返回zip路径
		ZipUtil.zip(this.packageTempFolder, false, "GBK");
		FileOperator.delFoldsWithChilds(this.packageTempFolder);
		clearTempFileFolder();

		return zipFilePath;
	}

	/**
	 * 更改当前策划的服务器的看板信息为已生成压缩包之后的看板信息
	 * 当前状态:策划审批通过-->表单已下发
	 * 下一步:下发表单到中转机-->PAD采集数据
	 * @param id
	 * @return
	 */
/*	private void changeRangeTestPlanServerWorkBoard(String missionId) {
		WorkBoard workBoard=workBoardDao.getByPlanId(missionId);
		workBoard.setDqzt("表单已下发");
		workBoard.setXyb("PAD数据采集");

	}*/

	/**
	 * @Author fuyong
	 * @Description: 用户信息导出
	 * @Params [fcId, nodeIds]
	 * @Date 2020/8/10 8:45
	 * @Return java.lang.String
	 */
	public String ExuserInfo() {
		String zipFilePath = "";
		try {
			String filePath = "";
			OrgUserInfo orgUserInfo = new OrgUserInfo();
			List<SysUser> userList = sysUserDao.getAll();
			List<SysOrg> orgList = sysOrgDao.getAll();
			List<Position> positionList = positionDao.getAll();
			List<UserPosition> userPositionList = userPositionDao.getAll();
			orgUserInfo.setOrgList(orgList);
			orgUserInfo.setPositionList(positionList);
			orgUserInfo.setUserList(userList);
			orgUserInfo.setUserPositionList(userPositionList);
			initTempFileFolder("用户信息包");
			zipFilePath = this.packageTempFolder + ".zip";
			String xml = XmlBeanUtil.marshall(orgUserInfo, OrgUserInfo.class);
			filePath = this.packageTempFolder + File.separator + "用户信息包" + IOConstans.FILE_XML_UNIQUE + ".xml";
			FileOperator.writeFile(filePath, xml, System.getProperty("file.encoding"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 压缩文件，并返回zip路径
			ZipUtil.zip(this.packageTempFolder, false, "GBK");
			FileOperator.delFoldsWithChilds(this.packageTempFolder);
			clearTempFileFolder();
		}
		return zipFilePath;

	}

	/**
	 * @Author fuyong
	 * @Description: 服务器导出(更新)
	 * @Params [fcId, nodeIds]
	 * @Date 2020/6/4 8:45
	 * @Return java.lang.String
	 */
	public String exportPackage(Long fcId, String nodeIds) {
		String zipFilePath = "";
		try {
			String filePath = "";
			ExportData exportData = new ExportData();

			List<SysFile> sysFileList = new ArrayList<>(); // 关于文件的list
			List<ProductCategoryBathBean> productCategoryBathBeanList = new ArrayList<>(); // 策划的所有信息包括 验收依据文件，验收组
			List<Map<String, Object>> productCategorymap = productCategoryBatchDao.getByIds(String.valueOf(fcId),
					nodeIds);

			// 获取当前节点的所有模板
			List<TableTemp> tableTempList = ioTableTempDao.getByProjectId(String.valueOf(fcId));
			List<TableTempBean> tableTempBeanList = new ArrayList<>();

			// 设置压缩包文件名
			exportData.setName("刻盘压缩包");
			initTempFileFolder(exportData.getName());
			zipFilePath = this.packageTempFolder + ".rar";
			for (Map<String, Object> productCategory : productCategorymap) {
				ProductCategoryBathBean productCategoryBathBean = new ProductCategoryBathBean();
				ProductCategoryBath productCategoryBath = new ProductCategoryBath(productCategory);
				productCategoryBathBean.setProductCategoryBath(productCategoryBath);
				Map<String, Object> productCatergory = productCategoryBatchDao
						.getById(String.valueOf(productCategory.get("F_SSCPLB")));

				List<AcceptancePlanBean> acceptancePlanBeanList = new ArrayList<>();
				// 创建临时文件夹，用于后续生成压缩包
				List<Map<String, Object>> packs = acceptancePlanDao
						.getMapByPc(String.valueOf(productCategoryBath.getId()));

				for (Map<String, Object> map : packs) {
					AcceptancePlanBean acceptancePlanBean = new AcceptancePlanBean();

					List<acceptanceReport> acceptanceReportList = new ArrayList<>();
					List<AcceptanceGroup> acceptanceGroupList = new ArrayList<>();
					// 处理实例回传数据
					List<TableInstanceBean> tableInstanceBeanList = new ArrayList<>();
					// 处理实例回传数据
					List<DataPackage> dataPackageList = new ArrayList<>();
					// 处理验收依据文件
					String fileInfo = map.get("F_YSYJWJ") != null ? String.valueOf(map.get("F_YSYJWJ")) : "";
					if (!"".equals(fileInfo)) {
						fileInfo = fileInfo.substring(1, fileInfo.length() - 1);
						JSONObject jsonObject = JSONObject.fromObject(fileInfo);
						String fileId = jsonObject.getString("id");
						SysFile sysFile = sysFileService.getById(Long.valueOf(fileId));
						accordingFileDeal(sysFile);
						sysFileList.add(sysFile);
					}
					if (sysFileList.size() != 0) {
						for (SysFile sysFile : sysFileList) {
							if (sysFile.getSecurity() != null) {
								if (sysFile.getSecurity().equals("12")) {
									exportData.setName("刻盘压缩包(机密)");
								} else if (sysFile.getSecurity().equals("9")) {
									exportData.setName("刻盘压缩包(秘密)");
								} else if (sysFile.getSecurity().equals("6")) {
									exportData.setName("刻盘压缩包(内部)");
								} else if (sysFile.getSecurity().equals("3")) {
									exportData.setName("刻盘压缩包(公开)");
								}
							}
						}
					}
					// 处理验收报告下发时会复制新的模板
					tableTempList.addAll(ioTableTempDao.getByProjectId(String.valueOf(map.get("ID"))));

					// 处理策划对应验收人员信息
					acceptanceGroupList.addAll(acceptanceGroupDao.getByAcceptancePanId(String.valueOf(map.get("ID"))));

					// 处理回传实例
					List<TableInstance> tableInstances = ioTableInstanceDao.getByPlanId(String.valueOf(map.get("ID")));
					for (int i = 0; i < tableInstances.size(); i++) {
						TableInstanceBean tableInstanceBean = new TableInstanceBean();
						tableInstanceBean.setTableInstance(tableInstances.get(i));
						List<SignResult> signResultList = ioSignResultDao.getByInstantId(tableInstances.get(i).getId());
						tableInstanceBean.setSignResultList(signResultList);
						for (int c = 0; c > signResultList.size(); c++) {
							SysFile sysFile = sysFileService.getFileByDataId(signResultList.get(c).getId());
							sysFileList.add(sysFile);
							accordingFileDeal(sysFile); // 复制签署图片到压缩包
						}
						tableInstanceBeanList.add(tableInstanceBean);
					}
					acceptancePlanBean.setTableInstanceBeanList(tableInstanceBeanList);

					// 处理数据包信息
					dataPackageList.addAll(dataPackageDao.getByPlanId(String.valueOf(map.get("ID"))));

					// 处理验收报告信息
					acceptanceReportList.addAll(acceptanceReportDao.getByPlanId(String.valueOf(map.get("ID"))));
					// 处理验收策划数据
					AcceptancePlan acceptancePlan = new AcceptancePlan(map);
					acceptancePlanBean.setAcceptancePlan(acceptancePlan);
					acceptancePlanBean.setDataPackageList(dataPackageList);
					acceptancePlanBean.setAcceptanceReportList(acceptanceReportList);
					acceptancePlanBeanList.add(acceptancePlanBean);
				}
				productCategoryBathBean.setAcceptancePlanBeanList(acceptancePlanBeanList);
				productCategoryBathBean.setSysFileList(sysFileList);
				productCategoryBathBeanList.add(productCategoryBathBean);

			}

			// 处理模板签署和检查条件信息
			for (int i = 0; i < tableTempList.size(); i++) {
				TableTempBean tableTempBean = new TableTempBean();
				tableTempBean.setCheckConditionList(ioCheckConditionDao.getByTempId(tableTempList.get(i).getId()));// 处理检查条件
				tableTempBean.setTableTemp(tableTempList.get(i));
				tableTempBean.setSignDefList(signDefDao.getByTempId(tableTempList.get(i).getId()));
				tableTempBean.setCheckItemDefList(ioCheckItemDefDao.getByTempId(tableTempList.get(i).getId()));
				tableTempBeanList.add(tableTempBean);
			}

			exportData.setTableTempBeanList(tableTempBeanList);
			exportData.setProductCategoryBathBean(productCategoryBathBeanList);

			String xml = XmlBeanUtil.marshall(exportData, ExportData.class);
			filePath = this.packageTempFolder + File.separator + exportData.getName() + IOConstans.FILE_XML_UNIQUE
					+ ".xml";
			FileOperator.writeFile(filePath, xml, System.getProperty("file.encoding"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 压缩文件，并返回zip路径
			ZipUtil.zip(this.packageTempFolder, false, "GBK");
			FileOperator.delFoldsWithChilds(this.packageTempFolder);
			clearTempFileFolder();
		}
		return zipFilePath;

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
	
	// 依据文件处理
		public void accordingFileDeal(SysFile sysFile,String security) throws JAXBException {
			String sourcePath = SysConfConstant.UploadFileFolder + File.separator + sysFile.getFilepath();
			String filepaths = sysFile.getFilepath();
			String strpath = "";
			String filename = "";
			if (filepaths != null || !filepaths.equals("")) {
				int pos = filepaths.lastIndexOf("\\");
				strpath = filepaths.substring(0, pos);
				filename = filepaths.substring(pos + 1, filepaths.length());
				pos = filename.lastIndexOf(".");
				filename= filename.substring(0,pos)+"("+security+")"+filename.substring(pos,filename.length());
			}
			String filePath = SysFileUtil.createFilePath(this.packageTempFolder + File.separator + strpath, filename);
			/* String str=this.packageTempFolder + File.separator+strpath+"\\"; */
			FileOperator.copyFile(sourcePath, filePath);
		}
	
	
	

	// 验收策划数据处理
	public void AcceptanPlanDeal(AcceptancePlan acceptancePlan) throws JAXBException {
		String AcceptanPlanxml = XmlBeanUtil.marshall(acceptancePlan, AcceptancePlan.class);
		String filePath = this.packageTempFolder + File.separator + "验收策划数据" + IOConstans.FILE_XML_UNIQUE
				+ acceptancePlan.getId() + ".xml";
		FileOperator.writeFile(filePath, AcceptanPlanxml, System.getProperty("file.encoding"));
	}

	/**
	 * 更新表单模板contentIds
	 */
	private void updateTableTempContentByItemdefAllIds() {
		// this.updateItemdefIds(CommonTools.Obj2String("10000019360028"));
		// 获取tbTemp的ID
		List<Map<String, Object>> tbTempList = dataSyncDao.getListByTableNameAndFilter("W_TABLE_TEMP", null);
		for (int i = 0; i < tbTempList.size(); i++) {
			this.updateTempContentByItemdefIds(CommonTools.Obj2String(tbTempList.get(i).get("ID")));
		}
	}

	/**
	 * 更新检查表实例的content
	 */
	private void updateInstantContentByCKresultAllIds() {
		// this.updateItemdefIds(CommonTools.Obj2String("10000019360028"));
		// 获取tbTemp的ID
		List<Map<String, Object>> instantList = dataSyncDao.getListByTableNameAndFilter("W_TB_INSTANT", null);
		for (int i = 0; i < instantList.size(); i++) {
			this.updateInsContentCKResultIds(CommonTools.Obj2String(instantList.get(i).get("ID")));
		}
	}

    /**
     * 根据发次Id获取型号对象
     *
     * @param fcId : 发次Id
     * @return
     */
    private Product getProductByFc(Long fcId) {
        Map<String, Object> map = productDao.getByFcId(fcId);
        Product project = new Product(map);
        return project;
    }

    /**
     * 根据发次Id,获取第一层数据包节点集合
     *
     * @param fcId : 发次Id
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<SimplePackage> getRootPackages(Long fcId) {
        Map<String, Object> keyValueMap = new HashMap();
        keyValueMap.put("F_SSFC", fcId);
        keyValueMap.put("F_PARENTID", "0");
        List<Map<String, Object>> list = packageDao.query(keyValueMap);
        return this.listToPackages(list);
    }

    /**
     * 根据发次Id,获取数据包节点集合
     *
     * @param ids : 发次Id
     * @return
     */
    private List<SimplePackage> getPackagesByIds(String ids) {
        List<Map<String, Object>> list = packageDao.getByIds(ids);
        return this.listToPackages(list);
    }

    /**
     * 根据发次Id,获取第一层数据包节点集合(选中的数据包节点)
     *
     * @param ids : 发次Id
     * @return
     */
    public List<SimplePackage> getPackagesByIdsAndParId(String ids) {
        List<Map<String, Object>> list = packageDao.getByIdsAndParentId(ids);
        return this.listToPackages(list);
    }

    /**
     * 设置pack的所有子节点
     * 递归
     *
     * @param pack ：数据包节点对象
     */
    @SuppressWarnings({"rawtypes"})
    private void setSonPackages(SimplePackage pack) {
        String id = pack.getId();
        List<Map<String, Object>> list = packageDao.getSon(Long.valueOf(id));
        List<SimplePackage> sonPacks = this.listToPackages(list);
        for (Iterator it = sonPacks.iterator(); it.hasNext(); ) {
            SimplePackage sonPack = (SimplePackage) it.next();
            this.setSonPackages(sonPack);
        }
        pack.setList(sonPacks);
    }

    /**
     * 设置pack的子节点(根据前台选中的数据包节点)，同时保存表单实例的xml和附件的文件，以及检查表单检查项中照片
     * 递归
     *
     * @param pack ：数据包节点对象
     */
    @SuppressWarnings({"rawtypes"})
    private void setSonPackagesByNodeIds(SimplePackage pack, String nodeIds) {
        String id = pack.getId();
        List<Map<String, Object>> list = packageDao.getSon(Long.valueOf(id));
        //保存筛选过后的数据
        List<Map<String, Object>> copyList = new ArrayList<>();

        //判定获取的数据包节点是否在从前台选中的数据包节点中.
        for (Map map : list) {
            String queryId = CommonTools.Obj2String(map.get("ID"));
            if (nodeIds.contains(queryId)) {
                copyList.add(map);
            }
        }
        // list对象转数据包节点集合，同时保存表单实例的xml和附件的文件
        List<SimplePackage> sonPacks = this.listToPackages(copyList);

        for (Iterator it = sonPacks.iterator(); it.hasNext(); ) {
            SimplePackage sonPack = (SimplePackage) it.next();
            this.setSonPackagesByNodeIds(sonPack, nodeIds);
        }
        pack.setList(sonPacks);
    }

    /**
     * list对象转数据包节点集合，同时保存表单实例的xml和附件的文件
     *
     * @param list
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<SimplePackage> listToPackages(List<Map<String, Object>> list) {
        List<SimplePackage> blist = new ArrayList();
        for (Map<String, Object> map : list) {
            SimplePackage simple = new SimplePackage(map);
            simple.setTeams(getTeamInfo(map));
            //保存实例xml文件以及关联的照片和其他类型的文件。
            simple.setDatas(getDataObjectInfo(map));
            blist.add(simple);
        }
        return blist;
    }

    /**
     * 获取工作队信息
     *
     * @param map
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<TestTeam> getTeamInfo(Map<String, Object> map) {
        List<TestTeam> teams = new ArrayList();
        String sssjb = CommonTools.Obj2String(map.get("ID"));
        Map queryMap = new HashMap();
        queryMap.put("F_SSSJB", sssjb);
        List<Map<String, Object>> teamList = teamDao.query(queryMap);
        for (Map<String, Object> teamMap : teamList) {
            TestTeam team = new TestTeam(teamMap);
            teams.add(team);
        }
        return teams;
    }

    /**
     * 获取数据包节点详细信息对象
     *
     * @param map
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<DataObject> getDataObjectInfo(Map<String, Object> map) {
        List<DataObject> dataObjects = new ArrayList();
        String sssjb = CommonTools.Obj2String(map.get("ID"));
        Map queryMap = new HashMap();
        queryMap.put("F_SSSJB", sssjb);
        List<Map<String, Object>> dataList = dataPackageDao.query(queryMap);
        for (Map<String, Object> data : dataList) {
            DataObject dataObject = new DataObject(data);
            String type = CommonTools.Obj2String(data.get("F_SJLX"));
            String fileInfo = CommonTools.Obj2String(data.get("F_SJZ"));
            String tableInsId = CommonTools.Obj2String(data.get("F_SSMB"));
            if (type.toString().equals(IOConstans.DATA_PACKAGE_FORM)) {
                //  并生成xml和检查项照片
                dataObject.setSsmbmc(getTableIns(tableInsId));
            } else {
                if ("".equals(fileInfo)) {
                    //	continue;
                }
                dataObject.setFile(getDpFile(fileInfo));
            }
            dataObjects.add(dataObject);
        }
        return dataObjects;
    }

	/**
	 * 华东所bug,由于检查结果的ID与content的ID对应不起来，更新检查项结果的ID----FromFanZH
	 *
	 * @param instanId
	 */
	private void updateInsContentCKResultIds(String instanId) {
		String ckResultname = productTypeDao.getProductTypeTableNameByInstanId(Long.parseLong(instanId));
		// 获取instant的ID
		List<Map<String, Object>> instanList = dataSyncDao.getListByTableNameAndFilter("W_TB_INSTANT",
				Arrays.asList(new SyncBaseFilter("ID", "=", instanId)));
		String insContent = CommonTools.Obj2String(instanList.get(0).get("F_CONTENT"));
		// 获取现有的检查项结果的ID List
		List<Map<String, Object>> ck_ResultList = dataSyncDao.getListByTableNameAndFilter(ckResultname,
				Arrays.asList(new SyncBaseFilter("F_TB_INSTAN", "=", instanId)));
		List<Long> ckResult_IdList = new ArrayList<>();
		for (int i = 0; i < ck_ResultList.size(); i++) {
			ckResult_IdList.add(CommonTools.Obj2Long(ck_ResultList.get(i).get("ID")));
		}
		// Content中的ckresultID
		List<Long> content_IdList = new ArrayList<>();
		// 获取Content中检查结果的ID列表。
		Document tableDoc = null;
		try {
			tableDoc = DocumentHelper.parseText(insContent);
			List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");
			for (Element table : tables) {
				List<Element> inputs = table.selectNodes(".//input");
				if (inputs.size() > 0) {
					for (int i = 0; i < inputs.size(); i++) {
						String idString = inputs.get(i).attributeValue("id");
						if (idString != null) {
							content_IdList.add(Long.valueOf(idString));
						}
					}
				}
			}
			// 更新Content的ID
			if (ckResult_IdList.size() > 0 && ckResult_IdList.size() == content_IdList.size()) {
				Long ckResult_IdMin = Collections.min(ckResult_IdList);
				Long content_IdMin = Collections.min(content_IdList);
				if (!ckResult_IdMin.equals(content_IdMin)) {
					for (Element table : tables) {
						List<Element> inputs = table.selectNodes(".//input");
						if (inputs.size() > 0) {
							for (int i = 0; i < inputs.size(); i++) {
								String idString = inputs.get(i).attributeValue("id");
								// formService.getDiaPathByID();
								if (idString != null) {
									long diffId = Long.valueOf(idString) - content_IdMin;
									System.out.println("旧实例" + inputs.get(i).attribute("id"));
									String idStringNew = CommonTools.Obj2String(Long.valueOf(ckResult_IdMin + diffId));
									inputs.get(i).setAttributeValue("id", idStringNew);
									System.out.println("新实例" + inputs.get(i).attribute("id"));
									//
									List<Map<String, Object>> fileList = dataSyncDao.getListByTableNameAndFilter(
											"CWM_SYS_FILE", Arrays.asList(new SyncBaseFilter("TABLEID", "=",
													"'" + idString + "&&" + ckResultname + "'")));
									for (int j = 0; j < fileList.size(); j++) {
										String fileId = CommonTools.Obj2String(fileList.get(i).get("FILEID"));
										Map<String, String> fileMap = new HashMap<>();
										fileMap.put("TABLEID", idStringNew + "&&" + ckResultname);
										dataSyncDao.updateModelDataByTableName("CWM_SYS_FILE", fileMap, fileId);
									}
									// content_IdList.add(Long.valueOf(idString));
								}
							}
						}
						insContent = tableDoc.asXML();
						Map<String, Object> paramTableInsMap = new HashMap<>();
						paramTableInsMap.put("ID", instanId);
						paramTableInsMap.put("F_CONTENT", insContent);// 处理表格模板中的content
						formService.updateTableIns(paramTableInsMap);
					}
				}
			} else {

            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * 根据cont内容更新检查项定义的ID------针对华东所
	 *
	 * @param tbTempId
	 */
	private void updateTempContentByItemdefIds(String tbTempId) {

		// 获取tbTemp的ID
		List<Map<String, Object>> tbTempList = dataSyncDao.getListByTableNameAndFilter("W_TABLE_TEMP",
				Arrays.asList(new SyncBaseFilter("ID", "=", tbTempId)));
		String tbTempContent = CommonTools.Obj2String(tbTempList.get(0).get("F_CONTENTS"));
		// 获取现有的检查项定义的ID List
		List<Map<String, Object>> itemDefList = dataSyncDao.getListByTableNameAndFilter("W_ITEMDEF",
				Arrays.asList(new SyncBaseFilter("F_TABLE_TEMP_ID", "=", tbTempId)));
		List<Long> itemDef_IdList = new ArrayList<>();
		for (int i = 0; i < itemDefList.size(); i++) {
			itemDef_IdList.add(CommonTools.Obj2Long(itemDefList.get(i).get("ID")));
		}
		// Content中的itemID
		List<Long> tempContentIdList = new ArrayList<>();
		// 获取Content中检查项定义的ID列表。
		Document tableDoc = null;
		try {
			if (tbTempContent.equalsIgnoreCase("")) {
				return;
			}
			tableDoc = DocumentHelper.parseText(tbTempContent);
			List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");
			for (Element table : tables) {
				List<Element> inputs = table.selectNodes(".//input");
				if (inputs.size() > 0) {
					for (int i = 0; i < inputs.size(); i++) {
						String idString = inputs.get(i).attributeValue("id");
						if (idString != null) {
							tempContentIdList.add(Long.valueOf(idString));

						}
					}
				}
			}
			// 更新Temp Content的ID(模板的content)
			if (tempContentIdList.size() > 0 && tempContentIdList.size() == itemDef_IdList.size()) {
				Long itemIdMin = Collections.min(itemDef_IdList);
				// System.out.println(ckResult_OldIdList);
				// System.out.println(ckResult_NewIdList);
				Long tempContentIdMin = Collections.min(tempContentIdList);
				if (!itemIdMin.equals(tempContentIdMin)) {
					for (Element table : tables) {
						List<Element> inputs = table.selectNodes(".//input");
						if (inputs.size() > 0) {
							for (int i = 0; i < inputs.size(); i++) {
								String idString = inputs.get(i).attributeValue("id");
								if (idString != null) {
									long diffId = Long.valueOf(idString) - tempContentIdMin;
									System.out.println("旧模板" + inputs.get(i).attribute("id"));
									inputs.get(i).setAttributeValue("id",
											CommonTools.Obj2String(Long.valueOf(itemIdMin + diffId)));
									System.out.println("新模板" + inputs.get(i).attribute("id"));
								}
							}
						}
					}
					tbTempContent = tableDoc.asXML();
					Map<String, Object> paramTableTempMap = new HashMap<>();
					paramTableTempMap.put("ID", tbTempId);
					paramTableTempMap.put("F_CONTENT", tbTempContent);// 处理表格模板中的content
					formService.updateTableTempContent(paramTableTempMap);
				}
			} else {

			}

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取上传的文件信息
	 *
	 * @param fileInfo
	 * @return
	 */
	private DpFile getDpFile(String fileInfo) {
		DpFile file = null;
		try {
			JSONArray arr = JSONArray.fromObject(fileInfo);
			JSONObject obj = (JSONObject) arr.get(0);
			Long fileId = obj.getLong("id");
			SysFile sysFile = sysFileService.getById(fileId);
			file = new DpFile(sysFile);
			// 将文件导出到指定位置
			String sourcePath = SysConfConstant.UploadFileFolder + File.separator + file.getFilePath();
			String filename = file.getFullFileName();
			String targetPath = this.packageTempFolder + File.separator + filename;
			File sourceFile = new File(sourcePath);
			if (sourceFile.exists()) {
				FileOperator.copyFile(sourcePath, targetPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 获取表单实例，并生成xml和检查项照片
	 *
	 * @param id
	 * @return
	 */
	private String getTableIns(String id) {
		String name = "";
		try {
			TableInstance ins = ioTableInstanceDao.getById(id, true);
			if (ins == null) {
				return name;
			}
			// 导出检查项 附件（照片和附件）
			this.exportInsImages(ins);
			name = ins.getName() + IOConstans.TABLEFORM_XML_UNIQUE + ins.getId() + ".xml";
			String xml = XmlBeanUtil.marshall(ins, TableInstance.class);
			String filePath = this.packageTempFolder + File.separator + name;
			FileOperator.writeFile(filePath, xml, System.getProperty("file.encoding"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 导出检查项 附件（照片和附件）
	 *
	 * @param ins
	 */
	private void exportInsImages(TableInstance ins) {
		List<CheckResult> checkList = ins.getCheckResultList();
		for (CheckResult cr : checkList) {
			List<DpFile> files = cr.getImages();
			for (DpFile file : files) {
				String sourcePath = SysConfConstant.UploadFileFolder + File.separator + file.getFilePath();
				String targetPath = this.packageTempFolder + File.separator + file.getFullFileName();
				FileOperator.copyFile(sourcePath, targetPath);
			}
			DpFile sketchImage = cr.getSketchImage();
			if (sketchImage != null) {
				String sourcePath = SysConfConstant.UploadFileFolder + File.separator + sketchImage.getFilePath();
				String targetPath = this.packageTempFolder + File.separator + sketchImage.getFullFileName();
				FileOperator.copyFile(sourcePath, targetPath);
			}
		}
		List<SignResult> sList = ins.getSignResultList();
		for (SignResult s : sList) {
			DpFile f = s.getImage();
			if (f != null) {
				String sourcePath = SysConfConstant.UploadFileFolder + File.separator + f.getFilePath();
				String targetPath = this.packageTempFolder + File.separator + f.getFullFileName();
				FileOperator.copyFile(sourcePath, targetPath);
			}
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;

	}

}
