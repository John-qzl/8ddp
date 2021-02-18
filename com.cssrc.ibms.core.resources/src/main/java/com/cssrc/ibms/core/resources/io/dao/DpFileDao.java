package com.cssrc.ibms.core.resources.io.dao;

import java.io.File;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.DpFile;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.util.SysFileUtil;

@Repository
public class DpFileDao {
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    private SysFileDao sysFileDao;

    /**
     * 处理文件
     *
     * @param dpFile : 文件
     * @param log
     */
    public SysFile insert(DpFile dpFile, String fileFloder) {
        if (dpFile == null) {
            return null;
        }
        this.delete(dpFile, true);
        String fileName = dpFile.getFullFileName();
        String noSemiconFileName = FileOperator.generateFilenameNoSemicolon(fileName);
        String filePath = fileFloder + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        String storePath = SysFileUtil.getStorePath();
        String targetFoldersPath = SysConfConstant.UploadFileFolder + File.separator + SysFileUtil.getStorePath() ;
        FileOperator.createFolders(targetFoldersPath);
        String targetpath = targetFoldersPath + File.separator + noSemiconFileName;
        SysFile sysFile = dpFile.toSysFile();
        sysFile.setFilepath(storePath + File.separator + noSemiconFileName);
        sysFile.setTotalBytes(file.length());
        sysFile.setFileType("FILE");
        sysFile.setCreatetime(new Date());
        //拷贝文件且保存到数据库中
        FileOperator.copyFile(filePath, targetpath);
        sysFileDao.add(sysFile);
        return sysFile;
    }

    /**
     * 处理文件 操作项照片
     *
     * @param log
     * @param dpFile : 文件
     * @param id
     */
    public SysFile insertOpratorPhoto(DpFile dpFile, String fileFloder, String ckResultId) {
        if (dpFile == null) {
            return null;
        }
        this.delete(dpFile, true);
        String fileName = dpFile.getFullFileName();
        String noSemiconFileName = FileOperator.generateFilenameNoSemicolon(fileName);
        String filePath = fileFloder + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        String storePath1 = SysConfConstant.UploadFileFolder;
//		String targetpath = SysConfConstant.UploadFileFolder +File.separator + SysFileUtil.getStorePath()+File.separator+noSemiconFileName;
        String storePath2 = ckResultId + File.separator + 0;
        FileOperator.createFolders(storePath1 + File.separator + storePath2);
        SysFile sysFile = dpFile.toSysFile();
        sysFile.setFilepath(storePath2 + File.separator + noSemiconFileName);
        sysFile.setTotalBytes(file.length());
        sysFile.setFileType("FILE");
        sysFile.setCreatetime(new Date());
        //拷贝文件且保存到数据库中
        FileOperator.copyFile(filePath, storePath1 + File.separator + storePath2 + File.separator + noSemiconFileName);
        sysFileDao.add(sysFile);
        return sysFile;
    }

    /**
     * 处理文件 签署照片
     *
     * @param log
     * @param dpFile : 文件
     * @param id
     */
    public SysFile insertSignPhoto(DpFile dpFile, String fileFloder, String ckResultId) {
        if (dpFile == null) {
            return null;
        }
        this.delete(dpFile, true);
        String fileName = dpFile.getFullFileName();
        String noSemiconFileName = FileOperator.generateFilenameNoSemicolon(fileName);
        String filePath = fileFloder + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        String storePath1 = SysConfConstant.UploadFileFolder;
//		String targetpath = SysConfConstant.UploadFileFolder +File.separator + SysFileUtil.getStorePath()+File.separator+noSemiconFileName;
        String storePath2 = 0 + File.separator + ckResultId;
        FileOperator.createFolders(storePath1 + File.separator + storePath2);
        SysFile sysFile = dpFile.toSysFile();
        sysFile.setFilepath(storePath2 + File.separator + noSemiconFileName);
        sysFile.setTotalBytes(file.length());
        sysFile.setFileType("FILE");
        sysFile.setCreatetime(new Date());
        //拷贝文件且保存到数据库中
        FileOperator.copyFile(filePath, storePath1 + File.separator + storePath2 + File.separator + noSemiconFileName);
        sysFileDao.add(sysFile);
        return sysFile;
    }

    //物理文件不删除，导入出错的时无法恢复
    public void delete(DpFile dpFile, boolean hasFile) {
        if (dpFile == null || dpFile.getId() == null) {
            return;
        }
        SysFile sysfile = sysFileDao.getById(dpFile.getId());
        if (sysfile == null) {
            return;
        }
        sysFileDao.delById(dpFile.getId());
        if (hasFile) {
            String targetpath = SysConfConstant.UploadFileFolder + File.separator + sysfile.getFilepath();
            //FileOperator.deleteFile(targetpath);
        }
    }
}
