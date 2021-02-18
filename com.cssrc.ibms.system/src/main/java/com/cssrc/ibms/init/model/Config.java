package com.cssrc.ibms.init.model;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.system.model.SysParameter;


public class Config extends JdbcDaoSupport{
	public static Config config = null;

	
	// 1.step
	public static void getInstance() {
		System.out.println("创建 CONFIG 对象!");
		config = new Config();
	}
	// 2.step
	private Config() {
		System.out.println("CONFIG 开始配置!");
		try {
			this.initParameter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 3.step
	private void initParameter() throws Exception {
		System.out.println("开始读取文件源信息 !!!");
		// 获取系统参数
		SysParameter sysParameter = new SysParameter();

		StringBuffer sql = new StringBuffer();
		StringBuffer where = new StringBuffer();
		where.append(" WHERE para.NAME IS NOT NULL ");

		// SQL
		sql.append("SELECT para.ID,para.NAME,para.DATATYPE,para.VALUE,para.DESCRIPTION");
		sql.append(" FROM CWM_SYS_PARAMETER para");
		sql.append(where.toString());
		sql.append(" ORDER BY para.ID DESC ");

		// 执行数据库查询
		List<SysParameter> resultList = new ArrayList<SysParameter>();

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			Configuration cfg = new Configuration("jdbc.properties");
			String className = cfg.getValue("jdbc.driverClassName");
			String userName = cfg.getValue("jdbc.username");
			String password = cfg.getValue("jdbc.password");
			String conName = cfg.getValue("jdbc.url");
			Class.forName(className);
			conn = DriverManager.getConnection(conName, userName, password);
			stat = conn.createStatement();
			rs = stat
					.executeQuery("select * from CWM_SYS_PARAMETER order by ID");

			while (rs.next()) {
				SysParameter info = new SysParameter();
				info.setId(Long.valueOf(CommonTools.null2String(rs
						.getString("ID"))));
				info.setParamname(CommonTools.null2String(rs.getString("NAME")));
				info.setDatatype(CommonTools.null2String(rs
						.getString("DATATYPE")));
				info.setParamvalue(CommonTools.null2String(rs.getString("VALUE")));
				info.setParamdesc(CommonTools.null2String(rs
						.getString("DESCRIPTION")));
				resultList.add(info);
			}
		} catch (SQLException e) {
			System.out.println("系统参数初始化失败。。");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("系统参数初始化失败。。");
			e.printStackTrace();
		} finally {
			try {
				stat.close();
				rs.close();
				conn.close();
				conn = null;
				rs = null;
				stat = null;
			} catch (Exception ex) {
				if (stat != null) {
					stat.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
		}

		if (resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
				sysParameter = (SysParameter) resultList.get(i);
				if(BeanUtils.isEmpty(SysConfConstant.SYS_PARAM_MAP.get(sysParameter.getParamname()))){
					SysConfConstant.SYS_PARAM_MAP.put(sysParameter.getParamname(), sysParameter.getParamvalue());
				}

				if (sysParameter.getParamname().equals("SYSTEM_TITLE")) {//系统标题
					SysConfConstant.SYSTEM_TITLE = sysParameter.getParamvalue();
				} else if(sysParameter.getParamname().equals("COMPANY_NAME")){//公司名称
					SysConfConstant.COMPANY_NAME =  sysParameter.getParamvalue();
				}else if(sysParameter.getParamname().equals("SYSTEM_TITLE_LOGO")){//系统logo路径
					SysConfConstant.SYSTEM_TITLE_LOGO = sysParameter.getParamvalue();					
				}else if (sysParameter.getParamname().equals("UploadFileFolder")) {//上传文件路径
					SysConfConstant.UploadFileFolder = sysParameter.getParamvalue();
					//Folder路径
					File folder = new File(SysConfConstant.UploadFileFolder);
					//判断文件夹是否为存在
					if(!folder.exists()){
						//不存在的话，生成文件
						folder.mkdirs();
					}
				}else if(sysParameter.getParamname().equals("RTX_NOTIFY_LINK")){//RTX消息地址
					SysConfConstant.RTX_NOTIFY_LINK = sysParameter.getParamvalue();
					
				}else if(sysParameter.getParamname().equals("RTX_NOTIFY_ON_OFF")){//RTX消息地址
					//判断参数值是否为空
					if(sysParameter.getParamvalue()!=null&&!sysParameter.getParamvalue().equals("")){
						SysConfConstant.RTX_NOTIFY_ON_OFF = Integer.valueOf(sysParameter.getParamvalue()).intValue();
					}
				}else if(sysParameter.getParamname().equals("RTX_RECEIVE_TYPE")){//RTX接收人账户
					//判断参数值是否为空
					if(sysParameter.getParamvalue()!=null&&!sysParameter.getParamvalue().equals("")){
						SysConfConstant.RTX_RECEIVE_TYPE = Integer.valueOf(sysParameter.getParamvalue()).intValue();
					}
				}
			}
		}	
	}

}
