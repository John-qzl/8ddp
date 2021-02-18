package com.cssrc.ibms.core.resources.util.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.util.common.CommonTools;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public class SysUserImportDao extends BaseDao<Object>{

	@Override
	public Class getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isExistJobName(String jobname) {
		Boolean flag=true;
		try {
			 String sql = "SELECT count(*) amount FROM CWM_SYS_JOB where JOBNAME='"+jobname+"' and isDelete=0";
			Map<String,Object> map = jdbcTemplate.queryForMap(sql);
			if(map!=null){
				Integer count = Integer.valueOf(CommonTools.Obj2String(map.get("amount")));
				if(count>0){
					flag=true;
				}else{
					flag=false;
				}
			}else{
				flag=false;
			}
		} catch (Exception ex) {
		}
		return flag;
	}

	public boolean isExistposName(String posName) {
		Boolean flag=true;
		try {
			 String sql = "SELECT count(*) amount FROM CWM_SYS_POS where POSNAME='"+posName+"' and isDelete=0";
			Map<String,Object> map = jdbcTemplate.queryForMap(sql);
			if(map!=null){
				Integer count = Integer.valueOf(CommonTools.Obj2String(map.get("amount")));
				if(count>0){
					flag=true;
				}else{
					flag=false;
				}
			}else{
				flag=false;
			}
		} catch (Exception ex) {
		}
		return flag;
	}

}
