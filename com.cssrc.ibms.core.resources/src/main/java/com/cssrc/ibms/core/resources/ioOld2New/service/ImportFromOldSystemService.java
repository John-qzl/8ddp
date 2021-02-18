package com.cssrc.ibms.core.resources.ioOld2New.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cssrc.ibms.dp.form.dao.ProductTypeDao;
import com.cssrc.ibms.dp.form.service.FormService;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.dao.DataPackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.PackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.TeamDao;
import com.cssrc.ibms.core.resources.io.bean.DataObject;
import com.cssrc.ibms.core.resources.io.bean.DpFile;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.pack.SimplePackage;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.resources.ioOld2New.bean.CKFileIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.CellIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.CkIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.ConditionIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.ConditionResIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.DataObjectIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.HeaderIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.InstanceIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.ItemIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.PackageIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.RdmFileIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.RootIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.SignFileModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.SignIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.SignResIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.TaskIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.TemplateFolderIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.TemplateIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.bean.TestTeamIOModel;
import com.cssrc.ibms.core.resources.ioOld2New.dao.ImportFromOldSystemDao;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.ZipUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.dp.sync.util.SyncBaseFilter;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SysFileService;
import com.cssrc.ibms.system.util.SysFileUtil;

import net.sf.json.JSONObject;

@Service
public class ImportFromOldSystemService {

    protected Logger logger = LoggerFactory.getLogger(ImportFromOldSystemService.class);

    @Resource
    private ImportFromOldSystemDao importFromOldSystemDao;
    @Resource
    private ISysUserService sysUserService;
    private JdbcDao jdbcDao;

    @Resource
    private DataPackageDao dataPackageDao;
    @Resource
    private FormService formService;
    @Resource
    private PackageDao packageDao;
    @Resource
    private TeamDao teamDao;
    @Resource
    private SysFileService sysFileService;
    @Resource
    private IOTableInstanceDao tableInstanceDao;
    @Resource
    ProductTypeDao productTypeDao ;

    private String packageTempFolder = "";
    /*
    存储新旧文件夹ID的对应关系
     */
    private Map<String, String> tempFileIdMap = new HashMap<>();
    /*
       cellId与itemId对应表
        */
    private Map<String, String> cellId_itemIdMap = new HashMap<>();//
    /*
          存储检查项ID与描述的对应信息
      */
    private Map<String, String> id_DecriptionMap = new HashMap<>();//
    /*
       存储检查项ID与是否拍照的对应信息
     */
    private Map<String, String> id_PhotoMap = new HashMap<>();//
    /*
           存储检查项ID和类型与旧--新转换的对应信息
    */
    private Map<String, String> itemId_OldAndMap = new HashMap<>();//
    /*
              存储检查项ID和检查结果的ID的对应信息
       */
    private Map<String, String> itemId_CkResMap = new HashMap<>();//


    public void clearTempFileFolder() {
        this.packageTempFolder = "";
    }

    /**
     * 发次信息
     *
     * @param file 上传的zip
     * @param fcId 新的发次ID
     * @return
     * @throws Exception
     */
    public String importPakage(MultipartFile file, String fcId) throws Exception {
        StringBuffer log = new StringBuffer();
        //1.解压zip
        //1.1初始化解压临时文件夹
        initTempFileFolder(IOConstans.IMPORT_FLODER_NAME);
        //1.2解压zip
        ZipUtil.unZipFile(file, this.packageTempFolder);
        //2.1获取主文件，即发次名称.xml
        File mainFile = findMainFile(this.packageTempFolder, file.getOriginalFilename());
        System.out.println("XML Path >>> " + mainFile.getAbsolutePath());
        FileInputStream is = new FileInputStream(mainFile);
        //2.2解析主文件xml
        RootIOModel rootIOModel = XmlBeanUtil.unmarshall(is, RootIOModel.class);
        //3校验型号代号、发次名称是否一致
        String oldProjectName = file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - 4);
        String productId = validationProductCode(rootIOModel, fcId, oldProjectName);
        //4导入模板文件夹
        List<TemplateFolderIOModel> templateFolderList = rootIOModel.getTemplateFolderList();
        //4.1删除更新,首先清空当前发次下的所有的模板文件夹
        importFromOldSystemDao.deleteModelDataByTableNameAndFilters("W_TEMP_FILE", Arrays.asList(new SyncBaseFilter("F_PROJECT_ID", "=", fcId)));
        for (TemplateFolderIOModel templateFolderIO : templateFolderList) {
            saveTemplateFolderIntoDB(templateFolderIO, fcId, "1");//1代表初始化模板的父模板id
        }
        //5创建数据包结构树、数据包详情、实例、工作队数据
        //5.1删除更新,首先清空当前发次下的所有的模板信息
        List<Map<String, Object>> tempList = importFromOldSystemDao.getListByTableNameAndFilter("W_TABLE_TEMP", Arrays.asList(new SyncBaseFilter("F_PROJECT_ID", "=", fcId)));
        for (Map<String, Object> map : tempList) {
            String tempId = CommonTools.Obj2String(map.get("ID"));
            importFromOldSystemDao.deleteModelDataByTableNameAndFilters("W_ITEMDEF", Arrays.asList(new SyncBaseFilter("F_TABLE_TEMP_ID", "=", tempId)));
            importFromOldSystemDao.deleteModelDataByTableNameAndFilters("W_CK_CONDITION", Arrays.asList(new SyncBaseFilter("F_TABLE_TEMP_ID", "=", tempId)));
            importFromOldSystemDao.deleteModelDataByTableNameAndFilters("W_SIGNDEF", Arrays.asList(new SyncBaseFilter("F_TABLE_TEMP_ID", "=", tempId)));
        }
        importFromOldSystemDao.deleteModelDataByTableNameAndFilters("W_TABLE_TEMP", Arrays.asList(new SyncBaseFilter("F_PROJECT_ID", "=", fcId)));
        //5.2删除更新,首先清空当前发次下的所有的实例信息
        List<Map<String, Object>> packgeList = importFromOldSystemDao.getListByTableNameAndFilter("W_PACKAGE", Arrays.asList(new SyncBaseFilter("F_SSFC", "=", fcId)));
        for (Map<String, Object> packMap : packgeList) {
            String packId = CommonTools.Obj2String(packMap.get("ID"));
            List<Map<String, Object>> dataObList = importFromOldSystemDao.getListByTableNameAndFilter("W_DATAPACKAGEINFO", Arrays.asList(new SyncBaseFilter("F_SSSJB", "=", packId)));
            for (Map<String, Object> dataObMap : dataObList) {
                String instantId = CommonTools.Obj2String(dataObMap.get("F_SSMB"));
                if (!instantId.equalsIgnoreCase("")) {
                    importFromOldSystemDao.deleteModelDataByTableNameAndFilters("W_CONDI_RES", Arrays.asList(new SyncBaseFilter("F_TB_INSTAN_ID", "=", instantId)));
                    importFromOldSystemDao.deleteModelDataByTableNameAndFilters("W_SIGNRESULT", Arrays.asList(new SyncBaseFilter("F_TB_INSTANT_ID", "=", instantId)));

                    //根据表单实例ID区分检查结果表的类型
                    Map typeMap = productTypeDao.getProductType(Long.valueOf(instantId));
                    String ck_resultName = "" ;
                    if("空间".equals(typeMap.get("TYPE"))){
                        ck_resultName = "W_CK_RESULT" ;
                    }else if("运载".equals(typeMap.get("TYPE"))){
                        ck_resultName = "W_CK_RESULT_CARRY" ;
                    }else{
                        ck_resultName = "W_CK_RESULT_JGJG" ;
                    }
                    importFromOldSystemDao.deleteModelDataByTableNameAndFilters(ck_resultName, Arrays.asList(new SyncBaseFilter("F_TB_INSTAN", "=", instantId)));

                    importFromOldSystemDao.deleteModelDataByTableNameAndFilters("W_TB_INSTANT", Arrays.asList(new SyncBaseFilter("ID", "=", instantId)));
                }
            }
            importFromOldSystemDao.deleteModelDataByTableNameAndFilters("W_DATAPACKAGEINFO", Arrays.asList(new SyncBaseFilter("F_SSSJB", "=", packId)));
        }
        importFromOldSystemDao.deleteModelDataByTableNameAndFilters("W_PACKAGE", Arrays.asList(new SyncBaseFilter("F_SSFC", "=", fcId)));
        //5.3创建数据包结构树、数据包详情、实例、工作队数据
        List<PackageIOModel> packageList = rootIOModel.getPackageList();
        for (PackageIOModel packageIO : packageList) {
            savePackageIOModelIntoDB(packageIO, productId, fcId, "0", oldProjectName);//1代表数据包结构树初始化的父结构树id
        }
        //清除临时文件夹
        FileOperator.delFoldsWithChilds(this.packageTempFolder);
        return log.toString();
    }

    /**
     * 5创建数据包结构树、数据包详情、实例、工作队数据
     *
     * @param packageIO
     * @param productId
     * @param projectId
     * @param parentPackageId   结构树父节点ID
     * @param parentPackageName 结构树父节点名称
     * @throws Exception
     */
    private void savePackageIOModelIntoDB(PackageIOModel packageIO, String productId, String projectId, String parentPackageId, String parentPackageName) throws Exception {
        //新创建的结构树ID
        String newPackageDataId = "";
        //5.1 创建结构树---start
        //数据包结构树的节点类型：普通节点、单机、试验、软件、设计
        String packageType = packageIO.getType();
        Map<String, String> tPackageDataMap = new HashMap();
        //结构树节点的基本信息
        tPackageDataMap.put("F_JDMC", packageIO.getName());
        tPackageDataMap.put("F_JDLX", CommonTools.Obj2String(packageType));
        tPackageDataMap.put("F_SM", packageIO.getDesc());
        tPackageDataMap.put("F_PARENTID", CommonTools.Obj2String(parentPackageId));
        tPackageDataMap.put("F_PARENTNAME", CommonTools.Obj2String(parentPackageName));
        tPackageDataMap.put("F_SSXH", CommonTools.Obj2String(productId));
        tPackageDataMap.put("F_SSFC", CommonTools.Obj2String(projectId));
        if (!packageType.equals("普通分类节点")) {
            String userLoginName = packageIO.getUser();
            String userId = importFromOldSystemDao.getUserIdByUserName(userLoginName);
            //若找不到用户ID
            if (userId.equals("")) {
                throw new RuntimeException("导入失败，系统中未找到数据包负责人账号(" + userLoginName + "),请及时联系管理员创建此用户！");
            }
            ISysUser user = sysUserService.getById(Long.valueOf(userId));
            String userFullName = user.getFullname();
            tPackageDataMap.put("F_FZRID", userId);
            tPackageDataMap.put("F_FZR", userFullName);
            if (packageType.equals("设计节点")) {
                String planStartTime = packageIO.getPlanStartTime();
                String planEndTime = packageIO.getPlanEndTime();
                planEndTime = "to_date('" + planEndTime + "','yyyy-mm-dd')";
                planStartTime = "to_date('" + planStartTime + "','yyyy-mm-dd')";
                tPackageDataMap.put("F_DESI_JHKSSJ", CommonTools.Obj2String(planStartTime));
                tPackageDataMap.put("F_DESI_JHJSSJ", CommonTools.Obj2String(planEndTime));
            } else if (packageType.equals("单机节点")) {
                String partId = packageIO.getPartId();
                String partName = packageIO.getPartName();
                String status = packageIO.getPartstatus();
                String unit = packageIO.getUnit();
                tPackageDataMap.put("F_PART_CPDH", CommonTools.Obj2String(partId));
                tPackageDataMap.put("F_PART_CPMC", CommonTools.Obj2String(partName));
                tPackageDataMap.put("F_PART_ZT", CommonTools.Obj2String(status));
                tPackageDataMap.put("F_PART_ZRDW", CommonTools.Obj2String(unit));
            } else if (packageType.equals("软件节点")) {
                String partId = packageIO.getPartId();
                String partName = packageIO.getPartName();
                String status = packageIO.getPartstatus();
                String unit = packageIO.getUnit();
                String version = packageIO.getVersion();
                tPackageDataMap.put("F_SOFT_RJDH", CommonTools.Obj2String(partId));
                tPackageDataMap.put("F_SOFT_RJMC", CommonTools.Obj2String(partName));
                tPackageDataMap.put("F_SOFT_ZT", CommonTools.Obj2String(status));
                tPackageDataMap.put("F_SOFT_ZRDW", CommonTools.Obj2String(unit));
                tPackageDataMap.put("F_SOFT_BBH", CommonTools.Obj2String(version));
            } else if (packageType.equals("试验节点")) {
                //试验地点
                String site = packageIO.getSite();
                String unit = packageIO.getUnit();
                String planStartTime = packageIO.getPlanStartTime();
                String planEndTime = packageIO.getPlanEndTime();
                planStartTime = "to_date('" + planStartTime + "','yyyy-mm-dd')";
                planEndTime = "to_date('" + planEndTime + "','yyyy-mm-dd')";
                tPackageDataMap.put("F_TEST_SYDD", CommonTools.Obj2String(site));
                tPackageDataMap.put("F_TEST_CSDW", CommonTools.Obj2String(unit));
                tPackageDataMap.put("F_TEST_JHKSSJ", CommonTools.Obj2String(planStartTime));
                tPackageDataMap.put("F_TEST_JHJSSJ", CommonTools.Obj2String(planEndTime));
            } else {

            }
            //插入结构树信息
            newPackageDataId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("W_PACKAGE", tPackageDataMap));
            //5.2导入工作队信息
            //用来存放 旧的工作队ID  与 新的工作队的ID建立一对一关系
            Map<String, String> testTeamIdMap = new HashMap<String, String>();
            List<TestTeamIOModel> testTeamList = packageIO.getTestTeamList();
            saveTestTeamIOModelIntoDB(testTeamList, newPackageDataId, testTeamIdMap);
            //5.3导入工作项目
            Map<String, String> taskIdMap = new HashMap<String, String>();
            List<TaskIOModel> taskList = packageIO.getTaskList();
            for (TaskIOModel taskIO : taskList) {
                //				saveTaskIOModelIntoDB(taskIO, projectId, newPackageDataId, "-1", testTeamIdMap, taskIdMap);
            }
            //5.4导入数据详情（表单、文件、数据等等）
            List<DataObjectIOModel> dataObjectList = packageIO.getDataObjectList();
            saveDataObjectIOModelIntoDB(dataObjectList, newPackageDataId, testTeamIdMap, taskIdMap, projectId);

        } else {
            //插入结构树信息
            newPackageDataId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("W_PACKAGE", tPackageDataMap));
        }
        List<PackageIOModel> subPackageList = packageIO.getSubPackageList();
        for (PackageIOModel subPackageIO : subPackageList) {
            savePackageIOModelIntoDB(subPackageIO, productId, projectId, newPackageDataId, packageIO.getName());
        }
    }

    /**
     * 5.1.3导入数据详情（表单、文件、数据等等）
     *
     * @param dataObjectList
     * @param newPackageDataId
     * @param testTeamIdMap
     * @param taskIdMap
     * @param projectId
     * @throws Exception
     */
    private void saveDataObjectIOModelIntoDB(List<DataObjectIOModel> dataObjectList, String newPackageDataId, Map<String, String> testTeamIdMap, Map<String, String> taskIdMap, String projectId) throws Exception {
        itemId_OldAndMap.clear();
        cellId_itemIdMap.clear();
        id_DecriptionMap.clear();
        id_PhotoMap.clear();
        itemId_CkResMap.clear();
        for (DataObjectIOModel dataObjectIO : dataObjectList) {
            //数据包详情
            Map<String, String> dataObjectMap = new HashMap<String, String>();
            //根据检查表模板Id拷贝检查表模板到新的发次下
            String oldTemplateId = dataObjectIO.getTemplateId();
            String tableInstanceRelatedName = dataObjectIO.getTableInstanceRelatedName();//文件的真实名字
            String type = dataObjectIO.getType();

            if (type.equals("表单")) {
                String oldDataObjectName = dataObjectIO.getName();
                String oldDataObjectUid = dataObjectIO.getDataUid();
                //确定数据包详情dataId
                //				List<Map<String,Object>> projectList= importFromOldSystemDao.getListByTableNameAndFilter("W_DATAPACKAGEINFO",Arrays.asList(new SyncBaseFilter("F_SSSJB","=",newPackageDataId)));
                //				String newProjectCode =CommonTools.Obj2String(projectList.get(0).get("F_FCDH"));
                String xmlFilePath = this.packageTempFolder + File.separator + tableInstanceRelatedName + ".xml";
                File xmlFile = new File(xmlFilePath);
                if (!xmlFile.exists()) {
                    throw new RuntimeException("导入失败，数据包文件(" + tableInstanceRelatedName + ")格式错误或已损坏！");
                }
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(xmlFile);
                    InstanceIOModel instanceIOModel = XmlBeanUtil.unmarshall(fis, InstanceIOModel.class);
                    //第一步：保存表格模板、检查项、条件、签署-----1.表格模板
                    TemplateIOModel tempModel = instanceIOModel.getTemplate();
                    Map<String, String> tableTempMap = new HashMap<String, String>();
                    tableTempMap.put("F_NAME", CommonTools.null2String(tempModel.getName()));
                    tableTempMap.put("F_NUMBER", CommonTools.null2String(tempModel.getNumber()));
                    String oldTempHtml = CommonTools.null2String(tempModel.getContents().toString());
                    tableTempMap.put("F_CONTENTS", oldTempHtml);//处理表格模板中的content
                    tableTempMap.put("F_ROWNUM", CommonTools.null2String(tempModel.getRownum()));
                    tableTempMap.put("F_TYPE", "1");
                    tableTempMap.put("F_TEMP_FILE_ID", tempFileIdMap.get(CommonTools.Obj2String(tempModel.getTemplateFolderId())));
                    tableTempMap.put("F_PROJECT_ID", CommonTools.null2String(projectId));
                    String newTableTempId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("W_TABLE_TEMP", tableTempMap));
                    //第一步：保存表格模板、检查项、条件、签署-----2.保存检查项
                    List<HeaderIOModel> headList = tempModel.getHeaderList();
                    for (HeaderIOModel headerIOModel : headList) {
                        List<CellIOModel> cellList = headerIOModel.getCellList();
                        for (CellIOModel cellIOModel : cellList) {
                            String cellId = cellIOModel.getId();
                            ItemIOModel itemModel = cellIOModel.getItem();
                            if (itemModel != null) {
                                Map<String, String> itemMap = new HashMap<String, String>();
                                String itemId = CommonTools.null2String(itemModel.getId());
//                                itemMap.put("ID", itemId);
                                itemMap.put("F_NAME", CommonTools.null2String(itemModel.getName()));
                                itemMap.put("F_TYPE", CommonTools.null2String(itemModel.getType()));
                                itemMap.put("F_ILDD", CommonTools.null2String(itemModel.getIldd()));
                                itemMap.put("F_IILDD", CommonTools.null2String(itemModel.getIildd()));
                                itemMap.put("F_YCN" + "", CommonTools.null2String(itemModel.getYcn()));
                                itemMap.put("F_IFMEDIA", CommonTools.null2String(itemModel.getIfmedia()));
                                itemMap.put("F_DESCRIPTION" + "", CommonTools.null2String(itemModel.getDescription()));
                                itemMap.put("F_ZHYCDZ" + "", CommonTools.null2String(itemModel.getZhycdz()));
                                itemMap.put("F_NJLJYQ" + "", CommonTools.null2String(itemModel.getNjljyq()));
                                itemMap.put("F_TABLE_TEMP_ID", CommonTools.null2String(newTableTempId));
                                String newItemId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("W_ITEMDEF", itemMap));

                                itemId_OldAndMap.put(itemId + "_Type", CommonTools.null2String(itemModel.getType()));
                                itemId_OldAndMap.put(itemId, newItemId);
                                cellId_itemIdMap.put(cellId, newItemId);//因为旧模板中，是用的cellId与检查项关联的。
                                id_DecriptionMap.put(newItemId, CommonTools.null2String(itemModel.getDescription()));
                                id_PhotoMap.put(newItemId, CommonTools.null2String(itemModel.getIfmedia()));
                            }
                        }
                    }
                    //重点：根据模板检查项中的ID，更新模板信息
                    String newTempHtml = this.oldTempHtmlToNew(oldTempHtml, cellId_itemIdMap, id_DecriptionMap, id_PhotoMap);
                    Map<String, Object> paramTableTempMap = new HashMap<>();
                    paramTableTempMap.put("ID", newTableTempId);
                    paramTableTempMap.put("CONTENTS", newTempHtml);//处理表格模板中的content
                    paramTableTempMap.put("status", IOConstans.TABLE_TEMP_COMPLETE);
                    formService.updateTableTemp(paramTableTempMap);
//                    importFromOldSystemDao.updateModelDataByTableName("W_TABLE_TEMP", tableTempMap, newTableTempId);

                    //第一步：保存表格模板、检查项、条件、签署----3.保存检查条件
                    Map<String, String> conditionIdMap = new HashMap<String, String>();//用于存储新旧ID的map
                    List<ConditionIOModel> conditionList = tempModel.getConditionList();
                    for (ConditionIOModel conditionIOModel : conditionList) {
                        Map<String, String> conditionMap = new HashMap<String, String>();
                        conditionMap.put("F_TABLE_TEMP_ID", CommonTools.null2String(newTableTempId));
                        conditionMap.put("F_NAME", CommonTools.null2String(conditionIOModel.getName()));
                        conditionMap.put("F_ORDER", CommonTools.null2String(conditionIOModel.getOrder()));
                        String condtionId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("W_CK_CONDITION", conditionMap));
                        conditionIdMap.put(CommonTools.null2String(conditionIOModel.getId()), condtionId);
                    }
                    //第一步：保存表格模板、检查项、条件、签署----4.保存签署
                    Map<String, String> signIdMap = new HashMap<String, String>();//用于存储新旧ID的map
                    List<SignIOModel> signIOModelList = tempModel.getSignList();
                    for (SignIOModel signIOModel : signIOModelList) {
                        Map<String, String> signMap = new HashMap<String, String>();
                        signMap.put("F_TABLE_TEMP_ID", CommonTools.null2String(newTableTempId));
                        signMap.put("F_NAME", CommonTools.null2String(signIOModel.getName()));
                        signMap.put("F_ORDER", CommonTools.null2String(signIOModel.getOrder()));
                        String signId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("W_SIGNDEF", signMap));
                        signIdMap.put(CommonTools.null2String(signIOModel.getId()), signId);

                    }

                    //第二步：保存数据包详情和表格实例---1.保存实例instance
                    Map<String, String> instanceMap = new HashMap();
                    String instanceName = CommonTools.null2String(instanceIOModel.getName());
                    instanceMap.put("F_NAME", instanceName);
                    instanceMap.put("F_NUMBER", CommonTools.null2String(instanceIOModel.getNumber()));
                    instanceMap.put("F_TABLE_TEMP_ID", newTableTempId);
                    instanceMap.put("F_NUMBER", CommonTools.null2String(instanceIOModel.getNumber()));
                    String oldInstanceHtml = CommonTools.null2String(instanceIOModel.getContent());
                    instanceMap.put("F_CONTENT", oldInstanceHtml);//处理检查实例中的content
                    //					instanceMap.put("STATUS_", CommonTools.null2String(instanceIOModel.getStatus()));
                    //					instanceMap.put("VERSION_", CommonTools.null2String(instanceIOModel.getVersion()));
                    //					instanceMap.put("BB_", CommonTools.null2String(instanceIOModel.getBb()));
                    //表格实例主键ID
                    String instanceId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("W_TB_INSTANT", instanceMap));
                    dataObjectMap.put("F_SSMB", instanceId);//表单实例ID
                    dataObjectMap.put("F_SSMBMC", instanceName);//表单实例名称
                    //第二步：保存数据包详情和表格实例---2.保存检查条件结果ID
                    List<ConditionResIOModel> conditionresList = instanceIOModel.getConditionresList();
                    for (ConditionResIOModel CResModel : conditionresList) {
                        Map<String, String> conditionResMap = new HashMap<String, String>();
                        conditionResMap.put("F_TB_INSTAN_ID", CommonTools.null2String(instanceId));
                        conditionResMap.put("F_VALUE", CommonTools.null2String(CResModel.getValue()));
                        String conditionId = conditionIdMap.get(CResModel.getConditionid());//根据旧的id获取新的ID
                        conditionResMap.put("F_CONDITION_ID", conditionId);
                        //表格实例主键ID
                        String conditionResId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("W_CONDI_RES", conditionResMap));
                    }
                    //第二步：保存数据包详情和表格实例---3.保存签署结果ID和照片
                    List<SignResIOModel> signresList = instanceIOModel.getSignresList();
                    for (SignResIOModel SResModel : signresList) {
                        Map<String, String> signResMap = new HashMap<String, String>();
                        signResMap.put("F_TB_INSTANT_ID", CommonTools.null2String(instanceId));
                        signResMap.put("F_SIGNTIME", CommonTools.null2String(SResModel.getSigntime()));
                        String signId = signIdMap.get(SResModel.getSigndefid());//根据旧的id获取新的ID
                        signResMap.put("F_SIGNDEF_ID", signId);
                        //表格实例主键ID
                        String signResId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("W_SIGNRESULT", signResMap));

                        //保存照片
                        SignFileModel signFileModel = SResModel.getSignfile();
                        if (signFileModel != null) {
                            String filelocation = signFileModel.getFilelocation();
                            String oldFilePath = this.packageTempFolder + filelocation;
                            String newFileFolderPath = File.separator + "0" + File.separator + signResId;
                            FileOperator.createFolders(SysConfConstant.UploadFileFolder + newFileFolderPath);
                            String newFilePath = SysConfConstant.UploadFileFolder + newFileFolderPath + File.separator + signFileModel.getFileName() + ".jpg";
                            FileOperator.copyFile(oldFilePath, newFilePath);
                            File newFile = new File(newFilePath);
                            Map<String, String> fileMap = new HashMap<String, String>();
                            fileMap.put("DATAID", CommonTools.null2String(signResId));
                            fileMap.put("FILENAME", CommonTools.null2String(signFileModel.getFileName()));
                            fileMap.put("FILEPATH", newFileFolderPath + File.separator + signFileModel.getFileName() + ".jpg");
                            fileMap.put("CREATETIME", "to_date('" + DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + "','yyyy-mm-dd hh24:mi:ss')");
                            fileMap.put("EXT", "JPG");
                            fileMap.put("FILETYPE", "FILE");
                            fileMap.put("TOTALBYTES", CommonTools.Obj2String(newFile.length()));//大小
                            fileMap.put("NOTE", CommonTools.Obj2String(FileOperator.getSize(newFile.length())));//大小
                            ISysUser currentUser = UserContextUtil.getCurrentUser();
                            fileMap.put("CREATORID", CommonTools.Obj2String(currentUser.getUserId()));//上传人ID
                            fileMap.put("CREATOR", CommonTools.Obj2String(currentUser.getFullname()));//上传人
                            String sysFileId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("CWM_SYS_FILE", fileMap));
                        }
                    }
                    //第二步：保存数据包详情和表格实例---4.保存检查结果+照片
                    List<CkIOModel> ckList = instanceIOModel.getCkresList();
                    for (CkIOModel ckModel : ckList) {
                        //保存检查结果
                        Map<String, String> ckMap = new HashMap<String, String>();
                        ckMap.put("F_TB_INSTAN", CommonTools.null2String(instanceId));//新实例ID
                        String itemId_new = CommonTools.null2String(itemId_OldAndMap.get(ckModel.getItemdefid()));
                        ckMap.put("F_ITEMDEF_ID", itemId_new);//将检查项的新ID保存到检查结果中。
                        String resValue = CommonTools.null2String(ckModel.getValue());
                        //将复选框选中状态的 is 更改为true和false
                        if (CommonTools.Obj2String(itemId_OldAndMap.get(ckModel.getItemdefid() + "_Type")).equalsIgnoreCase("1")) {
                            if (resValue.equalsIgnoreCase("is")) {
                                resValue = "true";
                            } else {
                                resValue = "false";
                            }
                        }
                        ckMap.put("F_VALUE", resValue);
                        ckMap.put("F_IFNUMM", CommonTools.null2String(ckModel.getIfnull()));
                        ckMap.put("F_SKETCHMAP", CommonTools.null2String(ckModel.getSketchmap()));
                        ckMap.put("F_RESULT", CommonTools.null2String(ckModel.getResult()));
                        //根据表单实例ID区分检查结果表的类型
                        Map typeMap = productTypeDao.getProductType(Long.valueOf(instanceId));
                        String ck_resultName = "" ;
                        if("空间".equals(typeMap.get("TYPE"))){
                            ck_resultName = "W_CK_RESULT" ;
                        }else if("运载".equals(typeMap.get("TYPE"))){
                            ck_resultName = "W_CK_RESULT_CARRY" ;
                        }else{
                            ck_resultName = "W_CK_RESULT_JGJG" ;
                        }
                        String ckResultId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName(ck_resultName, ckMap));
                        itemId_CkResMap.put(itemId_new, ckResultId);
                        //保存照片
                        List<CKFileIOModel> ckFileList = ckModel.getCKFile();
                        if (ckFileList != null && ckFileList.size() > 0) {
                            for (CKFileIOModel ckFile : ckFileList) {
                                String filelocation = ckFile.getFilelocation();
                                String oldFilePath = this.packageTempFolder + filelocation;
                                String newFileFolderPath = File.separator + ckResultId + File.separator + "0";
                                FileOperator.createFolders(SysConfConstant.UploadFileFolder + newFileFolderPath);
                                String newFilePath = SysConfConstant.UploadFileFolder + newFileFolderPath + File.separator + ckFile.getFileName() + ".jpg";
                                FileOperator.copyFile(oldFilePath, newFilePath);
                                File newFile = new File(newFilePath);
                                Map<String, String> fileMap = new HashMap<String, String>();
                                fileMap.put("TABLEID", CommonTools.null2String(ckResultId));
                                fileMap.put("FILENAME", CommonTools.null2String(ckFile.getFileName()));
                                fileMap.put("FILEPATH", newFileFolderPath + File.separator + ckFile.getFileName() + ".jpg");
                                fileMap.put("CREATETIME", "to_date('" + DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + "','yyyy-mm-dd hh24:mi:ss')");
                                fileMap.put("EXT", "JPG");
                                fileMap.put("FILETYPE", "FILE");
                                fileMap.put("TOTALBYTES", CommonTools.Obj2String(newFile.length()));//大小
                                fileMap.put("NOTE", CommonTools.Obj2String(FileOperator.getSize(newFile.length())));//大小
                                ISysUser currentUser = UserContextUtil.getCurrentUser();
                                fileMap.put("CREATORID", CommonTools.Obj2String(currentUser.getUserId()));//上传人ID
                                fileMap.put("CREATOR", CommonTools.Obj2String(currentUser.getFullname()));//上传人
                                String sysFileId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("CWM_SYS_FILE", fileMap));
                            }
                        }
                        //导入完所有其他表格，再导入Table相关XML，最主要的问题就是DataObjectId和instanceId的绑定
                        //导入表格实例的HTML文件时需要重新更新表格实例的HTML
                        //第三步：更新HTML---1.保存检查结果+照片的对应结果---从cellId 转化到检查项ID,再转换到检查结果的ID
                        String newInstanceHtml = this.oldINstanceHtmlToNew(oldInstanceHtml);
                        Map<String, Object> paramTableInsMap = new HashMap<>();
                        paramTableInsMap.put("ID", instanceId);
                        paramTableInsMap.put("F_CONTENT", newInstanceHtml);//处理表格模板中的content
                        formService.updateTableIns(paramTableInsMap);
                    }
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException("导入失败，数据包文件(" + tableInstanceRelatedName + ")格式错误或已损坏！");
                        }
                    }
                }
            } else {//文件类型
                List<RdmFileIOModel> rdmFileList = dataObjectIO.getRdmFileList();
                if (rdmFileList.size() > 0) {
                    for (RdmFileIOModel rdmFileIO : rdmFileList) {
                        String fileName = rdmFileIO.getName();
                        String fileType = rdmFileIO.getType();
                        String fileRealName = rdmFileIO.getValue();
                        String uploadDate = rdmFileIO.getUploadDate();
                        String uploadUserName = rdmFileIO.getUploadUser();
                        ISysUser user = sysUserService.getByUsername(uploadUserName);
                        String uploadDateStr = rdmFileIO.getUploadDate();


                        String oldFilePath = this.packageTempFolder + File.separator + rdmFileIO.getValue();
                        String dateDay = DateUtil.getCurDate();
                        String monthNow = dateDay.substring(4, 6).indexOf("0") == 0 ? dateDay.substring(5, 6) : dateDay.substring(4, 6);
                        String dayNow = dateDay.substring(6, 8).indexOf("0") == 0 ? dateDay.substring(7, 8) : dateDay.substring(6, 8);
                        String newFileFolderPath = File.separator + dateDay.substring(0, 4) + File.separator + monthNow + File.separator + dayNow;
                        FileOperator.createFolders(SysConfConstant.UploadFileFolder + newFileFolderPath);
                        String newFilePath = SysConfConstant.UploadFileFolder + newFileFolderPath + File.separator + rdmFileIO.getValue();
                        FileOperator.copyFile(oldFilePath, newFilePath);
                        File newFile = new File(newFilePath);
                        Map<String, String> fileMap = new HashMap<String, String>();
                        fileMap.put("FILENAME", fileName.substring(0, fileName.indexOf("." + fileType)));
                        fileMap.put("FILEPATH", newFileFolderPath + File.separator + rdmFileIO.getValue());
                        fileMap.put("CREATETIME", "to_date('" + DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss") + "','yyyy-mm-dd hh24:mi:ss')");
                        fileMap.put("EXT", fileType);
                        fileMap.put("FILETYPE", "FILE");
                        fileMap.put("TOTALBYTES", CommonTools.Obj2String(newFile.length()));//大小
                        fileMap.put("NOTE", CommonTools.Obj2String(FileOperator.getSize(newFile.length())));//大小
                        fileMap.put("CREATORID", CommonTools.Obj2String(user.getUserId()));//上传人ID
                        fileMap.put("CREATOR", CommonTools.Obj2String(user.getFullname()));//上传人
                        String sysFileId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("CWM_SYS_FILE", fileMap));
                        String fileValue = "[{\"id\":\"" + sysFileId + "\",\"name\":\"" + fileName + "\"}]";
                        dataObjectMap.put("F_SJZ", fileValue);//数据值
                    }
                }

            }
            //数据包详情创建
            dataObjectMap.put("F_SSSJB", newPackageDataId);//所属数据包
            dataObjectMap.put("F_SJMC", CommonTools.Obj2String(dataObjectIO.getName()));//
            dataObjectMap.put("F_SJLX", CommonTools.Obj2String(dataObjectIO.getType()));//类型
            dataObjectMap.put("F_MJ", CommonTools.Obj2String(dataObjectIO.getDataSecrecy()));//密级
            String userId = importFromOldSystemDao.getUserIdByUserName(CommonTools.Obj2String(dataObjectIO.getCreatorName()));
            //若找不到用户ID
            if (userId.equals("")) {
                throw new RuntimeException("导入失败，系统中未找到工作团队（责任团队）账号(" + dataObjectIO.getCreatorName() + "),请及时联系管理员创建此用户！");
            }
            dataObjectMap.put("F_SCR", userId);//密级
            ISysUser user = sysUserService.getById(Long.valueOf(userId));
            String userFullName = user.getFullname();
            dataObjectMap.put("F_SCR", userFullName);//全名
            dataObjectMap.put("F_MJ", CommonTools.Obj2String(dataObjectIO.getDataSecrecy()));//密级
            String newTestTeamId = CommonTools.Obj2String(testTeamIdMap.get(dataObjectIO.getStation()));//z最新的工作队ID
            dataObjectMap.put("F_GW", newTestTeamId);//岗位
            dataObjectMap.put("F_BMQX", CommonTools.Obj2String(dataObjectIO.getSecretTime()));//保密期限
            dataObjectMap.put("F_SCSJ", "to_date('" + CommonTools.Obj2String(dataObjectIO.getCreateTime()) + "','yyyy-mm-dd hh24:mi:ss')");//创建时间
            dataObjectMap.put("F_BB", CommonTools.Obj2String(dataObjectIO.getVersion()));//版本
            dataObjectMap.put("F_ZXZT", CommonTools.Obj2String(dataObjectIO.getStatus()));//执行状态
            importFromOldSystemDao.insertModelDataByTableName("W_DATAPACKAGEINFO", dataObjectMap);
        }

    }

    /**
     * 将旧实例html按照新的html导入系统中
     *
     * @param oldInstanceHtml
     * @return
     */
    private String oldINstanceHtmlToNew(String oldInstanceHtml) throws DocumentException {
        String newTempHtml = "";
        Document tableDoc = null;
        try {
            oldInstanceHtml = oldInstanceHtml.replaceAll("&", "#");
            oldInstanceHtml = oldInstanceHtml.replaceAll("#nbsp;", "");
            oldInstanceHtml = oldInstanceHtml.replaceAll("Table", "table");
            oldInstanceHtml = "<html>" + oldInstanceHtml + "</html>";
            tableDoc = DocumentHelper.parseText(oldInstanceHtml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //取得根结点
        Element root_Html = tableDoc.getRootElement();
        root_Html.remove(root_Html.element("script"));
        //1、table操作
        Element element_Table = root_Html.element("table");
        Attribute attr_Table_Bor = element_Table.attribute("border");
        element_Table.remove(attr_Table_Bor);
        element_Table.addAttribute("class", "layui-table");
        //2.colgroup操作
        Element element_Colgroup = element_Table.addElement("colgroup");
        //3.tbody操作
        //3.1处理表头信息
        Element element_Thead = element_Table.element("thead");
        Element element_Thead_Tr = element_Thead.element("tr");
        element_Thead_Tr.addAttribute("class", "firstRow");
        //3.2更新tableDoc--合并表头和内容
        oldInstanceHtml = tableDoc.asXML();
        oldInstanceHtml = oldInstanceHtml.replaceAll("</thead>", "");
        oldInstanceHtml = oldInstanceHtml.replaceAll("<tbody>", "");
        oldInstanceHtml = oldInstanceHtml.replaceAll("thead", "tbody");
        oldInstanceHtml = oldInstanceHtml.replaceAll("#nbsp;", "");
        tableDoc = DocumentHelper.parseText(oldInstanceHtml);
        //3.3更新tableDoc
        //3.3.1重新获取得根结点
        root_Html = tableDoc.getRootElement();
        element_Table = root_Html.element("table");
        Element element_Tbody = element_Table.element("tbody");
        List<Element> list_Tbody_Tr = element_Tbody.selectNodes(".//tr");

//        element_Tbody.sele
        for (Element element_Tbody_Tr : list_Tbody_Tr) {
            List<Element> list_Tbody_Tr_Td = element_Tbody_Tr.selectNodes(".//td");
            for (Element element_Tbody_Tr_Td : list_Tbody_Tr_Td) {
                String text = CommonTools.Obj2String(element_Tbody_Tr_Td.getText());
                if (text.equalsIgnoreCase("")) {//非描述信息
                    Boolean photoFlag = false;
                    if (element_Tbody_Tr_Td.selectNodes(".//input") != null && element_Tbody_Tr_Td.selectNodes(".//input").size() > 0) {//填值和照片
                        List<Element> list_Tbody_Tr_Td_Input = element_Tbody_Tr_Td.selectNodes(".//input");
                        for (Element inputElement : list_Tbody_Tr_Td_Input) {
                            if (CommonTools.Obj2String(inputElement.attributeValue("onclick")).equalsIgnoreCase("")) {//填值
                                element_Tbody_Tr_Td.addAttribute("input", "1");
//                                inputElement.remove(inputElement.attribute("type"));
                                inputElement.remove(inputElement.attribute("readonly"));
                                String itemId = itemId_CkResMap.get(cellId_itemIdMap.get(inputElement.attributeValue("id")));//从cellId 转化到检查项ID,再转换到检查结果的ID
                                inputElement.addAttribute("title", CommonTools.Obj2String(id_DecriptionMap.get(itemId)));
                                inputElement.addAttribute("class", "dpInputText");
                                inputElement.addAttribute("id", itemId);
                                if (CommonTools.Obj2String(id_PhotoMap.get(itemId)).equalsIgnoreCase("TRUE")) {
                                    photoFlag = Boolean.TRUE;
                                }
                            }
                        }
                    }
                    if (element_Tbody_Tr_Td.selectNodes(".//img") != null && element_Tbody_Tr_Td.selectNodes(".//img").size() > 0) {//复选框
                        element_Tbody_Tr_Td.addAttribute("checkbox", "1");
                        Element element_Tbody_Tr_Td_Img = element_Tbody_Tr_Td.element("img");
                        //获取检查项最新的ID
                        String itemId = itemId_CkResMap.get(cellId_itemIdMap.get(element_Tbody_Tr_Td_Img.attributeValue("id")));//从cellId 转化到检查项ID,再转换到检查结果的ID
                        //获取复选框的照片流
                        String srcVal = element_Tbody_Tr_Td_Img.attributeValue("src");
                        Element checkboxElelment = element_Tbody_Tr_Td.addElement("input");
                        if (srcVal.substring(srcVal.length() - 8, srcVal.length()).equalsIgnoreCase("rkJggg==")) {//勾选
                            checkboxElelment.addAttribute("checked", "checked");
                        } else {
                        }
                        checkboxElelment.addAttribute("type", "checkbox");
                        checkboxElelment.addAttribute("disabled", "true");
                        checkboxElelment.addAttribute("class", "dpCheckbox");
                        checkboxElelment.addAttribute("title", CommonTools.Obj2String(id_DecriptionMap.get(itemId)));
                        checkboxElelment.addAttribute("id", itemId);
                        if (CommonTools.Obj2String(id_PhotoMap.get(itemId)).equalsIgnoreCase("TRUE")) {
                            photoFlag = Boolean.TRUE;
                        }
                        element_Tbody_Tr_Td.remove(element_Tbody_Tr_Td_Img);
                    }
                    if (photoFlag) {
                        element_Tbody_Tr_Td.addAttribute("photo", "1");
                    }
                    if (element_Tbody_Tr_Td.selectNodes(".//input") != null && element_Tbody_Tr_Td.selectNodes(".//input").size() > 0) {//填值和照片
                        List<Element> list_Tbody_Tr_Td_Input = element_Tbody_Tr_Td.selectNodes(".//input");
                        for (Element inputElement : list_Tbody_Tr_Td_Input) {
                            if (!CommonTools.Obj2String(inputElement.attributeValue("onclick")).equalsIgnoreCase("")) {//拍照
                                Element photoElelment = element_Tbody_Tr_Td.addElement("input");
                                photoElelment.addAttribute("class", "dpInputBtn");
                                photoElelment.addAttribute("type", "button");
                                photoElelment.addAttribute("disabled", "true");
                                photoElelment.addAttribute("value", "附件");
                                photoElelment.addAttribute("onclick", "addAndShowPhoto(this)");
                                element_Tbody_Tr_Td.remove(inputElement);
                            }
                        }
                    }
                    element_Tbody_Tr_Td.remove(element_Tbody_Tr_Td.element("span"));
                } else {
//                    Element element_Tbody_Tr_Td_Span=element_Tbody_Tr_Td.addElement("span");
//                    element_Tbody_Tr_Td_Span.addAttribute("style","FONT-FAMILY: 宋体");
//                    element_Tbody_Tr_Td_Span.setText(text);
                }
            }

        }
        newTempHtml = tableDoc.asXML();
        System.out.println("实例html更新成功！");
        return newTempHtml;
    }

    /**
     * 将旧模板按照新的模板导入系统中。
     *
     * @param oldTempHtml
     * @param cellId_itemIdMap
     * @param id_DecriptionMap
     * @param id_PhotoMap      @return
     */
    private String oldTempHtmlToNew(String oldTempHtml, Map<String, String> cellId_itemIdMap, Map<String, String> id_DecriptionMap, Map<String, String> id_PhotoMap) throws DocumentException {
        String newTempHtml = "";
        Document tableDoc = null;
        try {
            oldTempHtml = oldTempHtml.replaceAll("&", "#");
            oldTempHtml = oldTempHtml.replaceAll("#nbsp;", "");
            oldTempHtml = oldTempHtml.replaceAll("Table", "table");
            oldTempHtml = "<html>" + oldTempHtml + "</html>";
            tableDoc = DocumentHelper.parseText(oldTempHtml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //取得根结点
        Element root_Html = tableDoc.getRootElement();
        //1、table操作
        Element element_Table = root_Html.element("table");
        Attribute attr_Table_Bor = element_Table.attribute("border");
        element_Table.remove(attr_Table_Bor);
        element_Table.addAttribute("class", "layui-table");
        //2.colgroup操作
        Element element_Colgroup = element_Table.addElement("colgroup");
        //3.tbody操作
        //3.1处理表头信息
        Element element_Thead = element_Table.element("thead");
        Element element_Thead_Tr = element_Thead.element("tr");
        element_Thead_Tr.addAttribute("class", "firstRow");
        //3.2更新tableDoc--合并表头和内容
        oldTempHtml = tableDoc.asXML();
        oldTempHtml = oldTempHtml.replaceAll("</thead>", "");
        oldTempHtml = oldTempHtml.replaceAll("<tbody>", "");
        oldTempHtml = oldTempHtml.replaceAll("thead", "tbody");
        oldTempHtml = oldTempHtml.replaceAll("</tbody>" + "<tbody>", "");
        tableDoc = DocumentHelper.parseText(oldTempHtml);
        //3.3更新tableDoc
        //3.3.1重新获取得根结点
        root_Html = tableDoc.getRootElement();
        element_Table = root_Html.element("table");
        Element element_Tbody = element_Table.element("tbody");
        List<Element> list_Tbody_Tr = element_Tbody.selectNodes(".//tr");

//        element_Tbody.sele
        for (Element element_Tbody_Tr : list_Tbody_Tr) {
            List<Element> list_Tbody_Tr_Td = element_Tbody_Tr.selectNodes(".//td");
            for (Element element_Tbody_Tr_Td : list_Tbody_Tr_Td) {
                String text = CommonTools.Obj2String(element_Tbody_Tr_Td.getText());
                if (text.equalsIgnoreCase("")) {//非描述信息
                    Boolean photoFlag = false;
                    if (element_Tbody_Tr_Td.selectNodes(".//input") != null && element_Tbody_Tr_Td.selectNodes(".//input").size() > 0) {
                        element_Tbody_Tr_Td.addAttribute("input", "1");
                        Element inputElement = element_Tbody_Tr_Td.element("input");
//                        inputElement.remove(inputElement.attribute("type"));
                        inputElement.remove(inputElement.attribute("readonly"));
                        String itemId = cellId_itemIdMap.get(inputElement.attributeValue("id"));
                        inputElement.addAttribute("title", CommonTools.Obj2String(id_DecriptionMap.get(itemId)));
                        inputElement.addAttribute("class", "dpInputText");
                        inputElement.addAttribute("id", itemId);
                        if (CommonTools.Obj2String(id_PhotoMap.get(itemId)).equalsIgnoreCase("TRUE")) {
                            photoFlag = Boolean.TRUE;
                        }
                    }
                    if (element_Tbody_Tr_Td.selectNodes(".//img") != null && element_Tbody_Tr_Td.selectNodes(".//img").size() > 0) {
                        element_Tbody_Tr_Td.addAttribute("checkbox", "1");
                        Element element_Tbody_Tr_Td_Img = element_Tbody_Tr_Td.element("img");
                        String itemId = cellId_itemIdMap.get(element_Tbody_Tr_Td_Img.attributeValue("id"));
                        Element checkboxElelment = element_Tbody_Tr_Td.addElement("input");
                        checkboxElelment.addAttribute("type", "checkbox");
                        checkboxElelment.addAttribute("disabled", "true");
                        checkboxElelment.addAttribute("class", "dpCheckbox");
                        checkboxElelment.addAttribute("id", itemId);
                        checkboxElelment.addAttribute("title", CommonTools.Obj2String(id_DecriptionMap.get(itemId)));
                        checkboxElelment.addAttribute("id", itemId);
                        if (CommonTools.Obj2String(id_PhotoMap.get(itemId)).equalsIgnoreCase("TRUE")) {
                            photoFlag = Boolean.TRUE;
                        }
                    }
                    if (photoFlag) {
                        element_Tbody_Tr_Td.addAttribute("photo", "1");
                        Element photoElelment = element_Tbody_Tr_Td.addElement("input");
                        photoElelment.addAttribute("class", "dpInputBtn");
                        photoElelment.addAttribute("type", "button");
                        photoElelment.addAttribute("disabled", "true");
                        photoElelment.addAttribute("value", "附件");
                        photoElelment.addAttribute("onclick", "addAndShowPhoto(this)");
                    }
                    element_Tbody_Tr_Td.remove(element_Tbody_Tr_Td.element("span"));
                    element_Tbody_Tr_Td.remove(element_Tbody_Tr_Td.element("img"));
                } else {
//                    Element element_Tbody_Tr_Td_Span=element_Tbody_Tr_Td.addElement("span");
//                    element_Tbody_Tr_Td_Span.addAttribute("style","FONT-FAMILY: 宋体");
//                    element_Tbody_Tr_Td_Span.setText(text);
                }
            }

        }
        newTempHtml = tableDoc.asXML();
        System.out.println("模板html更新成功！");
        return newTempHtml;
    }

    /**
     * 5.1.2导入工作项目
     *
     * @param taskIO
     * @param projectId
     * @param newPackageDataId
     * @param string
     * @param testTeamIdMap
     * @param taskIdMap
     * @throws Exception
     */
    private void saveTaskIOModelIntoDB(TaskIOModel taskIO, String projectId,
                                       String newPackageDataId, String parentDataId,
                                       Map<String, String> testTeamIdMap, Map<String, String> taskIdMap) throws Exception {
        Map<String, String> taskDataMap = new HashMap();

        taskDataMap.put("NAME_", taskIO.getName());
        taskDataMap.put("TYPE_", taskIO.getType());
        taskDataMap.put("STATUS_", taskIO.getStatus());
        taskDataMap.put("DESC_", taskIO.getDesc());
        taskDataMap.put("_", projectId);
        taskDataMap.put("_", newPackageDataId);
        taskDataMap.put("_", parentDataId);
        taskDataMap.put("SCHEDULER_SWITCH_", taskIO.getSchedulerSwitch());
        taskDataMap.put("FINISH_AUDIT_STATUS_", taskIO.getAuditStatus());

        taskDataMap.put("PLAN_START_TIME_", taskIO.getPlanStartTime());
        taskDataMap.put("PLAN_END_TIME_", taskIO.getPlanEndTime());
        taskDataMap.put("ACTUAL_START_TIME_", taskIO.getActualStartTime());
        taskDataMap.put("ACTUAL_END_TIME_", taskIO.getActualEndTime());

        taskDataMap.put("INDEX_", taskIO.getIndex());
        taskDataMap.put("TASK_UID_", taskIO.getTaskUid());

        String testTeamNewId = testTeamIdMap.get(taskIO.getTestTeam());
        taskDataMap.put("_", testTeamNewId);

        String newTaskDataId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("W_TASK", taskDataMap));
        taskIdMap.put(taskIO.getId(), newTaskDataId);
        List<TaskIOModel> subTaskList = taskIO.getSubTaskList();
        for (TaskIOModel subTask : subTaskList) {
            saveTaskIOModelIntoDB(subTask, projectId, newPackageDataId,
                    newTaskDataId, testTeamIdMap, taskIdMap);
        }
    }

    /**
     * 5.1.1导入工作队信息
     *
     * @param testTeamList
     * @param newPackageDataId
     * @param testTeamIdMap//用来存放 旧的工作队ID  与 新的工作队的ID建立一对一关系
     * @throws Exception
     */
    private void saveTestTeamIOModelIntoDB(List<TestTeamIOModel> testTeamList,
                                           String newPackageDataId, Map<String, String> testTeamIdMap) throws Exception {
        for (TestTeamIOModel testTeamIO : testTeamList) {
            //工作队名称
            String testTeamName = testTeamIO.getName();
            //工作队人员
            String testTeamUserNames = testTeamIO.getUsers();
            if (testTeamUserNames.equalsIgnoreCase("")) {
                throw new RuntimeException("导入失败，系统中未找到工作团队（责任团队）:" + testTeamName + "没有用户,请及时确认导出的压缩包是否损坏！");
            }
            String testTeamUserIds = "";
            String testTeamUserFullName = "";
            String[] testTeamUserNameArr = testTeamUserNames.split(",");
            for (String testTeamUserName : testTeamUserNameArr) {
                String userId = importFromOldSystemDao.getUserIdByUserName(testTeamUserName);
                //若找不到用户ID
                if (userId.equals("")) {
                    throw new RuntimeException("导入失败，系统中未找到工作团队（责任团队）账号(" + testTeamUserName + "),请及时联系管理员创建此用户！");
                }
                testTeamUserIds += userId + ",";
                ISysUser user = sysUserService.getById(Long.valueOf(userId));
                testTeamUserFullName = user.getFullname();
                testTeamUserFullName += testTeamUserFullName + ",";
            }
            testTeamUserIds = testTeamUserIds.substring(0, testTeamUserIds.length() - 1);
            Map<String, String> testTeamDataMap = new HashMap();
            testTeamDataMap.put("F_GZDMC", testTeamName);
            testTeamDataMap.put("F_CY", testTeamUserNames);
            testTeamDataMap.put("F_CYID", testTeamUserIds);
            testTeamDataMap.put("F_SSSJB", newPackageDataId);
            String newTestTeamId = CommonTools.Obj2String(importFromOldSystemDao.insertModelDataByTableName("W_WORKTEAM", testTeamDataMap));
            testTeamIdMap.put(testTeamIO.getId(), newTestTeamId);
        }
    }

    /**
     * 4 递归保存模板文件夹
     *
     * @param templateFolderIO
     * @param parentDataId     父节点
     * @param newProjectId     发次Id
     * @throws Exception
     */
    private void saveTemplateFolderIntoDB(TemplateFolderIOModel templateFolderIO, String newProjectId, String parentDataId) throws Exception {
        //发次代号 并非发次ID
        tempFileIdMap.clear();
        String oldProjectCode = templateFolderIO.getProjectId();
        List<Map<String, Object>> projectList = importFromOldSystemDao.getListByTableNameAndFilter("W_PROJECT", Arrays.asList(new SyncBaseFilter("ID", "=", newProjectId)));
        String newProjectCode = CommonTools.Obj2String(projectList.get(0).get("F_FCDH"));
        if (!oldProjectCode.equals(newProjectCode)) {
            throw new RuntimeException("导入失败，当前发次代号(" + newProjectCode + ")与导入的压缩包中的发次代号(" + oldProjectCode + ")不一致！");
        }
        Map<String, String> tFolderDataMap = new HashMap();
        String oldTempFileId = templateFolderIO.getId();
        tFolderDataMap.put("F_NAME", templateFolderIO.getName());
        tFolderDataMap.put("F_DESC", templateFolderIO.getDesc());
        tFolderDataMap.put("F_PROJECT_ID", CommonTools.Obj2String(newProjectId));
        tFolderDataMap.put("F_TEMP_FILE_ID", CommonTools.Obj2String(parentDataId));
        long folderId = importFromOldSystemDao.insertModelDataByTableName("W_TEMP_FILE", tFolderDataMap);
        tempFileIdMap.put(oldTempFileId, String.valueOf(folderId));
        List<TemplateFolderIOModel> subTFolderList = templateFolderIO.getSubTFolderList();
        for (TemplateFolderIOModel subTFolderIO : subTFolderList) {
            saveTemplateFolderIntoDB(subTFolderIO, newProjectId, CommonTools.Obj2String(folderId));
        }
    }

    /**
     * 3校验型号代号是否一致
     *
     * @param rootIOModel
     * @param fcId
     * @param oldProjectName
     * @return
     */
    private String validationProductCode(RootIOModel rootIOModel, String fcId, String oldProjectName) {
        //		oldProjectName=oldProjectName.substring(0, oldProjectName.length()-4);
        //获取型号ID
        List<Map<String, Object>> projectList = importFromOldSystemDao.getListByTableNameAndFilter("W_PROJECT", Arrays.asList(new SyncBaseFilter("ID", "=", fcId)));
        String productId = CommonTools.Obj2String(projectList.get(0).get("F_SSXH"));
        //发次名称
        String newProjectName = CommonTools.Obj2String(projectList.get(0).get("F_FCMC"));
        //型号代号
        List<Map<String, Object>> productList = importFromOldSystemDao.getListByTableNameAndFilter("W_PRODUCT", Arrays.asList(new SyncBaseFilter("ID", "=", productId)));
        String productCode = CommonTools.Obj2String(productList.get(0).get("F_XHDH"));

        String fileProductId = rootIOModel.getProductId();
        if (!productCode.equals(rootIOModel.getProductId())) {
            throw new RuntimeException("导入失败，当前型号代号(" + productCode + ")与导入的压缩包中的型号代号(" + fileProductId + ")不一致！");
        }
        if (!newProjectName.equals(oldProjectName)) {
            throw new RuntimeException("导入失败，当前发次名称(" + newProjectName + ")与导入的压缩包发次名称(" + oldProjectName + ")不一致,请重新选择压缩包！");
        }
        return productId;
    }

    /**
     * 1.1初始化解压临时文件夹
     *
     * @param name
     */
    public void initTempFileFolder(String name) {
        this.packageTempFolder = SysConfConstant.UploadFileFolder + File.separator + "temp" + File.separator +
                name + DateUtil.getCurrentDate("yyyyMMddHHmmss");
        FileOperator.createFolder(this.packageTempFolder);
    }

    /**
     * 2.1获取主文件，即发次名称.xml
     *
     * @param baseFloder
     * @param mainFileName
     * @return
     */
    private File findMainFile(String baseFloder, String mainFileName) {
        File floder = new File(baseFloder);
        File[] files = floder.listFiles();
        if (files.length == 0) {
            throw new RuntimeException("导入的压缩包中没有文件！");
        }
        File mainFile = null;
        boolean flag = false;
        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                if (name.indexOf(mainFileName.substring(0, mainFileName.length() - 4)) > -1) {
                    flag = true;
                    mainFile = file;
                    break;
                }
            }
        }
        if (!flag) {
            throw new RuntimeException("导入的压缩包中没有发现主文件（以‘压缩包名字.xml’结尾的文件）！");
        }
        return mainFile;
    }

}
