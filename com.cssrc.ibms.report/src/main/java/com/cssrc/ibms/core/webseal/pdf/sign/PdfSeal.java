package com.cssrc.ibms.core.webseal.pdf.sign;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.cssrc.ibms.api.report.model.IbmsSign;
import com.cssrc.ibms.core.webseal.pdf.KeyExecute;
import com.cssrc.ibms.core.webseal.pdf.KeyPosition;
import com.cssrc.ibms.core.webseal.pdf.PdfPositionEngine;
import com.cssrc.ibms.core.webseal.pdf.intf.AbstractIbmsSeal;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.FieldPosition;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @ClassName: PdfSeal
 * @Description: TODO(pdf文件图片签名)
 * @author zxg
 * @date 2016年12月5日 下午2:59:47
 * 
 */
public class PdfSeal extends AbstractIbmsSeal
{
    
    /**
     * @param infile 待签名文件绝对路径
     * @param outile
     * @param imageUrl
     * @throws Exception
     */
    public PdfSeal(String infile, String outile, KeyExecute keyExecute)
        throws Exception
    {
        super(infile, outile, keyExecute);
        this.reader = new PdfReader(this.inFile);
        this.ops = new FileOutputStream(this.outFile);
        this.stamper = new PdfStamper(reader, ops); // 生成的输出流
    }
    
    /**
     * @param bfile 待签名文件字节流
     * @param outile 输出文件
     * @param imageUrl 签名图片
     * @throws Exception
     */
    public PdfSeal(byte[] bfile, String outile, KeyExecute keyExecute)
        throws Exception
    {
        super(bfile, outile, keyExecute);
        this.reader = new PdfReader(this.bfile);
        this.ops = new FileOutputStream(this.outFile);
        this.stamper = new PdfStamper(reader, ops); // 生成的输出流
    }
    
    /**
     * 盖章
     * 
     * @return
     * @author zxg
     * @throws IOException
     */
    @Override
    public void seal(IbmsSign ibmsSign)
        throws Exception
    {
        try
        {
            int total = reader.getNumberOfPages() + 1;
            PdfContentByte content;
            PdfPositionEngine pdfPositionEngine = new PdfPositionEngine(this);
            for (int i = 1; i < total; i++)
            {
                this.setCreentPage(i);
                content = stamper.getOverContent(i);
                // stamper.addSignature("张新光",1,10,10,10,10);
                // 根据pdf 当前页，定位坐标
                List<KeyPosition> postions = pdfPositionEngine.getPosition();
                // 根据定位的坐标添加图片签章
                for (KeyPosition keyPosition : postions)
                {
                    // 插入图片
                    this.insertImage(content, keyPosition);
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
    
    /**
     * 创建Chunk
     * 
     * @return
     * @author zxg
     */
    public Chunk CreateChunk()
    {
        BaseFont bfChinese;
        Chunk chunk = null;
        try
        {
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            Font fontChinese = new Font(bfChinese, 10 * 4 / 3);
            chunk = new Chunk("ibms_seal", fontChinese);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return chunk;
    }
    
    /**
     * 插入图片
     * 
     * @param ps
     * @param s
     * @author zxg
     */
    public void insertImage(PdfContentByte under, KeyPosition postion)
    {
        try
        {
            // under.setColorFill(BaseColor.LIGHT_GRAY);
            Image image = creatImage(postion);
            // under.setColorFill(BaseColor.BLACK);
            under.addImage(image);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    private Image creatImage(KeyPosition postion)
    {
        Image image;
        try
        {
            image = Image.getInstance(postion.getImgPath());
            // image.setAlignment(Image.LEFT | Image.TEXTWRAP);
            // image.setBorder(Image.BOX);
            float x = postion.getX();
            float y = postion.getY();
            float height = postion.getImage_height();
            float width = postion.getImage_width();
            image.setAbsolutePosition(x, y);
            image.scaleToFit(width, height);
            return image;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
    
    /**
     * 插入文本
     * 
     * @return
     * @author zxg
     */
    public void insertText(AcroFields acroFields)
    {
        
        try
        {
            List<AcroFields.FieldPosition> list = acroFields.getFieldPositions("root");
            for (FieldPosition position : list)
            {
                Rectangle rect = position.position;
                PdfContentByte cb = stamper.getOverContent(1);
                PdfPTable table = new PdfPTable(1);
                float tatalWidth = rect.getRight() - rect.getLeft() - 1;
                table.setTotalWidth(tatalWidth);
                
                PdfPCell cell = new PdfPCell(new Phrase(CreateChunk()));
                cell.setFixedHeight(rect.getTop() - rect.getBottom() - 1);
                cell.setBorderWidth(0);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setLeading(0, (float)1.1);
                
                table.addCell(cell);
                table.writeSelectedRows(0, -1, rect.getLeft(), rect.getTop(), cb);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
}
