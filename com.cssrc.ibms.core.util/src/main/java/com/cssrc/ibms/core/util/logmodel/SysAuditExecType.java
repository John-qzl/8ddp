package com.cssrc.ibms.core.util.logmodel;

/**
 * 对象功能:系统日志类型 Model对象
 * @author liubo
 * @date 2017年8月28日下午4:47:01
 */
public enum SysAuditExecType {
	
	NULL("未指定"),
	SELECT_TYPE("查询"),
	UPDATE_TYPE("更新"),
	DELETE_TYPE("删除"),
	ADD_TYPE("新增"),
	FLOW_START("流程启动"),
	EXE_TASK("执行任务"),
	INIT_TYPE("初始化"),
	IMPORT_TYPE("导入"),
	EXPORT_TYPE("导出"),
	LOGIN_TYPE("登录"),
	FILESHRAE_TYPE("文件分享"),
	FILEUPLOAD_TYPE("文件上传"),
	FILEDOWNLOAD_TYPE("文件下载"),
	FILEDELETE_TYPE("文件删除"),
	FILEUPDATE_TYPE("文件修改"),
	FILEPREVIEW_TYPE("文件预览"),
	FILEVERIONUPDATE_TYPE("文件版本更新"),
	BACKUP_TYPE("备份"),
	COPY_TYPE("复制");
	
	private final String type;
	private SysAuditExecType(String type){
		this.type=type;
	}
	
	@Override
	public String toString(){
		return type;
	}
}