
package com.cssrc.ibms.core.web.servlet;

public class UploadListener implements OutputStreamListener{

	// 保存状态的内部类对象
	private FileUploadStats	fileUploadStats	= new FileUploadStats();

	// 构造方法
	public UploadListener(long totalSize){
		fileUploadStats.setTotalSize(totalSize);
	}

	public void start(){
		// 设置当前状态为开始
		fileUploadStats.setCurrentStatus("start");
	}

	public void bytesRead(int byteCount){
		// 将已读取的数据保存到状态对象中
		fileUploadStats.incrementBytesRead(byteCount);
		// 设置当前的状态为读取过程中
		fileUploadStats.setCurrentStatus("reading");
	}

	public void error(String s){
		// 设置当前状态为出错
		fileUploadStats.setCurrentStatus("error");
	}

	public void done(){
		// 设置当前已读取数据等于总数据大小
		fileUploadStats.setBytesRead(fileUploadStats.getTotalSize());
		// 设置当前状态为完成
		fileUploadStats.setCurrentStatus("done");
	}

	public FileUploadStats getFileUploadStats(){
		// 返回当前状态对象
		return fileUploadStats;
	}

	// 保存状态类
	public static class FileUploadStats{

		// 总数据的大小
		private long	totalSize		= 0;
		// 已读数据大小
		private long	bytesRead		= 0;
		// 开始读取的时间[返回以毫秒为单位的当前时间]
		private long	startTime		= System.currentTimeMillis();
		// 默认的状态
		private String	currentStatus	= "none";

		// 属性totalSize的get方法
		public long getTotalSize(){
			return totalSize - 100;
		}

		public void setTotalSize(long totalSize){
			this.totalSize = totalSize;
		}

		// 属性bytesRead的get方法
		public long getBytesRead(){
			return bytesRead;
		}

		// 获得已经上传得时间
		public long getElapsedTimeInSeconds(){
			return (System.currentTimeMillis() - startTime) / 1000;
		}

		// 属性currentStatus的get方法
		public String getCurrentStatus(){
			return currentStatus;
		}

		public void setCurrentStatus(String currentStatus){
			this.currentStatus = currentStatus;
		}

		public void setBytesRead(long bytesRead){
			this.bytesRead = bytesRead;
		}

		public void incrementBytesRead(int byteCount){
			this.bytesRead += byteCount;
		}

	}

}
