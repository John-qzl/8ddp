package com.cssrc.ibms.core.resources.io.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.dao.DataPackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.PackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.TeamDao;
import com.cssrc.ibms.core.resources.io.bean.DataObject;
import com.cssrc.ibms.core.resources.io.bean.DpFile;
import com.cssrc.ibms.core.resources.io.bean.Project;
import com.cssrc.ibms.core.resources.io.bean.ins.CheckResult;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.pack.SimplePackage;
import com.cssrc.ibms.core.resources.io.dao.DpFileDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.resources.product.dao.ProductDao;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.ZipUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.dp.form.service.FormService;
import com.cssrc.ibms.dp.form.util.FormUtils;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SysFileService;
@Service
public class ClientImportService {	
	protected Logger logger = LoggerFactory.getLogger(ClientImportService.class);
	
	@Resource
	private ProductDao productDao;
	@Resource
	private DataPackageDao dataPackageDao;
	@Resource
	private PackageDao packageDao;
	@Resource
	private TeamDao teamDao;
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private SysFileService sysFileService;
	@Resource
	private IOTableInstanceDao ioTableInstanceDao;
	@Resource
	private DpFileDao dpFileDao;
	@Resource
	private FormService formService;

	private String packageTempFolder = "";
	
	public void clearTempFileFolder() {
		this.packageTempFolder = "";
	}
	public void initTempFileFolder(String name) {
		this.packageTempFolder = SysConfConstant.UploadFileFolder+File.separator+"temp" + File.separator + 
				name+DateUtil.getCurrentDate("yyyyMMddHHmmss");
		FileOperator.createFolder(this.packageTempFolder);		
	}	
/*	*//**
	 * @param file
	 * @return
	 * @throws Exception
	 *//*
	public String importPakage(MultipartFile file) throws Exception {
		StringBuffer log = new StringBuffer();
		initTempFileFolder(IOConstans.IMPORT_FLODER_NAME);
		String floder = this.packageTempFolder;
		ZipUtil.unZipFile(file, this.packageTempFolder);
		File mainFile = findMainFile(this.packageTempFolder);
		FileInputStream is = new FileInputStream(mainFile);
		Project project = XmlBeanUtil.unmarshall(is, Project.class);
		asynDb(project.getList(),log);
		FileOperator.delFoldsWithChilds(floder);
		return log.toString();
	}*/
	

	public String importPakage(MultipartFile file) throws Exception {
		StringBuffer log = new StringBuffer();
		initTempFileFolder(IOConstans.IMPORT_FLODER_NAME);
		String floder = this.packageTempFolder;
		ZipUtil.unZipFile(file, this.packageTempFolder);
		File mainFile = findMainFile(this.packageTempFolder);
		FileInputStream is = new FileInputStream(mainFile);
		Project project = XmlBeanUtil.unmarshall(is, Project.class);
		asynDb(project.getList(),log);
		FileOperator.delFoldsWithChilds(floder);
		return log.toString();
	}
	
	
	
	
	
	private void asynDb(List<SimplePackage> packs,StringBuffer log)  throws Exception {
		try {
			if(packs==null) {
				return;
			}
			for(SimplePackage pack : packs){
				List<DataObject> datas = pack.getDatas();
				updateDataObject(datas,log);
				List<SimplePackage> sonPacks = pack.getList();
				asynDb(sonPacks,log);
			}
		}catch(Exception e) {
			log.append(e.getMessage()+"\r\n"+"数据包导入异常，数据已全部回滚！");
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
		}

	}
	/**
	 * 检查表实例更新,以及文件的保存
	 * @param datas
	 * @param log
	 */
	private void updateDataObject(List<DataObject> datas,StringBuffer log)  throws Exception {
		if(null==datas||datas.size()==0) {
			return;
		}
		for(DataObject data : datas) {
			String type = data.getSjlx();
			if(type.equals(IOConstans.DATA_PACKAGE_FORM)) {
				String fileName = data.getSsmbmc();
				if(fileName.equals("")) {
					log.append("[").append(data.getSjmc()).append("]").append("对应的表单实例为空！\r\n");
					continue;
				}
				this.updTableTemplate(data.getSsmbmc(),log);
			}else {
				String fileInfo = this.uploadDpFile(data.getFile(),log);
				dataPackageDao.updateFileInfo(fileInfo,data.getId());
			}
		}		
	}
	/**
	 * 处理文件
	 * @param dpFile : 文件
	 * @param log
	 */
	private String uploadDpFile(DpFile dpFile,StringBuffer log) {
		if(dpFile==null){
			return "";
		}		
		SysFile sysFile = this.dpFileDao.insert(dpFile, this.packageTempFolder);	
		JSONObject obj =  new JSONObject();
		obj.put("id", ""+sysFile.getFileId());
		obj.put("name", dpFile.getFilename()+"."+dpFile.getExt());
		
		return "["+obj.toString()+"]";
	}
	/**
	 * 处理表单实例
	 * @param dpFile
	 * @param log
	 */
	private void updTableTemplate(String fileName,StringBuffer log)  throws Exception {
		String filePath = this.packageTempFolder + File.separator + fileName;
		File file = new File(filePath);
		if(!file.exists()) {
			log.append("[").append(filePath).append("],").append("文件不存在！\r\n");
			return;
		}	
		FileInputStream is = new FileInputStream(file);
		TableInstance ins = XmlBeanUtil.unmarshall(is, TableInstance.class);
		
		//ioTableInstanceDao.deleteId(ins.getId(), true);	
		//this.ioTableInstanceDao.insertTableInstant(ins,this.packageTempFolder);
		this.ioTableInstanceDao.updateTableInstant(ins,this.packageTempFolder);
		
		//更新html
		//检查结果
		List<CheckResult> checkList = ins.getCheckResultList();
		List<Map<String, String>> contentslist = new ArrayList<Map<String,String>>();
		for (CheckResult checkRes : checkList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", checkRes.getId());
			map.put("value", checkRes.getValue());
			contentslist.add(map);
		}
		String html = ins.getContent();
		int index1 = html.indexOf("<table");
		int index2 = html.lastIndexOf("</table>");
		html = html.substring(index1, index2 + 8);
		html="<html>"+html+"</html>";
		Map<String, Object> newhtml = FormUtils.updateHtml(html, contentslist);
		if(newhtml.get("html").equals("")){
			log.append("[").append(ins.getId()+"-"+fileName).append("],").append("html更新失败！\r\n");
			return;
		}else{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ID", ins.getId());
			param.put("F_CONTENT", newhtml.get("html"));
			formService.updateTableIns(param);
			log.append("[").append(ins.getId()+"-"+fileName).append("],").append("表单实例已更新！\r\n");
		}
	}
	private File findMainFile(String baseFloder) {
		File floder = new File(baseFloder);
		//File floder2 = floder.listFiles()[0];
		//this.packageTempFolder = this.packageTempFolder+File.separator +floder2.getName();
		//File[]files = floder2.listFiles();
		File[]files = floder.listFiles();
		if(files.length==0) {
			throw new RuntimeException("导入的压缩包中没有文件！");
		}
		File mainFile = null;
		boolean flag = false;
		for(File file : files) {
			if(file.isFile()) {
				String name = file.getName();
				if(name.endsWith(IOConstans.FILE_XML_UNIQUE+".xml")) {
					flag = true;
					mainFile = file;
					break;
				}
			}
		}
		if(!flag) {
			throw new RuntimeException("导入的压缩包中没有发现主文件（以###.xml结尾的文件）！");
		}		
		return mainFile;
	} 
}
