package com.cssrc.ibms.icons.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class IconsService {

	public List<Map<String, Object>> getIcons(String iconPath, String type) throws IOException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			BufferedReader br =new BufferedReader(new InputStreamReader(new FileInputStream(new File(iconPath)),"UTF-8"));
			
			String lineTxt = null;
			while((lineTxt = br.readLine()) != null){
				Map<String, Object> map = new HashMap<String, Object>();
				if(lineTxt.indexOf("before") > 0 && lineTxt.indexOf(type) > 0){
					String iconClassName = lineTxt.substring(lineTxt.indexOf(".")+1, lineTxt.indexOf(":"));
					map.put("iconClassName", iconClassName);
					
					String iconContent = lineTxt.substring(lineTxt.indexOf("\"")+1, lineTxt.indexOf("\";"));
					map.put("iconContent", iconContent);
					list.add(map);
				}
			}
			br.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<Map<String, Object>> getIconImgClassNameAndContent(String iconPath) throws IOException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			BufferedReader br =new BufferedReader(new InputStreamReader(new FileInputStream(new File(iconPath)),"UTF-8"));
			
			String lineTxt = null;
			while((lineTxt = br.readLine()) != null){
				Map<String, Object> map = new HashMap<String, Object>();
				if(lineTxt.indexOf("{") > 0 && lineTxt.indexOf("-") > 0){
					String iconImgClassName = lineTxt.substring(lineTxt.indexOf(".")+1, lineTxt.indexOf("{"));
					map.put("iconImgClassName", iconImgClassName);
					
					String iconImgUrl = lineTxt.substring(lineTxt.indexOf("../i")+2, lineTxt.indexOf("')"));
					map.put("iconImgUrl", iconImgUrl);
					list.add(map);
				}
			}
			br.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
