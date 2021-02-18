package com.cssrc.ibms.dp.form.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.core.resources.datapackage.dao.DataPackageDao;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.csource.common.MyException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import com.cssrc.ibms.core.resources.io.dao.IOCheckConditionDao;
import com.cssrc.ibms.core.resources.io.dao.IOSignDefDao;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.resources.product.service.ProductService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.ZipCompressor;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.dp.form.dao.CheckConditionDao;
import com.cssrc.ibms.dp.form.dao.CheckFormDao;
import com.cssrc.ibms.dp.form.dao.CheckResultCarryDao;
import com.cssrc.ibms.dp.form.dao.CheckResultDao;
import com.cssrc.ibms.dp.form.dao.CheckResultJgjgDao;
import com.cssrc.ibms.dp.form.dao.CkConditionResultDao;
import com.cssrc.ibms.dp.form.dao.DefineCheckTypeDao;
import com.cssrc.ibms.dp.form.dao.DefineSignDao;
import com.cssrc.ibms.dp.form.dao.FormFolderDao;
import com.cssrc.ibms.dp.form.dao.FormUtilDao;
import com.cssrc.ibms.dp.form.dao.ProductTypeDao;
import com.cssrc.ibms.dp.form.dao.SignResultDao;
import com.cssrc.ibms.dp.form.dao.TbInstantDao;
import com.cssrc.ibms.dp.form.model.CheckCondition;
import com.cssrc.ibms.dp.form.model.CheckForm;
import com.cssrc.ibms.dp.form.model.CheckResult;
import com.cssrc.ibms.dp.form.model.CheckResultCarry;
import com.cssrc.ibms.dp.form.model.CheckResultJgjg;
import com.cssrc.ibms.dp.form.model.CkConditionResult;
import com.cssrc.ibms.dp.form.model.CrossRangeCellMeta;
import com.cssrc.ibms.dp.form.model.DefineCheckType;
import com.cssrc.ibms.dp.form.model.DefineSign;
import com.cssrc.ibms.dp.form.model.SignResult;
import com.cssrc.ibms.dp.form.model.Tcell;
import com.cssrc.ibms.dp.form.util.FormUtils;
import com.cssrc.ibms.dp.form.util.StandExlParseUtil;
import com.cssrc.ibms.dp.sync.util.SyncBaseFilter;
import com.cssrc.ibms.system.service.GlobalTypeService;
import com.cssrc.ibms.system.service.SysFileFolderService;
import com.cssrc.ibms.system.service.SysFileTypeService;
import com.cssrc.ibms.system.service.SysParameterService;
import com.cssrc.ibms.system.service.SysTypeKeyService;
import com.fr.report.core.A.i;

import groovyjarjarasm.asm.tree.IntInsnNode;

@Service
public class FormService {
    @Resource
    CheckConditionDao checkConditionDao;
    @Resource
    DefineSignDao defineSignDao;
    @Resource
    CheckFormDao checkFormDao;
    @Resource
    DefineCheckTypeDao definechecktypeDao;
    @Resource
    private FormFolderDao formFolderDao;
    @Resource
    TbInstantDao tbInstantDao;
    @Resource
    IOCheckConditionDao ioCheckConditionDao;
    @Resource
    private SysParameterService sysParameterService;
    @Resource
    CkConditionResultDao ckconditionresultDao;
    @Resource
    SignResultDao signresultDao;
    @Resource
    CheckResultDao checkresultDao;
    @Resource
    ProductTypeDao productTypeDao;
    @Resource
    CheckResultCarryDao checkresultCarryDao;
    @Resource
    CheckResultJgjgDao checkresultJgjgDao;
    @Resource
    FormUtilDao formUtilDao;
    @Resource
    private GlobalTypeService globalTypeService;
    @Resource
    private SysFileTypeService sysFileTypeService;
    @Resource
    private SysTypeKeyService sysTypeKeyService;
    @Resource
    private SysFileFolderService sysFileFolderService;
    @Resource
    private FormValidateService formValidateService;
    @Resource
    private ProductService productService;
    @Resource
    private ExcelPoiReadService excelPoiReadService;
    @Resource
    private DataPackageDao dataPackageDao;
    @Resource
    private IOSignDefDao ioSignDefDao;

    /**
     * !一个表单内的每个Input，全局[数据库中]唯一ID!
     * 校验用户粘贴、设计的表单注意事项：
     * 1.不支持Table嵌套、行或列数不能超过100【嵌套的内部Table会被忽略】
     * 2.第一个表格为标题、签署、检查条件、注意事项
     * 2.1  第一行第一列为标题名
     * 2.2  第二行前n列有内容的为多个签署人或检查人
     * 2.3  第三行前n列有内容的为多个检查条件
     * 2.4  第四行第一个列为注意事项
     * 3.输入项必须成行或者列 【输入项之间可以有不填项】
     * 4.输入项内的文本信息不会被当做标题【输入项内不要加入要查询的标题】
     * 5.第2、3、4.....n个表格作为一张检查表内的子表单
     * 以下为处理时的要求：
     * 6.防止数据量过大 对style的内容进行删减 仅保存width 和必要的 重点标记项
     * 7.样式 从Table处引用样式，样式统一
     * 8.前端:添加-格式刷功能-将Table的width改为98% 整体居中，~将tr的width值后加%~绝对去除LINE-HEIGHT样式~td的style删除
     *
     * @param html
     * @return [true/false,msg]
     */
    /*@SuppressWarnings("unchecked")
    public Map<String, Object> check(String html,String MID) {
		String msg="校验成功！";
		Map<String, Object>message=new TreeMap<String, Object>();
		//dom4j中不能出现& 否则无法转换
		Map<String, Object>TableRes;
		String title="";
		List<Map<String, Object>>formcontent=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>>colorformcontent=new ArrayList<Map<String, Object>>();
		Document tableDoc = null;
		try {
			html = html.replaceAll("&", "#");
			tableDoc = DocumentHelper.parseText(html);
		} catch (DocumentException e) {
			e.printStackTrace();
			msg="无法从html转到Dom对象：请检查html文件的闭合情况";
			message.put("success", "false");
			message.put("msg", msg);
			return message;
		}
		try {
			html="<html>"+html+"</html>";
			TableRes=FormUtils.getTableRes(html);
			String new_html=TableRes.get("html").toString();
			if(new_html!=""){
			//	msg="表单输入错误";
				msg=TableRes.get("error").toString();
				message.put("success", "false");
				message.put("msg", msg);
				message.put("html", new_html);
				return message;
			}else{
				message.put("success", "true");
				message.put("msg", msg);
				return message;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.put("success", "false");
			message.put("msg", "校验错误");
			return message;
		}
		
	}*/
    @SuppressWarnings("unchecked")
    public Map<String, Object> save(String html, String MID, String type) {
        String msg = "保存成功！";
        Map<String, Object> message = new TreeMap<String, Object>();
        //dom4j中不能出现& 否则无法转换
        Map<String, Object> TableRes;
        String title = "";
        List<Map<String, Object>> formcontent = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> colorformcontent = new ArrayList<Map<String, Object>>();
        Document tableDoc = null;
        try {
            html = html.replaceAll("&", "#");
            html = "<html>" + html + "</html>";
            tableDoc = DocumentHelper.parseText(html);
        } catch (DocumentException e) {
            e.printStackTrace();
            msg = "无法从html转到Dom对象：请检查html文件的闭合情况";
            message.put("success", "false");
            message.put("msg", msg);
            return message;
        }
        try {

            TableRes = FormUtils.getTableRes(html, type);
            title = TableRes.get("titles").toString();
            formcontent = (List<Map<String, Object>>) TableRes.get("formcontent");
            colorformcontent = (List<Map<String, Object>>) TableRes.get("colorformcontent");
        } catch (Exception e) {
            e.printStackTrace();
            message.put("success", "false");
            message.put("msg", "检查项保存失败");
            return message;
        }

        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("ID", MID);
            param.put("CONTENTS", title);
            param.put("status", IOConstans.TABLE_TEMP_COMPLETE);
            updateTableTemp(param);
            definechecktypeDao.deleteTableItem(MID);
            DefineCheckType definechecktype = new DefineCheckType();
            for (int i = 0; i < formcontent.size(); i++) {
                Map<String, Object> content = formcontent.get(i);
                Map<String, Object> colorcontent = colorformcontent.get(i);
                String colordescribe = (String) colorcontent.get("colordescribe");
                String colordescribenew = "";
                String[] shortname = colordescribe.split(",");
                if (shortname.length == 1) {
                    if (shortname[0].indexOf("*/") > -1)
                        colordescribenew = shortname[0].replaceAll("[*/]", "");
                    else if (shortname[0].indexOf("/*") > -1)
                        colordescribenew = shortname[0].replaceAll("[/*]", "");
                    else if (shortname[0].indexOf("*") > -1)
                        colordescribenew = shortname[0].replaceAll("[*]", "");
                }
                if (shortname.length == 2) {
                    if (shortname[0].contains("*")) {
                        if (shortname[0].indexOf("*/") > -1)
                            colordescribenew = shortname[0].replaceAll("[*/]", "") + shortname[1];
                        else if (shortname[0].indexOf("/*") > -1)
                            colordescribenew = shortname[0].replaceAll("[/*]", "") + shortname[1];
                        else if (shortname[0].indexOf("*") > -1)
                            colordescribenew = shortname[0].replaceAll("[*]", "") + shortname[1];
                    } else {
                        if (shortname[1].indexOf("*/") > -1)
                            colordescribenew = shortname[0] + "," + shortname[1].replaceAll("[*/]", "");
                        else if (shortname[1].indexOf("/*") > -1)
                            colordescribenew = shortname[0] + "," + shortname[1].replaceAll("[/*]", "");
                        else if (shortname[1].indexOf("*") > -1)
                            colordescribenew = shortname[0] + "," + shortname[1].replaceAll("[*]", "");
                    }
                }

                definechecktype.setID((Long) content.get("id"));
                definechecktype.setType((int) content.get("type"));
                definechecktype.setShortname(colordescribenew);
                definechecktype.setDescribe((String) content.get("describe"));
                definechecktype.setModuleID(Long.parseLong(MID));
                definechecktype.setIILdd("否");
                definechecktype.setILdd("否");
                definechecktype.setZhycdz("否");
                definechecktype.setNjljyq("否");
                definechecktype.setYcn("否");
                if (content.get("photo").equals("true"))
                    definechecktype.setIfmedia("是");
                else
                    definechecktype.setIfmedia("否");
                definechecktypeDao.add(definechecktype);
            }
            message.put("success", "true");
            message.put("msg", msg);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            message.put("success", "false");
            message.put("msg", "检查项保存失败");
            return message;
        }
    }


    public Map<String, Object> saveBasicInfo(String id, String name, String[] Sign, String[] Status, String attention, String pid, String fcid, String type, String formStatus) {
        Map<String, Object> message = new HashMap<String, Object>();
        Map<String, Object> checkRes = check_ID(id, "");
        if (checkRes.get("success").equals("false")) {
            return checkRes;
        }
        String msg = "";
        Long ID = UniqueIdUtil.genId();//模板ID
        try {
            CheckForm checkform = new CheckForm();
            checkform.setCheckformId(ID);
            checkform.setName(name);
            checkform.setSnum(id);
            checkform.setRemark(attention);
            checkform.setPid(Long.parseLong(pid));
            checkform.setFid(Long.parseLong(fcid));
            checkform.setType(type);
            checkform.setStatus(formStatus);
            checkFormDao.add(checkform);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "模板基础信息保存失败:" + e.getMessage();
            message.put("success", "false");
            message.put("msg", msg);
            return message;
        }

        message = saveCheckCondition(Status, ID);
        if (message.get("success").equals("false")) {
            message.put("ID", ID);
            return message;
        } else {
            message = saveDefineSign(Sign, ID);
            if (message.get("success").equals("false")) {
                message.put("ID", ID);
                return message;
            }
        }
        message.put("success", "true");
        message.put("msg", "模板基础信息保存成功");
        message.put("ID", ID);
        return message;
    }

    private Map<String, Object> saveDefineSign(String[] sign, Long tableTempId) {
        Map<String, Object> message = new HashMap<String, Object>();
        try {
            DefineSign definesign;
            int k = 1;
            for (String define : sign) {
                if (define == null || define.equals("")) {
                    continue;
                }
                definesign = new DefineSign();
                Long pk = UniqueIdUtil.genId();
                definesign.setDefinesignId(pk);
                definesign.setName(define);
                definesign.setSequence(k);
                definesign.setModule(tableTempId);
                defineSignDao.add(definesign);
                k++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "签署信息保存失败:" + e.getMessage();
            message.put("success", "false");
            message.put("msg", msg);
            return message;
        }
        message.put("success", "true");
        return message;
    }

    private Map<String, Object> saveCheckCondition(String[] status, Long tableTempId) {
        Map<String, Object> message = new HashMap<String, Object>();
        try {
            CheckCondition checkcondition;
            int k = 1;
            for (String check : status) {
                if (check == null || check.equals("")) {
                    continue;
                }
                checkcondition = new CheckCondition();
                Long pk = UniqueIdUtil.genId();
                checkcondition.setCheckconditionId(pk);
                checkcondition.setName(check);
                checkcondition.setSequence(k);//顺序
                checkcondition.setModule(tableTempId);
                checkConditionDao.add(checkcondition);
                k++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "检查条件保存失败:" + e.getMessage();
            message.put("success", "false");
            message.put("msg", msg);
            return message;
        }
        message.put("success", "true");
        return message;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> ifExistIns(String id) {
        Map<String, Object> message = new TreeMap<String, Object>();
        Integer num = tbInstantDao.isInstantExists(id);
        if (num > 0) {
            message.put("success", "false");
            message.put("msg", "存在表单实例，请先删除表单实例以及工作队信息");
            return message;
        } else {
            message.put("success", "true");
            return message;
        }
    }

//	@SuppressWarnings("unchecked")
//	public Map<String, Object> modifycheck(String html,String id) {
//		String msg="修改成功！";
//		Map<String, Object>message=new TreeMap<String, Object>();
//		//dom4j中不能出现& 否则无法转换
//			Map<String, Object>TableRes;
//			String title="";
//			List<Map<String, Object>>formcontent=new ArrayList<Map<String, Object>>();
//			List<Map<String, Object>>colorformcontent=new ArrayList<Map<String, Object>>();
//			Document tableDoc = null;
//			try {
//				html = html.replaceAll("&", "#");
//				int index=html.indexOf("</table>");
//				html=html.substring(0, index+8);
//				tableDoc = DocumentHelper.parseText(html);
//			} catch (DocumentException e) {
//				e.printStackTrace();
//				msg="无法从html转到Dom对象：请检查html文件的闭合情况";
//				message.put("success", "false");
//				message.put("msg", msg);
//				return message;
//			}
//			try {
//				html="<html>"+html+"</html>";
//				TableRes=FormUtils.getTableRes(html);
//				String new_html=TableRes.get("html").toString();
//				if(new_html!=""){
//					msg=TableRes.get("error").toString();
//					message.put("success", "false");
//					message.put("msg", msg);
//					message.put("html", new_html);
//					return message;
//				}else{
//					title=TableRes.get("titles").toString();
//					formcontent=(List<Map<String, Object>>)TableRes.get("formcontent");
//					colorformcontent=(List<Map<String, Object>>)TableRes.get("colorformcontent");
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			try{
//				CheckForm checkform=new CheckForm();
//				checkform.setCheckformId(Long.parseLong(id));
//				checkform.setContents(title);
//				checkFormDao.update(checkform);
//				definechecktypeDao.deleteTableItem(id);
//				
//				DefineCheckType definechecktype=new DefineCheckType();
//				for(int i=0;i<formcontent.size();i++){
//					Map<String,Object> content=formcontent.get(i);
//					Map<String,Object> colorcontent=colorformcontent.get(i);
//					String colordescribe=(String)colorcontent.get("colordescribe");
//					String colordescribenew="";
//					String [] shortname=colordescribe.split(",");
//					if(shortname.length==1){
//						if(shortname[0].indexOf("*/")>-1)
//							colordescribenew=shortname[0].replaceAll("[*/]", "");
//						else  if(shortname[0].indexOf("/*")>-1)
//							colordescribenew=shortname[0].replaceAll("[/*]", "");
//						else if(shortname[0].indexOf("*")>-1)
//							colordescribenew=shortname[0].replaceAll("[*]", "");
//					}	
//					if(shortname.length==2){
//						if(shortname[0].contains("*")){
//							if(shortname[0].indexOf("*/")>-1)
//								colordescribenew=shortname[0].replaceAll("[*/]", "")+shortname[1];
//							else if(shortname[0].indexOf("/*")>-1)
//								colordescribenew=shortname[0].replaceAll("[/*]", "")+shortname[1];
//							else if(shortname[0].indexOf("*")>-1)
//								colordescribenew=shortname[0].replaceAll("[*]", "")+shortname[1];
//						}else{
//							if(shortname[1].indexOf("*/")>-1)
//								colordescribenew=shortname[0]+","+shortname[1].replaceAll("[*/]", "");
//							else if(shortname[1].indexOf("/*")>-1)
//								colordescribenew=shortname[0]+","+shortname[1].replaceAll("[/*]", "");
//							else if (shortname[1].indexOf("*")>-1)
//								colordescribenew=shortname[0]+","+shortname[1].replaceAll("[*]", "");
//						}
//					}
//					definechecktype.setID((Long)content.get("id"));
//					definechecktype.setType((int)content.get("type"));
//					definechecktype.setShortname(colordescribenew);
//					definechecktype.setDescribe((String)content.get("describe"));
//					definechecktype.setModuleID(Long.parseLong(id));
//					definechecktype.setIILdd("否");
//					definechecktype.setILdd("否");
//					definechecktype.setZhycdz("否");
//					definechecktype.setNjljyq("否");
//					definechecktype.setYcn("否");
//					if(content.get("photo").equals("true"))
//						definechecktype.setIfmedia("是");
//					else
//						definechecktype.setIfmedia("否");
//					definechecktypeDao.add(definechecktype);
//				}
//				
//				message.put("success", "true");
//				message.put("msg", msg);
//				return message;
//			}catch(Exception e){
//				e.printStackTrace();
//				message.put("success", "false");
//				message.put("msg", "数据库修改数据失败");
//				return message;
//			}
//		
//	}

    @SuppressWarnings("unchecked")
    public Map<String, Object> deletecheck(String id) {
        Map<String, Object> message = new TreeMap<String, Object>();
        Integer num=dataPackageDao.countNotFinishedByTemplateId(id);
        //Integer num = tbInstantDao.isInstantExists(id);
        if (num > 0) {
            message.put("success", "false");
            message.put("msg", "存在未完成的表单");
           // message.put("msg", "存在表单实例，请先删除表单实例以及工作队信息");
            return message;

        } else {
            try {
                checkFormDao.deleteTemp(id);
                checkConditionDao.deleteCondition(id);
                definechecktypeDao.deleteTableItem(id);
                defineSignDao.deleteSign(id);
                message.put("success", "true");
                message.put("msg", "模板删除成功");
                return message;
            } catch (Exception e) {
                message.put("success", "false");
                message.put("msg", "模板删除失败");
                return message;
            }
        }
    }


    /**
     * 验证表单的表单编号是否唯一
     *
     * @param mid
     */
    public Map<String, Object> check_ID(String number, String mid) {
        Map<String, Object> message = new TreeMap<String, Object>();
        //如果有mid 说明是已经存在了 
        if (StringUtil.isNotEmpty(mid)) {
            //如果当前编号和id匹配 说明用户未修改 可以使用
            try {
                Integer num = checkFormDao.isNumberExistsByTabelTempId(number, mid);
                if (num == 1) {
                    message.put("success", "true");
                    message.put("msg", "检查表编号未被使用");
                    return message;
                }
            } catch (Exception e) {
                message.put("success", "false");
                message.put("msg", "数据库查询失败！");
                e.printStackTrace();
                return message;
            }

        }
        //直接查询编号是否唯一
        Integer num = checkFormDao.isIdExists(number);
        if (num > 0) {
            message.put("success", "false");
            message.put("msg", "检查表编号已存在");
            return message;
        } else {
            message.put("success", "true");
            message.put("msg", "检查表编号未被使用");
            return message;
        }
    }

    /**
     * 更新表单模板中的html
     *
     * @param param
     * @return
     */
    public void updateTableTemp(Map<String, Object> param) {
        formFolderDao.updateTableTempContent(param);
    }

    public void updateTableTempContent(Map<String, Object> param) {
        formFolderDao.updateTableTempContent2(param);
    }

    /**
     * 模板里的内容
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> template_content(Long id) {
        Map<String, Object> message = new TreeMap<String, Object>();
        CheckForm checkform = checkFormDao.getById(id);
        message.put("checkform", checkform);
        return message;
    }

    public List<Map<String, Object>> allFormFolder(String projectId) {
        return formFolderDao.allFormFolder(projectId);
    }

    public List<Map<String, Object>> getFormsByFolderID(String folderIds) {
        return formFolderDao.getFormsByFolderID(folderIds);
    }

    /**
     * 根据模板ID获取模板信息
     *
     * @param param
     * @return
     */
    public Map<String, Object> getTableTempById(Map<String, Object> param) {
        return formFolderDao.getTableTempById(param);
    }

    public String getTableTempIdById(String Id) {
        return formFolderDao.getTableTempIdById(Id);
    }

    public List<Map<String, Object>> getFormsByProjectID(String projectId) {
        return formFolderDao.getFormsByProjectID(projectId);
    }

    public List<Map<String, Object>> getFormsByProjectID(String projectId, boolean flag) {
        return formFolderDao.getFormsByProjectID(projectId, flag);
    }

    /**
     * 插入表单实例
     *
     * @param param
     * @return
     */
    public Map<String, Object> insertTableIns(Map<String, Object> param) {
        return formFolderDao.insertTableIns(param);
    }

    /**
     * 根据表格实例ID获取表格实例
     *
     * @param param
     * @return
     */
    public Map<String, Object> getTableInstantById(Map<String, Object> param) {
        return formFolderDao.getTableInstantById(param);
    }

    /**
     * 根据表格实例ID获取签署结果
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getSignResultById(String tableinsId) {
        return formFolderDao.getSignResultById(tableinsId);
    }

    public List<Map<String, Object>> getSignResById(String tableinsId) {
        return formFolderDao.getSignResById(tableinsId);
    }

    /**
     * 根据表格实例ID获取检查条件结果
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCondiResById(String tableinsId) {
        return formFolderDao.getCondiResById(tableinsId);
    }

    /**
     * 根据表格实例ID获取检查项和检查结果示意图
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCheckItemAndMapById(String tableinsId) {
        return formFolderDao.getCheckItemAndMapById(tableinsId);
    }


    /**
     * 根据表单模板ID获取检查条件
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCheckConditionById(String tabletempId) {
        return formFolderDao.getCheckConditionById(tabletempId);
    }

    /**
     * 根据表单模板ID获取签署人
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getSignById(String tabletempId) {
        return formFolderDao.getSignById(tabletempId);
    }

    /**
     * 根据表单模板ID获取检查项
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCheckItemById(String tabletempId) {
        return formFolderDao.getCheckItemById(tabletempId);
    }

    /**
     * 根据表单实例ID更新表格实例content
     *
     * @param param
     * @return
     */
    public void updateTableIns(Map<String, Object> param) {
        formFolderDao.updateTableIns(param);
    }

    /**
     * 上传模板
     *
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public String uploadExcelTemp(MultipartHttpServletRequest request, HttpServletResponse response, ISysUser appUser, long userId, String pid, String fcid,String extype) throws Exception {
    	 String msg = "{\"success\":\"true\",\"context\":\"上传成功\"}";//返回执行情况
         // 附件保存路径
         String saveType = PropertyUtil.getSaveType();
         long dataId = RequestUtil.getLong(request, "dataId", 0); //业务表记录的id
         long tableId = RequestUtil.getLong(request, "tableId", 0); //业务表的id
         long fileId = RequestUtil.getLong(request, "fileId", 0); //上一版本的id
         long maxSize = RequestUtil.getLong(request, "maxSize", 2 * 1024 * 1024 * 1024);
         FileItemFactory factory = new DiskFileItemFactory();
			// 文件上传核心工具类
		 ServletFileUpload upload = new ServletFileUpload(factory);
         Map<String, MultipartFile> files = request.getFileMap();
         Iterator<MultipartFile> it = files.values().iterator();
         while (it.hasNext()) {
        	 MultipartFile f = it.next();
        	 msg=excelPoiReadService.readExcel(f.getInputStream(),fcid,userId,pid,extype);
         }
    	 return msg;
    }
    
    
    
    
    
    /**
     * 上传模板
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String uploadAttach(MultipartHttpServletRequest request, HttpServletResponse response, ISysUser appUser, long userId, String pid, String fcid) throws IOException, MyException {

        StandExlParseUtil standExlParse;
        // 附件保存路径
        String saveType = PropertyUtil.getSaveType();
        String msg = "上传成功";//返回执行情况

        long dataId = RequestUtil.getLong(request, "dataId", 0); //业务表记录的id
        long tableId = RequestUtil.getLong(request, "tableId", 0); //业务表的id
        long fileId = RequestUtil.getLong(request, "fileId", 0); //上一版本的id
        long maxSize = RequestUtil.getLong(request, "maxSize", 2 * 1024 * 1024 * 1024);
        String storePath = "";
        if (dataId == 0 && tableId == 0) {
            Calendar cal = Calendar.getInstance();
            storePath = "" + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator + cal.get(Calendar.DAY_OF_MONTH);
        } else {
            storePath = "" + tableId + File.separator + dataId; //当dataId为0时都是无记录的，挂在tableId下
        }

        Long fileTypeId = RequestUtil.getLong(request, "fileTypeId", -1);//folder的Id
        String fileFormates = RequestUtil.getString(request, "fileFormates");//格式要求
        boolean mark = true;
        boolean fileSize = true;//文件大小符合判断
        //获取上传的文件流
        Map<String, MultipartFile> files = request.getFileMap();
        Iterator<MultipartFile> it = files.values().iterator();

        while (it.hasNext()) {
            MultipartFile f = it.next();
            String oriFileName = f.getOriginalFilename();
            String extName = FileOperator.getSuffix(oriFileName);
            String fileIndex = RequestUtil.getString(request, "fileIndex");//文件位置

            standExlParse = StandExlParseUtil.getInstance(f);
            standExlParse.unMergeCell(standExlParse.getTableContent());
            standExlParse.parseTableInfo();
            standExlParse.parseTableContent();
            List<List<DefineCheckType>> allCheckIdentifyList = new ArrayList<List<DefineCheckType>>();
            List<List<Tcell>> allCellList = standExlParse.getCellFromContent();
            int line = 2;
            String unRelatiionedline = "";
            for (List<Tcell> cellList : allCellList) {
                List<DefineCheckType> checkIdentifyList = new ArrayList<DefineCheckType>();
                int rowNum = -1;
                Tcell relation = null;
                if (-1 != standExlParse.getRelationIndex()) {
                    relation = cellList.get(standExlParse.getRelationIndex());

                }
                for (int index = 0; index < cellList.size(); index++) {
                    Tcell tcell = cellList.get(index);
                    //设置好cell的内容
                    if ("TRUE".equals(tcell.getIsResultItem())) {
                        DefineCheckType checkIdentify = standExlParse.getCheckIdentifyByTCell(tcell);
                        if (rowNum == tcell.getRowInExl()) {
                            checkIdentify.setIILdd("否");
                            checkIdentify.setILdd("否");
                            checkIdentify.setZhycdz("否");
                            checkIdentify.setNjljyq("否");
//							checkIdentify.setYcx("FALSE");
                            checkIdentify.setYcn("否");
                            checkIdentify.setIfmedia("否");
                        } else {
                            rowNum = tcell.getRowInExl();
                        }
                        checkIdentifyList.add(checkIdentify);
                    }
                }
                line++;
                allCheckIdentifyList.add(checkIdentifyList);
            }
            //检查定义表Bean
            Map<String, String> checkDataMap = standExlParse.getCheckDataMap();
            List<Map<String, String>> signDataMapList = standExlParse.getSignDataMapList();
            List<Map<String, String>> conditionMapList = standExlParse.getConditionMapList();
            String html = standExlParse.parseTableHtml();
            String context = standExlParse.getPraseInfo();
            System.out.println(context);

            CheckForm checkform = new CheckForm();
            Long ID = UniqueIdUtil.genId();
            checkform.setCheckformId(ID);
            checkform.setName(checkDataMap.get("NAME"));
            checkform.setSnum(checkDataMap.get("NUMBER"));
            checkform.setContents(html);
            checkform.setRemark(checkDataMap.get("M_CZSM"));
            checkform.setPid(Long.parseLong(pid));
            checkform.setFid(Long.parseLong(fcid));
            checkform.setStatus("已完成");
            checkFormDao.add(checkform);

            if (signDataMapList.size() > 0) {
                DefineSign definesign = new DefineSign();
                for (int i = 0; i < signDataMapList.size(); i++) {
                    Map<String, String> signDataMap = signDataMapList.get(i);
                    Long pk = UniqueIdUtil.genId();
                    definesign.setDefinesignId(pk);
                    definesign.setName(signDataMap.get("NAME"));
                    definesign.setSequence(Integer.parseInt(signDataMap.get("ORDER")));
                    definesign.setModule(ID);
                    defineSignDao.add(definesign);
                }
            }

            if (conditionMapList.size() > 0) {
                CheckCondition checkcondition = new CheckCondition();
                for (int i = 0; i < conditionMapList.size(); i++) {
                    Map<String, String> conditionMap = conditionMapList.get(i);
                    Long pk = UniqueIdUtil.genId();
                    checkcondition.setCheckconditionId(pk);
                    checkcondition.setName(conditionMap.get("NAME"));
                    checkcondition.setSequence(Integer.parseInt(conditionMap.get("ORDER")));
                    checkcondition.setModule(ID);
                    checkConditionDao.add(checkcondition);
                }
            }

            for (int i = 0; i < allCheckIdentifyList.size(); i++) {
                List<DefineCheckType> checkIdentify = allCheckIdentifyList.get(i);
                for (int j = 0; j < checkIdentify.size(); j++) {
                    DefineCheckType dcty = checkIdentify.get(j);
                    Long pk = UniqueIdUtil.genId();
                    dcty.setID(pk);
                    dcty.setModuleID(ID);
                    definechecktypeDao.add(dcty);
                }
            }
            msg = "{\"success\":\"true\",\"fileIndex\":\"" + fileIndex + "\",\"fileName\":\"" + oriFileName + "\"}";
        }

        return msg;
    }

    /**
     * 上传模板  word
     *
     * @param request
     * @param response
     * @param appUser
     * @param userId
     * @param parentId  所属文件夹 ID
     * @param projectId 所属发次ID
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String uploadAttachWord(MultipartHttpServletRequest request, HttpServletResponse response, ISysUser appUser, long userId, String parentId, String projectId) throws Exception {

        StandExlParseUtil standExlParse;
        // 附件保存路径
        String saveType = PropertyUtil.getSaveType();
        String msg = "上传成功";//返回执行情况

        long dataId = RequestUtil.getLong(request, "dataId", 0); //业务表记录的id
        long tableId = RequestUtil.getLong(request, "tableId", 0); //业务表的id
        long fileId = RequestUtil.getLong(request, "fileId", 0); //上一版本的id
        long maxSize = RequestUtil.getLong(request, "maxSize", 2 * 1024 * 1024 * 1024);
        String storePath = "";
        if (dataId == 0 && tableId == 0) {
            Calendar cal = Calendar.getInstance();
            storePath = "" + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator + cal.get(Calendar.DAY_OF_MONTH);
        } else {
            storePath = "" + tableId + File.separator + dataId; //当dataId为0时都是无记录的，挂在tableId下
        }

        Long fileTypeId = RequestUtil.getLong(request, "fileTypeId", -1);//folder的Id
        String fileFormates = RequestUtil.getString(request, "fileFormates");//格式要求
        boolean mark = true;
        boolean fileSize = true;//文件大小符合判断
        //获取上传的文件流
        Map<String, MultipartFile> files = request.getFileMap();
        Iterator<MultipartFile> it = files.values().iterator();

        while (it.hasNext()) {
            MultipartFile file = it.next();
            String oriFileName = file.getOriginalFilename();
            String extName = FileOperator.getSuffix(oriFileName);
            String fileIndex = RequestUtil.getString(request, "fileIndex");//文件位置

            //解析word2003
            String dealMsg = this.dealWord2003(file, parentId, projectId);

            msg = "{\"success\":\"true\",\"fileIndex\":\"" + fileIndex + "\",\"fileName\":\"" + oriFileName + "\"}";
        }
        return msg;
    }

    /**
     * 解析word2003
     *
     * @param files
     * @param parentId
     * @param projectId @return
     */
    private String dealWord2003(MultipartFile file, String parentId, String projectId) throws IOException {
        String msg = "";

        InputStream is = file.getInputStream();
        HWPFDocument doc = new HWPFDocument(is);
        Range range = doc.getRange();
        //遍历range范围内的table。
        TableIterator tableIter = new TableIterator(range);
        Table table;
        TableRow row;
        TableCell cell;
        List<String> list = new ArrayList<String>();
        int flag = 0;
        while (tableIter.hasNext()) {
            flag++;
            int Number = 0;
            String oldHtml = "";
            Element root = DocumentHelper.createElement("html");
            Document document = DocumentHelper.createDocument(root);
            Element tablelElement = root.addElement("table").addAttribute("width", "100%").addAttribute("class", "layui-table");
            ;
            Element tbodyElement = tablelElement.addElement("tbody");
            table = tableIter.next();
            int rowNum = table.numRows();

            String content = table.getRow(0).getCell(0).text().trim(); //获得表格第一行第一列的内容（用于遍历所有段落时，若一个段落内容与content匹配，那么它的上一个段落就是表名）
            int n = 0;
            int N = 0;
            if (list.size() > 0) {  //判断content前面是否已经出现过，出现过几次
                for (int i = 0; i < list.size(); i++) {
                    String Content = list.get(i);
                    if (Content.equals(content)) {
                        n++;
                    }
                }
            }
            int paraNum = range.numParagraphs();
            int titleNum = 0;
            for (int i = 0; i < paraNum; i++) { //遍历word中的所有段落，与content比较，若相等则上一个段落为title，用作导入模版名
                String para = "";
                para = range.getParagraph(i).text();
                if (!"".equals(para)) {
                    para = para.substring(0, para.length() - 1);
                    if (para.equals(content)) {
                        if (N == n) {
                            titleNum = i - 1;
                            break;
                        }
                        N++;
                    }
                }
            }
            String title = "";
            if (titleNum >= 0) {
                title = range.getParagraph(titleNum).text();
                title = title.substring(0, title.length() - 1);
                list.add(content);
            }

            for (int j = 0; j < rowNum; j++) {
                if (j == 0) {
                    //首行设置
                    Element trElement = tbodyElement.addElement("tr").addAttribute("class", "firstRow");
                    row = table.getRow(j);
                    int cellNum = row.numCells();
                    for (int k = 0; k < cellNum; k++) {
                        cell = row.getCell(k);
                        String cellText = cell.text().trim();
                        if (cellText.equalsIgnoreCase("")) {
                            //输出单元格的文本
                            msg = "表格的首行每一列首行均不能为空！";
                            break;
                        } else {
                            trElement.addElement("td").addAttribute("valign", "top").setText(cellText);
                        }
                    }
                } else {
                    Element trElement = tbodyElement.addElement("tr");
                    row = table.getRow(j);
                    int cellNum = row.numCells();
                    //检查项描述（本行）
                    String checkItemDesc = ",";
                    for (int k = 0; k < cellNum; k++) {
                        cell = row.getCell(k);
                        String cellText = cell.text().trim();
                        String Num = String.valueOf(Number + 1);
                        String number = "= " + Num + " \\*"; //用来判断字符串是否是编号
                        if (cellText.contains(number)) {  //判断解析出来的内容中是否包含上面的字符串，从而确定是否是编号，如果是则将cellText替换成数字
                            Number++;
                            String num = String.valueOf(Number);
                            cellText = num;
                        }
                        if (cellText.equalsIgnoreCase("")) {
                            String nowCheckItemDESC = "";
                            if (checkItemDesc.length() > 1) {
                                nowCheckItemDESC = checkItemDesc.substring(1, checkItemDesc.length());
                            }
                            Element tdElement = trElement.addElement("td").addAttribute("valign", "top").addAttribute("class", "selectTdClass").addAttribute("input", "1").addAttribute("checkbox", "1").addAttribute("photo", "1");
                            tdElement.setText(cellText);
                            //填写
                            tdElement.addElement("input").addAttribute("type", "text").addAttribute("class", "dpInputText").addAttribute("photo", "是").addAttribute("title", nowCheckItemDESC);
                            //对勾
                            tdElement.addElement("input").addAttribute("type", "checkbox").addAttribute("disabled", "true").addAttribute("class", "dpCheckbox").addAttribute("photo", "是").addAttribute("title", nowCheckItemDESC);
                            //附件，拍照
                            tdElement.addElement("input").addAttribute("type", "button").addAttribute("class", "dpInputBtn").addAttribute("disabled", "true").addAttribute("value", "附件").addAttribute("onclick", "addAndShowPhoto(this)");
                        } else {
                            checkItemDesc += cellText;
                            trElement.addElement("td").addAttribute("valign", "top").setText(cellText);
                        }
                    }
                }
            }
            oldHtml = document.asXML();
            int index1 = oldHtml.indexOf("<table");
            int index2 = oldHtml.lastIndexOf("</table>");
            oldHtml = oldHtml.substring(index1, index2 + 8);
            //System.out.println(oldHtml);
            // Map<String,
            // Object>msg=formService.check(html,id,name,sign,status,attention,pid,fcid,MID);
            //增夹检查表模板-----start--------------------
            Long checkFormIdLong = UniqueIdUtil.genId();
            String checkFormId = CommonTools.Obj2String(checkFormIdLong);
            CheckForm checkform = new CheckForm();
            checkform.setCheckformId(checkFormIdLong);
            if (!"".equals(title)) {
                checkform.setName(title);
            } else {
                checkform.setName(checkFormId);
            }

            checkform.setSnum(checkFormId);
            checkform.setRemark("表" + flag + ":通过Word（" + file.getOriginalFilename() + "）导入的表格！");
            checkform.setPid(Long.parseLong(projectId));
            checkform.setFid(Long.parseLong(parentId));
            checkform.setType("1");
            checkform.setStatus("已完成");
            checkFormDao.add(checkform);
            //增夹检查表模板-----end--------------------
            //校验html
            Map<String, Object> validateMsg = formValidateService.validate(oldHtml, "1", checkFormId);
            if (CommonTools.Obj2String(validateMsg.get("success")).equalsIgnoreCase("true")) {
                System.out.println("表" + flag + "校验通过！");
                //更新html
                Map<String, Object> updateHtmlMsg = this.save(oldHtml, checkFormId, "1");
                if (CommonTools.Obj2String(updateHtmlMsg.get("success")).equalsIgnoreCase("true")) {
                    System.out.println("表" + flag + "html更新通过！");
                } else {
                    msg += "表" + (flag + 1) + CommonTools.Obj2String(updateHtmlMsg.get("msg"));
                }
            } else {
                msg += "表" + (flag + 1) + "校验失败！···";
            }
        }
        if (msg.equalsIgnoreCase("")) {
            msg = "导入成功！";
        }
        return msg;
    }

    /**
     * 生成检查条件结果
     *
     * @param checkconditions
     * @param insId
     */
    public void createckcondition(List<Map<String, Object>> checkconditions, Long insId) {
        for (int i = 0; i < checkconditions.size(); i++) {
            Map<String, Object> checkcondition = checkconditions.get(i);
            Long ID = UniqueIdUtil.genId();
            CkConditionResult conditionresult = new CkConditionResult();
            conditionresult.setID(ID);
            conditionresult.setConditionID(Long.parseLong(checkcondition.get("ID").toString()));
            conditionresult.setInstantID(insId);
            ckconditionresultDao.add(conditionresult);
        }
    }

    /**
     * 生成签署结果
     *
     * @param signs
     * @param insId
     */
    public void createsignresult(List<Map<String, Object>> signs, Long insId) {
        for (int i = 0; i < signs.size(); i++) {
            Map<String, Object> sign = signs.get(i);
            Long ID = UniqueIdUtil.genId();
            SignResult signresult = new SignResult();
            signresult.setID(ID);
            signresult.setSignID(Long.parseLong(sign.get("ID").toString()));
            signresult.setInstantID(insId);
            signresultDao.add(signresult);
        }
    }

    /**
     * 生成检查结果、且替换掉表格实例内的content中input标签的ID
     *
     * @param checkitems
     * @param insId
     */
    public void createcheckresult(List<Map<String, Object>> checkitems, Long insId, String content) {
        for (int i = 0; i < checkitems.size(); i++) {
            Map<String, Object> checkitem = checkitems.get(i);
            Long ID = UniqueIdUtil.genId();
            //根据表单实例ID区分检查结果表的类型
            // 检查结果(W_CK_RESULT 空间检查结果/W_CK_RESULT_CARRY 运载检查结果 /W_CK_RESULT_JGJG 结构机构检查结果 )
            Map typeMap = productTypeDao.getProductType(insId);
            if (typeMap!=null) {
            	if ("空间".equals(typeMap.get("TYPE"))) {
                    CheckResult checkresult = new CheckResult();
                    checkresult.setID(ID);
                    checkresult.setItemID(Long.parseLong(checkitem.get("ID").toString()));
                    checkresult.setInstantID(insId);
                    checkresultDao.add(checkresult);
                    content = content.replaceAll(checkitem.get("ID").toString(), ID.toString());
                } else if ("运载".equals(typeMap.get("TYPE"))) {
                    CheckResultCarry checkresultCarry = new CheckResultCarry();
                    checkresultCarry.setID(ID);
                    checkresultCarry.setItemID(Long.parseLong(checkitem.get("ID").toString()));
                    checkresultCarry.setInstantID(insId);
                    checkresultCarryDao.add(checkresultCarry);
                    content = content.replaceAll(checkitem.get("ID").toString(), ID.toString());
                } else {
                    CheckResultJgjg checkresultJgjg = new CheckResultJgjg();
                    checkresultJgjg.setID(ID);
                    checkresultJgjg.setItemID(Long.parseLong(checkitem.get("ID").toString()));
                    checkresultJgjg.setInstantID(insId);
                    checkresultJgjgDao.add(checkresultJgjg);
                    content = content.replaceAll(checkitem.get("ID").toString(), ID.toString());
                }
			}
        }
        System.out.println(content);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ID", insId);
        param.put("F_CONTENT", content);
        updateTableIns(param);
    }

    /**
     * 保存表单录入信息
     *
     * @param list
     * @param slid
     * @return
     */
    public Map<String, Object> saveform(Map<String, List<Map<String, String>>> list, String html, String slid) {
        Map<String, Object> message = new HashMap<String, Object>();
        //检查条件
        List<Map<String, String>> conditionslist = list.get("condition");
        //检查结果(W_CK_RESULT 空间检查结果/W_CK_RESULT_CARRY 运载检查结果 /W_CK_RESULT_JGJG 结构机构 )
        List<Map<String, String>> contentslist = list.get("content");
        //签署定义
        List<Map<String, String>> signslist = list.get("sign");
        try {
            for (int i = 0; i < conditionslist.size(); i++) {
                Map<String, String> condition = conditionslist.get(i);
                CkConditionResult conditionresult = new CkConditionResult();
                conditionresult.setValue(condition.get("value"));
                /*conditionresult.setConditionID(Long.parseLong(condition.get("id").toString()));
                conditionresult.setInstantID(Long.parseLong(slid));*/
                conditionresult.setID(Long.parseLong(condition.get("id").toString()));

                ckconditionresultDao.update(conditionresult);
            }

            for (int i = 0; i < signslist.size(); i++) {
                Date date = new Date();
                Map<String, String> sign = signslist.get(i);
                SignResult signresult = new SignResult();
                signresult.setSignUser(sign.get("value"));
                // signresult.setSignID(Long.parseLong(sign.get("id").toString()));
                signresult.setSignTime(date);
                //signresult.setInstantID(Long.parseLong(slid));
                signresult.setID(Long.parseLong(sign.get("id").toString()));
                signresultDao.update(signresult);
            }
            //根据表单实例ID区分检查结果表的类型

            Map typeMap = productTypeDao.getProductType(Long.valueOf(slid));

            if(typeMap!=null) {
            	for (int i = 0; i < contentslist.size(); i++) {
                    Map<String, String> content = contentslist.get(i);
                    // 检查结果(W_CK_RESULT 空间检查结果/W_CK_RESULT_CARRY 运载检查结果 /W_CK_RESULT_JGJG 结构机构检查结果 )
                    if ("空间".equals(typeMap.get("TYPE"))) {
                        CheckResult checkresult = new CheckResult();
                        checkresult.setValue(content.get("value"));
                        checkresult.setID(Long.parseLong(content.get("id").toString()));
                        checkresultDao.update(checkresult);
                    } else if ("运载".equals(typeMap.get("TYPE"))) {
                        CheckResultCarry checkresultCarry = new CheckResultCarry();
                        checkresultCarry.setValue(content.get("value"));
                        checkresultCarry.setID(Long.parseLong(content.get("id").toString()));
                        checkresultCarryDao.update(checkresultCarry);
                    } else {
                        CheckResultJgjg checkresultJgjg = new CheckResultJgjg();
                        checkresultJgjg.setValue(content.get("value"));
                        checkresultJgjg.setID(Long.parseLong(content.get("id").toString()));
                        checkresultJgjgDao.update(checkresultJgjg);
                    }

                }
            }
            
            html = "<html>" + html + "</html>";
            Map<String, Object> newhtml = FormUtils.updateHtml(html, contentslist);
            if (newhtml.get("html").equals("")) {
                message.put("success", "false");
                message.put("msg", "保存表单失败");
                return message;
            } else {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("ID", slid);
                param.put("F_CONTENT", newhtml.get("html"));
                updateTableIns(param);
            }
            message.put("success", "true");
            message.put("msg", "保存表单成功");
        } catch (Exception e) {
            e.printStackTrace();
            message.put("success", "false");
            message.put("msg", "表单录入失败");
        }
        return message;
    }

    /**
     * 判断是否存在指定ID的图片
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> ifExistPic(String id) {
        Map<String, Object> message = new TreeMap<String, Object>();
        Map<String, Object> num = formFolderDao.isPicExists(id);
        if (Integer.parseInt(num.get("count").toString()) > 0) {
            message.put("success", "true");
            return message;
        } else {
            message.put("success", "false");
            message.put("msg", "当前检查项未上传图片无法预览");
            return message;
        }
    }



    public String importExcel(MultipartFile file, Long slid, String html) throws Exception {
        StringBuffer log = new StringBuffer();
        List<Map<String, String>> contentslist = new ArrayList<Map<String, String>>();

        String floder = SysConfConstant.UploadFileFolder + File.separator + "temp" + File.separator +
                IOConstans.IMPORT_HISTORY_DATA + DateUtil.getCurrentDate("yyyyMMddHHmmss");

        saveFile(file.getInputStream(), file.getOriginalFilename(), floder);
        File targetFile = new File(floder + File.separator + file.getOriginalFilename());
        //根据表单实例ID区分检查结果表的类型
        // 检查结果(W_CK_RESULT 空间检查结果/W_CK_RESULT_CARRY 运载检查结果 /W_CK_RESULT_JGJG 结构机构检查结果 )
        Map typeMap = productTypeDao.getProductType(slid);

        HSSFWorkbook wb = (HSSFWorkbook) WorkbookFactory.create(new FileInputStream(targetFile));
        HSSFSheet sheet = wb.getSheetAt(0);
        int rows = sheet.getLastRowNum();
        for (int i = 0; i <= rows; i++) {
            int cells = 0;
            HSSFRow row = sheet.getRow(i);
            if (row != null) {
                //单元格数据量
                cells = row.getPhysicalNumberOfCells();
                for (int j = 0; j < cells; j++) {
                    HSSFCell cell = row.getCell(j);
                    if (cell.getCellComment() != null) {
                        String checkItemid = cell.getCellComment().getString().getString();

                        if (!checkItemid.equals("") && checkItemid.matches("^[0-9]+$")) {

                            //更新html
                            String id = "";

                            Map<String, String> map = new HashMap<String, String>();

                            String value = "";
                            switch (cell.getCellType()) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    value = cell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    value = String.valueOf(cell.getNumericCellValue());
                                    break;
                            }
                            if ("空间".equals(typeMap.get("TYPE"))) {
                                CheckResult checkresult = new CheckResult();
                                checkresult.setValue(value);
                                checkresult.setItemID(Long.parseLong(checkItemid));
                                checkresult.setInstantID(slid);
                                checkresultDao.update2(checkresult);
                                id = checkresultDao.getId(checkItemid, slid).get("ID").toString();
                            } else if ("运载".equals(typeMap.get("TYPE"))) {
                                CheckResultCarry checkresultCarry = new CheckResultCarry();
                                checkresultCarry.setValue(value);
                                checkresultCarry.setItemID(Long.parseLong(checkItemid));
                                checkresultCarry.setInstantID(slid);
                                checkresultCarryDao.update2(checkresultCarry);
                                id = checkresultCarryDao.getId(checkItemid, slid).get("ID").toString();
                            } else {
                                CheckResultJgjg checkresultJgjg = new CheckResultJgjg();
                                checkresultJgjg.setValue(value);
                                checkresultJgjg.setItemID(Long.parseLong(checkItemid));
                                checkresultJgjg.setInstantID(slid);
                                checkresultJgjgDao.update2(checkresultJgjg);
                                id = checkresultJgjgDao.getId(checkItemid, slid).get("ID").toString();

                            }

                            map.put("id", id);
                            map.put("value", value);
                            contentslist.add(map);
                        }
                    }
                }
            }
        }

        FileOperator.delFoldsWithChilds(floder);

        html = "<html>" + html + "</html>";
        Map<String, Object> newhtml = FormUtils.updateHtml(html, contentslist);
        if (newhtml.get("html").equals("")) {
            log.append("导入失败");
        } else {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("ID", slid);
            param.put("F_CONTENT", newhtml.get("html"));
            updateTableIns(param);
            log.append("导入成功");
        }

        return log.toString();
    }

    public void saveFile(InputStream inputStream, String fileName, String filepath) {
        FileOutputStream os = null;
        //生成Excel文件
        try {
            byte[] bs = new byte[1024];
            int len = 0;
            File tempFile = new File(filepath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据ID获取图片文件所在的路径
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getPicPathByID(String Id) {
        return formFolderDao.getPicPathByID(Id);
    }

    /**
     * 根据ID获取检查项示意图文件所在的路径
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getDiaPathByID(String Id) {
        return formFolderDao.getDiaPathByID(Id);
    }

    /**
     * 根据表格实例ID和检查项ID获取检查结果
     *
     * @param param
     * @return
     */
    public Map<String, Object> getCkResultById(Map<String, Object> param) {
        return formFolderDao.getCkResultById(param);
    }

    /**
     * 根据检查结果ID插入示意图ID
     *
     * @param param
     * @return
     */
    public void updateckresDiagram(Map<String, Object> param) {
        formFolderDao.updateckresDiagram(param);
    }

    /**
     * 根据表格实例ID和示意图ID删除示意图ID
     *
     * @param param
     * @return
     */
    public void delckresDiagram(Map<String, Object> param) {
        formFolderDao.delckresDiagram(param);
    }
    
  

    /**
     * 导出Excel，并且将附件一起导出
     *
     * @param html
     * @param request
     * @param response
     * @param instantId 检查表实例ID
     * @param fileName
     */
    public void exportExcel(String html, HttpServletRequest request, HttpServletResponse response,
                            String instantId, String fileName,String security) {
        //生成Excel
        HSSFWorkbook wb = createExcel(html,security);
        //生成附件
        downloadopphotofinished(request, response, instantId, wb, fileName, security);
    }
    //导出表单模板
    public void exportModel(String html, HttpServletRequest request, HttpServletResponse response, String fileName) {
        HSSFWorkbook wb = createSignModelExcel(html);
        String attachPath = AppUtil.getAttachPath();
        String tempPath = attachPath + File.separator + System.currentTimeMillis() + File.separator + fileName;
        ;
        //生成Excel文件
        try {
            FileOperator.createFolder(tempPath);
            FileOutputStream os = new FileOutputStream(new File(tempPath + File.separator + fileName + ".xls"));
            try {
                wb.write(os);
                os.flush();
                os.close();
            } catch (IOException e) {

                e.printStackTrace();
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        File file = new File(tempPath + File.separator + fileName + ".xls");
        downloadFileByPath(wb, file.getName(), file, response, request);
        FileOperator.delFoldsWithChilds(tempPath);
    }
    //导出表单模板
    public void exportModel(String html, HttpServletRequest request, HttpServletResponse response, String fileName,TableTemp tableTemp) {
        HSSFWorkbook wb = createSignModelExcel(html,tableTemp);
        String attachPath = AppUtil.getAttachPath();
        String tempPath = attachPath + File.separator + System.currentTimeMillis() + File.separator + fileName;
        ;
        //生成Excel文件
        try {
            FileOperator.createFolder(tempPath);
            FileOutputStream os = new FileOutputStream(new File(tempPath + File.separator + fileName + ".xls"));
            try {
                wb.write(os);
                os.flush();
                os.close();
            } catch (IOException e) {

                e.printStackTrace();
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        File file = new File(tempPath + File.separator + fileName + ".xls");
        downloadFileByPath(wb, file.getName(), file, response, request);
        FileOperator.delFoldsWithChilds(tempPath);
    }

    /**
     * 根据表格实例ID获取所有的检查项结果
     *
     * @param Id
     * @return
     */
    public List<Map<String, Object>> getCheckresByID(String Id) {
        return formFolderDao.getCheckresByID(Id);
    }

    /**
     * 根据检查项结果ID获取图片文件
     *
     * @param Id
     * @return
     */
    public List<Map<String, Object>> getFileByID(String Id) {
        return formFolderDao.getFileByID(Id);
    }

    /**
     * 下载附件和Excel
     *
     * @param request
     * @param response
     * @param instantId
     * @param workBook
     * @param fileName
     */
    public void downloadopphotofinished(HttpServletRequest request, HttpServletResponse response,
                                        String instantId, HSSFWorkbook workBook, String fileName,String security) {
        // 附件保存路径
        String attachPath = AppUtil.getAttachPath();
        String tempPath = attachPath + File.separator + "temp" + File.separator + System.currentTimeMillis();
        FileOperator.createFolders(tempPath);
        //获取检查结果的list
        List<Map<String, Object>> resultList = getCheckresByID(instantId);
        String targetPath = tempPath + File.separator + fileName;
        //生成附件
        for (Map<String, Object> result : resultList) {
            String resId = CommonTools.Obj2String(result.get("ID").toString());
            Map typeMap = productTypeDao.getProductType(Long.valueOf(instantId));
            String ck_resultName = "";
            ck_resultName = "W_CK_RESULT_CARRY";
//            if ("空间".equals(typeMap.get("TYPE"))) {
//                ck_resultName = "W_CK_RESULT";
//            } else if ("运载".equals(typeMap.get("TYPE"))) {
//            } else {
//                ck_resultName = "W_CK_RESULT_JGJG";
//            }
            String filterTableId = resId + "&&" + ck_resultName;
            List<Map<String, Object>> sysFileList = formUtilDao.getListByTableNameAndFilter("CWM_SYS_FILE", Arrays.asList(new SyncBaseFilter("TABLEID", "=", "'" + filterTableId + "'")));
//            System.out.println(sysFileList.size());
            if (sysFileList.size() > 0) {
                //根据检查结果的ID，创建附件文件夹,附件的文件夹名字尽量用检查名称、简称和描述来代替resId----start
                String targetPathRes = targetPath + File.separator + resId;
                List<Map<String, Object>> ckResultList = formUtilDao.getListByTableNameAndFilter(ck_resultName, Arrays.asList(new SyncBaseFilter("ID", "=", "'" + resId + "'")));
                if (ckResultList.size() > 0) {
                    List<Map<String, Object>> itemDefList = formUtilDao.getListByTableNameAndFilter("W_ITEMDEF", Arrays.asList(new SyncBaseFilter("ID", "=", "'" + CommonTools.Obj2String(ckResultList.get(0).get("F_ITEMDEF_ID")) + "'")));
                    if (itemDefList.size() > 0) {
                        String itemName = CommonTools.Obj2String(itemDefList.get(0).get("F_SHORTNAME"));
                        if (itemName.equalsIgnoreCase("")) {
                            itemName = CommonTools.Obj2String(itemDefList.get(0).get("F_NAME"));
                        }
                        if (itemName.equalsIgnoreCase("")) {
                            itemName = CommonTools.Obj2String(itemDefList.get(0).get("F_DESCRIPTION"));
                            if (itemName.length() > 30) {
                                itemName = itemName.substring(0, 10) + "。。。。。。" + itemName.substring(itemName.length() - 10, itemName.length());
                            }
                        }
                        if (!itemName.equalsIgnoreCase("")) {
                            targetPathRes = targetPath + File.separator + itemName;
                        }
                    }
                }
                //根据检查结果的ID，创建附件文件夹,附件的文件夹名字尽量用检查名称、简称和描述来代替resId----end
                FileOperator.createFolders(targetPathRes);
                for (Map<String, Object> map : sysFileList) {
                    String resFile = attachPath + File.separator + CommonTools.Obj2String(map.get("FILEPATH"));
                    FileOperator.copyFile(resFile, targetPathRes + File.separator + CommonTools.Obj2String(map.get("FILENAME")) +"("+security+")"+
                            "." + CommonTools.Obj2String(map.get("EXT")));
                }
            }
        }
        //生成Excel文件
        try {
            File targetFile = new File(targetPath);
            //无拍照图片
            if (!targetFile.exists()) {
                FileOperator.createFolders(targetPath);
            }
            FileOutputStream os = new FileOutputStream(new File(targetPath + File.separator + fileName + ".xls"));
            try {
                workBook.write(os);
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        String resFile = tempPath + File.separator + fileName + "(" + security +")"+ ".zip";
        try {
            File insFile = new File(tempPath + File.separator + fileName + File.separator);
            if (insFile.exists()) {
                File[] ckFiles = insFile.listFiles();
                String[] filePath = new String[ckFiles.length];
                for (int i = 0; i < ckFiles.length; i++) {
                    filePath[i] = ckFiles[i].getPath();
                }
                ZipCompressor zc = new ZipCompressor(resFile);
                zc.compress(filePath);
                File file = new File(resFile);
                this.downloadFileByPath(workBook, file.getName(), file, response, request);
                FileOperator.delFoldsWithChilds(tempPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载单个文件
     *
     * @param fileName
     * @param downloadFile
     * @param response
     */
    private void downloadFileByPath(HSSFWorkbook workBook, String fileName, File downloadFile, HttpServletResponse response, HttpServletRequest request) {
        // 文件存在时才去下载
        if (downloadFile.exists()) {
            try {
                String userAgent = request.getHeader("USER-AGENT");
                String finalFileName = fileName;
                response.setContentType("application/vnd.ms-excel");
                //判断浏览器类型
                if (userAgent.contains("MSIE")) {//IE浏览器
                    finalFileName = URLEncoder.encode(fileName, "UTF-8");
                } else if (userAgent.contains("Mozilla")) {//google，火狐浏览器
                    finalFileName = new String(fileName.getBytes(), "ISO8859-1");
                    //finalFileName = new String(fileName.getBytes(),"UTF-8");
                } else {
                    finalFileName = URLEncoder.encode(fileName, "UTF-8");//其他
                }
                //将文件流存储在response中返回前台浏览器
                response.setHeader("Content-Disposition", "attachment;filename=" + finalFileName);

                BufferedInputStream in = new BufferedInputStream(new FileInputStream(downloadFile));
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                byte[] buffer = new byte[8192];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.flush();

                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * 移动模板
     *
     * @param param
     * @return
     */
    public Map<String, Object> moveModelById(Map<String, Object> param) {
        Map<String, Object> message = formFolderDao.moveModel(param);
        return message;
    }

    /**
     * 根据发次ID获取发次信息
     *
     * @param param
     * @return
     */
    public Map<String, Object> getProjectById(Map<String, Object> param) {
        return formFolderDao.getProjectById(param);
    }

    /**
     * 根据型号ID获取发次信息
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getAllProjectByID(String Id) {
        return formFolderDao.getAllProjectByID(Id);
    }

    /**
     * 根据表单模板id获取模板信息
     *
     * @param Id
     * @return
     */
    public Map<String, Object> getTabletempById(String Id) {
        return formFolderDao.getTabletempById(Id);
    }

    /**
     * 复制模板
     *
     * @param id 文件夹id
     * @param Mids 模板id
     * @param pid 发次id
     */
    public Map<String, Object> copyModel(Long id, String Mids[], Long pid) {
        Map<String, Object> message = new HashMap<String, Object>();
        for (int j = 0; j < Mids.length; j++) {
            Long mid = Long.parseLong(Mids[j]);

            try {
                CheckForm checkform = checkFormDao.getById(mid);
                Long mpk = UniqueIdUtil.genId();
                String name = checkform.getName();
                name += "-副本";
                String Name = name;
                int n = 0;
                List<Map<String, Object>> templates = checkFormDao.selectTemplates(pid, id, name);
                while (templates.size() > 0) {
                    n++;
                    String N = String.valueOf(n);
                    name = Name + N;
                    templates = checkFormDao.selectTemplates(pid, id, name);
                }
                checkform.setName(name);
                checkform.setCheckformId(mpk);
                checkform.setPid(pid);
                checkform.setFid(id);
                checkFormDao.add(checkform);

                //复制后的模板信息-替换content检查项id
                Map<String, Object> tabletemp = getTabletempById(mpk.toString());
                String content = tabletemp.get("F_CONTENTS").toString();

                List<CheckCondition> conditions = checkConditionDao.getByModelId(mid);
                for (int i = 0; i < conditions.size(); i++) {
                    CheckCondition condition = conditions.get(i);
                    Long conpk = UniqueIdUtil.genId();
                    condition.setCheckconditionId(conpk);
                    condition.setModule(mpk);
                    checkConditionDao.add(condition);
                }

                List<DefineSign> signs = defineSignDao.getByModelId(mid);
                for (int i = 0; i < signs.size(); i++) {
                    DefineSign sign = signs.get(i);
                    Long sipk = UniqueIdUtil.genId();
                    sign.setDefinesignId(sipk);
                    sign.setModule(mpk);
                    defineSignDao.add(sign);
                }

                List<DefineCheckType> checkitems = definechecktypeDao.getByModelId(mid);
                for (int i = 0; i < checkitems.size(); i++) {
                    DefineCheckType checkitem = checkitems.get(i);
                    Long ckpk = UniqueIdUtil.genId();
                    checkitem.setID(ckpk);
                    checkitem.setModuleID(mpk);
                    definechecktypeDao.add(checkitem);
                }
                List<Map<String, Object>> newCheckitems = getCheckItemById(mpk.toString());
                List<Map<String, Object>> oldCheckitems = getCheckItemById(mid.toString());
                for (int i = 0; i < newCheckitems.size(); i++) {
                    for (int k = 0; k < oldCheckitems.size(); k++) {
                        if (oldCheckitems.get(k).get("F_TYPE").toString()
                                .equals(newCheckitems.get(i).get("F_TYPE").toString())
                                && oldCheckitems.get(k).get("F_DESCRIPTION").toString()
                                .equals(newCheckitems.get(i).get("F_DESCRIPTION").toString())) {
                            content = content.replaceAll(oldCheckitems.get(k).get("ID").toString()
                                    , newCheckitems.get(i).get("ID").toString());
                            break;
                        }
                    }
                }

                Map<String, Object> param = new HashMap<String, Object>();
                param.put("ID", mpk);
                param.put("F_CONTENT", content);
                updateTableTempContent(param);
            } catch (Exception e) {
                e.printStackTrace();
                message.put("success", "false");
                message.put("msg", "模板复制失败,被复制的模板中存在检查内容为空的情况");
                return message;
            }
        }
        message.put("success", "true");
        message.put("msg", "模板复制成功");
        return message;
    }

    private String htmlToLower(String html) {
        char[] ch = html.toCharArray();
        StringBuilder str = new StringBuilder(ch.length);
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLowerCase(ch[i])) {
                str.append(Character.toLowerCase(ch[i]));
            } else {
                str.append(ch[i]);
            }

        }
        return str.toString();
    }
   /*//**
     * 模板导出Excel
     *
     *//*
    public void exportTempExcel(String html,String ) {
        HSSFWorkbook workbook = createModelExcel(html);
        String attachPath = AppUtil.getAttachPath();
        String tempPath = attachPath + File.separator + System.currentTimeMillis() + File.separator + fileName;;
        //生成Excel文件
        try {
            FileOperator.createFolder(tempPath);
            FileOutputStream os = new FileOutputStream(new File(tempPath + File.separator + fileName + ".xls"));
            try {
                wb.write(os);
                os.flush();
                os.close();
            } catch (IOException e) {

                e.printStackTrace();
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        File file = new File(tempPath + File.separator + fileName + ".xls");
        downloadFileByPath(wb, file.getName(), file, response, request);
        FileOperator.delFoldsWithChilds(tempPath);
    }*/
    /**
     * 导出Excel，生成Excel
     *
     * @param html
     * @return
     */
    public HSSFWorkbook createExcel(String html,String security) {
        html = html.replaceAll("&", "#");
        html = htmlToLower(html);
        // 以下开始写excel
        // 创建新的Excel 工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("表单内容");
        List<CrossRangeCellMeta> crossRowEleMetaLs = new ArrayList<CrossRangeCellMeta>();
        int rowIndex = 0;
        try {
            String newHtml = "";
            html = "<html>" + html + "</html>";
            //针对 pad同步回来的签名照片
            String regexImg = "<img src=";
            if (html.indexOf(regexImg) >= 0) {
                String[] htmlImgArr = html.split(regexImg);
                newHtml = htmlImgArr[0];
                for (int i = 1; i < htmlImgArr.length; i++) {
                    String temp = htmlImgArr[i];
                    int tag = temp.indexOf("</td>");
                    StringBuffer tempSB = new StringBuffer(temp);
                    tempSB.insert(tag, "</img>");
                    newHtml += regexImg + tempSB.toString();

                }

            }
            if (!newHtml.equalsIgnoreCase("")) {
                html = newHtml;
            }

            Document tableDoc = DocumentHelper.parseText(html);
            List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");

            //第一行标定密级
            HSSFRow firstRow = sheet.createRow(rowIndex);
            HSSFCellStyle headCellStyle = this.SetHeaderStyle(workbook);
            HSSFCell headCell = firstRow.createCell(0);
            headCell.setCellValue("密级：");
            headCell.setCellStyle(headCellStyle);
            HSSFCell securityCell = firstRow.createCell(1);
            securityCell.setCellValue(security);
            securityCell.setCellStyle(headCellStyle);
            //行+1
            rowIndex = rowIndex + 2 ;


            for (Element table : tables) {
                List<Element> trs = table.selectNodes(".//tr");
                HSSFCellStyle contentStyle = getContentStyle(workbook);
                HSSFCellStyle HlinkStyle = getHlinkStyle(workbook);
                for (int i = 0; i < trs.size(); i++) {
                    Element tr = trs.get(i);
                    HSSFRow row = sheet.createRow(rowIndex);
                    List<Element> tds = tr.selectNodes(".//td");
                    int cellIndex = 0;
                    //生成Excel的单元格（含签署照片）
                    makeRowCell(workbook, sheet, tds, rowIndex, row, cellIndex, contentStyle, HlinkStyle, crossRowEleMetaLs);
                    if (tr.asXML().contains("<img")) {
                        rowIndex += 3;
                    } else {
                        rowIndex++;
                    }
                }
                for (CrossRangeCellMeta crcm : crossRowEleMetaLs) {
                    sheet.addMergedRegion(new CellRangeAddress(crcm.getFirstRow(), crcm.getLsatRow(), crcm.getFirstCol(), crcm.getLastCol()));
                }

                rowIndex = rowIndex + 3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }
    public HSSFWorkbook createSignModelExcel(String html) {
        html = html.replaceAll("&", "#");
        // 以下开始写excel
        // 创建新的Excel 工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet1 = workbook.createSheet("sheet1");
       
        
        
        HSSFSheet sheet = workbook.createSheet("sheet2");
        List<CrossRangeCellMeta> crossRowEleMetaLs = new ArrayList<CrossRangeCellMeta>();
        int rowIndex = 0;
        try {
            html = "<html>" + html + "</html>";
            Document tableDoc = DocumentHelper.parseText(html);
            List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");
            for (Element table : tables) {
                List<Element> trs = table.selectNodes(".//tr");
                HSSFCellStyle contentStyle = getContentStyle(workbook);
                HSSFCellStyle HlinkStyle = getHlinkStyle(workbook);
                for (int i = 0; i < trs.size(); i++) {
                    Element tr = trs.get(i);
                    HSSFRow row = sheet.createRow(rowIndex);
                    List<Element> tds = tr.selectNodes(".//td");
                    int cellIndex = 0;
                    makeModelRowCell(workbook, sheet, tds, rowIndex, row, cellIndex, contentStyle, HlinkStyle, crossRowEleMetaLs);
                    rowIndex++;
                }
                for (CrossRangeCellMeta crcm : crossRowEleMetaLs) {
                    sheet.addMergedRegion(new CellRangeAddress(crcm.getFirstRow(), crcm.getLsatRow(), crcm.getFirstCol(), crcm.getLastCol()));
                }

                rowIndex = rowIndex + 3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }
    public String modelInfo(TableTemp tableTemp) {
        String sheet1Html="<html><body><table width=\"100%\" class=\"layui-table\"><tbody><tr class=\"firstRow\">";
        sheet1Html+="<td valign=\"top\" align=\"center\">模板种类</td>";
        String modelType="";
        switch (tableTemp.getModelType()) {
		case "1":
			modelType="常规验收项目表模板";
			break;
		case "2":
			modelType="功能性能验收项目表模板";
			break;
		case "6":
			modelType="验收报告表模板";
			break;
		default:
			modelType="其他";
			break;
		}
        sheet1Html+="<td valign=\"top\" align=\"center\">"+modelType+"</td>";
        sheet1Html+="<td valign=\"top\" align=\"center\"></td>";
        sheet1Html+="</tr><tr>";
        sheet1Html+=" <td valign=\"top\">表单标题</td>";
        sheet1Html+="<td valign=\"top\">"+tableTemp.getName()+"</td>";
        sheet1Html+="<td valign=\"top\" align=\"center\"></td>";
        sheet1Html+="</tr>";
        List<com.cssrc.ibms.core.resources.io.bean.template.CheckCondition> list=ioCheckConditionDao.getByTempId(tableTemp.getId());
        if(list!=null&&list.size()!=0) {
        	
        	sheet1Html+=" <tr><td valign=\"top\">检查条件</td>";
        	String checkName="";
        	for(int i =0;i<list.size();i++) {
        		checkName+=list.get(i).getName()+";";
        	}
        	 sheet1Html+="<td valign=\"top\">"+checkName+"</td>";
        	 sheet1Html+="<td valign=\"top\" align=\"center\"></td>";
        	 sheet1Html+="</tr>";
        }
        List<SignDef> signDefList=ioSignDefDao.getByTempId(tableTemp.getId());
        if(signDefList!=null&&list.size()!=0) {
        	
        	sheet1Html+=" <tr><td valign=\"top\">签署人员</td>";
        	String signDefName="";
        	for(int i =0;i<signDefList.size();i++) {
        		signDefName+=signDefList.get(i).getName()+";";
        	}
        	 sheet1Html+="<td valign=\"top\">"+signDefName+"</td>";
        	 sheet1Html+="<td valign=\"top\" align=\"center\"></td>";
        	 sheet1Html+="</tr>";
        }
        sheet1Html+="</tbody></table></body></html>";
        return sheet1Html;
    }
    public HSSFWorkbook createSignModelExcel(String html,TableTemp tableTemp) {
        html = html.replaceAll("&", "#");
        //剔除 (实测值) 的样式,防止导出的excel不含有 实测值 三个字
        html=html.replaceAll("<i class=\"markup\">","");
        html=html.replaceAll("</i>","");
        // 以下开始写excel
        // 创建新的Excel 工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet1 = workbook.createSheet("sheet1");
        /*html+=""*/
        String sheet1Html=modelInfo(tableTemp);
        List<CrossRangeCellMeta> crossRowEleMetaLsSign = new ArrayList<CrossRangeCellMeta>();
        int rowIndexSign = 0;
        try {
        	Document tableDocSign = DocumentHelper.parseText(sheet1Html);
        	List<Element> tablesSign = tableDocSign.getRootElement().selectNodes(".//table");
        	 for (Element table : tablesSign) {
                 List<Element> trs = table.selectNodes(".//tr");
                 HSSFCellStyle contentStyle = getContentStyle(workbook);
                 HSSFCellStyle HlinkStyle = getHlinkStyle(workbook);
                 for (int i = 0; i < trs.size(); i++) {
                     Element tr = trs.get(i);
                     HSSFRow row = sheet1.createRow(rowIndexSign);
                     List<Element> tds = tr.selectNodes(".//td");
                     int cellIndex = 0;
                     makeModelRowCell(workbook, sheet1, tds, rowIndexSign, row, cellIndex, contentStyle, HlinkStyle, crossRowEleMetaLsSign);
                     rowIndexSign++;
                 }
                 for (CrossRangeCellMeta crcm : crossRowEleMetaLsSign) {
                     sheet1.addMergedRegion(new CellRangeAddress(crcm.getFirstRow(), crcm.getLsatRow(), crcm.getFirstCol(), crcm.getLastCol()));
                 }

                 rowIndexSign = rowIndexSign + 3;
             }
        }catch (Exception e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = workbook.createSheet("sheet2");
        List<CrossRangeCellMeta> crossRowEleMetaLs = new ArrayList<CrossRangeCellMeta>();
        int rowIndex = 0;
        try {
            html = "<html>" + html + "</html>";
            Document tableDoc = DocumentHelper.parseText(html);
            List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");
            for (Element table : tables) {
                List<Element> trs = table.selectNodes(".//tr");
                HSSFCellStyle contentStyle = getContentStyle(workbook);
                HSSFCellStyle HlinkStyle = getHlinkStyle(workbook);
                for (int i = 0; i < trs.size(); i++) {
                    Element tr = trs.get(i);
                    HSSFRow row = sheet.createRow(rowIndex);
                    List<Element> tds = tr.selectNodes(".//td");
                    int cellIndex = 0;
                    makeModelRowCell(workbook, sheet, tds, rowIndex, row, cellIndex, contentStyle, HlinkStyle, crossRowEleMetaLs);
                    rowIndex++;
                }
                for (CrossRangeCellMeta crcm : crossRowEleMetaLs) {
                    sheet.addMergedRegion(new CellRangeAddress(crcm.getFirstRow(), crcm.getLsatRow(), crcm.getFirstCol(), crcm.getLastCol()));
                }

                rowIndex = rowIndex + 3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }
    public HSSFWorkbook createModelExcel(String html) {
        html = html.replaceAll("&", "#");
        // 以下开始写excel
        // 创建新的Excel 工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("表单内容");

        List<CrossRangeCellMeta> crossRowEleMetaLs = new ArrayList<CrossRangeCellMeta>();
        int rowIndex = 0;
        try {
            html = "<html>" + html + "</html>";
            Document tableDoc = DocumentHelper.parseText(html);
            List<Element> tables = tableDoc.getRootElement().selectNodes(".//table");
            for (Element table : tables) {
                List<Element> trs = table.selectNodes(".//tr");
                HSSFCellStyle contentStyle = getContentStyle(workbook);
                HSSFCellStyle HlinkStyle = getHlinkStyle(workbook);
                for (int i = 0; i < trs.size(); i++) {
                    Element tr = trs.get(i);
                    HSSFRow row = sheet.createRow(rowIndex);
                    List<Element> tds = tr.selectNodes(".//td");
                    int cellIndex = 0;
                    makeModelRowCell(workbook, sheet, tds, rowIndex, row, cellIndex, contentStyle, HlinkStyle, crossRowEleMetaLs);
                    rowIndex++;
                }
                for (CrossRangeCellMeta crcm : crossRowEleMetaLs) {
                    sheet.addMergedRegion(new CellRangeAddress(crcm.getFirstRow(), crcm.getLsatRow(), crcm.getFirstCol(), crcm.getLastCol()));
                }

                rowIndex = rowIndex + 3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     * 生产行内容
     *
     * @param workbook
     * @param sheet
     * @param tdLs              td集合
     * @param rowIndex          行号
     * @param row               行对象
     * @param startCellIndex
     * @param crossRowEleMetaLs 跨行元数据集合
     * @return 最后一列的cell index
     */
    public int makeRowCell(HSSFWorkbook workbook, HSSFSheet sheet, List<Element> tdLs, int rowIndex, HSSFRow row, int startCellIndex, HSSFCellStyle cellStyle, HSSFCellStyle hlinkStyle, List<CrossRangeCellMeta> crossRowEleMetaLs) throws IOException {
        BufferedImage bufferImg = null;
        String attachPath = AppUtil.getAttachPath();
        int i = startCellIndex;
        for (int eleIndex = 0; eleIndex < tdLs.size(); i++, eleIndex++) {
            boolean flag = false;
            int captureCellSize = getCaptureCellSize(rowIndex, i, crossRowEleMetaLs);
            while (captureCellSize > 0) {
                for (int j = 0; j < captureCellSize; j++) {//当前行跨列处理(补单元格)
                    HSSFCell c = row.createCell(i);
                    c.setCellStyle(cellStyle);
                    i++;
                }
                captureCellSize = getCaptureCellSize(rowIndex, i, crossRowEleMetaLs);
            }
            Element tdEle = tdLs.get(eleIndex);
            String val = tdEle.getTextTrim();
            if (val.equals("")) {
                // 检查项 的值  和勾选情况
                List<Element> inputs = tdEle.selectNodes(".//input");
                String s = "";
                if (inputs != null && inputs.size() > 0) {
                    for (Element input : inputs) {
                        if (input.attributeValue("type").equals("button")) {
                            s = s + "";
                            flag = true;
                        } else if (input.asXML().contains("text")) {
                            String value = input.attributeValue("value");
                            if (value == null) {
                                value = "";
                            }
                            s = s + value + "/";
                        } else if (input.asXML().contains("checkbox") && input.asXML().contains("checked")) {
                            s = s + "✔/";
                        } else if (input.asXML().contains("checkbox") && !(input.asXML().contains("checked"))) {
                            s = s + " /";
                        }
                    }
                }
                if (s.length() > 0) {
                    val = s.substring(0, s.length() - 1);
                } else {
                    val = s;
                }
                // 签署照片
                List<Element> imgs = tdEle.selectNodes(".//img");
                if (imgs != null && imgs.size() > 0) {
                    for (Element img : imgs) {
                        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

                        String fileId = img.attributeValue("src").split("fileid=").length > 0 ? img.attributeValue("src").split("fileid=")[1] : "";
                        if (!fileId.equalsIgnoreCase("")) {
                            //加载图片
                            List<Map<String, Object>> sysFileList = formUtilDao.getListByTableNameAndFilter("CWM_SYS_FILE", Arrays.asList(new SyncBaseFilter("FILEID", "=", "'" + fileId + "'")));
                            String signFile = attachPath + File.separator + CommonTools.Obj2String(sysFileList.get(0).get("FILEPATH"));
                            bufferImg = ImageIO.read(new File(signFile));
                            ImageIO.write(bufferImg, "jpg", byteArrayOut);
                            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                            /**
                             dx1 - the x coordinate within the first cell.//定义了图片在第一个cell内的偏移x坐标，既左上角所在cell的偏移x坐标，一般可设0
                             dy1 - the y coordinate within the first cell.//定义了图片在第一个cell的偏移y坐标，既左上角所在cell的偏移y坐标，一般可设0
                             dx2 - the x coordinate within the second cell.//定义了图片在第二个cell的偏移x坐标，既右下角所在cell的偏移x坐标，一般可设0
                             dy2 - the y coordinate within the second cell.//定义了图片在第二个cell的偏移y坐标，既右下角所在cell的偏移y坐标，一般可设0
                             col1 - the column (0 based) of the first cell.//第一个cell所在列，既图片左上角所在列
                             row1 - the row (0 based) of the first cell.//图片左上角所在行
                             col2 - the column (0 based) of the second cell.//图片右下角所在列
                             row2 - the row (0 based) of the second cell.//图片右下角所在行
                             */
                            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 2, rowIndex, (short) (2 + 3), rowIndex + 3);
                            //插入图片 1
                            patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }

                    }
                }
            } else {
                val = val.replaceAll("#nbsp;", " ");
            }

            HSSFCell c = row.createCell(i);
            c.setCellValue(val);
            c.setCellStyle(cellStyle);
            //在Excel中添加超链接
            if (flag) {
                String id = "";
                HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_FILE);
                List<Element> inputs = tdEle.selectNodes(".//input");
                for (Element input : inputs) {
                    if (input.asXML().contains("text")) {
                        id = input.attributeValue("id");
                        break;
                    } else if (input.asXML().contains("checkbox")) {
                        id = input.attributeValue("id");
                    }
                }
                List<Map<String, Object>> file = formFolderDao.getFileByID(id);
                if (file.size() > 0) {
                    link.setAddress(id);
                    c.setHyperlink(link);
                    c.setCellStyle(hlinkStyle);
                }
            }

            int rowspan = NumberUtils.toInt(tdEle.attributeValue("rowspan"), 1);
            int colspan = NumberUtils.toInt(tdEle.attributeValue("colspan"), 1);
            if (rowspan > 1 || colspan > 1) {
                crossRowEleMetaLs.add(new CrossRangeCellMeta(rowIndex, i, rowspan, colspan));
            }
            if (colspan > 1) {
                for (int j = 1; j < colspan; j++) {
                    i++;
                    //页眉页脚添加样式（框线）
                    HSSFCell cs = row.createCell(i);
                    cs.setCellStyle(cellStyle);
                }
            }
        }
        return i;
    }

    private int makeModelRowCell(HSSFWorkbook workbook, HSSFSheet sheet, List<Element> tdLs, int rowIndex
            , HSSFRow row, int startCellIndex, HSSFCellStyle cellStyle, HSSFCellStyle hlinkStyle
            , List<CrossRangeCellMeta> crossRowEleMetaLs) {

        int i = startCellIndex;
        //换图对象
        Drawing drawing = sheet.createDrawingPatriarch();
        //POI工具类
        CreationHelper factory = workbook.getCreationHelper();
        //注解
        ClientAnchor anchor = factory.createClientAnchor();
        Comment comment = null;
        RichTextString string = null;

        for (int eleIndex = 0; eleIndex < tdLs.size(); i++, eleIndex++) {
            boolean flag = false;
            int captureCellSize = getCaptureCellSize(rowIndex, i, crossRowEleMetaLs);
            while (captureCellSize > 0) {
                for (int j = 0; j < captureCellSize; j++) {//当前行跨列处理(补单元格)
                    HSSFCell c = row.createCell(i);
                    c.setCellStyle(cellStyle);
                    i++;
                }
                captureCellSize = getCaptureCellSize(rowIndex, i, crossRowEleMetaLs);
            }
            Element tdEle = tdLs.get(eleIndex);
            String val = tdEle.getTextTrim();

            if (val.equals("")) {
                List<Element> inputs = tdEle.selectNodes(".//input");
                if (inputs != null && inputs.size() > 0) {
                    for (Element input : inputs) {
                        if (input.asXML().contains("text")) {
                            String id = input.attributeValue("id");
                            comment = drawing.createCellComment(anchor);
                            string = factory.createRichTextString(id);
                            comment.setString(string);
                            comment.setAuthor("Apache POI");

                            String value = input.attributeValue("value");
                            if (value == null) {
                                value = "";
                            }
                            val = value;
                            flag = true;
                        }
                    }
                }
            } else {
                val = val.replaceAll("#nbsp;", " ");
            }

            HSSFCell c = row.createCell(i);
            c.setCellValue(val);
            c.setCellStyle(cellStyle);
            //在Excel中添加注解
            if (flag) {
                c.setCellComment(comment);
            }

            int rowspan = NumberUtils.toInt(tdEle.attributeValue("rowspan"), 1);
            int colspan = NumberUtils.toInt(tdEle.attributeValue("colspan"), 1);
            if (rowspan > 1 || colspan > 1) {
                crossRowEleMetaLs.add(new CrossRangeCellMeta(rowIndex, i, rowspan, colspan));
            }
            if (colspan > 1) {
                for (int j = 1; j < colspan; j++) {
                    i++;
                    HSSFCell cs = row.createCell(i);
                    cs.setCellStyle(cellStyle);
                }
            }
        }
        return i;
    }

    /**
     * Description : 通过ID查找对应的执行状态（W_DATAPACKAGEINFO表）
     * Author : XYF
     * Date : 2018年8月14日上午9:10:09
     * Return : String
     */
    public String selectZxztById(String id) {
        String state = checkFormDao.selectZxztById(id);
        return state;
    }

    /**
     * Description : 判断当前用户ID是否在该实例的工作队中
     * Author : XYF
     * Date : 2018年8月14日上午9:28:57
     * Return : String
     */
    public String userIdPower(String userId, String dataId) {
        String gw = checkFormDao.selectGwById(dataId);
        String workteam = checkFormDao.selectWorkTeamById(gw);
        String sssgb = checkFormDao.selectSssgbById(dataId);
        String fzr = checkFormDao.selectFzrIdById(sssgb);
        String[] list = workteam.split(",");
        String state = "";
        if (userId.equals(fzr)) {
            state = "yes";
        } else {
            for (int i = 0; i < list.length; i++) {
                String User = list[i];
                if (User.equals(userId)) {
                    state = "yes";
                    break;
                }
            }
        }

        if (state == "") {
            state = "no";
        }

        return state;
    }

    /**
     * 批量删除检查表模版
     *
     * @param ids
     * @return
     */
    public List<String> templateListDel(String ids) {
        List<String> templateList = new ArrayList<>();
        boolean flag = true;
        List<String> idArr = new ArrayList<>();
        if (ids.contains(",")) {
            idArr = Arrays.asList(ids.split(","));
        } else {
            idArr.add(ids);
        }
//检查是不是有不可以删除的模板
        boolean ifCouldBeDelete=true;   //默认可以删除
        for (String id : idArr){
            Integer num=dataPackageDao.countNotFinishedByTemplateId(id);
            if (num > 0) {
                //存在无法删除的条目
                ifCouldBeDelete=false;
                Map map = checkFormDao.getByIdM(Long.valueOf(id));
                templateList.add(CommonTools.Obj2String(map.get("F_NAME")));
            }
        }
        //如果可以删除,开始删除
        if (ifCouldBeDelete){
            for (String id : idArr) {
                checkFormDao.deleteTemp(id);
                checkConditionDao.deleteCondition(id);
                definechecktypeDao.deleteTableItem(id);
                defineSignDao.deleteSign(id);
            }
        }
        return templateList;
    }

    /**
     * 获取rowspan占据的单元格
     *
     * @param rowIndex                行号
     * @param colIndex                列号
     * @param crossRowEleMetaLs跨行列元数据
     * @return 当前行在某列需要占据单元格
     */
    private int getCaptureCellSize(int rowIndex, int colIndex, List<CrossRangeCellMeta> crossRowEleMetaLs) {
        int captureCellSize = 0;
        for (CrossRangeCellMeta crossRangeCellMeta : crossRowEleMetaLs) {
            if (crossRangeCellMeta.getFirstRow() < rowIndex && crossRangeCellMeta.getLsatRow() >= rowIndex) {
                if (crossRangeCellMeta.getFirstCol() <= colIndex && crossRangeCellMeta.getLastCol() >= colIndex) {
                    captureCellSize = crossRangeCellMeta.getLastCol() - colIndex + 1;
                }
            }
        }
        return captureCellSize;
    }

    private HSSFCellStyle getContentStyle(HSSFWorkbook wb) {
        short fontsize = 10;
        String fontName = "宋体";
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom((short) 1);
        style.setBorderTop((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        HSSFFont font = wb.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontsize);
        style.setFont(font);
        return style;
    }

    private HSSFCellStyle getHlinkStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setUnderline(HSSFFont.U_SINGLE);
        font.setColor(HSSFColor.BLUE.index);
        style.setFont(font);
        return style;
    }

    private  HSSFCellStyle SetHeaderStyle(HSSFWorkbook workBook){
        //字体加粗
        HSSFFont font = workBook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        //黄色背景：唯一
        HSSFCellStyle y_cellStyle = workBook.createCellStyle();
        y_cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        y_cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        return y_cellStyle;
    }
    /**
     * Description : 保存表单模版的创建人和创建时间
     * Author : XYF
     * Date : 2018年10月18日上午11:16:10
     * Return : void
     */
    public void saveCreate(String Id, String userId, String time) {
        List<Map<String, Object>> temps = checkFormDao.selectTableTempById(Id);
        //判断是否存在指定ID的表单模版
        if (temps.size() > 0) {
            Object createTime = temps.get(0).get("F_CREATE_TIME");
            String CreateTime = String.valueOf(createTime);
            if ("".equals(CreateTime) || "null".equals(CreateTime)) {
                checkFormDao.saveCreateInformation(Id, userId, time);
            }

        }
    }

    /**
     * Description : 根据ID查询表单模版
     * Author : XYF
     * Date : 2018年10月18日下午2:35:55
     * Return : List<Map<String,Object>>
     */
    public List<Map<String, Object>> selectTableTempById(String Id) {
        List<Map<String, Object>> temps = checkFormDao.selectTableTempById(Id);
        return temps;
    }

    /**
     * 更新表单模板基础信息
     *
     * @param mid       主键
     * @param id        编号
     * @param name      模板名称
     * @param sign      签署
     * @param status    检查条件
     * @param attention 注意事项
     * @param type      类别
     * @return
     */
    public Map<String, Object> updateBasicInfo(String mid, String id, String name, String[] sign, String[] status, String attention, String type) {
        Map<String, Object> message = new HashMap<String, Object>();
        try {
            CheckForm checkForm = new CheckForm();
            checkForm.setCheckformId(Long.valueOf(mid));
            checkForm.setSnum(id);
            checkForm.setName(name);
            checkForm.setRemark(attention);
            checkForm.setType(type);
            checkFormDao.update(checkForm);
            checkConditionDao.deleteCondition(mid);
            defineSignDao.deleteSign(mid);
            message = saveCheckCondition(status, Long.valueOf(mid));
            if (message.get("success").equals("false")) {
                message.put("ID", mid);
                return message;
            } else {
                message = saveDefineSign(sign, Long.valueOf(mid));
                if (message.get("success").equals("false")) {
                    message.put("ID", mid);
                    return message;
                }
            }
            message.put("success", "true");
        } catch (Exception e) {
            message.put("success", "false");
            message.put("msg", "表单模板数据修改失败！" + e.getMessage());
        }
        message.put("ID", mid);
        return message;
    }

    /**
     * Description : 判断是否存在附件
     * Author : XYF
     * Date : 2018年8月13日上午10:58:51
     * Return : String
     */
    public String ifExistEnclosure(String id) {
        String result = "";
        Map<String, Object> num = formFolderDao.isPicExists(id);
        if (Integer.parseInt(num.get("count").toString()) > 0) {
            result = "yes";
        } else {
            result = "no";
        }
        return result;
    }

    public Map checkWhetherFileExist(List<Map<String, Object>> fileList) {
        Map resultMap = new HashMap();
        int count = 0;
        for (Map dataMap : fileList) {
            String slId = CommonTools.Obj2String(dataMap.get("slId"));
            String tableId = CommonTools.Obj2String(dataMap.get("id"));
            String ckResultName = productService.getCondResultNameByInsId(Long.parseLong(slId));
            if (!"".equals(ckResultName)) {
                ckResultName = "&&" + ckResultName;
            }
            resultMap.put(count+"", this.ifExistEnclosure(tableId + ckResultName));
            count++;
        }
        return resultMap;

    }
	/**
	 * table中input框切换成td
	 * auth:fuyong
	 * @param html
	 * @return
	 */
    public String conversionHTML(String html) {
    	org.jsoup.nodes.Document doc = Jsoup.parse(html);
		Elements elList = doc.select("input");
		/*doc.select("input").attr("style", "display:none");*/
		for (org.jsoup.nodes.Element element : elList) {

			String value=element.val();
		/*	String type=element.attr("type");*/
			if(!value.equals("附件")){
                element.attr("style","display:none");
                element.append("<span style=\"width:60dp\">");
                element.append(value);
                element.append("</span>");
            }

			Boolean isExit=element.hasAttr("isfull");
			if (isExit) {
				String isCheck=element.attr("isfull");
				if (isCheck.equals("不合格")) {
					element.parent().attr("style","background:red");
				}
			}
		}
		return doc.html();
    }
}
