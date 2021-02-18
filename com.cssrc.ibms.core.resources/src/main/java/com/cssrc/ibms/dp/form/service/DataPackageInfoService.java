package com.cssrc.ibms.dp.form.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.resources.datapackage.dao.PackageDao;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.ZipCompressor;
import com.cssrc.ibms.dp.form.dao.CheckConditionDao;
import com.cssrc.ibms.dp.form.dao.CheckFormDao;
import com.cssrc.ibms.dp.form.dao.CheckResultCarryDao;
import com.cssrc.ibms.dp.form.dao.CheckResultDao;
import com.cssrc.ibms.dp.form.dao.CheckResultJgjgDao;
import com.cssrc.ibms.dp.form.dao.CkConditionResultDao;
import com.cssrc.ibms.dp.form.dao.DataPackageInfoDao;
import com.cssrc.ibms.dp.form.dao.DefineCheckTypeDao;
import com.cssrc.ibms.dp.form.dao.DefineSignDao;
import com.cssrc.ibms.dp.form.dao.FormUtilDao;
import com.cssrc.ibms.dp.form.dao.ProductTypeDao;
import com.cssrc.ibms.dp.form.dao.SignResultDao;
import com.cssrc.ibms.dp.form.dao.TbInstantDao;
import com.cssrc.ibms.dp.form.model.CrossRangeCellMeta;
import com.cssrc.ibms.dp.sync.util.SyncBaseFilter;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SysFileService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * User :  sgl.
 * Date : 2019/1/18.
 * Time : 14:25.
 */
@Service
public class DataPackageInfoService {
    private static final String TAG = "DataPackageInfoService";
    @Resource
    private FormService formService;
    @Resource
    private DataPackageInfoDao dataPackageInfoDao;
    @Resource
    CheckConditionDao checkConditionDao;
    @Resource
    DefineSignDao defineSignDao;
    @Resource
    CheckFormDao checkFormDao;
    @Resource
    DefineCheckTypeDao definechecktypeDao;
    @Resource
    TbInstantDao tbInstantDao;
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
    private PackageDao packageDao;
    /**
     * @Description: 生成Excel文件
     * @Author  shenguoliang
     * @param html
     * @param security
     * @Date 2019/1/19 9:56
     * @Return org.apache.poi.hssf.usermodel.HSSFWorkbook
     * @Line 308
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
                    formService.makeRowCell(workbook, sheet, tds, rowIndex, row, cellIndex, contentStyle, HlinkStyle, crossRowEleMetaLs);
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

    @Resource
    private SysFileService sysFileService;
    /**
     * @Description: 下载压缩的选择的所有表单实例
     * @Author  shenguoliang
     * @param request
     * @param response
     * @param dataList
     * @Date 2019/1/19 9:39
     * @Return com.cssrc.ibms.core.util.result.ResultMessage
     * @Line 83
     */
    public void batchExportExcel(HttpServletRequest request, HttpServletResponse response, List<Map<String, Object>> dataList) {
        List<Map<String, Object>> tempDataList = new ArrayList();
        tempDataList = this.getDataPackageInfoDataByDataIds(dataList);
        //所属数据包ID
        String packageId = CommonTools.Obj2String(tempDataList.get(0).get("package"));
        //所属数据包节点名称
        String packageName = CommonTools.Obj2String(packageDao.getById(Long.parseLong(packageId)).get("F_JDMC"));
        // 附件保存路径
        String attachPath = AppUtil.getAttachPath();
        //用于存储各个不同表单实例以及合并的压缩包文件夹
        String tempPath = attachPath + File.separator + "temp" + File.separator + System.currentTimeMillis();
        FileOperator.createFolders(tempPath);
        //压缩包文件名
        String fileName = packageName + "_" + new SimpleDateFormat("yyyyMMddHH24mmss").format(new Date());
        //导出的压缩包名
        String resFile = tempPath + File.separator + fileName +"(公开)"+".zip";
        //循环表单实例并生成文件夹
        for (Map dataMap : tempDataList) {
            Map htmlMap = this.getFormInstanceHTML(CommonTools.Obj2String(dataMap.get("slid")), request.getContextPath());
            String formInstanceHtml = this.spliceFormInstanceHTML(htmlMap);
            String slid = CommonTools.Obj2String(dataMap.get("slid")) ;
            String security = CommonTools.Obj2String(dataMap.get("security"));
            //导出表单实例Excel数据以及相关附件
            this.exportExcel(request, response, formInstanceHtml, slid,security,tempPath);
        }
        File fileFolder = new File(tempPath +  File.separator);
        if (fileFolder.exists()) {
            //将文件夹中所有的文件压缩成压缩包
            File[] ckFiles = fileFolder.listFiles();
            String[] filePath = new String[ckFiles.length];
            for (int i = 0; i < ckFiles.length; i++) {
                filePath[i] = ckFiles[i].getPath();
            }
            ZipCompressor zc = new ZipCompressor(resFile);
            //压缩多个文件
            zc.compress(filePath);
            //创建文件夹（用于存储各个表单实例文件夹）
            File file = new File(resFile);
            //将压缩包文件存储到response文件流中返回前台
            this.downloadFileByPath(file.getName(), file, response, request);
            //删除创建的临时文件夹
            FileOperator.delFoldsWithChilds(tempPath);
        }
    }
    /**
     * @Description: 根据前台的dataIds获取相关数据包详细信息
     * @Author  shenguoliang
     * @param dataList
     * @Date 2019/1/18 15:42
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Line 42
     */
    public List<Map<String, Object>>getDataPackageInfoDataByDataIds(List<Map<String, Object>> dataList) {
        List<Map<String, Object>> tempDataList = new ArrayList();
        for (Map dataMap : dataList) {
            Long dataPackageId = Long.parseLong(CommonTools.Obj2String(dataMap.get("id")));
            Map dataPackageInfoMap = dataPackageInfoDao.getDataMapById(dataPackageId);
            dataMap.put("slid",dataPackageInfoMap.get("F_SSMB"));//表单实例
            dataMap.put("security",dataPackageInfoMap.get("F_MJ"));//密级
            dataMap.put("package", dataPackageInfoMap.get("F_SSSJB"));//所属数据包
            tempDataList.add(dataMap);
        }
        return tempDataList;
    }

    /**
     * @Description: 拼接表单实例的HTML
     * @Author  shenguoliang
     * @param htmlMap
     * @Date 2019/1/19 9:38
     * @Return java.lang.String
     * @Line 140
     */
    public String  spliceFormInstanceHTML(Map htmlMap){
        String html = "";
        JSONArray signResult = JSONArray.fromObject(htmlMap.get("signresult"));
        String contentHtml = CommonTools.Obj2String(htmlMap.get("content"));
        JSONArray conditionRes = JSONArray.fromObject(htmlMap.get("condires"));
        //检查条件HTML
        StringBuilder conditionResHtml = new StringBuilder();
        conditionResHtml.append("<table> ");
        for(int i = 0 ; i < conditionRes.size() ; i ++){
            JSONObject obj = conditionRes.getJSONObject(i);
            conditionResHtml.append(" <tr> ");
            conditionResHtml.append("<td align=\"right \" width=\"20% \">"+obj.get("F_NAME")+"：</td> ");
            conditionResHtml.append("<td align=\"left \">"+obj.get("F_VALUE")+"</td> ");
            conditionResHtml.append(" </tr> ");
        }
        conditionResHtml.append(" </table> ");
        //签署项HTML
        StringBuilder signResultHtml = new StringBuilder();
        signResultHtml.append("<table> ");
        for(int i = 0 ; i < signResult.size() ; i ++){
            JSONObject obj = signResult.getJSONObject(i);
            signResultHtml.append(" <tr> ");
            signResultHtml.append("<td align=\"right \" width=\"20% \">"+obj.get("F_NAME")+"：</td> ");
            if(obj.get("imgSrc") == ""){
                signResultHtml.append("<td align=\"left \">"+obj.get("F_SIGNUSER")+"</td> ");
            }else{
                signResultHtml.append("<td align=\"left \">").append("<img src=\""+obj.get("imgSrc")+"\" >").append(" </td> ");
            }
            signResultHtml.append(" </tr> ");
        }
        signResultHtml.append(" </table> ");
        //合并HTML
        html = contentHtml + "\n" + conditionResHtml.toString()+ "\n" +signResultHtml.toString();
        return html;
    }
    /**
     * @Description: 导出表单实例Excel数据以及相关附件
     * @Author  shenguoliang
     * @param request
     * @param response
     * @param html
     * @param slid
     * @param security
     * @param fileDirPath
     * @Date 2019/1/19 9:43
     * @Return void
     * @Line 202
     */
    public void exportExcel(HttpServletRequest request, HttpServletResponse response,
                            String html,String slid,String security, String fileDirPath ) {
        //W_TB_INSTANT   实例id
        Map<String, Object> param = new HashMap();
        param.put("id", slid);
        Map<String, Object> tableins = formService.getTableInstantById(param);
        // W_TABLE_TEMP   模板id
        param.put("id", tableins.get("F_TABLE_TEMP_ID").toString());
        Map<String, Object> tabletemp = formService.getTableTempById(param);
        try {
            String fileName = tabletemp.get("F_NAME").toString();
            //生成Excel
            HSSFWorkbook wb = createExcel(html,security);
            //生成附件
            downloadopphotofinished(request, response, slid, wb, fileName,fileDirPath,security);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @Description: 获取表单实例中检查条件List\检查内容HTML\签署项List并存放于Map中
     * @Author  shenguoliang
     * @param slid
     * @param contextPath
     * @Date 2019/1/19 9:44
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     * @Line 230
     */
    public Map<String,Object> getFormInstanceHTML(String slid,String contextPath){
        // 附件保存路径
        String attachPath = AppUtil.getAttachPath();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", slid);
        Map<String, Object> tableins = formService.getTableInstantById(param);
        List<Map<String, Object>> signresult = formService
                .getSignResultById(slid);
        if (signresult.size() > 0) {
            for (int i = 0; i < signresult.size(); i++) {
                try {
                    String fileId = signresult.get(i).get("FILEID").toString();
                    SysFile sysFile = sysFileService.getById(Long.valueOf(fileId));
                    String filePath = sysFile.getFilepath();
                    String fileType = sysFile.getExt();
                    String fileName = sysFile.getFilename() + "." + sysFile.getExt();

                    Boolean isNoGroup = filePath.startsWith("group");// 判断是否分布式文件
                    String interview_server = FastDFSFileOperator.getInterviewServer(); // 分布式请求url端口
                    if (isNoGroup) {
                        if (sysFile.getIsEncrypt() != 1L) {
                            filePath = interview_server + "/" + sysFile.getFilepath();
                        }
                    } else {
                        filePath = attachPath + File.separator + filePath;
                    }
                    String destFilePath = sysFileService.getDecodeFilePath(filePath, fileName, sysFile.getIsEncrypt(), isNoGroup);
                    if (!"".equals(destFilePath)) {
                        filePath = destFilePath;
                        fileName = FileOperator.getFileNameByPath(filePath);
                    }
                    if ("png,bmp,gif,jpg".contains(fileType.toLowerCase())) {
                        String imgSrc = contextPath + "/oa/system/sysFile/getFileById.do?fileId=" + fileId;
                        signresult.get(i).put("imgSrc", imgSrc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            signresult = formService.getSignResById(slid);
        }
        //检查条件
        List<Map<String, Object>> condires = formService.getCondiResById(slid);
        //检查项描述
        List<Map<String, Object>> checkItemandmap = formService.getCheckItemAndMapById(slid);
        //检查内容
        String content = tableins.get("F_CONTENT").toString();
        int index1 = content.indexOf("<table");
        int index2 = content.lastIndexOf("</table>");
        content = content.substring(index1, index2 + 8);
        content = content.replaceAll(
                "<input class=\"dpInputBtn\" type=\"button\" disabled=\"true\"",
                "<input class=\"dpInputBtn\" type=\"button\"");
        //可能存在未能将按钮的disabled属性正确替换的情况（因为构建表单模板时的一些顺序因素），这里需要重新替换
        content = content.replaceAll("disabled=\"true\"", " ");
        Map htmlMap = new HashMap();
        htmlMap.put("content", content);
        htmlMap.put("signresult", signresult);
        htmlMap.put("condires", condires);

        return htmlMap;
    }

    /**
     * @Description: 将表单实例中的Excel数据以及附件导出在文件夹中
     * @Author  shenguoliang
     * @param request
     * @param response
     * @param instantId
     * @param workBook
     * @param fileName
     * @param fileDirPath
     * @Date 2019/1/19 9:54
     * @Return void
     * @Line 375
     */
    public void downloadopphotofinished(HttpServletRequest request, HttpServletResponse response,
                                        String instantId, HSSFWorkbook workBook, String fileName,String fileDirPath,String security) {
        // 附件保存路径
        String attachPath = AppUtil.getAttachPath();
        //获取检查结果的list
        List<Map<String, Object>> resultList = formService.getCheckresByID(instantId);
        String targetPath = fileDirPath + File.separator + fileName;
        //生成附件
        for (Map<String, Object> result : resultList) {
            String resId = CommonTools.Obj2String(result.get("ID").toString());
            Map typeMap = productTypeDao.getProductType(Long.valueOf(instantId));
            String ck_resultName = "";
            if ("空间".equals(typeMap.get("TYPE"))) {
                ck_resultName = "W_CK_RESULT";
            } else if ("运载".equals(typeMap.get("TYPE"))) {
                ck_resultName = "W_CK_RESULT_CARRY";
            } else {
                ck_resultName = "W_CK_RESULT_JGJG";
            }
            String filterTableId = resId + "&&" + ck_resultName;
            List<Map<String, Object>> sysFileList = formUtilDao.getListByTableNameAndFilter("CWM_SYS_FILE",
                    Arrays.asList(new SyncBaseFilter("TABLEID", "=", "'" + filterTableId + "'")));
            if (sysFileList.size() > 0) {
                //根据检查结果的ID，创建附件文件夹,附件的文件夹名字尽量用检查名称、简称和描述来代替resId----start
                String targetPathRes = targetPath + File.separator + resId;
                List<Map<String, Object>> ckResultList = formUtilDao.getListByTableNameAndFilter(ck_resultName,
                        Arrays.asList(new SyncBaseFilter("ID", "=", "'" + resId + "'")));
                if (ckResultList.size() > 0) {
                    List<Map<String, Object>> itemDefList = formUtilDao.getListByTableNameAndFilter("W_ITEMDEF",
                            Arrays.asList(new SyncBaseFilter("ID", "=", "'" + CommonTools.Obj2String(ckResultList.get(0).get("F_ITEMDEF_ID")) + "'")));
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
                    FileOperator.copyFile(resFile, targetPathRes + File.separator +
                            CommonTools.Obj2String(map.get("FILENAME")) +"("+security+")"+"." + CommonTools.Obj2String(map.get("EXT")));
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

        String resFile = fileDirPath + File.separator + fileName + ".zip";
        try {
            //注销这里的导出压缩表单实例的数据所在文件夹
//            File insFile = new File(fileDirPath + File.separator + fileName + File.separator);
//            if (insFile.exists()) {
//                File[] ckFiles = insFile.listFiles();
//                String[] filePath = new String[ckFiles.length];
//                for (int i = 0; i < ckFiles.length; i++) {
//                    filePath[i] = ckFiles[i].getPath();
//                }
//                ZipCompressor zc = new ZipCompressor(resFile);
//                zc.compress(filePath);
////                File file = new File(resFile);
////                this.downloadFileByPath(file.getName(), file, response, request);
//
//                FileOperator.delFoldsWithChilds(fileDirPath);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @Description: 下载单个文件(//将文件流存储在response中返回前台浏览器)
     * @Author  shenguoliang
     * @param fileName
     * @param downloadFile
     * @param response
     * @param request
     * @Date 2019/1/18 17:13
     * @Return void
     * @Line 304
     */
    private void downloadFileByPath( String fileName, File downloadFile, HttpServletResponse response, HttpServletRequest request) {
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
}
