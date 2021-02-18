package com.cssrc.ibms.init.model;

/*
 * Copyright (c) 1999-2009 WION, Inc.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of 
 * WION, Inc. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WION.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.cssrc.ibms.core.constant.system.SysConfConstant;



/********************************************************
@Function Name:  Configuration
@Description:
@Date Created:  2010-9-16 上午11:51:51
@Author: Liang Long
@Last Modified:    ,  Date Modifed:   
********************************************************/
public class Configuration {

	
	private Properties props;
	private InputStream ips;
	private static final Logger log = Logger.getLogger(Configuration.class);

	public Configuration() {
		props = new Properties();
	}

	public Configuration(String filename) {
		props = new Properties();
		try {
			String filepath = SysConfConstant.CONF_ROOT + File.separator + "properties" + File.separator + filename;
			ips = new FileInputStream(filepath);
			props.load(ips);
			ips.close();
		} catch (FileNotFoundException e) {
			log.error("Can't find the file!");
		} catch (IOException e) {
			log.error("Defeat lodaing the file!");
		}
	}

	public String getValue(String key) {
		String value = "";
		if (props.containsKey(key))
			value = props.getProperty(key);
		return value;
	}

	public String getValue(String fileName, String key) {
		String value = null;
		try {
			ips = new FileInputStream(fileName);
			props.load(ips);
			ips.close();
			if (props.containsKey(key)) {
				value = props.getProperty(key);
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			value = "";
		} catch (IOException e) {
			log.error(e.getMessage());
			value = "";
		} catch (Exception ex) {
			log.error(ex.getMessage());
			value = "";
		}
		return value;
	}

	public void clear() {
		props.clear();
	}
}
