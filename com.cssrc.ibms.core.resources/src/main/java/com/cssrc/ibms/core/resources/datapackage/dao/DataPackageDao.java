package com.cssrc.ibms.core.resources.datapackage.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.model.DataPackage;
import com.cssrc.ibms.core.resources.io.bean.DataObject;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.pack.SimplePackage;
import com.cssrc.ibms.core.resources.io.dao.DpFileDao;
import com.cssrc.ibms.core.resources.io.dao.IOCheckResultDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableTempDao;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.system.model.SysFile;
import net.sf.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Repository
public class DataPackageDao {
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private IOTableInstanceDao iOTableInstanceDao;
	@Resource
	private IOTableTempDao iOTableTempDao;
	private IOCheckResultDao iOCheckResultDao;
	@Resource
	private DpFileDao dpFileDao;
	
	public void delAllData(String tablename) {
		String sql = "truncate table " + tablename;
		jdbcDao.exesql(sql, null);
	}
	
	/**
	 * 所选记录是否都在可执行状态范围内
	 * @param ids
	 * @param zts
	 * @return
	 */
	public boolean canOperaterZt(String ids,String[] zts) {
		String ztStr = StringUtil.getStringFromArray(zts, ",");
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from W_DATAPACKAGEINFO ");
		sql.append(" where id in ("+ids+") ");
		sql.append(" and F_ZXZT not in ('"+ztStr.replace(",", "','")+"')");
		int count = jdbcDao.queryForInt(sql.toString(), null);
		return count==0;
	}
	
	/**
	 * 根据主键获取数据包详细信息
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Object> getById(Long id){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_DATAPACKAGEINFO where ID=:ID");
		Map params = new HashMap();
		params.put("ID", id);
		return jdbcDao.queryForMap(sql.toString(), params);
	}

	/**
	 * 更改数据包状态为指定状态
	 * @param id
	 * @return
	 */
	public void changeStatus(String id,String status){
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_DATAPACKAGEINFO set F_ZXZT=:status where ID=:ID");
		Map params = new HashMap();
		params.put("ID", id);
		params.put("status",status);
		jdbcDao.exesql(sql.toString(), params);
	}
	
	/**
	 * 根据planId 策划数据id获取数据包信息
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<DataPackage> getByPlanId(String planId){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_DATAPACKAGEINFO where F_ACCEPTANCEPLANID=:F_ACCEPTANCEPLANID");
		List<DataPackage> dataPackageList=new ArrayList<>();
		Map params = new HashMap();
		params.put("F_ACCEPTANCEPLANID", planId);
		List<Map<String, Object>> mapList=jdbcDao.queryForList(sql.toString(), params);
		if(mapList==null) {
			return null;
		}
		for (Map<String, Object> map : mapList) {
			dataPackageList.add(new DataPackage(map));
		}
		 return dataPackageList;
	}
	
	/**
	 * 根据主键获取数据包详细信息
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String,Object>> getByIds(String ids){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_DATAPACKAGEINFO where ID in ("+ids+")");
		Map params = new HashMap();
		params.put("ID", ids);
		return jdbcDao.queryForList(sql.toString(), params);
	}
	
	
	
	
	/**
	 * Description : 通过所属发次查找数据包信息
	 * Author : XYF
	 * Date : 2018年9月8日下午4:33:31
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectPackageByFcId(String Id){
        String sql="SELECT * FROM W_PACKAGE WHERE F_SSFC ="+Id;
		return jdbcDao.queryForList(sql,null);
    }
	
	/**
	 * Description : 通过所属型号查询发次信息
	 * Author : XYF
	 * Date : 2018年9月10日下午2:25:24
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectprojectByXhId(String Id){
        String sql="SELECT * FROM W_PROJECT WHERE F_SSXH ="+Id;
		return jdbcDao.queryForList(sql,null);
    }
	
	/**
	 * Description : 通过Id查找发次信息
	 * Author : XYF
	 * Date : 2018年9月8日下午4:41:24
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectProjectById(String Id){
        String sql="SELECT * FROM W_PROJECT WHERE ID ="+Id;
		return jdbcDao.queryForList(sql,null);
    }
	
	/**
	 * Description : 通过Id查找型号信息
	 * Author : XYF
	 * Date : 2018年9月10日下午2:27:35
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectProductById(String Id){
        String sql="SELECT * FROM W_PRODUCT WHERE ID ="+Id;
		return jdbcDao.queryForList(sql,null);
    }
	
	/**
	 * Description : 通过用户ID查询用户信息
	 * Author : XYF
	 * Date : 2018年9月14日下午2:00:51
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectUserById(Long Id){
        String sql="SELECT * FROM CWM_SYS_USER WHERE USERID ="+Id;
		return jdbcDao.queryForList(sql,null);
    }
	/**
	 * 查询
	 * @param keyValueMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> query(Map<String,Object> keyValueMap){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_DATAPACKAGEINFO where 1=1 ");
		Set keys = keyValueMap.keySet();
		for(Iterator it = keys.iterator();it.hasNext();) {
			String key = (String)it.next();
			Object value = keyValueMap.get(key);
			sql.append(" and ").append(key).append(" = '").append(value).append("'");
		}
		return jdbcDao.queryForList(sql.toString(), null);
	}
	public void updateFileInfo(String fileInfo,String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_DATAPACKAGEINFO set F_SJZ='").append(fileInfo).append("'");
		sql.append(" where id='").append(id).append("'");
		jdbcDao.exesql(sql.toString(), null);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(DataObject data) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_DATAPACKAGEINFO ");
		sql.append(" (ID, F_SJMC, F_SJLX, F_SJZ, F_GW, F_MJ, F_BMQX, F_SCR, F_SCRID, F_SCSJ, F_BB, F_SM, F_SPJD, F_ZXZT, F_WCSJ, F_SSSJB, F_SSMB, F_SSMBMC)");
		sql.append(" values ");
		sql.append(" (:ID,:F_SJMC,:F_SJLX,:F_SJZ,:F_GW,:F_MJ,:F_BMQX,:F_SCR,:F_SCRID,:F_SCSJ,:F_BB,:F_SM,:F_SPJD,:F_ZXZT,:F_WCSJ,:F_SSSJB,:F_SSMB,:F_SSMBMC) ");
		Map map = new HashMap();
		map.put("ID", data.getId() );
		map.put("F_SJMC", data.getSjmc() );
		map.put("F_SJLX", data.getSjlx() );
		map.put("F_SJZ", data.getSjz());
		map.put("F_GW", data.getGw() );
		map.put("F_MJ", data.getMj() );
		map.put("F_BMQX", data.getBmqx() );
		map.put("F_SCR", data.getScr() );
		map.put("F_SCRID", data.getScrID() );
		map.put("F_SCSJ", DateUtil.getDate(data.getScsj()));
		map.put("F_BB", data.getBb() );
		map.put("F_SM", data.getSm() );
		map.put("F_SPJD", data.getSpjd() );
		map.put("F_ZXZT", data.getZxzt() );
		map.put("F_WCSJ", DateUtil.getDate(data.getWcsj()) );
		map.put("F_SSSJB", data.getSssjb() );
		map.put("F_SSMB", data.getSsmb() );
		String ssmbmc = data.getSsmbmc();
		if(ssmbmc.contains(IOConstans.TABLEFORM_XML_UNIQUE)) {
			ssmbmc = ssmbmc.split(IOConstans.TABLEFORM_XML_UNIQUE)[0];
		}
		map.put("F_SSMBMC",ssmbmc);
		jdbcDao.exesql(sql.toString(), map);
		return Long.valueOf(map.get("ID").toString());
	}
	
	public void insertBean(DataPackage dataPackage) {
		String  sql = SqlHelp.getInsertSql(DataPackage.class, "W_DATAPACKAGEINFO");
		Map<String, Object> map = MapUtil.transBean2Map(dataPackage);
		jdbcDao.exesql(sql, map);
	}
	
	public void update(DataPackage dataPackage) {
		String  sql = SqlHelp.getUpdateSql(DataPackage.class, "W_DATAPACKAGEINFO");
		Map<String, Object> map = MapUtil.transBean2Map(dataPackage);
		jdbcDao.exesql(sql, map);
	}
	/**
	 * @Author  shenguoliang
	 * @Description:数据包详细信息及关联数据信息导入
	 * @Params [data, floderPath]
	 * @Date 2018/6/14 10:01
	 * @Return void
	 */
	public void insertDataObjectImp(DataObject data, SimplePackage pack, String floderPath) throws Exception{
		//新增W_DATAPACKAGEINFO
		this.insert(data);
		String type = data.getSjlx();
		//表单类型实例
		if(type.equals(IOConstans.DATA_PACKAGE_FORM)) {
			String fileName = data.getSsmbmc();
			String filePath = floderPath + File.separator + fileName;
			File file = new File(filePath);
			if(!file.exists()) {
				return;
			}
			FileInputStream is = new FileInputStream(file);
			TableInstance ins = XmlBeanUtil.unmarshall(is, TableInstance.class);
			//设置表单实例ID
			ins.setId( data.getSsmb()+"");
			//设置表单实例所属的表单模板ID
			//根据表单模板编号 表单模板名称以及所属发次过滤 出相应的表单模板
			Map queryMap = new HashMap() ;
			queryMap.put("F_PROJECT_ID",pack.getSsfc());
			queryMap.put("F_NUMBER",ins.getNumber());
			queryMap.put("F_NAME", ins.getName());
			List<Map<String,Object>> dataList = iOTableTempDao.query(queryMap) ;
			//表单模板ID
			String tempId = "";
			//找到过滤后的表单模板后赋值表单实例所属表单模板ID
			if(dataList.size() > 0 ) {
				tempId = CommonTools.Obj2String(dataList.get(0).get("ID"));
				if(tempId != "" || tempId!=null) {
					//设置表单实例所属的表单模板ID
					ins.setTableTempID(tempId);
					//新增表单实例及其相关表的数据
					this.iOTableInstanceDao.insertTableInstantImp(ins, floderPath);
				}else{
					//未找到表单模板(可能原因是 前面服务器导入的操作中未将对应的表单模板插入数据库中)
					System.out.println("未找到编号为:"+ins.getNumber()+",名称为:"+ins.getName()+"的表单实例所属的表单模板");
				}
			}

		}else {//非表单类型
			SysFile sysFile = this.dpFileDao.insert(data.getFile(), floderPath);
			if(sysFile!=null) {
				JSONObject obj =  new JSONObject();
				obj.put("id", ""+sysFile.getFileId());
				obj.put("name", sysFile.getFilename());
				this.updateFileInfo("["+obj.toString()+"]",data.getId());
			}
		}
	}
	
	/**
	 * Description : 在W_PACKAGE表中通过ID查找F_FZRID
	 * Author : XYF
	 * Date : 2018年8月15日上午9:55:48
	 * Return : String
	 */
	public String selectFzrIdById(String Id) {
		String sql="SELECT F_FZRID FROM W_PACKAGE  WHERE ID = '"+Id+"'";
		String FzrId = jdbcTemplate.queryForObject(sql, String.class);
		return FzrId;
	}
	/**
	 * 1.删除表单实例
	 * 2.删除文件
	 * 3.删除自身
	 * 4.(补充)删除该实例录入数据
	 * @param id ： 数据包节点主键 W_DATAPACKAGEINFO 表 的ID
	 * @param deleteSon ：数据包详情的关系信息是否删除
	 */
	@SuppressWarnings("unchecked")
	public void deleteById(String id,boolean deleteSon) {
		if(deleteSon) {
			Map<String,Object> map = getById(Long.valueOf(id));
			if(map!=null) {
				//实例模板ID W_FB_INSTANT
				String insId = CommonTools.Obj2String(map.get("F_SSMB"));
				if(!insId.equals("")) {
					//根据表单实例删除检查结果\检查条件结果\签署结果
					iOTableInstanceDao.deleteId(insId, deleteSon);

				}
			}
		}
		String sql = "delete from W_DATAPACKAGEINFO where ID=:ID";
		Map<String,Object> parameter = new HashMap();
		parameter.put("ID", id);
		jdbcDao.exesql(sql, parameter);	
	}
	public void deleteByPackage(String packageId,boolean deleteSon) {
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_SSSJB", packageId);
		List<Map<String,Object>> list = this.query(keyValueMap);
		for(Map<String,Object> map : list) {
			deleteById(CommonTools.Obj2String(map.get("ID")),deleteSon);
		}
	}
	
	/**
	 * 查找指定模板的不是完成状态的表单数量
	 * @param templateId
	 * @return
	 */
	public Integer countNotFinishedByTemplateId(String templateId){
	   Map<String, Object> mapValue = new HashMap<String, Object>();
	   mapValue.put("templateId", templateId);
	   String sql = "SELECT * FROM W_DATAPACKAGEINFO where F_ZXZT!='已完成' and F_TEMPLATEID=:templateId";
	   List<Map<String, Object>> mapRes=jdbcDao.queryForList(sql,mapValue);
	   Integer number=mapRes.size();
	   return number;
	}

}
