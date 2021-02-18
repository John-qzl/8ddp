package com.cssrc.ibms.dp.sync.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Set;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import com.cssrc.ibms.dp.sync.bean.MmcBean;
import com.cssrc.ibms.dp.sync.bean.TasksBean;
import com.cssrc.ibms.dp.sync.bean.UserBean;
import com.cssrc.ibms.dp.sync.bean.UserRwTableBean;
import com.cssrc.ibms.dp.sync.service.DataSyncService;


public class BeanToXML {
	
	/**
	 * 用户信息转化成XML
	 * @return
	 */
	public static String userBeanToXML(UserBean bean){
		Writer writer = null;
		String value=null;
		Mapping map = new Mapping();
		try {
			map.loadMapping(DataSyncService.class.getResource("/com/cssrc/ibms/dp/sync/xml/") + "user-gw.xml");
			writer = new StringWriter();
			Marshaller marshaller;
			marshaller = new Marshaller(writer);
			marshaller.setEncoding("UTF-8");
			marshaller.setMapping(map);
			marshaller.marshal(bean);
			value=writer.toString();
			//System.out.println(value);
		} catch (MarshalException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MappingException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
		return value;
	}
	
	/**
	 * 表格信息转换成XML
	 * @return
	 */
	public static String taskBeanToXML(TasksBean tasks){
		Writer a = null;
		String value=null;
		Mapping map = new Mapping();
		try {
			map.loadMapping(DataSyncService.class.getResource("/com/cssrc/ibms/dp/sync/xml/") + "post.xml");
			a = new StringWriter();
			Marshaller marshaller;
			marshaller = new Marshaller(a);
			marshaller.setEncoding("UTF-8");
			marshaller.setMapping(map);
			marshaller.marshal(tasks);
			value=a.toString();
			//System.out.println(value);
		} catch (MarshalException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MappingException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
		return value;
	}
	
	
	/**
	 * 人员任务表格信息转换成XML
	 * @return
	 */
	public static String UserRwTableBeanToXML(UserRwTableBean tempBean){
		Writer a = null;
		String value=null;
		Mapping map = new Mapping();
		try {
			map.loadMapping(DataSyncService.class.getResource("/com/cssrc/ibms/dp/sync/xml/") + "user-rw-table.xml");
			a = new StringWriter();
			Marshaller marshaller;
			marshaller = new Marshaller(a);
			marshaller.setEncoding("UTF-8");
			marshaller.setMapping(map);
			marshaller.marshal(tempBean);
			value=a.toString();
			//System.out.println(value);
		} catch (MarshalException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MappingException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
		return value;
	}
	
	
	/**
	 * 多媒体信息转换成XML
	 * @return
	 */
	public static String MmcToXML(MmcBean mmcBean){
		Writer a = null;
		String value=null;
		Mapping map = new Mapping();
		try {
			map.loadMapping(DataSyncService.class.getResource("/com/cssrc/ibms/dp/sync/xml/") + "mmc.xml");
			a = new StringWriter();
			Marshaller marshaller;
			marshaller = new Marshaller(a);
			marshaller.setEncoding("UTF-8");
			marshaller.setMapping(map);
			marshaller.marshal(mmcBean);
			value=a.toString();
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MappingException e) {
			e.printStackTrace();
		}	 
		return value;
	}
	
}
