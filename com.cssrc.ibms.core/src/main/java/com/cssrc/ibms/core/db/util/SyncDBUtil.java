package com.cssrc.ibms.core.db.util;

import java.util.Map;


public class SyncDBUtil {
	
	private Map<String,DBSynchronization> dBSynchronization;
	private String dbType;
	
	public void syncDb() throws Exception{
		dBSynchronization.get("syncdb."+dbType).syncDB();
	}


	public Map<String, DBSynchronization> getdBSynchronization() {
		return dBSynchronization;
	}


	public void setdBSynchronization(
			Map<String, DBSynchronization> dBSynchronization) {
		this.dBSynchronization = dBSynchronization;
	}




	public String getDbType() {
		return dbType;
	}


	public void setDbType(String dbType) {
		this.dbType = dbType;
	}


	
}
