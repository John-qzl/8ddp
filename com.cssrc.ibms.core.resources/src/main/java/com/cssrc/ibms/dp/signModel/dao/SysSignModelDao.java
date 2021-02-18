package com.cssrc.ibms.dp.signModel.dao;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.signModel.entity.CwmSysSignModel;
import com.cssrc.ibms.dp.signModel.entity.WPadhcqzb;
import com.cssrc.ibms.system.dao.SysFileDao;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Repository
public class SysSignModelDao {
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    private SysFileDao sysFileDao;

    /**
     * 根据用户id在签章表里查信息
     * @param userId
     * @return
     */
    public CwmSysSignModel selectByUserId(String userId) {
        String sql = " select * from CWM_SYS_SIGN_MODEL where USER_ID="+userId;
        Map<String,Object> map = jdbcDao.queryForMap(sql, null);
        CwmSysSignModel cwmSysSignModel = JSON.parseObject(JSON.toJSONString(map), CwmSysSignModel.class);
        return cwmSysSignModel;
    }

    /**
     * 统计当前表中该用户的记录数
     * @return
     */
    public Integer countByUserid(BigDecimal userid) {
        String sql="select * from CWM_SYS_SIGN_MODEL where USER_ID='"+userid+"'";
        Map<String,Object> map = jdbcDao.queryForMap(sql, null);
        if (map==null){
            return 0;
        }
        return map.size();
    }

    /**
     * 更新userid对应的imgpath
     * @param userId
     * @param imgPath
     */
    public void updateByUserid(BigDecimal userId, String imgPath) {
        String sql="update CWM_SYS_SIGN_MODEL SET IMG_PATH='"+imgPath+"' "+"where USER_ID='"+userId+"'";
        jdbcDao.exesql(sql, null);
    }

    /**
     * 根据userid和fileId新建用户签章
     * @param cwmSysSignModel
     */
    public void insert(CwmSysSignModel cwmSysSignModel) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql="insert into CWM_SYS_SIGN_MODEL " +
                "(id,img_path,user_id,code,name,is_default,type_,start_date,end_date,passw,path_type,version) values ("
                +cwmSysSignModel.getId()+",'"
                +cwmSysSignModel.getImg_Path()+"',"
                +cwmSysSignModel.getUser_Id()+",'"
                +cwmSysSignModel.getCode()+"','"
                +cwmSysSignModel.getName()+"',"
                +cwmSysSignModel.getIs_Default()+","
                +cwmSysSignModel.getType_()+","
                +"to_date('"+sdf.format(cwmSysSignModel.getStart_Date()).substring(0,10)+"','yyyy-mm-dd'),"
                +"to_date('"+sdf.format(cwmSysSignModel.getStart_Date()).substring(0,10)+"','yyyy-mm-dd'),'"
                +cwmSysSignModel.getPassw()+"',"
                +cwmSysSignModel.getPath_Type()+",'"
                +cwmSysSignModel.getVersion()+"')";
        jdbcDao.exesql(sql, null);
    }

    /**
     * 根据文件id删除指定条目
     * @param fileId
     */
    public void deleteByFileId(String fileId) {
        String sql="delete CWM_SYS_SIGN_MODEL where DBMS_LOB.SUBSTR(IMG_PATH, 200, 1)='"+fileId+"'";
        jdbcDao.exesql(sql, null);
    }
}
