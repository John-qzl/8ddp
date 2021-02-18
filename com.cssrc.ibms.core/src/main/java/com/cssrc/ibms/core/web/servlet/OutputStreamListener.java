
package com.cssrc.ibms.core.web.servlet;

public interface OutputStreamListener{

	// 启动监听器
	public void start();

	// 读数据
	public void bytesRead(int bytesRead);

	// 出错处理
	public void error(String message);

	// 完成监听
	public void done();
}
