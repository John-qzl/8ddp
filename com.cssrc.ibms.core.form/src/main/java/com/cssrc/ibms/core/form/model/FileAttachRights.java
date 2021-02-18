package com.cssrc.ibms.core.form.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 文件附件权限类
 * <p>Title:FileAttachRights</p>
 * @author Yangbo 
 * @date 2016年10月14日下午3:35:52
 */
@XmlType(name = "fileAttachRights")
public class FileAttachRights implements Serializable{
	
	public FileAttachRights() {
	}

	protected Long ID;
	
	protected String rightName;

	protected String rightType;
	
	protected String rightMemo;
	
	protected Long isDel=0L;

	public String getRightName() {
		return rightName;
	}

	public void setRightName(String rightName) {
		this.rightName = rightName;
	}

	public String getRightType() {
		return rightType;
	}

	public void setRightType(String rightType) {
		this.rightType = rightType;
	}

	public String getRightMemo() {
		return rightMemo;
	}

	public void setRightMemo(String rightMemo) {
		this.rightMemo = rightMemo;
	}

	public Long getIsDel() {
		return isDel;
	}

	public void setIsDel(Long isDel) {
		this.isDel = isDel;
	}
	public static Map<String, Long> getTreeDetail(){
		Map<String, Long> p = new HashMap();
		p.put("filefolderview", 1L);
		p.put("filefolderadd", 0L);
		p.put("filefolderedit", 0L);
		p.put("filefolderdel", 0L);
		p.put("filefolderdownload", 1L);
		return p;
	}
	public static Map<String, Long> getListDetail(){
		Map<String, Long> p = new HashMap();
		p.put("attachdownloadall", 1L);
		p.put("manageupdate", 0L);
		p.put("managedownload", 1L);
		p.put("attachdownload", 1L);
		p.put("attachdel", 0L);
		p.put("attachupload", 0L);
		p.put("managedelete", 0L);
		p.put("managepreview", 1L);
		return p;
	}
	//public List<FileAttachRights> getAttachRights(){
	public JSONArray getAttachRights(){
		JSONArray jsonAry = new JSONArray();
		for(int i=1;i<=30;i++){
			switch(i){
			case 1:
				rightName="fileFolderView";
				rightMemo="查看（无此权限，则文件树不可见）";
				break;
			case 2:
				rightName="fileFolderAdd";
				rightMemo="新增";
				break;
			case 3:
				rightName="fileFolderEdit";
				rightMemo="编辑";
				break;
			case 4:
				rightName="fileFolderDel";
				rightMemo="删除";
				break;
			case 5:
				rightName="fileFolderDownload";
				rightMemo="打包下载";
				break;
			case 10:
				rightName="attachUpload";
				rightMemo="上传";
				break;
			case 11:
				rightName="attachDownload";
				rightMemo="下载";
				break;
			case 12:
				rightName="attachDel";
				rightMemo="删除";
				break;
			case 13:
				rightName="attachDownloadall";
				rightMemo="打包下载";
				break;
			case 20:
				rightName="manageDelete";
				rightMemo="删除";
				break;
			case 21:
				rightName="managePreview";
				rightMemo="预览";
				break;
			case 22:
				rightName="manageUpdate";
				rightMemo="更新附件版本";
				break;
			case 23:
				rightName="manageDownload";
				rightMemo="下载";
				break;
			 default:
				rightName="";
				rightMemo="";
				break;
			}
			if(rightName.equals("")||rightMemo.equals("")){
				continue;
			}
			FileAttachRights rights=new FileAttachRights();
			rights.setRightName(rightName);
			rights.setRightMemo(rightMemo);
			JSONObject json = new JSONObject();
			json.put("rightName", rightName);
			json.put("rightMemo", rightMemo);
			jsonAry.add(json);
		}
		return jsonAry;		
	}
	
	
}
