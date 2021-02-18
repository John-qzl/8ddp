package com.cssrc.ibms.core.resources.io.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.DpFile;
import com.cssrc.ibms.core.resources.io.bean.ins.CheckResult;
import com.cssrc.ibms.dp.form.dao.ProductTypeDao;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.SysFile;
@Repository
public class IOCheckResultDao {
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private SysFileDao sysFileDao;
	@Resource
	private DpFileDao dpFileDao;
	@Resource
	ProductTypeDao productTypeDao ;
	public CheckResult getById(String id) {
		String sql = " select * from W_CK_RESULT where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			return new CheckResult(map);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void update(CheckResult checkResult) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_CK_RESULT set ");
		sql.append(" F_RESULT=:F_RESULT,F_VALUE=:F_VALUE,F_IFNUMM=:F_IFNUMM,");
		sql.append(" F_SKETCHMAP=:F_SKETCHMAP,F_ITEMDEF_ID=:F_ITEMDEF_ID,F_TB_INSTAN=:F_TB_INSTAN ");
		sql.append(" where id=:ID");
		Map map = new HashMap();
		map.put("ID", checkResult.getId());
		map.put("F_RESULT", checkResult.getResult());
		map.put("F_VALUE", checkResult.getValue());
		map.put("F_IFNUMM", checkResult.getIfNumm());
		map.put("F_SKETCHMAP", checkResult.getSketchMap());
		map.put("F_ITEMDEF_ID", checkResult.getItemDef_id());
		map.put("F_TB_INSTAN", checkResult.getTb_instan_id());
		jdbcDao.exesql(sql.toString(), map);		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(CheckResult checkResult) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_CK_RESULT (ID,F_RESULT,F_VALUE,F_IFNUMM,F_SKETCHMAP,F_ITEMDEF_ID,F_TB_INSTAN)");
		sql.append(" values (:ID,:F_RESULT,:F_VALUE,:F_IFNUMM,:F_SKETCHMAP,:F_ITEMDEF_ID,:F_TB_INSTAN)");
		Map map = new HashMap();
		map.put("ID", checkResult.getId());
		map.put("F_RESULT", checkResult.getResult());
		map.put("F_VALUE", checkResult.getValue());
		map.put("F_IFNUMM", checkResult.getIfNumm());
		map.put("F_SKETCHMAP", checkResult.getSketchMap());
		map.put("F_ITEMDEF_ID", checkResult.getItemDef_id());
		map.put("F_TB_INSTAN", checkResult.getTb_instan_id());
		jdbcDao.exesql(sql.toString(), map);
		return Long.valueOf(map.get("ID").toString());
	}
	public Long insert(CheckResult checkResult,String floderPath) {
		if(checkResult.getImages()!=null) {
			for(DpFile file : checkResult.getImages()) {
				this.dpFileDao.insert(file, floderPath);
			}
		}
		DpFile sfile = checkResult.getSketchImage();
		if(sfile!=null) {
			this.dpFileDao.insert(sfile, floderPath);
		}
		return this.insert(checkResult);
	}
	public void update(CheckResult checkResult,String floderPath) {
		if(checkResult.getImages()!=null) {
			for(DpFile file : checkResult.getImages()) {
				this.dpFileDao.insert(file, floderPath);
			}
		}
		DpFile sfile = checkResult.getSketchImage();
		if(sfile!=null) {
			this.dpFileDao.insert(sfile, floderPath);
		}
		this.update(checkResult);
	}
	/**
	 * 查询
	 * @param keyValueMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> query(Map<String,Object> keyValueMap){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_CK_RESULT where 1=1 ");
		Set keys = keyValueMap.keySet();
		for(Iterator it = keys.iterator();it.hasNext();) {
			String key = (String)it.next();
			Object value = keyValueMap.get(key);
			sql.append(" and ").append(key).append(" = '").append(value).append("'");
		}
		sql.append(" order by to_number(ID) ");
		return jdbcDao.queryForList(sql.toString(), null);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CheckResult> getByInsId(String insId){
		List<CheckResult> checkResultList = new ArrayList();
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_TB_INSTAN", insId);
		List<Map<String,Object>> list = this.query(keyValueMap);
		for(Map<String,Object> map : list) {
			CheckResult s = new CheckResult(map);
			s.setImages(getImages(s.getId()));
			if(!s.getSketchMap().equals("")) {
				s.setSketchImage(getSkepImage(s.getSketchMap()));
			}			
			checkResultList.add(s);
		}
		return checkResultList;
	}
	/**
	 * 检查结果图片
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<DpFile> getImages(String id){
		List<DpFile> files = new ArrayList();
		Map<String,Object> map = new HashMap();
		map.put("tableId", id+"&&W_CK_RESULT");
		List<SysFile> sysfiles = sysFileDao.getByParams(map);
		for(SysFile s: sysfiles) {
			files.add(new DpFile(s));
		}
		return files;
	}
	/**
	 * 检查结果示意图 
	 * @param fileId
	 * @return
	 */
	public DpFile getSkepImage(String fileId){		
		DpFile file = null;
		SysFile sysfile = sysFileDao.getById(Long.valueOf(fileId));
		if(sysfile!=null) {
			file = new DpFile(sysfile);
		}
		return file;
	}
	/**
	 * @Author  shenguoliang
	 * @Description:根据表单实例删除检查结果
	 * @Params [insId, deleteImage]
	 * @Date 2018/4/4 14:02
	 * @Return void
	 */
	public void deleteByIns(String insId) {
		//根据表单实例ID区分检查结果表的类型
		Map typeMap = productTypeDao.getProductType(Long.valueOf(insId));
		String ck_resultName = "" ;
		if(null != typeMap) {
			if ("空间".equals(typeMap.get("TYPE"))) {
				ck_resultName = "W_CK_RESULT";
			} else if ("运载".equals(typeMap.get("TYPE"))) {
				ck_resultName = "W_CK_RESULT_CARRY";
			} else {
				ck_resultName = "W_CK_RESULT_JGJG";
			}
			String sql = "delete from " + ck_resultName + " where F_TB_INSTAN=:F_TB_INSTAN";
			Map<String, Object> parameter = new HashMap();
			parameter.put("F_TB_INSTAN", insId);
			jdbcDao.exesql(sql, parameter);
		}
	}
}
