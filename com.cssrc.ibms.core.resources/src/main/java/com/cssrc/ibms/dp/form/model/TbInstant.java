package com.cssrc.ibms.dp.form.model;

public class TbInstant {
	/*主键*/
	protected Long ID;
	/*表格名称*/
	protected String name;
	/*表格编号*/
	protected String number;
	/*表格状态*/
	protected String status;
	/*内容*/
	protected String content;
	/*上传时间*/
	protected String uploadtime;
	/*所属表格模板*/
	 protected Long module;
	 /*所属工作项目*/
	 protected Long taskId;
	 /*版本*/
	 protected String version;
	 /*开始检查时间*/
	 protected String starttime;
	 /*结束检查时间*/
	 protected String endtime;
	 
	 public Long getID(){
		 return ID;
	 }
	 public void setID(Long ID){
		 this.ID=ID;
	 }
	 
	 public String getName(){
		 return name;
	 }
	 public void setName(String name){
		 this.name=name;
	 }
	 
	 public String getNumber(){
		 return number;
	 }
	 public void setNumber(String number){
		 this.number=number;
	 }
	 
	 public String getStatus(){
		 return status;
	 }
	 public void setStatus(String status){
		 this.status=status;
	 }
	 
	 public String getContent(){
		 return content;
	 }
	 public void setContent(String content){
		 this.content=content;
	 }
	 
	 public String getUplodatime(){
		 return uploadtime;
	 }
	 public void setUploadtime(String uploadtiem){
		 this.uploadtime=uploadtiem;
	 }
	 
	 public Long getModule(){
			return module;
		}
		public void setModule(Long module){
			this.module=module;
		}
		
		public Long getTaskId(){
			return module;
		}
		public void setTaskId(Long taskId){
			this.taskId=taskId;
		}
		
		public String getVersion(){
			return version;
		}
		public void setVersion(String version){
			this.version=version;
		}
		
		public String getStartatime(){
			 return starttime;
		 }
		 public void setStartdtime(String starttime){
			 this.starttime=starttime;
		 }
		 
		 public String getEndtime(){
			 return endtime;
		 }
		 public void setEndtime(String endtime){
			 this.endtime=endtime;
		 }
}
