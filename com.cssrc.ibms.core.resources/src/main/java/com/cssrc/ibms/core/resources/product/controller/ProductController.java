package com.cssrc.ibms.core.resources.product.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.form.service.DataTemplateService;
import com.cssrc.ibms.core.resources.product.dao.ExeSqlDao;
import com.cssrc.ibms.core.resources.product.service.ProductService;
import com.cssrc.ibms.core.resources.project.service.ProjectService;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.FileXmlUtil;
import com.cssrc.ibms.core.util.file.ZipUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.service.SysParameterService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/project/tree/stree/")
public class ProductController  extends BaseController {
	@Resource
	private ProductService productService;	
	@Resource
	private ProjectService projectService;
	@Resource
	private SysUserService sysUserService ;
	@Resource
	private ExeSqlDao exeSqlDao;
	@Resource
	private SysParameterService sysParameterService;
	@Resource
	private DataTemplateService dataTemplateService;
    
	@RequestMapping({"getTreeData"})
	@ResponseBody
	@Action(description = "加载型号树")
	public void getTreeData(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//IE中文传入后台乱码问题解决
		String flag = URLDecoder.decode(request.getParameter("flag")) ;
		List<String> productTree = new ArrayList<String>();
		String productUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000000480036\"";
		String projectUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000000560353\"";
		String dataPackageUrl = "\"/dataPackage/tree/dataPackage/manage.do\"";
		String flagName= "" ;
		if(flag.equals("空间")){
			flagName = "空间科学" ;
		}else if(flag.equals("运载")){
			flagName = "全部" ;
		}else{
			flagName = flag ;
		}
		
		//添加根节点
		Long rootId = 0L;
		String rootName = ""+flagName+"型号";
//		rootName = URLEncoder.encode(rootName, "UTF-8") ;
		Long rootParentId = -1L;
		String rootNode = "{typeId:"+ rootId +",dbomSql : \"F_TYPE='"+flag+"'\", parentId:"+ rootParentId +", typeName:\""+ rootName +"\" , tempUrl:"+productUrl+", target : \"listFrame\",open:true}";
		productTree.add(rootNode);

		//目录树结构信息
		List<Map<String, Object>> treeInfo = productService.queryTreeAllByType(flag);
		
		if (treeInfo.size() > 0) {
			for(int i = 0; i < treeInfo.size(); i++){
				String typeId = CommonTools.Obj2String(treeInfo.get(i).get("ID"));
				String parentId = "0";
				String typeName = CommonTools.Obj2String(treeInfo.get(i).get("F_XHMC"));
				String node = "{typeId:"+ typeId +",dbomSql:'F_SSXH="+typeId+"',parentId:"+ parentId +", typeName:\""+ typeName +"\" , tempUrl:"+projectUrl+", target : \"listFrame\",open:true}";
				productTree.add(node);
				
				//查询发次信息
				List<Map<String, Object>> projectInfo = projectService.queryProjectNodeById(typeId);
				if(projectInfo.size() > 0){
					for(int j = 0; j < projectInfo.size(); j++){
						String proId = CommonTools.Obj2String(projectInfo.get(j).get("ID"));
						String proName = CommonTools.Obj2String(projectInfo.get(j).get("F_FCMC"));
						String proNode = "{typeId:"+ proId +", parentId:"+ typeId +", typeName:\""+ proName +"\" , tempUrl:"+dataPackageUrl+", target : \"listFrame\",open:true}";
						productTree.add(proNode);
					}
				}
			}			
		}
		
        //利用Json插件将Array转换成Json格式  
        response.getWriter().print(JSONArray.fromObject(productTree).toString());  
	}
	/**
	 * @Author  shenguoliang
	 * @Description:获取当前登录用户相关信息
	 * @Params [request, response]
	 * @Date 2018/5/17 15:52
	 * @Return com.cssrc.ibms.api.sysuser.model.ISysUser
	 */
	@RequestMapping("getCurUserInfo")
	@ResponseBody
	public ISysUser getCurUserInfo(HttpServletRequest request, HttpServletResponse response) {

		Object userInfo = request.getSession().getAttribute("userInfo") ;
		//获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		return curUser;
	}
	@RequestMapping("getNodeCharger")
	@ResponseBody
	public Boolean getNodeCharger(HttpServletRequest request, HttpServletResponse response) {
		Object userInfo = request.getSession().getAttribute("userInfo") ;
		String fcId = request.getParameter("fcId") ;
		//获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		boolean flag = productService.getNodeChargerAndWorkTeam(fcId,curUser.getUserId()) ;

		return flag;
	}

	
	//型号树节点上移
		@RequestMapping("setUp")
		@ResponseBody
		public void setUp(HttpServletRequest request) {
			String fcId = request.getParameter("fcId") ;
			String prId = request.getParameter("prId") ;
			if(prId.equals("0")){
				List<Map<String,Object>> list = productService.selectNullTcpxproduct();//定义一个list寻找tcpx值为空的记录
				
				if(list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String, Object> resMap= list.get(i) ;
						String ID = resMap.get("ID").toString();
						String tcpx=productService.selectTcpxByParentIdFromProduct();
						productService.updateTcpxByIdFromProduct(ID,tcpx);
					}
				}
				String tcpx = productService.selectTcpxByIdFromProduct(fcId); //通过型号id查询同层排序
				String upTcpx = productService.selectUptcpxBytcpx(tcpx,prId);
				String upId = productService.selectIdByTcpxFromProduct(upTcpx); //通过上层型号的同层排序得到上层型号的型号id
				String t = upTcpx;
				productService.updateTcpxByIdFromProduct(upId,tcpx);
				productService.updateTcpxByIdFromProduct(fcId,t);//将同层排序与上层型号的同层排序互换
			}else{
                List<Map<String,Object>> list = productService.selectNullTcpxproject();;//定义一个list寻找tcpx值为空的记录
				
				if(list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String, Object> resMap= list.get(i) ;
						String ID = resMap.get("ID").toString();
						String tcpx=productService.selectTcpxByParentId(prId);
						productService.updateTcpxById(ID,tcpx);
					}
				}
			 
			String tcpx = productService.selectTcpxById(fcId); //通过发次id查询同层排序
			
			String upTcpx = productService.selectUptcpxBytcpx(tcpx,prId);
			String upId = productService.selectIdByTcpx(upTcpx,prId); //通过上层发次的同层排序得到上层发次的发次id
			String t = upTcpx;
			productService.updateTcpxById(upId,tcpx);
			productService.updateTcpxById(fcId,t);//将同层排序与上层发次的同层排序互换
				
			}
			
		}
		
		//型号数节点下移
		@RequestMapping("setDown")
		@ResponseBody
		public void setDown(HttpServletRequest request) {
			String fcId = request.getParameter("fcId") ; 
			String prId = request.getParameter("prId") ;
			if(prId.equals("0")){
				
             List<Map<String,Object>> list = productService.selectNullTcpxproduct();;//定义一个list寻找tcpx值为空的记录
				
				if(list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String, Object> resMap= list.get(i) ;
						String ID = resMap.get("ID").toString();
						String tcpx=productService.selectTcpxByParentIdFromProduct();
						productService.updateTcpxByIdFromProduct(ID,tcpx);
					}
				}
				String tcpx = productService.selectTcpxByIdFromProduct(fcId); //通过型号id查询同层排序
				String downTcpx = productService.selectDowntcpxBytcpx(tcpx,prId);
				String downId = productService.selectIdByTcpxFromProduct(downTcpx); //通过下层型号的同层排序得到下层型号的型号id
				String t = downTcpx;
				productService.updateTcpxByIdFromProduct(downId,tcpx);
				productService.updateTcpxByIdFromProduct(fcId,t);//将同层排序与下层型号的同层排序互换
			}else{
				
				 List<Map<String,Object>> list = productService.selectNullTcpxproject();;//定义一个list寻找tcpx值为空的记录
					
					if(list.size()>0){
						for(int i=0;i<list.size();i++){
							Map<String, Object> resMap= list.get(i) ;
							String ID = resMap.get("ID").toString();
							String tcpx=productService.selectTcpxByParentId(prId);
							productService.updateTcpxById(ID,tcpx);
						}
					}
			String tcpx = productService.selectTcpxById(fcId); //通过发次id查询同层排序
			String downTcpx = productService.selectDowntcpxBytcpx(tcpx,prId);
			String downId = productService.selectIdByTcpx(downTcpx,prId); //通过下层发次的同层排序得到下层发次的发次id
			String t = downTcpx;
			productService.updateTcpxById(downId,tcpx);
			productService.updateTcpxById(fcId,t);//将同层排序与下层发次的同层排序互换
			}
			
		}
		
		//新增节点同层排序自动赋值(发次层)
		@RequestMapping("editTcpx")
		@ResponseBody
		public void editTcpx(HttpServletRequest request) {
			String Id = request.getParameter("Id") ; //得到新增节点的ID
			String prId = productService.selectPridByIdFromProject(Id);
			String tcpx= productService.selectTcpxByParentId(prId);
			productService.updateTcpxById(Id,tcpx);
			/*if(prId.equals("0")){     //当父节点ID是0时，说明新增的节点是型号
				tcpx=productService.selectTcpxByParentIdFromProduct();
				
			}else{       //否则新增的节点是发次
			    tcpx = productService.selectTcpxByParentId(prId); 
			
			}*/
		//	return tcpx;
		}
		
		//新增节点同层排序自动赋值(型号层)
				@RequestMapping("editTcpxProduct")
				@ResponseBody
				public void editTcpxProduct(HttpServletRequest request) {
					String Id = request.getParameter("Id") ; //得到新增节点的ID
					String tcpx=productService.selectTcpxByParentIdFromProduct();
					
					productService.updateTcpxByIdFromProduct(Id,tcpx);
					
						
					
				}
		
		
	/**
	 * @Author  shenguoliang
	 * @Description: 根据实例ID获取检查结果表名
	 * @Params [request, response]
	 * @Date 2018/5/29 14:19
	 * @Return java.lang.String
	 */
	@RequestMapping("getCondResultNameByInsId")
	@ResponseBody
	public String getCondResultNameByInsId(HttpServletRequest request, HttpServletResponse response) {
		Long slId = CommonTools.Obj2Long(request.getParameter("slId")) ;

		String ck_resultName = productService.getCondResultNameByInsId(slId) ;

		return ck_resultName;
	}
	
	/**
	 * Description : 获取所有型号
	 * Author : XYF
	 * Date : 2018年10月18日下午6:13:05
	 * Return : void
	 */
	@RequestMapping({"getAllProduct"})
    @Action(description = "获取所有型号")
    public void getAllProduct(HttpServletRequest request ,HttpServletResponse response)throws IOException {
		List<Map<String,Object>> office = productService.getProjectOffice();
		String product="";
		String pro = request.getParameter("product");
		for(int j=0;j<office.size();j++){
			Object Product = office.get(j).get("F_PRODUCT");
			String Pro = String.valueOf(Product);
			if(Pro.equals(pro)){
				office.remove(j);
			}
		}
		if(office.size()==1){
			Object Product = office.get(0).get("F_PRODUCT");
			product="('"+Product.toString()+"')";
		}else{
			for(int i=0;i<office.size();i++){
	    		if(i==0){
	    			Object Product = office.get(0).get("F_PRODUCT");
	    			product="('"+Product.toString();
	    		}else if(i==office.size()-1){
	    			Object Product = office.get(i).get("F_PRODUCT");
	    			product+="','"+Product.toString()+"')";
	    		}else{
	    			Object Product = office.get(i).get("F_PRODUCT");
	    			product+="','"+Product.toString();
	    		}
	    	}
		}
		
    	List<Map<String,Object>> list = productService.getAllProduct(product);
        // 利用Json插件将Array转换成Json格式
        response.getWriter().print(JSONArray.fromObject(list).toString());
    }
	/**
	 * Description : 根据型号ID获取型号名称并存入项目办人员管理表型号字段中
	 * Author : XYF
	 * Date : 2018年10月18日下午6:13:30
	 * Return : String
	 */
	@RequestMapping({"getProductName"})
    @Action(description = "根据型号ID获取型号名称并存入项目办人员管理表型号字段中")
	@ResponseBody
	public void getProductName(HttpServletRequest request ,HttpServletResponse response)throws IOException {
		String productName="";
		String type = "";
		String productId = request.getParameter("productId");
		String Id = request.getParameter("ID");
		List<Map<String,Object>> list = productService.selectProductById(productId);
		if(list.size()>0){
			Object Name = list.get(0).get("F_XHMC");
			productName = String.valueOf(Name);
			Object Type = list.get(0).get("F_TYPE");
			type = String.valueOf(Type);
		}
		productService.saveProductName(Id,productName,type);
	}
	
	/**
	 * Description : 对型号发次操作时判断当前用户是否是负责该型号的项目办人员
	 * Author : XYF
	 * Date : 2018年10月19日上午11:17:27
	 * Return : String
	 */
	@RequestMapping("checkUser")
	@ResponseBody
	public String checkUser(HttpServletRequest request, HttpServletResponse response) {
		String productId = request.getParameter("Id");
		String result = "0";
		Long curUserId = UserContextUtil.getCurrentUserId();
		String userId = String.valueOf(curUserId);
		List<Map<String,Object>> list = productService.getProjectOffById(productId);
		if(list.size()==0){
			result="2";
		}else if(list.size()>0){
			for(int i=0;i<list.size();i++){
				Object People = list.get(i).get("F_OFFICEPEOPLEID");
				String people = String.valueOf(People);
				String[] Person = people.split(",");
				for(int j=0;j<Person.length;j++){
					String person = Person[j];
					if(person.equals(userId)){
						result="1";
						return result;
					}
				}
			}
		}
		return result;
	}
	@RequestMapping("ExeSqlInfo")
	@ResponseBody
	public void ExeSqlInfo(HttpServletRequest request, HttpServletResponse response) {
		String[] tableNames= {"W_XHJBSXB","W_TABLE_TEMP","W_CPLBPCB","W_CPYSBGB","W_CPYSZB","W_DATAPACKAGEINFO","W_TB_INSTANT"
				,"W_CPYSBGB","W_SIGNDEF","W_SIGNRESULT","CWM_SYS_FILE","W_FILE_DATA","W_CPB","W_YSSJB","W_CK_CONDITION"};
		List<Map<String,Object>> list=new ArrayList<>();
		String folderPath=sysParameterService.getByAlias("UploadFileFolder", "D:\\ibms\\attachFile");
		String filePath=FileXmlUtil.createFilePath(folderPath, "SynchronizeDataFolder");
		for(int i=0;i<tableNames.length;i++) {
			FileXmlUtil.writeXmlFile(tableNames[i]+".xml", exeSqlDao.getExeSql(tableNames[i]), filePath);
		}
		ZipUtil.zip(filePath);
		try {
			FileOperator.downLoadFile(request, response,filePath+".zip", "SynchronizeDataFolder.zip");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	@RequestMapping("parsing")
	@ResponseBody
	public void parsing(HttpServletRequest request, HttpServletResponse response) {
 	/*	String[] tableNames= {"W_XHJBSXB","W_TABLE_TEMP","W_CPLBPCB","W_CPYSBGB","W_CPYSZB","W_DATAPACKAGEINFO","W_TB_INSTANT"
				,"W_CPYSBGB","W_SIGNDEF","W_SIGNRESULT","CWM_SYS_FILE","W_FILE_DATA","W_CPB","W_YSSJB","W_CK_CONDITION"};*/
		String[] tableNames= {"W_XHJBSXB"};
 		String folderPath=sysParameterService.getByAlias("UploadFileFolder", "D:\\ibms\\attachFile");
		String fileName="SynchronizeDataFolder";
		File file=new File(folderPath+"\\"+fileName);
		if(!file.exists()) {
			return;
		}
/*		ZipUtil.unZip(file.getPath(), file.getPath(), "");*/
		for(int i=0;i<tableNames.length;i++) {
			List<Map<String,Object>> list=exeSqlDao.Dom4JforXML(tableNames[i], file.getPath()+"\\"+tableNames[i]);
			System.out.println(list);
		}
		
	}
}
