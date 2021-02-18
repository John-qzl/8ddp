package com.cssrc.ibms.system.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.core.util.bean.BeanUtils;

/**
 * Description:
 * <p>FileDimension.java</p>
 * @author dengwenjie 
 * @date 2017年4月24日
 */
public class FileDimension{
	/*文件类别表DEPTH字段值为1表示为维度节点*/
	public static final Integer dimensionDepth = 1;
	/*需要加载的维度标识*/
	private String dimensionPath;
	/*初始化不成功：传入的list为空*/
	private boolean initSuccess = false;
	/*维度List(只有维度信息)*/
	private List<SysFileType> dimensionList = new ArrayList<SysFileType>();
	/*某维度下属文件类型集合*/
	private List<SysFileType> fileTypeList = new ArrayList<SysFileType>();
	
	private List<SysFileType> allFileTypeList = null;
	
	public void init(List<SysFileType> list,String dimensionKey){
		SysFileType currentDimension = null;
		allFileTypeList = list;
		if(BeanUtils.isEmpty(list)){
			return;
		}
		initDimenSion(list);
		if(BeanUtils.isEmpty(dimensionList)){
			return;
		}	
		if(BeanUtils.isEmpty(dimensionKey)){
			currentDimension = dimensionList.get(0);
			this.dimensionPath = dimensionList.get(0).getNodePath();
		}else{
			for(SysFileType filetype :  dimensionList){
				if(filetype.getNodeKey().equals(dimensionKey)){
					this.dimensionPath = filetype.getNodePath();
					currentDimension = filetype;
					break;
				}
			}
		}
		for(SysFileType filetype :  list){
			if(filetype.getNodePath().contains(this.dimensionPath)){
				fileTypeList.add(filetype);
			}
		}
		//fileTypeList.add(currentDimension);
		initSuccess = true;
	}
	private void initDimenSion(List<SysFileType> list){
		for(SysFileType filetype :  list){
			Integer depth = filetype.getDepth();
			if(depth == dimensionDepth){
				dimensionList.add(filetype);
			}
		}
	}
	public List<SysFileType> getDimensionInfo(List<SysFileType> list){
		List<SysFileType> dList = new ArrayList<SysFileType>();
		if(BeanUtils.isEmpty(list)){
			return list;
		}
		for(SysFileType filetype :  list){
			Integer depth = filetype.getDepth();
			if(depth == dimensionDepth){
				dList.add(filetype);
			}
		}
		return dList;
	}
	public List<Map> getDimensionMap(List<SysFileType> list){
		/*List<Map> map{dimension:,son:}*/
		List<Map> dimensionMap = new ArrayList<Map>();
		List<SysFileType> dList = new ArrayList<SysFileType>();
		if(BeanUtils.isEmpty(list)){
			return dimensionMap;
		}
		//获取维度
		for(SysFileType filetype :  list){
			Integer depth = filetype.getDepth();
			if(depth == dimensionDepth){
				dList.add(filetype);
			}
		}
		//获取维度son
		for(SysFileType filetype :  dList){
			String path = filetype.getNodePath();
			List<SysFileType> sList = new ArrayList<SysFileType>();
			for(SysFileType sonType :  list){
				if(sonType.getNodePath().contains(path)&&!sonType.getNodePath().equals(path)){
					sList.add(sonType);
				}
			}
			Map map = new HashMap();
			map.put("dimension", filetype);
			map.put("son", sList);
			dimensionMap.add(map);
		}
		return dimensionMap;
	}
	public List<SysFileType> getDimensionList() {
		return dimensionList;
	}
	public void setDimensionList(List<SysFileType> dimensionList){
		this.dimensionList = dimensionList;
	}
	public List<SysFileType> getFileTypeList() {
		if(initSuccess){
			return fileTypeList;
		}else{
			return allFileTypeList;
		}
	}
	public void setFileTypeList(List<SysFileType> fileTypeList) {
		this.fileTypeList = fileTypeList;
	}
	public String getDimensionPath() {
		return dimensionPath;
	}
	public void setDimensionPath(String dimensionPath) {
		this.dimensionPath = dimensionPath;
	}
}
