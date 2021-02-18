package com.cssrc.ibms.dp.signModel.service;

import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.dp.signModel.dao.CwmSysFileDao;
import com.cssrc.ibms.dp.signModel.dao.PADreturnSignModelDao;
import com.cssrc.ibms.dp.signModel.dao.SysSignModelDao;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
public class PADSignSyncService {
    @Resource
    private PADreturnSignModelDao pADreturnSignModelDao;
    @Resource
    private SysSignModelDao sysSignModelDao;
    @Resource
    private CwmSysFileDao cwmSysFileDao;


    /**
     * 签章页面删除时，同步数据到签章表，文件表
     * @param signModelIds
     * @return
     */
    public void SyncSignModelInfo(String signModelIds) {
        //拆分id为String数组
        String[] ids=signModelIds.split(",");
        for (String id:ids ){
            String fileId=pADreturnSignModelDao.selectById(id).getF_Qzid();
            String filePath=cwmSysFileDao.selectById(fileId).getFilepath();
            sysSignModelDao.deleteByFileId(fileId);
            cwmSysFileDao.deleteByFileId(fileId);
            pADreturnSignModelDao.deleteByFileId(fileId);
            //物理删除文件
            String attachPath = AppUtil.getAttachPath();
            if (filePath.startsWith("group")) {
                FastDFSFileOperator.deleteFile(filePath);
            } else {
                FileOperator.deleteFile(attachPath + File.separator + filePath);
            }
        }
    }
}
