package com.cssrc.ibms.dp.form.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FormFolderDao {
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    ProductTypeDao productTypeDao;
    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> allFormFolder(String projectId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("projectId", projectId);
        String sql = "SELECT * FROM W_TEMP_FILE WHERE F_PROJECT_ID=:projectId";
        return jdbcDao.queryForList(sql, param);
    }

    public List<Map<String, Object>> getFormsByFolderID(String folderIds) {
//		Map<String,Object> param=new HashMap<String,Object>();
//		param.put("folderIds", folderIds);
        String sql = "SELECT * FROM W_TABLE_TEMP WHERE F_TEMP_FILE_ID IN(" + folderIds + ")";
        return jdbcDao.queryForList(sql, null);
    }

    public Map<String, Object> getTableTempById(Map<String, Object> param) {
        String sql = "SELECT * FROM W_TABLE_TEMP WHERE ID=:id";
        return jdbcDao.queryForMap(sql, param);
    }

    public String getTableTempIdById(String Id) {
        String sql = "SELECT F_TABLE_TEMP_ID FROM W_TB_INSTANT  WHERE ID = " + Id;
        String s = jdbcTemplate.queryForObject(sql, String.class);
        return s;

    }

    public Map<String, Object> getTableInstantById(Map<String, Object> param) {
        String sql = "SELECT * FROM W_TB_INSTANT WHERE ID=:id";
        return jdbcDao.queryForMap(sql, param);
    }

    public Map<String, Object> getSignResultById(Map<String, Object> param) {
        String sql = "SELECT * FROM W_SIGNRESULT WHERE F_SIGNDEF_ID=:id";
        return jdbcDao.queryForMap(sql, param);
    }

    public List<Map<String, Object>> getSignResultById(String tableinsId) {
        String sql = "SELECT signres.*,signdef.F_NAME,sysfile.FILEID FROM W_SIGNRESULT signres, W_SIGNDEF signdef,CWM_SYS_FILE sysfile  WHERE signres.F_SIGNDEF_ID=signdef.\"ID\" AND  signres.ID=sysfile.\"DATAID\" AND  signres.F_TB_INSTANT_ID=:tableinsId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tableinsId", tableinsId);
        return jdbcDao.queryForList(sql, param);
    }

    public List<Map<String, Object>> getSignResById(String tableinsId) {
        String sql = "SELECT signres.*,signdef.F_NAME FROM W_SIGNRESULT signres, W_SIGNDEF signdef  WHERE signres.F_SIGNDEF_ID=signdef.\"ID\" AND  signres.F_TB_INSTANT_ID=:tableinsId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tableinsId", tableinsId);
        return jdbcDao.queryForList(sql, param);
    }

    public List<Map<String, Object>> getCondiResById(String tableinsId) {
        String sql = "SELECT condires.*,ckcondi.F_NAME FROM W_CONDI_RES condires ,W_CK_CONDITION ckcondi WHERE condires.F_CONDITION_ID=ckcondi.\"ID\" AND condires.F_TB_INSTAN_ID=:tableinsId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tableinsId", tableinsId);
        return jdbcDao.queryForList(sql, param);
    }

    public List<Map<String, Object>> getCheckItemAndMapById(String tableinsId) {
        //根据表单实例ID区分检查结果表的类型
        Map typeMap = productTypeDao.getProductType(Long.valueOf(tableinsId));
        String ck_resultName = "";
        if (typeMap!=null) {
        	if ("空间".equals(typeMap.get("TYPE"))) {
                ck_resultName = "W_CK_RESULT";
            } else if ("运载".equals(typeMap.get("TYPE"))) {
                ck_resultName = "W_CK_RESULT_CARRY";
            } else {
                ck_resultName = "W_CK_RESULT_JGJG";
            }
		}else {
			// 默认值
			ck_resultName = "W_CK_RESULT_CARRY";
		}
        String sql = "SELECT checkitem.*,res.F_SKETCHMAP FROM " + ck_resultName + " res,W_ITEMDEF checkitem WHERE res.F_ITEMDEF_ID=checkitem.\"ID\" AND RES.F_TB_INSTAN=:tableinsId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tableinsId", tableinsId);
        return jdbcDao.queryForList(sql, param);

    }

    public List<Map<String, Object>> getCheckConditionById(String tabletempId) {
        String sql = "SELECT * FROM W_CK_CONDITION WHERE F_TABLE_TEMP_ID=:tabletempId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tabletempId", tabletempId);
        return jdbcDao.queryForList(sql, param);
    }

    public List<Map<String, Object>> getSignById(String tabletempId) {
        String sql = "SELECT * FROM W_SIGNDEF WHERE F_TABLE_TEMP_ID=:tabletempId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tabletempId", tabletempId);
        return jdbcDao.queryForList(sql, param);
    }

    public List<Map<String, Object>> getCheckItemById(String tabletempId) {
        String sql = "SELECT * FROM W_ITEMDEF WHERE F_TABLE_TEMP_ID=:tabletempId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tabletempId", tabletempId);
        return jdbcDao.queryForList(sql, param);
    }

    public List<Map<String, Object>> getFormsByProjectID(String projectId) {
        String sql = "SELECT * FROM W_TABLE_TEMP WHERE F_PROJECT_ID=:projectId  ORDER BY ID  ";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("projectId", projectId);
        return jdbcDao.queryForList(sql, param);
    }

    public List<Map<String, Object>> getFormsByProjectID(String projectId, boolean flag) {
        String sql = "SELECT * FROM W_TABLE_TEMP WHERE F_PROJECT_ID=:projectId AND F_STATUS=:status ORDER BY ID";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("projectId", projectId);
        param.put("status", IOConstans.TABLE_TEMP_COMPLETE);
        return jdbcDao.queryForList(sql, param);
    }

    public Map<String, Object> insertTableIns(Map<String, Object> param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            String sql = "INSERT INTO W_TB_INSTANT(ID,F_NAME,F_NUMBER,F_CONTENT,F_TABLE_TEMP_ID) VALUES(:insId,:F_NAME,:F_NUMBER,:F_CONTENTS,:ID)";
            int k = jdbcDao.exesql(sql, param);
            msg.put("success", true);
            msg.put("msg", "数据插入成功");
        } catch (Exception e) {
            msg.put("success", false);
            msg.put("msg", e.getMessage());
        }
        return msg;
    }

    public void updateTableIns(Map<String, Object> param) {
        String sql = "UPDATE W_TB_INSTANT SET F_CONTENT=:F_CONTENT,F_HCR=:userName,F_HCRID=:userName WHERE ID=:ID";
        int k = jdbcDao.exesql(sql, param);
    }

    public List<Map<String, Object>> getPicPathByID(String Id) {
        String sql = "SELECT * FROM CWM_SYS_FILE WHERE TABLEID=:Id";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("Id", Id);
        return jdbcDao.queryForList(sql, param);
    }

    public List<Map<String, Object>> getDiaPathByID(String Id) {
        String sql = "SELECT * FROM CWM_SYS_FILE WHERE FILEID=:Id";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("Id", Id);
        return jdbcDao.queryForList(sql, param);
    }

    public Map<String, Object> getCkResultById(Map<String, Object> param) {
        String sql = "SELECT * FROM W_CK_RESULT WHERE F_ITEMDEF_ID=:id AND F_TB_INSTAN=:slid";
        return jdbcDao.queryForMap(sql, param);
    }

    public void updateckresDiagram(Map<String, Object> param) {
        String sql = "UPDATE W_CK_RESULT SET F_SKETCHMAP=:diaId WHERE ID=:chresId";
        int k = jdbcDao.exesql(sql, param);
    }

    public void delckresDiagram(Map<String, Object> param) {

        String sql = "UPDATE W_CK_RESULT SET F_SKETCHMAP='' where F_SKETCHMAP=:diaId AND F_TB_INSTAN=:slid";
        int k = jdbcDao.exesql(sql, param);
    }


    /**
     * 更新检查项结果
     *
     * @param param
     * @param instantId
     */
    public void updateckres(Map<String, Object> param, String instantId) {
        Map typeMap = productTypeDao.getProductType(Long.valueOf(instantId));
        String ck_resultName = "W_CK_RESULT_CARRY";
//        if ("空间".equals(typeMap.get("TYPE"))) {
//            ck_resultName = "W_CK_RESULT";
//        } else if ("运载".equals(typeMap.get("TYPE"))) {
//            ck_resultName = "W_CK_RESULT_CARRY";
//        } else {
//            ck_resultName = "W_CK_RESULT_JGJG";
//        }
        String sql = "UPDATE " + ck_resultName + " SET F_VALUE=:value WHERE ID =:resultid";
        int k = jdbcDao.exesql(sql, param);
    }

    public void updatecondies(Map<String, Object> param) {
        String sql = "UPDATE W_CONDI_RES SET F_VALUE=:conditionvalue WHERE ID=:conditionid";
        int k = jdbcDao.exesql(sql, param);
    }

    public List<Map<String,Object>> queryCondition(String conditionId) {
        String sql = "SELECT * FROM W_CONDI_RES WHERE ID = " + conditionId;
        return jdbcDao.queryForList(sql, null);
    }

    public List<Map<String,Object>> queryConditionByTaskId(String broTaskId) {
        String sql = "SELECT * FROM W_CONDI_RES WHERE F_TB_INSTAN_ID = " + broTaskId;
        return jdbcDao.queryForList(sql, null);
    }

    public List<Map<String,Object>> querySign(String signresesId) {
        String sql = "SELECT * FROM W_SIGNRESULT WHERE ID = " + signresesId;
        return jdbcDao.queryForList(sql, null);
    }

    public List<Map<String,Object>> querySignByTaskId(String tempId) {
        String sql = "SELECT * FROM W_SIGNDEF WHERE F_TABLE_TEMP_ID = " + tempId;
        return jdbcDao.queryForList(sql, null);
    }

    public void updatesignres(Map<String, Object> param) {
        String sql = "UPDATE W_SIGNRESULT SET F_SIGNTIME=to_date('" + param.get("time") + "','yyyy-mm-dd hh24:mi:ss')  WHERE ID=:signid";
        int k = jdbcDao.exesql(sql, param);
    }

    public void updateTableInsime(Map<String, Object> param) {
/*
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String uploadTime=df.format(new Date()).substring(0,10);// 获取当前系统时间
        param.put("uploadTime",uploadTime);
        String sql = "UPDATE W_TB_INSTANT SET F_STARTTIME=:starttime, F_ENDTIME=:endtime, F_UPLOAD_TIME=:uploadTime, F_TASK_ID=:padCode WHERE ID=:ID";
*/
        String sql = "UPDATE W_TB_INSTANT SET F_STARTTIME=:starttime, F_ENDTIME=:endtime, F_TASK_ID=:padCode WHERE ID=:ID";
        int k = jdbcDao.exesql(sql, param);
    }
    public void updateTableInsCode(Map<String, Object> param) {
        String sql = "UPDATE W_TB_INSTANT SET F_TASK_ID=:padCode  WHERE ID=:ID";
        int k = jdbcDao.exesql(sql, param);
    }

    public void updateDataPackageById(Map<String, Object> param ,String time) {
        String sql = "UPDATE W_DATAPACKAGEINFO SET F_ZXZT=:zxzt, F_WCSJ=to_date('"+time+"','yyyy-mm-dd hh24:mi:ss') " +
                "WHERE ID=:id";
        jdbcDao.exesql(sql, param);
    }

    public Map<String, Object> moveModel(Map<String, Object> param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        String sql = "UPDATE W_TABLE_TEMP SET F_TEMP_FILE_ID=:fileId  WHERE ID=:id";
        int k = jdbcDao.exesql(sql, param);
        if (k > 0) {
            msg.put("success", "true");
            msg.put("msg", "模板移动成功");
        } else {
            msg.put("success", "false");
            msg.put("msg", "模板移动失败");
        }
        return msg;
    }

    public Map<String, Object> getProjectById(Map<String, Object> param) {
        String sql = "SELECT * FROM W_PROJECT  WHERE ID=:id";
        return jdbcDao.queryForMap(sql, param);
    }

    public List<Map<String, Object>> getAllProjectByID(String Id) {
        String sql = "SELECT * FROM W_PROJECT WHERE F_SSXH=:Id";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("Id", Id);
        return jdbcDao.queryForList(sql, param);
    }

    /**
     * 更新表单模板中的html
     *
     * @param param
     */
    public void updateTableTempContent(Map<String, Object> param) {
        String sql = "UPDATE W_TABLE_TEMP SET F_CONTENTS=:CONTENTS, F_STATUS=:status WHERE ID=:ID";

        int k = jdbcDao.exesql(sql, param);
    }

    public void updateTableTempContent2(Map<String, Object> param) {
        String sql = "UPDATE W_TABLE_TEMP SET F_CONTENTS=:F_CONTENT WHERE ID=:ID";
        int k = jdbcDao.exesql(sql, param);
    }


    public Map<String, Object> isPicExists(String Id) {
        String sql = "SELECT COUNT(1) as count FROM CWM_SYS_FILE WHERE TABLEID=:Id";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("Id", Id);
        return jdbcDao.queryForMap(sql, param);
    }

    public List<Map<String, Object>> getCheckresByID(String Id) {
        Map typeMap = productTypeDao.getProductType(Long.valueOf(Id));
        String ck_resultName = "";
        if ("空间".equals(typeMap.get("TYPE"))) {
            ck_resultName = "W_CK_RESULT";
        } else if ("运载".equals(typeMap.get("TYPE"))) {
            ck_resultName = "W_CK_RESULT_CARRY";
        } else {
            ck_resultName = "W_CK_RESULT_JGJG";
        }
        String sql = "SELECT ckres.*,itemdef.F_DESCRIPTION  FROM " + ck_resultName + " ckres,W_ITEMDEF itemdef  WHERE ckres.F_ITEMDEF_ID=itemdef.\"ID\" AND ckres.F_TB_INSTAN=:Id";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("Id", Id);
        return jdbcDao.queryForList(sql, param);
    }

    public List<Map<String, Object>> getFileByID(String Id) {
        String sql = "SELECT * FROM CWM_SYS_FILE WHERE TABLEID=:Id";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("Id", Id);
        return jdbcDao.queryForList(sql, param);
    }

    public Map<String, Object> getTabletempById(String id) {
        String sql = "SELECT * FROM W_TABLE_TEMP WHERE ID=:ID";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ID", id);
        return jdbcDao.queryForMap(sql, param);
    }

    /**
     * @Author shenguoliang
     * @Description:根据表单实例获取对应的型号的类型来区分检查结果
     * @Params [instanId]表单实例
     * @Date 2018/5/11 16:14
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     */
    public Map<String, Object> getProductType(Long instanId) {

        String sql = " SELECT DISTINCT XH.ID,XH.F_TYPE FROM W_PRODUCT XH , " +
                "W_PROJECT FC , W_TABLE_TEMP MB ,W_TB_INSTANT SL " +
                "WHERE XH.ID = FC.F_SSXH " +
                "AND FC.ID = MB.F_PROJECT_ID " +
                "AND MB.ID = SL.F_TABLE_TEMP_ID  " +
                "AND SL.ID =:instanId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanId", instanId);
        return jdbcDao.queryForMap(sql, params);
    }
}
