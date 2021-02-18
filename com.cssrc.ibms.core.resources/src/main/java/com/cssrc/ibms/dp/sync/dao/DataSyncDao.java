package com.cssrc.ibms.dp.sync.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.dp.form.dao.ProductTypeDao;
import com.cssrc.ibms.dp.sync.bean.ConditionBean;
import com.cssrc.ibms.dp.sync.bean.DataPakageFile;
import com.cssrc.ibms.dp.sync.bean.Mmc;
import com.cssrc.ibms.dp.sync.bean.Project;
import com.cssrc.ibms.dp.sync.bean.SignBean;
import com.cssrc.ibms.dp.sync.bean.TaskList;
import com.cssrc.ibms.dp.sync.bean.TasksBean;
import com.cssrc.ibms.dp.sync.model.SyncUser;
import com.cssrc.ibms.dp.sync.util.SyncBaseDao;
import com.cssrc.ibms.dp.sync.util.SyncBaseFilter;

import net.sf.json.JSONArray;

@Repository
public class DataSyncDao extends SyncBaseDao {
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    ProductTypeDao productTypeDao;

    /**
     * 根据用户登陆名获取用户ID
     *
     * @param userName
     * @return
     */
    public String getUserIdByUserName(String userName) {
        String userId = "";
        String sql = "SELECT USERID FROM CWM_SYS_USER WHERE USERNAME ='" + userName + "'";
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sql, null);
        for (Map<String, Object> map : dataList) {
            userId = CommonTools.Obj2String(map.get("USERID"));
        }
        return userId;
    }

    /**
     * @param
     * @return
     * @Description: 根据broPathId查询 W_CPB
     * @author qiaozhili
     * @date 2019/3/9 15:03
     */
    public List<Map<String, Object>> getCpByPathId(String pathId) {
        String sql = "SELECT * FROM W_CPB where ID = " + pathId;
        List<Map<String, Object>> packageList = jdbcDao.queryForList(sql, null);
        return packageList;
    }

    public Long insertCPB(String path, String pathId, String bropathId, String broTaskId, String taskId, String chId) throws Exception{
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into W_CPB (ID, F_SSCPPC, F_SSCPLB, F_CPLBDH, F_CPPCDH, F_CPMC, F_CPBH, F_SSXH, F_XHJD,");
        sql.append(" F_SSXHFC, F_SSXHPC, F_SSXHJD, F_SSPCCH, F_PCCHBH) ");
        sql.append(" values (:ID,:F_SSCPPC,:F_SSCPLB,:F_CPLBDH,:F_CPPCDH,:F_CPMC,:F_CPBH,:F_SSXH,:F_XHJD,");
        sql.append(" :F_SSXHFC,:F_SSXHPC,:F_SSXHJD,:F_SSPCCH,:F_PCCHBH)");
        Map map = new HashMap();
        map.put("ID", pathId);
        map.put("F_SSCPPC", bropathId);
        map.put("F_SSCPLB", "");
        map.put("F_CPLBDH", "");
        map.put("F_CPPCDH", path);
        map.put("F_CPMC", path);
        map.put("F_CPBH", path);
        map.put("F_SSXH", "");
        map.put("F_XHJD", "");
        map.put("F_SSXHFC", "");
        map.put("F_SSXHPC", "");
        map.put("F_SSXHJD", "");
        map.put("F_SSPCCH", chId);
        map.put("F_PCCHBH", "");
        jdbcDao.exesql(sql.toString(), map);
        return Long.valueOf(map.get("ID").toString());
    }

    /**
     * @Description: 根据broPathId查询 W_DATAPACKAGEINFO
     * @author qiaozhili
     * @date 2019/3/18 10:36
     * @param
     * @return
     */
    public List<Map<String, Object>> getDataPackageInfoByPathId(String broTaskId) {
        String sql = "SELECT * FROM W_DATAPACKAGEINFO where F_SSMB = " + broTaskId;
        List<Map<String, Object>> dataPackageInfoList = jdbcDao.queryForList(sql, null);
        return dataPackageInfoList;
    }

    public Long dataPackageInfoInsert(Map<String, Object> map) {
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into W_DATAPACKAGEINFO ");
        sql.append(" (ID,F_SJMC,F_SJLX,F_SJZ,F_GW,F_MJ,F_BMQX,F_SCR,F_SCRID,F_SCSJ,F_BB,F_SM,F_SPJD,F_ZXZT,F_WCSJ,F_SSSJB,F_SSMB,F_SSMBMC,F_SSRW)");
        sql.append(" values ");
        sql.append(" (:ID,:F_SJMC,:F_SJLX,:F_SJZ,:F_GW,:F_MJ,:F_BMQX,:F_SCR,:F_SCRID,:F_SCSJ,:F_BB,:F_SM,:F_SPJD,:F_ZXZT,:F_WCSJ,:F_SSSJB,:F_SSMB,:F_SSMBMC,:F_SSRW) ");
        jdbcDao.exesql(sql.toString(), map);
        return Long.valueOf(map.get("F_SSSJB").toString());
    }

    public List<Map<String, Object>> getDataPackageInfoId() {
        String sql = "SELECT ID FROM W_DATAPACKAGEINFO";
        List<Map<String, Object>> dataPackageInfoIdList = jdbcDao.queryForList(sql, null);
        return dataPackageInfoIdList;
    }

    /**
     * @Description: 根据broTaskId查询 W_TABLEB_TEMP
     * @author qiaozhili
     * @date 2019/3/18 10:36
     * @param
     * @return
     */
    public List<Map<String, Object>> getTbInstanceByBroTaskId(String broTaskId) {
        String sql = "SELECT * FROM W_TABLE_TEMP where ID = " + broTaskId;
        List<Map<String, Object>> packageList = jdbcDao.queryForList(sql, null);
        return packageList;
    }

    /**
     * @Description: 根据broTaskId查询 W_SIGNRESULT
     * @author qiaozhili
     * @date 2019/3/20 9:41
     * @param
     * @return
     */
    public List<Map<String, Object>> getSignResultByBroTaskId(String broTaskId) {
        String sql = "SELECT * FROM W_SIGNRESULT where F_TB_INSTANT_ID = " + broTaskId;
        List<Map<String, Object>> signResultList = jdbcDao.queryForList(sql, null);
        return signResultList;
    }

    /**
     * @Description: 根据broTaskId查询 W_CONDI_RES
     * @author qiaozhili
     * @date 2019/3/20 9:45
     * @param
     * @return
     */
    public List<Map<String, Object>> getCondiResByBroTaskId(String broTaskId) {
        String sql = "SELECT * FROM W_CONDI_RES where F_TB_INSTAN_ID = " + broTaskId;
        List<Map<String, Object>> condiResList = jdbcDao.queryForList(sql, null);
        return condiResList;
    }

    /**
     * 获取所有的用户
     *
     * @return
     */
    public List<SyncUser> getAllUser() {
        List<SyncUser> ret = new ArrayList<SyncUser>();
        String sql = "SELECT * FROM CWM_SYS_USER ";
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sql, null);
        for (Map<String, Object> map : dataList) {
            SyncUser syncUser = new SyncUser();
            syncUser.setId(Long.valueOf(CommonTools.Obj2String(map.get("USERID"))));
            syncUser.setUserName(CommonTools.Obj2String(map.get("USERNAME")));
            syncUser.setDisplayName(CommonTools.Obj2String(map.get("FULLNAME")));
            syncUser.setPassWord(CommonTools.Obj2String(map.get("PASSWORD")));
            syncUser.setEmail(CommonTools.Obj2String(map.get("EMAIL")));
            ret.add(syncUser);
        }
        return ret;
    }

    /**
     * @Description: 获取admin账户
     * @author qiaozhili
     * @date 2020/9/30 11:26
     * @param
     * @return
     */
    public List<SyncUser> getAdminUser() {
        List<SyncUser> ret = new ArrayList<SyncUser>();
        String sql = "SELECT * FROM CWM_SYS_USER WHERE USERNAME = 'admin'";
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sql, null);
        for (Map<String, Object> map : dataList) {
            SyncUser syncUser = new SyncUser();
            syncUser.setId(Long.valueOf(CommonTools.Obj2String(map.get("USERID"))));
            syncUser.setUserName(CommonTools.Obj2String(map.get("USERNAME")));
            syncUser.setDisplayName(CommonTools.Obj2String(map.get("FULLNAME")));
            syncUser.setPassWord(CommonTools.Obj2String(map.get("PASSWORD")));
            syncUser.setEmail(CommonTools.Obj2String(map.get("EMAIL")));
            ret.add(syncUser);
        }
        return ret;
    }

    /**
     * @param
     * @return
     * @Description: 查询所有工作队中所用户ID，并根据用户ID查询所有用户信息
     * @author qiaozhili
     * @date 2019/2/20 9:52
     */

    public List<SyncUser> getGroupUsers(String userID) {
        List<SyncUser> ret = new ArrayList<SyncUser>();
        String sql = "SELECT * FROM W_CPYSZB WHERE F_XMID = " + userID;
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sql, null);
        List<String> userIds = new ArrayList<>();
        for (Map<String, Object> map : dataList) {
            String CPID = CommonTools.Obj2String(map.get("F_SSCP"));
            if (CPID != null && !CPID.equals("")) {
                List<String> userIDList = getAllUserIDByCPCHId(CPID);
                for (String id : userIDList) {
                    if (!userIds.contains(id)) {
                        userIds.add(id);
                    }
                }
            } else {
                String BCID = CommonTools.Obj2String(map.get("F_SSBCCH"));
                List<String> userIDList = getAllUserIDByBCCHId(BCID);
                for (String id : userIDList) {
                    if (!userIds.contains(id)) {
                        userIds.add(id);
                    }
                }
            }
        }
        for (int i = 0; i < userIds.size(); i++) {
            String sql1 = "SELECT * FROM CWM_SYS_USER WHERE USERID = " + userIds.get(i);
            List<Map<String, Object>> dataList1 = jdbcDao.queryForList(sql1, null);
            if (dataList1.size() != 0) {
                SyncUser syncUser = new SyncUser();
                syncUser.setId(Long.valueOf(CommonTools.Obj2String(dataList1.get(0).get("USERID"))));
                syncUser.setUserName(CommonTools.Obj2String(dataList1.get(0).get("USERNAME")));
                syncUser.setDisplayName(CommonTools.Obj2String(dataList1.get(0).get("FULLNAME")));
                syncUser.setPassWord(CommonTools.Obj2String(dataList1.get(0).get("PASSWORD")));
                syncUser.setEmail(CommonTools.Obj2String(dataList1.get(0).get("EMAIL")));
                ret.add(syncUser);
            }
        }
        return ret;
    }
    /**
     * @Description:
     * @author qiaozhili
     * @date 2020/9/24 20:31
     * @param
     * @return
     */
    public List<String> getAllUserIDByCPCHId(String CPID) {
        List<String> ret = new ArrayList<String>();
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT F_XMID FROM W_cpyszb T  WHERE ");//
        sqlSB.append("F_SSCP = '" + CPID+"'");
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        for (Map<String, Object> map : dataList) {
            if(map.get("F_XMID")!=null&&map.get("F_XMID")!="") {
                ret.add(CommonTools.Obj2String(map.get("F_XMID")));
            }

        }
        return ret;
    }
    public List<String> getAllUserIDByBCCHId(String BCID) {
        List<String> ret = new ArrayList<String>();
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT F_XMID FROM W_cpyszb T  WHERE ");//
        sqlSB.append("F_SSBCCH = '" + BCID+"'");
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        for (Map<String, Object> map : dataList) {
            if(map.get("F_XMID")!=null&&map.get("F_XMID")!="") {
                ret.add(CommonTools.Obj2String(map.get("F_XMID")));
            }

        }
        return ret;
    }

    /**
     * 根据用户ID查询到该用户所属的任务信息
     */
    public List<Project> getProjectByUserID(Long id) {
        //
        List<Project> ret = new ArrayList<Project>();
        StringBuffer sqlSB = new StringBuffer();
        sqlSB.append("SELECT * FROM W_CPYSCHBGB WHERE ");//发次的ID在数据包结构树下
        sqlSB.append("F_YSZZZID  = '" + id + "'");//
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        for (Map<String, Object> map : dataList) {
            Project project = new Project();
            project.setProjectId(Long.valueOf(CommonTools.Obj2String(map.get("ID"))));
            project.setProjectName(CommonTools.Obj2String(map.get("F_CHBGBBH")));
            project.setSscpmc(CommonTools.Obj2String(map.get("F_CPPC")));
            project.setSscppc(CommonTools.Obj2String(map.get("F_SSCPPC")));
            project.setSsxh(CommonTools.Obj2String(map.get("F_SSXHID")));
            ret.add(project);
        }
        return ret;
    }

    /**
     * @Description: 根据用户ID获取型号
     * @author qiaozhili
     * @date 2020/7/13 21:46
     * @param
     * @return
     */
    public String getXHDHByXHID(Long id) {
        String ret = "";
        StringBuffer sqlSB = new StringBuffer();
        sqlSB.append("SELECT F_XHDH FROM W_XHJBSXB WHERE ");//发次的ID在数据包结构树下
        sqlSB.append("ID  = '" + id + "'");
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        if (dataList.size() > 0) {
            ret = (CommonTools.Obj2String(dataList.get(0).get("F_XHDH")));
        } else {
            ret = "";
        }
        return ret;
    }

    /**
     * @Description: 根据用户获取用户ID
     * @author qiaozhili
     * @date 2020/9/24 20:18
     * @param
     * @return
     */
    public String getUserIDByUsername(String userName) {
        String ret = "";
        StringBuffer sqlSB = new StringBuffer();
        sqlSB.append("SELECT USERID FROM CWM_SYS_USER WHERE ");//发次的ID在数据包结构树下
        sqlSB.append("USERNAME  = '" + userName + "'");
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        if (dataList.size() > 0) {
            ret = (CommonTools.Obj2String(dataList.get(0).get("USERID")));
        } else {
            ret = "";
        }
        return ret;
    }

    /**
     * @Description: 查询靶场策划信息
     * @author qiaozhili
     * @date 2020/9/21 16:38
     * @param
     * @return
     */
    public List<Map<String, Object>> getBCCHByUserID(Long id) {
        List<String> ret = new ArrayList<String>();
        StringBuffer sqlSB = new StringBuffer();
        sqlSB.append("SELECT * FROM W_BCRWCHBGB WHERE ");//发次的ID在数据包结构树下
        sqlSB.append("ID  = '" + id + "'");
        sqlSB.append("and F_SPZT  = '审批通过'");
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        return dataList;
    }

    /**
     * @Description: 根据策划Dlist获取产品信息
     * @author qiaozhili
     * @date 2020/7/13 22:14
     * @param
     * @return
     */
    public List<Project> getProjectByCHID(List<String> CHIDList) {
        List<Project> ret = new ArrayList<Project>();
        for (String CHID : CHIDList) {
            StringBuffer sqlSB = new StringBuffer();
            sqlSB.append("SELECT * FROM W_CPYSCHBGB WHERE ");//发次的ID在数据包结构树下
            sqlSB.append("ID  = '" + CHID + "'");//
            List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
            for (Map<String, Object> map : dataList) {
                Project project = new Project();
                project.setProjectId(Long.valueOf(CommonTools.Obj2String(map.get("ID"))));
                project.setProjectName(CommonTools.Obj2String(map.get("F_CHBGBBH")));
                project.setSscpmc(CommonTools.Obj2String(map.get("F_CPPC")));
                project.setSscppc(CommonTools.Obj2String(map.get("F_SSCPPC")));
                project.setSsxh(CommonTools.Obj2String(map.get("F_SSXHID")));
                ret.add(project);
            }
        }
        return ret;
    }

    /**
     * @Description: 根据用户策划ID获取策划策划信息
     * @author qiaozhili
     * @date 2020/7/13 21:46
     * @param
     * @return
     */
    public List<Project> getCHByUserCHID(Long id) {
        //
        List<Project> ret = new ArrayList<Project>();
        StringBuffer sqlSB = new StringBuffer();
        sqlSB.append("SELECT * FROM W_CPYSCHBGB WHERE ");//发次的ID在数据包结构树下
        sqlSB.append("F_YSZZZID  = '" + id + "'");//
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        for (Map<String, Object> map : dataList) {
            Project project = new Project();
            project.setProjectId(Long.valueOf(CommonTools.Obj2String(map.get("ID"))));
            project.setProjectName(CommonTools.Obj2String(map.get("F_CHBGBBH")));
            project.setSscpmc(CommonTools.Obj2String(map.get("F_CPPC")));
            project.setSscppc(CommonTools.Obj2String(map.get("F_SSCPPC")));
            project.setSsxh(CommonTools.Obj2String(map.get("F_SSXHID")));
            ret.add(project);
        }
        return ret;
    }

    /**
     * 根据用户名ID获取所有工作队的Id
     *
     * @param userId
     * @return
     */
    public List<Long> getTestTeamIDByUserId(String userId) {
        List<Long> ret = new ArrayList<Long>();
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT ID FROM W_WorkTeam T  WHERE ");//
        sqlSB.append("F_CYID like '" + userId + "'");//只有一个人
        sqlSB.append(" Or F_CYID  like '" + userId + ",%'");//第一个人
        sqlSB.append(" Or F_CYID  like '%," + userId + ",%'");//中间一个人
        sqlSB.append(" Or F_CYID  like '%," + userId + "'");//最后一个人
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        for (Map<String, Object> map : dataList) {
            ret.add(Long.valueOf(CommonTools.Obj2String(map.get("ID"))));
        }
        return ret;
    }

    /**
     * @Description: 根据人员ID，查询W_cpyszb获取所有验收策划ID
     * @author qiaozhili
     * @date 2020/2/26 10:03
     * @param
     * @return
     */

    public List<Long> getAllYSCHIDByUserId(String userId) {
        List<Long> ret = new ArrayList<Long>();
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT F_SSCP FROM W_cpyszb T  WHERE ");//
        sqlSB.append("F_XMID = '" + userId+"'");
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        for (Map<String, Object> map : dataList) {
        	if(map.get("F_SSCP")!=null&&map.get("F_SSCP")!="") {
        		ret.add(Long.valueOf(CommonTools.Obj2String(map.get("F_SSCP"))));
        	}
            
        }
        return ret;
    }
    /**
     * @Description: 根据人员ID，查询W_cpyszb获取所有靶场策划ID
     * @author qiaozhili
     * @date 2020/9/24 16:49
     * @param
     * @return
     */
    public List<Long> getAllBCCHIDByUserId(String userId) {
        List<Long> ret = new ArrayList<Long>();
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT F_SSBCCH FROM W_cpyszb T  WHERE ");//
        sqlSB.append("F_XMID = '" + userId+"'");
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        for (Map<String, Object> map : dataList) {
            if(map.get("F_SSBCCH")!=null&&map.get("F_SSBCCH")!="") {
                ret.add(Long.valueOf(CommonTools.Obj2String(map.get("F_SSBCCH"))));
            }

        }
        return ret;
    }

    /**
     * @Description: 根据获取的所有验收策划ID，筛选出已审批的验收策划ID
     * @author qiaozhili
     * @date 2020/2/26 10:21
     * @param
     * @return
     */

    public String getYSCHIDById(String YSCHId) {
        String ret = null;
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT F_SPZT FROM W_CPYSCHBGB T  WHERE ");//
        sqlSB.append("ID = " + YSCHId);
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        if (dataList.size() > 0) {
            ret = (CommonTools.Obj2String(dataList.get(0).get("F_SPZT")));
        } else {
            ret = "";
        }
        return ret;
    }

    /**
     * @Description:
     * @author qiaozhili
     * @date 2020/9/24 16:57
     * @param
     * @return
     */
    public List<Map<String, Object>> getXHIDByCHID(String YSCHId) {
        String ret = null;
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT * FROM W_CPYSCHBGB T  WHERE ");//
        sqlSB.append("ID = " + YSCHId);
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        if (dataList.size() > 0) {
            ret = (CommonTools.Obj2String(dataList.get(0).get("F_SSXHID")));
        } else {
            ret = "";
        }
        return dataList;
    }

    /**
     * @Description: 根据策划ID查询Datapackageinfo表中实例
     * @author qiaozhili
     * @date 2020/9/24 21:36
     * @param
     * @return
     */
    public List<Map<String, Object>> getTaskByCHID(String YSCHId) {
        String ret = null;
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT * FROM W_DATAPACKAGEINFO T  WHERE ");//
        sqlSB.append("F_ACCEPTANCEPLANID = " + YSCHId);
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        return dataList;
    }

    /**
     * @Description: 靶场 武器所检策划
     * @author qiaozhili
     * @date 2020/9/23 14:14
     * @param
     * @return
     */
    public List<Map<String, Object>> getBCCHIDById(String userId) {
        String ret = null;
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT * FROM W_BCRWCHBGB T  WHERE ");//
        sqlSB.append("F_SYZZID = '" + userId);
        sqlSB.append("' and F_SPZT = '" + "审批通过'");
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        return dataList;
    }

    /**
     * @Description: 根据模板ID查询TaskId
     * @author qiaozhili
     * @date 2020/2/27 14:36
     * @param
     * @return
     */
    public String getHtmlByTempId(String instanId) {
        String ret = null;
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT * FROM W_TABLE_TEMP WHERE ");//
        sqlSB.append("ID = " + instanId);
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        ret = (CommonTools.Obj2String(dataList.get(0).get("F_CONTENTS")));
        return ret;
    }

    /**
     * @param
     * @return
     * @Description: 根据userId查询所负责节点id
     * @author qiaozhili
     * @date 2019/2/15 17:28
     */
    public List<Long> getPackageIdByUserId(String userId) {

        List<Long> ret = new ArrayList<Long>();
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT ID FROM W_PACKAGE T  WHERE ");
        sqlSB.append("F_FZRID like '" + userId + "'");
        sqlSB.append(" Or F_FZRID  like '" + userId + ",%'");
        sqlSB.append(" Or F_FZRID  like '%," + userId + ",%'");
        sqlSB.append(" Or F_FZRID  like '%," + userId + "'");
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        for (Map<String, Object> map : dataList) {
            ret.add(Long.valueOf(CommonTools.Obj2String(map.get("ID"))));
        }
        return ret;
    }

    /**
     * @param
     * @return
     * @Description: 通过userID和projectID，查询次用户在该发次中所负责的节点ID
     * @author qiaozhili
     * @date 2019/2/19 11:12
     */

    public List<Long> getPackageIdByUserIdAndProjectId(String userId, String projectId) {

        List<Long> ret = new ArrayList<Long>();
        StringBuffer sqlSB = new StringBuffer();
        sqlSB.append("SELECT * FROM ");
        sqlSB.append("(SELECT ID , F_SSFC FROM W_PACKAGE T  WHERE ");
        sqlSB.append("F_FZRID like '" + userId + "'");
        sqlSB.append(" Or F_FZRID  like '" + userId + ",%'");
        sqlSB.append(" Or F_FZRID  like '%," + userId + ",%'");
        sqlSB.append(" Or F_FZRID  like '%," + userId + "')");
        sqlSB.append(" AB WHERE AB.F_SSFC = '" + projectId + "'");

        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        for (Map<String, Object> map : dataList) {
            ret.add(Long.valueOf(CommonTools.Obj2String(map.get("ID"))));
        }
        return ret;
    }

    /**
     * 根据用户userName获取用户信息
     *
     * @param userName
     * @return
     */
    public SyncUser getUserInfoByUserName(String userName) {
        SyncUser syncUser = new SyncUser();
        String sql = "SELECT * FROM CWM_SYS_USER WHERE USERNAME ='" + userName + "'";
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sql, null);
        for (Map<String, Object> map : dataList) {
            syncUser.setId(Long.parseLong(CommonTools.Obj2String(map.get("USERID"))));
            syncUser.setUserName(CommonTools.Obj2String(map.get("USERNAME")));
            syncUser.setDisplayName(CommonTools.Obj2String(map.get("FULLNAME")));
            syncUser.setPassWord(CommonTools.Obj2String(map.get("PASSWORD")));
        }
        return syncUser;
    }

    /**
     * 根据工作队的Id获取
     *
     * @param userName
     * @return
     */
    public List<TaskList> getTaskIdByTestTeamID(String testTeamID) {
        List<TaskList> ret = new ArrayList<TaskList>();
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT * FROM W_DATAPACKAGEINFO  WHERE ");//
        sqlSB.append("F_ACCEPTANCEPLANID  = '" + testTeamID + "'");
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        for (Map<String, Object> map : dataList) {
            TaskList taskList = new TaskList();
            taskList.setInstanceId(CommonTools.Obj2String(map.get("ID")));
            taskList.setTempId(CommonTools.Obj2String(map.get("F_TEMPLATEID")));
            taskList.setState(CommonTools.Obj2String(map.get("F_ZXZT")));
            taskList.setPath(CommonTools.Obj2String(map.get("F_ZXZT")));
            ret.add(taskList);
        }
        return ret;
    }

    /***
     * 根据检查表实例的ID，获取签署的相关信息
     *
     * @param tableId
     * @return
     */
    public List<SignBean> getSignListByInstanceID(String tableId) {
        List<SignBean> ret = new ArrayList<SignBean>();
        List<Map<String, Object>> signBeanList = super.getListByTableNameAndFilter("W_SIGNDEF", Arrays.asList(new SyncBaseFilter("F_TABLE_TEMP_ID", "=", tableId)));
        for (Map<String, Object> map : signBeanList) {
            SignBean signBean = new SignBean();
            signBean.setSignId(CommonTools.Obj2String(map.get("ID")));
//            signBean.setRemark(CommonTools.Obj2String(map.get("F_REMARK")));
//            signBean.setTime(CommonTools.Obj2String(map.get("F_SIGNTIME")));
//			signBean.setPostid(CommonTools.Obj2String(map.get("STATE")));
//            signBean.setSigndef(CommonTools.Obj2String(map.get("F_SIGNDEF_ID")));
            ret.add(signBean);
        }

        return ret;
    }

    /**
     * 根据tb_Instanceid获取conditon
     *
     * @param tableId
     * @return
     */
    public List<ConditionBean> getConditionListByTbinstanId(String tableId) {
        List<ConditionBean> ret = new ArrayList<ConditionBean>();
        List<Map<String, Object>> conditionList = super.getListByTableNameAndFilter("W_CK_CONDITION", Arrays.asList(new SyncBaseFilter("F_TABLE_TEMP_ID", "=", tableId)));
        for (Map<String, Object> map : conditionList) {
            ConditionBean conditionBean = new ConditionBean();
            conditionBean.setConditionId(CommonTools.Obj2String(map.get("ID")));
//            conditionBean.setValuename(CommonTools.Obj2String(map.get("F_VALUE")));
//            conditionBean.setRef_conditionId(CommonTools.Obj2String(map.get("F_CONDITION_ID")));
            ret.add(conditionBean);
        }
        return ret;
    }

    /**
     * 根据tb_Instanceid获取检查实力taskbean
     *
     * @param tempId
     * @return
     */
    public TasksBean getTaskByInstanId(String tempId) {
        List<ConditionBean> ret = new ArrayList<ConditionBean>();
        TasksBean tasksBean = new TasksBean();
        List<Map<String, Object>> conditionList = super.getListByTableNameAndFilter("W_TABLE_TEMP", Arrays.asList(new SyncBaseFilter("ID", "=", tempId)));
        for (Map<String, Object> map : conditionList) {
            tasksBean.setTableinstanceId(CommonTools.Obj2String(map.get("ID")));
//            tasksBean.setVersion(CommonTools.Obj2String(map.get("F_VERSION")));
            tasksBean.setName(CommonTools.Obj2String(map.get("F_NAME")));
//            tasksBean.setIsfinished(CommonTools.Obj2String(map.get("F_STATUS")));
//            tasksBean.setStarttime(CommonTools.Obj2String(map.get("F_STARTTIME")));
//            tasksBean.setEndtime(CommonTools.Obj2String(map.get("F_ENDTIME")));
        }
        return tasksBean;
    }

    /**
     * 根据表单实例ID获取数据包详情中的状态
     *
     * @param tableInstanId
     * @return
     */
    public String getDataPackageStatus(Long tableInstanId) {
        String sql = "SELECT * FROM W_DATAPACKAGEINFO WHERE F_SSMB=" + tableInstanId;
        Map<String, Object> dpstatus = jdbcDao.queryForMap(sql, null);
        return dpstatus.get("F_ZXZT").toString();
    }

    public String getFileIdByDataId(String dataId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dataId", dataId);
        String sql = "SELECT　FILEID FROM CWM_SYS_FILE WHERE DATAID=:dataId";
        List<Map<String, Object>> file = jdbcDao.queryForList(sql, map);
        if (file.size() > 0) {
            return file.get(0).get("FILEID").toString();
        }
        return "";
    }

    public String getFilePathByFileId(String fileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fileId", fileId);
        String sql = "SELECT　FILEPATH FROM CWM_SYS_FILE WHERE FILEID=:fileId";
        List<Map<String, Object>> file = jdbcDao.queryForList(sql, map);
        if (file.size() > 0) {
            return file.get(0).get("FILEPATH").toString();
        }
        return "";
    }

    public List<Map<String, Object>> getCKResultByInsId(String taskId) {
        //根据表单实例ID区分检查结果表的类型
        Map typeMap = productTypeDao.getProductType(Long.valueOf(taskId));
        String ck_resultName = "";
        ck_resultName = "W_CK_RESULT_CARRY";
//        if ("空间".equals(typeMap.get("TYPE"))) {
//            ck_resultName = "W_CK_RESULT";
//        } else if ("运载".equals(typeMap.get("TYPE"))) {
//        } else {
//            ck_resultName = "W_CK_RESULT_JGJG";
//        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskId", taskId);
        String sql = "SELECT　* FROM " + ck_resultName + " WHERE F_TB_INSTAN=:taskId";
        return jdbcDao.queryForList(sql, map);
    }

    /**
     * 根据 TeamId 获取文件类型的信息
     *
     * @param ttId
     * @return
     */
    public List<Mmc> getDataPakageInfoByTeamId(String ttId) {
        List<Mmc> ret = new ArrayList<Mmc>();
        StringBuffer sqlSB = new StringBuffer();

        sqlSB.append("SELECT * FROM W_DATAPACKAGEINFO  WHERE ");//
        sqlSB.append("F_GW  = '" + ttId + "'");
        sqlSB.append(" AND (F_SJLX  like '" + "数据" + "'");
        sqlSB.append(" OR F_SJLX  like '" + "文件" + "'");
        sqlSB.append(" OR F_SJLX  like '" + "三维模型" + "'");
        sqlSB.append(" OR F_SJLX  like '" + "声像" + "')");
        sqlSB.append(" AND F_ZXZT  like '" + "待下载" + "'");
        List<Map<String, Object>> dataList = jdbcDao.queryForList(sqlSB.toString(), null);
        for (Map<String, Object> map : dataList) {

            String fileJson = CommonTools.Obj2String(map.get("F_SJZ"));
            JSONArray array = JSONArray.fromObject(fileJson);
            List<DataPakageFile> list = JSONArray.toList(array, DataPakageFile.class);
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Mmc mmc = new Mmc();
                    mmc.setGwId(ttId);
                    mmc.setId(CommonTools.Obj2Long(map.get("ID")));
                    mmc.setMmcId(CommonTools.Obj2String(map.get("ID")));
                    mmc.setTaskId(CommonTools.Obj2String(map.get("ID")));
                    mmc.setPackageId(CommonTools.Obj2String(map.get("F_SSSJB")));
                    mmc.setMmcId(list.get(i).getId());
                    mmc.setMmcName(CommonTools.Obj2String(map.get("F_SJMC")));
                    ret.add(mmc);
                }
            }
        }
        return ret;
    }

    public String getProjectByPackageID(String packageId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", packageId);
        String sql = "SELECT　F_SSFC FROM W_PACKAGE WHERE ID=:ID";
        List<Map<String, Object>> file = jdbcDao.queryForList(sql, map);
        if (file.size() > 0) {
            return file.get(0).get("F_SSFC").toString();
        }
        return "";
    }

    /**
     * @param FILEID
     * @return
     */
    public String getFlieNameByID(String FILEID) {
        String sql = "SELECT * FROM CWM_SYS_FILE WHERE FILEID=:FILEID";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("FILEID", FILEID);
        List<Map<String, Object>> file = jdbcDao.queryForList(sql, param);
        if (file.size() > 0) {
            return file.get(0).get("FILENAME").toString() + "." + file.get(0).get("EXT").toString();
        }
        return "";
    }
}
