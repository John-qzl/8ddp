package com.cssrc.ibms.core.resources.util.service;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.excel.reader.DataEntity;
import com.cssrc.ibms.core.excel.reader.ExcelReader;
import com.cssrc.ibms.core.excel.reader.FieldEntity;
import com.cssrc.ibms.core.excel.reader.TableEntity;
import com.cssrc.ibms.core.resources.product.dao.ModuleManageDao;
import com.cssrc.ibms.core.resources.util.dao.SysUserImportDao;
import com.cssrc.ibms.core.resources.util.intf.ISysUserConst;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.model.*;
import com.cssrc.ibms.core.user.service.PositionService;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.user.service.SysRoleService;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.user.service.UserCustomService;
import com.cssrc.ibms.core.user.service.UserPositionService;
import com.cssrc.ibms.core.user.service.UserRoleService;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.core.util.encrypt.PasswordUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.system.model.Job;
import com.cssrc.ibms.system.service.JobService;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Service
public class SysUserImportService extends BaseService<Object> {
    @Resource
    private SysUserImportDao sysUserImportDao;
    @Resource
    private SysUserService userService;
    @Resource
    private SysOrgService sysOrgService;
    @Resource
    private UserPositionService userPositionService;
    @Resource
    private JobService jobService;
    @Resource
    private PositionService positionService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private UserCustomService userCustomService;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private SysUserDao sysUserDao;
    @Resource
    private ModuleManageDao moduleManageDao;

    @Override
    protected IEntityDao<Object, Long> getEntityDao() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Description : 导入用户
     * Author : XX
     * Date : 2019年7月15日 上午10:58:01
     * Return : ResultMessage
     */
    public ResultMessage importSysUser(InputStream inputStream) throws Exception {
        ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, "用户导入成功");
        //用于记录导出校验信息
        StringBuffer importInfo = new StringBuffer();

        ExcelReader excel = new ExcelReader();
        // 读出execl
        TableEntity tableEntity = excel.readFile(inputStream);
        // 校验excel信息
        importInfo = checkUser(tableEntity, importInfo);
        if (!importInfo.toString().equals("")) {
            resultMessage = new ResultMessage(ResultMessage.Fail, importInfo.toString());
            return resultMessage;
        }
        if (tableEntity.getDataEntityList().size() == 0) {
            importInfo.append("此表未有数据导入！\r\n");
            resultMessage = new ResultMessage(ResultMessage.Fail, importInfo.toString());
        }
        this.addUser(tableEntity, importInfo, resultMessage);
        return resultMessage;
    }
    
    /**
     * Description : 新增系统用户及其组织、岗位、职务
     * Author : XX
     * Date : 2019年7月15日 上午11:06:05
     * Return : void
     */
	private void addUser(TableEntity tableEntity, StringBuffer importInfo,ResultMessage resultMessage) {
		int rowNum = 2;
        for (DataEntity dataEntity : tableEntity.getDataEntityList()) {
            List<FieldEntity> list = dataEntity.getFieldEntityList();
            Map<String, Object> userInfoMap = new HashMap<>();
            // 对字段名称进行遍历
            for (FieldEntity fieldEntity : list) {
                String name = fieldEntity.getName();
                String value = fieldEntity.getValue();
                userInfoMap.put(name, value);
            }
            String username = CommonTools.Obj2String(userInfoMap.get("帐号"));
            SysUser sysUser = userService.getByUsername(username);
            Boolean flag = true;
            try {
                flag = saveSysUser(userInfoMap, sysUser);
            } catch (Exception e) {
                resultMessage = new ResultMessage(ResultMessage.Fail, e.getCause().getMessage());
                System.out.println("--------------------by XX-----------------错误信息为:" + e.getCause().getMessage() + ","
                        + "当前输出的所在类为:SysUserImportService.addUser()");
            }
            if (!importInfo.toString().contains("导入结果如下：")) {
                importInfo.append("---------------------导入结果如下：-------------------------\r\n");
            }
            if (!flag) {
                importInfo.append("第").append(rowNum).append("行记录插入失败！\r\n");
                resultMessage = new ResultMessage(ResultMessage.Fail, importInfo.toString());
            }
            rowNum++;
        }
		
	}
	
	/**
	 * Description : 新增或者更新人员表信息
	 * Author : XX
	 * Date : 2019年7月15日 上午11:07:32
	 * Return : Boolean
	 */
	private Boolean saveSysUser(Map<String, Object> userInfoMap, SysUser sysUser) {
		
		String username = CommonTools.Obj2String(userInfoMap.get("帐号"));
        String fullname = CommonTools.Obj2String(userInfoMap.get("姓名"));
        String sex = CommonTools.Obj2String(userInfoMap.get("性别"));
        //默认角色
        String roleName = ISysUserConst.DEFAULT_ROLE_NAME;

        boolean flag = true;
        boolean isAdd = false;
        //判断用户是否存在
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setUserId(UniqueIdUtil.genId());
            isAdd = true;
        }
        sysUser.setUsername(username);
        sysUser.setFullname(fullname);
        if (sex != "") {
            if ("男".equals(sex)) {
                sysUser.setSex((short) 1);
            } else {
                sysUser.setSex((short) 0);
            }
        } else {
            sysUser.setSex((short) 1);
        }
        sysUser.setDelFlag((short) 0);
        //状态
        sysUser.setStatus((short) 1);
        //锁定状态
        sysUser.setLockState((short) 0);
        //登录失败次数
        sysUser.setLoginFailures("0");
        try {
            if (isAdd) {
                //添加数据库
                String enPassword = PasswordUtil.generatePassword("123456");
                sysUser.setPassword(enPassword);
                userService.add(sysUser);
                //添加用户自定义
                UserCustom userCustom = new UserCustom();
                userCustom.setUserId(sysUser.getUserId());
                userCustom.setCustomInfo("{}");
                userCustomService.add(userCustom);
                //提示信息
                System.out.println(sysUser.getFullname()+" 用户新增成功");
            } else {
                //更新数据库
                userService.update(sysUser);
                System.out.println(sysUser.getFullname()+" 用户更新成功");
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        //保存角色
        try {
            saveSysRole(roleName, sysUser);
        }catch(Exception e){
            System.out.println("--------------------by XX-----------------错误信息为:" +e.getCause().getMessage()+ ","
                    + "当前输出的所在类为:SysUserImportService.saveSysUser()");
            e.printStackTrace() ;
        }
        try {
            //保存所属组织、岗位、职务
            saveSysOrg(userInfoMap, sysUser);
        } catch (Exception e) {
            flag = false;
            System.out.println("--------------------by XX-----------------错误信息为:" + e.getCause().getMessage() + ","
                    + "当前输出的所在类为:SysUserImportService.addUser()");
            e.printStackTrace();
        }
        return flag;
	}
	
	/**
	 * Description : 添加或者更新所属组织、职务、岗位信息
	 * Author : XX
	 * Date : 2019年7月15日 上午11:14:30
	 * Return : void
	 */
	private void saveSysOrg(Map<String, Object> userInfoMap, SysUser sysUser) {
		String orgs = CommonTools.Obj2String(userInfoMap.get("部门"));
        String jobname = CommonTools.Obj2String(userInfoMap.get("岗位"));
        //用户ID
        Long userID = sysUser.getUserId();
        //处理组织信息 start
        if (orgs != "") {
        //添加职务
            //职务id
            String jobId = null;
            //岗位id
            String posId = null;
            if (jobname != "") {
                jobId = saveJob(jobname);
            }
            //组织信息
            SysOrg sysOrg = null;
            List<SysOrg> sysOrgList = sysOrgService.getByOrgName(orgs);
            if (sysOrgList != null && sysOrgList.size() > 0) {
                sysOrg = sysOrgList.get(0);
            }
            //组织不存在
            if (sysOrg == null) {
                System.out.println(sysUser.getFullname()+"用户所属部门不存在");
            } else {
                //组织
                Long orgId = sysOrg.getOrgId();
                if (jobId != null) {
                    String jobCode = PinyinUtil.getPinYinHeadChar(jobname);
                    String orgCode = PinyinUtil.getPinYinHeadChar(orgs);
                    String posCode = orgCode + "_" + jobCode;
                    String posName = orgs + "_" + jobname;
                    //组织岗位id
                    posId = savePosition(orgs, orgId, posCode, posName, jobId);
                }
                UserPosition position = null;
                List<UserPosition> userposList = userPositionService.getByUserId(userID);
                //用户是否已有组织
                if (userposList != null && userposList.size() > 0) {
                    boolean isNoExist = true;
                    for (UserPosition userPosition : userposList) {
                        String pos_Id = String.valueOf(userPosition.getPosId());
                        if (posId.equals(pos_Id)) {
                            isNoExist = false;
                            break;
                        }
                    }
                    //不存在该岗位
                    if (isNoExist) {
                        position = new UserPosition();

                        position.setUserPosId(UniqueIdUtil.genId());
                        position.setOrgId(orgId);
                        position.setUserId(userID);
                        if (posId != null && jobId != null) {
                            position.setPosId(Long.valueOf(posId));
                            position.setJobId(Long.valueOf(jobId));
                        }
                        //非主岗位
                        position.setIsPrimary((short) 0);
                        //主负责人
                        position.setIsCharge((short) 0);
                        userPositionService.add(position);
                    }
                } else {
                    position = new UserPosition();

                    position.setUserPosId(UniqueIdUtil.genId());
                    position.setOrgId(orgId);
                    position.setUserId(userID);
                    if (posId != null && jobId != null) {
                        position.setPosId(Long.valueOf(posId));
                        position.setJobId(Long.valueOf(jobId));
                    }
                    //主岗位
                    position.setIsPrimary((short) 1);
                    //主负责人
                    position.setIsCharge((short) 0);
                    userPositionService.add(position);
                }
            }
        }
		
	}
	
	/**
	 * Description : 新增或者更新岗位
	 * Author : XX
	 * Date : 2019年7月15日 下午1:40:57
	 * Return : String
	 */
	private String savePosition(String orgs, Long orgId, String posCode,String posName, String jobId) {
		Position position = new Position();
        String id = null;
        boolean isAdd = false;
        if (this.positionService.isPoscodeUsed(posCode)) {
            isAdd = true;
        } else {
            boolean isExistposName = sysUserImportDao.isExistposName(posName);
            if (!isExistposName) {
                isAdd = true;
                posCode = posCode + UniqueIdUtil.genId();
            } else {
                String sql = "select * from cwm_sys_pos where POSNAME='" + posName + "' and isDelete=0";
                Map<String, Object> map = jdbcTemplate.queryForMap(sql);
                if (map != null) {
                    id = CommonTools.Obj2String(map.get("POSID"));
                }
            }
        }
        if (isAdd) {
            position.setPosId(Long.valueOf(UniqueIdUtil.genId()));
            position.setPos_creatorId(UserContextUtil.getCurrentUserId());
            position.setPos_createTime(new Date());
            position.setPos_updateId(UserContextUtil.getCurrentUserId());
            position.setPos_updateTime(new Date());
            position.setOrgId(orgId);
            position.setPosCode(posCode);
            position.setPosName(posName);
            position.setJobId(Long.valueOf(jobId));
            this.positionService.add(position);
            id = position.getPosId().toString();
        }
        return id;
	}

	/**
	 * Description : 新增或者更新职务
	 * Author : XX
	 * Date : 2019年7月15日 上午11:15:43
	 * Return : String
	 */
	private String saveJob(String jobname) {
		Job job = new Job();
        String id = null;
        job.setJobid(Long.valueOf(UniqueIdUtil.genId()));
        String jobCode = PinyinUtil.getPinYinHeadChar(jobname);
        //是否新增
        boolean isAdd = false;
        boolean isExistjobCode = jobService.isExistJobCode(jobCode);
        boolean isExistJobName = sysUserImportDao.isExistJobName(jobname);
        if (!isExistjobCode) {
            isAdd = true;
        } else {
            if (!isExistJobName) {
                isAdd = true;
                jobCode = jobCode + UniqueIdUtil.genId();
            } else {
                id = jobService.getByJobCode(jobCode).getJobid().toString();
                String sql = "select * from cwm_sys_job where jobname='" + jobname + "' and isDelete=0";
                Map<String, Object> map = jdbcTemplate.queryForMap(sql);
                if (map != null) {
                    id = CommonTools.Obj2String(map.get("JOBID"));
                }
            }
        }
        if (isAdd) {
            job.setJob_creatorId(UserContextUtil.getCurrentUserId());
            job.setJob_createTime(new Date());
            job.setJob_updateId(UserContextUtil.getCurrentUserId());
            job.setJob_updateTime(new Date());
            job.setJobname(jobname);
            job.setJobcode(jobCode);
            job.setJobdesc(jobname);
            this.jobService.add(job);
            id = job.getJobid().toString();
        }
        return id;
	}

	/**
	 * Description : 添加默认角色
	 * Author : XX
	 * Date : 2019年7月15日 上午11:13:10
	 * Return : void
	 * @throws Exception 
	 */
	private void saveSysRole(String roleName, SysUser sysUser) throws Exception {
		//判断有无该角色
        List<SysRole> sysRoleList = sysRoleService.getByRoleName(roleName);
        SysRole sysRole = null;
        if (sysRoleList != null && sysRoleList.size() > 0) {
            sysRole = sysRoleList.get(0);
        }
        //角色存在
        if (sysRole != null) {
            Long sysRoleId = sysRole.getRoleId();
            Long sysUserId = sysUser.getUserId();
            Long[] sysUserIds = new Long[1];
            sysUserIds[0] = sysUserId;
            userRoleService.add(sysRoleId,sysUserIds);
        }else{
            //角色不存在则过滤
        }
	}

	/**
	 * Description : 校验导入的人员excel必填信息
	 * Author : XX
	 * Date : 2019年7月15日 上午11:00:16
	 * Return : StringBuffer
	 */
	private StringBuffer checkUser(TableEntity tableEntity,StringBuffer importInfo) {
		
		int rowNum = 2;
        for (DataEntity dataEntity : tableEntity.getDataEntityList()) {
            String fieldName = "";
            List<FieldEntity> list = dataEntity.getFieldEntityList();
            // 对字段名称进行遍历
            for (FieldEntity fieldEntity : list) {
                String name = fieldEntity.getName();
                String value = fieldEntity.getValue();
                fieldName += name + ",";
                if ("帐号".equals(name) || "姓名".equals(name) || "性别".equals(name) || "部门".equals(name) || "岗位".equals(name)) {

                } else {
                    return importInfo.append("列名不正确，请确保列名为以下其中一个：【帐号】、【姓名】、【性别】、【部门】、【岗位】");
                }
                if ("帐号".equals(name) && "".equals(value)) {
                    if (!importInfo.toString().contains("必填校验结果如下：")) {
                        importInfo.append("--------必填校验结果如下：--------\r\n");
                    }
                    return importInfo.append("第").append(rowNum).append("行").append("（列名为" + name + "）,不能为空！\r\n");
                }
                if ("姓名".equals(name) && "".equals(value)) {
                    if (!importInfo.toString().contains("必填校验结果如下：")) {
                        importInfo.append("--------必填校验结果如下：--------\r\n");
                    }
                    return importInfo.append("第").append(rowNum).append("行").append("（列名为" + name + "）,不能为空！\r\n");
                }
            }
            rowNum++;
        }
        return importInfo;
    }

    /**
     * @Description  excel导入型号团队  导入格式:权限,姓名
     * @Author ZMZ
     * @Date 2020/12/16 15:34
     * @param inputStream
     * @param xhId 型号id
     * @Return java.lang.String
     */
    public String importMuduleTeam(InputStream inputStream, String xhId) throws IOException, InvalidFormatException {
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
            //表头格式校验
            switch (j){
                //权限,姓名,部门
                case 0:if (!"权限".equals(data)){
                    return "{\"success\":\"false\",\"context\":\"表格内容需严格符合以下顺序:权限-姓名-部门\"}";
                }
                    break;

                case 1:if (!"姓名".equals(data)){
                    return "{\"success\":\"false\",\"context\":\"表格内容需严格符合以下顺序:权限-姓名-部门\"}";
                }
                    break;
                default:
                    return "{\"success\":\"false\",\"context\":\"未知错误,请检查模板并联系工程师\"}";
            }
        }

        //开始取数据
        for (int i=1;i<=totalRow;i++){
            //每一行都是一个独立的团队人员信息
            //表格格式  权限,姓名
            teamInfo=new HashMap<>();
            for (int j=0;j<totalColumn;j++){

                try{
                    sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                }catch (NullPointerException e){
                    if (j==0){
                        //当前取的是权限
                        e.printStackTrace();
                        return "{\"success\":\"false\",\"context\":\"第"+(i+1)+"行,第"+(j+1)+"列为空！\"}";
                    }
                }

                String data = sheet.getRow(i).getCell(j).getStringCellValue();
                switch (j){
                    //表格格式  权限,姓名
                    case 0:
                        if ("管理人员".equals(data)||"团队人员".equals(data)){
                            teamInfo.put("authority","管理人员".equals(data)?"manage":"team");
                        }
                        break;
                    case 1:
                        List<? extends ISysUser> sysUser=sysUserDao.getByFullname(data);
                        if (sysUser.size()==0){
                            return "{\"success\":\"false\",\"context\":\""+data+" 用户不存在\"}";
                        }else {
                            teamInfo.put("userId",sysUser.get(0).getUserId().toString());
                            teamInfo.put("userName",data);
                        }
                        break;
                }
            }

            //遍历完一行信息了,该存到数据库了
            //去重
            boolean ifUserAlreadyInCurModule=false;
            List<String>  curModuleUser=moduleManageDao.getByModuleIdDic(xhId);
            for (String userNameAlreadyExists:curModuleUser){
                if (teamInfo.get("userId").equals(userNameAlreadyExists)){
                    ifUserAlreadyInCurModule=true;
                }
            }
            if (!ifUserAlreadyInCurModule){
                moduleManageDao.insert(xhId,teamInfo.get("userId"),teamInfo.get("userName"),
                        teamInfo.get("authority"));
            }
        }
        jsonObject.put("success","true");

        return jsonObject.toString();
    }
   
}
