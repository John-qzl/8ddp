package com.cssrc.ibms.system.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import com.cssrc.ibms.core.resources.io.dao.IOSignDefDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.util.common.CommonTools;

import com.cssrc.ibms.dp.form.dao.SignResultDao;
import com.cssrc.ibms.dp.form.model.SignResult;
import com.cssrc.ibms.dp.product.acceptance.service.AcceptancePlanService;
import com.cssrc.ibms.dp.sync.bean.PadPhotoInfo;
import com.cssrc.ibms.dp.sync.dao.PadPhotoInfoDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipOutputStream;
import org.com.cssrc.ibms.solrclient.data.FileQueryResult;
import org.com.cssrc.ibms.solrclient.intf.ISolrQueryService;
import org.csource.common.MyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.jms.model.ISyncModel;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.system.model.ISysParameter;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.file.ZipUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.report.service.SignModelService;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.GlobalType;
import com.cssrc.ibms.system.model.SolrFile;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.model.SysFileFolder;
import com.cssrc.ibms.system.model.SysFileType;
import com.cssrc.ibms.system.model.SysParameter;
import com.cssrc.ibms.system.util.FileVersionManage;
import com.cssrc.ibms.system.util.SysFileUtil;

@Service
public class SysFileService extends BaseService<SysFile> implements
		ISysFileService {

	@Resource
	private SysFileDao dao;
	@Resource
	private IFormTableService formTableService;
	@Resource
	private IDataTemplateService dataTemplateService;
	@Resource
	private SysTypeKeyService sysTypeKeyService;
	@Resource
	private IFormFieldService formFieldService;
	@Resource
	private SysParameterService sysParameterService;
	@Resource
	private GlobalTypeService globalTypeService;
	@Resource
	private SysFileFolderService sysFileFolderService;
	@Resource
	private SysFileTypeService sysFileTypeService;
	@Resource
	private SolrService solrService;
	@Resource
	private ModelInfoSyncService modelInfoSyncService;

	@Override
	protected IEntityDao<SysFile, Long> getEntityDao() {
		return dao;
	}

	@Override
	public Class<SysFile> getSysFileClass() {
		return SysFile.class;
	}
	@Resource
	public PadPhotoInfoDao padPhotoInfoDao;
	@Resource
	public SignResultDao signResultDao;
	@Resource
	public IOSignDefDao ioSignDefDao;
	@Resource
	public SysUserDao sysUserDao;
	@Resource
	public AcceptancePlanService acceptancePlanService;
	@Resource
	public IOTableInstanceDao ioTableInstanceDao;

	/**
	 * 跳号，前面的加一，后面的数字归零
	 *
	 * @param fileId
	 * @return
	 */
	public String getJumpVersion(Long fileId) {
		String version = dao.getCurVersion(fileId);
		return FileVersionManage.jump(version);
	}

	public String getNextVersion(Long fileId) {
		String version = dao.getCurVersion(fileId);
		return FileVersionManage.getNext(version);
	}

	/**
	 * 分享文件
	 * */
	public void saveShareFile(Long[] ids) {
		for (Long id : ids) {
			SysFile sysFile = dao.getById(id);
			sysFile.setShared(SysFile.SHARED_TRUE);
			dao.update(sysFile);
		}
	}

	public List<SysFile> getSysFileByFilePath(String filepath) {
		return dao.getSysFileByFilePath(filepath);
	}

	/**
	 * 关闭分享
	 * **/
	public void saveCloseShare(Long[] ids) {
		for (Long id : ids) {
			SysFile sysFile = dao.getById(id);
			sysFile.setShared(SysFile.SHARED_FALSE);
			dao.update(sysFile);
		}
	}

	/**
	 * 通过数据id获取附件信息
	 * **/
	public SysFile getFileByDataId(String dataId) {
		return dao.getFileByDataId(dataId);
	}

	/**
	 * 查找文件夹下的所有文件
	 * */
	public List<SysFile> getSysFileByFolder(Long folderId) {
		return dao.getSysFileByFolder(folderId);
	}

	/**
	 * 根据文件夹查找文件-用于附件管理
	 * */
	public List<SysFile> getSysFileByFolder(Long userId, Boolean isRootFolder,
			Boolean isSharedFolder, String folderPath, QueryFilter filter) {
		return dao.getSysFileByFolder(userId, isRootFolder, isSharedFolder,
				folderPath, filter);
	}

	/**
	 * 根据文件ID获取文件信息
	 *
	 * @param filter
	 * @return
	 */
	public List<SysFile> getSysFileByFilter(QueryFilter filter) {
		return dao.getSysFileByFilter(filter);
	}

	/**
	 * 获取文件下一个ID值
	 *
	 * @param
	 * @return
	 */
	public Long doNextVal() {
		return UniqueIdUtil.genId();
	}

	/**
	 * 压缩文件
	 *
	 * @param zipOutStream
	 * @param filePath
	 * @param fileName
	 */
	public void compressFile(ZipOutputStream zipOutStream, String filePath,
			String fileName) {
		SysFileUtil.compressFile(zipOutStream, filePath, fileName, false);
	}

	/**
	 * 文件导入：文件上传到指定位置、文件表记录文件信息
	 */
	@Override
	public void importSysFile(List<? extends ISysFile> sysFileList,
			String unzipFilePath, String realPath) {
		for (ISysFile sysFile : sysFileList) {
			Long id = sysFile.getFileId();
			SysFile file = this.getById(id);
			sysFile = this.parseSysFile(sysFile, realPath, unzipFilePath);
			Object[] args = { id };
			if (BeanUtils.isEmpty(file)) {
				this.add((SysFile) sysFile);
				MsgUtil.addMsg(
						MsgUtil.SUCCESS,
						getText("service.bpmDefinition.import.sysFile.isNew",
								args));
			} else {
				BeanUtils.copyNotNullProperties(file, sysFile);
				file.setCreatetime(sysFile.getCreatetime());
				this.update(file);
				MsgUtil.addMsg(
						MsgUtil.WARN,
						getText("service.bpmDefinition.import.sysFile.isExist",
								args));
			}
		}

	}

	/**
	 * 将文件拷贝到指定位置
	 *
	 * @param sysFile
	 *            ： 文件属性信息bean
	 * @param realPath
	 *            ： 拷贝文件放置的目的地
	 * @param unzipFilePath
	 *            ： 待拷贝文件的位置（不含文件名称）
	 */
	public void copyToSysFile(ISysFile sysFile, String realPath,
			String unzipFilePath) {
		try {
			realPath = realPath + File.separator
					+ sysFile.getFilepath().replace("/", File.separator);
			String fileName = sysFile.getFileId() + "." + sysFile.getExt();
			String filePath = unzipFilePath + fileName;
			// 复制到指定文件
			File file = new File(filePath);
			if (file.exists()) {
				FileOperator.createFolderFile(realPath);
				FileOperator.copyFile(filePath, realPath);
			} else {
				MsgUtil.addMsg(
						MsgUtil.ERROR,
						getText("service.bpmDefinition.import.sysFile.notExist",
								new Object[] { fileName }));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param sysFile
	 *            文件属性信息bean
	 * @param realPath
	 *            ： 拷贝文件放置的目的地
	 * @param unzipFilePath
	 *            ： 待拷贝文件的位置（不含文件名称）
	 * @return
	 */
	private ISysFile parseSysFile(ISysFile sysFile, String realPath,
			String unzipFilePath) {
		ISysUser sysUser = (ISysUser) UserContextUtil.getCurrentUser();
		sysFile.setCreatorId(sysUser.getUserId());
		sysFile.setCreator(sysUser.getUsername());
		this.copyToSysFile(sysFile, realPath, unzipFilePath);
		return sysFile;
	}

	public void replaceFileId(Map<String, Object> rowValue, IFormField field,
			Map<Long, ? extends ISysFile> fileMap) {
		Short controlType = field.getControlType();
		if (controlType.shortValue() == IFieldPool.ATTACHEMENT) {
			Object fileJson = rowValue
					.get((ITableModel.CUSTOMER_COLUMN_PREFIX + field
							.getFieldName()).toLowerCase());
			if (!BeanUtils.isEmpty(fileJson)) {
				String json = fileJson.toString().replace("￥@@￥", "\"");
				JSONArray arr = JSONArray.fromObject(json);
				for (int i = 0; i < arr.size(); i++) {
					JSONObject j = arr.getJSONObject(i);
					String fileId = j.getString("id");
					ISysFile file = fileMap.get(Long.valueOf(fileId));
					if (!BeanUtils.isEmpty(file)) {
						j.put("id", file.getFileId());
					}
				}
				rowValue.put((ITableModel.CUSTOMER_COLUMN_PREFIX + field
						.getFieldName()).toLowerCase(), arr.toString());
			}

		}
	}

	/*
	 * @Override public void fixSysFileId(IFormData syncData, ISyncModel
	 * syncModel) { //保存SysFile Map<Long,SysFile> fileMap = new
	 * HashMap<Long,SysFile>(); for(ISysFile file:syncModel.getSysFiles()){
	 * List<SysFile> files = this.getSysFileByFilePath(file.getFilepath()); Long
	 * oldFileId = file.getFileId(); if(files.size()>0){//有同名记录,更新 SysFile
	 * oldFile = files.get(0); file.setFileId(oldFile.getFileId());
	 * this.update((SysFile)file); }else{ file.setFileId(null);
	 * this.add((SysFile)file); } fileMap.put(oldFileId, (SysFile)file);
	 *
	 * } List<? extends IFormField> list =
	 * syncData.getFormTable().getFieldList(); Map<String,Object> mainField =
	 * syncData.getMainFields(); for(IFormField field:list){
	 * replaceFileId(mainField,field,fileMap); } for(ISubTable
	 * subTable:syncData.getSubTableList()){ IFormTable table =
	 * this.formTableService.getByTableName(subTable.getTableName(), 1); List<?
	 * extends IFormField> subFields = table.getFieldList();
	 * for(Map<String,Object> rowValue:subTable.getDataList()){ for(IFormField
	 * f:subFields){ replaceFileId(rowValue,f,fileMap); } } } }
	 */
	@Override
	public Map<Long, SysFile> fixSysFileId(ISyncModel syncModel) {
		Map<Long, SysFile> fileMap = new HashMap<Long, SysFile>();
		for (ISysFile file : syncModel.getSysFiles()) {
			List<SysFile> files = this.getSysFileByFilePath(file.getFilepath());
			Long oldFileId = file.getFileId();
			if (files.size() > 0) {// 有同名记录,更新
				ISysFile oldFile = files.get(0);
				file.setFileId(oldFile.getFileId());
				this.update((SysFile) file);
			} else {
				file.setFileId(null);
				this.add((SysFile) file);
			}
			fileMap.put(oldFileId, (SysFile) file);
		}
		return fileMap;
	}

	/**
	 * 用户下附件所有附件
	 *
	 * @author YangBo @date 2016年10月18日上午9:57:53
	 * @param fileter
	 * @return
	 */
	public List<SysFile> getFileAttch(QueryFilter fileter) {
		return this.dao.getFileAttch(fileter);
	}

	/**
	 * 将明细多Tab的html插入到数据库字段
	 *
	 * @author YangBo @date 2016年10月20日下午2:50:59
	 * @param id
	 * @throws IOException
	 */
	public void saveMultiTabHtml(Long id) {
		IDataTemplate dataTemplate = dataTemplateService.getById(id);
		String multiTabTempHtml = getMultiTabJSPToStr();
		// 插入数据字段
		dataTemplate.setMultiTabTempHtml(multiTabTempHtml);
		dataTemplateService.update(dataTemplate);
	}

	/**
	 * 将文件附件html文件插入数据库字段
	 *
	 * @author YangBo @date 2016年10月20日下午2:50:59
	 * @param id
	 * @throws IOException
	 */
	public void saveFileAndAttachHtml(Long id) {
		IDataTemplate dataTemplate = dataTemplateService.getById(id);
		String fileTempHtml = getFileJSPToStr();
		String attacTempHtml = getAttachJSPToStr();
		// 插入数据字段
		dataTemplate.setFileTempHtml(fileTempHtml);
		dataTemplate.setAttacTempHtml(attacTempHtml);
		dataTemplateService.update(dataTemplate);
	}

	/**
	 * 将流程监控html文件插入数据库字段
	 *
	 * @author Liubo
	 * @date 2017-01-17 10:50:19
	 * @param id
	 * @throws IOException
	 */
	public void saveProcessTempHtml(Long id) {
		IDataTemplate dataTemplate = dataTemplateService.getById(id);
		String processTempHtml = getProcessTempJSPToStr();
		// 插入数据字段
		dataTemplate.setProcessTempHtml(processTempHtml);
		dataTemplateService.update(dataTemplate);
	}

	/**
	 * 将流程监控条件插入数据库字段
	 *
	 * @author Liubo
	 * @date 2017-01-18
	 * @param id
	 * @throws IOException
	 */
	public void saveProcessCondition(Long id) {
		IDataTemplate dataTemplate = dataTemplateService.getById(id);
		String processTempHtml = getProcessTempJSPToStr();
		// 插入数据字段
		dataTemplate.setProcessTempHtml(processTempHtml);
		dataTemplateService.update(dataTemplate);
	}

	/**
	 * 生成文件附件临时JSP文件
	 *
	 * @author YangBo @date 2016年10月20日下午3:43:27
	 * @param id
	 */
	public void createFileAttachJSP(Long id) {

		IDataTemplate dataTemplate = dataTemplateService.getById(id);
		String fileTempHtml = dataTemplate.getFileTempHtml();
		String processTempHtml = dataTemplate.getProcessTempHtml();
		String attacTempHtml = dataTemplate.getAttacTempHtml();
		String root = FileUtil.getRootPath();
		String filePathName = root + File.separator + "attachFile"
				+ File.separator + "fileView" + id + ".jsp";
		String processPathName = root + File.separator + "attachFile"
				+ File.separator + "fileView" + id + ".jsp";
		String attachPathName = root + File.separator + "attachFile"
				+ File.separator + "fileList" + id + ".jsp";

		// 清除临时JSP
		boolean existFile = FileOperator.isFileExist(filePathName);
		boolean existProcess = FileOperator.isFileExist(processPathName);
		boolean existAttach = FileOperator.isFileExist(attachPathName);
		if (existFile || existAttach) {
			FileOperator.deleteFile(filePathName);
			FileOperator.deleteFile(attachPathName);
		}
		// 生成临时文件
		FileOperator.createFile(filePathName);
		FileOperator.createFile(attachPathName);
		FileOperator.writeFile(filePathName, fileTempHtml);
		FileOperator.writeFile(attachPathName, attacTempHtml);
	}

	/**
	 * 生成流程监控临时JSP文件
	 *
	 * @author Liubo
	 * @date 2017-01-17
	 * @param id
	 */
	public void createProcessJSP(Long id) {

		IDataTemplate dataTemplate = dataTemplateService.getById(id);
		String processTempHtml = dataTemplate.getProcessTempHtml();
		String root = FileUtil.getRootPath();
		String processPathName = root + File.separator + "attachFile"
				+ File.separator + "processView" + id + ".jsp";

		// 清除临时JSP
		boolean existProcess = FileOperator.isFileExist(processPathName);
		if (existProcess) {
			FileOperator.deleteFile(processPathName);
		}
		// 生成临时文件
		FileOperator.createFile(processPathName);
		FileOperator.writeFile(processPathName, processTempHtml);
	}

	/**
	 * 获取明细多Tab展示的html
	 *
	 * @author dengwenjie @date 2017年4月1日11:11:35
	 * @return
	 * @throws IOException
	 */
	public String getMultiTabJSPToStr() {
		// jsp路径
		String path = SysConfConstant.CONF_ROOT + File.separator + "template"
				+ File.separator + "jsp" + File.separator;
		String jsp = FileOperator.readFile(path + SysFile.MLUTITAB_VIEW
				+ ".jsp");
		return jsp;

	}

	public void createMultiTabJSP(Long id) {

		IDataTemplate dataTemplate = dataTemplateService.getById(id);
		String multiTabTempHtml = dataTemplate.getMultiTabTempHtml();
		String root = FileUtil.getRootPath();
		String multiTabPathName = root + File.separator + "attachFile"
				+ File.separator + SysFile.MLUTITAB_VIEW + id + ".jsp";

		// 清除临时JSP
		boolean exist = FileOperator.isFileExist(multiTabPathName);
		if (exist) {
			FileOperator.deleteFile(multiTabPathName);
		}
		// 生成临时文件
		FileOperator.createFile(multiTabPathName);
		FileOperator.writeFile(multiTabPathName, multiTabTempHtml);
	}

	/**
	 * 获取文件夹html
	 *
	 * @author YangBo @date 2016年10月20日下午4:03:09
	 * @return
	 * @throws IOException
	 */
	public String getFileJSPToStr() {
		// jsp路径
		String path = SysConfConstant.CONF_ROOT + File.separator + "template"
				+ File.separator + "jsp" + File.separator;
		String jsp = FileOperator.readFile(path + SysFile.FILEVIEW + ".jsp");
		return jsp;

	}

	/**
	 * 获取流程监控模板
	 *
	 * @author liubo
	 * @date 2017-01-17 10:53:28
	 * @return
	 */
	public String getProcessTempJSPToStr() {
		// jsp模板路径
		String path = SysConfConstant.CONF_ROOT + File.separator + "template"
				+ File.separator + "jsp" + File.separator;
		String jsp = FileOperator.readFile(path + SysFile.PROCESSVIEW + ".jsp");
		return jsp;
	}

	/**
	 * 获取文附件列表html
	 *
	 * @author YangBo @date 2016年10月20日下午4:16:27
	 * @return
	 */
	public String getAttachJSPToStr() {
		String path = SysConfConstant.CONF_ROOT + File.separator + "template"
				+ File.separator + "jsp" + File.separator;
		String jsp = FileOperator.readFile(path + SysFile.FILELIST + ".jsp");
		return jsp;
	}

	/**
	 * 此处用来处理脚本中传的参数获取附件列表 获取本身类别及其子类别的文件
	 *
	 * @throws Exception
	 */
	public List<SysFile> getAttachList(HttpServletRequest request,
			JSONObject jsonObject, Long typeId, Long dataId, Long tableId)
			throws Exception {
		return getAttachList(request, jsonObject, typeId, dataId, tableId,
				false);
	}

	/**
	 * 此处用来处理脚本中传的参数获取附件列表
	 *
	 * @author YangBo @date 2016年11月4日上午11:20:12
	 * @param request
	 * @param typeId
	 * @param dataId
	 * @param tableId
	 * @return
	 */
	public List<SysFile> getAttachList(HttpServletRequest request,
			JSONObject jsonObject, Long typeId, Long dataId, Long tableId,
			boolean onlySelfType) throws Exception {

		QueryFilter filter = new QueryFilter(request, "sysFileItem");

		Long maindata = JSONUtil.getLong(jsonObject, "maindata", 0L);// 用于判断是否显示该记录所在表的所有附件：1.显示,0不显示
		String mainfield = JSONUtil.getString(jsonObject, "mainfield");// 用于显示该记录有关附件字段的相关附件：附件字段名
		String reldata = JSONUtil.getString(jsonObject, "reldata");// 用于表示关联关系表下的附件
																	// 关联表：关联字段：附件字段#。。。(多表关联)
		String reldatafield = JSONUtil.getString(jsonObject, "reldatafield");// 用于获取相关表的附件
																				// 关联表：关联字段：附件字段#。。。。(多表关联)

		/* 查询条件BEGIN */
		String fileName = RequestUtil.getString(request, "Q_fileName_SL");
		if (fileName.length() != 0) {
			filter.addFilterForIB("filename", fileName);
		}
		String creator = RequestUtil.getString(request, "Q_creator_SL");
		if (creator.length() != 0) {
			filter.addFilterForIB("creator", creator);
		}
		String ext = RequestUtil.getString(request, "Q_ext_SL");
		if (ext.length() != 0) {
			filter.addFilterForIB("ext", ext);
		}

		Date beginTime = RequestUtil.getDate(request, "Q_begincreatetime_DL");
		if (beginTime != null) {
			filter.addFilterForIB("beginTime", beginTime);
		}

		Date endTime = RequestUtil.getDate(request, "Q_endcreatetime_DG");
		if (endTime != null) {
			filter.addFilterForIB("endTime", endTime);
		}

		// 对查看当前人员下的附件也通过密级筛选
		// 获取密级参数数据
		List<SysParameter> spDatas = sysParameterService
				.getByParamName(ISysFile.IS_DISPLAY_SECURITY);
		if (spDatas.size() > 0 && "1".equals(spDatas.get(0).getParamvalue())) {
			String securityConditions = getFileConditions("SECURITY_");
			if (securityConditions != null) {
				filter.addFilterForIB("securityConditions", securityConditions);
			}
		}

		/* 查询条件END */
		// 初始化sql变量
		String sql = "";

		// 关联表关联字段查询
		if (reldata != null && "".equals(reldatafield) && reldata.length() != 0) {
			String[] oneDatas = reldata.split("#");
			for (int i = 0; i < oneDatas.length; i++) {
				String[] relTabledata = oneDatas[i].split(":");
				String relTableName = relTabledata[0];
				String relTableField = relTabledata[1];
				String relField = ITableModel.CUSTOMER_COLUMN_PREFIX
						+ relTableField.toUpperCase();
				String relTableNameW = ITableModel.CUSTOMER_TABLE_PREFIX
						+ relTableName.toUpperCase();
				if (relField.length() != 0 && relTableName.length() != 0) {
					String relFileIdDataId = "";
					sql += dao.getSql(relTableName, relTableNameW,
							relFileIdDataId, relField, dataId);
				}
			}

		} else if (reldatafield != null && reldatafield.length() != 0) {// 存在附件字段
			String[] twoDatas = reldatafield.split("#");
			for (int i = 0; i < twoDatas.length; i++) {
				String[] relTabledata = twoDatas[i].split(":");

				String relTableName = relTabledata[0];
				String relTableNameW = ITableModel.CUSTOMER_TABLE_PREFIX
						+ relTableName.toUpperCase();// 关联表名完全名称

				String relTableField = relTabledata[1];
				String relField = ITableModel.CUSTOMER_COLUMN_PREFIX
						+ relTableField.toUpperCase(); // 关联表字段

				if (relField.length() != 0 && relTableName.length() != 0) {
					String relTableDataField = relTabledata[2];// fjsh字段
					// 关联表字段查询ids
					List<? extends IFormField> formFieldList = formFieldService
							.getRelFieldsByTableId(null, tableId);
					String relFileIdDataId = "";
					for (IFormField formField : formFieldList) {
						if (relTableField.equals(formField.getFieldName())) {
							IFormTable relFormTable = formTableService
									.getById(formField.getTableId());
							relFileIdDataId = this.getMainFieldDataIds(
									relTableDataField, relTableName, dataId,
									relFormTable, Long.valueOf(2), relField);
						}
					}
					if (relFileIdDataId.length() != 0)
						sql += dao.getSql(relTableName, relTableNameW,
								relFileIdDataId, relField, dataId);
				}
			}

		}
		// 拼接的sql
		if (sql.length() != 0)
			filter.addFilterForIB("addSql", sql);

		// 主表附件字段查询ids
		if (mainfield.length() != 0) {
			IFormTable formTable = formTableService.getById(tableId);
			String tableName = formTable.getTableName();
			String mainFieldDataId = this.getMainFieldDataIds(mainfield,
					tableName, dataId, formTable, Long.valueOf(1), null);
			if (mainFieldDataId.length() != 0) {
				filter.addFilterForIB("mainFileIdId", mainFieldDataId);
			}
		}

		Long cateTypeId = this.sysTypeKeyService.getByKey("FILE")
				.getTypeKeyId();

		String typeIds = "";
		/* add by dwj onlySelfType：{true:只获取自己本身类别的文件，false:获取本身类别及其子类别的文件} */
		if (onlySelfType) {
			if (dataId.longValue() != 0L) {
				if (typeId.longValue() != 0L) {
					filter.addFilterForIB("protypeId", typeId);
					List<String> dimensionList = Arrays.asList(typeId
							.toString());
					filter.addFilterForIB("dimensionList", dimensionList);
				}
				filter.addFilterForIB("protypeId", typeId);
			} else {
				if ((typeId.longValue() != 0L) && (!typeId.equals(cateTypeId))) {
					filter.addFilterForIB("protypeId", typeId);
					List<String> dimensionList = Arrays.asList(typeId
							.toString());
					filter.addFilterForIB("dimensionList", dimensionList);
				} else {
					List<String> dimensionList = new ArrayList();
					filter.addFilterForIB("dimensionList", dimensionList);
					filter.addFilterForIB("protypeId", null);
				}
			}
		} else {
			if (dataId.longValue() != 0L) {
				if (typeId.longValue() == 0L) {
					typeIds = sysFileTypeService.getIdsByParentId(cateTypeId,
							dataId);
					List<String> dimensionList = new ArrayList();
					filter.addFilterForIB("dimensionList", dimensionList);
				} else {
					typeIds = sysFileTypeService.getIdsByParentId(typeId,
							dataId);
					filter.addFilterForIB("protypeId", typeIds);
					String[] ids = typeIds.split(",");
					List<String> dimensionList = Arrays.asList(ids);
					filter.addFilterForIB("dimensionList", dimensionList);
				}
				filter.addFilterForIB("protypeId", typeIds);
			} else {
				if ((typeId.longValue() != 0L) && (!typeId.equals(cateTypeId))) {
					typeIds = globalTypeService.getIdsByParentId(typeId);
					filter.addFilterForIB("protypeId", typeIds);
					String[] ids = typeIds.split(",");
					List<String> dimensionList = Arrays.asList(ids);
					filter.addFilterForIB("dimensionList", dimensionList);
				} else {
					List<String> dimensionList = new ArrayList();
					filter.addFilterForIB("dimensionList", dimensionList);
					filter.addFilterForIB("protypeId", null);
				}
			}
		}

		// 此处呈现最新版本
		filter.addFilterForIB("isnew", SysFile.ISNEW_VERSION);

		if (maindata == 0L) {
			// 表单一行记录id控制
			filter.addFilterForIB("tableId", tableId);
			if (dataId.longValue() != 0L)
				filter.addFilterForIB("dataId", dataId);
		} else {
			// 业务表控制
			if (tableId.longValue() != 0L)
				filter.addFilterForIB("tableId", tableId);
		}
		// 查找附件列表
		try {
			List<SysFile> list = this.getFileAttch(filter);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取附件列表出错");
			return null;
		}

	}

	/**
	 * 获取业务表中一行记录附件上传字段的id
	 *
	 * @author YangBo @date 2016年11月4日上午11:12:16
	 * @param MainField
	 * @param tableName
	 * @param
	 * @param formTable
	 * @param flag
	 * @param relField
	 * @return
	 * @throws Exception
	 */
	public String getMainFieldDataIds(String MainField, String tableName,
			Long dataId, IFormTable formTable, Long flag, String relField)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		String dataIdS = "";
		// 转成数据库中的字段和表名
		String field = ITableModel.CUSTOMER_COLUMN_PREFIX
				+ MainField.toUpperCase();
		tableName = ITableModel.CUSTOMER_TABLE_PREFIX + tableName.toUpperCase();
		sql.append("SELECT ").append(field).append(" FROM ").append(tableName);
		if (flag.longValue() == 1) {
			sql.append(" WHERE ID=").append(dataId);
		} else {
			sql.append(" WHERE ").append(relField).append("=").append(dataId);
		}

		JdbcHelper jdbcHelper = dataTemplateService.getJdbcHelper(formTable);
		try {
			List<Map> list = jdbcHelper.queryForList(sql.toString(), null);
			for (int i = 0; i < list.size(); i++) {
				Map map = list.get(i);
				Object o = map.get(field);
				if (o != null) {
					JSONArray json = JSONArray.fromObject(o);
					List<Map<String, String>> list1 = (List) json;
					for (Map<String, String> m : list1) {
						for (String key : m.keySet()) {
							if (key.equals("id"))
								dataIdS += m.get(key) + ",";
						}

					}
				}
			}
			if (dataIdS.length() != 0)
				dataIdS = dataIdS.substring(0, dataIdS.length() - 1);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("取附件上传字段ID错误");
		}
		return dataIdS;

	}

	public void downAttach(HttpServletRequest request,
			HttpServletResponse response, String fileId) throws IOException {
		// 附件保存路径
		String saveType = PropertyUtil.getSaveType();
		String fileName = "";
		// by weilei:支持打包批量下载
		String[] fileIds = fileId.split(",");
		if (fileIds.length > 1) { // zip
			QueryFilter filter = new QueryFilter(request, "");
			List<SysFile> sysFileList = this.getSysFileByFilter(filter);
			fileName = new SimpleDateFormat("yyyyMMddHHmmsssss")
					.format(new Date()) + ".zip";
			// 下载压缩包方法
			this.downByZip(request, response, fileName, sysFileList);

		} else {
			SysFile sysFile = this.getById(Long.valueOf(fileId));
			if (sysFile == null)
				return;
			fileName = sysFile.getFilename() + "." + sysFile.getExt();
			// by yangBo 存储类型为database
			if (saveType.contains("database")) {
				FileUtil.downLoadFileByByte(request, response,
						sysFile.getFileBlob(), fileName);
			} else {
				// by liubo 判断是本地下载还是服务器下载
				String filePath = sysFile.getFilepath();
				if (StringUtil.isEmpty(filePath))
					return;
				Boolean fileType = filePath.startsWith("group");
				if (fileType) {
					// 对加密文件进行解密处理
					String destFilePath = getDecodeFilePath(filePath, fileName,
							sysFile.getIsEncrypt(), true);
					if (!"".equals(destFilePath)) {
						FileOperator.downLoadFile(request, response,
								destFilePath, fileName);
						// 删除解密文件
						File destFile = new File(destFilePath);
						if (destFile.exists()) {
							destFile.delete();
						}
					} else {
						try {
							FastDFSFileOperator.download(request, response,
									filePath, fileName);
						} catch (MyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					// 附件保存路径
					String attachPath = AppUtil.getAttachPath();
					String fullPath = StringUtil.trimSufffix(attachPath,
							File.separator)
							+ File.separator
							+ filePath.replace("/", File.separator);

					// by weilei:对加密文件进行解密处理
					String destFilePath = getDecodeFilePath(fullPath, fileName,
							sysFile.getIsEncrypt(), false);
					if (!"".equals(destFilePath)) {
						FileOperator.downLoadFile(request, response,
								destFilePath, fileName);
						// 删除解密文件
						File destFile = new File(destFilePath);
						if (destFile.exists()) {
							destFile.delete();
						}
					} else {
						FileOperator.downLoadFile(request, response, fullPath,
								fileName);
					}
				}
			}
		}
	}

	/**
	 * 获取文件加密后的路径
	 *
	 * @author liubo
	 * @param origFilePath
	 * @param oriFileName
	 * @return
	 */
	public String getEcryptFilePath(String origFilePath, String oriFileName) {
		// 附件保存路径
		String attachPath = AppUtil.getAttachPath();
		String destFilePath = "";
		try {
			// step 1:判断是否需要进行加密
			List<SysParameter> spDatas = sysParameterService
					.getByParamName("IS_SAVE_SECURITY");
			if (spDatas.size() > 0
					&& "是".equals(spDatas.get(0).getParamvalue())) {
				spDatas.clear();
				// step 2:获取Key名称
				spDatas = sysParameterService.getByParamName("FILE_KEY_NAME");
				if (spDatas.size() > 0) {
					FileOperator.keyName = spDatas.get(0).getParamvalue();
					destFilePath = SysFileUtil.createFilePath(attachPath
							+ File.separator, FileOperator
							.generateFilenameNoSemicolon(oriFileName));
					// step 3:生成加密文件
					FileOperator.encrypt(origFilePath, destFilePath);
					File origFile = new File(origFilePath);
					File destFile = new File(destFilePath);
					if (destFile.exists()) {
						// step 4:删除原始文件
						origFile.delete();
						// step 5:加密文件重命名
						FileOperator.reNameFile(destFilePath, origFilePath);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return destFilePath;
	}

	/**
	 * 获取文件解密后的路径
	 *
	 * @author liubo
	 * @param origFilePath
	 * @param oriFileName
	 * @param isEcrypt
	 *            是否已加密
	 * @param isFastDFS
	 *            是否为分布式存储
	 * @return
	 */
	public String getDecodeFilePath(String origFilePath, String oriFileName,
			Long isEcrypt, boolean isFastDFS) {
		// 附件保存路径
		String attachPath = AppUtil.getAttachPath();
		String destFilePath = "";
		try {
			// step 1:判断是否需要进行解密
			if (isEcrypt == 1L) {
				// step 2:获取Key名称
				List<SysParameter> keyName = sysParameterService
						.getByParamName("FILE_KEY_NAME");

				if (keyName.size() > 0) {
					FileOperator.keyName = keyName.get(0).getParamvalue();
					destFilePath = SysFileUtil.createFilePath(attachPath
							+ File.separator + "preview" + File.separator,
							FileOperator
									.generateFilenameNoSemicolon(oriFileName));
					// step 3:生成解密文件
					// 对服务器和本地存储的文件解密分别处理
					if (isFastDFS) {
						byte[] content = FastDFSFileOperator
								.getFileByte(origFilePath);
						// FileOperator.writeByte("D:/ibms/attachFile/preview/2017070198.txt",
						// content);
						FileOperator.decrypt(content, destFilePath);
					} else {
						FileOperator.decrypt(origFilePath, destFilePath);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return destFilePath;
	}

	/**
	 * 压缩下载 by YangBo
	 *
	 * @param request
	 * @param response
	 * @param fileName
	 * @param sysFileList
	 * @throws IOException
	 */
	public void downByZip(HttpServletRequest request,
			HttpServletResponse response, String fileName,
			List<SysFile> sysFileList) throws IOException {

		// 压缩包路径
		String fullPath = AppUtil.getAttachPath() + File.separator + fileName;
		File file = new File(fullPath);
		if (!file.exists())
			file.createNewFile();
		// 服务器上创建文件压缩包
		ZipOutputStream zos = new ZipOutputStream(file);
		zos.setEncoding("GBK");
		for (SysFile obj : sysFileList) {
			String filename = obj.getFilename() + "." + obj.getExt();
			String filepath = AppUtil.getAttachPath() + File.separator
					+ String.valueOf(obj.getFilepath());
			Boolean isNoGroup = obj.getFilepath().startsWith("group");// 判断是否分布式文件
			String destFilePath = "";
			if (isNoGroup) {// 分布式文件存储解密操作
				filepath = obj.getFilepath();
				// by liubo:对加密文件进行解密处理
				destFilePath = getDecodeFilePath(filepath, filename,
						obj.getIsEncrypt(), true);
				if (!"".equals(destFilePath)) {
					filepath = destFilePath;
					// 分布式文件进行解密处理后就按照本地文件进行压缩等操作
					isNoGroup = false;
				}
			} else {
				// by weilei:对加密文件进行解密处理
				destFilePath = getDecodeFilePath(filepath, filename,
						obj.getIsEncrypt(), false);
				if (!"".equals(destFilePath)) {
					filepath = destFilePath;
				}
			}

			// 将附件压缩进zos压缩包中
			SysFileUtil.compressFile(zos, filepath, filename, isNoGroup);
			// 删除解密文件
			File destFile = new File(destFilePath);
			if (destFile.exists()) {
				destFile.delete();
			}
		}
		zos.flush();
		zos.close();
		// 下载文件到本地，文件源地址为fullPath，下载后名称为fileName
		FileOperator.downLoadFile(request, response, fullPath, fileName);
		// 删除zip文件
		File zipFile = new File(fullPath);
		if (zipFile.exists())
			zipFile.delete();
	}

	/**
	 * 上传附件
	 *
	 * @author YangBo @date 2016年11月1日下午10:25:08
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String uploadAttach(MultipartHttpServletRequest request,
			HttpServletResponse response, ISysUser appUser, long userId)
			throws IOException, MyException {

		// 附件保存路径
		String saveType = PropertyUtil.getSaveType();
		String msg = "";// 返回执行情况

		long typeId = RequestUtil.getLong(request, "typeId", -1); // globalType分类的id
		long dataId = RequestUtil.getLong(request, "dataId", 0); // 业务表记录的id
		long tableId = RequestUtil.getLong(request, "tableId", 0); // 业务表的id
		long fileId = RequestUtil.getLong(request, "fileId", 0); // 上一版本的id
		long maxSize = RequestUtil.getLong(request, "maxSize", Long.MAX_VALUE);

		String photoType = RequestUtil.getString(request, "photoType");

		String describe = RequestUtil.getString(request, "describe");

		String ckResultName = CommonTools.null2String(RequestUtil.getString(
				request, "ckResultName")); // 检查结果的表名
		ckResultName = !ckResultName.equalsIgnoreCase("") ? ckResultName
				: CommonTools
						.Obj2String((request.getAttribute("ckResultName")));
		if (!"".equals(ckResultName)) {
			ckResultName = "&&" + ckResultName;
		}

		String storePath = "";
		if (dataId == 0 && tableId == 0) {
			Calendar cal = Calendar.getInstance();
			storePath = "" + cal.get(Calendar.YEAR) + File.separator
					+ (cal.get(Calendar.MONTH) + 1) + File.separator
					+ cal.get(Calendar.DAY_OF_MONTH);
		} else {
			storePath = "" + tableId + File.separator + dataId; // 当dataId为0时都是无记录的，挂在tableId下
		}
		if ("uploadsignphoto".equals(photoType)) {
			int count = dao.getFileDataByDataId(String.valueOf(dataId));
			if (count <= 0) {
				System.out.println(String.valueOf(tableId) + ckResultName
						+ "没有签署项附件");
			} else {
				int flag = dao.deleteFileDataByDataId(String.valueOf(dataId),
						describe);
			}
		} else if ("uploadopphoto".equals(photoType)) {
			int count = dao.getFileDataByTableId(String.valueOf(tableId)
					+ ckResultName);
			if (count <= 0) {
				System.out.println(String.valueOf(tableId) + ckResultName
						+ "没有附件");
			} else {
				int flag = dao.deleteFileDataByTableId(String.valueOf(tableId)
						+ ckResultName, describe);
			}
		}

		GlobalType globalType = null;
		if (typeId > 0L) {
			globalType = (GlobalType) this.globalTypeService.getById(Long
					.valueOf(typeId));
			if (dataId > 0) {
				SysFileType sysFileType = sysFileTypeService.getFileType(
						Long.valueOf(typeId), dataId);
				globalType = new GlobalType(sysFileType);
			}
		}

		Long fileTypeId = RequestUtil.getLong(request, "fileTypeId", -1);// folder的Id
		String fileFormates = RequestUtil.getString(request, "fileFormates");// 格式要求
		boolean mark = true;
		boolean fileSize = true;// 文件大小符合判断
		// 获取上传的文件流
		Map<String, MultipartFile> files = request.getFileMap();
		Iterator<MultipartFile> it = files.values().iterator();

		while (it.hasNext()) {
			MultipartFile f = it.next();
			String oriFileName = f.getOriginalFilename();
			String extName = FileOperator.getSuffix(oriFileName);
			String fileIndex = RequestUtil.getString(request, "fileIndex");// 文件位置
			if (f.getSize() > maxSize) {// 文件大小超过最大值
				fileSize = false;
			}

			if (StringUtil.isNotEmpty(fileFormates)) { // 文件格式要求
				if (!(fileFormates.contains("*." + extName))) { // 不符合文件格式要求的就标志为false
					mark = false;
				}
			}

			if (mark && fileSize) {
				List<SysParameter> fastDFS = sysParameterService
						.getByParamName("IS_FILE_FASTDFS");
				Long id = fastDFS.get(0).getId();
				String paramvalue = fastDFS.get(0).getParamvalue();

				String relativeFullPath = FileOperator
						.generateFilenameNoSemicolon(oriFileName);
				// 附件保存路径
				String attachPath = AppUtil.getAttachPath();
				String filePath = "";
				SysFile sysFile = new SysFile();
				sysFile.setFileId(UniqueIdUtil.genId());

				// By weilei：添加文件上传参数：密级、描述
				String security = RequestUtil.getString(request, "security");
				if ("null".equals(security))
					security = "";
				// String describe = RequestUtil.getString(request, "describe");
				// By dwj：添加维度属性
				String dimension = RequestUtil.getString(request, "dimension",
						"");
				if (!dimension.equals("")) {
					/* 维度进行了设置、关闭文件类别 */
					typeId = 0L;
					globalType.setTypeId(0L);
				}
				// SysParameter fastDFS1 = fastDFS.get(0);
				sysFile.setSecurity(security);
				sysFile.setDescribe(describe);
				sysFile.setDimension(dimension);

				// 获取系统是否加密的参数
				List<SysParameter> needEncrypt = sysParameterService
						.getByParamName("IS_SAVE_SECURITY");
				if (needEncrypt.size() > 0
						&& "是".equals(needEncrypt.get(0).getParamvalue())) {
					// 给文件是否加密属性设置为加密
					sysFile.setIsEncrypt(1L);
				}

				if (saveType.contains("database"))
					sysFile.setFileBlob(f.getBytes());
				else {
					// by liubo 根据参数判断是本地存储还是分布式文件存储
					if (StringUtil.isNotEmpty(paramvalue)
							&& paramvalue.equals("1")) {

						// 判断是否需要进行加密
						if (needEncrypt.size() > 0
								&& "是".equals(needEncrypt.get(0)
										.getParamvalue())) {
							needEncrypt.clear();
							// 获取加密Key名称
							needEncrypt = sysParameterService
									.getByParamName("FILE_KEY_NAME");
							if (needEncrypt.size() > 0) {
								try {
									Cipher cipher = Cipher.getInstance("DES");
									Key key = FileOperator.getKey(needEncrypt
											.get(0).getParamvalue());
									// 设置key值
									FileOperator.keyName = needEncrypt.get(0)
											.getParamvalue();
									cipher.init(Cipher.ENCRYPT_MODE, key);

									InputStream is = new ByteArrayInputStream(
											f.getBytes());
									ByteArrayOutputStream out = new ByteArrayOutputStream();
									CipherInputStream cis = new CipherInputStream(
											is, cipher);

									byte[] buffer = new byte[2048];
									int r;
									while ((r = cis.read(buffer)) > 0) {
										out.write(buffer, 0, r);
									}
									byte[] byteFile = out.toByteArray();
									cis.close();
									is.close();

									// 上传成功后返回路径
									String path = FastDFSFileOperator
											.uploadFile(byteFile, extName);
									filePath = path;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							// 上传成功后返回路径
							String path = FastDFSFileOperator.uploadFile(f,
									extName);
							filePath = path;
						}
					} else {
						filePath = SysFileUtil.createFilePath(attachPath
								+ File.separator + storePath + File.separator,
								relativeFullPath);
						FileOperator.writeByte(filePath, f.getBytes());

						// by weilei:判断是否对文件进行加密处理
						this.getEcryptFilePath(filePath, oriFileName);
					}

				}
				// end 写入物理文件
				// 向数据库中添加数据 FileUtils
				// 附件名称
				sysFile.setFilename(oriFileName.substring(0,
						oriFileName.lastIndexOf('.')));
				Calendar cal = Calendar.getInstance();
				Integer year = cal.get(Calendar.YEAR);
				Integer month = cal.get(Calendar.MONTH) + 1;

				// by liubo 根据参数判断是本地存储还是分布式文件存储
				if (StringUtil.isNotEmpty(paramvalue) && paramvalue.equals("1")) {
					sysFile.setFilepath(filePath); // 保存storageServer服务器上的卷名+文件名
					sysFile.setStoreWay(1L);// by yangbo 这里存储1区分是走分布式存储的
				} else {
					relativeFullPath = storePath + File.separator
							+ relativeFullPath;
					sysFile.setFilepath(relativeFullPath); // 保存相对路径
				}

				// 上传时间
				sysFile.setCreatetime(new java.util.Date());
				// 扩展名
				sysFile.setExt(extName);
				// 字节总数
				sysFile.setTotalBytes(f.getSize());
				// 说明
				sysFile.setNote(FileOperator.getSize(f.getSize()));
				// 当前用户的信息
				if (appUser != null) {
					sysFile.setCreatorId(appUser.getUserId());
					sysFile.setCreator(appUser.getFullname());
				} else {
					sysFile.setCreatorId(UserContextUtil.getCurrentUser()
							.getUserId());
					sysFile.setCreator(UserContextUtil.getCurrentUser()
							.getFullname());
				}
				// 总的字节数
				sysFile.setDelFlag(SysFile.FILE_NOT_DEL);

				// 更新新版本
				if (fileId > 0L) {
					SysFile oldSysFile = this.getById(fileId);
					if (BeanUtils.isEmpty(oldSysFile.getParentId())) {
						oldSysFile.setParentId(fileId);
						sysFile.setParentId(fileId);
					} else {
						sysFile.setParentId(oldSysFile.getParentId());// 初始版本id相同
					}

					oldSysFile.setIsnew(SysFile.ISOLD_VERSION);// 改成旧版本
					// 继承老版本分类节点
					sysFile.setProtypeId(oldSysFile.getProtypeId());
					sysFile.setFileType(oldSysFile.getFileType());
					sysFile.setDimension(oldSysFile.getDimension());

					this.update(oldSysFile);
					// 版本的增加
					String oldVersion = oldSysFile.getVersion();
					sysFile.setVersion(FileVersionManage.getNext(oldVersion));
				} else {

					sysFile.setParentId(sysFile.getFileId());// 初始版本两值一致
					// 类型
					if (globalType != null) {
						sysFile.setProtypeId(globalType.getTypeId());
						sysFile.setFileType(globalType.getTypeName());
					} else {
						// 未选取文件分类或者在folder树上传时默认加分类type根id
						sysFile.setProtypeId(this.sysTypeKeyService.getByKey(
								"FILE").getTypeKeyId());
						sysFile.setFileType(this.sysTypeKeyService.getByKey(
								"FILE").getTypeKey());

					}
				}
				// 0 不开启solr文件搜索服务 1为开启
				// by songchen 创建用于solr检索的索引
				String s = sysParameterService.getByAlias("SOLRSERVER");
				if (s.startsWith("1") && s.contains(extName.toLowerCase())) {
					try {
						SolrFile solrFile = new SolrFile(sysFile);
						solrFile.setBytes(f.getBytes());
						solrService.createIndex(solrFile);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(e.getMessage());
					}
				}

				// 挂在业务表记录下
				if (dataId != 0L)
					sysFile.setDataId(String.valueOf(dataId));
				if (tableId != 0L) {
					sysFile.setTableId(String.valueOf(tableId) + ckResultName);
				}

				SysFileFolder folder = null;
				// 判断是否存在文库节点
				if (null == fileTypeId || fileTypeId <= 0L) {
					folder = sysFileFolderService.getRootFolderByUserId(userId);
					if (BeanUtils.isEmpty(folder)) {// 为空生成新的
						sysFileFolderService.saveFolder(userId);
					}
					folder = sysFileFolderService.getTmpFolderByUserId(userId);
					sysFile.setFolderid(folder.getId());
				} else {
					folder = sysFileFolderService.getById(fileTypeId);
					sysFile.setFolderid(fileTypeId);
				}
				sysFile.setFileatt(SysFile.FILEATT_TRUE);
				sysFile.setFolderPath(folder.getPath());
				sysFile.setShared(null != folder.getSharedNode()
						&& folder.getSharedNode() ? SysFile.SHARED_TRUE
						: SysFile.SHARED_FALSE);
				this.add(sysFile);

				// 暂时保留
				/*
				 * if("pictureShow".equals(uploadType)){
				 * //pictureShow控件要做多一个文件压缩图 //filePath
				 * =filePath.replaceAll(fileName, fileId + "_small"); 压缩后的文件路径
				 * 如D:/1234.jpg 变成 D:/1234_small.jpg int width =
				 * Integer.parseInt
				 * (configproperties.getProperty("compression.width")); String
				 * filePrex = filePath.substring(0, filePath.lastIndexOf("."));
				 * filePath = filePrex + "_small" +
				 * filePath.substring(filePrex.length()); String reutrnStr =
				 * ImageUtil.doCompressByByte(f.getBytes(), filePath, width, 40,
				 * 1, true); logger.info("压缩后的文件："+reutrnStr); }
				 */
				// end 向数据库中添加数据
				msg = "{\"success\":\"true\",\"fileIndex\":\"" + fileIndex
						+ "\",\"fileId\":\"" + sysFile.getFileId()
						+ "\",\"fileName\":\"" + oriFileName + "\"}";
				try {
					List<SysFile> sysFiles;
					if (LogThreadLocalHolder.getParamerter("sysFiles") == null) {
						sysFiles = new ArrayList<SysFile>();
						LogThreadLocalHolder
								.putParamerter("sysFiles", sysFiles);
					} else {
						sysFiles = (List<SysFile>) LogThreadLocalHolder
								.getParamerter("sysFiles");
					}
					sysFiles.add(sysFile);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			} else {
				if (!fileSize) {
					msg = "{\"success\":\"false\",\"fileSize\":false,\"fileIndex\":\""
							+ fileIndex
							+ "\",\"fileName\":\""
							+ oriFileName
							+ "\"}";
				} else {
					logger.error("文件格式不符合要求！");
					msg = "{\"success\":\"false\",\"fileIndex\":\"" + fileIndex
							+ "\",\"fileName\":\"" + oriFileName + "\"}";
				}

			}

		}
		return msg;
	}

	/**
	 * @Description: 定制重新回传附件()
	 * @Author shenguoliang
	 * @param request
	 * @param response
	 * @param appUser
	 * @param userId
	 * @param photoType
	 * @Date 2018/12/25 19:32
	 * @Return java.lang.String
	 * @Line 1355
	 * 增加了参数unrealFullName，用于指定当前pad传来的用户的全名供签章用（加这个参数是因为当用户不存在时，无法从参数里的appUser拿这个参数
	 * instanceId 是当前实例id,用于把照片单独存储到一张表里
	 */
	public String uploadAttach1(MultipartHttpServletRequest request,HttpServletResponse response,ISysUser appUser,long userId,String photoType,String unrealFullName,String instanceId)
			throws IOException, MyException{
		// 附件保存路径
 		String saveType = PropertyUtil.getSaveType();
		String msg="";//返回执行情况

		long typeId = RequestUtil.getLong(request, "typeId",-1); //globalType分类的id
		long dataId = RequestUtil.getLong(request, "dataId",0); //业务表记录的id
		long tableId = RequestUtil.getLong(request, "tableId",0); //业务表的id
		long fileId = RequestUtil.getLong(request, "fileId",0); //上一版本的id
		long maxSize=RequestUtil.getLong(request, "maxSize", Long.MAX_VALUE);

		String describe = RequestUtil.getString(request, "describe");


		String ckResultName = CommonTools.null2String(RequestUtil.getString(request, "ckResultName")); //检查结果的表名
		ckResultName=!ckResultName.equalsIgnoreCase("")?ckResultName:CommonTools.Obj2String((request.getAttribute("ckResultName")));
		if (!"".equals(ckResultName)) {
			ckResultName = "&&" + ckResultName;
		}

		String storePath = "";
		if(dataId==0 && tableId==0){
			Calendar cal = Calendar.getInstance();
			storePath= ""+cal.get(Calendar.YEAR)+File.separator+(cal.get(Calendar.MONTH)+1)+File.separator+cal.get(Calendar.DAY_OF_MONTH);
		} else {
			storePath = ""+ tableId + File.separator + dataId; //当dataId为0时都是无记录的，挂在tableId下
		}
		if("uploadsignphoto".equals(photoType)) {
			int count = dao.getFileDataByDataId(String.valueOf(dataId));
			if (count <= 0) {
				System.out.println(String.valueOf(tableId) + ckResultName + "没有签署项附件");
			} else {
				int flag = dao.deleteFileDataByDataId(String.valueOf(dataId), describe);
			}
		}else if ("uploadopphoto".equals(photoType)){
			int count = dao.getFileDataByTableId(String.valueOf(tableId) + ckResultName);
			if(count <=0){
				System.out.println(String.valueOf(tableId) + ckResultName+"没有附件");
			}else {
				int flag = dao.deleteFileDataByTableId(String.valueOf(tableId) + ckResultName,describe);
			}
		}


		GlobalType globalType = null;
		if(typeId>0L){
			globalType = (GlobalType)this.globalTypeService.getById(Long.valueOf(typeId));
			if(dataId>0){
				SysFileType sysFileType = sysFileTypeService.getFileType(Long.valueOf(typeId),dataId);
				globalType = new GlobalType(sysFileType);
			}
		}

		Long fileTypeId = RequestUtil.getLong(request, "fileTypeId",-1);//folder的Id
		String fileFormates = RequestUtil.getString(request, "fileFormates");//格式要求
		boolean mark = true;
		boolean fileSize=true;//文件大小符合判断
		//获取上传的文件流
		Map<String, MultipartFile> files = request.getFileMap();
		Iterator<MultipartFile> it = files.values().iterator();

		while (it.hasNext()) {
			MultipartFile f = it.next();
			String oriFileName = f.getOriginalFilename();
			String extName = FileOperator.getSuffix(oriFileName);
			String fileIndex= RequestUtil.getString(request, "fileIndex");//文件位置
			if(f.getSize()>maxSize){//文件大小超过最大值
				fileSize=false;
			}

			if(StringUtil.isNotEmpty(fileFormates)){            //文件格式要求
				if( !( fileFormates.contains("*."+extName) ) ){       //不符合文件格式要求的就标志为false
					mark = false;
				}
			}

			if(mark&&fileSize){
				List<SysParameter> fastDFS = sysParameterService.getByParamName("IS_FILE_FASTDFS");
				Long id = fastDFS.get(0).getId();
				String paramvalue = fastDFS.get(0).getParamvalue();

				String relativeFullPath =   FileOperator.generateFilenameNoSemicolon(oriFileName);
				// 附件保存路径
				String attachPath = AppUtil.getAttachPath();
				String filePath = "";
				SysFile sysFile = new SysFile();
				sysFile.setFileId(UniqueIdUtil.genId());

				//By weilei：添加文件上传参数：密级、描述
				String security = RequestUtil.getString(request, "security");
				if("null".equals(security)) security = "";
//				String describe = RequestUtil.getString(request, "describe");
				//By dwj：添加维度属性
				String dimension = RequestUtil.getString(request, "dimension","");
				if(!dimension.equals("")){
					/*维度进行了设置、关闭文件类别*/
					typeId = 0L;
					globalType.setTypeId(0L);
				}
				//SysParameter fastDFS1 = fastDFS.get(0);
				sysFile.setSecurity(security);
				sysFile.setDescribe(describe);
				sysFile.setDimension(dimension);	

				//获取系统是否加密的参数
				List<SysParameter> needEncrypt = sysParameterService.getByParamName("IS_SAVE_SECURITY");
				if (needEncrypt.size() > 0 && "是".equals(needEncrypt.get(0).getParamvalue())) {
					//给文件是否加密属性设置为加密
					sysFile.setIsEncrypt(1L);
				}

				if (saveType.contains("database"))
					sysFile.setFileBlob(f.getBytes());
				else {
					//by liubo 根据参数判断是本地存储还是分布式文件存储
					if(StringUtil.isNotEmpty(paramvalue) && paramvalue.equals("1")){

						//判断是否需要进行加密
						if (needEncrypt.size() > 0 && "是".equals(needEncrypt.get(0).getParamvalue())) {
							needEncrypt.clear();
							//获取加密Key名称
							needEncrypt = sysParameterService.getByParamName("FILE_KEY_NAME");
							if (needEncrypt.size() > 0) {
								try {
									Cipher cipher = Cipher.getInstance("DES");
									Key key = FileOperator.getKey(needEncrypt.get(0).getParamvalue());
									//设置key值
									FileOperator.keyName = needEncrypt.get(0).getParamvalue();
									cipher.init(Cipher.ENCRYPT_MODE, key);

									InputStream is = new ByteArrayInputStream(f.getBytes());
									ByteArrayOutputStream out = new ByteArrayOutputStream();
									CipherInputStream cis = new CipherInputStream(is, cipher);

									byte[] buffer = new byte[2048];
									int r;
									while ((r = cis.read(buffer)) > 0) {
										out.write(buffer, 0, r);
									}
									byte[] byteFile = out.toByteArray();
									cis.close();
									is.close();

									//上传成功后返回路径
									String path = FastDFSFileOperator.uploadFile(byteFile,extName);
									filePath = path;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}else{
							//上传成功后返回路径
							String path = FastDFSFileOperator.uploadFile(f,extName);
							filePath = path;
						}
					}else{
						filePath = SysFileUtil.createFilePath(attachPath+ File.separator+storePath + File.separator, relativeFullPath);
						FileOperator.writeByte(filePath, f.getBytes());

						//by weilei:判断是否对文件进行加密处理
						this.getEcryptFilePath(filePath, oriFileName);
					}


				}
				// end 写入物理文件
				// 向数据库中添加数据 FileUtils
				// 附件名称
				sysFile.setFilename(oriFileName.substring(0, oriFileName.lastIndexOf('.')));
				Calendar cal = Calendar.getInstance();
				Integer year = cal.get(Calendar.YEAR);
				Integer month = cal.get(Calendar.MONTH) + 1;

				//by liubo 根据参数判断是本地存储还是分布式文件存储
				if(StringUtil.isNotEmpty(paramvalue) && paramvalue.equals("1")){
					sysFile.setFilepath(filePath);  //保存storageServer服务器上的卷名+文件名
					sysFile.setStoreWay(1L);//by yangbo 这里存储1区分是走分布式存储的
				}else{
					relativeFullPath = storePath + File.separator + relativeFullPath;
					sysFile.setFilepath(relativeFullPath);  //保存相对路径
				}
				//保存pad拍照信息到pad拍照信息表里
				savePadPhotoInfo(instanceId,oriFileName,relativeFullPath,sysFile.getFileId().toString());

				// 上传时间
				sysFile.setCreatetime(new java.util.Date());
				// 扩展名
				sysFile.setExt(extName);
				// 字节总数
				sysFile.setTotalBytes(f.getSize());
				// 说明
				sysFile.setNote(FileOperator.getSize(f.getSize()));
				// 当前用户的信息
				if (appUser != null) {
					sysFile.setCreatorId(appUser.getUserId());
					sysFile.setCreator(appUser.getFullname());
				} else {
					//由于业务需要,当前人员可能不存在,这里的文件上传者预设为implement
					sysFile.setCreatorId((long)-4);
					sysFile.setCreator("系统实施人员");
				}
				// 总的字节数
				sysFile.setDelFlag(SysFile.FILE_NOT_DEL);

				//更新新版本
				if(fileId>0L){
					SysFile oldSysFile=this.getById(fileId);
					if(BeanUtils.isEmpty(oldSysFile.getParentId())){
						oldSysFile.setParentId(fileId);
						sysFile.setParentId(fileId);
					}else{
						sysFile.setParentId(oldSysFile.getParentId());//初始版本id相同
					}

					oldSysFile.setIsnew(SysFile.ISOLD_VERSION);//改成旧版本
					//继承老版本分类节点
					sysFile.setProtypeId(oldSysFile.getProtypeId());
					sysFile.setFileType(oldSysFile.getFileType());
					sysFile.setDimension(oldSysFile.getDimension());

					this.update(oldSysFile);
					//版本的增加
					String oldVersion=oldSysFile.getVersion();
					sysFile.setVersion(FileVersionManage.getNext(oldVersion));
				}else{

					sysFile.setParentId(sysFile.getFileId());//初始版本两值一致
					//类型
					if (globalType != null) {
						sysFile.setProtypeId(globalType.getTypeId());
						sysFile.setFileType(globalType.getTypeName());
					}else{
						//未选取文件分类或者在folder树上传时默认加分类type根id
						sysFile.setProtypeId(this.sysTypeKeyService.getByKey("FILE").getTypeKeyId());
						sysFile.setFileType(this.sysTypeKeyService.getByKey("FILE").getTypeKey());

					}
				}
				//0 不开启solr文件搜索服务  1为开启
				//by songchen 创建用于solr检索的索引
				String s=sysParameterService.getByAlias("SOLRSERVER");
				if(s.startsWith("1")&&s.contains(extName.toLowerCase())){
					try {
						SolrFile solrFile=new SolrFile(sysFile);
						solrFile.setBytes(f.getBytes());
						solrService.createIndex(solrFile);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(e.getMessage());
					}
				}


				//挂在业务表记录下
				if(dataId!= 0L)
					sysFile.setDataId(String.valueOf(dataId));
				if(tableId!=0L) {
					sysFile.setTableId(String.valueOf(tableId) + ckResultName);
				}

				SysFileFolder folder = null;
				//判断是否存在文库节点
				if(null==fileTypeId||fileTypeId<=0L){
					folder = sysFileFolderService.getRootFolderByUserId(userId);
					if(BeanUtils.isEmpty(folder)){//为空生成新的
						sysFileFolderService.saveFolder(userId);
					}
					folder=sysFileFolderService.getTmpFolderByUserId(userId);
					sysFile.setFolderid(folder.getId());
				}else{
					folder = sysFileFolderService.getById(fileTypeId);
					sysFile.setFolderid(fileTypeId);
				}
				sysFile.setFileatt(SysFile.FILEATT_TRUE);
				sysFile.setFolderPath(folder.getPath());
				sysFile.setShared(null != folder.getSharedNode()
						&& folder.getSharedNode() ? SysFile.SHARED_TRUE
						: SysFile.SHARED_FALSE);
				this.add(sysFile);
				//回传签章的时候,自动帮到对应的人员上
				//经测试,只有回传签署的时候会有dataId,照片和统一采集签章都不会有
				if (!"".equals(sysFile.getDataId())){
					//不是批量采集签章,而是在填写完实例表之后,对实例表里的签署进行自动分配
					//大概思路为:sysFile.dataId->signResult.Id->signResult.signDefId->userName->user.userId
					autoAssignSignModel(sysFile);
				}
				/**
				 * 预留的接口：ModelInfoSyncService.autoSaveModel(ISysUser user, String fileId)
				 *      * 自动辨别回传的签章
				 *      * 如果系统已有该用户，则直接分配签章到该用户
				 *      * 无论系统是否有该用户，都会存在PADHCQZB表里
				 *      * 返回1代表有用户，
				 *      * 返回0代表无此用户
				 *      by zmz
				 */
				/**
				 * 一共有两种情况
				 * 		写表单 uploadSignPhoto
				 * 		同步签章 uploadpersonalsignphoto
				 * 	只有当同步签章时,才可以调用下面的这个方法
				 */
				if ("uploadpersonalsignphoto".equals(photoType)){
					modelInfoSyncService.autoSaveModel(appUser,sysFile.getFileId().toString(),unrealFullName);
				}

				//暂时保留
				/*if("pictureShow".equals(uploadType)){            //pictureShow控件要做多一个文件压缩图
					//filePath =filePath.replaceAll(fileName, fileId + "_small");
					 压缩后的文件路径      如D:/1234.jpg 变成 D:/1234_small.jpg
					int width = Integer.parseInt(configproperties.getProperty("compression.width"));
					String filePrex = filePath.substring(0, filePath.lastIndexOf("."));
					filePath = filePrex + "_small" + filePath.substring(filePrex.length());
					String reutrnStr = ImageUtil.doCompressByByte(f.getBytes(), filePath, width, 40, 1, true);
				    logger.info("压缩后的文件："+reutrnStr);
				}*/
				// end 向数据库中添加数据
				msg="{\"success\":\"true\",\"fileIndex\":\""+fileIndex+"\",\"fileId\":\"" + sysFile.getFileId() + "\",\"fileName\":\"" + oriFileName + "\"}";
				try {
					List<SysFile> sysFiles;
					if (LogThreadLocalHolder.getParamerter("sysFiles") == null) {
						sysFiles = new ArrayList<SysFile>();
						LogThreadLocalHolder.putParamerter("sysFiles", sysFiles);
					} else {
						sysFiles = (List<SysFile>) LogThreadLocalHolder.getParamerter("sysFiles");
					}
					sysFiles.add(sysFile);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}else{
				if(!fileSize){
					msg="{\"success\":\"false\",\"fileSize\":false,\"fileIndex\":\""+fileIndex+"\",\"fileName\":\"" + oriFileName + "\"}";
				}else{
					logger.error("文件格式不符合要求！");
					msg="{\"success\":\"false\",\"fileIndex\":\""+fileIndex+"\",\"fileName\":\"" + oriFileName + "\"}";
				}

			}

		}
		return msg;
	}

	/**
	 * @Description 从文件中获取dataId,多表联查,将当前的图片(签章)分配给指定的人员
	 * 大概思路为:sysFile.dataId->signResult.Id->signResult.signDefId->userName->user.userId
	 * @Author ZMZ
	 * @Date 2020/12/4 16:25
	 * @param sysFile
	 * @Return void
	 */
	private void autoAssignSignModel(SysFile sysFile) {
		String dataId=sysFile.getDataId();
		SignResult signResult=signResultDao.getSignResultById(dataId);
		SignDef signDef=ioSignDefDao.getById(signResult.getSignID().toString());
		String signName=signDef.getName();
		String userName="";
		//名字里有括号的才是user,没有括号的是操作人员
		if(signName.indexOf("签署人员")==-1) {
			if (signName.indexOf("(")!=-1||signName.indexOf("（")!=-1){
				if (signName.indexOf("(")!=-1){
					userName=signName.substring(signName.indexOf("(")+1,signName.indexOf(")"));
				}else {
					userName=signName.substring(signName.indexOf("（")+1,signName.indexOf("）"));
				}
				if (signName.indexOf("组长")!=-1){
					TableInstance tableInstance=ioTableInstanceDao.getById(signResult.getInstantID().toString());
					acceptancePlanService.updateEndTimeBySignResultOfTeamLeader(tableInstance.getPlanId(),
							signResult.getSignTime());
				}
				ISysUser sysUser=sysUserDao.getByFullname(userName).get(0);
				modelInfoSyncService.autoSaveModel(sysUser,sysFile.getFileId().toString(),userName);

			}
		}
		
	}

	/**
	 * 获取pad拍照的信息保存到pad拍照信息表里
	 * @param instanceId
	 * @param oriFileName
	 * @param relativeFilePath
	 * @param fileId
	 */
	public void savePadPhotoInfo(String instanceId,String oriFileName,String relativeFilePath,String fileId){
		PadPhotoInfo padPhotoInfo=new PadPhotoInfo();
		Long id=UniqueIdUtil.genId();
		padPhotoInfo.setId(id.toString());
		padPhotoInfo.setInstanceId(instanceId);
		padPhotoInfo.setWjm(oriFileName);
		padPhotoInfo.setXddz(relativeFilePath);
		padPhotoInfo.setFileId(fileId);
		padPhotoInfoDao.insert(padPhotoInfo);
	}

	public String flieuploadAttach(MultipartHttpServletRequest request,
			HttpServletResponse response, ISysUser appUser, long userId,
			Long diaId) throws IOException, MyException {

		// 附件保存路径
		String saveType = PropertyUtil.getSaveType();
		String msg = "";// 返回执行情况

		long typeId = RequestUtil.getLong(request, "typeId", -1); // globalType分类的id
		long dataId = RequestUtil.getLong(request, "dataId", 0); // 业务表记录的id
		long tableId = RequestUtil.getLong(request, "tableId", 0); // 业务表的id
		long fileId = RequestUtil.getLong(request, "fileId", 0); // 上一版本的id
		int maxSize = RequestUtil.getInt(request, "maxSize", 200 * 1024 * 1024);
		String storePath = "";
		if (dataId == 0 && tableId == 0) {
			Calendar cal = Calendar.getInstance();
			storePath = "" + cal.get(Calendar.YEAR) + File.separator
					+ (cal.get(Calendar.MONTH) + 1) + File.separator
					+ cal.get(Calendar.DAY_OF_MONTH);
		} else {
			storePath = "" + tableId + File.separator + dataId; // 当dataId为0时都是无记录的，挂在tableId下
		}

		GlobalType globalType = null;
		if (typeId > 0L) {
			globalType = (GlobalType) this.globalTypeService.getById(Long
					.valueOf(typeId));
			if (dataId > 0) {
				SysFileType sysFileType = sysFileTypeService.getFileType(
						Long.valueOf(typeId), dataId);
				globalType = new GlobalType(sysFileType);
			}
		}

		Long fileTypeId = RequestUtil.getLong(request, "fileTypeId", -1);// folder的Id
		String fileFormates = RequestUtil.getString(request, "fileFormates");// 格式要求
		boolean mark = true;
		boolean fileSize = true;// 文件大小符合判断
		// 获取上传的文件流
		Map<String, MultipartFile> files = request.getFileMap();
		Iterator<MultipartFile> it = files.values().iterator();

		while (it.hasNext()) {
			MultipartFile f = it.next();
			String oriFileName = f.getOriginalFilename();
			String extName = FileOperator.getSuffix(oriFileName);
			String fileIndex = RequestUtil.getString(request, "fileIndex");// 文件位置
			if (f.getSize() > maxSize) {// 文件大小超过最大值
				fileSize = false;
			}

			if (StringUtil.isNotEmpty(fileFormates)) { // 文件格式要求
				if (!(fileFormates.contains("*." + extName))) { // 不符合文件格式要求的就标志为false
					mark = false;
				}
			}

			if (mark && fileSize) {
				List<SysParameter> fastDFS = sysParameterService
						.getByParamName("IS_FILE_FASTDFS");
				Long id = fastDFS.get(0).getId();
				String paramvalue = fastDFS.get(0).getParamvalue();

				String relativeFullPath = FileOperator
						.generateFilenameNoSemicolon(oriFileName);
				// 附件保存路径
				String attachPath = AppUtil.getAttachPath();
				String filePath = "";
				SysFile sysFile = new SysFile();
				sysFile.setFileId(UniqueIdUtil.genId());

				// By weilei：添加文件上传参数：密级、描述
				String security = RequestUtil.getString(request, "security");
				if ("null".equals(security))
					security = "";
				String describe = RequestUtil.getString(request, "describe");
				// By dwj：添加维度属性
				String dimension = RequestUtil.getString(request, "dimension",
						"");
				if (!dimension.equals("")) {
					/* 维度进行了设置、关闭文件类别 */
					typeId = 0L;
					globalType.setTypeId(0L);
				}
				// SysParameter fastDFS1 = fastDFS.get(0);
				sysFile.setSecurity(security);
				sysFile.setDescribe(describe);
				sysFile.setDimension(dimension);
				// 获取系统是否加密的参数
				List<SysParameter> needEncrypt = sysParameterService
						.getByParamName("IS_SAVE_SECURITY");
				if (needEncrypt.size() > 0
						&& "是".equals(needEncrypt.get(0).getParamvalue())) {
					// 给文件是否加密属性设置为加密
					sysFile.setIsEncrypt(1L);
				}

				if (saveType.contains("database"))
					sysFile.setFileBlob(f.getBytes());
				else {
					// by liubo 根据参数判断是本地存储还是分布式文件存储
					if (StringUtil.isNotEmpty(paramvalue)
							&& paramvalue.equals("1")) {

						// 判断是否需要进行加密
						if (needEncrypt.size() > 0
								&& "是".equals(needEncrypt.get(0)
										.getParamvalue())) {
							needEncrypt.clear();
							// 获取加密Key名称
							needEncrypt = sysParameterService
									.getByParamName("FILE_KEY_NAME");
							if (needEncrypt.size() > 0) {
								try {
									Cipher cipher = Cipher.getInstance("DES");
									Key key = FileOperator.getKey(needEncrypt
											.get(0).getParamvalue());
									// 设置key值
									FileOperator.keyName = needEncrypt.get(0)
											.getParamvalue();
									cipher.init(Cipher.ENCRYPT_MODE, key);

									InputStream is = new ByteArrayInputStream(
											f.getBytes());
									ByteArrayOutputStream out = new ByteArrayOutputStream();
									CipherInputStream cis = new CipherInputStream(
											is, cipher);

									byte[] buffer = new byte[2048];
									int r;
									while ((r = cis.read(buffer)) > 0) {
										out.write(buffer, 0, r);
									}
									byte[] byteFile = out.toByteArray();
									cis.close();
									is.close();

									// 上传成功后返回路径
									String path = FastDFSFileOperator
											.uploadFile(byteFile, extName);
									filePath = path;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							// 上传成功后返回路径
							String path = FastDFSFileOperator.uploadFile(f,
									extName);
							filePath = path;
						}
					} else {
						filePath = SysFileUtil.createFilePath(attachPath
								+ File.separator + storePath + File.separator,
								relativeFullPath);
						FileOperator.writeByte(filePath, f.getBytes());

						// by weilei:判断是否对文件进行加密处理
						this.getEcryptFilePath(filePath, oriFileName);
					}

				}
				// end 写入物理文件
				// 向数据库中添加数据 FileUtils
				// 附件名称
				sysFile.setFilename(oriFileName.substring(0,
						oriFileName.lastIndexOf('.')));
				Calendar cal = Calendar.getInstance();
				Integer year = cal.get(Calendar.YEAR);
				Integer month = cal.get(Calendar.MONTH) + 1;

				// by liubo 根据参数判断是本地存储还是分布式文件存储
				if (StringUtil.isNotEmpty(paramvalue) && paramvalue.equals("1")) {
					sysFile.setFilepath(filePath); // 保存storageServer服务器上的卷名+文件名
					sysFile.setStoreWay(1L);// by yangbo 这里存储1区分是走分布式存储的
				} else {
					relativeFullPath = storePath + File.separator
							+ relativeFullPath;
					sysFile.setFilepath(relativeFullPath); // 保存相对路径
				}

				// 上传时间
				sysFile.setCreatetime(new java.util.Date());
				// 扩展名
				sysFile.setExt(extName);
				// 字节总数
				sysFile.setTotalBytes(f.getSize());
				// 说明
				sysFile.setNote(FileOperator.getSize(f.getSize()));
				// 当前用户的信息
				if (appUser != null) {
					sysFile.setCreatorId(appUser.getUserId());
					sysFile.setCreator(appUser.getFullname());
				} else {
					sysFile.setCreatorId(UserContextUtil.getCurrentUser()
							.getUserId());
					sysFile.setCreator(UserContextUtil.getCurrentUser()
							.getFullname());
				}
				// 总的字节数
				sysFile.setDelFlag(SysFile.FILE_NOT_DEL);

				// 更新新版本
				if (fileId > 0L) {
					SysFile oldSysFile = this.getById(fileId);
					if (BeanUtils.isEmpty(oldSysFile.getParentId())) {
						oldSysFile.setParentId(fileId);
						sysFile.setParentId(fileId);
					} else {
						sysFile.setParentId(oldSysFile.getParentId());// 初始版本id相同
					}

					oldSysFile.setIsnew(SysFile.ISOLD_VERSION);// 改成旧版本
					// 继承老版本分类节点
					sysFile.setProtypeId(oldSysFile.getProtypeId());
					sysFile.setFileType(oldSysFile.getFileType());
					sysFile.setDimension(oldSysFile.getDimension());

					this.update(oldSysFile);
					// 版本的增加
					String oldVersion = oldSysFile.getVersion();
					sysFile.setVersion(FileVersionManage.getNext(oldVersion));
				} else {

					sysFile.setParentId(sysFile.getFileId());// 初始版本两值一致
					// 类型
					if (globalType != null) {
						sysFile.setProtypeId(globalType.getTypeId());
						sysFile.setFileType(globalType.getTypeName());
					} else {
						// 未选取文件分类或者在folder树上传时默认加分类type根id
						sysFile.setProtypeId(this.sysTypeKeyService.getByKey(
								"FILE").getTypeKeyId());
						sysFile.setFileType(this.sysTypeKeyService.getByKey(
								"FILE").getTypeKey());

					}
				}
				// 0 不开启solr文件搜索服务 1为开启
				// by songchen 创建用于solr检索的索引
				String s = sysParameterService.getByAlias("SOLRSERVER");
				if (s.startsWith("1") && s.contains(extName.toLowerCase())) {
					try {
						SolrFile solrFile = new SolrFile(sysFile);
						solrFile.setBytes(f.getBytes());
						solrService.createIndex(solrFile);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(e.getMessage());
					}
				}

				// 挂在业务表记录下
				if (dataId != 0L)
					sysFile.setDataId(String.valueOf(dataId));
				if (tableId != 0L)
					sysFile.setTableId(String.valueOf(tableId));
				SysFileFolder folder = null;
				// 判断是否存在文库节点
				if (null == fileTypeId || fileTypeId <= 0L) {
					folder = sysFileFolderService.getRootFolderByUserId(userId);
					if (BeanUtils.isEmpty(folder)) {// 为空生成新的
						sysFileFolderService.saveFolder(userId);
					}
					folder = sysFileFolderService.getTmpFolderByUserId(userId);
					sysFile.setFolderid(folder.getId());
				} else {
					folder = sysFileFolderService.getById(fileTypeId);
					sysFile.setFolderid(fileTypeId);
				}
				sysFile.setFileatt(SysFile.FILEATT_TRUE);
				sysFile.setFolderPath(folder.getPath());
				sysFile.setShared(null != folder.getSharedNode()
						&& folder.getSharedNode() ? SysFile.SHARED_TRUE
						: SysFile.SHARED_FALSE);
				sysFile.setFileId(diaId);
				this.add(sysFile);
				// end 向数据库中添加数据
				msg = "{\"success\":\"true\",\"fileIndex\":\"" + fileIndex
						+ "\",\"fileId\":\"" + sysFile.getFileId()
						+ "\",\"fileName\":\"" + oriFileName + "\"}";
				try {
					List<SysFile> sysFiles;
					if (LogThreadLocalHolder.getParamerter("sysFiles") == null) {
						sysFiles = new ArrayList<SysFile>();
						LogThreadLocalHolder
								.putParamerter("sysFiles", sysFiles);
					} else {
						sysFiles = (List<SysFile>) LogThreadLocalHolder
								.getParamerter("sysFiles");
					}
					sysFiles.add(sysFile);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			} else {
				if (!fileSize) {
					msg = "{\"success\":\"false\",\"fileSize\":false,\"fileIndex\":\""
							+ fileIndex
							+ "\",\"fileName\":\""
							+ oriFileName
							+ "\"}";
				} else {
					logger.error("文件格式不符合要求！");
					msg = "{\"success\":\"false\",\"fileIndex\":\"" + fileIndex
							+ "\",\"fileName\":\"" + oriFileName + "\"}";
				}

			}

		}
		return msg;
	}

	/**
	 * 根据主键或者表ID获取列表 by YangBo
	 *
	 * @param
	 * @return
	 */
	public List<SysFile> getFileListByDataId(String dataIds, String tableIds)
			throws Exception {
		List<SysFile> list = dao.getFileListByDataIds(dataIds, tableIds);
		return list;
	}

	/**
	 * 根据id删除附件 by YangBo
	 *
	 * @param sysFile
	 * @param attachPath
	 */
	public void delFiles(ISysFile sysFile, String attachPath) throws Exception {
		String filePath = sysFile.getFilepath();
		long id = sysFile.getFileId();
		if (filePath.startsWith("group")) {
			FastDFSFileOperator.deleteFile(filePath);
		} else {
			FileOperator.deleteFile(attachPath + File.separator + filePath);
		}
		dao.delById(Long.valueOf(id));
	}

	/**
	 * 根据主键获取附件列表 by YangBo
	 *
	 * @param fileIds
	 * @return
	 */
	public List<SysFile> getFileByIds(String fileIds) {
		return dao.getFileByIds(fileIds);
	}

	/**
	 * 通过当前人员密级获取具有查看权限的文件
	 *
	 * @author liubo
	 * @return
	 */
	public List<SysFile> getFileByUserSecurity() {
		List<SysFile> fileList = dao
				.getFileBySecurity(getFileConditions("SECURITY_"));
		return fileList;
	}

	/**
	 * 通过当前人员密级与传入的密级字段获取筛选条件
	 *
	 * @author liubo
	 * @param fieldNameStandard
	 * @return
	 */
	public String getFileConditions(String fieldNameStandard) {
		// 存储密级map
		Map<String, String> securityMap = ISysFile.SECURITY_CHINESE_MAP;
		// 获取当前用户密级权限
		String userSecurity = UserContextUtil.getCurrentUserSecurity();
		// 拼写查询条件
		StringBuffer scriptSecurity = new StringBuffer();
		if (BeanUtils.isEmpty(userSecurity)) {
			// 没有设置密级
			scriptSecurity.append(fieldNameStandard).append(" is NULL");
			scriptSecurity.append(" OR ");
			scriptSecurity.append(fieldNameStandard).append("=")
					.append("'" + "'");
			Set set = securityMap.keySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				Double keyDouble = Double.parseDouble(key);
				Double userKey = Double.parseDouble(ISysUser.SECURITY_FEIMI);
				if (keyDouble.compareTo(userKey) <= 0) {
					scriptSecurity.append(" OR ");
					scriptSecurity.append(fieldNameStandard).append("=")
							.append("'" + key + "'");
				}
			}
		} else {
			// 核心，能够查看所有数据
			// 重要，能够查看除绝密数据外的其他数据
			// 一般，只能查看秘密及其以下的数据，包含没有设置密级的数据
			// 非密，只能查看公开及其以下的数据，包含没有设置密级的数据
			Double userKey = Double.parseDouble(userSecurity);

			scriptSecurity.append(fieldNameStandard).append(" is NULL");
			scriptSecurity.append(" OR ");
			scriptSecurity.append(fieldNameStandard).append("=")
					.append("'" + "'");
			Set set = securityMap.keySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				Double keyDouble = Double.parseDouble(key);
				if (keyDouble.compareTo(userKey) <= 0) {
					scriptSecurity.append(" OR ");
					scriptSecurity.append(fieldNameStandard).append("=")
							.append("'" + key + "'");
				}
			}
		}
		return scriptSecurity.toString();
	}

	/**
	 * 通过查询条件查询符合条件的所有附件
	 *
	 * @author liubo
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	public List<SysFile> getSysFileList(HttpServletRequest request)
			throws ParseException {
		QueryFilter filter = new QueryFilter(request, "sysFileItem");
		/* 查询条件BEGIN */
		String fileName = RequestUtil.getString(request, "Q_fileName_SL");
		if (fileName.length() != 0) {
			filter.addFilterForIB("filename", fileName);
		}
		String creator = RequestUtil.getString(request, "Q_creator_SL");
		if (creator.length() != 0) {
			filter.addFilterForIB("creator", creator);
		}
		String ext = RequestUtil.getString(request, "Q_ext_SL");
		if (ext.length() != 0) {
			filter.addFilterForIB("ext", ext);
		}

		Date beginTime = RequestUtil.getDate(request, "Q_begincreatetime_DL");
		if (beginTime != null) {
			filter.addFilterForIB("beginTime", beginTime);
		}

		Date endTime = RequestUtil.getDate(request, "Q_endcreatetime_DG");
		if (endTime != null) {
			filter.addFilterForIB("endTime", endTime);
		}

		// 对查看当前人员下的附件也通过密级筛选
		// 获取密级参数数据
		List<SysParameter> isDisplaySecurity = sysParameterService
				.getByParamName(ISysFile.IS_DISPLAY_SECURITY);
		if (isDisplaySecurity.size() > 0
				&& "1".equals(isDisplaySecurity.get(0).getParamvalue())) {
			String securityConditions = getFileConditions("SECURITY_");
			if (securityConditions != null) {
				filter.addFilterForIB("securityConditions", securityConditions);
			}
		}
		// 查找附件列表
		try {
			List<SysFile> list = this.dao.getSysFilesByConditions(filter);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取附件列表出错");
			return null;
		}
	}

	/**
	 * by YangBo 根据图片上传类型获取对应图片库
	 *
	 * @param fileType
	 * @param currentUserId
	 * @return
	 */
	public List<SysFile> getImagesByFileType(String fileType, Long currentUserId) {
		return this.dao.getByFileType(fileType, currentUserId);
	}

	/**
	 * @author liubo 根据参数重新获取文件密级map
	 */
	public void addFileSecurity() {
		// 先清空原有的数据
		ISysFile.SECURITY_CHINESE_MAP.clear();
		// 获取系统参数
		List<? extends ISysParameter> fileSecurity = sysParameterService
				.getByParamName(ISysFile.FILE_SECURITY_MAP);
		String fileSecurityValue = fileSecurity.get(0).getParamvalue();
		// 获取属性，将值解析为我们需要的map
		String[] fileSecuritys = fileSecurityValue.split("-");
		for (int i = 0; i < fileSecuritys.length; i++) {
			String keyAndValue = fileSecuritys[i];
			String key = keyAndValue.split(":")[0];
			String value = keyAndValue.split(":")[1];
			ISysFile.SECURITY_CHINESE_MAP.put(key, value);
		}
	}

	public String[] downAttachByType(HttpServletRequest request,
			String paramJson, Long typeId) throws Exception {
		/* 参数准备 */
		JSONObject jsonObject = JSONObject.fromObject(paramJson);
		Long dataTempId = JSONUtil.getLong(jsonObject, "dataTempId", 0L);
		Long dataId = JSONUtil.getLong(jsonObject, "dataId", 0L);
		IDataTemplate dataTemplate = dataTemplateService.getById(dataTempId);
		Long tableId = dataTemplate.getTableId();
		/* 获取此类别下的所有子类别信息 */
		List<SysFileType> typeList = sysFileTypeService.getByParentId(typeId,
				dataId);
		/* 获取typeid与文件夹名称的对应关系 */
		Map<String, String> map = getPathInfo(typeList);

		String rootFloder = "preview" + File.separator;
		String secondeFloder = "";
		/* 根据类别信息循环、获取下属文件信息； */
		for (SysFileType filetype : typeList) {
			if (filetype.getTypeId().equals(typeId)) {
				secondeFloder = SysFileUtil.getFloderName(map,
						filetype.getNodePath()).replaceAll("\\\\", "");
			}
			String fileFloderName = rootFloder
					+ SysFileUtil.getFloderName(map, filetype.getNodePath());
			List<SysFile> fileList = this.getAttachList(request, jsonObject,
					filetype.getTypeId(), dataId, tableId, true);
			String targetFloderPath = AppUtil.getAttachPath() + File.separator
					+ fileFloderName;
			FileUtil.createFolderFile(targetFloderPath);
			for (SysFile file : fileList) {
				String filename = file.getFilename() + "." + file.getExt();
				String sourceFilepath = AppUtil.getAttachPath()
						+ File.separator + String.valueOf(file.getFilepath());
				String filePath = file.getFilepath();
				String targetFilePth = targetFloderPath + File.separator
						+ filename;

				// by liubo 判断是本地下载还是服务器下载
				Boolean isFastDFS = file.getFilepath().startsWith("group");
				if (isFastDFS) {// 分布式
					String destFilePath = getDecodeFilePath(filePath, filename,
							file.getIsEncrypt(), isFastDFS);
					if (!"".equals(destFilePath)) {
						FileUtil.copyFile(destFilePath, targetFilePth);
						FileUtil.deleteFile(destFilePath);
					} else {
						FileUtil.writeByte(targetFilePth,
								FastDFSFileOperator.getFileByte(filePath));
					}
				} else {// 本地
					String destFilePath = getDecodeFilePath(sourceFilepath,
							filename, file.getIsEncrypt(), isFastDFS);
					if (!"".equals(destFilePath)) {
						FileUtil.copyFile(destFilePath, targetFilePth);
						FileUtil.deleteFile(destFilePath);
					} else {
						FileUtil.copyFile(sourceFilepath, targetFilePth);
					}
				}
			}
		}
		/* 将生成的文件压缩并删除 */
		String zipPath = AppUtil.getAttachPath() + File.separator + rootFloder
				+ secondeFloder;
		ZipUtil.zip(zipPath, false, "GBK");
		FileUtil.deleteDir(new File(zipPath + File.separator));
		return new String[] {
				zipPath + ".zip",
				secondeFloder
						+ new SimpleDateFormat("yyyyMMddHHmmsssss")
								.format(new Date()) + ".zip" };
	}

	private Map<String, String> getPathInfo(List<SysFileType> typeList) {
		Map<String, String> map = new HashMap<String, String>();
		for (SysFileType filetype : typeList) {
			String nodePath = filetype.getNodePath();
			String name = filetype.getTypeName();
			if (nodePath.contains(".")) {
				String[] nodeArr = nodePath.split("\\.");
				String lastNode = nodeArr[nodeArr.length - 1];
				map.put(lastNode, name);
			}
		}
		return map;
	}

	/**
	 * 将文件夹下的所有文件上传至fastdfs
	 *
	 * @param folderPath
	 */
	public void uploadFileByFolder(String folderPath) {
		File file = new File(folderPath);
		Map<String, Object> parma = new HashMap<String, Object>();
		if (file.exists()) {
			File[] files = file.listFiles();
			for (File f : files) {
				String ext = "";
				String fileId = "";
				String fileName = f.getName();
				int pos = fileName.lastIndexOf(".");
				if (pos > -1 && f.isFile()) {
					ext = fileName.substring(pos + 1);
					fileId = fileName.substring(0, pos);
					byte[] buff = FileUtil.readByte(f.getAbsolutePath());
					String path = FastDFSFileOperator.uploadFile(buff, ext);
					parma.clear();
					parma.put("filepath", path);
					parma.put("fileId", fileId);
					this.dao.update("updateFilePath", parma);
					f.delete();
				}
			}
		}
	}

	/**
	 * 获取指定文件的字节数组
	 */
	public byte[] getFileByteByFileId(Long fileId) {

		byte[] content = null;
		try {
			SysFile sysFile = this.getById(fileId);
			List<SysParameter> fastDFS = sysParameterService
					.getByParamName("IS_FILE_FASTDFS");
			String paramvalue = fastDFS.get(0).getParamvalue();
			if (StringUtil.isNotEmpty(paramvalue) && "1".equals(paramvalue)) {// 判断是否分布式文件
				content = FastDFSFileOperator
						.getFileByte(sysFile.getFilepath());
			} else {
				String realPath = AppUtil.getAttachPath() + File.separator
						+ sysFile.getFilepath();
				File file = new File(realPath);
				if (file.exists()) {
					content = FileUtil.readByte(realPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	public void getFileById(HttpServletRequest request,
			HttpServletResponse response, Long fileId) throws IOException {
		// Long fileId = RequestUtil.getLong(request, "fileId");
		SysFile sysFile = this.getById(fileId);
		Boolean isNoGroup = sysFile.getFilepath().startsWith("group");// 判断是否分布式文件
		if (isNoGroup) {
			String destFilePath = this.getDecodeFilePath(sysFile.getFilepath(),
					sysFile.getFilename(), sysFile.getIsEncrypt(), true);
			if (!"".equals(destFilePath)) {
				// 读取文件并输出
				OutputStream outp = response.getOutputStream();
				File file = new File(destFilePath);
				if (file.exists()) {
					response.setContentLength((int) file.length());
					FileInputStream in = null;
					try {
						in = new FileInputStream(destFilePath);
						byte[] b = new byte[1024];
						int i = 0;
						while ((i = in.read(b)) > 0) {
							outp.write(b, 0, i);
						}
						outp.flush();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (in != null) {
							in.close();
							in = null;
						}
						if (outp != null) {
							outp.close();
							outp = null;
							response.flushBuffer();
						}
						// 删除解密文件
						File destFile = new File(destFilePath);
						if (destFile.exists())
							destFile.delete();
					}
				} else {
					response.getWriter()
							.print("<font style='font-weight:800;color:#696969;font-size:14px;text-align:center;'>The file is not exists!</font>");
				}
				if (outp != null) {
					outp.close();
					outp = null;
					response.flushBuffer();
				}
			} else {
				byte[] content = FastDFSFileOperator.getFileByte(sysFile
						.getFilepath());
				response.getOutputStream().write(content);
				response.flushBuffer();
			}
		} else {
			getFile(request, response, fileId);
		}
	}

	/**
	 *
	 获取图片流 getFile
	 *
	 * @param
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 */
	public void getFile(HttpServletRequest request,
			HttpServletResponse response, Long fileId) throws IOException {
		response.reset();
		// String vers = request.getHeader("USER-AGENT");
		SysFile sysFile = this.getById(fileId);

		if (sysFile == null) {
			response.getWriter()
					.print("<font style='font-weight:800;color:#696969;font-size:14px;text-align:center;'>The file is not exists!</font>");
		}

		String contextType = SysFileUtil
				.getContextType(sysFile.getExt(), false);
		OutputStream outp = response.getOutputStream();
		response.setContentType(contextType);
		response.setCharacterEncoding("utf-8");
		String fileName = sysFile.getFilename() + "." + sysFile.getExt();
		String isDownload = request.getParameter("download");
		/*
		 * if (vers.indexOf("Chrome") != -1 && vers.indexOf("Mobile") != -1) {
		 * fileName = fileName.toString(); } else { fileName =
		 * StringUtil.encodingString(fileName, "GB2312","ISO-8859-1"); }
		 */
		fileName = StringUtil.encodingString(fileName, "GB2312", "ISO-8859-1");
		if ("application/octet-stream".equals(contextType)
				|| StringUtils.isNotBlank(isDownload)) {
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
		} else {
			response.addHeader("Content-Disposition", "filename=" + fileName);
		}
		response.addHeader("Content-Transfer-Encoding", "binary");
		String saveType = PropertyUtil.getSaveType();
		// 附件保存路径
		String attachPath = AppUtil.getAttachPath();
		// by --yangbo 加入database的文件获取
		if (saveType.contains("database")) {
			byte[] bytes = (byte[]) null;
			bytes = sysFile.getFileBlob();
			response.setContentLength(bytes.length);
			outp.write(bytes);
		} else {
			String filePath = StringUtil
					.trimSufffix(attachPath, File.separator);
			String realPath = "";
			realPath = filePath + File.separator
					+ sysFile.getFilepath().replace("/", File.separator);

			// by weilei:对加密文件进行解密处理
			String destFilePath = this.getDecodeFilePath(realPath, fileName,
					sysFile.getIsEncrypt(), false);
			if (!"".equals(destFilePath)) {
				realPath = destFilePath;
			}
			// 读取文件并输出
			File file = new File(realPath);
			if (file.exists()) {
				response.setContentLength((int) file.length());
				FileInputStream in = null;
				try {
					in = new FileInputStream(realPath);
					byte[] b = new byte[1024 * 10];
					int i = 0;
					while ((i = in.read(b)) > 0) {
						outp.write(b, 0, i);
					}
					outp.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						in.close();
						in = null;
					}
					if (outp != null) {
						outp.close();
						outp = null;
						response.flushBuffer();
					}
					// 删除解密文件
					File destFile = new File(destFilePath);
					if (destFile.exists())
						destFile.delete();
				}
			} else {
				response.getWriter()
						.print("<font style='font-weight:800;color:#696969;font-size:14px;text-align:center;'>The file is not exists!</font>");
			}

			if (outp != null) {
				outp.close();
				outp = null;
				response.flushBuffer();
			}
		}

	}

	/**
	 * @Description: 获取help/pad路径下最新版本APK文件
	 * @author qiaozhili
	 * @date 2020/8/25 21:25
	 * @param
	 * @return
	 */
	public boolean checkJDKExisted(String resourcePath, String version) throws Exception {
		boolean isContent = false;
		File file = new File(resourcePath);
		File[] fileList = file.listFiles();

		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isFile()) {
				String fileName = fileList[i].getName();
				if (fileName.contains(version)) {
					isContent = true;
				}
				System.out.println("文件名：" + fileName);
			}
		}
		return isContent;
	}

	/**
	 * @Description: 获取pad安装包apk文件
	 * @author qiaozhili
	 * @date 2020/8/25 21:26
	 * @param
	 * @return
	 */
	public void getPadApk(HttpServletRequest request,HttpServletResponse response, String versionNew) throws IOException {
//		String versionNew = sysParameterService.getByAlias("pad_version");
		String destFilePath = AppUtil.getRealPath("/") + "help" + File.separator + "PAD" + File.separator + versionNew + ".apk";
		File file = new File(destFilePath);
		try {
			response.reset();
			response.setHeader(
					"Content-Disposition",
					"attachment;filename="
							+ URLEncoder.encode(file.getName(), "UTF-8")
							+ ";filelength="
							+ file.length());
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream fout = new BufferedOutputStream(response.getOutputStream());
			byte[] buffer = new byte[8192];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				fout.write(buffer, 0, len);
			}
			in.close();
			fout.write(buffer);
			fout.flush();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
