package com.cssrc.ibms.core.resources.io.dao;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.ins.CheckResult;
import com.cssrc.ibms.core.resources.io.bean.ins.ConditionResult;
import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.ins.ProductColumn;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.dp.form.dao.ProductTypeDao;
import com.mysql.fabric.xmlrpc.base.Array;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
@Repository
public class IOTableInstanceDao {
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private IOCheckResultDao ioCheckResultDao;
	@Resource
	private IOConditionResultDao ioConditionResultDao;
	@Resource
	private IOSignResultDao ioSignResultDao;
	@Resource
	private IOSignDefDao ioSignDefDao;
	@Resource
	private IOCheckConditionDao ioCheckConditionDao;
	@Resource
	private IOCheckItemDefDao ioCheckItemDefDao;
	@Resource
	private ProductTypeDao productTypeDao;
	@Resource
	private IOCheckResultCarryDao ioCheckResultCarryDao;
	@Resource
	private IOCheckResultJGJGDao ioCheckResultJGJGDao;

	public TableInstance getById(String id,boolean init) {
		String sql = " select * from W_TB_INSTANT where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			TableInstance ins = new TableInstance(map);
			if(init) {
				String insId = ins.getId();
				//型号类型
				Map typeMap = productTypeDao.getProductType(Long.valueOf(ins.getId()));
				if("空间".equals(typeMap.get("TYPE"))){
					ins.setCheckResultList(ioCheckResultDao.getByInsId(insId));
				}else if("运载".equals(typeMap.get("TYPE"))){
					ins.setCheckResultList(ioCheckResultCarryDao.getByInsId(insId));
				}else{
					ins.setCheckResultList(ioCheckResultJGJGDao.getByInsId(insId));
				}
				ins.setConditionResultList(ioConditionResultDao.getByInsId(insId));
				ins.setSignResultList(ioSignResultDao.getByInsId(insId));
			}
			return ins;
		}
	}

	/**
	 * 根据策划id查找实例(返回entity
	 * @param planId
	 * @return
	 */
	public List<TableInstance> getByPlanId(String planId) {
		String sql = " select * from W_TB_INSTANT where F_PLANID='"+planId+"'";	
		List<Map<String,Object>>  mapList = jdbcDao.queryForList(sql, null);
		List<TableInstance> tableInstanceList=new ArrayList<>();
		for (Map<String, Object> map : mapList) {
			if(map!=null) {
				tableInstanceList.add(new TableInstance(map));
			}
		}
		return tableInstanceList;
	}
	/**
	 * 根据策划id查找实例(返回entity 不要文件附件  不要文件附件  不要文件附件
	 * @param planId
	 * @return
	 */
	public List<TableInstance> getByPlanIdExceptFile(String planId) {
		String sql = "select * from W_TB_INSTANT where F_PLANID='"+planId+"' and (F_BDZL!='17' or F_BDZL is NULL)" ;
		List<Map<String,Object>>  mapList = jdbcDao.queryForList(sql, null);
		List<TableInstance> tableInstanceList=new ArrayList<>();
		for (Map<String, Object> map : mapList) {
			if(map!=null) {
				tableInstanceList.add(new TableInstance(map));
			}
		}
		return tableInstanceList;
	}
	
	/**
	 * 根据策划id查找已经不是废弃的实例(返回entity
	 * @param planId
	 * @return
	 */
	public List<TableInstance> getCompleByPlanId(String planId) {
		String sql = " select * from W_TB_INSTANT where F_PLANID='"+planId+"' and F_STATUS!='废弃'";	
		List<Map<String,Object>>  mapList = jdbcDao.queryForList(sql, null);
		List<TableInstance> tableInstanceList=new ArrayList<>();
		for (Map<String, Object> map : mapList) {
			if(map!=null) {
				tableInstanceList.add(new TableInstance(map));
			}
		}
		return tableInstanceList;
	}

	public TableInstance getById(String id) {
		String sql = " select * from W_TB_INSTANT where ID='"+id+"'";	
		Map<String, Object> map=jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}
		return new TableInstance(map);
		
	}
	
	/**
	 * 查询
	 * @param keyValueMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> query(Map<String,Object> keyValueMap){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_TB_INSTANT where 1=1 ");
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
	public void update(TableInstance ins) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_TB_INSTANT set ");
		sql.append(" F_NAME=:name,F_NUMBER=:number,F_STATUS=:status,");
		sql.append(" F_TABLE_TEMP_ID=:tableTempID,F_TASK_ID=:taskId,F_CONTENT=:content,");
		sql.append(" F_VERSION=:version ");		
		sql.append(" where id=:id");
		Map map = new HashMap();
		map.put("id", ins.getId());
		map.put("name", ins.getName());
		map.put("number", ins.getNumber());
		map.put("status", ins.getStatus());
		map.put("tableTempID", ins.getTableTempID());
		map.put("taskId", ins.getTaskId());
		map.put("content", ins.getContent());
		map.put("version", ins.getVersion());
		jdbcDao.exesql(sql.toString(), map);		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ProductColumn insertcp(Long id,String F_planId,String F_cpmc,String F_jl,String tableInstanId,int i) {
		ProductColumn productColumn=new ProductColumn();
		Long ID=id;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into W_CPB ");
		sql.append("(ID,F_CPMC,F_PLANID,F_JL,F_ssslId) ");
		sql.append("values(:ID,:F_cpmc,:F_planId,:F_JL,:F_ssslId)");
		Map map=new HashMap();
		map.put("ID", ID);
		map.put("F_cpmc", F_cpmc);
		map.put("F_planId", F_planId); 	
		map.put("F_JL", F_jl);
		map.put("F_ssslId", tableInstanId);
		jdbcDao.exesql(sql.toString(), map);
		productColumn.setProductId(String.valueOf(ID));
		productColumn.setProductName(F_cpmc);
/*		productColumn.setIns_Id(ins_Id);*/
		productColumn.setRow(i);
		return productColumn;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertcp(ProductColumn productColumn) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into W_CPB ");
		sql.append("(ID,F_CPMC,F_PLANID,F_JL,F_ssslId) ");
		sql.append("values(:ID,:F_cpmc,:F_planId,:F_JL,:F_ssslId)");
		Map map=new HashMap();
		map.put("ID", productColumn.getProductId());
		map.put("F_cpmc", productColumn.getProductName());
		map.put("F_planId", productColumn.getPlainId()); 	
		map.put("F_JL", productColumn.getCheck());
		map.put("F_ssslId", productColumn.getIns_Id());
		jdbcDao.exesql(sql.toString(), map);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertReport(String F_planId,JSONObject reportString) {
		StringBuffer sql = new StringBuffer();
		sql.append("update  w_cpyschbgb set F_YSBGHCSJ=:F_YSBGHCSJ where ID=:ID");
		Map map=new HashMap();
		map.put("F_YSBGHCSJ", reportString.toString());
		map.put("ID", F_planId);
		jdbcDao.exesql(sql.toString(), map);
	}
	
	
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertFileData(String F_MZ,String F_PLANID,String F_SJID) {
		Long ID=UniqueIdUtil.genId();
		StringBuffer sql = new StringBuffer();
		String F_SJLB="3";
		if(F_MZ.indexOf("常规")>=0) {
			 F_SJLB="2";
		}
		else if(F_MZ.indexOf("报告")>=0) {
			 F_SJLB="4";
		}else if (F_MZ.indexOf("试验问题")!=-1){
			F_SJLB="10";
		}else if (F_MZ.indexOf("所检问题表")!=-1){
			F_SJLB="13";
		}
		else {
			 F_SJLB="3";
		}
		sql.append(" insert into W_FILE_DATA ");
		sql.append("(ID,F_MZ,F_PLANID,F_SJID,F_SJLB) ");
		sql.append("values(:ID,:F_MZ,:F_PLANID,:F_SJID,:F_SJLB)");

		Map map = new HashMap();
		map.put("ID", ID);
		map.put("F_MZ", F_MZ);
		map.put("F_PLANID", F_PLANID);
		map.put("F_SJID", F_SJID);
		map.put("F_SJLB", F_SJLB);
		jdbcDao.exesql(sql.toString(),map);

	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(TableInstance ins) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_TB_INSTANT ");
		sql.append(" (ID,F_NAME,F_NUMBER,F_STATUS,F_TABLE_TEMP_ID,F_TASK_ID,F_CONTENT,F_VERSION,F_PRODUCTID,F_planId,F_STARTTIME,F_ENDTIME,F_BDZL,F_HCR,F_HCRID)");
		sql.append(" values ");
		sql.append(" (:ID,:F_NAME,:F_NUMBER,:F_STATUS,:F_TABLE_TEMP_ID,:F_TASK_ID,:F_CONTENT,:F_VERSION,:F_PRODUCTID,:F_planId,:F_STARTTIME,:F_ENDTIME,:F_BDZL,:F_HCR,:F_HCRID) ");
		Map map = new HashMap();
		map.put("ID", ins.getId());
		map.put("F_NAME", ins.getName());
		map.put("F_NUMBER", ins.getNumber());
		map.put("F_STATUS", "正在使用");
		map.put("F_TABLE_TEMP_ID", ins.getTableTempID());
		map.put("F_TASK_ID", ins.getTaskId());
		map.put("F_CONTENT", ins.getContent());
		map.put("F_VERSION", ins.getVersion());
		map.put("F_PRODUCTID", ins.getProductId());
	    map.put("F_planId", ins.getPlanId());
	    map.put("F_STARTTIME", ins.getStartTime());
	    map.put("F_ENDTIME", ins.getEndTime());
	    map.put("F_BDZL", ins.getType());
	    map.put("F_HCR", ins.getUserName());
	    map.put("F_HCRID", ins.getUserId());
		jdbcDao.exesql(sql.toString(), map);
		return Long.valueOf(map.get("ID").toString());
	}
	/**
	 * @Author  shenguoliang
	 * @Description: 删除自身及关联信息
	 * @Params [id:表单实例ID, deleteSon:是否删除关联信息]
	 * @Date 2018/4/4 15:13
	 * @Return void
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteId(String id,boolean deleteSon) {
		if(deleteSon) {
			//删除检查结果
			ioCheckResultDao.deleteByIns(id);
			//删除检查条件结果
			ioConditionResultDao.deleteByIns(id);
			//删除签署结果
			ioSignResultDao.deleteByIns(id);
		}
		//删除表单实例前要做判断,如果在数据包详细信息表中,仍有使用该表单实例的情况则不删除(超过1条记录),否则删除掉
		String checkSql = "select count(ID) from W_DATAPACKAGEINFO WHERE F_SSMB = :ID ";
		Map<String,Object> param = new HashMap();
		param.put("ID", id);
		int recordCount = jdbcDao.queryForInt(checkSql, param);
		if(recordCount <=1){
			String sql = "delete from W_TB_INSTANT where ID=:ID";
			Map<String,Object> parameter = new HashMap();
			parameter.put("ID", id);
			jdbcDao.exesql(sql, parameter);
		}else{

		}

	}

	public void insertTableInstant(TableInstance ins,String floderPath) throws ParseException {
		this.insert(ins);
		//签署结果
		List<SignResult> signList = ins.getSignResultList();
		if(signList != null){

			for(SignResult sign : signList) {
				Long tempId = UniqueIdUtil.genId();
				sign.setId(tempId + "");
				sign.setTb_instan_id(ins.getId());
				this.ioSignResultDao.insert(sign,floderPath);
			}
		}
		//检查结果
		List<CheckResult> checkList = ins.getCheckResultList();

		if( checkList != null) {
			for (CheckResult check : checkList) {
				Long tempId = UniqueIdUtil.genId();
				check.setId(tempId + "");
				check.setTb_instan_id(ins.getId());
				this.ioCheckResultDao.insert(check, floderPath);
			}
		}
		//检查条件结果
		List<ConditionResult> conditionList = ins.getConditionResultList();
		if(conditionList != null) {
			for (ConditionResult condition : conditionList) {
				Long tempId = UniqueIdUtil.genId();
				condition.setId(tempId + "");
				condition.setTb_instan_id(ins.getId());
				this.ioConditionResultDao.insert(condition);
			}
		}
	}
	/**
	* @Author  shenguoliang
	* @Description: 表单实例及其相关联表的数据新增(服务器导入)
	* @Params [ins, floderPath]
	* @Date 2018/6/15 9:18
	* @Return void
	*/
	public void insertTableInstantImp(TableInstance ins,String floderPath) throws ParseException {
		//新增表单实例
		this.insert(ins);
		//签署结果表(所属表格实例F_TB_INSTANT_ID 所属签署定义F_SIGNDEF_ID)
		List<SignResult> signList = ins.getSignResultList();
		if(signList != null){
			//根据表单模板ID 找到所对应的签署定义项定义ID
			Map queryMap = new HashMap() ;
			queryMap.put("F_TABLE_TEMP_ID",ins.getTableTempID());
			//签署定义表(所属模板 F_TABLE_TEMP_ID)
			List<Map<String,Object>> signDefList = ioSignDefDao.query(queryMap) ;
			int i = 0 ;

			for(SignResult sign : signList) {
				Long dataId = UniqueIdUtil.genId();
				sign.setId(dataId + "");
				//设置所属表单实例
				sign.setTb_instan_id(ins.getId());
				//设置所属的签署定义
				if(signDefList.size() > i) {
					sign.setSigndef_id(signDefList.get(i).get("ID") + "");
				}else {
					String error = "签署定义所在表单模板未找到,表单模板插入失败";
					throw new RuntimeException(error);
				}
				this.ioSignResultDao.insert(sign,floderPath);
				i++ ;
			}
		}
		//检查结果
		List<CheckResult> checkList = ins.getCheckResultList();
		//根据表单模板ID 找到所对应的检查项定义ID
		Map queryItemDefMap = new HashMap() ;
		queryItemDefMap.put("F_TABLE_TEMP_ID",ins.getTableTempID());
		List<Map<String,Object>> itemDefList = ioCheckItemDefDao.query(queryItemDefMap) ;

		//型号类型
		Map typeMap = productTypeDao.getProductType(Long.valueOf(ins.getId()));
		if( checkList != null) {

			int i = 0 ;
			for (CheckResult check : checkList) {
				String dataIdString= CommonTools.Obj2String(check.getId());
				Long dataId = dataIdString.equalsIgnoreCase("")?UniqueIdUtil.genId():Long.valueOf(dataIdString);
				check.setId(dataId + "");

				check.setTb_instan_id(ins.getId());
				if(itemDefList.size() > i) {
					//设置所属检查结果定义
					check.setItemDef_id(itemDefList.get(i).get("ID") + "");
				}else {
					String error = "检查定义所在表单模板未找到,表单模板插入失败";
					throw new RuntimeException(error);
				}
				if("空间".equals(typeMap.get("TYPE"))){
					this.ioCheckResultDao.insert(check, floderPath);
				}else if("运载".equals(typeMap.get("TYPE"))){
					this.ioCheckResultCarryDao.insert(check, floderPath);
				}else{
					this.ioCheckResultJGJGDao.insert(check, floderPath);
				}
				i++;
			}
		}else {
			for (Map itemDefMap : itemDefList) {
				CheckResult check = new CheckResult();

				Long dataId = UniqueIdUtil.genId();
				check.setId(dataId + "");
				//设置所属表单实例
				check.setTb_instan_id(ins.getId());
				//设置所属检查结果定义
				check.setItemDef_id(itemDefMap.get("ID") + "");

				if ("空间".equals(typeMap.get("TYPE"))) {
					this.ioCheckResultDao.insert(check, floderPath);
				} else if ("运载".equals(typeMap.get("TYPE"))) {
					this.ioCheckResultCarryDao.insert(check, floderPath);
				} else {
					this.ioCheckResultJGJGDao.insert(check, floderPath);
				}
			}
		}
		//检查条件结果
		List<ConditionResult> conditionList = ins.getConditionResultList();
		if(conditionList != null) {
			//根据表单模板ID 找到所对应的检查项定义ID
			Map queryMap = new HashMap() ;
			queryMap.put("F_TABLE_TEMP_ID",ins.getTableTempID());

			List<Map<String,Object>> checkCondDefList = ioCheckConditionDao.query(queryMap) ;
			int i = 0 ;
			for (ConditionResult condition : conditionList) {

				Long dataId = UniqueIdUtil.genId();
				condition.setId(dataId + "");
				condition.setTb_instan_id(ins.getId());

				if(checkCondDefList.size() > i) {
					condition.setCondition_id(checkCondDefList.get(i).get("ID") + "");
				}else {
					String error = "检查条件定义所在表单模板未找到,表单模板插入失败";
					throw new RuntimeException(error);
				}
				this.ioConditionResultDao.insert(condition);
				i++ ;
			}
		}
	}

	public void updateTableInstant(TableInstance ins,String floderPath) {
		//this.insert(ins);
		//签署结果
		List<SignResult> signList = ins.getSignResultList();
		if (signList != null) {
			for(SignResult sign : signList) {
				this.ioSignResultDao.update(sign,floderPath);
			}
		}
		//检查结果
		List<CheckResult> checkList = ins.getCheckResultList();
		if (checkList != null) {
			for(CheckResult check : checkList) {
				this.ioCheckResultDao.update(check,floderPath);
			}
		}
		
		//检查条件结果
		List<ConditionResult> conditionList = ins.getConditionResultList();
		if (conditionList != null) {
			for(ConditionResult condition : conditionList) {
				this.ioConditionResultDao.update(condition);
			}
		}
	}



}
