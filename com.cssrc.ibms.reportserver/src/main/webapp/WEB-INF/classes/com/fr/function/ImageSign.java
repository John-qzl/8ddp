package com.fr.function;

import com.fr.data.impl.AbstractDBDataModel;
import com.fr.data.impl.DBTableData;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.script.AbstractFunction;


public class ImageSign extends AbstractFunction {
	public Object run(Object[] args) {
		//数据集
		String connName = (String) args[0];
		//usrtID
		String userID =  (String) args[1];
		//图片根目录
		String pathRoot = (String) args[2];
		//取得结果
		Object object = "";
		//判断用户名是否为空
		if(userID==null||"".equals(userID)){
			object = pathRoot+"system\\default_sys_sign_pic.jpg";
		}else{
			//取得电子签名照
			String selectSql = "select concat(concat(b.FILENAME,'.'),b.EXT) FILENAME from CWM_SYS_USER a,CWM_SYS_FILE b where a.SIGN_PIC=b.FILEID and a.USERID="+userID; 
			//链接数据信息
			AbstractDBDataModel localAbstractDBDataModel = DBTableData.createCacheableDBResultSet(new NameDatabaseConnection(connName),selectSql, 0);
			System.out.println("test"+localAbstractDBDataModel);
			try {
				object = localAbstractDBDataModel.getValueAt(0, 0);
				System.out.println("test"+object);
				//判断是否为空
				if(object==null||"".equals(object)){
					object = pathRoot+"system\\default_sys_sign_pic.jpg";
				}else{
					object = pathRoot+object;
				}
			} catch (Exception e) {
				object = pathRoot+"system\\default_sys_sign_pic.jpg";
			}
		}
		return object;
	}
}
