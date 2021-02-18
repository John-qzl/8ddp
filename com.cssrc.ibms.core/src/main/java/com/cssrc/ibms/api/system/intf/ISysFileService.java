package com.cssrc.ibms.api.system.intf;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipOutputStream;
import org.csource.common.MyException;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import net.sf.json.JSONObject;

import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.jms.model.ISyncModel;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface ISysFileService
{
	/**
	 * 获取当前文件对应的版本集合的下一个版本号
	 * @param fileId
	 * @return
	 */
	public String getJumpVersion(Long fileId);   
	/**
	 * 获取当前文件对应的版本集合的下一个版本号
	 * @param fileId
	 * @return
	 */
	public String getNextVersion(Long fileId);
    /**
     * 分享文件
     * */
    public abstract void saveShareFile(Long[] ids);
    
    public abstract List<? extends ISysFile> getSysFileByFilePath(String filepath);
    
    /**
     * 关闭分享
     * **/
    public abstract void saveCloseShare(Long[] ids);
    
    /**
     * 查找文件夹下的所有文件
     * */
    public abstract List<? extends ISysFile> getSysFileByFolder(Long folderId);
    
    /**
     * 根据文件夹查找文件-用于附件管理
     * */
    public abstract List<? extends ISysFile> getSysFileByFolder(Long userId, Boolean isRootFolder,
        Boolean isSharedFolder, String folderPath, QueryFilter filter);
    
    /**
     * 根据文件ID获取文件信息
     * 
     * @param filter
     * @return
     */
    public abstract List<? extends ISysFile> getSysFileByFilter(QueryFilter filter);
    
    /**
     * 获取文件下一个ID值
     * 
     * @param filter
     * @return
     */
    public abstract Long doNextVal();
    
    public abstract ISysFile getById(Long attachment);
    
    /**
     * 文件导入
     * 
     * @param sysFileList
     * @param unzipFilePath
     * @param realPath
     */
    public abstract void importSysFile(List<? extends ISysFile> sysFileList, String unzipFilePath, String realPath);
    
    // 保存SysFile
    public abstract Map<Long, ? extends ISysFile> fixSysFileId(ISyncModel syncModel);
    
    public abstract void replaceFileId(Map<String, Object> mainField, IFormField field,
        Map<Long, ? extends ISysFile> fileMap);
    
    /**
     * 生成文件附件临时JSP文件
     *
     * @author YangBo @date 2016年10月20日下午3:43:27
     * @param id
     */
    public abstract void createFileAttachJSP(Long dataTempId);
    /**
     * 此处用来处理脚本中传的参数获取附件列表
     *
     * @author YangBo @date 2016年11月4日上午11:20:12
     * @param request
     * @param typeId
     * @param dataId
     * @param tableId
     * @return
     * @throws Exception
     */
    public abstract List<? extends ISysFile> getAttachList(HttpServletRequest request, JSONObject jsonObject,
        Long typeId, Long dataId, Long tableId)
        throws Exception;
    
    /**
     * 将文件附件html文件插入数据库字段
     *
     * @author YangBo @date 2016年10月20日下午2:50:59
     * @param id
     */
    public abstract void saveFileAndAttachHtml(Long dataTempId);
    
    /**
     * 将流程监控html文件插入数据库字段
     *@author Liubo 
	 *@date 2017-01-17 10:50:19
	 *@param id
     */
    public abstract void saveProcessTempHtml(Long dataTempId);
    
    /**
     * 将流程监控条件字段插入数据库字段
     *@author Liubo 
	 *@date 2017-01-18
	 *@param id
     */
    public abstract void saveProcessCondition(Long dataTempId);
    
    /**
     * 用户下附件所有附件
     *
     * @author YangBo @date 2016年10月18日上午9:57:53
     * @param fileter
     * @return
     */
    public abstract List<? extends ISysFile> getFileAttch(QueryFilter filter);
    
    /**
     * 下载附件接口
     *
     * @author YangBo @date 2016年12月8日下午5:08:09
     * @param request
     * @param response
     * @param fileId
     */
    public abstract void downAttach(HttpServletRequest request, HttpServletResponse response, String fileId)
        throws IOException;
    
    /**
     * 上传附件服务接口
     *
     * @author YangBo @date 2016年12月8日下午9:42:00
     * @param request
     * @param response
     * @param appUser
     * @param userId
     * @return
     * @throws MyException
     * @throws IOException
     */
    public abstract String uploadAttach(MultipartHttpServletRequest request, HttpServletResponse response,
        ISysUser appUser, long userId)
        throws IOException, MyException;
    
    /**
     * 根据id获取list
     * 
     * @param dataIds
     * @param tableIds TODO
     * @return
     * @throws Exception
     */
    public abstract List<? extends ISysFile> getFileListByDataId(String dataIds, String tableIds)
        throws Exception;
    
    /**
     * 获取加密文件路径
     * 
     * @param filepath
     * @param filename
     * @param string
     * @return
     */
    public abstract String getEcryptFilePath(String origFilePath, String oriFileName);
    /**
     * 获取解密文件路径
     * 
     * @param filepath
     * @param filename
     * @param string
     * @return
     */
    public abstract String getDecodeFilePath(String origFilePath, String oriFileName,Long isEcrypt,boolean isFastDFS);
    
    /**
     * 压缩文件
     * 
     * @param zos
     * @param filepath
     * @param filename
     */
    public abstract void compressFile(ZipOutputStream zos, String filepath, String filename);
    
    /**
     * 删除附件
     * 
     * @param sysFile
     * @param attachPath
     * @throws Exception
     */
    public abstract void delFiles(ISysFile sysFile, String attachPath)
        throws Exception;
    
    /**
     * 获取附件字段值
     * 
     * @param MainField
     * @param tableName
     * @param dataId
     * @param formTable
     * @param flag
     * @param relField
     * @return
     * @throws Exception
     */
    public abstract String getMainFieldDataIds(String MainField, String tableName, Long dataId, IFormTable formTable,
        Long flag, String relField)
        throws Exception;
    
    /**
     * 更加主键获取附件列表
     * 
     * @param fileIds
     * @return
     */
    public List<? extends ISysFile> getFileByIds(String fileIds);
    
    public abstract Class<? extends ISysFile> getSysFileClass();

    /**
     * 生成流程监控临时JSP文件
     * @author Liubo
     * @date 2017-01-17
     * @param dataTempId
     */
    public abstract void createProcessJSP(Long dataTempId);

    /**
	 * 通过当前人员密级与传入的密级字段获取筛选条件
	 * @author liubo
	 * @param fieldNameStandard
	 * @return
	 */
	public abstract String getFileConditions(String fieldNameStandard);
    
	/**
	 * @author  dengwenjie
	 * @param dataTempId :业务数据模板Id
	 */
	public abstract void saveMultiTabHtml(Long dataTempId);
	
	/**
	 * 生成或置换JSP临时文件
	 * @author  dengwenjie
	 * @param dataTempId :业务数据模板Id
	 */
    public abstract void createMultiTabJSP(Long dataTempId);
    
    
    /**
     * 将文件夹下的所有文件上传至fastdfs
     * @param folderPath
     */
    public void uploadFileByFolder(String folderPath);
    
    /**
     * 获取指定附件的字节流数组.
     * @param fileId:附件ID
     */
    public byte[] getFileByteByFileId(Long fileId);
    
}