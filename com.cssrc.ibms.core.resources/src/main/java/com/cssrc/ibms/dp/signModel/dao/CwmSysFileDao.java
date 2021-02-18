package com.cssrc.ibms.dp.signModel.dao;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.signModel.entity.CwmSysFile;
import com.cssrc.ibms.dp.signModel.entity.CwmSysSignModel;
import com.cssrc.ibms.system.dao.SysFileDao;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Map;

@Repository
public class CwmSysFileDao {
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    private SysFileDao sysFileDao;

    /**
     * 根据文件id查找文件信息
     * @param ID
     * @return
     */
    public CwmSysFile selectById(String ID) {
        String sql = " select * from CWM_SYS_FILE where FILEID="+ID;
        Map<String,Object> map = jdbcDao.queryForMap(sql, null);
        CwmSysFile cwmSysFile = JSON.parseObject(JSON.toJSONString(map), CwmSysFile.class);
        return cwmSysFile;
    }

    /**
     * 保存文件信息
     * @param cwmSysFile
     */
    public void insert(CwmSysFile cwmSysFile) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql="insert into CWM_SYS_FILE " +
                "(fileid,filename,filepath,createtime,filetype,creator,creatorid) values ("
                +cwmSysFile.getFileid()+",'"
                +cwmSysFile.getFilename()+"','"
                +cwmSysFile.getFilepath()+"',"
                +"to_date('"+sdf.format(cwmSysFile.getCreatetime()).substring(0,10)+"','yyyy-mm-dd'),'"
                +cwmSysFile.getFiletype()+"','"
                +cwmSysFile.getCreator()+"',"
                +cwmSysFile.getCreatorid()+")";
        jdbcDao.exesql(sql, null);
    }

    /**
     * 根据fileid获取文件数量,主要是看看fileId是否被占用
     * 存在返回true
     * 不存在返回false
     * @param fileid
     * @return
     */
    public boolean checkFileidExist(Long fileid) {
        String sql = " select * from CWM_SYS_FILE where FILEID="+fileid;
        Map<String,Object> map = jdbcDao.queryForMap(sql, null);
        CwmSysFile cwmSysFile = JSON.parseObject(JSON.toJSONString(map), CwmSysFile.class);
        if (cwmSysFile==null){
            return false;
        }else {
            return true;
        }
    }
    /**
     * 根据文件id删除记录
     * @param fileId
     */
    public void deleteByFileId(String fileId) {
        String sql="delete CWM_SYS_FILE where FileID='"+fileId+"'";
        jdbcDao.exesql(sql, null);
}

}
