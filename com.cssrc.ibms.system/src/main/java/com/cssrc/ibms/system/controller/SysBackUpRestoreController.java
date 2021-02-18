/**
 * backUpRestore控制层
 */
package com.cssrc.ibms.system.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.SysBackUpRestore;
import com.cssrc.ibms.system.service.SysBackUpRestoreService;
/**
 * 职位功能管理
 * @see
 */
@Controller
@RequestMapping("/oa/system/sysBackUpRestore/")
public class SysBackUpRestoreController extends BaseController {
  
	@Resource
	private SysBackUpRestoreService sysBackUpRestoreService;
	
	/**
	 * 数据列表
	 * @throws Exception 
	 * */
	@RequestMapping( { "list" })
	@Action(description = "查看备份分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception{
		QueryFilter filter = new QueryFilter(request,"sysBackUpRestoreItem");
		List<SysBackUpRestore> list = sysBackUpRestoreService.getAll(filter);
		ModelAndView mv = getAutoView().addObject("sysBackUpRestoreList", list);
		return mv;
	}
	/**
	 * cmd命令
	 * @param backstr
	 * @return
	 */
	public boolean expBackup(String backstr) {
		Runtime rt = Runtime.getRuntime();
		Process processexp = null;
		try {
			processexp = rt.exec(backstr);
			new Thread(new ThreadUtil(processexp.getInputStream())).start();
			new Thread(new ThreadUtil(processexp.getErrorStream())).start();
			try {
				processexp.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 备份还原基础信息获取
	 * @param datestr
	 * @param flag
	 * @return
	 */
	public Map<String,String> getProperties(String datestr,long flag){
		Map<String,String> map=new  HashMap<String, String>();
		//获取数据库用户密码地址
		String path = FileOperator.getClassesPath() + "jdbc.properties";
		String username = FileOperator.readFromProperties(path, "jdbc.username");
		map.put("username", username);
		String password = FileOperator.readFromProperties(path, "jdbc.password");
		map.put("password", password);
		
		//服务器地址
		String url = FileOperator.readFromProperties(path, "jdbc.url");
		String []urls=url.split(":");
		//获取导出路径
		//String backpath = FileOperator.getClassesPath() + "fileServer.properties";
		//String file=FileOperator.readFromProperties(backpath, "fileServer.homedirectory");
		String file = SysConfConstant.UploadFileFolder;
		file=file+File.separator+"dmp";
		//判断文件是否成功
		File isfile=new File(file);
		if(!isfile.exists()){
			isfile.mkdirs();
		}
		map.put("filepath", file);
		String sqlpath="";
		//flag==1代表导出
		if(flag==1){
			sqlpath="exp "+username+"/"+password+urls[3]+":"+urls[4]+"/"+urls[5];
			sqlpath+=""+"file="+'"'+file+File.separator+datestr+".dmp"+'"';
		}else if(flag==2){
			//判断是否存在dmp
			File dmpFile = new File(file + File.separator + datestr+".dmp");
			if(dmpFile.exists())
			{
				sqlpath="imp "+username+"/"+password+urls[3]+":"+urls[4]+"/"+urls[5];
				sqlpath+=""+"file="+'"'+file+File.separator+datestr+".dmp"+'"'+" full=y ignore=y";
			}else{
				sqlpath="未得到dmp";
			}
		}else{
			System.out.println("未得到备份或还原指令！");
		}
		map.put("sqlpath",sqlpath);
		return map;
	}
	/**
	 * 保存备份数据
	 * @throws IOException 
	 * */
	public void backup(SysBackUpRestore sysBackUpRestore) throws IOException{
		Date date=new Date();
		long flag=1;
		sysBackUpRestore.setBackid(Long.valueOf(UniqueIdUtil.genId()));
		sysBackUpRestore.setDatetime(date);
		sysBackUpRestore.setUsername(UserContextUtil.getCurrentUser().getUsername());
		//备份文件名
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String datestr=format.format(date);
		//获取命令
		Map<String,String> map=getProperties(datestr,flag);
		String sqlpath=map.get("sqlpath");
		
		//备份成功
		if(expBackup(sqlpath)){
			sysBackUpRestoreService.add(sysBackUpRestore);
			/*ResultMessage resultMessage =new ResultMessage(ResultMessage.Success, String.valueOf(sysBackUpRestore.getBackid()));
			writeResultMessage(response.getWriter(), resultMessage);*/
		}else{
			System.out.println("备份失败！");
		}
		
	}
	/**
	 * 编辑列表
	 * @param request
	 * @param response
	 * @param sysBackUpRestore
	 * @throws IOException
	 */
	@RequestMapping("edit")
	public ModelAndView edit(HttpServletRequest request) throws Exception{
		/*sysBackUpRestoreService.update(sysBackUpRestore);
		ResultMessage resultMessage =new ResultMessage(ResultMessage.Success, String.valueOf(sysBackUpRestore.getBackid()));
		writeResultMessage(response.getWriter(), resultMessage);*/
		
		Long id = Long.valueOf(RequestUtil.getLong(request, "id", 0L));
		String returnUrl = RequestUtil.getPrePage(request);
		SysBackUpRestore sysBackUpRestore = (SysBackUpRestore) this.sysBackUpRestoreService
				.getById(id);

		return getAutoView().addObject("sysBackUpRestore", sysBackUpRestore).addObject(
				"returnUrl", returnUrl);
	}
	/**
	 * 备份详情
	 * */
	@RequestMapping("get")
	@ResponseBody
	public Map<String,Object> get(HttpServletRequest request,HttpServletResponse response){
		Map <String,Object> map = new HashMap<String,Object>();
		Long id = RequestUtil.getLong(request, "id");
		SysBackUpRestore sysBackUpRestore = sysBackUpRestoreService.getById(id);
		map.put("success", "true");
		map.put("data", sysBackUpRestore);
		return map;
	}
	/**
	 * 还原数据
	 * @throws ParseException 
	 * */
	@RequestMapping("restore")
	@ResponseBody
	public void  restore(HttpServletRequest request,HttpServletResponse response,@RequestParam("datetime") String datetime ) throws ParseException {
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat f2 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date=f1.parse(datetime);
		//flag==2说明还原状态
		long flag=2;
		//获得dmp文件名
		String datestr=f2.format(date);
		
		//获取基本信息
		Map<String,String> map=getProperties(datestr,flag);
		String username=map.get("username");
		String password=map.get("password");
		String sqlpath=map.get("sqlpath");
		//附加执行命令
		StringBuffer sb=new StringBuffer("sqlplus / as sysdba").append("\r\n");
		sb.append("drop user "+username+" cascade;").append("\r\n");
		sb.append("create user "+username+" identified by "+password+";").append("\r\n");
		sb.append("grant connect,resource,dba to "+username+";").append("\r\n");
		sb.append("exit").append("\r\n");
		sb.append(sqlpath).append("\r\n");
		sb.append("exit").append("\r\n");
		String content=sb.toString();
		expBackup(content);
		
	}


	/**
	 * 删除备份数据
	 * @throws IOException 
	 * */
	@RequestMapping("del")
	public void del(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try
		{
			Long[] ids=RequestUtil.getLongAryByStr(request, "id");
			this.sysBackUpRestoreService.delByIds(ids);
			message=new ResultMessage(1,"删除成功");
		}
		catch (Exception ex)
		{
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	
private class ThreadUtil implements Runnable{
		
		private InputStream is;
		public ThreadUtil(InputStream is) {
			this.is = is;
		}
		@Override
		public void run() {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(is, "gbk"));
				String lineStr = null;
				while((lineStr = br.readLine())!=null){
					System.out.println(lineStr);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(br!=null)
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	public void execute(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		
	}
	
	@RequestMapping( { "save" })
	@Action(description = "添加或更新参数表")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SysBackUpRestore sysBackUpRestore) throws Exception {
		String resultMsg = null;
		try {
			boolean isExistParamName = false;
			if ((sysBackUpRestore.getBackid() == null) || (sysBackUpRestore.getBackid().longValue() == 0L)) {
				this.backup(sysBackUpRestore);
				resultMsg = "添加备份成功";
			}else{
				sysBackUpRestoreService.update(sysBackUpRestore);
				resultMsg="备份编辑成功";
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			resultMsg="添加或更新备份错误";
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
			e.printStackTrace();
		}
	}

}
