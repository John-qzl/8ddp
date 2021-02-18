package com.cssrc.ibms.core.webseal.pdf.sign;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cssrc.ibms.api.report.model.IbmsSign;
import com.cssrc.ibms.core.encrypt.KGBase64;
import com.cssrc.ibms.core.webseal.pdf.ElectronicSealName;
import com.cssrc.ibms.core.webseal.pdf.KeyExecute;
import com.cssrc.ibms.core.webseal.pdf.KeyPosition;
import com.cssrc.ibms.core.webseal.pdf.PdfPositionEngine;
import com.cssrc.ibms.core.webseal.pdf.intf.AbstractIbmsSeal;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;

/*
 * @ClassName: PdfSealOverFlow
 * @Description: TODO(pdf文件图片悬浮签名)
 * @author zxg
 * @date 2016年12月5日 下午3:00:14
 */

public class PdfSealOverFlow extends AbstractIbmsSeal
{
    
    public PdfSealOverFlow(String infile, String outile, KeyExecute keyExecute)
        throws Exception
    {
        super(infile, outile, keyExecute);
        this.reader = new PdfReader(this.inFile);
        this.ops = new FileOutputStream(this.outFile);
        this.stamper = new PdfStamper(reader, ops); // 生成的输出流
        
    }
    
    public PdfSealOverFlow(byte[] bfile, String outile, KeyExecute keyExecute)
        throws Exception
    {
        super(bfile, outile, keyExecute);
        this.reader = new PdfReader(this.bfile);
        this.ops = new FileOutputStream(this.outFile);
        this.stamper = new PdfStamper(reader, ops); // 生成的输出流
    }
    
    /*
     * 盖章
     * 
     * @return
     * 
     * @author zxg
     * 
     * @throws IOException
     */
    @Override
    public void seal(IbmsSign ibmsSign)
        throws Exception
    {
        try
        {
            int total = reader.getNumberOfPages() + 1;
            PdfPositionEngine pdfPositionEngine = new PdfPositionEngine(this);
            for (int i = 1; i < total; i++)
            {
                this.setCreentPage(i);
                // 根据pdf 当前页，定位坐标
                List<KeyPosition> postions = pdfPositionEngine.getPosition();
                // 根据定位的坐标添加图片签章
                for (KeyPosition keyPosition : postions)
                {
                    // 插入图片
                    this.sign(keyPosition, ibmsSign, i);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (stamper != null)
            {
                stamper.close();
            }
            if (reader != null)
            {
                reader.close();
            }
            if (ops != null)
            {
                ops.close();
            }
        }
        
    }
    
    public void sign(KeyPosition keyPosition, IbmsSign ibmsSign, int pageNum)
        throws Exception
    {
        
        Image img = Image.getInstance(keyPosition.getImgPath());
        
        Float imgW = ibmsSign != null && ibmsSign.getSignW() != null ? ibmsSign.getSignW() : img.getScaledWidth();
        Float imgH = ibmsSign != null && ibmsSign.getSignH() != null ? ibmsSign.getSignH() : img.getScaledHeight();
        Float offSetH = ibmsSign != null && ibmsSign.getSignH() != null ? ibmsSign.getOffSetH() : 0f;
        float x = keyPosition.getX();
        float y = keyPosition.getY();
        float left = x;
        float top = (y - imgH / 2f) + offSetH;
        float right = x + imgW;
        float bottom = (y + imgH / 2f) + offSetH;
        Rectangle rect = new Rectangle(left, top, right, bottom);
        
        //this.sealNumb = getSealNum(reader);
        PdfWriter pdfWriter = stamper.getWriter();
        
        rect.normalize();
        
        PdfAnnotation annotation = new PdfAnnotation(pdfWriter, rect);
        
        annotation.put(PdfName.TYPE, PdfName.ANNOT);
        annotation.put(PdfName.SUBTYPE, ElectronicSealName.SEAL);
        annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
        
        PdfAppearance app = PdfAppearance.createAppearance(pdfWriter, rect.getWidth(), rect.getHeight());
        
        PdfGState gstate = new PdfGState();
        gstate.setBlendMode(PdfGState.BM_MULTIPLY);
        app.setGState(gstate);
        
        app.addImage(img, rect.getWidth(), 0.0F, 0.0F, rect.getHeight(), 0.0F, 0.0F);
        
        annotation.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, app);
        
        annotation.put(PdfName.INFO, getKGSealInfoRef(reader, stamper, img));
        reader.getCatalog().put(ElectronicSealName.SEALNUM, new PdfNumber(++this.sealNumb));
        stamper.addAnnotation(annotation, pageNum);
        
    }
    
    private int getSealNum(PdfReader pdfReader)
    {
        PdfNumber pdfNumber = pdfReader.getCatalog().getAsNumber(ElectronicSealName.SEALNUM);
        if (pdfNumber != null)
        {
            return pdfNumber.intValue();
        }
        return 0;
    }
    
    private PdfIndirectReference getKGSealInfoRef(PdfReader pdfReader, PdfStamper pdfStamper, Image image)
    {
        PdfDictionary dicInfo = new PdfDictionary();
        dicInfo.put(ElectronicSealName.PROVERSION, new PdfNumber(this.proversion));
        dicInfo.put(ElectronicSealName.UNICODE, new PdfBoolean(true));
        Map<String, String> map = new HashMap<String, String>();
        map.put("sealIndex", String.valueOf(this.sealNumb));
        map.put("Width", String.valueOf(image.getWidth()));
        map.put("Height", String.valueOf(image.getHeight()));
        map.put("actWidth", String.valueOf(image.getScaledWidth()));
        map.put("actHeight", String.valueOf(image.getScaledHeight()));
        map.put("bProtectDoc", "1");
        map.put("bEffect", "1");
        map.put("bCertSign", "0");
        map.put("typeSign", "1");
        map.put("doffSign", "0");
        map.put("DateCheck", "0");
        map.put("noConnection", "0");
        
        KGBase64 base64 = new KGBase64();
        base64.setBase64Table("=ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
        PdfWriter pdfWriter = pdfStamper.getWriter();
        try
        {
            String sealMsg = base64.encode(getBaseInfo(map).getBytes("UTF-16LE"));
            PdfStream baseInfo = new PdfStream(sealMsg.getBytes());
            PdfNumber pdfNumber = new PdfNumber(sealMsg.length());
            
            PdfIndirectObject pdfNumberIndObj = pdfWriter.addToBody(pdfNumber);
            baseInfo.put(ElectronicSealName.STRLEN, pdfNumberIndObj.getIndirectReference());
            
            PdfIndirectObject baseInfoIndObj = pdfWriter.addToBody(baseInfo);
            dicInfo.put(ElectronicSealName.BASEINFO, baseInfoIndObj.getIndirectReference());
            PdfIndirectObject dicInfoUndObj = pdfWriter.addToBody(dicInfo);
            
            return dicInfoUndObj.getIndirectReference();
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private String getBaseInfo(Map<String, String> map)
    {
        StringBuilder builder = new StringBuilder();
        Set<String> keys = map.keySet();
        for (String key : keys)
        {
            builder.append(key + "=" + (String)map.get(key)).append("\r\n");
        }
        return builder.toString();
    }
    

    
}
