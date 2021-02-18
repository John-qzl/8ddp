package com.cssrc.ibms.core.resources.io.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.DpFile;
import com.cssrc.ibms.core.resources.io.bean.ins.AcceptanceData;
import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.SysFile;
@Repository
public class IOSignResultDao {
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private SysFileDao sysFileDao;
	@Resource
	private DpFileDao dpFileDao;
	public SignResult getById(String id) { 
		String sql = " select * from W_SIGNRESULT where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			return new SignResult(map);
		}
	}
	
	
	public List<SignResult> getByInstantId(String InstantId) {
		String sql = " select * from W_SIGNRESULT where F_TB_INSTANT_ID='"+InstantId+"'";	
		List<Map<String,Object>> mapList = jdbcDao.queryForList(sql, null);
		List<SignResult> signResultList=new ArrayList<>();
		for (Map<String,Object> map : mapList) {
			if(map!=null) {
				signResultList.add(new SignResult(map));
			}
		}
		return signResultList;
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void update(SignResult signResult) {
		String signTime = signResult.getSignTime();
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_SIGNRESULT set ");
		sql.append(" F_SIGNUSER=:F_SIGNUSER,");
		if (!signTime.equals("")) {
			sql.append(" F_SIGNTIME=to_date('")
				.append(DateUtil.getDateStr(signTime, "yyyy-MM-dd HH:mm:ss"))
				.append("','yyyy/mm/dd HH24:MI:SS'),");
		} else {
			sql.append(" F_SIGNTIME='',");
		}
		sql.append(" F_REMARK=:F_REMARK,");
		sql.append(" F_SIGNDEF_ID=:F_SIGNDEF_ID,F_TB_INSTANT_ID=:F_TB_INSTANT_ID");
		sql.append(" where id=:ID");
		Map map = new HashMap();
		map.put("ID", signResult.getId());
		map.put("F_SIGNUSER", signResult.getSignUser());
		
		//map.put("F_SIGNTIME", signResult.getSignTime());
		map.put("F_REMARK", signResult.getRemark());
		map.put("F_SIGNDEF_ID", signResult.getSigndef_id());
		map.put("F_TB_INSTANT_ID", signResult.getTb_instan_id());
		jdbcDao.exesql(sql.toString(), map);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(SignResult signResult) throws ParseException {		
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_SIGNRESULT (ID,F_SIGNUSER,F_SIGNTIME,F_REMARK,F_SIGNDEF_ID,F_TB_INSTANT_ID)");
		sql.append(" values (:ID,:F_SIGNUSER,:F_SIGNTIME,:F_REMARK,:F_SIGNDEF_ID,:F_TB_INSTANT_ID)");
		Map map = new HashMap();
		map.put("ID", signResult.getId());
		map.put("F_SIGNUSER", signResult.getSignUser());
		map.put("F_SIGNTIME", DateUtil.getDate(signResult.getSignTime()));
		map.put("F_REMARK", signResult.getRemark());
		map.put("F_SIGNDEF_ID", signResult.getSigndef_id());
		map.put("F_TB_INSTANT_ID", signResult.getTb_instan_id());
		jdbcDao.exesql(sql.toString(), map);
		return Long.valueOf(map.get("ID").toString());
	}
	public Long insert(SignResult signResult,String floderPath) throws ParseException {
		DpFile file = signResult.getImage();
		if(file!=null) {
			file.setDataId(signResult.getId());
			this.dpFileDao.insert(file, floderPath);
		}
		
		return this.insert(signResult);
	}
	public void update(SignResult signResult,String floderPath) {
		DpFile file = signResult.getImage();
		if(file!=null) {
			file.setDataId(signResult.getId());
			this.dpFileDao.insert(file, floderPath);
		}
		this.update(signResult);
	}
	/**
	 * 查询签署结果表
	 * @param keyValueMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> query(Map<String,Object> keyValueMap){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_SIGNRESULT where 1=1 ");
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
	public List<SignResult> getByInsId(String insId){
		List<SignResult> signResultList = new ArrayList();
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_TB_INSTANT_ID", insId);
		List<Map<String,Object>> list = this.query(keyValueMap);
		for(Map<String,Object> map : list) {
			SignResult s = new SignResult(map);
			s.setImage(getImage(s.getId()));
			signResultList.add(s);
		}
		return signResultList;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DpFile getImage(String id) {
		DpFile file = null;
		Map<String,Object> map = new HashMap();
		map.put("dataId", id);
		List<SysFile> files = sysFileDao.getByParams(map);
		for(SysFile s: files) {
			file = new DpFile(s);
			break;
		}
		return file;
	}
	
	public int getSignByDef(String signDefId,String instantId) {
		String sql="select count(*) from W_SIGNRESULT where F_SIGNDEF_ID='"+signDefId+"' and F_TB_INSTANT_ID='"+instantId+"'";
		return jdbcDao.queryForInt(sql,null);
	}
	/**
	 * @Author  shenguoliang
	 * @Description:根据表单实例ID删除该表单实例对应的签署结果
	 * @Params [insId]
	 * @Date 2018/4/4 15:07
	 * @Return void
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteByIns(String insId) {
		String sql = "delete from W_SIGNRESULT where F_TB_INSTANT_ID=:F_TB_INSTANT_ID";
		Map<String,Object> parameter = new HashMap();
		parameter.put("F_TB_INSTANT_ID", insId);
		jdbcDao.exesql(sql, parameter);
	}
	
	public void insertAcceptanceData(AcceptanceData acceptanceData) {
		String sql ="insert into w_yssjb(ID,f_ysxm,F_sscp,F_dw,F_yqz,F_scz,F_cpmc,F_slid,F_czyq,F_sfhg) ";
		sql+="values (:ID,:f_ysxm,:F_sscp,:F_dw,:F_yqz,:F_scz,:F_cpmc,:F_slid,:F_czyq,:F_sfhg)";
		Map<String,Object> parameter = new HashMap();
		Long Id=UniqueIdUtil.genId();
		parameter.put("ID",Id);
		parameter.put("f_ysxm",acceptanceData.getAcceptanceProject());
		parameter.put("F_sscp",acceptanceData.getProductId());
		parameter.put("F_dw",acceptanceData.getUnit());
		parameter.put("F_yqz",acceptanceData.getRequiredValue());
		parameter.put("F_scz",acceptanceData.getRealValue());
		parameter.put("F_cpmc",acceptanceData.getProductName());
		parameter.put("F_slid",acceptanceData.getIns_Id());
		parameter.put("F_sfhg", acceptanceData.getIsCheck());
		parameter.put("F_czyq", acceptanceData.getRequirements());
		jdbcDao.exesql(sql, parameter);
	}
}
