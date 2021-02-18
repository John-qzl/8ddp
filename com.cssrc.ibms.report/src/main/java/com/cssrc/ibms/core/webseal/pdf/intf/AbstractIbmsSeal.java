package com.cssrc.ibms.core.webseal.pdf.intf;

import java.io.FileOutputStream;

import com.cssrc.ibms.api.report.model.IbmsSign;
import com.cssrc.ibms.core.webseal.pdf.KeyExecute;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @ClassName: AbstractIbmsSeal
 * @Description: pdf签名抽象类
 * @author zxg
 * @date 2016年12月5日 下午3:04:31
 * 
 */
public abstract class AbstractIbmsSeal
{
    private int creentPage;
    
    protected KeyExecute keyExecute;
    
    protected String inFile;
    
    protected byte[] bfile;
    
    protected String outFile;
    
    protected PdfReader reader;
    
    protected PdfStamper stamper;
    
    protected FileOutputStream ops;
    
    protected int sealNumb;
    
    protected int proversion=6;
    
    public AbstractIbmsSeal()
    {
        
    }
    
    public AbstractIbmsSeal(String inFile, String outFile, KeyExecute keyExecute)
    {
        this.inFile = inFile;
        this.outFile = outFile;
        this.creentPage = 1;
    }
    
    public AbstractIbmsSeal(byte[] bfile, String outFile, KeyExecute keyExecute)
    {
        this.keyExecute = keyExecute;
        this.bfile = bfile;
        this.outFile = outFile;
        this.creentPage = 1;
    }
    
    public String getInFile()
    {
        return inFile;
    }
    
    public void setInFile(String inFile)
    {
        this.inFile = inFile;
    }
    
    public String getOutFile()
    {
        return outFile;
    }
    
    public void setOutFile(String outFile)
    {
        this.outFile = outFile;
    }
    
    public KeyExecute getKeyExecute()
    {
        return keyExecute;
    }
    
    public void setKeyExecute(KeyExecute keyExecute)
    {
        this.keyExecute = keyExecute;
    }
    
    public int getCreentPage()
    {
        return creentPage;
    }
    
    public void setCreentPage(int creentPage)
    {
        this.creentPage = creentPage;
    }
    
    public PdfReader getReader()
    {
        return reader;
    }
    
    public void setReader(PdfReader reader)
    {
        this.reader = reader;
    }
    
    public PdfStamper getStamper()
    {
        return stamper;
    }
    
    public void setStamper(PdfStamper stamper)
    {
        this.stamper = stamper;
    }
    
    public FileOutputStream getOps()
    {
        return ops;
    }
    
    public void setOps(FileOutputStream ops)
    {
        this.ops = ops;
    }
    
    public int getSealNumb()
    {
        return sealNumb;
    }

    public void setSealNumb(int sealNumb)
    {
        this.sealNumb = sealNumb;
    }

    public abstract void seal(IbmsSign ibmsSign)
        throws Exception;
}
