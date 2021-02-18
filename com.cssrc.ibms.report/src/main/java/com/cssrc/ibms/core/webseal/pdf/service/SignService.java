package com.cssrc.ibms.core.webseal.pdf.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.report.inf.ISignHandlerAdapter;
import com.cssrc.ibms.api.report.inf.ISignModelService;
import com.cssrc.ibms.api.report.inf.ISignService;
import com.cssrc.ibms.api.report.model.ISignModel;
import com.cssrc.ibms.api.report.model.IbmsSign;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.HttpClientUtil;
import com.cssrc.ibms.core.webseal.pdf.KeyExecute;
import com.cssrc.ibms.core.webseal.pdf.PdfSignException;
import com.cssrc.ibms.core.webseal.pdf.sign.PdfSealOverFlow;
import com.cssrc.ibms.report.model.ReportTemplate;
import com.cssrc.ibms.report.service.ReportTemplateService;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

@Service("signService")
public class SignService implements ISignService, ISignHandlerAdapter
{
    @Resource
    ISysUserService sysUserService;
    
    @Resource
    ISignModelService signModelService;
    
    @Resource
    ReportTemplateService reportTemplateService;
    
    /**
     * 签名
     * 
     * @param ibmsSign
     * @return
     * @throws Exception
     */
    @Override
    public byte[] sign(List<IbmsSign> ibmsSigns)
        throws Exception
    {
        List<byte[]> signReports = new ArrayList<byte[]>();
        List<Long> signUsers = new ArrayList<Long>();
        for (IbmsSign ibmsSign : ibmsSigns)
        {
            byte[] bt = HttpClientUtil.httpGetByte(creatReportUrl(ibmsSign));
            signReports.add(bt);
            List<Long> users = ibmsSign.getSignUsers();
            for (Long u : users)
            {
                if (!signUsers.contains(u))
                {
                    signUsers.add(u);
                }
            }
        }
        // 临时文件保存签章后的报表
        File tempF = null;
        // 创建临时文件
        tempF = FileUtil.generateTempFile();
        byte[] bt=this.concatPDFs(signReports, true);
        // 创建签名Execute
        KeyExecute keyExecute = creatSignExecute(signUsers);
        // 创建签名引擎
        PdfSealOverFlow pdfSeal = new PdfSealOverFlow(bt, tempF.getPath(), keyExecute);
        pdfSeal.setKeyExecute(keyExecute);
        pdfSeal.seal(ibmsSigns.get(0));
        byte[] result = FileUtil.readByte(tempF.getPath());
        tempF.delete();
        return result;
    }
    
    /**
     * 签名
     * 
     * @param ibmsSign
     * @return
     * @throws Exception
     */
    @Override
    public byte[] sign(IbmsSign ibmsSign)
        throws Exception
    {
        // 报表名称
        // String reportlet = ibmsSign.getReportname();
        // 获取报表文件流
        byte[] bt = HttpClientUtil.httpGetByte(creatReportUrl(ibmsSign));
        // 临时文件保存签章后的报表
        File tempF = null;
        try
        {
            // 创建临时文件
            tempF = FileUtil.generateTempFile();
            // 创建签名Execute
            KeyExecute keyExecute = creatSignExecute(ibmsSign);
            // 创建签名引擎
            PdfSealOverFlow pdfSeal = new PdfSealOverFlow(bt, tempF.getPath(), keyExecute);
            pdfSeal.setKeyExecute(keyExecute);
            pdfSeal.seal(ibmsSign);
            byte[] result = FileUtil.readByte(tempF.getPath());
            tempF.delete();
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (tempF != null && tempF.exists())
            {
                tempF.delete();
            }
        }
    }
    
    /**
     * 前端js签名后下载
     * 
     * @param title
     * @param refUserSign
     * @return
     * @throws Exception
     */
    @Override
    public byte[] signDownload(HttpServletRequest request, String title, String refUserSign)
        throws Exception
    {
        
        ISignHandlerAdapter signUser = (ISignHandlerAdapter)AppUtil.getBean(refUserSign);
        if (signUser == null)
        {
            String msg = "没有找到'" + ISignHandlerAdapter.class.getName() + "的实现'" + refUserSign + "'";
            throw new PdfSignException(PdfSignException.RCE_NOKEY, msg);
        }
        // 需要签名的用户
        List<Long> users = signUser.getSignUsers(request);
        // 需要签名的报表文件--reportname为报表服务器上存储的报表模板的相对路径
        ReportTemplate reportTemplate = this.reportTemplateService.getByTitle(title);
        String reportname = reportTemplate.getReportlocation();
        // 报表需要的参数
        Map<String, String> param = signUser.getReportParam(request);
        // 签名模型
        IbmsSign ibmsSign = new IbmsSign(users, reportname, param);
        return this.sign(ibmsSign);
    }
    
    /**
     * 创建签名引擎KeyExecute
     * 
     * @param ibmsSign
     * @return
     */
    private KeyExecute creatSignExecute(IbmsSign ibmsSign)
    {
        List<Long> users = ibmsSign.getSignUsers();
        return creatSignExecute(users);
    }
    
    private KeyExecute creatSignExecute(List<Long> users)
    {
        KeyExecute keyExecute = new KeyExecute();
        for (Long user : users)
        {
            ISysUser sysuser = sysUserService.getById(user);
            if (BeanUtils.isEmpty(sysuser))
            {
                throw new PdfSignException(PdfSignException.RCE_NOKEY, "签名用户：" + user + ":没有找到对应的用户");
            }
            ISignModel sign = signModelService.getDefaultByUserId(user);
            if (BeanUtils.isEmpty(sign))
            {
                throw new PdfSignException(PdfSignException.RCE_NOKEY, "签名用户：" + user + ":没有找到用户默认的印章");
            }
            /*
             * 签名印章的印章图片路径 可能存储在分布式服务器，或者是本地服务器，或者是数据库
             */
            String signImage = sign.getSignImage();
            keyExecute.addExecute(ISignModel.PRE_KEY + sysuser.getFullname(), signImage);
        }
        return keyExecute;
    }
    
    /**
     * 获取报表服务器读取报表模板文件的URL,URL中包含参数等信息
     * 
     * @param ibmsSign
     * @return
     */
    private String creatReportUrl(IbmsSign ibmsSign)
    {
        String reportlet = ibmsSign.getReportname();
        // 报表参数
        Map<String, String> paramMap = ibmsSign.getParamMap();
        // 报表服务器地址
        String url = AppConfigUtil.get("pluginproperties", "fr.server.url");
        // 报表生成接口mapping
        String mapping = "/getReport";
        url = url + mapping;
        String param = "?reportlet=" + reportlet;
        if (!BeanUtils.isEmpty(paramMap))
        {
            for (Map.Entry<String, String> entry : paramMap.entrySet())
            {
                param += "&" + entry.getKey() + "=" + entry.getValue();
            }
        }
        return url + param;
    }
    
    /**
     * 前端js 签名的话需要实现该方法， 以下只是一段demo 代码
     * 
     * @param request
     * @return List<Long>
     */
    @Override
    public List<Long> getSignUsers(HttpServletRequest request)
    {
        List<Long> users = new ArrayList<Long>();
        users.add(10000003150001l);
        return users;
    }
    
    /**
     * 前端js 签名的话需要实现该方法， 以下只是一段demo 代码
     * 
     * @param request
     * @return List<Long>
     */
    @Override
    public Map<String, String> getReportParam(HttpServletRequest request)
    {
        Map<String, String> param = new HashMap<String, String>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements())
        {
            String paramName = params.nextElement();
            String val = request.getParameter(paramName);
            param.put(paramName, val);
        }
        return param;
    }
    
    public byte[] concatPDFs(List<byte[]> streamOfPDFFiles, boolean paginate)
    {
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        try
        {
            List<byte[]> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<byte[]> iteratorPDFs = pdfs.iterator();
            
            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext())
            {
                byte[] pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            
            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // data
            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();
            
            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext())
            {
                PdfReader pdfReader = iteratorPDFReader.next();
                
                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages())
                {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                    
                    // Code for pagination.
                    if (paginate)
                    {
                        cb.beginText();
                        cb.setFontAndSize(bf, 9);
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                            "" + currentPageNumber + " of " + totalPages,
                            520,
                            5,
                            0);
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
            return outputStream.toByteArray();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (document.isOpen())
                document.close();
            try
            {
                if (outputStream != null)
                    outputStream.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
    }
    
}
