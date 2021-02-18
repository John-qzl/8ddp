package com.cssrc.ibms.core.resources.io.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.dao.DataPackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.PackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.TeamDao;
import com.cssrc.ibms.core.resources.io.bean.DataObject;
import com.cssrc.ibms.core.resources.io.bean.DpFile;
import com.cssrc.ibms.core.resources.io.bean.Product;
import com.cssrc.ibms.core.resources.io.bean.Project;
import com.cssrc.ibms.core.resources.io.bean.TestTeam;
import com.cssrc.ibms.core.resources.io.bean.ins.CheckResult;
import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.pack.SimplePackage;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.resources.product.dao.ProductDao;
import com.cssrc.ibms.core.resources.project.dao.ProjectDao;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.ZipUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SysFileService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Service
public class ClientExportService {
	protected Logger logger = LoggerFactory.getLogger(ClientExportService.class);
	
	@Resource
	private ProductDao productDao;
	@Resource
	private DataPackageDao dataPackageDao;
	@Resource
	private PackageDao packageDao;
	@Resource
	private TeamDao teamDao;
	@Resource
	private ProjectDao projectDao;
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private SysFileService sysFileService;
	@Resource
	private IOTableInstanceDao ioTableInstanceDao;
	
	private String packageTempFolder = "";
	
	public void clearTempFileFolder() {
		this.packageTempFolder = "";
	}
	public void initTempFileFolder(String name) {
		this.packageTempFolder = SysConfConstant.UploadFileFolder+File.separator+"temp" + File.separator + 
				name+DateUtil.getCurrentDate("yyyyMMddHHmmss");
		FileOperator.createFolder(this.packageTempFolder);
	}
	/**
	 * @param fcId : 发次Id
	 * @return filePath ：文件路径
	 * @desc导出数据包
	 */
	@SuppressWarnings({"rawtypes" })
	public String exportPackage(Long fcId,String nodeIds) {
		String zipFilePath = "";
		try {
			String filePath = "";
			//Product product = this.getProductByFc(fcId);
			Project project = projectDao.getBeanById(fcId);
			initTempFileFolder(project.getFcmc());
			zipFilePath = this.packageTempFolder+".zip";
			//数据包信息
			List<SimplePackage> packs = this.getPackagesByIds(nodeIds);

			for(Iterator it = packs.iterator();it.hasNext();) {
				SimplePackage sonPack = (SimplePackage)it.next();
				setSonPackages(sonPack);
			}
			project.setList(packs);


			String xml = XmlBeanUtil.marshall(project, Project.class);
			filePath = this.packageTempFolder + File.separator + project.getFcmc()+IOConstans.FILE_XML_UNIQUE+".xml";
			FileOperator.writeFile(filePath,xml,"UTF-8");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			//压缩文件，并返回zip路径
			ZipUtil.zipByFiles(this.packageTempFolder, false,"GBK");
			//ZipUtil.zip(this.packageTempFolder, false,"GBK");
			FileOperator.delFoldsWithChilds(this.packageTempFolder);
			clearTempFileFolder();
		}
		return zipFilePath;
	}
	/**
	 * 根据发次Id获取型号对象
	 * @param fcId : 发次Id
	 * @return
	 */
	private Product getProductByFc(Long fcId) {
		Map<String, Object> map = productDao.getByFcId(fcId);
		Product project = new Product(map);
		return project;
	}	
	/**
	 * 根据发次Id,获取第一层数据包节点集合
	 * @param fcId : 发次Id 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<SimplePackage> getRootPackages(Long fcId){
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_SSFC", fcId);
		keyValueMap.put("F_PARENTID", "0");
		List<Map<String,Object>> list = packageDao.query(keyValueMap);
		return this.listToPackages(list);
	}
	/**
	 * 根据发次Id,获取第一层数据包节点集合
	 * @param fcId : 发次Id 
	 * @return
	 */
	private List<SimplePackage> getPackagesByIds(String ids){
		List<Map<String,Object>> list = packageDao.getByIds(ids);
		return this.listToPackages(list);
	}
	/**
	 * 设置pack的所有子节点
	 * @param pack ：数据包节点对象
	 */
	@SuppressWarnings({"rawtypes" })
	private void setSonPackages(SimplePackage pack){
		String id = pack.getId();
		List<Map<String,Object>> list = packageDao.getSon(Long.valueOf(id));
		List<SimplePackage> sonPacks = this.listToPackages(list);
		for(Iterator it = sonPacks.iterator();it.hasNext();) {
			SimplePackage sonPack = (SimplePackage)it.next();
			this.setSonPackages(sonPack);
		}
		pack.setList(sonPacks);
	}
	/**
	 * list对象转数据包节点集合
	 * @param list
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<SimplePackage> listToPackages(List<Map<String,Object>>  list) {
		List<SimplePackage> blist = new ArrayList();
		for(Map<String,Object> map : list) {
			SimplePackage simple = new SimplePackage(map);
			simple.setTeams(getTeamInfo(map));
			simple.setDatas(getDataObjectInfo(map));
			blist.add(simple);
		}
		return blist;
	}
	/**
	 * 获取工作队信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<TestTeam> getTeamInfo(Map<String,Object> map) {
		List<TestTeam> teams = new ArrayList();
		String sssjb = CommonTools.Obj2String(map.get("ID"));
		Map queryMap = new HashMap();
		queryMap.put("F_SSSJB", sssjb);
		List<Map<String,Object>> teamList = teamDao.query(queryMap);
		for(Map<String,Object> teamMap : teamList) {
			TestTeam team = new TestTeam(teamMap);
			teams.add(team);
		}
		return teams;
	}
	/**
	 * 获取数据包节点详细信息对象
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	private List<DataObject> getDataObjectInfo(Map<String,Object> map) {
		List<DataObject> dataObjects = new ArrayList();
		String sssjb = CommonTools.Obj2String(map.get("ID"));
		Map queryMap = new HashMap();
		queryMap.put("F_SSSJB", sssjb);
		List<Map<String,Object>> dataList = dataPackageDao.query(queryMap);
		for(Map<String,Object> data : dataList) {
			DataObject dataObject = new DataObject(data);
			String type = CommonTools.Obj2String(data.get("F_SJLX"));
			String fileInfo = CommonTools.Obj2String(data.get("F_SJZ"));
			String tableInsId = CommonTools.Obj2String(data.get("F_SSMB"));
			if(type.toString().equals(IOConstans.DATA_PACKAGE_FORM)) {
				dataObject.setSsmbmc(getTableIns(tableInsId));
			}else {
				dataObject.setFile(getDpFile(fileInfo));
			}			
			dataObjects.add(dataObject);
		}
		return dataObjects;
	}
	/**
	 * 获取上传的文件信息
	 * @param fileInfo
	 * @return
	 */
	private DpFile getDpFile(String fileInfo) {
		DpFile file = null;
		try {
			JSONArray arr = JSONArray.fromObject(fileInfo);
			JSONObject obj = (JSONObject)arr.get(0);
			Long fileId = obj.getLong("id");
			SysFile sysFile = sysFileService.getById(fileId);
			file = new DpFile(sysFile);
			//将文件导出到指定位置
			String sourcePath = SysConfConstant.UploadFileFolder+File.separator+file.getFilePath();
			String filename = file.getFullFileName();
			String targetPath = this.packageTempFolder +File.separator + filename;
			FileOperator.copyFile(sourcePath, targetPath);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return file;
	}
	/**
	 * 获取表单实例
	 * @param id
	 * @return
	 */
	private String getTableIns(String id) {
		String name="";
		try {
			TableInstance ins = ioTableInstanceDao.getById(id,true);
			if(ins==null) {
				return name;
			}
			this.exportInsImages(ins);
			name = ins.getName()+IOConstans.TABLEFORM_XML_UNIQUE+ins.getId()+".xml";
			String xml = XmlBeanUtil.marshall(ins, TableInstance.class);
			String filePath = this.packageTempFolder + File.separator + name;
			FileOperator.writeFile(filePath,xml,"UTF-8");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return name;
	}
	private void exportInsImages(TableInstance ins) {
		List<CheckResult> checkList = ins.getCheckResultList();
		for(CheckResult cr : checkList) {
			List<DpFile> files = cr.getImages();
			for(DpFile file : files) {
				String sourcePath = SysConfConstant.UploadFileFolder+File.separator+file.getFilePath();
				String targetPath = this.packageTempFolder +File.separator + file.getFullFileName();
				FileOperator.copyFile(sourcePath, targetPath);
			}
			DpFile sketchImage = cr.getSketchImage();
			if(sketchImage!=null) {
				String sourcePath = SysConfConstant.UploadFileFolder+File.separator+sketchImage.getFilePath();
				String targetPath = this.packageTempFolder +File.separator + sketchImage.getFullFileName();
				FileOperator.copyFile(sourcePath, targetPath);
			}
		}
		List<SignResult>  sList = ins.getSignResultList();
		for(SignResult s : sList) {
			DpFile f = s.getImage();
			if(f!=null) {
				String sourcePath = SysConfConstant.UploadFileFolder+File.separator+f.getFilePath();
				String targetPath = this.packageTempFolder +File.separator + f.getFullFileName();
				FileOperator.copyFile(sourcePath, targetPath);
			}
		}
	}
}
