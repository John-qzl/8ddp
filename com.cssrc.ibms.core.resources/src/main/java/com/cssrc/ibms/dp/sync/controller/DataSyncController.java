package com.cssrc.ibms.dp.sync.controller;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.cssrc.ibms.api.core.util.UniqueIdUtil;

import com.cssrc.ibms.core.resources.mission.dao.TestPlanDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.bean.WorkBoard;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceGroupDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceMessageDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptancePlanDao;
import com.cssrc.ibms.dp.product.acceptance.service.AcceptancePlanService;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.http.RequestUtil;

import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.dp.form.model.Equipment;
import com.cssrc.ibms.dp.form.service.EquipmentInfoService;
import com.cssrc.ibms.dp.form.service.TbInstantService;
import com.cssrc.ibms.dp.product.acceptance.dao.WorkBoardDao;
import com.cssrc.ibms.dp.sync.service.DataSyncService;
import com.cssrc.ibms.system.service.SysFileService;
import com.cssrc.ibms.system.service.SysParameterService;


@Controller
@RequestMapping("/datasync")
public class DataSyncController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Resource
    private DataSyncService dataSyncService;
    //	@Autowired
//	private TaskListBusiness taskListBusiness;
//	@Autowired
//	private TableBusiness tableBusiness;
//	@Autowired
//	private PhotoBusiness photoBusiness;
//	@Autowired
//	private MmcBusiness mmcBusiness;
//	@Autowired
//	protected MetaDAOFactory metaDaoFactory;
//	@Autowired
//	private TableInstanDAO TableInstanDao;
    @Resource
    TbInstantService tbInstantService;
    @Resource
    SysFileService sysFileService;
    @Resource
    ISysUserService sysUserService;
    @Resource
    EquipmentInfoService equipmentInfoService;
    @Resource
    WorkBoardDao workBoardDao;
    @Resource
    SysParameterService sysParameterService;
    @Resource
    private AcceptanceMessageDao acceptanceMessageDao;
    @Resource
    private TestPlanDao testPlanDao;
    @Resource
    SysUserDao sysUserDao;
    @Resource
    AcceptanceGroupDao acceptanceGroupDao;
    @Resource
    AcceptancePlanDao acceptancePlanDao;

    /**
     * 获取userXML
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/sync")
    @ResponseBody
    // public String getUsersXML(HttpServletRequest request,
    // @RequestParam("operationType") String operationType) throws
    // UnsupportedEncodingException{
    public void getUsersXML(HttpServletRequest request, HttpServletResponse response){
        String operation = request.getParameter("operationType");
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    response.getOutputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("错误码9191");
        }
        if (operation.equals("getUsers")) {
            System.out.println("getUsers");
            String userName = CommonTools.null2String(request.getParameter("userName"));
//            String userXml = dataSyncService.getUserXML();        //获取所有用户信息
            String userXml = dataSyncService.getGroupUserXml(userName);     //qzl 获取所有工作队中所有用户信息
            try {
                out.write(userXml);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码102102");
            }
        } else if (operation.equals("login")) {
            System.out.println("login");
            String username = CommonTools.null2String(request.getParameter("username"));
            String password = CommonTools.null2String(request.getParameter("password"));
            try {
                out.write(dataSyncService.validationOnline(username, password));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码112112");
            }

        } else if (operation.equals("gettasklist")) { // 未完成表格清单
            System.out.println("gettasklist---未完成表格清单");
            String userName = CommonTools.null2String(request
                    .getParameter("username"));
            String test = dataSyncService.getTaskListByUserName2(userName,
                    "unfinish");
            System.out.println(test);
            try {
                out.write(test);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码126126");
            }
        } else if (operation.equals("gettask")) { // 未完成表格实例
            if (1 == 2) {
                String task = dataSyncService.getLocalTask(request);
                try {
                    out.write(task);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("错误码135135");
                }
            } else {
                System.out.println("gettask---未完成表格实例---开始");
                String taskInstanceId = CommonTools.null2String(request.getParameter("taskinstanceId"));
                String tempId = CommonTools.null2String(request.getParameter("tempId"));
                String fieldTye = CommonTools.null2String(request.getParameter("fieldTye"));
                try {
                    out.write(dataSyncService.getTableByTableId(taskInstanceId, "unfinish", tempId, fieldTye));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("错误码146146");
                }
                System.out.println("gettask---未完成表格实例---结束");
            }
        }
        else if (operation.equals("uploadpackageinfo")) {
            try {
                request.setCharacterEncoding("utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.out.println("错误码156156");
            }
            String planId = request.getParameter("planId");
            String pathId = request.getParameter("pathId");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            startTime=startTime.substring(0,startTime.indexOf(' '));
            endTime=endTime.substring(0,endTime.indexOf(' '));
            String tempId = request.getParameter("tempId");
            String taskId = request.getParameter("taskId");
            String taskName = request.getParameter("taskName");
            try {
                out.write(dataSyncService.createCopyNodeInfo(planId, pathId, tempId, taskId, taskName, startTime, endTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (operation.equals("padstatus")) {
            try {
                request.setCharacterEncoding("utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.out.println("错误码178178");
            }
            String status=request.getParameter("status").equals("1")?"正在使用":"离线";
        	String equipmentNum=request.getParameter("equipmentNum");
        	String equipmentName=request.getParameter("equipmentName");
        	Equipment equipment=equipmentInfoService.findEquipmentByNum(equipmentNum);
        	if(equipment==null) {
        		Equipment equipmentinfo=new Equipment();
        		equipmentinfo.setEquipmentName(equipmentName);
        		equipmentinfo.setEquipmentNum(equipmentNum);
        		equipmentinfo.setEquipmentStatus(status);
        		equipmentinfo.setEquipmentID(equipmentNum);
        		Long id=UniqueIdUtil.genId();
        		equipmentinfo.setID(id);
        		equipmentInfoService.insertEquipment(equipmentinfo);
        	}
        	else {
        		equipmentInfoService.updataStatus(equipmentNum, status);
        	}
        	
        }
        /**
         * pad采集完数据后
         * 将实例的内容发回给服务器
         */
        else if (operation.equals("uploadtask")) { // 客户端上传表格实例
            try {
                request.setCharacterEncoding("utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.out.println("208208");
            }
            String planId = request.getParameter("planId");
            String tableInstanId = request.getParameter("tableInstanId");
            String broTaskId = request.getParameter("broTaskId");
            String taskXml = request.getParameter("taskContent"); // 表格实例
            String taskHtml = request.getParameter("htmlContent"); // html内容
            String tempId = request.getParameter("tempId"); // 复制来源taskId
            String userid = request.getParameter("userid"); // 上传人Id
            // return tableBusiness.uploadTask(taskXml, taskHtml);
            //判断一下到底是改成验收的看板状态
            // 还是改成靶场的看板
            // 还是改成所检的看板
            WorkBoard workBoard=workBoardDao.getByPlanId(planId);
            if (workBoard.getGzm().indexOf("YSCH")!=-1){
                //验收的
                sendMassageWhenPADToPCForYSCH(planId);
                workBoardDao.updatework(planId, "数据已采集", "发起验收总结");

            }else if (workBoard.getGzm().indexOf("BCSY")!=-1){
                //靶场的
                sendMassageWhenPADToPCForBCSY(planId);
                workBoardDao.updatework(planId, "数据已采集", "发起数据确认");
            }else {
                //武器所检的
                sendMassageWhenPADToPCForBCSY(planId);
                workBoardDao.updatework(planId, "数据已采集", "发起数据确认");
            }

            try {
                out.write(dataSyncService.uploadTask(taskXml, taskHtml, tableInstanId, tempId, planId, broTaskId,userid));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码223223");
            }
        } else if (operation.equals("getfinishedtasklist")) { // 已完成表格清单
            String userName = CommonTools.null2String(request
                    .getParameter("username"));
            // return taskListBusiness.getTaskListByUserName(userName,
            // "finish");
            String test = dataSyncService.getTaskListByUserName2(userName,
                    "finish");
            System.out.println(test);
            // out.write(taskListBusiness.getTaskListByUserName(userName,
            // "finish"));		
            try {
                out.write(test);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("239239");
            }
        } else if (operation.equals("getfinishedtask")) { // 已完成表格实例
            String tableId = CommonTools.null2String(request
                    .getParameter("taskinstanceId"));
            if (!"".equals(tableId)) {
                // return tableBusiness.getTableByTableId(tableId);
                try {
                    out.write(dataSyncService.getTableByTableId(tableId, "finish", "", ""));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("错误码250250");
                }
            } else {
                // return "";
                try {
                    out.write("");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("错误码258258");
                }
            }
            // return tableBusiness.getTableByTableId(tableId);

        } else if (operation.equals("getuserrwrelation")) { // 任务，人员关系表结构
            // return tableBusiness.getUserRwTableRelation();
            String userName = CommonTools.null2String(request
                    .getParameter("username"));
            System.out.println("getuserrwrelation");
            try {
                out.write(dataSyncService.getUserRwTableRelation(userName));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码267267");
            }
        } else if (operation.equals("getBCuserrwrelation")) { // 获取靶场策划信息
            String userName = CommonTools.null2String(request
                    .getParameter("username"));
            System.out.println("getBCuserrwrelation");
            try {
                out.write(dataSyncService.getBCUserRwTableRelation(userName));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码282282");
            }
        } else if (operation.equals("gethtml")) {
            String tbinstanId = CommonTools.null2String(request.getParameter("taskinstanceId"));
            String tempId = CommonTools.null2String(request.getParameter("tempId"));
            try {
                out.write(dataSyncService.getHtmlByInstanId(tbinstanId, tempId));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码291291");
            }
        } else if (operation.equals("uploadsignphoto")) { //上传签署照片
            String userName = CommonTools.null2String(request.getParameter("username"));
            String userId = CommonTools.null2String(request.getParameter("userid"));
            String resultId = CommonTools.null2String(request.getParameter("resultId"));//签署项ID
			String describe = CommonTools.null2String(request.getParameter("describe"));
            Long tableInstanId = 0L;
            try {
                tableInstanId =RequestUtil.getLong(request, "tableInstanId");//表单实例
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (tableInstanId != 0L) {
//                String tableInstanStatue = dataSyncService.getDataPackageStatus(tableInstanId);
                //	if(tableInstanStatue.equals("进行中")){
                boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                if (isMultipart) {
                    //再将request中的数据转化成multipart类型的数据
                    MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                    try {
                        ISysUser appUser = null;
                        if (Long.parseLong(userId) > 0) {
                            appUser = sysUserService.getById(Long.parseLong(userId));
                        }
                        multiRequest.setAttribute("dataId", resultId);
                        multiRequest.setAttribute("describe",describe);
                        multiRequest.setAttribute("photoType",operation);

                        /**
                         * 为了签章正常运行，这里增加参数userName
                         * 签章不在这里用，但是因为这个方法在保存签章的时候也调了
                         * 所以只能一起带上这个参数
                         */
                        sysFileService.uploadAttach1(multiRequest, response, appUser, Long.parseLong(userId), operation,userName,tableInstanId.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //	}
            }

        } 
        
        else if (operation.equals("checkversion")) {
            String version = CommonTools.null2String(request.getParameter("version"));
            String versionNew=sysParameterService.getByAlias("pad_version");
            
            String destFilePath = AppUtil.getRealPath("/") + "help" + File.separator + "PAD" + File.separator;
            boolean isUpdata = false;
            try {
                isUpdata = sysFileService.checkJDKExisted(destFilePath, versionNew);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                out.write(String.valueOf(isUpdata));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码351351");
            }
        } else if (operation.equals("downloadapk")) {
            String version = CommonTools.null2String(request.getParameter("version"));
            String versionNew=sysParameterService.getByAlias("pad_version");
            try {
                sysFileService.getPadApk(request, response, versionNew);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码360360");
            }
        } 
        
        else if (operation.equals("uploadpersonalsignphoto")) { //上传电子签章

            String fullName = CommonTools.null2String(request.getParameter("username"));
            //fullname是pad里的姓名
            String  uesrName= CommonTools.null2String(request.getParameter("userid"));
            //userName是pad里的工号(以此处注释为准
            Long tableInstanId = 0L;
            try {
                tableInstanId = RequestUtil.getLong(request, "tableInstanId");//表单实例
            } catch (Exception e) {
                e.printStackTrace();
            }
//                String tableInstanStatue = dataSyncService.getDataPackageStatus(tableInstanId);
                //	if(tableInstanStatue.equals("进行中")){
                boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                if (isMultipart) {
                    //再将request中的数据转化成multipart类型的数据
                    MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                    try {
                        ISysUser appUser = null;
                        if (!"".equals(uesrName)) {
                           //传回来了用户的fullname
                            appUser = sysUserService.getByUsername(uesrName);
                            multiRequest.setAttribute("photoType",operation);
                            //考虑当前系统无此用户的情况
                            Long appUserId;
                            if (appUser==null){
                                appUserId=0L;
                            }else {
                                appUserId=appUser.getUserId();
                            }
                            //数据同步(会同步签章)
                            out.write(sysFileService.uploadAttach1(multiRequest, response, appUser, appUserId, operation,uesrName,tableInstanId.toString()));
                        }else {

                        }
                       
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //	}
        

        } else if (operation.equals("downloadsignphoto")) { //下载签署照片
            String taskId = CommonTools.null2String(request.getParameter("taskid"));
            String signId = CommonTools.null2String(request.getParameter("signid"));
            String fileId = dataSyncService.getPhotoIdBySignId(signId);
            if (StringUtil.isNotEmpty(fileId) && StringUtil.isNumberic(fileId)) {
                try {
                    sysFileService.getFileById(request, response, Long.valueOf(fileId));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("错误码417417");
                }
            }
        } else if (operation.equals("uploadopphoto")) { // 上传操作项照片
            String userName = CommonTools.null2String(request
                    .getParameter("username"));
            String userId = CommonTools.null2String(request
                    .getParameter("userid"));
            String tableInstanId = CommonTools.null2String(request
                    .getParameter("tableInstanId"));
            String picContent = CommonTools.null2String(request
                    .getParameter("picContent"));
            String photoName = CommonTools.null2String(request
                    .getParameter("photoName"));
            String opId = CommonTools.null2String(request
                    .getParameter("operationId"));
			//
			String describe = CommonTools.null2String(request.getParameter("describe"));
			String photoType = CommonTools.null2String(request.getParameter("operationType"));

			dataSyncService.uploadopphoto(userName,userId,tableInstanId,picContent,photoName,opId,describe,photoType,request,response);
        }
		else if (operation.equals("downloadopphoto")) { // 下载操作项图片
            String taskId = CommonTools.null2String(request.getParameter("taskid"));//10000003560118
            dataSyncService.downloadopphotofinished(response, taskId);
        }
        else if (operation.equals("downloadproductlist")) {
			// 下载产品列表 示例：productid,productName; productid,productName
            try {
                out.write(dataSyncService.downloadproductlist());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码449449");
            }
        }
        else if (operation.equals("downloadmmclist")) { // 下载多媒体资料
            String username = CommonTools.null2String(request
                    .getParameter("username"));
            String userid = CommonTools.null2String(request
                    .getParameter("userid"));
            try {
                out.write(dataSyncService.downloadmmclist(username, userid));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码461461");
            }
        } else if (operation.equals("dowmloadmmc")) { // 根据每个mmc的id来下载mmc
            String mmcid = CommonTools.null2String(request.getParameter("mmcid"));
            try {
                File file = null;
                try {
                    file = dataSyncService.dowmloadmmc(mmcid);
                } catch (Exception e) {
                    System.out.println("错误码:470470");
                    e.printStackTrace();
                }
                response.reset();
                response.setHeader(
                        "Content-Disposition",
                        "attachment;filename="
                                + URLEncoder.encode(file.getName(), "UTF-8")
                                + ";filelength=" + file.length());
                if (file.exists()){
                    BufferedInputStream in = new BufferedInputStream(
                            new FileInputStream(file));
                    BufferedOutputStream fout = new BufferedOutputStream(
                            response.getOutputStream());
                    byte[] buffer = new byte[8192];
                    int len = 0;
                    while ((len = in.read(buffer)) != -1) {
                        fout.write(buffer, 0, len);
                    }
                    in.close();
                    fout.write(buffer);
                    fout.flush();
                    fout.close();
                }else {
                    System.out.println("pad在取依据文件的时候没有找到这个文件,已经跳过了\n"+file.getPath());
                }


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("错误码496496");
            }
        }else {
            try {
                out.write("");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("错误码496496");
            }
        }

        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("错误码505505");
        }

    }
    /**
     * @Description 靶场pad回传表单给中转机的时候发消息
     * @Author ZMZ
     * @Date 2020/12/7 9:22
     * @param missionId
     * @Return void
     */
    public void sendMassageWhenPADToPCForYSCH(String missionId){
        //定位策划
        Map<String,Object> rangeTestPlan=acceptancePlanDao.getMapById(missionId);
        //定位组员表
        List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByAcceptancePanId(missionId);
        for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
            SysUser teamMember=sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
            if ("组长".equals(acceptanceGroup.getZw())){
                //是组长   直接发消息
                acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"表单已回传至中转机","组长");
            }else {
                //不是组长
                //先看下这个人在组里是不是有担任组长的同时担任组员
                if (curUserIsTeamLeader(acceptanceGroupList,acceptanceGroup.getXmId())){
                    //当前这个人既是组员也是组长
                    //组长的消息是上面发的,这里直接跳过
                }else {
                    //当前这个人不是组长,直接发消息
                    acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"表单已回传至中转机","组员");
                }
            }
        }
       /* //确定发起人
        String sponsorId=rangeTestPlan.get("F_FQRID").toString();
        SysUser sponsor=sysUserDao.getById(Long.valueOf(sponsorId));
        if (curUserInTeam(acceptanceGroupList,sponsorId)){
            //发起人属于组员/组长
            //该消息已由上面发送
        }else {
            //发起人不属于组员/组长
            acceptanceMessageDao.insertMessageForCommon(sponsor,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"表单已回传至中转机","发起人");
        }*/
    }
    /**
     * @Description 靶场pad回传表单给中转机的时候发消息
     * @Author ZMZ
     * @Date 2020/12/7 9:22
     * @param missionId
     * @Return void
     */
    public void sendMassageWhenPADToPCForBCSY(String missionId){
        //定位策划
        Map<String,Object> rangeTestPlan=testPlanDao.getPlanById(missionId).get(0);
        //定位组员表
        List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByMissionId(missionId);
        for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
            SysUser teamMember=sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
            if ("组长".equals(acceptanceGroup.getZw())){
                //是组长   直接发消息
                acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"表单已回传至中转机","组长");
            }else {
                //不是组长
                //先看下这个人在组里是不是有担任组长的同时担任组员
                if (curUserIsTeamLeader(acceptanceGroupList,acceptanceGroup.getXmId())){
                    //当前这个人既是组员也是组长
                    //组长的消息是上面发的,这里直接跳过
                }else {
                    //当前这个人不是组长,直接发消息
                    acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"表单已回传至中转机","组员");
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
            acceptanceMessageDao.insertMessageForCommon(sponsor,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_SYRWMC"),"表单已回传至中转机","发起人");
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
     * 表格上传步骤： 1，已完成的表格实例 2，已完成表格实例的HTML 3，已完成表格实例的检查项照片 4，任务级别的照片
     */
    protected boolean saveUploadFile(InputStream input, File dst) {
        boolean flag = false;
        OutputStream out = null;
        try {
            if (dst.exists()) {
                out = new BufferedOutputStream(new FileOutputStream(dst, true),
                        204800);
            } else {
                out = new BufferedOutputStream(new FileOutputStream(dst),
                        204800);
            }

            byte[] buffer = new byte[204800];
            int len = 0;
            while ((len = input.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

}
