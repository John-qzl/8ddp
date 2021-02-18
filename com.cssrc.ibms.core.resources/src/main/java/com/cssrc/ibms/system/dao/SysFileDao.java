package com.cssrc.ibms.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.system.model.SysFile;

import javax.annotation.Resource;

@Repository
public class SysFileDao extends BaseDao<SysFile>{
	
	@Override
	public Class<SysFile> getEntityClass() {
		return SysFile.class;
	}
	/**
	 * 获取当前文件文件的版本
	 * @param fileId
	 * @return
	 */
	public String getCurVersion(Long fileId){
		SysFile file = getCurFile(fileId);
		if(BeanUtils.isEmpty(file)){
			return "";
		}else{
			return file.getVersion();
		}
	}
	public List<SysFile> getByParams(Map<String, Object> map){
		return this.getBySqlKey("getByParams", map);
	}
	
	public List<SysFile> getPhoto(){
		return this.getBySqlKey("getPhoto",new HashMap<>());
	}
	/**
	 * 获取当前最新版本的文件
	 * @param fileId
	 * @return
	 */
	public SysFile getCurFile(Long fileId){
		return (SysFile)this.getOne("getCurFileVersion", fileId);
	}
	public List<SysFile> getFileVersionList(Long fileId){
		return this.getBySqlKey("getFileVersionList", fileId);
	}
	public void delByDataId(Map<String,Object> map){
		this.delBySqlKey("delByDataId", map);
	}
	public void updateDataId(Map<String,Object> map){
		this.update("updateDataId", map);
	}
	public void insertFileInfo(SysFile sysFile) {
		this.add(sysFile);
	}
	
	
	/**
	 * 查找文件夹下的所有文件
	 * */
	public List<SysFile> getSysFileByFolder(Long folderId){
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("folderid", folderId);
		map.put("delflag", SysFile.FILE_NOT_DEL);
		return this.getBySqlKey("getSysFileByFolderId", map);
	}
	
	public SysFile getFileByDataId(String dataId) {
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("dataId", dataId);
		return this.getUnique("getFileByDataId", map);
	}
	
	/**
	 * 查找文件夹下的所有文件
	 * */
	public List<SysFile> getSysFileByFilePath(String filepath){

		Map<String, Object>map = new HashMap<String, Object>();
		map.put("filepath", filepath);
		map.put("delflag", SysFile.FILE_NOT_DEL);
		return this.getBySqlKey("getSysFileByFilePath", map);
	}
	
	/**
	 * 根据文件夹查找文件-用于附件管理
	 * */
	public List<SysFile> getSysFileByFolder(Long userId,Boolean isRootFolder,Boolean isSharedFolder,String folderPath,QueryFilter filter){
		
		if(isRootFolder){
			//所有文库
			filter.addFilterForIB("creatorId", userId);
			filter.addFilterForIB("delFlag", SysFile.FILE_NOT_DEL);
			filter.addFilterForIB("shared", SysFile.SHARED_TRUE);
			filter.addFilterForIB("fileatt", SysFile.FILEATT_TRUE);
			return this.getBySqlKey("getSysFileByRootFolder", filter);
		}else if(isSharedFolder){
			//共享文库
			filter.addFilterForIB("creatorId", userId);
			filter.addFilterForIB("delFlag", SysFile.FILE_NOT_DEL);
			filter.addFilterForIB("fileatt", SysFile.FILEATT_TRUE);
			filter.addFilterForIB("shared", SysFile.SHARED_TRUE);
			filter.addFilterForIB("folderPath", folderPath);
			return this.getBySqlKey("getSysFileBySharedFolder", filter);
		}else{
			//一般文库
			filter.addFilterForIB("creatorId", userId);
			filter.addFilterForIB("delFlag", SysFile.FILE_NOT_DEL);
			filter.addFilterForIB("fileatt", SysFile.FILEATT_TRUE);
			filter.addFilterForIB("shared", SysFile.SHARED_FALSE);
			filter.addFilterForIB("folderPath", folderPath);
			return this.getBySqlKey("getSysFileByFolder", filter);
		}
	}
	
	/**
	 * 根据文件ID获取文件信息
	 * 
	 * @param fileId
	 * @param filter
	 * @return
	 */
	public List<SysFile> getSysFileByFilter(QueryFilter filter){
		String fileId = CommonTools.Obj2String(filter.getFilters().get("fileId"));
		String fileIds = CommonTools.Obj2String(filter.getFilters().get("fileIds"));
		String fileid = "-1";
		if(!"".equals(fileId))  fileid = fileId;
		if(!"".equals(fileIds)) fileid = fileIds;
		//重置当前页码
		int currentPage = filter.getPagingBean().getCurrentPage();
		int pageSize = filter.getPagingBean().getPageSize();
		currentPage = currentPage/pageSize + 1;
		filter.getPagingBean().setCurrentPage(currentPage);
		filter.addFilterForIB("fileId", fileid);
		Map<String, String> map = new HashMap<String, String>();
		map.put("fileId", fileid);
		return this.getBySqlKey("getSysFileByFileId", map);
	}
	
	/**
	 * 获取用户附件
	 *@author YangBo @date 2016年10月18日上午9:56:40
	 *@param fileter
	 *@return
	 */
	public List<SysFile> getFileAttch(QueryFilter fileter) {
		return getBySqlKey("getAllPersonalFile", fileter);
	}
	
	/**
	 *拼接多关联表查询
	 *@author YangBo @date 2016年11月7日上午8:56:10
	 *@param relTableName
	 *@param relTableNameW
	 *@param relFileIdDataId
	 *@param relField
	 *@param dataId
	 *@return
	 */
	public String getSql(String relTableName,String relTableNameW,String relFileIdDataId,String relField,Long dataId){
		String colums="FILEID,FILENAME,FILEPATH,CREATETIME,EXT,FILETYPE,NOTE,CREATORID,CREATOR,TOTALBYTES,DELFLAG,PROTYPEID,";
		colums+="TABLEID,DATAID,SHARED,FOLDERID,FILEATT,FOLDERPATH,SECURITY_,DESCRIBE_,FILING,PARENTID,ISNEW,VERSION,STOREWAY,DIMENSION";
		String sql="union select "+colums+" from CWM_SYS_FILE WHERE TABLEID in";
		sql+="(SELECT TABLEID from IBMS_FORM_TABLE WHERE TABLENAME='"+relTableName+"')";
		sql+="and dataId in (select ID from "+relTableNameW+" WHERE "+relField+"="+dataId+")";
		
		if(relFileIdDataId.length()!=0){ //考虑到未绑定附件字段的情况
			sql+="OR FILEID in("+relFileIdDataId+")";
		}
		
				
		return sql;
	}
	
	
	/**
	 * 根据文件IDS获取文件信息
	 * @param tableIds TODO
	 * @param fileId
	 * @param filter
	 * 
	 * @return
	 */
	public List<SysFile> getFileListByDataIds(String dataIds, String tableIds){
		Map<String, String> map = new HashMap<String, String>();
		map.put("dataId", dataIds);
		map.put("tableId", tableIds);
		return this.getBySqlKey("getFileListByDataIds", map);
	}
	
	public List<SysFile> getFileByIds(String fileIds){
		Map<String, String> map = new HashMap<String, String>();
		map.put("fileId", fileIds);
		return this.getBySqlKey("getSysFileByFileId", map);
	}
	
	//通过传入的密级条件筛选文件
	public List<SysFile> getFileBySecurity(String conditions){
		Map<String, String> map = new HashMap<String, String>();
		map.put("conditions", conditions);
		return this.getBySqlKey("getFileBySecurity", map);
	}
	
	/**
	 * 获取筛选条件下的系统附件列表
	 *@author liubo
	 *@date 2017年03月13日上午16:04:48
	 *@param fileter
	 *@return
	 */
	public List<SysFile> getSysFilesByConditions(QueryFilter fileter) {
		return getBySqlKey("getSysFilesByConditions", fileter);
	}
	
	public SysFile getFileByFileId(String fileId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("fileId", fileId);
		return this.getUnique("getFileByFileId", map);
	}
	
	/**
	 * by YangBo
	 * 获取用户创建的某类型文件
	 * @param fileType
	 * @param currentUserId
	 * @return
	 */
	public List<SysFile> getByFileType(String fileType, Long currentUserId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fileType", fileType);
		map.put("creator", currentUserId);
		return this.getBySqlKey("getByFileType", map);
	}
	@Resource
	private JdbcDao jdbcDao;

	/**
	 * @Description:根据tableId删除表单实例单元格中存储的附件
	 * @author qiaozhili
	 * @date 2018/12/24 14:43
	 * @param tableId
	 * @return int
	 */
	public int getFileDataByTableId(String tableId){
		String sql = "select count(*) from CWM_SYS_FILE WHERE TABLEID = '" + tableId + "&&W_CK_RESULT_CARRY"+"'";
		int count = jdbcDao.queryForInt(sql, null);
		return count;
	}
	public int getFileDataByDataId(String dataId){
		String sql = "select count(*) from CWM_SYS_FILE WHERE DATAID = '" + dataId + "'";
		int count = jdbcDao.queryForInt(sql, null);
		return count;
	}
	/**
	 * @Description: 根据TABLEID以及"时间戳"删除某一次的表单实例中的附件
	 * @Author  shenguoliang
	 * @param tableId
	 * @param describe
	 * @Date 2018/12/25 19:30
	 * @Return int
	 * @Line 251
	 */
	public int deleteFileDataByTableId(String tableId,String describe) {
		int flag = 0;
		StringBuilder sql = new StringBuilder();
		sql.append(" DELETE FROM CWM_SYS_FILE ");
		sql.append(" WHERE  TABLEID = '" + tableId + "' ");
		sql.append(" AND DESCRIBE_ != '" + describe + "'");
		flag = jdbcDao.exesql(sql.toString(), null);
		return flag;
	}
	/**
	 * @Description: 根据DATAID以及"时间戳"删除某一次的表单实例的签署图片
	 * @Author  shenguoliang
	 * @param dataId
	 * @param describe
	 * @Date 2018/12/25 19:29
	 * @Return int
	 * @Line 260
	 */
	public int deleteFileDataByDataId(String dataId,String describe) {
		int flag = 0;
		StringBuilder sql = new StringBuilder();
		sql.append(" DELETE FROM CWM_SYS_FILE ");
		sql.append(" WHERE  DATAID = '" + dataId + "' ");
		sql.append(" AND DESCRIBE_ != '" + describe + "'");
		flag = jdbcDao.exesql(sql.toString(), null);
		return flag;
	}

}