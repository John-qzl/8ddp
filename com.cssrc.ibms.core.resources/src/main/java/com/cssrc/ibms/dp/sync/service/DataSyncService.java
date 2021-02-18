package com.cssrc.ibms.dp.sync.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.resources.datapackage.dao.PackageDao;
import com.cssrc.ibms.core.resources.io.bean.TitleName;
import com.cssrc.ibms.core.resources.io.bean.ins.AcceptanceData;
import com.cssrc.ibms.core.resources.io.bean.ins.ConditionResult;
import com.cssrc.ibms.core.resources.io.bean.ins.ProductColumn;
import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.pack.SimplePackage;
import com.cssrc.ibms.core.resources.io.dao.IOConditionResultDao;
import com.cssrc.ibms.core.resources.io.dao.IOConventionalDao;
import com.cssrc.ibms.core.resources.io.dao.IOSignResultDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.ioOld2New.dao.ImportFromOldSystemDao;
import com.cssrc.ibms.core.resources.product.dao.ModuleDao;
import com.cssrc.ibms.core.resources.product.dao.ProductCategoryBatchDao;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.encrypt.PasswordUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.ZipCompressor;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.dp.form.dao.CheckResultCarryDao;
import com.cssrc.ibms.dp.form.dao.CheckResultDao;
import com.cssrc.ibms.dp.form.dao.CheckResultJgjgDao;
import com.cssrc.ibms.dp.form.dao.DataPackageInfoDao;
import com.cssrc.ibms.dp.form.dao.FormFolderDao;
import com.cssrc.ibms.dp.form.dao.ProductTypeDao;
import com.cssrc.ibms.dp.form.dao.TbInstantDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptancePlanDao;
import com.cssrc.ibms.dp.sync.bean.CellBean;
import com.cssrc.ibms.dp.sync.bean.ConditionBean;
import com.cssrc.ibms.dp.sync.bean.ConditionsBean;
import com.cssrc.ibms.dp.sync.bean.Conventional;
import com.cssrc.ibms.dp.sync.bean.HeaderBean;
import com.cssrc.ibms.dp.sync.bean.Mmc;
import com.cssrc.ibms.dp.sync.bean.MmcBean;
import com.cssrc.ibms.dp.sync.bean.OperationBean;
import com.cssrc.ibms.dp.sync.bean.Project;
import com.cssrc.ibms.dp.sync.bean.RowBean;
import com.cssrc.ibms.dp.sync.bean.RowsBean;
import com.cssrc.ibms.dp.sync.bean.SignBean;
import com.cssrc.ibms.dp.sync.bean.SignsBean;
import com.cssrc.ibms.dp.sync.bean.TaskList;
import com.cssrc.ibms.dp.sync.bean.TasksBean;
import com.cssrc.ibms.dp.sync.bean.UserBean;
import com.cssrc.ibms.dp.sync.dao.DataSyncDao;
import com.cssrc.ibms.dp.sync.model.SyncUser;
import com.cssrc.ibms.dp.sync.model.SyncUserXML;
import com.cssrc.ibms.dp.sync.util.BeanToXML;
import com.cssrc.ibms.dp.sync.util.SyncBaseFilter;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SysFileService;
import com.google.code.yanf4j.util.ResourcesUtils;

@Service
public class DataSyncService {
    @Resource
    SysFileService fileService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    DataSyncDao dataSyncDao;
    @Resource
    TbInstantDao tbInstantDao;
    @Resource
    CheckResultDao checkResultDao;
    @Resource
    CheckResultJgjgDao checkResultJgjgDao;
    @Resource
    CheckResultCarryDao checkResultCarryDao;
    @Resource
    DataPackageInfoDao dataPackageInfoDao;
    //	@Resource
//	DataSyncDao TableInstanDao;
    @Resource
    private FormFolderDao formFolderDao;
    @Resource
    private ProductTypeDao productTypeDao;
    @Resource
    private ImportFromOldSystemDao importFromOldSystemDao;
    @Resource
    SysFileService sysFileService;
    @Resource
    private PackageDao packageDao;
    @Resource
    IOTableInstanceDao iOTableInstanceDao;
    @Resource
    IOSignResultDao iOSignResultDao;
    @Resource
    IOConditionResultDao iOConditionResultDao;
    @Resource
    AcceptancePlanDao acceptancePlanDao;
    @Resource
    ProductCategoryBatchDao productCategoryBatchDao;
    @Resource
    ModuleDao modelDao;
    @Resource
    IOConventionalDao conventionalDao;
    @Resource
    RangeTestPlanDao rangeTestPlanDao;
    /**
     * 获取所有用户信息XML START
     */
    public String getUserXML() {
        // List<? extends ISysUser> userList = sysUserService.getAll();
        List<SyncUser> userList = dataSyncDao.getAllUser();
        UserBean userbean = new UserBean();
        Set userSet = new HashSet(0);
        for (int i = 0; i < userList.size(); i++) {
            if (Long.valueOf(userList.get(i).getId()) < 0) {
                continue;
            }
            List<Project> projectList = new ArrayList<Project>();
            SyncUserXML userXML = new SyncUserXML();
            userXML.setUserInfoBean(userList.get(i));
            projectList = dataSyncDao.getProjectByUserID(userList.get(i)
                    .getId()); // 根据用户ID查找到任务级别ID（重要）
            String projectIDStr = getprojectIDStr(projectList);
            String projectNameStr = getprojectNameStr(projectList);
            String testteamIds = "";
            List<Long> testteamList = dataSyncDao
                    .getTestTeamIDByUserId(CommonTools.Obj2String(userXML
                            .getUserId()));// 获取岗位ID
            for (Long inter : testteamList) {
                testteamIds += inter + ",";
            }
            String commanderIds = "";
            List<Long> commanderList = dataSyncDao
                    .getPackageIdByUserId(CommonTools.Obj2String(userXML
                            .getUserId()));// 获取所负责节点ID;
            for (Long inter : commanderList) {
                commanderIds += inter + ",";
            }
            userXML.setCommanderId(commanderIds);
            userXML.setTtidandname(testteamIds);
            userXML.setProjectId(projectIDStr);
            userXML.setProjectName(projectNameStr);
            userSet.add(userXML);
        }
        userbean.setUserset(userSet);
        return BeanToXML.userBeanToXML(userbean);
    }

    /**
     * @param
     * @return
     * @Description: 获取系统中所有在工作队的人员信息
     * @author qiaozhili
     * @date 2019/2/20 9:13
     */
    public String getGroupUserXml(String userName) {
        String userID = dataSyncDao.getUserIDByUsername(userName);
        List<SyncUser> userList = dataSyncDao.getGroupUsers(userID);
        //默认添加admin账户
        userList.add(dataSyncDao.getAdminUser().get(0));
        UserBean userbean = new UserBean();
        Set userSet = new HashSet(0);
        for (int i = 0; i < userList.size(); i++) {
            if (Long.valueOf(userList.get(i).getId()) < 0) {
                continue;
            }
            List<Project> projectList = new ArrayList<Project>();
            SyncUserXML userXML = new SyncUserXML();
            userXML.setUserInfoBean(userList.get(i));
            projectList = dataSyncDao.getProjectByUserID(userList.get(i)
                    .getId()); // 根据用户ID查找到任务级别ID（重要）
            String projectIDStr = getprojectIDStr(projectList);
            String projectNameStr = getprojectNameStr(projectList);
            String testteamIds = "";
            List<Long> testteamList = dataSyncDao
                    .getTestTeamIDByUserId(CommonTools.Obj2String(userXML
                            .getUserId()));// 获取岗位ID
            for (Long inter : testteamList) {
                testteamIds += inter + ",";
            }
            String commanderIds = "";
            List<Long> commanderList = dataSyncDao
                    .getPackageIdByUserId(CommonTools.Obj2String(userXML
                            .getUserId()));// 获取所负责节点ID;
            for (Long inter : commanderList) {
                commanderIds += inter + ",";
            }
            userXML.setCommanderId(commanderIds);
            userXML.setTtidandname(testteamIds);
            userXML.setProjectId(projectIDStr);
            userXML.setProjectName(projectNameStr);
            userSet.add(userXML);
        }
        userbean.setUserset(userSet);
        return BeanToXML.userBeanToXML(userbean);
    }

    public String getprojectIDStr(List<Project> projectList) {
        String ids = "";
        for (int i = 0; i < projectList.size(); i++) {
            ids = ids + projectList.get(i).getProjectId() + ",";
        }
        return ids;
    }

    public String getprojectNameStr(List<Project> projectList) {
        String names = "";
        for (int i = 0; i < projectList.size(); i++) {
            names = names + projectList.get(i).getProjectName() + ",";
        }
        return names;
    }

    /**
     * 获取所有用户信息XML END
     */

    /**
     * 在线验证用户登录 start
     */
    public String validationOnline(String username, String password) {
        SyncUser user = dataSyncDao.getUserInfoByUserName(username);
        if (CommonTools.Obj2String(user.getId()).equalsIgnoreCase("")) {
            return "userIsNotExist";
        } else {
            String localPassWord = user.getPassWord();
            boolean isTrue = PasswordUtil.validatePassword(localPassWord,
                    password);
            if (isTrue) {
                return "success";
            } else {
                return "error";
            }
        }
    }

    /**
     * 在线验证用户登录 end
     */
    /**
     * 返回用户要下载的表格清单 start
     */
    public String getTaskListByUserName2(String userName, String type) {
        StringBuffer allPathunfinish = new StringBuffer();
        StringBuffer allPathfinish = new StringBuffer();
        Map<String, Object> taskMap = new HashMap();
        // 1,得到该用户所有的验收策划（试验队）ID
        String userId = dataSyncDao.getUserIdByUserName(userName);
        String fieldType = "";
//        List<Long> testTeamIdList = dataSyncDao.getTestTeamIDByUserId(userId);// 获取岗位ID
        List<Long> YSCHIdListAll = dataSyncDao.getAllYSCHIDByUserId(userId);// 获取产品验收组表中人员对应验收策划ID  产品验收
        // 获取已审批的验收策划ID
        for (int k = 0; k < YSCHIdListAll.size(); k++) {
            //根据验收策划ID查询其对应状态，获取已审批的验收策划
            long YSCHId = YSCHIdListAll.get(k);
            fieldType = "1";
            String YSCHStatus = dataSyncDao.getYSCHIDById(String.valueOf(YSCHId));
            if (YSCHStatus.equals("审批通过")) {
                List<TaskList> taskList = dataSyncDao.getTaskIdByTestTeamID(YSCHId + "");
                if (taskList.size() > 0) {
                    for (int j = 0; j < taskList.size(); j++) {
                        String instanId = taskList.get(j).getInstanceId();
                        String tempId = taskList.get(j).getTempId();

                        // String instanId = "21";
                        if (null != instanId && !"".equals(instanId)) {
                            // 获取版本
                            List<Map<String, Object>> datapackageInfoList = dataSyncDao.getListByTableNameAndFilter("W_DATAPACKAGEINFO",
                                    Arrays.asList(new SyncBaseFilter("ID", "=", instanId)));
                            String instanVersion = CommonTools.Obj2String(datapackageInfoList.get(0).get("F_BB"));
                            //qzl 修改task name
                            String instanName =CommonTools.Obj2String(datapackageInfoList.get(0).get("F_SJMC"));
                            String pathId = CommonTools.Obj2String(datapackageInfoList.get(0).get("F_SSSJB"));
                            if (instanVersion.equals("")) {
                                instanVersion = "0.1";
                            }

                            if (taskList.get(j).getState().equals("待下载")) {
//                                String taskId = dataSyncDao.getTaskIdByTempId(instanId);
                                String path = getPathByInstanceId(instanId, "1");
                                allPathunfinish.append(instanId).append(",").append(tempId).append(",")
                                        .append(path).append(",").append(pathId).append(",").append(instanName)
                                        .append(",").append(instanVersion).append(",").append(fieldType).append("?");
                            }
                        } else {
                            System.out.println("该表格实例为null");
                            // break;
                        }
                    }
                }
            }
        }
        fieldType = "";
        List<Long> bcckList = dataSyncDao.getAllBCCHIDByUserId(userId);
        for (Long bcchId : bcckList) {
            List<Map<String, Object>> BCCHIdListAll = dataSyncDao.getBCCHByUserID(bcchId);// 靶场策划ID
            // 获取已审批的验收策划ID
            for (int k = 0; k < BCCHIdListAll.size(); k++) {
                //根据验收策划ID查询其对应状态，获取已审批的验收策划
                String BCCHId = BCCHIdListAll.get(k).get("ID").toString();
                String BCCHMC = BCCHIdListAll.get(k).get("F_CHBGBBH").toString();
                if (BCCHMC.contains("BCSY")) {
                    fieldType = "3";
                } else if (BCCHMC.contains("WQSJ")){
                    fieldType = "2";
                }
                List<TaskList> taskList = dataSyncDao.getTaskIdByTestTeamID(BCCHId + "");
                if (taskList.size() > 0) {
                    for (int j = 0; j < taskList.size(); j++) {
                        String instanId = taskList.get(j).getInstanceId();
                        String tempId = taskList.get(j).getTempId();

                        if (null != instanId && !"".equals(instanId)) {
                            // 获取版本
                            List<Map<String, Object>> datapackageInfoList = dataSyncDao.getListByTableNameAndFilter("W_DATAPACKAGEINFO",
                                    Arrays.asList(new SyncBaseFilter("ID", "=", instanId)));
                            String instanVersion = CommonTools.Obj2String(datapackageInfoList.get(0).get("F_BB"));
                            //qzl 修改task name
                            String instanName =CommonTools.Obj2String(datapackageInfoList.get(0).get("F_SJMC"));
                            String pathId = CommonTools.Obj2String(datapackageInfoList.get(0).get("F_SSSJB"));
                            if (instanVersion.equals("")) {
                                instanVersion = "0.1";
                            }

                            if (taskList.get(j).getState().equals("待下载")) {
//                                String taskId = dataSyncDao.getTaskIdByTempId(instanId);
//                            String path = getPathByInstanceId(instanId);
                                allPathunfinish.append(instanId).append(",").append(tempId).append(",")
                                        .append(BCCHMC).append(",").append(pathId).append(",").append(instanName)
                                        .append(",").append(instanVersion).append(",").append(fieldType).append("?");
                            }
                        } else {
                            System.out.println("该表格实例为null");
                            // break;
                        }
                    }
                }
            }
        }
        if (type.equals("unfinish")) {
            return allPathunfinish.toString();
        } else {
            return allPathfinish.toString();
        }
    }

    public String getPathByInstanceId(String taskId, String fieldTye) {
        StringBuffer path = new StringBuffer();
        String temppath = ""; // 存放path

        temppath = CommonTools.null2String(getPacPathByInstanId(taskId + "", fieldTye));
        path.append(temppath);
        return path.toString();
    }

    // 根据表格实例ID获得package的路径:例如：型号->产品类别->产品批次->产品
    public String getPacPathByInstanId(String taskId, String fieldTye) {
        String finalPath = "";
        StringBuffer path = new StringBuffer();
//        //1、根据taskId获取table_temp_Id
//        List<Map<String, Object>> TBInstantList = dataSyncDao.getListByTableNameAndFilter("W_TB_INSTANT", Arrays
//                .asList(new SyncBaseFilter("ID", "=", taskId)));
//        String TempId = CommonTools.Obj2String(TBInstantList.get(0).get("F_TABLE_TEMP_ID"));
        //2、根据table_temp_Id获取策划验收ID
        List<Map<String, Object>> datapackageInfoList = dataSyncDao.getListByTableNameAndFilter("W_DATAPACKAGEINFO", Arrays
                .asList(new SyncBaseFilter("ID", "=", taskId)));
        String YSCHId = CommonTools.Obj2String(datapackageInfoList.get(0).get("F_ACCEPTANCEPLANID"));
        String PCId = "";
        String PCName = "";
        String YSCHCode = "";
        //3、根据策划验收ID获取策划验收名称 及对应批次ID
        if (fieldTye.equals("1")) {
            List<Map<String, Object>> YSCHInfoList = dataSyncDao.getListByTableNameAndFilter("W_CPYSCHBGB", Arrays
                    .asList(new SyncBaseFilter("ID", "=", YSCHId)));
            PCId = CommonTools.Obj2String(YSCHInfoList.get(0).get("F_SSCPPC"));
            YSCHCode = CommonTools.Obj2String(YSCHInfoList.get(0).get("F_CHBGBBH"));
            PCName = CommonTools.Obj2String(YSCHInfoList.get(0).get("F_CPPC"));
        } else {
            PCId = YSCHId;
            PCName = CommonTools.Obj2String(datapackageInfoList.get(0).get("F_SYRWMC"));
        }
        path.insert(0, PCId + "->");
        path.insert(0, PCName + "->");

        return PCName;
    }

    public String getTaskPathByInstanId(String taskId) {
        String finalPath = "";
        StringBuffer path = new StringBuffer();
        // String rwId = TaskListDao.getTaskIDByInstanceID(ConfigInfo.SCHEMA_ID,
        // Integer.parseInt(taskId)); //任务ID
        String rwId = taskId;

        List<Map<String, Object>> taskList = dataSyncDao.getListByTableNameAndFilter("W_TASK",
                Arrays.asList(new SyncBaseFilter("ID", "=", taskId)));
        String rwPath = CommonTools.Obj2String(taskList.get(0).get("F_NAME"));// 非普通节点的节点名称
        while (true) {
            path.insert(0, rwPath + "->");
            if ("-1".equals(rwId)) {
                int firstLocation = path.indexOf("->");
                int endLocation = path.lastIndexOf("->");
                finalPath = path.substring(firstLocation + 2, endLocation);
                break;
            } else {
                taskList.clear();
                taskList = dataSyncDao.getListByTableNameAndFilter("W_TASK", Arrays.asList(new SyncBaseFilter("ID", "=", taskId)));
                rwId = CommonTools.Obj2String(taskList.get(0).get("F_PARENTID"));// 父节点名称赋值给pcId
                if (rwId.equals("-1")) {
                    rwPath = "";
                } else {
                    rwPath = CommonTools.Obj2String(taskList.get(0).get("F_NAME"));// 父节点名称
                }
            }
        }
        return finalPath;
    }

    /**
     * 返回用户要下载的表格清单 end
     */

    /**
     * 根据表格实例的ID，拼接出一个XML形式的表格内容 START
     *
     * @param tableId
     * @return
     */
    public String getTableByTableId(String tableId, String statue, String tempId, String fieldTye) {
        // 0,组装ConditionBean
        // 1,组装SignsBean
        // 2,组装RowsBean
        // 3,组装TableBean
        System.out.println("组装task开始时间:" + System.currentTimeMillis());
        Set<SignsBean> signsSet = CombinateSign(tableId,tempId);

        Set<ConditionsBean> conditionsSet = CombinateCondition(tableId, tempId);
        Set<RowsBean> rowsSet = CombinateRow(tempId);
        TasksBean tasksbean = CombinateTask(tableId, tempId, fieldTye);
        tasksbean.setSigns(signsSet);
        tasksbean.setConditions(conditionsSet);
        tasksbean.setRows(rowsSet);
        String taskXml = BeanToXML.taskBeanToXML(tasksbean);
        if (statue.equals("unfinish")) {
            if (CommonTools.isNullString(taskXml)) {
                return "[error]taskBean转xml出错";
            } else {
                StringBuffer sql = new StringBuffer();
                sql.append("UPDATE W_DATAPACKAGEINFO R SET R.F_ZXZT = '已下载' WHERE R.ID = ")
                        .append(tableId);
                dataSyncDao.exesql(sql.toString());
            }
        } else {
            if (CommonTools.isNullString(taskXml)) {
                return "";
            }
            StringBuffer sql = new StringBuffer();
            sql.append(
                    "UPDATE W_DATAPACKAGEINFO R SET R.F_ZXZT = '已完成' WHERE R.F_SSMB = ").append(tableId);
            dataSyncDao.exesql(sql.toString());
        }
        System.out.println("组装task结束:" + System.currentTimeMillis());
        System.out.println(taskXml);
        return taskXml;
    }

    /**
     * 1,组装SignsBean
     *
     * @param tableId //检查表实例ID
     * @return
     */
    public Set<SignsBean> CombinateSign(String tableId, String tempId) {
        System.out.println("开始组装signbean:" + (System.currentTimeMillis()));
        Set<SignsBean> signsSet = new HashSet(0);
        Set<SignBean> signSet = new HashSet<SignBean>(0);
        SignsBean signsbean = new SignsBean();
        List<SignBean> signList = dataSyncDao.getSignListByInstanceID(tempId);// 签署结果
        // list
        for (int i = 0; i < signList.size(); i++) {
            String signdefID = signList.get(i).getSignId();// 签署表定义的ID
            // 获取签署人员的名字
            List<Map<String, Object>> signefList = dataSyncDao.getListByTableNameAndFilter("W_SIGNDEF", Arrays.asList(new SyncBaseFilter("ID", "=", signdefID)));
            String name = CommonTools.Obj2String(signefList.get(0).get("F_NAME"));// 签署人的名称
            // 签署顺序
            String order = CommonTools.Obj2String(signefList.get(0).get("F_ORDER"));
            signList.get(i).setName(name);
            signList.get(i).setSignorder(order);
            signSet.add(signList.get(i));
        }
        signsbean.setSign(signSet);
        signsSet.add(signsbean);
        System.out.println("结束组装signbean:" + (System.currentTimeMillis()));
        return signsSet;
    }

    /**
     * 0.组装ConditionsBean
     *
     * @param tableId
     * @return
     */

    public Set<ConditionsBean> CombinateCondition(String tableId, String tempId) {
        System.out
                .println("开始组装ConditionsBean:" + (System.currentTimeMillis()));
        Set<ConditionsBean> conditionsSet = new HashSet(0);
        Set<ConditionBean> conditionSet = new HashSet<ConditionBean>(0);
        ConditionsBean conditionsbean = new ConditionsBean();
        List<ConditionBean> conditionList = dataSyncDao.getConditionListByTbinstanId(tempId);
        for (int i = 0; i < conditionList.size(); i++) {
            String conditionResID = conditionList.get(i).getConditionId();// 签署结果ID
//            String ref_conditionID = conditionList.get(i).getRef_conditionId();// 签署定义结果ID
            // 获取签署人员的名字
            List<Map<String, Object>> ref_conditionList = dataSyncDao.getListByTableNameAndFilter("W_CK_CONDITION", Arrays
                    .asList(new SyncBaseFilter("ID", "=", conditionResID)));
            String name = CommonTools.Obj2String(ref_conditionList.get(0).get("F_NAME"));// 条件名称
            String order = CommonTools.Obj2String(ref_conditionList.get(0).get("F_ORDER"));// 顺序
            conditionList.get(i).setConditionname(name);
            conditionList.get(i).setOrder(order);
            conditionSet.add(conditionList.get(i));
        }
        conditionsbean.setCondition(conditionSet);
        conditionsSet.add(conditionsbean);
        System.out.println("结束组装ConditionsBean:" + (System.currentTimeMillis()));
        return conditionsSet;
    }

    /**
     * 2,组装RowsBean 宋晨晨
     *
     * @param tempId ：表单实例的ID
     * @return
     */
    public Set<RowsBean> CombinateRow(String tempId) {
        // 1,根据表格实例号找到该实例模板的ID，再得到实例模板的行数
        // 2,根据行号和所属模板ID确定List<Cell>(模板)，每一行的CELL
        // 3,遍历每一行的CELL,如果该CELL有检查项，则遍历该CELL下所有的检查项模板（组装CELL）
        // 4,根据检查项模板ID和检查表格实例ID确定具体的某一个检查项实例（组装operation）
        // 5,根据2~4，可以（组装row）
        // 6,根据2~5，可以（组装rows）
//        TbInstant ins = tbInstantDao.getById(Long.valueOf(tempId));
        String html = dataSyncDao.getHtmlByTempId(tempId);
        Set rowsSet = new HashSet(0);
        List<Element> tables = tables(html);
        try {

            for (int j = 0; j < tables.size(); j++) {

                Element table = tables.get(j);
                // 表格实例所有行
                List<Element> trs = table.selectNodes(".//tr");
                // 表格实例的行数，默认去除第一行
                // int rowNum = trs.size()-1;
                // 根据表格实例ID得到表格模板ID
//                Long tabletempid = ins.getModule();
                RowsBean rowsbean = new RowsBean();
                rowsbean.setRowsid(j + 1 + "");
                if (trs == null || trs.size() < 2) { // 该表格不存在或者有误
                    System.err.println("表格内容错误，内部只有一个tr标签，表格实例ID为：" + tempId);
                } else {
                    Set rowSet = new HashSet(0);
                    List<HeaderBean> headerbeans = null;
                    for (int i = 0; i < trs.size(); i++) {
                        // 针对一行数据
                        Element tr = trs.get(i);
                        if (i == 0) {
                            headerbeans = getHeaderBeans(tr);
                            continue;
                        }
                        RowBean rowbean = new RowBean();
                        rowbean.setId(i + "");

                        Set cellSet = new HashSet(0);
                        // 得到该表格实例的列数
                        List<Element> tds = tr.selectNodes(".//td");
                        // String line = "";
                        // 获取每行有多少个CELL表，如果CELL表个数大于line个数，则说明有多个operation项
                        for (int m = 0; m < tds.size(); m++) {
                            // 一行中的一列---一个单元格
                            Element td = tds.get(m);
                            CellBean cellBean = getCellBeanByTd(td, m + 1, headerbeans.get(m));
                            cellSet.add(cellBean);
                        }
                        rowbean.setCell(cellSet);
                        rowSet.add(rowbean);
                    }
                    rowsbean.setRow(rowSet);
                    rowsSet.add(rowsbean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowsSet;
    }

    private CellBean getCellBeanByTd(Element td, int i, HeaderBean headerBean) {
        CellBean cell = new CellBean();
        // cell.setCellId(UniqueIdUtil.getGuid());
        cell.setOrder(i + "");
        cell.setColumn(headerBean.getHeaderName());
        cell.setColumnId(headerBean.getHeaderId());
        //qzl 标记判读标签
        if (td.attribute("class") != null) {
            cell.setMarkup(td.attribute("class").getValue());
        }
        if (td.attribute("input") != null || td.attribute("checkbox") != null) {
            cell.setType("TRUE");
            List<Element> inputs = td.selectNodes(".//input");
            Set operationSet = new HashSet(0);
            Element input = null;
            Element checkbox = null;
            // boolean photo=false;
            for (Element in : inputs) {
                if (in.asXML().contains("button")) {
                    // photo=true;
                    continue;
                }
                if (in.asXML().contains("checkbox")) {
                    checkbox = in;
                } else {
                    input = in;
                }
            }
            String cellList = "";
            if (input != null && checkbox != null) {
                // 处理input
                OperationBean opt_input = getOperationBean(td, input, true);
                cell.setCellId(opt_input.getResultid());
                cellList = cell.getCellId();
                opt_input.setCellid(cell.getCellId());
                opt_input.setType("2");
                opt_input.setRealcellid(cell.getCellId());
                operationSet.add(opt_input);
                // 处理checkbox
                OperationBean opt_checkbox = getOperationBean(td, checkbox, false);
                opt_checkbox.setCellid(cell.getCellId());
                opt_checkbox.setType("1");
                opt_checkbox.setRealcellid(cell.getCellId());
                cellList = cellList + "," + opt_checkbox.getRealcellid() + ",";
                operationSet.add(opt_checkbox);
            } else if (input != null) {
                // 处理input
                OperationBean opt_input = getOperationBean(td, input, true);
                cell.setCellId(opt_input.getResultid());
                opt_input.setCellid(cell.getCellId());
                opt_input.setType("2");
                opt_input.setRealcellid(cell.getCellId());
                operationSet.add(opt_input);
            } else if (checkbox != null) {
                // 处理checkbox
                OperationBean opt_checkbox = getOperationBean(td, checkbox, true);
                cell.setCellId(opt_checkbox.getResultid());
                opt_checkbox.setCellid(cell.getCellId());
                opt_checkbox.setRealcellid(cell.getCellId());
                opt_checkbox.setType("1");
                operationSet.add(opt_checkbox);
            }
            cell.setOperation(operationSet);
            if (StringUtil.isNotEmpty(cellList)) {
                cell.setCellList(cellList);
            }

        } else {
            cell.setType("FALSE");
            cell.setTextValue(td.getStringValue().trim());
        }

        // 列合并数
        Attribute colspan = td.attribute("colspan");
        int colSize = colspan == null ? 1 : Integer.valueOf(colspan.getValue());
        // 行合并数
        Attribute rowspan = td.attribute("rowspan");
        int rowSize = rowspan == null ? 1 : Integer.valueOf(rowspan.getValue());
        cell.setColspan(colSize);
        cell.setRowspan(rowSize);

        return cell;
    }

    /**
     * 判断Td中是否含有此项属性标记
     *
     * @param td
     * @param name
     * @return
     */
    private String checkTdSign(Element td, String name) {
        String val = td.attributeValue(name);
        if (StringUtil.isNotEmpty(val)) {
            return "TRUE";
        }
        return "FALSE";
    }

    /**
     * 获取input/chcekbox的bean
     *
     * @param td    单元格
     * @param input 内部的一个输入项 inout/checkbox
     * @param
     * @return
     */
    private OperationBean getOperationBean(Element td, Element input, boolean opt) {
//        Map typeMap = productTypeDao.getProductType(Long.valueOf(tableId));//获取 运载、空间、结构机构的表单类型
        OperationBean opBean = new OperationBean();
        opBean.setResultid(input.attributeValue("id"));
        String ildd = checkTdSign(td, "ildd");
        String iildd = checkTdSign(td, "iildd");
        String tighten = checkTdSign(td, "tighten");
        String err = checkTdSign(td, "err");
        String lastaction = checkTdSign(td, "lastaction");
        String ismedia = checkTdSign(td, "photo");
        String value = "";
//        if ("空间".equals(typeMap.get("TYPE"))) {
//            CheckResult result = checkResultDao.getById(Long.valueOf(opBean.getResultid()));
//            if (result.getSketchmap() != null) {
//                opBean.setBitmapid(result.getSketchmap());
//            }
//            value = result.getValue();
//        } else if ("运载".equals(typeMap.get("TYPE"))) {
//            CheckResultCarry result = checkResultCarryDao.getById(Long.valueOf(opBean.getResultid()));
//            if (result.getSketchmap() != null) {
//                opBean.setBitmapid(result.getSketchmap());
//            }
//
//            value = result.getValue();
//        } else {
//            CheckResultJgjg result = checkResultJgjgDao.getById(Long.valueOf(opBean.getResultid()));
//            if (result.getSketchmap() != null) {
//                opBean.setBitmapid(result.getSketchmap());
//            }
//            value = result.getValue();
//        }
        opBean.setIldd(ildd);
        opBean.setIildd(iildd);
        opBean.setTighten(tighten);
        opBean.setErr(err);
        opBean.setLastaction(lastaction);
        opBean.setIsmedia(ismedia);
        if (StringUtil.isNotEmpty(value)) {
            if (value.equals("true")) {
                opBean.setValue("is");
            } else {
                opBean.setValue(value);
            }
        }
        if (opt) {
            opBean.setOperationtype(getOperationType(ildd, iildd, ismedia) + "");
        } else {
            opBean.setOperationtype("0");
        }
        return opBean;
    }

    private List<HeaderBean> getHeaderBeans(Element tr) {
        List<Element> tds = tr.selectNodes(".//td");
        List<HeaderBean> headers = new ArrayList<HeaderBean>();
        for (int i = 1; i <= tds.size(); i++) {
            HeaderBean headbean = new HeaderBean();
            headbean.setHeaderId(UniqueIdUtil.getGuid());
            headbean.setHeaderName(tds.get(i - 1).getStringValue().trim());
            headbean.setHeaderOrder(i + "");
            headers.add(headbean);
        }
        return headers;
    }

    // 获取OperationType的值
    public int getOperationType(String ildd, String iildd, String ismedia) {
        int operationtype = 0;
        if (ildd.equals("TRUE")) {
            operationtype += 2;
        }
        if (iildd.equals("TRUE")) {
            operationtype += 64;
        }
        if (ismedia.equals("TRUE")) {
            operationtype += 128;
        }
        if (!ildd.equals("TRUE") && iildd.equals("TRUE")
                && ismedia.equals("TRUE")) {
            return 1;
        } else {
            return operationtype;
        }
    }

    /**
     * 3,组装TableBean instanceId就是表格实例ID
     */
    public TasksBean CombinateTask(String taskId, String tempId, String fieldTye) {
        TasksBean taskInstan = dataSyncDao.getTaskByInstanId(taskId);
        String path = getPathByInstanceId(taskId, fieldTye);

        //1、根据table_temp_Id获取策划验收ID
        List<Map<String, Object>> datapackageInfoList1 = dataSyncDao.getListByTableNameAndFilter("W_DATAPACKAGEINFO", Arrays
                .asList(new SyncBaseFilter("ID", "=", taskId)));
        String YSCHId = CommonTools.Obj2String(datapackageInfoList1.get(0).get("F_ACCEPTANCEPLANID"));
        //2、根据策划验收ID获取策划验收名称 及对应批次ID
        String PCId = "";
        String YSCHCode = "";
        String XHID = "";
        String sscplb = "";//所属产品类别
        String CPDH = "";//产品代号

        // 数据包详细信息表
        List<Map<String, Object>> datapackageInfoList = dataSyncDao.getListByTableNameAndFilter("W_DATAPACKAGEINFO", Arrays
                .asList(new SyncBaseFilter("ID", "=", taskId)));
        String CPLBId = CommonTools.Obj2String(datapackageInfoList.get(0).get("F_PRODUCTBATCHID"));
        //qzl 修改task name
        String tastName = CommonTools.Obj2String(datapackageInfoList.get(0).get("F_SJMC"));
        String version = CommonTools.Obj2String(datapackageInfoList.get(0).get("F_BB"));// 版本

        if (fieldTye.equals("1")) {
            List<Map<String, Object>> YSCHInfoList = dataSyncDao.getListByTableNameAndFilter("W_CPYSCHBGB", Arrays
                    .asList(new SyncBaseFilter("ID", "=", YSCHId)));
            PCId = CommonTools.Obj2String(YSCHInfoList.get(0).get("F_SSCPPC"));
            YSCHCode = CommonTools.Obj2String(YSCHInfoList.get(0).get("F_CHBGBBH"));
            XHID = CommonTools.Obj2String(YSCHInfoList.get(0).get("F_SSXHID"));
            // 产品批次
            List<Map<String, Object>> cppcList = dataSyncDao.getListByTableNameAndFilter("W_CPLBPCB",
                    Arrays.asList(new SyncBaseFilter("ID", "=", CPLBId)));
            String PCH = CommonTools.Obj2String(cppcList.get(0).get("F_PCH"));// 批次号
            sscplb = CommonTools.Obj2String(cppcList.get(0).get("F_SSCPLB"));//所属产品类别
            CPDH = CommonTools.Obj2String(cppcList.get(0).get("F_CPDH"));//产品代号
        } else {
            List<Map<String, Object>> BCCHInfoList = dataSyncDao.getListByTableNameAndFilter("W_BCRWCHBGB", Arrays
                    .asList(new SyncBaseFilter("ID", "=", YSCHId)));
            YSCHCode = CommonTools.Obj2String(BCCHInfoList.get(0).get("F_SYRWMC"));
            XHID = CommonTools.Obj2String(BCCHInfoList.get(0).get("F_XHID"));
        }

        //3、根据XHID获取型号名称
        List<Map<String, Object>> XHList = dataSyncDao.getListByTableNameAndFilter("W_XHJBSXB", Arrays
                .asList(new SyncBaseFilter("ID", "=", XHID)));
        String XHDH = CommonTools.Obj2String(XHList.get(0).get("F_XHDH"));  //型号名称

        //4、查询表单类型
        List<Map<String, Object>> TempList = dataSyncDao.getListByTableNameAndFilter("W_TABLE_TEMP", Arrays
                .asList(new SyncBaseFilter("ID", "=", tempId)));
        String tempType = CommonTools.Obj2String(TempList.get(0).get("F_ZL"));  //表单类型

        if (version.equals(""))
            version = "0.1";
        taskInstan.setVersion(version);
        taskInstan.setPath(path);
        taskInstan.setPathId(PCId);
        taskInstan.setRwid(sscplb);
        taskInstan.setPclbid(sscplb);
        taskInstan.setRwname(CPDH);
        taskInstan.setName(tastName);
        taskInstan.setChbh(YSCHCode);
        taskInstan.setChId(YSCHId);
        taskInstan.setXhId(XHID);
        taskInstan.setXhmc(XHDH);
        taskInstan.setTempId(tempId);
        taskInstan.setTableinstanceId(taskId);
        taskInstan.setTempType(tempType);
        return taskInstan;
    }

    StringBuffer taskpath = new StringBuffer();

    /**
     * 获取人员-任务-表格实例关系XML START
     */
    public String getUserRwTableRelation(String userName) {
        StringBuffer relationStr = new StringBuffer();
        String userId = dataSyncDao.getUserIdByUserName(userName);
        //1、查询产品验收策划信息
        List<Long> CHIdListAll = dataSyncDao.getAllYSCHIDByUserId(userId);// 根据用户ID获取所属策划ID
        for (Long CHID : CHIdListAll) {
            List<Map<String, Object>> taskList = dataSyncDao.getTaskByCHID(String.valueOf(CHID));
            if (taskList.size() > 0) {
                List<Map<String, Object>> XHIDList = dataSyncDao.getXHIDByCHID(String.valueOf(CHID));
                if (XHIDList.size() > 0) {
                    String XHMC = CommonTools.Obj2String(XHIDList.get(0).get("F_SSXH"));  //型号名称
                    String XHID = CommonTools.Obj2String(XHIDList.get(0).get("F_SSXHID"));  //型号ID
                    String XHDH = dataSyncDao.getXHDHByXHID(Long.valueOf(XHID));  ////型号代号
                    if (!relationStr.toString().contains(String.valueOf(CHID))) {
                        relationStr.append(XHID).append(",")
                                .append(XHDH).append(",")
                                .append(CHID).append(",")
                                .append("")
                                .append("?");
                    }
                }
            }
        }
        return relationStr.toString();
    }
    /**
     * @Description: 获取靶场策划信息
     * @author qiaozhili
     * @date 2020/9/21 16:36
     * @param
     * @return
     */
    public String getBCUserRwTableRelation(String userName) {
        StringBuffer relationStr = new StringBuffer();
        String userId = dataSyncDao.getUserIdByUserName(userName);
        List<Long> bcckList = dataSyncDao.getAllBCCHIDByUserId(userId);
        for (Long bcchId : bcckList) {
            List<Map<String, Object>> taskList = dataSyncDao.getTaskByCHID(String.valueOf(bcchId));
            if (taskList.size() > 0) {
                List<Map<String, Object>> BCCHMapList = dataSyncDao.getBCCHByUserID(bcchId);// 靶场策划ID
                if (BCCHMapList.size() > 0) {
                    for (int i = 0; i < BCCHMapList.size(); i++) {
                        String CHID = BCCHMapList.get(i).get("ID") + "";   //策划ID
                        String CHName = BCCHMapList.get(i).get("F_CHBGBBH") + "";   //策划名称
                        String SSXH = BCCHMapList.get(i).get("F_XHID") + "";   //所属型号ID
                        String SSDH = BCCHMapList.get(i).get("F_XHDH") + "";   //所属型号ID
                        if (!relationStr.toString().contains(CHID)) {
                            relationStr.append(CHID).append(",")
                                    .append(CHName).append(",")
                                    .append(SSXH).append(",")
                                    .append(SSDH).append(",")
                                    .append("")
                                    .append("?");
                        }
                    }
                }
            }
        }
        return relationStr.toString();
    }

    // public Set<UserXML> CombinateUser(){
    // Set<UserXML> userSet = new HashSet(0);
    // // Set<Project> projectSet = CombinateProject();
    // List<User> userList = UserDao.getAllUser();
    // for(int i=0; i<userList.size(); i++){
    // UserXML user = new UserXML();
    // user.setUserId(userList.get(i).getId());
    // user.setUserName(userList.get(i).getUserName());
    // Set<Project> projectSet = CombinateProject(user);
    // user.setRw(projectSet);
    // userSet.add(user);
    // }
    // return userSet;
    // }
    //
    // public Set<Project> CombinateProject(UserXML user){
    // Set<Project> proSet = new HashSet(0);
    // List<Project> projectList =
    // ProjectDao.getProjectByUserID(user.getUserId());
    // for(int i=0; i<projectList.size(); i++){
    // Project pro = projectList.get(i);
    // Set<TableInstance> tbInstan = CombinateTable(pro.getProjectId());
    // pro.setInstanSet(tbInstan);
    // proSet.add(pro);
    // }
    // return proSet;
    // }
    //
    // public Set<TableInstance> CombinateTable(int proId){
    // Set<TableInstance> tbInstan = new HashSet(0);
    // List<TableInstance> tableInstan = TableInstanDao.getInstanByProId(proId,
    // ConfigInfo.TBINSTAN_MODELID, ConfigInfo.SCHEMA_ID);
    // for(int i=0; i<tableInstan.size(); i++){
    // TableInstance tb = new TableInstance();
    // tb.setTableinstanceId(tableInstan.get(i).getTableinstanceId());
    // tb.setName(tableInstan.get(i).getName());
    // tbInstan.add(tb);
    // }
    // return tbInstan;
    // }
    /**
     * 获取人员-任务-表格实例关系XML END
     */
    /**
     * 根据表格实例ID获得该表格实例的HTML
     */
    public String getHtmlByInstanId(String instanId, String tempId) {
        // 获取实例
        List<Map<String, Object>> instanList = dataSyncDao
                .getListByTableNameAndFilter("W_TABLE_TEMP",
                        Arrays.asList(new SyncBaseFilter("ID", "=", tempId)));
        String htmlContent = CommonTools.Obj2String(instanList.get(0).get(
                "F_CONTENTS"));
        return htmlContent;
    }

    /**
     * 根据表格实例ID获得数据包详情的状态
     */
    public String getDataPackageStatus(Long tableInstanId) {
        return dataSyncDao.getDataPackageStatus(tableInstanId);
    }

    /**
     * 上传已经完成的检查表
     *
     * @param taskXml
     * @param tableInstanId
     * @return
     */
    public String uploadTask(String taskXml, String taskHtml, String tableInstanId, String tempId, String planId, String broTaskId,String userId) {
        Map<String, Object> slparam = new HashMap<String, Object>();
        slparam.put("id", tableInstanId);
        String html = taskHtml;

        int index1 = html.indexOf("<table");
        int index2 = html.lastIndexOf("</table>");
        html = html.substring(index1, index2 + 8);
        // 解析表格实例入库
        Map<String, Object> message = commitTaskXml(taskXml, tableInstanId, html, tempId,userId);
        String title = message.get("title").toString();
        List<Map<String, Object>> signreses = message.get("signreses") == null ? null : (List<Map<String, Object>>) message.get("signreses");
        analysisHtml(message.get("html").toString(), planId, title, signreses, tableInstanId);
        Map<String, Object> param = new HashMap<String, Object>();
        if (message.get("success").equals("true")) {
            //更新对应W_DATAPACKAGEINFO数据状态  已完成
            param.put("ZXZT", "已完成");
            param.put("taskId", broTaskId);
            dataSyncDao.upDataDatapackage(param);
            return "true";
        } else if (message.get("success").equals("false")) {
            return "false";
        }
        // }
        return "true";
    }
    /**
     * 定制，解析html取出产品入库
     * @param
     * @param tableInstanId
     * @param html
     * @return
     */
    public void analysisHtml(String html,String planId,String tableTitle,List<Map<String, Object>> signreses,String tableInstanId){
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        JSONObject reportString=new JSONObject();
        List<Conventional> conventionalList=null;
        if(doc.select("table[class=layui-table]").size()!=0) {
            Elements rows = doc.select("table[class=layui-table]").get(0).select("tr");
            List<ProductColumn> productList=new ArrayList<>();
            List<TitleName> titleNameList=new ArrayList<>();
            JSONObject rangeTestMainInfo=new JSONObject();
            for (int i = 0; i < rows.size(); i++) {
                org.jsoup.nodes.Element row = rows.get(i);
                String acceptanceProject="";
                String indicators="";
                String unit="";
                String requirements="";
                if(i==0) {
                    if(row.select("td").size()!=0) {
                        for(int j=0;j<row.select("td").size();j++) {
                            String title=row.select("td").get(j).text();
                            TitleName titleName=new TitleName();
                            titleName.setTitleName(title);
                            if(title.indexOf("实测值")>=0) {
                                title=rows.get(i+1).select("td").get(j).select("input").val();
                                String xh=title;
                                if(xh!=null&&!xh.equals("")) {
                                    String F_JL=rows.get(rows.size()-1).select("td").get(j).select("input").val();
                                    Long id=UniqueIdUtil.genId();
                                    titleName.setProductId(String.valueOf(id));
                                    titleName.setProductName(title);
                                    productList.add(iOTableInstanceDao.insertcp(id,planId,title,F_JL,tableInstanId,j));
                                }
                            }
                            else if(title.indexOf("检查内容")>=0) {
                                conventionalList=new ArrayList<>();
                            }

                            titleName.setIndex(j);
                            titleNameList.add(titleName);
                        }
                    }
                }
                else if(i!=1&&productList.size()>0) {
                    for (TitleName titleName : titleNameList) {
                        String value=row.select("td").get(titleName.getIndex()).attr("value").equals("")
                                ?row.select("td").get(titleName.getIndex()).text():row.select("td").get(titleName.getIndex()).attr("value");
                        if(titleName.getTitleName().indexOf("验收项目")>=0) {
                            acceptanceProject=value;
                        }
                        else if(titleName.getTitleName().indexOf("计量单位")>=0) {
                            unit=value;
                        }
                        else if(titleName.getTitleName().indexOf("要求值")>=0) {
                            indicators=value;
                        }
                        else if(titleName.getTitleName().indexOf("操作要求")>=0) {
                            requirements=value;
                        }
                        else if(titleName.getTitleName().indexOf("实测值")>=0) {
                            AcceptanceData acceptanceData=new AcceptanceData();
                            acceptanceData.setProductId(titleName.getProductId());
                            acceptanceData.setAcceptanceProject(acceptanceProject);
                            acceptanceData.setIsCheck(row.select("td").get(titleName.getIndex()).select("input").attr("isFull"));
                            acceptanceData.setProductName(titleName.getProductName());
                            value=row.select("td").get(titleName.getIndex()).select("input").attr("value");
                            acceptanceData.setRealValue(value);
                            acceptanceData.setIns_Id(tableInstanId);
                            acceptanceData.setRequirements(requirements);
                            acceptanceData.setRequiredValue(indicators);
                            acceptanceData.setUnit(unit);
                            if(!acceptanceData.getAcceptanceProject().equals("")&&acceptanceData!=null) {
                                iOSignResultDao.insertAcceptanceData(acceptanceData);
                            }

                        }

                    }
                }else if(i!=0&&conventionalList!=null) {
                    Conventional conventional=new Conventional();
                    for (TitleName titleName : titleNameList) {
                        String value=row.select("td").get(titleName.getIndex()).attr("value").equals("")
                                ?row.select("td").get(titleName.getIndex()).text():row.select("td").get(titleName.getIndex()).attr("value");
                        if(titleName.getTitleName().indexOf("序号")>=0) {
                            conventional.setXh(value);
                        }
                        else if(titleName.getTitleName().indexOf("内容")>=0) {
                            conventional.setJcnr(value);;
                        }
                        else if(titleName.getTitleName().indexOf("要求")>=0) {
                            conventional.setJcyq(value);
                        }
                        else if(titleName.getTitleName().indexOf("检查结果")>=0) {
                            value=row.select("td").get(titleName.getIndex()).select("input").attr("value");
                            conventional.setJcjg(value);
                        }
                        else if(titleName.getTitleName().indexOf("结论")>=0) {
                            value=row.select("td").get(titleName.getIndex()).select("input").attr("value");
                            conventional.setJl(value);
                        }
                        else if(titleName.getTitleName().indexOf("备注")>=0) {
                            value=row.select("td").get(titleName.getIndex()).select("input").attr("value");
                            conventional.setBz(value);
                        }
                    }
                    conventional.setPlanId(planId);
                    conventional.setSlId(tableInstanId);
                    conventional.setId(String.valueOf(UniqueIdUtil.genId()));
                    conventionalDao.insert(conventional);
                }
                else if(i!=0&&row.select("input").size()!=0) {

                    for(int j=0;j<row.select("input").size();j++) {
                        String title=row.select("input").get(j).attr("title");
                        if(tableTitle.indexOf("验收报告")>=0) {
                            if(title.indexOf("问题")>=0) {
                                String problem=row.select("input").get(j).attr("value");
                                if(!problem.equals("")&&!problem.isEmpty()){
                                    reportString.put("problem", problem);
                                }
                            }
                            else if(title.indexOf("验收结论")>=0){
                                String opinion=row.select("input").get(j).attr("value");
                                if(!opinion.equals("")&&!opinion.isEmpty()){
                                    reportString.put("opinion", opinion);
                                }
                            }
                            else if(title.indexOf("承制方意见")>=0){
                                String sellerOpinion=row.select("input").get(j).attr("value");
                                if(!sellerOpinion.equals("")&&!sellerOpinion.isEmpty()){
                                    reportString.put("sellerOpinion", sellerOpinion);
                                }
                            }
                            else if(title.indexOf("说明")>=0){
                                String instructions=row.select("input").get(j).attr("value");
                                if(!instructions.equals("")&&!instructions.isEmpty()){
                                    reportString.put("instructions", instructions);
                                }
                            }
                            else if(title.indexOf("编号")>=0){
                                String serialNumber=row.select("input").get(j).attr("value");
                                if(!serialNumber.equals("")&&!serialNumber.isEmpty()){
                                    reportString.put("serialNumber", serialNumber);
                                }
                            }

                        }else if (tableTitle.indexOf("问题表")>=0){
                            //如果是靶场或者所检的试验问题表
                            if(title.indexOf("发现的主要问题")!=-1) {
                                String problem=row.select("input").get(j).attr("value");
                                if(!problem.equals("")&&!problem.isEmpty()){
                                    if ("/".equals(problem)){
                                        //如果是空
                                        rangeTestMainInfo.put("problem", "无");
                                    }else {
                                        rangeTestMainInfo.put("problem", problem);
                                    }

                                }
                                /*else {
                                    //补上缺省值
                                    rangeTestMainInfo.put("problem", "无");
                                }*/
                            }
                            if(title.indexOf("其他说明")!=-1) {
                                String instructions=row.select("input").get(j).attr("value");
                                if(!instructions.equals("")&&!instructions.isEmpty()){
                                    if ("/".equals(instructions)){
                                        //如果是空
                                        rangeTestMainInfo.put("problem", "无");
                                    }else {
                                        rangeTestMainInfo.put("instructions", instructions);
                                    }

                                }
                                /*else {
                                    //补上缺省值
                                    rangeTestMainInfo.put("instructions", "无");
                                }*/
                            }

                        }
                    }

                }

            }
            rangeTestPlanDao.updateBackData(planId, rangeTestMainInfo.toString());
        }

        if(reportString!=null&&tableTitle.indexOf("验收报告")>=0) {
            reportString.put("signreses", signreses);
            //如果要自动给签章的话,可以考虑在这里给,但是要考虑到执行这个操作的时候,图片还没同步过来的情况
            iOTableInstanceDao.insertReport(planId, reportString);
        }
    }

    /**
     * 将返回xml解析入库
     * @param xmlStr
     * @param tableInstanId
     * @param html
     * @return
     */
    public Map<String, Object> commitTaskXml(String xmlStr, String tableInstanId, String html, String tempId,String userId) {
        Map<String, Object> message = new HashMap<String, Object>();
        try {
            int index1 = xmlStr.indexOf("<task");
            int index2 = xmlStr.lastIndexOf("</task>");
            xmlStr = xmlStr.substring(index1, index2 + 7);
            xmlStr = "<html>" + xmlStr + "</html>";
            //1.获取所有检查条件结果、签署结果、和检查项结果，暂存在Map中。
            Map<String, List<Map<String, Object>>> allresult = resolveXml(xmlStr);
            String title=getTitle(xmlStr);
            String padCode=getPadVersion(xmlStr);
            //2.更新 检查条件结果、签署结果、和检查项结果    --------------Start----------
            if (allresult.size() > 0) {
                List<Map<String, Object>> conditionreses = allresult
                        .get("conditionreses");
                List<Map<String, Object>> signreses = allresult
                        .get("signreses");
                List<Map<String, Object>> operationreses = allresult
                        .get("operationreses");
                List<Map<String, Object>> check = allresult
                        .get("check");

                message.put("signreses", signreses);
                for (int i = 0; i < conditionreses.size(); i++) {
                    String conditionId = conditionreses.get(i).get("conditionid").toString();
                    Map<String, Object> param = conditionreses.get(i);

                    //判断检查条件是否存在，pad复制的表单的检查条件需要插入，而不是updata
//                    if (formFolderDao.queryCondition(conditionId).size() > 0) {
//                        formFolderDao.updatecondies(param);
//                    } else {
//                    }
                    insertCondies(tableInstanId, param, tempId);
                }
                for (int i = 0; i < signreses.size(); i++) {
                    String signresesId = signreses.get(i).get("signid").toString();
                    Map<String, Object> param = signreses.get(i);

                    //判断签署条件是否存在，pad复制的表单的签署信息需要插入，而不是updata
//                    if (formFolderDao.querySign(signresesId).size() > 0) {
//                        formFolderDao.updatesignres(param);
//                    } else {
//                    }
                    insertSignResult(tableInstanId, param, tempId,i);
                }
                //更新检查项结果表  需要判断。
                for (int i = 0; i < operationreses.size(); i++) {
                    Map<String, Object> param = operationreses.get(i);
                    formFolderDao.updateckres(param, tableInstanId);
                }
                //2.更新 检查条件结果、签署结果、和检查项结果     --------------end----------
                //3.更新Tb_Instant 中的content内容
                Map<String, Object> newhtml = updateHtml(html, operationreses,check);
                if (!newhtml.get("html").equals("")) {
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("ID", tableInstanId);
                    param.put("F_CONTENT", newhtml.get("html"));
                    String userName=sysUserService.getById(Long.valueOf(userId)).getFullname();
                    param.put("userId", userId);
                    param.put("userName", userName);
                    formFolderDao.updateTableIns(param);
                }

                List<Map<String, Object>> checktimes = allresult
                        .get("checktimes");
                Map<String, Object> tbinsparam = new HashMap<String, Object>();
                tbinsparam.put("ID", tableInstanId);
                tbinsparam.put("starttime", checktimes.get(0).get("starttime"));
                tbinsparam.put("endtime", checktimes.get(0).get("endtime"));
                tbinsparam.put("padCode", padCode);
                formFolderDao.updateTableInsime(tbinsparam);
                Map<String, Object> packageparam = new HashMap<String, Object>();
                packageparam.put("id", tableInstanId);
                packageparam.put("zxzt", "已完成");
                packageparam.put("endtime", checktimes.get(0).get("endtime"));

                formFolderDao.updateDataPackageById(packageparam, CommonTools.Obj2String(checktimes.get(0).get("endtime")));
                message.put("html", newhtml.get("html"));
                message.put("success", "true");
                message.put("msg", "保存表单成功");
                message.put("title", title);
                message.put("padCode", padCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.put("success", "false");
            message.put("msg", "表单录入失败");
            message.put("title", null);
            message.put("signreses", null);
        }

        return message;
    }
    /**
     * 从xml中解析title
     * @return
     */
    public String getPadVersion(String taskXml) {
        taskXml = taskXml.replaceAll("&", "#");
        Map<String, List<Map<String, Object>>> allresult = new HashMap<String, List<Map<String, Object>>>();
        try {
            Document tableDoc = DocumentHelper.parseText(taskXml);
            List<Element> tasks = tableDoc.getRootElement().selectNodes(
                    ".//task");
            String title="";
            for (Element task : tasks) {
                title=task.attributeValue("deviceCode");
                if(title!=null) {
                    return title;
                }
            }
        }catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 从xml中解析title

     * @return
     */
    public String getTitle(String taskXml) {
        taskXml = taskXml.replaceAll("&", "#");
        Map<String, List<Map<String, Object>>> allresult = new HashMap<String, List<Map<String, Object>>>();
        try {
            Document tableDoc = DocumentHelper.parseText(taskXml);
            List<Element> tasks = tableDoc.getRootElement().selectNodes(
                    ".//task");
            String title="";
            for (Element task : tasks) {
                title=task.attributeValue("name");
                if(title!=null) {
                    return title;
                }
            }
        }catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 从PAD中获取最新的html保存
     * @param html
     * @param contentslist
     * @return
     */
    public static Map<String, Object> updateHtml(String html,
                                                 List<Map<String, Object>> contentslist,List<Map<String, Object>> check) {
        html = "<html>" + html + "</html>";
        html = html.replaceAll("&", "#");
        Map<String, Object> message = new HashMap<String, Object>();
        try {
            Document tableDoc = DocumentHelper.parseText(html);
            List<Element> tables = tableDoc.getRootElement().selectNodes(
                    ".//table");
            for (Element table : tables) {
                List<Element> inputs = table.selectNodes(".//input");
                // if(inputs.size()!=contentslist.size()){
                // message.put("html", "");
                // return message;
                // }

                Map<String, String> contentMap = listToMap(contentslist);
                Map<String, String> checkMap=listFormat(check);
                for (int i = 0; i < inputs.size(); i++) {
                    String resId = inputs.get(i).attributeValue("id");
                    if (StringUtil.isNotEmpty(resId)
                            && contentMap.containsKey(resId)) {
                        String input = inputs.get(i).asXML();
                        String val = contentMap.get(resId);
                        String isFull=checkMap.get(resId);
                        if (input.contains("text")) {
                            inputs.get(i).addAttribute("value", val);
                            inputs.get(i).addAttribute("isFull", isFull);
                            if("不合格".equals(isFull)) {
                                inputs.get(i).addAttribute("style", "background:red;width:60px");
                            }

                        } else if (input.contains("checkbox")) {
                            if (val.equals("true")) {
                                inputs.get(i)
                                        .addAttribute("checked", "checked");
                            }
                        }
                    }
                    // for(int j=0;j<inputs.size();j++){
                    // if(ID.equals(contentslist.get(j).get("resultid").toString())){
                    //
                    //
                    // break;
                    // }
                    // }
                }
                message.put("msg", "");
            }
            message.put("html", tableDoc.asXML().replaceAll("#", "&"));
        } catch (Exception e) {
            e.printStackTrace();
            message.put("html", "");
        }
        return message;
    }

    private static Map<String, String> listToMap(
            List<Map<String, Object>> contentslist) {
        Map<String, String> contentMap = new HashMap<String, String>();
        for (Map<String, Object> content : contentslist) {
            contentMap.put(content.get("resultid").toString(),
                    content.get("value").toString());
        }
        return contentMap;
    }

    private static Map<String, String> listFormat(
            List<Map<String, Object>> check) {
        Map<String, String> contentMap = new HashMap<String, String>();
        for (Map<String, Object> content : check) {
            contentMap.put(content.get("cellId").toString(),
                    content.get("isFuhe").toString());
        }
        return contentMap;
    }


    public String saveTaskBean(TasksBean tasksbean) {
        String tableInstanId = tasksbean.getTableinstanceId();
        String tableVersion = tasksbean.getVersion();
        // String versionHeader = tableVersion.split("\\.")[0];

        // 修改签署信息
        for (Iterator signsit = tasksbean.getSigns().iterator(); signsit
                .hasNext(); ) {
            SignsBean signsbean = (SignsBean) signsit.next();
            for (Iterator signit = signsbean.getSign().iterator(); signit
                    .hasNext(); ) {
                SignBean signbean = (SignBean) signit.next();
                String signid = signbean.getSignId();
                String signTime = CommonTools.null2String(signbean.getTime());
                // Timestamp.valueOf(signTime);
                // 暂时没有对签署项进行update操作，唯一一个可操作的属性值就是签署时间
                // boolean isOk = signDao.updateSign(signbean.getSignId(),
                // signbean.getTime(),
                // signbean.getRemark(), signbean.getName(),
                // ConfigInfo.SCHEMA_ID,
                // ConfigInfo.SIGNRESULT_MODELID);
                // System.out.println("sign保存"+isOk);

                // String formatSignTime = "to_date('" + signTime +
                // "','yyyy-mm-dd hh24:mi:ss') ";

                // StringBuffer sql = new StringBuffer();
                // sql.append("UPDATE SIGNRESULT_").append(ConfigInfo.SCHEMA_ID)
                // .append(" SIGNUSER SET SIGNTIME_").append(ConfigInfo.SIGNRESULT_MODELID)
                // .append(" = ").append(formatSignTime).append(" WHERE SIGNUSER.ID = ")
                // .append(signid);
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("formatSignTime", signTime);
                param.put("signid", signid);
                formFolderDao.updatesignres(param);
            }
        }

        // 更新conditon_res表中的信息
        for (Iterator conditonsit = tasksbean.getConditions().iterator(); conditonsit
                .hasNext(); ) {
            ConditionsBean conditionsbean = (ConditionsBean) conditonsit.next();
            for (Iterator conditionit = conditionsbean.getCondition()
                    .iterator(); conditionit.hasNext(); ) {
                ConditionBean conditionbean = (ConditionBean) conditionit
                        .next();
                String conditionid = conditionbean.getConditionId();
                String conditionvalue = CommonTools.null2String(conditionbean
                        .getValuename());
                // StringBuffer sql = new StringBuffer();
                // sql.append("UPDATE W_CONDI_RES").append(ConfigInfo.SCHEMA_ID)
                // .append(" CONDITION SET VALUE_").append(ConfigInfo.CONDIRES_MODELID)
                // .append(" = '").append(conditionvalue).append("'").append(" WHERE CONDITION.ID = ")
                // .append(conditionid);

                Map<String, Object> param = new HashMap<String, Object>();
                param.put("conditionvalue", conditionvalue);
                param.put("conditionid", conditionid);
                formFolderDao.updatecondies(param);
            }
        }

        // 修改检查操作信息
        for (Iterator rowsit = tasksbean.getRows().iterator(); rowsit.hasNext(); ) {
            RowsBean rowsbean = (RowsBean) rowsit.next();
            for (Iterator rowit = rowsbean.getRow().iterator(); rowit.hasNext(); ) {
                RowBean rowbean = (RowBean) rowit.next();
                for (Iterator cellit = rowbean.getCell().iterator(); cellit
                        .hasNext(); ) {
                    CellBean cellbean = (CellBean) cellit.next();
                    if (cellbean.getType().equals("TRUE")) {
                        for (Iterator operationit = cellbean.getOperation()
                                .iterator(); operationit.hasNext(); ) {
                            OperationBean operationbean = (OperationBean) operationit
                                    .next();
                            String type = CommonTools.null2String(operationbean
                                    .getType());
                            String value = CommonTools
                                    .null2String(operationbean.getValue());
                            if (value.contains("\'")) {
                                value = value.toString().replaceAll("'", "''");
                            }
                            String remark = CommonTools
                                    .null2String(operationbean.getRemark());
                            String resultId = CommonTools
                                    .null2String(operationbean.getResultid());
                            String time = CommonTools.null2String(operationbean
                                    .getTime());
                            // operationDao.updateOperation(resultId, value,
                            // time, ConfigInfo.SCHEMA_ID,
                            // ConfigInfo.CKRESULT_MODELID);
                            // StringBuffer sql = new StringBuffer();
                            // sql.append("UPDATE W_CK_RESULT").append(" SET F_VALUE").append(" = '").append(value).append("'").append(" WHERE ID = ").append(resultId);

                            Map<String, Object> param = new HashMap<String, Object>();
                            param.put("value", value);
                            param.put("resultId", resultId);
                            formFolderDao.updateckres(param,tableInstanId);
                        }
                    }
                }
            }
        }

        // 修改表格是否正确信息
        return tableInstanId;
    }

    /**
     * 根据表格实例ID获取表格实例
     *
     * @param param
     * @return
     */
    public Map<String, Object> getTableInstantById(Map<String, Object> param) {
        return formFolderDao.getTableInstantById(param);
    }

    /**
     * 解析XML，将结果值，解析保存。
     * @param taskXml
     * @return
     */
    public Map<String, List<Map<String, Object>>> resolveXml(String taskXml) {
        taskXml = taskXml.replaceAll("&", "#");
        Map<String, List<Map<String, Object>>> allresult = new HashMap<String, List<Map<String, Object>>>();

        try {
            Document tableDoc = DocumentHelper.parseText(taskXml);
            List<Element> tasks = tableDoc.getRootElement().selectNodes(
                    ".//task");
            List<Map<String, Object>> conditionreses = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> signreses = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> operationreses = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> checktimes = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> check=new ArrayList<>();
            for (Element task : tasks) {
                String starttime = task.attributeValue("starttime");
                String endtime = task.attributeValue("endtime");
                Map<String, Object> checktime = new HashMap<String, Object>();
                checktime.put("starttime", starttime);
                checktime.put("endtime", endtime);
                checktimes.add(checktime);

                //检查条件结果---start
                List<Element> conditions = task.selectNodes(".//condition");
                for (int i = 0; i < conditions.size(); i++) {
                    String conditionId = conditions.get(i).attributeValue("conditionId");
                    String conditionDefId = conditions.get(i).attributeValue("conditionDefId");
                    String valuename = conditions.get(i).attributeValue("valuename");
                    if (conditionId != null && valuename != null) {
                        Map<String, Object> conditionres = new HashMap<String, Object>();
                        conditionres.put("conditionid", conditionId);
                        conditionres.put("conditionDefId", conditionDefId);
                        conditionres.put("conditionvalue", valuename.replace("#nbsp;", " "));
                        conditionreses.add(conditionres);
                    }
                }
                //检查条件结果---end
                //签署结果---start
                List<Element> signs = task.selectNodes(".//sign");
                for (int i = 0; i < signs.size(); i++) {
                    String signId = signs.get(i).attributeValue("signId");
                    String signDefId = signs.get(i).attributeValue("signDefId");
                    String time = signs.get(i).attributeValue("time");
                    if (signId != null && time != null) {
                        Map<String, Object> signres = new HashMap<String, Object>();
                        signres.put("signid", signId);
                        signres.put("signDefId", signDefId);
                        signres.put("time", time);// "to_date('"+time+"','yyyy-mm-dd hh24:mi:ss')"
                        signreses.add(signres);
                    }
                }

                List<Element> cell = task.selectNodes(".//cell");
                for (Element element : cell) {
                    String type = element.attributeValue("type");
                    if(type!=null&&type.equals("TRUE")) {
                        String isFuhe=element.attributeValue("isFuhe");
                        if(isFuhe!=null&&!"".equals(isFuhe)) {
                            Map<String, Object> map=new HashMap<String, Object>();
                            String cellId=element.attributeValue("cellid");
                            String columnId=element.attributeValue("columnid");
                            map.put("isFuhe", isFuhe);
                            map.put("cellId", cellId);
                            map.put("columnId", columnId);
                            check.add(map);
                        }

                    }

                }

                //签署结果---end
                //检查项---start
                List<Element> operations = task.selectNodes(".//operation");
                for (int i = 0; i < operations.size(); i++) {
                    String resultid = operations.get(i).attributeValue(
                            "resultid");
                    String value = operations.get(i).attributeValue("value");
                    String type = operations.get(i).attributeValue("type");
                    String isFuhe=operations.get(i).attributeValue("isFuhe");
                    //乔志理修改  打钩情况取值应为value，value=“is"为打钩，type为判断检查项类型，type=1为checkbox，type=2为填值
                    String value1 = "";
                    if (resultid != null && value != null && type != null) {
                        if (type.equals("1") && value.equals("is")) {
                            value = "true";
                        }
                        Map<String, Object> operationres = new HashMap<String, Object>();
                        operationres.put("resultid", resultid);
                        operationres.put("value", value.replace("#nbsp;", " "));
                        operationreses.add(operationres);
                    }
                }
            }
            allresult.put("checktimes", checktimes);
            allresult.put("conditionreses", conditionreses);
            allresult.put("signreses", signreses);
            allresult.put("operationreses", operationreses);
            allresult.put("check", check);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return allresult;
    }
//	/**
//	 * 下载照片到客户端
//	 * @return
//	 */
//	public void downloadsignphoto(HttpServletResponse response, String taskId, String signId){
//		return
//	
//	/**
//	 * 下载操作项照片到客户端
//	 * @return
//	 */
//	public void downloadopphoto(HttpServletResponse response, String taskId, String photoName){
//		String path = "";
//		if(!"".equals(path) && path != null){
//			File file = new File(path);
//			FileUtil.downloadFileByPath(file.getName(), file, response);
//    	}
//	}

    public String getPhotoIdBySignId(String dataId) {
        return dataSyncDao.getFileIdByDataId(dataId);
    }

    public void downloadopphotofinished(HttpServletResponse response, String taskId) {
        // 附件保存路径
        String attachPath = AppUtil.getAttachPath();
        String tempPath = attachPath + File.separator + System.currentTimeMillis();
        FileOperator.createFolder(tempPath);

        List<Map<String, Object>> resultList = dataSyncDao.getCKResultByInsId(taskId);
        for (Map<String, Object> result : resultList) {
            String resId = CommonTools.Obj2String(result.get("ID").toString());
            String resFile = attachPath + File.separator + resId + File.separator + "0";
            FileOperator.copyDirectiory(resFile, tempPath + File.separator + taskId + File.separator + resId);
        }
        String resFile = tempPath + File.separator + taskId + ".zip";
        try {
            File insFile = new File(tempPath + File.separator + taskId + File.separator);
            if (insFile.exists()) {
                File[] ckFiles = insFile.listFiles();
                String[] filePath = new String[ckFiles.length];
                for (int i = 0; i < ckFiles.length; i++) {
                    filePath[i] = ckFiles[i].getPath();
                }
                ZipCompressor zc = new ZipCompressor(resFile);
                zc.compress(filePath);
                File file = new File(resFile);
                this.downloadFileByPath(file.getName(), file, response);
                FileOperator.delFoldsWithChilds(tempPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载单个文件
     *
     * @param fileName
     * @param downloadFile
     * @param response
     */
    private void downloadFileByPath(String fileName, File downloadFile, HttpServletResponse response) {
        // 文件存在时才去下载
        if (downloadFile.exists()) {
            try {
                String contentType = "application/msword";
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(downloadFile));
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                byte[] buffer = new byte[8192];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public String getLocalTask(HttpServletRequest request) {
        try {
            File file = ResourcesUtils.getResourceAsFile("task.txt");
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            return document.asXML();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 下载整个系统的产品列表
     *
     * @return
     */
    public String downloadproductlist() {
        //1，下载所有的产品列表
        List<Map<String, Object>> w_productList = importFromOldSystemDao.getListByTableNameAndFilter("W_PRODUCT", null);
        StringBuffer productStr = new StringBuffer();
        for (Map<String, Object> w_product : w_productList) {
            String product_Id = CommonTools.Obj2String(w_product.get("ID"));
            String product_Name = CommonTools.Obj2String(w_product.get("F_XHMC"));
            productStr.append(product_Id).append(",").append(product_Name).append(";");
        }
        return CommonTools.null2String(productStr.toString());
    }

    /**
     * 下载所属试验队下所有的多媒体资料
     *
     * @param username
     * @param userId
     * @return
     */
    public String downloadmmclist(String username, String userId) {
        MmcBean mmcBean = new MmcBean();
        Set mmcSet = new HashSet(0);
        //1、产品验收场景附件
        List<Map<String, Object>> acceptancePlanList=acceptancePlanDao.getAllAcceptancePlan();
        getCPFJ(acceptancePlanList, mmcSet);
        //2、武器和靶场场景附件
        List<Map<String, Object>> BCacceptancePlanList=acceptancePlanDao.getAllBCAcceptancePlan();
        getBCFJ(BCacceptancePlanList, mmcSet);
        mmcBean.setMmcset(mmcSet);
        if (mmcBean != null)
            return BeanToXML.MmcToXML(mmcBean);
        else
            return "";
    }

    //获取产品验收附件
    public void getCPFJ(List<Map<String, Object>> acceptancePlanList,Set mmcSet) {
        for (Map<String, Object> map : acceptancePlanList) {
            String fileInfo=map.get("F_YSYJWJ")!=null?String.valueOf(map.get("F_YSYJWJ")):"";
            if(fileInfo.equals("")) {
                continue;
            }
            JSONArray jsonArray=JSONObject.parseArray(fileInfo);
            for (int i=0;i<jsonArray.size();i++){
                Mmc mc=new Mmc();
                //循环遍历json数组
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String fileId=jsonObject.getString("id");
                SysFile sysFile=sysFileService.getById(Long.valueOf(fileId));
                if(sysFile!=null) {
                    Map<String,Object> batchInfo=productCategoryBatchDao.getById(String.valueOf(map.get("F_SSCPPC")));

                    String categoryId=batchInfo.get("F_SSCPLB").toString();
                    String batchId=batchInfo.get("ID").toString();
                    Map<String,Object> categoryInfo=productCategoryBatchDao.getById(categoryId);
                    String modelId=batchInfo.get("F_SSXH").toString();
                    String acceptancePlanId=map.get("ID").toString();
                    Map<String, Object> modelMap=modelDao.getById(modelId);
                    mc.setAcceptancePlanName(String.valueOf(map.get("F_chbgbbh")));
                    mc.setModelName(String.valueOf(modelMap.get("F_XHMC")));
                    mc.setBatchName(String.valueOf(batchInfo.get("F_pch")));  //批次
                    mc.setCategoryName(String.valueOf(categoryInfo.get("F_CPDH")));  //类别
                    mc.setMmcId(String.valueOf(sysFile.getFileId()));
                    mc.setMmcName(String.valueOf(sysFile.getFilename()));
                    mc.setType(sysFile.getExt());
                    mc.setId(UniqueIdUtil.genId());
                    mc.setAcceptancePlanId(acceptancePlanId);
                    mc.setBatchId(batchId);
                    mc.setCategoryId(categoryId);
                    mc.setModelId(modelId);
                    mc.setFieldType("1");
                    mmcSet.add(mc);

                }

            }
        }
    }

    //获取武器及靶场附件
    public void getBCFJ(List<Map<String, Object>> acceptancePlanList,Set mmcSet) {
        for (Map<String, Object> map : acceptancePlanList) {
            String fileInfo=map.get("F_SYYJWJ")!=null?String.valueOf(map.get("F_SYYJWJ")):"";
            if(fileInfo.equals("")) {
                continue;
            }
            JSONArray jsonArray=JSONObject.parseArray(fileInfo);
            for (int i=0;i<jsonArray.size();i++){
                Mmc mc=new Mmc();
                //循环遍历json数组
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String fileId=jsonObject.getString("id");
                SysFile sysFile=sysFileService.getById(Long.valueOf(fileId));
                if(sysFile!=null) {

                    String modelId=map.get("F_XHID").toString();
                    String acceptancePlanId=map.get("ID").toString();
                    String BCCHName = map.get("F_CHBGBBH").toString();
                    mc.setAcceptancePlanName(BCCHName);
                    mc.setModelName(map.get("F_XHDH").toString());
                    mc.setMmcId(String.valueOf(sysFile.getFileId()));
                    mc.setMmcName(String.valueOf(sysFile.getFilename()));
                    mc.setType(sysFile.getExt());
                    mc.setId(UniqueIdUtil.genId());
                    mc.setAcceptancePlanId(acceptancePlanId);
                    mc.setModelId(modelId);
                    if (BCCHName.contains("BCSY")) {
                        mc.setFieldType("3");
                    }else if (BCCHName.contains("WQSJ")) {
                        mc.setFieldType("2");
                    }
                    mmcSet.add(mc);

                }

            }
        }
    }

    /**
     * @param
     * @return
     * @Description: 自动生成对应复制节点
     * @author qiaozhili
     * @date 2019/3/9 14:25
     */
    public String createCopyNodeInfo(String planId, String pathId, String tempId, String taskId, String taskName, String startTime, String endTime) throws Exception {
//        List<Map<String, Object>> cpList = dataSyncDao.getCpByPathId(pathId);
//        if (cpList.size() == 0) {
//            Long id = dataSyncDao.insertCPB(path, pathId, bropathId, broTaskId, taskId, chId);
//        }

//        if (cpList.size() > 0) {
//            //1、创建复制的产品信息W_CPB
//            createCPInfo(path, pathId, cpList, bropathId, chId);
//        }
//        List<Map<String, Object>> dataPackageInfoList = dataSyncDao.getDataPackageInfoByPathId(broTaskId);
//        //2、创建dataPackageInfo
//        if (dataPackageInfoList.size() > 0) {
//            createDataPackageInfo(broTaskId, pathId, dataPackageInfoList, taskId);
//        }
        String message = "";
        try {
            List<Map<String, Object>> tbInstanceList = dataSyncDao.getTbInstanceByBroTaskId(tempId);
            if (tbInstanceList.size() > 0) {
                //创建tb_instant
                createTbInstanceInfo(taskId, tbInstanceList, tempId, pathId, planId, taskName, startTime, endTime);
                message = "true";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "false";
        }
        return message;

//        //创建W_SIGNRESULT
//        List<Map<String, Object>> signResultList = dataSyncDao.getSignResultByBroTaskId(broTaskId);
//        if (signResultList.size() > 0) {
//            creatSignResult(taskId, signResultList);
//        }

//        //创建W_CONDI_RES
//        List<Map<String, Object>> condiResList = dataSyncDao.getCondiResByBroTaskId(broTaskId);
//        if (condiResList.size() > 0) {
//            insertCondies(taskId, condiResList);
//        }

    }

    /**
     * @param
     * @return
     * @Description: 创建pad端复制的创建dataPackageInfo信息
     * @author qiaozhili
     * @date 2019/3/18 10:26
     */
    public void createDataPackageInfo(String broTaskId, String pathId, List<Map<String, Object>> dataPackageInfoList, String taskId) {
        Map<String, Object> map = dataPackageInfoList.get(0);
        String id = String.valueOf(map.get("ID"));
        //新的dataPackageInfo的ID由旧的taskId和对应dataPackageInfo表中的ID组成
        String idN = String.valueOf(Long.parseLong(id) + Long.parseLong(taskId));

        map.remove("F_SSSJB");
        map.remove("F_SSMB");
        map.remove("ID");
        map.put("F_SSSJB", pathId);
        map.put("F_SSMB", taskId);
        map.put("ID", idN);
        List<Map<String, Object>> dataPackageInfoIdList = dataSyncDao.getDataPackageInfoId();
        List<String> idList = new ArrayList<>();
        for (int i = 0; i < dataPackageInfoIdList.size(); i++) {
            idList.add(dataPackageInfoIdList.get(i).get("ID").toString());
        }
        if (!idList.contains(idN)) {
            dataSyncDao.dataPackageInfoInsert(map);
        }
    }

    /**
     * @param
     * @return
     * @Description: 创建pad端复制的创建dataPackageInfo信息
     * @author qiaozhili
     * @date 2019/3/18 10:26
     */
    public void createTbInstanceInfo(String taskId, List<Map<String, Object>> tbInstanceList, String broTaskId, String pathId, String planId,
                                     String taskName, String startTime, String endTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());

        try {
            TableInstance tbInstance = new TableInstance();
            tbInstance.setId(taskId);
            tbInstance.setName(taskName);
            tbInstance.setNumber(String.valueOf(tbInstanceList.get(0).get("F_NUMBER")));
//            tbInstance.setStatus(String.valueOf(tbInstanceList.get(0).get("F_STATUS")));
            tbInstance.setContent(String.valueOf(tbInstanceList.get(0).get("F_CONTENTS")));
//            tbInstance.setUploadTime(String.valueOf(tbInstanceList.get(0).get("F_UPLOAD_TIME")));
            tbInstance.setTableTempID(String.valueOf(broTaskId));
//            tbInstance.setTaskId(String.valueOf(tbInstanceList.get(0).get("F_TASK_ID")));
//            tbInstance.setVersion(String.valueOf(tbInstanceList.get(0).get("F_VERSION")));
            tbInstance.setStartTime(startTime);
            tbInstance.setEndTime(endTime);
            tbInstance.setUploadTime(currentTime);
            tbInstance.setProductId(String.valueOf(pathId));
            tbInstance.setPlanId(String.valueOf(planId));
            iOTableInstanceDao.insert(tbInstance);
            iOTableInstanceDao.insertFileData(tbInstance.getName(), planId, tbInstance.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertSignResult(String taskId, Map<String, Object> param, String tempId,int i) {
        try {
            List<Map<String, Object>> signResList = formFolderDao.querySignByTaskId(tempId);
            String signResId = "";
            if (signResList.size() > 0) {
                signResId = signResList.get(i).get("ID").toString();
            }
            SignResult signResult = new SignResult();
            signResult.setId(param.get("signid").toString());
            signResult.setSignTime(param.get("time").toString());
            signResult.setTb_instan_id(taskId);
            signResult.setSigndef_id(param.get("signDefId").toString());
            if("".equals(signResult.getSignTime())||signResult.getSignTime()==null) {
                return;
            }
            iOSignResultDao.insert(signResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void insertCondies(String taskId, Map<String, Object> param, String broTaskId) {
        List<Map<String, Object>> conResList = formFolderDao.queryConditionByTaskId(broTaskId);
        String conditonId = "";
        if (conResList.size() > 0) {
            conditonId = conResList.get(0).get("F_CONDITION_ID").toString();
        }
        ConditionResult conditionResult = new ConditionResult();
        conditionResult.setId(param.get("conditionid").toString());
        conditionResult.setTb_instan_id(taskId);
        conditionResult.setCondition_id(param.get("conditionDefId").toString());
        conditionResult.setValue(param.get("conditionvalue").toString());
        iOConditionResultDao.insert(conditionResult);
    }

    /**
     * @param
     * @return
     * @Description: 创建pad端复制的W_CPB信息
     * @author qiaozhili
     * @date 2019/3/18 10:20
     */
    public void createCPInfo(String path, String pathId, List<Map<String, Object>> packageList, String bropathId, String chId) {
        try {
            SimplePackage mypackage = new SimplePackage();
            mypackage.setId(pathId);
            mypackage.setJdmc(path);
            mypackage.setJdlx(packageList.get(0).get("F_JDLX").toString());
            mypackage.setFzr(String.valueOf(packageList.get(0).get("F_FZR")));
            mypackage.setFzrID(String.valueOf(packageList.get(0).get("F_FZRID")));
            mypackage.setParentID(String.valueOf(packageList.get(0).get("F_PARENTID")));
            mypackage.setSsxh(String.valueOf(packageList.get(0).get("F_SSXH")));
            mypackage.setSsfc(String.valueOf(packageList.get(0).get("F_SSFC")));
            mypackage.setParentName(String.valueOf(packageList.get(0).get("F_PARENTNAME")));
            packageDao.insert(mypackage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDataPath(Mmc mmc) {
        StringBuffer path = new StringBuffer();
        String temppath = "";            //存放path
        String temppath2 = "";
        String taskId = CommonTools.null2String(mmc.getTaskId());
        String packageId = mmc.getPackageId();
        String lasttaskId = "";
        if (taskId.equals("")) {
            lasttaskId = "";
        } else {
//            lasttaskId = TaskListDao.getLastTaskId(taskId);		//得到最后一个taskId
        }
        taskpath.delete(0, taskpath.length());
        pacpath.delete(0, pacpath.length());
        if (lasttaskId.equals("-1")) {                //最后一层taskid,那么该多媒体资料在package下
            temppath = getPacPathByPackageId(packageId);
            path.append(temppath);
        } else if (lasttaskId.equals("")) {
            temppath = getPacPathByPackageId(packageId);
            path.append(temppath);
        } else {                                                    //遍历所有的路径
            temppath = getPacPathByPackageId(packageId);
//            temppath2 = getDataTaskPathByTaskId(taskId);
//            path.append(temppath).append("->").append(temppath2);
        }
        String pathStr = path.toString().replace("->->", "->");
        return pathStr;

    }
//    public String getDataTaskPathByTaskId(String taskId){
//        TaskInfo task = PackageDao.getTaskByTaskId(taskId);
//        if(task != null){
//            String pid = task.getPid();
//            String pathName = task.getTaskName();
//            if(pid.equals("-1")){					//最后一个
//                StringBuffer str = new StringBuffer(pathName);
//                taskpath = str.append(taskpath.toString());
////				taskpath = taskpath.append(pathName);
//            }else{
//                StringBuffer str = new StringBuffer("->"+pathName);
////				taskpath = taskpath.append("->").append(pathName);
//                taskpath = str.append(taskpath.toString());
//                getPacPathByPackageId(pid);
//            }
//        }
//        return CommonTools.null2String(taskpath.toString());
//    }

    StringBuffer pacpath = new StringBuffer();

    public String getPacPathByPackageId(String packageId) {
        List<Map<String, Object>> w_packageList = importFromOldSystemDao.getListByTableNameAndFilter("W_CPB", Arrays.asList(new SyncBaseFilter("ID", "=", packageId)));
        StringBuffer productStr = new StringBuffer();
        for (Map<String, Object> w_package : w_packageList) {
            String product_Id = CommonTools.Obj2String(w_package.get("ID"));
            String product_Name = CommonTools.Obj2String(w_package.get("F_XHMC"));
            productStr.append(product_Id).append(",").append(product_Name).append(";");

            String pid = CommonTools.Obj2String(w_package.get("F_PARENTID"));
            String pathName = CommonTools.Obj2String(w_package.get("F_JDMC"));
            if (pid.equals("0")) {                    //最后一个
                StringBuffer str = new StringBuffer(pathName);
                pacpath = str.append(pacpath.toString());
            } else {
                StringBuffer str = new StringBuffer("->" + pathName);
                pacpath = str.append(pacpath.toString());
//				pacpath = pacpath.append("->").append(pathName);
                getPacPathByPackageId(pid);
            }
        }

        return CommonTools.null2String(pacpath.toString());
    }

    /**
     * 根据fileid来下载多媒体文件
     *
     * @param mmcid
     * @return
     * @throws Exception
     */
    public File dowmloadmmc(String mmcid) throws Exception {

        String attachPath = AppUtil.getAttachPath();
        String filePath = dataSyncDao.getFilePathByFileId(mmcid);
        String tempPath = attachPath + File.separator + filePath;

//        String finalValue = mmcDAO.getMmcFinalValue(mmcid);
//        String path = FileConfig.DEFAULT_PATH+finalValue;
        //去除服务端解密过程，把解密过程放到终端
//		String realpath =FileConfig.DECODE_FILE_PATH + finalValue;
//		FileEncryption.decrypt(path, realpath);
//		File file  = new File(realpath);//新建一个file文件

        File file = new File(tempPath);
        return file;
    }

    /**
     * 从(i,j)开始，将str放入行数为rowSize,列数为colSize的矩阵中
     */
    private void putKey(String[][] keys, int i, int j, int colSize, int rowSize, String str) {
        while (keys[i][j] != null) {
            j++;
        }
        for (int m = i; m < i + rowSize; m++) {
            for (int n = j; n < j + colSize; n++) {
                keys[m][n] = str;
            }
        }

    }

    //将合并的html转换为非合并的html
    private List<Element> tables(String html) {
        html = html.replaceAll("&", "#");
        List<Element> tables = null;
        List<Element> tables1 = new ArrayList<>();
        try {
            Document tableDoc = DocumentHelper.parseText(html);
            tables = tableDoc.getRootElement().selectNodes(".//table");
            StringBuffer sb = new StringBuffer();
            for (Element table : tables) {
                List<Element> trs = table.selectNodes(".//tr");
                String[][] keys = new String[100][100];
                Map<String, Element> cellMaps = new HashMap<String, Element>();
                sb.append("<html>");
                sb.append("<table>");
                sb.append("<tbody>");
                for (int i = 0; i < trs.size(); i++) {
//				for(Element tr:trs){
                    Element tr = trs.get(i);
                    if (tr.attribute("type") != null && tr.attribute("type").equals("tablefront")) {
                        //页眉，跳过第一行
                        continue;
                    }
                    //footer 页脚
                    else if (tr.attribute("type") != null && tr.attribute("type").equals("tabletail")) {
                        continue;
                    }
                    sb.append("<tr>");
                    //非页眉 非页脚
                    List<Element> tds = tr.selectNodes(".//td");
                    for (int m = 0; m < tds.size(); m++) {
                        // 一行中的一列---一个单元格
                        Element td = tds.get(m);
                        String key = "c-" + i + "-" + m;
                        Attribute colspan = td.attribute("colspan");
                        Attribute rowspan = td.attribute("rowspan");
                        int rowSize = rowspan == null ? 1 : Integer.valueOf(rowspan.getValue());
                        int colSize = colspan == null ? 1 : Integer.valueOf(colspan.getValue());
                        //对合并单元格进行拆分
                        if (rowSize > 1 || colSize > 1) {
//							Cell cell=TdToCell(td,key);
                            cellMaps.put(key, td);
                            putKey(keys, i, m, colSize, rowSize, key);
                            for (int k = 0; k < rowSize; k++) {
                                for (int n = 0; n < colSize; n++) {
                                    String position = keys[k][n];
                                    Element td1 = cellMaps.get(keys[k][n]);
                                    sb.append(td1.asXML());
                                }
                            }
                        } else {
                            //无行列合并的单元格直接进行html组装
                            sb.append(td.asXML());
                        }
                    }
                    sb.append("</tr>");
                }
                sb.append("</tbody>");
                sb.append("</table>");
                sb.append("</html>");

                Document htmlDoc = DocumentHelper.parseText(sb.toString());
                tables1.addAll(htmlDoc.getRootElement().selectNodes(".//table"));
                sb.setLength(0);
                ;
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return tables1;
    }

    /**
     * 上传检查项照片
     *
     * @param userName
     * @param userId
     * @param tableInstanId
     * @param picContent
     * @param photoName
     * @param opId
     * @param request
     * @param response
     */
    public void uploadopphoto(String userName, String userId, String tableInstanId, String picContent,
                              String photoName, String opId, String describe,String photoType,
                              HttpServletRequest request, HttpServletResponse response) {
//        String tableInstanStatue = this.getDataPackageStatus(Long.parseLong(tableInstanId));
        //	if(tableInstanStatue.equals("进行中")){
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            //再将request中的数据转化成multipart类型的数据
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Map typeMap = productTypeDao.getProductType(Long.valueOf(tableInstanId));//获取 运载、空间、结构机构的表单类型
            multiRequest.setAttribute("ckResultName", "W_CK_RESULT_CARRY");
//            if ("运载".equals(typeMap.get("TYPE"))) {
//            } else if ("空间".equals(typeMap.get("TYPE"))) {
//                multiRequest.setAttribute("ckResultName", "W_CK_RESULT");
//            } else {
//                multiRequest.setAttribute("ckResultName", "W_CK_RESULT_JGJG");
//            }
            try {
                ISysUser appUser = null;
                if (Long.parseLong(userId) > 0) {
                    appUser = sysUserService.getById(Long.parseLong(userId));
                }
                multiRequest.setAttribute("tableId", opId);
                multiRequest.setAttribute("describe", describe);
                multiRequest.setAttribute("photoType", photoType);

                sysFileService.uploadAttach1(multiRequest, response, appUser, Long.parseLong(userId), photoType,userName,tableInstanId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description: 获取当前时间毫秒
     * @author qiaozhili
     * @date 2019/3/18 14:40
     * @param
     * @return
     */
    public static String getTime(String timeString){

        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date d;
        try{
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch(ParseException e){
            e.printStackTrace();
        }
        return timeStamp;
    }
}
